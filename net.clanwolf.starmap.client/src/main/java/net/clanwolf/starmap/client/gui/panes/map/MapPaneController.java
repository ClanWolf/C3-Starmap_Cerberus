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

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.*;
import net.clanwolf.starmap.client.enums.C3MESSAGES;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;
import net.clanwolf.starmap.transfer.enums.PRIVILEGES;
import net.clanwolf.starmap.client.gui.messagepanes.C3Message;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Pane;
import net.clanwolf.starmap.client.gui.panes.map.tools.VoronoiDelaunay;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.universe.*;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.Tools;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.enums.POPUPS;
import org.kynosarges.tektosyne.geometry.PointD;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.*;
import java.util.List;

import static net.clanwolf.starmap.constants.Constants.ROLE_ATTACKER_COMMANDER;
import static net.clanwolf.starmap.constants.Constants.ROLE_DEFENDER_COMMANDER;

/**
 * The controller for the starmap panel.
 *
 * @author Meldric
 */
public class MapPaneController extends AbstractC3Controller implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@FXML
	AnchorPane anchorPane, paneAttackDetail;
	@FXML
	Pane starMapPane;
	@FXML
	Pane buttonBackground;
	@FXML
	Pane paneSystemDetail;
	@FXML
	Label labelSystemName;
	@FXML
	private Label roundPhaseLabel;
	@FXML
	ImageView labelSystemImage;
	@FXML
	ImageView labelFactionImage;

	@FXML
	ImageView ivFactionImageAttacker;
	@FXML
	ImageView ivFactionImageDefender;
	@FXML
	Label lblAttackHeadline;
	@FXML
	TextField taAttackDescription;
	@FXML
	Circle circleScore1;
	@FXML
	Circle circleScore2;
	@FXML
	Circle circleScore3;
	@FXML
	Circle circleScore4;
	@FXML
	Circle circleScore5;

	@FXML
	Pane paneJumpshipDetail;
	@FXML
	Label labelJumpshipName;
	@FXML
	ImageView labelJumpshipImage;
	@FXML
	ImageView labelJumpshipFactionImage;
	@FXML
	ImageView ivForbidden;
	@FXML
	ImageView ivSurfaceMap;
	@FXML
	Label labelMouseCoords;
	@FXML
	Button mapButton01; // confirm
	@FXML
	Button mapButton02;
	@FXML
	Button mapButton03;
	@FXML
	Button mapButton04; // previous Jumpship
	@FXML
	Button mapButton05; // next Jumpship
	@FXML
	Button mapButton06; // Attack / join battle

	@FXML
	TextArea taAlliances;

	private boolean universeMapGenerationStarted = false;
	private BOUniverse boUniverse = null;
	private PannableCanvas canvas;
	private Pane borders;
	private Pane attacksPane;
	private boolean firstCreationDone = false;
	private Long currentlyDisplayedAttackDetailsId = null;
	private SceneGestures sceneGestures;
	private BOJumpship currentlyCenteredJumpship = null;
	private NodeGestures nodeGestures;
	private Long currentPlayerRoleInInvasion = 0L;

	private final Image systemForbiddenIcon0 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/forbidden0.png")));
	private final Image systemForbiddenIcon1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/forbidden1.png")));
	private final Image systemForbiddenIcon2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/forbidden2.png")));
	private final Image systemForbiddenIcon3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/forbidden3.png")));

	@FXML
	private void handleCenterHomeworldButtonClick() {
		moveMapToPosition(Nexus.getHomeworld());
	}

	@FXML
	private void handleCenterJumpshipButtonClick() {
		for (BOJumpship j : Nexus.getBoUniverse().jumpshipBOs.values()) {
			long jsid = j.getJumpshipFaction();
			if ((int) jsid == Nexus.getCurrentUser().getCurrentCharacter().getFactionId()) {
				currentlyCenteredJumpship = j;
				BOStarSystem s = currentlyCenteredJumpship.getCurrentSystem(currentlyCenteredJumpship.getCurrentSystemID());
				moveMapToPosition(s);
			}
		}
		currentlyCenteredJumpship.getJumpshipImageView().toFront();
		currentlyCenteredJumpship.getJumpshipLevelLabel().toFront();
		ActionManager.getAction(ACTIONS.SHOW_JUMPSHIP_DETAIL).execute(currentlyCenteredJumpship);
	}

	private void centerJumpship() {
		if (currentlyCenteredJumpship == null) {
			for (BOJumpship j : Nexus.getBoUniverse().jumpshipBOs.values()) {
				long jsid = j.getJumpshipFaction();
				if ((int) jsid == Nexus.getCurrentUser().getCurrentCharacter().getFactionId()) {
					currentlyCenteredJumpship = j;
					BOStarSystem s = currentlyCenteredJumpship.getCurrentSystem(currentlyCenteredJumpship.getCurrentSystemID());
					moveMapToPosition(s);
				}
			}
		} else {
			BOStarSystem s = currentlyCenteredJumpship.getCurrentSystem(currentlyCenteredJumpship.getCurrentSystemID());
			moveMapToPosition(s);
		}
		currentlyCenteredJumpship.getJumpshipImageView().toFront();
		currentlyCenteredJumpship.getJumpshipLevelLabel().toFront();
		ActionManager.getAction(ACTIONS.SHOW_JUMPSHIP_DETAIL).execute(currentlyCenteredJumpship);
	}

	@FXML
	private void handlePreviousJumpshipButtonClick() {
		boolean found = false;
		if (currentlyCenteredJumpship == null) {
			currentlyCenteredJumpship = Nexus.getCurrentlySelectedJumpship();
			found = true;
		} else {
			for (BOJumpship js : Nexus.getBoUniverse().getJumpshipListSorted()) {
				if (js.getJumpshipName().equals(currentlyCenteredJumpship.getJumpshipName())) {
					if (Nexus.getBoUniverse().getJumpshipListSorted().lower(js) != null) {
						currentlyCenteredJumpship = Nexus.getBoUniverse().getJumpshipListSorted().lower(js);
						found = true;
						break;
					}
				}
			}
		}
		if (found) {
			centerJumpship();
		} else {
			mapButton04.setDisable(true);
			mapButton05.setDisable(false);
		}
	}

	@FXML
	private void handleNextJumpshipButtonClick() {
		boolean found = false;
		if (currentlyCenteredJumpship == null) {
			currentlyCenteredJumpship = Nexus.getCurrentlySelectedJumpship();
			found = true;
		} else {
			for (BOJumpship js : Nexus.getBoUniverse().getJumpshipListSorted()) {
				if (js.getJumpshipName().equals(currentlyCenteredJumpship.getJumpshipName())) {
					if (Nexus.getBoUniverse().getJumpshipListSorted().higher(js) != null) {
						currentlyCenteredJumpship = Nexus.getBoUniverse().getJumpshipListSorted().higher(js);
						found = true;
						break;
					}
				}
			}
		}
		if (found) {
			centerJumpship();
		} else {
			mapButton04.setDisable(false);
			mapButton05.setDisable(true);
		}
	}

	@FXML
	private void handleAttackButtonClick() {
		mapButton06.setDisable(true);
		mapButton06.setVisible(false);
		ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("attack_youAlreadyHaveAFight"), true));

		BOAttack a = Nexus.getCurrentlySelectedStarSystem().getAttack();

		logger.info("Starting or joining attack on " + a.getStarSystemName() + " with Character: " + Nexus.getCurrentChar().getName());

		AttackCharacterDTO ac = new AttackCharacterDTO();
		ac.setAttackID(a.getAttackDTO().getId());
		ac.setCharacterID(Nexus.getCurrentChar().getId());
		boolean defenderHasCommander = false;
		if (currentPlayerRoleInInvasion == Constants.ROLE_DEFENDER_COMMANDER || currentPlayerRoleInInvasion == Constants.ROLE_DEFENDER_WARRIOR) { // Defender, check if defenders already have a commander
			for (AttackCharacterDTO acl : a.getAttackDTO().getAttackCharList()) {
				if (acl.getType() == Constants.ROLE_DEFENDER_COMMANDER) {
					defenderHasCommander = true;
					break;
				}
			}
			if (!defenderHasCommander) {
				currentPlayerRoleInInvasion = Constants.ROLE_DEFENDER_COMMANDER; // The first defender to show up will initially get the role as commander
			} else {
				currentPlayerRoleInInvasion = Constants.ROLE_DEFENDER_WARRIOR; // If there is a commander, I will fall back to warrior
			}
		}
		boolean attackerHasCommander = false;
		if (currentPlayerRoleInInvasion == Constants.ROLE_ATTACKER_COMMANDER || currentPlayerRoleInInvasion == Constants.ROLE_ATTACKER_WARRIOR) {
			for (AttackCharacterDTO acl : a.getAttackDTO().getAttackCharList()) {
				if (acl.getType() == Constants.ROLE_ATTACKER_COMMANDER) {
					attackerHasCommander = true;
					break;
				}
			}
			if (!attackerHasCommander) {
				currentPlayerRoleInInvasion = Constants.ROLE_ATTACKER_COMMANDER; // The first attacker to show up will initially get the role as commander
			} else {
				currentPlayerRoleInInvasion = Constants.ROLE_ATTACKER_WARRIOR; // If there is a commander, I will fall back to warrior
			}
		}
		ac.setType(currentPlayerRoleInInvasion);
		a.getAttackDTO().getAttackCharList().add(ac);

		if (a.getCharacterId() == null || a.getStoryId() == null) {
			a.getAttackDTO().setCharacterID(Nexus.getCurrentChar().getId());
		}
		a.storeAttack();
	}

	private void checkForOrdersToSend() {
		Platform.runLater(() -> {
			boolean somethingToSend = false;

			for (BOJumpship js : Nexus.getBoUniverse().jumpshipBOs.values()) {
				if (js.getJumpshipFaction() == Nexus.getCurrentUser().getCurrentCharacter().getFactionId()
						&& Nexus.getBoUniverse().routesList.get(js.getJumpshipId()) != null
						&& Nexus.getBoUniverse().routesList.get(js.getJumpshipId()).size() > 1 // routepoint 1 is the starting system
						&& js.isAttackReady()
						&& Nexus.getBoUniverse().currentlyDraggedJumpship != null) {

					somethingToSend = true;
				}
			}

			mapButton01.setDisable(!somethingToSend);
		});
	}

	@FXML
	private void handleConfirmButtonClick() {

		boolean somethingToSend = false;

		// Store jumproutes
		for (BOJumpship js : Nexus.getBoUniverse().jumpshipBOs.values()) {

			BOStarSystem s3 = boUniverse.starSystemBOs.get(js.getCurrentSystemID());
			s3.setLockedByJumpship(true);

			if (js.getJumpshipFaction() == Nexus.getCurrentUser().getCurrentCharacter().getFactionId()
					&& Nexus.getBoUniverse().routesList.get(js.getJumpshipId()) != null
					&& Nexus.getBoUniverse().routesList.get(js.getJumpshipId()).size() > 1 // routepoint 1 is the starting system. if there is no second system in the route, this will fail
					&& js.isAttackReady()
					&& Nexus.getBoUniverse().currentlyDraggedJumpship != null) {

				somethingToSend = true;

				logger.info("Storing route to database");
				ArrayList<RoutePointDTO> route = Nexus.getBoUniverse().routesList.get(js.getJumpshipId());
				JumpshipDTO jsDto = js.getJumpshipDTO();
				jsDto.setRoutepointList(route);
				setJumpshipToAttackReady(js, false);
				js.setAttackReady(false);
				js.storeJumpship(jsDto);

				// Is the first coming jump (next round) to an enemy planet (?)
				RoutePointDTO rp = route.get(1);
				BOStarSystem s = Nexus.getBoUniverse().starSystemBOs.get(rp.getSystemId());
				if (s.getFactionId() != js.getJumpshipFaction()
						&& !(Nexus.getBoUniverse().getAlliedFactions().contains(s.getFactionId()))
				) {
					// This means there is an attack to be stored
					AttackDTO attack = new AttackDTO();
					attack.setAttackedFromStarSystemID((route.get(0)).getSystemId());
					attack.setAttackTypeID(1L); // Type 1: Planetary Assault
					attack.setFactionID_Defender(s.getFactionId());
					attack.setRound(Nexus.getCurrentRound() + 1);
					attack.setSeason(Nexus.getCurrentSeason());
					attack.setJumpshipID(js.getJumpshipId());
					attack.setStarSystemID(rp.getSystemId());
					attack.setStarSystemDataID(s.getStarSystemDataId());
					attack.setCharacterID(Nexus.getCurrentChar().getId());
					attack.setFightsStarted(false);
					attack.setScoreAttackerVictories(0L);
					attack.setScoreDefenderVictories(0L);

					BOAttack boAttack = new BOAttack(attack);
					Nexus.getBoUniverse().attackBOsOpenInThisRound.put(boAttack.getAttackDTO().getId(), boAttack);
					boAttack.storeAttack();

					BOStarSystem attackedSystem;
					BOStarSystem attackerStartedFromSystem;
					attackedSystem = boUniverse.starSystemBOs.get(boAttack.getStarSystemId());
					attackedSystem.setCurrentlyUnderAttack(true);
					attackerStartedFromSystem = boUniverse.starSystemBOs.get(boAttack.getAttackedFromStarSystem());

					if ((boAttack.getRound().equals(boUniverse.currentRound + 1))) {
						attackedSystem.setNextRoundUnderAttack(true);
					}

					// Remove old attack visuals
					ArrayList<Node> lineElementsToRemove = new ArrayList<>();
					for (Node n : attacksPane.getChildren()) {
						if (("attackVisuals" + js.getJumpshipName()).equals(n.getId())) {
							lineElementsToRemove.add(n);
						}
					}
					attacksPane.getChildren().removeAll(lineElementsToRemove);

					ArrayList<Node> backgroundElementsToRemove = new ArrayList<>();
					for (Node n : canvas.getChildren()) {
						if (("attackVisuals" + js.getJumpshipName()).equals(n.getId())) {
							backgroundElementsToRemove.add(n);
						}
					}
					canvas.getChildren().removeAll(backgroundElementsToRemove);

					if (attackedSystem != null && attackerStartedFromSystem != null) {
						Platform.runLater(() -> {
							if (Config.MAP_FLASH_ATTACKED_SYSTEMS) {
								PointD[] points = attackedSystem.getVoronoiRegion();
								if (points != null) {
									Circle circle = new Circle(attackedSystem.getScreenX(), attackedSystem.getScreenY(), Config.MAP_BACKGROUND_AREA_RADIUS);
									circle.setVisible(false);
									Shape systemBackground = Shape.intersect(new Polygon(PointD.toDoubles(points)), circle);
									String colorString = boUniverse.factionBOs.get(attackerStartedFromSystem.getAffiliation()).getColor();
									Color c = Color.web(colorString);
									systemBackground.setFill(c);
									FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.0), (systemBackground));
									fadeTransition.setFromValue(0.5);
									fadeTransition.setToValue(0.0);
									fadeTransition.setAutoReverse(true);
									fadeTransition.setCycleCount(Animation.INDEFINITE);
									fadeTransition.play();
									systemBackground.setId("attackVisuals" + js.getJumpshipName());
									canvas.getChildren().add(systemBackground);
									systemBackground.setVisible(true);
									systemBackground.toBack();
								}
							}

							double attackedSysX = attackedSystem.getScreenX();
							double attackedSysY = attackedSystem.getScreenY();
							double attackedFromSysX = attackerStartedFromSystem.getScreenX();
							double attackedFromSysY = attackerStartedFromSystem.getScreenY();

							Line line = new Line(attackedSysX, attackedSysY, attackedFromSysX, attackedFromSysY);
							line.getStrokeDashArray().setAll(50d, 20d, 5d, 20d);
							line.setStrokeWidth(3);
							line.setStroke(Color.RED);
							line.setStrokeLineCap(StrokeLineCap.ROUND);
							line.setId("attackVisuals" + js.getJumpshipName());

							final double maxOffset = line.getStrokeDashArray().stream().reduce(0d, Double::sum);

							Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(line.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)), new KeyFrame(Duration.seconds(1), new KeyValue(line.strokeDashOffsetProperty(), maxOffset, Interpolator.LINEAR)));
							timeline.setCycleCount(Timeline.INDEFINITE);
							timeline.play();
							attacksPane.getChildren().add(line);
						});
					}
				}

				Platform.runLater(() -> {
					ArrayList<Line> removeLines = new ArrayList<>();
					for (Node n : canvas.getChildren()) {
						if (n instanceof Line) {
							if ("existingRouteLine".equals(n.getId())) {
								n.setVisible(false);
								removeLines.add((Line)n);
							}
						}
					}
					canvas.getChildren().removeAll(removeLines);

					// draw existing route points for my own ships
					ArrayList<Line> lines = new ArrayList<>();
					if (route != null) {
						for (int y = 0; y < route.size() - 1; y++) {
							RoutePointDTO rp1 = route.get(y);
							RoutePointDTO rp2 = route.get(y + 1);
							BOStarSystem s1 = Nexus.getBoUniverse().starSystemBOs.get(rp1.getSystemId());
							BOStarSystem s2 = Nexus.getBoUniverse().starSystemBOs.get(rp2.getSystemId());

							// Dotted line to every stop on the route
							Line line = new Line(s1.getScreenX(), s1.getScreenY(), s2.getScreenX(), s2.getScreenY());
							line.setStrokeWidth(2.5);
							line.getStrokeDashArray().setAll(2d, 10d, 8d, 10d);
							line.setOpacity(0.8);
							if (y == 0) {
								line.setStroke(Color.LIGHTYELLOW);
							} else {
								line.setStroke(Color.LIGHTYELLOW);
							}
							line.setStrokeLineCap(StrokeLineCap.ROUND);
							line.setId("existingRouteLine");
							lines.add(line);
						}
					}
					canvas.getChildren().addAll(lines);
					for (Line l : lines) {
						l.toBack();
					}

					FadeTransition fadeOut = new FadeTransition(Duration.millis(8000), js.routeLines);
					fadeOut.setFromValue(1.0);
					fadeOut.setToValue(0.0);
					fadeOut.setOnFinished(event -> js.routeLines.getChildren().clear());
					fadeOut.play();

					Long currentSystemID = route.get(0).getSystemId();
					ImageView jsi = js.getJumpshipImageView();
					jsi.setMouseTransparent(true);
					jsi.toFront();
					jsi.setVisible(true);

					TranslateTransition transition = new TranslateTransition(Duration.millis(100), jsi);
					transition.setFromX(jsi.getTranslateX());
					transition.setFromY(jsi.getTranslateY());
					transition.setToX(boUniverse.starSystemBOs.get(currentSystemID).getScreenX() - 35);
					transition.setToY(boUniverse.starSystemBOs.get(currentSystemID).getScreenY() - 8);
//					transition.setOnFinished(event -> ActionManager.getAction(ACTIONS.SHOW_POPUP).execute(POPUPS.Orders_Confirmed));
					transition.playFromStart();

					ActionManager.getAction(ACTIONS.SHOW_POPUP).execute(POPUPS.Orders_Confirmed);
				});
			} else {
				logger.info(js.getJumpshipName() + " is not attack ready, nothing happens.");
			}
		}

		if (!somethingToSend) {
			// Warning the user that there were no routes stored!
			logger.info("!!!!!! No routes to store, no orders have been sent !!!!!!");
			ActionManager.getAction(ACTIONS.SHOW_POPUP).execute(POPUPS.Orders_None);
		}
	}

	public void setJumpshipToAttackReady(BOJumpship js, boolean value) {
		if (!value) {
			js.setAttackReady(false);
			String imageName = "jumpship_Faction" + js.getJumpshipFaction() + "_disabled.png";
			Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/" + imageName)));
			js.setJumpshipImage(i);

			ImageView jsiv = js.getJumpshipImageView();
			jsiv.removeEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
			jsiv.removeEventFilter(MouseEvent.DRAG_DETECTED, nodeGestures.getOnMouseDragDetectedEventHandler());
		} else {
			js.setAttackReady(true);
			String imageName = "jumpship_Faction" + js.getJumpshipFaction() + ".png";
			Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/" + imageName)));
			js.setJumpshipImage(i);

			ImageView jsiv = js.getJumpshipImageView();
			jsiv.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
			jsiv.addEventFilter(MouseEvent.DRAG_DETECTED, nodeGestures.getOnMouseDragDetectedEventHandler());
		}
	}

	private synchronized void refreshUniverseMap() {
		// Refresh universe map (GUI)
		ActionManager.getAction(ACTIONS.NOISE).execute(200);
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("10");

		Platform.runLater(() -> {
			if (boUniverse != null) { // this is the same universe, but the objects have been updated to the returned, new universe
				Nexus.setCurrentSeason(boUniverse.currentSeason);
				Nexus.setCurrentSeasonMetaPhase(boUniverse.currentSeasonMetaPhase);
				Nexus.setCurrentRound(boUniverse.currentRound);
				Nexus.setCurrentDate(boUniverse.currentDate);

				ArrayList<Node> nodesToRemove = new ArrayList<>();
				for (Node n : canvas.getChildren()) {
					if ("swordsIcon".equals(n.getId())) {
						nodesToRemove.add(n);
					}
					if (n.getId() != null && n.getId().startsWith("attackFinishedInThisRoundVisuals")) {
						nodesToRemove.add(n);
					}
				}
				canvas.getChildren().removeAll(nodesToRemove);

				// update systems (owner color and active status)
				for (BOStarSystem starSystem : boUniverse.starSystemBOs.values()) {

					String colorString = boUniverse.factionBOs.get(starSystem.getAffiliation()).getColor();
					Color c = Color.web(colorString);
					starSystem.getStarSystemCircle().setStroke(c.deriveColor(1, 1, 1, 0.8));
					starSystem.getStarSystemCircle().setFill(c.deriveColor(1, 1, 1, 0.4));

					starSystem.setLockedByJumpship(false); // will be set later for each ship

					BOAttack a = starSystem.getAttack();
					Image attackMarker;
					if (a != null) {
						if (a.getRound().equals(boUniverse.currentRound) && a.getStarSystemId().equals(starSystem.getStarSystemId())) {
							attackMarker = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/attack2.png")));
							double markerDim = 16.0d;
							ImageView marker;
							marker = new ImageView();
							marker.setFitWidth(markerDim);
							marker.setFitHeight(markerDim);
							marker.setImage(attackMarker);
							marker.setTranslateX(starSystem.getScreenX() - (markerDim / 2));
							marker.setTranslateY(starSystem.getScreenY() - (markerDim / 2) - 12);
							marker.setMouseTransparent(true);
							marker.toFront();
							marker.setId("swordsIcon");
							canvas.getChildren().add(marker);
						}
					}

					if (starSystem.isActive()) {
						if (starSystem.isActiveInPhase(Nexus.getCurrentSeasonMetaPhase())) {
							// logger.info("System is active in the current MetaPhase!");
							starSystem.getStarSystemGroup().setOpacity(1.0d);
							starSystem.getLevelLabel().setOpacity(1.0d);
							if (starSystem.getIndustryImage() != null) {
								starSystem.getIndustryImage().setOpacity(1.0d);
							}
							starSystem.getStarSystemGroup().setMouseTransparent(false);
						} else {
							// logger.info("System is NOT active in the current MetaPhase!");
							starSystem.getStarSystemGroup().setOpacity(0.2d);
							starSystem.getLevelLabel().setOpacity(0.2d);
							if (starSystem.getIndustryImage() != null) {
								starSystem.getIndustryImage().setOpacity(0.2d);
							}
							starSystem.getStarSystemGroup().setMouseTransparent(true);
						}
					}
				}

				ArrayList<Shape> dashedBackgrounds = new ArrayList<>();
				ArrayList<Shape> flashingBackgrounds = new ArrayList<>();

				// Move the ships
				for (BOJumpship js : boUniverse.getJumpshipList()) {
					Long currentSystemID = js.getCurrentSystemID();
					boolean myOwnShip = js.getJumpshipFaction() == Nexus.getCurrentUser().getCurrentCharacter().getFactionId();
					ImageView jumpshipImage = js.getJumpshipImageView();

					BOStarSystem s3 = boUniverse.starSystemBOs.get(js.getCurrentSystemID());
					s3.setLockedByJumpship(true);

					Long targetSystemId = null;
					Long fallbackToSystemId = null;

					ArrayDeque<String> history = new ArrayDeque<>(Arrays.asList(js.getJumpshipDTO().getStarSystemHistory().split(";")));
					if (history.size() > 0) {
						String targetSystemIdString = history.getLast();
						targetSystemId = Long.parseLong(targetSystemIdString);
						history.removeLast();
						if (history.size() > 0) {
							String fallbackToSystemIdString = history.getLast();
							fallbackToSystemId = Long.parseLong(fallbackToSystemIdString);
						}
					}

					if (myOwnShip) {
						if (js.isAttackReady()) {
							//Image left_blue = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/jumpship_left_blue_1.png")));
							String imageName = "jumpship_Faction" + js.getJumpshipFaction() + ".png";
							Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/" + imageName)));

							jumpshipImage.setImage(i);
							jumpshipImage.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
							jumpshipImage.addEventFilter(MouseEvent.DRAG_DETECTED, nodeGestures.getOnMouseDragDetectedEventHandler());
						} else {
							//Image left_neutral = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/jumpship_left_neutral.png")));
							String imageName = "jumpship_Faction" + js.getJumpshipFaction() + "_disabled.png";
							Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/" + imageName)));

							jumpshipImage.setImage(i);
						}
					} else {
						jumpshipImage.removeEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
						jumpshipImage.removeEventFilter(MouseEvent.DRAG_DETECTED, nodeGestures.getOnMouseDragDetectedEventHandler());

						if (js.isAttackReady()) {
							//Image right_red = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/jumpship_right_red.png")));
							String imageName = "jumpship_Faction" + js.getJumpshipFaction() + ".png";
							Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/" + imageName)));

							jumpshipImage.setImage(i);
						} else {
							//Image right_red = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/jumpship_right_red.png")));
							String imageName = "jumpship_Faction" + js.getJumpshipFaction() + ".png";
							Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/" + imageName)));

							jumpshipImage.setImage(i);
						}
					}

					// Remove old attack visuals
					ArrayList<Node> lineElementsToRemove = new ArrayList<>();
					for (Node n : attacksPane.getChildren()) {
						if (("attackVisuals" + js.getJumpshipName()).equals(n.getId())) {
							lineElementsToRemove.add(n);
						}
					}
					attacksPane.getChildren().removeAll(lineElementsToRemove);

					ArrayList<Node> backgroundElementsToRemove = new ArrayList<>();
					for (Node n : canvas.getChildren()) {
						if (("attackVisuals" + js.getJumpshipName()).equals(n.getId())) {
							backgroundElementsToRemove.add(n);
						}
					}
					canvas.getChildren().removeAll(backgroundElementsToRemove);

					for (BOAttack boAttack : boUniverse.attackBOsOpenInThisRound.values()) {
						if (boAttack.getAttackDTO().getJumpshipID().equals(js.getJumpshipId())) {
							BOStarSystem attackedSystem;
							BOStarSystem attackerStartedFromSystem;
							attackedSystem = boUniverse.starSystemBOs.get(boAttack.getStarSystemId());
							attackedSystem.setCurrentlyUnderAttack(true);
							attackerStartedFromSystem = boUniverse.starSystemBOs.get(boAttack.getAttackedFromStarSystem());

							if ((boAttack.getRound().equals(boUniverse.currentRound + 1))) {
								attackedSystem.setNextRoundUnderAttack(true);
							}

							if (attackerStartedFromSystem != null) {
								if (Config.MAP_FLASH_ATTACKED_SYSTEMS) {
									PointD[] points = attackedSystem.getVoronoiRegion();
									if (points != null) {
										Circle circle = new Circle(attackedSystem.getScreenX(), attackedSystem.getScreenY(), Config.MAP_BACKGROUND_AREA_RADIUS);
										circle.setVisible(false);
										Shape systemBackground = Shape.intersect(new Polygon(PointD.toDoubles(points)), circle);
										String colorString = boUniverse.factionBOs.get(attackerStartedFromSystem.getAffiliation()).getColor();
										Color c = Color.web(colorString);
										systemBackground.setFill(c);

										double flashIntensity = Config.MAP_FLASH_ATTACKED_SYSTEMS_INTENSITY;
										long flashDuration = Config.MAP_FLASH_ATTACKED_SYSTEMS_DURATION;
										if (boAttack.getAttackDTO().getRound().equals(boUniverse.currentRound + 1)) {
											flashIntensity = Config.MAP_FLASH_ATTACKED_SYSTEMS_INTENSITY_NEXTROUND;
											flashDuration = Config.MAP_FLASH_ATTACKED_SYSTEMS_DURATION_NEXTROUND;
										}

										// logger.info("Intensity: " + flashIntensity);

										FadeTransition fadeTransition = new FadeTransition(Duration.seconds(flashDuration), (systemBackground));
										fadeTransition.setFromValue(flashIntensity);
										fadeTransition.setToValue(0.0);
										fadeTransition.setAutoReverse(true);
										fadeTransition.setCycleCount(Animation.INDEFINITE);
										fadeTransition.play();
										systemBackground.setId("attackVisuals" + js.getJumpshipName());
										canvas.getChildren().add(systemBackground);
										systemBackground.setVisible(true);
										systemBackground.toBack();
										flashingBackgrounds.add(systemBackground);
									}
								}
							}

							break;
						}
					}

					for (BOAttack boAttack : boUniverse.attackBOsAllInThisRound.values()) {
						if (boAttack.getAttackDTO().getJumpshipID().equals(js.getJumpshipId())) {
							BOStarSystem attackedSystem;
							BOStarSystem attackerStartedFromSystem;
							attackedSystem = boUniverse.starSystemBOs.get(boAttack.getStarSystemId());
							attackedSystem.setCurrentlyUnderAttack(true);
							attackerStartedFromSystem = boUniverse.starSystemBOs.get(boAttack.getAttackedFromStarSystem());

							double attackedSysX = attackedSystem.getScreenX();
							double attackedSysY = attackedSystem.getScreenY();
							double attackedFromSysX = attackerStartedFromSystem.getScreenX();
							double attackedFromSysY = attackerStartedFromSystem.getScreenY();

							Line line = new Line(attackedSysX, attackedSysY, attackedFromSysX, attackedFromSysY);
							line.getStrokeDashArray().setAll(50d, 20d, 5d, 20d);
							line.setStrokeWidth(3);
							line.setStroke(Color.RED);
							line.setStrokeLineCap(StrokeLineCap.ROUND);
							line.setId("attackVisuals" + js.getJumpshipName());

							final double maxOffset = line.getStrokeDashArray().stream().reduce(0d, Double::sum);

							Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(line.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)), new KeyFrame(Duration.seconds(1), new KeyValue(line.strokeDashOffsetProperty(), maxOffset, Interpolator.LINEAR)));
							timeline.setCycleCount(Timeline.INDEFINITE);
							timeline.play();
							attacksPane.getChildren().add(line);

							break;
						}
					}

					if (currentSystemID != null && targetSystemId != null) {
						// TODO_C3: Check if the attack succeeded or if the unit lost (then move backwards and delete route)
						// TODO_C3: If the fallback system has been taken by the enemy, trigger a new event here. Scenario "Fighting retreat"?
						// TODO_C3: Check what faction owns the target system
						if (!currentSystemID.equals(targetSystemId)) {
							ImageView jsi = js.getJumpshipImageView();
							jsi.setMouseTransparent(true);
							jsi.toFront();
							jsi.setVisible(true);

							TranslateTransition transition = new TranslateTransition(Duration.millis(500), jsi);
							transition.setFromX(jsi.getTranslateX());
							transition.setFromY(jsi.getTranslateY());
							transition.setToX(boUniverse.starSystemBOs.get(targetSystemId).getScreenX() - 35);
							transition.setToY(boUniverse.starSystemBOs.get(targetSystemId).getScreenY() - 8);
							transition.setOnFinished(event -> {
								jsi.setMouseTransparent(false);
								ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("9");
							});
							transition.playFromStart();
						} else {
							ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("10");
						}
						jumpshipImage.setMouseTransparent(false);
						js.getJumpshipImageView().toFront();
						js.getJumpshipLevelLabel().toFront();

						js.setCurrentSystemID(targetSystemId);
						js.setRoute(boUniverse.routesList.get(js.getJumpshipId()));
					} else {
						logger.info("Jumpship '" + js.getJumpshipName() + "' has no current system. Seems to be a mistake!");
						ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("11");
					}
				}

				for (BOAttack boAttack : boUniverse.attackBOsFinishedInThisRound.values()) {
					BOStarSystem attackedSystem;
					BOStarSystem attackerStartedFromSystem;
					attackedSystem = boUniverse.starSystemBOs.get(boAttack.getStarSystemId());
					attackerStartedFromSystem = boUniverse.starSystemBOs.get(boAttack.getAttackedFromStarSystem());

					if (attackedSystem != null && attackerStartedFromSystem != null) {
						PointD[] points = attackedSystem.getVoronoiRegion();
						if (points != null) {
							Circle circle = new Circle(attackedSystem.getScreenX(), attackedSystem.getScreenY(), Config.MAP_BACKGROUND_AREA_RADIUS);
							circle.setVisible(false);
							Shape systemBackground = Shape.intersect(new Polygon(PointD.toDoubles(points)), circle);

							BOFaction factionAttacker = boUniverse.getFactionByID(boAttack.getAttackerFactionId().longValue());
							BOFaction factionDefender = boUniverse.getFactionByID(boAttack.getDefenderFactionId().longValue());
							String colorString1 = boUniverse.factionBOs.get(factionAttacker.getShortName()).getColor();
							String colorString2 = boUniverse.factionBOs.get(factionDefender.getShortName()).getColor();

							if (boAttack.getAttackerFactionId().equals(boAttack.getAttackDTO().getFactionID_Winner().intValue())) {
								Color c1 = Color.web(colorString1);
								Color c2 = Color.web(colorString2);
								Stop[] stops = new Stop[]{new Stop(0.8, c1), new Stop(0.8, c2)};
								LinearGradient lg = new LinearGradient(0, 0, 10, 10, false, CycleMethod.REPEAT, stops);
								systemBackground.setFill(lg);
								systemBackground.setOpacity(0.5);
							} else {
								Color c1 = Color.web(colorString2);
								Color c2 = Color.web(colorString1);
								Stop[] stops = new Stop[]{new Stop(0.8, c1), new Stop(0.8, c2)};
								LinearGradient lg = new LinearGradient(0, 0, 10, 10, false, CycleMethod.REPEAT, stops);
								systemBackground.setFill(lg);
								systemBackground.setOpacity(0.16);
							}
							systemBackground.setId("attackFinishedInThisRoundVisuals" + attackedSystem.getName());

							Shape oldBG = null;
							for (Node n : canvas.getChildren()) {
								if (n.getId() != null && n.getId().equals("attackFinishedInThisRoundVisuals" + attackedSystem.getName())) {
									oldBG = (Shape) n;
									break;
								}
							}
							if (oldBG != null) {
								canvas.getChildren().remove(oldBG);
							}
							canvas.getChildren().add(systemBackground);
							systemBackground.setVisible(true);
							systemBackground.toBack();
							dashedBackgrounds.add(systemBackground);
						}
					}
				}

				canvas.getChildren().remove(borders);
				borders = VoronoiDelaunay.updateAreas();
				canvas.getChildren().add(borders);
				borders.toBack();
				for (Shape s : dashedBackgrounds) {
					s.toBack();
				}
				for (Shape s : flashingBackgrounds) {
					s.toBack();
				}
				ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("10_33");
			}
		});
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("10_33");
	}

	/**
	 * Initializes the universe star map from the universe business object.
	 */
	private void initializeUniverseMap() {
		boUniverse = Nexus.getBoUniverse();
		boolean mapCenteredOnJumpship = false;

		if (boUniverse != null) {
			String dims = C3Properties.getProperty(C3PROPS.MAP_DIMENSIONS);
			int d = Integer.parseInt(dims);
			if (d < 3000) {
				d = 3000;
			}
			if (d < Config.MAP_DIM) {
				logger.info("Using map dimensions from user properties: " + d);
				Config.setMapDim(d);
			}
			logger.info("Beginning to build the star map from received universe data.");

			Nexus.setCurrentSeason(boUniverse.currentSeason);
			Nexus.setCurrentSeasonMetaPhase(boUniverse.currentSeasonMetaPhase);
			Nexus.setCurrentRound(boUniverse.currentRound);
			Nexus.setCurrentDate(boUniverse.currentDate);

			ArrayList<Line> lines = new ArrayList<>();

			Image selectionMarker = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/selectionIndicator.png")));
			Image attackMarker = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/attackIndicator.png")));
			Image travelMarker = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/travelIndicator.png")));

			starMapPane.setOpacity(0.0f);
			mapButton01.setOpacity(0.0f);
			mapButton02.setOpacity(0.0f);
			mapButton03.setOpacity(0.0f);
//			mapButton04.setOpacity(0.0f);
//			mapButton05.setOpacity(0.0f);
			mapButton06.setOpacity(0.0f);
			paneSystemDetail.setOpacity(0.0f);
			paneAttackDetail.setOpacity(0.0f);
			paneJumpshipDetail.setOpacity(0.0f);
			buttonBackground.setOpacity(0.0f);

			try {
				canvas = new PannableCanvas();
				canvas.setTranslateX(Config.MAP_INITIAL_TRANSLATE_X);
				canvas.setTranslateY(Config.MAP_INITIAL_TRANSLATE_Y);
				canvas.setPrefSize(Config.MAP_DIM, Config.MAP_DIM);

				nodeGestures = new NodeGestures(canvas);
				nodeGestures.setSelectionMarker(selectionMarker);
				nodeGestures.setAttackMarker(attackMarker);
				nodeGestures.setTravelMarker(travelMarker);

				for (BOStarSystem starSystem : boUniverse.starSystemBOs.values()) {
					String name = starSystem.getName();
					Long id = starSystem.getStarSystemId();
					double x = starSystem.getScreenX();
					double y = starSystem.getScreenY();

					if (starSystem.isCapitalWorld()) {
						logger.info(starSystem.getName() + " is the capital planet of faction " + starSystem.getAffiliation());
					}

					if ("Terra".equals(name)) {
						Nexus.setTerra(starSystem);
					}

					Integer myFactionId = Nexus.getCurrentChar().getFactionId();
					if (starSystem.isCapitalWorld() && starSystem.getFactionId().intValue() == myFactionId) {
						Nexus.setCurrentlySelectedStarSystem(starSystem);
						Nexus.setHomeWorld(starSystem);
					}

					BOAttack a = starSystem.getAttack();
					Image attackMarkerSwords;
					if (a != null) {
						if (a.getRound().equals(boUniverse.currentRound) && a.getStarSystemId().equals(starSystem.getStarSystemId())) {
							attackMarkerSwords = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/attack2.png")));
							double markerDim = 16.0d;
							ImageView marker;
							marker = new ImageView();
							marker.setFitWidth(markerDim);
							marker.setFitHeight(markerDim);
							marker.setImage(attackMarkerSwords);
							marker.setTranslateX(starSystem.getScreenX() - (markerDim / 2));
							marker.setTranslateY(starSystem.getScreenY() - (markerDim / 2) - 12);
							marker.setMouseTransparent(true);
							marker.toFront();
							marker.setId("swordsIcon");
							canvas.getChildren().add(marker);
						}
					}

					Group starSystemGroup = new Group();
					starSystemGroup.setId(id.toString());
					StackPane stackPane = new StackPane();

					Label starSystemLabel = new Label(name);
					starSystemLabel.setCacheHint(CacheHint.SCALE);
					starSystemLabel.setPadding(new Insets(25, 0, 0, 0));
					starSystemLabel.setStyle("-fx-font-family:'Arial';-fx-font-size:10px;-fx-text-fill:#ffffff;");

					starSystem.setStarSystemLabel(starSystemLabel);
					stackPane.getChildren().add(0, starSystemLabel);

					String colorString = boUniverse.factionBOs.get(starSystem.getAffiliation()).getColor();
					Color c = Color.web(colorString);
					Circle starSystemCircle = new Circle(5);
					starSystemCircle.setId(starSystem.getStarSystemId().toString());
					starSystemCircle.setStroke(c.deriveColor(1, 1, 1, 0.8));
					starSystemCircle.setFill(c.deriveColor(1, 1, 1, 0.4));
					starSystemCircle.setVisible(true);
					starSystemCircle.toFront();
					starSystemCircle.setCacheHint(CacheHint.SCALE);
					starSystemCircle.addEventFilter(MouseEvent.MOUSE_ENTERED, nodeGestures.getOnStarSystemHoverEnteredEventHandler());
					starSystemCircle.addEventFilter(MouseEvent.MOUSE_EXITED, nodeGestures.getOnStarSystemHoverExitedEventHandler());
					starSystemCircle.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, nodeGestures.getOnStarSystemDragEnteredEventHandler());
					starSystemCircle.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, nodeGestures.getOnStarSystemDragExitedEventHandler());

					starSystem.setStarSystemCircle(starSystemCircle);

					// Capital worlds
					if (starSystem.isCapitalWorld()) {
						Circle starSystemCapitalCircle = new Circle(12);
						starSystemCapitalCircle.setStrokeWidth(3.0);
						starSystemCapitalCircle.setId(starSystem.getId().toString() + "_CapitalMarker");
						starSystemCapitalCircle.setStroke(c.deriveColor(1, 1, 1, 0.75));
						starSystemCapitalCircle.setFill(c.deriveColor(1, 1, 1, 0.0));
						starSystemCapitalCircle.setVisible(true);
						starSystemCapitalCircle.toBack();
						starSystemCapitalCircle.setMouseTransparent(true);
						stackPane.getChildren().add(starSystemCapitalCircle);
						starSystemLabel.toFront();
					}

					starSystem.setLockedByJumpship(false); // will be set later for each ship

					// Industrial worlds
					ImageView industryImage = null;
					if (starSystem.getType().equals(1L)) {
						// This is an industrial world
						industryImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon_industry.png"))));
						industryImage.setId(starSystem.getName() + "_industrialMarker");
						industryImage.setPreserveRatio(true);
						industryImage.setFitWidth(12);
						industryImage.setCacheHint(CacheHint.QUALITY);
						industryImage.setSmooth(false);
						industryImage.setMouseTransparent(true);
						industryImage.setOpacity(0.9d);
						industryImage.toFront();
						industryImage.setVisible(true);
						industryImage.setTranslateX(starSystem.getScreenX() + 6);
						industryImage.setTranslateY(starSystem.getScreenY() - 5);
						starSystem.setIndustryMarker(industryImage);
					}

					// add level indicator
					Rectangle bgBox = new Rectangle();
					bgBox.setStrokeWidth(0.8);
					bgBox.setStroke(Color.BLACK);
					bgBox.setFill(Color.WHITE);
					bgBox.setWidth(5);
					bgBox.setHeight(7);
					bgBox.setLayoutX(0);
					bgBox.setLayoutY(0);
					bgBox.setMouseTransparent(true);
					bgBox.toBack();

					Text starSystemLevelLabel = new Text();
					starSystemLevelLabel.setId(name + "_Level");
					starSystemLevelLabel.setCacheHint(CacheHint.SCALE);
					starSystemLevelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 5));
					starSystemLevelLabel.setFill(Color.BLACK);
					starSystemLevelLabel.setStrokeWidth(0);
					starSystemLevelLabel.setStroke(Color.BLACK);
					starSystemLevelLabel.setBoundsType(TextBoundsType.LOGICAL_VERTICAL_CENTER);
					starSystemLevelLabel.setText(String.valueOf(starSystem.getLevel()));
					starSystemLevelLabel.setMouseTransparent(true);
					starSystemLevelLabel.setLayoutX(1.2);
					starSystemLevelLabel.setLayoutY(5.4);
					starSystemLevelLabel.toFront();

					Pane levelLabel = new Pane();
					levelLabel.setPadding(new Insets(0,0,0,0));
					levelLabel.getChildren().add(0, bgBox);
					levelLabel.getChildren().add(1, starSystemLevelLabel);
					levelLabel.setMouseTransparent(true);
					levelLabel.setTranslateX(starSystem.getScreenX() + 2.4);
					levelLabel.setTranslateY(starSystem.getScreenY() - 0.6);

					// stack star system layers to stackpane
					stackPane.getChildren().add(1, starSystemCircle);
					starSystemCircle.toFront();

					starSystemGroup.getChildren().add(stackPane);

					// Level and industry icons need to be added to the canvas directly, or the centering of circles
					// and labels does not work anymore!
					canvas.getChildren().add(levelLabel);
					if (industryImage != null) {
						canvas.getChildren().add(industryImage);
					}
					starSystemGroup.setTranslateX(x);
					starSystemGroup.setTranslateY(y);
					starSystemGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMouseClickedEventHandler());

					if (starSystem.isActive()) {
						if (starSystem.isActiveInPhase(Nexus.getCurrentSeasonMetaPhase())) {
							// logger.info("System is active in the current MetaPhase!");
						} else {
							starSystemGroup.setOpacity(0.2d);
							levelLabel.setOpacity(0.2d);
							if (industryImage != null) {
								industryImage.setOpacity(0.2d);
							}
							starSystemGroup.setMouseTransparent(true);
						}
					}

					starSystem.setStarSystemStackPane(stackPane);
					starSystem.setStarSystemGroup(starSystemGroup);
					starSystem.setLevelLabel(levelLabel);
					canvas.getChildren().add(starSystemGroup);

					levelLabel.toFront();
				}

				canvas.addStarPane();
				canvas.addGrid_Center();
				canvas.addGrid_500();
				canvas.addGrid_250();
				canvas.addOuterBorder();
				canvas.setVisibility();

				//				Circle circle1 = new Circle(Config.MAP_WIDTH / 2, Config.MAP_HEIGHT / 2, 40);
				//				circle1.setStroke(Color.ORANGE);
				//				circle1.setFill(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
				//				circle1.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
				//				circle1.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
				//				canvas.getChildren().add(circle1);

				borders = VoronoiDelaunay.getAreas();
				borders.setId("borderPane");
				canvas.getChildren().add(borders);
				borders.toBack();

				// Attacks pane
				attacksPane = new Pane();
				canvas.setAttacksPane(attacksPane);

