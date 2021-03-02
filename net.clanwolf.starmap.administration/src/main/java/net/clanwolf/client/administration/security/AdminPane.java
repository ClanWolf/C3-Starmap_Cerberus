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
package net.clanwolf.client.administration.security;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.clanwolf.client.administration.util.Internationalization;

import java.util.Locale;

public class AdminPane extends Application {

	private final static Locale GERMAN = Locale.GERMAN;
	private final static Locale ENGLISH = Locale.ENGLISH;

	public AdminPane(Locale locale) {
		Parent root;
		Internationalization.setLocale(locale);
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(this.getClass().getResource("/fxml/AdminPane.fxml"));

			root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle(Internationalization.getString("AdminSecurityHeadline"));
			stage.setScene(new Scene(root, 800, 600));
			stage.setResizable(false);
			stage.show();

			AdminPaneController controller = fxmlLoader.getController();
			controller.init();

			start(stage);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AdminPane() {
		this(GERMAN);
	}

	@Override
	public void start(Stage stage) throws Exception {

	}

	public static void main(String[] args) {
		launch(AdminPane.class, args);
	}
}
