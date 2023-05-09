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
package net.clanwolf.starmap.transfer.saveobjects;

import net.clanwolf.starmap.transfer.dtos.FactionDTO;
import net.clanwolf.starmap.transfer.dtos.UserDTO;

import java.util.ArrayList;

public class UserSaveObject {
	public ArrayList<UserDTO> userlist;
	public Long requestedFactionId;
	public String factionKeyForRequestedFaction;
	public FactionDTO editedFaction;

	@SuppressWarnings("unused")
	public ArrayList<UserDTO> getUserlist() {
		return userlist;
	}

	@SuppressWarnings("unused")
	public void setUserlist(ArrayList<UserDTO> userlist) {
		this.userlist = userlist;
	}

	@SuppressWarnings("unused")
	public Long getRequestedFactionId() {
		return requestedFactionId;
	}

	@SuppressWarnings("unused")
	public void setRequestedFactionId(Long requestedFactionId) {
		this.requestedFactionId = requestedFactionId;
	}

	@SuppressWarnings("unused")
	public String getFactionKeyForRequestedFaction() {
		return factionKeyForRequestedFaction;
	}

	@SuppressWarnings("unused")
	public void setFactionKeyForRequestedFaction(String factionKeyForRequestedFaction) {
		this.factionKeyForRequestedFaction = factionKeyForRequestedFaction;
	}

	@SuppressWarnings("unused")
	public FactionDTO getEditedFaction() {
		return editedFaction;
	}

	@SuppressWarnings("unused")
	public void setEditedFaction(FactionDTO editedFaction) {
		this.editedFaction = editedFaction;
	}
}