//				logger.info("Marker 01");

				for (BOAttack attack : boUniverse.attackBOsAllInThisRound.values()) {
					if (attack.getSeason().equals(boUniverse.currentSeason)
							&& (attack.getRound().equals(boUniverse.currentRound + 1))
							|| (attack.getRound().equals(boUniverse.currentRound))
					) {
						BOStarSystem attackedSystem;
						BOStarSystem attackerStartedFromSystem;
						BOJumpship jumpship = boUniverse.getJumpshipByID(attack.getJumpshipId());

						attackedSystem = boUniverse.starSystemBOs.get(attack.getStarSystemId());
						attackedSystem.setCurrentlyUnderAttack(true);
						if ((attack.getRound().equals(boUniverse.currentRound + 1))) {
							attackedSystem.setNextRoundUnderAttack(true);
						}
						attackerStartedFromSystem = boUniverse.starSystemBOs.get(attack.getAttackedFromStarSystem());

						if (attackerStartedFromSystem != null) {
							if (Config.MAP_FLASH_ATTACKED_SYSTEMS) {
								if (attack.getAttackDTO().getFactionID_Winner() == null) {
									PointD[] points = attackedSystem.getVoronoiRegion();
									if (points != null) {
										Circle circle = new Circle(attackedSystem.getScreenX(), attackedSystem.getScreenY(), Config.MAP_BACKGROUND_AREA_RADIUS);
										circle.setVisible(false);
										Shape systemBackground = Shape.intersect(new Polygon(PointD.toDoubles(points)), circle);
										String colorString = boUniverse.factionBOs.get(attackerStartedFromSystem.getAffiliation()).getColor();
										Color c = Color.web(colorString);
										systemBackground.setFill(c);

										double flashIntensity = Config.MAP_FLASH_ATTACKED_SYSTEMS_INTENSITY;
										long flashDuration = Config.MAP_FLASH_ATTACKED_SYSTEMS_DURATION;
										if (attack.getAttackDTO().getRound().equals(boUniverse.currentRound + 1)) {
											flashIntensity = Config.MAP_FLASH_ATTACKED_SYSTEMS_INTENSITY_NEXTROUND;
											flashDuration = Config.MAP_FLASH_ATTACKED_SYSTEMS_DURATION_NEXTROUND;
										}

										// logger.info("Intensity: " + flashIntensity);

										FadeTransition fadeTransition = new FadeTransition(Duration.seconds(flashDuration), (systemBackground));
										fadeTransition.setFromValue(flashIntensity);
										fadeTransition.setToValue(0.0);
										fadeTransition.setAutoReverse(true);
										fadeTransition.setCycleCount(Animation.INDEFINITE);
										fadeTransition.play();
										systemBackground.setId("attackVisuals" + jumpship.getJumpshipName());
										canvas.getChildren().add(systemBackground);
										systemBackground.setVisible(true);
										systemBackground.toBack();
									}
								} else {
									// The fight has been finished here, so no flashing.
									// The system will be marked with stripes later.
								}
							}

							double attackedSysX = attackedSystem.getScreenX();
							double attackedSysY = attackedSystem.getScreenY();
							double attackedFromSysX = attackerStartedFromSystem.getScreenX();
							double attackedFromSysY = attackerStartedFromSystem.getScreenY();

							Line line = new Line(attackedSysX, attackedSysY, attackedFromSysX, attackedFromSysY);
							line.getStrokeDashArray().setAll(50d, 20d, 5d, 20d);
							line.setStrokeWidth(3);
							line.setStroke(Color.RED);
							line.setStrokeLineCap(StrokeLineCap.ROUND);
							line.setId("attackVisuals" + jumpship.getJumpshipName());

							final double maxOffset = line.getStrokeDashArray().stream().reduce(0d, Double::sum);

							Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(line.strokeDashOffsetProperty(), 0, Interpolator.LINEAR)), new KeyFrame(Duration.seconds(1), new KeyValue(line.strokeDashOffsetProperty(), maxOffset, Interpolator.LINEAR)));
							timeline.setCycleCount(Timeline.INDEFINITE);
							timeline.play();
							attacksPane.getChildren().add(line);
						}
					}
				}

