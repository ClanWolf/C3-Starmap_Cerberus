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
 * Copyright (c) 2001-2022, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.transfer.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.transfer.Dto;

@JsonIdentityInfo(
		scope= AttackCharacterDTO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class AttackCharacterDTO extends Dto {

//	@Column(name = "ID")
	private Long id;

//	@Column(name = "AttackID")
	private Long attackID;

//	@JoinColumn(name = "CharacterID")
	private Long characterID;

//	@Column(name = "nextStoryId")
	private Long nextStoryId;

//	@Column(name = "Type")
	private Long type;

	public Long getNextStoryId() {
		return nextStoryId;
	}

	public void setNextStoryId(Long nextStoryId) {
		this.nextStoryId = nextStoryId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAttackID() {
		return attackID;
	}

	public void setAttackID(Long attackID) {
		this.attackID = attackID;
	}

	public Long getCharacterID() {
		return characterID;
	}

	public void setCharacterID(Long characterID) {
		this.characterID = characterID;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}
}
