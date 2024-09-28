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
package net.clanwolf.starmap.client.enums;

import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.util.Internationalization;

/**
 * @author Christian
 *
 */
public enum C3MESSAGES {

	DOWNLOAD_CLIENT(1000, "app_new_version_available", C3MESSAGETYPES.YES_NO),

	WARNING_CLIENT_LOGOUT_AFTER_FACTION_CHANGE(2000, "faction_change_logout_message", C3MESSAGETYPES.CLOSE),
	WARNING_BLACKBOX_TEAMS_INVALID(2010, "C3_Lobby_Error_Teams_Invalid", C3MESSAGETYPES.CLOSE),

	ERROR_CLIENT_DOWNLOAD_FAILED(5000, "general_download_client_error", C3MESSAGETYPES.CLOSE),
	ERROR_NOT_ALLOWED(5010, "general_notallowed", C3MESSAGETYPES.CLOSE),
	ERROR_WRONG_CREDENTIALS(5020, "app_error_credentials_wrong", C3MESSAGETYPES.CLOSE),
	ERROR_NO_EDITING_ALLOWED(5030, "app_error_no_editing_allowed", C3MESSAGETYPES.CLOSE),
	ERROR_SERVER_OFFLINE(5040, "app_error_server_offline", C3MESSAGETYPES.CLOSE),
	ERROR_DATABASE_OFFLINE(5050, "app_database_indicator_message_OFFLINE", C3MESSAGETYPES.CLOSE),
	ERROR_DATABASE_CONNECTION_FAILED(5060, "app_database_indicator_message_OFFLINE", C3MESSAGETYPES.CLOSE),
	ERROR_SERVER_CONNECTION_LOST(5070, "general_server_connection_lost", C3MESSAGETYPES.CLOSE),
	ERROR_USER_IS_IN_REGISTRATION(5080, "general_user_registration_username_error", C3MESSAGETYPES.CLOSE);

	private final int id;
	private final String textKey;
	private final String text;
	private final C3MESSAGETYPES type;

	C3MESSAGES(final int id, final String textKey, final C3MESSAGETYPES type) {
		this.id = id;
		this.textKey = textKey;
		this.type = type;
		this.text = Internationalization.getString(textKey).replace("{version}", Nexus.getLastAvailableClientVersion());
	}

	public int getId() {
		return id;
	}

	public String getTextKey() {
		return textKey;
	}

	public String getText() {
		return text;
	}

	public C3MESSAGETYPES getType() {
		return type;
	}
}
