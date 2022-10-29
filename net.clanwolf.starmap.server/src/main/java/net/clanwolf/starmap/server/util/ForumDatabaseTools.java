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
package net.clanwolf.starmap.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class ForumDatabaseTools {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final Properties auth = new Properties();
	private static final String authFileName = "auth.properties";

	public ForumDatabaseTools() {
		try {
			InputStream inputStream = ForumDatabaseTools.class.getClassLoader().getResourceAsStream(authFileName);
			if (inputStream != null) {
				auth.load(inputStream);
			} else {
				throw new FileNotFoundException("Auth-Property file '" + authFileName + "' not found in classpath.");
			}
		} catch (IOException ioe) {
			logger.error("IOException", ioe);
		}
	}

	private Long selectLong(String sql, String columnName) {
		long number = -1;
		try {
			String user = auth.getProperty("user");
			String password = auth.getProperty("password");

			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clanwolf", user, password);
			Statement stmt = con.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);

			while (resultSet.next()) {
				number = resultSet.getLong(columnName);
			}

			stmt.close();
			con.close();
		} catch(Exception e) {
			logger.error("Exception while direct db access", e);
		}

		return number;
	}

	private long insert(String sql) {
		long generatedId = -1L;

		try {
			String user = auth.getProperty("user");
			String password = auth.getProperty("password");

			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clanwolf", user, password);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					generatedId = generatedKeys.getLong(1);
				}
			}

			stmt.close();
			con.close();
		} catch(Exception e) {
			logger.error("Exception while direct db access", e);
		}

		return generatedId;
	}

	private void update(String sql) {
		try {
			String user = auth.getProperty("user");
			String password = auth.getProperty("password");

			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clanwolf", user, password);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);

			stmt.close();
			con.close();
		} catch(Exception e) {
			logger.error("Exception while direct db access", e);
		}
	}

	public void createNewAttackEntries(Long season, Long round, String system, String attacker, String defender, Long attackType, Long attackId) {
		String sql = "";
		String attackThreadUrl = "";

		// Forum-ID: 65
		// "ClanWolf Netzwerk > Rollenspiel ->> C3 / HammerHead - Season 1"
		// https://www.clanwolf.net/forum/viewforum.php?forum_id=65

		long unixTime = System.currentTimeMillis() / 1000L;

		// -------------------------------------------------------------------------------------------------------------

		String subject = "[C3] " + attacker + " greift " + system + " (" + defender + ") an";
		String text = "";
		text += "<table width=\"100%\"><tr><td align=\"left\"><img src=\"https://www.clanwolf.net/images/Logos/JadeFalcon.png\" width=\"70px\"></td><td align=\"center\" valign=\"top\">[b][size=12][color=#ffcc00]System:[/color][/size]<br>[size=24][color=#ffcc00]Rasalgethi[/color][/size][/b]</td><td align=\"right\"><img src=\"https://www.clanwolf.net/images/Logos/LyranCommonwealth.png\" width=\"70px\"></td></tr></table>";
		text += "Season: " + season;
		text += "Runde: " + round;
		text += "Angreifer: " + attacker;
		text += "Verteidiger: " + defender;
		text += "Typ: Planetare Invasion";
		text += "<br>";

		// -1: Clan vs IS
		// -2: Clan vs Clan
		// -3: IS vs Clan
		// -4: IS vs IS
		if (attackType != null) {
			text += switch (attackType.intValue()) {
				case -1 -> "Der Clan befindet sich im Anflug auf " + system + ". Während des Anflugs wird das Batchall übertragen...";
				case -2 -> "Der Clan befindet sich im Anflug auf " + system + ". Während des Anflugs wird das Batchall übertragen...";
				case -3 -> "Die Streitkräfte der Invasoren fliegen zur Hauptwelt des Systems. Sie werden bereits erwartet.";
				case -4 -> "Die angreifenden Kräfte werden sehr bald auf dem Planeten landen. Eine Verteidigung wird bereits organisiert.";
				default -> "FEHLER: Angriffstyp ist leer!";
			};
		} else {
			text += "FEHLER: Angriffstyp ist leer!";
		}
		text += "footer";

		// -------------------------------------------------------------------------------------------------------------

		// Thread
		sql = "";
		sql += "INSERT INTO cwfusion_threads ";
		sql += "(forum_id, site_id, thread_subject, thread_author, thread_views, thread_lastpost_id, thread_lastpost, thread_lastpost_snippet, thread_lastuser, thread_sticky, thread_locked, thread_replies) ";
		sql += "VALUES ";
		sql += "(65, 1, '" + subject + "', 702, 1, 1, '" + unixTime + "', '" + subject + "', 702, 0, 0, 0) ";
		long threadId = insert(sql);

		attackThreadUrl = "https://www.clanwolf.net/forum/viewthread.php?thread_id=" + threadId;

		// Post
		sql = "";
		sql += "INSERT INTO cwfusion_posts ";
		sql += "(forum_id, thread_id, site_id, post_subject, post_message, post_showsig, post_smileys, post_author, post_datestamp, post_ip, post_edituser, post_edittime) ";
		sql += "VALUES ";
		sql += "(65, " + threadId + ", 1, '" + subject + "', '" + text + "', 0, 0, 702, '" + unixTime + "', '0.0.0.0', 0, 0) ";
		long postId = insert(sql);

		// Get Postcount
		sql = "";
		sql += "SELECT forum_posts ";
		sql += "FROM cwfusion_forums ";
		sql += "WHERE forum_id=65 ";
		long postCount = selectLong(sql, "forum_posts") + 1;

		// Get Threadcount
		sql = "";
		sql += "SELECT forum_threads ";
		sql += "FROM cwfusion_forums ";
		sql += "WHERE forum_id=65 ";
		long threadCount = selectLong(sql, "forum_threads") + 1;

		// Forums
		sql = "";
		sql += "UPDATE cwfusion_forums ";
		sql += "SET forum_lastpost=" + postId + ", forum_lastuser=702, forum_threads=" + threadCount + ", forum_posts=" + postCount + ", forum_lastthread=" + threadId + " ";
		sql += "WHERE forum_id=65 ";
		update(sql);

		// Threads
		sql = "";
		sql += "UPDATE cwfusion_threads ";
		sql += "SET thread_lastpost_id=" + postId + " ";
		sql += "WHERE thread_id= " + threadId;
		update(sql);

		// Update attack and add threadlink
		sql = "";
		sql += "UPDATE C3._HH_ATTACK ";
		sql += "SET ForumThreadLink='" + attackThreadUrl + "' ";
		sql += "WHERE ID= " + attackId;
		update(sql);

		// -1: Clan vs IS
		// -2: Clan vs Clan
		// -3: IS vs Clan
		// -4: IS vs IS
		logger.info("--------------------------------------------------------- AttackType: " + attackType);
		String image = "";
		if (attackType != null) {
			 image = switch (attackType.intValue()) {
				case -1 -> "https://www.clanwolf.net/images/C3_RP_Header_01.png";
				case -2 -> "https://www.clanwolf.net/images/C3_RP_Header_02.png";
				case -3 -> "https://www.clanwolf.net/images/C3_RP_Header_03.png";
				case -4 -> "https://www.clanwolf.net/images/C3_RP_Header_04.png";
				default -> "";
			};
		} else {
			image = "https://www.clanwolf.net/images/C3_RP_Header_01.png";
		}

		// Roleplay thread
		sql = "";
		sql += "INSERT INTO codax_W7_RP_threads ";
		sql += "(threadid, title, imagelink, closed) ";
		sql += "VALUES ";
		sql += "(" + threadId + ", '" + subject + "', '" + image + "', 0) ";
		insert(sql);

		// Get Tickercount
		sql = "";
		sql += "SELECT count(*) count ";
		sql += "FROM clanwolf_ticker ";
		String tickerId = "";
		long tickerCount = selectLong(sql, "count") + 1;
		if (tickerCount < 10) {
			tickerId = "0" + tickerCount;
		} else {
			tickerId = "" + tickerCount;
		}

		// Ticker
		sql = "";
		sql += "INSERT INTO clanwolf_ticker ";
		sql += "(tickernumber, tickermessage, tickerurl, tickertarget) ";
		sql += "VALUES ";
		sql += "('" + tickerId + "', '" + subject + "', '" + attackThreadUrl + "', '_BLANK') ";
		insert(sql);
	}
}
