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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeGestures {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final DragContext nodeDragContext = new DragContext();
	private PannableCanvas canvas;
	private Image selectionMarker;
	private Image attackMarker;
	private Image travelMarker;
	private BOStarSystem previousSelectedSystem;
	private final BOUniverse boUniverse = Nexus.getBoUniverse();
	private final HashMap<Long, ArrayList<Text>> routePointLabelsMap = new HashMap<>();

	private double draggedStartedX = 0.0f;
	private double draggedStartedY = 0.0f;
	private boolean moveJumpShipToDragStart = false;

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

	public EventHandler<MouseEvent> getOnJumpShipClickedEventHandler() { return onJumpShipClickedEventHandler; }

	public EventHandler<MouseEvent> getOnMouseReleasedEventHandler() {
		return onMouseReleasedEventHandler;
	}

	private final EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
		Node node = (Node) event.getSource();
		if (node instanceof ImageView) {
			BOJumpship js = boUniverse.jumpshipBOs.get(node.getId());
			if (js != null) {
				js.getJumpshipLevelLabel().setTranslateX(js.getJumpshipImageView().getTranslateX() + 8);
				js.getJumpshipLevelLabel().setTranslateY(js.getJumpshipImageView().getTranslateY() - 2);
				js.getJumpshipLevelLabel().setVisible(true);

				if (moveJumpShipToDragStart) {
					ImageView s = js.getJumpshipImageView();
					s.setTranslateX(draggedStartedX - 35);
					s.setTranslateY(draggedStartedY - 8);

					draggedStartedX = 0.0f;
					draggedStartedY = 0.0f;
					moveJumpShipToDragStart = false;
				} else {
					if (boUniverse.currentlyDraggedJumpship != null) {
						js.getPredictedRouteLine().toFront();
						node.toFront();
						boUniverse.currentlyDraggedJumpship.getPredictedRouteLine().setVisible(false);
					}
				}
			}
		}
	};

	private final EventHandler<MouseEvent> onMouseDragDetectedEventHandler = event -> {
		Node sourceNode = (Node) event.getSource();
		sourceNode.startFullDrag();
	};

	private double getWidth(Text text) {
		new Scene(new Group(text));
		text.applyCss();
		return text.getLayoutBounds().getWidth();
	}

	@SuppressWarnings("unused")
	private double getHeight(Text text) {
		new Scene(new Group(text));
		text.applyCss();
		return text.getLayoutBounds().getHeight();
	}

	private final EventHandler<MouseDragEvent> onStarSystemDragEnteredEventHandler = event -> {
		Node node = (Node) event.getSource();
		Circle c = (Circle) node;
		c.setRadius(8);
		event.consume();

		// Starting system and dragged target can be taken from new member vars to be created
		BOStarSystem startSystem = boUniverse.starSystemBOs.get(boUniverse.currentlyDraggedJumpship.getCurrentSystemID());
		BOStarSystem hovered = boUniverse.starSystemBOs.get(Long.parseLong(c.getId()));

		logger.info(boUniverse.currentlyDraggedJumpship.getJumpshipName() + " : " + startSystem.getName() + " : " + hovered.getName());
		List<BOStarSystem> route = RouteCalculator.calculateRoute(startSystem, hovered);

		boUniverse.currentlyDraggedJumpship.setRouteSystems(route);
		int currentRound = Nexus.getCurrentRound();

		canvas.getChildren().remove(boUniverse.currentlyDraggedJumpship.routeLines);

		routePointLabelsMap.computeIfAbsent(boUniverse.currentlyDraggedJumpship.getJumpshipId(), k -> new ArrayList<>());
		canvas.getChildren().removeAll(routePointLabelsMap.get(boUniverse.currentlyDraggedJumpship.getJumpshipId()));
		routePointLabelsMap.get(boUniverse.currentlyDraggedJumpship.getJumpshipId()).clear();

		boUniverse.currentlyDraggedJumpship.routeLines = new Group();

		ArrayList<Line> lines = new ArrayList<>();
		ArrayList<Circle> circles = new ArrayList<>();
		ArrayList<Text> texts = new ArrayList<>();
		ArrayList<ImageView> markers = new ArrayList<>();

		for (int y = 0; y < route.size() - 1; y++) {
			BOStarSystem s1 = route.get(y);
			BOStarSystem s2 = route.get(y + 1);

			int thisRound = currentRound + y + 1;
			logger.info("Drawing route for round: " + thisRound + " (" + s2.getName() + ")");

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
				circleS1.setRadius(10);
				circleS1.setStrokeWidth(s1.getStarSystemCircle().getStrokeWidth());
				circleS1.setStroke(Color.web(boUniverse.factionBOs.get(s1.getAffiliation()).getColor()));
				circleS1.setFill(Color.web(boUniverse.factionBOs.get(s1.getAffiliation()).getColor()));
				circleS1.setOpacity(1.0);
				circleS1.setVisible(true);
				circleS1.setMouseTransparent(true);
				circleS1.toFront();

				circles.add(circleS1);
			}

			// Filled dots for every stop on the route (S2)
			Circle circleS2 = new Circle();
			circleS2.setCenterX(s2.getScreenX());
			circleS2.setCenterY(s2.getScreenY());
//			circleS2.setRadius(s2.getStarSystemCircle().getRadius());
			circleS2.setRadius(10);
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
			circleS2.setMouseTransparent(true);
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
			text.setMouseTransparent(true);
			text.toFront();

			texts.add(text);

			if (y == 0 && !("" + Nexus.getCurrentUser().getCurrentCharacter().getFactionId()).equals("" + s2.getFactionId())) {
				logger.info("Attacking: " + s2.getName());
				double markerDim = 36.0d;
				ImageView marker;
				marker = new ImageView();
				marker.setFitWidth(markerDim);
				marker.setFitHeight(markerDim);
				marker.setImage(attackMarker);
				marker.setTranslateX(s2.getScreenX() - (markerDim / 2));
				marker.setTranslateY(s2.getScreenY() - (markerDim / 2));
				marker.setMouseTransparent(true);

				markers.add(marker);
			}
		}

		if (route.contains(hovered)) {
			// return to normal
			// logger.info("Possible jump");
			ActionManager.getAction(ACTIONS.SHOW_FORBIDDEN_ICON_MAP).execute(false);
		} else {
			// show indicator "jump can not be made"
			// logger.info("Impossible jump");
			ActionManager.getAction(ACTIONS.SHOW_FORBIDDEN_ICON_MAP).execute(true);
		}

		// Add elements all at once in the end to ensure correct z-order
		boUniverse.currentlyDraggedJumpship.routeLines.getChildren().addAll(lines);
		boUniverse.currentlyDraggedJumpship.routeLines.getChildren().addAll(markers);
		boUniverse.currentlyDraggedJumpship.routeLines.getChildren().addAll(circles);
		boUniverse.currentlyDraggedJumpship.routeLines.getChildren().addAll(texts);
		boUniverse.currentlyDraggedJumpship.routeLines.toFront();
		boUniverse.currentlyDraggedJumpship.getJumpshipImageView().toFront();

		canvas.getChildren().add(boUniverse.currentlyDraggedJumpship.routeLines);
		ActionManager.getAction(ACTIONS.ENABLE_JUMP_BUTTON).execute();
	};

	private final EventHandler<MouseDragEvent> onStarSystemDragExitedEventHandler = event -> {
		Node node = (Node) event.getSource();
		Circle c = (Circle) node;
		c.setRadius(5);
		ActionManager.getAction(ACTIONS.SHOW_FORBIDDEN_ICON_MAP).execute(false);
		event.consume();
	};

	private final EventHandler<MouseEvent> onStarSystemHoverEnteredEventHandler = event -> {
		if (Config.MAP_HIGHLIGHT_HOVERED_STARSYSTEM) {
			Node node = (Node) event.getSource();
			Circle c = (Circle) node;
			c.setRadius(8);

			BOStarSystem hoveredStarSystem = boUniverse.starSystemBOs.get(Long.parseLong(node.getId()));
			Double x = hoveredStarSystem.getX();
			Double y = hoveredStarSystem.getY();
			ActionManager.getAction(ACTIONS.UPDATE_COORD_INFO).execute(hoveredStarSystem.getName() + " [X:" + String.format("%.2f", x) + "] - [Y:" + String.format("%.2f", y) + "]");

//			if (Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN)) {
//				logger.info("Hovered System information: " + hoveredStarSystem.getName() + " (x: " + hoveredStarSystem.getX() + " | y: " + hoveredStarSystem.getY() + ") - " + "[StarSystemId / StarSystemDataId: " + hoveredStarSystem.getStarSystemId() + " / " + hoveredStarSystem.getStarSystemDataId() + "]");
//			}
		}
	};

	private final EventHandler<MouseEvent> onStarSystemHoverExitedEventHandler = event -> {
		if (Config.MAP_HIGHLIGHT_HOVERED_STARSYSTEM) {
			Node node = (Node) event.getSource();
			Circle c = (Circle) node;
			c.setRadius(5);

//			BOStarSystem hoveredStarSystem = boUniverse.starSystemBOs.get(Long.parseLong(node.getId()));
			Double x = Nexus.getCurrentlySelectedStarSystem().getX();
			Double y = Nexus.getCurrentlySelectedStarSystem().getY();
			ActionManager.getAction(ACTIONS.UPDATE_COORD_INFO).execute(Nexus.getCurrentlySelectedStarSystem().getName() + " [X:" + String.format("%.2f", x) + "] - [Y:" + String.format("%.2f", y) + "]");
		}
	};

	private final EventHandler<MouseEvent> onJumpShipClickedEventHandler = event -> {
		if (event.getTarget() instanceof ImageView) {
			// This is probably a jumpship
			Node node = (Node) event.getSource();
			String name = node.getId();
			BOJumpship ship = boUniverse.jumpshipBOs.get(name);
			if (ship != null) {
				ActionManager.getAction(ACTIONS.SHOW_JUMPSHIP_DETAIL).execute(ship);
			}
		}
	};

	private final EventHandler<MouseEvent> getOnMouseClickedEventHandler = new EventHandler<>() {
		public void handle(MouseEvent event) {
			if (event.isSecondaryButtonDown()) {
				logger.info("RIGHTCLICK");
			}

			// left mouse button click
			if (!event.isPrimaryButtonDown()) {
				return;
			}

			Node node = (Node) event.getSource();
			BOStarSystem clickedStarSystem = boUniverse.starSystemBOs.get(Long.parseLong(node.getId()));
			StackPane sp = clickedStarSystem.getStarSystemStackPane();
			Group group = clickedStarSystem.getStarSystemGroup();

			logger.info("System: " + clickedStarSystem.getName() + " (x: " + clickedStarSystem.getX() + " | y: " + clickedStarSystem.getY() + ") - " + "[StarsystemDataID: " + clickedStarSystem.getStarSystemDataId() + ", StarsystemId: " + clickedStarSystem.getStarSystemId() + "]");

			if (event.getTarget() instanceof Circle) {
				canvas.showStarSystemMarker(clickedStarSystem);
				Nexus.setCurrentlySelectedStarSystem(clickedStarSystem);
				ActionManager.getAction(ACTIONS.SHOW_SYSTEM_DETAIL).execute(clickedStarSystem);
				ActionManager.getAction(ACTIONS.SYSTEM_WAS_SELECTED).execute(clickedStarSystem);
				if (group != null) {
					double markerDim = 36.0d;
					ImageView marker;
					marker = new ImageView();
					marker.setFitWidth(markerDim);
					marker.setFitHeight(markerDim);

					if (clickedStarSystem.isAttackedThisOrNextRound()) {
						// This system is under attack
						marker.setImage(attackMarker);
					} else {
						// This system is not attacked
						if (("" + Nexus.getCurrentUser().getCurrentCharacter().getFactionId()).equals("" + clickedStarSystem.getFactionId())) {
							// This system belongs to my own faction
							marker.setImage(travelMarker);
						} else {
							// This system belongs to another faction
							marker.setImage(selectionMarker);
						}
					}

					marker.setTranslateX((sp.getWidth() / 2) - (markerDim / 2) + 0.5);
					marker.setTranslateY((sp.getHeight() / 2) - (markerDim / 2) + 0.5);
					if (previousSelectedSystem != null) {
						if (previousSelectedSystem.getStarSystemSelectionMarker() != null) {
							Group previousgroup = previousSelectedSystem.getStarSystemGroup();
							previousgroup.getChildren().remove(previousSelectedSystem.getStarSystemSelectionMarker());
							previousSelectedSystem = clickedStarSystem;
							previousSelectedSystem.setStarSystemSelectionMarker(marker);
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

	private final EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
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

	private final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<>() {
		public void handle(MouseEvent event) {
			// left mouse button => dragging
			if (!event.isPrimaryButtonDown()) {
				return;
			}

			double scale = canvas.getScale();
			double newTranslateX = nodeDragContext.translateAnchorX + ((event.getSceneX() - nodeDragContext.mouseAnchorX) / scale);
			double newTranslateY = nodeDragContext.translateAnchorY + ((event.getSceneY() - nodeDragContext.mouseAnchorY) / scale);

			Node node = (Node) event.getSource();
			if (node instanceof ImageView) { // may be a jumpship
				String name = node.getId();
				BOJumpship ship = boUniverse.jumpshipBOs.get(name);
				ship.getJumpshipLevelLabel().setVisible(false);

				// Is the dragged node a ship (?) and does it belong to my faction (?)
				if (ship != null && ship.getJumpshipFaction() == Nexus.getCurrentUser().getCurrentCharacter().getFactionId()) {
//					if ((Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.FACTIONLEAD_HAS_ROLE) || Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.UNITLEAD_HAS_ROLE)) && Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.FACTIONLEAD_MOVE_JUMPSHIP)) {
					if (Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.FACTIONLEAD_MOVE_JUMPSHIP)) {
						boUniverse.currentlyDraggedJumpship = boUniverse.jumpshipBOs.get(node.getId());
						node.toBack();

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
						if (moveJumpShipToDragStart == false) {
							String mes = Internationalization.getString("C3_Speech_app_starmap_moving_jumpship_not_allowed");
							StatusTextEntryActionObject o = new StatusTextEntryActionObject(mes, true, "YELLOW");
							ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(o);
							C3SoundPlayer.getTTSFile(mes);

							// Stop dragging of the jumpship here!
							draggedStartedX = boUniverse.starSystemBOs.get(ship.getCurrentSystemID()).getScreenX();
							draggedStartedY = boUniverse.starSystemBOs.get(ship.getCurrentSystemID()).getScreenY();
							moveJumpShipToDragStart = true;
						}
					}
				}
			}

			node.setTranslateX(newTranslateX);
			node.setTranslateY(newTranslateY);
			event.consume();
		}
	};
}
