/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : http://www.clanwolf.net                            |
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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes.map;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

class SceneGestures {
	private double previousX;
	private double previousY;

	private DragContext sceneDragContext = new DragContext();
	private PannableCanvas canvas;

	SceneGestures(PannableCanvas canvas) {
		this.canvas = canvas;
	}

	EventHandler<MouseEvent> getOnMousePressedEventHandler() {
		return onMousePressedEventHandler;
	}

	EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
		return onMouseDraggedEventHandler;
	}

	EventHandler<ScrollEvent> getOnScrollEventHandler() {
		return onScrollEventHandler;
	}

	EventHandler<MouseEvent> getOnMouseMovedEventHandler() {
		return onMouseMovedEventHandler;
	}

	private EventHandler<MouseEvent> onMouseMovedEventHandler = event -> {
		// TODO: Do something with hovered coordinates
		// Fire an action to inform the surrounding frame about the currently hovered universe coordinates
		// double universeX = getUniverseX(event.getX());
		// double universeY = getUniverseX(event.getY());
		// System.out.println("[" + universeX + ", " + universeY + "]");
	};

	private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent event) {

			if (event.isPrimaryButtonDown()) {
				canvas.hideStarSystemMarker();
			}

			// right mouse button => panning
			if (!event.isSecondaryButtonDown()) {
				return;
			}

			previousX = event.getX();
			previousY = event.getY();

			sceneDragContext.mouseAnchorX = event.getSceneX();
			sceneDragContext.mouseAnchorY = event.getSceneY();

			sceneDragContext.translateAnchorX = canvas.getTranslateX();
			sceneDragContext.translateAnchorY = canvas.getTranslateY();
		}
	};

	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {

			// right mouse button => panning
			if (!event.isSecondaryButtonDown()) {
				return;
			}

			double diffX = sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX;
			double diffY = sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY;

//			String directionH = "";
//			String directionV = "";
			double multix = 0;
			double multiy = 0;
			double x = event.getX();
			double y = event.getY();
			if (x < previousX) {
//				directionH = "right";
				multix = -1;
			} else if (x > previousX) {
//				directionH = "left";
				multix = 1;
			}
			if (y < previousY) {
//				directionV = "up";
				multiy = -1;
			} else if (y > previousY) {
//				directionV = "down";
				multiy = 1;
			}
			for (int[] layer : Config.BACKGROUND_STARS_LAYERS) {
				int level = layer[0];
				int factor = layer[2];
				canvas.moveBackgroundStarPane(level, factor * multix, factor * multiy);
			}

			previousX = x;
			previousY = y;

			canvas.setTranslateX(diffX);
			canvas.setTranslateY(diffY);

			event.consume();
		}
	};

	/**
	 * Mouse wheel handler: zoom to pivot point
	 */
	private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

		@Override
		public void handle(ScrollEvent event) {
			double delta = 1.2;
			double scale = canvas.getScale(); // only use Y, same value for X
			double oldScale = scale;

			if (event.getDeltaY() < 0) {
				scale /= delta;
			} else {
				scale *= delta;
			}

			scale = clamp(scale);
			double f = (scale / oldScale) - 1;

			// maxX = right overhang, maxY = lower overhang
			double maxX = canvas.getBoundsInParent().getMaxX()
					- canvas.localToParent(canvas.getPrefWidth(), canvas.getPrefHeight()).getX();
			double maxY = canvas.getBoundsInParent().getMaxY()
					- canvas.localToParent(canvas.getPrefWidth(), canvas.getPrefHeight()).getY();

			// minX = left overhang, minY = upper overhang
			double minX = canvas.localToParent(0, 0).getX() - canvas.getBoundsInParent().getMinX();
			double minY = canvas.localToParent(0, 0).getY() - canvas.getBoundsInParent().getMinY();

			// adding the overhangs together, as we only consider the width of
			// canvas itself
			double subX = maxX + minX;
			double subY = maxY + minY;

			// subtracting the overall overhang from the width and only the left
			// and upper overhang from the upper left point
			double dx = (event.getSceneX() - ((canvas.getBoundsInParent().getWidth() - subX) / 2
					+ (canvas.getBoundsInParent().getMinX() + minX)));
			double dy = (event.getSceneY() - ((canvas.getBoundsInParent().getHeight() - subY) / 2
					+ (canvas.getBoundsInParent().getMinY() + minY)));

			canvas.setScale(scale);

			// note: pivot value must be untransformed, i. e. without scaling
			canvas.setPivot(f * dx, f * dy);

			event.consume();
		}
	};

	private static double clamp(double value) {
		if (Double.compare(value, Config.MAP_MIN_SCALE) < 0) {
			return Config.MAP_MIN_SCALE;
		}
		if (Double.compare(value, Config.MAP_MAX_SCALE) > 0) {
			return Config.MAP_MAX_SCALE;
		}
		return value;
	}

	@SuppressWarnings("unused")
	private double getUniverseX(double screenX) {
		double universeX = screenX - (Config.MAP_WIDTH / 2);
		universeX = universeX / Config.MAP_COORDINATES_MULTIPLICATOR;
		return universeX;
	}

	@SuppressWarnings("unused")
	private double getUniverseY(double screenY) {
		double universeY = screenY - (Config.MAP_HEIGHT / 2);
		universeY = universeY / Config.MAP_COORDINATES_MULTIPLICATOR;
		return universeY;
	}
}