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
package net.clanwolf.starmap.client.process.universe;

import javafx.scene.image.Image;
import net.clanwolf.starmap.client.gui.panes.map.tools.GraphManager;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.transfer.dtos.*;
import org.kynosarges.tektosyne.geometry.GeoUtils;
import org.kynosarges.tektosyne.geometry.PointD;
import org.kynosarges.tektosyne.geometry.PolygonLocation;
import org.kynosarges.tektosyne.geometry.VoronoiResults;
import org.kynosarges.tektosyne.subdivision.Subdivision;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class BOUniverse {

	private UniverseDTO universeDTO;

	public HashMap<Integer, BOStarSystem> starSystemBOs = new HashMap<>();
	public HashMap<String, BOFaction> factionBOs = new HashMap<>();
	public HashMap<String, BOJumpship> jumpshipBOs = new HashMap<>();
	public ArrayList<BOAttack> attackBOs = new ArrayList<>();

	public Integer currentSeason;
	public Integer currentRound;
	public String currentDate;

	public VoronoiResults voronoiResults = null;
	public Subdivision delaunaySubdivision = null;
	public GraphManager graphManager = null;

	public BOJumpship currentlyDraggedJumpship = null;

	public BOStarSystem getStarSystemByPoint(PointD p) {
		PointD[] foundRegion = null;
		for (PointD[] region : voronoiResults.voronoiRegions()) {
			PolygonLocation location = GeoUtils.pointInPolygon(p, region);
			if ("INSIDE".equals(location.toString())) {
				foundRegion = region;
			}
		}
		if (foundRegion != null) {
			for (BOStarSystem boSs : starSystemBOs.values()) {
				if (foundRegion.equals(boSs.getVoronoiRegion())) {
					return boSs;
				}
			}
		}
		return null;
	}

	public BOUniverse(UniverseDTO universeDTO) {
		this.universeDTO = universeDTO;

		for (StarSystemDTO ss : universeDTO.starSystems.values()) {
			BOStarSystem boStarSystem = new BOStarSystem(ss);
			starSystemBOs.put(ss.getId(), boStarSystem);
		}

		for (AttackDTO att : universeDTO.attacks) {
			BOAttack boAttack = new BOAttack(att);
			attackBOs.add(boAttack);
		}

		for (FactionDTO factionDTO : universeDTO.factions.values()) {
			BOFaction boFaction = new BOFaction(factionDTO);
			factionBOs.put(factionDTO.getShortName(), boFaction);
		}

		for (JumpshipDTO jumpshipDTO : universeDTO.jumpships.values()) {
			BOJumpship boJumpship = new BOJumpship(jumpshipDTO);
			jumpshipBOs.put(jumpshipDTO.getShipName(), boJumpship);
		}

		currentSeason = universeDTO.currentSeason;
		currentRound = universeDTO.currentRound;
		currentDate = universeDTO.currentDate;
	}

	public ArrayList<BOFaction> getFactionList(){
		ArrayList<BOFaction> factionList = new ArrayList<BOFaction>(factionBOs.values());
		return factionList;
	}

	public BOFaction getFactionByID(Long id){
		for(BOFaction faction : Nexus.getBoUniverse().getFactionList()){
			if(faction.getID().equals(id))
				return faction;
		}
		return null;
	}
}
