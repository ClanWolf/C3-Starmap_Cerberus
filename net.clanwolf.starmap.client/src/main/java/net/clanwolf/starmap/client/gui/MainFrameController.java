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
package net.clanwolf.starmap.client.gui;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import net.clanwolf.starmap.client.action.*;
import net.clanwolf.starmap.client.enums.C3MESSAGERESULTS;
import net.clanwolf.starmap.client.enums.C3MESSAGES;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;
import net.clanwolf.starmap.client.gui.panes.TerminalCommandHandler;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.DiplomacyDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.enums.PRIVILEGES;
import net.clanwolf.starmap.client.gui.messagepanes.C3Message;
import net.clanwolf.starmap.client.gui.messagepanes.C3MessagePane;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Pane;
import net.clanwolf.starmap.client.gui.panes.WaitAnimationPane;
import net.clanwolf.starmap.client.gui.panes.chat.ChatPane;
import net.clanwolf.starmap.client.gui.panes.confirmAppClose.ConfirmAppClosePane;
import net.clanwolf.starmap.client.gui.panes.dice.DicePane;
import net.clanwolf.starmap.client.gui.panes.diplomacy.DiplomacyPane;
import net.clanwolf.starmap.client.gui.panes.logging.LogPane;
import net.clanwolf.starmap.client.gui.panes.login.LoginPane;
import net.clanwolf.starmap.client.gui.panes.map.starmap.StarmapPane;
import net.clanwolf.starmap.client.gui.panes.rp.RolePlayBasicPane;
import net.clanwolf.starmap.client.gui.panes.rp.StoryEditorPane;
import net.clanwolf.starmap.client.gui.panes.usereditor.UsereditorPane;
import net.clanwolf.starmap.client.gui.panes.settings.SettingsPane;
import net.clanwolf.starmap.client.gui.panes.userinfo.UserInfoPane;
import net.clanwolf.starmap.client.gui.popuppanes.C3MedalPane;
import net.clanwolf.starmap.client.gui.popuppanes.C3PopupPane;
import net.clanwolf.starmap.client.net.Server;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.logout.Logout;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.*;
import net.clanwolf.starmap.transfer.dtos.AttackDTO;
import net.clanwolf.starmap.transfer.dtos.UserDTO;
import net.clanwolf.starmap.transfer.enums.MEDALS;
import net.clanwolf.starmap.transfer.enums.POPUPS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private boolean factionLogoVisible = false;
	private boolean enableLanguageSwitch = true;
	private boolean buttonsAreMoving = false;
	private boolean adminMenuActive = false;
	private boolean openAdministrationPane = false;
	private boolean openLogPane = false;
	private boolean openEditorPane = false;
	private static boolean adminButtonHasFocus = false;
	private static volatile boolean userlistHovered = false;
	private static volatile boolean userlistTriggerHovered = false;
	private AbstractC3Pane currentlyDisplayedPane = null;
	private AbstractC3Pane nextToDisplayPane = null;
	private LoginPane loginPane = null;
	private LogPane logPane = null;
	private UserInfoPane userInfoPane = null;
	private StarmapPane mapPane = null;
	private RolePlayBasicPane attackPane = null;
	private ChatPane chatPane = null;
	private DicePane dicePane = null;
	private DiplomacyPane diplomacyPane = null;
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
	private LinkedList<C3MessagePane> messagePaneStack = new LinkedList<>();
	private C3MedalPane medalPane = null;
	private C3PopupPane popupPane = null;
	private int menuIndicatorPos = 0;
	private boolean adminPaneOpen = false;

	private final Image imageAdminButtonOff = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/adminOff.png")));
	private final Image imageAdminButtonOn = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/adminOn.png")));
	private final Image imageAdminButtonOff_Focus = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/adminOff_focus.png")));
	private final Image imageAdminButtonOn_Focus = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/adminOn_focus.png")));
	private final Image muteButtonImageHover = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/mute_hover.png")));
	private final Image muteButtonImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/mute.png")));
	private boolean helpvoiceplayedonce = false;

//	private static int counterWaitCursor = 0; // a global counter
//	private static ReentrantLock counterWaitCursorLock = new ReentrantLock(true); // enable fairness policy

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
	@FXML
	private Label lbUserSelf;

	@FXML
	private Label labelWaitText;
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
	private Button exitButton;

	// Column 1
	@FXML
	private Button rolePlayButton;
	@FXML
	private Button languageButton;
	@FXML
	private Button mapButton;
	@FXML
	private Button diplomacyButton;
	@FXML
	private Button attackButton;
	@FXML
	private Button chatButton;

	// Column 2
	@FXML
	private Button storyEditorButton;
	@FXML
	private Button adminPaneButton;
	@FXML
	private Button renameMeButton3;
	@FXML
	private Button renameMeButton4;
	@FXML
	private Button logButton;

	@FXML
	private AnchorPane rootAnchorPane;
	@FXML
	BorderPane paneBorder;
	@FXML
	private Label systemConsole;
	@FXML
	private Label systemConsoleCurrentLine;
	@FXML
	private Label systemConsoleCursor;
	@FXML
	private ImageView spectrumImage;
//	@FXML
//	private ImageView noiseImage;
	@FXML
	private ImageView hudinfo1;
	@FXML
	private ImageView ivMWOLogo;
	@FXML
	private ImageView ivLoggedOnUserImage;
	@FXML
	private ImageView ivFactionLogo;
	@FXML
	private ImageView ivFactionLogoShield;
	@FXML
	private ImageView ivSupportButton;
	@FXML
	private Label helpLabel;
	@FXML
	private Label gameInfoLabel;
	@FXML
	private TextField terminalPrompt;
	@FXML
	private ImageView ircIndicator, ivDiplomacyIndicator;
	@FXML
	private ProgressBar TFSProgress;
	@FXML
	private VBox TFSInfo;
	@FXML
	private VBox UserHistoryInfo;
	@FXML
	private Label labelTFSProgress;

	private static String[] topTexts = {"1// Communicate", "", "2// Command", "", "3// Control", ""};
	@FXML
	private Slider slVolumeControl;
	@FXML
	private ImageView ivMuteToggle, ivCharacterImage, ivCharacterFaction;
	@FXML
	private Pane paneVolumeControl;
	@FXML
	private Pane paneWindowMoverHandle;
	@FXML
	private TableView<UserHistoryEntry> tblUserHistory;
	@FXML
	private Pane mouseStopper, paneBackgroundTerminal;

	@FXML
	private AnchorPane paneNoise;

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

//	private static void decrementCounter() {
//		counterWaitCursorLock.lock();
//		try {
//			counterWaitCursor--;
//			if (counterWaitCursor < 0) {
//				counterWaitCursor = 0;
//			}
//		} finally {
//			counterWaitCursorLock.unlock();
//		}
//	}

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

//	private static void resetCounter() {
//		counterWaitCursorLock.lock();
//		try {
//			counterWaitCursor = 0;
//		} finally {
//			counterWaitCursorLock.unlock();
//		}
//	}

