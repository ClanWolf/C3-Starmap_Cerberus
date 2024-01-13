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

import net.clanwolf.starmap.transfer.Dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
	private Long characterID;
	private Long storyID;
	private Long lastStoryID;
	private Long factionID_Winner;
	private Boolean fightsStarted;
	private Long attackCostAttacker;
	private Long attackCostDefender;
	private Long scoreAttackerVictories;
	private Long scoreDefenderVictories;
	private String remarks;
	private String forumThreadLink;
	private Long forumThreadId;
	private Timestamp updated;
	private List<AttackVarsDTO> attackVarList = new ArrayList<>();
	private List<AttackCharacterDTO> attackCharList;

	@SuppressWarnings("unused")
	public Long getCharacterID() {
		return characterID;
	}

	@SuppressWarnings("unused")
	public void setCharacterID(Long characterID) {
		this.characterID = characterID;
	}

	@SuppressWarnings("unused")
	public Long getStoryID() {
		return storyID;
	}

	@SuppressWarnings("unused")
	public void setStoryID(Long storyID) {
		this.storyID = storyID;
	}

	@SuppressWarnings("unused")
	public Long getFactionID_Winner() {
		return factionID_Winner;
	}

	@SuppressWarnings("unused")
	public void setFactionID_Winner(Long factionID_Winner) {
		this.factionID_Winner = factionID_Winner;
	}

	@SuppressWarnings("unused")
	public String getRemarks() {
		return remarks;
	}

	@SuppressWarnings("unused")
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@SuppressWarnings("unused")
	public String getForumThreadLink() {
		return forumThreadLink;
	}

	@SuppressWarnings("unused")
	public void setForumThreadLink(String forumThreadLink) {
		this.forumThreadLink = forumThreadLink;
	}

	@SuppressWarnings("unused")
	public Long getForumThreadId() {
		return forumThreadId;
	}

	@SuppressWarnings("unused")
	public void setForumThreadId(Long forumThreadId) {
		this.forumThreadId = forumThreadId;
	}

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
	public Long getAttackCostAttacker() {
		return this.attackCostAttacker;
	}

	@SuppressWarnings("unused")
	public void setAttackCostAttacker(Long cost) {
		this.attackCostAttacker = cost;
	}

	@SuppressWarnings("unused")
	public void setStarSystemDataID(Long starSystemDataID) {
		this.starSystemDataID = starSystemDataID;
	}

	@SuppressWarnings("unused")
	public Long getFactionID_Defender() {
		return factionID_Defender;
	}

	@SuppressWarnings("unused")
	public void setFactionID_Defender(Long defenderID) {
		this.factionID_Defender = defenderID;
	}

	@SuppressWarnings("unused")
	public List<AttackVarsDTO> getAttackVarList() {
		return attackVarList;
	}

	@SuppressWarnings("unused")
	public void setAttackVarList(List<AttackVarsDTO> attackVarList) {
		this.attackVarList = attackVarList;
	}

	@SuppressWarnings("unused")
	public List<AttackCharacterDTO> getAttackCharList() {
		return attackCharList;
	}

	@SuppressWarnings("unused")
	public void setAttackCharList(List<AttackCharacterDTO> attackCharList) {
		this.attackCharList = attackCharList;
	}

	@SuppressWarnings("unused")
	public Boolean getFightsStarted() {
		return fightsStarted;
	}

	@SuppressWarnings("unused")
	public void setFightsStarted(Boolean fightsStarted) {
		this.fightsStarted = fightsStarted;
	}


	@SuppressWarnings("unused")
	public Long getLastStoryID() {
		return lastStoryID;
	}

	@SuppressWarnings("unused")
	public void setLastStoryID(Long lastStoryID) {
		this.lastStoryID = lastStoryID;
	}

	@SuppressWarnings("unused")
	public Long getScoreAttackerVictories() {
		return scoreAttackerVictories;
	}

	@SuppressWarnings("unused")
	public void setScoreAttackerVictories(Long scoreAttackerVictories) {
		this.scoreAttackerVictories = scoreAttackerVictories;
	}

	@SuppressWarnings("unused")
	public Long getScoreDefenderVictories() {
		return scoreDefenderVictories;
	}

	@SuppressWarnings("unused")
	public void setScoreDefenderVictories(Long scoreDefenderVictories) {
		this.scoreDefenderVictories = scoreDefenderVictories;
	}

	@SuppressWarnings("unused")
	public Long getAttackCostDefender() {
		return attackCostDefender;
	}

	@SuppressWarnings("unused")
	public void setAttackCostDefender(Long attackCostDefender) {
		this.attackCostDefender = attackCostDefender;
	}

	@SuppressWarnings("unused")
	public Timestamp getUpdated() {
		return updated;
	}

	@SuppressWarnings("unused")
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public AttackDTO() {
		// empty constructor
	}

	public AttackDTO(long id) {
		this.id = id;
	}
}
