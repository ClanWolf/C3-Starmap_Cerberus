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
package net.clanwolf.starmap.client.gui.panes.map;

import javafx.scene.paint.Color;

public class Config {

	// Map
	public static final int MAP_COORDINATES_MULTIPLICATOR = 5;
	public static final Color MAP_BACKGROUND_AREA_BORDER_COLOR = Color.DARKORANGE;
	public static final Color MAP_RANGE_CIRCLE_COLOR = Color.LIGHTBLUE;
	public static final boolean MAP_HIGHLIGHT_HOVERED_STARSYSTEM = true;
	public static final boolean MAP_FLASH_ATTACKED_SYSTEMS = true;
	public static final double MAP_WIDTH = 6000;
	public static final double MAP_HEIGHT = 6000;
	public static final double MAP_MAX_SCALE = 3.0d;
	public static final double MAP_MIN_SCALE = .2d;
	public static final double MAP_INITIAL_TRANSLATE_X = -2500;
	public static final double MAP_INITIAL_TRANSLATE_Y = -1620;
	public static final double MAP_BACKGROUND_AREA_RADIUS = 90;
	public static final double MAP_BACKGROUND_AREA_RADIUS_BORDER_WIDTH = 3;

	// Hide on zoom
	public static final double zoomLevelToHideGrid1 = 0.4;
	public static final double zoomLevelToHideGrid2 = 0.7;
	public static final double zoomLevelToHideStarSystemLabels = 1.0;
	public static final double zoomLevelToHideStarSystemCircles = 0.5;
	public static final double zoomLevelToHideAttacks = 0.5;
	public static final double zoomLevelToHideJumpships = 1.3;

	// Background stars
	public static final int BACKGROUND_STARS_MAX_SIZE = 4;
	public static final int BACKGROUND_STARS_LAYERS[][] = {{1, 300, 1}, {2, 200, 2}, {3, 100, 4}}; // level, number of stars, moving factor

	private Config() {
		// private constructor
	}
}
