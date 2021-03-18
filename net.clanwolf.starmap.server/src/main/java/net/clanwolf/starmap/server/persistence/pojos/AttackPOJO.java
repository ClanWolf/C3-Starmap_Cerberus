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
package net.clanwolf.starmap.server.persistence.pojos;

import net.clanwolf.starmap.server.persistence.Pojo;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "_HH_ATTACK", catalog = "C3")
public class AttackPOJO extends Pojo {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Season")
	private Long season;

	@Column(name = "Round")
	private Long round;

	@Column(name = "StarSystemID")
	private Long starSystemID;

	@Column(name = "StarSystemDataID")
	private Long starSystemDataID;

	@Column(name = "AttackTypeID")
	private Long attackTypeID;

	@Column(name = "AttackedFromStarSystemID")
	private Long attackedFromStarSystemID;

	@Column(name = "FactionID_Defender")
	private Long factionID_Defender;

	@Column(name = "JumpshipID")
	private Long jumpshipID;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSeason() {
		return season;
	}

	public void setSeason(Long season) {
		this.season = season;
	}

	public Long getRound() {
		return round;
	}

	public void setRound(Long round) {
		this.round = round;
	}

	public Long getStarSystemID() {
		return starSystemID;
	}

	public void setStarSystemID(Long starSystemID) {
		this.starSystemID = starSystemID;
	}

	public Long getStarSystemDataID() {
		return starSystemDataID;
	}

	public void setStarSystemDataID(Long starSystemDataID) {
		this.starSystemDataID = starSystemDataID;
	}

	public Long getAttackedFromStarSystemID() {
		return attackedFromStarSystemID;
	}

	public void setAttackedFromStarSystemID(Long attackedFromStarSystemID) {
		this.attackedFromStarSystemID = attackedFromStarSystemID;
	}

	public Long getAttackTypeID() {
		return attackTypeID;
	}

	public void setAttackTypeID(Long attackTypeID) {
		this.attackTypeID = attackTypeID;
	}

	public Long getFactionID_Defender() {
		return factionID_Defender;
	}

	public void setFactionID_Defender(Long factionID_Defender) {
		this.factionID_Defender = factionID_Defender;
	}

	public Long getJumpshipID() {
		return jumpshipID;
	}

	public void setJumpshipID(Long jumpshipID) {
		this.jumpshipID = jumpshipID;
	}
}
