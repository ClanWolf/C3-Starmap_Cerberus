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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes.usereditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.transfer.dtos.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Locale;

public class UsereditorPane extends Application {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final static Locale GERMAN = Locale.GERMAN;
	private final static Locale ENGLISH = Locale.ENGLISH;

	private UsereditorPaneController controller;

	public UsereditorPane(ArrayList<UserDTO> userListFromNexus, Stage parentStage, Locale locale) {
		ArrayList<UserDTO> userList = userListFromNexus;

		Parent root;
		Internationalization.setLocale(locale);
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(this.getClass().getResource("/fxml/UsereditorPane.fxml"));

			root = fxmlLoader.load();

			Stage stage = new Stage();
			stage.setTitle(Internationalization.getString("AdminSecurityHeadline"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.setResizable(true);
			stage.initOwner(parentStage);
			stage.initModality(Modality.APPLICATION_MODAL);

			InputStream is = this.getClass().getResourceAsStream("/icons/C3_Icon2.png");
			stage.getIcons().add(new Image(is));

			stage.setMinWidth(800);
			stage.setMinHeight(600);

			controller = fxmlLoader.getController();
			controller.init(locale, userList);

			stage.showAndWait();
		} catch (Exception e) {
			logger.error("Error in adminPane", e);
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage stage) throws Exception {

	}
}
