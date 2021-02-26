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
package net.clanwolf.client.db;

import com.mysql.cj.jdbc.MysqlDataSource;
import net.clanwolf.client.irc.CWIRCBot;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

	private Connection conn = null;
	private CWIRCBot bot = null;

	public void setBot(CWIRCBot bot) {
		this.bot = bot;
	}

	public boolean connected() {
		return conn != null;
	}

	public DBConnection() {
		try {
			Context context = new InitialContext();
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setUser("C3_Workbench");
			dataSource.setPassword("C69udmj99cz7z3cm6a6f48lrolsfkfr7");
			dataSource.setServerName("localhost");
			dataSource.setDatabaseName("C3");
			dataSource.setServerTimezone("CET");

			conn = dataSource.getConnection();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
			if (bot != null) {
				if (CWIRCBot.dropDebugStrings) bot.send(e.getMessage());
			}
		}
	}

//	public void getValue() {
//		try {
//			Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT ID FROM USERS");
//
//			rs.close();
//			stmt.close();
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
