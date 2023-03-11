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
package net.clanwolf.starmap.bots.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class ExtcomMonitor {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public synchronized static LinkedList<String> getMessages() throws Exception {
		// get messages from database
		// mark message as processed by IRCBot in database
		Statement stmt_select = null;
		Statement stmt_update = null;
		ResultSet rs_select = null;
		LinkedList<String> messages = new LinkedList<>();

//		logger.info("Getting Messages from DB");
		DBConnection dbc = new DBConnection();
		try {
			String sql_select = "SELECT Text, ProcessedIRC, Updated FROM EXT_COM WHERE ProcessedIRC = 0; ";
			// sql_select.append("AND Updated > now() - interval 1 MINUTE; ");

			stmt_select = dbc.getConnection().createStatement();
			rs_select = stmt_select.executeQuery(sql_select);
			if (rs_select.next()) {
				do {
					messages.add(rs_select.getString("Text"));
				} while (rs_select.next());
			}
			rs_select.close();
			stmt_select.close();

			// Update to set processed to 1
			stmt_update = dbc.getConnection().createStatement();
			stmt_update.executeUpdate("UPDATE EXT_COM set ProcessedIRC = 1;"); // Update all lines to be processed

			stmt_update.close();
		} catch (SQLException e) {
			logger.error("Exception while extracting extcom messages from db.", e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception while extracting extcom messages from db. [12]", e);
			e.printStackTrace();
		} finally {
			if (rs_select != null) {
				try {
					rs_select.close();
				} catch (Exception ignored) {
				}
			}
			if (stmt_select != null) {
				try {
					stmt_select.close();
				} catch (Exception ignored) {
				}
			}
			if (stmt_update != null) {
				try {
					stmt_update.close();
				} catch (Exception ignored) {
				}
			}
			if (!dbc.getConnection().getAutoCommit()) {
				dbc.getConnection().commit();
			}
			dbc.getConnection().close();
			dbc = null;
		}
		return messages;
	}
}
