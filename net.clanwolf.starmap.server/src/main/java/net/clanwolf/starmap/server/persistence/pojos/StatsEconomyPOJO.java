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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import net.clanwolf.starmap.server.persistence.Pojo;

import static jakarta.persistence.GenerationType.IDENTITY;


@JsonIdentityInfo(scope = StatsEconomyPOJO.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "STATS_ECONOMY", catalog = "C3")
public class StatsEconomyPOJO extends Pojo {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "SeasonId")
	private Long season;

	@Column(name = "RoundId")
	private Long round;

	@Column(name = "FactionId")
	private Long factionID;

	@Column(name = "AttackId")
	private Long attackID;

	@Column(name = "StarSystemDataID")
	private Long starSystemDataID;

	@Column(name = "Income")
	private Long income;

	@Column(name = "Cost")
	private Long cost;

	@Column(name = "CostEnhancement")
	private Long costEnhancement;

	@SuppressWarnings("unused")
	public Long getId() {
		return id;
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
	public void setSeason(Long season) {
		this.season = season;
	}

	@SuppressWarnings("unused")
	public Long getRound() {
		return round;
	}

	@SuppressWarnings("unused")
	public void setRound(Long round) {
		this.round = round;
	}

	@SuppressWarnings("unused")
	public Long getFactionID() {
		return factionID;
	}

	@SuppressWarnings("unused")
	public void setFactionID(Long factionID) {
		this.factionID = factionID;
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
	public Long getStarSystemDataID() {
		return starSystemDataID;
	}

	@SuppressWarnings("unused")
	public void setStarSystemDataID(Long starSystemDataID) {
		this.starSystemDataID = starSystemDataID;
	}

	@SuppressWarnings("unused")
	public Long getIncome() {
		return income;
	}

	@SuppressWarnings("unused")
	public void setIncome(Long income) {
		this.income = income;
	}

	@SuppressWarnings("unused")
	public Long getCost() {
		return cost;
	}

	@SuppressWarnings("unused")
	public void setCost(Long cost) {
		this.cost = cost;
	}

	@SuppressWarnings("unused")
	public Long getCostEnhancement() {
		return costEnhancement;
	}

	@SuppressWarnings("unused")
	public void setCostEnhancement(Long costEnhancement) {
		this.costEnhancement = costEnhancement;
	}
}
