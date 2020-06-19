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
 * Copyright (c) 2001-2020, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes.login;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;
import net.clanwolf.starmap.client.gui.messagepanes.C3Message;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.process.login.Login;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.Internationalization;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Meldric
 */
public class LoginPaneController extends AbstractC3Controller implements ActionCallBackListener {

	@FXML
	private CheckBox cbGuestAccount;
	@FXML
	private CheckBox cbStorePassword;
	@FXML
	private CheckBox cbDoAutoLogin;
	@FXML
	private Label labelUsername;
	@FXML
	private Label labelPassword;
	@FXML
	private Label panelHeadline;
	@FXML
	private Button buttonLogin;
	@FXML
	private Button buttonCancel;
	@FXML
	private Label labelFingerprint;
	@FXML
	private TextField tfUserName;
	@FXML
	private TextField tfPassword;
	@FXML
	private TextField tfFactionKey;
	@FXML
	private Rectangle recScanner;
	@FXML
	private Rectangle recScannerFingerprint;
	@FXML
	private Label labelFactionKey;

	private FadeTransition fadeInTransition_01 = null;
	private FadeTransition fadeInTransition_02 = null;
	private FadeTransition fadeInTransition_03 = null;
	private SequentialTransition sequentialTransition = null;
	private Timeline timeline01 = null;
	private double initialPositionY;
	private ChangeListener<? super String> userNameFieldChangeListener;
	private ChangeListener<? super String> userPassFieldChangeListener;
	private boolean StorePassword_OldValue = false;
	private boolean AutoStart_OldValue = false;
	private boolean password_encrypted = false;
	private String user = "";
	private String pass = "";

	private boolean autologin = false;

	@FXML
	private void handleCancelButtonHoverEnter() {
		// if (somethingToSave) {
		// warningLabel.setText(Internationalization.getString("app_pane_settings_cancelWarning"));
		// } else {
		// warningLabel.setText("");
		// }
	}

	@FXML
	private void handleCancelButtonHoverExit() {
		// warningLabel.setText("");
	}

	@FXML
	private void handleCancelButtonClick() {
		// resetValuesToInitial();
		ActionManager.getAction(ACTIONS.PANE_DESTROY_CURRENT).execute();
	}

	@FXML
	private void handleLoginButtonClick() throws Exception {
		ActionManager.getAction(ACTIONS.SET_CONSOLE_OPACITY).execute(0.4);
		String username = tfUserName.getText();
		// String factionKey = tfFactionKey.getText();

		Login.login(username, pass, tfFactionKey.getText(), password_encrypted);
	}

