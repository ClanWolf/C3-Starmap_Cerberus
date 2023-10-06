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
package net.clanwolf.starmap.client.gui.panes;

import javafx.scene.control.ListView;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.StatusTextEntryActionObject;
import net.clanwolf.starmap.client.enums.C3MESSAGES;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;
import net.clanwolf.starmap.client.gui.messagepanes.C3Message;
import net.clanwolf.starmap.client.gui.panes.chat.ChatPaneController;
import net.clanwolf.starmap.client.net.irc.IRCClient;
import net.clanwolf.starmap.client.net.irc.MessageActionObject;
import net.clanwolf.starmap.client.net.irc.NickChangeObject;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import net.clanwolf.starmap.client.process.universe.BOJumpship;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.enums.MEDALS;
import net.clanwolf.starmap.transfer.enums.POPUPS;
import net.clanwolf.starmap.transfer.enums.PRIVILEGES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static net.clanwolf.starmap.client.gui.panes.chat.ChatPaneController.addChatLine;

public class TerminalCommandHandler {

	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static synchronized void handleCommand(String com) {
		String currentPane = "";
		if (Nexus.getCurrentlyOpenedPane() != null) {
			currentPane = Nexus.getCurrentlyOpenedPane().getPaneName();
		}

		boolean isGodAdmin = Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN);
		boolean sendingString = true;
		boolean mapPaneSpecificCommand = false;
		boolean chatPaneSpecificCommand = false;

		// ALL COMMANDS:
		// - "!find "
		// - "!create universe"
		// - "!finalize round"
		// - "!reset attack <planetname>"
		// - "/nick <new nick>"
		// - "/me <text>"
		// - "!restart server"
		// - "!test popup"
		// - "!test medal"
		// - "!test error"

		if (com.toLowerCase().startsWith("!find")
				|| com.toLowerCase().startsWith("!create universe")
				|| com.toLowerCase().startsWith("!finalize round")
				|| com.toLowerCase().startsWith("!reset attack")
		) {
			sendingString = false;
			mapPaneSpecificCommand = true;
		}

		if (com.toLowerCase().startsWith("/nick")
				|| com.toLowerCase().startsWith("/me")
		) {
			sendingString = false;
			chatPaneSpecificCommand = true;
		}

