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
		scope= RolePlayCharacterStatsPOJO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "ROLEPLAY_CHARACTER_STATS", catalog = "C3")
public class RolePlayCharacterStatsPOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	public Long id;

	@Column(name = "RoleplayCharacterId")
	private Long roleplayCharacterId;

	@Column(name = "SeasonId")
	private Long seasonId;

	@Column(name = "AttackId")
	private Long attackId;

	@Column(name = "LeadingPosition")
	private Boolean LeadingPosition;

	@Column(name = "MwoMatchId")
	private String mwoMatchId;

	@Column(name = "MwoKills")
	private Long mwoKills;

	@Column(name = "MWODamage")
	private Long mwoDamage;

	@Column(name = "MWOMatchScore")
	private Long mwoMatchScore;

	@Column(name = "MWOSurvivalPercentage")
	private Long mwoSurvivalPercentage;

	public RolePlayCharacterStatsPOJO(){
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
	public Long getRoleplayCharacterId() {
		return roleplayCharacterId;
	}

	@SuppressWarnings("unused")
	public void setRoleplayCharacterId(Long roleplayCharacterId) {
		this.roleplayCharacterId = roleplayCharacterId;
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
	public Boolean getLeadingPosition() {
		return LeadingPosition;
	}

	@SuppressWarnings("unused")
	public void setLeadingPosition(Boolean leadingPosition) {
		LeadingPosition = leadingPosition;
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
	public Long getMwoKills() {
		return mwoKills;
	}

	@SuppressWarnings("unused")
	public void setMwoKills(Long mwoKills) {
		this.mwoKills = mwoKills;
	}

	@SuppressWarnings("unused")
	public Long getMwoDamage() {
		return mwoDamage;
	}

	@SuppressWarnings("unused")
	public void setMwoDamage(Long mwoDamage) {
		this.mwoDamage = mwoDamage;
	}

	@SuppressWarnings("unused")
	public Long getMwoMatchScore() {
		return mwoMatchScore;
	}

	@SuppressWarnings("unused")
	public void setMwoMatchScore(Long mwoMatchScore) {
		this.mwoMatchScore = mwoMatchScore;
	}

	@SuppressWarnings("unused")
	public Long getMwoSurvivalPercentage() {
		return mwoSurvivalPercentage;
	}

	@SuppressWarnings("unused")
	public void setMwoSurvivalPercentage(Long mwoSurvivalPercentage) {
		this.mwoSurvivalPercentage = mwoSurvivalPercentage;
	}
}