	@FXML
	private void handleUseGuestLoginCheckboxClick() {
		enableListeners(false);

		if (cbGuestAccount.isSelected()) {

			AutoStart_OldValue = cbDoAutoLogin.isSelected();
			StorePassword_OldValue = cbStorePassword.isSelected();

			tfUserName.setText(Internationalization.getString("app_pane_login_GuestAccountName"));
			tfPassword.setText(Internationalization.getString("app_pane_login_GuestAccountPWName"));
			// tfPassword.setText("guest password");

			tfUserName.setDisable(true);
			tfPassword.setDisable(true);
			cbDoAutoLogin.setDisable(true);
			cbStorePassword.setDisable(true);
			cbDoAutoLogin.setSelected(false);
			cbStorePassword.setSelected(false);

			tfFactionKey.setVisible(true);
			labelFactionKey.setVisible(true);

			C3Properties.setProperty(C3PROPS.USE_GUEST_ACCOUNT, "true", true);
			C3Properties.setProperty(C3PROPS.AUTO_LOGIN, "false", true);
			// C3Properties.setProperty(C3PROPS.STORE_LOGIN_PASSWORD, "false",
			// true);

		} else {

			if ((!"".equals(C3Properties.getProperty(C3PROPS.LOGIN_USER))) && (!"unknown".equals(C3Properties.getProperty(C3PROPS.LOGIN_USER)))) {
				user = C3Properties.getProperty(C3PROPS.LOGIN_USER);
			}

			if ((!"".equals(C3Properties.getProperty(C3PROPS.LOGIN_PASSWORD))) && (!"unknown".equals(C3Properties.getProperty(C3PROPS.LOGIN_PASSWORD)))) {
				pass = C3Properties.getProperty(C3PROPS.LOGIN_PASSWORD);
				password_encrypted = true;
			}

			tfUserName.setText(C3Properties.getProperty(C3PROPS.LOGIN_USER));
			tfPassword.setText(Internationalization.getString("app_pane_login_GuestAccountPWName"));
			// tfPassword.setText(pass);

			tfUserName.setDisable(false);
			tfPassword.setDisable(false);
			cbDoAutoLogin.setDisable(false);
			cbStorePassword.setDisable(false);
			cbDoAutoLogin.setSelected(AutoStart_OldValue);
			cbStorePassword.setSelected(StorePassword_OldValue);

			tfFactionKey.setVisible(false);
			labelFactionKey.setVisible(false);

			C3Properties.setProperty(C3PROPS.USE_GUEST_ACCOUNT, "false", true);
			C3Properties.setProperty(C3PROPS.AUTO_LOGIN, AutoStart_OldValue + "", true);
			// C3Properties.setProperty(C3PROPS.STORE_LOGIN_PASSWORD,
			// StorePassword_OldValue + "", true);
		}
		buttonLogin.setDisable(!checkTextFieldsForContent());
		enableListeners(true);
	}

	@FXML
	private void handleStorePasswordCheckboxClick() {
		if (cbStorePassword.isSelected()) {
			C3Properties.setProperty(C3PROPS.STORE_LOGIN_PASSWORD, "true", true);
			cbDoAutoLogin.setSelected(false);
			cbDoAutoLogin.setDisable(false);
		} else {
			C3Properties.setProperty(C3PROPS.STORE_LOGIN_PASSWORD, "false", true);
			cbDoAutoLogin.setSelected(false);
			cbDoAutoLogin.setDisable(true);
		}
	}

	@FXML
	private void handleDoAutoLoginCheckboxClick() {
		if (cbDoAutoLogin.isSelected()) {
			C3Properties.setProperty(C3PROPS.AUTO_LOGIN, "true", true);
			autologin = true;
		} else {
			C3Properties.setProperty(C3PROPS.AUTO_LOGIN, "false", true);
			autologin = false;
		}
		C3Logger.info("Auto-Login: " + autologin);
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_BEGINS, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_DESTRUCTION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGON_FINISHED_SUCCESSFULL, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGON_FINISHED_WITH_ERROR, this);
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

		panelHeadline.setText(Internationalization.getString("app_pane_login_headline"));
		cbGuestAccount.setText(Internationalization.getString("app_pane_login_ToggleGuestAccount"));
		cbStorePassword.setText(Internationalization.getString("app_pane_login_ToggleStorePassword"));
		cbDoAutoLogin.setText(Internationalization.getString("app_pane_login_ToggleDoAutoLogin"));
		labelUsername.setText(Internationalization.getString("general_username"));
		labelPassword.setText(Internationalization.getString("general_password"));
		labelFactionKey.setText(Internationalization.getString("app_pane_login_FactionKey"));

		buttonLogin.setText(Internationalization.getString("general_login"));
		buttonCancel.setText(Internationalization.getString("general_close"));

		buttonLogin.setDisable(true);
		buttonCancel.setDisable(false);

		recScanner.setOpacity(0.0);
		labelFingerprint.setOpacity(0.0);
		recScannerFingerprint.setOpacity(0.0);

		initialPositionY = 0;

