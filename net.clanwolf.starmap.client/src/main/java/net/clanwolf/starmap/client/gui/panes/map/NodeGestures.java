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
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.StatusTextEntryActionObject;
import net.clanwolf.starmap.client.enums.PRIVILEGES;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.gui.panes.map.tools.RouteCalculator;
import net.clanwolf.starmap.client.process.universe.BOJumpship;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.dtos.RoutePointDTO;
import net.clanwolf.starmap.transfer.enums.MEDALS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeGestures {
	private final DragContext nodeDragContext = new DragContext();
	private PannableCanvas canvas;
	private Image selectionMarker;
	private Image attackMarker;
	private Image travelMarker;
	private BOStarSystem previousSelectedSystem;
	private final BOUniverse boUniverse = Nexus.getBoUniverse();
	private HashMap<Long, ArrayList<Text>> routePointLabelsMap = new HashMap<>();

	NodeGestures(PannableCanvas canvas) {
		this.canvas = canvas;
	}

	public void setSelectionMarker(Image im) {
		selectionMarker = im;
	}

	public void setAttackMarker(Image im) {
		attackMarker = im;
	}

	public void setTravelMarker(Image im) {
		travelMarker = im;
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

			ArrayList<RoutePointDTO> route = Nexus.getBoUniverse().routesList.get(boUniverse.currentlyDraggedJumpship.getJumpshipId());
			boUniverse.currentlyDraggedJumpship.storeRouteToDatabase(route);
		}
	};

	private EventHandler<MouseEvent> onMouseDragDetectedEventHandler = event -> {
		Node sourceNode = (Node) event.getSource();
		sourceNode.startFullDrag();
	};

	private double getWidth(Text text) {
		new Scene(new Group(text));
		text.applyCss();
		return text.getLayoutBounds().getWidth();
	}

