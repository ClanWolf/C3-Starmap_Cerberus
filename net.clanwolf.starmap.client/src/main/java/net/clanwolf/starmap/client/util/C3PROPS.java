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
 * Copyright (c) 2001-2024, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.util;

public enum C3PROPS {

	// Values must be in upper case here, or the re-creation of the user property file will result
	// in exceptions (during the put, the key needs to be uppercase).

	// SERVER
	TCP_HOSTNAME,
	TCP_PORT,
	SERVER_URL,
	MANUAL_DOWNLOAD_CLIENT_URL,
	AUTOMATIC_DOWNLOAD_CLIENT_URL,

	// LOGIN
	LOGIN_USER,
	LOGIN_PASSWORD,
	LOGIN_DATABASE,
	AUTO_LOGIN,
	STORE_LOGIN_PASSWORD,
	USE_GUEST_ACCOUNT,
	FACTION_KEY,

	REGISTRATION,

	// PROXY
	USE_PROXY,
	USE_SYSTEM_PROXY,
	PROXY_SERVER,
	PROXY_PORT,
	PROXY_USER,
	PROXY_PASSWORD,
	PROXY_DOMAIN,
	PROXY_NEEDS_AUTHENTICATION,
	PROXY_SAVE_PASSWORD,

	// FTP
	FTP_SERVER,
	FTP_PORT,
	FTP_USER,
	FTP_PASSWORD,
	FTP_USER_LOGUPLOAD,
	FTP_PASSWORD_LOGUPLOAD,
	FTP_USER_HISTORYUPLOAD,
	FTP_PASSWORD_HISTORYUPLOAD,

	// GUI
	LANGUAGE,
	MAP_DIMENSIONS,

	// SOUND
	PLAY_VOICE,
	PLAY_MUSIC,
	PLAY_SOUND,
	SOUNDVOLUME,
	SPEECHVOLUME,
	MUSICVOLUME,
	MUSICTRACK,
	VOICERSSAPIKEY,

	// GENERALS
	GENERALS_CLIPBOARD_API,
	GENERALS_SCREENSHOT_HISTORY,

	// SYSTEM
	VERSION,
	BROWSER,

	// Whenever the version changes, the user properties file is regenerated, containing only the
	// properties that are contained in this file (with their previously given values by the user).
	// This helps to clean the property file and keep it free from old entries.

	// RUNTIME - Not stored!

	// GAME
	CHECK_DB_CONNECTION_STATUS,
	CHECK_ONLINE_STATUS,
	CHECK_LOGIN_STATUS,
	CONNECTED_SUCCESSFULLY_ONCE,
	LOGGED_IN_SUCCESSFULLY_ONCE,

	// ENV
	LOGFILE,
	DEV_PC
}