//	private static void incrementCounter() {
//		counterWaitCursorLock.lock();
//		try {
//			counterWaitCursor++;
//		} finally {
//			counterWaitCursorLock.unlock();
//		}
//	}

	@FXML
	private void handleUserListEntered() {
		UserHistoryInfo.toFront();
		UserHistoryInfo.setVisible(true);
		userlistHovered = true;
	}

	@FXML
	private void handleUserListExited() {
		UserHistoryInfo.toFront();
		final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				if (userlistHovered || userlistTriggerHovered) {
					//
				} else {
					UserHistoryInfo.setVisible(false);
				}
			}
		}));
		timeline.play();
		userlistHovered = false;
	}

	@FXML
	private void handleTFSProgressEntered() {
		double tfsProgress = (100d / Nexus.getBoUniverse().maxNumberOfRoundsForSeason * Nexus.getBoUniverse().currentRound) / 100;
		String progString = Nexus.getBoUniverse().currentRound + " / " + Nexus.getBoUniverse().maxNumberOfRoundsForSeason;
		labelTFSProgress.setText(progString);
		TFSInfo.toFront();
		TFSProgress.toFront();
		TFSProgress.setProgress(tfsProgress);
		if (tfsProgress > 1) {
			TFSProgress.lookup(".bar").setStyle("-fx-background-color: -fx-box-border, #ff0000");
		}
		TFSInfo.setVisible(true);
	}

	@FXML
	private void handleTFSProgressExited() {
		double tfsProgress = (100d / Nexus.getBoUniverse().maxNumberOfRoundsForSeason * Nexus.getBoUniverse().currentRound) / 100;
		String progString = Nexus.getBoUniverse().currentRound + " / " + Nexus.getBoUniverse().maxNumberOfRoundsForSeason;
		labelTFSProgress.setText(progString);
		TFSInfo.toFront();
		TFSProgress.toFront();
		TFSProgress.setProgress(tfsProgress);
		if (tfsProgress > 1) {
			TFSProgress.lookup(".bar").setStyle("-fx-background-color: -fx-box-border, #ff0000");
		}
		TFSInfo.setVisible(false);
	}

	@FXML
	private void handleExitLabelTopRightMouseEventEnter() {
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
		//		if (!helpvoiceplayedonce) {
		//			C3SoundPlayer.getTTSFile(Internationalization.getString("app_web_help"));
		//			helpvoiceplayedonce = true;
		//		}
	}

	public static Date translateRealDateToSeasonDate(Date date, Long seasonId) {
		if (Nexus.isLoggedIn()) {
			Calendar c = Calendar.getInstance();
			c.setTime(Nexus.getBoUniverse().currentSeasonStartDate);
			int seasonStartYear = c.get(Calendar.YEAR);
			Long seasonStartDateRealYear = Nexus.getBoUniverse().currentSeasonStartDateRealYear;

			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			int diff = (int) Math.round((seasonStartYear - currentYear + (currentYear - seasonStartDateRealYear)) * 365.243); // Days in year + leap year factor

			return addDaysToDate(date, diff);
		}
		return null;
	}

	@FXML
	private void handleSupportMouseEventClick() {
		logger.info("Support clicked");
		ActionManager.getAction(ACTIONS.OPEN_SUPPORT_LIBERA).execute();
	}

	@FXML
	private void handleSupportButtonHoverEnter() {
		Image supportImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/liberapay_hover.png")));
		ivSupportButton.setImage(supportImg);
	}


	@FXML
	private void handleUserInfoEntered() {
		if (Nexus.isLoggedIn()) {
			userlistTriggerHovered = true;
			tblUserHistory.getItems().clear();
			ArrayList<UserDTO> userList = Nexus.getCurrentlyOnlineUserList();

			Iterator iter = userList.iterator();
			while (iter.hasNext()) {
				UserDTO u = (UserDTO) iter.next();
				BOFaction f = null;
				Iterator i = Nexus.getBoUniverse().factionBOs.values().iterator();
				while (i.hasNext()) {
					BOFaction fa = (BOFaction) i.next();
					if (fa.getID() == u.getCurrentCharacter().getFactionId().longValue()) {
						f = fa;
					}
				}
				String factionShortName = "";
				if (f != null) {
					factionShortName = f.getShortName();
				} else {
					factionShortName = "/";
				}

				if (Nexus.getCurrentUser().getUserId().equals(u.getUserId())) {
					Platform.runLater(() -> {
						lbUserSelf.setText(u.getUserName());
						Image charImage;
						try {
							charImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Nexus.getCurrentChar().getCharImage())));
						} catch (Exception e) {
							// image not found
							charImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/chars/no_avatar.png")));
						}
						ivCharacterImage.setImage(charImage);
						if (Nexus.getFactionLogo() != null) {
							ivCharacterFaction.setImage(Nexus.getFactionLogo());
						}

						String attackedSystem = "";
						AttackDTO a = Nexus.getCurrentOpenAttackForUser(u);
						if (a != null) {
							BOStarSystem s = Nexus.getBoUniverse().starSystemBOs.get(a.getStarSystemID());
							if (s != null) {
								attackedSystem = s.getName();
							}
							lbUserSelf.setText(u.getUserName() + "\n(" + attackedSystem + ")");
						}
					});
				} else {
					String userString = "";
					String attackedSystem = "";

					AttackDTO a = Nexus.getCurrentOpenAttackForUser(u);
					if (a != null) {
						BOStarSystem s = Nexus.getBoUniverse().starSystemBOs.get(a.getStarSystemID());
						if (s != null) {
							attackedSystem = s.getName();
						}
					}

					userString += u.getUserName();
					UserHistoryEntry entry = new UserHistoryEntry(userString, factionShortName, "", "", attackedSystem);
					tblUserHistory.getItems().add(entry);
				}
			}

			//			for (int i=1; i<50;i++) {
			//				UserHistoryEntry entry = new UserHistoryEntry("test", "CHH", "", "", "Nori");
			//				tblUserHistory.getItems().add(entry);
			//			}

			// Sort userlist
			TableColumn c1 = tblUserHistory.getColumns().get(0);
			TableColumn c2 = tblUserHistory.getColumns().get(1);
			TableColumn c3 = tblUserHistory.getColumns().get(2);
			c1.setSortable(true);
			c2.setSortable(true);
			c3.setSortable(true);
			tblUserHistory.getSortOrder().addAll(c2, c1, c3);
			c1.setSortType(TableColumn.SortType.ASCENDING);
			c2.setSortType(TableColumn.SortType.ASCENDING);
			c3.setSortType(TableColumn.SortType.ASCENDING);
			tblUserHistory.sort();

			UserHistoryInfo.toFront();
			UserHistoryInfo.setVisible(true);
		}
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
	private void handleLogButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_log_infotext"), false);
		Tools.playButtonHoverSound();
	}

	@FXML
	private void handlePersonalRPButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_personalrp_infotext"), false);
		Tools.playButtonHoverSound();
	}

	@FXML
	private void handleDiplomacyButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_diplomacy_infotext"), false);
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
			logger.info("Enter pressed on terminal. Entered command: " + enteredCommand);
			Platform.runLater(() -> {
				ActionManager.getAction(ACTIONS.TERMINAL_COMMAND).execute(enteredCommand);
				terminalPrompt.setText("");
			});
		}
	}

	@FXML
	private void rpMuteButtonClick() {
		Platform.runLater(() -> {
			slVolumeControl.setValue(0.0d);
		});
	}

	@FXML
	private void rpMuteButtonHoverEnter() {
		Platform.runLater(() -> {
			ivMuteToggle.setImage(muteButtonImageHover);
		});
	}

	@FXML
	private void rpMuteButtonHoverExit() {
		Platform.runLater(() -> {
			ivMuteToggle.setImage(muteButtonImage);
		});
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
			logger.info("Language changed. Switched to: " + Internationalization.getLanguage());
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

	@FXML
	private void handleUserInfoExited() {
		if (Nexus.isLoggedIn()) {
			userlistTriggerHovered = false;
			// tblUserHistory.getItems().clear();
			UserHistoryInfo.toFront();

			final Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					if (userlistHovered || userlistTriggerHovered) {
						//
					} else {
						UserHistoryInfo.setVisible(false);
					}
				}
			}));
			timeline.play();
		}
	}

	// Mouse exited
	@FXML
	private void handleOnlineIndicatorLabelMouseEventExit() {
		setStatusText("", false);
	}

	@FXML
	private void handleAdminButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_admin_infotext").replace("%20", " "), true);
		// Tools.playButtonHoverSound();
	}
	// Mouse entered

	@FXML
	private void handleAdminPaneButtonMouseEventEnter() {
		setStatusText(Internationalization.getString("app_adminpane_infotext").replace("%20", " "), false);
		// Tools.playButtonHoverSound();
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
				String t = Internationalization.getString("app_login_indicator_message_LOGGED_ON");
				if (Nexus.getLastServerHeartbeatTimestamp() != null) {
					t += " (HB: " + Nexus.getLastServerHeartbeatTimestamp() + ")";
				} else {
					t += " (HB: -)";
				}
				setStatusText(t, false);
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
		// logger.info("Application close requested by user.");
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

	// ONLINE INDICATOR LABEL
	// Mouse entered
	@FXML
	private void handleOnlineIndicatorLabelMouseEventEnter() {
		switch (C3Properties.getProperty(C3PROPS.CHECK_ONLINE_STATUS)) {
			case "NOT_STARTED" ->
					setStatusText(Internationalization.getString("app_online_indicator_message_NOT_STARTED"), false);
			case "RUNNING_CHECK" ->
					setStatusText(Internationalization.getString("app_online_indicator_message_RUNNING_CHECK"), false);
			case "ONLINE" ->
					setStatusText(Internationalization.getString("app_online_indicator_message_ONLINE"), false);
			case "OFFLINE" ->
					setStatusText(Internationalization.getString("app_online_indicator_message_OFFLINE"), false);
		}
	}

	@FXML
	private void handleOnlineIndicatorLabelMouseEventClick() {
		Server.checkServerStatusTask();
		switch (C3Properties.getProperty(C3PROPS.CHECK_ONLINE_STATUS)) {
			case "ONLINE" -> C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Server_Status_Online"));
			case "OFFLINE" ->
					C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Server_Status_Offline"));
		}
	}

	@FXML
	private void handleDatabaseAccessibleIndicatorLabelMouseEventEnter() {
		switch (C3Properties.getProperty(C3PROPS.CHECK_CONNECTION_STATUS)) {
			case "NOT_STARTED" ->
					setStatusText(Internationalization.getString("app_database_indicator_message_NOT_STARTED"), false);
			case "RUNNING_CHECK" ->
					setStatusText(Internationalization.getString("app_database_indicator_message_RUNNING_CHECK"), false);
			case "ONLINE" ->
					setStatusText(Internationalization.getString("app_database_indicator_message_ONLINE"), false);
			case "OFFLINE" ->
					setStatusText(Internationalization.getString("app_database_indicator_message_OFFLINE"), false);
		}
	}

	private void setStatusText(String t, boolean flash) {
		setStatusText(t, flash, "");
	}

	private void setStatusText(String t, boolean flash, String color) {
		setStatusText(t, flash, "", false);
	}

	private void setStatusText(String t, boolean flash, String color, boolean justUpdate) {
		Platform.runLater(() -> {
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
				if (justUpdate) {

				} else {
					fadeTransition_flash.stop();
					fadeTransition_fadein.play();
				}
			}
		});
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

					if (!targetPane.getPaneName().equals("RolePlayBasicPane")) {
						C3SoundPlayer.pauseRPSound();
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
		ActionManager.addActionCallbackListener(ACTIONS.SHOW_POPUP, this);
		ActionManager.addActionCallbackListener(ACTIONS.SET_TERMINAL_TEXT, this);
		ActionManager.addActionCallbackListener(ACTIONS.HIDE_IRC_INDICATOR, this);
		ActionManager.addActionCallbackListener(ACTIONS.SHOW_IRC_INDICATOR, this);
		ActionManager.addActionCallbackListener(ACTIONS.ENABLE_MAIN_MENU_BUTTONS, this);
		ActionManager.addActionCallbackListener(ACTIONS.SWITCH_TO_INVASION, this);
		ActionManager.addActionCallbackListener(ACTIONS.SWITCH_TO_MAP, this);
		ActionManager.addActionCallbackListener(ACTIONS.FLASH_MWO_LOGO_ONCE, this);
		ActionManager.addActionCallbackListener(ACTIONS.IRC_DISCONNECT_NOW, this);
		ActionManager.addActionCallbackListener(ACTIONS.SERVER_CONNECTION_LOST, this);
		ActionManager.addActionCallbackListener(ACTIONS.SET_FACTION_LOGO, this);
		ActionManager.addActionCallbackListener(ACTIONS.SET_FACTION_LOGO_INVISIBLE, this);
		ActionManager.addActionCallbackListener(ACTIONS.DIPLOMACY_STATUS_PENDING, this);
		ActionManager.addActionCallbackListener(ACTIONS.ENABLE_DEFAULT_BUTTON, this);
		ActionManager.addActionCallbackListener(ACTIONS.DISABLE_DEFAULT_BUTTON, this);
		ActionManager.addActionCallbackListener(ACTIONS.TERMINAL_COMMAND, this);
		ActionManager.addActionCallbackListener(ACTIONS.ADD_CONSOLE_LINE, this);
	}

	public static Date addDaysToDate(Date date, int daysToAdd) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, daysToAdd);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String newDateString = sdf.format(c.getTime());

		return Date.valueOf(newDateString);
	}

	@FXML
	private void handleSupportButtonHoverExit() {
		Image supportImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/liberapay.png")));
		ivSupportButton.setImage(supportImg);
	}

	private void moveMenuIndicator(int pos) {
		if (!adminMenuActive) {
			Platform.runLater(() -> {
				// logger.info("Moving menu indicator to: " + pos);
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

	@Override
	public void setFocus() {
		Platform.runLater(() -> {
			//
		});
	}

	/**
	 * Set Strings for GUI (on initialization and on change of language.
	 */
	@Override
	public void setStrings() {
		//
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
					logger.info("Login opened by user.");
					setStatusText(Internationalization.getString("app_login_infotext").replace("%20", " "), false);
					targetPane = loginPane;
					if (!adminMenuActive) {
						showMenuIndicator(true);
					}
					menuIndicatorPos = 0;
					moveMenuIndicator(menuIndicatorPos);
					adminPaneOpen = false;
				} else {
					logger.info("Open Userinfo panel for logged in user.");
					setStatusText(Internationalization.getString("app_open_userinfo").replace("%20", " "), false);
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
				logger.info("Settings opened by user.");
				setStatusText(Internationalization.getString("app_settings_infotext").replace("%20", " "), false);
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
				logger.info("Map opened by user.");
				setStatusText(Internationalization.getString("app_map_infotext").replace("%20", " "), false);
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
				logger.info("Attack on a starsystem selected.");
				setStatusText(Internationalization.getString("app_attack_infotext").replace("%20", " "), false);
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
				logger.info("RolePlay opened by user.");
				setStatusText(Internationalization.getString("app_settings_infotext").replace("%20", " "), false);
				targetPane = rolePlayPane;
				if (!adminMenuActive) {
					showMenuIndicator(true);
				}
				// menuIndicatorPos = 190;
				menuIndicatorPos = 233;
				moveMenuIndicator(menuIndicatorPos);
				adminPaneOpen = false;
				ActionManager.getAction(ACTIONS.START_ROLEPLAY).execute();
			}
			// DIPLOMACY
			if (bn.equals(diplomacyButton)) {
				logger.info("Diplomacy opened by user.");
				setStatusText(Internationalization.getString("app_diplomacy_infotext").replace("%20", " "), false);
				targetPane = diplomacyPane;
				if (!adminMenuActive) {
					showMenuIndicator(true);
				}
				// menuIndicatorPos = 233;
				menuIndicatorPos = 276;
				moveMenuIndicator(menuIndicatorPos);
				adminPaneOpen = false;
			}
			// CHAT
			if (bn.equals(chatButton)) {
				logger.info("Chat opened by user.");
				setStatusText(Internationalization.getString("app_chat_infotext").replace("%20", " "), false);
				targetPane = chatPane;
				if (!adminMenuActive) {
					showMenuIndicator(true);
				}
				// menuIndicatorPos = 233;
				// menuIndicatorPos = 276;
				menuIndicatorPos = 319;
				moveMenuIndicator(menuIndicatorPos);
				adminPaneOpen = false;
			}
			// LOG
			if (bn.equals(logButton)) {
				logger.info("Log opened by user.");
				setStatusText(Internationalization.getString("app_log_infotext").replace("%20", " "), false);

				openLogPane = true;
			}
			// EXIT
			if (bn.equals(exitButton)) {
				// logger.info("Application close requested by user.");
				spokenMessage = Internationalization.getString("C3_Speech_close_warning");
				setStatusText(Internationalization.getString("C3_Speech_close_warning").replace("%20", " "), false);
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
				logger.info("Admin menu opened by user.");
				setStatusText(Internationalization.getString("app_admin_infotext").replace("%20", " "), true);
				shiftButtonColumn();
				Tools.playButtonClickSound();
				targetPane = null;
			}
			// STORY EDITOR
			if (bn.equals(storyEditorButton)) {
				logger.info("Sory editor opened by user.");
				setStatusText(Internationalization.getString("app_storyeditor_infotext").replace("%20", " "), false);
				adminPaneOpen = true;
				openEditorPane = true;
			}
			// ADMIN PANE
			if (bn.equals(adminPaneButton)) {
				logger.info("Administration pane (Security) opened by user.");
				setStatusText(Internationalization.getString("app_adminpane_infotext").replace("%20", " "), false);
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
		//} else if (!openAdministrationPane && !openEditorPane) {
			// logger.info("TargetPane not defined!");
		}
		if (openAdministrationPane) {
			// logger.info("Opening administration window!");

			ArrayList<UserDTO> userListFromNexus = Nexus.getUserList();

			Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
			UsereditorPane ap = new UsereditorPane(userListFromNexus, stage, Internationalization.getLocale());
			openAdministrationPane = false;
		}

		if (openEditorPane) {
			logger.info("Opening storyeditor window!");

			Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
			StoryEditorPane ep = new StoryEditorPane(stage);
			openEditorPane = false;
		}

		if (openLogPane) {
			logger.info("Opening log window!");
			Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
			if (logPane == null) {
				logPane = new LogPane(stage, Internationalization.getLocale());
			} else {
				if (LogPane.isVisible) {
					logPane.hide();
				} else {
					logPane.show();
				}
			}
			openLogPane = false;
		}
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
			logButton.setVisible(true);
			showMenuIndicator(false);
			ivDiplomacyIndicator.setVisible(false);
			ircIndicator.setVisible(false);
		} else {
			rolePlayButton.setVisible(true);
			mapButton.setVisible(true);
			attackButton.setVisible(true);
			diplomacyButton.setVisible(true);
			chatButton.setVisible(true);
			if (!adminPaneOpen && Nexus.getCurrentlyOpenedPane() != null) {
				showMenuIndicator(true);
				moveMenuIndicator(menuIndicatorPos);
			}
		}

		// logger.info("Adminmenu: " + adminMenuActive);

		Runnable r = () -> {
			buttonsAreMoving = true;
			if (adminButtonHasFocus) {
				if (adminMenuActive) {
					Platform.runLater(() -> adminButton.setGraphic(new ImageView(imageAdminButtonOn_Focus)));
				} else {
					Platform.runLater(() -> adminButton.setGraphic(new ImageView(imageAdminButtonOff_Focus)));
				}
			} else {
				if (adminMenuActive) {
					Platform.runLater(() -> adminButton.setGraphic(new ImageView(imageAdminButtonOn)));
				} else {
					Platform.runLater(() -> adminButton.setGraphic(new ImageView(imageAdminButtonOff)));
				}
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
					diplomacyButton.setLayoutX(newX1);
					chatButton.setLayoutX(newX1);

					// Column 2
					storyEditorButton.setLayoutX(newX2);
					adminPaneButton.setLayoutX(newX2);
					renameMeButton3.setLayoutX(newX2);
					renameMeButton4.setLayoutX(newX2);
					logButton.setLayoutX(newX2);
				});
			}
			buttonsAreMoving = false;
			if (adminMenuActive) {
				Platform.runLater(() -> {
					rolePlayButton.setVisible(false);
					mapButton.setVisible(false);
					attackButton.setVisible(false);
					diplomacyButton.setVisible(false);
					chatButton.setVisible(false);

					rolePlayButton.setFocusTraversable(false);
					mapButton.setFocusTraversable(false);
					attackButton.setFocusTraversable(false);
					diplomacyButton.setFocusTraversable(false);
					chatButton.setFocusTraversable(false);
					storyEditorButton.setFocusTraversable(true);
					adminPaneButton.setFocusTraversable(true);
					renameMeButton3.setFocusTraversable(true);
					renameMeButton4.setFocusTraversable(true);
					logButton.setFocusTraversable(true);
				});
			} else {
				Platform.runLater(() -> {
					storyEditorButton.setVisible(false);
					adminPaneButton.setVisible(false);
					renameMeButton3.setVisible(false);
					renameMeButton4.setVisible(false);
					logButton.setVisible(false);

					rolePlayButton.setFocusTraversable(true);
					mapButton.setFocusTraversable(true);
					attackButton.setFocusTraversable(true);
					diplomacyButton.setFocusTraversable(true);
					chatButton.setFocusTraversable(true);
					storyEditorButton.setFocusTraversable(false);
					adminPaneButton.setFocusTraversable(false);
					renameMeButton3.setFocusTraversable(false);
					renameMeButton4.setFocusTraversable(false);
					logButton.setFocusTraversable(false);
				});
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	private void setToLevelLoggedOutText() {
		Runnable r = () -> {
			int i = 0;
			while (true) {
				if (Nexus.isLoggedIn()) {
					String tcphostname = C3Properties.getProperty(C3PROPS.TCP_HOSTNAME);
					int tcpPort = Integer.parseInt(C3Properties.getProperty(C3PROPS.TCP_PORT));
					if (Nexus.getHoursLeftInThisRound() != 0) {
						topTexts = new String[]{Nexus.getCurrentUser().getUserName(), tcphostname + ":" + tcpPort, "", "T-" + Nexus.getHoursLeftInThisRound() + "h in R" + Nexus.getCurrentRound(), ""};
					} else {
						topTexts = new String[]{Nexus.getCurrentUser().getUserName(), tcphostname + ":" + tcpPort, ""};
					}
				} else {
					topTexts = new String[]{"1// Communicate", "", "2// Command", "", "3// Control", ""};
				}

				if (i >= topTexts.length) {
					i = 0;
				}
				final int ii = i;
				Platform.runLater(() -> toplabel.setText(topTexts[ii]));

				if (Nexus.isLoggedIn()) {
					if (Nexus.getBoUniverse() != null && Nexus.getCurrentRound() != 0) {
						Date nowDate = new Date(System.currentTimeMillis());
						Date translatedNowDate = translateRealDateToSeasonDate(nowDate, (long) Nexus.getCurrentSeason());
						LocalDate translatedLocalDate = new java.util.Date(translatedNowDate.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						LocalTime nowLocalTime = LocalTime.now();
						LocalDateTime now = LocalDateTime.of(translatedLocalDate, nowLocalTime);
						LocalDateTime endTime = Nexus.getBoUniverse().currentRoundEndDateTime;

						Nexus.setHoursLeftInThisRound(java.time.Duration.between(now, endTime).toHours());

//						logger.info("Now: " + now);
//						logger.info("End time for this round: " + endTime);
//						logger.info("Calculated hours left in this round: " + java.time.Duration.between(now, endTime).toHours());

						long diff = java.time.Duration.between(now, endTime).toMinutes();
						long days = diff / 24 / 60;
						long hours = diff / 60 % 24;
						long minutes = diff % 60;

						String daysString = days == 1 ? Internationalization.getString("general_day") : Internationalization.getString("general_days");
						String hourString = hours == 1 ? Internationalization.getString("general_hour") : Internationalization.getString("general_hours");
						String minuteString = minutes == 1 ? Internationalization.getString("general_minute") : Internationalization.getString("general_minutes");

						String timeString;
						if (days != 0) {
							if (hours != 0) {
								timeString = days + " " + daysString + ", " + hours + " " + hourString + " " + Internationalization.getString("general_and") + " " + minutes + " " + minuteString + " " + Internationalization.getString("general_left_in_round") + " " + Nexus.getCurrentRound();
							} else {
								timeString = days + " " + daysString + " " + Internationalization.getString("general_and") + " " + minutes + " " + minuteString + " " + Internationalization.getString("general_left_in_round") + " " + Nexus.getCurrentRound();
							}
						} else if (hours != 0) {
							timeString = hours + " " + hourString + " " + Internationalization.getString("general_and") + " " + minutes + " " + minuteString + " " + Internationalization.getString("general_left_in_round") + " " + Nexus.getCurrentRound();
						} else {
							timeString = minutes + " " + minuteString + " " + Internationalization.getString("general_left_in_round") + " " + Nexus.getCurrentRound() + ".";
						}
						timeString = timeString + " (" + java.time.Duration.between(now, endTime).toHours() + "h).";
						ActionManager.getAction(ACTIONS.UPDATE_ROUND_COUNTDOWN).execute(timeString);
					}
				}

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
		Thread textTopRightThread = new Thread(r);
		textTopRightThread.start();
	}

//	private void setupNoiseAnimation() {
//		Platform.runLater(() -> {
//			int COLUMNS = 2;
//			int COUNT = 4;
//			int OFFSET_X = 0;
//			int OFFSET_Y = 0;
//			int WIDTH = 860;
//			int HEIGHT = 524;
////			int WIDTH = (int) Math.round(noiseImage.getFitWidth());
////			int HEIGHT = (int) Math.round(noiseImage.getFitHeight());
//
//			InputStream is = this.getClass().getResourceAsStream("/images/noise/noisemap.png");
//			noiseImage.setImage(new Image(is));
//			noiseImage.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
//			noiseImage.setCache(true);
//			noiseImage.setCacheHint(CacheHint.SPEED);
//			noiseAnimation = new SpriteAnimation(noiseImage, Duration.millis(200), COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT);
//			noiseAnimation.setCycleCount(Animation.INDEFINITE);
//		});
//	}

	private void setupSpeechSpectrumAnimation() {
		if (spectrumAnimation == null) {
			Platform.runLater(() -> {
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
			});
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
			diplomacyButton.setDisable(!Security.hasPrivilege(PRIVILEGES.FACTIONLEAD_DIPLOMACY));
			chatButton.setDisable(false);

			// Column 2
			if (admin) {
				adminButton.setDisable(false);

				storyEditorButton.setDisable(false);
				//adminPaneButton.setDisable(false);
				renameMeButton3.setDisable(true);
				renameMeButton4.setDisable(true);
				logButton.setDisable(false);
			} else {
				storyEditorButton.setDisable(true);
				//adminPaneButton.setDisable(true);
				renameMeButton3.setDisable(true);
				renameMeButton4.setDisable(true);
				logButton.setDisable(false);
			}
			adminPaneButton.setDisable(false);
		} else {
			adminButton.setDisable(false);

			// Column 1
			rolePlayButton.setDisable(true);
			mapButton.setDisable(true);
			attackButton.setDisable(true);
			diplomacyButton.setDisable(true);
			chatButton.setDisable(true);

			// Column 2
			storyEditorButton.setDisable(true);
			adminPaneButton.setDisable(true);
			renameMeButton3.setDisable(true);
			renameMeButton4.setDisable(true);
			logButton.setDisable(false);
		}
	}

	/**
	 * @param url url
	 * @param rb  resource bundle
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
		adminButton.setDisable(false);

		int year = Calendar.getInstance().get(Calendar.YEAR);
		hudinfo1.setOpacity(0.7);
		copyrightLabel.setText("©2000-" + year + " - clanwolf.net");
		spectrumImage.setOpacity(0.1);
		spectrumImage.setVisible(false);

//		noiseImage.setOpacity(0.0);
//		noiseImage.setVisible(false);
//		noiseImage.toFront();

		paneNoise.setOpacity(0.0);
		paneNoise.setVisible(false);

		Image helpImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/help.png")));
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

		//		createNewPaneObjects(); // re-create Map pane. This method is also used if user loggs off to create a new map pane (to avoid problems with user privs)
		mapPane = new StarmapPane();
		mapPane.setShowsMouseFollow(false);
		mapPane.setShowsPlanetRotation(false);
		mapPane.setCacheHint(CacheHint.SPEED);
		mapPane.getController().addActionCallBackListeners();

		chatPane = new ChatPane();
		chatPane.setShowsMouseFollow(false);
		chatPane.setShowsPlanetRotation(false);
		chatPane.setCacheHint(CacheHint.SPEED);
		chatPane.getController().addActionCallBackListeners();

		dicePane = new DicePane();
		dicePane.setShowsMouseFollow(false);
		dicePane.setShowsPlanetRotation(false);
		dicePane.setCacheHint(CacheHint.SPEED);
		dicePane.getController().addActionCallBackListeners();

		diplomacyPane = new DiplomacyPane();
		diplomacyPane.setShowsMouseFollow(false);
		diplomacyPane.setShowsPlanetRotation(false);
		diplomacyPane.setCacheHint(CacheHint.SPEED);
		diplomacyPane.getController().addActionCallBackListeners();

		String paneNameCharacter = "CharacterPane";
		rolePlayPane = new RolePlayBasicPane(paneNameCharacter);
		rolePlayPane.setShowsMouseFollow(false);
		rolePlayPane.setShowsPlanetRotation(false);
		rolePlayPane.setCache(true);
		rolePlayPane.setCacheHint(CacheHint.SPEED);
		rolePlayPane.getController().addActionCallBackListeners();
		rolePlayPane.getController().setPaneName(paneNameCharacter);
		logger.info("RolePlayPane: " + rolePlayPane + " -> Controller: " + rolePlayPane.getController());

		String paneNameAttack = "AttackPane";
		attackPane = new RolePlayBasicPane(paneNameAttack);
		attackPane.setShowsMouseFollow(false);
		attackPane.setShowsPlanetRotation(false);
		attackPane.setCache(true);
		attackPane.setCacheHint(CacheHint.SPEED);
		attackPane.getController().addActionCallBackListeners();
		attackPane.getController().setPaneName(paneNameAttack);
		logger.info("AttackPane: " + attackPane + " -> Controller: " + attackPane.getController());

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
		diplomacyButton.setVisible(true);
		chatButton.setVisible(true);

		storyEditorButton.setVisible(false);
		adminPaneButton.setVisible(false);
		renameMeButton3.setVisible(false);
		renameMeButton4.setVisible(false);
		logButton.setVisible(false);

		showMenuIndicator(false);

		terminalPrompt.toFront();
		paneVolumeControl.toFront();
		slVolumeControl.toFront();
		ivMuteToggle.toFront();
		addActionCallBackListeners();

		tblUserHistory.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		tblUserHistory.getStyleClass().add("noheader");
		tblUserHistory.setPlaceholder(new Label("..."));
		TableColumn<UserHistoryEntry, String> userColumn = new TableColumn<>("");
		userColumn.setCellValueFactory(data -> data.getValue().getUser());
		userColumn.setPrefWidth(140);
		TableColumn<UserHistoryEntry, String> factionColumn = new TableColumn<>("");
		factionColumn.setCellValueFactory(data -> data.getValue().getFaction());
		factionColumn.setPrefWidth(50);
		//		TableColumn<UserHistoryEntry, String> versionColumn = new TableColumn<>("");
		//		versionColumn.setCellValueFactory(data -> data.getValue().getVersion());
		//		versionColumn.setPrefWidth(250);
		//		TableColumn<UserHistoryEntry, String> timeColumn = new TableColumn<>("");
		//		timeColumn.setCellValueFactory(data -> data.getValue().getTime());
		//		timeColumn.setPrefWidth(102);
		//		timeColumn.setMaxWidth(105);
		//		timeColumn.setMinWidth(100);
		TableColumn<UserHistoryEntry, String> attackColumn = new TableColumn<>("");
		attackColumn.setCellValueFactory(data -> data.getValue().getInFightForPlanet());
		attackColumn.setPrefWidth(170);
		tblUserHistory.getColumns().addAll(userColumn, factionColumn,
				//versionColumn
				//timeColumn
				attackColumn);
		tblUserHistory.getSortOrder().add(attackColumn);
		tblUserHistory.getSortOrder().add(userColumn);

		tblUserHistory.setRowFactory(tblUserHistory -> new TableRow<UserHistoryEntry>() {
			@Override
			protected void updateItem(UserHistoryEntry item, boolean empty) {
				super.updateItem(item, empty);
			}
		});

		adminButton.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
				if (newPropertyValue) {
					// Change image on admin switcher to reflect that there is focus
					adminButtonHasFocus = true;
					if (adminMenuActive) {
						Platform.runLater(() -> adminButton.setGraphic(new ImageView(imageAdminButtonOn_Focus)));
					} else {
						Platform.runLater(() -> adminButton.setGraphic(new ImageView(imageAdminButtonOff_Focus)));
					}
				} else {
					// Change image on admin switcher to reflect that there is no focus
					adminButtonHasFocus = false;
					if (adminMenuActive) {
						Platform.runLater(() -> adminButton.setGraphic(new ImageView(imageAdminButtonOn)));
					} else {
						Platform.runLater(() -> adminButton.setGraphic(new ImageView(imageAdminButtonOff)));
					}
				}
			}
		});
		toplabel.toFront();

		terminalPrompt.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
			@Override
			public void handle(ContextMenuEvent event) {
				event.consume();
			}
		});
	}

	/**
	 * Handle Actions
	 *
	 * @param action action
	 * @param o      action object
	 * @return boolean (always true, ignore)
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case SERVER_CONNECTION_LOST:
				// raise error message
				C3Message messageServerConnectionLost = new C3Message(C3MESSAGES.ERROR_SERVER_CONNECTION_LOST);
				String m = Internationalization.getString("general_server_connection_lost");
				messageServerConnectionLost.setText(m);
				messageServerConnectionLost.setType(C3MESSAGETYPES.CLOSE);
				ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(messageServerConnectionLost);

				// Stop heartbeat timer
				if (Nexus.getServerHeartBeatTimer() != null) {
					Nexus.getServerHeartBeatTimer().cancel();
					Nexus.getServerHeartBeatTimer().purge();
				}
				break;

			case DIPLOMACY_STATUS_PENDING:
				if (!adminMenuActive) {
					ivDiplomacyIndicator.setVisible(true);
					logger.info("Diplomacy status pending, reaction expected!");
				}
				break;

			case ENABLE_DEFAULT_BUTTON:
				enableDefaultButton(true);
				break;

			case DISABLE_DEFAULT_BUTTON:
				enableDefaultButton(false);
				break;

			case DIPLOMACY_STATUS_PENDING_HIDE:
				ivDiplomacyIndicator.setVisible(false);
				logger.info("No Diplomacy requests pending deactivate warning!");
				break;

			case CHANGE_LANGUAGE:
				if (currentlyDisplayedPane != null) {
					ActionManager.getAction(ACTIONS.NOISE).execute();
					ActionManager.getAction(ACTIONS.UPDATE_GAME_INFO).execute();
				}
				break;

			case READY_TO_LOGIN:
				boolean va = (boolean) o.getObject();
				if (va) {
					Platform.runLater(() -> loginIndicatorLabel.setStyle("-fx-background-color: #008000;"));
				} else {
					Platform.runLater(() -> loginIndicatorLabel.setStyle("-fx-background-color: #c00000;"));
				}
				break;

			case SET_STATUS_TEXT:
				String color = "";
				StatusTextEntryActionObject ste = (StatusTextEntryActionObject) o.getObject();
				if (ste.getColor() != null && !"".equals(ste.getColor())) {
					switch (ste.getColor()) {
						case "GREEN" -> color = "#00ff00";
						case "BLUE" -> color = "#0000ff";
						case "YELLOW" -> color = "#ffff00";
					}
				}
				String finalColor = color;
				Platform.runLater(() -> setStatusText(ste.getMessage(), ste.isFlash(), finalColor, ste.isJustUpdate()));
				break;

			case ONLINECHECK_STARTED:
				ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("3");
				// Log.debug("ACTIONS.ONLINE_STATUS_CHECK_STARTED catched.");
				Platform.runLater(() -> onlineIndicatorLabel.setStyle("-fx-background-color: #808000;"));
				C3Properties.setProperty(C3PROPS.CHECK_ONLINE_STATUS, "RUNNING_CHECK", false);
				break;

			case ONLINECHECK_FINISHED:
				boolean result = (boolean) o.getObject();
				if (result) {
					Platform.runLater(() -> onlineIndicatorLabel.setStyle("-fx-background-color: #008000;"));
					C3Properties.setProperty(C3PROPS.CHECK_ONLINE_STATUS, "ONLINE", false);
					ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("3");
				} else {
					Platform.runLater(() -> onlineIndicatorLabel.setStyle("-fx-background-color: #c00000;"));
					C3Properties.setProperty(C3PROPS.CHECK_ONLINE_STATUS, "OFFLINE", false);
					C3Message message = new C3Message(C3MESSAGES.ERROR_SERVER_OFFLINE);
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
					logger.info("Server is offline, DB is not checked!");
				} else {
					ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("4");
					Platform.runLater(() -> databaseAccessibleIndicatorLabel.setStyle("-fx-background-color: #808000;"));
					C3Properties.setProperty(C3PROPS.CHECK_CONNECTION_STATUS, "RUNNING_CHECK", false);
				}
				break;

			case DATABASECONNECTIONCHECK_FINISHED:
				boolean result3 = (boolean) o.getObject();
				// Log.debug("ACTIONS.DATABASECONNECTIONCHECK_FINISHED catched. Online: " + result3);
				if (result3) {
					Platform.runLater(() -> databaseAccessibleIndicatorLabel.setStyle("-fx-background-color: #008000;"));
					C3Properties.setProperty(C3PROPS.CHECK_CONNECTION_STATUS, "ONLINE", false);
					ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("4");
				} else {
					Platform.runLater(() -> databaseAccessibleIndicatorLabel.setStyle("-fx-background-color: #c00000;"));
					C3Properties.setProperty(C3PROPS.CHECK_CONNECTION_STATUS, "OFFLINE", false);
					C3Message message = new C3Message(C3MESSAGES.ERROR_DATABASE_OFFLINE);
					message.setType(C3MESSAGETYPES.CLOSE);
					message.setText(Internationalization.getString("app_database_indicator_message_OFFLINE"));
					C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
					ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("app_database_indicator_message_OFFLINE"), true));
					ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
				}
				break;

			case LOGGING_OFF:
				Platform.runLater(() -> {
					Nexus.setLoggedInStatus(false);
					enableMainMenuButtons(Nexus.isLoggedIn(), false);

					//					createNewPaneObjects();
				});
				break;

			case LOGGED_OFF_COMPLETE:
				Platform.runLater(() -> loginIndicatorLabel.setStyle("-fx-background-color: #c00000;"));
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

				ActionManager.getAction(ACTIONS.SET_FACTION_LOGO_INVISIBLE).execute();
				break;

			case LOGGED_OFF_AFTER_DOUBLE_LOGIN_COMPLETE:
				ActionManager.getAction(ACTIONS.LOGGED_OFF_COMPLETE).execute();

				// Dialog
				Platform.runLater(() -> {
					Alert alert = Tools.C3Dialog(AlertType.ERROR, Internationalization.getString("ACTIONS.LOGGED_OFF_AFTER_DOUBLE_LOGIN_COMPLETE.name"), Internationalization.getString("ACTIONS.LOGGED_OFF_AFTER_DOUBLE_LOGIN_COMPLETE.name"), Internationalization.getString("ACTIONS.LOGGED_OFF_AFTER_DOUBLE_LOGIN_COMPLETE.description"));
					Optional<ButtonType> r = alert.showAndWait();
					if (r.isPresent() && ButtonType.OK == r.get()) {
						logger.info("Message has been confirmed.");
					}
				});
				break;

			case LOGGED_ON:
				Platform.runLater(() -> {
					//
				});
				break;

			case LOGON_FINISHED_SUCCESSFULL:
				Platform.runLater(() -> {
					// logger.info("Current user is: " + Nexus.getCurrentUser() + " (Check this not to be NULL)");

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

					// Print information about the server logged in gui
					String tcphostname = C3Properties.getProperty(C3PROPS.TCP_HOSTNAME);
					int tcpPort = Integer.parseInt(C3Properties.getProperty(C3PROPS.TCP_PORT));

					Nexus.setLoggedInStatus(true);
					//					cycleTopRightTexts.set(false);
					//					textTopRightThread.interrupt();

					if (Nexus.getCurrentUser() != null) {
						if (Nexus.getCurrentUser().getUserName().length() > 15) {
							toplabel.setText(Nexus.getCurrentUser().getUserName());
						} else {
							toplabel.setText(Nexus.getCurrentUser().getUserName() + " @ " + tcphostname + ":" + tcpPort);
						}
						toplabel.setTooltip(new Tooltip(Nexus.getCurrentUser().getUserName() + " - " + tcphostname + ":" + tcpPort));
					} else {
						toplabel.setText("Con // " + tcphostname + ":" + tcpPort);
					}
					enableMainMenuButtons(Nexus.isLoggedIn(), Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN));
					BOUniverse boUniverse = Nexus.getBoUniverse();

					Date nowDate = new Date(System.currentTimeMillis());
					Date translatedNowDate = translateRealDateToSeasonDate(nowDate, (long) Nexus.getCurrentSeason());
					LocalDate translatedLocalDate = new java.util.Date(translatedNowDate.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					LocalTime nowLocalTime = LocalTime.now();
					LocalDateTime now = LocalDateTime.of(translatedLocalDate, nowLocalTime);
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Internationalization.getString("general_date"));
					String formatDateTime = now.format(formatter);

					//					gameInfoLabel.setText("S" + boUniverse.currentSeason + " R" + boUniverse.currentRound + "[" + (boUniverse.currentRoundPhase == 1 ? "." : ":") + "]" + "/" + boUniverse.maxNumberOfRoundsForSeason + " " + Tools.getRomanNumber(boUniverse.currentSeasonMetaPhase) + " - " + boUniverse.currentDate);
					gameInfoLabel.setText("S" + boUniverse.currentSeason + "*" + Tools.getRomanNumber(boUniverse.currentSeasonMetaPhase) + "/R" + boUniverse.currentRound + " - " + formatDateTime);

					labelTFSProgress.setText(boUniverse.currentRound + " / " + boUniverse.maxNumberOfRoundsForSeason);

					double tfsProgress = (100d / Nexus.getBoUniverse().maxNumberOfRoundsForSeason * Nexus.getBoUniverse().currentRound) / 100;
					TFSProgress.setProgress(tfsProgress);
					TFSProgress.setVisible(true);
					TFSProgress.toFront();
					TFSInfo.setVisible(false);
					if (tfsProgress > 1) {
						TFSProgress.lookup(".bar").setStyle("-fx-background-color: -fx-box-border, #ff0000");
					}

					UserHistoryInfo.setVisible(false);

					ActionManager.getAction(ACTIONS.DIPLOMACY_STATUS_PENDING_HIDE).execute();
					for (DiplomacyDTO d : Nexus.getBoUniverse().getDiplomacy()) {
						if (d.getStartingInRound() <= Nexus.getBoUniverse().currentRound) {
							if (d.getFactionID_ACCEPTED().equals(Nexus.getCurrentChar().getFactionId().longValue())) {
								Pair<Long, Long> dp = new Pair<>(d.getFactionID_REQUEST(), d.getFactionID_ACCEPTED());
								boolean alreadyAnswered = false;
								for (DiplomacyDTO d1 : Nexus.getBoUniverse().getDiplomacy()) {
									Pair<Long, Long> dp1 = new Pair<>(d1.getFactionID_REQUEST(), d1.getFactionID_ACCEPTED());
									if (dp.getKey().equals(dp1.getValue())) {
										alreadyAnswered = true;
									}
								}
								if (!alreadyAnswered) {
									ActionManager.getAction(ACTIONS.DIPLOMACY_STATUS_PENDING).execute();
								}
							}
						}
					}

					ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("5");
				});
				break;

			case SET_FACTION_LOGO:
				Platform.runLater(() -> {
					if (!factionLogoVisible) {
						Image factionLogo = Nexus.getFactionLogo();
						Image characterImage = Nexus.getCurrentCharImage();

						if (factionLogo != null) {
							ivFactionLogoShield.setVisible(true);

							ivFactionLogo.setOpacity(0.0d);
							ivFactionLogo.setImage(factionLogo);
							ivFactionLogo.setVisible(true);

							ivLoggedOnUserImage.setOpacity(0.0d);
							ivLoggedOnUserImage.setImage(characterImage);
							ivLoggedOnUserImage.setVisible(true);

							FadeTransition fadeInTransition = new FadeTransition(Duration.millis(1500), ivFactionLogo);
							fadeInTransition.setFromValue(0.0);
							fadeInTransition.setToValue(1.0);
							fadeInTransition.setCycleCount(1);
							fadeInTransition.setOnFinished(event -> {
								// ivLoggedOnUserImage.setVisible(true);
							});
							fadeInTransition.play();

							FadeTransition fadeInTransitionUserImage = new FadeTransition(Duration.millis(5000), ivLoggedOnUserImage);
							fadeInTransitionUserImage.setFromValue(0.0);
							fadeInTransitionUserImage.setToValue(1.0);
							fadeInTransitionUserImage.setCycleCount(1);
							fadeInTransitionUserImage.setOnFinished(event -> {
								// ivLoggedOnUserImage.setVisible(true);
							});
							fadeInTransitionUserImage.play();
						}
						factionLogoVisible = true;
					}
				});
				break;

			case SET_FACTION_LOGO_INVISIBLE:
				Platform.runLater(() -> {
					if (factionLogoVisible) {
						FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(800), ivFactionLogo);
						fadeOutTransition.setFromValue(1.0);
						fadeOutTransition.setToValue(0.0);
						fadeOutTransition.setCycleCount(1);
						fadeOutTransition.setOnFinished(event -> {
							// ivFactionLogoShield.setVisible(false);
							// ivLoggedOnUserImage.setVisible(false);
						});
						fadeOutTransition.play();

						FadeTransition fadeOutTransitionUserImage = new FadeTransition(Duration.millis(800), ivLoggedOnUserImage);
						fadeOutTransitionUserImage.setFromValue(1.0);
						fadeOutTransitionUserImage.setToValue(0.0);
						fadeOutTransitionUserImage.setCycleCount(1);
						fadeOutTransitionUserImage.setOnFinished(event -> {
							// ivFactionLogoShield.setVisible(false);
							// ivLoggedOnUserImage.setVisible(false);
						});
						fadeOutTransitionUserImage.play();

						factionLogoVisible = false;
					}
				});
				break;

			case LOGON_RUNNING:
				loginIndicatorLabel.setStyle("-fx-background-color: #808000;");
				C3Properties.setProperty(C3PROPS.CHECK_LOGIN_STATUS, "LOGON_RUNNING", false);
				break;

			case PANE_CREATION_BEGINS:
				Platform.runLater(() -> {
					paneTransitionInProgress = true;
					systemConsole.setOpacity(0.2);
					systemConsoleCurrentLine.setOpacity(0.4);
					spectrumImage.setOpacity(0.8);
				});
				break;

			case ADD_CONSOLE_LINE:
				Platform.runLater(() -> {
					if (o.getText() != null && !"".equals(o.getText())) {
						setConsoleEntry(o.getText());
					}
				});
				break;

			case PANE_CREATION_FINISHED:
				currentlyDisplayedPane = (AbstractC3Pane) o.getObject();
				ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("6");
				paneTransitionInProgress = false;

				Platform.runLater(() -> {
					if (currentlyDisplayedPane instanceof RolePlayBasicPane) {
						paneVolumeControl.setVisible(true);
						slVolumeControl.setVisible(true);
						ivMuteToggle.setVisible(true);

						if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_MUSIC))) {
							C3SoundPlayer.pauseMusic();
						}
					} else {
						paneVolumeControl.setVisible(false);
						slVolumeControl.setVisible(false);
						ivMuteToggle.setVisible(false);

						if ("true".equals(C3Properties.getProperty(C3PROPS.PLAY_MUSIC))) {
							C3SoundPlayer.startMusic();
						}
					}
					currentlyDisplayedPane.setFocus();
				});
				break;

			case PANE_DESTROY_CURRENT:
				paneTransitionInProgress = true;
				currentlyDisplayedPane.paneDestruction();
				break;

			case PANE_DESTRUCTION_FINISHED:
				if (o.getObject() instanceof Pane) {
					Platform.runLater(() -> {
						Pane p = (Pane) o.getObject();
						rootAnchorPane.getChildren().remove(p);
						currentlyDisplayedPane = null;
						if (nextToDisplayPane != null) {
							createNextPane();
						} else {
							ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("7");
							systemConsole.setOpacity(0.2);
							systemConsoleCurrentLine.setOpacity(0.4);
							spectrumImage.setOpacity(0.1);

							showMenuIndicator(false);
							adminPaneOpen = false;
							Nexus.setCurrentlyOpenedPane(null);
						}
						paneTransitionInProgress = false;
					});
				}
				break;

			case SET_CONSOLE_OPACITY:
				Platform.runLater(() -> {
					double v = (double) o.getObject();
					systemConsole.setOpacity(v);
					systemConsoleCurrentLine.setOpacity(v);
				});
				break;

			case SET_CONSOLE_OUTPUT_LINE:
				Platform.runLater(() -> {
					String s = (String) o.getObject();
					setConsoleEntry(s);
				});
				break;

			case TERMINAL_COMMAND:
				String commandline = o.getText();
				TerminalCommandHandler.handleCommand(commandline);
				break;

			case NOISE:
				Platform.runLater(() -> {
					int duration;
					if ((o != null) && (o.getObject() instanceof Integer)) {
						duration = (Integer) o.getObject();
					} else {
						Random random = new Random();
						duration = random.nextInt(400) + 200;
					}

					// Move the pane node back behind the noise pane, so that noise is infront of content
					// The order in this list reflects the z-order of the nodes for display
					boolean finished = false;
					int cycleCounter = 0;
					ObservableList<Node> workingCollection = FXCollections.observableArrayList(rootAnchorPane.getChildren());
					do {
						for (int ia = 1; ia < workingCollection.size(); ia++) {
							if (workingCollection.get(ia) instanceof AbstractC3Pane) {
								if (workingCollection.get(ia + 1).getId().equals("paneNoise")) {
									finished = true;
								} else {
									Collections.swap(workingCollection, ia - 1, ia);
								}
								break;
							}
						}
						cycleCounter++;
					} while (!finished || cycleCounter < 5000); // Savety exit in case paneNoise is not found
					rootAnchorPane.getChildren().setAll(workingCollection);
					paneNoise.setVisible(true);

					FadeTransition FadeOutTransition = new FadeTransition(Duration.millis(duration), paneNoise);
					FadeOutTransition.setFromValue(0.60);
					FadeOutTransition.setToValue(0.0);
					FadeOutTransition.setCycleCount(1);
					FadeOutTransition.setOnFinished(event -> {
						paneNoise.setVisible(false);
					});

					FadeTransition FadeInTransition = new FadeTransition(Duration.millis(duration), paneNoise);
					FadeInTransition.setFromValue(0.0);
					FadeInTransition.setToValue(0.60);
					FadeInTransition.setCycleCount(1);
					FadeInTransition.setOnFinished(event -> FadeOutTransition.play());

					FadeInTransition.play();
					paneVolumeControl.toFront();
					slVolumeControl.toFront();
					ivMuteToggle.toFront();
				});
				break;

			case START_SPEECH_SPECTRUM:
				Platform.runLater(() -> {
					spectrumImage.setVisible(true);
					if (spectrumAnimation != null) {
						spectrumAnimation.play();
					}
				});
				break;

			case STOP_SPEECH_SPECTRUM:
				Platform.runLater(() -> {
					spectrumImage.setVisible(false);
					if (spectrumAnimation != null) {
						spectrumAnimation.stop();
					}
				});
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
				if (messagePaneStack.size() == 0 && !Nexus.oneOrMoreMessageOpen.get()) {
					Platform.runLater(() -> {
						Nexus.setMainFrameEnabled(true);
						mouseStopper.toFront();
						paneVolumeControl.toFront();
						slVolumeControl.toFront();
						ivMuteToggle.toFront();
						waitAnimationPane.showCircleAnimation(false);
						mouseStopper.setMouseTransparent(true);
						labelWaitText.setText("");
						labelWaitText.setVisible(false);
						paneVolumeControl.toFront();
						slVolumeControl.toFront();
						ivMuteToggle.toFront();
						ActionManager.getAction(ACTIONS.ACTION_SUCCESSFULLY_EXECUTED).execute(o);
					});
				}
				break;

			case CURSOR_REQUEST_WAIT:
				Platform.runLater(() -> {
					String sourceIdRW = "";
					if (o.getText() != null && !"".equals(o.getText())) {
						sourceIdRW = o.getText();
					}
					Nexus.setMainFrameEnabled(false);

					mouseStopper.toFront();
					paneVolumeControl.toFront();
					slVolumeControl.toFront();
					ivMuteToggle.toFront();
					// mouseStopper.setBackground(new Background(new BackgroundFill(Color.BLUE, new CornerRadii(0), Insets.EMPTY)));
					waitAnimationPane.showCircleAnimation(true);
					mouseStopper.setMouseTransparent(false);
					if (sourceIdRW.length() > 3) {
						labelWaitText.setText(sourceIdRW);
					}
					labelWaitText.setVisible(true);
					paneVolumeControl.toFront();
					slVolumeControl.toFront();
					ivMuteToggle.toFront();
				});
				break;

			case SHOW_MESSAGE:
				Platform.runLater(() -> {
					if ((o != null) && (o.getObject() instanceof C3Message message)) {
						showMessage(message);
					}
					paneVolumeControl.toFront();
					slVolumeControl.toFront();
					ivMuteToggle.toFront();
				});
				break;

			case SHOW_MEDAL:
				Platform.runLater(() -> {
					if ((o != null) && (o.getObject() instanceof MEDALS)) {
						// Integer id = ((MEDALS)o.getObject()).getId();
						String imageName = o.getObject().toString();
						String desc = Internationalization.getString("MEDALS_" + imageName + "_desc");
						Image med = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/gui/rewards/" + imageName + ".png")));
						showMedal(med, desc);
					}
					paneVolumeControl.toFront();
					slVolumeControl.toFront();
					ivMuteToggle.toFront();
				});
				break;

			case SHOW_POPUP:
				Platform.runLater(() -> {
					if ((o != null) && (o.getObject() instanceof POPUPS)) {
						Integer id = ((POPUPS) o.getObject()).getId();
						String colorCode = "green";
						switch (id) {
							case 1:
								colorCode = "green";
								break;
							case 2:
								colorCode = "orange";
								break;
						}
						String imageName = o.getObject().toString();
						String desc = Internationalization.getString("POPUPS_" + imageName + "_desc");
						Image pop = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/gui/popups/" + imageName + ".png")));
						showPopup(pop, desc, colorCode);
					}
					paneVolumeControl.toFront();
					slVolumeControl.toFront();
					ivMuteToggle.toFront();
				});
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
					if (Nexus.isLoggedIn()) {
						BOUniverse boUniverse = Nexus.getBoUniverse();

						Date nowDate = new Date(System.currentTimeMillis());
						Date translatedNowDate = translateRealDateToSeasonDate(nowDate, (long) Nexus.getCurrentSeason());
						LocalDate translatedLocalDate = new java.util.Date(translatedNowDate.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						LocalTime nowLocalTime = LocalTime.now();
						LocalDateTime now = LocalDateTime.of(translatedLocalDate, nowLocalTime);
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Internationalization.getString("general_date"));
						String formatDateTime = now.format(formatter);

						//					gameInfoLabel.setText("S" + boUniverse.currentSeason + " R" + boUniverse.currentRound + "[" + (boUniverse.currentRoundPhase == 1 ? "." : ":") + "]" + "/" + boUniverse.maxNumberOfRoundsForSeason + " " + Tools.getRomanNumber(boUniverse.currentSeasonMetaPhase) + " - " + boUniverse.currentDate);
						gameInfoLabel.setText("S" + boUniverse.currentSeason + "*" + Tools.getRomanNumber(boUniverse.currentSeasonMetaPhase) + "/R" + boUniverse.currentRound + " - " + formatDateTime);

						double tfsProgress = (100d / Nexus.getBoUniverse().maxNumberOfRoundsForSeason * Nexus.getBoUniverse().currentRound) / 100;
						TFSProgress.setProgress(tfsProgress);
						if (tfsProgress > 1) {
							TFSProgress.lookup(".bar").setStyle("-fx-background-color: -fx-box-border, #ff0000");
						}

						String alliancesString = "";
						ArrayList<Pair<BOFaction, BOFaction>> factionAlliencesList = Nexus.getBoUniverse().getFactionAlliencesList();
						for (Pair<BOFaction, BOFaction> p : factionAlliencesList) {
							BOFaction f1 = p.getKey();
							BOFaction f2 = p.getValue();
							alliancesString += f1.getFactionDTO().getShortName() + " \uD83E\uDD1D " + f2.getFactionDTO().getShortName() + "\r\n";
						}
						if (!"".equals(alliancesString)) {
							ActionManager.getAction(ACTIONS.UPDATE_ALLIANCES_LIST).execute(Internationalization.getString("general_alliances") + ":\r\n" + alliancesString);
						}
					}
				});
				break;

			case HIDE_IRC_INDICATOR:
				ircIndicator.setVisible(false);
				break;

			case SHOW_IRC_INDICATOR:
				if (currentlyDisplayedPane == null || !currentlyDisplayedPane.getPaneName().equals("ChatPane")) {
					if (!ircIndicator.isVisible()) {
						ircIndicator.setVisible(true);
						C3SoundPlayer.play("sound/fx/beep_electric_3.mp3", false);
					}
				}
				break;

			case IRC_DISCONNECT_NOW:
				Platform.runLater(() -> {
					//					chatPane = null;
					//					chatPane = new ChatPane();
					//					chatPane.setShowsMouseFollow(false);
					//					chatPane.setShowsPlanetRotation(false);
					//					chatPane.setCacheHint(CacheHint.SPEED);
					//					chatPane.getController().addActionCallBackListeners();
				});
				break;

			case FLASH_MWO_LOGO_ONCE:
				Platform.runLater(() -> {
					ivMWOLogo.setVisible(true);
					ivMWOLogo.setOpacity(1.0);
					ivMWOLogo.toFront();
					//					C3SoundPlayer.play("sound/fx/beep_03.mp3", false);
					FadeTransition fadeOutTransition = new FadeTransition(Duration.millis(2000), ivMWOLogo);
					fadeOutTransition.setFromValue(1.0);
					fadeOutTransition.setToValue(0.0);
					fadeOutTransition.setCycleCount(1);
					fadeOutTransition.setOnFinished(event -> ivMWOLogo.setVisible(false));
					fadeOutTransition.play();
				});
				break;

			case ENABLE_MAIN_MENU_BUTTONS:
				enableMainMenuButtons(Nexus.isLoggedIn(), Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN));
				Platform.runLater(() -> {
					paneVolumeControl.toFront();
					slVolumeControl.toFront();
					ivMuteToggle.toFront();
				});
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

			case SWITCH_TO_MAP:
				if (attackPane.isDisplayed()) {
					C3SoundPlayer.play("sound/fx/cursor_selection_07.mp3", false);
					openTargetPane(mapPane, "");
					if (!adminMenuActive) {
						showMenuIndicator(true);
					}
					menuIndicatorPos = 147;
					moveMenuIndicator(menuIndicatorPos);
					Nexus.stopMWOTimer();
				}

				// If this happens, we have been dropping out of an invasion lobby or game.
				// Either the round was finalized or we have been kicked from the lobby.
				// Anyway, we can not just click on the attack-button at this point.
				attackButton.setDisable(true);

				ActionManager.getAction(ACTIONS.RESET_STORY_PANES).execute();
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
				logger.info("Do something with the new UNIVERSE (here)!!!");
				break;

			default:
				break;
		}
		return true;
	}

	/**
	 * Here all the actions go in that need to be performed once the client is started up and showing
	 */
	private void startup() {
		setupSpeechSpectrumAnimation();
//		setupNoiseAnimation();

//		noiseImage.toFront();
//		noiseImage.setVisible(true);
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

		setConsoleEntry("Setting control elements");

		enableMainMenuButtons(Nexus.isLoggedIn(), Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN));

		// Add values to the property file in case they are not present
		// Do not change them if they are present

		// encrypted
		//ftp_password=DJ9G4ix1bYTy/K5QmR8jdQ==
		//ftp_password_logupload=AsdSqD58lmfkL7oyS+oenQ==
		//ftp_password_historyupload=ipmMIwmjxlpI1s7JJ1Ei6g==

		if (C3Properties.getProperty(C3PROPS.FTP_PORT).equals("unknown") || C3Properties.getProperty(C3PROPS.FTP_PORT).equals("")) {
			C3Properties.setProperty(C3PROPS.FTP_PORT, "21", true);
		}
		if (C3Properties.getProperty(C3PROPS.FTP_USER).equals("unknown") || C3Properties.getProperty(C3PROPS.FTP_USER).equals("")) {
			C3Properties.setProperty(C3PROPS.FTP_USER, "c3_client", true);
		}
		if (C3Properties.getProperty(C3PROPS.FTP_USER_LOGUPLOAD).equals("unknown") || C3Properties.getProperty(C3PROPS.FTP_USER_LOGUPLOAD).equals("")) {
			C3Properties.setProperty(C3PROPS.FTP_USER_LOGUPLOAD, "c3_client_logupload", true);
		}
		if (C3Properties.getProperty(C3PROPS.FTP_USER_HISTORYUPLOAD).equals("unknown") || C3Properties.getProperty(C3PROPS.FTP_USER_HISTORYUPLOAD).equals("")) {
			C3Properties.setProperty(C3PROPS.FTP_USER_HISTORYUPLOAD, "c3_client_historyupload", true);
		}
		if (C3Properties.getProperty(C3PROPS.FTP_PASSWORD).equals("unknown") || C3Properties.getProperty(C3PROPS.FTP_PASSWORD).equals("")) {
			C3Properties.setProperty(C3PROPS.FTP_PASSWORD, "DJ9G4ix1bYTy/K5QmR8jdQ==", true, false);
		}
		if (C3Properties.getProperty(C3PROPS.FTP_PASSWORD_LOGUPLOAD).equals("unknown") || C3Properties.getProperty(C3PROPS.FTP_PASSWORD_LOGUPLOAD).equals("")) {
			C3Properties.setProperty(C3PROPS.FTP_PASSWORD_LOGUPLOAD, "AsdSqD58lmfkL7oyS+oenQ==", true, false);
		}
		if (C3Properties.getProperty(C3PROPS.FTP_PASSWORD_HISTORYUPLOAD).equals("unknown") || C3Properties.getProperty(C3PROPS.FTP_PASSWORD_HISTORYUPLOAD).equals("")) {
			C3Properties.setProperty(C3PROPS.FTP_PASSWORD_HISTORYUPLOAD, "ipmMIwmjxlpI1s7JJ1Ei6g==", true, false);
		}

		C3SoundPlayer.getSamples();

		if (Nexus.promptNewVersionInstall) {
			C3Message message = new C3Message(C3MESSAGES.DOWNLOAD_CLIENT);
			String m = Internationalization.getString("app_new_version_available");
			m = m.replace("{version}", Nexus.getLastAvailableClientVersion());
			message.setText(m);
			message.setType(C3MESSAGETYPES.YES_NO);
			ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(message);
		} else {
			openTargetPane(loginPane, "");
		}

		Platform.runLater(() -> {
			Double v = 0.1d;
			String speechVolume = C3Properties.getProperty(C3PROPS.SPEECHVOLUME);
			if (speechVolume != null && !"unknown".equals(speechVolume) && !"".equals(speechVolume)) {
				try {
					v = Double.parseDouble(speechVolume);
				} catch (Exception e) {
					// do nothing, value could not be set
				}
			}

			paneVolumeControl.toFront();
			ivMuteToggle.toFront();
			slVolumeControl.toFront();
			slVolumeControl.setMin(0.0d);
			slVolumeControl.setMax(1.0d);
			slVolumeControl.setValue(v);
			Nexus.setMainFrameVolumeSlider(slVolumeControl);
		});

		// remove all default buttons to prevent unwanted action if a command is send by enter from terminal
		terminalPrompt.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
				if (newPropertyValue) {
					ActionManager.getAction(ACTIONS.DISABLE_DEFAULT_BUTTON).execute();
				} else {
					ActionManager.getAction(ACTIONS.ENABLE_DEFAULT_BUTTON).execute();
				}
			}
		});

//		C3Message m1 = new C3Message(C3MESSAGES.WARNING_BLACKBOX_TEAMS_INVALID);
//		m1.setType(C3MESSAGETYPES.CLOSE);
//		m1.setText("Test 1");
//		ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(m1);
//
//		C3Message m2 = new C3Message(C3MESSAGES.WARNING_BLACKBOX_TEAMS_INVALID);
//		m2.setType(C3MESSAGETYPES.CLOSE);
//		m2.setText("Test 2");
//		ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(m2);
//
//		C3Message m3 = new C3Message(C3MESSAGES.WARNING_BLACKBOX_TEAMS_INVALID);
//		m3.setType(C3MESSAGETYPES.CLOSE);
//		m3.setText("Test 3");
//		ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(m3);
	}

	private void showMessage(C3Message message) {
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("5");
		Nexus.oneOrMoreMessageOpen.set(true);
		Platform.runLater(() -> {
			for (C3MessagePane mp : messagePaneStack) {
				if (message.equals(mp.getText())) {
					return;
				}
			}

			C3MessagePane messagePane = new C3MessagePane(message);
			Tools.playGUICreationSound();
			mouseStopper.getChildren().add(messagePane);
			messagePaneStack.add(messagePane);
			messagePane.fadeIn();
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("5");
		});
	}

	private void showMedal(Image image, String desc) {
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("6");
		Platform.runLater(() -> {
			medalPane = new C3MedalPane(image, desc);
			Tools.playGUICreationSound();
			mouseStopper.getChildren().add(medalPane);
			medalPane.fadeIn();
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("6");
		});
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

	private void showPopup(Image image, String desc, String colorCode) {
		Platform.runLater(() -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("7");
			mouseStopper.getChildren().remove(popupPane);

			popupPane = new C3PopupPane(image, desc, colorCode);
			Tools.playGUICreationSound();
			mouseStopper.getChildren().add(popupPane);
			popupPane.fadeIn();
		});
	}

	private void closeMessage(C3Message message) {
		C3MESSAGERESULTS userReactionResult = message.getResult();
		logger.info("USER ANSWERED '" + userReactionResult + "' to MESSAGE '" + message.getMessage() + "'");

		if (message.getMessage() == C3MESSAGES.DOWNLOAD_CLIENT) {
			if (C3MESSAGERESULTS.YES.equals(userReactionResult)) {
				ActionManager.getAction(ACTIONS.OPEN_CLIENTVERSION_DOWNLOADPAGE).execute();
			} else if (C3MESSAGERESULTS.NO.equals(userReactionResult)) {
				logger.info("The latest version should be installed in any situation!");
			}
		}

		if (message.getMessage() == C3MESSAGES.ERROR_SERVER_CONNECTION_LOST) {
			Logout.doLogout();
		}

		C3MessagePane topMessage = messagePaneStack.getLast();
		Platform.runLater(() -> mouseStopper.getChildren().remove(topMessage));
		messagePaneStack.remove(topMessage);
		if (messagePaneStack.size() == 0) {
			Nexus.oneOrMoreMessageOpen.set(false);
		}
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("8");
	}
}
