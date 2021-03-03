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

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import net.clanwolf.client.administration.util.Internationalization;
import net.clanwolf.starmap.security.enums.PRIVILEGES;

import java.util.*;

public class AdminPaneController {

	private static ResourceBundle sMessagesPrivileges;
	private HashMap<Integer, CheckBox> privilegeBoxes = new HashMap<Integer, CheckBox>();

	@FXML
	ImageView ivLogo;

	@FXML
	Label labelDescription, labelUser, labelPrivCode;

	@FXML
	Tab tabPrivileges;

	@FXML
	Button btnSave, btnCancel;

	@FXML
	ScrollPane srollPane;

	@FXML
	public void btnSaveClicked() {
//		System.out.println("Clicked Save");
	}

	@FXML
	public void btnCancelClicked() {
//		System.out.println("Clicked Cancel");
	}

	private void calculatePrivCode() {
		long privCode = 0;
		Iterator<Integer> it = privilegeBoxes.keySet().iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			CheckBox b = privilegeBoxes.get(key);
			if (b.isSelected()) {
				if (key == 64) {
					privCode = -1;
					break;
				}
				privCode = privCode + key;
			} else {
				//
			}
		}
		System.out.println("PrivCode: " + privCode);
		labelPrivCode.setText("" + privCode);
	}

	private void setCheckBoxesForUser() {
		// TODO: Inititalize privileges for user
	}

	public void init(Locale locale) {
		labelDescription.setText(Internationalization.getString("AdminSecurityDescription"));
		tabPrivileges.setText(Internationalization.getString("AdminSecurityTabPrivileges"));
		labelUser.setText(Internationalization.getString("AdminSecurityUserLabel"));
		btnSave.setText(Internationalization.getString("AdminSecurityButtonSave"));
		btnCancel.setText(Internationalization.getString("AdminSecurityButtonCancel"));

		sMessagesPrivileges = ResourceBundle.getBundle("MessagesPrivilegeBundle", locale);

		VBox root = new VBox();

		int j = 1;
		int jj = 0;
		Iterator i = Arrays.stream(PRIVILEGES.values()).sequential().iterator();
		while (i.hasNext()) {
			PRIVILEGES p = (PRIVILEGES) i.next();

			String desc = sMessagesPrivileges.getString("" + p);
			if (!"DUMMY".equals(desc)) {
				System.out.println(j + " " + p + " --- " + desc);
				CheckBox cb = new CheckBox("[" + String.format("%02d", j) + "] - " + desc);
				cb.setOnAction(event -> {
					calculatePrivCode();
				});
				cb.setPrefWidth(630);
				cb.setPrefHeight(25);
				root.getChildren().add(cb);
				privilegeBoxes.put(j, cb);
				jj++;
			}
			j++;
		}
		srollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		srollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		srollPane.setContent(root);
	}
}
