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
package net.clanwolf.starmap.transfer.saveObjects;

import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.UserDTO;

import java.util.ArrayList;

public class UsereditorSaveObject {
	private Long factionToChangeTo;
	private String factionKey;
	private RolePlayCharacterDTO rpCharacter;
	private ArrayList<UserDTO> usersToSave;

	public Long getFactionToChangeTo() {
		return factionToChangeTo;
	}

	public void setFactionToChangeTo(Long factionToChangeTo) {
		this.factionToChangeTo = factionToChangeTo;
	}

	public String getFactionKey() {
		return factionKey;
	}

	public void setFactionKey(String factionKey) {
		this.factionKey = factionKey;
	}

	public RolePlayCharacterDTO getRpCharacter() {
		return rpCharacter;
	}

	public void setRpCharacter(RolePlayCharacterDTO rpCharacter) {
		this.rpCharacter = rpCharacter;
	}

	public ArrayList<UserDTO> getUsersToSave() {
		return usersToSave;
	}

	public void setUsersToSave(ArrayList<UserDTO> usersToSave) {
		this.usersToSave = usersToSave;
	}
}
