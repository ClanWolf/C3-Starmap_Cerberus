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
 * Copyright (c) 2001-2022, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.preloader;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * C3 Preloader
 *
 * @author Meldric
 */
public class C3_Preloader extends Preloader {

	private WaitAnimationPane waitAnimationPane;
	ProgressBar bar;
	Stage stage;
	boolean isEmbedded = false;

	private Scene createPreloaderScene() {
		double SceneWidth;
		double SceneHeight;
		if (isEmbedded) {
			SceneWidth = stage.getWidth();
			SceneHeight = stage.getHeight();
		} else {
			SceneWidth = 668;
			SceneHeight = 230;
		}
		double BarWidth = 200;
		double BarHeight = 10;
		double BarPositionX = (SceneWidth - BarWidth) / 2;
		double BarPositionY = ((SceneHeight - BarHeight) / 2) + 35;

		double WaitAnimationX = SceneWidth / 2;
		double WaitAnimationY = SceneHeight / 2;

		waitAnimationPane = new WaitAnimationPane(WaitAnimationX, WaitAnimationY);
		bar = new ProgressBar();
		bar.getStyleClass().add("progress");
		bar.setPrefWidth(BarWidth);
		bar.setPrefHeight(BarHeight);
		bar.setLayoutX(BarPositionX);
		bar.setLayoutY(BarPositionY);
		Pane p = new Pane();
		p.getStyleClass().add("bpane");
		p.getChildren().add(waitAnimationPane);
		p.getChildren().add(bar);
		Scene preloaderScene = new Scene(p, SceneWidth, SceneHeight);
		preloaderScene.setFill(null);
		preloaderScene.getStylesheets().add(this.getClass().getResource("/C3_Preloader.css").toExternalForm());

		return preloaderScene;
	}

	// Main method to enable Eclipse to run the project
	public static void main(String[] args) {
		Application.launch(args);
	}

	/**
	 * Start
	 * 
	 * @param stage
	 * @throws Exception
	 */
	@Override
	public void start(Stage stage) throws Exception {
		// embedded stage has preset size
		isEmbedded = (stage.getWidth() > 0);
		this.stage = stage;
		stage.setTitle("Loading...");
		stage.getIcons().add(new Image("C3_Icon2.png"));
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setResizable(false);
		stage.setScene(createPreloaderScene());
		waitAnimationPane.showCircleAnimation(true);

		stage.show();
	}

	/**
	 * handle state change notification
	 *
	 * @param evt
	 */
	@Override
	public void handleStateChangeNotification(StateChangeNotification evt) {
		if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
			if (!isEmbedded && stage.isShowing() && stage.getScene() != null) {
				// fade out, hide stage at the end of animation
				FadeTransition ft = new FadeTransition(Duration.millis(1000), stage.getScene().getRoot());
				ft.setFromValue(1.0);
				ft.setToValue(0.0);
				final Stage s = stage;
				EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent t) {
						s.hide();
					}
				};
				ft.setOnFinished(eh);
				ft.play();
			} else {
				stage.hide();
			}
		}
	}

	/**
	 * Handle progress notification
	 *
	 * @param pn
	 */
	@Override
	public void handleProgressNotification(ProgressNotification pn) {
		if (pn.getProgress() != 1 && !stage.isShowing()) {
			stage.show();
		}
		bar.setProgress(pn.getProgress());
	}

	@Override
	public void handleApplicationNotification(PreloaderNotification arg0) {
		if (arg0 instanceof ProgressNotification) {
			ProgressNotification pn = (ProgressNotification) arg0;
			if (pn.getProgress() != 1 && !stage.isShowing()) {
				stage.show();
			}
			bar.setProgress(pn.getProgress());
		}
	}
}
