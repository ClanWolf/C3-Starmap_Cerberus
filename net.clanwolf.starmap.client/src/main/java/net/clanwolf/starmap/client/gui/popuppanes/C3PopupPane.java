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
package net.clanwolf.starmap.client.gui.popuppanes;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Tools;

public class C3PopupPane extends Pane {

	private final Rectangle rect;
	private final Rectangle rectBorder;
	private final ImageView view;

	public C3PopupPane(Image image, String desc) {

		rectBorder = new Rectangle(600, 400);
		rectBorder.setStroke(Color.rgb(130, 170, 20, 1.0));
		rectBorder.setStrokeWidth(3.0);
		rectBorder.setFill(Color.TRANSPARENT);
		rectBorder.setOpacity(1.0);
		rectBorder.setLayoutX(-190);
		rectBorder.setLayoutY(-185);
		rectBorder.setWidth(600);
		rectBorder.setHeight(400);
		rectBorder.setMouseTransparent(true);

		rect = new Rectangle(100, 100);
		rect.setFill(Color.rgb(81, 114, 15, 0.95));

		view = new ImageView();
		view.setImage(image);
		view.setFitWidth(300);
		view.setFitHeight(110);
		view.setTranslateX(-40);
		view.setTranslateY(-110);
		view.setOpacity(0.0);

		TextField textField = new TextField();
		textField.setMaxWidth(600);
		textField.setPrefWidth(600);
		textField.setMinWidth(600);
		textField.setMaxHeight(50);
		textField.setPrefHeight(50);
		textField.setMinHeight(50);
		textField.setTranslateX(-190);
		textField.setTranslateY(120);
		textField.setStyle("-fx-font-alignment:center;-fx-background-color:transparent;-fx-border-color:transparent;");
		textField.setAlignment(Pos.BASELINE_CENTER);
		textField.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
		textField.setText(desc);

		this.getChildren().add(rect);
		this.getChildren().add(view);
		this.getChildren().add(textField);

		this.setWidth(600);
		this.setHeight(400);
		this.setPrefWidth(600);
		this.setPrefHeight(400);
		this.setMaxWidth(600);
		this.setMaxHeight(400);
		this.setLayoutX(515 - rect.getWidth() / 2);
		this.setLayoutY(315 - rect.getHeight() / 2);
		this.setOpacity(0.0);
		this.toFront();
		this.setCache(true);
		this.setCacheHint(CacheHint.SPEED);
		this.setMouseTransparent(true);
	}

	public void fadeIn() {
		rectBorder.toFront();
		Tools.playGUICreationSound();

		C3SoundPlayer.play("sound/fx/cursor_selection_11.mp3", false);

		FadeTransition fadeInTransition = new FadeTransition(Duration.millis(10), this);
		fadeInTransition.setFromValue(0.0);
		fadeInTransition.setToValue(1.0);
		fadeInTransition.setCycleCount(1);

		// Fade in transition 01 (Background)
		FadeTransition fadeInTransition_01 = new FadeTransition(Duration.millis(80), rect);
		fadeInTransition_01.setFromValue(0.0);
		fadeInTransition_01.setToValue(1.0);
		fadeInTransition_01.setCycleCount(2);

		// Fade in transition 02 (Border)
		FadeTransition fadeInTransition_02 = new FadeTransition(Duration.millis(250), rectBorder);
		fadeInTransition_02.setFromValue(0.0);
		fadeInTransition_02.setToValue(1.0);
		fadeInTransition_02.setCycleCount(4);

		Timeline timeline = new Timeline();
		KeyValue key1 = new KeyValue(rect.translateXProperty(), 60);
		KeyValue key2 = new KeyValue(rect.translateYProperty(), -35);
		KeyValue key3 = new KeyValue(rect.scaleXProperty(), 6);
		KeyValue key4 = new KeyValue(rect.scaleYProperty(), 4);
		final C3PopupPane mp = this;
		KeyFrame frame1 = new KeyFrame(Duration.seconds(.15), key1, key2, key3, key4);
		timeline.getKeyFrames().addAll(frame1);
		timeline.setOnFinished(event -> {
			if (!mp.getChildren().contains(rectBorder)) {
				mp.getChildren().add(rectBorder);
			}
			double w = rect.getWidth() * 6;
			double h = rect.getHeight() * 4;
			double x = rect.getX();
			double y = rect.getY();
			rectBorder.setWidth(w);
			rectBorder.setHeight(h);
			rectBorder.setX(x);
			rectBorder.setY(y);

			// Fade in transition 03 (Medal)
			FadeTransition fadeInTransition_03 = new FadeTransition(Duration.millis(10), view);
			fadeInTransition_03.setFromValue(0.0);
			fadeInTransition_03.setToValue(1.0);
			fadeInTransition_03.setCycleCount(1);

			fadeInTransition_03.play();
		});

		ScaleTransition st = new ScaleTransition(Duration.millis(2000), view);
		st.setToX(1.2f);
		st.setToY(1.2f);
		st.setCycleCount(1);

		ActionManager.getAction(ACTIONS.NOISE).execute(600);

		// Transition sequence
		SequentialTransition sequentialTransition = new SequentialTransition();
		sequentialTransition.setOnFinished(event -> fadeOut());
		sequentialTransition.getChildren().addAll(fadeInTransition, fadeInTransition_01, timeline, fadeInTransition_02, st);
		sequentialTransition.setCycleCount(1);
		sequentialTransition.play();
	}

	public void fadeOut() {
		FadeTransition FadeOutTransition = new FadeTransition(Duration.millis(1500), this);
		FadeOutTransition.setFromValue(1.0);
		FadeOutTransition.setToValue(0.0);
		FadeOutTransition.setCycleCount(1);
		FadeOutTransition.setOnFinished(event -> ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute());
		FadeOutTransition.play();
	}
}
