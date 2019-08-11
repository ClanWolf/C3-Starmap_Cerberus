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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes.userinfo;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.*;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.process.logout.Logout;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.Tools;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.UserDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Meldric
 */
public class UserInfoPaneController extends AbstractC3Controller implements ActionCallBackListener {

	private boolean cancelWarning = true;
	private boolean enableExitEvent = true;
	private boolean cooledOff = false;

	@FXML
	private Label labelAvatar;
	@FXML
	private Label labelFactionLogo;
	@FXML
	private Label labelCharacterPortrait;

	@FXML
	private Label labelUsername;
	@FXML
	private Label labelFaction;
	@FXML
	private Label labelCreated;
	@FXML
	private Label valueUsername;
	@FXML
	private Label valueFaction;
	@FXML
	private Label valueCreated;

	@FXML
	private Label labelCharacterChooser;
	@FXML
	private Label labelCharName;
	@FXML
	private Label labelCharAge;
	@FXML
	private Label labelCharRank;
	@FXML
	private Label labelCharLocation;
	@FXML
	private Label labelCharXP;
	@FXML
	private Label valueCharName;
	@FXML
	private Label valueCharAge;
	@FXML
	private Label valueCharRank;
	@FXML
	private Label valueCharLocation;
	@FXML
	private Label valueCharXP;

	@FXML
	private Label labelBalance;
	@FXML
	private Label labelIncome;
	@FXML
	private Label labelSystems;
	@FXML
	private Label labelAtWar;
	@FXML
	private Label valueBalance;
	@FXML
	private Label valueIncome;
	@FXML
	private Label valueSystems;
	@FXML
	private Label valueAtWar;

	@FXML
	private Label labelLastLogin;
	@FXML
	private Label valueLastLogin;

	@FXML
	private Button buttonLogout;
	@FXML
	private Button buttonCancel;

	@FXML
	private ComboBox<RolePlayCharacterDTO> cbCharChooser;

	@FXML
	private void handleCancelButtonHoverEnter() {
		// do nothing here for now
	}

	@FXML
	private void handleCancelButtonHoverExit() {
		// do nothing here for now
	}

	@FXML
	private void handleCancelButtonClick() {
		cancelWarning = true;
		ActionManager.getAction(ACTIONS.PANE_DESTROY_CURRENT).execute();
	}

	@FXML
	private void handleLogoutButtonClick() throws Exception {
		cancelWarning = true;
		ActionManager.getAction(ACTIONS.SET_CONSOLE_OPACITY).execute(0.4);
		Logout.doLogout();
	}

	@FXML
	private void handleLogoutButtonHoverEnter() {
		setWarningOn(false);
		if (warningActive) {
			labelWarningText.setText(Internationalization.getString("app_user_info_panel_logoutWarning"));
			ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_warning"), true));

			if (cancelWarning) {
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_WarningLogout"));
				Tools.playAttentionSound();
				enableExitEvent = false;
				Platform.runLater(() -> {
					ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute();

					FadeTransition FadeInTransition = new FadeTransition(Duration.millis(300), buttonLogout);
					FadeInTransition.setFromValue(0.0);
					FadeInTransition.setToValue(1.0);
					FadeInTransition.setCycleCount(3);
					FadeInTransition.setOnFinished(event -> {
						C3SoundPlayer.play("sound/fx/beep_02.wav", false);
						ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("LogoutWarning");
						ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject("", false));
						labelWarningText.setText("");
						cancelWarning = false;
						enableExitEvent = true;
					});
					FadeInTransition.play();
				});
			}
		}
	}

	@FXML
	private void handleCharSelection() {
		C3Logger.info("Character selected");
		RolePlayCharacterDTO character = cbCharChooser.getSelectionModel().getSelectedItem();
		setCharValues(character);
		Nexus.getCurrentUser().setCurrentCharacter(character);

		save(Nexus.getCurrentUser());
	}

	@FXML
	private void handleOpenCharList() {
		C3Logger.info("Opened Char selection.");
	}

	public void save(UserDTO user) {
		GameState state = new GameState(GAMESTATEMODES.USER_SAVE);
		state.addObject(user);

		Nexus.fireNetworkEvent(state);
	}

	@FXML
	private void handleLogoutButtonHoverExit() {
		if (enableExitEvent) {
			labelWarningText.setText("");
			ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject("", false));
		}
	}

	private void setValues() {
		// set logos
		labelAvatar.setText("");

		// set values
		valueUsername.setText(Nexus.getCurrentUser().getUserName());
		valueLastLogin.setText(Nexus.getCurrentUser().getLastLogin() + "");
		valueCreated.setText(Nexus.getCurrentUser().getCreated() + "");
	}

	private void setCharValues(RolePlayCharacterDTO character) {
		// set logos
		labelFactionLogo.setText("");
		labelCharacterPortrait.setText("");

		// set values
		valueFaction.setText("");
		valueCharName.setText(character.getName());
		valueCharAge.setText("");
		valueCharRank.setText("");
		valueCharLocation.setText("");
		valueCharXP.setText("");
		valueBalance.setText("");
		valueIncome.setText("");
		valueSystems.setText("");
		valueAtWar.setText("");
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_BEGINS, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_DESTRUCTION_FINISHED, this);
	}

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 * @param rb
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		buttonLogout.setDisable(true);
		if(Nexus.getCurrentUser().getAvatar() != null) {
			Image imageAvatar = new Image(Nexus.getCurrentUser().getAvatar(), 84, 84, false, false);
			labelAvatar.setGraphic(new ImageView(imageAvatar));
		}

		cbCharChooser.setEditable(false);
