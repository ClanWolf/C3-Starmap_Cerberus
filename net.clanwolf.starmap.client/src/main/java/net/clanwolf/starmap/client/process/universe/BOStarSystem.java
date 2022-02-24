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
package net.clanwolf.starmap.client.process.universe;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import net.clanwolf.starmap.client.gui.panes.map.Config;
import net.clanwolf.starmap.client.nexus.Nexus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.transfer.dtos.StarSystemDataDTO;
import org.kynosarges.tektosyne.geometry.PointD;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

public class BOStarSystem {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private StarSystemDataDTO hh_starSystemDataDTO;
	private Circle starSystemCircle;
	private Label starSystemLabel;
	private Pane levelLabel;
	private StackPane starSystemStackPane;
	private Group starSystemGroup;
	private PointD[] voronoiRegion;
	private Point2D coord;
	private ImageView marker = null;
	private ImageView industryMarker = null;

	private boolean isLockedByJumpship = false;
	private boolean isCurrentlyUnderAttack = false;
	private boolean isNextRoundUnderAttack = false;
//	private boolean wasLastRoundUnderAttack = false;

	@SuppressWarnings("unused")
	public void setStarSystemDataDTO(StarSystemDataDTO hh_starSystemDataDTO) {
		this.hh_starSystemDataDTO = hh_starSystemDataDTO;
	}

	@SuppressWarnings("unused")
	public void setLevelLabel(Pane level) {
		this.levelLabel = level;
	}

	@SuppressWarnings("unused")
	public Pane getLevelLabel() {
		return this.levelLabel;
	}

	@SuppressWarnings("unused")
	public Long getLevel() {
		return hh_starSystemDataDTO.getLevel();
	}

	@SuppressWarnings("unused")
	public Point2D getCoordinates() {
		if (coord == null) {
			coord = new Point2D(this.getScreenX(), this.getScreenY());
		}
		return coord;
	}

	@SuppressWarnings("unused")
	public void setIndustryMarker(ImageView v) {
		this.industryMarker = v;
	}

	@SuppressWarnings("unused")
	public ImageView getIndustryImage() {
		return industryMarker;
	}

	@SuppressWarnings("unused")
	public boolean isActiveInPhase(int phase) {
		// return (hh_starSystemDataDTO.getActiveInMetaPhase() & (long) phase) == (long) phase;
		return hh_starSystemDataDTO.getActiveInMetaPhase().intValue() <= phase;
	}

	@SuppressWarnings("unused")
	public boolean isActive() {
		return hh_starSystemDataDTO.getActive();
	}

	@SuppressWarnings("unused")
	public Long getActiveInMetaPhase() {
		return hh_starSystemDataDTO.getActiveInMetaPhase();
	}

	@SuppressWarnings("unused")
	public PointD[] getVoronoiRegion() {
		return voronoiRegion;
	}

	@SuppressWarnings("unused")
	public void setVoronoiRegion(PointD[] voronoiRegion) {
		this.voronoiRegion = voronoiRegion;
	}

	@SuppressWarnings("unused")
	public double getScreenX() {
		double screenX = (hh_starSystemDataDTO.getStarSystemID().getX().doubleValue()) * Config.MAP_COORDINATES_MULTIPLICATOR;
		screenX = (Config.MAP_WIDTH / 2) + screenX;
		return screenX;
	}

	@SuppressWarnings("unused")
	public double getScreenY() {
		double screenY = (hh_starSystemDataDTO.getStarSystemID().getY().doubleValue()) * Config.MAP_COORDINATES_MULTIPLICATOR;
		screenY = Config.MAP_HEIGHT - ((Config.MAP_HEIGHT /2) + screenY);
		return screenY;
	}

	@SuppressWarnings("unused")
	public Boolean isCapitalWorld() {
		return hh_starSystemDataDTO.getCapitalWorld();
	}

	@SuppressWarnings("unused")
	public Long getStarSystemDataId() {
		return hh_starSystemDataDTO.getId();
	}

	@SuppressWarnings("unused")
	public Long getStarSystemId() {
		return hh_starSystemDataDTO.getStarSystemID().getId();
	}

	@SuppressWarnings("unused")
	public Circle getStarSystemCircle() {
		return starSystemCircle;
	}

	@SuppressWarnings("unused")
	public void setStarSystemCircle(Circle starSystemCircle) {
		this.starSystemCircle = starSystemCircle;
	}

	@SuppressWarnings("unused")
	public Label getStarSystemLabel() {
		return starSystemLabel;
	}

	@SuppressWarnings("unused")
	public void setStarSystemLabel(Label starSystemLabel) {
		this.starSystemLabel = starSystemLabel;
	}

	@SuppressWarnings("unused")
	public Group getStarSystemGroup() {
		return starSystemGroup;
	}

	@SuppressWarnings("unused")
	public void setStarSystemGroup(Group starSystemGroup) {
		this.starSystemGroup = starSystemGroup;
	}

