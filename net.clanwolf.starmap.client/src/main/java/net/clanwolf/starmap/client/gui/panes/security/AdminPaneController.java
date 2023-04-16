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
 * Copyright (c) 2001-2023, ClanWolf.net                            |
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
import net.clanwolf.starmap.client.enums.PRIVILEGES;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import net.clanwolf.starmap.client.process.universe.BOJumpship;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.security.FinancesInfo;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.FactionDTO;
import net.clanwolf.starmap.transfer.dtos.UserDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.*;

public class AdminPaneController {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static ResourceBundle sMessagesPrivileges;
	@FXML
	CheckBox cbActiveUser;
	private UserDTO currentUser = null;
	private HashMap<Integer, CheckBox> privilegeBoxes = new HashMap<>();
	private ArrayList<UserDTO> userList = new ArrayList<>();
	private HashMap<String, Long> originalPrivileges = new HashMap<>();
	@FXML
	ComboBox<String> cbUser;
	@FXML
	ComboBox<BOFaction> cbFaction, cbUserFaction;
	private final ArrayList<BOJumpship> activeJumpships = Nexus.getBoUniverse().getJumpshipList();
	private final DecimalFormat nf = new DecimalFormat();

	@FXML
	Label labelUser, labelPrivCode, labelPrivCodeBinary;

	@FXML
	Tab tabUser, tabCharacter, tabPrivileges, tabFinances;

	@FXML
	Button btnSave, btnCancel;

	@FXML
	ScrollPane srollPane;
	private HashMap<String, Integer> originalActivatedStatus = new HashMap<>();
//	private HashMap<String, BOFaction> originalFaction = new HashMap<>();
	private ObservableList<FinancesInfo> financesInfos = FXCollections.observableArrayList();

	@FXML
	TableColumn<FinancesInfo,String> tblCIncome, tblCIncomeDescription;

	@FXML
	TableColumn<FinancesInfo,String> TblCCost;

	@FXML
	TableView<FinancesInfo> tableFinances;

	@FXML
	Label lCurrentBalance;

	// User Edit Tab
	@FXML
	Label lblName, lblPassword, lblPasswordConfirm, lblMail, lblMWOUser;

	@FXML
	TextField tfName, tfPassword, tfPasswordConfirm, tfMail, tfMWOUser;

	@FXML
	public void handleSetActiveUser(){
		Iterator<UserDTO> it = this.userList.iterator();
		while (it.hasNext()) {

			UserDTO u = (UserDTO)it.next();

			if(u.getUserName().equals( cbUser.getSelectionModel().getSelectedItem())) {
				u.setActive(cbActiveUser.isSelected() ? 1 : 0);
			}
		}
	}

