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
package net.clanwolf.starmap.client.process.network;

import io.nadron.client.app.Session;
import io.nadron.client.event.Event;
import io.nadron.client.event.Events;
import io.nadron.client.event.NetworkEvent;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.process.logout.Logout;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.util.Compressor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Christian
 */
public class EventCommunications {

	public static void onDataIn(Session session, Event event) {
		C3Logger.info("EventCommunications.onDataIn: " + event.getType());

		if (event.getSource() instanceof GameState) {
			GameState state = (GameState) event.getSource();
//			C3Logger.info("Event received: " + state.getMode());

			showErrorMessage(state);

			switch (state.getMode()) {
				case USER_GET_NEW_PLAYERLIST:
					C3Logger.info("EventCommunications.onDataIn: neue Playerliste ->" + state.getObject());

					@SuppressWarnings("unchecked")
					ArrayList<UserDTO> userList = (ArrayList<UserDTO>) state.getObject();

					for (UserDTO anUserList : userList) {
						C3Logger.info(anUserList.getUserName() + " from UserDTO object");
					}
					Nexus.setCurrentlyOnlineUserList(userList);
					ActionManager.getAction(ACTIONS.NEW_PLAYERLIST_RECEIVED).execute();
					break;

				case USER_LOGGED_IN_DATA:

					// ACHTUNG:
					// Wenn das Event hier geschickt wird, aber im Client nichts ankommt und nirgends eine Fehlermeldung
					// auftaucht, dann ist wahrscheinlich das UniverseDTO zu groß für Netty (Paketgröße 65kB).
					// Dann wird entweder das UniverseDTO immer größer, weil irgendwo ein .clear() fehlt (Mai 2021), oder
					// es sind zu viele Daten in dem Objekt, weil das Spiel an sich zu groß geworden ist.
					// Lösung:
					// - Das Universe darf nicht durch ein fehlendes clear() immer weiter wachsen!
					// - Die Daten müssen aufgeteilt werden, bis sie wieder in die Pakete passen!

					// set current user
					Nexus.setUser((UserDTO) state.getObject());
					C3Logger.info("EventCommunications.onDataIn: myPlayerSessionID: -> " + Nexus.getMyPlayerSessionID());

					UniverseDTO uni = (UniverseDTO) Compressor.deCompress((byte[]) state.getObject2());
					Nexus.setBOUniverse(new BOUniverse(uni));
					//Nexus.setBOUniverse(new BOUniverse((UniverseDTO) state.getObject2()));

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
								C3Logger.info("User " + u.getUserName() + " does not have a character!");
							}
						}
						Nexus.setCharacterList(allChars);
					}

					// own playersession on server
					Nexus.setMyPlayerSessionID(state.getReceiver());
					C3Logger.info("EventCommunications.onDataIn: myPlayerSessionID: -> " + Nexus.getMyPlayerSessionID());

					// Send new playerlist
					GameState stateSendPlayList = new GameState(GAMESTATEMODES.BROADCAST_SEND_NEW_PLAYERLIST);

					NetworkEvent networkEventPlayList = Events.networkEvent(stateSendPlayList);
					session.onEvent(networkEventPlayList);

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
					C3Logger.info("Attack has been started.");
					AttackDTO attack = (AttackDTO) state.getObject();
					//RolePlayStoryDTO rpOldDTO = Nexus.getBoUniverse().getAttackStories().get(Nexus.getCurrentAttackOfUser().getAttackDTO().getStoryID());
					boolean storyWasChanged = false;
					if(	Nexus.getStoryBeforeSaving() != null && !attack.getStoryID().equals(Nexus.getStoryBeforeSaving())){
						storyWasChanged = true;
					};

					String userIDOfSavingUser = (String) state.getObject2(); // --> session.getId();
					if (userIDOfSavingUser == null) {
						// My attack was saved
						// If I was the one who saved this attack, in my universe I already have the attack without an id
						// this needs to be removed and replaced with the one that comes back from the server (this one has
						// the id that was persisted.
						BOAttack attackToBeRemoved = null;
						for (BOAttack boa : Nexus.getBoUniverse().attackBOs.values()) {
							if (boa.getAttackDTO().getId() == null) {
								attackToBeRemoved = boa;
								break;
							}
						}
						if (attackToBeRemoved != null) {
							Nexus.getBoUniverse().attackBOs.remove(attackToBeRemoved.getAttackDTO().getId());
						}
					} else {
						// Someone else moved this jumpship and managed to save faster than I did, no luck!
					}

					BOAttack boa = new BOAttack(attack);
					Nexus.getBoUniverse().attackBOs.put(boa.getAttackDTO().getId(), boa);
					RolePlayStoryDTO rpDTO = (RolePlayStoryDTO) state.getObject3();
					if(rpDTO != null) {
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
					}

					if(storyWasChanged) {
						ActionManager.getAction(ACTIONS.ROLEPLAY_NEXT_STEP_CHANGE_PANE).execute(state.getObject());
						Nexus.setStoryBeforeSaving(Nexus.getCurrentAttackOfUser().getStoryId().longValue());
					}
					break;

