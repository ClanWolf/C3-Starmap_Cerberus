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
package net.clanwolf.util;

import net.clanwolf.client.mail.MailManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.TimerTask;

/**
 * @author Meldric
 */
public class CheckShutdownFlagTimerTask extends TimerTask {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private String dir = "";
	private String botName = "";

	public CheckShutdownFlagTimerTask(String path, String botName) {
		this.dir = path;
		this.botName = botName;
		// Cleanup the flags once before the timer starts in case there were flags left from a previous time
		cleanupFlagFiles(botName);
	}

	private void cleanupFlagFiles(String botname) {
		File shutdownFlagFile = new File(dir + File.separator + "C3-" + botname + "_shutdown.flag");
		if (shutdownFlagFile.isFile()) {
			boolean deleted = shutdownFlagFile.delete();
		}
	}

	@Override
	public void run() {
		File shutdownFlagFile = new File(dir + File.separator + "C3-" + botName + "_shutdown.flag");
		if (shutdownFlagFile.exists() && shutdownFlagFile.isFile() && shutdownFlagFile.canRead()) {
			// On the server, a script checks if the server is running every couple of minutes.
			// If this methods shuts the server down, it will be going up by the script shortly after.
			// This is used in case a new version of the jar file was uploaded.
//			logger.info("Cleaning up flag files.");
//			cleanupFlagFiles();

//			logger.info("Sending info mail.");
			String[] receivers = { "keshik@googlegroups.com" };
			boolean sent = false;
			sent = MailManager.sendMail("c3@clanwolf.net", receivers, botName + " goes down after flag request", botName + " is shutting down...", false);
			if (sent) {
				// sent
				logger.info("Mail sent. [1]");
			} else {
				// error during email sending
				logger.info("Error during mail dispatch. [1]");
			}

			System.exit(5);
		}
	}
}
