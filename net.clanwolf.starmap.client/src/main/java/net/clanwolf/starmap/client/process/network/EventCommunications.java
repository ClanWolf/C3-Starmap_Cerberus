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
package net.clanwolf.starmap.client.process.network;

import io.nadron.client.app.Session;
import io.nadron.client.event.Event;
import io.nadron.client.event.Events;
import io.nadron.client.event.NetworkEvent;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.enums.C3MESSAGES;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;
import net.clanwolf.starmap.client.gui.messagepanes.C3Message;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.logout.Logout;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.util.Compressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Christian
 */
public class EventCommunications {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static Boolean mwoStatsSaved = null;
	private static Boolean attackStatsSaved = null;
	private static Boolean charStatsSaved = null;

	public static void onDataIn(Session session, Event event) {
		logger.info("EventCommunications.onDataIn: " + event.getType());

		if (event.getSource() instanceof GameState state) {
			// logger.info("Event received: " + state.getMode());

			showErrorMessage(state);

			switch (state.getMode()) {
				case FOUND_BROKEN_ATTACK:
					logger.info("Broken attack was found (drops started but one or both dropleads are offline)");
					if (state.getObject() != null && Nexus.getCurrentAttackOfUser() != null && ((Long) state.getObject()).equals(Nexus.getCurrentAttackOfUser().getAttackDTO().getId())) {
						// this is the attack I am in currently
						logger.info("My attack is broken");
						Long timerStartMillis = (Long) state.getObject2();
						ActionManager.getAction(ACTIONS.CURRENT_ATTACK_IS_BROKEN).execute(timerStartMillis);
					}
					break;

				case BROKEN_ATTACK_KILL_FIVE_MINUTE_WARNING:
					if (state.getObject() != null && Nexus.getCurrentAttackOfUser() != null && ((Long) state.getObject()).equals(Nexus.getCurrentAttackOfUser().getAttackDTO().getId())) {
						// this is the attack I am in currently
						logger.info("My broken attack will be killed by the server, FIVE MINUTE WARNING");
						Long timerStartMillis = (Long) state.getObject2();
						ActionManager.getAction(ACTIONS.CURRENT_ATTACK_IS_BROKEN_WARNING).execute(timerStartMillis);
					}
					break;

				case BROKEN_ATTACK_KILL_AFTER_TIMEOUT:
					// My attack will be killed, because a droplead went missing and did not reconnect after 10 mins
					if (state.getObject() != null && Nexus.getCurrentAttackOfUser() != null && ((Long) state.getObject()).equals(Nexus.getCurrentAttackOfUser().getAttackDTO().getId())) {
						// this is the attack I am in currently
						logger.info("My broken attack was killed by the server");
						Long timerStartMillis = (Long) state.getObject2();
						ActionManager.getAction(ACTIONS.CURRENT_ATTACK_IS_BROKEN_KILLED).execute(timerStartMillis);
					}
					break;

				case BROKEN_ATTACK_HEALED:
					// My attack was healed, dropleads did reconnect
					if (state.getObject() != null && Nexus.getCurrentAttackOfUser() != null && ((Long) state.getObject()).equals(Nexus.getCurrentAttackOfUser().getAttackDTO().getId())) {
						// this is the attack I am in currently
						logger.info("My attack was healed");
						ActionManager.getAction(ACTIONS.CURRENT_ATTACK_IS_HEALED).execute();
					}
					break;

				case USER_GET_NEW_PLAYERLIST:
					logger.info("EventCommunications.onDataIn: neue Playerliste ->" + state.getObject());

					@SuppressWarnings("unchecked") ArrayList<UserDTO> userList = (ArrayList<UserDTO>) state.getObject();

					for (UserDTO anUserList : userList) {
						if (anUserList != null) {
							logger.info(anUserList.getUserName() + " from UserDTO object");
						}
					}
					Nexus.setCurrentlyOnlineUserList(userList);
					ActionManager.getAction(ACTIONS.NEW_PLAYERLIST_RECEIVED).execute();
					break;

				case USER_LOGGED_IN_DATA:
					// set current user
					UserDTO us = null;
					if (state.getObject() != null && state.getObject() instanceof UserDTO) {
						us = (UserDTO) state.getObject();
						if (us.getActive() == 0) {
							// User is in registration, can not be logged in yet
							C3Message messageUserIsInRegistration = new C3Message(C3MESSAGES.ERROR_USER_IS_IN_REGISTRATION);
							String m = Internationalization.getString("general_user_is_in_registration");
							messageUserIsInRegistration.setText(m);
							messageUserIsInRegistration.setType(C3MESSAGETYPES.CLOSE);
							ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(messageUserIsInRegistration);

							// TODO_C3: Is this second call necessary?
							// ???
							ActionManager.getAction(ACTIONS.LOGON_FINISHED_WITH_ERROR).execute();
							break;
						}
					}

					Nexus.setUser(us);
					logger.info("EventCommunications.onDataIn: myPlayerSessionID: -> " + Nexus.getMyPlayerSessionID());

					// ACHTUNG:
					// Wenn das Event hier geschickt wird, aber im Client nichts ankommt und nirgends eine Fehlermeldung
					// auftaucht, dann ist wahrscheinlich das UniverseDTO zu groß für Netty (Paketgröße 65kB).
					// Dann wird entweder das UniverseDTO immer größer, weil irgendwo ein .clear() fehlt (Mai 2021), oder
					// es sind zu viele Daten in dem Objekt, weil das Spiel an sich zu groß geworden ist.
					// Lösung:
					// - Das Universe darf nicht durch ein fehlendes clear() immer weiter wachsen!
					// - Die Daten müssen aufgeteilt werden, bis sie wieder in die Pakete passen!

					UniverseDTO uni = (UniverseDTO) Compressor.deCompress((byte[]) state.getObject2());
					if (Nexus.getBoUniverse() == null) {
						Nexus.setBOUniverse(new BOUniverse(uni));
					} else {
						Nexus.getBoUniverse().setUniverseDTO(uni);
					}

					ActionManager.getAction(ACTIONS.UPDATE_GAME_INFO).execute();

					Object o3 = state.getObject3();
					if (o3 instanceof ArrayList) {
						ArrayList<UserDTO> allUsers = (ArrayList<UserDTO>) state.getObject3();
						Nexus.setUserList(allUsers);
						HashMap<Long, RolePlayCharacterDTO> allChars = new HashMap<>();
						for (UserDTO u : allUsers) {
							RolePlayCharacterDTO c = u.getCurrentCharacter();
							if (c != null) {
								allChars.put(c.getId(), c);
							} else {
								logger.info("User " + u.getUserName() + " does not have a character!");
							}
						}
						Nexus.setCharacterList(allChars);
					}

					// own playersession on server
					Nexus.setMyPlayerSessionID(state.getReceiver());
					logger.info("EventCommunications.onDataIn: myPlayerSessionID: -> " + Nexus.getMyPlayerSessionID());

					// Send new playerlist
					GameState stateSendPlayerList = new GameState(GAMESTATEMODES.BROADCAST_SEND_NEW_PLAYERLIST);
					NetworkEvent networkEventPlayerList = Events.networkEvent(stateSendPlayerList);
					session.onEvent(networkEventPlayerList);

					// Check for double logins
					GameState stateCheckDoubleLogin = new GameState(GAMESTATEMODES.USER_CHECK_DOUBLE_LOGIN);

					NetworkEvent networkEventCheckDoubleLogin = Events.networkEvent(stateCheckDoubleLogin);
					session.onEvent(networkEventCheckDoubleLogin);

					BOAttack att = Nexus.getCurrentAttackOfUser();
					if (att != null) {
						Nexus.setStoryBeforeSaving(att.getStoryId().longValue());
					}

					ActionManager.getAction(ACTIONS.LOGON_FINISHED_SUCCESSFULL).execute();
					break;

				case NO_MODE:
					break;

				case ATTACK_SAVE_RESPONSE:
					logger.info("Attack has been saved.");
					AttackDTO attack = (AttackDTO) state.getObject();
					boolean iAmInAttack = false;
					for (AttackCharacterDTO ac : attack.getAttackCharList()) {
						if (ac.getCharacterID().equals(Nexus.getCurrentChar().getId())) {
							iAmInAttack = true;
						}
					}

					BOAttack attackToBeReplaced = null;
					for (BOAttack boa : Nexus.getBoUniverse().attackBOsOpenInThisRound.values()) {
						//if (boa.getAttackDTO().getId() != null && boa.getAttackDTO().getId().equals(attack.getId())) {

						if (boa.getAttackDTO().getJumpshipID().equals(attack.getJumpshipID())) {
							// An attack was saved that already existed in my universe
							// It needs to be replaced with the one that was returned from the
							// save event
							attackToBeReplaced = boa;
						}
					}
					if (attackToBeReplaced != null) {
						attackToBeReplaced.setAttackDTO(attack);
					}

					if (iAmInAttack) {
						//RolePlayStoryDTO rpOldDTO = Nexus.getBoUniverse().getAttackStories().get(Nexus.getCurrentAttackOfUser().getAttackDTO().getStoryID());
						boolean storyWasChanged = Nexus.getStoryBeforeSaving() != null && !attack.getStoryID().equals(Nexus.getStoryBeforeSaving());

						String userIDOfSavingUser = (String) state.getObject2(); // --> session.getId();
						if (userIDOfSavingUser == null) {
							// My attack was saved
							// If I was the one who saved this attack, in my universe I already have the attack without an id
							// this needs to be removed and replaced with the one that comes back from the server (this one has
							// the id that was persisted.
							BOAttack attackToBeRemoved = null;
							for (BOAttack boa : Nexus.getBoUniverse().attackBOsOpenInThisRound.values()) {
								if (boa.getAttackDTO().getId() == null) {
									attackToBeRemoved = boa;
									break;
								}
							}
							if (attackToBeRemoved != null) {
								Nexus.getBoUniverse().attackBOsOpenInThisRound.remove(attackToBeRemoved.getAttackDTO().getId());
							}
						} else {
							// Someone else moved this jumpship and managed to save faster than I did, no luck!
						}

						BOAttack boa = new BOAttack(attack);
						Nexus.getBoUniverse().attackBOsOpenInThisRound.put(boa.getAttackDTO().getId(), boa);
						RolePlayStoryDTO rpDTO = (RolePlayStoryDTO) state.getObject3();
						if (rpDTO != null) {
							Nexus.getBoUniverse().getAttackStories().put(rpDTO.getId(), rpDTO);
						}

						ActionManager.getAction(ACTIONS.ENABLE_MAIN_MENU_BUTTONS).execute();
						ActionManager.getAction(ACTIONS.UPDATE_USERS_FOR_ATTACK).execute();

						boolean currentCharInList = false;
						for (AttackCharacterDTO c : attack.getAttackCharList()) {
							if (c.getCharacterID().equals(Nexus.getCurrentChar().getId())) {
								currentCharInList = true;
							}
						}
						if (!currentCharInList) {
							// Obviously I have been kicked and need to be moved from the lobby
							Nexus.setCurrentAttackOfUserToNull();
							ActionManager.getAction(ACTIONS.SWITCH_TO_MAP).execute();
						} else {
							// I am in the list, check if the invasion pane is already open
							ActionManager.getAction(ACTIONS.SWITCH_TO_INVASION).execute();
						}

						if (storyWasChanged) {
							ActionManager.getAction(ACTIONS.ROLEPLAY_NEXT_STEP_CHANGE_PANE).execute(state.getObject());
							BOAttack a = Nexus.getCurrentAttackOfUser();
							if (a == null) {
								a = Nexus.getFinishedAttackInThisRoundForUser();
							}
							if (a != null) {
								Nexus.setStoryBeforeSaving(a.getStoryId().longValue());
							}
						}

						// Wenn Kampf beendet wurde dann den aktuellen Kampf des Users auf Null setzen
						Long factionWinnerId = attack.getFactionID_Winner();
						if (factionWinnerId != null) {
							Nexus.setCurrentAttackOfUserToNull();
						}
					} else {
						// I am not in this attack
						// Is the attack id the same as the attack I have been in? Then I was kicked!
						if (Nexus.getCurrentAttackOfUser() != null) {
							if (attack.getId().equals(Nexus.getCurrentAttackOfUser().getAttackDTO().getId())) {
								ActionManager.getAction(ACTIONS.ENABLE_MAIN_MENU_BUTTONS).execute();
								ActionManager.getAction(ACTIONS.UPDATE_USERS_FOR_ATTACK).execute();
								Nexus.setCurrentAttackOfUserToNull();
								ActionManager.getAction(ACTIONS.SWITCH_TO_MAP).execute();
							}
						}
					}
					break;

				//				case ATTACK_CHARACTER_SAVE_RESPONSE:
				//					logger.info("Attack has changed, a user joined or left.");
				//					AttackDTO attackDTO = (AttackDTO) state.getObject();
				////					ArrayList<RolePlayCharacterDTO> rpCharList = (ArrayList<RolePlayCharacterDTO>)state.getObject2();
				//
				//					for (BOAttack a : Nexus.getBoUniverse().attackBOsOpenInThisRound.values()) {
				//						if (attackDTO.getId().equals(a.getAttackDTO().getId())) {
				//							a.setAttackDTO(attackDTO);
				//							ActionManager.getAction(ACTIONS.UPDATE_USERS_FOR_ATTACK).execute(a);
				//							break;
				//						}
				//					}
				//
				//					break;

				case STATS_MWO_SAVE_RESPONSE:
					BOAttack attack1 = Nexus.getCurrentAttackOfUser();
					Long attackId1 = (Long) state.getObject();
					Boolean attackHasBeenRepeated = false;
					if (state.getObject2() != null && state.getObject2() instanceof Boolean) {
						attackHasBeenRepeated = (Boolean) state.getObject2();
					}
					if (attack1 != null && attack1.getAttackDTO().getId().equals(attackId1)) {
						if (state.isAction_successfully()) {
							mwoStatsSaved = true;
							logger.info("MWO stats saved!");
						} else {
							mwoStatsSaved = false;
							logger.info("MWO stats NOT saved!");
						}
						if (mwoStatsSaved != null && attackStatsSaved != null && charStatsSaved != null) {
							if (mwoStatsSaved && attackStatsSaved && charStatsSaved) {
								if (attackHasBeenRepeated) {
									C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_AttackStatsStoreSuccessfull_Repeated"));
								} else {
									C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_AttackStatsStoreSuccessfull"));
								}
							} else {
								C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_AttackStatsStoreFailed"));
							}
							mwoStatsSaved = null;
							attackStatsSaved = null;
							charStatsSaved = null;
						}
					}
					break;

				case ATTACK_STATS_SAVE_RESPONSE:
					BOAttack attack2 = Nexus.getCurrentAttackOfUser();
					Long attackId2 = (Long) state.getObject();
					if (attack2 != null && attack2.getAttackDTO().getId().equals(attackId2)) {
						if (state.isAction_successfully()) {
							attackStatsSaved = true;
							logger.info("Attack stats saved!");
						} else {
							attackStatsSaved = false;
							logger.info("Attack stats NOT saved!");
						}
						if (mwoStatsSaved != null && attackStatsSaved != null && charStatsSaved != null) {
							if (mwoStatsSaved && attackStatsSaved && charStatsSaved) {
								C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_AttackStatsStoreSuccessfull"));
							} else {
								C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_AttackStatsStoreFailed"));
							}
							mwoStatsSaved = null;
							attackStatsSaved = null;
							charStatsSaved = null;
						}
					}
					break;

				case CHARACTER_STATS_SAVE_RESPONSE:
					BOAttack attack3 = Nexus.getCurrentAttackOfUser();
					Long attackId3 = (Long) state.getObject();
					if (attack3 != null && attack3.getAttackDTO().getId().equals(attackId3)) {
						if (state.isAction_successfully()) {
							charStatsSaved = true;
							logger.info("Character stats saved!");
						} else {
							charStatsSaved = false;
							logger.info("Character stats NOT saved!");
						}
						if (mwoStatsSaved != null && attackStatsSaved != null && charStatsSaved != null) {
							if (mwoStatsSaved && attackStatsSaved && charStatsSaved) {
								C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_AttackStatsStoreSuccessfull"));
							} else {
								C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_AttackStatsStoreFailed"));
							}
							mwoStatsSaved = null;
							attackStatsSaved = null;
							charStatsSaved = null;
						}
					}
					break;

				case ERROR_MESSAGE:
					logger.info("ErrorMessage");
					logger.error((String) state.getObject());
					break;
				case USER_LOG_OUT:
					break;
				case USER_LOGOUT_AFTER_DOUBLE_LOGIN:
					logger.info("EventCommunications.onDataIn: USER_LOGOUT_AFTER_DOUBLE_LOGIN -> " + state.getReceiver().toString());
					if (state.getReceiver().toString().equals(Nexus.getMyPlayerSessionID().toString())) {
						logger.info("onDataIn: LOGOFF_AFTER_DOUBLE_LOGIN: Logout wegen Doppellogin");
						Logout.doLogout(true);
					}
					break;
				case ROLEPLAY_SAVE_STORY:
					if (state.isAction_successfully() == null) {
						logger.info("ROLEPLAY_SAVE_STORY: No action state: " + state.getObject().toString());
					} else if (state.isAction_successfully()) {
						logger.info("ROLEPLAY_SAVE_STORY: Speichern erfolgreich! - " + state.getObject().toString());
						ActionManager.getAction(ACTIONS.SAVE_ROLEPLAY_STORY_OK).execute(state);
					} else if (!state.isAction_successfully()) {
						logger.info("ROLEPLAY_SAVE_STORY: Fehler beim Speichern: " + state.getObject().toString());
						ActionManager.getAction(ACTIONS.SAVE_ROLEPLAY_STORY_ERR).execute(state.getObject());
					}
					break;
				case ROLEPLAY_DELETE_STORY:
					if (state.isAction_successfully() == null) {
						logger.info("ROLEPLAY_DELETE_STORY: No action state: " + state.getObject().toString());
					} else if (state.isAction_successfully()) {
						logger.info("ROLEPLAY_DELETE_STORY_OK");
						ActionManager.getAction(ACTIONS.DELETE_ROLEPLAY_STORY_OK).execute();
					} else if (!state.isAction_successfully()) {
						logger.info("ROLEPLAY_DELETE_STORY_ERR");
						ActionManager.getAction(ACTIONS.DELETE_ROLEPLAY_STORY_ERR).execute(state.getObject());
					}
					break;
				case ROLEPLAY_GET_ALLSTORIES:
					logger.info("ROLEPLAY_GET_ALLSTORIES " + state.getObject().toString());
					ActionManager.getAction(ACTIONS.GET_ROLEPLAY_ALLSTORIES).execute(state.getObject());
					break;
				case ROLEPLAY_GET_STEPSBYSTORY:
					logger.info("ROLEPLAY_GET_STEPSBYSTORY " + state.getObject().toString());
					ActionManager.getAction(ACTIONS.GET_ROLEPLAY_STEPSBYSTORY).execute(state.getObject());
					break;
				case ROLEPLAY_GET_ALLCHARACTER:
					logger.info("ROLEPLAY_GET_ALLCHARACTER " + state.getObject().toString());
					ActionManager.getAction(ACTIONS.GET_ROLEPLAY_ALLCHARACTER).execute(state.getObject());
					break;
				case ROLEPLAY_GET_CHAPTER_BYSORTORDER:
				case ROLEPLAY_GET_STEP_BYSORTORDER:
				case ROLEPLAY_SAVE_NEXT_STEP:
					logger.info("ROLEPLAY_GET_CHAPTER_BYSORTORDER " + state.getObject().toString());

					RolePlayCharacterDTO rpo = (RolePlayCharacterDTO) state.getObject();
					Nexus.setChar(rpo);
					Nexus.getCurrentUser().setCurrentCharacter(rpo);

					ActionManager.getAction(ACTIONS.ROLEPLAY_NEXT_STEP_CHANGE_PANE).execute(state.getObject());
					break;

				case GET_UNIVERSE_DATA:
					logger.info("Re-created universe received from server!");
					ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("13");
					UniverseDTO universeDTO = (UniverseDTO) Compressor.deCompress((byte[]) state.getObject());
					if (universeDTO != null) {
						Nexus.injectNewUniverseDTO(universeDTO);
					} else {
						logger.error("****************** RECEIVED A BROKEN (EMPTY) UNIVERSE! ******************");
					}
					ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("13_33");
					ActionManager.getAction(ACTIONS.NEW_UNIVERSE_RECEIVED).execute();
					break;

				case USER_CHECK_DOUBLE_LOGIN:
					break;

				case USER_REQUEST_LOGGED_IN_DATA:
					break;

				//				case USER_SAVE:
				//					break;
				case BROADCAST_SEND_NEW_PLAYERLIST:
					break;
				case ROLEPLAY_REQUEST_ALLSTORIES:
					break;
				case ROLEPLAY_GET_STORYANDCHAPTER:
					break;
				case ROLEPLAY_REQUEST_STORYANDCHAPTER:
					break;
				case ROLEPLAY_REQUEST_ALLCHARACTER:
					break;
				case FINALIZE_ROUND: // This is the gamestatemode coming from the server!
					logger.info("Server did finalize round.");
					ActionManager.getAction(ACTIONS.FINALIZE_ROUND).execute(); // This is the local event fired!
					break;
				case SERVER_HEARTBEAT:
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					Nexus.setLastServerHeartbeatTimestamp(timestamp);
					logger.info("Server heartbeat received (" + timestamp + "), Server still up and running! No further action (pong).");
					break;
				case SERVER_GOES_DOWN:
					logger.info("Server goes down, need to disconnect if connected!");
					ActionManager.getAction(ACTIONS.SERVER_CONNECTION_LOST).execute();
					break;
				default:
					break;
			}
		}
	}

	public static void showErrorMessage(GameState g) {
		if (g.isAction_successfully() != null && !g.isAction_successfully()) {
			logger.error((String) g.getObject());
		}
	}
}
