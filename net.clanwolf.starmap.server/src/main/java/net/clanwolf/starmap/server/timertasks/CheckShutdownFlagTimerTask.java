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

import net.clanwolf.starmap.mail.MailManager;
import net.clanwolf.starmap.server.beans.C3GameSessionHandler;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.GameServer;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.TimerTask;

/**
 * @author Meldric
 */
public class CheckShutdownFlagTimerTask extends TimerTask {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private String dir = "";

	private void cleanupFlagFiles() {
		File shutdownFlagFile = new File(dir + File.separator + "C3-Server_shutdown.flag");
		File restartFlagFile = new File(dir + File.separator + "C3-Server_restart.flag");

		if (shutdownFlagFile.isFile()) {
			boolean deletedShutdownFlagFile = shutdownFlagFile.delete();
		}
		if (restartFlagFile.isFile()) {
			boolean deletedRestartFlagFile = restartFlagFile.delete();
		}
	}

	public CheckShutdownFlagTimerTask(String path) {
		this.dir = path;
		// Cleanup the flags once before the timer starts in case there were flags left from a previous time
		cleanupFlagFiles();
	}

	@Override
	public void run() {
		File shutdownFlagFile = new File(dir + File.separator + "C3-Server_shutdown.flag");
		File restartFlagFile = new File(dir + File.separator + "C3-Server_restart.flag");

		boolean shutdownFlagFileFound = shutdownFlagFile.exists() && shutdownFlagFile.isFile() && shutdownFlagFile.canRead();
		boolean restartFlagFileFound = restartFlagFile.exists() && restartFlagFile.isFile() && restartFlagFile.canRead();

		if (shutdownFlagFileFound || restartFlagFileFound) {
			// On the server, a script checks if the server is running every couple of minutes.
			// If this methods shuts the server down, it will be going up by the script shortly after.
			// This is used in case a new version of the jar file was uploaded.
			if (restartFlagFileFound) {
				logger.info("Found restart flag, cleaning up flag files.");
				cleanupFlagFiles();
			}
			if (shutdownFlagFileFound) {
				logger.info("Found shutdown flag, leaving flag files alone until new version has been uploaded.");
			}

			if(!GameServer.isDevelopmentPC) {
				ServerNexus.getEci().sendExtCom("Server is going down.", "en",true, true, true);
				ServerNexus.getEci().sendExtCom("Server fährt herunter.", "de",true, true, true);

				logger.info("Sending info mail.");
				String[] receivers = { "keshik@googlegroups.com" };
				boolean sent = false;
				sent = MailManager.sendMail("c3@clanwolf.net", receivers, "C3 Server goes down", "C3 Server is shutting down...", false);
				if (sent) {
					// sent
					logger.info("Mail sent. [6]");
				} else {
					// error during email sending
					logger.info("Error during mail dispatch. [6]");
				}
			}

			// Informing clients about the shutdown
			GameState gameStateShutdownMessage = new GameState(GAMESTATEMODES.SERVER_GOES_DOWN);
			gameStateShutdownMessage.addObject(null);
			gameStateShutdownMessage.setAction_successfully(Boolean.TRUE);
			C3GameSessionHandler.sendBroadCast(gameStateShutdownMessage);

			logger.info("Exiting server.");
			System.exit(5);
		}
	}
}
