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
package net.clanwolf.starmap.client.net.irc;

import com.ircclouds.irc.api.domain.messages.ChanPartMessage;
import com.ircclouds.irc.api.domain.messages.ChannelKick;
import com.ircclouds.irc.api.domain.messages.QuitMessage;
import com.ircclouds.irc.api.listeners.VariousMessageListenerAdapter;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class ChannelLeaveListener extends VariousMessageListenerAdapter {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	@SuppressWarnings("unused")
	public void onChannelPart(ChanPartMessage aMsg) {
		logger.info("User " + aMsg.getSource().getNick() + " left channel" + aMsg.getChannelName());
		ActionManager.getAction(ACTIONS.IRC_USER_PART).execute(aMsg);
	}

	@Override
	@SuppressWarnings("unused")
	public void onChannelKick(ChannelKick aMsg) {
		logger.info("User " + aMsg.getSource().getNick() + " was kicked from channel" + aMsg.getChannelName());
		ActionManager.getAction(ACTIONS.IRC_USER_KICKED).execute(aMsg);
	}

	@Override
	@SuppressWarnings("unused")
	public void onUserQuit(QuitMessage aMsg) {
		logger.info("User " + aMsg.getSource().getNick() + " quit");
		ActionManager.getAction(ACTIONS.IRC_USER_QUIT).execute(aMsg);
	}
}
