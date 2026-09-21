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
 * Copyright (c) 2001-2026, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import net.clanwolf.starmap.server.persistence.Pojo;

import java.sql.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= ScorePOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "c3_SCORE", catalog = "cwg")
public class ScorePOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "SeasonId")
	private Long seasonId;

	@Column(name = "FactionId")
	private Long factionId;

	@Column(name = "AttackId")
	private Long attackId;

	@Column(name = "Description")
	private String description;

	@Column(name = "PCP")
	private Long pcp;

	@Column(name = "VP")
	private Long vp;

	public Long getId() {
		return id;
	}

	@Override
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
	public Long getFactionId() {
		return factionId;
	}

	@SuppressWarnings("unused")
	public void setFactionId(Long factionId) {
		this.factionId = factionId;
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
	public String getDescription() {
		return description;
	}

	@SuppressWarnings("unused")
	public void setDescription(String description) {
		this.description = description;
	}

	@SuppressWarnings("unused")
	public Long getPcp() {
		return pcp;
	}

	@SuppressWarnings("unused")
	public void setPcp(Long pcp) {
		this.pcp = pcp;
	}

	@SuppressWarnings("unused")
	public Long getVp() {
		return vp;
	}

	@SuppressWarnings("unused")
	public void setVp(Long vp) {
		this.vp = vp;
	}
}
