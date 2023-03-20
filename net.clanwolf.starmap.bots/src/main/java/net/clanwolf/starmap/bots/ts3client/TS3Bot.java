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
package net.clanwolf.starmap.bots.ts3client;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import net.clanwolf.starmap.bots.db.DBConnection;
import net.clanwolf.starmap.mail.MailManager;
import net.clanwolf.starmap.logging.C3LogUtil;
import net.clanwolf.starmap.bots.util.CheckShutdownFlagTimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.*;

public class TS3Bot {
	//private static String serverBaseDir = new File("c:\\temp").getAbsolutePath();
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final Random RANDOM = new Random();
	private static final String serverBaseDir = new File("/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server").getAbsolutePath();
	private static TS3Api api = null;
	final TS3Query query;

	public TS3Bot() {
		prepareLogging();

		Timer checkShutdownFlag = new Timer();
		checkShutdownFlag.schedule(new CheckShutdownFlagTimerTask(serverBaseDir, "TS3Bot"), 1000, 1000 * 10);

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

//		logger.info("!!! Enable loggin in CWTS3Bot, L.81 !!!");
		final TS3Config config = new TS3Config();
		config.setHost(auth.getProperty("ts3botip"));
		config.setEnableCommunicationsLogging(true);

		query = new TS3Query(config);
		query.connect();

		api = query.getApi();
		api.login(auth.getProperty("ts3botuser"), auth.getProperty("ts3botpass"));
		api.selectVirtualServerById(3);
		api.setNickname("Ulric");
		api.sendServerMessage("Ulric online!");

		api.registerEvent(TS3EventType.SERVER);
		api.addTS3Listeners(new TS3EventAdapter() {
			@Override
			public void onClientJoin(ClientJoinEvent e) {
				final int clientId = e.getClientId();
				int clientDatabaseId = 0;

				List<Client> clients = api.getClients();
				for (Client client : clients) {
					if ((client.getId() == clientId) && !client.isServerQueryClient()) {
						clientDatabaseId = client.getDatabaseId();
					}
				}

				if (clientDatabaseId != 0) {
					boolean onlyGuest = true;
					List<ServerGroup> serverGroups = api.getServerGroupsByClientId(clientDatabaseId);
					for (ServerGroup serverGroup : serverGroups) {
						if (!"Gast".equalsIgnoreCase(serverGroup.getName())) {
							onlyGuest = false;
						}
					}

					String privateMessage;
					if (onlyGuest) {
						privateMessage = "";
						privateMessage = privateMessage + "\n\n-----------------------------------------------------------------\n";
						privateMessage = privateMessage + "Willkommen in den Hallen der Wölfe!\n";
						privateMessage = privateMessage + "Schön, dass Du den Weg hierher gefunden hast!\n";
						privateMessage = privateMessage + "Mein Name ist Ulric.\n";
						privateMessage = privateMessage + "Ich werde die Information weitergeben, dass Du angekommen bist!\n";
						privateMessage = privateMessage + "Falls Du noch nicht auf https://www.clanwolf.net angemeldet bist,\n";
						privateMessage = privateMessage + "könntest Du jetzt einen Account erstellen!\n";
						privateMessage = privateMessage + "-----------------------------------------------------------------\n";
						privateMessage = privateMessage + "\n\n";
						api.pokeClient(clientId, "Dir wurde eine Nachricht geschickt!");
						api.sendPrivateMessage(clientId, privateMessage);
					} else {
						privateMessage = "Willkommen in den Hallen der Wölfe!";
						api.sendPrivateMessage(clientId, privateMessage);
					}

				}
			}
		});
	}

	public static void prepareLogging() {
		String logFileName = serverBaseDir + File.separator + "log" + File.separator + "TS3Bot.log";
		C3LogUtil.loadConfigurationAndSetLogFile(logFileName);
	}

	public static void main(String[] args) {
		Locale.setDefault(new Locale("en", "EN"));
		TS3Bot ts3bot = new TS3Bot();
		ts3bot.botJoinedMail();
	}

	private void botJoinedMail() {
		String sender = "c3@clanwolf.net";
		String[] receivers = { "keshik@googlegroups.com" };
		String subject = "Bot has joined TS3";
		String content = "Ulric has joined TeamSpeak server.";
		boolean success = MailManager.sendMail(sender, receivers, subject, content, false);
		if (!success) {
			api.sendServerMessage("Mail could not be sent to inform about Ulric logging into TeamSpeak!");
		}
	}
}
