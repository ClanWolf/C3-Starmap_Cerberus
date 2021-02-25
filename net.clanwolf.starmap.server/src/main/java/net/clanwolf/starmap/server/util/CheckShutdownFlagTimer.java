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

import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.enums.SystemListTypes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimerTask;

/**
 * @author Meldric
 *
 */
public class CheckShutdownFlagTimer extends TimerTask {

	private String dir = "";

	public CheckShutdownFlagTimer(String path) {
		this.dir = path;
	}

	@Override
	public void run() {
		File shutdownFlagFile = new File(dir + File.separator + "shutdown.flag");
    if (shutdownFlagFile.isFile()) {
      // On the server, a script checks if the server is running every couple of minutes.
      // If this methods shuts the server down, it will be going up by the script shortly after.
      // This is used in case a new version of the jar file was uploaded.
      // System.exit();
    }
	}
}
