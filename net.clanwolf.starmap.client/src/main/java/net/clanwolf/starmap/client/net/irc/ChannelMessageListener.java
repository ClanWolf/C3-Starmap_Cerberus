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

import com.ircclouds.irc.api.domain.messages.*;
import com.ircclouds.irc.api.domain.messages.interfaces.IMessage;
import com.ircclouds.irc.api.listeners.VariousMessageListenerAdapter;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ChannelMessageListener extends VariousMessageListenerAdapter {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	@SuppressWarnings("unused")
	public void onMessage(IMessage msg) {
		logger.info("Message: (" + msg.getSource() + "): " + msg);
		ActionManager.getAction(ACTIONS.IRC_MESSAGE_IN_GENERAL).execute(msg);
	}

	@Override
	@SuppressWarnings("unused")
	public void onChannelMessage(ChannelPrivMsg msg) {
		logger.info("Channel message: (" + msg.getSource().getNick() + "): " + msg.getText());
		ActionManager.getAction(ACTIONS.IRC_MESSAGE_IN_CHANNEL).execute(msg);
	}

	@Override
	@SuppressWarnings("unused")
	public void onUserPrivMessage(UserPrivMsg msg) {
		logger.info("Private Message: (" + msg.getSource() + "): " + msg);
		ActionManager.getAction(ACTIONS.IRC_MESSAGE_IN_PRIVATE).execute(msg);
	}

	@Override
	@SuppressWarnings("unused")
	public void onUserNotice(UserNotice aMsg) {
		logger.info("User notice: " + aMsg.getText() + " (from: " + aMsg.getSource() + ")");
	}

	@Override
	@SuppressWarnings("unused")
	public void onServerNumericMessage(ServerNumericMessage aMsg) {
		logger.info("Server numeric message: [" + aMsg.getNumericCode() + "] " + aMsg.getText() + " (from: " + aMsg.getSource() + ")");
		if (aMsg.getNumericCode() == 353) {  // userlist from server
			String[] list = (aMsg.getText().split(":"))[1].split(" ");
			ArrayList<String> l = new ArrayList<String>(Arrays.asList(list));
			Collections.sort(l);
			ActionManager.getAction(ACTIONS.IRC_UPDATED_USERLIST_RECEIVED).execute(l);
		}
	}

	@Override
	@SuppressWarnings("unused")
	public void onServerNotice(ServerNotice aMsg) {
		logger.info("Server notice: " + aMsg.getText() + " (from: " + aMsg.getSource() + ")");
	}
}
