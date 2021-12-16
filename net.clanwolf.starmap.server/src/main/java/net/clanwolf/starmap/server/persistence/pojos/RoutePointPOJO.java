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

@JsonIdentityInfo(
		scope= RoutePointPOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property="id")
@Entity
@Table(name = "_HH_ROUTEPOINT", catalog = "C3")
public class RoutePointPOJO extends Pojo {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "SeasonID")
	private Long seasonId;

	@Column(name = "RoundID")
	private Long roundId;

	@Column(name = "JumpshipID")
	private Long jumpshipId;

	@Column(name = "SystemID")
	private Long systemId;

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
	public void setSeasonId(Long seasonID) {
		this.seasonId = seasonID;
	}

	@SuppressWarnings("unused")
	public Long getRoundId() {
		return roundId;
	}

	@SuppressWarnings("unused")
	public void setRoundId(Long roundID) {
		this.roundId = roundID;
	}

	@SuppressWarnings("unused")
	public Long getJumpshipId() {
		return jumpshipId;
	}

	@SuppressWarnings("unused")
	public void setJumpshipId(Long jumpshipId) {
		this.jumpshipId = jumpshipId;
	}

	@SuppressWarnings("unused")
	public Long getSystemId() {
		return systemId;
	}

	@SuppressWarnings("unused")
	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}
}
