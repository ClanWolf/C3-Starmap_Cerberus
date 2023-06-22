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
package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@JsonIdentityInfo(
		scope= AttackPOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
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

	@Column(name = "CharacterID")
	private Long characterID;

	@JoinColumn(name = "StoryID")
	private Long storyID;

	@JoinColumn(name = "LastStoryID")
	private Long lastStoryID;

	@Column(name = "FactionID_Winner")
	private Long factionID_Winner;

	@Column(name = "AttackCostAttacker")
	private Long attackCostAttacker;

	@Column(name = "AttackCostDefender")
	private Long attackCostDefender;

	@Column(name = "FightsStarted")
	private Boolean fightsStarted;

	@Column(name = "ScoreAttackerVictories")
	private Long scoreAttackerVictories;

	@Column(name = "ScoreDefenderVictories")
	private Long scoreDefenderVictories;

	@Column(name = "Remarks")
	private String remarks;

	@Column(name = "ForumThreadLink")
	private String forumThreadLink;

	@Column(name = "ForumThreadId")
	private Long forumThreadId;

	@Column(name = "Updated")
	private Timestamp updated;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, targetEntity = AttackVarsPOJO.class)
	@JoinColumn(name = "AttackID")
	private List<AttackVarsPOJO> attackVarList = new ArrayList<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, targetEntity = AttackCharacterPOJO.class)
	@JoinColumn(name = "AttackID")
	// Without the tag @Fetch hibernate throws a MultipleBagFetchException.
	// https://stackoverflow.com/questions/4334970/hibernate-throws-multiplebagfetchexception-cannot-simultaneously-fetch-multipl/5865605#5865605
	@Fetch(value = FetchMode.SUBSELECT)
	private List<AttackCharacterPOJO> attackCharList = new ArrayList<>();


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
	public Long getFactionID_Winner() {
		return factionID_Winner;
	}

	@SuppressWarnings("unused")
	public void setFactionID_Winner(Long factionID_Winner) {
		this.factionID_Winner = factionID_Winner;
	}

	@SuppressWarnings("unused")
	public Long getId() {
		return id;
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
	public Long getStoryID() {
		return storyID;
	}

	@SuppressWarnings("unused")
	public void setStoryID(Long storyID) {
		this.storyID = storyID;
	}

	@SuppressWarnings("unused")
	public void setId(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
	public Long getSeason() {
		return season;
	}

	@SuppressWarnings("unused")
	public void setSeason(Long season) { this.season = season; }

	@SuppressWarnings("unused")
	public Long getRound() {
		return round;
	}

	@SuppressWarnings("unused")
	public void setRound(Long round) {
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
	public Long getAttackCostAttacker() {
		return this.attackCostAttacker;
	}

	@SuppressWarnings("unused")
	public void setAttackCostAttacker(Long cost) {
		this.attackCostAttacker = cost;
	}

	@SuppressWarnings("unused")
	public Long getAttackedFromStarSystemID() {
		return attackedFromStarSystemID;
	}

	@SuppressWarnings("unused")
	public void setAttackedFromStarSystemID(Long attackedFromStarSystemID) {
		this.attackedFromStarSystemID = attackedFromStarSystemID;
	}

	@SuppressWarnings("unused")
	public Long getAttackTypeID() {
		return attackTypeID;
	}

	@SuppressWarnings("unused")
	public void setAttackTypeID(Long attackTypeID) {
		this.attackTypeID = attackTypeID;
	}

	@SuppressWarnings("unused")
	public Long getFactionID_Defender() {
		return factionID_Defender;
	}

	@SuppressWarnings("unused")
	public void setFactionID_Defender(Long factionID_Defender) {
		this.factionID_Defender = factionID_Defender;
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
	public List<AttackVarsPOJO> getAttackVarList() {
		return attackVarList;
	}

	@SuppressWarnings("unused")
	public void setAttackVarList(List<AttackVarsPOJO> attackVarList) {
		this.attackVarList = attackVarList;
	}

	@SuppressWarnings("unused")
	public Boolean getFightsStarted() {
		return fightsStarted;
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
	public void setFightsStarted(Boolean fightsStarted) {
		this.fightsStarted = fightsStarted;
	}

	@SuppressWarnings("unused")
	public List<AttackCharacterPOJO> getAttackCharList() {
		return attackCharList;
	}

	@SuppressWarnings("unused")
	public void setAttackCharList(List<AttackCharacterPOJO> attackCharList) {
		this.attackCharList = attackCharList;
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
	public Timestamp getUpdated() {
		return updated;
	}

	@SuppressWarnings("unused")
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}
}
