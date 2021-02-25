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
import net.clanwolf.starmap.logging.C3Logger;
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

	public static void main(String[] args) {
		// Logging
		File dir;
		if(args.length > 0 && args[0].equals("IDE")) {
			dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3");
		} else {
			dir = new File("/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server");
			
			// TODO: Use this to get the servers home dir:
			File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
		}
		serverBaseDir = dir;

		boolean res = dir.mkdirs();
		if (res || dir.exists()) {
			String logFileName = dir + File.separator + "C3_Server.log";

			C3Logger.setC3Logfile(logFileName);
			C3Logger.setC3LogLevel(Level.FINEST);
		}

		if(args.length > 0 && args[0].equals("IDE")) {
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
			C3Logger.exception("Unable to start servers cleanly.", e);
		}
		C3Logger.print("Started servers");
		startGames(ctx);
	}

	public static AbstractApplicationContext getApplicationContext() {
		return ctx;
	}

	private static void cleanupFlagFiles() {
		File shutdownFlagFile = new File(dir + File.separator + "shutdown.flag");
    		if (shutdownFlagFile.isFile()) {
			boolean deleted = shutdownFlagFile.delete();
		}
	}

	public static void startGames(AbstractApplicationContext ctx) {
		try {
			// EntityManagerHelper.getEntityManager();
			// Log.print("EntityManager initialized");
			C3Logger.print("Server ready");

			cleanupFlagFiles();

			// write heartbeat file every 5 minutes
			Timer serverHeartBeat = new Timer();
			serverHeartBeat.schedule(new HeartBeatTimer(), 1000, 1000 * 60 * 5);

			// check shutdown flagfile every 30 seconds
			Timer checkShutdownFlag = new Timer();
			checkShutdownFlag.schedule(new CheckShutdownFlagTimer(serverBaseDir), 1000, 1000 * 30);

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
