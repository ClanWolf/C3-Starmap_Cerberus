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
 * Copyright (c) 2001-2022, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.logging;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.logging.FileHandler;

public class C3LogUtil {

	public static void loadConfigurationAndSetLogFile(String logFileName) {
		try {
			int FILE_SIZE_LIMIT = 4 * 1024 * 1024;
			java.util.logging.LogManager.getLogManager().readConfiguration(MethodHandles.lookup().lookupClass().getClassLoader().getResourceAsStream("logging.properties"));
			if (logFileName != null) {
				FileHandler fileHandler = new FileHandler(logFileName, FILE_SIZE_LIMIT, 3, false);
				fileHandler.setEncoding("UTF-8");
				//fileHandler.setFormatter(new SocotoLogFormatter());

				java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger("");
				julLogger.addHandler(fileHandler);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}