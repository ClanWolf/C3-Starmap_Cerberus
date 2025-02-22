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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.bots.ircclient;

import net.clanwolf.starmap.bots.Bots;
import net.clanwolf.starmap.bots.db.ExtcomMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.TimerTask;

/**
 * @author Meldric
 */
public class ExtcomIRCTimerTask extends TimerTask {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static IRCBot bot = null;

	public ExtcomIRCTimerTask() {
	}

	public void setBot(IRCBot bot) {
		ExtcomIRCTimerTask.bot = bot;
	}

	@Override
	public void run() {
		try {
			//			logger.info("bot: " + (bot == null ? "null" : bot.getUserListString()));
			//			logger.info("Connected: " + bot.connected);

			if (bot != null && IRCBot.connected) {
				//				logger.info("Requesting new entries from ext_com table");

				LinkedList<String> msgs = ExtcomMonitor.getMessages(Bots.IRCBot, IRCBot.getLang());

				String[] msgscut = {"...", "...", "...", "...", "..."};

				for (int i = 4; i >= 0; i--) {
					try {
						msgscut[i] = msgs.getLast();
						msgs.removeLast();
					} catch (NoSuchElementException nsee) {
						break;
					}
				}
				for (String s : msgscut) {
					if (!"...".equalsIgnoreCase(s)) {
						if (!s.startsWith("@@@DISCORD-CMD:")) {
							if (s.contains(" ⚔ ")) {
								s = s.replace(" - <", " - ");
								s = s.replace(">", "");
							}
							bot.send(s);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in IRCBot message pickup", e);
		}
	}
}
