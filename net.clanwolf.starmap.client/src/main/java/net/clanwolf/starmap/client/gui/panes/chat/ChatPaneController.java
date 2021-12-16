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
package net.clanwolf.starmap.client.gui.panes.chat;

import com.ircclouds.irc.api.domain.messages.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatPaneController extends AbstractC3Controller implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private IRCClient ircClient;
	private List<String> userList = new ArrayList<>();
	private String selectedUser = "";
	private boolean initStarted = false;

	@FXML
	TableView<ChatEntry> tableViewChat;

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

	private static ChatPaneController instance = null;

	private void init() {
		initStarted = true;
		tableViewChat.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableViewChat.getStyleClass().add("noheader");
		TableColumn<ChatEntry, String> chatTimeColumn = new TableColumn<>("");
		chatTimeColumn.setCellValueFactory(data -> data.getValue().getChatTime());
		chatTimeColumn.setPrefWidth(70);
		chatTimeColumn.setMaxWidth(70);
		chatTimeColumn.setMinWidth(70);
		TableColumn<ChatEntry, String> chatUserColumn = new TableColumn<>("");
		chatUserColumn.setCellValueFactory(data -> data.getValue().getChatUser());
		chatUserColumn.setPrefWidth(100);
		chatUserColumn.setMaxWidth(100);
		chatUserColumn.setMinWidth(100);
		TableColumn<ChatEntry, String> chatTextColumn = new TableColumn<>("");
		chatTextColumn.setCellValueFactory(data -> data.getValue().getChatText());
//		chatTextColumn.setPrefWidth(250);
		tableViewChat.getColumns().addAll(  chatTimeColumn,
											chatUserColumn,
											chatTextColumn
		);

		tableViewChat.setRowFactory(tableViewChat -> new TableRow<ChatEntry>() {
			@Override
			protected void updateItem(ChatEntry item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					String color = item.getColor().get();
					if (color != null && !"".equals(color)) {
						setStyle(color);
					}
				} else {
					setStyle("");
				}
			}
		});

		instance = this;
		tableViewChat.setVisible(true);
		if (!IRCClient.connected) {
			addChatLine("", Internationalization.getString("C3_IRC_Connecting"));
		}
	}

	private void handleCommand(String com) {
		boolean sendingString = true;

		if (!com.startsWith("*!!!*")) {
			if (!"".equals(com)) {
				logger.info("Received command: '" + com + "'");
				Nexus.commandHistory.add(com);
				if (Nexus.commandHistory.size() > 50) {
					Nexus.commandHistory.remove(0);
				}
				Nexus.commandHistoryIndex = Nexus.commandHistory.size();
			}
		}

		if ("*!!!*historyBack".equals(com)) {
			if (Nexus.commandHistoryIndex > 0) {
				Nexus.commandHistoryIndex--;
				logger.info("History back to index: " + Nexus.commandHistoryIndex);
				String histCom = Nexus.commandHistory.get(Nexus.commandHistoryIndex);
				ActionManager.getAction(ACTIONS.SET_TERMINAL_TEXT).execute(histCom);
			}
			sendingString = false;
		}

		if ("*!!!*historyForward".equals(com)) {
			if (Nexus.commandHistoryIndex < Nexus.commandHistory.size() - 1) {
				Nexus.commandHistoryIndex++;
				logger.info("History forward to index: " + Nexus.commandHistoryIndex);
				String histCom = Nexus.commandHistory.get(Nexus.commandHistoryIndex);
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
			Nexus.storeCommandHistory();
		}

//		if (com.startsWith("/names")) {
//			ActionManager.getAction(ACTIONS.IRC_GET_NAMELIST).execute();
//			sendingString = false;
//  		Nexus.storeCommandHistory();
//		}

		if (com.startsWith("/me ")) {
			com = com.substring(4);
			if (!"".equals(com)) {
				ActionManager.getAction(ACTIONS.IRC_SENDING_ACTION).execute(com);
			}
			sendingString = false;
			Nexus.storeCommandHistory();
		}

		if (sendingString) {
			if (!"".equals(com.trim())) {
				String color = "";
				logger.info("Sending to IRC: " + com);
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
					logger.info("Private message to: " + lvUsers.getSelectionModel().getSelectedItems().get(0));
					addChatLine(IRCClient.myNick + " [" + Internationalization.getString("C3_IRC_Priv") + "] ", "(-> " + tar + ") " + com);
				} else {
					addChatLine(IRCClient.myNick + " ", com);
				}
				mo.setSource("");
				mo.setMessage(com);
				ActionManager.getAction(ACTIONS.IRC_SEND_MESSAGE).execute(mo);
				Nexus.storeCommandHistory();
			}
		}
	}

	public static void autoResizeColumns(TableView<?> table) {
		TableColumn<?, ?> column = table.getColumns().get(1);
		Text t;
		double max = 5.0d;
		for (int i = 0; i < table.getItems().size(); i++) {
			if (column.getCellData(i) != null) {
				t = new Text(column.getCellData(i).toString());
				double calcwidth = t.getLayoutBounds().getWidth();
				if (calcwidth > max) {
					max = calcwidth;
				}
			}
		}
		column.setPrefWidth(max + 30.0d);
		column.setMaxWidth(max + 30.0d);
		column.setMinWidth(max + 30.0d);
	}

	public static void addChatLine(String chatUser, String chatText) {
		if (instance != null) {
			Platform.runLater(() -> {
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				final long currentTime = System.currentTimeMillis();
				final String chatTime = "" + timeFormat.format(currentTime);

				String c = "";
				if (chatUser != null) {
					if (chatUser.equalsIgnoreCase("Ulric".toLowerCase())) {
						c = "-fx-background-color: #1d374b;";
					} else if (chatUser.contains("[" + Internationalization.getString("C3_IRC_Priv") + "]")) {
						c = "-fx-background-color: #294d69;";
					}
				}

				if (chatText.contains(" ")) {
					boolean firstLineDone = false;
					String line = "";
					String[] words = chatText.split(" ");
					ChatEntry entry;
					for (String word : words) {
						if (line.length() + word.length() + 2 < 55) {
							line = line + word + " ";
						} else {
							if (firstLineDone) {
								entry = new ChatEntry(c,null, null, line);
							} else {
								entry = new ChatEntry(c, chatTime, chatUser, line);
							}
							instance.tableViewChat.getItems().add(entry);
							line = word + " ";
							firstLineDone = true;
						}
					}
					if (!"".equals(line)) {
						if (firstLineDone) {
							entry = new ChatEntry(c,null, null, line);
						} else {
							entry = new ChatEntry(c, chatTime, chatUser, line);
						}
						instance.tableViewChat.getItems().add(entry);
					}
				} else {
					ChatEntry entry = new ChatEntry(c, chatTime, chatUser, chatText);
					instance.tableViewChat.getItems().add(entry);
				}
				instance.tableViewChat.scrollTo(instance.tableViewChat.getItems().size());
				ActionManager.getAction(ACTIONS.SHOW_IRC_INDICATOR).execute();
			});
		}
	}

	private void removeUser(String userName) {
		Platform.runLater(() -> {
			Iterator<String> i = lvUsers.getItems().iterator();
			int c = 0;
			String s = null;
			boolean found = false;
			while (i.hasNext()) {
				s = i.next();
//				if (s.endsWith(userName) || s.startsWith(userName)) {
				if (s.replace("@", "").equals(userName)) {
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
				ircClient = new IRCClient(); // Does NOT connect in constructor (!) - Connecting when pane is opened!
				break;

			case CHANGE_LANGUAGE:
				setStrings();
				break;

			case PANE_DESTROY_CURRENT:
				break;

			case PANE_CREATION_BEGINS:
				break;

			case PANE_CREATION_FINISHED:
				if (o.getObject().getClass() == ChatPane.class) {
					logger.info("Chat window opened.");
					if (ircClient != null && !IRCClient.connected) {
						logger.info("Connecting to IRC...");
						ircClient.connect();
					}
					if (ircClient != null && !initStarted) {
						logger.info("Initializing IRC panel.");
						init();
					}
					ActionManager.getAction(ACTIONS.HIDE_IRC_INDICATOR).execute();
//					ActionManager.getAction(ACTIONS.SHOW_IRC_INDICATOR).execute();
				}
				break;

			case IRC_CONNECTED:
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_IRC_Connected"));
				break;

			case IRC_USER_JOINED:
				Platform.runLater(() -> {
					ChanJoinMessage cjm = (ChanJoinMessage) o.getObject();
					if (!lvUsers.getItems().contains(cjm.getSource().getNick())) {
						lvUsers.getItems().add(cjm.getSource().getNick());
						addChatLine(null, cjm.getSource().getNick() + " " + Internationalization.getString("C3_IRC_Joined"));
					}
				});
				break;

			case IRC_USER_LEFT:
				break;

			case IRC_ERROR:
				Platform.runLater(() -> {
					String eMsg = o.getText();
					addChatLine(null, Internationalization.getString("C3_Speech_Failure") + " " + eMsg);
				});
				break;

			case IRC_USER_PART:
				Platform.runLater(() -> {
					ChanPartMessage pmsg = (ChanPartMessage) o.getObject();
					removeUser(pmsg.getSource().getNick());
					addChatLine(null, pmsg.getSource().getNick() + " " + Internationalization.getString("C3_IRC_Left"));
				});
				break;

			case IRC_USER_KICKED:
				Platform.runLater(() -> {
					ChannelKick kmsg = (ChannelKick) o.getObject();
					removeUser(kmsg.getKickedNickname());
					addChatLine(null, kmsg.getKickedNickname() + " " + Internationalization.getString("C3_IRC_WasKicked"));
				});
				break;

			case IRC_USER_QUIT:
				Platform.runLater(() -> {
					QuitMessage qmsg = (QuitMessage) o.getObject();
					removeUser(qmsg.getSource().getNick());
					addChatLine(null, qmsg.getSource().getNick() + " " + Internationalization.getString("C3_IRC_Quit") + " " + qmsg.getQuitMsg());
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
					addChatLine(null, nco.getOldNick() + " " + v + " " + nco.getNewNick());
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
					addChatLine(msg.getSource().getNick() + " [" + Internationalization.getString("C3_IRC_Priv") + "] ", "(-> " + msg.getToUser() + ") " + msg.getText());
				});
				break;

			case IRC_MESSAGE_IN_GENERAL:
				Platform.runLater(() -> {
					ChannelPrivMsg msg2 = (ChannelPrivMsg) o.getObject();
					addChatLine(msg2.getSource().getNick() + "[" + Internationalization.getString("C3_IRC_General") + "] ", msg2.getText());
				});
				break;

			case IRC_SENDING_ACTION:
				Platform.runLater(() -> {
					String com2 = o.getText();
					addChatLine(IRCClient.myNick, com2);
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
					addChatLine(msg3.getSource().getNick(), msg3.getText());
				});
				break;

			case TERMINAL_COMMAND:
				String com1 = o.getText();
				if (Nexus.isLoggedIn()) {
					if (Nexus.getCurrentlyOpenedPane() instanceof ChatPane) {
						handleCommand(com1);
					}
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
