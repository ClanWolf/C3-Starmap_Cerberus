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

import net.clanwolf.starmap.server.persistence.Pojo;

import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "_HH_STARSYSTEMDATA", catalog = "C3")
public class StarSystemDataPOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "StarSystemID")
	private StarSystemPOJO starSystemID;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "FactionID")
	private FactionPOJO factionID;

	@Column(name = "FactionID_Start")
	private Long factionID_Start;

	@Column(name = "Infrastructure")
	private Long infrastructure;

	@Column(name = "Wealth")
	private Long wealth;

	@Column(name = "Veternacy")
	private Long veternacy;

	@Column(name = "Type")
	private Long type;

	@Column(name = "Description")
	private String description;

	@Column(name = "CapitalWorld")
	private Boolean capitalWorld;

	@Column(name = "Active")
	private Boolean active;

	@Column(name = "ActiveInMetaPhase")
	private Long activeInMetaPhase;

	@Column(name = "Level")
	private Long level;

	@Column(name = "LockedUntilRound")
	private Long lockedUntilRound;

	@SuppressWarnings("unused")
	public Long getLockedUntilRound() {
		return lockedUntilRound;
	}

	@SuppressWarnings("unused")
	public void setLockedUntilRound(Long lockedUntilRound) {
		this.lockedUntilRound = lockedUntilRound;
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
	public StarSystemPOJO getStarSystemID() {
		return starSystemID;
	}

	@SuppressWarnings("unused")
	public void setStarSystemID(StarSystemPOJO starSystemID) {
		this.starSystemID = starSystemID;
	}

	@SuppressWarnings("unused")
	public FactionPOJO getFactionID() {
		return factionID;
	}

	@SuppressWarnings("unused")
	public void setFactionID(FactionPOJO factionID) {
		this.factionID = factionID;
	}

	@SuppressWarnings("unused")
	public Long getFactionID_Start() {
		return factionID_Start;
	}

	@SuppressWarnings("unused")
	public void setFactionID_Start(Long factionID_Start) {
		this.factionID_Start = factionID_Start;
	}

	@SuppressWarnings("unused")
	public Long getInfrastructure() {
		return infrastructure;
	}

	@SuppressWarnings("unused")
	public void setInfrastructure(Long infrastructure) {
		this.infrastructure = infrastructure;
	}

	@SuppressWarnings("unused")
	public Long getWealth() {
		return wealth;
	}

	@SuppressWarnings("unused")
	public void setWealth(Long wealth) {
		this.wealth = wealth;
	}

	@SuppressWarnings("unused")
	public Long getVeternacy() {
		return veternacy;
	}

	@SuppressWarnings("unused")
	public void setVeternacy(Long veternacy) {
		this.veternacy = veternacy;
	}

	@SuppressWarnings("unused")
	public Long getType() {
		return type;
	}

	@SuppressWarnings("unused")
	public void setType(Long type) {
		this.type = type;
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
	public Boolean getCapitalWorld() {
		return this.capitalWorld;
	}

	@SuppressWarnings("unused")
	public void setCapitalWorld(Boolean captial) {
		this.capitalWorld = captial;
	}

	@SuppressWarnings("unused")
	public Boolean getActive() {
		return active;
	}

	@SuppressWarnings("unused")
	public void setActive(Boolean active) {
		this.active = active;
	}

	@SuppressWarnings("unused")
	public Long getActiveInMetaPhase() {
		return activeInMetaPhase;
	}

	@SuppressWarnings("unused")
	public void setActiveInMetaPhase(Long activeInMetaPhase) {
		this.activeInMetaPhase = activeInMetaPhase;
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
