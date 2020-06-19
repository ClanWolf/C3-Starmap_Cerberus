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
 * Copyright (c) 2001-2020, ClanWolf.net                            |
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
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.process.logout.Logout;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.UniverseDTO;
import net.clanwolf.starmap.transfer.dtos.UserDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.util.ArrayList;

/**
 * @author Christian
 */
public class EventCommunications {

	public static void onDataIn(Session session, Event event) {
		C3Logger.info("EventCommunications.onDataIn: " + event.getType());

		if (event.getSource() instanceof GameState) {
			GameState state = (GameState) event.getSource();

			switch (state.getMode()) {
				case USER_GET_NEW_PLAYERLIST:
					C3Logger.info("EventCommunications.onDataIn: neue Playerliste ->" + state.getObject());

					@SuppressWarnings("unchecked")
					ArrayList<UserDTO> userList = (ArrayList<UserDTO>) state.getObject();

					for (UserDTO anUserList : userList) {
						C3Logger.info(anUserList.getUserName() + " from UserDTO object");
					}
					break;
				case USER_LOGGED_IN_DATA:
					// set current user
					Nexus.setUser((UserDTO) state.getObject());
					C3Logger.info("EventCommunications.onDataIn: myPlayerSessionID: -> " + Nexus.getMyPlayerSessionID());

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

					ActionManager.getAction(ACTIONS.LOGON_FINISHED_SUCCESSFULL).execute();
					break;
				case NO_MODE:
					break;
				case ERROR_MESSAGE:
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
					UniverseDTO universeDTO = (UniverseDTO) state.getObject();
					Nexus.setUniverseDTO(universeDTO);

					ActionManager.getAction(ACTIONS.NEW_UNIVERSE_RECEIVED).execute();

					break;
				case USER_CHECK_DOUBLE_LOGIN:
					break;
				case USER_REQUEST_LOGGED_IN_DATA:
					break;
				case USER_SAVE:
					break;
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
				default:
					break;
			}
		}
	}
}
