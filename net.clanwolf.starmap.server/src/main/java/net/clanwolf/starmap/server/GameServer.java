/* ---------------------------------------------------------------- |
 * W-7 Research Group / C3                                          |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *          W-7 Facility / Research, Software Development           |
 *                    Tranquil (Mobile Division)                    |
 * __        __  _____   ____                               _       |
 * \ \      / / |___  | |  _ \ ___  ___  ___  __ _ _ __ ___| |__    |
 *  \ \ /\ / /____ / /  | |_) / _ \/ __|/ _ \/ _` | '__/ __| '_ \   |
 *   \ V  V /_____/ /   |  _ <  __/\__ \  __/ (_| | | | (__| | | |  |
 *    \_/\_/     /_/    |_| \_\___||___/\___|\__,_|_|  \___|_| |_|  |
 *                                                                  |
 *  W-7 is the production facility of Clan Wolf. The home base is   |
 *  Tranquil, but there are several mobile departments. In the      |
 *  development department there is a small group of developers and |
 *  designers busy to field new software products for battlefield   |
 *  commanders as well as for strategic dimensions of the clans     |
 *  operations. The department is led by a experienced StarColonel  |
 *  who fell out of active duty due to a wound he suffered during   |
 *  the battle on Tukkayid. His name and dossier are classified,    |
 *  get in contact through regular chain of command.                |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MkIII "Damien"                   |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 *  One of the products used to control the production and the      |
 *  transport of frontline troops is C3. C3 stands for              |
 *  "Communication - Command - Control".                            |
 *  Because there is a field based system to control the            |
 *  communication and data transfer of Mechs, this system is often  |
 *  refered to as "Big C3", however, the official name is           |
 *  "W-7 C3 / MkIII 'Damien'".                                      |
 *                                                                  |
 *  Licensing through W-7 Facility Central Office, Tranquil.        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  Info        : http://www.clanwolf.net                           |
 *  Forum       : http://www.clanwolf.net                           |
 *  Web         : http://c3.clanwolf.net                            |
 *  GitHub      : https://github.com/ClanWolf/C3-Java_Client        |
 *                                                                  |
 *  IRC         : starmap.clanwolf.net @ Quakenet                        |
 *                                                                  |
 *  Devs        : - Christian (Meldric)                    [active] |
 *                - Werner (Undertaker)                    [active] |
 *                - Thomas (xfirestorm)                    [active] |
 *                - Domenico (Nonnex)                     [retired] |
 *                - Dirk (kotzbroken2)                    [retired] |
 *                                                                  |
 *                  (see Wolfnet for up-to-date information)        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  C3 includes libraries and source code by various authors,       |
 *  for credits and info, see README.                               |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 * Copyright 2016 ClanWolf.net                                      |
 *                                                                  |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 *                                                                  |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server;

import io.nadron.server.ServerManager;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.util.HeartBeatTimer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.File;
import java.util.Timer;
import java.util.logging.Level;

public class GameServer {
	private static AbstractApplicationContext ctx;

	public static void main(String[] args) {
		// Logging
		File dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3");
		boolean res = dir.mkdirs();
		if (res || dir.exists()) {
			String logFileName = dir + File.separator + "server.log";

			C3Logger.setC3Logfile(logFileName);
			C3Logger.setC3LogLevel(Level.FINEST);
		}
		// PropertyConfigurator.configure(System.getProperty("log4j.configuration"));
		ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
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

	public static void startGames(AbstractApplicationContext ctx) {
		// EntityManagerHelper.getEntityManager();
		// Log.print("EntityManager initialized");
		C3Logger.print("Server ready");

		// write heartbeat file every 5 minutes
		Timer serverHeartBeat = new Timer();
		serverHeartBeat.schedule(new HeartBeatTimer(), 1000, 1000 * 60 * 5);

		// World world = ctx.getBean(World.class);
		// GameRoom room1 = (GameRoom)ctx.getBean("Zombie_ROOM_1");
		// GameRoom room2 = (GameRoom)ctx.getBean("Zombie_ROOM_2");
		// Task monitor1 = new WorldMonitor(world,room1);
		// Task monitor2 = new WorldMonitor(world,room2);
		// TaskManagerService taskManager = ctx.getBean(TaskManagerService.class);
		// taskManager.scheduleWithFixedDelay(monitor1, 1000, 5000, TimeUnit.MILLISECONDS);
		// taskManager.scheduleWithFixedDelay(monitor2, 2000, 5000, TimeUnit.MILLISECONDS);
	}

}