//		if (Nexus.getCurrentUser().getCharacterList() != null) {
//			cbCharChooser.getItems().setAll(Nexus.getCurrentUser().getCharacterList());
//		}
		if (Nexus.getCurrentChar() != null) {
			cbCharChooser.getSelectionModel().select(Nexus.getCurrentChar());
			handleCharSelection();
		}

		// find the currently selected character.
		// either there is only one here in the list --> select it
		// or there are more but there was one selected previously --> select this one
		// or there are more but none has ever been selected --> wait for selection

		// Image imageFactionLogo = new Image(Nexus.getCurrentChar().getImage(), 100, 100, false, false);

		setValues();

		createListeners();
		enableListeners(true);
	}

	/**
	 * Set the strings on the gui (on initialize and on language change).
	 */
	@Override
	public void setStrings() {
		Platform.runLater(() -> {
			labelUsername.setText(Internationalization.getString("app_user_info_panel_username"));
			labelFaction.setText(Internationalization.getString("app_user_info_panel_faction"));
			labelCreated.setText(Internationalization.getString("app_user_info_panel_created"));
			labelCharacterChooser.setText(Internationalization.getString("app_user_info_panel_characterchooser"));
			labelCharName.setText(Internationalization.getString("app_user_info_panel_charname"));
			labelCharAge.setText(Internationalization.getString("app_user_info_panel_charage"));
			labelCharRank.setText(Internationalization.getString("app_user_info_panel_charrank"));
			labelCharLocation.setText(Internationalization.getString("app_user_info_panel_charlocation"));
			labelCharXP.setText(Internationalization.getString("app_user_info_panel_charxp"));
			labelBalance.setText(Internationalization.getString("app_user_info_panel_balance"));
			labelIncome.setText(Internationalization.getString("app_user_info_panel_income"));
			labelSystems.setText(Internationalization.getString("app_user_info_panel_systems"));
			labelAtWar.setText(Internationalization.getString("app_user_info_panel_contested"));
			labelLastLogin.setText(Internationalization.getString("app_user_info_panel_lastlogin"));

			buttonLogout.setText(Internationalization.getString("general_logout"));
			buttonCancel.setText(Internationalization.getString("general_cancel"));
		});
	}

	private void createListeners() {
		// userNameFieldChangeListener = new ChangeListener<String>() {
		// @Override
		// public void changed(ObservableValue<? extends String> ov, String
		// old_val, String new_val) {
		// buttonLogin.setDisable(!checkTextFieldsForContent());
		// user = tfUserName.getText();
		// }
		// };
		// userPassFieldChangeListener = new ChangeListener<String>() {
		// @Override
		// public void changed(ObservableValue<? extends String> ov, String
		// old_val, String new_val) {
		// buttonLogin.setDisable(!checkTextFieldsForContent());
		// if (!cbGuestAccount.isSelected()) {
		// if (!"".equals(tfPassword.getText())) {
		// if (password_encrypted) {
		// tfPassword.setText("");
		// pass = "";
		// password_encrypted = false;
		// // Log.debug("Reseting encryption / setting back
		// // [encrypted pw cannot be edited in field]");
		// } else {
		// pass = tfPassword.getText();
		// password_encrypted = false;
		// // Log.debug("Reseting encryption / taking new pw");
		// }
		// }
		// }
		// }
		// };
	}

	// @SuppressWarnings("unchecked")
	private void enableListeners(boolean enableListerners) {
		// if (enableListerners) {
		// tfUserName.textProperty().addListener(userNameFieldChangeListener);
		// tfPassword.textProperty().addListener(userPassFieldChangeListener);
		// } else {
		// tfUserName.textProperty().removeListener(userNameFieldChangeListener);
		// tfPassword.textProperty().removeListener(userPassFieldChangeListener);
		// }
	}

	private void setLogoutButtonText(String t) {
		Platform.runLater(() -> buttonLogout.setText(t));
	}

	private void enableLogoutButton() {
		Platform.runLater(() -> buttonLogout.setDisable(false));
	}

	/**
	 * Handle Actions
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case CHANGE_LANGUAGE:
				setStrings();
				break;

			case PANE_CREATION_BEGINS:
				break;

			case PANE_CREATION_FINISHED:
				if (!cooledOff) {
					Thread coolOffThread = new Thread(() -> {
						String caption = buttonLogout.getText();
						try {
							setLogoutButtonText(caption + " - 3");
							Thread.sleep(500);
							setLogoutButtonText(caption + " - 2");
							Thread.sleep(500);
							setLogoutButtonText(caption + " - 1");
							Thread.sleep(500);
							setLogoutButtonText(caption);
							enableLogoutButton();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					});
					coolOffThread.start();
					cooledOff = true;
				}
				break;

			case PANE_DESTROY_CURRENT:
				break;

			case PANE_DESTRUCTION_FINISHED:
				break;

			case LOGON_FINISHED_SUCCESSFULL:
				break;

			// case LOGON_FINISHED_WITH_ERROR:
			// Log.info("Login Error");
			// break;

			default:
				break;

		}
		return true;
	}

	/**
	 *
	 */
	@Override
	public void warningOnAction() {
		buttonLogout.setDisable(false);
	}

	/**
	 *
	 */
	@Override
	public void warningOffAction() {
		buttonLogout.setDisable(true);
	}
}
