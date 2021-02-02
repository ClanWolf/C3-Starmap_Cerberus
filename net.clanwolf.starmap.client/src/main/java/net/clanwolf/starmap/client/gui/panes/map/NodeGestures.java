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
package net.clanwolf.starmap.client.gui.panes.map;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.gui.panes.map.tools.Route;
import net.clanwolf.starmap.client.process.universe.BOJumpship;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import net.clanwolf.starmap.logging.C3Logger;

import java.util.List;

public class NodeGestures {
	private DragContext nodeDragContext = new DragContext();
	private PannableCanvas canvas;

	private BOUniverse boUniverse = Nexus.getBoUniverse();

	NodeGestures(PannableCanvas canvas) {
		this.canvas = canvas;
	}

	@SuppressWarnings("unused")
	public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
		return onMousePressedEventHandler;
	}

	public EventHandler<MouseEvent> getOnMouseClickedEventHandler() {
		return getOnMouseClickedEventHandler;
	}

	public EventHandler<MouseEvent> getOnMouseDragDetectedEventHandler() {
		return onMouseDragDetectedEventHandler;
	}

	@SuppressWarnings("unused")
	public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
		return onMouseDraggedEventHandler;
	}

	public EventHandler<MouseEvent> getOnStarSystemHoverEnteredEventHandler() {
		return onStarSystemHoverEnteredEventHandler;
	}

	public EventHandler<MouseEvent> getOnStarSystemHoverExitedEventHandler() {
		return onStarSystemHoverExitedEventHandler;
	}

	public EventHandler<MouseDragEvent> getOnStarSystemDragEnteredEventHandler() {
		return onStarSystemDragEnteredEventHandler;
	}

	public EventHandler<MouseDragEvent> getOnStarSystemDragExitedEventHandler() {
		return onStarSystemDragExitedEventHandler;
	}

	public EventHandler<MouseEvent> getOnMouseReleasedEventHandler() {
		return onMouseReleasedEventHandler;
	}

	private EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
		Node node = (Node) event.getSource();
		if (node instanceof ImageView) {
			BOJumpship js = boUniverse.jumpshipBOs.get(node.getId());
			js.getPredictedRouteLine().toFront();
			node.toFront();
			boUniverse.currentlyDraggedJumpship.getPredictedRouteLine().setVisible(false);
		}
	};

	private EventHandler<MouseEvent> onMouseDragDetectedEventHandler = event -> {
		Node sourceNode = (Node) event.getSource();
		sourceNode.startFullDrag();
	};

	private EventHandler<MouseDragEvent> onStarSystemDragEnteredEventHandler = event -> {
		Node node = (Node) event.getSource();
		Circle c = (Circle) node;
		c.setRadius(8);
		event.consume();

		// TODO: Get the route
		// Starting system and dragged target can be taken from new member vars to be created
		BOStarSystem startSystem = boUniverse.starSystemBOs.get(boUniverse.currentlyDraggedJumpship.getCurrentSystemID());
		BOStarSystem hovered = boUniverse.starSystemBOs.get(Integer.parseInt(c.getId()));

		C3Logger.info(boUniverse.currentlyDraggedJumpship.getShipName() + " : " + startSystem.getName() + " : " + hovered.getName());
		List<BOStarSystem> route = Route.getRoute(startSystem, hovered);

		if (canvas.getChildren().contains(boUniverse.currentlyDraggedJumpship.routeLines)) {
			canvas.getChildren().remove(boUniverse.currentlyDraggedJumpship.routeLines);
		}
		boUniverse.currentlyDraggedJumpship.routeLines = new Group();
		for (int y = 0; y < route.size() - 1; y++) {
			BOStarSystem s1 = (BOStarSystem) route.get(y);
			BOStarSystem s2 = (BOStarSystem) route.get(y + 1);

			// Dotted line to every stop on the route
			Line line = new Line(s1.getScreenX(), s1.getScreenY(), s2.getScreenX(), s2.getScreenY());
			line.setStrokeWidth(4);
			line.getStrokeDashArray().setAll(5d, 5d, 5d, 5d);
			if (y == 0) {
				line.setStroke(Color.GREEN);
			} else {
				line.setStroke(Color.LIGHTGREEN);
			}
			line.setStrokeLineCap(StrokeLineCap.ROUND);
			boUniverse.currentlyDraggedJumpship.routeLines.getChildren().add(line);

			// Filled dots for every stop on the route (S1)
			Circle circleS1 = new Circle();
			circleS1.setCenterX(s1.getScreenX());
			circleS1.setCenterY(s1.getScreenY());
			circleS1.setRadius(s1.getStarSystemCircle().getRadius());
			circleS1.setStrokeWidth(s1.getStarSystemCircle().getStrokeWidth());
			circleS1.setStroke(Color.web(boUniverse.factionBOs.get(s1.getAffiliation()).getColor()));
			circleS1.setFill(Color.web(boUniverse.factionBOs.get(s1.getAffiliation()).getColor()));
			circleS1.setOpacity(1.0);
			circleS1.setVisible(true);
			boUniverse.currentlyDraggedJumpship.routeLines.getChildren().add(circleS1);

			// Filled dots for every stop on the route (S2)
			Circle circleS2 = new Circle();
			circleS2.setCenterX(s2.getScreenX());
			circleS2.setCenterY(s2.getScreenY());
			circleS2.setRadius(s2.getStarSystemCircle().getRadius());
			circleS2.setStrokeWidth(s2.getStarSystemCircle().getStrokeWidth());
			circleS2.setStroke(Color.web(boUniverse.factionBOs.get(s2.getAffiliation()).getColor()));
			circleS2.setFill(Color.web(boUniverse.factionBOs.get(s2.getAffiliation()).getColor()));
			circleS2.setOpacity(1.0);
			circleS2.setVisible(true);
			boUniverse.currentlyDraggedJumpship.routeLines.getChildren().add(circleS2);
		}
		canvas.getChildren().add(boUniverse.currentlyDraggedJumpship.routeLines);
		boUniverse.currentlyDraggedJumpship.routeLines.toBack();
	};

	private EventHandler<MouseDragEvent> onStarSystemDragExitedEventHandler = event -> {
		Node node = (Node) event.getSource();
		Circle c = (Circle) node;
		c.setRadius(5);
		event.consume();
	};

	private EventHandler<MouseEvent> onStarSystemHoverEnteredEventHandler = event -> {
		if (Config.MAP_HIGHLIGHT_HOVERED_STARSYSTEM) {
			Node node = (Node) event.getSource();
			Circle c = (Circle) node;
			c.setRadius(8);
		}
	};

	private EventHandler<MouseEvent> onStarSystemHoverExitedEventHandler = event -> {
		if (Config.MAP_HIGHLIGHT_HOVERED_STARSYSTEM) {
			Node node = (Node) event.getSource();
			Circle c = (Circle) node;
			c.setRadius(5);
		}
	};

	private EventHandler<MouseEvent> getOnMouseClickedEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {
			if (event.isSecondaryButtonDown()) {
				C3Logger.info("RIGHTCLICK");
			}

			// left mouse button click
			if (!event.isPrimaryButtonDown()) {
				return;
			}
			Node node = (Node) event.getSource();
			BOStarSystem clickedStarSystem = boUniverse.starSystemBOs.get(Integer.parseInt(node.getId()));

			C3Logger.info("System: "
					+ clickedStarSystem.getName()
					+ " (x: " + clickedStarSystem.getX()
					+ " | y: " + clickedStarSystem.getY()
					+ ") - "
					+ "[id:"
					+ clickedStarSystem.getId()
					+ "]");
//			C3Logger.info("ScreenX: " + clickedStarSystem.getScreenX());
//			C3Logger.info("ScreenY: " + clickedStarSystem.getScreenY());

			canvas.showStarSystemMarker(clickedStarSystem);
		}
	};

	private EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
		// left mouse button => dragging
		if (!event.isPrimaryButtonDown()) {
			return;
		}

		nodeDragContext.mouseAnchorX = event.getSceneX();
		nodeDragContext.mouseAnchorY = event.getSceneY();

		Node node = (Node) event.getSource();

		nodeDragContext.translateAnchorX = node.getTranslateX();
		nodeDragContext.translateAnchorY = node.getTranslateY();
	};

	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {
			// left mouse button => dragging
			if (!event.isPrimaryButtonDown()) {
				return;
			}

			double scale = canvas.getScale();
			double newTranslateX = nodeDragContext.translateAnchorX + ((event.getSceneX() - nodeDragContext.mouseAnchorX) / scale);
			double newTranslateY = nodeDragContext.translateAnchorY + ((event.getSceneY() - nodeDragContext.mouseAnchorY) / scale);

			Node node = (Node) event.getSource();
			if (node instanceof ImageView) { // must be a jumpship
				boUniverse.currentlyDraggedJumpship = boUniverse.jumpshipBOs.get(node.getId());
				node.toBack();
				String name = node.getId();
				BOJumpship ship = boUniverse.jumpshipBOs.get(name);
				canvas.showStarSystemMarker(boUniverse.starSystemBOs.get(ship.getCurrentSystemID()));
				Line routeLine = ship.getPredictedRouteLine();
				double startX = boUniverse.starSystemBOs.get(ship.getCurrentSystemID()).getScreenX();
				double startY = boUniverse.starSystemBOs.get(ship.getCurrentSystemID()).getScreenY();

				routeLine.setStartX(startX);
				routeLine.setStartY(startY);
				routeLine.setEndX(newTranslateX + 20);
				routeLine.setEndY(newTranslateY + 10);
				routeLine.toBack();
				routeLine.setVisible(true);
				routeLine.setOpacity(0.1);
				if (!canvas.getChildren().contains(routeLine)) {
					canvas.getChildren().add(routeLine);
				}
			}

			node.setTranslateX(newTranslateX);
			node.setTranslateY(newTranslateY);
			event.consume();
		}
	};
}
