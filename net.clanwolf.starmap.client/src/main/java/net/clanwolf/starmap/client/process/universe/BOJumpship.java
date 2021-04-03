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

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import net.clanwolf.starmap.client.gui.panes.map.tools.RouteCalculator;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.JumpshipDTO;
import net.clanwolf.starmap.transfer.dtos.RoutePointDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.util.ArrayList;
import java.util.List;

public class BOJumpship {

	private JumpshipDTO jumpshipDTO;
	private ImageView jumpshipImage;
	private Line predictedRouteLine = null;
	private List<BOStarSystem> routeSystems = null;
	private ArrayList<RoutePointDTO> route = null;
	public Group routeLines = null;

	@SuppressWarnings("unused")
	public Line getPredictedRouteLine() {
		if (predictedRouteLine == null) {
			predictedRouteLine = new Line();
			predictedRouteLine.setStrokeWidth(3);
			predictedRouteLine.setStroke(Color.ORANGE);
			predictedRouteLine.setStrokeLineCap(StrokeLineCap.ROUND);
		}
		return predictedRouteLine;
	}

	public void storeRouteToDatabase(ArrayList<RoutePointDTO> route) {
		GameState saveRouteState = new GameState();
		saveRouteState.setMode(GAMESTATEMODES.ROUTE_SAVE);
		saveRouteState.addObject(route);
		Nexus.fireNetworkEvent(saveRouteState);
	}

	@SuppressWarnings("unused")
	public void setRouteSystems(List<BOStarSystem> routeSystems) {
		this.routeSystems = routeSystems;
		if (route == null) {
			route = new ArrayList<RoutePointDTO>();
		}
		route.clear();
		int dist = 0;
		C3Logger.info("Setting route for Jumpship '" + jumpshipDTO.getJumpshipName() + "':");
		for (BOStarSystem s : routeSystems) {
			int round = Nexus.getBoUniverse().currentRound + dist;
			RoutePointDTO rp = new RoutePointDTO();
			rp.setSystemId(Long.valueOf(s.getId()));
			rp.setJumpshipId(jumpshipDTO.getID());
			rp.setSeasonId(Long.valueOf(Nexus.getBoUniverse().currentSeason));
			rp.setRoundId(Long.valueOf(round));
			route.add(rp);
			C3Logger.info("--- RoutePoint: " + s.getName() + " (in round " + round + ")");
			dist++;
		}
		C3Logger.info("Route set.");
		Nexus.getBoUniverse().routesList.remove(jumpshipDTO.getID());
		Nexus.getBoUniverse().routesList.put(jumpshipDTO.getID(), route);
	}

	@SuppressWarnings("unused")
	public List<RoutePointDTO> getRoute() {
		return route;
	}

	@SuppressWarnings("unused")
	public void setRoute(ArrayList<RoutePointDTO> route) {
		this.route = route;
	}

	@SuppressWarnings("unused")
	public List<BOStarSystem> getRouteSystems() {
		return this.routeSystems;
	}

	@SuppressWarnings("unused")
	public void setPredictedRouteLine(Line predictedRouteLine) {
		this.predictedRouteLine = predictedRouteLine;
	}

	@SuppressWarnings("unused")
	public ImageView getJumpshipImage() {
		return jumpshipImage;
	}

	@SuppressWarnings("unused")
	public void setJumpshipImage(ImageView jumpshipImage) {
		this.jumpshipImage = jumpshipImage;
	}

	@SuppressWarnings("unused")
	public Long getJumpshipId() {
		return this.jumpshipDTO.getID();
	}

	@SuppressWarnings("unused")
	public BOJumpship(JumpshipDTO jumpshipDTO) {
		this.jumpshipDTO = jumpshipDTO;
	}

	@SuppressWarnings("unused")
	public long getJumpshipFaction() {
		return this.jumpshipDTO.getJumpshipFactionID();
	}

	@SuppressWarnings("unused")
	public Long getCurrentSystemID() {
		return jumpshipDTO.getCurrentSystemID();
	}

	@SuppressWarnings("unused")
	public BOStarSystem getCurrentSystem(long id) {
		return (BOStarSystem) Nexus.getBoUniverse().starSystemBOs.get(id);
	}

	@SuppressWarnings("unused")
	public String getJumpshipName() {
		return jumpshipDTO.getJumpshipName();
	}

	@SuppressWarnings("unused")
	public boolean isAttackReady() { return jumpshipDTO.isAttackReady(); }

	@SuppressWarnings("unused")
	public ArrayList<Long> getStarSystemHistoryArray() { return jumpshipDTO.getStarSystemHistoryArray(); }
}
