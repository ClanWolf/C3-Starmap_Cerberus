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
package net.clanwolf.starmap.client.process.universe;

import javafx.scene.shape.Path;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.transfer.dtos.FactionDTO;
import org.kynosarges.tektosyne.geometry.PointD;

import java.util.ArrayList;
import java.util.Objects;

public class BOFaction implements Comparable<BOFaction> {

	private FactionDTO factionDTO;
	private Path backgroundPath;
	private ArrayList<PointD[]> voronoiRegions = new ArrayList<>();

	@SuppressWarnings("unused")
	public int getSystemCountAll() {
		int i = 0;
		for (BOStarSystem ss : Nexus.getBoUniverse().starSystemBOs.values()) {
			if (ss.getFactionId() == Nexus.getCurrentChar().getFactionId().longValue()) {
				i++;
			}
		}
		return i;
	}

	@SuppressWarnings("unused")
	public int getSystemsCountAttacking() {
		int i = 0;
		for (BOAttack a : Nexus.getBoUniverse().attackBOsOpenInThisRound.values()) {
			if (Objects.equals(a.getAttackerFactionId(), Nexus.getCurrentChar().getFactionId())) {
				i++;
			}
		}
		return i;
	}

	@SuppressWarnings("unused")
	public int getSystemCountDefending() {
		int i = 0;
		for (BOStarSystem ss : Nexus.getBoUniverse().starSystemBOs.values()) {
			if (ss.getFactionId() == Nexus.getCurrentChar().getFactionId().longValue()) {
				if (ss.isCurrentlyAttacked()) {
					i++;
				}
			}
		}
		return i;
	}

	@SuppressWarnings("unused")
	public int getSystemCountRegular() {
		int i = 0;
		for (BOStarSystem ss : Nexus.getBoUniverse().starSystemBOs.values()) {
			if (ss.getFactionId() == Nexus.getCurrentChar().getFactionId().longValue()) {
				if (ss.getLevel() == 1) {
					i++;
				}
			}
		}
		return i;
	}

	@SuppressWarnings("unused")
	public int getSystemCountIndustrial() {
		int i = 0;
		for (BOStarSystem ss : Nexus.getBoUniverse().starSystemBOs.values()) {
			if (ss.getFactionId() == Nexus.getCurrentChar().getFactionId().longValue()) {
				if (ss.getLevel() == 2) {
					i++;
				}
			}
		}
		return i;
	}

	@SuppressWarnings("unused")
	public int getSystemCountCapital() {
		int i = 0;
		for (BOStarSystem ss : Nexus.getBoUniverse().starSystemBOs.values()) {
			if (ss.getFactionId() == Nexus.getCurrentChar().getFactionId().longValue()) {
				if (ss.getLevel() == 3) {
					i++;
				}
			}
		}
		return i;
	}

	public double getIncome() {
		double income = 0;
		income = income + getSystemCountRegular() * Constants.REGULAR_SYSTEM_GENERAL_INCOME;
		income = income + getSystemCountIndustrial() * Constants.INDUSTRIAL_SYSTEM_GENERAL_INCOME;
		income = income + getSystemCountCapital() * Constants.CAPITAL_SYSTEM_GENERAL_INCOME;

		return income;
	}

	public double getCost() {
		double cost = 0;
		// Regular costs
		cost = cost + getSystemCountRegular() * Constants.REGULAR_SYSTEM_GENERAL_COST;
		cost = cost + getSystemCountIndustrial() * Constants.INDUSTRIAL_SYSTEM_GENERAL_COST;
		cost = cost + getSystemCountCapital() * Constants.CAPITAL_SYSTEM_GENERAL_COST;

		// Costs for defending systems
		for (BOStarSystem ss : Nexus.getBoUniverse().starSystemBOs.values()) {
			if (ss.getFactionId() == Nexus.getCurrentChar().getFactionId().longValue()) {
				switch (ss.getLevel().intValue()) {
					case 1 -> { // Regular world
						if (ss.isCurrentlyAttacked()) cost = cost + Constants.REGULAR_SYSTEM_DEFEND_COST;
					}
					case 2 -> { // Industrial world
						if (ss.isCurrentlyAttacked()) cost = cost + Constants.INDUSTRIAL_SYSTEM_DEFEND_COST;
					}
					case 3 -> { // Captial world
						if (ss.isCurrentlyAttacked()) cost = cost + Constants.CAPITAL_SYSTEM_DEFEND_COST;
					}
				}
			}
			// Costs for attacking starsystems
			if (ss.getAttack() != null) {
				if (Objects.equals(ss.getAttack().getAttackerFactionId(), Nexus.getCurrentChar().getFactionId())) {
					switch (ss.getLevel().intValue()) {
						case 1 -> // Regular world
								cost = cost + Constants.REGULAR_SYSTEM_ATTACK_COST;
						case 2 -> // Industrial world
								cost = cost + Constants.INDUSTRIAL_SYSTEM_ATTACK_COST;
						case 3 -> // Captial world
								cost = cost + Constants.CAPITAL_SYSTEM_ATTACK_COST;
					};
				}
			}
		}
		return cost;
	}

	@SuppressWarnings("unused")
	public void clearRegions() {
		this.voronoiRegions.clear();
	}

	@SuppressWarnings("unused")
	public ArrayList<PointD[]> getVoronoiRegions() {
		return voronoiRegions;
	}

	@SuppressWarnings("unused")
	public void setVoronoiRegions(ArrayList<PointD[]> voronoiRegions) {
		this.voronoiRegions = voronoiRegions;
	}

	@SuppressWarnings("unused")
	public void addVoronoiRegion(PointD[] voronoiRegion) {
		this.voronoiRegions.add(voronoiRegion);
	}

	@SuppressWarnings("unused")
	public Path getBackgroundPath() {
		return backgroundPath;
	}

	@SuppressWarnings("unused")
	public void setBackgroundPath(Path backgroundPath) {
		this.backgroundPath = backgroundPath;
	}

	@SuppressWarnings("unused")
	public BOFaction(FactionDTO factionDTO) {
		this.factionDTO = factionDTO;
	}

	@SuppressWarnings("unused")
	public FactionDTO getFactionDTO() {
		return this.factionDTO;
	}

	@SuppressWarnings("unused")
	public String getColor() { return factionDTO.getColor(); }

	@SuppressWarnings("unused")
	public String getLogo() { return factionDTO.getLogo(); }

	@SuppressWarnings("unused")
	public String getShortName() { return factionDTO.getShortName(); }

	@SuppressWarnings("unused")
	public String getName() { return factionDTO.getName_en(); }

	@SuppressWarnings("unused")
	public String getLocalizedName() {
		if ("en".equals(Internationalization.getLanguage())) {
			return factionDTO.getName_en();
		} else {
			return factionDTO.getName_de();
		}
	}

	@SuppressWarnings("unused")
	public Long getID() { return factionDTO.getId();}

	@Override
	public String toString() {
		return getShortName();
	}

	@Override
	public int compareTo(BOFaction f) {
		if (f.getShortName() == null && this.getShortName() == null) {
			return 0;
		}
		if (this.getShortName() == null) {
			return 1;
		}
		if (f.getShortName() == null) {
			return -1;
		}
		return this.getShortName().compareTo(f.getShortName());
	}
}
