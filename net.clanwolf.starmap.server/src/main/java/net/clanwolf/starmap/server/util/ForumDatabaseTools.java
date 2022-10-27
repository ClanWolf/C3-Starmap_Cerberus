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

	public void createNewAttackEntries(Long season, Long round, String system, String attacker, String defender) {
		String sql = "";

		// Forum-ID: 65
		// "ClanWolf Netzwerk > Rollenspiel ->> C3 / HammerHead - Season 1"
		// https://www.clanwolf.net/forum/viewforum.php?forum_id=65

		long unixTime = System.currentTimeMillis() / 1000L;
		String text = "";
		text += "test";

		// Thread
		sql = "";
		sql += "INSERT INTO cwfusion_threads ";
		sql += "(forum_id, site_id, thread_subject, thread_author, thread_views, thread_lastpost_id, thread_lastpost, thread_lastpost_snippet, thread_lastuser, thread_sticky, thread_locked, thread_replies) ";
		sql += "values ";
		sql += "(65, 1, '[C3] Angriff auf " + system + " (" + defender + ")', 702, 1, 1, '" + unixTime + "', '[C3] Angriff auf " + system + "', 702, 0, 0, 0)";
		long id1 = insert(sql);

		// Post
		sql = "";
		sql += "INSERT INTO cwfusion_posts ";
		sql += "(forum_id, thread_id, site_id, post_subject, post_message, post_showsig, post_smileys, post_author, post_datestamp, post_ip, post_edituser, post_edittime) ";
		sql += "values ";
		sql += "(65, " + id1 + ", 1, '[C3] Angriff auf " + system + " (" + defender  + ")', '" + text + "', 0, 0, 702, '" + unixTime + "', '0.0.0.0', 0, 0)";
		insert(sql);
	}
}