				case ATTACK_CHARACTER_SAVE_RESPONSE:
					C3Logger.info("Attack has changed, a user joined or left.");
					AttackDTO attackDTO = (AttackDTO) state.getObject();
//					ArrayList<RolePlayCharacterDTO> rpCharList = (ArrayList<RolePlayCharacterDTO>)state.getObject2();

					for (BOAttack a : Nexus.getBoUniverse().attackBOs.values()) {
						if (attackDTO.getId().equals(a.getAttackDTO().getId())) {
							a.setAttackDTO(attackDTO);
							ActionManager.getAction(ACTIONS.UPDATE_USERS_FOR_ATTACK).execute(a);
							break;
						}
					}

					break;
				case ERROR_MESSAGE:
					C3Logger.info("ErrorMessage");
					C3Logger.error((String) state.getObject());
					break;
				case USER_LOG_OUT:
					break;
				case USER_LOGOUT_AFTER_DOUBLE_LOGIN:
					C3Logger.info("EventCommunications.onDataIn: USER_LOGOUT_AFTER_DOUBLE_LOGIN -> " + state.getReceiver().toString());
					if (state.getReceiver().toString().equals(Nexus.getMyPlayerSessionID().toString())) {
						C3Logger.info("onDataIn: LOGOFF_AFTER_DOUBLE_LOGIN: Logout wegen Doppellogin");
						Logout.doLogout(true);
					}
					break;
				case ROLEPLAY_SAVE_STORY:
					if (state.isAction_successfully() == null) {
						C3Logger.info("ROLEPLAY_SAVE_STORY: No action state: " + state.getObject().toString());

					} else if (state.isAction_successfully()) {
						C3Logger.info("ROLEPLAY_SAVE_STORY: Speichern erfolgreich! - " + state.getObject().toString());
						ActionManager.getAction(ACTIONS.SAVE_ROLEPLAY_STORY_OK).execute(state);

					} else if (!state.isAction_successfully()) {
						C3Logger.info("ROLEPLAY_SAVE_STORY: Fehler beim Speichern: " + state.getObject().toString());
						ActionManager.getAction(ACTIONS.SAVE_ROLEPLAY_STORY_ERR).execute(state.getObject());

					}
					break;
				case ROLEPLAY_DELETE_STORY:
					if (state.isAction_successfully() == null) {
						C3Logger.info("ROLEPLAY_DELETE_STORY: No action state: " + state.getObject().toString());

					} else if (state.isAction_successfully()) {
						C3Logger.info("ROLEPLAY_DELETE_STORY_OK");
						ActionManager.getAction(ACTIONS.DELETE_ROLEPLAY_STORY_OK).execute();

					} else if (!state.isAction_successfully()) {
						C3Logger.info("ROLEPLAY_DELETE_STORY_ERR");
						ActionManager.getAction(ACTIONS.DELETE_ROLEPLAY_STORY_ERR).execute(state.getObject());

					}
					break;
				case ROLEPLAY_GET_ALLSTORIES:
					C3Logger.info("ROLEPLAY_GET_ALLSTORIES " + state.getObject().toString());
					ActionManager.getAction(ACTIONS.GET_ROLEPLAY_ALLSTORIES).execute(state.getObject());
					break;
				case ROLEPLAY_GET_ALLCHARACTER:
					C3Logger.info("ROLEPLAY_GET_ALLCHARACTER " + state.getObject().toString());
					ActionManager.getAction(ACTIONS.GET_ROLEPLAY_ALLCHARACTER).execute(state.getObject());
					break;

				case ROLEPLAY_GET_CHAPTER_BYSORTORDER:
				case ROLEPLAY_GET_STEP_BYSORTORDER:
				case ROLEPLAY_SAVE_NEXT_STEP:
					C3Logger.info("ROLEPLAY_GET_CHAPTER_BYSORTORDER " + state.getObject().toString());

					RolePlayCharacterDTO rpo = (RolePlayCharacterDTO) state.getObject();
					Nexus.setChar(rpo);
					Nexus.getCurrentUser().setCurrentCharacter(rpo);

					ActionManager.getAction(ACTIONS.ROLEPLAY_NEXT_STEP_CHANGE_PANE).execute(state.getObject());
					break;

				case GET_UNIVERSE_DATA:
					C3Logger.info("Re-created universe received from server!");
					ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("13");
					UniverseDTO universeDTO = (UniverseDTO) Compressor.deCompress((byte[])state.getObject());
					Nexus.injectNewUniverseDTO(universeDTO);
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
				case FINALIZE_ROUND:
					C3Logger.info("Server did finalize round.");
					ActionManager.getAction(ACTIONS.FINALIZE_ROUND).execute();
					break;
				default:
					break;
			}
		}
	}

	public static void showErrorMessage(GameState g){
		if(g.isAction_successfully() != null && !g.isAction_successfully()){
			C3Logger.error((String) g.getObject());
		}
	}
}
