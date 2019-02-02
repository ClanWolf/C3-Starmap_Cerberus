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
package net.clanwolf.starmap.client.gui.panes;

import javafx.animation.*;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * This provides a opague pane to block all mouse and keyboard operations on the client in case a long actions is going on and gui interaction needs to be blocked.
 *
 * // http://docs.oracle.com/javafx/2/canvas/jfxpub-canvas.htm
 *
 * TODO: Check for keyboard entrys!
 *
 * @author Meldric
 */
public class WaitAnimationPane extends Pane {

	private boolean animationPlaying = false;
	private final double centerX = 0.0f;
	private final double centerY = 0.0f;
	private Group group = null;
	private FadeTransition fadeInTransition = null;
	private FadeTransition fadeOutTransition = null;

	/**
	 * Wait animation pane
	 */
	public WaitAnimationPane() {

		// 1030 x 630

		this.setPrefWidth(100);
		this.setPrefHeight(100);
		this.setLayoutX(515);
		this.setLayoutY(315);

		group = new Group();

		DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetY(0.0f);
		dropShadow.setOffsetX(0.0f);
		dropShadow.setRadius(15.0f);
		dropShadow.setColor(Color.web("#e5e5e5"));

		Circle circle = new Circle();
		circle.setCenterX(centerX);
		circle.setCenterY(centerY);
		circle.setEffect(dropShadow);
		circle.setStrokeWidth(2);
		circle.setStroke(Color.web("#699ecd"));
		circle.setStrokeType(StrokeType.CENTERED);
		circle.setFill(Color.web("#172a39"));
		circle.setRadius(35.0f);

		Arc arc1 = createArc(centerX, centerY, 15.0f, 15.0f, 270.0f, 250.0f, Color.web("#354f63"), 9, Duration.seconds(10));
		Arc arc2 = createArc(centerX, centerY, 20.0f, 20.0f, 270.0f, 76.0f, Color.web("#45677f"), 8, Duration.seconds(2));
		Arc arc3 = createArc(centerX, centerY, 27.0f, 27.0f, 270.0f, 50.0f, Color.web("#1b465f"), 6, Duration.seconds(3));
		Arc arc4 = createArc(centerX, centerY, 31.0f, 31.0f, 270.0f, 10.0f, Color.web("#317fad"), 4, Duration.seconds(7));

		group.getChildren().add(circle);
		group.getChildren().add(arc1);
		group.getChildren().add(arc2);
		group.getChildren().add(arc3);
		group.getChildren().add(arc4);
		group.setOpacity(0.0);
		group.setCache(true);
		group.setCacheHint(CacheHint.SPEED);
		group.setVisible(false);

		this.getChildren().add(group);
	}

	/**
	 * Toggel circle animation
	 *
	 * @param v
	 *            boolean
	 */
	public void showCircleAnimation(boolean v) {
		if ((!animationPlaying) && (v == true)) {
			group.setOpacity(0.0);
			group.setVisible(v);
			if (fadeInTransition == null) {
				fadeInTransition = new FadeTransition(Duration.millis(100), group);
				fadeInTransition.setFromValue(0.0);
				fadeInTransition.setToValue(1.0);
				fadeInTransition.setCycleCount(1);
			}
			fadeInTransition.play();
			animationPlaying = true;
		}
		if ((animationPlaying) && (v == false)) {
			if (fadeOutTransition == null) {
				fadeOutTransition = new FadeTransition(Duration.millis(100), group);
				fadeOutTransition.setFromValue(1.0);
				fadeOutTransition.setToValue(0.1);
				fadeOutTransition.setCycleCount(1);
			}
			fadeOutTransition.play();
			group.setVisible(v);
			animationPlaying = false;
		}
	}

	/**
	 * Create arc for circular animation
	 *
	 * @param x
	 *            double
	 * @param y
	 *            double
	 * @param rx
	 *            double
	 * @param ry
	 *            double
	 * @param startAngle
	 *            double
	 * @param length
	 *            double
	 * @param c
	 *            Color
	 * @param strokeWidth
	 *            int
	 * @param d
	 *            Duration
	 * @return Arc
	 */
	public static Arc createArc(double x, double y, double rx, double ry, double startAngle, double length, Color c, int strokeWidth, Duration d) {
		Arc arc = new Arc(x, y, rx, ry, startAngle, length);
		arc.setType(ArcType.OPEN);
		arc.setStrokeWidth(strokeWidth);
		arc.setStroke(c);
		arc.setStrokeType(StrokeType.CENTERED);
		arc.setFill(null);

		final Rotate rotationTransform = new Rotate(0, x, y);
		arc.getTransforms().add(rotationTransform);

		final Timeline rotationAnimation = new Timeline();
		rotationAnimation.getKeyFrames().add(new KeyFrame(d, new KeyValue(rotationTransform.angleProperty(), 360)));
		rotationAnimation.setCycleCount(Animation.INDEFINITE);
		rotationAnimation.play();

		return arc;
	}
}
