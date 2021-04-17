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
package net.clanwolf.starmap.transfer.dtos;

import net.clanwolf.starmap.transfer.Dto;

import java.math.BigDecimal;

public class StarSystemDTO_old extends Dto {

	private Long id;
	private Long starSystemDataId;
	private String name;
	protected BigDecimal x;
	protected BigDecimal y;
	private Long factionId;
	private String affiliation;
	private String starType1;
	private String starClass;
	private String sarnaLink;
	private String infrastructure;
	private String wealth;
	private String veternacy;
	private String type;
	private String systemImageName;
	private String description;
	private Boolean captial;

	@SuppressWarnings("unused")
	public String getDescription() {
		return this.description;
	}

	@SuppressWarnings("unused")
	public void setDescription(String s) {
		this.description = s;
	}

	@SuppressWarnings("unused")
	public Boolean isCaptial() {
		return this.captial;
	}

	@SuppressWarnings("unused")
	public void setCaptial(Boolean b) {
		this.captial = b;
	}

	@SuppressWarnings("unused")
	public void setSystemImageName(String n) {
		systemImageName = n;
	}

	@SuppressWarnings("unused")
	public void setStarSystemDataId(Long id) {
		this.starSystemDataId = id;
	}

	@SuppressWarnings("unused")
	public Long getStarSystemDataId() {
		return this.starSystemDataId;
	}

	@SuppressWarnings("unused")
	public String getSystemImageName() {
		return systemImageName;
	}

	@SuppressWarnings("unused")
	public String getSarnaLink() {
		return sarnaLink;
	}

	@SuppressWarnings("unused")
	public void setSarnaLink(String sarnaLink) {
		this.sarnaLink = sarnaLink;
	}

	@SuppressWarnings("unused")
	public String getInfrastructure() {
		return infrastructure;
	}

	@SuppressWarnings("unused")
	public void setInfrastructure(String infrastructure) {
		this.infrastructure = infrastructure;
	}

	@SuppressWarnings("unused")
	public String getWealth() {
		return wealth;
	}

	@SuppressWarnings("unused")
	public void setWealth(String wealth) {
		this.wealth = wealth;
	}

	@SuppressWarnings("unused")
	public String getVeternacy() {
		return veternacy;
	}

	@SuppressWarnings("unused")
	public void setVeternacy(String veternacy) {
		this.veternacy = veternacy;
	}

	@SuppressWarnings("unused")
	public String getType() {
		return type;
	}

	@SuppressWarnings("unused")
	public void setType(String type) {
		this.type = type;
	}

	@SuppressWarnings("unused")
	public String getStarClass() {
		return starClass;
	}

	@SuppressWarnings("unused")
	public void setStarClass(String starClass) {
		this.starClass = starClass;
	}

	@SuppressWarnings("unused")
	public String getStarType1() {
		return starType1;
	}

	@SuppressWarnings("unused")
	public void setStarType1(String starType1) {
		this.starType1 = starType1;
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
	public String getName() {
		return name;
	}

	@SuppressWarnings("unused")
	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unused")
	public double getX() {
		return x.doubleValue();
	}

	@SuppressWarnings("unused")
	public void setX(BigDecimal x) {
		this.x = x;
	}

	@SuppressWarnings("unused")
	public double getY() {
		return y.doubleValue();
	}

	@SuppressWarnings("unused")
	public void setY(BigDecimal y) {
		this.y = y;
	}

	@SuppressWarnings("unused")
	public String getAffiliation() {
		return affiliation;
	}

	@SuppressWarnings("unused")
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	@SuppressWarnings("unused")
	public Long getFactionId() {
		return factionId;
	}

	@SuppressWarnings("unused")
	public void setFactionId(Long factionId) {
		this.factionId = factionId;
	}

	@Override
	@SuppressWarnings("unused")
	public String toString() {
		return this.name;
	}
}
