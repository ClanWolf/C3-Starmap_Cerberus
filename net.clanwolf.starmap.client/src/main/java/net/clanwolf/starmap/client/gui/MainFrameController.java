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
package net.clanwolf.starmap.client.gui;

import io.nadron.client.app.Game;
import io.nadron.client.event.NetworkEvent;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.clanwolf.starmap.client.gui.panes.chat.ChatPane;
import net.clanwolf.starmap.client.gui.panes.logging.LogPane;
import net.clanwolf.starmap.client.gui.panes.security.AdminPane;
import net.clanwolf.starmap.client.action.*;
import net.clanwolf.starmap.client.enums.C3MESSAGERESULTS;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;
import net.clanwolf.starmap.client.enums.PRIVILEGES;
import net.clanwolf.starmap.client.gui.medalpanes.C3MedalPane;
import net.clanwolf.starmap.client.gui.messagepanes.C3Message;
import net.clanwolf.starmap.client.gui.messagepanes.C3MessagePane;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Pane;
import net.clanwolf.starmap.client.gui.panes.WaitAnimationPane;
import net.clanwolf.starmap.client.gui.panes.confirmAppClose.ConfirmAppClosePane;
import net.clanwolf.starmap.client.gui.panes.login.LoginPane;
import net.clanwolf.starmap.client.gui.panes.map.MapPane;
import net.clanwolf.starmap.client.gui.panes.rp.RolePlayBasicPane;
import net.clanwolf.starmap.client.gui.panes.rp.StoryEditorPane;
import net.clanwolf.starmap.client.gui.panes.settings.SettingsPane;
import net.clanwolf.starmap.client.gui.panes.userinfo.UserInfoPane;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import net.clanwolf.starmap.client.process.universe.BOJumpship;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.net.Server;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.*;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.AttackCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.AttackDTO;
import net.clanwolf.starmap.transfer.dtos.UserDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.enums.MEDALS;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controlls gui objects for the MainFrame class.
 *
 * @author Meldric
 */
public class MainFrameController extends AbstractC3Controller implements ActionCallBackListener {

	private boolean enableLanguageSwitch = true;
	private boolean buttonsAreMoving = false;
	private boolean adminMenuActive = false;
	private boolean openAdministrationPane = false;
	private boolean openLogPane = false;
	private boolean openEditorPane = false;
	private AbstractC3Pane currentlyDisplayedPane = null;
	private AbstractC3Pane nextToDisplayPane = null;
	private LoginPane loginPane = null;
	private LogPane logPane = null;
	private UserInfoPane userInfoPane = null;
	private MapPane mapPane = null;
	private RolePlayBasicPane attackPane = null;
	private ChatPane chatPane = null;
	private SettingsPane settingsPane = null;
	private RolePlayBasicPane rolePlayPane = null;
	// private InfoPane infoPane = null;
	private ConfirmAppClosePane confirmAppClosePane = null;
	private WaitAnimationPane waitAnimationPane = new WaitAnimationPane();
	private FadeTransition fadeTransition_flash;
	private FadeTransition fadeTransition_fadein;
	private boolean paneTransitionInProgress = false;
	private int rowCount = 0;
	private String oldContent = "";
	private Animation spectrumAnimation = null;
	private Animation noiseAnimation = null;
	private ExecutorService exec = null;
	private C3MessagePane messagePane = null;
	private C3MedalPane medalPane = null;
	private int menuIndicatorPos = 0;
	private boolean adminPaneOpen = false;

	private final Image imageAdminButtonOff = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/adminOff.png")));
	private final Image imageAdminButtonOn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/adminOn.png")));

	@FXML
	private Label statuslabel;
	@FXML
	private Label toplabel;
	@FXML
	private Label exitLabelTopRight;
	@FXML
	private Label copyrightLabel;
	@FXML
	private Label onlineIndicatorLabel;
	@FXML
	private Label loginIndicatorLabel;
	@FXML
	private Label onlineIndicatorLabelHoverHelper;
	@FXML
	private Label databaseAccessibleIndicatorLabel;
	@FXML
	private Label databaseAccessibleIndicatorLabelHoverHelper;
	@FXML
	private Label versionLabel;
	// @FXML
	// private Label webButton_ClanPage;
	// @FXML
	// private Label webButton_Help;
	// @FXML
	// private Label webButton_Google;
	// @FXML
	// private Label webButton_SourceForge;
	// @FXML
	// private Label webButton_BugZilla;
	@FXML
	private Label labelResizerControl;

	// Top Buttons
	@FXML
	private Button userButton;
	@FXML
	private Button settingsButton;
	@FXML
	private Button adminButton;
	@FXML
	private Button adminPaneButton;
	@FXML
	private Button exitButton;

	// Column 1
	@FXML
	private Button rolePlayButton;
	@FXML
	private Button languageButton;
	@FXML
	private Button mapButton;
	@FXML
	private Button chatButton;
	@FXML
	private Button attackButton;
//	@FXML
//	private Button industryButton;
	@FXML
	private Button logButton;

	// Column 2
	@FXML
	private Button storyEditorButton;
	@FXML
	private Button renameMeButton2;
	@FXML
	private Button renameMeButton3;
	@FXML
	private Button renameMeButton4;
	@FXML
	private Button renameMeButton5;

	@FXML
	private AnchorPane rootAnchorPane;
	@FXML
	private Pane mouseStopper;
	@FXML
	private Label systemConsole;
	@FXML
	private Label systemConsoleCurrentLine;
	@FXML
	private Label systemConsoleCursor;
	@FXML
	private ImageView spectrumImage;
	@FXML
	private ImageView noiseImage;

	@FXML
	private ImageView hudinfo1;

	@FXML
	private Label helpLabel;

	@FXML
	private Label gameInfoLabel;

	@FXML
	private TextField terminalPrompt;

	@FXML
	private ImageView ircIndicator;

	@FXML
	private ProgressBar TFSProgress;

	@FXML
	private VBox TFSInfo;

	@FXML
	private Label labelTFSProgress;

	// -------------------------------------------------------------------------
	//
	// Button hovering
	//
	// -------------------------------------------------------------------------
	// BUTTON EXIT
	// Mouse entered
	@FXML
	private void handleExitButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_pane_confirm_infotext"), false);
		Tools.playButtonHoverSound();
	}
	// Mouse entered

	@FXML
	private void handleHelpButtonHoverEnter() {
		Image helpImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/help_hover.png")));
		ImageView view = new ImageView(helpImg);
		view.setFitHeight(16);
		view.setPreserveRatio(true);
		helpLabel.setGraphic(view);
		helpLabel.setText("");
		setStatusText(Internationalization.getString("app_pane_open_manual_infotext"), false);
		Tools.playButtonHoverSound();
		C3SoundPlayer.getTTSFile(Internationalization.getString("app_web_help"));
	}

	@FXML
	private void handleHelpButtonHoverExit() {
		Image helpImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/help.png")));
		ImageView view = new ImageView(helpImg);
		view.setFitHeight(16);
		view.setPreserveRatio(true);
		helpLabel.setGraphic(view);
		helpLabel.setText("");
		setStatusText("", false);
	}

	@FXML
	private void handleTFSProgressEntered() {
		double tfsProgress = 100d / 60d * Nexus.getBoUniverse().currentRound;
		String progString = Nexus.getBoUniverse().currentRound + " / 60";
		labelTFSProgress.setText(progString);
		TFSInfo.toFront();
		TFSProgress.toFront();
		TFSProgress.setProgress(tfsProgress);
		TFSInfo.setVisible(true);
	}

	@FXML
	private void handleTFSProgressExited() {
		double tfsProgress = 100d / 60d * Nexus.getBoUniverse().currentRound;
		String progString = Nexus.getBoUniverse().currentRound + " / 60";
		labelTFSProgress.setText(progString);
		TFSInfo.toFront();
		TFSProgress.toFront();
		TFSProgress.setProgress(tfsProgress);
		TFSInfo.setVisible(false);
	}

	@FXML
	private void handleExitLabelTopRightMouseEventEnter() {
		setStatusText(Internationalization.getString("app_pane_confirm_infotext"), false);
		Tools.playButtonHoverSound();
	}

	@FXML
	private void handleAdminButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_admin_infotext").replace("%20", " ") + ".", true);