		if (Nexus.isLoggedIn()) {
			if (!com.startsWith("*!!!*")) {
				if (!com.isEmpty()) {
					logger.info("Received command: '" + com + "' (CurrentPane: " + currentPane + ")");
					String lastEntry = null;
					if (!Nexus.commandHistory.isEmpty()) {
						lastEntry = Nexus.commandHistory.getLast();
					}
					if (lastEntry == null) {
						Nexus.commandHistory.add(com);
					} else if (!Nexus.commandHistory.getLast().equals(com)) {
						Nexus.commandHistory.add(com);
					}
					if (Nexus.commandHistory.size() > 50) {
						Nexus.commandHistory.remove(0);
					}
					Nexus.commandHistoryIndex = Nexus.commandHistory.size();
				}
			}

			if ("*!!!*historyBack".equals(com)) {
				if (Nexus.commandHistoryIndex > 0) {
					Nexus.commandHistoryIndex--;
					// logger.info("History back to index: " + Nexus.commandHistoryIndex);
					String histCom = Nexus.commandHistory.get(Nexus.commandHistoryIndex);
					ActionManager.getAction(ACTIONS.SET_TERMINAL_TEXT).execute(histCom);
					sendingString = false;
				}
			}

			if ("*!!!*historyForward".equals(com)) {
				if (Nexus.commandHistoryIndex < Nexus.commandHistory.size() - 1) {
					Nexus.commandHistoryIndex++;
					// logger.info("History forward to index: " + Nexus.commandHistoryIndex);
					String histCom = Nexus.commandHistory.get(Nexus.commandHistoryIndex);
					ActionManager.getAction(ACTIONS.SET_TERMINAL_TEXT).execute(histCom);
					sendingString = false;
				}
			}

			if (!com.startsWith("*!!!*") && !com.startsWith("/")) {
				ActionManager.getAction(ACTIONS.ADD_CONSOLE_LINE).execute(com);
			}

			if (currentPane.equals("MapPane")) {
				// ---------------------------------
				// find
				// ---------------------------------
				if (com.toLowerCase().startsWith("!find ")) {
					String value = com.substring(5);
					if (!value.isEmpty()) {
						logger.info("Searching for '" + value + "'");
					}
					logger.info("Searching starsystems...");
					for (BOStarSystem ss : Nexus.getBoUniverse().starSystemBOs.values()) {
						if (ss.getName().equalsIgnoreCase(value)) {
							logger.info("Found starsystem '" + value + "'");
							ActionManager.getAction(ACTIONS.MAP_MOVE_TO_STARSYSTEM).execute(ss);
						}
					}
					logger.info("Searching jumpships...");
					for (BOJumpship js : Nexus.getBoUniverse().jumpshipBOs.values()) {
						if (js.getJumpshipName().equalsIgnoreCase(value)) {
							logger.info("Found jumpship '" + value + "'");
							ActionManager.getAction(ACTIONS.MAP_MOVE_TO_JUMPSHIP).execute(js);
						}
					}
					Nexus.storeCommandHistory();
					sendingString = false;
				}

				// ---------------------------------
				// create universe
				// ---------------------------------
				if (com.toLowerCase().startsWith("!create universe")) {
					if (isGodAdmin) {
						ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_success"), false));
						GameState s = new GameState();
						s.setMode(GAMESTATEMODES.FORCE_NEW_UNIVERSE);
						Nexus.fireNetworkEvent(s);
						Nexus.storeCommandHistory();
					} else {
						ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_notallowed"), false));
						C3Message message = new C3Message(C3MESSAGES.ERROR_NOT_ALLOWED);
						message.setType(C3MESSAGETYPES.CLOSE);
						message.setText(Internationalization.getString("general_notallowed"));
						C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
						ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
					}
					sendingString = false;
				}

				// ---------------------------------
				// force finalize round
				// ---------------------------------
				if (com.toLowerCase().startsWith("!finalize round")) {
					if (Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_FINALIZE_ROUND)) {

						// Check if there are any routepoints that have not been saved yet!
						boolean unsavedRoutesFound = false;
						for (BOJumpship js : Nexus.getBoUniverse().getJumpshipList()) {
							if (js.getJumpshipFaction() == Nexus.getCurrentUser().getCurrentCharacter().getFactionId()) {
								// My own jumpship. May have been moved and may have an unsaved route

								if (js.routeLines != null) {
									if (!js.routeLines.getChildren().isEmpty()) {
										logger.info("There are routepoints to store.");
										unsavedRoutesFound = true;
									} else {
										logger.info("NO routepoints.");
									}
								}
							}
						}

						if (unsavedRoutesFound) {
							// stop action here, save routes first!
							ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_saveRoutesBeforeFinalizeRound"), true));
						} else {
							ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_command_has_been_disabled"), true));
//							ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_success"), false));
//							GameState s = new GameState();
//							s.setMode(GAMESTATEMODES.FORCE_FINALIZE_ROUND);
//							Nexus.fireNetworkEvent(s);
							Nexus.storeCommandHistory();
						}
					} else {
						ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_notallowed"), false));
						C3Message message = new C3Message(C3MESSAGES.ERROR_NOT_ALLOWED);
						message.setType(C3MESSAGETYPES.CLOSE);
						message.setText(Internationalization.getString("general_notallowed"));
						C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
						ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
					}
					// Nexus.storeCommandHistory(); // do not store to prevent accidental sending of this
					sendingString = false;
				}

				// ---------------------------------
				// reset attack
				// ---------------------------------
				if (com.toLowerCase().startsWith("!reset attack")) {
					if (isGodAdmin) {
						String systemNameOfAttack = com.substring(com.lastIndexOf(" "));
						boolean foundAttack = false;
						for (BOAttack at : Nexus.getBoUniverse().attackBOsOpenInThisRound.values()) {
							if (at.getStarSystemName().equals(systemNameOfAttack)) {
								GameState s = new GameState();
								s.setMode(GAMESTATEMODES.RESET_FIGHT);
								s.addObject(at.getAttackDTO().getId());
								Nexus.fireNetworkEvent(s);
								Nexus.storeCommandHistory();

								foundAttack = true;
								ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_success"), false));
								break;
							}
						}
						if (!foundAttack) {
							ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_failure"), true));
						}
					} else {
						ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_notallowed"), false));
						C3Message message = new C3Message(C3MESSAGES.ERROR_NOT_ALLOWED);
						message.setType(C3MESSAGETYPES.CLOSE);
						message.setText(Internationalization.getString("general_notallowed"));
						C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
						ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
					}
					sendingString = false;
				}
			}

