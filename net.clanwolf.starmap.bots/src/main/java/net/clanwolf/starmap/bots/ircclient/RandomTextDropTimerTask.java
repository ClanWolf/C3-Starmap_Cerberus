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
package net.clanwolf.starmap.bots.ircclient;

import java.util.Random;
import java.util.TimerTask;

public class RandomTextDropTimerTask extends TimerTask {
	private IRCBot bot = null;

	public void setBot(IRCBot bot) {
		this.bot = bot;
	}

	@Override
	public void run() {
		if (bot != null) {
			String users = bot.getUserListString();
			if (users != null) {
				users = users.replaceAll("@Meldric\r\n", "");
				users = users.replaceAll("@Q\r\n", "");
				users = users.replaceAll("D\r\n", "");
				users = users.replaceAll("Ulric\r\n", "");
				users = users.replaceAll("Topic:.*\r\n", "");
				users = users.trim();

				if (!"".contentEquals(users)) {
					Random r = new Random();
					int randomInt = r.nextInt(25) + 1;
					if (randomInt <= 2) {
						if (IRCBot.dropDebugStrings) {
							bot.send("Users currently present (topics and admins stripped): #" + users + "#");
						}
						bot.dropRandomLine();
					}
				}
			}
		}
	}
}
