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
package net.clanwolf.starmap.client.net;

import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.enums.C3MESSAGES;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;
import net.clanwolf.starmap.client.gui.messagepanes.C3Message;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.logout.Logout;
import net.clanwolf.starmap.client.util.Internationalization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.util.TimerTask;

/**
 * @author Undertaker
 *
 */
public class GameSessionHeartBeatTimer extends TimerTask {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public GameSessionHeartBeatTimer() {
		//
	}

	@Override
	public void run() {
		// Checking the last heartbeat (pong) from the server and see how old it is
		Timestamp tsServerHeartbeat = Nexus.getLastServerHeartbeatTimestamp();
		Timestamp tsNow = new Timestamp(System.currentTimeMillis());
		long diff = 0L;
		if (tsServerHeartbeat != null) {
			diff = (tsNow.getTime() - tsServerHeartbeat.getTime()) * 1000; // difference in seconds
		}

		if (diff > (diff * 60 * 6)) {
			// Server did not responded for longer than 6 Minutes
			// - may be offline
			// - connection could have been reseted

			logger.info("Server did not respond for at least 5 minutes!");
			ActionManager.getAction(ACTIONS.SERVER_CONNECTION_LOST).execute();

			return;
		}

		if (Nexus.isLoggedIn()) {
			logger.info("Sending keepalive signal to server (ping).");
			GameState heartbeatState = new GameState();
			heartbeatState.setMode(GAMESTATEMODES.SESSION_KEEPALIVE);
			heartbeatState.addObject(null);
			Nexus.fireNetworkEvent(heartbeatState);
		} else {
			logger.info("Not sending keepalive, because client is not logged in.");
		}
	}
}
