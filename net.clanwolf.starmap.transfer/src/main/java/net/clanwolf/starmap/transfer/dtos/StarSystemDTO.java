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
 * Copyright (c) 2001-2020, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.transfer.dtos;

import net.clanwolf.starmap.transfer.Dto;

import java.math.BigDecimal;
import java.util.HashMap;

public class StarSystemDTO extends Dto {

	private Integer id;
	private String name;
	protected BigDecimal x;
	protected BigDecimal y;
	private String affiliation;
	private String starType1;
	private String starClass;
	private String sarnaLink;
	private String infrastructure;
	private String wealth;
	private String veternacy;
	private String type;
	private HashMap<String, String> maps;

	public String getSarnaLink() {
		return sarnaLink;
	}

	public void setSarnaLink(String sarnaLink) {
		this.sarnaLink = sarnaLink;
	}

	public String getInfrastructure() {
		return infrastructure;
	}

	public void setInfrastructure(String infrastructure) {
		this.infrastructure = infrastructure;
	}

	public String getWealth() {
		return wealth;
	}

	public void setWealth(String wealth) {
		this.wealth = wealth;
	}

	public String getVeternacy() {
		return veternacy;
	}

	public void setVeternacy(String veternacy) {
		this.veternacy = veternacy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public HashMap<String, String> getMaps() {
		return maps;
	}

	public void setMaps(HashMap<String, String> maps) {
		this.maps = maps;
	}

	public String getStarClass() {
		return starClass;
	}

	public void setStarClass(String starClass) {
		this.starClass = starClass;
	}

	public String getStarType1() {
		return starType1;
	}

	public void setStarType1(String starType1) {
		this.starType1 = starType1;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x.doubleValue();
	}

	public void setX(BigDecimal x) {
		this.x = x;
	}

	public double getY() {
		return y.doubleValue();
	}

	public void setY(BigDecimal y) {
		this.y = y;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
