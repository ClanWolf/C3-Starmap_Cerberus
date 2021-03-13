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
package net.clanwolf.starmap.server.util;

import net.clanwolf.client.mail.MailManager;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.GameServer;

import java.io.File;
import java.util.TimerTask;

/**
 * @author Meldric
 */
public class CheckShutdownFlagTimer extends TimerTask {

	private String dir = "";

	private void cleanupFlagFiles() {
		File shutdownFlagFile = new File(dir + File.separator + "C3-Server_shutdown.flag");
		if (shutdownFlagFile.isFile()) {
			boolean deleted = shutdownFlagFile.delete();
		}
	}

	public CheckShutdownFlagTimer(String path) {
		this.dir = path;
		// Cleanup the flags once before the timer starts in case there were flags left from a previous time
		cleanupFlagFiles();
	}

	@Override
	public void run() {
		File shutdownFlagFile = new File(dir + File.separator + "C3-Server_shutdown.flag");
		if (shutdownFlagFile.exists() && shutdownFlagFile.isFile() && shutdownFlagFile.canRead()) {
			// On the server, a script checks if the server is running every couple of minutes.
			// If this methods shuts the server down, it will be going up by the script shortly after.
			// This is used in case a new version of the jar file was uploaded.
//			C3Logger.info("Cleaning up flag files.");
//			cleanupFlagFiles();

			if(!GameServer.isDevelopmentPC) {
				C3Logger.info("Sending info mail.");
				String[] receivers = { "keshik@googlegroups.com" };
				boolean sent = false;
				sent = MailManager.sendMail("c3@clanwolf.net", receivers, "C3 Server goes down after flag request", "C3 Server is shutting down...", false);
				if (sent) {
					// sent
					C3Logger.info("Mail sent.");
				} else {
					// error during email sending
					C3Logger.info("Error during mail dispatch.");
				}
			}
			C3Logger.info("Exiting server.");
			System.exit(5);
		}
	}
}
