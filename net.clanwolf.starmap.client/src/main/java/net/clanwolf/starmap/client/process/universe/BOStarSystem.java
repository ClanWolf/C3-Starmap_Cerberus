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

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import net.clanwolf.starmap.client.gui.panes.map.Config;
import net.clanwolf.starmap.transfer.dtos.StarSystemDTO;
import org.kynosarges.tektosyne.geometry.PointD;

public class BOStarSystem {

	private StarSystemDTO starSystemDTO;
	private Circle starSystemCircle;
	private Label starSystemLabel;
	private StackPane starSystemStackPane;
	private Group starSystemGroup;
	private PointD[] voronoiRegion;
	private Point2D coord;
	private ImageView marker = null;
	private boolean isCurrentlyUnderAttack = false;

	@SuppressWarnings("unused")
	public Point2D getCoordinates() {
		if (coord == null) {
			coord = new Point2D(this.getScreenX(), this.getScreenY());
		}
		return coord;
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
		double screenX = (starSystemDTO.getX()) * Config.MAP_COORDINATES_MULTIPLICATOR;
		screenX = (Config.MAP_WIDTH / 2) + screenX;
		return screenX;
	}

	@SuppressWarnings("unused")
	public double getScreenY() {
		double screenY = (starSystemDTO.getY()) * Config.MAP_COORDINATES_MULTIPLICATOR;
		screenY = Config.MAP_HEIGHT - ((Config.MAP_HEIGHT /2) + screenY);
		return screenY;
	}

	@SuppressWarnings("unused")
	public Boolean isCapital() {
		return starSystemDTO.isCaptial();
	}

	@SuppressWarnings("unused")
	public Long getStarSystemDataId() {
		return starSystemDTO.getStarSystemDataId();
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
	public BOStarSystem(StarSystemDTO starSystemDTO) {
		this.starSystemDTO = starSystemDTO;
	}

	@SuppressWarnings("unused")
	public String getName() { return starSystemDTO.getName(); }

	@SuppressWarnings("unused")
	public Long getId() { return starSystemDTO.getId(); }

	@SuppressWarnings("unused")
	public Double getX() { return starSystemDTO.getX(); }

	@SuppressWarnings("unused")
	public Double getY() { return starSystemDTO.getY(); }

	@SuppressWarnings("unused")
	public String getAffiliation() { return starSystemDTO.getAffiliation(); }

	@SuppressWarnings("unused")
	public Long getFactionId() { return starSystemDTO.getFactionId(); }

	@SuppressWarnings("unused")
	public String getSystemImageName() { return starSystemDTO.getSystemImageName(); }

	@SuppressWarnings("unused")
	public void setCurrentlyUnderAttack(boolean b) {
		this.isCurrentlyUnderAttack = b;
	}

	@SuppressWarnings("unused")
	public boolean isCurrentlyUnderAttack() {
		return this.isCurrentlyUnderAttack;
	}

	@SuppressWarnings("unused")
	public String getDescription() {
		return this.starSystemDTO.getDescription();
	}
}
