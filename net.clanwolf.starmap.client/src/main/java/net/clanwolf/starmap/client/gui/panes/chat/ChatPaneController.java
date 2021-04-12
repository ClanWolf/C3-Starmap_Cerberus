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
package net.clanwolf.starmap.client.gui.panes.chat;

import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.net.irc.IRCClient;
import net.clanwolf.starmap.client.net.irc.MessageActionObject;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.logging.C3Logger;

import java.util.ArrayList;
import java.util.List;

public class ChatPaneController extends AbstractC3Controller implements ActionCallBackListener {

	private IRCClient ircClient;
	private List<String> userList = new ArrayList<>();

	@Override
	public void setStrings() {

	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_DESTROY_CURRENT, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_BEGINS, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGON_FINISHED_SUCCESSFULL, this);
		ActionManager.addActionCallbackListener(ACTIONS.TERMINAL_COMMAND, this);

		ActionManager.addActionCallbackListener(ACTIONS.IRC_USER_JOINED, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_USER_LEFT, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_ERROR, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_USER_JOINED, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_USER_PART, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_USER_KICKED, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_USER_QUIT, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_USER_NICKCHANGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_MESSAGE_IN_GENERAL, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_MESSAGE_IN_CHANNEL, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_MESSAGE_IN_PRIVATE, this);
	}

	private void handleCommand(String com) {
		C3Logger.info("Sending to IRC: " + com);
		MessageActionObject mo = new MessageActionObject();
		// TODO: Is there a different target?
		mo.setSource("");
		mo.setTarget("");
		mo.setMessage(com);
		ActionManager.getAction(ACTIONS.IRC_SEND_MESSAGE).execute(mo);
	}

	/**
	 * Handles actions.
	 *
	 * @param action incoming action to be handled
	 * @param o      the action object passed along with the action
	 * @return wether the handling should continue (this should be true in general)
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case CHANGE_LANGUAGE:
				setStrings();
				break;

			case PANE_DESTROY_CURRENT:
				break;

			case PANE_CREATION_BEGINS:
				break;

			case PANE_CREATION_FINISHED:
				break;

			case LOGON_FINISHED_SUCCESSFULL:
				ircClient = new IRCClient(); // Connects in constructor
				break;

			case IRC_SEND_MESSAGE:

				break;

			case IRC_USER_JOINED:

				break;

			case IRC_USER_LEFT:

				break;

			case IRC_ERROR:

				break;

			case IRC_USER_PART:

				break;

			case IRC_USER_KICKED:

				break;

			case IRC_USER_QUIT:

				break;

			case IRC_USER_NICKCHANGE:

				break;

			case IRC_MESSAGE_IN_PRIVATE:

				break;

			case IRC_MESSAGE_IN_GENERAL:

				break;

			case IRC_MESSAGE_IN_CHANNEL:

				break;

			case TERMINAL_COMMAND:
				String com = o.getText();
				if (Nexus.getCurrentlyOpenedPane() instanceof ChatPane) {
					handleCommand(com);
				}
				break;

			default:
				break;
		}
		return true;
	}

	@Override
	public void warningOnAction() {
	}

	@Override
	public void warningOffAction() {
	}
}
