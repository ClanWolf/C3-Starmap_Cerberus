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
package net.clanwolf.starmap.transfer.dtos;

import net.clanwolf.starmap.transfer.Dto;

import java.util.ArrayList;
import java.util.List;

public class JumpshipDTO extends Dto {

	//@Id
	//@GeneratedValue(strategy = IDENTITY)
	//@Column(name = "ID")
	private Long id;

	//@Column(name = "JumpshipName")
	private String jumpshipName;

	//@Column(name = "UnitName")
	private String unitName;

	//@Column(name = "DropshipNames")
	private String dropshipNames;

	//@Column(name = "JumpshipFactionID")
	private Long jumpshipFactionID;

	//@Column(name = "HomeSystemID")
	private Long homeSystemID;

	//@Column(name = "StarSystemHistory")
	private String starSystemHistory;

	//@Column(name = "AttackReady")
	private Boolean attackReady;

	//@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = RoutePointPOJO.class)
	//@JoinColumn(name = "JumpshipID")
	private List<RoutePointDTO> routepointList = new ArrayList<>();

	//@Column(name = "UnitXP")
	private Long unitXP;

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
	public String getUnitName() {
		return unitName;
	}

	@SuppressWarnings("unused")
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@SuppressWarnings("unused")
	public String getDropshipNames() {
		return dropshipNames;
	}

	@SuppressWarnings("unused")
	public void setDropshipNames(String dropshipNames) {
		this.dropshipNames = dropshipNames;
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
	public Long getHomeSystemID() {
		return homeSystemID;
	}

	@SuppressWarnings("unused")
	public void setHomeSystemID(Long homeSystemID) {
		this.homeSystemID = homeSystemID;
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
	public List<RoutePointDTO> getRoutepointList() {
		return routepointList;
	}

	@SuppressWarnings("unused")
	public void setRoutepointList(List<RoutePointDTO> routepointList) {
		this.routepointList = routepointList;
	}

	@SuppressWarnings("unused")
	public Long getUnitXP() {
		return unitXP;
	}

	@SuppressWarnings("unused")
	public void setUnitXP(Long unitXP) {
		this.unitXP = unitXP;
	}

	/*public ArrayList<Long> getStarSystemHistoryArray() {
		Long currentSystemID;
		ArrayList<Long> hist = null;
		if (starSystemHistory != null && !"".equals(starSystemHistory)) {
			if (starSystemHistory.endsWith(";")) {
				starSystemHistory = starSystemHistory.substring(0, starSystemHistory.length() - 1);
			}
			if (!starSystemHistory.contains(";")) {
				try {
					currentSystemID = Long.parseLong(starSystemHistory);
				} catch (NumberFormatException nfe) {
					// not a valid id
				}
			} else {
				String[] hs = starSystemHistory.split(";");
				hist = new ArrayList<>();
				for (String s : hs) {
					try {
						Long i = Long.parseLong(s);
						hist.add(i);
						currentSystemID = i;
					} catch (NumberFormatException nfe) {
						// not a valid id
					}
				}
			}
		}
		return hist;
	}*/
}
