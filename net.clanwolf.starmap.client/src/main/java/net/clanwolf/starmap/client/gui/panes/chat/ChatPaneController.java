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

import com.ircclouds.irc.api.domain.messages.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.net.irc.IRCClient;
import net.clanwolf.starmap.client.net.irc.MessageActionObject;
import net.clanwolf.starmap.client.net.irc.NickChangeObject;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.logging.C3Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatPaneController extends AbstractC3Controller implements ActionCallBackListener {

	private IRCClient ircClient;
	private List<String> userList = new ArrayList<>();
	private final LinkedList<String> commandHistory = new LinkedList<>();
	private int commandHistoryIndex = 0;
	private String selectedUser = "";

	@FXML
	TextFlow textFlowChat;

	@FXML
	ScrollPane spChat;

	@FXML
	ListView<String> lvUsers;

	@FXML
	public void handleUserlistClick() {
		Platform.runLater(() -> {
			if (lvUsers.getSelectionModel().getSelectedItems().size() > 0) {
				if (lvUsers.getSelectionModel().getSelectedItems().get(0).equals(selectedUser)) {
					lvUsers.getSelectionModel().clearSelection();
					selectedUser = "";
				} else {
					selectedUser = (String) lvUsers.getSelectionModel().getSelectedItems().get(0);
				}
			}
		});
	}

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

		ActionManager.addActionCallbackListener(ACTIONS.IRC_CONNECTED, this);
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
		ActionManager.addActionCallbackListener(ACTIONS.IRC_CHANGE_NICK, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_SENDING_ACTION, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_UPDATED_USERLIST_RECEIVED, this);
	}

	private void init() {
		spChat.vvalueProperty().bind(textFlowChat.heightProperty());
	}

	private void handleCommand(String com) {
		boolean sendingString = true;

		if (!com.startsWith("*!!!*")) {
			if (!"".equals(com)) {
				C3Logger.info("Received command: '" + com + "'");
				commandHistory.add(com);
				commandHistoryIndex = commandHistory.size();
				if (commandHistory.size() > 50) {
					commandHistory.remove(0);
				}
			}
		}

		if ("*!!!*historyBack".equals(com)) {
			if (commandHistoryIndex > 0) {
				C3Logger.info("History back");
				commandHistoryIndex--;
				String histCom = commandHistory.get(commandHistoryIndex);
				ActionManager.getAction(ACTIONS.SET_TERMINAL_TEXT).execute(histCom);
			}
			sendingString = false;
		}

		if ("*!!!*historyForward".equals(com)) {
			if (commandHistoryIndex < commandHistory.size() - 1) {
				C3Logger.info("History forward");
				commandHistoryIndex++;
				String histCom = commandHistory.get(commandHistoryIndex);
				ActionManager.getAction(ACTIONS.SET_TERMINAL_TEXT).execute(histCom);
			}
			sendingString = false;
		}

		if (com.startsWith("/nick ")) {
			com = com.substring(6);
			if (!"".equals(com)) {
				NickChangeObject nco = new NickChangeObject();
				nco.setOldNick(IRCClient.myNick);
				nco.setNewNick(com);
				ActionManager.getAction(ACTIONS.IRC_CHANGE_NICK).execute(nco);
			}
			sendingString = false;
		}

//		if (com.startsWith("/names")) {
//			ActionManager.getAction(ACTIONS.IRC_GET_NAMELIST).execute();
//			sendingString = false;
//		}

		if (com.startsWith("/me ")) {
			com = com.substring(4);
			if (!"".equals(com)) {
				ActionManager.getAction(ACTIONS.IRC_SENDING_ACTION).execute(com);
			}
			sendingString = false;
		}

		if (sendingString) {
			if (!"".equals(com.trim())) {
				String color = "";
				C3Logger.info("Sending to IRC: " + com);
				MessageActionObject mo = new MessageActionObject();
				if (lvUsers.getSelectionModel().getSelectedItems().size() > 0) {
					String tar = lvUsers.getSelectionModel().getSelectedItems().get(0);
					if (tar.startsWith("@")
							|| tar.startsWith("+")
							|| tar.startsWith("-")
							|| tar.startsWith("!")
							|| tar.startsWith("<")) {
						tar = tar.substring(1);
					}
					mo.setTarget(tar);
					C3Logger.info("Private message to: " + lvUsers.getSelectionModel().getSelectedItems().get(0));
					color = "#99ff99";
					addText(color, IRCClient.myNick + " [" + Internationalization.getString("C3_IRC_Priv") + "]: " + com + System.getProperty("line.separator"));
				} else {
					color = "#edf2be";
					addText(color, IRCClient.myNick + ": " + com + System.getProperty("line.separator"));
				}
				mo.setSource("");
				mo.setMessage(com);


				ActionManager.getAction(ACTIONS.IRC_SEND_MESSAGE).execute(mo);
			}
		}
	}

	private void addText(String t) {
		addText("", t);
	}

	private void addText(String color, String t) {
		Platform.runLater(() -> {
			DateFormat timeFormat = new SimpleDateFormat("HH:mm");
			final long currentTime = System.currentTimeMillis();

			Text t1 = new Text();

			if (!"".equals(color)) {
				t1.setStyle("-fx-fill:" + color + ";");
			} else {
				t1.setStyle("-fx-fill:#81d4ee;");
			}

			t1.setText("[" + timeFormat.format(currentTime) + "] " + t);
			textFlowChat.getChildren().add(t1);
		});
	}

	private void removeUser(String userName) {
		Platform.runLater(() -> {
			Iterator<String> i = lvUsers.getItems().iterator();
			int c = 0;
			String s = null;
			boolean found = false;
			while (i.hasNext()) {
				s = (String) i.next();
				if (s.endsWith(userName) || s.startsWith(userName)) {
					found = true;
					break;
				}
				c++;
			}
			if (s != null && found) {
				lvUsers.getItems().remove(s);
			}
		});
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
			case LOGON_FINISHED_SUCCESSFULL:
				ircClient = new IRCClient(); // Connects in constructor
				break;

			case CHANGE_LANGUAGE:
				setStrings();
				break;

			case PANE_DESTROY_CURRENT:
				break;

			case PANE_CREATION_BEGINS:
				break;

			case PANE_CREATION_FINISHED:
				init();
				break;

			case IRC_CONNECTED:
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_IRC_Connected"));
				break;

			case IRC_USER_JOINED:
				Platform.runLater(() -> {
					ChanJoinMessage cjm = (ChanJoinMessage) o.getObject();
					if (!lvUsers.getItems().contains(cjm.getSource().getNick())) {
						lvUsers.getItems().add(cjm.getSource().getNick());
						addText("#aaaaaa", cjm.getSource().getNick() + " " + Internationalization.getString("C3_IRC_Joined") + " " + System.getProperty("line.separator"));
					}
				});
				break;

			case IRC_USER_LEFT:
				break;

			case IRC_ERROR:
				Platform.runLater(() -> {
					String eMsg = o.getText();
					addText("#ff0000", Internationalization.getString("C3_Speech_Failure") + " " + eMsg + System.getProperty("line.separator"));
				});
				break;

			case IRC_USER_PART:
				Platform.runLater(() -> {
					ChanPartMessage pmsg = (ChanPartMessage) o.getObject();
					removeUser(pmsg.getSource().getNick());
					addText("#aaaaaa", pmsg.getSource().getNick() + " " + Internationalization.getString("C3_IRC_Left") + " " + System.getProperty("line.separator"));
				});
				break;

			case IRC_USER_KICKED:
				Platform.runLater(() -> {
					ChannelKick kmsg = (ChannelKick) o.getObject();
					removeUser(kmsg.getKickedNickname());
					addText("#aaaaaa", kmsg.getKickedNickname() + " " + Internationalization.getString("C3_IRC_WasKicked") + " " + System.getProperty("line.separator"));
				});
				break;

			case IRC_USER_QUIT:
				Platform.runLater(() -> {
					QuitMessage qmsg = (QuitMessage) o.getObject();
					removeUser(qmsg.getSource().getNick());
					addText("#aaaaaa", qmsg.getSource().getNick() + " " + Internationalization.getString("C3_IRC_Quit") + " " + qmsg.getQuitMsg() + System.getProperty("line.separator"));
				});
				break;

			case IRC_USER_NICKCHANGE:
				Platform.runLater(() -> {
					NickChangeObject nco = (NickChangeObject) o.getObject();
					Iterator<String> i = lvUsers.getItems().iterator();
					int c = 0;
					String s = null;
					boolean found = false;
					while (i.hasNext()) {
						s = (String) i.next();
						if (s.endsWith(nco.getOldNick()) || s.startsWith(nco.getOldNick())) {
							s = s.replace(nco.getOldNick(), nco.getNewNick());
							found = true;
							break;
						}
						c++;
					}
					if (s != null && found) {
						lvUsers.getItems().set(c, s);
					}
					String v = Internationalization.getString("C3_IRC_IsNowKnownAs");
					addText("#aaaaaa", nco.getOldNick() + " " + v + " " + nco.getNewNick() + System.getProperty("line.separator"));
				});
				break;

			case IRC_UPDATED_USERLIST_RECEIVED:
				ArrayList<String> list = (ArrayList<String>) o.getObject();
				list.remove("@Q");
				list.remove("D");
				Platform.runLater(() -> {
					lvUsers.getItems().clear();
					lvUsers.getItems().addAll(list);
				});
				break;

			case IRC_MESSAGE_IN_PRIVATE:
				Platform.runLater(() -> {
					UserPrivMsg msg = (UserPrivMsg) o.getObject();
					addText("#99ff99", msg.getSource().getNick() + " [" + Internationalization.getString("C3_IRC_Priv") + "]: " + msg.getText() + System.getProperty("line.separator"));
				});
				break;

			case IRC_MESSAGE_IN_GENERAL:
				Platform.runLater(() -> {
					ChannelPrivMsg msg2 = (ChannelPrivMsg) o.getObject();
					addText("#99ff99", msg2.getSource().getNick() + "[" + Internationalization.getString("C3_IRC_General") + "]: " + msg2.getText() + System.getProperty("line.separator"));
				});
				break;

			case IRC_SENDING_ACTION:
				Platform.runLater(() -> {
					String com2 = o.getText();
					addText("#77ffee", IRCClient.myNick + " " + com2 + System.getProperty("line.separator"));
				});
				break;

			case IRC_CHANGE_NICK:
//				NickChangeObject nco2 = (NickChangeObject)o.getObject();
//				String v = Internationalization.getString("C3_IRC_IsNowKnownAs");
//				addText("#aaaaaa", nco2.getOldNick() + " " + v + " " + nco2.getNewNick() + System.getProperty("line.separator"));
				break;

			case IRC_MESSAGE_IN_CHANNEL:
				Platform.runLater(() -> {
					ChannelPrivMsg msg3 = (ChannelPrivMsg) o.getObject();
					if ("Ulric".equals(msg3.getSource().getNick())) {
						addText("#addae7", msg3.getSource().getNick() + ": " + msg3.getText() + System.getProperty("line.separator"));
					} else {
						addText(msg3.getSource().getNick() + ": " + msg3.getText() + System.getProperty("line.separator"));
					}
				});
				break;

			case TERMINAL_COMMAND:
				String com1 = o.getText();
				if (Nexus.getCurrentlyOpenedPane() instanceof ChatPane) {
					handleCommand(com1);
				}
				break;

			default:
				Collections.sort(lvUsers.getItems());
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