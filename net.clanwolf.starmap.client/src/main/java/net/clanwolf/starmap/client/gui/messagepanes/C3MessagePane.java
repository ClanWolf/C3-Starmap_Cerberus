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
package net.clanwolf.starmap.client.gui.messagepanes;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.enums.C3MESSAGERESULTS;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;
import net.clanwolf.starmap.client.util.Tools;

public class C3MessagePane extends Pane {

	private C3Message message = null;
	private String text = null;
	private C3MESSAGETYPES type = null;

	private Button yesButton = new Button("Yes");
	private Button noButton = new Button("No");
	private Button okButton = new Button("OK");
	private Button closeButton = new Button("Close");
	private Button cancelButton = new Button("Cancel");

	private Rectangle rect;
	private Rectangle rectBorder;
	private Text messageText;

	public C3MessagePane(C3Message m) {
		this.message = m;
		this.text = message.getText();
		this.type = message.getType();

		yesButton.setLayoutX(80);
		yesButton.setLayoutY(160);
		yesButton.setMinWidth(150);
		yesButton.getStyleClass().add("contentButtonRed");
		yesButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				message.setResult(C3MESSAGERESULTS.YES);
				fadeOut();
			}
		});
		yesButton.setOnMouseEntered(e -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL_MESSAGE).execute();
		});
		yesButton.setOnMouseExited(e -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT_MESSAGE).execute();
		});

		noButton.setLayoutX(240);
		noButton.setLayoutY(160);
		noButton.setMinWidth(150);
		noButton.getStyleClass().add("contentButtonRed");
		noButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				message.setResult(C3MESSAGERESULTS.NO);
				fadeOut();
			}
		});
		noButton.setOnMouseEntered(e -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL_MESSAGE).execute();
		});
		noButton.setOnMouseExited(e -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT_MESSAGE).execute();
		});

		okButton.setLayoutX(80);
		okButton.setLayoutY(160);
		okButton.setMinWidth(150);
		okButton.getStyleClass().add("contentButtonRed");
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				message.setResult(C3MESSAGERESULTS.OK);
				fadeOut();
			}
		});
		okButton.setOnMouseEntered(e -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL_MESSAGE).execute();
		});
		okButton.setOnMouseExited(e -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT_MESSAGE).execute();
		});

		closeButton.setLayoutX(240);
		closeButton.setLayoutY(160);
		closeButton.setMinWidth(150);
		closeButton.getStyleClass().add("contentButtonRed");
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				message.setResult(C3MESSAGERESULTS.CLOSED);
				fadeOut();
			}
		});
		closeButton.setOnMouseEntered(e -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL_MESSAGE).execute();
		});
		closeButton.setOnMouseExited(e -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT_MESSAGE).execute();
		});

		cancelButton.setLayoutX(240);
		cancelButton.setLayoutY(160);
		cancelButton.setMinWidth(150);
		cancelButton.getStyleClass().add("contentButtonRed");
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				message.setResult(C3MESSAGERESULTS.CANCELED);
				fadeOut();
			}
		});
		cancelButton.setOnMouseEntered(e -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL_MESSAGE).execute();
		});
		cancelButton.setOnMouseExited(e -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT_MESSAGE).execute();
		});

		rectBorder = new Rectangle(600, 400);
		rectBorder.setStroke(Color.rgb(254, 47, 0, 1.0));
		rectBorder.setStrokeWidth(1.0);
		rectBorder.setFill(Color.TRANSPARENT);
		rectBorder.setOpacity(1.0);
		rectBorder.setLayoutX(-190);
		rectBorder.setLayoutY(-185);
		rectBorder.setWidth(600);
		rectBorder.setHeight(400);
		rectBorder.setMouseTransparent(true);

		rect = new Rectangle(100, 100);
		rect.setFill(Color.rgb(97, 9, 9, 0.95));
		messageText = new Text();
		messageText.setFill(Color.rgb(240, 240, 240, 1.0));
		messageText.setText(text);

		this.getChildren().add(rect);
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
		this.setMouseTransparent(false);
	}

	public void fadeIn() {
		// Fade in transition 01 (Background)
		FadeTransition fadeInTransition_01 = new FadeTransition(Duration.millis(140), this);
		fadeInTransition_01.setFromValue(0.0);
		fadeInTransition_01.setToValue(0.2);
		fadeInTransition_01.setCycleCount(4);

		// Fade in transition 02 (Border)
		FadeTransition fadeInTransition_02 = new FadeTransition(Duration.millis(240), this);
		fadeInTransition_02.setFromValue(0.0);
		fadeInTransition_02.setToValue(1.0);
		fadeInTransition_02.setCycleCount(2);

		// Transition sequence
		SequentialTransition sequentialTransition = new SequentialTransition();
		final C3MessagePane mp = this;
		ActionManager.getAction(ACTIONS.NOISE).execute(800);
		sequentialTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				rectBorder.toFront();
				Timeline timeline = new Timeline();

				KeyValue key1 = new KeyValue(rect.translateXProperty(), 60);
				KeyValue key2 = new KeyValue(rect.translateYProperty(), -35);
				KeyValue key3 = new KeyValue(rect.scaleXProperty(), 6);
				KeyValue key4 = new KeyValue(rect.scaleYProperty(), 4);

				KeyFrame frame1 = new KeyFrame(Duration.seconds(.2), key1, key2, key3, key4);
				timeline.getKeyFrames().addAll(frame1);
				timeline.play();
				timeline.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						mp.getChildren().add(rectBorder);

						double w = rect.getWidth() * 6;
						double h = rect.getHeight() * 4;
						double x = rect.getX();
						double y = rect.getY();
						rectBorder.setWidth(w);
						rectBorder.setHeight(h);
						rectBorder.setX(x);
						rectBorder.setY(y);

						mp.getChildren().add(messageText);

						switch (type) {
						case OK_CANCEL:
							mp.getChildren().add(okButton);
							mp.getChildren().add(cancelButton);
							break;
						case YES_NO:
							mp.getChildren().add(yesButton);
							mp.getChildren().add(noButton);
							break;
						case CLOSE:
							mp.getChildren().add(closeButton);
							break;
						default:
							break;
						}
					}
				});
			}
		});
		sequentialTransition.getChildren().addAll(fadeInTransition_01, fadeInTransition_02);
		sequentialTransition.setCycleCount(1);
		sequentialTransition.play();
	}

	public void fadeOut() {
		Tools.playGUICreationSound();

		yesButton.setDisable(true);
		noButton.setDisable(true);
		okButton.setDisable(true);
		closeButton.setDisable(true);
		cancelButton.setDisable(true);

		FadeTransition FadeOutTransition = new FadeTransition(Duration.millis(600), this);
		FadeOutTransition.setFromValue(1.0);
		FadeOutTransition.setToValue(0.0);
		FadeOutTransition.setCycleCount(1);
		FadeOutTransition.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ActionManager.getAction(ACTIONS.SHOW_MESSAGE_WAS_ANSWERED).execute(message);
			}
		});
		FadeOutTransition.play();
	}
}