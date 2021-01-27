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

	private Integer id = 0;
	private Integer season = 0;
	private Integer round = 0;
	private Integer starSystemId = 0;
	private Integer starSystemDataId = 0;
	private Integer attackedFromStarSystem = 0;
	private Integer attackerId = 0;
	private Integer defenderId = 0;
	private Integer priority;
	private Integer attackType;

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getAttackType() {
		return attackType;
	}

	public void setAttackType(Integer attackType) {
		this.attackType = attackType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAttackedFromStarSystem() {
		return attackedFromStarSystem;
	}

	public void setAttackedFromStarSystem(Integer attackedFromStarSystem) {
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

	public Integer getStarSystemId() {
		return starSystemId;
	}

	public void setStarSystemId(Integer starSystemId) {
		this.starSystemId = starSystemId;
	}

	public Integer getStarSystemDataId() {
		return starSystemDataId;
	}

	public void setStarSystemDataId(Integer starSystemDataId) {
		this.starSystemDataId = starSystemDataId;
	}

	public Integer getAttackerId() {
		return attackerId;
	}

	public void setAttackerId(Integer attackerId) {
		this.attackerId = attackerId;
	}

	public Integer getDefenderId() {
		return defenderId;
	}

	public void setDefenderId(Integer defenderId) {
		this.defenderId = defenderId;
	}

	public AttackDTO() {
		// empty constructor
	}
}
