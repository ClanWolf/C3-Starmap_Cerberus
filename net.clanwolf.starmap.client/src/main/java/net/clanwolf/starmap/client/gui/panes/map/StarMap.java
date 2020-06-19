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

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.gui.panes.map.tools.VoronoiDelaunay;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import net.clanwolf.starmap.client.process.universe.BOJumpship;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import org.kynosarges.tektosyne.geometry.PointD;

import java.util.ArrayList;

/**
 * An application with a zoomable and pannable canvas.
 */
public class StarMap extends Application {

	BOUniverse boUniverse;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		boUniverse = Nexus.getBoUniverse();

		try {
			PannableCanvas canvas = new PannableCanvas();
			canvas.setTranslateX(Config.MAP_INITIAL_TRANSLATE_X);
			canvas.setTranslateY(Config.MAP_INITIAL_TRANSLATE_Y);

			// create sample nodes which can be dragged
			NodeGestures nodeGestures = new NodeGestures(canvas);

			for (BOStarSystem starSystem : boUniverse.starSystemBOs.values()) {
				String name = starSystem.getName();
				Integer id = starSystem.getId();
				double x = starSystem.getScreenX();
				double y = starSystem.getScreenY();

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
				Circle starSystemCircle = new Circle(4);
				starSystemCircle.setId(starSystem.getId().toString());
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
				stackPane.getChildren().add(1, starSystemCircle);

				starSystemGroup.getChildren().add(stackPane);
				starSystemGroup.setTranslateX(x);
				starSystemGroup.setTranslateY(y);
				starSystemGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMouseClickedEventHandler());

				starSystem.setStarSystemStackPane(stackPane);
				starSystem.setStarSystemGroup(starSystemGroup);
				canvas.getChildren().add(starSystemGroup);
			}

			canvas.addStarPane();
			canvas.addGrid_Center();
			canvas.addGrid_500();
			canvas.addGrid_250();
			canvas.setVisibility();

			Circle circle1 = new Circle(3000, 3000, 40);
			circle1.setStroke(Color.ORANGE);
			circle1.setFill(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
			circle1.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
			circle1.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
			canvas.getChildren().add(circle1);

			Pane borders = VoronoiDelaunay.getAreas();
			canvas.getChildren().add(borders);
			borders.toBack();

			// Attacks pane
			Pane attacksPane = new Pane();
			canvas.setAttacksPane(attacksPane);

			for (BOAttack attack : boUniverse.attackBOs) {
				if (attack.getSeason().equals(boUniverse.currentSeason) && attack.getRound().equals(boUniverse.currentRound)) {
					BOStarSystem attackedSystem;
					BOStarSystem attackerStartedFromSystem;

					attackedSystem = boUniverse.starSystemBOs.get(attack.getStarSystemId());
					attackerStartedFromSystem = boUniverse.starSystemBOs.get(attack.getAttackedFromStarSystem());

					if (attackedSystem != null && attackerStartedFromSystem != null) {
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

						final double maxOffset = line.getStrokeDashArray().stream().reduce(0d, (a, b) -> a + b);

						Timeline timeline = new Timeline(
								new KeyFrame(
										Duration.ZERO,
										new KeyValue(
												line.strokeDashOffsetProperty(),
												0,
												Interpolator.LINEAR
										)
								),
								new KeyFrame(
										Duration.seconds(1),
										new KeyValue(
												line.strokeDashOffsetProperty(),
												maxOffset,
												Interpolator.LINEAR
										)
								)
						);
						timeline.setCycleCount(Timeline.INDEFINITE);
						timeline.play();
						attacksPane.getChildren().add(line);
					}
				}
			}
			canvas.getChildren().add(attacksPane);
			attacksPane.toBack();

			for (BOJumpship js : boUniverse.jumpshipBOs.values()) {
				// TODO: Jumpships
				Integer currentSystemID = js.getCurrentSystemID();
				ArrayList<Integer> hist = js.getStarSystemHistoryArray();

				if (currentSystemID != null) {
					ImageView jumpshipImage;
					if (js.isCombatReady()) {
						jumpshipImage = new ImageView(new Image("images/map/jumpship_left_blue.png"));
						jumpshipImage.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
						jumpshipImage.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
						jumpshipImage.addEventFilter(MouseEvent.DRAG_DETECTED, nodeGestures.getOnMouseDragDetectedEventHandler());
						jumpshipImage.addEventFilter(MouseEvent.MOUSE_RELEASED, nodeGestures.getOnMouseReleasedEventHandler());
					} else {
						jumpshipImage = new ImageView(new Image("images/map/jumpship_left_red.png"));
					}
					jumpshipImage.setId(js.getShipName());
					jumpshipImage.setPreserveRatio(true);
					jumpshipImage.setFitWidth(30);
					jumpshipImage.setCacheHint(CacheHint.QUALITY);
					jumpshipImage.setSmooth(false);
					jumpshipImage.setTranslateX(boUniverse.starSystemBOs.get(currentSystemID).getScreenX() - 35);
					jumpshipImage.setTranslateY(boUniverse.starSystemBOs.get(currentSystemID).getScreenY() - 8);
					jumpshipImage.setMouseTransparent(false);
					jumpshipImage.toFront();
					jumpshipImage.setVisible(false);
					canvas.getChildren().add(jumpshipImage);

					js.setJumpshipImage(jumpshipImage);
				}
			}

			String image = "images/map/background.jpg";
			String style = "";
			style = style + "-fx-background-image:url('";
			style = style + image;
			style = style + "');-fx-background-position:center center;-fx-background-repeat:repeat;";

			Pane p = new Pane();
			p.setStyle(style);
			p.getChildren().add(canvas);

			// create scene which can be dragged and zoomed
			Scene scene = new Scene(p, 1024, 768);

			SceneGestures sceneGestures = new SceneGestures(canvas);
			scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
			scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
			scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
			canvas.addEventFilter(MouseEvent.MOUSE_MOVED, sceneGestures.getOnMouseMovedEventHandler());

			stage.setScene(scene);
			stage.show();

			// do this after stage.show in order for the stackpane to have an actual size!
			for (BOStarSystem ss : boUniverse.starSystemBOs.values()) {
				StackPane sp = ss.getStarSystemStackPane();
				Group g = ss.getStarSystemGroup();
				g.setLayoutX(-sp.getWidth() / 2);
				g.setLayoutY(-sp.getHeight() / 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
