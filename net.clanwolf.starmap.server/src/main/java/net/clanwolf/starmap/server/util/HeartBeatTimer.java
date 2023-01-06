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
package net.clanwolf.starmap.server.util;

import net.clanwolf.starmap.server.Nexus.Nexus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.beans.C3Room;
import net.clanwolf.starmap.server.enums.SystemListTypes;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RoundDAO;
import net.clanwolf.starmap.server.persistence.pojos.RoundPOJO;
import net.clanwolf.starmap.server.process.EndRound;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.util.Compressor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Calendar;
import java.util.TimerTask;

/**
 * @author Undertaker
 *
 */
public class HeartBeatTimer extends TimerTask {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private boolean informClients = false;
	private String tempDir = "";
	private boolean currentlyRunning = false;

	public HeartBeatTimer() {
		String property = "java.io.tmpdir";
		tempDir = System.getProperty(property);
	}

	public HeartBeatTimer(boolean informClients) {
		String property = "java.io.tmpdir";
		tempDir = System.getProperty(property);
		this.informClients = informClients;
	}

	@Override
	public synchronized void run() {
		if (!currentlyRunning) {
			currentlyRunning = true;

			//logger.info("Writing heartbeat ping to " + tempDir);
			logger.info("Writing heartbeat ping to " + "/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/c3.heartbeat");

			Calendar calendar = Calendar.getInstance();
			java.util.Date now = calendar.getTime();
			java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());

			File heartbeatfile = new File("/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/c3.heartbeat");
			try (BufferedWriter br = new BufferedWriter(new FileWriter(heartbeatfile))) {
				br.write("" + currentTimestamp.getTime());
			} catch (IOException ioe) {
				logger.error("Error writing heartbeat file", ioe);
			}

			Long seasonId = GameServer.getCurrentSeason();
			RoundDAO roundDAO = RoundDAO.getInstance();
			RoundPOJO r = roundDAO.findBySeasonId(seasonId);
			int round = r.getRound().intValue();

			String resultProtocol = EndRound.finalizeRound(seasonId, round);

			logger.info("Calling list creation methods...");
//			logger.info("Calling list creation (Factions)...");
			WebDataInterface.createSystemList(SystemListTypes.Factions);
//			logger.info("Calling list creation (HH_StarSystems)...");
			WebDataInterface.createSystemList(SystemListTypes.HH_StarSystems);
//			logger.info("Calling list creation (HH_Attacks)...");
			WebDataInterface.createSystemList(SystemListTypes.HH_Attacks);
//			logger.info("Calling list creation (HH_Jumpships)...");
			WebDataInterface.createSystemList(SystemListTypes.HH_Jumpships);
//			logger.info("Calling list creation (HH_Routepoints)...");
			WebDataInterface.createSystemList(SystemListTypes.HH_Routepoints);
//			logger.info("Calling list creation (CM_StarSystems)...");
			WebDataInterface.createSystemList(SystemListTypes.CM_StarSystems);

			if (!"".equals(resultProtocol)) {
				WebDataInterface.getUniverse().lastRoundResultProtocol = resultProtocol;
			}

			if (informClients || Nexus.sendUniverseToClients == true) {
				// Broadcast new version of the universe to the clients
				logger.info("Send updated universe to all clients.");
				GameState response = new GameState(GAMESTATEMODES.GET_UNIVERSE_DATA);
				response.addObject(Compressor.compress(WebDataInterface.getUniverse()));
				C3Room.sendBroadcastMessage(response);

				if (Nexus.sendUniverseToClients) {
					Nexus.sendUniverseToClients = false;
				}
			}

			currentlyRunning = false;
		}

		// Broadcast heartbeat to the clients
		logger.info("Send server heartbeat event to all clients (server is still up) (pong).");
		GameState heartbeat = new GameState(GAMESTATEMODES.SERVER_HEARTBEAT);
		C3Room.sendBroadcastMessage(heartbeat);
	}
}
