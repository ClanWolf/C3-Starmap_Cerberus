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

import com.mysql.cj.jdbc.MysqlDataSource;
import net.clanwolf.starmap.bots.ircclient.IRCBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private Connection conn = null;

	public DBConnection() {
		getNewDBConnection();
	}

	public void getNewDBConnection() {
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

		try {
			Context context = new InitialContext();
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setUser(auth.getProperty("user"));
			dataSource.setPassword(auth.getProperty("password"));
			dataSource.setServerName("localhost");
			dataSource.setDatabaseName("C3");
			dataSource.setServerTimezone("CET");

			conn = dataSource.getConnection();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
			logger.error("Exception in IRCBot getting DB connection", e);
		}
	}

	public boolean connected() throws SQLException {
		return conn != null && !conn.isClosed();
	}

	public Connection getConnection() throws SQLException {
		if (this.conn == null || this.conn.isClosed()) {
			this.getNewDBConnection();
		}
		return this.conn;
	}
}
