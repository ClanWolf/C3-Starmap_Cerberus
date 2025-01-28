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
package net.clanwolf.starmap.client.gui.panes.chat;

import com.ircclouds.irc.api.domain.messages.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.gui.panes.TerminalCommandHandler;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ChatPaneController extends AbstractC3Controller implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private IRCClient ircClient;
	private List<String> userList = new ArrayList<>();
	private String selectedUser = "";
	private boolean initStarted = false;

	private static ListView<String> lvUsersExt;

	@FXML
	ListView<String> lvUsers;
	@FXML
	private TableView<ChatEntry> tableViewChat;

	public static ListView<String> getLvUsers() {
		return lvUsersExt;
	}

	public static void addChatLine(String chatUser, String chatText) {
		if (instance != null) {
			Platform.runLater(() -> {
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				final long currentTime = System.currentTimeMillis();
				final String chatTime = timeFormat.format(currentTime);

				String c = "";
				if (chatUser != null) {
					if (chatUser.equalsIgnoreCase("Ulric".toLowerCase())) {
						c = "-fx-background-color:#1d374b;";
					} else if (chatUser.contains("[" + Internationalization.getString("C3_IRC_Priv") + "]")) {
						c = "-fx-background-color:#125a2f;";
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
								entry = new ChatEntry(c, null, null, line);
							} else {
								entry = new ChatEntry(c, chatTime, chatUser, line);
							}
							instance.tableViewChat.getItems().add(entry);
							line = word + " ";
							firstLineDone = true;
						}
					}
					if (!line.isEmpty()) {
						if (firstLineDone) {
							entry = new ChatEntry(c, null, null, line);
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

	@Override
	public void setStrings() {

	}

	@Override
	public void setFocus() {
		Platform.runLater(() -> {
			//
		});
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_DESTROY_CURRENT, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_BEGINS, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGON_FINISHED_SUCCESSFULL, this);

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
		ActionManager.addActionCallbackListener(ACTIONS.IRC_DISCONNECT_NOW, this);

		// Added in AbstractC3Controller:
		// ActionManager.addActionCallbackListener(ACTIONS.ENABLE_DEFAULT_BUTTON, this);
		// ActionManager.addActionCallbackListener(ACTIONS.DISABLE_DEFAULT_BUTTON, this);
	}

	private static ChatPaneController instance = null;

	/**
	 * Handles actions.
	 *
	 * @param action incoming action to be handled
	 * @param o      the action object passed along with the action
	 * @return wether the handling should continue (this should be true in general)
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {

		if (o.getObject() instanceof UnknownMessage) {
			UnknownMessage msg2 = (UnknownMessage) o.getObject();
			// addChatLine("" + "[" + Internationalization.getString("C3_IRC_General") + "] ", msg2.asRaw());
		} else {
			switch (action) {
				case LOGON_FINISHED_SUCCESSFULL:
					ircClient = new IRCClient(); // Does NOT connect in constructor (!) - Connecting when pane is opened!
					break;

				case CHANGE_LANGUAGE:
					setStrings();
					break;

				case ENABLE_DEFAULT_BUTTON:
					enableDefaultButton(true);
					break;

				case DISABLE_DEFAULT_BUTTON:
					enableDefaultButton(false);
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

				case IRC_DISCONNECT_NOW:
					Platform.runLater(() -> {
						addChatLine(null, "**");
						addChatLine(null, "**");
						addChatLine(null, "** " + Internationalization.getString("C3_IRC_DisconnectedByC3Logout"));
						addChatLine(null, "**");
						addChatLine(null, "**");
						ActionManager.getAction(ACTIONS.HIDE_IRC_INDICATOR).execute();
					});
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
					String myOwnEntry = null;
					for (String s : list) {
						if (s.equals("C3\\" + Nexus.getCurrentUser().getUserName())) {
							myOwnEntry = s;
						}
					}
					list.remove(myOwnEntry);
					list.remove("@Q");
					list.remove("D");
					Collections.sort(list);
					String finalMyOwnEntry = myOwnEntry;

					Platform.runLater(() -> {
						String selectedItem = lvUsers.getSelectionModel().getSelectedItem();
						lvUsers.getItems().clear();
						lvUsers.getItems().add(finalMyOwnEntry);
						lvUsers.getItems().add("-----");
						lvUsers.getItems().addAll(list);
						lvUsers.getSelectionModel().clearSelection();
						if (selectedItem != null) {
							lvUsers.getSelectionModel().select(selectedItem);
						}
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
						if (o.getObject() instanceof ChannelPrivMsg msg2) {
							addChatLine(msg2.getSource().getNick() + "[" + Internationalization.getString("C3_IRC_General") + "] ", msg2.getText());
						}
						//						else if (o.getObject() instanceof UnknownMessage) {
						//							UnknownMessage msg2 = (UnknownMessage) o.getObject();
						//							addChatLine("" + "[" + Internationalization.getString("C3_IRC_General") + "] ", msg2.asRaw());
						//						}
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

				default:
					Collections.sort(lvUsers.getItems());
					break;
			}
		}
		return true;
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

	@FXML
	public void handleUserlistClick() {
		Platform.runLater(() -> {
			if (!lvUsers.getSelectionModel().getSelectedItems().isEmpty()) {
				if (lvUsers.getSelectionModel().getSelectedItems().get(0).equals(selectedUser)) {
					lvUsers.getSelectionModel().clearSelection();
					selectedUser = "";
				} else {
					selectedUser = (String) lvUsers.getSelectionModel().getSelectedItems().get(0);
				}
				if (!lvUsers.getSelectionModel().getSelectedItems().isEmpty()) {
					if (lvUsers.getSelectionModel().getSelectedItems().get(0).equals("C3\\" + Nexus.getCurrentUser().getUserName()) || lvUsers.getSelectionModel().getSelectedItems().get(0).equals("-----")) {
						lvUsers.getSelectionModel().clearSelection();
						selectedUser = "";
					}
				}
			}
		});
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

	private void init() {
		initStarted = true;
		tableViewChat.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		tableViewChat.getStyleClass().add("noheader");
		TableColumn<ChatEntry, String> chatTimeColumn = new TableColumn<>("");
		chatTimeColumn.setCellValueFactory(data -> data.getValue().getChatTime());
		chatTimeColumn.setPrefWidth(62);
		chatTimeColumn.setMaxWidth(65);
		chatTimeColumn.setMinWidth(60);
		TableColumn<ChatEntry, String> chatUserColumn = new TableColumn<>("");
		chatUserColumn.setCellValueFactory(data -> data.getValue().getChatUser());
		chatUserColumn.setPrefWidth(102);
		chatUserColumn.setMaxWidth(105);
		chatUserColumn.setMinWidth(100);
		TableColumn<ChatEntry, String> chatTextColumn = new TableColumn<>("");
		chatTextColumn.setCellValueFactory(data -> data.getValue().getChatText());
		//		chatTextColumn.setPrefWidth(250);
		tableViewChat.getColumns().addAll(chatTimeColumn, chatUserColumn, chatTextColumn);

		tableViewChat.setRowFactory(tableViewChat -> new TableRow<ChatEntry>() {
			@Override
			protected void updateItem(ChatEntry item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					String color = item.getColor().get();
					if (color != null && !color.isEmpty()) {
						setStyle(color);
					} else {
						setStyle("");
					}
				} else {
					setStyle("");
				}
//				setPrefHeight(24);
//				setMinHeight(24);
//				setMaxHeight(24);
			}
		});

		instance = this;
		tableViewChat.setVisible(true);

		// hide horizontal scrollbar

		for (Node n : tableViewChat.lookupAll(".scroll-bar:horizontal")) {
			if (n instanceof ScrollBar scrollBar) {
				scrollBar.setPrefHeight(0);
				scrollBar.setMaxHeight(0);
				scrollBar.setVisible(false);
				scrollBar.setOpacity(1);
				scrollBar.setStyle("-fx-background-color: transparent !important;");
			}
		}

		lvUsersExt = lvUsers;
		lvUsers.getSelectionModel().clearSelection();
		if (!IRCClient.connected) {
			addChatLine("", Internationalization.getString("C3_IRC_Connecting"));
		}
	}

	@Override
	public void warningOnAction() {
	}

	@Override
	public void warningOffAction() {
	}
}
