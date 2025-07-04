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
package net.clanwolf.starmap.transfer.enums;

/**
 * @author Undertaker
 */
public enum GAMESTATEMODES {
	NO_MODE,                                    /* Default mode */
	CLIENT_READY_FOR_EVENTS,

	CLIENT_LOGOUT_AFTER_FACTION_CHANGE,

	ERROR_MESSAGE,                              /* Send a error message string (Object: String with the error message) */

	USER_LOG_OUT,                               /* UserDTO logout */
	USER_LOGOUT_AFTER_DOUBLE_LOGIN,             /* Send a BROADCAST with a specified receiver (Receiver: PlayerSession.getID() ) */
	USER_CHECK_DOUBLE_LOGIN,
	USER_REQUEST_LOGGED_IN_DATA,
	USER_LOGGED_IN_DATA,                        /* Send answer to the client after successfully login (Object: UserDTO and Receiver:PlayerSession.getID() -> its the own id of the player session as string */
	USER_GET_NEW_PLAYERLIST,                    /* Gets a new list of actual players (Object: ArrayList<UserDTO>)*/
	USER_SAVE,
	FACTION_SAVE,
	USER_SESSION_SAVE,
	USER_SAVE_LAST_LOGIN_DATE,
	USERDATA_SAVE,
	JUMPSHIP_SAVE,
	ATTACK_SAVE,
	ATTACK_SAVE_WITH_CHARACTERS,
	ATTACK_SAVE_RESPONSE,
	ATTACK_CHARACTER_SAVE,
	ATTACK_CHARACTER_SAVE_WITHOUT_NEW_UNIVERSE,
	ATTACK_CHARACTER_SAVE_RESPONSE,
	ATTACK_TEAM_ERROR,
	ATTACK_TEAM_ERROR_RESPONSE,

	DIPLOMACY_SAVE,
	DIPLOMACY_SAVE_RESPONSE,

	STATS_MWO_SAVE,
	STATS_MWO_SAVE_RESPONSE,
	CHARACTER_STATS_SAVE,
	CHARACTER_STATS_SAVE_RESPONSE,
	ATTACK_STATS_SAVE,
	ATTACK_STATS_SAVE_RESPONSE,

	BROADCAST_SEND_NEW_PLAYERLIST,              /* Inform all clients after a successfull login */
	FOUND_BROKEN_ATTACK,
	BROKEN_ATTACK_KILL_FIVE_MINUTE_WARNING,
	BROKEN_ATTACK_KILL_AFTER_TIMEOUT,
	BROKEN_ATTACK_HEALED,
	ROLEPLAY_GET_ALLSTORIES,                    /* OBJECT: null */
	ROLEPLAY_REQUEST_ALLSTORIES,                /* OBJECT: Arraylist<RolePlayStoryDTO> - Gets a list with storys and chapters and steps*/
	ROLEPLAY_GET_STORYANDCHAPTER,               /* OBJECT: null */
	ROLEPLAY_REQUEST_STORYANDCHAPTER,           /* OBJECT: Arraylist<RolePlayStoryDTO> - Gets a list with storys and chapters*/

	ROLEPLAY_GET_CHAPTER_BYSORTORDER,           /* OBJECT; RolePlayStoryDTO.class parent story, OBJECT2: Integer.class sortorder - Returns (OBJECT: RolePlayStoryDTO) chapter for the story with a special sort order*/
	ROLEPLAY_GET_STEP_BYSORTORDER,              /* OBJECT; RolePlayStoryDTO.class parent story, OBJECT2: Integer.class sortorder - Returns (OBJECT: RolePlayStoryDTO) chapter for the story with a special sort order*/
	ROLEPLAY_SAVE_NEXT_STEP,                    /* OBJECT: RolePlayCharacterDTO.class */

	ROLEPLAY_SAVE_STORY,                        /* OBJECT: RolePlayStoryDTO.class - Save a RolePlayStoryDTO on the database */
	ROLEPLAY_DELETE_STORY,                      /* OBJECT: RolePlayStoryDTO.class - Delete a RolePlayStoryDTO on the database */
	ROLEPLAY_REQUEST_ALLCHARACTER,              /**/
	ROLEPLAY_GET_ALLCHARACTER,                  /**/
	ROLEPLAY_REQUEST_STEPSBYSTORY,
	ROLEPLAY_GET_STEPSBYSTORY,

	ROLL_RANDOM_MAP,
	RETURN_ROLLED_RANDOM_MAP_FOR_INVASION,

	PLAY_SOUNDBOARD_SOUND_EVENT_01,
	PLAY_SOUNDBOARD_SOUND_EVENT_02,
	PLAY_SOUNDBOARD_SOUND_EVENT_03,
	PLAY_SOUNDBOARD_SOUND_EVENT_EXECUTE_01,
	PLAY_SOUNDBOARD_SOUND_EVENT_EXECUTE_02,
	PLAY_SOUNDBOARD_SOUND_EVENT_EXECUTE_03,
	GET_UNIVERSE_DATA,                          /* Get all universe data from server */
	FINALIZE_ROUND,
	SESSION_KEEPALIVE,

	FORCE_FINALIZE_ROUND,
	FORCE_NEW_UNIVERSE,

	RESET_FIGHT,
	RESTART_SERVER,
	SERVER_HEARTBEAT,
	SERVER_GOES_DOWN;
}
