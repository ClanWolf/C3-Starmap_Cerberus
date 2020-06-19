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
package net.clanwolf.starmap.client.process.universe;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
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

	public Point2D getCoordinates() {
		if (coord == null) {
			coord = new Point2D(this.getScreenX(), this.getScreenY());
		}
		return coord;
	}

	public PointD[] getVoronoiRegion() {
		return voronoiRegion;
	}

	public void setVoronoiRegion(PointD[] voronoiRegion) {
		this.voronoiRegion = voronoiRegion;
	}

	public double getScreenX() {
		double screenX = starSystemDTO.getX() * Config.MAP_COORDINATES_MULTIPLICATOR;
		screenX = (Config.MAP_WIDTH / 2) + screenX;
		return screenX;
	}

	public double getScreenY() {
		double screenY = starSystemDTO.getY() * Config.MAP_COORDINATES_MULTIPLICATOR;
		screenY = Config.MAP_HEIGHT - ((Config.MAP_HEIGHT /2) + screenY);
		return screenY;
	}

	@SuppressWarnings("unused")
	public Circle getStarSystemCircle() {
		return starSystemCircle;
	}

	public void setStarSystemCircle(Circle starSystemCircle) {
		this.starSystemCircle = starSystemCircle;
	}

	public Label getStarSystemLabel() {
		return starSystemLabel;
	}

	public void setStarSystemLabel(Label starSystemLabel) {
		this.starSystemLabel = starSystemLabel;
	}

	public Group getStarSystemGroup() {
		return starSystemGroup;
	}

	public void setStarSystemGroup(Group starSystemGroup) {
		this.starSystemGroup = starSystemGroup;
	}

	public StackPane getStarSystemStackPane() {
		return starSystemStackPane;
	}

	public void setStarSystemStackPane(StackPane starSystemStackPane) {
		this.starSystemStackPane = starSystemStackPane;
	}

	public BOStarSystem(StarSystemDTO starSystemDTO) {
		this.starSystemDTO = starSystemDTO;
	}

	public String getName() { return starSystemDTO.getName(); }

	public Integer getId() { return starSystemDTO.getId(); }

	public Double getX() { return starSystemDTO.getX(); }

	public Double getY() { return starSystemDTO.getY(); }

	public String getAffiliation() { return starSystemDTO.getAffiliation(); }
}