//				logger.info("Marker 02");

				for (BOAttack boAttack : boUniverse.attackBOsFinishedInThisRound.values()) {
					BOStarSystem attackedSystem;
					BOStarSystem attackerStartedFromSystem;
					attackedSystem = boUniverse.starSystemBOs.get(boAttack.getStarSystemId());
					attackerStartedFromSystem = boUniverse.starSystemBOs.get(boAttack.getAttackedFromStarSystem());

					if (attackedSystem != null && attackerStartedFromSystem != null) {
						PointD[] points = attackedSystem.getVoronoiRegion();
						if (points != null) {
							Circle circle = new Circle(attackedSystem.getScreenX(), attackedSystem.getScreenY(), Config.MAP_BACKGROUND_AREA_RADIUS);
							circle.setVisible(false);
							Shape systemBackground = Shape.intersect(new Polygon(PointD.toDoubles(points)), circle);

							BOFaction factionAttacker = boUniverse.getFactionByID(boAttack.getAttackerFactionId().longValue());
							BOFaction factionDefender = boUniverse.getFactionByID(boAttack.getDefenderFactionId().longValue());
							String colorString1 = boUniverse.factionBOs.get(factionAttacker.getShortName()).getColor();
							String colorString2 = boUniverse.factionBOs.get(factionDefender.getShortName()).getColor();

							if (boAttack.getAttackerFactionId().equals(boAttack.getAttackDTO().getFactionID_Winner().intValue())) {
								Color c1 = Color.web(colorString1);
								Color c2 = Color.web(colorString2);
								Stop[] stops = new Stop[]{new Stop(0.8, c1), new Stop(0.8, c2)};
								LinearGradient lg = new LinearGradient(0, 0, 10, 10, false, CycleMethod.REPEAT, stops);
								systemBackground.setFill(lg);
								systemBackground.setOpacity(0.5);
							} else {
								Color c1 = Color.web(colorString2);
								Color c2 = Color.web(colorString1);
								Stop[] stops = new Stop[]{new Stop(0.8, c1), new Stop(0.8, c2)};
								LinearGradient lg = new LinearGradient(0, 0, 10, 10, false, CycleMethod.REPEAT, stops);
								systemBackground.setFill(lg);
								systemBackground.setOpacity(0.16);
							}
							systemBackground.setId("attackFinishedInThisRoundVisuals" + attackedSystem.getName());
							canvas.getChildren().add(systemBackground);
							systemBackground.setVisible(true);
							systemBackground.toBack();
						}
					}
				}

				canvas.getChildren().add(attacksPane);
				attacksPane.toBack();

				HashMap<Long, Integer> numberOfJumpshipsOnSystem = new HashMap<>();

				for (BOJumpship js : boUniverse.getJumpshipList()) {
					Long currentSystemID = js.getCurrentSystemID();
					boolean myOwnShip = js.getJumpshipFaction() == Nexus.getCurrentUser().getCurrentCharacter().getFactionId();

					if (numberOfJumpshipsOnSystem.get(js.getCurrentSystemID()) == null) {
						numberOfJumpshipsOnSystem.put(js.getCurrentSystemID(), 0);
					} else {
						Integer number = numberOfJumpshipsOnSystem.get(js.getCurrentSystemID());
						number += 1;
						numberOfJumpshipsOnSystem.remove(js.getCurrentSystemID());
						numberOfJumpshipsOnSystem.put(js.getCurrentSystemID(), number);
					}

					BOStarSystem s3 = boUniverse.starSystemBOs.get(js.getCurrentSystemID());
					s3.setLockedByJumpship(true);

					if (js.getRoute() != null) {
						for (RoutePointDTO rp : js.getRoute()) {
							if (rp.getRoundId().intValue() == Nexus.getCurrentRound()) {
								if (rp.getSystemId().equals(currentSystemID)) {
									// In this round, the jumpship should be at System of this routepoint
									// --> all is fine, the jumpship is where it is expected to be
									logger.info("Jumpship " + js.getJumpshipName() + " is where it is expected to be.");
								} else {
									// The jumpship has not jumped and is not at the system it is expected
									currentSystemID = rp.getSystemId();
									logger.info("Fixing position of jumpship that apparently did not jump!");
								}
							}
						}
					}

					Long jsLevel = js.getLevel();
					BOFaction f = Nexus.getBoUniverse().getFactionByID(js.getJumpshipFaction());

					javafx.scene.control.Label levelLabel = Tools.getLevelLabel(jsLevel + "", f.getColor());
					levelLabel.setMouseTransparent(true);

					if (currentSystemID != null) {
						ImageView jumpshipImage;
						if (myOwnShip) {
							if (js.isAttackReady()) {
								String imageName = "jumpship_Faction" + js.getJumpshipFaction() + ".png";
								Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/" + imageName)));
								jumpshipImage = new ImageView(i);
								jumpshipImage.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
								jumpshipImage.addEventFilter(MouseEvent.DRAG_DETECTED, nodeGestures.getOnMouseDragDetectedEventHandler());
							} else {
								String imageName = "jumpship_Faction" + js.getJumpshipFaction() + "_disabled.png";
								Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/" + imageName)));
								jumpshipImage = new ImageView(i);
							}

							// draw existing route points for my own ships
							List<BOStarSystem> route = js.getStoredRoute();
							if (route != null) {
								for (int y = 0; y < route.size() - 1; y++) {
									BOStarSystem s1 = route.get(y);
									BOStarSystem s2 = route.get(y + 1);

									// Dotted line to every stop on the route
									Line line = new Line(s1.getScreenX(), s1.getScreenY(), s2.getScreenX(), s2.getScreenY());
									line.setStrokeWidth(2.5);
									line.getStrokeDashArray().setAll(2d, 10d, 8d, 10d);
									line.setOpacity(0.8);
									if (y == 0) {
										line.setStroke(Color.LIGHTYELLOW);
									} else {
										line.setStroke(Color.LIGHTYELLOW);
									}
									line.setStrokeLineCap(StrokeLineCap.ROUND);
									line.setId("existingRouteLine");
									lines.add(line);
								}
							}
						} else {
							if (js.isAttackReady()) {
								String imageName = "jumpship_Faction" + js.getJumpshipFaction() + ".png";
								Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/" + imageName)));
								jumpshipImage = new ImageView(i);
							} else {
								String imageName = "jumpship_Faction" + js.getJumpshipFaction() + ".png";
								Image i = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/" + imageName)));
								jumpshipImage = new ImageView(i);
							}
						}

						int number = numberOfJumpshipsOnSystem.get(js.getCurrentSystemID());
						int offset = 7;

						levelLabel.setId(js.getJumpshipName() + "LEVEL");
						levelLabel.setTranslateX((boUniverse.starSystemBOs.get(currentSystemID).getScreenX() - 27 - offset * number));
						levelLabel.setTranslateY((boUniverse.starSystemBOs.get(currentSystemID).getScreenY() - 10 + offset * number));
						levelLabel.setMouseTransparent(false);
						levelLabel.toFront();
						levelLabel.setVisible(true);
						canvas.getChildren().add(levelLabel);

						jumpshipImage.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
						jumpshipImage.addEventFilter(MouseEvent.MOUSE_RELEASED, nodeGestures.getOnMouseReleasedEventHandler());
						jumpshipImage.addEventFilter(MouseEvent.MOUSE_CLICKED, nodeGestures.getOnJumpShipClickedEventHandler());

						jumpshipImage.setId(js.getJumpshipName());
						jumpshipImage.setPreserveRatio(true);
						jumpshipImage.setFitWidth(30);
						jumpshipImage.setCacheHint(CacheHint.QUALITY);
						jumpshipImage.setSmooth(false);
						jumpshipImage.setTranslateX((boUniverse.starSystemBOs.get(currentSystemID).getScreenX() - 35) - offset * number);
						jumpshipImage.setTranslateY((boUniverse.starSystemBOs.get(currentSystemID).getScreenY() - 8) + offset * number);
						jumpshipImage.setMouseTransparent(false);
						jumpshipImage.toFront();
						jumpshipImage.setVisible(true);
						canvas.getChildren().add(jumpshipImage);

						js.setJumpshipImageView(jumpshipImage);
						js.setJumpshipLevelLabel(levelLabel);
						js.setRoute(boUniverse.routesList.get(js.getJumpshipId()));
					} else {
						String m = "Jumpship '" + js.getJumpshipName() + "' has no current system. Needs to be checked!";
						logger.info(m);
						Tools.sendMailToAdminGroup(m);
					}
					if (Nexus.getCurrentChar().getJumpshipId().intValue() == js.getJumpshipId()) {
						// This is my own personal unit, move map to this ship
						currentlyCenteredJumpship = js;
						mapCenteredOnJumpship = true;
					}
				}

				if (mapCenteredOnJumpship) {
					if (currentlyCenteredJumpship != null) {
						moveMapToJumpship(currentlyCenteredJumpship);
					}
				} else {
					if (Nexus.getHomeworld() != null) {
						moveMapToPosition(Nexus.getHomeworld());
					}
				}

				starMapPane.getChildren().add(canvas);

				Rectangle clip = new Rectangle(Config.CLIP_X, Config.CLIP_Y);
				clip.setLayoutX(0);
				clip.setLayoutY(0);
				starMapPane.setClip(clip);
				starMapPane.setPickOnBounds(false);

				sceneGestures = new SceneGestures(canvas);
				addMouseFilters();

				canvas.setPaneSystemDetail(paneSystemDetail);

				buttonBackground.toFront();
				mapButton01.toFront();
				mapButton02.toFront();
				mapButton03.toFront();
				mapButton04.toFront();
				mapButton05.toFront();
				mapButton06.toFront();
				paneSystemDetail.toFront();
				paneSystemDetail.setOpacity(0.0f);
				paneAttackDetail.toFront();
				paneAttackDetail.setOpacity(0.0f);
				paneJumpshipDetail.toFront();
				paneJumpshipDetail.setOpacity(0.0f);
				canvas.getChildren().addAll(lines);

				Circle circleTukayyidLine = new Circle(Nexus.getTerra().getScreenX(), Nexus.getTerra().getScreenY(), 1052.7d);
				circleTukayyidLine.setVisible(true);
				circleTukayyidLine.setOpacity(0.22f);
				circleTukayyidLine.getStrokeDashArray().setAll(50d, 20d, 5d, 20d);
				circleTukayyidLine.setStrokeWidth(7);
				circleTukayyidLine.setStroke(Color.YELLOW);
				circleTukayyidLine.setFill(Color.TRANSPARENT);
				circleTukayyidLine.toBack();
				circleTukayyidLine.setMouseTransparent(true);
				canvas.getChildren().add(circleTukayyidLine);

				for (Line l : lines) {
					l.toBack();
				}
			} catch (Exception e) {
				logger.error("Error while init universe", e);
			}
			setStrings();
			logger.info("Finished to build the starmap.");

