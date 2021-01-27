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
package net.clanwolf.starmap.client.util;

public enum C3PROPS {

	// Server
	TCP_HOSTNAME,
	TCP_PORT,

	// Login
	SERVER_URL,
	LOGIN_DATABASE,
	LOGIN_USER,
	LOGIN_PASSWORD,
	AUTO_LOGIN,
	STORE_LOGIN_PASSWORD,
	USE_GUEST_ACCOUNT,

	// Proxy
	USE_PROXY,
	USE_SYSTEM_PROXY,
	PROXY_SERVER,
	PROXY_PORT,
	PROXY_USER,
	PROXY_PASSWORD,
	PROXY_DOMAIN,
	PROXY_NEEDS_AUTHENTICATION,
	PROXY_SAVE_PASSWORD,

	//FTP
	FTP_SERVER,
	FTP_PORT,
	FTP_USER,
	FTP_PASSWORD,

	// Game
	VERSION,
	CHECK_CONNECTION_STATUS,
	CHECK_ONLINE_STATUS,
	CHECK_LOGIN_STATUS,
	CONNECTED_SUCCESSFULLY_ONCE,
	LOGGED_IN_SUCCESSFULLY_ONCE,

	// Media
	PLAY_VOICE,
	PLAY_SOUND,
	PLAY_MUSIC,
	SOUNDVOLUME,
	SPEECHVOLUME,
	MUSICVOLUME,
	VOICERSSAPIKEY,

	// Environment
	BROWSER,
	DOC_DIR,
	LOGFILE,
	DEV_PC,

	// Gui
	COUNTRY,
	LANGUAGE,
	CONFIRM_EXIT;
}
