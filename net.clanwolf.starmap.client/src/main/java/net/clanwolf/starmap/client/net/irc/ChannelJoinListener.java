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
 * Copyright (c) 2001-2021, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.net.irc;

import com.ircclouds.irc.api.domain.messages.ChanJoinMessage;
import com.ircclouds.irc.api.domain.messages.NickMessage;
import com.ircclouds.irc.api.listeners.VariousMessageListenerAdapter;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.logging.C3Logger;

public class ChannelJoinListener extends VariousMessageListenerAdapter {
	@Override
	@SuppressWarnings("unused")
	public void onChannelJoin(ChanJoinMessage aMsg) {
		C3Logger.info("User " + aMsg.getSource().getNick() + " joined channel" + aMsg.getChannelName());
		ActionManager.getAction(ACTIONS.IRC_USER_JOINED).execute(aMsg);
	}

	@Override
	@SuppressWarnings("unused")
	public void onNickChange(NickMessage aMsg) {
		C3Logger.info("User " + aMsg.getSource().getNick() + " is now known as " + aMsg.getNewNick());
		NickChangeObject nco = new NickChangeObject();
		nco.setOldNick(aMsg.getSource().getNick());
		nco.setNewNick(aMsg.getNewNick());
		ActionManager.getAction(ACTIONS.IRC_USER_NICKCHANGE).execute(nco);
	}
}