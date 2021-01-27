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
package net.clanwolf.starmap.client.gui.panes;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

/**
 *
 * @author Meldric
 */
public class PlanetRotationPane extends Pane {

	private final double widthX = 500.0f;
	private final double widthY = 500.0f;
	private final double centerX = widthX / 2;
	private final double centerY = widthY / 2;

	/**
	 * The pane with the rotating planets.
	 */
	public PlanetRotationPane() {
		this.setPrefWidth(widthX);
		this.setPrefHeight(widthY);
		this.setLayoutX(270);
		this.setLayoutY(-60);

		Group group = new Group();

		Circle sun = new Circle();
		sun.setCenterX(centerX);
		sun.setCenterY(centerY);
		sun.setStrokeWidth(2);
		sun.setStroke(Color.web("#699ecd"));
		sun.setStrokeType(StrokeType.CENTERED);
		sun.setFill(Color.web("#699ecd"));
		sun.setRadius(15.0f);

		Circle border = new Circle();
		border.setCenterX(centerX);
		border.setCenterY(centerY);
		border.setStrokeWidth(2);
		border.setStroke(Color.web("#699ecd"));
		border.setStrokeType(StrokeType.CENTERED);
		border.setFill(Color.web("#699ecd"));
		border.setRadius(220.0f);
		border.setOpacity(0.0f);

		Group planet1 = createPlanet(38, 8, Color.web("#1b465f"), Duration.seconds(10));
		Group planet2 = createPlanet(88, 5, Color.web("#1b465f"), Duration.seconds(22));
		Group planet3 = createPlanet(105, 12, Color.web("#1b465f"), Duration.seconds(34));
		Group planet4 = createPlanet(138, 7, Color.web("#1b465f"), Duration.seconds(28));

		group.getChildren().add(sun);
		group.getChildren().add(planet1);
		group.getChildren().add(planet2);
		group.getChildren().add(planet3);
		group.getChildren().add(planet4);
		group.getChildren().add(border);
		group.setVisible(true);

		// http://docs.oracle.com/javafx/2/visual_effects/perspective.htm#CACFAAEJ

		PerspectiveTransform perspectiveTransform = new PerspectiveTransform();
		perspectiveTransform.setUlx(100.0f); // Upper left X
		perspectiveTransform.setUly(150.0f); // Upper left Y
		perspectiveTransform.setUrx(400.0f); // Upper right X
		perspectiveTransform.setUry(150.0f); // Upper right Y
		perspectiveTransform.setLrx(500.0f); // Lower right X
		perspectiveTransform.setLry(300.0f); // Lower right Y
		perspectiveTransform.setLlx(0.0f); // Lower left X
		perspectiveTransform.setLly(300.0f); // Lower left Y
		group.setEffect(perspectiveTransform);
		group.getTransforms().add(new Scale(1.5, 1.9));
		group.setOpacity(0.2f);
		group.setCache(true);
		group.setCacheHint(CacheHint.QUALITY);

		this.getChildren().add(group);
	}

	private Group createPlanet(double r, double size, Color c, Duration d) {
		Group planetGroup = new Group();

		Circle planet = new Circle();
		planet.setCenterX(centerX + r);
		planet.setCenterY(centerY + r);
		planet.setStrokeWidth(1);
		planet.setStroke(Color.web("#699ecd"));
		planet.setStrokeType(StrokeType.CENTERED);
		planet.setFill(c);
		planet.setRadius(size);

		Circle orbit = new Circle();
		orbit.setCenterX(centerX);
		orbit.setCenterY(centerY);
		orbit.setStrokeWidth(2);
		orbit.getStrokeDashArray().addAll(2d, 4d);
		orbit.setStroke(Color.web("#699ecd"));
		orbit.setFill(null);
		orbit.setRadius(Math.sqrt((r * r) + (r * r)));

		Rotate rotationTransformPlanet = new Rotate(0, centerX, centerY);
		planet.getTransforms().add(rotationTransformPlanet);
		Timeline rotationAnimationPlanet = new Timeline();
		rotationAnimationPlanet.getKeyFrames().add(new KeyFrame(d, new KeyValue(rotationTransformPlanet.angleProperty(), 360)));
		rotationAnimationPlanet.setCycleCount(Animation.INDEFINITE);
		rotationAnimationPlanet.play();

		planetGroup.getChildren().add(orbit);
		planetGroup.getChildren().add(planet);

		return planetGroup;
	}
}
