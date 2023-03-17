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
package net.clanwolf.starmap.server;

import io.nadron.server.ServerManager;
import jakarta.persistence.EntityTransaction;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.mail.MailManager;
import net.clanwolf.starmap.logging.C3LogUtil;
import net.clanwolf.starmap.server.nexus2.Nexus;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.C3GameConfigDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackPOJO;
import net.clanwolf.starmap.server.persistence.pojos.C3GameConfigPOJO;
import net.clanwolf.starmap.server.timertasks.DropLeadCheckTimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.timertasks.HeartBeatTimerTask;
import net.clanwolf.starmap.server.timertasks.CheckShutdownFlagTimerTask;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static net.clanwolf.starmap.constants.Constants.*;

public class GameServer {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static AbstractApplicationContext ctx;
	private static String serverBaseDir;
	public static boolean isDevelopmentPC;

	private static Long currentSeason = 1L;

	// This needs to be done in the main class once at startup to set the file handler for the logger
	public static void prepareLogging() {
		File dir;
		if(isDevelopmentPC) {
			dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3");
			logger.error("Server directory was set manually to: " + dir + " (Development PC is set to true)");
		} else {
			try {
				dir = new File(GameServer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
				logger.error("Found server directory programmatically at: " + dir.getAbsolutePath());
			} catch (URISyntaxException e) {
				logger.error("Server directory could not be found programmatically. Check this!");
				e.printStackTrace();
				dir = new File("/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server");
				logger.error("Server directory was set manually to: " + dir);
			}
		}
		serverBaseDir = dir.getAbsolutePath();

		boolean res = dir.mkdirs();
		if (res || dir.exists()) {
			String logFileName = dir + File.separator + "log" + File.separator + "C3-Server.log";
			C3LogUtil.loadConfigurationAndSetLogFile(logFileName);
		}
	}

	public static Long getCurrentSeason() {
		return currentSeason;
	}

	public static void setCurrentSeason(Long s) {
		currentSeason = s;
	}

	public static void checkDBConst(){
		ArrayList<C3GameConfigPOJO> c3GameConfigPOJO = C3GameConfigDAO.getInstance().getAllGameConfigValues();
		Map<String,Long> constantValues = new HashMap<>();

		//XP rewards during an invasion
		constantValues.put("C3_" + "XP_REWARD_VICTORY",  XP_REWARD_VICTORY);
		constantValues.put("C3_" + "XP_REWARD_LOSS", XP_REWARD_LOSS);
		constantValues.put("C3_" + "XP_REWARD_TIE",XP_REWARD_TIE);
		constantValues.put("C3_" + "XP_REWARD_COMPONENT_DESTROYED",XP_REWARD_COMPONENT_DESTROYED);
		constantValues.put("C3_" + "XP_REWARD_EACH_MATCH_SCORE",XP_REWARD_EACH_MATCH_SCORE);
		constantValues.put("C3_" + "XP_REWARD_EACH_MATCH_SCORE_RANGE",XP_REWARD_EACH_MATCH_SCORE_RANGE);
		constantValues.put("C3_" + "XP_REWARD_EACH_DAMAGE",XP_REWARD_EACH_DAMAGE);
		constantValues.put("C3_" + "XP_REWARD_EACH_DAMAGE_RANGE",XP_REWARD_EACH_DAMAGE_RANGE);
		constantValues.put("C3_" + "XP_REWARD_EACH_SURVIVAL_PERCENTAGE",XP_REWARD_EACH_SURVIVAL_PERCENTAGE);
		constantValues.put("C3_" + "XP_REWARD_EACH_SURVIVAL_PERCENTAGE_RANGE",XP_REWARD_EACH_SURVIVAL_PERCENTAGE_RANGE);
		constantValues.put("C3_" + "XP_REWARD_EACH_TEAM_DAMAGE",XP_REWARD_EACH_TEAM_DAMAGE);
		constantValues.put("C3_" + "XP_REWARD_EACH_TEAM_DAMAGE_RANGE",XP_REWARD_EACH_TEAM_DAMAGE_RANGE);

		//Reward payments when calculating the cost
		constantValues.put("C3_" + "REWARD_VICTORY",REWARD_VICTORY);
		constantValues.put("C3_" + "REWARD_LOSS",REWARD_LOSS);
		constantValues.put("C3_" + "REWARD_TIE",REWARD_TIE);
		constantValues.put("C3_" + "REWARD_ASSIST",REWARD_ASSIST);
		constantValues.put("C3_" + "REWARD_EACH_COMPONENT_DESTROYED",REWARD_EACH_COMPONENT_DESTROYED);
		constantValues.put("C3_" + "REWARD_EACH_MACHT_SCORE",REWARD_EACH_MACHT_SCORE);
		constantValues.put("C3_" + "REWARD_EACH_DAMAGE",REWARD_EACH_DAMAGE);
		constantValues.put("C3_" + "REWARD_EACH_TEAM_DAMAGE",REWARD_EACH_TEAM_DAMAGE);
		constantValues.put("C3_" + "REWARD_NO_TEAM_DAMAGE",REWARD_NO_TEAM_DAMAGE);
		constantValues.put("C3_" + "REWARD_EACH_KILL",REWARD_EACH_KILL);

		//Suche nach der Konstanze in der DB.
		//Wird eine Konstanze gefunden, wird diese aus der constantValues entfernt.
		for (Map.Entry<String, Long> entry : constantValues.entrySet()) {
			String constantName = entry.getKey();
			for (C3GameConfigPOJO config : c3GameConfigPOJO) {
				if (config.getKey().equals(constantName)) {
					constantValues.remove(entry.getKey(),entry.getValue());
					break;
				}
			}
		}

		EntityTransaction transaction = EntityManagerHelper.getEntityManager(Nexus.END_ROUND_USERID).getTransaction();
		transaction.begin();

		//Fehlende Konstanzen werden in der DB eingetragen.
		for (Map.Entry<String, Long> entry : constantValues.entrySet()) {
			C3GameConfigPOJO addC3ConfigPOJO = new C3GameConfigPOJO();
			addC3ConfigPOJO.setKey(entry.getKey());
			addC3ConfigPOJO.setValue(entry.getValue());
			C3GameConfigDAO.getInstance().update(Nexus.END_ROUND_USERID,addC3ConfigPOJO);
		}

		transaction.commit();
	}
	public static void main(String[] args) {
		Properties props = System.getProperties();
		props.setProperty("org.jboss.logging.provider", "slf4j");

		if(args.length > 0) {
			for (String a : args) {
				isDevelopmentPC = a.equals("IDE");
				Nexus.isDevelopmentPC = a.equals("IDE");;
				if (a.toLowerCase().startsWith("season=")) {
					try {
						// Season is defaulting to 1, can be set by a parameter
						String[] v = a.split("=");
						Long s = Long.valueOf(v[1]);
						setCurrentSeason(s);
					} catch(NumberFormatException e) {
						logger.info("Parameter for Season could not be parsed to a number! Defaulting to 1.");
						System.out.println("Parameter for Season could not be parsed to a number! Defaulting to 1.");
					}
				}
			}
		}

		// Logging
		prepareLogging();

		if(isDevelopmentPC) {
			ctx = new AnnotationConfigApplicationContext(SpringConfigIDE.class);
		} else {
			ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
		}
		// For the destroy method to work.
		ctx.registerShutdownHook();

		// Start the main game server
		ServerManager serverManager = ctx.getBean(ServerManager.class);

		try {
			serverManager.startServers();

			// serverManager.startServers(18090,843,8081);
		} catch (Exception e) {
			logger.error("Unable to start servers cleanly.", e);
		}
		logger.info("Started servers");
		startGames(ctx);
		checkDBConst();
	Nexus.serverStartTime = new Timestamp(System.currentTimeMillis());
	}

	public static AbstractApplicationContext getApplicationContext() {
		return ctx;
	}

	public static void startGames(AbstractApplicationContext ctx) {
		try {
			// EntityManagerHelper.getEntityManager();
			// Log.print("EntityManager initialized");

			String jarPath = GameServer.class
					.getProtectionDomain()
					.getCodeSource()
					.getLocation()
					.toURI()
					.getPath();
			String jarName = jarPath.substring(jarPath.lastIndexOf("/") + 1);
			Nexus.jarName = jarName;

			if( !isDevelopmentPC) {
				logger.info("Sending info mail.");
				String[] receivers = { "keshik@googlegroups.com" };
				boolean sent = false;
				sent = MailManager.sendMail("c3@clanwolf.net", receivers, "C3 Server is up again", "C3 Server started.", false);
				if (sent) {
					// sent
					logger.info("Mail sent. [3]");
				} else {
					// error during email sending
					logger.info("Error during mail dispatch. [3]");
				}
			}

			// run regular checks if attacks missing dropleads
			Timer checkOpenAttacksForDropleadsTimer = new Timer();
			DropLeadCheckTimerTask dropLeadCheck = new DropLeadCheckTimerTask();
			checkOpenAttacksForDropleadsTimer.schedule(dropLeadCheck, 15000, 20000);

			// write heartbeat file every some minutes
			Timer serverHeartBeat = new Timer();
			serverHeartBeat.schedule(new HeartBeatTimerTask(true, null), 1000, 1000 * 60 * 3);

			// check shutdown flagfile every some seconds
			Timer checkShutdownFlag = new Timer();
			checkShutdownFlag.schedule(new CheckShutdownFlagTimerTask(serverBaseDir), 1000, 1000 * 5);

			logger.info(jarName + " is up and ready");
			Nexus.getEci().sendExtCom(jarName + " is up and ready");

			// World world = ctx.getBean(World.class);
			// GameRoom room1 = (GameRoom)ctx.getBean("Zombie_ROOM_1");
			// GameRoom room2 = (GameRoom)ctx.getBean("Zombie_ROOM_2");
			// Task monitor1 = new WorldMonitor(world,room1);
			// Task monitor2 = new WorldMonitor(world,room2);
			// TaskManagerService taskManager = ctx.getBean(TaskManagerService.class);
			// taskManager.scheduleWithFixedDelay(monitor1, 1000, 5000, TimeUnit.MILLISECONDS);
			// taskManager.scheduleWithFixedDelay(monitor2, 2000, 5000, TimeUnit.MILLISECONDS);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
