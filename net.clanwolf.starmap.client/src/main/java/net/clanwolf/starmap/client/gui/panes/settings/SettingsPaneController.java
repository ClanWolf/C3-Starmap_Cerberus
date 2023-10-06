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
package net.clanwolf.starmap.client.gui.panes.settings;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.*;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.net.Server;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.Tools;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Meldric
 */
public class SettingsPaneController extends AbstractC3Controller implements ActionCallBackListener {

	private boolean cancelWarning = true;
	private boolean enableExitEvent = true;
	private final ToggleGroup proxyToggleGroup = new ToggleGroup();
	@FXML
	private Label panelHeadline;
	@FXML
	private Button buttonYes;
	@FXML
	private Button buttonNo;
	@FXML
	private Button testConnectionButton;
	@FXML
	private Label serverURL;
	@FXML
	private Label databaseName;
	@FXML
	private Label resultIcon;
	@FXML
	private TextField edit_ServerURL;
	@FXML
	private TextField edit_DatabaseName;
	@FXML
	private Tab tab1;
	@FXML
	private Tab tab2;
	@FXML
	private Tab tab3;
	@FXML
	private Tab tab4;

	// Proxy-Tab
	@FXML
	private RadioButton rb_NoProxy;
	@FXML
	private RadioButton rb_Proxy;
	@FXML
	private RadioButton rb_SystemProxy;
	@FXML
	private Label label_ProxyURL;
	@FXML
	private Label label_ProxyPort;
	@FXML
	private CheckBox cb_Authentication;
	@FXML
	private Label label_ProxyAuthUser;
	@FXML
	private Label label_ProxyAuthPassword;
	@FXML
	private Label label_ProxyAuthDomain;
	@FXML
	private CheckBox cb_SaveProxyPassword;
	@FXML
	private TextField edit_ProxyServerURL;
	@FXML
	private TextField edit_ProxyServerPort;
	@FXML
	private TextField edit_ProxyServerUserName;
	@FXML
	private TextField edit_ProxyServerPassword;
	@FXML
	private TextField edit_ProxyServerDomain;

	// Media-Tab
	@FXML
	private Label mediaLabel;
	@FXML
	private Label musicLabel;
	@FXML
	private Label volumeLabel;
	// @FXML
	// private Label trackLabel;
	@FXML
	private CheckBox checkbox_Speech;
	@FXML
	private CheckBox checkbox_Sound;
	@FXML
	private CheckBox checkbox_Music;
	@FXML
	private Slider slider_SpeechVolume;
	@FXML
	private Slider slider_SoundVolume;
	@FXML
	private Slider slider_MusicVolume;
	// @FXML
	// private TextField selectTrack;
	// @FXML
	// private TextArea selectTrackHelp;

	@FXML
	private CheckBox checkbox_HistoryScreenshot;
	@FXML
	private CheckBox checkbox_CheckClipBoardForMWOAPI;

	// Volumes
	private double SpeechVolume = 0.0;
	private double SoundVolume = 0.0;
	private double MusicVolume = 0.0;
	// Stored initial values in case of a cancel
	private double initial_SpeechVolume = 0.0;
	private double initial_SoundVolume = 0.0;
	private double initial_MusicVolume = 0.0;
	private boolean initial_PlaySpeech;
	private boolean initial_PlaySound;
	private boolean initial_PlayMusic;
	private boolean announce_check_result = false;
	private int initial_UseProxy_ToggleIndex;
	private String initial_edit_ServerURL;
	private String initial_edit_DatabaseName;
	private String initial_edit_ProxyServerURL;
	private String initial_edit_ProxyServerPort;
	private String initial_edit_ProxyServerUserName;
	private String initial_edit_ProxyServerPassword;
	private String initial_edit_ProxyServerDomain;
	// private String initial_selectTrack;
	private boolean initial_UseProxyAuthentication;
	private boolean initial_StoreProxyPassword;
	private boolean initial_checkbox_HistoryScreenshot;
	private boolean initial_checkbox_CheckClipBoardForMWOAPI;
	private ChangeListener<? super String> editFieldChangeListener;
	private ChangeListener<? super Number> editSliderSpeechChangeListener;
	private ChangeListener<? super Number> editSliderSoundChangeListener;
	private ChangeListener<? super Number> editSliderMusicChangeListener;
	private ChangeListener<? super Toggle> editProxyToggleChangeListener;
	private ChangeListener<? super Boolean> editProxyNeedsAuthenticationChangeListener;
	private ChangeListener<? super Boolean> editProxySavePasswordChangeListener;
	private ChangeListener<? super Boolean> editcheckbox_CheckClipBoardForMWOAPI;
	private ChangeListener<? super Boolean> editcheckbox_HistoryScreenshot;

