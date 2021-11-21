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
 * Copyright (c) 2001-2021, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server;

import io.nadron.server.ServerManager;
import net.clanwolf.client.mail.MailManager;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.logging.LogLevel;
import net.clanwolf.starmap.server.util.HeartBeatTimer;
import net.clanwolf.starmap.server.util.CheckShutdownFlagTimer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.File;
import java.util.Timer;
import java.util.logging.Level;

public class GameServer {
	private static AbstractApplicationContext ctx;
	private static String serverBaseDir;
	public static boolean isDevelopmentPC;

	private static Long currentSeason = 1L;
	public static Long getCurrentSeason() {
		return currentSeason;
	}
	public static void setCurrentSeason(Long s) {
		currentSeason = s;
	}

	public static void main(String[] args) {
		// Logging
		File dir;
		if(args.length > 0) {
			for (String a : args) {
				isDevelopmentPC = a.equals("IDE");
				if (a.toLowerCase().startsWith("season=")) {
					try {
						// Season is defaulting to 1, can be set by a parameter
						String[] v = a.split("=");
						Long s = Long.valueOf(v[1]);
						setCurrentSeason(s);
					} catch(NumberFormatException e) {
						C3Logger.info("Parameter for Season could not be parsed to a number! Defaulting to 1.");
					}
				}
			}
		}

		if(isDevelopmentPC) {
			dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3");
		} else {
			dir = new File("/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server");

			// TODO: Find location of the jar file programmatically
			// TODO: Use this to get the servers home dir:
			//File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
		}
		serverBaseDir = dir.getAbsolutePath();

		boolean res = dir.mkdirs();
		if (res || dir.exists()) {
			String logFileName = dir + File.separator + "log" + File.separator + "C3-Server.log";

			C3Logger.setC3Logfile(logFileName);
			C3Logger.setC3LogLevel(Level.FINEST);
		}

		if(isDevelopmentPC) {
			ctx = new AnnotationConfigApplicationContext(SpringConfigIDE.class);
		} else {
			ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
		}
		// For the destroy method to work.
		ctx.registerShutdownHook();

		// Start the main game server
		ServerManager serverManager = ctx.getBean(ServerManager.class);

		LogLevel.setSQLLevelOff();

		try {
			serverManager.startServers();
			// serverManager.startServers(18090,843,8081);
		} catch (Exception e) {
			C3Logger.exception("Unable to start servers cleanly.", e);
		}
		C3Logger.print("Started servers");
		startGames(ctx);
	}

	public static AbstractApplicationContext getApplicationContext() {
		return ctx;
	}

	public static void startGames(AbstractApplicationContext ctx) {
		try {
			// EntityManagerHelper.getEntityManager();
			// Log.print("EntityManager initialized");
			C3Logger.print("Server ready");

			if( !isDevelopmentPC) {
				C3Logger.info("Sending info mail.");
				String[] receivers = { "keshik@googlegroups.com" };
				boolean sent = false;
				sent = MailManager.sendMail("c3@clanwolf.net", receivers, "C3 Server is up again", "C3 Server started.", false);
				if (sent) {
					// sent
					C3Logger.info("Mail sent.");
				} else {
					// error during email sending
					C3Logger.info("Error during mail dispatch.");
				}
			}

			// write heartbeat file every some minutes
			Timer serverHeartBeat = new Timer();
			serverHeartBeat.schedule(new HeartBeatTimer(), 1000, 1000 * 60 * 5);

			// check shutdown flagfile every some seconds
			Timer checkShutdownFlag = new Timer();
			checkShutdownFlag.schedule(new CheckShutdownFlagTimer(serverBaseDir), 1000, 1000 * 10);

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