	@FXML
	public void btnSaveClicked() {
//		Iterator iterator = this.userList.iterator();
//		while (iterator.hasNext()) {
//			UserDTO u = iterator.next();
//			logger.info("User " + u.getUserName() + ": " + u.getPrivileges());
//		}

		ArrayList<UserDTO> usersToSave = new ArrayList<>();
		Iterator<UserDTO> it = this.userList.iterator();
		while (it.hasNext()) {
			UserDTO u = it.next();
			logger.info("User " + u.getUserName() + ": " + u.getPrivileges());
			if (!(originalPrivileges.get(u.getUserName())).equals(u.getPrivileges())) {
				usersToSave.add(u);
			}
			if (!(originalActivatedStatus.get(u.getUserName())).equals(u.getActive())) {
				usersToSave.add(u);
			}
		}
		GameState saveUsersState = new GameState();
		saveUsersState.setMode(GAMESTATEMODES.PRIVILEGE_SAVE);
		saveUsersState.addObject(usersToSave);
		Nexus.fireNetworkEvent(saveUsersState);

		Stage stage = (Stage) btnSave.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void btnCancelClicked() {
		Iterator<UserDTO> iterator = this.userList.iterator();
		while (iterator.hasNext()) {
			UserDTO u = iterator.next();
			long privs = originalPrivileges.get(u.getUserName());
			Integer as = originalActivatedStatus.get(u.getUserName());
			u.setPrivileges(privs);
			u.setActive(as);
		}
		Stage stage = (Stage) btnSave.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void userSelectionChanged() {
		setCheckBoxesForUser((String)cbUser.getSelectionModel().getSelectedItem());
	}

	@FXML
	public void cbFactionSelectionChanged() {
		getIncomeByIndex(cbFaction.getSelectionModel().getSelectedIndex());
	}

	private void getIncomeByIndex(int index){

		Long factionId = activeJumpships.get(index).getJumpshipFaction(), balance = 0L;
		long lIncome = 0L;
		String incomeDes = null;
		financesInfos.clear();

		for(Map.Entry<Long, BOStarSystem> entry : Nexus.getBoUniverse().starSystemBOs.entrySet()) {
			Long key = entry.getKey();
			BOStarSystem value = entry.getValue();

			if(Objects.equals(value.getFactionId(), factionId)){
				switch ((int) value.getLevel().longValue()) {
					case 1 -> {
						lIncome = Constants.REGULAR_SYSTEM_GENERAL_INCOME * 1000;
						incomeDes = value.getName() + " - Regular System";
					}
					case 2 -> {
						lIncome = Constants.INDUSTRIAL_SYSTEM_GENERAL_INCOME * 1000;
						incomeDes = value.getName() + " - Industrial System";
					}
					case 3 -> {
						lIncome = Constants.CAPITAL_SYSTEM_GENERAL_INCOME * 1000;
						incomeDes = value.getName() + " - Capital System";
					}
				}
				balance = balance + lIncome;
				financesInfos.add(new FinancesInfo(nf.format(lIncome),incomeDes));
			}
		}

		lCurrentBalance.setText("Current Blanace: " + nf.format(balance));
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
		currentUser.setLastModifiedByUserID(Nexus.getCurrentUser().getUserId());
	}

	private void setCheckBoxesForUser(String username) {
//		logger.info("User changed to: " + username);
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
//					logger.info("User has: " + privs);
					Platform.runLater(() -> {
						String b = Long.toBinaryString(privs);
						String binCode = String.format("%64.64s", b).replace(' ', '0');
						labelPrivCode.setText("" + privs);
						labelPrivCodeBinary.setText("" + binCode);

						cbActiveUser.setSelected(user.getActive() == 1);
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
			originalActivatedStatus.put(u.getUserName(), u.getActive());
		}

//		labelDescription.setText(Internationalization.getString("AdminSecurityDescription"));
		tabPrivileges.setText(Internationalization.getString("AdminSecurityTabPrivileges"));
		labelUser.setText(Internationalization.getString("AdminSecurityUserLabel"));
		btnSave.setText(Internationalization.getString("AdminSecurityButtonSave"));
		btnCancel.setText(Internationalization.getString("AdminSecurityButtonCancel"));

		lblName.setText(Internationalization.getString("AdminUserLabelName"));
		lblPassword.setText(Internationalization.getString("AdminUserLabelPassword"));
		lblPasswordConfirm.setText(Internationalization.getString("AdminUserLabelPasswordConfirm"));
		lblMail.setText(Internationalization.getString("AdminUserLabelMail"));
		lblMWOUser.setText(Internationalization.getString("AdminUserLabelMWOUser"));

		if (Nexus.getCurrentUser() != null) {
			tfName.setText(Nexus.getCurrentUser().getUserName());
			tfPassword.setText(Nexus.getCurrentUser().getUserPassword());
			tfPasswordConfirm.setText("");
			tfMail.setText(Nexus.getCurrentUser().getUserEMail());
			tfMWOUser.setText(Nexus.getCurrentUser().getMwoUsername());
		}

		tfPassword.setDisable(true);
		tfPasswordConfirm.setDisable(true);

		sMessagesPrivileges = ResourceBundle.getBundle("MessagesPrivilegeBundle", locale);

		VBox root = new VBox();

		int j = 1;
		int jj = 0;
		Iterator i = Arrays.stream(PRIVILEGES.values()).sequential().iterator();
		while (i.hasNext()) {
			PRIVILEGES p = (PRIVILEGES) i.next();

			String desc = sMessagesPrivileges.getString(String.valueOf(p));
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
		Iterator<UserDTO> iter = this.userList.iterator();
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

		ObservableList<BOFaction> factions = FXCollections.observableArrayList();
		factions.addAll(Nexus.getBoUniverse().getActiveFactions());
		Collections.sort(factions);
		cbUserFaction.setItems(factions);
		cbUserFaction.setDisable(!Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_CHANGE_FACTION_FOR_USER));

		//Finances
		cbActiveUser.setText(Internationalization.getString("general_user_is_active_checkbox"));
		cbFaction.setItems(factions);
		cbFaction.getSelectionModel().select(0);

		tblCIncome.setCellValueFactory(cellData -> cellData.getValue().incomeProperty());
		tblCIncomeDescription.setCellValueFactory(cellData -> cellData.getValue().incomeDescriptionProperty());
		tableFinances.setItems(financesInfos);
		getIncomeByIndex(0);

		// Show or hide tabs according to privileges
		boolean privs = Security.hasPrivilege(PRIVILEGES.ADMIN_IS_GOD_ADMIN);
		boolean finances = Security.hasPrivilege(PRIVILEGES.FACTIONLEAD_HAS_ROLE);

		tabUser.setDisable(false);
		tabCharacter.setDisable(false);
		tabFinances.setDisable(!finances);
		tabPrivileges.setDisable(!privs);
	}
}