		if ("true".equals(C3Properties.getProperty(C3PROPS.USE_GUEST_ACCOUNT))) {
			cbGuestAccount.setSelected(true);
			tfUserName.setText(Internationalization.getString("app_pane_login_GuestAccountName"));
			tfPassword.setText(Internationalization.getString("app_pane_login_GuestAccountPWName"));

			tfUserName.setDisable(true);
			tfPassword.setDisable(true);

			cbStorePassword.setDisable(true);
			cbDoAutoLogin.setDisable(true);
			cbStorePassword.setSelected(false);
			cbDoAutoLogin.setSelected(false);

			buttonLogin.setDisable(false);

			tfFactionKey.setVisible(true);
			labelFactionKey.setVisible(true);

		} else {
			cbGuestAccount.setSelected(false);
			user = C3Properties.getProperty(C3PROPS.LOGIN_USER);
			tfUserName.setText(user);
			if (!"".equals(C3Properties.getProperty(C3PROPS.LOGIN_PASSWORD))) {
				cbStorePassword.setSelected(true);
				pass = C3Properties.getProperty(C3PROPS.LOGIN_PASSWORD);
				tfPassword.setText(Internationalization.getString("app_pane_login_GuestAccountPWName"));
				// tfPassword.setText(pass);
				password_encrypted = true;
				buttonLogin.setDisable(false);
			} else {
				cbStorePassword.setSelected(false);
				tfPassword.setText("");
			}
			if ("true".equals(C3Properties.getProperty(C3PROPS.AUTO_LOGIN))) {
				cbDoAutoLogin.setSelected(true);
				autologin = true;
			}

			tfFactionKey.setVisible(false);
			labelFactionKey.setVisible(false);
		}

		StorePassword_OldValue = "true".equals(C3Properties.getProperty(C3PROPS.STORE_LOGIN_PASSWORD));