//	private double getHeight(Text text) {
//		new Scene(new Group(text));
//		text.applyCss();
//		return text.getLayoutBounds().getHeight();
//	}

	private EventHandler<MouseDragEvent> onStarSystemDragEnteredEventHandler = event -> {
		Node node = (Node) event.getSource();
		Circle c = (Circle) node;
		c.setRadius(8);
		event.consume();

		// Starting system and dragged target can be taken from new member vars to be created
		BOStarSystem startSystem = boUniverse.starSystemBOs.get(boUniverse.currentlyDraggedJumpship.getCurrentSystemID());
		BOStarSystem hovered = boUniverse.starSystemBOs.get(Integer.parseInt(c.getId()));

		C3Logger.info(boUniverse.currentlyDraggedJumpship.getJumpshipName() + " : " + startSystem.getName() + " : " + hovered.getName());
		List<BOStarSystem> route = RouteCalculator.calculateRoute(startSystem, hovered);

		boUniverse.currentlyDraggedJumpship.setRouteSystems(route);
		int currentRound = Nexus.getCurrentRound();

		canvas.getChildren().remove(boUniverse.currentlyDraggedJumpship.routeLines);

		routePointLabelsMap.computeIfAbsent(boUniverse.currentlyDraggedJumpship.getJumpshipId(), k -> new ArrayList<Text>());
		canvas.getChildren().removeAll(routePointLabelsMap.get(boUniverse.currentlyDraggedJumpship.getJumpshipId()));
		routePointLabelsMap.get(boUniverse.currentlyDraggedJumpship.getJumpshipId()).clear();

		boUniverse.currentlyDraggedJumpship.routeLines = new Group();

		ArrayList<Line> lines = new ArrayList<>();
		ArrayList<Circle> circles = new ArrayList<>();
		ArrayList<Text> texts = new ArrayList<>();
		ArrayList<ImageView> markers = new ArrayList<>();

		for (int y = 0; y < route.size() - 1; y++) {
			BOStarSystem s1 = (BOStarSystem) route.get(y);
			BOStarSystem s2 = (BOStarSystem) route.get(y + 1);

			int thisRound = currentRound + y + 1;
			C3Logger.info("Drawing route for round: " + thisRound + " (" + s2.getName() + ")");

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
			lines.add(line);

			if (y == 0) {
				// Filled dots for every stop on the route (S1)
				Circle circleS1 = new Circle();
				circleS1.setCenterX(s1.getScreenX());
				circleS1.setCenterY(s1.getScreenY());
//			    circleS1.setRadius(s1.getStarSystemCircle().getRadius());
				circleS1.setRadius(8);
				circleS1.setStrokeWidth(s1.getStarSystemCircle().getStrokeWidth());
				circleS1.setStroke(Color.web(boUniverse.factionBOs.get(s1.getAffiliation()).getColor()));
				circleS1.setFill(Color.web(boUniverse.factionBOs.get(s1.getAffiliation()).getColor()));
				circleS1.setOpacity(1.0);
				circleS1.setVisible(true);
				circleS1.toFront();

				circles.add(circleS1);
			}

			// Filled dots for every stop on the route (S2)
			Circle circleS2 = new Circle();
			circleS2.setCenterX(s2.getScreenX());
			circleS2.setCenterY(s2.getScreenY());
//			circleS2.setRadius(s2.getStarSystemCircle().getRadius());
			circleS2.setRadius(8);
			circleS2.setStrokeWidth(s2.getStarSystemCircle().getStrokeWidth());
			circleS2.setStroke(Color.web(boUniverse.factionBOs.get(s2.getAffiliation()).getColor()));
			circleS2.setFill(Color.web(boUniverse.factionBOs.get(s2.getAffiliation()).getColor()));
			if (y == 0 && !("" + Nexus.getCurrentUser().getCurrentCharacter().getFactionId()).equals("" + s2.getFactionId())) {
				circleS2.setFill(Color.RED);
				circleS2.setStroke(Color.WHITE);
			} else {
				circleS2.setFill(Color.WHITE);
				circleS2.setStroke(Color.BLACK);
			}
			circleS2.setOpacity(1.0);
			circleS2.setVisible(true);
			circleS2.toFront();

			circles.add(circleS2);

			Text text;
			text = new Text();
			text.setText("" + thisRound);
			if (y == 0 && !("" + Nexus.getCurrentUser().getCurrentCharacter().getFactionId()).equals("" + s2.getFactionId())) {
				text.setFill(Color.WHITE);
				text.setStroke(Color.WHITE);
			} else {
				text.setFill(Color.BLACK);
				text.setStroke(Color.BLACK);
			}
			text.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
			text.setMouseTransparent(true);
			text.setLayoutX(s2.getScreenX() - (getWidth(text) / 2));
			text.setLayoutY(s2.getScreenY() + 4);
			text.toFront();

			texts.add(text);

			if (y == 0 && !("" + Nexus.getCurrentUser().getCurrentCharacter().getFactionId()).equals("" + s2.getFactionId())) {
				C3Logger.info("Attacking: " + s2.getName());
				Double markerDim = 38.0d;
				ImageView marker;
				marker = new ImageView();
				marker.setFitWidth(markerDim);
				marker.setFitHeight(markerDim);
				marker.setImage(attackMarker);
				marker.setTranslateX(s2.getScreenX() - (markerDim / 2));
				marker.setTranslateY(s2.getScreenY() - (markerDim / 2));

				markers.add(marker);
			}
		}

		// Add elements all at once in the end to ensure correct z-order
		boUniverse.currentlyDraggedJumpship.routeLines.getChildren().addAll(lines);
		boUniverse.currentlyDraggedJumpship.routeLines.getChildren().addAll(markers);
		boUniverse.currentlyDraggedJumpship.routeLines.getChildren().addAll(circles);
		boUniverse.currentlyDraggedJumpship.routeLines.getChildren().addAll(texts);
		boUniverse.currentlyDraggedJumpship.routeLines.toFront();

		canvas.getChildren().add(boUniverse.currentlyDraggedJumpship.routeLines);
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

			BOStarSystem hoveredStarSystem = boUniverse.starSystemBOs.get(Integer.parseInt(node.getId()));
			Double x = hoveredStarSystem.getX();
			Double y = hoveredStarSystem.getY();
			ActionManager.getAction(ACTIONS.UPDATE_COORD_INFO).execute(hoveredStarSystem.getName() + " [X:" + String.format("%.2f", x) + "] - [Y:" + String.format("%.2f", y) + "]");
		}
	};

	private EventHandler<MouseEvent> onStarSystemHoverExitedEventHandler = event -> {
		if (Config.MAP_HIGHLIGHT_HOVERED_STARSYSTEM) {
			Node node = (Node) event.getSource();
			Circle c = (Circle) node;
			c.setRadius(5);

//			BOStarSystem hoveredStarSystem = boUniverse.starSystemBOs.get(Integer.parseInt(node.getId()));
			Double x = Nexus.getCurrentlySelectedStarSystem().getX();
			Double y = Nexus.getCurrentlySelectedStarSystem().getY();
			ActionManager.getAction(ACTIONS.UPDATE_COORD_INFO).execute(Nexus.getCurrentlySelectedStarSystem().getName() + " [X:" + String.format("%.2f", x) + "] - [Y:" + String.format("%.2f", y) + "]");
		}
	};

	private EventHandler<MouseEvent> getOnMouseClickedEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {
			if (event.isSecondaryButtonDown()) {
//				C3Logger.info("RIGHTCLICK");
			}

			// left mouse button click
			if (!event.isPrimaryButtonDown()) {
				return;
			}

			Node node = (Node) event.getSource();
			BOStarSystem clickedStarSystem = boUniverse.starSystemBOs.get(Integer.parseInt(node.getId()));
			StackPane sp = clickedStarSystem.getStarSystemStackPane();
			Group group = clickedStarSystem.getStarSystemGroup();

			C3Logger.info("System: "
					+ clickedStarSystem.getName()
					+ " (x: " + clickedStarSystem.getX()
					+ " | y: " + clickedStarSystem.getY()
					+ ") - "
					+ "[id:"
					+ clickedStarSystem.getId()
					+ "]");

			if (clickedStarSystem != null && event.getTarget() instanceof Circle) {
				canvas.showStarSystemMarker(clickedStarSystem);
				Nexus.setCurrentlySelectedStarSystem(clickedStarSystem);
				ActionManager.getAction(ACTIONS.SHOW_SYSTEM_DETAIL).execute(clickedStarSystem);
				if (group != null) {
					Double markerDim = 38.0d;
					ImageView marker;
					marker = new ImageView();
					marker.setFitWidth(markerDim);
					marker.setFitHeight(markerDim);

					if (("" + Nexus.getCurrentUser().getCurrentCharacter().getFactionId()).equals("" + clickedStarSystem.getFactionId())) {
						// This is one of my own systems
						marker.setImage(travelMarker);
					} else {
						// This is an enemy system
						marker.setImage(attackMarker);
					}
//					marker.setImage(selectionMarker);




					ActionManager.getAction(ACTIONS.SHOW_MEDAL).execute(MEDALS.First_Blood);










					marker.setTranslateX((sp.getWidth() / 2) - (markerDim / 2));
					marker.setTranslateY((sp.getHeight() / 2) - (markerDim / 2));
					if (previousSelectedSystem != null) {
						if (previousSelectedSystem.getStarSystemSelectionMarker() != null) {
							Group previousgroup = previousSelectedSystem.getStarSystemGroup();
							previousgroup.getChildren().remove(previousSelectedSystem.getStarSystemSelectionMarker());
							previousSelectedSystem = clickedStarSystem;
							previousSelectedSystem.setStarSystemSelectionMarker(marker);
						} else {
							// Marker is null. Probably this is the first system to be marked.
						}
					} else {
						previousSelectedSystem = clickedStarSystem;
						previousSelectedSystem.setStarSystemSelectionMarker(marker);
					}
					group.getChildren().add(marker);
				}
			}
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
				if (Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.FACTIONLEAD_HAS_ROLE) && Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.FACTIONLEAD_MOVE_JUMPSHIP)) {
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
				} else {
					// No privileges to move the jumpship
					String mes = Internationalization.getString("C3_Speech_app_starmap_moving_jumpship_not_allowed");
					StatusTextEntryActionObject o = new StatusTextEntryActionObject(mes, true, "YELLOW");
					ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(o);
					C3SoundPlayer.getTTSFile(mes);
				}
			}

			node.setTranslateX(newTranslateX);
			node.setTranslateY(newTranslateY);
			event.consume();
		}
	};
}
