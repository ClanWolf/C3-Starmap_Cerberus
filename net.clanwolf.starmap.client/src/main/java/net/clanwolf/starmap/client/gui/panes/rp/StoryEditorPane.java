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
package net.clanwolf.starmap.client.gui.panes.rp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Pane;
import net.clanwolf.starmap.client.util.Internationalization;

import java.io.InputStream;
import java.util.Locale;

/**
 * Login scene to log into the database.
 *
 * @author Meldric
 * @version 1.0
 */
public class StoryEditorPane extends Application {

	private final static Locale GERMAN = Locale.GERMAN;
	private final static Locale ENGLISH = Locale.ENGLISH;

	private StoryEditorPaneController controller;

	public StoryEditorPane( Stage parentStage) {
		//super("/fxml/StoryEditorPane.fxml", false, true);
		//paneName = "StoryEditorPane";

		Parent root;
		//Internationalization.setLocale(locale);
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(this.getClass().getResource("/fxml/StoryEditorPane.fxml"));

			root = fxmlLoader.load();

			Stage stage = new Stage();
			stage.setTitle(Internationalization.getString("StoryEditorPane"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.setResizable(true);
			stage.initOwner(parentStage);
			stage.initModality(Modality.APPLICATION_MODAL);

			InputStream is = this.getClass().getResourceAsStream("/icons/C3_Icon2.png");
			stage.getIcons().add(new Image(is));

			stage.setMinWidth(1200);
			stage.setMinHeight(700);

			controller = fxmlLoader.getController();
			controller.initialize();

			stage.showAndWait();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage stage) throws Exception {

	}
}
