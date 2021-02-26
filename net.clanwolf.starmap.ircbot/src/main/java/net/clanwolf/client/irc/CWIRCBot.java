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
 * Copyright (c) 2001-2020, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.client.irc;

import com.google.common.collect.ImmutableSortedSet;
import net.clanwolf.client.db.DBConnection;
import net.clanwolf.client.mail.MailManager;
import net.clanwolf.client.util.Internationalization;
import org.pircbotx.*;
import org.pircbotx.exception.DaoException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.PartEvent;
import org.pircbotx.hooks.events.QuitEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CWIRCBot extends ListenerAdapter {

	// QuakeNet Servers:
	// - euroserv.fr.quakenet.org
	// - port80a.se.quakenet.org

	public static boolean dropDebugStrings = false;

	private static PircBotX pIrcBot;
	private static DBConnection dbc;
	private static UserChannelDao<User, Channel> channel = null;
	private static final boolean server = true;
	private static boolean started = false;
	private static final String ircUserName = "Ulric";
	private static final String ircServerUrl = "port80a.se.quakenet.org";
	private static final String ircServerChannel = "#c3.clanwolf.net";
	private static final String ircUserListFileName = "/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/ircUser.lst";
	private static final String heartbeatFileName = "/var/www/vhosts/clanwolf.net/httpdocs/apps/C3/c3.heartbeat";
	private static StringBuilder userListString = null;

	private static String lang = "de";

	private static final String[] message = {
		Internationalization.getString("msg0001"),
		Internationalization.getString("msg0002"),
		Internationalization.getString("msg0003"),
		Internationalization.getString("msg0004"),
		Internationalization.getString("msg0005"),
		Internationalization.getString("msg0006"),
		Internationalization.getString("msg0007"),
		Internationalization.getString("msg0008"),
		Internationalization.getString("msg0009"),
		Internationalization.getString("msg0010"),
		Internationalization.getString("msg0011"),
		Internationalization.getString("msg0012"),
		Internationalization.getString("msg0013"),
		Internationalization.getString("msg0014")
	};

	private static final String[] welcomeMessage = {
		Internationalization.getString("wel0001"),
		Internationalization.getString("wel0002"),
		Internationalization.getString("wel0003"),
		Internationalization.getString("wel0004"),
		Internationalization.getString("wel0005"),
		Internationalization.getString("wel0006"),
		Internationalization.getString("wel0007"),
		Internationalization.getString("wel0008")
	};

	private static final HashMap<String, String> answerableMessages = new HashMap<>();

	static {
		// en
		answerableMessages.put("Hello", "Welcome to Clan Wolfs (CWG) IRC channel! Visit us at https://www.clanwolf.net!");
		answerableMessages.put("Hi", "Welcome to Clan Wolfs (CWG) IRC channel! Visit us at https://www.clanwolf.net!");
		answerableMessages.put("Moin", "Welcome to Clan Wolfs (CWG) IRC channel! Visit us at https://www.clanwolf.net!");
		answerableMessages.put("Thanks", "Oh, you are welcome!");
		answerableMessages.put("Thank you", "You are welcome!");

		// de
		answerableMessages.put("Hallo", "Willkommen im IRC Channel von Clan Wolf (CWG)! Besuche uns auf https://www.clanwolf.net!");
		answerableMessages.put("Tag", "Willkommen im IRC Channel von Clan Wolf (CWG)! Besuche uns auf https://www.clanwolf.net!");
	}

	public void saveUserList() {
		saveUserList(null, null);
	}

	public void saveUserList(GenericMessageEvent event, UserChannelDao<User, Channel> ucd) {
		if (ucd != null) {
			channel = ucd;
		} else {
			if (channel == null) {
				if (event != null) {
					channel = event.getBot().getUserChannelDao();
				} else {
					return;
				}
			}
		}

		Channel ch = null;
		try {
			ch = channel.getChannel(ircServerChannel);
		} catch (DaoException e) {
			// e.printStackTrace();
			if (dropDebugStrings) {
				send(Internationalization.getString("channelNotFound", ircServerChannel));                 // [e002]
				send("Exception: " + e.getMessage());
			}
		}

		if (ch != null) {
			userListString = new StringBuilder();

			String topic = ch.getTopic();
			userListString.append("Topic: ").append(topic).append("\r\n");

			ImmutableSortedSet<User> userList = channel.getAllUsers();
			for (User user : userList) {
				String userName = user.getNick();
				userListString.append(userName).append("\r\n");
			}

			File f = new File(ircUserListFileName);
			if (!f.isFile()) {
				try {
					boolean result = f.createNewFile();
					if (!result) {
						send(Internationalization.getString("unableToWriteUserList"));                     // [e001]
					}
				} catch (IOException e) {
					e.printStackTrace();
					send(Internationalization.getString("unableToWriteUserList"));                         // [e001]
				}
			}

			if (f.isFile() && f.canWrite()) {
				boolean deleted = f.delete();
				if (!deleted) {
					send("Could not delete old user list file!");
				}
				try (PrintWriter out = new PrintWriter(ircUserListFileName)) {
					out.println(userListString.toString());
					if (dropDebugStrings) {
						send("UserList dropped");
					}
				} catch (FileNotFoundException fnfe) {
					fnfe.printStackTrace();
				}
			} else {
				send(Internationalization.getString("unableToWriteUserList"));                             // [e001]
			}
		} else {
			if (dropDebugStrings) {
				send(Internationalization.getString("channelNotFound", ircServerChannel));                 // [e002]
			}
		}
	}

	public void dropRandomLine() {
		// Random sentences --------------------------------------------------------------------------------------------
		int num = (int) (Math.random() * message.length);
		send(message[num]);
	}

	@Override
	public void onJoin(JoinEvent event) {
		String user = Objects.requireNonNull(event.getUser()).getNick();
		String sender = "c3@clanwolf.net";
		String[] receivers = { "keshik@googlegroups.com" };
		String subject = "New user (" + user + ") entered #c3.clanwolf.net (EoM)";
		String content = "A user has entered the Clan IRC channel on QuakeNet. Do not reply to this message.";
		boolean success = MailManager.sendMail(sender, receivers, subject, content, false);
		if (!success) {
			send("Mail could not be sent to inform about new users!");
		}

		// Welcome -----------------------------------------------------------------------------------------------------
		if (!"Ulric".equals(user)) {
			int num = (int) (Math.random() * welcomeMessage.length);
			send("---------------------------------------------------------------");
			send(welcomeMessage[num] + " " + user + "!");
			send("Use '!" + ircUserName + ", help' to see list of commands!");
			if (lang.equals("en")) {
				send("I currently speak english.");
			} else if (lang.equals("de")) {
				send("Im Augenblick spreche ich deutsch.");
			}
			//			send("---------------------------------------------------------------");
		}

		checkDB();
		checkServerHeartbeat();
		listStatus();

		UserChannelDao<User, Channel> ucd = event.getBot().getUserChannelDao();
		saveUserList(null, ucd);
	}

	@Override
	public void onPart(PartEvent event) {
		UserChannelDao<User, Channel> ucd = event.getBot().getUserChannelDao();
		saveUserList(null, ucd);
	}

	@Override
	public void onQuit(QuitEvent event) {
		UserChannelDao<User, Channel> ucd = event.getBot().getUserChannelDao();
		saveUserList(null, ucd);
	}

	@Override
	public void onNickChange(NickChangeEvent event) {
		UserChannelDao<User, Channel> ucd = event.getBot().getUserChannelDao();
		saveUserList(null, ucd);
	}

	@Override
	public void onGenericMessage(GenericMessageEvent event) {
		if (server) {
			try {
				// Check for a command ---------------------------------------------------------------------------------
				String m = event.getMessage().toLowerCase();
				if (m.contains("!" + ircUserName.toLowerCase())) {
					if (m.contains("list status".toLowerCase())) {
						listStatus();
					} else if (m.contains("help".toLowerCase())) {
						send("You can use the following commands:");
						send("- !" + ircUserName + ", help!");
						send("- !" + ircUserName + ", list status!");
						send("- !" + ircUserName + ", check database connection!");
						send("- !" + ircUserName + ", check server heartbeat!");
						send("- !" + ircUserName + ", toggle debug!");
						send("- !" + ircUserName + ", switch lang to en!");
						send("- !" + ircUserName + ", switch lang to de!");
//						send("- !" + ircUserName + ", !");
//						send("- !" + ircUserName + ", !");
//						send("- !" + ircUserName + ", !");
//						send("- !" + ircUserName + ", !");
//						send("- !" + ircUserName + ", !");
//						send("- !" + ircUserName + ", !");
//						send("- !" + ircUserName + ", !");
//						send("- !" + ircUserName + ", !");
//						send("- !" + ircUserName + ", !");
//						send("- !" + ircUserName + ", !");
//						send("- !" + ircUserName + ", !");
					} else if (m.contains("check database connection".toLowerCase())
							|| m.contains("check connection".toLowerCase())
							|| m.contains("check database".toLowerCase())) {
						checkDB();
					} else if (m.contains("check server heartbeat".toLowerCase())
							|| m.contains("check heartbeat".toLowerCase())
							|| m.contains("check server".toLowerCase())) {
						checkServerHeartbeat();
					} else if (m.contains("toggle debug".toLowerCase())) {
						dropDebugStrings = !dropDebugStrings;
						send("Debug mode was toggled to " + dropDebugStrings + "!");
					} else if (m.contains("switch lang to en")) {
						lang = "en";
						Internationalization.setLocale(Locale.ENGLISH);
						send("Switched to english!");
					} else if (m.contains("switch lang to de")) {
						lang = "de";
						Internationalization.setLocale(Locale.GERMAN);
						send("Ich spreche jetzt deutsch!");
					}
//					else if (m.contains("list status")) {
//
//					}
//					else if (m.contains("list status")) {
//
//					}
//					else if (m.contains("list status")) {
//
//					}
//					else if (m.contains("list status")) {
//
//					}
//					else if (m.contains("list status")) {
//
//					}
//					else if (m.contains("list status")) {
//
//					}
//					else if (m.contains("list status")) {
//
//					}
				} else {
					// Responses ---------------------------------------------------------------------------------------
					for (String c : answerableMessages.keySet()) {
						if ((event.getMessage().toLowerCase()).startsWith(c.toLowerCase())) {
							send(answerableMessages.get(c));
						}
					}
				}

				// Random sentences ------------------------------------------------------------------------------------
				int num1 = (int) (Math.random() * 100);
				if (num1 > 90) {
					dropRandomLine();
				}

				// Drop user list --------------------------------------------------------------------------------------
				saveUserList(event, null);
			} catch (Exception e) {
				e.printStackTrace();
				send(e.getMessage());
			}
		}
	}

	public void start() throws Exception {
		Configuration configuration = new Configuration.Builder()
				.setName(ircUserName)
				.addServer(ircServerUrl)
				.addAutoJoinChannel(ircServerChannel)
				.addListener(new CWIRCBot())
				.buildConfiguration();
		pIrcBot = new PircBotX(configuration);
		pIrcBot.startBot();
	}

	public synchronized void checkDB() {
		if (dbc == null) {
			dbc = new DBConnection();
		}
		if (!dbc.connected()) {
			send(Internationalization.getString("sbConnectError"));                                        // [e004]
		} else {
			send(Internationalization.getString("dbConnected"));                                           // [i001]
		}
	}

	public synchronized void listStatus() {
		// List info ---------------------------------------------------------------------------------------------------
//		send("Listing all ongoing battles in active campaign:");
//		send("- to be implemented soon...");
	}

	public synchronized String getUserListString() {
		if (userListString != null) {
			return userListString.toString();
		}
		return null;
	}

	public synchronized void checkServerHeartbeat() {
		File hbf = new File(heartbeatFileName);
		if (hbf.isFile() && hbf.canRead()) {
			try {
				DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				String timestamp = new String(Files.readAllBytes(Paths.get(hbf.getAbsolutePath())));
				Date d = new Date(Long.parseLong(timestamp));
				send(Internationalization.getString("lastReportTime", formatter.format(d)));               // [i002]
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			send(Internationalization.getString("timeStampNotFound"));                                     // [e003]
		}
	}

	public void send(String message) {
		if (started) {
			pIrcBot.send().message(ircServerChannel, message);
		}
	}

	public static void main(String[] args) throws Exception {
		int oneMinute = 1000 * 60;
		dbc = new DBConnection();

		Timer userlistDropTimer = new Timer();
		UserListDrop userlistDrop = new UserListDrop();
		userlistDropTimer.schedule(userlistDrop, 1000, 5 * oneMinute);

		Timer randomTextDropTimer = new Timer();
		RandomTextDrop randomTextDrop = new RandomTextDrop();
		randomTextDropTimer.schedule(randomTextDrop, 1000, 3 * oneMinute);

		CWIRCBot cwBot = new CWIRCBot();
		userlistDrop.setBot(cwBot);
		randomTextDrop.setBot(cwBot);
		Internationalization.setBot(cwBot);
		dbc.setBot(cwBot);
		started = true;
		cwBot.start();
	}
}
