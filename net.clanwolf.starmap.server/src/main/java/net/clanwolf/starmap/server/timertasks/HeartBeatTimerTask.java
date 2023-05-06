/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : https://www.clanwolf.net                           |
 * GitHub      : https://github.com/ClanWolf                        |
 * ---------------------------------------------------------------- |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
 * C3 includes libraries and source code by various authors.        |
 * Copyright (c) 2001-2023, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.timertasks;

import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.beans.C3Room;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RoundDAO;
import net.clanwolf.starmap.server.persistence.pojos.RoundPOJO;
import net.clanwolf.starmap.server.process.EndRound;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import net.clanwolf.starmap.server.util.WebDataInterface;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.UniverseDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.util.Compressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * @author Undertaker
 * @author Meldric
 */
public class HeartBeatTimerTask extends TimerTask {
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static volatile long lastReportedHour = 0;
    private volatile boolean informClients = false;
    private String tempDir = "";
    private volatile boolean currentlyRunning = false;
    private volatile CountDownLatch latch;

    public HeartBeatTimerTask(boolean informClients, CountDownLatch latch) {
        this.informClients = informClients;
        String property = "java.io.tmpdir";
        tempDir = System.getProperty(property);
        this.latch = latch;
    }

    @Override
    public synchronized void run() {
        if (!currentlyRunning) {
            currentlyRunning = true;

            String heartBeatFileName;
            if (ServerNexus.isDevelopmentPC) {
                heartBeatFileName = tempDir + File.separator + "c3.heartbeat";
            } else {
                heartBeatFileName = "/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/c3.heartbeat";
            }

            Calendar calendar = Calendar.getInstance();
            java.util.Date now = calendar.getTime();
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

            try {

                File heartbeatfile = new File(heartBeatFileName);

                // Überprüfe, ob die Datei nicht existiert
                if (!heartbeatfile.exists()) {
                    // Wenn sie nicht existiert, erstelle sie
                    if (heartbeatfile.createNewFile()) {
						logger.info("heartbeat file created: " + heartbeatfile.getAbsolutePath());
                    } else {
						logger.error("The heartbeat file could not be created!");
                    }
                }

                BasicFileAttributes attr = Files.readAttributes(Paths.get(heartBeatFileName), BasicFileAttributes.class);

                FileTime t = attr.lastModifiedTime();
                Long fileTimeStamp = t.toMillis();
                Long systemTimeStamp = System.currentTimeMillis();
                long diff = systemTimeStamp - fileTimeStamp;

                if (diff > (1000 * 60)) { // every 60 seconds there is a heartbeat
                    try (BufferedWriter br = new BufferedWriter(new FileWriter(heartbeatfile))) {
                        logger.info("Writing heartbeat ping to " + heartBeatFileName);
                        br.write("" + currentTimestamp.getTime());
                    } catch (IOException ioe) {
                        logger.error("Exception while writing heartbeat file [001].", ioe);
                    }
                }
            } catch (IOException e) {
                logger.error("Exception while writing heartbeat file [002].");
                throw new RuntimeException(e);
            }

            Long seasonId = GameServer.getCurrentSeason();
            RoundDAO roundDAO = RoundDAO.getInstance();
            RoundPOJO r = roundDAO.findBySeasonId(seasonId);
            int round = r.getRound().intValue();

            String resultProtocol = EndRound.finalizeRound(seasonId, round);

            UniverseDTO universe = WebDataInterface.initUniverse();
            universe.lastRoundResultProtocol = resultProtocol;

            WebDataInterface.loadFactions();
            WebDataInterface.load_HH_StarSystemData();
            WebDataInterface.loadAttacks(seasonId);
            WebDataInterface.loadJumpshipsAndRoutePoints();

//			logger.info("Calling list creation methods...");
////			logger.info("Calling list creation (Factions)...");
//			WebDataInterface.createSystemList(SystemListTypes.Factions);
////			logger.info("Calling list creation (HH_StarSystems)...");
//			WebDataInterface.createSystemList(SystemListTypes.HH_StarSystems);
////			logger.info("Calling list creation (HH_Attacks)...");
//			WebDataInterface.createSystemList(SystemListTypes.HH_Attacks);
////			logger.info("Calling list creation (HH_Jumpships)...");
//			WebDataInterface.createSystemList(SystemListTypes.HH_Jumpships);
////			logger.info("Calling list creation (HH_Routepoints)...");
//			WebDataInterface.createSystemList(SystemListTypes.HH_Routepoints);
////			logger.info("Calling list creation (CM_StarSystems)...");
////			WebDataInterface.createSystemList(SystemListTypes.CM_StarSystems);

            if (informClients || ServerNexus.sendUniverseToClients) {
                // Broadcast new version of the universe to the clients
                logger.info("Send updated universe to all clients.");

                GameState response = new GameState(GAMESTATEMODES.GET_UNIVERSE_DATA);
                response.addObject(Compressor.compress(universe));
                C3Room.sendBroadcastMessage(response);

                if (ServerNexus.sendUniverseToClients) {
                    ServerNexus.sendUniverseToClients = false;
                }
            }
            currentlyRunning = false;
        }

        if (latch != null) {
            latch.countDown();
        }

        // log server uptime
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Date firstParsedDate = new Date(now.getTime());
        Date secondParsedDate = new Date(ServerNexus.serverStartTime.getTime());
        long diffmilliseconds = firstParsedDate.getTime() - secondParsedDate.getTime();
        long hours = diffmilliseconds / (1000 * 60 * 60);
        long minutes = (diffmilliseconds - hours * 1000 * 60 * 60) / (1000 * 60);
        long seconds = (diffmilliseconds - hours * 1000 * 60 * 60 - minutes * 1000 * 60) / 10000;
        long days = hours / 24;
        String uptime = String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
        logger.info(uptime + " (" + days + " days)");

        // Send uptime message to bots (irc, ts3 and discord), only once an hour
        if (hours > lastReportedHour + 23) {
            ServerNexus.getEci().sendExtCom("Server is up since " + hours + "hours (" + days + " days).", "en", true, true, true);
            ServerNexus.getEci().sendExtCom("Server ist online seit " + hours + " Stunden (" + days + " Tage).", "de", true, true, true);
            lastReportedHour = hours;
        }

        // Broadcast heartbeat to the clients
        logger.info("Send server heartbeat event to all clients (server is still up) (pong).");
        GameState heartbeatState = new GameState(GAMESTATEMODES.SERVER_HEARTBEAT);
        heartbeatState.addObject(uptime);
        C3Room.sendBroadcastMessage(heartbeatState);
    }
}
