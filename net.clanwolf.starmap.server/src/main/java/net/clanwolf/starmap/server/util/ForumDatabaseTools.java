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
package net.clanwolf.starmap.server.util;

import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.AttackDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackPOJO;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
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

			Class.forName("com.mysql.cj.jdbc.Driver");
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

			Class.forName("com.mysql.cj.jdbc.Driver");
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

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clanwolf", user, password);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);

			stmt.close();
			con.close();
		} catch(Exception e) {
			logger.error("Exception while direct db access", e);
		}
	}

	private void delete(String sql) {
		try {
			String user = auth.getProperty("user");
			String password = auth.getProperty("password");

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/clanwolf", user, password);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);

			stmt.close();
			con.close();
		} catch(Exception e) {
			logger.error("Exception while direct db access", e);
		}
	}

	public void clearTickerEntriesForRound(Long round) {
		if(GameServer.isDevelopmentPC){ return; };
		String sql;
		sql = "";
		sql += "DELETE from clanwolf_ticker ";
		sql += "WHERE tickerNumber='R" + round + "' ";
		delete(sql);
	}

	public void createFinalizingEntryForAttack(Long attackId, Long season, Long round, String system, String attacker, String winner, boolean serverPickedRandomWinner) {
		if(GameServer.isDevelopmentPC){ return; };

		AttackPOJO attack = AttackDAO.getInstance().findById(Nexus.DUMMY_USERID, attackId);
		Long threadId = attack.getForumThreadId();

		logger.info("Finalizing invasion thread...");
		if (threadId != null) {
			String sql;
			long unixTime = System.currentTimeMillis() / 1000L;
			String attackThreadUrl = "https://www.clanwolf.net/forum/viewthread.php?thread_id=" + threadId;

			// Roleplay thread
			sql = "";
			sql += "SELECT closed from codax_W7_RP_threads ";
			sql += "WHERE threadid=" + threadId;
			long threadClosed = selectLong(sql, "closed");
			if (threadClosed == 0) { // thread has not been closed before
				String logo = "https://www.clanwolf.net/apps/C3/static/logos/factions/banner/Banner_" + winner + ".png";
				String subject = "Der Kampf um " + system + " ist entschieden.";
				String text = "";
				if (winner.equalsIgnoreCase(attacker)) {
					text += "[center][b][size=16][color=#ff9900]" + winner + " ist jetzt der Besitzer von " + system + ".[/color][/size][/b][/center]\r\n";
				} else {
					text += "[center][b][size=16][color=#ff9900]" + winner + " bleibt der Besitzer von " + system + ".[/color][/size][/b][/center]\r\n";
				}
				text += "<table width=\"100%\">";
				text += "<tr>";
				text += "<td align=\"right\"> </td>";
				text += "<td align=\"center\"><img src=\"" + logo + "\" width=\"190px\"></td>";
				text += "<td align=\"left\"> </td>";
				text += "</tr>";
				text += "</table>";
				if (serverPickedRandomWinner) {
					text += "\r\n<hr>\r\n[center][b][color=#ffcc00]Dieser Kampf wurde ausgewürfelt.[/color][/b][/center]";
				}

				// Post
				sql = "";
				sql += "INSERT INTO cwfusion_posts ";
				sql += "(forum_id, thread_id, site_id, post_subject, post_message, post_showsig, post_smileys, post_author, post_datestamp, post_ip, post_edituser, post_edittime) ";
				sql += "VALUES ";
				sql += "(65, " + threadId + ", 1, '" + subject + "', '" + text + "', 0, 0, 702, '" + unixTime + "', '0.0.0.0', 0, 0) ";
				long postId = insert(sql);

				// Get Postdatestamp
				sql = "";
				sql += "SELECT post_datestamp ";
				sql += "FROM cwfusion_posts ";
				sql += "WHERE post_id=" + postId + " ";
				long postDatestamp = selectLong(sql, "post_datestamp") + 1;

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

				// Get Replycount
				sql = "";
				sql += "SELECT thread_replies ";
				sql += "FROM cwfusion_threads ";
				sql += "WHERE thread_id=" + threadId + " ";
				long replyCount = selectLong(sql, "thread_replies") + 1;

				// Threads
				sql = "";
				sql += "UPDATE cwfusion_threads ";
				sql += "SET thread_lastpost_id=" + postId + ", thread_lastpost_snippet='" + subject + "', thread_lastuser=702, thread_lastpost=" + postDatestamp + ", thread_replies=" + replyCount + " ";
				sql += "WHERE thread_id=" + threadId + " ";
				update(sql);

				// Forums
				sql = "";
				sql += "UPDATE cwfusion_forums ";
				sql += "SET forum_lastpost=" + postId + ", forum_lastuser=702, forum_lastpost_datestamp=" + postDatestamp + ", forum_threads=" + threadCount + ", forum_posts=" + postCount + ", forum_lastthread=" + threadId + " ";
				sql += "WHERE forum_id=65 ";
				update(sql);

				// Roleplay thread
				sql = "";
				sql += "UPDATE codax_W7_RP_threads ";
				sql += "SET closed=1 ";
				sql += "WHERE threadid=" + threadId;
				update(sql);

				// Ticker
				String roundS = "R" + round;
				String subjectS = system + " gehört jetzt " + winner;
				sql = "";
				sql += "INSERT INTO clanwolf_ticker ";
				sql += "(tickernumber, tickermessage, tickerurl, tickertarget) ";
				sql += "VALUES ";
				sql += "('" + roundS + "', '" + subjectS + "', '" + attackThreadUrl + "', '_BLANK') ";
				insert(sql);
			}
		}
	}

	public void createNewAttackEntries(Long season, Long round, String system, String attacker, String defender, Long attackType, Long attackId,
	                                   String systemImageName, String rank, String name, String unit,
	                                   String dropshipName
	) {
		if(GameServer.isDevelopmentPC){ return; };

		String sql = "";
		String attackThreadUrl = "";

		// Forum-ID: 65
		// "ClanWolf Netzwerk > Rollenspiel ->> C3 / HammerHead - Season 1"
		// https://www.clanwolf.net/forum/viewforum.php?forum_id=65

		long unixTime = System.currentTimeMillis() / 1000L;

		// -------------------------------------------------------------------------------------------------------------

		String attackerLogoLink = "https://www.clanwolf.net/apps/C3/static/logos/factions/" + attacker + ".png";
		String defenderLogoLink = "https://www.clanwolf.net/apps/C3/static/logos/factions/" + defender + ".png";
		if (systemImageName.length() == 2) {
			systemImageName = "0" + systemImageName;
		} else if (systemImageName.length() == 1) {
			systemImageName = "00" + systemImageName;
		}
		String planetImage = "https://www.clanwolf.net/apps/C3/static/planets/" + systemImageName + ".png";

		String subject = "[R" + round + "] " + attacker + " greift " + system + " (" + defender + ") an";
		String text = "";

		text += "<table width=\"100%\">";
		text += "<tr>";
		text += "<td width=\"30%\" align=\"right\"><img src=\"" + attackerLogoLink + "\" width=\"70px\"></td>";
		text += "<td width=\"40%\" align=\"center\" valign=\"top\">[b][size=12][color=#ffcc00]System:[/color][/size]<br>[size=24][color=#ffcc00]" + system + "[/color][/size][/b]</td>";
		text += "<td width=\"30%\" align=\"left\"><img src=\"" + defenderLogoLink + "\" width=\"70px\"></td>";
		text += "</tr>";
		text += "<tr>";
		text += "<td align=\"right\">" + unit + "<br>" + rank + " " + name + " (CO)</td>";
		text += "<td align=\"center\"><img src=\"" + planetImage + "\" width=\"90px\"></td>";
		text += "<td align=\"left\">Planetare<br>Verteidigungstreitkräfte</td>";
		text += "</tr>";
		text += "<tr><td></td><td width=\"40%\" align=\"center\">";
		text += "<br>";
		text += "<table cellspacing=\"1\" width=\"100%\">";
		text += "<tr><td align=\"right\" width=\"50%\">[color=#ffffff]Season:</td><td align=\"left\" width=\"50%\">[color=#ffffff]" + season + "[/color]</td></tr>";
		text += "<tr><td align=\"right\">[color=#ffffff]Runde:[/color]</td><td align=\"left\">[color=#ffffff]" + round + "[/color]</td></tr>";
		text += "<tr><td align=\"right\">[color=#ffffff]Angreifer:[/color]</td><td align=\"left\">[color=#ffffff]" + attacker + "[/color]</td></tr>";
		text += "<tr><td align=\"right\">[color=#ffffff]Verteidiger:[/color]</td><td align=\"left\">[color=#ffffff]" + defender + "[/color]</td></tr>";

		// -1: Clan vs IS
		// -2: Clan vs Clan
		// -3: IS vs Clan
		// -4: IS vs IS
		if (attackType != null) {
			text += switch (attackType.intValue()) {
				case -1 -> "<tr><td align=\"right\" valign=\"top\">[color=#ffffff]Typ:[/color]</td><td align=\"left\" valign=\"top\">[color=#ffffff]Planetare Invasion[/color]<br>(<a href=\"https://www.clanwolf.net/apps/C3/static/scenarios/CWG_S001_var01_v4-PlanetareInvasion_CLAN_vs_IS.png\" target=\"_BLANK\">Clan vs IS</a>)[/color]</td></tr>";
				case -2 -> "<tr><td align=\"right\" valign=\"top\">[color=#ffffff]Typ:[/color]</td><td align=\"left\" valign=\"top\">[color=#ffffff]Planetare Invasion[/color]<br>(<a href=\"https://www.clanwolf.net/apps/C3/static/scenarios/CWG_S003_var01_v1-PlanetareInvasion_CLAN_vs_CLAN.png\" target=\"_BLANK\">Clan vs Clan)[/color]</td></tr>";
				case -3 -> "<tr><td align=\"right\" valign=\"top\">[color=#ffffff]Typ:[/color]</td><td align=\"left\" valign=\"top\">[color=#ffffff]Planetare Invasion[/color]<br>(<a href=\"https://www.clanwolf.net/apps/C3/static/scenarios/CWG_S002_var01_v5-PlanetareInvasion_IS_vs_CLAN.png\" target=\"_BLANK\">IS vs Clan)[/color]</td></tr>";
				case -4 -> "<tr><td align=\"right\" valign=\"top\">[color=#ffffff]Typ:[/color]</td><td align=\"left\" valign=\"top\">[color=#ffffff]Planetare Invasion[/color]<br>(<a href=\"https://www.clanwolf.net/apps/C3/static/scenarios/CWG_S004_var01_v1-PlanetareInvasion_IS_vs_IS.png\" target=\"_BLANK\">IS vs IS)[/color]</td></tr>";
				default -> "<tr><td align=\"right\" valign=\"top\">[color=#ffffff]Typ:[/color]</td><td align=\"left\" valign=\"top\">[color=#ffffff]Planetare Invasion[/color]</td></tr>";
			};
		} else {
			text += "FEHLER: Angriffstyp ist leer!" + "<br>";
		}
		text += "</table>";
		text += "</td><td></td></tr>";
		text += "<tr><td></td><td><br>[color=#ffcc00]";

		SimpleDateFormat dtf = new SimpleDateFormat("dd.MM.yyyy");
		Calendar calendar = Calendar.getInstance();
		Date dateObj = calendar.getTime();
		String formattedDate = dtf.format(dateObj);

		// -1: Clan vs IS
		// -2: Clan vs Clan
		// -3: IS vs Clan
		// -4: IS vs IS
		if (attackType != null) {
			text += switch (attackType.intValue()) {
				case -1 -> "<i>" + system + "<br>Landungsschiff der Union-C-Klasse \"" + dropshipName + "\"<br>Im Anflug auf " + system + "</i><br><br>" + unit + " (" + attacker + ") befindet sich im Anflug auf " + system + ". Während des Anfluges wird das Batchall übertragen." + "<br>";
				case -2 -> "<i>" + system + "<br>Landungsschiff der Union-C-Klasse \"" + dropshipName + "\"<br>Im Anflug auf " + system + "</i><br><br>" + unit + " (" + attacker + ") befindet sich im Anflug auf " + system + ". Während des Anfluges wird das Batchall übertragen." + "<br>";
				case -3 -> "<i>" + system + "<br>Landungsschiff der Union-Klasse \"" + dropshipName + "\"<br>Im Anflug auf " + system + "</i><br><br>" + unit + " (" + attacker + ") ist auf dem Weg nach " + system + ", der Hauptwelt des Systems. Sie werden bereits erwartet." + "<br>";
				case -4 -> "<i>" + system + "<br>Landungsschiff der Union-Klasse \"" + dropshipName + "\"<br>Im Anflug auf " + system + "</i><br><br>" + unit + " (" + attacker + ") werden sehr bald auf " + system + " landen. Eine Verteidigung wird bereits organisiert." + "<br>";
				default -> "Landungsschiffe nähern sich " + system + "!";
			};
		} else {
			text += "FEHLER: Angriffstyp ist leer!" + "<br>";
		}

		String image_url = "https://www.clanwolf.net/apps/C3/seasonhistory/S" + season + "/C3_S1_R" + round + "_map_history.png";
		String image_url_alternative = "https://www.clanwolf.net/images/map.png";

		text += "[/color]<br><br></td><td></td></tr>";
		text += "<tr><td colspan=\"3\" align=\"center\">";
		text += "<a href=\"https://www.clanwolf.net/apps/C3/seasonhistory/S" + season + "/starmap.php\" target=\"_BLANK\">";
		text += "<img src=\"" + image_url + "\" width=\"400px\" onError=\"this.src=&#39;" + image_url_alternative + "&#39;;this.style.width=&#39;35px&#39;;\">";
		text += "</a>";
		text += "</td></tr>";
		text += "</table>";
		text += "<br><br>";

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
		sql += "SET ForumThreadLink='" + attackThreadUrl + "', ForumThreadId=" + threadId + " ";
		sql += "WHERE ID= " + attackId;
		update(sql);

		// attacker:
		// - LA  : Lyran Alliance
		// - DC  : Draconis Combine
		// - FRR : Free Rasalhague Republic
		// - CS  : ComStar
		// - CW  : Clan Wolf
		// - CJF : Clan Jade Falcon
		// - CGB : Clan Ghost Bear
		logger.info("---------------------------------------------------------   Attacker: " + attacker);
		String image = "";
		if (attacker != null) {
			image = switch (attacker) {
				case "LA" -> "https://www.clanwolf.net/images/C3_RP_Header_LA.png";
				case "DC" -> "https://www.clanwolf.net/images/C3_RP_Header_DC.png";
				case "CS" -> "https://www.clanwolf.net/images/C3_RP_Header_CS.png";
				case "CW" -> "https://www.clanwolf.net/images/C3_RP_Header_CW.png";
				case "FRR" -> "https://www.clanwolf.net/images/C3_RP_Header_FRR.png";
				case "CJF" -> "https://www.clanwolf.net/images/C3_RP_Header_CJF.png";
				case "CGB" -> "https://www.clanwolf.net/images/C3_RP_Header_CGB.png";
				default -> "https://www.clanwolf.net/images/C3_RP_Header_01.png";
			};
		} else {
			image = "https://www.clanwolf.net/images/C3_RP_Header_01.png";
		}

		// attackType:
		// -1: Clan vs IS
		// -2: Clan vs Clan
		// -3: IS vs Clan
		// -4: IS vs IS
//		logger.info("--------------------------------------------------------- AttackType: " + attackType);
//		if (attackType != null) {
//			 image = switch (attackType.intValue()) {
//				case -1 -> "https://www.clanwolf.net/images/C3_RP_Header_01.png";
//				case -2 -> "https://www.clanwolf.net/images/C3_RP_Header_02.png";
//				case -3 -> "https://www.clanwolf.net/images/C3_RP_Header_03.png";
//				case -4 -> "https://www.clanwolf.net/images/C3_RP_Header_04.png";
//				default -> "";
//			};
//		} else {
//			image = "https://www.clanwolf.net/images/C3_RP_Header_01.png";
//		}

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
		String roundS = "R" + round;
		String subjectS = subject.replaceFirst("[R" + round + "] ", "");
		sql = "";
		sql += "INSERT INTO clanwolf_ticker ";
		sql += "(tickernumber, tickermessage, tickerurl, tickertarget) ";
		sql += "VALUES ";
		sql += "('" + roundS + "', '" + subjectS + "', '" + attackThreadUrl + "', '_BLANK') ";
		insert(sql);
	}
}
