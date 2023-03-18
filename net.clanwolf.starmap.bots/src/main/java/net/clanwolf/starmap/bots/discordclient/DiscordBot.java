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
package net.clanwolf.starmap.bots.discordclient;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import net.clanwolf.starmap.bots.db.DBConnection;
import net.clanwolf.starmap.bots.ts3client.TS3Bot;
import net.clanwolf.starmap.bots.util.CheckShutdownFlagTimerTask;
import net.clanwolf.starmap.logging.C3LogUtil;
import net.clanwolf.starmap.mail.MailManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;

public class DiscordBot {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String serverBaseDir = new File("/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server").getAbsolutePath();

	public DiscordBot() {
		prepareLogging();

		Timer checkShutdownFlag = new Timer();
		checkShutdownFlag.schedule(new CheckShutdownFlagTimerTask(serverBaseDir, "DiscordBot"), 1000, 1000 * 10);

		final Properties auth = new Properties();
		try {
			final String authFileName = "auth.properties";
			InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream(authFileName);
			if (inputStream != null) {
				auth.load(inputStream);
			} else {
				throw new FileNotFoundException("Auth-Property file '" + authFileName + "' not found in classpath.");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void prepareLogging() {
		String logFileName = serverBaseDir + File.separator + "log" + File.separator + "DiscordBot.log";
		C3LogUtil.loadConfigurationAndSetLogFile(logFileName);
	}

	public static void main(String[] args) {
		DiscordBot discordbot = new DiscordBot();
	}
}
