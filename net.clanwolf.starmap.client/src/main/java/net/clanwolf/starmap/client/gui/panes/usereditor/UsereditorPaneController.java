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

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.transfer.enums.PRIVILEGES;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import net.clanwolf.starmap.client.process.universe.BOJumpship;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.security.FinancesInfo;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.util.Encryptor;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.UserDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class UsereditorPaneController {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final ArrayList<BOJumpship> activeJumpships = Nexus.getBoUniverse().getJumpshipList();
	private final DecimalFormat nf = new DecimalFormat();
	private final HashMap<Integer, CheckBox> privilegeBoxes = new HashMap<>();
	private final HashMap<String, Long> originalPrivileges = new HashMap<>();
	@FXML
	ComboBox<String> cbUser;
	private final ObservableList<FinancesInfo> financesInfos = FXCollections.observableArrayList();
	private final HashMap<String, Integer> originalActivatedStatus = new HashMap<>();
	@FXML
	Tab tabUser,
			tabCharacter,
			tabFaction,
			tabPrivileges,
			tabFinances;
	@FXML
	ScrollPane srollPane;
	@FXML
	CheckBox cbActiveUser,
			cbRequestFactionChange;
	@FXML
	ComboBox<BOFaction> cbFaction,
			cbRequestedFaction;
	@FXML
	Label labelUser, labelPrivCode,
			labelPrivCodeBinary,
			labelMaleCharImageList,
			labelFemaleCharImageList;
	@FXML
	Button btnSave,
			btnCancel;
	@FXML
	TableColumn<FinancesInfo, String> tblCIncome,
			tblCIncomeDescription,
			TblCCost;
	@FXML
	ImageView btShowPassword,
			btShowPasswordConfirm,
			ivCharImage;
	@FXML
	TableView<FinancesInfo> tableFinances;
	Image eye,
			eyeClosed;
	// User Edit Tab
	@FXML
	Label lblName,
			lblPassword,
			lblPasswordConfirm,
			lblMail,
			lblMWOUser,
			lblPasswordConfirmClear,
			lblPasswordClear,
			lblFacktionKeyHint,
			lblCurrentFactionKey,
			lblOldImageId,
			lCurrentBalance,
			lblFactionKey,
			lblFactionKeyLead,
			lblCharacterName;
	@FXML
	TextField tfName,
			tfPassword,
			tfPasswordConfirm,
			tfMail,
			tfMWOUser,
			tfFactionKey,
			tfFactionKeyLead,
			tfCharacterName;
	@FXML
	ListView<String> lvImageSelectorMale,
			lvImageSelectorFemale;
	private UserDTO currentUser = null;
	private ArrayList<UserDTO> userList = new ArrayList<>();
	private boolean currentUserWasChanged = false;
	private boolean showPW = false;
	private boolean usernameOk = true;
	private boolean pwOk = true;
	private boolean mailOk = true;
	private boolean mwoUsernameOk = true;
	private String originalUsername = "";
	private boolean characterNameOk = true;
	private String originalMail = "";
	private String originalCharacterName = "";
	private String originalCharacterImage = "";
	private String newSelectedCharacterImage = "";
	private String originalMWOUser = "";
	private boolean factionkeyOk = false;

	@FXML
	public void handleImageSelectedMale() {
		String s = lvImageSelectorMale.getSelectionModel().getSelectedItem();
		if (s != null) {
//			String imageName = "/images/chars/male/" + s + ".png";
			Image charImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(s)));
			ivCharImage.setImage(charImage);
			newSelectedCharacterImage = s;
			currentUserWasChanged = true;
			lblOldImageId.setText("(" + originalCharacterImage.substring(originalCharacterImage.length() - 8, originalCharacterImage.length() - 4) + ")");
			checkUserChanges();
		}
	}

	@FXML
	public void handleImageSelectedFemale() {
		String s = lvImageSelectorFemale.getSelectionModel().getSelectedItem();
		if (s != null) {
//			String imageName = "/images/chars/female/" + s + ".png";
			Image charImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(s)));
			ivCharImage.setImage(charImage);
			newSelectedCharacterImage = s;
			currentUserWasChanged = true;
			lblOldImageId.setText("(" + originalCharacterImage.substring(originalCharacterImage.length() - 8, originalCharacterImage.length() - 4) + ")");
			checkUserChanges();
		}
	}

	@FXML
	public void showPWButtonClick() {
		if (showPW) {
			showPW = false;
			lblPasswordClear.setText("*****");
			lblPasswordConfirmClear.setText("*****");
			btShowPassword.setImage(eyeClosed);
			btShowPasswordConfirm.setImage(eyeClosed);
		} else {
			showPW = true;
			lblPasswordClear.setText(tfPassword.getText());
			lblPasswordConfirmClear.setText(tfPasswordConfirm.getText());
			btShowPassword.setImage(eye);
			btShowPasswordConfirm.setImage(eye);
		}
	}

	@FXML
	public void handleSetActiveUser() {
		for (UserDTO u : this.userList) {
			if (u.getUserName().equals(cbUser.getSelectionModel().getSelectedItem())) {
				u.setActive(cbActiveUser.isSelected() ? 1 : 0);
			}
		}
	}

	@FXML
	public void handleRequestFactionChangeClick() {
		if (cbRequestFactionChange.isSelected()) {
			cbRequestedFaction.setDisable(false);
			cbRequestedFaction.getSelectionModel().select(0);
			lblFactionKey.setDisable(false);
			tfFactionKey.setDisable(false);
			lblFactionKey.setDisable(false);
			lblFacktionKeyHint.setDisable(false);
		} else {
			cbRequestedFaction.setDisable(true);
			cbRequestedFaction.getSelectionModel().clearSelection();
			lblFactionKey.setDisable(true);
			tfFactionKey.setDisable(true);
			lblFactionKey.setDisable(true);
			lblFacktionKeyHint.setDisable(true);
		}
	}

	@FXML
	public void btnSaveClicked() {
		ArrayList<UserDTO> usersToSave = new ArrayList<>();
		for (UserDTO u : this.userList) {
			// logger.info("User " + u.getUserName() + ": " + u.getPrivileges());
			if (!(originalPrivileges.get(u.getUserName())).equals(u.getPrivileges())) {
				if (!usersToSave.contains(u)) {
					usersToSave.add(u);
				}
			}
			if (!(originalActivatedStatus.get(u.getUserName())).equals(u.getActive())) {
				if (!usersToSave.contains(u)) {
					usersToSave.add(u);
				}
			}
			if (u.id.equals(Nexus.getCurrentUser().getUserId())) {
				if (currentUserWasChanged) {
					u.setUserName(tfName.getText());
					u.getCurrentCharacter().setName(tfCharacterName.getText());
					if (!"".equals(newSelectedCharacterImage)) {
						u.getCurrentCharacter().setCharImage(newSelectedCharacterImage);
					}
					u.setUserEMail(tfMail.getText());
					u.setMwoUsername(tfMWOUser.getText());

					Nexus.getCurrentUser().setUserName(tfName.getText());
					Nexus.getCurrentUser().setUserEMail(tfMail.getText());
					Nexus.getCurrentUser().setMwoUsername(tfMWOUser.getText());
					Nexus.getCurrentChar().setName(tfCharacterName.getText());
					if (!"".equals(newSelectedCharacterImage)) {
						Nexus.getCurrentChar().setCharImage(newSelectedCharacterImage);
						Nexus.setLoggedOnUserImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(newSelectedCharacterImage))));
						ActionManager.getAction(ACTIONS.SET_USER_IMAGE).execute();
					}

					if (tfPassword.getText().length() > 5
							&& tfPasswordConfirm.getText().length() > 5
							&& tfPassword.getText().equals(tfPasswordConfirm.getText())
					) {
						String pw = Encryptor.createSinglePassword(tfPassword.getText());
						u.setUserPasswordWebsite(pw);
						Nexus.getCurrentUser().setUserPasswordWebsite(pw);
					}
					if (!usersToSave.contains(u)) {
						usersToSave.add(u);
					}
				}
			}
		}

		GameState saveUsersState = new GameState();
		saveUsersState.setMode(GAMESTATEMODES.USERDATA_SAVE);
		saveUsersState.addObject(usersToSave);
		if (cbRequestFactionChange.isSelected()) {
			saveUsersState.addObject2(cbRequestedFaction.getSelectionModel().getSelectedItem().getFactionDTO().getId()); // BO Faction
			saveUsersState.addObject3(tfFactionKey.getText());
		}

		Nexus.fireNetworkEvent(saveUsersState);

		if (!"".equals(tfFactionKeyLead.getText())) {
			// Save changed FactionKey value
			Nexus.getCurrentFaction().getFactionDTO().setFactionKey(tfFactionKeyLead.getText());

			GameState saveFactionState = new GameState();
			saveFactionState.setMode(GAMESTATEMODES.FACTION_SAVE);
			saveFactionState.addObject(Nexus.getCurrentFaction().getFactionDTO());
			Nexus.fireNetworkEvent(saveFactionState);
		}
		currentUserWasChanged = false;
		newSelectedCharacterImage = "";

		Stage stage = (Stage) btnSave.getScene().getWindow();
		stage.close();
	}

	@FXML
	public void btnCancelClicked() {
		for (UserDTO u : this.userList) {
			long privs = originalPrivileges.get(u.getUserName());
			Integer as = originalActivatedStatus.get(u.getUserName());
			u.setPrivileges(privs);
			u.setActive(as);
		}
		Stage stage = (Stage) btnSave.getScene().getWindow();
		stage.close();

		showPW = false;
	}

	@FXML
	public void userSelectionChanged() {
		setCheckBoxesForUser((String) cbUser.getSelectionModel().getSelectedItem());
	}

	@FXML
	public void cbFactionSelectionChanged() {
		getIncomeByIndex(cbFaction.getSelectionModel().getSelectedIndex());
	}

	private void getIncomeByIndex(int index) {

		Long factionId = activeJumpships.get(index).getJumpshipFaction(), balance = 0L;
		long lIncome = 0L;
		String incomeDes = null;
		financesInfos.clear();

		for (Map.Entry<Long, BOStarSystem> entry : Nexus.getBoUniverse().starSystemBOs.entrySet()) {
			Long key = entry.getKey();
			BOStarSystem value = entry.getValue();

			if (Objects.equals(value.getFactionId(), factionId)) {
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
				financesInfos.add(new FinancesInfo(nf.format(lIncome), incomeDes));
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
				long tempPrivCode = 1L << key - 1;
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
		labelPrivCode.setText(String.valueOf(privCode));
		labelPrivCodeBinary.setText(binCode);
		currentUser.setPrivileges(privCode);
		currentUser.setLastModifiedByUserID(Nexus.getCurrentUser().getUserId());
	}

	private void setCheckBoxesForUser(String username) {
		//		logger.info("User changed to: " + username);
		boolean godadmin = false;

		Iterator<UserDTO> iter = this.userList.iterator();
		while (iter.hasNext()) {
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
						labelPrivCode.setText(String.valueOf(privs));
						labelPrivCodeBinary.setText(binCode);

						cbActiveUser.setSelected(user.getActive() == 1);
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error while setting checkboxes.", e);
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

	private void checkUserChanges() {
		if (!tabUser.getText().endsWith("*")) {
			tabUser.setText(tabUser.getText() + " *");
		}
		if (!tabCharacter.getText().endsWith("*")) {
			tabCharacter.setText(tabCharacter.getText() + " *");
		}

		if (Objects.equals(originalCharacterImage, newSelectedCharacterImage)) {
			lblOldImageId.setText("");
		}

		if (Objects.equals(originalUsername, tfName.getText())
				&& Objects.equals(originalMail, tfMail.getText())
				&& Objects.equals(originalMWOUser, tfMWOUser.getText())
				&& Objects.equals(originalCharacterName, tfCharacterName.getText())
				&& Objects.equals(originalCharacterImage, newSelectedCharacterImage)
				&& tfPassword.getText().length() == 0
				&& tfPasswordConfirm.getText().length() == 0
				&& Objects.equals(tfPassword.getText(), tfPasswordConfirm.getText())
		) {
			// All fields are unchanged, pw fields are empty
			// Nothing to save!
			if (tabUser.getText().endsWith("*")) {
				tabUser.setText(tabUser.getText().substring(0, tabUser.getText().length() - 2));
				tabCharacter.setText(tabCharacter.getText().substring(0, tabCharacter.getText().length() - 2));
			}
			btnSave.setDisable(false);
			currentUserWasChanged = false;
		} else {
			btnSave.setDisable(!usernameOk || !characterNameOk || !pwOk || !mailOk || !mwoUsernameOk);
		}
	}

	public void init(Locale locale, ArrayList<UserDTO> userListFromNexus) {
		this.userList = userListFromNexus;
		for (UserDTO u : userListFromNexus) {
			originalPrivileges.put(u.getUserName(), u.getPrivileges());
			originalActivatedStatus.put(u.getUserName(), u.getActive());
		}

		eye = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/eye.png")));
		eyeClosed = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/eye_closed.png")));

		btShowPassword.setImage(eyeClosed);
		btShowPasswordConfirm.setImage(eyeClosed);

		tabUser.setText(Internationalization.getString("AdminSecurityTabUser"));
		tabCharacter.setText(Internationalization.getString("AdminSecurityTabCharacter"));
		tabFinances.setText(Internationalization.getString("AdminSecurityTabFinances"));
		tabPrivileges.setText(Internationalization.getString("AdminSecurityTabPrivileges"));

		labelUser.setText(Internationalization.getString("AdminSecurityUserLabel"));
		btnSave.setText(Internationalization.getString("AdminSecurityButtonSave"));
		btnCancel.setText(Internationalization.getString("AdminSecurityButtonCancel"));

		lblName.setText(Internationalization.getString("AdminUserLabelName"));
		lblPassword.setText(Internationalization.getString("AdminUserLabelPassword"));
		lblPasswordConfirm.setText(Internationalization.getString("AdminUserLabelPasswordConfirm"));
		lblMail.setText(Internationalization.getString("AdminUserLabelMail"));
		lblMWOUser.setText(Internationalization.getString("AdminUserLabelMWOUser"));
		lblFactionKey.setText(Internationalization.getString("AdminUserLabelFactionKey"));
		lblFacktionKeyHint.setText(Internationalization.getString("AdminUserLabelFactionKeyHint"));
		lblCurrentFactionKey.setText(Nexus.getCurrentFaction().getFactionDTO().getFactionKey());

		cbRequestFactionChange.setText(Internationalization.getString("AdminUserRequestFactionChange"));

		if (Nexus.getCurrentUser() != null) {
			tfName.setText(Nexus.getCurrentUser().getUserName());
			tfCharacterName.setText(Nexus.getCurrentUser().getCurrentCharacter().getName());
			tfPasswordConfirm.setText("");
			tfMail.setText(Nexus.getCurrentUser().getUserEMail());
			tfMWOUser.setText(Nexus.getCurrentUser().getMwoUsername());
		}

		originalUsername = tfName.getText();
		originalMail = tfMail.getText();
		originalMWOUser = tfMWOUser.getText();
		originalCharacterName = tfCharacterName.getText();

		ResourceBundle sMessagesPrivileges = ResourceBundle.getBundle("MessagesPrivilegeBundle", locale);

		VBox root = new VBox();

		int j = 1;
		int jj = 0;
		Iterator<PRIVILEGES> i = Arrays.stream(PRIVILEGES.values()).sequential().iterator();
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
		while (iter.hasNext()) {
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
		setCheckBoxesForUser((String) cbUser.getSelectionModel().getSelectedItem());

		ObservableList<BOFaction> factions = FXCollections.observableArrayList();
		factions.addAll(Nexus.getBoUniverse().getActiveFactions());
		Collections.sort(factions);
		//		cbUserFaction.setItems(factions);
		//		cbUserFaction.setDisable(!Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_CHANGE_FACTION_FOR_USER));

		//Finances
		cbActiveUser.setText(Internationalization.getString("general_user_is_active_checkbox"));
		cbFaction.setItems(factions);
		cbFaction.getSelectionModel().select(0);

		cbRequestedFaction.setItems(factions);

		tblCIncome.setCellValueFactory(cellData -> cellData.getValue().incomeProperty());
		tblCIncomeDescription.setCellValueFactory(cellData -> cellData.getValue().incomeDescriptionProperty());
		tableFinances.setItems(financesInfos);
		getIncomeByIndex(0);

		currentUserWasChanged = false;

		ChangeListener<? super String> userNameFieldChangeListener = (ChangeListener<String>) (ov, old_val, new_val) -> {
			currentUserWasChanged = true;
			usernameOk = tfName.getText().length() >= 3;
			if (usernameOk) {
				tfName.setStyle("-fx-text-fill:green;");
				lblName.setStyle("-fx-text-fill:green;");
			} else {
				tfName.setStyle("-fx-text-fill:red;");
				lblName.setStyle("-fx-text-fill:red;");
			}
			checkUserChanges();
		};
		ChangeListener<? super String> characterNameFieldChangeListener = (ChangeListener<String>) (ov, old_val, new_val) -> {
			currentUserWasChanged = true;
			characterNameOk = tfCharacterName.getText().length() >= 3;
			if (characterNameOk) {
				tfCharacterName.setStyle("-fx-text-fill:green;");
				lblCharacterName.setStyle("-fx-text-fill:green;");
			} else {
				tfCharacterName.setStyle("-fx-text-fill:red;");
				lblCharacterName.setStyle("-fx-text-fill:red;");
			}
			checkUserChanges();
		};
		ChangeListener<? super String> userPasswordFieldChangeListener = (ChangeListener<String>) (ov, old_val, new_val) -> {
			currentUserWasChanged = true;
			if ((tfPassword.getText().length() == 0 || tfPassword.getText().length() > 5) && tfPasswordConfirm.getText().equals(tfPassword.getText())) {
				tfPassword.setStyle("-fx-text-fill:green;");
				tfPasswordConfirm.setStyle("-fx-text-fill:green;");
				lblPassword.setStyle("-fx-text-fill:green;");
				lblPasswordConfirm.setStyle("-fx-text-fill:green;");
				pwOk = true;
			} else {
				tfPassword.setStyle("-fx-text-fill:red;");
				tfPasswordConfirm.setStyle("-fx-text-fill:red;");
				lblPassword.setStyle("-fx-text-fill:red;");
				lblPasswordConfirm.setStyle("-fx-text-fill:red;");
				pwOk = false;
			}
			if (showPW) {
				lblPasswordClear.setText(tfPassword.getText());
				lblPasswordConfirmClear.setText(tfPasswordConfirm.getText());
			}

			checkUserChanges();
		};
		ChangeListener<? super String> userPasswordConfirmFieldChangeListener = (ChangeListener<String>) (ov, old_val, new_val) -> {
			currentUserWasChanged = true;
			if ((tfPassword.getText().length() == 0 || tfPassword.getText().length() > 5) && tfPasswordConfirm.getText().equals(tfPassword.getText())) {
				tfPassword.setStyle("-fx-text-fill:green;");
				tfPasswordConfirm.setStyle("-fx-text-fill:green;");
				lblPassword.setStyle("-fx-text-fill:green;");
				lblPasswordConfirm.setStyle("-fx-text-fill:green;");
				pwOk = true;
			} else {
				tfPassword.setStyle("-fx-text-fill:red;");
				tfPasswordConfirm.setStyle("-fx-text-fill:red;");
				lblPassword.setStyle("-fx-text-fill:red;");
				lblPasswordConfirm.setStyle("-fx-text-fill:red;");
				pwOk = false;
			}
			if (showPW) {
				lblPasswordClear.setText(tfPassword.getText());
				lblPasswordConfirmClear.setText(tfPasswordConfirm.getText());
			}

			checkUserChanges();
		};
		ChangeListener<? super String> userMailFieldChangeListener = (ChangeListener<String>) (ov, old_val, new_val) -> {
			currentUserWasChanged = true;
			mailOk = !"".equals(tfMail.getText()) && tfMail.getText().length() > 5 && tfMail.getText().contains("@") && tfMail.getText().contains(".") && tfMail.getText().lastIndexOf(".") > tfMail.getText().indexOf("@") && !tfMail.getText().endsWith(".");
			if (mailOk) {
				tfMail.setStyle("-fx-text-fill:green;");
				lblMail.setStyle("-fx-text-fill:green;");
			} else {
				tfMail.setStyle("-fx-text-fill:red;");
				lblMail.setStyle("-fx-text-fill:red;");
			}
			checkUserChanges();
		};
		ChangeListener<? super String> userMWONameFieldChangeListener = (ChangeListener<String>) (ov, old_val, new_val) -> {
			currentUserWasChanged = true;
			mwoUsernameOk = tfMWOUser.getText().length() > 0;
			if (mwoUsernameOk) {
				tfMWOUser.setStyle("-fx-text-fill:green;");
				lblMWOUser.setStyle("-fx-text-fill:green;");
			} else {
				tfMWOUser.setStyle("-fx-text-fill:red;");
				lblMWOUser.setStyle("-fx-text-fill:red;");
			}
			checkUserChanges();
		};

		ChangeListener<? super String> factionkeyFieldChangeListener = (ChangeListener<String>) (ov, old_val, new_val) -> {
			currentUserWasChanged = true;
			factionkeyOk = tfFactionKey.getText().equals(cbRequestedFaction.getSelectionModel().getSelectedItem().getFactionDTO().getFactionKey());
			if (factionkeyOk) {
				tfFactionKey.setStyle("-fx-text-fill:green;");
				lblFactionKey.setStyle("-fx-text-fill:green;");
			} else {
				tfFactionKey.setStyle("-fx-text-fill:red;");
				lblFactionKey.setStyle("-fx-text-fill:red;");
			}
			checkUserChanges();
		};

		tfName.textProperty().addListener(userNameFieldChangeListener);
		tfPassword.textProperty().addListener(userPasswordFieldChangeListener);
		tfPasswordConfirm.textProperty().addListener(userPasswordConfirmFieldChangeListener);
		tfMail.textProperty().addListener(userMailFieldChangeListener);
		tfMWOUser.textProperty().addListener(userMWONameFieldChangeListener);
		tfFactionKey.textProperty().addListener(factionkeyFieldChangeListener);
		tfCharacterName.textProperty().addListener(characterNameFieldChangeListener);

		if (showPW) {
			lblPasswordClear.setText(tfPassword.getText());
			lblPasswordConfirmClear.setText(tfPasswordConfirm.getText());
		}

		tfPassword.setStyle("-fx-text-fill:red;");
		tfPasswordConfirm.setStyle("-fx-text-fill:red;");
		tfFactionKey.setStyle("-fx-text-fill:red;");

		// Show or hide tabs according to privileges
		boolean privs = Security.hasPrivilege(PRIVILEGES.ADMIN_IS_GOD_ADMIN);
		boolean finances = Security.hasPrivilege(PRIVILEGES.FACTIONLEAD_HAS_ROLE);
		boolean factionEdit = Security.hasPrivilege(PRIVILEGES.FACTIONLEAD_HAS_ROLE);

		cbRequestedFaction.setDisable(true);
		cbRequestedFaction.getSelectionModel().clearSelection();
		tfFactionKey.setDisable(true);
		lblFactionKey.setDisable(true);
		lblFacktionKeyHint.setDisable(true);
		tfFactionKey.setText("");

		// Character

		lblCharacterName.setText(Internationalization.getString("general_charactername"));
		labelMaleCharImageList.setText(Internationalization.getString("general_gender_male"));
		labelFemaleCharImageList.setText(Internationalization.getString("general_gender_female"));

		String imageName = Nexus.getCurrentChar().getCharImage();
		originalCharacterImage = imageName;
		Image charImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imageName)));
		ivCharImage.setImage(charImage);

		lvImageSelectorFemale.setCellFactory(listView -> new ListCell<>() {
			private final ImageView imageView = new ImageView();

			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				imageView.setFitWidth(80);
				imageView.setFitHeight(80);

				if (empty) {
					setGraphic(null);
				} else {
					Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(item)));
					imageView.setImage(image);
					setGraphic(imageView);
					setStyle("-fx-alignment: CENTER;");
				}
			}
		});

		lvImageSelectorMale.setCellFactory(listView -> new ListCell<>() {
			private final ImageView imageView = new ImageView();

			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				imageView.setFitWidth(80);
				imageView.setFitHeight(80);

				if (empty) {
					setGraphic(null);
				} else {
					Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(item)));
					imageView.setImage(image);
					setGraphic(imageView);
					setStyle("-fx-alignment: CENTER;");
				}
			}
		});

		for (int iii = 1000; iii < 1200; iii++) {
			URL urlMale = getClass().getResource("/images/chars/male/" + iii + ".jpg");
			if (urlMale != null) {
				lvImageSelectorMale.getItems().add("/images/chars/male/" + iii + ".jpg");
			}
		}

		for (int iii = 1000; iii < 1200; iii++) {
			URL urlMale = getClass().getResource("/images/chars/female/" + iii + ".jpg");
			if (urlMale != null) {
				lvImageSelectorFemale.getItems().add("/images/chars/female/" + iii + ".jpg");
			}
		}

		tabUser.setDisable(false);
		tabCharacter.setDisable(false);
		tabFaction.setDisable(!factionEdit);
		tabFinances.setDisable(!finances);
		tabPrivileges.setDisable(!privs);
	}
}
