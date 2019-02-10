/* ---------------------------------------------------------------- |
 * W-7 Research Group / C3                                          |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *          W-7 Facility / Research, Software Development           |
 *                    Tranquil (Mobile Division)                    |
 * __        __  _____   ____                               _       |
 * \ \      / / |___  | |  _ \ ___  ___  ___  __ _ _ __ ___| |__    |
 *  \ \ /\ / /____ / /  | |_) / _ \/ __|/ _ \/ _` | '__/ __| '_ \   |
 *   \ V  V /_____/ /   |  _ <  __/\__ \  __/ (_| | | | (__| | | |  |
 *    \_/\_/     /_/    |_| \_\___||___/\___|\__,_|_|  \___|_| |_|  |
 *                                                                  |
 *  W-7 is the production facility of Clan Wolf. The home base is   |
 *  Tranquil, but there are several mobile departments. In the      |
 *  development department there is a small group of developers and |
 *  designers busy to field new software products for battlefield   |
 *  commanders as well as for strategic dimensions of the clans     |
 *  operations. The department is led by a experienced StarColonel  |
 *  who fell out of active duty due to a wound he suffered during   |
 *  the battle on Tukkayid. His name and dossier are classified,    |
 *  get in contact through regular chain of command.                |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MkIII "Damien"                   |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 *  One of the products used to control the production and the      |
 *  transport of frontline troops is C3. C3 stands for              |
 *  "Communication - Command - Control".                            |
 *  Because there is a field based system to control the            |
 *  communication and data transfer of Mechs, this system is often  |
 *  refered to as "Big C3", however, the official name is           |
 *  "W-7 C3 / MkIII 'Damien'".                                      |
 *                                                                  |
 *  Licensing through W-7 Facility Central Office, Tranquil.        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  Info        : http://www.clanwolf.net                           |
 *  Forum       : http://www.clanwolf.net                           |
 *  Web         : http://c3.clanwolf.net                            |
 *  GitHub      : https://github.com/ClanWolf/C3-Java_Client        |
 *                                                                  |
 *  IRC         : c3.clanwolf.net @ Quakenet                        |
 *                                                                  |
 *  Devs        : - Christian (Meldric)                    [active] |
 *                - Werner (Undertaker)                    [active] |
 *                - Thomas (xfirestorm)                    [active] |
 *                - Domenico (Nonnex)                     [retired] |
 *                - Dirk (kotzbroken2)                    [retired] |
 *                                                                  |
 *                  (see Wolfnet for up-to-date information)        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  C3 includes libraries and source code by various authors,       |
 *  for credits and info, see README.                               |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 * Copyright 2016 ClanWolf.net                                      |
 *                                                                  |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 *                                                                  |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
 * ---------------------------------------------------------------- |
 */

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
		preloaderScene.getStylesheets().add(this.getClass().getResource("C3_Preloader.css").toExternalForm());

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
		stage.getIcons().add(new Image("c3_preloader/C3_Icon2.png"));
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
			if (!isEmbedded && stage.isShowing()) {
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