//			if (boUniverse.currentRoundPhase == 1) {
//				roundPhaseLabel.setText(Internationalization.getString("app_map_phase_movement"));
//				mapButton01.setTooltip(new Tooltip(Internationalization.getString("app_map_phase_movement"))); // confirm
//				mapButton01.setDisable(false);
//				mapButton06.setTooltip(new Tooltip(Internationalization.getString("app_map_phase_movement"))); // Attack / join battle
//				mapButton06.setDisable(true);
//			} else {
//				roundPhaseLabel.setText(Internationalization.getString("app_map_phase_combat"));
//				mapButton01.setTooltip(new Tooltip(Internationalization.getString("app_map_phase_combat"))); // confirm
//				mapButton01.setDisable(true);
//				mapButton06.setTooltip(new Tooltip(Internationalization.getString("app_map_phase_combat"))); // Attack / join battle
//				mapButton06.setDisable(false);
//			}

			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("12");
			ActionManager.getAction(ACTIONS.MAP_CREATION_FINISHED).execute();
		}
	}

	private void addMouseFilters() {
		if (sceneGestures != null) {
			starMapPane.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
			starMapPane.addEventFilter(MouseEvent.MOUSE_RELEASED, sceneGestures.getOnMouseReleasedEventHandler());
			starMapPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
			starMapPane.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
			starMapPane.addEventFilter(MouseEvent.MOUSE_MOVED, sceneGestures.getOnMouseMovedEventHandler());
			starMapPane.addEventFilter(MouseEvent.MOUSE_CLICKED, sceneGestures.getOnMouseClickedEventHandler());
		}
	}

	private void removeMouseFilters() {
		if (sceneGestures != null) {
			starMapPane.removeEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
			starMapPane.removeEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
			starMapPane.removeEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
			starMapPane.removeEventFilter(MouseEvent.MOUSE_MOVED, sceneGestures.getOnMouseMovedEventHandler());
			starMapPane.removeEventFilter(MouseEvent.MOUSE_CLICKED, sceneGestures.getOnMouseClickedEventHandler());
		}
	}

	private void buildGuiEffect() {
		// Fade in transition 01 (Background)
		FadeTransition fadeInTransition_01 = new FadeTransition(Duration.millis(80), starMapPane);
		fadeInTransition_01.setFromValue(0.0);
		fadeInTransition_01.setToValue(1.0);
		fadeInTransition_01.setCycleCount(3);

		//		// Fade in transition 01a (Background)
		//		FadeTransition fadeInTransition_01a = new FadeTransition(Duration.millis(500), starMapPane);
		//		fadeInTransition_01a.setFromValue(0.0);
		//		fadeInTransition_01a.setToValue(1.0);
		//		fadeInTransition_01a.setCycleCount(1);

		// Fade in transition 01b (Background)
		FadeTransition fadeInTransition_01b = new FadeTransition(Duration.millis(50), buttonBackground);
		fadeInTransition_01b.setFromValue(0.0);
		fadeInTransition_01b.setToValue(1.0);
		fadeInTransition_01b.setCycleCount(3);

		// Fade in transition 02 (Button)
		FadeTransition fadeInTransition_02 = new FadeTransition(Duration.millis(40), mapButton01);
		fadeInTransition_02.setFromValue(0.0);
		fadeInTransition_02.setToValue(1.0);
		fadeInTransition_02.setCycleCount(3);

		// Fade in transition 03 (Button)
		FadeTransition fadeInTransition_03 = new FadeTransition(Duration.millis(40), mapButton02);
		fadeInTransition_03.setFromValue(0.0);
		fadeInTransition_03.setToValue(1.0);
		fadeInTransition_03.setCycleCount(3);

		// Fade in transition 04 (Button)
		FadeTransition fadeInTransition_04 = new FadeTransition(Duration.millis(40), mapButton03);
		fadeInTransition_04.setFromValue(0.0);
		fadeInTransition_04.setToValue(1.0);
		fadeInTransition_04.setCycleCount(3);

		// Fade in transition 05 (Button)
		FadeTransition fadeInTransition_05 = new FadeTransition(Duration.millis(40), mapButton06);
		fadeInTransition_05.setFromValue(0.0);
		fadeInTransition_05.setToValue(1.0);
		fadeInTransition_05.setCycleCount(3);

		//		// Fade in transition 05 (DetailPane)
		//		FadeTransition fadeInTransition_05 = new FadeTransition(Duration.millis(60), paneSystemDetail);
		//		fadeInTransition_05.setFromValue(0.0);
		//		fadeInTransition_05.setToValue(1.0);
		//		fadeInTransition_05.setCycleCount(4);
		//
		//		// Fade in transition 06 (DetailPane)
		//		FadeTransition fadeInTransition_06 = new FadeTransition(Duration.millis(470), paneSystemDetail);
		//		fadeInTransition_06.setFromValue(1.0);
		//		fadeInTransition_06.setToValue(0.0);
		//		fadeInTransition_06.setCycleCount(1);

		// Transition sequence
		SequentialTransition sequentialTransition = new SequentialTransition();
		sequentialTransition.getChildren().addAll(      fadeInTransition_01,
														// fadeInTransition_01a,
														fadeInTransition_01b,
														fadeInTransition_02,
														fadeInTransition_03,
														fadeInTransition_04,
														fadeInTransition_05
														// fadeInTransition_06
		);
		sequentialTransition.setCycleCount(1);
		sequentialTransition.setOnFinished(event -> {
			if (!firstCreationDone) {
				// moveMapToPosition(Nexus.getCurrentlySelectedStarSystem());
				firstCreationDone = true;
			}
		});
		sequentialTransition.play();
		mapButton06.setVisible(false);
		C3SoundPlayer.play("sound/fx/cursor_click_11.mp3", false);
	}

	@SuppressWarnings("unused")
	private void reCenterMap() {
		removeMouseFilters();
		mapButton01.setDisable(true);
		mapButton02.setDisable(true);
		mapButton03.setDisable(true);
		mapButton04.setDisable(true);
		mapButton05.setDisable(true);
		mapButton06.setDisable(true);
		mapButton06.setVisible(false);

		logger.info("Travel to Homeworld");
		logger.info("X: " + Config.MAP_INITIAL_TRANSLATE_X);
		logger.info("Y: " + Config.MAP_INITIAL_TRANSLATE_Y);

		for (int[] layer : Config.BACKGROUND_STARS_LAYERS) {
			int level = layer[0];
			canvas.fadeoutStars(level);
		}

		TranslateTransition move = new TranslateTransition(Duration.millis(10), canvas);
		move.setCycleCount(1);
		move.setToX(Config.MAP_INITIAL_TRANSLATE_X);
		move.setToY(Config.MAP_INITIAL_TRANSLATE_Y);
		move.setOnFinished(event -> {
			addMouseFilters();
			boolean mayMoveJumpships = Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.FACTIONLEAD_MOVE_JUMPSHIP);
			if (mayMoveJumpships) {
				mapButton01.setDisable(false);
				checkForOrdersToSend();
			}
			mapButton02.setDisable(false);
			mapButton03.setDisable(false);
			mapButton04.setDisable(false);
			mapButton05.setDisable(false);
			mapButton06.setDisable(false);
			for (int[] layer : Config.BACKGROUND_STARS_LAYERS) {
				int level = layer[0];
				canvas.resetBackgroundStarPane(level);
				canvas.showStarSystemMarker(Nexus.getTerra());
				Nexus.setCurrentlySelectedStarSystem(Nexus.getTerra());
				ActionManager.getAction(ACTIONS.SHOW_SYSTEM_DETAIL).execute(Nexus.getTerra());
			}
		});
		move.play();
	}

	private void moveMapToPosition(BOStarSystem sys) {
		removeMouseFilters();

		mapButton01.setDisable(true);
		mapButton02.setDisable(true);
		mapButton03.setDisable(true);
		mapButton04.setDisable(true);
		mapButton05.setDisable(true);
		mapButton06.setDisable(true);

		logger.info("Travel to " + sys.getName());
		logger.info("X: " + sys.getX());
		logger.info("Y: " + sys.getY());

		for (int[] layer : Config.BACKGROUND_STARS_LAYERS) {
			int level = layer[0];
			canvas.fadeoutStars(level);
		}

		TranslateTransition move01 = new TranslateTransition(Duration.millis(0), canvas);
		move01.setCycleCount(1);
		move01.setToX(Config.MAP_INITIAL_TRANSLATE_X);
		move01.setToY(Config.MAP_INITIAL_TRANSLATE_Y);

		TranslateTransition move02 = new TranslateTransition(Duration.millis(10), canvas);
		move02.setCycleCount(1);
		move02.setByX(-sys.getX() * Config.MAP_COORDINATES_MULTIPLICATOR * canvas.getScale());
		move02.setByY(sys.getY() * Config.MAP_COORDINATES_MULTIPLICATOR * canvas.getScale());

		SequentialTransition seq = new SequentialTransition();
		seq.getChildren().addAll(move01, move02);
		seq.setOnFinished(event -> {
			addMouseFilters();
			boolean mayMoveJumpships = Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.FACTIONLEAD_MOVE_JUMPSHIP);
			if (mayMoveJumpships) {
				mapButton01.setDisable(false);
				checkForOrdersToSend();
			}
			mapButton02.setDisable(false);
			mapButton03.setDisable(false);
			mapButton04.setDisable(false);
			mapButton05.setDisable(false);
			mapButton06.setDisable(false);
			for (int[] layer : Config.BACKGROUND_STARS_LAYERS) {
				int level = layer[0];
				canvas.resetBackgroundStarPane(level);
				canvas.showStarSystemMarker(sys);
				Nexus.setCurrentlySelectedStarSystem(sys);
				ActionManager.getAction(ACTIONS.SHOW_SYSTEM_DETAIL).execute(sys);

//				Platform.runLater(() -> {
//					for (double i = 0.2d; i < 3.0d; i = i + 0.001d) {
//						canvas.setScale(i);
//						canvas.setPivot(1, 1);
//					}
//				});
			}
		});
		seq.play();
	}

	private void moveMapToJumpship(BOJumpship jumpship) {
		BOStarSystem starsystem = jumpship.getCurrentSystem(jumpship.getCurrentSystemID());
		logger.info("Travel to position of jumpship " + jumpship.getJumpshipName() + " --> " + starsystem.getName());
		moveMapToPosition(starsystem);
		jumpship.getJumpshipLevelLabel().toFront();
		jumpship.getJumpshipImageView().toFront();
	}

	private void centerStarSystemGroups() {
		// this needs to be done after the stage is actually visible (stage.show)
		// in order for the stackpane to have an actual size.
		// Otherwise StarSystemGroups appear off from their real coordinates.
		// Moved slightly to right and to the bottom
		for (BOStarSystem ss : boUniverse.starSystemBOs.values()) {
			StackPane sp = ss.getStarSystemStackPane();
			Group g = ss.getStarSystemGroup();
			g.setLayoutX(-sp.getWidth() / 2);
			g.setLayoutY(-sp.getHeight() / 2);
		}
	}

	public void hideSystemDetail() {
		if (paneSystemDetail != null) {
			if (paneSystemDetail.getOpacity() != 0.0) {
				// Fade in transition 06 (DetailPane)
				FadeTransition fadeInTransition_06 = new FadeTransition(Duration.millis(650), paneSystemDetail);
				fadeInTransition_06.setFromValue(1.0);
				fadeInTransition_06.setToValue(0.0);
				fadeInTransition_06.setCycleCount(1);

				// Transition sequence
				SequentialTransition sequentialTransition = new SequentialTransition();
				sequentialTransition.getChildren().addAll(fadeInTransition_06);
				sequentialTransition.setCycleCount(1);
				sequentialTransition.play();
			}
		}
	}

	public void hideAttackDetail() {
		currentlyDisplayedAttackDetailsId = null;

		if (paneAttackDetail != null) {
			if (paneAttackDetail.getOpacity() != 0.0) {

				// Fade in transition 06 (DetailPane)
				FadeTransition fadeInTransition_06 = new FadeTransition(Duration.millis(650), paneAttackDetail);
				fadeInTransition_06.setFromValue(1.0);
				fadeInTransition_06.setToValue(0.0);
				fadeInTransition_06.setCycleCount(1);

				// Transition sequence
				SequentialTransition sequentialTransition = new SequentialTransition();
				sequentialTransition.getChildren().addAll(fadeInTransition_06);
				sequentialTransition.setCycleCount(1);
				sequentialTransition.play();
			}
		}
	}

	public void hideJumpshipDetail() {
		if (paneJumpshipDetail != null) {
			if (paneJumpshipDetail.getOpacity() != 0.0) {
				// Fade in transition 06 (DetailPane)
				FadeTransition fadeInTransition_06 = new FadeTransition(Duration.millis(650), paneJumpshipDetail);
				fadeInTransition_06.setFromValue(1.0);
				fadeInTransition_06.setToValue(0.0);
				fadeInTransition_06.setCycleCount(1);

				paneJumpshipDetail.setMouseTransparent(true);

				// Transition sequence
				SequentialTransition sequentialTransition = new SequentialTransition();
				sequentialTransition.getChildren().addAll(fadeInTransition_06);
				sequentialTransition.setCycleCount(1);
				sequentialTransition.play();
			}
		}
	}

	@FXML
	public void handleSurfaceMapButtonClick() {
		logger.info("clicked on surface map");
	}

	public void showSystemDetail(BOStarSystem sys) {
		if (paneSystemDetail != null) {
			// Set system information
			Nexus.setSelectedStarSystem(sys);
			C3SoundPlayer.play("sound/fx/beep_electric.mp3", false);

			Platform.runLater(() -> {
				//				String name = boUniverse.factionBOs.get(sys.getAffiliation()).getName();
				//				String color = boUniverse.factionBOs.get(sys.getAffiliation()).getColor();
				//				String shortName = boUniverse.factionBOs.get(sys.getAffiliation()).getShortName();
				String logo = boUniverse.factionBOs.get(sys.getAffiliation()).getLogo();
				Image imageFaction = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/factions/" + logo)));

				labelSystemImage.setImage(sys.getSystemImage());
				labelSystemName.setText(sys.getName());
				labelFactionImage.setImage(imageFaction);
				Double x = sys.getX();
				Double y = sys.getY();
				ActionManager.getAction(ACTIONS.UPDATE_COORD_INFO).execute(sys.getName() + " [X:" + String.format("%.2f", x) + "] - [Y:" + String.format("%.2f", y) + "]");

				if (sys.systemHasSurfaceMap) {
					ivSurfaceMap.setVisible(true);
				} else {
					ivSurfaceMap.setVisible(false);
				}

				// Fade in transition 06 (DetailPane)
				FadeTransition fadeInTransition_06 = new FadeTransition(Duration.millis(200), paneSystemDetail);
				fadeInTransition_06.setFromValue(0.0);
				fadeInTransition_06.setToValue(1.0);
				fadeInTransition_06.setCycleCount(1);

				// Transition sequence
				SequentialTransition sequentialTransition = new SequentialTransition();
				sequentialTransition.getChildren().addAll(fadeInTransition_06);
				sequentialTransition.setCycleCount(1);
				sequentialTransition.play();
			});
		}
	}

	public void showJumpshipDetail(BOJumpship ship) {
		if (paneJumpshipDetail != null) {
			// Set jumpship information
			Nexus.setCurrentlySelectedJumpship(ship);
			C3SoundPlayer.play("sound/fx/beep_electric.mp3", false);

			Platform.runLater(() -> {
				Long factionId = ship.getJumpshipFaction();
				String jumpshipName = ship.getJumpshipName();
				String logo = boUniverse.getFactionByID(factionId).getLogo();
				//				String factionName = boUniverse.getFactionByID(factionId).getName();
				//				String factionShortName = boUniverse.getFactionByID(factionId).getShortName();
				//				String color = boUniverse.getFactionByID(factionId).getColor();

				Image imageFaction = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/factions/" + logo)));
				labelJumpshipName.setText(jumpshipName);
				labelJumpshipImage.setImage(ship.getJumpshipImageView().getImage());
				labelJumpshipFactionImage.setImage(imageFaction);

				// Fade in transition 06 (DetailPane)
				FadeTransition fadeInTransition_06 = new FadeTransition(Duration.millis(200), paneJumpshipDetail);
				fadeInTransition_06.setFromValue(0.0);
				fadeInTransition_06.setToValue(1.0);
				fadeInTransition_06.setCycleCount(1);

				paneJumpshipDetail.setMouseTransparent(false);

				// Transition sequence
				SequentialTransition sequentialTransition = new SequentialTransition();
				sequentialTransition.getChildren().addAll(fadeInTransition_06);
				sequentialTransition.setCycleCount(1);
				sequentialTransition.play();
			});
		}
	}

	public void updateAttackDetail(BOAttack attack) {
		updateAttackDetail(attack, "");
	}

	public void updateAttackDetail(BOAttack attack, String eventDescription) {
		if (Nexus.getCurrentRound() == attack.getRound()) {
			if (currentlyDisplayedAttackDetailsId != null && currentlyDisplayedAttackDetailsId.equals(attack.getAttackDTO().getId())) {
				boolean fightsStarted = attack.attackFightsHaveBeenStarted();
				BOFaction factionAttacker = Nexus.getBoUniverse().getFactionByID(attack.getAttackerFactionId().longValue());
				BOFaction factionDefender = Nexus.getBoUniverse().getFactionByID(attack.getDefenderFactionId().longValue());

				String attackerlogo = factionAttacker.getLogo();
				String defenderlogo = factionDefender.getLogo();
				Image imageAttackerLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/factions/" + attackerlogo)));
				Image imageDefenderLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/factions/" + defenderlogo)));
				ivFactionImageAttacker.setImage(imageAttackerLogo);
				ivFactionImageDefender.setImage(imageDefenderLogo);

				ArrayList<Circle> scoreCircles = new ArrayList<>();
				scoreCircles.add(circleScore1);
				scoreCircles.add(circleScore2);
				scoreCircles.add(circleScore3);
				scoreCircles.add(circleScore4);
				scoreCircles.add(circleScore5);

				lblAttackHeadline.setText(Internationalization.getString("starmap_attackinfo_ActiveInvasion") + ": " + attack.getStarSystemName());

				boolean lobbyOpened = false;
				boolean attackBroken = false;
				int numberOfPilots = 0;
				if (attack.getAttackCharList() != null) {
					if (attack.getAttackCharList().size() > 0) {
						lobbyOpened = true;
						numberOfPilots = attack.getAttackCharList().size();

						boolean foundOnlineDropleadAttacker = false;
						boolean foundOnlineDropleadDefender = false;

						for (AttackCharacterDTO ac : attack.getAttackCharList()) {
							if (ac.getType().equals(ROLE_ATTACKER_COMMANDER)) {
								for (UserDTO u : Nexus.getCurrentlyOnlineUserList()) {
									if (Objects.equals(u.getCurrentCharacter().getId(), ac.getCharacterID())) {
										foundOnlineDropleadAttacker = true;
										break;
									}
								}
							}
							if (ac.getType().equals(ROLE_DEFENDER_COMMANDER)) {
								for (UserDTO u : Nexus.getCurrentlyOnlineUserList()) {
									if (Objects.equals(u.getCurrentCharacter().getId(), ac.getCharacterID())) {
										foundOnlineDropleadDefender = true;
										break;
									}
								}
							}
						}
						attackBroken = !foundOnlineDropleadAttacker || !foundOnlineDropleadDefender;
					}
				}

				if (fightsStarted) {
					taAttackDescription.setText(Internationalization.getString("starmap_attackinfo_FightsStarted"));
					long attackerWins = attack.getAttackDTO().getScoreAttackerVictories();
					long defenderWins = attack.getAttackDTO().getScoreDefenderVictories();

					int count = 1;
					for (Circle c : scoreCircles) {
						if (count <= attackerWins) {
							// color this circle red
							c.setStroke(Color.web("#a2270c"));
							c.setFill(Color.web("#511d14"));
						} else if (count > 5 - defenderWins) {
							// color this circle blue
							c.setStroke(Color.web("#6292a4"));
							c.setFill(Color.web("#113544"));
						} else {
							// color this circle gray
							c.setStroke(Color.web("#ffffff"));
							c.setFill(Color.web("#5d6165"));
						}
						count++;
					}

					if (attackBroken) {
						taAttackDescription.setText(Internationalization.getString("starmap_attackinfo_Broken"));
					} else {
						taAttackDescription.setText(Internationalization.getString("starmap_attackinfo_FightsStarted"));
					}
				} else {
					if (lobbyOpened) {
						taAttackDescription.setText(Internationalization.getString("starmap_attackinfo_LobbyOpen") + " (" + numberOfPilots + ")" );
					} else {
						taAttackDescription.setText(Internationalization.getString("starmap_attackinfo_WaitingForLobby"));
					}
					for (Circle c : scoreCircles) {
						// color this circle gray
						c.setStroke(Color.web("#ffffff"));
						c.setFill(Color.web("#5d6165"));
					}
				}
			}
		}
	}

	public void showAttackDetail(BOAttack attack) {
		if (paneAttackDetail != null) {
			C3SoundPlayer.play("sound/fx/beep_electric.mp3", false);

			Platform.runLater(() -> {
				currentlyDisplayedAttackDetailsId = attack.getAttackDTO().getId();
				updateAttackDetail(attack);

				// Fade in transition 06 (DetailPane)
				FadeTransition fadeInTransition_06 = new FadeTransition(Duration.millis(200), paneAttackDetail);
				fadeInTransition_06.setFromValue(0.0);
				fadeInTransition_06.setToValue(1.0);
				fadeInTransition_06.setCycleCount(1);

				// Transition sequence
				SequentialTransition sequentialTransition = new SequentialTransition();
				sequentialTransition.getChildren().addAll(fadeInTransition_06);
				sequentialTransition.setCycleCount(1);
				sequentialTransition.play();
			});
		}
	}

	/**
	 * Sets strings for the current gui panel. This is done when the user switches languages.
	 */
	@Override
	public void setStrings() {
		Platform.runLater(() -> {
			mapButton01.setText(Internationalization.getString("starmap_confirm_orders"));
			mapButton02.setText(Internationalization.getString("starmap_center_selected_jumpship"));
			mapButton03.setText(Internationalization.getString("starmap_center_homeworld"));
			mapButton04.setText(Internationalization.getString("starmap_previous_jumpship"));
			mapButton05.setText(Internationalization.getString("starmap_next_jumpship"));
			mapButton06.setText("..."); // depends on the planet that is selected
		});
	}

	@Override
	public void setFocus() {
		Platform.runLater(() -> {
			//
		});
	}

	/**
	 * If a warning is active, this is what happens if the warning is on.
	 */
	@Override
	public void warningOnAction() {
		//
	}

	/**
	 * If a warning is active, this is what happens if the warning is off.
	 */
	@Override
	public void warningOffAction() {
		//
	}

