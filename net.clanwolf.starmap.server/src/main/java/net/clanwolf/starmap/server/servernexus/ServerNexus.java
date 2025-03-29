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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.servernexus;

import net.clanwolf.starmap.server.beans.C3GameSessionHandler;
import net.clanwolf.starmap.server.util.ExternalCommunicationInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Properties;

public class ServerNexus {
	public static Long currentSeason = 1L;
	public static Long DUMMY_USERID = -1L;
	public static Long END_ROUND_USERID = -2L;
	public static volatile boolean sendUniverseToClients = false;
	public static String patternTimestamp = "yyyy-MM-dd HH:mm:ss";
	public static String mailServer = "";
	public static String mailUser = "";
	public static String mailPw = "";
	private static final ExternalCommunicationInterface eci = new ExternalCommunicationInterface();
	public static String jarName = "";
	public static C3GameSessionHandler gmSessionHandler;
	public static boolean isDevelopmentPC = false;
	public static Timestamp serverStartTime;
	public static HashMap<Long, Long> brokenAttackTimers = new HashMap<>();
	public static String serverBaseDir = "";
	public static volatile boolean heartbeatCurrentlyRunning = false;
	public static volatile boolean executingCommand = false;

	public static ExternalCommunicationInterface getEci() {
		return eci;
	}

	public static String getServerBaseDir() {
		return serverBaseDir;
	}

	public static void setServerBaseDir(String serverBaseDir) {
		ServerNexus.serverBaseDir = serverBaseDir;
	}

	public static String authProperties(String key){
		final Properties auth = new Properties();
		try {
			final String authFileName = "auth.properties";
			InputStream inputStream = ServerNexus.class.getClassLoader().getResourceAsStream(authFileName);
			if (inputStream != null) {
				auth.load(inputStream);
				return auth.getProperty(key);
			} else {
				throw new FileNotFoundException("Auth-Property file '" + authFileName + "' not found in classpath.");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return null;
	}
}
