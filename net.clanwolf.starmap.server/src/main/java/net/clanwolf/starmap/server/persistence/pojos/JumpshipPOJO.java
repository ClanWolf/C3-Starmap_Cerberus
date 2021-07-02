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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= JumpshipPOJO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property="id")
@Entity
@Table(name = "_HH_JUMPSHIP", catalog = "C3")
public class JumpshipPOJO extends Pojo {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "JumpshipName")
	private String jumpshipName;

	@Column(name = "JumpshipFactionID")
	private Long jumpshipFactionID;

	@Column(name = "StarSystemHistory")
	private String starSystemHistory;

	@Column(name = "AttackReady")
	private Boolean attackReady;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = RoutePointPOJO.class)
	@JoinColumn(name = "JumpshipID")
	private List<RoutePointPOJO> routepointList = new ArrayList<>();

	@Column(name = "Level")
	private Long level;

/*@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = RoutePointPOJO.class)
	@JoinColumn(name = "AttackidID")
	private List<AttackPOJO> allAttacks = new ArrayList<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = RoutePointPOJO.class)
	@JoinColumn(name = "AttackidID")
	@Where()
	private List<AttackPOJO> openAttacks = new ArrayList<>();
*/

	@SuppressWarnings("unused")
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	public void setId(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
	public String getJumpshipName() {
		return jumpshipName;
	}

	@SuppressWarnings("unused")
	public void setJumpshipName(String jumpshipName) {
		this.jumpshipName = jumpshipName;
	}

	@SuppressWarnings("unused")
	public Long getJumpshipFactionID() {
		return jumpshipFactionID;
	}

	@SuppressWarnings("unused")
	public void setJumpshipFactionID(Long jumpshipFactionID) {
		this.jumpshipFactionID = jumpshipFactionID;
	}

	@SuppressWarnings("unused")
	public String getStarSystemHistory() {
		return starSystemHistory;
	}

	@SuppressWarnings("unused")
	public void setStarSystemHistory(String starSystemHistory) {
		this.starSystemHistory = starSystemHistory;
	}

	@SuppressWarnings("unused")
	public Boolean getAttackReady() {
		return attackReady;
	}

	@SuppressWarnings("unused")
	public void setAttackReady(Boolean attackReady) {
		this.attackReady = attackReady;
	}

	@SuppressWarnings("unused")
	public List<RoutePointPOJO> getRoutepointList() {
		return routepointList;
	}

	@SuppressWarnings("unused")
	public void setRoutepointList(List<RoutePointPOJO> routepointList) {
		this.routepointList = routepointList;
	}

	@SuppressWarnings("unused")
	public Long getLevel() {
		return level;
	}

	@SuppressWarnings("unused")
	public void setLevel(Long level) {
		this.level = level;
	}
}