		createListeners();
		enableListeners(true);
	}

	/**
	 * Transition to fade in the scanner object.
	 */
	public void fadeInScanner() {

		// Fade in transition 01 (ScannerArea)
		fadeInTransition_01 = new FadeTransition(Duration.millis(150), recScanner);
		fadeInTransition_01.setFromValue(0.0);
		fadeInTransition_01.setToValue(0.75);
		fadeInTransition_01.setCycleCount(4);

		// Fade in transition 02 (Fingerprint)
		fadeInTransition_02 = new FadeTransition(Duration.millis(500), labelFingerprint);
		fadeInTransition_02.setFromValue(0.0);
		fadeInTransition_02.setToValue(0.75);
		fadeInTransition_02.setCycleCount(1);

		// Fade in transition 03 (Moving Scanner)
		fadeInTransition_03 = new FadeTransition(Duration.millis(750), recScannerFingerprint);
		fadeInTransition_03.setFromValue(0.0);
		fadeInTransition_03.setToValue(0.75);
		fadeInTransition_03.setCycleCount(1);

		// Move up and down 04 (Scanner movement)
		timeline01 = new Timeline();
		timeline01.setCycleCount(Timeline.INDEFINITE);
		timeline01.setAutoReverse(true);
		KeyValue kv = new KeyValue(recScannerFingerprint.yProperty(), 195);
		KeyFrame kf = new KeyFrame(Duration.millis(1800), kv);
		timeline01.getKeyFrames().add(kf);

		// Transition sequence
		sequentialTransition = new SequentialTransition();
		sequentialTransition.setOnFinished((ActionEvent event) -> {
		});
		sequentialTransition.getChildren().addAll(fadeInTransition_01, fadeInTransition_02, fadeInTransition_03, timeline01);
		sequentialTransition.setCycleCount(1);
		sequentialTransition.play();

		tfUserName.requestFocus();
		tfUserName.selectEnd();
	}

	/**
	 * Set the strings on the gui (on initialize and on language change).
	 */
	@Override
	public void setStrings() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				panelHeadline.setText(Internationalization.getString("app_pane_login_headline"));
				cbGuestAccount.setText(Internationalization.getString("app_pane_login_ToggleGuestAccount"));
				cbStorePassword.setText(Internationalization.getString("app_pane_login_ToggleStorePassword"));
				cbDoAutoLogin.setText(Internationalization.getString("app_pane_login_ToggleDoAutoLogin"));
				labelUsername.setText(Internationalization.getString("general_username"));
				labelPassword.setText(Internationalization.getString("general_password"));
				labelFactionKey.setText(Internationalization.getString("app_pane_login_FactionKey"));

				buttonLogin.setText(Internationalization.getString("general_login"));
				buttonCancel.setText(Internationalization.getString("general_cancel"));
			}
		});

		if (cbGuestAccount.isSelected()) {
			tfUserName.setText(Internationalization.getString("app_pane_login_GuestAccountName"));
			tfPassword.setText(Internationalization.getString("app_pane_login_GuestAccountPWName"));
		}
	}

	private boolean checkTextFieldsForContent() {
		boolean bothFieldsContainValues = false;

		if ((!"".equals(tfUserName.getText())) && (!"".equals(tfPassword.getText()))) {
			bothFieldsContainValues = true;
		}

		return bothFieldsContainValues;
	}

	private void createListeners() {
		userNameFieldChangeListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				buttonLogin.setDisable(!checkTextFieldsForContent());
				user = tfUserName.getText();
			}
		};
		userPassFieldChangeListener = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				buttonLogin.setDisable(!checkTextFieldsForContent());
				if (!cbGuestAccount.isSelected()) {
					if (!"".equals(tfPassword.getText())) {
						if (password_encrypted) {
							tfPassword.setText("");
							pass = "";
							password_encrypted = false;
							// Log.debug("Reseting encryption / setting back [encrypted pw cannot be edited in field]");
						} else {
							pass = tfPassword.getText();
							password_encrypted = false;
							// Log.debug("Reseting encryption / taking new pw");
						}
					}
				}
			}
		};
	}

	// @SuppressWarnings("unchecked")
	private void enableListeners(boolean enableListerners) {
		if (enableListerners) {
			tfUserName.textProperty().addListener(userNameFieldChangeListener);
			tfPassword.textProperty().addListener(userPassFieldChangeListener);
		} else {
			tfUserName.textProperty().removeListener(userNameFieldChangeListener);
			tfPassword.textProperty().removeListener(userPassFieldChangeListener);
		}
	}

	/**
	 * Handle Actions
	 *
	 * @param action
	 * @param o
	 * @return
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case CHANGE_LANGUAGE:
				setStrings();
				break;

			case PANE_CREATION_BEGINS:
				if (o.getObject().getClass() == LoginPane.class) {
					recScannerFingerprint.setY(initialPositionY); // .setLayoutY(positionY);
					recScanner.setOpacity(0.0);
					labelFingerprint.setOpacity(0.0);
					recScannerFingerprint.setOpacity(0.0);
				}
				break;

			case PANE_CREATION_FINISHED:
				if (o.getObject().getClass() == LoginPane.class) {
					fadeInScanner();
				}
				break;

			case PANE_DESTROY_CURRENT:
				// recScanner.setOpacity(0.0);
				// labelFingerprint.setOpacity(0.0);
				// recScannerFingerprint.setOpacity(0.0);
				break;

			case PANE_DESTRUCTION_FINISHED:
				if (o.getObject().getClass() == LoginPane.class) {
					sequentialTransition.stop();
					sequentialTransition = null;
					timeline01 = null;
					// recScanner.setOpacity(0.0);
					// labelFingerprint.setOpacity(0.0);
					// recScannerFingerprint.setOpacity(0.0);
				}
				break;

			case LOGON_FINISHED_SUCCESSFULL:
				C3Logger.info("Successfull login");
				break;

			case LOGON_FINISHED_WITH_ERROR:
				C3Logger.info("Login error");

				C3Message m = new C3Message();
				m.setText("jkjkjkj");
				m.setType(C3MESSAGETYPES.YES_NO);
				ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(m);

				// int result = showMessage("Login ERROR!", 1);
				// System.out.println("Result of message (user interaction): " + result);
				break;

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
	}

	/**
	 *
	 */
	@Override
	public void warningOffAction() {
	}
}
