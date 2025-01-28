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

import java.sql.Timestamp;

import static jakarta.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= RoundPOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "_HH_ROUND", catalog = "C3")
public class RoundPOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Round")
	private Long round;

	@Column(name = "RoundPhase")
	private Long roundPhase;

	@Column(name = "Season")
	private Long season;

	@Column(name = "CurrentRoundStartDate")
	private String currentRoundStartDate;

	@Column(name = "CurrentRoundStartDateRealTime")
	private String currentRoundStartDateRealTime;

	@SuppressWarnings("unused")
	public String getCurrentRoundStartDate() {
		return currentRoundStartDate;
	}

	@SuppressWarnings("unused")
	public void setCurrentRoundStartDate(String currentRoundStartDate) {
		this.currentRoundStartDate = currentRoundStartDate;
	}

	@SuppressWarnings("unused")
	public String getCurrentRoundStartDateRealTime() {
		return currentRoundStartDateRealTime;
	}

	@SuppressWarnings("unused")
	public void setCurrentRoundStartDateRealTime(String currentRoundStartDateRealTime) {
		this.currentRoundStartDateRealTime = currentRoundStartDateRealTime;
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
	public Long getRound() {
		return round;
	}

	@SuppressWarnings("unused")
	public void setRound(Long round) {
		this.round = round;
	}

	@SuppressWarnings("unused")
	public Long getRoundPhase() {
		return roundPhase;
	}

	@SuppressWarnings("unused")
	public void setRoundPhase(Long roundPhase) {
		this.roundPhase = roundPhase;
	}

	@SuppressWarnings("unused")
	public Long getSeason() {
		return season;
	}

	@SuppressWarnings("unused")
	public void setSeason(Long season) {
		this.season = season;
	}
}
