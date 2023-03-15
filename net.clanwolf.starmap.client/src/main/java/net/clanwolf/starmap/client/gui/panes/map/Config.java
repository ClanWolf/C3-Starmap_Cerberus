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
package net.clanwolf.starmap.client.gui.panes.map;

import javafx.scene.paint.Color;

public class Config {
	public static final double CLIP_X = 776;
	public static final double CLIP_Y = 471;

	// Initially working with 6000, but this may cause problems on weaker computers
	// due to a bug in JavaFx. So, it may be changed by a user-property in order to make
	// it work properly.
	// e.g. map_dimensions=3000
	public static double MAP_DIM = 4000;
	public static double MAP_WIDTH = MAP_DIM;
	public static double MAP_HEIGHT = MAP_DIM;
	public static double MAP_INITIAL_TRANSLATE_X = -(MAP_WIDTH / 2) + (CLIP_X / 2);   // X: 0 Center = Terra
	public static double MAP_INITIAL_TRANSLATE_Y = -(MAP_HEIGHT / 2) + (CLIP_Y / 2);  // Y: 0 Center = Terra

	public static final int MAP_COORDINATES_MULTIPLICATOR = 5;
	public static final Color MAP_BACKGROUND_AREA_BORDER_COLOR = Color.DARKORANGE;
	public static final Color MAP_RANGE_CIRCLE_COLOR = Color.LIGHTBLUE;
	public static final boolean MAP_HIGHLIGHT_HOVERED_STARSYSTEM = true;
	public static final boolean MAP_FLASH_ATTACKED_SYSTEMS = true;
	public static final double MAP_MAX_SCALE = 3.0d;
	public static final double MAP_MIN_SCALE = .2d;
	public static final double MAP_BACKGROUND_AREA_RADIUS = 140;
	public static final double MAP_BACKGROUND_AREA_RADIUS_BORDER_WIDTH = 5;

	// Hide on zoom
	public static final double zoomLevelToHideGrid1 = 0.4;
	public static final double zoomLevelToHideGrid2 = 0.7;
	public static final double zoomLevelToHideLevelIcons = 1.8;
	public static final double zoomLevelToHideStarSystemLabels = 1.0;
	public static final double zoomLevelToHideStarSystemCircles = 0.5;
	public static final double zoomLevelToHideAttacks = 0.5;
	public static final double zoomLevelToHideJumpships = 0.5;

	// Background stars
	public static int BACKGROUND_STARS_MAX_SIZE = 4;
	public static int BACKGROUND_STARS_LAYERS[][] = {{1, 150, 1}, {2, 180, 2}, {3, 200, 4}}; // level, number of stars, moving factor

	private Config() {
		// private constructor
	}

	public static void setMapDim(int i) {
		MAP_DIM = i;
		MAP_WIDTH = MAP_DIM;
		MAP_HEIGHT = MAP_DIM;
		MAP_INITIAL_TRANSLATE_X = -(MAP_WIDTH / 2) + (CLIP_X / 2);   // X: 0 Center = Terra
		MAP_INITIAL_TRANSLATE_Y = -(MAP_HEIGHT / 2) + (CLIP_Y / 2);  // Y: 0 Center = Terra

		BACKGROUND_STARS_MAX_SIZE = 2;
		BACKGROUND_STARS_LAYERS[0][0] = 1;
		BACKGROUND_STARS_LAYERS[0][1] = 200;
		BACKGROUND_STARS_LAYERS[0][2] = 1;
		BACKGROUND_STARS_LAYERS[1][0] = 2;
		BACKGROUND_STARS_LAYERS[1][1] = 300;
		BACKGROUND_STARS_LAYERS[1][2] = 2;
		BACKGROUND_STARS_LAYERS[2][0] = 3;
		BACKGROUND_STARS_LAYERS[2][1] = 300;
		BACKGROUND_STARS_LAYERS[2][2] = 4;
	}
}