//	public void removeActionCallBackListeners() {
//		ActionManager.removeActionCallbackListener(this);
//	}

	/**
	 * Adds action callback listeners.
	 */
	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_BEGINS, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_DESTROY_CURRENT, this);
		ActionManager.addActionCallbackListener(ACTIONS.NEW_UNIVERSE_RECEIVED, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGON_FINISHED_SUCCESSFULL, this);
		ActionManager.addActionCallbackListener(ACTIONS.SHOW_SYSTEM_DETAIL, this);
		ActionManager.addActionCallbackListener(ACTIONS.HIDE_SYSTEM_DETAIL, this);
		ActionManager.addActionCallbackListener(ACTIONS.UPDATE_COORD_INFO, this);
		ActionManager.addActionCallbackListener(ACTIONS.MAP_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.SHOW_JUMPSHIP_DETAIL, this);
		ActionManager.addActionCallbackListener(ACTIONS.HIDE_JUMPSHIP_DETAIL, this);
		ActionManager.addActionCallbackListener(ACTIONS.TERMINAL_COMMAND, this);
		ActionManager.addActionCallbackListener(ACTIONS.SYSTEM_WAS_SELECTED, this);
		ActionManager.addActionCallbackListener(ACTIONS.REPAINT_MAP, this);
		ActionManager.addActionCallbackListener(ACTIONS.FINALIZE_ROUND, this);
		ActionManager.addActionCallbackListener(ACTIONS.SHOW_FORBIDDEN_ICON_MAP, this);
		ActionManager.addActionCallbackListener(ACTIONS.UPDATE_GAME_INFO, this);
		ActionManager.addActionCallbackListener(ACTIONS.UPDATE_ROUND_COUNTDOWN, this);
		ActionManager.addActionCallbackListener(ACTIONS.ENABLE_JUMP_BUTTON, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURRENT_ATTACK_IS_HEALED, this);
		ActionManager.addActionCallbackListener(ACTIONS.WATCHED_ATTACK_IS_BROKEN, this);
		ActionManager.addActionCallbackListener(ACTIONS.WATCHED_ATTACK_IS_BROKEN_WARNING, this);
		ActionManager.addActionCallbackListener(ACTIONS.WATCHED_ATTACK_IS_BROKEN_KILLED, this);
		ActionManager.addActionCallbackListener(ACTIONS.WATCHED_ATTACK_IS_HEALED, this);
		ActionManager.addActionCallbackListener(ACTIONS.UPDATE_ALLIANCES_LIST, this);
	}

	/**
	 * Initializes the pane controller.
	 *
	 * @param url the url.
	 * @param rb  the resource bundle containing the language strings.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
	}

	/**
	 * Handles actions.
	 *
	 * @param action incoming action to be handled
	 * @param o      the action object passed along with the action
	 * @return wether the handling should continue (this should be true in general)
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		String eventDescription = "";

		switch (action) {
			case UPDATE_UNIVERSE:
				break;

			case REPAINT_MAP:
				if (Nexus.isLoggedIn()) {
					logger.info("Map will be repainted.");
					refreshUniverseMap();
					for (BOAttack a : Nexus.getBoUniverse().attackBOsAllInThisRound.values()) {
						if (a.getAttackDTO().getId().equals(currentlyDisplayedAttackDetailsId)) {
							updateAttackDetail(a);
							break;
						}
					}

				} else {
					logger.info("Map not repainting, no user logged in!");
				}
				break;

			case NEW_UNIVERSE_RECEIVED:
				if (Nexus.isLoggedIn()) {
					logger.info("Received new universe, repainting map.");
					refreshUniverseMap();
					for (BOAttack a : Nexus.getBoUniverse().attackBOsAllInThisRound.values()) {
						if (a.getAttackDTO().getId().equals(currentlyDisplayedAttackDetailsId)) {
							updateAttackDetail(a);
							break;
						}
					}

					ActionManager.getAction(ACTIONS.UPDATE_GAME_INFO).execute();
				} else {
					logger.info("Map not repainting, no user logged in!");
				}
				break;

			case WATCHED_ATTACK_IS_BROKEN:
				eventDescription = "1";
			case WATCHED_ATTACK_IS_BROKEN_WARNING:
				eventDescription = "2";
			case WATCHED_ATTACK_IS_BROKEN_KILLED:
				eventDescription = "3";
			case WATCHED_ATTACK_IS_HEALED:
				eventDescription = "4";

			case CURRENT_ATTACK_IS_BROKEN:
				eventDescription = "5";
			case CURRENT_ATTACK_IS_BROKEN_WARNING:
				eventDescription = "6";
			case CURRENT_ATTACK_IS_BROKEN_KILLED:
				eventDescription = "7";
			case CURRENT_ATTACK_IS_HEALED:
				eventDescription = "8";

				if (Nexus.isLoggedIn()) {
					for (BOAttack a : Nexus.getBoUniverse().attackBOsAllInThisRound.values()) {
						if (a.getAttackDTO().getId().equals(currentlyDisplayedAttackDetailsId)) {
							updateAttackDetail(a);
							break;
						}
					}
				}
				break;

			case FINALIZE_ROUND:
				logger.info("Client-MappaneController: Server did finalize round.");
				logger.info("Current round (after finalizing): " + Nexus.getBoUniverse().currentRound);
				ActionManager.getAction(ACTIONS.UPDATE_GAME_INFO).execute();
				break;

			case CHANGE_LANGUAGE:
				setStrings();
				break;

			case PANE_DESTROY_CURRENT:
			case PANE_CREATION_BEGINS:
				if (o.getObject() instanceof AbstractC3Pane) {
					Platform.runLater(() -> {
						starMapPane.setOpacity(0.0f);
						buttonBackground.setOpacity(0.0f);
						mapButton01.setOpacity(0.0f);
						mapButton02.setOpacity(0.0f);
						mapButton03.setOpacity(0.0f);
						//				mapButton04.setOpacity(0.0f);
						//				mapButton05.setOpacity(0.0f);
						mapButton06.setOpacity(0.0f);
						paneSystemDetail.setOpacity(0.0f);
						paneJumpshipDetail.setOpacity(0.0f);
						paneAttackDetail.setOpacity(0.0f);
					});
				}
				break;

			case MAP_CREATION_FINISHED:
				Platform.runLater(() -> {
					centerStarSystemGroups();

					double w = Config.MAP_WIDTH;
					double h = Config.MAP_HEIGHT;

					canvas.show3DStars(false);
					canvas.hideElementsForScreenshot(true);
					if (!Nexus.isDevelopmentPC() && C3Properties.getBoolean(C3PROPS.GENERALS_SCREENSHOT_HISTORY)) {
						Tools.saveMapScreenshot((int) w, (int) h / 2 + 200, canvas);
						logger.info("Saved history screenshot of the starmap.");
					} else {
						logger.info("Saved history screenshot has been disabled.");
					}
					canvas.hideElementsForScreenshot(false);
					canvas.setVisibility();
					canvas.show3DStars(true);

					buildGuiEffect();
				});
				logger.info("Map is ready!");
				break;

			case PANE_CREATION_FINISHED:
				if (o.getObject() instanceof AbstractC3Pane p) {
					if ("MapPane".equals(p.getPaneName())) {
						if (!firstCreationDone) {
							if (!universeMapGenerationStarted) {
								universeMapGenerationStarted = true;
								ActionManager.getAction(ACTIONS.NOISE).execute(1100);
								Platform.runLater(this::initializeUniverseMap);
							}
							ActionManager.getAction(ACTIONS.UPDATE_GAME_INFO).execute();
						} else {
							Platform.runLater(this::buildGuiEffect);
						}
					}
				}
				break;

			case LOGON_FINISHED_SUCCESSFULL:
				break;

			case SHOW_SYSTEM_DETAIL:
				if (o.getObject() instanceof BOStarSystem ss) {
					if (!ss.isAttackedThisRound()) {
						showSystemDetail(ss);
						hideAttackDetail();
					}
				} else {
					hideSystemDetail();
					hideAttackDetail();
				}
				break;

			case UPDATE_ALLIANCES_LIST:
				if (o.getText() != null) {
					Platform.runLater(() -> {
						taAlliances.setText(o.getText());
						taAlliances.toFront();
					});
				}
				break;

			case SYSTEM_WAS_SELECTED:
				if (o.getObject() instanceof BOStarSystem ss) {

					boolean hasAttack = false;
					boolean attackAlreadyStarted;
					boolean startAttackEnabled = false;

					BOAttack attackOfSelectedSystem = null;

					// Check if the currently logged on char participates in any other attack
					// if yes, he cannot join this one

					if (!BOAttack.charHasAnActiveAttack()) {
						for (BOAttack a : Nexus.getBoUniverse().attackBOsOpenInThisRound.values()) {
							//attackAlreadyStarted = a.getStoryId() != null;
							attackAlreadyStarted = a.attackLobbyHasBeenStarted();

							// Correct season and round
							if (Nexus.getCurrentSeason() == a.getSeason() && Nexus.getCurrentRound() == a.getRound()) {
								// Clicked star system has an attack going on
								if (ss.getStarSystemId().equals(a.getStarSystemId())) {
									hasAttack = true;
									attackOfSelectedSystem = a;

									mapButton06.getStyleClass().remove("contentButton");
									mapButton06.getStyleClass().remove("contentButtonRed");
									mapButton06.getStyleClass().remove("contentButtonBlue");
									mapButton06.getStyleClass().remove("contentButtonYellow");

									if (Nexus.getCurrentUser().getCurrentCharacter().getFactionId().equals(a.getAttackerFactionId())) {
										// I am from the attacker faction
										logger.info("I am the attacker.");
										mapButton06.getStyleClass().add("contentButtonRed");
										mapButton06.setText(Internationalization.getString("starmap_attack_system"));

										if (!attackAlreadyStarted) {
											// The attack has not been started yet, I am from the attacking faction, so I can start it now
											startAttackEnabled = true;
											currentPlayerRoleInInvasion = Constants.ROLE_ATTACKER_COMMANDER; // Attacker commander
											ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("attack_youMayStartTheAttack"), true));
										} else {
											// Another warrior of my faction has started the attack, I am joining the attack
											startAttackEnabled = true;
											currentPlayerRoleInInvasion = Constants.ROLE_ATTACKER_WARRIOR; // Attacker warrior
											ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("attack_youMayJoinTheAttack"), true));
										}
									} else if (Nexus.getCurrentUser().getCurrentCharacter().getFactionId().equals(a.getDefenderFactionId())) {
										// I am from the defender faction
										logger.info("I am the defender.");
										mapButton06.getStyleClass().add("contentButtonBlue");
										mapButton06.setText(Internationalization.getString("starmap_defend_system"));

										if (!attackAlreadyStarted) {
											// I cannot join the defenders, the attackers did not attack yet
											ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("attack_attackersDidNotAttackYet"), false));
										} else {
											// I can join the defenders
											startAttackEnabled = true;
											currentPlayerRoleInInvasion = Constants.ROLE_DEFENDER_WARRIOR; // Defender (First will become commander --> 3L)
											ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("attack_youMayJoinTheDefenders"), true));
										}
									} else {
										// I am someone else
										logger.info("I want to join the fight.");
										mapButton06.getStyleClass().add("contentButtonYellow");
										mapButton06.setText(Internationalization.getString("starmap_join_fight"));

										if (!attackAlreadyStarted) {
											// I cannot join the attack, the attackers did not attack yet
											ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("attack_attackersDidNotAttackYet"), false));
										} else {
											// I can join the attack
											startAttackEnabled = true;
											currentPlayerRoleInInvasion = Constants.ROLE_SUPPORTER; // Supporter (Will be placed in team with less pilots or random // cannot be commander)
											ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("attack_youMayJoinTheAttack"), true));
										}
									}
									if (startAttackEnabled) {
										mapButton06.setDisable(false);
										mapButton06.setVisible(true);

										FadeTransition fadeInTransition = new FadeTransition(Duration.millis(850), mapButton06);
										fadeInTransition.setFromValue(0.2);
										fadeInTransition.setToValue(1.0);
										FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(850), mapButton06);
										fadeOutTransition.setFromValue(1.0);
										fadeOutTransition.setToValue(0.2);
										SequentialTransition sequentialTransition = new SequentialTransition();
										sequentialTransition.getChildren().addAll(fadeInTransition, fadeOutTransition);
										sequentialTransition.setCycleCount(Animation.INDEFINITE);
										sequentialTransition.play();
									}
									if (a.getAttackDTO().getFightsStarted()) {
										// Drops for this fight have started already, joining now is not possible.
										// Too late.
										logger.info("Fights have started already. You can not get there in time!");
										mapButton06.setDisable(true);
										ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("attack_fightsHaveStartedAlready"), true));
									}
								}
							}
						}
						if (!hasAttack) {
							// disable attack button
							mapButton06.setDisable(true);
							mapButton06.setVisible(false);
							ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("attack_planetHasNoAttack"), false));
						} else {
							if (attackOfSelectedSystem != null) {
								showAttackDetail(attackOfSelectedSystem);
								hideJumpshipDetail();
								hideSystemDetail();
							}
						}
					} else {
						// disable attack button
						mapButton06.setDisable(true);
						mapButton06.setVisible(false);
						ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("attack_youAlreadyHaveAFight"), true));
					}
				}
				break;

			case HIDE_SYSTEM_DETAIL:
				hideSystemDetail();
				hideAttackDetail();
				break;

			case SHOW_JUMPSHIP_DETAIL:
				logger.info("Showing jumpship detail");
				if (o.getObject() instanceof BOJumpship js) {
					showJumpshipDetail(js);
					hideAttackDetail();
				} else {
					hideJumpshipDetail();
				}
				break;

			case HIDE_JUMPSHIP_DETAIL:
				hideJumpshipDetail();
				break;

			case UPDATE_COORD_INFO:
				String v = o.getText();
				Platform.runLater(() -> labelMouseCoords.setText(v));
				break;

			case TERMINAL_COMMAND:
				String com = o.getText();
				if (Nexus.isLoggedIn()) {
					if (Nexus.getCurrentlyOpenedPane() instanceof MapPane) {
						if (!com.startsWith("*!!!*")) {
							handleCommand(com);
						}
					}
				}
				break;

			case SHOW_FORBIDDEN_ICON_MAP:
				Integer i = (Integer)o.getObject();
				// logger.info("Forbidden: " + b);
				Platform.runLater(() -> {
					ivForbidden.toFront();
					switch (i) {
						case 0 -> {
							ivForbidden.setImage(systemForbiddenIcon0);
							ivForbidden.setVisible(false); // hide forbidden icon
						}
						case 1 -> {
							ivForbidden.setImage(systemForbiddenIcon1);
							ivForbidden.setVisible(true); // locked because of jumpship
						}
						case 2 -> {
							ivForbidden.setImage(systemForbiddenIcon2);
							ivForbidden.setVisible(true); // locked because previous attack
						}
						case 3 -> {
							ivForbidden.setImage(systemForbiddenIcon3);
							ivForbidden.setVisible(true); // locked because insufficient level
						}
					}
				});
				break;

			case UPDATE_ROUND_COUNTDOWN:
				Platform.runLater(() -> {
					String s = "";
					if (o.getText() != null && !"".equals(o.getText())) {
						s = o.getText();
					}
					roundPhaseLabel.setText(s);
				});
				break;

			case UPDATE_GAME_INFO:
//				if (boUniverse != null) {
//					if (boUniverse.currentRoundPhase == 1) {
//						roundPhaseLabel.setText(Internationalization.getString("app_map_phase_movement"));
//						mapButton01.setTooltip(new Tooltip(Internationalization.getString("app_map_phase_movement"))); // confirm
//						mapButton01.setDisable(false);
//						mapButton06.setTooltip(new Tooltip(Internationalization.getString("app_map_phase_movement"))); // Attack / join battle
//						mapButton06.setDisable(true);
//					} else {
//						roundPhaseLabel.setText(Internationalization.getString("app_map_phase_combat"));
//						mapButton01.setTooltip(new Tooltip(Internationalization.getString("app_map_phase_combat"))); // confirm
//						mapButton01.setDisable(true);
//						mapButton06.setTooltip(new Tooltip(Internationalization.getString("app_map_phase_combat"))); // Attack / join battle
//						mapButton06.setDisable(false);
//					}
//				}
				break;

			case ENABLE_JUMP_BUTTON:
				checkForOrdersToSend();
				break;

			default:
				break;
		}
		return true;
	}

	public void handleCommand(String com) {
		if (!com.startsWith("*!!!*")) {
			if (!"".equals(com)) {
				logger.info("Received command: '" + com + "'");
				String lastEntry = null;
				if (Nexus.commandHistory.size() > 0) {
					lastEntry = Nexus.commandHistory.getLast();
				}
				if (lastEntry == null) {
					Nexus.commandHistory.add(com);
				} else if (!Nexus.commandHistory.getLast().equals(com)) {
					Nexus.commandHistory.add(com);
				}
				if (Nexus.commandHistory.size() > 50) {
					Nexus.commandHistory.remove(0);
				}
				Nexus.commandHistoryIndex = Nexus.commandHistory.size();
			}
		}

		if ("*!!!*historyBack".equals(com)) {
			if (Nexus.commandHistoryIndex > 0) {
				Nexus.commandHistoryIndex--;
				logger.info("History back to index: " + Nexus.commandHistoryIndex);
				String histCom = Nexus.commandHistory.get(Nexus.commandHistoryIndex);
				ActionManager.getAction(ACTIONS.SET_TERMINAL_TEXT).execute(histCom);
			}
		}

		if ("*!!!*historyForward".equals(com)) {
			if (Nexus.commandHistoryIndex < Nexus.commandHistory.size() - 1) {
				Nexus.commandHistoryIndex++;
				logger.info("History forward to index: " + Nexus.commandHistoryIndex);
				String histCom = Nexus.commandHistory.get(Nexus.commandHistoryIndex);
				ActionManager.getAction(ACTIONS.SET_TERMINAL_TEXT).execute(histCom);
			}
		}

		// ---------------------------------
		// find
		// ---------------------------------
		if (com.toLowerCase().startsWith("find ")) {
			String value = com.substring(5);
			if (!"".equals(value)) {
				logger.info("Searching for '" + value + "'");
			}
			logger.info("Searching starsystems...");
			for (BOStarSystem ss : boUniverse.starSystemBOs.values()) {
				if (ss.getName().equalsIgnoreCase(value)) {
					logger.info("Found starsystem '" + value + "'");
					moveMapToPosition(ss);
				}
			}
			logger.info("Searching jumpships...");
			for (BOJumpship js : boUniverse.jumpshipBOs.values()) {
				if (js.getJumpshipName().equalsIgnoreCase(value)) {
					logger.info("Found jumpship '" + value + "'");
					moveMapToJumpship(js);
				}
			}
			Nexus.storeCommandHistory();
		}

		// ---------------------------------
		// force finalize round
		// ---------------------------------
		if (com.toLowerCase().startsWith("finalize round")) {
			if (Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_FINALIZE_ROUND)) {

				// Check if there are any routepoints that have not been saved yet!
				boolean unsavedRoutesFound = false;
				for (BOJumpship js : boUniverse.getJumpshipList()) {
					if (js.getJumpshipFaction() == Nexus.getCurrentUser().getCurrentCharacter().getFactionId()) {
						// My own jumpship. May have been moved and may have an unsaved route

						if (js.routeLines != null) {
							if (js.routeLines.getChildren().size() > 0) {
								logger.info("There are routepoints to store.");
								unsavedRoutesFound = true;
							} else {
								logger.info("NO routepoints.");
							}
						}
					}
				}

				if (unsavedRoutesFound) {
					// stop action here, save routes first!
					ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_saveRoutesBeforeFinalizeRound"), true));
				} else {
					ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_success"), false));
					GameState s = new GameState();
					s.setMode(GAMESTATEMODES.FORCE_FINALIZE_ROUND);
					Nexus.fireNetworkEvent(s);
					Nexus.storeCommandHistory();
				}
			} else {
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_notallowed"), false));
				C3Message message = new C3Message(C3MESSAGES.ERROR_NOT_ALLOWED);
				message.setType(C3MESSAGETYPES.CLOSE);
				message.setText(Internationalization.getString("general_notallowed"));
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
				ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
			}
//			Nexus.storeCommandHistory();
		}

		// ---------------------------------
		// re-create universe
		// ---------------------------------
		if (com.toLowerCase().startsWith("create universe")) {
			if (Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN)) {
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_success"), false));
				GameState s = new GameState();
				s.setMode(GAMESTATEMODES.FORCE_NEW_UNIVERSE);
				Nexus.fireNetworkEvent(s);
				Nexus.storeCommandHistory();
			} else {
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_notallowed"), false));
				C3Message message = new C3Message(C3MESSAGES.ERROR_NOT_ALLOWED);
				message.setType(C3MESSAGETYPES.CLOSE);
				message.setText(Internationalization.getString("general_notallowed"));
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
				ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
			}
//			Nexus.storeCommandHistory();
		}
	}
}
