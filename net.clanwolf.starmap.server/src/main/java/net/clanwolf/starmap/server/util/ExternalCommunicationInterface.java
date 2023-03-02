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
import net.clanwolf.starmap.server.nexus2.Nexus;
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

public class ExternalCommunicationInterface {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final Properties auth = new Properties();
	private static final String authFileName = "auth.properties";

	public ExternalCommunicationInterface() {
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

	private synchronized long insert(String sql) {
		long generatedId = -1L;

		try {
			String user = auth.getProperty("user");
			String password = auth.getProperty("password");

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/C3", user, password);
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
			logger.error("Exception during direct db access", e);
		}

		return generatedId;
	}

	private synchronized void delete(String sql) {
		try {
			String user = auth.getProperty("user");
			String password = auth.getProperty("password");

			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/C3", user, password);
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);

			stmt.close();
			con.close();
		} catch(Exception e) {
			logger.error("Exception during direct db access", e);
		}
	}

	public synchronized void sendExtCom(String message) {
		message = message.replace("'", "\"");
		if (!GameServer.isDevelopmentPC) {
			try {
				// Insert message for IRC and TS3 Bots to process
				String sql_insert = "";
				sql_insert += "INSERT INTO EXT_COM ";
				sql_insert += "(Text,ServerVersion) ";
				sql_insert += "VALUES ";
				sql_insert += "('" + message + "', '" + Nexus.jarName + "'); ";
				long extcomid = insert(sql_insert);

				// Delete all old entries, check if they have been processed
				String sql_delete = "";
				sql_delete += "DELETE from EXT_COM ";
				sql_delete += "WHERE ProcessedIRC > 0 ";
				sql_delete += "AND ProcessedTS3 > 0 ";
				sql_delete += "OR Updated < now() - interval 1 DAY; ";
				delete(sql_delete);
			} catch(Exception e) {
				logger.error("Error inserting extcom entry.", e);
			}
		}
	}
}
