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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.clanwolf.client.administration.util.Internationalization;
import net.clanwolf.starmap.transfer.dtos.UserDTO;

import java.util.ArrayList;
import java.util.Locale;

public class AdminPane extends Application {

	private final static Locale GERMAN = Locale.GERMAN;
	private final static Locale ENGLISH = Locale.ENGLISH;

	private AdminPaneController controller;

	private ArrayList<UserDTO> userList = new ArrayList<UserDTO>();

	public void setUserList(ArrayList<UserDTO> list) {
		this.userList = list;
	}

	public ArrayList<UserDTO> getUserList() {
		return this.userList;
	}

	public AdminPane(ArrayList<UserDTO> userList, Stage parentStage, Locale locale) {
		this.userList = userList;

		// https://stackoverflow.com/questions/10486731/how-to-create-a-modal-window-in-javafx-2-1

		Parent root;
		Internationalization.setLocale(locale);
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(this.getClass().getResource("/fxml/AdminPane.fxml"));

			root = fxmlLoader.load();

			Stage stage = new Stage();
			stage.setTitle(Internationalization.getString("AdminSecurityHeadline"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.setResizable(true);
			stage.initOwner(parentStage);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

			stage.setMinWidth(stage.getWidth());
			stage.setMinHeight(stage.getHeight());

			controller = fxmlLoader.getController();
			controller.init(locale);

			start(stage);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public AdminPane() {
//		this(GERMAN);
//	}

	@Override
	public void start(Stage stage) throws Exception {

	}

	public static void main(String[] args) {
		launch(AdminPane.class, args);
	}
}
