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
package net.clanwolf.ircclient;

import net.clanwolf.db.DBConnection;
import net.clanwolf.db.ExtcomMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * @author Meldric
 */
public class ExtcomTimerTask extends TimerTask {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final ExtcomMonitor extcomMonitor = new ExtcomMonitor();
	private IRCBot bot = null;
	private DBConnection dbc = null;

	public ExtcomTimerTask(DBConnection dbc) {
		this.dbc = dbc;
	}

	public void setBot(IRCBot bot) {
		this.bot = bot;
	}

	@Override
	public void run() {
		if (bot != null) {
			if (bot.connected) {
				ArrayList<String> m = extcomMonitor.getMessages(dbc);

				String lastMessage = "";
				if (m.size() > 1) {
					lastMessage = "[...] ";
				}

				if (m.size() > 0) {
					lastMessage += m.get(m.size() - 1);
					for (String s : m) {
						// Send string to irc
						bot.send(lastMessage);
					}
				}
			}
		}
	}
}
