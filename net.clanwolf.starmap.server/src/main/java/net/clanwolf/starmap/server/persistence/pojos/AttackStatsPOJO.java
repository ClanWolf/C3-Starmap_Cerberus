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
package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author Undertaker
 *
 */
@JsonIdentityInfo(
		scope= AttackStatsPOJO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "_HH_ATTACK_STATS", catalog = "C3")
public class AttackStatsPOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	public Long id;

	@Column(name = "SeasonID")
	private Long seasonId;

	@Column(name = "AttackID")
	private Long attackId;

	@Column(name = "DropID")
	private String dropId;

	@Column(name = "MwoMatchID")
	private String mwoMatchId;

	@Column(name = "StarSystemDataId")
	private Long starSystemDataId;

	@Column(name = "AttackerFactionId")
	private Long attackerFactionId;

	@Column(name = "DefenderFactionId")
	private Long defenderFactionId;

	@Column(name = "WinnerFactionId")
	private Long winnerFactionId;

	@Column(name = "AttackerTonnage")
	private Long attackerTonnage;

	@Column(name = "DefenderTonnage")
	private Long defenderTonnage;

	@Column(name = "AttackerKillCount")
	private Long attackerKillCount;

	@Column(name = "DefenderKillCount")
	private Long defenderKillCount;

	@Column(name = "AttackerLostTonnage")
	private Long attackerLostTonnage;

	@Column(name = "DefenderLostTonnage")
	private Long defenderLostTonnage;

	public AttackStatsPOJO(){
		//
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
	public Long getSeasonId() {
		return seasonId;
	}

	@SuppressWarnings("unused")
	public void setSeasonId(Long seasonId) {
		this.seasonId = seasonId;
	}

	@SuppressWarnings("unused")
	public Long getAttackId() {
		return attackId;
	}

	@SuppressWarnings("unused")
	public void setAttackId(Long attackId) {
		this.attackId = attackId;
	}

	@SuppressWarnings("unused")
	public String getDropId() {
		return dropId;
	}

	@SuppressWarnings("unused")
	public void setDropId(String dropId) {
		this.dropId = dropId;
	}

	@SuppressWarnings("unused")
	public String getMwoMatchId() {
		return mwoMatchId;
	}

	@SuppressWarnings("unused")
	public void setMwoMatchId(String mwoMatchId) {
		this.mwoMatchId = mwoMatchId;
	}

	@SuppressWarnings("unused")
	public Long getStarSystemDataId() {
		return starSystemDataId;
	}

	@SuppressWarnings("unused")
	public void setStarSystemDataId(Long starSystemDataId) {
		this.starSystemDataId = starSystemDataId;
	}

	@SuppressWarnings("unused")
	public Long getAttackerFactionId() {
		return attackerFactionId;
	}

	@SuppressWarnings("unused")
	public void setAttackerFactionId(Long attackerFactionId) {
		this.attackerFactionId = attackerFactionId;
	}

	@SuppressWarnings("unused")
	public Long getDefenderFactionId() {
		return defenderFactionId;
	}

	@SuppressWarnings("unused")
	public void setDefenderFactionId(Long defenderFactionId) {
		this.defenderFactionId = defenderFactionId;
	}

	@SuppressWarnings("unused")
	public Long getWinnerFactionId() {
		return winnerFactionId;
	}

	@SuppressWarnings("unused")
	public void setWinnerFactionId(Long winnerFactionId) {
		this.winnerFactionId = winnerFactionId;
	}

	@SuppressWarnings("unused")
	public Long getAttackerTonnage() {
		return attackerTonnage;
	}

	@SuppressWarnings("unused")
	public void setAttackerTonnage(Long attackerTonnage) {
		this.attackerTonnage = attackerTonnage;
	}

	@SuppressWarnings("unused")
	public Long getDefenderTonnage() {
		return defenderTonnage;
	}

	@SuppressWarnings("unused")
	public void setDefenderTonnage(Long defenderTonnage) {
		this.defenderTonnage = defenderTonnage;
	}

	@SuppressWarnings("unused")
	public Long getAttackerKillCount() {
		return attackerKillCount;
	}

	@SuppressWarnings("unused")
	public void setAttackerKillCount(Long attackerKillCount) {
		this.attackerKillCount = attackerKillCount;
	}

	@SuppressWarnings("unused")
	public Long getDefenderKillCount() {
		return defenderKillCount;
	}

	@SuppressWarnings("unused")
	public void setDefenderKillCount(Long defenderKillCount) {
		this.defenderKillCount = defenderKillCount;
	}

	@SuppressWarnings("unused")
	public Long getAttackerLostTonnage() {
		return attackerLostTonnage;
	}

	@SuppressWarnings("unused")
	public void setAttackerLostTonnage(Long attackerLostTonnage) {
		this.attackerLostTonnage = attackerLostTonnage;
	}

	@SuppressWarnings("unused")
	public Long getDefenderLostTonnage() {
		return defenderLostTonnage;
	}

	@SuppressWarnings("unused")
	public void setDefenderLostTonnage(Long defenderLostTonnage) {
		this.defenderLostTonnage = defenderLostTonnage;
	}
}