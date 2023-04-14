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
package net.clanwolf.starmap.transfer.dtos;

import net.clanwolf.starmap.transfer.Dto;

public class FactionDTO extends Dto {
	//@Id
	//@GeneratedValue(strategy = IDENTITY)
	//@Column(name = "ID")
	private Long id;
	//@Column(name = "Name_en")
	private String name_en;
	//@Column(name = "Name_de")
	private String name_de;
	//@Column(name = "ShortName")
	private String shortName;
	//@Column(name = "Color")
	private String color;
	//@Column(name = "Logo")
	private String logo;
	//@Column(name = "FactionTypeID")
	private Long FactionTypeID;
	//@Column(name = "Minor")
	private Boolean minor;
	//@Column(name = "Abandoned")
	private Boolean abandoned;
	//@Column(name = "Chaos")
	private Boolean chaos;
	//@Column(name = "Inactive")
	private Boolean inactive;
	//@Column(name = "StartYear")
	private String startYear;
	//@Column(name = "EndYear")
	private String endYear;
	//@Column(name = "MainSystem")
	private String mainSystem;
	//@Column(name = "SarnaLinkFaction")
	private String sarnaLinkFaction;
	//@Column(name = "Canon")
	private Boolean canon;

	@SuppressWarnings("unused")
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	public void setId(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
	public String getName_en() {
		return name_en;
	}

	@SuppressWarnings("unused")
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}

	@SuppressWarnings("unused")
	public String getName_de() {
		return name_de;
	}

	@SuppressWarnings("unused")
	public void setName_de(String name_de) {
		this.name_de = name_de;
	}

	@SuppressWarnings("unused")
	public String getShortName() {
		return shortName;
	}

	@SuppressWarnings("unused")
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@SuppressWarnings("unused")
	public String getColor() {
		return color;
	}

	@SuppressWarnings("unused")
	public void setColor(String color) {
		this.color = color;
	}

	@SuppressWarnings("unused")
	public String getLogo() {
		return logo;
	}

	@SuppressWarnings("unused")
	public void setLogo(String logo) {
		this.logo = logo;
	}

	@SuppressWarnings("unused")
	public Long getFactionTypeID() {
		return FactionTypeID;
	}

	@SuppressWarnings("unused")
	public void setFactionTypeID(Long factionTypeID) {
		FactionTypeID = factionTypeID;
	}

	@SuppressWarnings("unused")
	public Boolean getMinor() {
		return minor;
	}

	@SuppressWarnings("unused")
	public void setMinor(Boolean minor) {
		this.minor = minor;
	}

	@SuppressWarnings("unused")
	public Boolean getAbandoned() {
		return abandoned;
	}

	@SuppressWarnings("unused")
	public void setAbandoned(Boolean abandoned) {
		this.abandoned = abandoned;
	}

	@SuppressWarnings("unused")
	public Boolean getChaos() {
		return chaos;
	}

	@SuppressWarnings("unused")
	public void setChaos(Boolean chaos) {
		this.chaos = chaos;
	}

	@SuppressWarnings("unused")
	public Boolean getInactive() {
		return inactive;
	}

	@SuppressWarnings("unused")
	public void setInactive(Boolean inactive) {
		this.inactive = inactive;
	}

	@SuppressWarnings("unused")
	public String getStartYear() {
		return startYear;
	}

	@SuppressWarnings("unused")
	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	@SuppressWarnings("unused")
	public String getEndYear() {
		return endYear;
	}

	@SuppressWarnings("unused")
	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	@SuppressWarnings("unused")
	public String getMainSystem() {
		return mainSystem;
	}

	@SuppressWarnings("unused")
	public void setMainSystem(String mainSystem) {
		this.mainSystem = mainSystem;
	}

	@SuppressWarnings("unused")
	public String getSarnaLinkFaction() {
		return sarnaLinkFaction;
	}

	@SuppressWarnings("unused")
	public void setSarnaLinkFaction(String sarnaLinkFaction) {
		this.sarnaLinkFaction = sarnaLinkFaction;
	}

	@SuppressWarnings("unused")
	public Boolean getCanon() {
		return canon;
	}

	@SuppressWarnings("unused")
	public void setCanon(Boolean canon) {
		this.canon = canon;
	}

	public String toString() {
		return this.shortName;
	}
}
