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
package net.clanwolf.starmap.client.action;

public enum ACTIONS {
	APPLICATION_EXIT_REQUEST,
	APPLICATION_EXIT,
	APPLICATION_STARTUP,

	CURSOR_REQUEST_NORMAL,
	CURSOR_REQUEST_WAIT,
	CURSOR_REQUEST_RESIZE,
	CURSOR_REQUEST_MOVE,

	SET_STATUS_TEXT,
	SET_CONSOLE_OPACITY,
	SET_CONSOLE_OUTPUT_LINE,

	CHANGE_LANGUAGE,

	ONLINECHECK_STARTED,
	ONLINECHECK_FINISHED,
	DATABASECONNECTIONCHECK_STARTED,
	DATABASECONNECTIONCHECK_FINISHED,
	LOGINCHECK_STARTED,
	LOGINCHECK_FINISHED,

	SERVER_CONNECTION_LOST,

	CLEAR_PASSWORD_FIELD,
	DISABLE_REGISTRATION,
	READY_TO_LOGIN,
	LOGGING_OFF,
	LOGGED_OFF_COMPLETE,
	LOGGED_OFF_AFTER_DOUBLE_LOGIN_COMPLETE,
	LOGGED_ON,
	LOGON_RUNNING,
	LOGON_FINISHED_SUCCESSFULL,
	LOGON_FINISHED_WITH_ERROR,
	LOGON_NETTY_FAILED,

	MOUSE_MOVED,

	MUSIC_SELECTION_CHANGED,

	START_SPEECH_SPECTRUM,
	STOP_SPEECH_SPECTRUM,
	PANE_CREATION_BEGINS,
	PANE_CREATION_FINISHED,
	PANE_DESTRUCTION_FINISHED,
	PANE_DESTROY_CURRENT,
	MAP_CREATION_FINISHED,

	SAVE_ROLEPLAY_STORY_OK,
	SAVE_ROLEPLAY_STORY_ERR,
	DELETE_ROLEPLAY_STORY_OK,
	DELETE_ROLEPLAY_STORY_ERR,
	GET_ROLEPLAY_ALLSTORIES,
	GET_ROLEPLAY_ALLCHARACTER,
	GET_ROLEPLAY_STEPSBYSTORY,
	ROLEPLAY_NEXT_STEP,
	ROLEPLAY_NEXT_STEP_CHANGE_PANE,

	DISPLAY_RANDOM_MAP_ROLL,

	PLAY_SOUNDBAR_EXECUTE_01,
	PLAY_SOUNDBAR_EXECUTE_02,
	PLAY_SOUNDBAR_EXECUTE_03,

	NOISE,
	ACTION_SUCCESSFULLY_EXECUTED,
	SHOW_MESSAGE,
	SHOW_MEDAL,
	SHOW_POPUP,
	CURSOR_REQUEST_NORMAL_MESSAGE,
	CURSOR_REQUEST_WAIT_MESSAGE,
	SHOW_MESSAGE_WAS_ANSWERED,
	START_ROLEPLAY,
	UPDATE_GAME_INFO,
	UPDATE_ALLIANCES_LIST,
	UPDATE_ROUND_COUNTDOWN,
	OPEN_MANUAL,
	OPEN_ATTACKTHREAT,
	OPEN_PATREON,
	OPEN_SUPPORT_LIBERA,
	OPEN_CLIENTVERSION_DOWNLOADPAGE,
	CLIENT_INSTALLER_DOWNLOAD_COMPLETE,
	CLIENT_INSTALLER_DOWNLOAD_ERROR,

	MWO_DROPSTATS_RECEIVED,
	FLASH_MWO_LOGO_ONCE,
	RESET_STORY_PANES,

	SHOW_FORBIDDEN_ICON_MAP,

	FINALIZE_ROUND,
	REPAINT_MAP,
	NEW_UNIVERSE_RECEIVED,
	NEW_PLAYERLIST_RECEIVED,
	UPDATE_UNIVERSE,
	SHOW_SYSTEM_DETAIL,
	HIDE_SYSTEM_DETAIL,
	SHOW_JUMPSHIP_DETAIL,
	HIDE_JUMPSHIP_DETAIL,
	UPDATE_COORD_INFO,
	SET_TERMINAL_TEXT,
	TERMINAL_COMMAND,
	ENABLE_DEFAULT_BUTTON,
	DISABLE_DEFAULT_BUTTON,
	SYSTEM_WAS_SELECTED,

	ENABLE_MAIN_MENU_BUTTONS,
	ENABLE_JUMP_BUTTON,
	UPDATE_USERS_FOR_ATTACK,
	SWITCH_TO_INVASION,
	SWITCH_TO_MAP,
	CREATE_ATTACK_SCREENSHOT,
	SET_FACTION_LOGO,
	SET_FACTION_LOGO_INVISIBLE,
	SET_USER_IMAGE,
	HIDE_IRC_INDICATOR,
	SHOW_IRC_INDICATOR,

	IRC_CONNECTED,
	IRC_DISCONNECT_NOW,
	IRC_ERROR,
	IRC_SENDING_ACTION,
	IRC_GET_NAMELIST,
	IRC_USER_JOINED,
	IRC_USER_LEFT,
	IRC_USER_PART,
	IRC_USER_KICKED,
	IRC_USER_QUIT,
	IRC_USER_NICKCHANGE,
	IRC_SEND_MESSAGE,
	IRC_CHANGE_NICK,
	IRC_MESSAGE_IN_GENERAL,
	IRC_MESSAGE_IN_CHANNEL,
	IRC_MESSAGE_IN_PRIVATE,
	IRC_UPDATED_USERLIST_RECEIVED,

	DIPLOMACY_SITUATION_CHANGED,
	DIPLOMACY_STATUS_PENDING,
	DIPLOMACY_STATUS_PENDING_HIDE,

	CURRENT_ATTACK_IS_BROKEN,
	CURRENT_ATTACK_IS_BROKEN_WARNING,
	CURRENT_ATTACK_IS_BROKEN_KILLED,
	CURRENT_ATTACK_IS_HEALED,
	WATCHED_ATTACK_IS_BROKEN,
	WATCHED_ATTACK_IS_BROKEN_WARNING,
	WATCHED_ATTACK_IS_BROKEN_KILLED,
	WATCHED_ATTACK_IS_HEALED,

	ADD_CONSOLE_LINE,
	CLOSE_SURFACE_MAP,
	MAP_MOVE_TO_STARSYSTEM,
	MAP_MOVE_TO_JUMPSHIP;

	private ACTIONS() {
		// do nothing
	}

	public String getNameKey() {
		return "ACTIONS." + name() + ".name";
	}

	public String getToolTipKey() {
		return "ACTIONS." + name() + ".toolTip";
	}

	public String getDescriptionKey() {
		return "ACTIONS." + name() + ".description";
	}

	public String getAcceleratorKey() {
		return "ACTIONS." + name() + ".accelerator";
	}

	public String getMnemonicIndex() {
		return "ACTIONS." + name() + ".mnemonicIndex";
	}
}
