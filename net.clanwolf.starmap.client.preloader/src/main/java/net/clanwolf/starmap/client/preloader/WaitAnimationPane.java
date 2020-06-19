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
package net.clanwolf.starmap.client.preloader;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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

// http://docs.oracle.com/javafx/2/canvas/jfxpub-canvas.htm
/**
 *
 * @author Meldric
 */
public class WaitAnimationPane extends Pane {

	private boolean animationPlaying = false;
	private double centerX = 0.0f;
	private double centerY = 0.0f;
	private Group group = null;
	private FadeTransition fadeInTransition = null;
	private FadeTransition fadeOutTransition = null;

	private Timeline rotationAnimation1;
	private Timeline rotationAnimation2;
	private Timeline rotationAnimation3;
	private Timeline rotationAnimation4;

	/**
	 * Wait animation pane
	 *
	 * @param layoutX
	 * @param layoutY
	 */
	public WaitAnimationPane(double layoutX, double layoutY) {

		// 668 x 230

		this.setPrefWidth(100);
		this.setPrefHeight(100);
		this.setLayoutX(layoutX);
		this.setLayoutY(layoutY);

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

		Arc arc1 = createArc(centerX, centerY, 15.0f, 15.0f, 270.0f, 250.0f, Color.web("#354f63"), 9);
		Arc arc2 = createArc(centerX, centerY, 20.0f, 20.0f, 270.0f, 76.0f, Color.web("#45677f"), 8);
		Arc arc3 = createArc(centerX, centerY, 27.0f, 27.0f, 270.0f, 50.0f, Color.web("#1b465f"), 6);
		Arc arc4 = createArc(centerX, centerY, 31.0f, 31.0f, 270.0f, 10.0f, Color.web("#317fad"), 4);

		group.getChildren().add(circle);
		group.getChildren().add(arc1);
		group.getChildren().add(arc2);
		group.getChildren().add(arc3);
		group.getChildren().add(arc4);
		group.setOpacity(1.0);
		group.setVisible(true);

		final Rotate rotationTransform1 = new Rotate(0, centerX, centerY);
		arc1.getTransforms().add(rotationTransform1);
		rotationAnimation1 = new Timeline();
		rotationAnimation1.getKeyFrames().add(new KeyFrame(Duration.seconds(10), new KeyValue(rotationTransform1.angleProperty(), 360)));
		rotationAnimation1.setCycleCount(Animation.INDEFINITE);

		final Rotate rotationTransform2 = new Rotate(0, centerX, centerY);
		arc2.getTransforms().add(rotationTransform2);
		rotationAnimation2 = new Timeline();
		rotationAnimation2.getKeyFrames().add(new KeyFrame(Duration.seconds(2), new KeyValue(rotationTransform2.angleProperty(), 360)));
		rotationAnimation2.setCycleCount(Animation.INDEFINITE);

		final Rotate rotationTransform3 = new Rotate(0, centerX, centerY);
		arc3.getTransforms().add(rotationTransform3);
		rotationAnimation3 = new Timeline();
		rotationAnimation3.getKeyFrames().add(new KeyFrame(Duration.seconds(3), new KeyValue(rotationTransform3.angleProperty(), 360)));
		rotationAnimation3.setCycleCount(Animation.INDEFINITE);

		final Rotate rotationTransform4 = new Rotate(0, centerX, centerY);
		arc4.getTransforms().add(rotationTransform4);
		rotationAnimation4 = new Timeline();
		rotationAnimation4.getKeyFrames().add(new KeyFrame(Duration.seconds(7), new KeyValue(rotationTransform4.angleProperty(), 360)));
		rotationAnimation4.setCycleCount(Animation.INDEFINITE);

		this.getChildren().add(group);
		startCircleAnimation();
	}

	public void startCircleAnimation() {
		rotationAnimation1.play();
		rotationAnimation2.play();
		rotationAnimation3.play();
		rotationAnimation4.play();
	}

	/**
	 * Show circle animation
	 * 
	 * @param v
	 */
	public void showCircleAnimation(boolean v) {
		if ((!animationPlaying) && (v == true)) {
			startCircleAnimation();
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
				fadeOutTransition = new FadeTransition(Duration.millis(50), group);
				fadeOutTransition.setFromValue(1.0);
				fadeOutTransition.setToValue(0.1);
				fadeOutTransition.setCycleCount(1);
			}
			fadeOutTransition.play();
			group.setVisible(v);
			animationPlaying = false;
		}
	}

	private Arc createArc(double x, double y, double rx, double ry, double startAngle, double length, Color c, int strokeWidth) {
		Arc arc = new Arc(x, y, rx, ry, startAngle, length);
		arc.setType(ArcType.OPEN);
		arc.setStrokeWidth(strokeWidth);
		arc.setStroke(c);
		arc.setStrokeType(StrokeType.CENTERED);
		arc.setFill(null);

		return arc;
	}
}
