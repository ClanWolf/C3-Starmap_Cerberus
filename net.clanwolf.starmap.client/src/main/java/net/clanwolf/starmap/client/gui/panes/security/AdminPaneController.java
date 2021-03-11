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
package net.clanwolf.starmap.client.gui.panes.security;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.enums.PRIVILEGES;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.UserDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.util.*;

public class AdminPaneController {

	private static ResourceBundle sMessagesPrivileges;
	private HashMap<Integer, CheckBox> privilegeBoxes = new HashMap<Integer, CheckBox>();
	private UserDTO currentUser = null;
	private ArrayList userList = new ArrayList<UserDTO>();
	private HashMap<String, Long> originalPrivileges = new HashMap<String, Long>();

	@FXML
	Label labelDescription, labelUser, labelPrivCode, labelPrivCodeBinary;

	@FXML
	Tab tabPrivileges;

	@FXML
	Button btnSave, btnCancel;

	@FXML
	ScrollPane srollPane;

	@FXML
	ComboBox cbUser;

	@FXML
	public void btnSaveClicked() {
		Iterator iterator = this.userList.iterator();
		while (iterator.hasNext()) {
			UserDTO u = (UserDTO) iterator.next();
			C3Logger.info("User " + u.getUserName() + ": " + u.getPrivileges());
		}

		GameState saveUsersState = new GameState();
		saveUsersState.setMode(GAMESTATEMODES.PRIVILEGE_SAVE);
		saveUsersState.addObject(this.userList);
		Nexus.fireNetworkEvent(saveUsersState);

		Stage stage = (Stage) btnSave.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void btnCancelClicked() {
		Iterator iterator = this.userList.iterator();
		while (iterator.hasNext()) {
			UserDTO u = (UserDTO) iterator.next();
			long privs = originalPrivileges.get(u.getUserName());
			u.setPrivileges(privs);
		}
		Stage stage = (Stage) btnSave.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void userSelectionChanged() {
		setCheckBoxesForUser((String)cbUser.getSelectionModel().getSelectedItem());
	}

	private void calculatePrivCode() {
		long privCode = 0;
		Iterator<Integer> it = privilegeBoxes.keySet().iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			CheckBox cb = privilegeBoxes.get(key);
			if (cb.isSelected()) {
				if (key == 64) {
					privCode = 1L << 63;
					for (int a = 1; a <= 63; a++) {
						CheckBox cb1 = privilegeBoxes.get(a);
						if (cb1 != null) {
							cb1.setSelected(false);
							cb1.setDisable(true);
						}
					}
					break;
				}
				Long tempPrivCode = 1L << key - 1;
				privCode = privCode | tempPrivCode;
			} else {
				if (key == 64) {
					for (int a = 1; a <= 63; a++) {
						CheckBox cb1 = privilegeBoxes.get(a);
						if (cb1 != null) {
							cb1.setDisable(false);
						}
					}
				}
			}
		}
		String b = Long.toBinaryString(privCode);
		String binCode = String.format("%64.64s", b).replace(' ', '0');
		labelPrivCode.setText("" + privCode);
		labelPrivCodeBinary.setText("" + binCode);
		currentUser.setPrivileges(privCode);
	}

	private void setCheckBoxesForUser(String username) {
//		C3Logger.info("User changed to: " + username);
		boolean godadmin = false;

		Iterator iter = this.userList.iterator();
		while(iter.hasNext()) {
			try {
				UserDTO user = (UserDTO) iter.next();
				if (user.getUserName().equals(username)) {
					currentUser = user;
					long privs = user.getPrivileges();
					if (privs < 0) {
						// god admin detected
						godadmin = true;
					}
//					C3Logger.info("User has: " + privs);
					Platform.runLater(() -> {
						String b = Long.toBinaryString(privs);
						String binCode = String.format("%64.64s", b).replace(' ', '0');
						labelPrivCode.setText("" + privs);
						labelPrivCodeBinary.setText("" + binCode);
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (currentUser != null) {
			if (!godadmin) {
				for (int a = 1; a <= 64; a++) {
					boolean p = Security.hasPrivilege(currentUser, PRIVILEGES.values()[a - 1]);
					CheckBox cb = privilegeBoxes.get(a);
					if (cb != null) {
						cb.setSelected(p);
						cb.setDisable(false);
					}
				}
			} else {
				for (int a = 1; a <= 64; a++) {
					CheckBox cb = privilegeBoxes.get(a);
					if (cb != null) {
						cb.setSelected(false);
						cb.setDisable(true);
					}
				}
				CheckBox cb = privilegeBoxes.get(64);
				cb.setSelected(true);
				cb.setDisable(false);
			}
		}
	}

	public void init(Locale locale, ArrayList userListFromNexus) {
		this.userList = userListFromNexus;

		Iterator iterator = userListFromNexus.iterator();
		while (iterator.hasNext()) {
			UserDTO u = (UserDTO) iterator.next();
			originalPrivileges.put(u.getUserName(), u.getPrivileges());
		}

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
				String binVal = Long.toBinaryString(1L << jj);

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

		ObservableList<String> data = FXCollections.observableArrayList();
		Iterator iter = this.userList.iterator();
		while(iter.hasNext()) {
			try {
				UserDTO user = (UserDTO) iter.next();
				data.add(user.getUserName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(data);
		cbUser.setItems(data);
		cbUser.getSelectionModel().select(0);
		setCheckBoxesForUser((String)cbUser.getSelectionModel().getSelectedItem());
	}
}
