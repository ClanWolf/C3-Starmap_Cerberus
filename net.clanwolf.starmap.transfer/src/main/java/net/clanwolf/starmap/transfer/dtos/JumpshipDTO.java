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

import java.util.ArrayList;

public class JumpshipDTO extends Dto {

	private String shipName = "";
	private String starSystemHistory = "";
	private Integer shipID = 0;
	private Integer factionID = 0;
	private Integer lastMovedInRound = 0;
	private boolean movedInCurrentRound = false;
	private boolean attackReady;
	private Integer currentSystemID = null;

	public ArrayList<Integer> getStarSystemHistoryArray() {
		ArrayList<Integer> hist = null;
		if (!"".equals(starSystemHistory)) {
			if (starSystemHistory.endsWith(";")) {
				starSystemHistory = starSystemHistory.substring(0, starSystemHistory.length() - 1);
			}
			if (!starSystemHistory.contains(";")) {
				try {
					currentSystemID = Integer.parseInt(starSystemHistory);
				} catch (NumberFormatException nfe) {
					// not a valid id
				}
			} else {
				String[] hs = starSystemHistory.split(";");
				hist = new ArrayList<>();
				for (String s : hs) {
					try {
						Integer i = Integer.parseInt(s);
						hist.add(i);
						currentSystemID = i;
					} catch (NumberFormatException nfe) {
						// not a valid id
					}
				}
			}
		}
		return hist;
	}

	@SuppressWarnings("unused")
	public Integer getCurrentSystemID() {
		if (currentSystemID == null) {
			ArrayList<Integer> hist = getStarSystemHistoryArray();
		}
		return currentSystemID;
	}

	@SuppressWarnings("unused")
	public boolean isAttackReady() {
		return attackReady;
	}

	@SuppressWarnings("unused")
	public void setAttackReady(boolean attackReady) {
		this.attackReady = attackReady;
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
	public Integer getLastMovedInRound() {
		return lastMovedInRound;
	}

	@SuppressWarnings("unused")
	public void setLastMovedInRound(Integer lastMovedInRound) {
		this.lastMovedInRound = lastMovedInRound;
	}

	@SuppressWarnings("unused")
	public Integer getFactionID() {
		return factionID;
	}

	@SuppressWarnings("unused")
	public void setFactionID(Integer factionID) {
		this.factionID = factionID;
	}

	@SuppressWarnings("unused")
	public boolean isMovedInCurrentRound() {
		return movedInCurrentRound;
	}

	@SuppressWarnings("unused")
	public void setMovedInCurrentRound(boolean movedInCurrentRound) {
		this.movedInCurrentRound = movedInCurrentRound;
	}

	@SuppressWarnings("unused")
	public Integer getShipID() {
		return shipID;
	}

	@SuppressWarnings("unused")
	public void setShipID(Integer shipID) {
		this.shipID = shipID;
	}

	@SuppressWarnings("unused")
	public String getShipName() {
		return shipName;
	}

	@SuppressWarnings("unused")
	public void setShipName(String shipName) {
		this.shipName = shipName;
	}
}
