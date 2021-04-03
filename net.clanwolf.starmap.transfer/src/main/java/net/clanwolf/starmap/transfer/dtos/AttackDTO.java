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
	private Long starSystemId;
	private Long starSystemDataId;
	private Long attackType;
	private Long attackedFromStarSystem;
	private Long factionId_defender;
	private Long jumpshipId;

	public Long getJumpshipId() {
		return jumpshipId;
	}

	public void setJumpshipId(Long jumpshipId) {
		this.jumpshipId = jumpshipId;
	}

	public Long getAttackType() {
		return attackType;
	}

	public void setAttackType(Long attackType) {
		this.attackType = attackType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAttackedFromStarSystem() {
		return attackedFromStarSystem;
	}

	public void setAttackedFromStarSystem(Long attackedFromStarSystem) {
		this.attackedFromStarSystem = attackedFromStarSystem;
	}

	public Integer getSeason() {
		return season;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public Long getStarSystemId() {
		return starSystemId;
	}

	public void setStarSystemId(Long starSystemId) {
		this.starSystemId = starSystemId;
	}

	public Long getStarSystemDataId() {
		return starSystemDataId;
	}

	public void setStarSystemDataId(Long starSystemDataId) {
		this.starSystemDataId = starSystemDataId;
	}

	public Long getfactionId_defender() {
		return factionId_defender;
	}

	public void setfactionId_defender(Long defenderId) {
		this.factionId_defender = defenderId;
	}

	public AttackDTO() {
		// empty constructor
	}
}