	@SuppressWarnings("unused")
	public StackPane getStarSystemStackPane() {
		return starSystemStackPane;
	}

	@SuppressWarnings("unused")
	public void setStarSystemStackPane(StackPane starSystemStackPane) {
		this.starSystemStackPane = starSystemStackPane;
	}

	@SuppressWarnings("unused")
	public void setStarSystemSelectionMarker(ImageView m) {
		this.marker = m;
	}

	@SuppressWarnings("unused")
	public ImageView getStarSystemSelectionMarker() {
		return marker;
	}

	@SuppressWarnings("unused")
	public BOStarSystem(StarSystemDataDTO starSystemDTO) {
		this.hh_starSystemDataDTO = starSystemDTO;
	}

	@SuppressWarnings("unused")
	public String getName() { return hh_starSystemDataDTO.getStarSystemID().getName(); }

	@SuppressWarnings("unused")
	public Long getId() { return hh_starSystemDataDTO.getId(); }

	@SuppressWarnings("unused")
	public Double getX() { return hh_starSystemDataDTO.getStarSystemID().getX().doubleValue(); }

	@SuppressWarnings("unused")
	public Double getY() { return hh_starSystemDataDTO.getStarSystemID().getY().doubleValue(); }

	@SuppressWarnings("unused")
	public String getAffiliation() { return hh_starSystemDataDTO.getFactionID().getShortName(); }

//	@SuppressWarnings("unused")
//	public void setFactionId(Long factionId) {
//		BOFaction faction = null;
//		for (BOFaction f : Nexus.getBoUniverse().factionBOs.values()) {
//			if (f.getID().equals(factionId)) {
//				faction = f;
//			}
//		}
//		if (faction != null) {
//			this.hh_starSystemDataDTO.setFactionID(faction.getFactionDTO());
//		}
//	}

	@SuppressWarnings("unused")
	public Long getFactionId() { return hh_starSystemDataDTO.getFactionID().getId(); }

	@SuppressWarnings("unused")
	public String getSystemImageName() { return hh_starSystemDataDTO.getStarSystemID().getSystemImageName(); }

	@SuppressWarnings("unused")
	public Image getSystemImage() {
		Image imagePlanet;
		String systemImageName = String.format("%03d", Integer.parseInt(hh_starSystemDataDTO.getStarSystemID().getSystemImageName()));
		try {
			imagePlanet = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/planets/" + systemImageName + ".png")));
		} catch (Exception e) {
			logger.info("Planet picture not found! Consider adding a fitting image for id: " + systemImageName);
			imagePlanet = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/planets/000_default.png")));
		}
		return imagePlanet;
	}

	@SuppressWarnings("unused")
	public void setCurrentlyUnderAttack(boolean b) {
		this.isCurrentlyUnderAttack = b;
	}

	@SuppressWarnings("unused")
	public boolean isCurrentlyUnderAttack() {
		return this.isCurrentlyUnderAttack;
	}

	@SuppressWarnings("unused")
	public boolean isNextRoundUnderAttack() {
		return this.isNextRoundUnderAttack;
	}

	@SuppressWarnings("unused")
	public void setNextRoundUnderAttack(boolean nextRoundUnderAttack) {
		isNextRoundUnderAttack = nextRoundUnderAttack;
	}

	@SuppressWarnings("unused")
	public boolean isLockedByJumpship() {
		return isLockedByJumpship;
	}

	@SuppressWarnings("unused")
	public void setLockedByJumpship(boolean lockedByJumpship) {
		this.isLockedByJumpship = lockedByJumpship;
	}

//	@SuppressWarnings("unused")
//	public boolean wasLastRoundUnderAttack() {
//		return wasLastRoundUnderAttack;
//	}
//
//	@SuppressWarnings("unused")
//	public void setLastRoundUnderAttack(boolean wasLastRoundUnderAttack) {
//		this.wasLastRoundUnderAttack = wasLastRoundUnderAttack;
//	}

	@SuppressWarnings("unused")
	public String getDescription() {
		return this.hh_starSystemDataDTO.getDescription();
	}

	@SuppressWarnings("unused")
	public Long getType() {
		return this.hh_starSystemDataDTO.getType();
	}

	@SuppressWarnings("unused")
	public boolean isCurrentlyAttacked() {
		boolean hasAttack = false;
		for (BOAttack a : Nexus.getBoUniverse().attackBOsOpenInThisRound.values()) {
			if (a.getStarSystemId().equals(this.hh_starSystemDataDTO.getStarSystemID().getId())) {
				hasAttack = true;
			}
		}
		return hasAttack;
	}

	@SuppressWarnings("unused")
	public BOAttack getAttack() {
		for (BOAttack a : Nexus.getBoUniverse().attackBOsOpenInThisRound.values()) {
			if (hh_starSystemDataDTO.getStarSystemID().getId().equals(a.getStarSystemId())) {
				return a;
			}
		}
		return null;
	}
}
