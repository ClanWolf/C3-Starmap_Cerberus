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
package net.clanwolf.starmap.transfer.dtos;

import net.clanwolf.starmap.transfer.Dto;

public class AttackDTO extends Dto {

	private Long id;
	private Integer season;
	private Integer round;
	private Long starSystemID;
	private Long starSystemDataID;
	private Long attackTypeID;
	private Long attackedFromStarSystemID;
	private Long factionID_Defender;
	private Long jumpshipID;

	@SuppressWarnings("unused")
	public Long getJumpshipID() {
		return jumpshipID;
	}

	@SuppressWarnings("unused")
	public void setJumpshipID(Long jumpshipID) {
		this.jumpshipID = jumpshipID;
	}

	@SuppressWarnings("unused")
	public Long getAttackTypeID() {
		return attackTypeID;
	}

	@SuppressWarnings("unused")
	public void setAttackTypeID(Long attackType) {
		this.attackTypeID = attackType;
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
	public Long getAttackedFromStarSystemID() {
		return attackedFromStarSystemID;
	}

	@SuppressWarnings("unused")
	public void setAttackedFromStarSystemID(Long attackedFromStarSystem) {
		this.attackedFromStarSystemID = attackedFromStarSystem;
	}

	@SuppressWarnings("unused")
	public Integer getSeason() {
		return season;
	}

	@SuppressWarnings("unused")
	public void setSeason(Integer season) {
		this.season = season;
	}

	@SuppressWarnings("unused")
	public Integer getRound() {
		return round;
	}

	@SuppressWarnings("unused")
	public void setRound(Integer round) {
		this.round = round;
	}

	@SuppressWarnings("unused")
	public Long getStarSystemID() {
		return starSystemID;
	}

	@SuppressWarnings("unused")
	public void setStarSystemID(Long starSystemID) {
		this.starSystemID = starSystemID;
	}

	@SuppressWarnings("unused")
	public Long getStarSystemDataID() {
		return starSystemDataID;
	}

	@SuppressWarnings("unused")
	public void setStarSystemDataID(Long starSystemDataID) {
		this.starSystemDataID = starSystemDataID;
	}

	@SuppressWarnings("unused")
	public Long getfactionID_Defender() {
		return factionID_Defender;
	}

	@SuppressWarnings("unused")
	public void setfactionID_Defender(Long defenderID) {
		this.factionID_Defender = defenderID;
	}

	public AttackDTO() {
		// empty constructor
	}
}