	@FXML
	private void handleCancelButtonHoverEnter() {
		if (warningActive) {

			labelWarningText.setText(Internationalization.getString("app_pane_settings_cancelWarning"));
			ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_warning"), true));

			if (cancelWarning) {
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_WarningNotSaved"));
				Tools.playAttentionSound();
				enableExitEvent = false;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						// TODO_C3: Abstract the behavior of the cancel button
						ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("11");

						FadeTransition FadeInTransition = new FadeTransition(Duration.millis(400), buttonNo);
						FadeInTransition.setFromValue(0.0);
						FadeInTransition.setToValue(1.0);
						FadeInTransition.setCycleCount(3);
						FadeInTransition.setOnFinished(event -> {
							C3SoundPlayer.play("/sound/fx/beep_02.mp3", false);
							ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("CancelWarning");
							ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject("", false));
							labelWarningText.setText("");
							cancelWarning = false;
							enableExitEvent = true;
						});
						FadeInTransition.play();
					}
				});
			}
		}
	}

	@FXML
	private void handleCancelButtonHoverExit() {
		if (enableExitEvent) {
			labelWarningText.setText("");
			ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject("", false));
		}
	}

	@FXML
	private void handleCancelButtonClick() {
		resetValuesToInitial();
		ActionManager.getAction(ACTIONS.PANE_DESTROY_CURRENT).execute();
		cancelWarning = true;
	}

	@FXML
	private void handleConfirmButtonClick() {
		save();
		ActionManager.getAction(ACTIONS.PANE_DESTROY_CURRENT).execute();
		cancelWarning = true;
	}

	@FXML
	private void handleSpeechSelectButton(Event event) {
		CheckBox chk = (CheckBox) event.getSource();
		if (chk.isSelected()) {
			C3Properties.setProperty(C3PROPS.PLAY_VOICE, "true", true);
			slider_SpeechVolume.setDisable(false);
		} else {
			C3Properties.setProperty(C3PROPS.PLAY_VOICE, "false", true);
			slider_SpeechVolume.setDisable(true);
		}
		setWarningOn(true);
	}

	@FXML
	private void handleEffectsSelectButton(Event event) {
		CheckBox chk = (CheckBox) event.getSource();
		if (chk.isSelected()) {
			C3Properties.setProperty(C3PROPS.PLAY_SOUND, "true", true);
			slider_SoundVolume.setDisable(false);
		} else {
			C3Properties.setProperty(C3PROPS.PLAY_SOUND, "false", true);
			slider_SoundVolume.setDisable(true);
		}
		setWarningOn(true);
	}

	@FXML
	private void handleMusicSelectButton(Event event) {
		CheckBox chk = (CheckBox) event.getSource();
		if (chk.isSelected()) {
			C3Properties.setProperty(C3PROPS.PLAY_MUSIC, "true", true);
			slider_MusicVolume.setDisable(false);
			// selectTrack.setDisable(false);
			// trackLabel.setDisable(false);
			C3SoundPlayer.startMusic();
		} else {
			C3Properties.setProperty(C3PROPS.PLAY_MUSIC, "false", true);
			slider_MusicVolume.setDisable(true);
			// selectTrack.setDisable(true);
			// trackLabel.setDisable(true);
			C3SoundPlayer.pauseMusic();
		}
		setWarningOn(true);
	}

	private void resetValuesToInitial() {
		enableListeners(false);
		checkbox_Speech.setSelected(initial_PlaySpeech);
		checkbox_Sound.setSelected(initial_PlaySound);
		checkbox_Music.setSelected(initial_PlayMusic);
		if (initial_PlaySpeech) {
			C3Properties.setProperty(C3PROPS.PLAY_VOICE, "true", true);
		} else {
			C3Properties.setProperty(C3PROPS.PLAY_VOICE, "false", true);
		}
		if (initial_PlaySound) {
			C3Properties.setProperty(C3PROPS.PLAY_SOUND, "true", true);
		} else {
			C3Properties.setProperty(C3PROPS.PLAY_SOUND, "false", true);
		}
		if (initial_PlayMusic) {
			C3Properties.setProperty(C3PROPS.PLAY_MUSIC, "true", true);
			slider_MusicVolume.setDisable(false);
			// selectTrack.setDisable(false);
			// trackLabel.setDisable(false);
			C3SoundPlayer.startMusic();
		} else {
			C3Properties.setProperty(C3PROPS.PLAY_MUSIC, "false", true);
			slider_MusicVolume.setDisable(true);
			// selectTrack.setDisable(true);
			// trackLabel.setDisable(true);
			C3SoundPlayer.pauseMusic();
		}

		slider_SpeechVolume.setValue(initial_SpeechVolume * 100);
		slider_SoundVolume.setValue(initial_SoundVolume * 100);
		slider_MusicVolume.setValue(initial_MusicVolume * 100);

		slider_SpeechVolume.setDisable(!initial_PlaySpeech);
		slider_SoundVolume.setDisable(!initial_PlaySound);
		slider_MusicVolume.setDisable(!initial_PlayMusic);

		C3Properties.setDouble(C3PROPS.SOUNDVOLUME, initial_SoundVolume, true);
		C3Properties.setDouble(C3PROPS.SPEECHVOLUME, initial_SpeechVolume, true);
		C3Properties.setDouble(C3PROPS.MUSICVOLUME, initial_MusicVolume, true);

		proxyToggleGroup.getToggles().get(initial_UseProxy_ToggleIndex).setSelected(true);

		switch (initial_UseProxy_ToggleIndex) {
		case 0:
			disableProxyAll(true);
			break;
		case 1:
			disableProxyAll(true);
			break;
		case 2:
			disableProxyAll(false);
			break;
		}

		edit_ServerURL.setText(initial_edit_ServerURL);
		edit_DatabaseName.setText(initial_edit_DatabaseName);

		edit_ProxyServerURL.setText(initial_edit_ProxyServerURL);
		edit_ProxyServerPort.setText(initial_edit_ProxyServerPort);
		edit_ProxyServerUserName.setText(initial_edit_ProxyServerUserName);
		edit_ProxyServerPassword.setText(initial_edit_ProxyServerPassword);
		edit_ProxyServerDomain.setText(initial_edit_ProxyServerDomain);
		cb_Authentication.setSelected(initial_UseProxyAuthentication);
		cb_SaveProxyPassword.setSelected(initial_StoreProxyPassword);

		// selectTrack.setText(initial_selectTrack);

		setWarningOff();
		enableListeners(true);
	}

	private void save() {
		// set the initial values to the ones that have been saved to
		// make the cancel function on the next cycle work properly
		initial_SpeechVolume = slider_SpeechVolume.getValue() / 100;
		initial_SoundVolume = slider_SoundVolume.getValue() / 100;
		initial_MusicVolume = slider_MusicVolume.getValue() / 100;
		initial_PlaySpeech = checkbox_Speech.isSelected();
		initial_PlaySound = checkbox_Sound.isSelected();
		initial_PlayMusic = checkbox_Music.isSelected();
		initial_edit_ServerURL = edit_ServerURL.getText();
		initial_edit_DatabaseName = edit_DatabaseName.getText();
		initial_edit_ProxyServerURL = edit_ProxyServerURL.getText();
		initial_edit_ProxyServerPort = edit_ProxyServerPort.getText();
		initial_edit_ProxyServerUserName = edit_ProxyServerUserName.getText();
		initial_edit_ProxyServerPassword = edit_ProxyServerPassword.getText();
		initial_edit_ProxyServerDomain = edit_ProxyServerDomain.getText();
		initial_UseProxy_ToggleIndex = proxyToggleGroup.getToggles().indexOf(proxyToggleGroup.getSelectedToggle());
		initial_UseProxyAuthentication = cb_Authentication.isSelected();
		initial_StoreProxyPassword = cb_SaveProxyPassword.isSelected();
		initial_checkbox_HistoryScreenshot = checkbox_HistoryScreenshot.isSelected();
		initial_checkbox_CheckClipBoardForMWOAPI = checkbox_CheckClipBoardForMWOAPI.isSelected();
		// initial_selectTrack = selectTrack.getText();

		C3Properties.setProperty(C3PROPS.SERVER_URL, edit_ServerURL.getText(), true);
		C3Properties.setProperty(C3PROPS.LOGIN_DATABASE, edit_DatabaseName.getText(), true);

		C3Properties.setProperty(C3PROPS.PROXY_SERVER, edit_ProxyServerURL.getText(), true);
		C3Properties.setProperty(C3PROPS.PROXY_PORT, edit_ProxyServerPort.getText(), true);
		C3Properties.setProperty(C3PROPS.PROXY_USER, edit_ProxyServerUserName.getText(), true);
		C3Properties.setProperty(C3PROPS.PROXY_PASSWORD, edit_ProxyServerPassword.getText(), true);
		C3Properties.setProperty(C3PROPS.PROXY_DOMAIN, edit_ProxyServerDomain.getText(), true);

		// C3Properties.setProperty(C3PROPS.MUSICTRACK, selectTrack.getText(), true);

		setWarningOff();
	}

	private void disableProxyAll(boolean v) {
		label_ProxyURL.setDisable(v);
		label_ProxyPort.setDisable(v);
		cb_Authentication.setDisable(v);
		label_ProxyAuthUser.setDisable(v);
		label_ProxyAuthPassword.setDisable(v);
		label_ProxyAuthDomain.setDisable(v);
		cb_SaveProxyPassword.setDisable(v);
		edit_ProxyServerURL.setDisable(v);
		edit_ProxyServerPort.setDisable(v);
		edit_ProxyServerUserName.setDisable(v);
		edit_ProxyServerPassword.setDisable(v);
		edit_ProxyServerDomain.setDisable(v);

		if (!v) {
			disableProxyAuthentication();
		}
	}

	@FXML
	private void disableProxyAuthentication() {
		boolean v = !cb_Authentication.isSelected();

		label_ProxyAuthUser.setDisable(v);
		label_ProxyAuthPassword.setDisable(v);
		label_ProxyAuthDomain.setDisable(v);
		cb_SaveProxyPassword.setDisable(v);
		edit_ProxyServerUserName.setDisable(v);
		edit_ProxyServerPassword.setDisable(v);
		edit_ProxyServerDomain.setDisable(v);
	}

	/**
	 * Check the database connection.
	 */
	@FXML
	public void checkDatabaseConnection() {
		C3Properties.setProperty(C3PROPS.SERVER_URL, edit_ServerURL.getText(), true);
		C3Properties.setProperty(C3PROPS.LOGIN_DATABASE, edit_DatabaseName.getText(), true);
		resultIcon.getStyleClass().remove("resultIconLabel_Failure");
		resultIcon.getStyleClass().remove("resultIconLabel_Success");
		resultIcon.getStyleClass().add("resultIconLabel_Undetermined");
		Server.checkDatabaseConnectionTask();
		announce_check_result = true;
	}

	private void setDatabaseConnectionCheckResult(boolean r) {
		if (r) {
			// Success: The database is accessible
			C3Properties.setProperty(C3PROPS.CONNECTED_SUCCESSFULLY_ONCE, "true", true);

			initial_edit_ServerURL = edit_ServerURL.getText();
			initial_edit_DatabaseName = edit_DatabaseName.getText();

			resultIcon.getStyleClass().remove("resultIconLabel_Undetermined");
			resultIcon.getStyleClass().remove("resultIconLabel_Failure");
			resultIcon.getStyleClass().add("resultIconLabel_Success");

			if (announce_check_result) {
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Success"));
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("app_database_indicator_message_ONLINE"), false));
			}
		} else {
			// Failure: The database is NOT accessible
			resultIcon.getStyleClass().remove("resultIconLabel_Undetermined");
			resultIcon.getStyleClass().remove("resultIconLabel_Success");
			resultIcon.getStyleClass().add("resultIconLabel_Failure");

			if (announce_check_result) {
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("app_database_indicator_message_OFFLINE"), false));
			}
		}
		announce_check_result = false;
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.DATABASECONNECTIONCHECK_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.ACTION_SUCCESSFULLY_EXECUTED, this);

		// Added in AbstractC3Controller:
		// ActionManager.addActionCallbackListener(ACTIONS.ENABLE_DEFAULT_BUTTON, this);
		// ActionManager.addActionCallbackListener(ACTIONS.DISABLE_DEFAULT_BUTTON, this);
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		panelHeadline.setText(Internationalization.getString("app_pane_settings_headline"));

		// Tab 1
		tab1.setText(Internationalization.getString("app_pane_settings_tab1Name_Connection"));

		serverURL.setText(Internationalization.getString("app_pane_settings_serverURL"));
		databaseName.setText(Internationalization.getString("app_pane_settings_databaseName"));
		testConnectionButton.setText(Internationalization.getString("app_pane_settings_testConnectionButton"));

		// Tab 2
		tab2.setText(Internationalization.getString("app_pane_settings_tab2Name_Network"));

		rb_NoProxy.setText(Internationalization.getString("app_pane_settings_useNoProxy"));
		rb_Proxy.setText(Internationalization.getString("app_pane_settings_useProxy"));
		rb_SystemProxy.setText(Internationalization.getString("app_pane_settings_useSystemProxy"));
		label_ProxyURL.setText(Internationalization.getString("app_pane_settings_proxyURL"));
		label_ProxyPort.setText(Internationalization.getString("app_pane_settings_proxyPort"));
		cb_Authentication.setText(Internationalization.getString("app_pane_settings_proxyUseAuthentication"));
		label_ProxyAuthUser.setText(Internationalization.getString("app_pane_settings_proxyAuthUser"));
		label_ProxyAuthPassword.setText(Internationalization.getString("app_pane_settings_proxyAuthPassword"));
		label_ProxyAuthDomain.setText(Internationalization.getString("app_pane_settings_proxyAuthDomain"));
		cb_SaveProxyPassword.setText(Internationalization.getString("app_pane_settings_proxyAuthStorePassword"));

		// Tab 3
		tab3.setText(Internationalization.getString("app_pane_settings_tab3Name_System"));

		mediaLabel.setText(Internationalization.getString("app_pane_settings_Media"));
		musicLabel.setText(Internationalization.getString("app_pane_settings_Music"));
		volumeLabel.setText(Internationalization.getString("app_pane_settings_Volume"));
		// trackLabel.setText(Internationalization.getString("app_pane_settings_Track"));
		checkbox_Speech.setText(Internationalization.getString("app_pane_settings_EnableSpeech"));
		checkbox_Sound.setText(Internationalization.getString("app_pane_settings_EnableSound"));
		checkbox_Music.setText(Internationalization.getString("app_pane_settings_EnableMusic"));

		//Tab 4 Generals
		tab4.setText(Internationalization.getString("app_pane_settings_tab4Name_Generals"));
		checkbox_HistoryScreenshot.setText(Internationalization.getString("app_pane_settings_generals_historyscreenshots"));
		checkbox_CheckClipBoardForMWOAPI.setText(Internationalization.getString("app_pane_settings_generals_access_clipborad"));

		buttonNo.setText(Internationalization.getString("general_cancel"));
		buttonYes.setText(Internationalization.getString("general_save"));

		try {
			SpeechVolume = C3Properties.getDouble(C3PROPS.SPEECHVOLUME);
		} catch (NumberFormatException e) {
			SpeechVolume = 0.5;
		}
		try {
			SoundVolume = C3Properties.getDouble(C3PROPS.SOUNDVOLUME);
		} catch (NumberFormatException e) {
			SoundVolume = 0.5;
		}
		try {
			MusicVolume = C3Properties.getDouble(C3PROPS.MUSICVOLUME);
		} catch (NumberFormatException e) {
			MusicVolume = 0.5;
		}

		slider_SpeechVolume.setValue(SpeechVolume * 100);
		slider_SoundVolume.setValue(SoundVolume * 100);
		slider_MusicVolume.setValue(MusicVolume * 100);

		C3SoundPlayer.setBackgroundVolume(MusicVolume);
		C3SoundPlayer.setSoundVolume(SoundVolume);
		C3SoundPlayer.setVoiceVolume(SpeechVolume);

		// store initial values in case the settings are canceled.
		initial_SpeechVolume = SpeechVolume;
		initial_SoundVolume = SoundVolume;
		initial_MusicVolume = MusicVolume;

		if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_VOICE))) {
			checkbox_Speech.setSelected(true);
			slider_SpeechVolume.setDisable(false);
			initial_PlaySpeech = true;
		} else {
			checkbox_Speech.setSelected(false);
			slider_SpeechVolume.setDisable(true);
			initial_PlaySpeech = false;
		}

		if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_SOUND))) {
			checkbox_Sound.setSelected(true);
			slider_SoundVolume.setDisable(false);
			initial_PlaySound = true;
		} else {
			checkbox_Sound.setSelected(false);
			slider_SoundVolume.setDisable(true);
			initial_PlaySound = false;
		}

		if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_MUSIC))) {
			checkbox_Music.setSelected(true);
			slider_MusicVolume.setDisable(false);
			// selectTrack.setDisable(false);
			// trackLabel.setDisable(false);
			initial_PlayMusic = true;
		} else {
			checkbox_Music.setSelected(false);
			slider_MusicVolume.setDisable(true);
			// selectTrack.setDisable(true);
			// trackLabel.setDisable(true);
			initial_PlayMusic = false;
		}

		edit_ServerURL.setText(C3Properties.getProperty(C3PROPS.SERVER_URL));
		edit_DatabaseName.setText(C3Properties.getProperty(C3PROPS.LOGIN_DATABASE));

		// selectTrack.setText(C3Properties.getProperty(C3PROPS.MUSICTRACK));

		rb_NoProxy.setToggleGroup(proxyToggleGroup);
		rb_SystemProxy.setToggleGroup(proxyToggleGroup);
		rb_Proxy.setToggleGroup(proxyToggleGroup);

		cb_Authentication.setSelected(C3Properties.getBoolean(C3PROPS.PROXY_NEEDS_AUTHENTICATION));
		cb_SaveProxyPassword.setSelected(C3Properties.getBoolean(C3PROPS.PROXY_SAVE_PASSWORD));

		// Default value: no proxy
		// in case there are no settings yet
		proxyToggleGroup.getToggles().get(0).setSelected(true);
		disableProxyAll(true);
		initial_UseProxy_ToggleIndex = 0;

		if (("true".equals(C3Properties.getProperty(C3PROPS.USE_PROXY))) && ("true".equals(C3Properties.getProperty(C3PROPS.USE_SYSTEM_PROXY)))) {
			proxyToggleGroup.getToggles().get(1).setSelected(true);
			disableProxyAll(true);
			initial_UseProxy_ToggleIndex = 1;
		}
		if (("true".equals(C3Properties.getProperty(C3PROPS.USE_PROXY))) && ("false".equals(C3Properties.getProperty(C3PROPS.USE_SYSTEM_PROXY)))) {
			proxyToggleGroup.getToggles().get(2).setSelected(true);
			disableProxyAll(false);
			initial_UseProxy_ToggleIndex = 2;
		}
		if (("false".equals(C3Properties.getProperty(C3PROPS.USE_PROXY))) && ("true".equals(C3Properties.getProperty(C3PROPS.USE_SYSTEM_PROXY)))) {
			proxyToggleGroup.getToggles().get(1).setSelected(true);
			disableProxyAll(true);
			initial_UseProxy_ToggleIndex = 1;
		}
		if (("false".equals(C3Properties.getProperty(C3PROPS.USE_PROXY))) && ("false".equals(C3Properties.getProperty(C3PROPS.USE_SYSTEM_PROXY)))) {
			proxyToggleGroup.getToggles().get(0).setSelected(true);
			disableProxyAll(true);
			initial_UseProxy_ToggleIndex = 0;
		}

		edit_ProxyServerURL.setText(C3Properties.getProperty(C3PROPS.PROXY_SERVER));
		edit_ProxyServerPort.setText(C3Properties.getProperty(C3PROPS.PROXY_PORT));
		edit_ProxyServerUserName.setText(C3Properties.getProperty((C3PROPS.PROXY_USER)));
		edit_ProxyServerPassword.setText(C3Properties.getProperty(C3PROPS.PROXY_PASSWORD));
		edit_ProxyServerDomain.setText(C3Properties.getProperty(C3PROPS.PROXY_DOMAIN));

		// selectTrackHelp.setText(Internationalization.getString("app_pane_settings_musicHelp"));

		initial_edit_ServerURL = edit_ServerURL.getText();
		initial_edit_DatabaseName = edit_DatabaseName.getText();

		initial_edit_ProxyServerURL = edit_ProxyServerURL.getText();
		initial_edit_ProxyServerPort = edit_ProxyServerPort.getText();
		initial_edit_ProxyServerUserName = edit_ProxyServerUserName.getText();
		initial_edit_ProxyServerPassword = edit_ProxyServerPassword.getText();
		initial_edit_ProxyServerDomain = edit_ProxyServerDomain.getText();
		initial_UseProxyAuthentication = cb_Authentication.isSelected();
		initial_StoreProxyPassword = cb_SaveProxyPassword.isSelected();

		// initial_selectTrack = selectTrack.getText();

		buttonYes.setDisable(true);

		if ("true".equals(C3Properties.getProperty(C3PROPS.CONNECTED_SUCCESSFULLY_ONCE))) {
			resultIcon.getStyleClass().remove("resultIconLabel_Undetermined");
			resultIcon.getStyleClass().remove("resultIconLabel_Failure");
			resultIcon.getStyleClass().add("resultIconLabel_Success");
		}

		if ("true".equals(C3Properties.getProperty(C3PROPS.GENERALS_SCREENSHOT_HISTORY))) {
			checkbox_HistoryScreenshot.setSelected(true);
		}

		if ("true".equals(C3Properties.getProperty(C3PROPS.GENERALS_CLIPBOARD_API))) {
			checkbox_CheckClipBoardForMWOAPI.setSelected(true);
		}

		createListeners();
		enableListeners(true);
		disableContextMenusForEditFields();

		edit_DatabaseName.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				event.consume();
			}
		});
		edit_ProxyServerDomain.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				event.consume();
			}
		});
		edit_ProxyServerPassword.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				event.consume();
			}
		});
		edit_ProxyServerPort.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				event.consume();
			}
		});
		edit_ProxyServerURL.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				event.consume();
			}
		});
		edit_DatabaseName.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				event.consume();
			}
		});
		edit_ServerURL.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				event.consume();
			}
		});
	}

	private void createListeners() {
		editFieldChangeListener = (ChangeListener<String>) (ov, old_val, new_val) -> setWarningOn(true);
		editSliderSpeechChangeListener = (ChangeListener<Number>) (ov, old_val, new_val) -> {
			double volume = (double) new_val / 100;
			C3SoundPlayer.setVoiceVolume(volume);
			C3Properties.setDouble(C3PROPS.SPEECHVOLUME, volume, true);
			setWarningOn(true);
		};
		editSliderSoundChangeListener = (ChangeListener<Number>) (ov, old_val, new_val) -> {
			double volume = (double) new_val / 100;
			C3SoundPlayer.setSoundVolume(volume);
			C3Properties.setDouble(C3PROPS.SOUNDVOLUME, volume, true);
			setWarningOn(true);
		};
		editSliderMusicChangeListener = (ChangeListener<Number>) (ov, old_val, new_val) -> {
			double volume = (double) new_val / 100;
			C3SoundPlayer.setBackgroundVolume(volume / 10);
			C3Properties.setDouble(C3PROPS.MUSICVOLUME, volume, true);
			setWarningOn(true);
		};
		editProxyToggleChangeListener = (ChangeListener<Toggle>) (ov, old_toggle, new_toggle) -> {
			setWarningOn(true);
			if (proxyToggleGroup.getSelectedToggle() != null) {
				if ((proxyToggleGroup.getSelectedToggle() == rb_Proxy)) {
					disableProxyAll(false);
					C3Properties.setProperty(C3PROPS.USE_PROXY, "true", true);
					C3Properties.setProperty(C3PROPS.USE_SYSTEM_PROXY, "false", true);
				}
				if ((proxyToggleGroup.getSelectedToggle() == rb_NoProxy)) {
					disableProxyAll(true);
					C3Properties.setProperty(C3PROPS.USE_PROXY, "false", true);
					C3Properties.setProperty(C3PROPS.USE_SYSTEM_PROXY, "false", true);
				}
				if ((proxyToggleGroup.getSelectedToggle() == rb_SystemProxy)) {
					disableProxyAll(true);
					C3Properties.setProperty(C3PROPS.USE_PROXY, "false", true);
					C3Properties.setProperty(C3PROPS.USE_SYSTEM_PROXY, "true", true);
				}
			}
		};
		editProxyNeedsAuthenticationChangeListener = (ChangeListener<Boolean>) (ov, old_val, new_val) -> {
			if (cb_Authentication.isSelected()) {
				C3Properties.setProperty(C3PROPS.PROXY_NEEDS_AUTHENTICATION, "true", true);
			} else {
				C3Properties.setProperty(C3PROPS.PROXY_NEEDS_AUTHENTICATION, "false", true);
			}
			setWarningOn(true);
		};
		editProxySavePasswordChangeListener = (ChangeListener<Boolean>) (ov, old_val, new_val) -> {
			if (cb_SaveProxyPassword.isSelected()) {
				C3Properties.setProperty(C3PROPS.PROXY_SAVE_PASSWORD, "true", true);
			} else {
				C3Properties.setProperty(C3PROPS.PROXY_SAVE_PASSWORD, "false", true);
			}
			setWarningOn(true);
		};
		editcheckbox_CheckClipBoardForMWOAPI = (ChangeListener<Boolean>) (ov, old_val, new_val) -> {
			if (checkbox_CheckClipBoardForMWOAPI.isSelected()) {
				C3Properties.setProperty(C3PROPS.GENERALS_CLIPBOARD_API, "true", true);
			} else {
				C3Properties.setProperty(C3PROPS.GENERALS_CLIPBOARD_API, "false", true);
			}
			setWarningOn(true);
		};
		editcheckbox_HistoryScreenshot = (ChangeListener<Boolean>) (ov, old_val, new_val) -> {
			if (checkbox_HistoryScreenshot.isSelected()) {
				C3Properties.setProperty(C3PROPS.GENERALS_SCREENSHOT_HISTORY, "true", true);
			} else {
				C3Properties.setProperty(C3PROPS.GENERALS_SCREENSHOT_HISTORY, "false", true);
			}
			setWarningOn(true);
		};
	}

	// @SuppressWarnings("unchecked")
	private void enableListeners(boolean enableListerners) {
		if (enableListerners) {
			edit_ServerURL.textProperty().addListener(editFieldChangeListener);
			edit_DatabaseName.textProperty().addListener(editFieldChangeListener);
			edit_ProxyServerURL.textProperty().addListener(editFieldChangeListener);
			edit_ProxyServerPort.textProperty().addListener(editFieldChangeListener);
			edit_ProxyServerUserName.textProperty().addListener(editFieldChangeListener);
			edit_ProxyServerPassword.textProperty().addListener(editFieldChangeListener);
			edit_ProxyServerDomain.textProperty().addListener(editFieldChangeListener);
			// selectTrack.textProperty().addListener(editFieldChangeListener);
			slider_SpeechVolume.valueProperty().addListener(editSliderSpeechChangeListener);
			slider_SoundVolume.valueProperty().addListener(editSliderSoundChangeListener);
			slider_MusicVolume.valueProperty().addListener(editSliderMusicChangeListener);
			proxyToggleGroup.selectedToggleProperty().addListener(editProxyToggleChangeListener);
			cb_Authentication.selectedProperty().addListener(editProxyNeedsAuthenticationChangeListener);
			cb_SaveProxyPassword.selectedProperty().addListener(editProxySavePasswordChangeListener);
			checkbox_HistoryScreenshot.selectedProperty().addListener(editcheckbox_HistoryScreenshot);
			checkbox_CheckClipBoardForMWOAPI.selectedProperty().addListener(editcheckbox_CheckClipBoardForMWOAPI);
		} else {
			edit_ServerURL.textProperty().removeListener(editFieldChangeListener);
			edit_DatabaseName.textProperty().removeListener(editFieldChangeListener);
			edit_ProxyServerURL.textProperty().removeListener(editFieldChangeListener);
			edit_ProxyServerPort.textProperty().removeListener(editFieldChangeListener);
			edit_ProxyServerUserName.textProperty().removeListener(editFieldChangeListener);
			edit_ProxyServerPassword.textProperty().removeListener(editFieldChangeListener);
			edit_ProxyServerDomain.textProperty().removeListener(editFieldChangeListener);
			// selectTrack.textProperty().removeListener(editFieldChangeListener);
			slider_SpeechVolume.valueProperty().removeListener(editSliderSpeechChangeListener);
			slider_SoundVolume.valueProperty().removeListener(editSliderSoundChangeListener);
			slider_MusicVolume.valueProperty().removeListener(editSliderMusicChangeListener);
			proxyToggleGroup.selectedToggleProperty().removeListener(editProxyToggleChangeListener);
			cb_Authentication.selectedProperty().removeListener(editProxyNeedsAuthenticationChangeListener);
			cb_SaveProxyPassword.selectedProperty().removeListener(editProxySavePasswordChangeListener);
			checkbox_HistoryScreenshot.selectedProperty().removeListener(editcheckbox_HistoryScreenshot);
			checkbox_CheckClipBoardForMWOAPI.selectedProperty().removeListener(editcheckbox_CheckClipBoardForMWOAPI);
		}
	}

	/**
	 * Set strings.
	 */
	@Override
	public void setStrings() {
		Platform.runLater(() -> {
			panelHeadline.setText(Internationalization.getString("app_pane_settings_headline"));

			// Tab 1
			tab1.setText(Internationalization.getString("app_pane_settings_tab1Name_Connection"));

			serverURL.setText(Internationalization.getString("app_pane_settings_serverURL"));
			databaseName.setText(Internationalization.getString("app_pane_settings_databaseName"));
			// selectTrackHelp.setText(Internationalization.getString("app_pane_settings_musicHelp"));
			testConnectionButton.setText(Internationalization.getString("app_pane_settings_testConnectionButton"));

			// Tab 2
			tab2.setText(Internationalization.getString("app_pane_settings_tab2Name_Network"));

			rb_NoProxy.setText(Internationalization.getString("app_pane_settings_useNoProxy"));
			rb_Proxy.setText(Internationalization.getString("app_pane_settings_useProxy"));
			rb_SystemProxy.setText(Internationalization.getString("app_pane_settings_useSystemProxy"));
			label_ProxyURL.setText(Internationalization.getString("app_pane_settings_proxyURL"));
			label_ProxyPort.setText(Internationalization.getString("app_pane_settings_proxyPort"));
			cb_Authentication.setText(Internationalization.getString("app_pane_settings_proxyUseAuthentication"));
			label_ProxyAuthUser.setText(Internationalization.getString("app_pane_settings_proxyAuthUser"));
			label_ProxyAuthPassword.setText(Internationalization.getString("app_pane_settings_proxyAuthPassword"));
			label_ProxyAuthDomain.setText(Internationalization.getString("app_pane_settings_proxyAuthDomain"));
			cb_SaveProxyPassword.setText(Internationalization.getString("app_pane_settings_proxyAuthStorePassword"));

			// Tab 3
			tab3.setText(Internationalization.getString("app_pane_settings_tab3Name_System"));

			mediaLabel.setText(Internationalization.getString("app_pane_settings_Media"));
			musicLabel.setText(Internationalization.getString("app_pane_settings_Music"));
			volumeLabel.setText(Internationalization.getString("app_pane_settings_Volume"));
			// trackLabel.setText(Internationalization.getString("app_pane_settings_Track"));
			checkbox_Speech.setText(Internationalization.getString("app_pane_settings_EnableSpeech"));
			checkbox_Sound.setText(Internationalization.getString("app_pane_settings_EnableSound"));
			checkbox_Music.setText(Internationalization.getString("app_pane_settings_EnableMusic"));

			//Tab 4 Generals
			tab4.setText(Internationalization.getString("app_pane_settings_tab4Name_Generals"));
			checkbox_HistoryScreenshot.setText(Internationalization.getString("app_pane_settings_generals_historyscreenshots"));
			checkbox_CheckClipBoardForMWOAPI.setText(Internationalization.getString("app_pane_settings_generals_access_clipborad"));

			buttonNo.setText(Internationalization.getString("general_cancel"));
			buttonYes.setText(Internationalization.getString("general_save"));
		});
	}

	@Override
	public void setFocus() {
		Platform.runLater(() -> {
			//
		});
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
		case ENABLE_DEFAULT_BUTTON:
			enableDefaultButton(true);
			break;
		case DISABLE_DEFAULT_BUTTON:
			enableDefaultButton(false);
			break;
		case DATABASECONNECTIONCHECK_FINISHED:
			boolean r = (boolean) o.getObject();
			setDatabaseConnectionCheckResult(r);
			break;
		case ACTION_SUCCESSFULLY_EXECUTED:
			if ("CancelWarning".equals(o.getObject())) {

			}
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
		buttonYes.setDisable(false);
	}

	/**
	 *
	 */
	@Override
	public void warningOffAction() {
		buttonYes.setDisable(true);

	}
}
