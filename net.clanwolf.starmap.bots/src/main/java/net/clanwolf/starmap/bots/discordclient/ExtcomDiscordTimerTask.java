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
package net.clanwolf.starmap.bots.discordclient;

import net.clanwolf.starmap.bots.Bots;
import net.clanwolf.starmap.bots.db.ExtcomMonitor;
import net.clanwolf.starmap.bots.ircclient.IRCBot;
import net.dv8tion.jda.api.JDA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.TimerTask;

/**
 * @author Meldric
 */
public class ExtcomDiscordTimerTask extends TimerTask {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	public static DiscordBot bot = null;

	public ExtcomDiscordTimerTask() {
	}

	public void setBot(DiscordBot bot) {
		ExtcomDiscordTimerTask.bot = bot;
	}

	@Override
	public void run() {
		try {
			if (bot != null && DiscordBot.jda.getStatus().isInit()) {
				LinkedList<String> msgs = ExtcomMonitor.getMessages(Bots.DiscordBot, "de");

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
						logger.info(s);

						if (!s.startsWith("@@@DISCORD-CMD:")) {
							DiscordBot.sendMessageToChannel(s);
						} else {
							if (s.startsWith("@@@DISCORD-CMD:CREATE_ATTACK_THREAD@@@")) {
								// Message in Status:   "Carse wird in Runde 41 von "Steelserpent" (CSV) angegriffen!"
								// Name of Thread:      "[R36] DC ⚔ Grumium (FRR)"
								// Tags:                "Runde"
								String[] commandParts = s.split("@@@");
								if (commandParts.length == 8) {
									String command = commandParts[1];
									String season = commandParts[2];
									String round = commandParts[3];
									String systemName = commandParts[4];
									String attackerShortname = commandParts[5];
									String defenderShortname = commandParts[6];
									String attackId = commandParts[7];

									int randNum = (int)((Math.random()) * 324 + 100000);

									String header =  "[S" + season + "R" + round + "] " + attackerShortname + " ⚔ " + systemName + " ("+ defenderShortname + ")";
									String msg = "";
									//msg += attackerShortname + " greift " + systemName + " ("+ defenderShortname + ") an.\n";
									msg += "https://www.clanwolf.net/apps/C3/seasonhistory/S1/AttackImages/C3_S" + season + "_R" + round + "_" + attackId + ".png?" + randNum;
									DiscordBot.createAttackThread(header, msg, season, round);
								} else {
									DiscordBot.sendMessageToChannel("Error while inserting attack thread! Check log!");
									DiscordBot.sendMessageToChannel("Length: " + commandParts.length);
									DiscordBot.sendMessageToChannel("0: " + commandParts[0]);
									DiscordBot.sendMessageToChannel("1: " + commandParts[1]);
									DiscordBot.sendMessageToChannel("2: " + commandParts[2]);
									DiscordBot.sendMessageToChannel("3: " + commandParts[3]);
									DiscordBot.sendMessageToChannel("4: " + commandParts[4]);
									DiscordBot.sendMessageToChannel("5: " + commandParts[5]);
									DiscordBot.sendMessageToChannel("6: " + commandParts[6]);
									DiscordBot.sendMessageToChannel("7: " + commandParts[7]);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in Discord bot message pickup", e);
		}
	}
}
