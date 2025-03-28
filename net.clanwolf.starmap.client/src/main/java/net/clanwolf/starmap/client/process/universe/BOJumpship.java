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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.process.universe;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import net.clanwolf.starmap.client.nexus.Nexus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.JumpshipDTO;
import net.clanwolf.starmap.transfer.dtos.RoutePointDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class BOJumpship implements Comparable<BOJumpship> {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private JumpshipDTO jumpshipDTO;
	private ImageView jumpshipImage;
	private Label jumpshipLevelLabel;
	private Line predictedRouteLine = null;
	private List<BOStarSystem> routeSystems = null;
	private ArrayList<RoutePointDTO> route = null;
	public Group routeLines = null;
	private Long currentSystemID;

	@SuppressWarnings("unused")
	public void setJumpshipDTO(JumpshipDTO jumpshipDTO) {
		this.jumpshipDTO = jumpshipDTO;
	}

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

	@SuppressWarnings("unused")
	public Long getLevel() {
		long calculatedLevel = ((Double)Math.ceil(jumpshipDTO.getUnitXP() / 300.0)).longValue();
		return calculatedLevel + 1;
	}

	@SuppressWarnings("unused")
	public void storeJumpship(JumpshipDTO jsDto) {
		GameState saveJS = new GameState();
		saveJS.setMode(GAMESTATEMODES.JUMPSHIP_SAVE);
		saveJS.addObject(jsDto);
		Nexus.fireNetworkEvent(saveJS);
	}

	@SuppressWarnings("unused")
	public void setRouteSystems(List<BOStarSystem> routeSystems) {
		if (routeSystems.size() > 0) {
			this.routeSystems = routeSystems;
			if (route == null) {
				route = new ArrayList<>();
			}
			route.clear();
			int dist = 0;
			logger.info("Setting route for Jumpship '" + jumpshipDTO.getJumpshipName() + "':");
			for (BOStarSystem s : routeSystems) {
				int round = Nexus.getBoUniverse().currentRound + dist;
				RoutePointDTO rp = new RoutePointDTO();
				rp.setSystemId(s.getStarSystemId());
				rp.setJumpshipId(jumpshipDTO.getId());
				rp.setSeasonId(Long.valueOf(Nexus.getBoUniverse().currentSeason));
				rp.setRoundId((long) round);
				rp.setCharacterID(Nexus.getCurrentChar().getId());
				route.add(rp);
				logger.info("--- RoutePoint: " + s.getName() + " (in round " + round + ")");
				dist++;
			}
			logger.info("Route set.");
			Nexus.getBoUniverse().routesList.remove(jumpshipDTO.getId());
			Nexus.getBoUniverse().routesList.put(jumpshipDTO.getId(), route);
		} else {
			logger.info("Route was empty, nothing was set.");
			Nexus.getBoUniverse().routesList.remove(jumpshipDTO.getId());
		}
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
	public List<BOStarSystem> getStoredRoute() {
		List<RoutePointDTO> storedSystems = this.jumpshipDTO.getRoutepointList();
		List<BOStarSystem> storedSystemBos = new ArrayList<>();
		for (RoutePointDTO rp : storedSystems) {
			BOStarSystem sbo = Nexus.getBoUniverse().starSystemBOs.get(rp.getSystemId());
			storedSystemBos.add(sbo);
		}
		return storedSystemBos;
	}

	@SuppressWarnings("unused")
	public void setPredictedRouteLine(Line predictedRouteLine) {
		this.predictedRouteLine = predictedRouteLine;
	}

	@SuppressWarnings("unused")
	public ImageView getJumpshipImageView() {
		return jumpshipImage;
	}

	@SuppressWarnings("unused")
	public Label getJumpshipLevelLabel() {
		return jumpshipLevelLabel;
	}

	@SuppressWarnings("unused")
	public void setJumpshipLevelLabel(Label jumpshipLevelLabel) {
		this.jumpshipLevelLabel = jumpshipLevelLabel;
	}

	@SuppressWarnings("unused")
	public void setJumpshipImageView(ImageView jumpshipImage) {
		this.jumpshipImage = jumpshipImage;
	}

	@SuppressWarnings("unused")
	public void setJumpshipImage(Image i) {
		this.jumpshipImage.setImage(i);
	}

	@SuppressWarnings("unused")
	public Long getJumpshipId() {
		return this.jumpshipDTO.getId();
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
	public ArrayList<Long> getStarSystemHistoryArray() {
		String starSystemHistory = jumpshipDTO.getStarSystemHistory();
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
	}

	@SuppressWarnings("unused")
	public Long getCurrentSystemID() {
		if (currentSystemID == null) {
			ArrayList<Long> hist = getStarSystemHistoryArray();
		}
		return currentSystemID;
	}

	@SuppressWarnings("unused")
	public BOStarSystem getCurrentSystem(long id) {
		return Nexus.getBoUniverse().starSystemBOs.get(id);
	}

	@SuppressWarnings("unused")
	public void setCurrentSystemID(Long id) {
		currentSystemID = id;
	}

	@SuppressWarnings("unused")
	public String getJumpshipName() {
		return jumpshipDTO.getJumpshipName();
	}

	@SuppressWarnings("unused")
	public void setAttackReady(boolean s) {
		jumpshipDTO.setAttackReady(s);
	}

	@SuppressWarnings("unused")
	public boolean isAttackReady() { return jumpshipDTO.getAttackReady(); }

	@SuppressWarnings("unused")
	public JumpshipDTO getJumpshipDTO(){ return jumpshipDTO;}

	@Override
	public int compareTo(BOJumpship o) {
		return getJumpshipName().compareTo(o.getJumpshipName());
	}
}