//		Tools.playButtonHoverSound();
	}

	@FXML
	private void handleSettingsButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_settings_infotext"), false);
		Tools.playButtonHoverSound();
	}
	// Mouse exited

	@FXML
	private void handleLoginButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_login_infotext"), false);
		Tools.playButtonHoverSound();
	}
	// Mouse exited

	@FXML
	private void handleMapButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_map_infotext"), false);
		Tools.playButtonHoverSound();
	}

	@FXML
	private void handleChatButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_chat_infotext"), false);
		Tools.playButtonHoverSound();
	}

//	@FXML
//	private void handleCharacterButtonMouseEventEnter() {
//		setStatusText(Internationalization.getString("app_character_infotext"), false);
//		Tools.playButtonHoverSound();
//	}

	@FXML
	private void handleStoryEditorButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_storyeditor_infotext"), false);
		Tools.playButtonHoverSound();
	}

	@FXML
	private void handleAttackButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_attack_infotext"), false);
		Tools.playButtonHoverSound();
	}

	@FXML
	private void handlePluginsButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_plugins_infotext"), false);
		Tools.playButtonHoverSound();
	}

	@FXML
	private void handleEndroundButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_endround_infotext"), false);
		Tools.playButtonHoverSound();
	}

	@FXML
	private void handleMenuButtonMouseEventExit() {
		setStatusText("", false);
	}
	// BUTTON LANGUAGE
	// Mouse entered

	@FXML
	private void handleLanguageButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_language_infotext"), false);
		// String lang = Internationalization.getLanguage();
		languageButton.getStyleClass().remove("languageButton_de");
		languageButton.getStyleClass().remove("languageButton_en");
		languageButton.getStyleClass().add("languageButton_hover");
	}
	// Mouse exited

	@FXML
	private void handleHelpMouseEventClick() {
		ActionManager.getAction(ACTIONS.OPEN_MANUAL).execute();
	}

	@FXML
	private void handleLanguageButtonMouseEventExit() {
		setStatusText("", false);
		languageButton.getStyleClass().remove("languageButton_hover");
		languageButton.getStyleClass().add("languageButton_" + Internationalization.getLanguage());
		enableLanguageSwitch = true;
	}

	@FXML
	private void handleTerminalEnterButton(KeyEvent event) {
		if (event.getCode() == KeyCode.UP) {
			ActionManager.getAction(ACTIONS.TERMINAL_COMMAND).execute("*!!!*historyBack");
		}
		if (event.getCode() == KeyCode.DOWN) {
			ActionManager.getAction(ACTIONS.TERMINAL_COMMAND).execute("*!!!*historyForward");
		}
		if (event.getCode() == KeyCode.ENTER) {
			String enteredCommand = terminalPrompt.getText();
			C3Logger.info("Enter pressed on terminal. Entered command: " + enteredCommand);
			Platform.runLater(() -> {
				ActionManager.getAction(ACTIONS.TERMINAL_COMMAND).execute(enteredCommand);
				terminalPrompt.setText("");
			});
		}
	}

	// @FXML
	// private void handleClanButtonMouseEventClick() {
	// Tools.startBrowser("http://www.clanwolf.net");
	// }

	// @FXML
	// private void handleClanButtonMouseEventEnter() {
	// setStatusText(Internationalization.getString("app_web_clanhome"), false);
	// }

	// @FXML
	// private void handleGoogleButtonMouseEventClick() {
	// Tools.startBrowser("https://groups.google.com/forum/#!forum/c3_clanwolf");
	// }

	// @FXML
	// private void handleGoogleButtonMouseEventEnter() {
	// setStatusText(Internationalization.getString("app_web_googleGroup"), false);
	// }

	// @FXML
	// private void handleSourceForgeButtonMouseEventClick() {
	// Tools.startBrowser("https://sourceforge.net/projects/battleforge");
	// }

	// @FXML
	// private void handleSourceForgeButtonMouseEventEnter() {
	// setStatusText(Internationalization.getString("app_web_sourceForge"), false);
	// }

	// @FXML
	// private void handleHelpButtonMouseEventClick() {
	// String spokenMessage = Internationalization.getString("C3_Speech_HelpPage");
	// setStatusText(Internationalization.getString("C3_Speech_HelpPage").replace("%20", " ") + ".", false);
	// openTargetPane(infoPane, spokenMessage);
	//
	// // Tools.startBrowser("http://www.clanwolf.net/apps/C3/help/index.php");
	// }

	// @FXML
	// private void handleHelpButtonMouseEventEnter() {
	// setStatusText(Internationalization.getString("app_web_help"), false);
	// }

	// @FXML
	// private void handleBugZillaButtonMouseEventClick() {
	// Tools.startBrowser(C3Properties.getProperty(C3PROPS.BUGTRACKING_URL));
	// }

	// @FXML
	// private void handleBugZillaButtonMouseEventEnter() {
	// setStatusText(Internationalization.getString("app_web_bugzilla"), false);
	// }

	@FXML
	private void handleLanguageButtonMouseEventClick() {
		if (enableLanguageSwitch) {
			if ("de".equals(Internationalization.getLanguage())) {
				Internationalization.setLocale(Internationalization.ENGLISH);
				C3Properties.setProperty(C3PROPS.LANGUAGE, "en", true);
				ActionManager.getAction(ACTIONS.CHANGE_LANGUAGE).execute(Internationalization.ENGLISH);
			} else {
				Internationalization.setLocale(Internationalization.GERMAN);
				C3Properties.setProperty(C3PROPS.LANGUAGE, "de", true);
				ActionManager.getAction(ACTIONS.CHANGE_LANGUAGE).execute(Internationalization.GERMAN);
			}
			C3Logger.info("Language changed. Switched to: " + Internationalization.getLanguage());
			languageButton.getStyleClass().remove("languageButton_hover");
			languageButton.getStyleClass().remove("languageButton_de");
			languageButton.getStyleClass().remove("languageButton_en");
			languageButton.getStyleClass().add("languageButton_" + C3Properties.getProperty(C3PROPS.LANGUAGE));
			setStatusText(Internationalization.getString("app_language_switched"), false);
			Tools.playButtonClickSound();
			C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_language_was_changed"));
		}
		enableLanguageSwitch = false;
	}

	// ONLINE INDICATOR LABEL
	// Mouse entered
	@FXML
	private void handleOnlineIndicatorLabelMouseEventEnter() {
		switch (C3Properties.getProperty(C3PROPS.CHECK_ONLINE_STATUS)) {
			case "NOT_STARTED" -> setStatusText(Internationalization.getString("app_online_indicator_message_NOT_STARTED"), false);
			case "RUNNING_CHECK" -> setStatusText(Internationalization.getString("app_online_indicator_message_RUNNING_CHECK"), false);
			case "ONLINE" -> setStatusText(Internationalization.getString("app_online_indicator_message_ONLINE"), false);
			case "OFFLINE" -> setStatusText(Internationalization.getString("app_online_indicator_message_OFFLINE"), false);
		}
	}

	// Mouse exited
	@FXML
	private void handleOnlineIndicatorLabelMouseEventExit() {
		setStatusText("", false);
	}

	@FXML
	private void handleOnlineIndicatorLabelMouseEventClick() {
		Server.checkServerStatusTask();
		switch (C3Properties.getProperty(C3PROPS.CHECK_ONLINE_STATUS)) {
			case "ONLINE":
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Server_Status_Online"));
				break;
			case "OFFLINE":
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Server_Status_Offline"));
				break;
		}
	}
	// Mouse entered

	@FXML
	private void handleDatabaseAccessibleIndicatorLabelMouseEventEnter() {
		switch (C3Properties.getProperty(C3PROPS.CHECK_CONNECTION_STATUS)) {
			case "NOT_STARTED" -> setStatusText(Internationalization.getString("app_database_indicator_message_NOT_STARTED"), false);
			case "RUNNING_CHECK" -> setStatusText(Internationalization.getString("app_database_indicator_message_RUNNING_CHECK"), false);
			case "ONLINE" -> setStatusText(Internationalization.getString("app_database_indicator_message_ONLINE"), false);
			case "OFFLINE" -> setStatusText(Internationalization.getString("app_database_indicator_message_OFFLINE"), false);
		}
	}

	@FXML
	private void handleDatabaseAccessibleIndicatorLabelMouseEventExit() {
		setStatusText("", false);
	}

	@FXML
	private void handleLoginIndicatorLabelMouseEventEnter() {
		switch (C3Properties.getProperty(C3PROPS.CHECK_LOGIN_STATUS)) {
			case "LOGGED_OFF":
				setStatusText(Internationalization.getString("app_login_indicator_message_LOGGED_OFF"), false);
				break;
			case "LOGGED_ON":
				setStatusText(Internationalization.getString("app_login_indicator_message_LOGGED_ON"), false);
				break;
			case "LOGON_RUNNING":
				setStatusText(Internationalization.getString("app_login_indicator_message_LOGON_RUNNING"), false);
				break;
		}
	}
	// Mouse exited

	@FXML
	private void handleLoginIndicatorLabelMouseEventExit() {
		setStatusText("", false);
	}

	@FXML
	private void handleTopRightExitLabel() {
		// C3Logger.info("Application close requested by user.");
		AbstractC3Pane targetPane;
		String spokenMessage = Internationalization.getString("C3_Speech_close_warning");
		setStatusText(Internationalization.getString("C3_Speech_close_warning").replace("%20", " ") + ".", false);
		targetPane = confirmAppClosePane;
		if (!adminMenuActive) {
			showMenuIndicator(true);
		}
		menuIndicatorPos = 376;
		moveMenuIndicator(menuIndicatorPos);
		adminPaneOpen = false;
		openTargetPane(targetPane, spokenMessage);
	}

	private void showMenuIndicator(boolean show) {
		hudinfo1.setVisible(show);
	}

	private void moveMenuIndicator(int pos) {
		if (!adminMenuActive) {
			Platform.runLater(() -> {
//				C3Logger.info("Moving menu indicator to: " + pos);
				hudinfo1.setOpacity(1.0);
				hudinfo1.setCache(true);
				hudinfo1.setCacheHint(CacheHint.SPEED);
				KeyValue key1 = new KeyValue(hudinfo1.translateYProperty(), pos);
				Timeline timeline = new Timeline();
				KeyFrame frame1 = new KeyFrame(Duration.millis(150), key1);
				timeline.getKeyFrames().addAll(frame1);
				timeline.play();
				timeline.setOnFinished(event -> hudinfo1.setOpacity(0.7));
			});
		}
	}

	// -------------------------------------------------------------------------
	//
	// Buttons handler
	//
	// -------------------------------------------------------------------------
	// handle toolbarbutton: Labeling around as test
	@FXML
	private void handleMenuButtonAction(ActionEvent event) {
		Button bn = null;
		if (event.getSource() instanceof Button) {
			bn = (Button) event.getSource();
		}
		AbstractC3Pane targetPane = null;
		String spokenMessage = "";

		int menuIndicatorPosOld = menuIndicatorPos;

		if (bn != null) {
			// LOGIN / USERINFO
			if (bn.equals(userButton)) {
				if (!Nexus.isLoggedIn()) {
					C3Logger.info("Login opened by user.");
					setStatusText(Internationalization.getString("app_login_infotext").replace("%20", " ") + ".", false);
					targetPane = loginPane;
					if (!adminMenuActive) {
						showMenuIndicator(true);
					}
					menuIndicatorPos = 0;
					moveMenuIndicator(menuIndicatorPos);
					adminPaneOpen = false;
				} else {
					C3Logger.info("Open Userinfo panel for logged in user.");
					setStatusText(Internationalization.getString("app_open_userinfo").replace("%20", " ") + ".", false);
					targetPane = userInfoPane;
					if (!adminMenuActive) {
						showMenuIndicator(true);
					}
					menuIndicatorPos = 0;
					moveMenuIndicator(menuIndicatorPos);
					adminPaneOpen = false;
				}
			}
			// SETTINGS
			if (bn.equals(settingsButton)) {
				C3Logger.info("Settings opened by user.");
				setStatusText(Internationalization.getString("app_settings_infotext").replace("%20", " ") + ".", false);
				targetPane = settingsPane;
				if (!adminMenuActive) {
					showMenuIndicator(true);
				}
				menuIndicatorPos = 46;
				moveMenuIndicator(menuIndicatorPos);
				adminPaneOpen = false;
			}
			// STARMAP
			if (bn.equals(mapButton)) {
				C3Logger.info("Map opened by user.");
				setStatusText(Internationalization.getString("app_map_infotext").replace("%20", " ") + ".", false);
				targetPane = mapPane;
				if (!adminMenuActive) {
					showMenuIndicator(true);
				}
				menuIndicatorPos = 147;
				moveMenuIndicator(menuIndicatorPos);
				adminPaneOpen = false;
			}
			// ATTACK / INVASION
			if (bn.equals(attackButton)) {
				C3Logger.info("Attack on a starsystem selected.");
				setStatusText(Internationalization.getString("app_attack_infotext").replace("%20", " ") + ".", false);
				targetPane = attackPane;
				if (!adminMenuActive) {
					showMenuIndicator(true);
				}
				menuIndicatorPos = 190;
				moveMenuIndicator(menuIndicatorPos);
				adminPaneOpen = false;
			}
			// ROLEPLAY
			if (bn.equals(rolePlayButton)) {
				C3Logger.info("RolePlay opened by user.");
				setStatusText(Internationalization.getString("app_settings_infotext").replace("%20", " ") + ".", false);
				targetPane = rolePlayPane;
				if (!adminMenuActive) {
					showMenuIndicator(true);
				}
//				menuIndicatorPos = 190;
				menuIndicatorPos = 233;
				moveMenuIndicator(menuIndicatorPos);
				adminPaneOpen = false;
				ActionManager.getAction(ACTIONS.START_ROLEPLAY).execute();
			}
			// CHAT
			if (bn.equals(chatButton)) {
				C3Logger.info("Chat opened by user.");
				setStatusText(Internationalization.getString("app_chat_infotext").replace("%20", " ") + ".", false);
				targetPane = chatPane;
				if (!adminMenuActive) {
					showMenuIndicator(true);
				}
//				menuIndicatorPos = 233;
				menuIndicatorPos = 276;
				moveMenuIndicator(menuIndicatorPos);
				adminPaneOpen = false;
			}
			// LOG
			if (bn.equals(logButton)) {
				C3Logger.info("Log opened by user.");
				setStatusText(Internationalization.getString("app_log_infotext").replace("%20", " ") + ".", false);

				openLogPane = true;
			}
			// EXIT
			if (bn.equals(exitButton)) {
				// C3Logger.info("Application close requested by user.");
				spokenMessage = Internationalization.getString("C3_Speech_close_warning");
				setStatusText(Internationalization.getString("C3_Speech_close_warning").replace("%20", " ") + ".", false);
				targetPane = confirmAppClosePane;
				if (!adminMenuActive) {
					showMenuIndicator(true);
				}
				menuIndicatorPos = 376;
				moveMenuIndicator(menuIndicatorPos);
				adminPaneOpen = false;
			}

			// ADMIN BUTTONS
			// ADMINS
			if (bn.equals(adminButton)) {
				C3Logger.info("Admin menu opened by user.");
				setStatusText(Internationalization.getString("app_admin_infotext").replace("%20", " ") + ".", true);
				shiftButtonColumn();
				Tools.playButtonClickSound();
				targetPane = null;
			}
			// STORY EDITOR
			if (bn.equals(storyEditorButton)) {
				C3Logger.info("Sory editor opened by user.");
				setStatusText(Internationalization.getString("app_storyeditor_infotext").replace("%20", " ") + ".", false);
				adminPaneOpen = true;
				openEditorPane = true;
			}
			// ADMIN PANE
			if (bn.equals(adminPaneButton)) {
				C3Logger.info("Administration pane (Security) opened by user.");
				setStatusText(Internationalization.getString("app_adminpane_infotext").replace("%20", " ") + ".", false);
				adminPaneOpen = true;
				openAdministrationPane = true;
			}
		}
		if (targetPane != null) {
			AtomicBoolean success = new AtomicBoolean(openTargetPane(targetPane, spokenMessage));

			if (!success.get() && currentlyDisplayedPane != null) {
				moveMenuIndicator(menuIndicatorPosOld);
				menuIndicatorPos = menuIndicatorPosOld;
				Tools.playButtonClickSound();
			}
		} else if (!openAdministrationPane && !openEditorPane && !openLogPane) {
			C3Logger.info("TargetPane not defined!");
		}
		if (openAdministrationPane) {
			C3Logger.info("Opening administration window!");

			ArrayList<UserDTO> userListFromNexus = Nexus.getUserList();

			Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
			AdminPane ap = new AdminPane(userListFromNexus, stage, Internationalization.getLocale());
			openAdministrationPane = false;
		}

		if (openEditorPane) {
			C3Logger.info("Opening storyeditor window!");

			Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
			StoryEditorPane ep = new StoryEditorPane(stage);
			openEditorPane = false;
		}

		if (openLogPane) {
			C3Logger.info("Opening log window!");
			Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
			if (logPane == null) {
				logPane = new LogPane(stage, Internationalization.getLocale());
			} else {
				if (logPane.isVisible) {
					logPane.hide();
				} else {
					logPane.show();
				}
			}
			openLogPane = false;
		}
	}

	private void shiftButtonColumn() {
		if (buttonsAreMoving) {
			return;
		}

		double oldX1 = rolePlayButton.getLayoutX();
		double oldX2 = storyEditorButton.getLayoutX();
		int distance = 47;

		final int step;
		if (adminMenuActive) {
			 step = 1;
		} else {
			step = -1;
		}

		adminMenuActive = !adminMenuActive;

		if (adminMenuActive) {
			storyEditorButton.setVisible(true);
			adminPaneButton.setVisible(true);
			renameMeButton3.setVisible(true);
			renameMeButton4.setVisible(true);
			renameMeButton5.setVisible(true);
			showMenuIndicator(false);
		} else {
			rolePlayButton.setVisible(true);
			mapButton.setVisible(true);
			attackButton.setVisible(true);
			chatButton.setVisible(true);
//			industryButton.setVisible(true);
			logButton.setVisible(true);
			if (!adminPaneOpen && Nexus.getCurrentlyOpenedPane() != null) {
				showMenuIndicator(true);
				moveMenuIndicator(menuIndicatorPos);
			}
		}

		C3Logger.info("Adminmenu: " + adminMenuActive);

		Runnable r = () -> {
			buttonsAreMoving = true;
			if (adminMenuActive) {
				Platform.runLater(() -> adminButton.setGraphic(new ImageView(imageAdminButtonOn)));
			} else {
				Platform.runLater(() -> adminButton.setGraphic(new ImageView(imageAdminButtonOff)));
			}
			for (int i = 0; i < distance; i++) {

				double newX1 = oldX1 + (i * step);
				double newX2 = oldX2 + (i * step);

				try {
					TimeUnit.MILLISECONDS.sleep(1);
				} catch (InterruptedException e) {
					//
				}

				Platform.runLater(() -> {
					// Column 1
					rolePlayButton.setLayoutX(newX1);
					mapButton.setLayoutX(newX1);
					attackButton.setLayoutX(newX1);
					chatButton.setLayoutX(newX1);
//					industryButton.setLayoutX(newX1);
					logButton.setLayoutX(newX1);

					// Column 2
					storyEditorButton.setLayoutX(newX2);
					adminPaneButton.setLayoutX(newX2);
					renameMeButton3.setLayoutX(newX2);
					renameMeButton4.setLayoutX(newX2);
					renameMeButton5.setLayoutX(newX2);
				});
			}
			buttonsAreMoving = false;
			if (adminMenuActive) {
				Platform.runLater(() -> {
					rolePlayButton.setVisible(false);
					mapButton.setVisible(false);
					attackButton.setVisible(false);
					chatButton.setVisible(false);
					//				industryButton.setVisible(false);
					logButton.setVisible(false);
				});
			} else {
				Platform.runLater(() -> {
					storyEditorButton.setVisible(false);
					adminPaneButton.setVisible(false);
					renameMeButton3.setVisible(false);
					renameMeButton4.setVisible(false);
					renameMeButton5.setVisible(false);
				});
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	private void setStatusText(String t, boolean flash) {
		setStatusText(t, flash, "");
	}

	private void setStatusText(String t, boolean flash, String color) {
		if (fadeTransition_flash == null) {
			fadeTransition_flash = new FadeTransition(Duration.millis(200), statuslabel);
			fadeTransition_flash.setFromValue(1.0);
			fadeTransition_flash.setToValue(0.0);
			fadeTransition_flash.setAutoReverse(true);
			fadeTransition_flash.setCycleCount(FadeTransition.INDEFINITE);
		}

		if (fadeTransition_fadein == null) {
			fadeTransition_fadein = new FadeTransition(Duration.millis(100), statuslabel);
			fadeTransition_fadein.setFromValue(0.0);
			fadeTransition_fadein.setToValue(1.0);
			fadeTransition_fadein.setAutoReverse(true);
			fadeTransition_fadein.setCycleCount(3);
		}

		statuslabel.setText(t);
		if (!"".equals(color)) {
			statuslabel.setTextFill(Color.web(color));
		}

		if (flash) {
			if ("".equals(color)) {
				statuslabel.setTextFill(Color.web("#ff8080"));
			}
			fadeTransition_flash.play();
		} else {
			if ("".equals(color)) {
				statuslabel.setTextFill(Color.web("#667288"));
			}
			fadeTransition_flash.stop();
			fadeTransition_fadein.play();
		}
	}

	private boolean openTargetPane(AbstractC3Pane targetPane, String sm) {

		AtomicBoolean changedPane = new AtomicBoolean(false);

		if (currentlyDisplayedPane != null) {
			// there is a pane showing already
			if (!currentlyDisplayedPane.isModal()) {
				// There is another dialog open, so this needs to be closed
				// first
				if (currentlyDisplayedPane == targetPane) {
					// the pane is open already, do nothing
					setStatusText(Internationalization.getString("app_already_open_warning"), false);
				} else {
					// it is a different pane to be opened
					currentlyDisplayedPane.paneDestruction();
					nextToDisplayPane = targetPane;
					changedPane.set(true);

					if (!"".equals(sm)) {
						C3SoundPlayer.getTTSFile(sm);
					}
				}
			} else {
				if (currentlyDisplayedPane == targetPane) {
					// the pane is open already, do nothing
					setStatusText(Internationalization.getString("app_already_open_warning"), false);
				} else {
					// Current pane is modal, so nothing to do here
					// the pane can be closed by a button on the pane
					setStatusText(Internationalization.getString("app_modal_warning"), true);
					C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Warning"));
					Tools.playAttentionSound();
				}
			}
		} else {
			// there is no pane open yet
			// nothing there, fade in
			Platform.runLater(() -> {
				if (!rootAnchorPane.getChildren().contains(targetPane)) {
					rootAnchorPane.getChildren().add(targetPane);
					targetPane.paneCreation();
					changedPane.set(true);
					if (!"".equals(sm)) {
						C3SoundPlayer.getTTSFile(sm);
					}
				}
			});
		}

		return changedPane.get();
	}

	private void createNextPane() {
		if (!rootAnchorPane.getChildren().contains(nextToDisplayPane)) {
			rootAnchorPane.getChildren().add(nextToDisplayPane);
		}
		nextToDisplayPane.paneCreation();
		nextToDisplayPane = null;
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.ONLINECHECK_STARTED, this);
		ActionManager.addActionCallbackListener(ACTIONS.ONLINECHECK_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGGING_OFF, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGGED_OFF_COMPLETE, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGGED_OFF_AFTER_DOUBLE_LOGIN_COMPLETE, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGGED_ON, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGON_RUNNING, this);

		ActionManager.addActionCallbackListener(ACTIONS.LOGON_FINISHED_SUCCESSFULL, this);

		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_BEGINS, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_DESTRUCTION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_DESTROY_CURRENT, this);
		ActionManager.addActionCallbackListener(ACTIONS.APPLICATION_EXIT_REQUEST, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_NORMAL, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_WAIT, this);
		ActionManager.addActionCallbackListener(ACTIONS.SET_STATUS_TEXT, this);
		ActionManager.addActionCallbackListener(ACTIONS.APPLICATION_STARTUP, this);
		ActionManager.addActionCallbackListener(ACTIONS.START_SPEECH_SPECTRUM, this);
		ActionManager.addActionCallbackListener(ACTIONS.STOP_SPEECH_SPECTRUM, this);
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.NOISE, this);
		ActionManager.addActionCallbackListener(ACTIONS.SET_CONSOLE_OPACITY, this);
		ActionManager.addActionCallbackListener(ACTIONS.SET_CONSOLE_OUTPUT_LINE, this);
		ActionManager.addActionCallbackListener(ACTIONS.DATABASECONNECTIONCHECK_STARTED, this);
		ActionManager.addActionCallbackListener(ACTIONS.DATABASECONNECTIONCHECK_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.READY_TO_LOGIN, this);
		ActionManager.addActionCallbackListener(ACTIONS.SHOW_MESSAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.SHOW_MESSAGE_WAS_ANSWERED, this);
		ActionManager.addActionCallbackListener(ACTIONS.START_ROLEPLAY, this);
		ActionManager.addActionCallbackListener(ACTIONS.UPDATE_GAME_INFO, this);
		ActionManager.addActionCallbackListener(ACTIONS.SHOW_MEDAL, this);
		ActionManager.addActionCallbackListener(ACTIONS.SET_TERMINAL_TEXT, this);
//		ActionManager.addActionCallbackListener(ACTIONS.NEW_UNIVERSE_RECEIVED, this);
		ActionManager.addActionCallbackListener(ACTIONS.HIDE_IRC_INDICATOR, this);
		ActionManager.addActionCallbackListener(ACTIONS.SHOW_IRC_INDICATOR, this);
		ActionManager.addActionCallbackListener(ACTIONS.ENABLE_MAIN_MENU_BUTTONS, this);
		ActionManager.addActionCallbackListener(ACTIONS.SWITCH_TO_INVASION, this);
		ActionManager.addActionCallbackListener(ACTIONS.TERMINAL_COMMAND, this);
	}

	private void setToLevelLoggedOutText() {
		final String[] topTexts = {"1// Communicate", "", "2// Command", "", "3// Control", ""};
		Runnable r = () -> {
			int i = 0;
			while (!Nexus.isLoggedIn()) {
				if (i == topTexts.length) {
					i = 0;
				}
				final int ii = i;
				Platform.runLater(() -> toplabel.setText(topTexts[ii]));
				try {
					if ("".equals(topTexts[i])) {
						TimeUnit.SECONDS.sleep(1);
					} else {
						TimeUnit.SECONDS.sleep(5);
					}
				} catch (InterruptedException e) {
					//
				}
				i++;
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	/**
	 * @param url url
	 * @param rb resource bundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		setToLevelLoggedOutText();

		Runnable rCursor = () -> {
			//noinspection InfiniteLoopStatement
			while (true) {
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Platform.runLater(() -> systemConsoleCursor.setText("▂"));
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Platform.runLater(() -> systemConsoleCursor.setText(" "));
			}
		};
		Thread t1 = new Thread(rCursor);
		t1.start();

		Platform.runLater(() -> adminButton.setGraphic(new ImageView(imageAdminButtonOff)));
		adminButton.setDisable(true);

		int year = Calendar.getInstance().get(Calendar.YEAR);
		hudinfo1.setOpacity(0.7);
		copyrightLabel.setText("©2000-" + year + " - clanwolf.net");
		spectrumImage.setOpacity(0.1);
		spectrumImage.setVisible(false);
		noiseImage.setOpacity(0.0);
		noiseImage.setVisible(false);
		noiseImage.toFront();

		Image helpImg = new Image(getClass().getResourceAsStream("/images/buttons/help.png"));
		ImageView view = new ImageView(helpImg);
		view.setFitHeight(16);
		view.setPreserveRatio(true);
		helpLabel.setGraphic(view);
		helpLabel.setText("");

		enableMainMenuButtons(Nexus.isLoggedIn(), Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN));

		String versionString = "Version: " + Tools.getVersionNumber();
		versionLabel.setText(versionString);

		onlineIndicatorLabel.setStyle("-fx-background-color: #808000;");
		loginIndicatorLabel.setStyle("-fx-background-color: #c00000;");
		databaseAccessibleIndicatorLabel.setStyle("-fx-background-color: #808000;");

		C3Properties.setProperty(C3PROPS.CHECK_ONLINE_STATUS, "NOT_STARTED", false);
		C3Properties.setProperty(C3PROPS.CHECK_LOGIN_STATUS, "LOGGED_OFF", false);

		languageButton.getStyleClass().add("languageButton_" + Internationalization.getLanguage());

		// ------------------------------------------------------------------------------------------
		// The ActionCallBackListeners are added here, because otherwise they get lost in the
		// constructors

		loginPane = new LoginPane();
		loginPane.setCache(true);
		loginPane.setCacheHint(CacheHint.SPEED);
		loginPane.getController().addActionCallBackListeners();

		settingsPane = new SettingsPane();
		settingsPane.setCache(true);
		settingsPane.setCacheHint(CacheHint.SPEED);
		settingsPane.getController().addActionCallBackListeners();

		confirmAppClosePane = new ConfirmAppClosePane();
		confirmAppClosePane.setCache(true);
		confirmAppClosePane.setCacheHint(CacheHint.SPEED);
		confirmAppClosePane.getController().addActionCallBackListeners();

		confirmAppClosePane = new ConfirmAppClosePane();
		confirmAppClosePane.setCache(true);
		confirmAppClosePane.setCacheHint(CacheHint.SPEED);
		confirmAppClosePane.getController().addActionCallBackListeners();

		mapPane = new MapPane();
		mapPane.setShowsMouseFollow(false);
		mapPane.setShowsPlanetRotation(false);
		mapPane.setCacheHint(CacheHint.SPEED);
		mapPane.getController().addActionCallBackListeners();

		chatPane = new ChatPane();
		chatPane.setShowsMouseFollow(false);
		chatPane.setShowsPlanetRotation(false);
		chatPane.setCacheHint(CacheHint.SPEED);
		chatPane.getController().addActionCallBackListeners();

		String paneNameCharacter = "CharacterPane";
		rolePlayPane = new RolePlayBasicPane(paneNameCharacter);
		rolePlayPane.setShowsMouseFollow(false);
		rolePlayPane.setShowsPlanetRotation(false);
		rolePlayPane.setCache(true);
		rolePlayPane.setCacheHint(CacheHint.SPEED);
		rolePlayPane.getController().addActionCallBackListeners();
		rolePlayPane.getController().setPaneName(paneNameCharacter);
		C3Logger.debug("RolePlayPane: " + rolePlayPane + " -> Controller: " + rolePlayPane.getController());

		String paneNameAttack = "AttackPane";
		attackPane = new RolePlayBasicPane(paneNameAttack);
		attackPane.setShowsMouseFollow(false);
		attackPane.setShowsPlanetRotation(false);
		attackPane.setCache(true);
		attackPane.setCacheHint(CacheHint.SPEED);
		attackPane.getController().addActionCallBackListeners();
		attackPane.getController().setPaneName(paneNameAttack);
		C3Logger.debug("AttackPane: " + attackPane + " -> Controller: " + attackPane.getController());

		// infoPane = new InfoPane();
		// infoPane.getController().addActionCallBackListener();

		// ------------------------------------------------------------------------------------------

		waitAnimationPane = new WaitAnimationPane();
		waitAnimationPane.setMouseTransparent(true);
		waitAnimationPane.showCircleAnimation(false);
		waitAnimationPane.toFront();

		mouseStopper.getChildren().add(waitAnimationPane);
		mouseStopper.setMouseTransparent(true);
		mouseStopper.toFront();

		rolePlayButton.setVisible(true);
		mapButton.setVisible(true);
		attackButton.setVisible(true);
		chatButton.setVisible(true);
//		industryButton.setVisible(true);
		logButton.setVisible(true);

		storyEditorButton.setVisible(false);
		adminPaneButton.setVisible(false);
		renameMeButton3.setVisible(false);
		renameMeButton4.setVisible(false);
		renameMeButton5.setVisible(false);

		showMenuIndicator(false);

		terminalPrompt.toFront();
		addActionCallBackListeners();
	}

	/**
	 * Set Strings for GUI (on initialization and on change of language.
	 */
	@Override
	public void setStrings() {
		//
	}

	/**
	 * @param contentLine the string to be displayed
	 */
	private void setConsoleEntry(String contentLine) {
		if (exec == null) {
			exec = Executors.newSingleThreadExecutor();
		}
		if (!systemConsole.isCache()) {
			systemConsole.setCache(true);
			systemConsole.setCacheHint(CacheHint.SPEED);
		}

		Runnable r = () -> {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				//
			}
			rowCount++;
			Platform.runLater(() -> systemConsole.setText(oldContent));
			String screenNumber = ("000" + rowCount).substring((rowCount + "").length());
			StringBuilder typeString = new StringBuilder();
			try {
				for (int i = 0; i < contentLine.length(); i++) {
					char ch = contentLine.charAt(i);
					typeString.append(ch);
					final String type = typeString.toString();
					Platform.runLater(() -> {
						systemConsoleCurrentLine.setText(type + " < " + screenNumber + "#");
					});
					TimeUnit.MILLISECONDS.sleep(60);
				}
				Platform.runLater(() -> {
					systemConsoleCurrentLine.setText(contentLine + " < " + screenNumber + "#");
				});
			} catch (InterruptedException e) {
				//
			}
			if (rowCount > 8) {
				oldContent = oldContent.substring(oldContent.indexOf("#") + 1);
			}
			oldContent = oldContent + contentLine + " < " + screenNumber + "#" + "\r\n";
		};
		exec.execute(r);
	}

	/**
	 * Here all the actions go in that need to be performed once the client is started up and showing
	 */
	private void startup() {
		SpeechSpectrumAnimation();
		NoiseAnimation();

		noiseImage.toFront();
		noiseImage.setVisible(true);
		C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_welcome_message"));
		C3SoundPlayer.play("/sound/fx/beep_02.mp3", false);
		C3SoundPlayer.startMusic();

		String tcphostname = C3Properties.getProperty(C3PROPS.TCP_HOSTNAME);
		int tcpPort = Integer.parseInt(C3Properties.getProperty(C3PROPS.TCP_PORT));

		// Start has been executed, app is running
		setConsoleEntry("Initializing security context [encrypting]");
		setConsoleEntry("Accessing");
		setConsoleEntry("Access granted! [CBGGE88776]");
		setConsoleEntry("Initializing C3-Network");
		setConsoleEntry("Connecting to " + tcphostname + ":" + tcpPort + "...");

		Server.checkServerStatusTask();
		Server.checkDatabaseConnectionTask();

		setConsoleEntry("Setting controll elements");

		enableMainMenuButtons(Nexus.isLoggedIn(), Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN));

		C3SoundPlayer.getSamples();
	}

	private void NoiseAnimation() {
		int COLUMNS = 2;
		int COUNT = 4;
		int OFFSET_X = 0;
		int OFFSET_Y = 0;
		int WIDTH = 860;
		int HEIGHT = 524;

		InputStream is = this.getClass().getResourceAsStream("/images/noise/noisemap.png");
		noiseImage.setImage(new Image(is));
		noiseImage.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
		noiseImage.setCache(true);
		noiseImage.setCacheHint(CacheHint.SPEED);
		noiseAnimation = new SpriteAnimation(noiseImage, Duration.millis(200), COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
		noiseAnimation.setCycleCount(Animation.INDEFINITE);
	}

	private void SpeechSpectrumAnimation() {
		if (spectrumAnimation == null) {
			int COLUMNS = 3;
			int COUNT = 21;
			int OFFSET_X = 0;
			int OFFSET_Y = 0;
			int WIDTH = 247;
			int HEIGHT = 63;

			InputStream is = this.getClass().getResourceAsStream("/images/spectrum/spectrum.png");
			spectrumImage.setImage(new Image(is));
			spectrumImage.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
			spectrumImage.setCache(true);
			spectrumImage.setCacheHint(CacheHint.QUALITY);
			spectrumAnimation = new SpriteAnimation(spectrumImage, Duration.millis(1000), COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
			spectrumAnimation.setCycleCount(Animation.INDEFINITE);
		}
	}

	private void enableMainMenuButtons(boolean loggedOn, boolean admin) {
		// always enabled
		userButton.setDisable(false);
		exitButton.setDisable(false);
		settingsButton.setDisable(false);

		if (loggedOn) {
			// Column 1
			rolePlayButton.setDisable(Nexus.getCurrentChar().getStory() == null);
			mapButton.setDisable(false);
			attackButton.setDisable(!Nexus.userHasAttack());
			chatButton.setDisable(false);
			logButton.setDisable(false);

			// Column 2
			if (admin) {
				adminButton.setDisable(false);

				storyEditorButton.setDisable(false);
				adminPaneButton.setDisable(false);
				renameMeButton3.setDisable(true);
				renameMeButton4.setDisable(true);
				renameMeButton5.setDisable(true);
			} else {
				storyEditorButton.setDisable(true);
				adminPaneButton.setDisable(true);
				renameMeButton3.setDisable(true);
				renameMeButton4.setDisable(true);
				renameMeButton5.setDisable(true);
			}
		} else {
			adminButton.setDisable(true);

			// Column 1
			rolePlayButton.setDisable(true);
			mapButton.setDisable(true);
			attackButton.setDisable(true);
			chatButton.setDisable(true);
			logButton.setDisable(false);

			// Column 2
			storyEditorButton.setDisable(true);
			adminPaneButton.setDisable(true);
			renameMeButton3.setDisable(true);
			renameMeButton4.setDisable(true);
			renameMeButton5.setDisable(true);
		}
	}

	public void handleCommand(String com) {
		if (!com.startsWith("*!!!*")) {
			if (!"".equals(com)) {
				C3Logger.info("Received command: '" + com + "'");
				Nexus.commandHistory.add(com);
				Nexus.commandHistoryIndex = Nexus.commandHistory.size();
				if (Nexus.commandHistory.size() > 50) {
					Nexus.commandHistory.remove(0);
				}
			}
		}

		if ("*!!!*historyBack".equals(com)) {
			if (Nexus.commandHistoryIndex > 0) {
				C3Logger.info("History back");
				Nexus.commandHistoryIndex--;
				String histCom = Nexus.commandHistory.get(Nexus.commandHistoryIndex);
				ActionManager.getAction(ACTIONS.SET_TERMINAL_TEXT).execute(histCom);
			}
		}

		if ("*!!!*historyForward".equals(com)) {
			if (Nexus.commandHistoryIndex < Nexus.commandHistory.size() - 1) {
				C3Logger.info("History forward");
				Nexus.commandHistoryIndex++;
				String histCom = Nexus.commandHistory.get(Nexus.commandHistoryIndex);
				ActionManager.getAction(ACTIONS.SET_TERMINAL_TEXT).execute(histCom);
			}
		}

		// ---------------------------------
		// force finalize round
		// ---------------------------------
		if (com.toLowerCase().startsWith("finalize round")) {
			if (Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN)) {
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_success"), false));
				GameState s = new GameState();
				s.setMode(GAMESTATEMODES.FORCE_FINALIZE_ROUND);
				Nexus.fireNetworkEvent(s);
				Nexus.storeCommandHistory();
			} else {
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_notallowed"), false));
				C3Message message = new C3Message();
				message.setType(C3MESSAGETYPES.CLOSE);
				message.setText(Internationalization.getString("general_notallowed"));
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
				ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
			}
		}

		// ---------------------------------
		// re-create universe
		// ---------------------------------
		if (com.toLowerCase().startsWith("create universe")) {
			if (Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN)) {
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_success"), false));
				GameState s = new GameState();
				s.setMode(GAMESTATEMODES.FORCE_NEW_UNIVERSE);
				Nexus.fireNetworkEvent(s);
				Nexus.storeCommandHistory();
			} else {
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_notallowed"), false));
				C3Message message = new C3Message();
				message.setType(C3MESSAGETYPES.CLOSE);
				message.setText(Internationalization.getString("general_notallowed"));
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
				ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
			}
		}
	}

	/**
	 * Handle Actions
	 *
	 * @param action action
	 * @param o action object
	 * @return boolean (always true, ignore)
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case CHANGE_LANGUAGE:
				if (currentlyDisplayedPane != null) {
					ActionManager.getAction(ACTIONS.NOISE).execute();
				}
				break;

			case READY_TO_LOGIN:
				boolean va = (boolean) o.getObject();
				if (va) {
					loginIndicatorLabel.setStyle("-fx-background-color: #008000;");
				} else {
					loginIndicatorLabel.setStyle("-fx-background-color: #c00000;");
				}
				break;

			case SET_STATUS_TEXT:
				String color = "";
				StatusTextEntryActionObject ste = (StatusTextEntryActionObject) o.getObject();
				if (ste.getColor() != null && !"".equals(ste.getColor())) {
					switch (ste.getColor()) {
						case "GREEN":
							color = "#00ff00";
							break;
						case "BLUE":
							color = "#0000ff";
							break;
						case "YELLOW":
							color = "#ffff00";
							break;
					}
				}
				setStatusText(ste.getMessage(), ste.isFlash(), color);

				break;

			case ONLINECHECK_STARTED:
				ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute();
				// Log.debug("ACTIONS.ONLINE_STATUS_CHECK_STARTED catched.");
				onlineIndicatorLabel.setStyle("-fx-background-color: #808000;");
				C3Properties.setProperty(C3PROPS.CHECK_ONLINE_STATUS, "RUNNING_CHECK", false);
				break;

			case ONLINECHECK_FINISHED:
				boolean result = (boolean) o.getObject();
				if (result) {
					onlineIndicatorLabel.setStyle("-fx-background-color: #008000;");
					C3Properties.setProperty(C3PROPS.CHECK_ONLINE_STATUS, "ONLINE", false);
					ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute();
				} else {
					onlineIndicatorLabel.setStyle("-fx-background-color: #c00000;");
					C3Properties.setProperty(C3PROPS.CHECK_ONLINE_STATUS, "OFFLINE", false);
					C3Message message = new C3Message();
					message.setType(C3MESSAGETYPES.CLOSE);
					message.setText("Server seems to be offline.");
					C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
					ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("app_online_indicator_message_OFFLINE"), true));
					ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
				}
				break;

			case DATABASECONNECTIONCHECK_STARTED:
				if (C3Properties.getProperty(C3PROPS.CHECK_ONLINE_STATUS).equals("OFFLINE")) {
					// server is not online, do not try to check the database
					C3Logger.info("Server is offline, DB is not checked!");
				} else {
					ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute();
					databaseAccessibleIndicatorLabel.setStyle("-fx-background-color: #808000;");
					C3Properties.setProperty(C3PROPS.CHECK_CONNECTION_STATUS, "RUNNING_CHECK", false);
				}
				break;

			case DATABASECONNECTIONCHECK_FINISHED:
				boolean result3 = (boolean) o.getObject();
				// Log.debug("ACTIONS.DATABASECONNECTIONCHECK_FINISHED catched. Online: " + result3);
				if (result3) {
					databaseAccessibleIndicatorLabel.setStyle("-fx-background-color: #008000;");
					C3Properties.setProperty(C3PROPS.CHECK_CONNECTION_STATUS, "ONLINE", false);
					ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute();
				} else {
					databaseAccessibleIndicatorLabel.setStyle("-fx-background-color: #c00000;");
					C3Properties.setProperty(C3PROPS.CHECK_CONNECTION_STATUS, "OFFLINE", false);
					C3Message message = new C3Message();
					message.setType(C3MESSAGETYPES.CLOSE);
					message.setText(Internationalization.getString("app_database_indicator_message_OFFLINE"));
					C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
					ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("app_database_indicator_message_OFFLINE"), true));
					ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
				}
				break;

			case LOGGING_OFF:
				Nexus.setLoggedInStatus(false);
				enableMainMenuButtons(Nexus.isLoggedIn(), false);
				break;

			case LOGGED_OFF_COMPLETE:
				loginIndicatorLabel.setStyle("-fx-background-color: #c00000;");
				C3Properties.setProperty(C3PROPS.CHECK_LOGIN_STATUS, "LOGGED_OFF", false);

				//noinspection StatementWithEmptyBody
				do {
					// wait for the pane transition to finish
				} while (paneTransitionInProgress);

				setToLevelLoggedOutText();

				if (currentlyDisplayedPane != null) {
					ActionManager.getAction(ACTIONS.PANE_DESTROY_CURRENT).execute();
				}

//				GameState state = new GameState(GAMESTATEMODES.USER_LOG_OUT);
//				Nexus.fireNetworkEvent(state);

				break;

			case LOGGED_OFF_AFTER_DOUBLE_LOGIN_COMPLETE:
				ActionManager.getAction(ACTIONS.LOGGED_OFF_COMPLETE).execute();

				// Dialog
				Platform.runLater(() -> {
					Alert alert = Tools.C3Dialog(AlertType.ERROR, Internationalization.getString("ACTIONS.LOGGED_OFF_AFTER_DOUBLE_LOGIN_COMPLETE.name"), Internationalization.getString("ACTIONS.LOGGED_OFF_AFTER_DOUBLE_LOGIN_COMPLETE.name"),
							Internationalization.getString("ACTIONS.LOGGED_OFF_AFTER_DOUBLE_LOGIN_COMPLETE.description"));
					Optional<ButtonType> r = alert.showAndWait();
					if (r.isPresent() && ButtonType.OK == r.get()) {
						C3Logger.info("Message has been confirmed.");
					}
				});
				break;

			case LOGGED_ON:
				break;

			case LOGON_FINISHED_SUCCESSFULL:
				// TODO: This may take to long and a response object may arrive before the pane exists and lead to nullpointers!
				// if the user is null here, this may cause the endless Nullpointer Exceptions that sometimes occur on/after login
				C3Logger.info("Current user is: " + Nexus.getCurrentUser() + " (Check this not to be NULL)");

				userInfoPane = new UserInfoPane();
				userInfoPane.setCache(true);
				userInfoPane.setCacheHint(CacheHint.SPEED);
				userInfoPane.getController().addActionCallBackListeners();

				ActionManager.getAction(ACTIONS.CHANGE_LANGUAGE).execute();
				openTargetPane(userInfoPane, Internationalization.getString("C3_Speech_Successful_Login"));

				loginIndicatorLabel.setStyle("-fx-background-color: #008000;");
				C3Properties.setProperty(C3PROPS.CHECK_LOGIN_STATUS, "LOGGED_ON", false);

				setConsoleEntry("Logged on to C3-Network");
				setConsoleEntry("Verifying privileges");
				setConsoleEntry("Applying security level");

				// Print information about the server logged in to to gui
				String tcphostname = C3Properties.getProperty(C3PROPS.TCP_HOSTNAME);
				int tcpPort = Integer.parseInt(C3Properties.getProperty(C3PROPS.TCP_PORT));

				Nexus.setLoggedInStatus(true);
				Platform.runLater(() -> {
					toplabel.setText("Con // " + tcphostname + ":" + tcpPort);
					enableMainMenuButtons(Nexus.isLoggedIn(), Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN));
					BOUniverse boUniverse = Nexus.getBoUniverse();
					gameInfoLabel.setText("S" + boUniverse.currentSeason + "/R" + boUniverse.currentRound + " * Phase " + Tools.getRomanNumber(boUniverse.currentSeasonMetaPhase) + " - " + boUniverse.currentDate);

					labelTFSProgress.setText(boUniverse.currentRound + " / 60");
					TFSProgress.setProgress(100d / 60d * boUniverse.currentRound);
					TFSProgress.setVisible(true);
					TFSProgress.toFront();
					TFSInfo.setVisible(false);
				});
				break;

			case LOGON_RUNNING:
				loginIndicatorLabel.setStyle("-fx-background-color: #808000;");
				C3Properties.setProperty(C3PROPS.CHECK_LOGIN_STATUS, "LOGON_RUNNING", false);
				break;

			case PANE_CREATION_BEGINS:
				paneTransitionInProgress = true;
				systemConsole.setOpacity(0.1);
				systemConsoleCurrentLine.setOpacity(0.1);
				spectrumImage.setOpacity(0.8);
				break;

			case PANE_CREATION_FINISHED:
				currentlyDisplayedPane = (AbstractC3Pane) o.getObject();
				ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute();
				paneTransitionInProgress = false;
				break;

			case PANE_DESTROY_CURRENT:
				paneTransitionInProgress = true;
				currentlyDisplayedPane.paneDestruction();
				break;

			case PANE_DESTRUCTION_FINISHED:
				if (o.getObject() instanceof Pane) {
					Pane p = (Pane) o.getObject();
					rootAnchorPane.getChildren().remove(p);
					currentlyDisplayedPane = null;
					if (nextToDisplayPane != null) {
						createNextPane();
					} else {
						ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute();
						systemConsole.setOpacity(0.4);
						systemConsoleCurrentLine.setOpacity(0.4);
						spectrumImage.setOpacity(0.1);

						showMenuIndicator(false);
						adminPaneOpen = false;
						Nexus.setCurrentlyOpenedPane(null);
					}
					paneTransitionInProgress = false;
				}
				break;

			case SET_CONSOLE_OPACITY:
				double v = (double) o.getObject();
				systemConsole.setOpacity(v);
				systemConsoleCurrentLine.setOpacity(v);
				break;

			case SET_CONSOLE_OUTPUT_LINE:
				String s = (String) o.getObject();
				setConsoleEntry(s);
				break;

			case TERMINAL_COMMAND:
				String com = o.getText();
				// TODO: Command category "general" --> not connected to a pane
				if (Nexus.isLoggedIn()) {
					if (com.contains("finalize round")
					|| com.contains("create universe")) {
						handleCommand(com);
					}
				}
				break;

			case NOISE:
				Platform.runLater(() -> {
					int duration;
					if ((o != null) && (o.getObject() instanceof Integer)) {
						duration = (Integer) o.getObject();
					} else {
						Random random = new Random();
						duration = random.nextInt(1000 - 100) + 100;
					}

					noiseImage.toFront();
					noiseImage.setVisible(true);
					noiseAnimation.play();

					FadeTransition FadeOutTransition = new FadeTransition(Duration.millis(duration), noiseImage);
					FadeOutTransition.setFromValue(0.4);
					FadeOutTransition.setToValue(0.0);
					FadeOutTransition.setCycleCount(1);
					FadeOutTransition.setOnFinished(event -> {
						noiseAnimation.stop();
						noiseImage.setVisible(false);
					});

					FadeTransition FadeInTransition = new FadeTransition(Duration.millis(duration), noiseImage);
					FadeInTransition.setFromValue(0.0);
					FadeInTransition.setToValue(0.4);
					FadeInTransition.setCycleCount(1);
					FadeInTransition.setOnFinished(event -> FadeOutTransition.play());

					FadeInTransition.play();
				});
				break;

			case START_SPEECH_SPECTRUM:
				spectrumImage.setVisible(true);
				spectrumAnimation.play();
				break;

			case STOP_SPEECH_SPECTRUM:
				spectrumImage.setVisible(false);
				spectrumAnimation.stop();
				break;

//			case APPLICATION_EXIT:
//				break;

			case APPLICATION_STARTUP:
				startup();
				break;

			case APPLICATION_EXIT_REQUEST:
				openTargetPane(confirmAppClosePane, Internationalization.getString("C3_Speech_close_warning"));
				break;

			case CURSOR_REQUEST_NORMAL:
				Nexus.setMainFrameEnabled(true);
				Platform.runLater(() -> {
					mouseStopper.toFront();
					waitAnimationPane.showCircleAnimation(false);
					mouseStopper.setMouseTransparent(true);
				});
				ActionManager.getAction(ACTIONS.ACTION_SUCCESSFULLY_EXECUTED).execute(o);
				break;

			case CURSOR_REQUEST_WAIT:
				Nexus.setMainFrameEnabled(false);
				Platform.runLater(() -> {
					mouseStopper.toFront();
					waitAnimationPane.showCircleAnimation(true);
					mouseStopper.setMouseTransparent(false);
				});
				break;

			case SHOW_MESSAGE:
				if ((o != null) && (o.getObject() instanceof C3Message)) {
					C3Message message = (C3Message) o.getObject();
					showMessage(message);
				}
				break;

			case SHOW_MEDAL:
				if ((o != null) && (o.getObject() instanceof MEDALS)) {
					Integer id = ((MEDALS)o.getObject()).getId();
					String imageName = ((MEDALS)o.getObject()).toString();
					String desc = Internationalization.getString("MEDALS_" + imageName + "_desc");
					Image med = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/gui/rewards/" + imageName + ".png")));
					showMedal(med, desc);
				}
				break;

			case SET_TERMINAL_TEXT:
				String commandFromHistory = o.getText();
				Platform.runLater(() -> {
					terminalPrompt.setText(commandFromHistory);
					terminalPrompt.positionCaret(commandFromHistory.length());
				});
				break;

			case SHOW_MESSAGE_WAS_ANSWERED:
				if ((o != null) && (o.getObject() instanceof C3Message)) {
					C3Message message = (C3Message) o.getObject();
					closeMessage(message);
				}
				break;

			case UPDATE_GAME_INFO:
				Platform.runLater(() -> {
					BOUniverse boUniverse = Nexus.getBoUniverse();
					gameInfoLabel.setText("S" + boUniverse.currentSeason + "/R" + boUniverse.currentRound + " * Phase " + Tools.getRomanNumber(boUniverse.currentSeasonMetaPhase) + " - " + boUniverse.currentDate);
				});
				break;

			case HIDE_IRC_INDICATOR:
				ircIndicator.setVisible(false);
				break;

			case SHOW_IRC_INDICATOR:
				if (!currentlyDisplayedPane.getPaneName().equals("ChatPane")) {
					if (!ircIndicator.isVisible()) {
						ircIndicator.setVisible(true);
						C3SoundPlayer.play("sound/fx/beep_electric_3.mp3", false);
					}
				}
				break;

			case ENABLE_MAIN_MENU_BUTTONS:
				enableMainMenuButtons(Nexus.isLoggedIn(), Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN));
				break;

			case SWITCH_TO_INVASION:
				openTargetPane(attackPane, "");
				if (!adminMenuActive) {
					showMenuIndicator(true);
				}
				menuIndicatorPos = 190;
				moveMenuIndicator(menuIndicatorPos);

				attackButton.setDisable(false);
				break;

//			case START_ROLEPLAY:
//				break;
//			case LOGINCHECK_STARTED:
//				break;
//			case LOGINCHECK_FINISHED:
//				break;
//			case LOGON_FINISHED_WITH_ERROR:
//				break;
//			case MOUSE_MOVED:
//				break;
//			case MUSIC_SELECTION_CHANGED:
//				break;
//			case SAVE_ROLEPLAY_STORY_OK:
//				break;
//			case SAVE_ROLEPLAY_STORY_ERR:
//				break;
//			case DELETE_ROLEPLAY_STORY_OK:
//				break;
//			case DELETE_ROLEPLAY_STORY_ERR:
//				break;
//			case GET_ROLEPLAY_ALLSTORIES:
//				break;
//			case GET_ROLEPLAY_ALLCHARACTER:
//				break;
//			case ROLEPLAY_NEXT_STEP:
//				break;
//			case ROLEPLAY_NEXT_STEP_CHANGE_PANE:
//				break;
//			case ACTION_SUCCESSFULLY_EXECUTED:
//				break;
//			case CURSOR_REQUEST_NORMAL_MESSAGE:
//				break;
//			case CURSOR_REQUEST_WAIT_MESSAGE:
//				break;
			case NEW_UNIVERSE_RECEIVED:
				C3Logger.info("Do something with the new UNIVERSE (here)!!!");
				break;

			default:
				break;
		}
		return true;
	}

	private void showMessage(C3Message message) {
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute();

		messagePane = new C3MessagePane(message);
		Platform.runLater(() -> {
			Tools.playGUICreationSound();
			mouseStopper.getChildren().add(messagePane);
			messagePane.fadeIn();
		});
	}

	private void showMedal(Image image, String desc) {
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute();

		medalPane = new C3MedalPane(image, desc);
		Platform.runLater(() -> {
			Tools.playGUICreationSound();
			mouseStopper.getChildren().add(medalPane);
			medalPane.fadeIn();
		});
	}

	private void closeMessage(C3Message message) {
		C3MESSAGERESULTS userReactionResult = message.getResult();







		C3Logger.info("ERROR: " + userReactionResult);
		C3Logger.info("OUT: " + userReactionResult);

		Platform.runLater(() -> mouseStopper.getChildren().remove(messagePane));

		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute();
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
