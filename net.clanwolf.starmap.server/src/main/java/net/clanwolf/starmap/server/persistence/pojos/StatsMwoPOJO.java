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
import static jakarta.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= StatsMwoPOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "STATS_MWO", catalog = "C3")
public class StatsMwoPOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "SeasonId")
	private Long seasonId;

	@Column(name = "AttackId")
	private Long attackId;

	@Column(name = "GameId")
	private String gameId;

	@Column(name = "RoleplayId")
	private Long roleplayId;

	@Column(name = "RawData")
	private String rawData;

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
	public String getGameId() {
		return gameId;
	}

	@SuppressWarnings("unused")
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	@SuppressWarnings("unused")
	public String getRawData() {
		return rawData;
	}

	@SuppressWarnings("unused")
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	@SuppressWarnings("unused")
	public Long getRoleplayId() {
		return roleplayId;
	}

	@SuppressWarnings("unused")
	public void setRoleplayId(Long roleplayId) {
		this.roleplayId = roleplayId;
	}
}