			if (currentPane.equals("ChatPane")) {
				if (com.startsWith("/nick ")) {
					com = com.substring(6);
					if (!com.isEmpty()) {
						NickChangeObject nco = new NickChangeObject();
						nco.setOldNick(IRCClient.myNick);
						nco.setNewNick(com);
						ActionManager.getAction(ACTIONS.IRC_CHANGE_NICK).execute(nco);
					}
					sendingString = false;
					Nexus.storeCommandHistory();
				}

				if (com.startsWith("/me ")) {
					com = com.substring(4);
					if (!com.isEmpty()) {
						ActionManager.getAction(ACTIONS.IRC_SENDING_ACTION).execute(com);
					}
					sendingString = false;
					Nexus.storeCommandHistory();
				}

				if (sendingString) {
					if (!com.trim().isEmpty()) {
						ListView<String> lvUsers = ChatPaneController.getLvUsers();

						logger.info("Sending to IRC: " + com);
						MessageActionObject mo = new MessageActionObject();
						if (!lvUsers.getSelectionModel().getSelectedItems().isEmpty()) {
							String tar = lvUsers.getSelectionModel().getSelectedItems().get(0);
							if (tar.startsWith("@") || tar.startsWith("+") || tar.startsWith("-") || tar.startsWith("!") || tar.startsWith("<")) {
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

			// Not specific to any pane commands

			// ---------------------------------
			// restart server
			// ---------------------------------
			if (com.toLowerCase().startsWith("!restart server")) {
				if (isGodAdmin) {
//					ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_command_has_been_disabled"), true));
					ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_success"), false));
					GameState s = new GameState();
					s.setMode(GAMESTATEMODES.RESTART_SERVER);
					Nexus.fireNetworkEvent(s);
					// Nexus.storeCommandHistory(); // do not store to prevent accidental sending of this
				} else {
					ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_notallowed"), false));
					C3Message message = new C3Message(C3MESSAGES.ERROR_NOT_ALLOWED);
					message.setType(C3MESSAGETYPES.CLOSE);
					message.setText(Internationalization.getString("general_notallowed"));
					C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
					ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
				}
			}

			// Terminal commands with "test (something)" end up here (if godadmin)
			if (isGodAdmin) {
				// ---------------------------------
				// tests
				// ---------------------------------
				if (com.toLowerCase().startsWith("!test popup")) {
					ActionManager.getAction(ACTIONS.SHOW_POPUP).execute(POPUPS.Orders_Confirmed);
					Nexus.storeCommandHistory();
				}

				if (com.toLowerCase().startsWith("!test medal")) {
					ActionManager.getAction(ACTIONS.SHOW_MEDAL).execute(MEDALS.First_Blood);
					Nexus.storeCommandHistory();
				}

				if (com.toLowerCase().startsWith("!test error")) {
					C3Message m = new C3Message(C3MESSAGES.WARNING_BLACKBOX_TEAMS_INVALID);
					m.setType(C3MESSAGETYPES.CLOSE);
					m.setText("Teams seem to be invalid!");
					ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(m);
				}
			}

			if (!currentPane.equals("MapPane") && mapPaneSpecificCommand) {
				StatusTextEntryActionObject ste = new StatusTextEntryActionObject(Internationalization.getString("general_only_works_on_map"), true);
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(ste);
			}

			if (!currentPane.equals("ChatPane") && chatPaneSpecificCommand) {
				StatusTextEntryActionObject ste = new StatusTextEntryActionObject(Internationalization.getString("general_only_works_on_chat"), true);
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(ste);
			}
		} else {
			logger.info("You need to be logged in to use the terminal.");
			ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_need_login"), false));
		}
	}
}
