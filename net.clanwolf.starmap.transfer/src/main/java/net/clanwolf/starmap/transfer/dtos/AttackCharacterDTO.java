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

//	@Column(name = "selectedAttackerWon")
	private Boolean selectedAttackerWon;

//	@Column(name = "selectedDefenderWon")
	private Boolean selectedDefenderWon;

//	@Column(name = "UsedMechChassis")
	private String usedMechChassis;

	@SuppressWarnings("unused")
	public Boolean getSelectedAttackerWon() {
		return selectedAttackerWon;
	}

	@SuppressWarnings("unused")
	public void setSelectedAttackerWon(Boolean selectedAttackerWon) {
		this.selectedAttackerWon = selectedAttackerWon;
	}

	@SuppressWarnings("unused")
	public Boolean getSelectedDefenderWon() {
		return selectedDefenderWon;
	}

	@SuppressWarnings("unused")
	public void setSelectedDefenderWon(Boolean selectedDefenderWon) {
		this.selectedDefenderWon = selectedDefenderWon;
	}

	@SuppressWarnings("unused")
	public Long getNextStoryId() {
		return nextStoryId;
	}

	@SuppressWarnings("unused")
	public void setNextStoryId(Long nextStoryId) {
		this.nextStoryId = nextStoryId;
	}

	@SuppressWarnings("unused")
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	public void setId(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
	public Long getAttackID() {
		return attackID;
	}

	@SuppressWarnings("unused")
	public void setAttackID(Long attackID) {
		this.attackID = attackID;
	}

	@SuppressWarnings("unused")
	public Long getCharacterID() {
		return characterID;
	}

	@SuppressWarnings("unused")
	public void setCharacterID(Long characterID) {
		this.characterID = characterID;
	}

	@SuppressWarnings("unused")
	public String getUsedMechChassis() {
		return usedMechChassis;
	}

	@SuppressWarnings("unused")
	public void setUsedMechChassis(String usedMechChassis) {
		this.usedMechChassis = usedMechChassis;
	}

	@SuppressWarnings("unused")
	public Long getType() {
		return type;
	}

	@SuppressWarnings("unused")
	public void setType(Long type) {
		this.type = type;
	}
}
