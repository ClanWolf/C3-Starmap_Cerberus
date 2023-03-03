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
package net.clanwolf.starmap.client.gui.panes.rp;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.client.mwo.CheckClipboardForMwoApi;
import net.clanwolf.starmap.client.mwo.ResultAnalyzer;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.RPVarReplacer_DE;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;
import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Undertaker
 */
public class RolePlayInvasionPaneController extends AbstractC3RolePlayController implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@FXML
	private AnchorPane anchorPane;

	//@FXML
	//private ImageView rpIBackgroundImage;

	@FXML
	private ImageView rpImage;

	@FXML
	private ImageView confirmAttacker1, confirmAttacker2, confirmAttacker3, confirmAttacker4;

	@FXML
	private ImageView confirmDefender1, confirmDefender2, confirmDefender3, confirmDefender4;

	@FXML
	private ImageView attackerHeader, defenderHeader;

	@FXML
	private TextArea taRpText;

	@FXML
	private Button btChoice1;

	@FXML
	private Button btChoice2;

	@FXML
	private Button btChoice3;

	@FXML
	private Button btChoice4;

	@FXML
	private Pane paneCurrentScore;

	@FXML
	private ImageView defenderButtonIcon;

	@FXML
	private ImageView attackerButtonIcon;

	@FXML
	private ImageView ivDefenderWaiting;

	@FXML
	private ImageView ivAttackerWaiting;

	private final Image imageUnselected = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/check.png")));
	private final Image imageSelected = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/checked.png")));

	@FXML
	private Circle circleScore01, circleScore02, circleScore03, circleScore04, circleScore05;
	ArrayList<Circle> scoreCircles = null;
	@FXML
	private ImageView ivPlanet, ivLocation;

	private Integer attackerDropVictories = 0;
	private Integer defenderDropVictories = 0;
	Image imageStatic = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/gui/static.gif")));
	@FXML
	private ImageView ivStatic;
	@FXML
	private VBox VBoxError;
	@FXML
	private Label labelError;
	private Image im2;

	public RolePlayInvasionPaneController() {
	}

	private void init() {
		taRpText.setStyle("-fx-opacity: 1");
		taRpText.setEditable(false);

		btChoice1.setVisible(false);
		btChoice2.setVisible(false);
		btChoice3.setVisible(false);
		btChoice4.setVisible(false);

		btChoice1.setDisable(true);
		btChoice2.setDisable(true);
		btChoice3.setDisable(true);
		btChoice4.setDisable(true);

		confirmAttacker1.setVisible(false);
		confirmAttacker2.setVisible(false);
		confirmAttacker3.setVisible(false);
		confirmAttacker4.setVisible(false);

		confirmDefender1.setVisible(false);
		confirmDefender2.setVisible(false);
		confirmDefender3.setVisible(false);
		confirmDefender4.setVisible(false);

		confirmAttacker1.setImage(imageUnselected);
		confirmAttacker2.setImage(imageUnselected);
		confirmAttacker3.setImage(imageUnselected);
		confirmAttacker4.setImage(imageUnselected);

		confirmDefender1.setImage(imageUnselected);
		confirmDefender2.setImage(imageUnselected);
		confirmDefender3.setImage(imageUnselected);
		confirmDefender4.setImage(imageUnselected);

		attackerHeader.setVisible(true);
		defenderHeader.setVisible(true);

		if (!Nexus.isMwoCheckingActive()) {
			if (C3Properties.getBoolean(C3PROPS.GENERALS_CLIPBOARD_API)) {
				Clipboard cb = Clipboard.getSystemClipboard();
				cb.clear();

				Timer checkSystemClipboardForMWOResultTimer = new Timer();
				checkSystemClipboardForMWOResultTimer.schedule(new CheckClipboardForMwoApi(), 0, 2000);
				Nexus.setCheckSystemClipboardForMWOResultTimer(checkSystemClipboardForMWOResultTimer);
				Nexus.setMWOCheckingActive(true);
			} else {
				logger.info(Internationalization.getString("app_checkclipboarddisable"));
			}
		}

		BOAttack a = Nexus.getCurrentAttackOfUser();
		BOFaction attacker = Nexus.getBoUniverse().getFactionByID(a.getAttackerFactionId().longValue());
		BOFaction defender = Nexus.getBoUniverse().getFactionByID(a.getDefenderFactionId().longValue());

		Image attackerLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/factions/" + attacker.getLogo())));
		Image defenderLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/factions/" + defender.getLogo())));

		ivPlanet.setImage(Nexus.getBoUniverse().starSystemBOs.get(a.getStarSystemId()).getSystemImage());
		ivPlanet.toFront();
		ivLocation.toFront();
		attackerHeader.setImage(attackerLogo);
		defenderHeader.setImage(defenderLogo);
		attackerButtonIcon.setImage(attackerLogo);
		defenderButtonIcon.setImage(defenderLogo);
	}

	public void statusUpdate() {
		Platform.runLater(() -> {
			BOAttack att = Nexus.getCurrentAttackOfUser();
			if (att == null) {
				att = Nexus.getFinishedAttackInThisRoundForUser();
			}

			if (att != null && att.getStoryId() != null) {
				confirmAttacker1.setImage(imageUnselected);
				confirmAttacker2.setImage(imageUnselected);
				confirmAttacker3.setImage(imageUnselected);
				confirmAttacker4.setImage(imageUnselected);

				confirmDefender1.setImage(imageUnselected);
				confirmDefender2.setImage(imageUnselected);
				confirmDefender3.setImage(imageUnselected);
				confirmDefender4.setImage(imageUnselected);

				for (AttackCharacterDTO ac : att.getAttackCharList()) {
					if (ac.getType().equals(Constants.ROLE_ATTACKER_COMMANDER)) {
						if (ac.getNextStoryId() != null && ac.getNextStoryId() != att.getStoryId().longValue()) {
							ivAttackerWaiting.setVisible(false);
							if (ac.getSelectedAttackerWon() != null && ac.getSelectedAttackerWon()) {
								confirmAttacker1.setImage(imageSelected);
								confirmAttacker2.setImage(imageUnselected);
							} else if (ac.getSelectedDefenderWon() != null && ac.getSelectedDefenderWon()) {
								confirmAttacker1.setImage(imageUnselected);
								confirmAttacker2.setImage(imageSelected);
							}
						}
					}
					if (ac.getType().equals(Constants.ROLE_DEFENDER_COMMANDER)) {
						if (ac.getNextStoryId() != null && ac.getNextStoryId() != att.getStoryId().longValue()) {
							ivDefenderWaiting.setVisible(false);
							if (ac.getSelectedAttackerWon() != null && ac.getSelectedAttackerWon()) {
								confirmDefender1.setImage(imageSelected);
								confirmDefender2.setImage(imageUnselected);
							} else if (ac.getSelectedDefenderWon() != null && ac.getSelectedDefenderWon()) {
								confirmDefender1.setImage(imageUnselected);
								confirmDefender2.setImage(imageSelected);
							}
						}
					}
				}
			}
		});
	}

	@Override
	public void setStrings() {
		Platform.runLater(() -> {
			// set strings
		});
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.FINALIZE_ROUND, this);
		ActionManager.addActionCallbackListener(ACTIONS.START_ROLEPLAY, this);
		ActionManager.addActionCallbackListener(ACTIONS.MWO_DROPSTATS_RECEIVED, this);
		ActionManager.addActionCallbackListener(ACTIONS.UPDATE_USERS_FOR_ATTACK, this);
		ActionManager.addActionCallbackListener(ACTIONS.ROLEPLAY_NEXT_STEP_CHANGE_PANE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_BEGINS, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURRENT_ATTACK_IS_BROKEN, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURRENT_ATTACK_IS_BROKEN_WARNING, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURRENT_ATTACK_IS_BROKEN_KILLED, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURRENT_ATTACK_IS_HEALED, this);
	}

	/******************************** THIS ********************************/

	@Override
	public void getStoryValues(RolePlayStoryDTO rpStory) {
		if (rpStory.getStoryIntro() == null) {
			//set background image
			Image im = BORolePlayStory.getRPG_Image(null);
			backgroundImage.setImage(im);

			//set story image
			im2 = BORolePlayStory.getRPG_Image(rpStory);
			rpImage.setImage(im2);
			rpImage.toFront();
			ivStatic.setOpacity(0.45);
			ivStatic.toFront();

			paneCurrentScore.setVisible(false);
		}

		// play sound
		C3SoundPlayer.playRPSound(Objects.requireNonNull(BORolePlayStory.getRPG_Soundfile(rpStory)), audioStartedOnce);
		audioStartedOnce = true;

		// REPLACE VARS!
		taRpText.setText(RPVarReplacer_DE.replaceVars(rpStory.getStoryText()));

		if (rpStory.getVar9ID() != null) {
			RolePlayStoryVar9DTO rpVar9 = rpStory.getVar9ID();

			double x = 59;
			double y = 452;
			double offset = 40;

			double x2 = 768;
			double x3 = 806;

			attackerDropVictories = rpVar9.getAttackerDropVictories();
			defenderDropVictories = rpVar9.getDefenderDropVictories();
			if (attackerDropVictories != null && defenderDropVictories != null) {
				scoreAnimation(attackerDropVictories, defenderDropVictories);
			} else {
				logger.error("Current score is not available!");
				scoreAnimation(1, 1);
			}

			// rpVar9
			if (rpVar9.getOption4StoryID() != null) {
				btChoice4.setVisible(true);
				confirmAttacker4.setVisible(true);
				confirmDefender4.setVisible(true);

				btChoice4.setLayoutX(x);
				btChoice4.setLayoutY(y);

				confirmAttacker4.setLayoutX(x2);
				confirmAttacker4.setLayoutY(y);

				confirmDefender4.setLayoutX(x3);
				confirmDefender4.setLayoutY(y);

				y = y - offset;

				btChoice4.setText(rpVar9.getOption4Text());
			}

			if (rpVar9.getOption3StoryID() != null) {
				btChoice3.setVisible(true);
				confirmAttacker3.setVisible(true);
				confirmDefender3.setVisible(true);

				btChoice3.setLayoutX(x);
				btChoice3.setLayoutY(y);

				confirmAttacker3.setLayoutX(x2);
				confirmAttacker3.setLayoutY(y);

				confirmDefender3.setLayoutX(x3);
				confirmDefender3.setLayoutY(y);

				y = y - offset;

				btChoice3.setText(rpVar9.getOption3Text());
			}

			if (rpVar9.getOption2StoryID() != null) {
				btChoice2.setVisible(true);
				confirmAttacker2.setVisible(true);
				confirmDefender2.setVisible(true);

				btChoice2.setLayoutX(x);
				btChoice2.setLayoutY(y);

				confirmAttacker2.setLayoutX(x2);
				confirmAttacker2.setLayoutY(y);

				confirmDefender2.setLayoutX(x3);
				confirmDefender2.setLayoutY(y);

				defenderButtonIcon.setLayoutY(y + 4);
				//					defenderButtonIcon.setVisible(true);

				y = y - offset;

				btChoice2.setText(rpVar9.getOption2Text());
			}

			if (rpVar9.getOption1StoryID() != null) {
				btChoice1.setVisible(true);
				confirmAttacker1.setVisible(true);
				confirmDefender1.setVisible(true);

				btChoice1.setLayoutX(x);
				btChoice1.setLayoutY(y);

				confirmAttacker1.setLayoutX(x2);
				confirmAttacker1.setLayoutY(y);

				confirmDefender1.setLayoutX(x3);
				confirmDefender1.setLayoutY(y);

				btChoice1.setText(rpVar9.getOption1Text());

				attackerButtonIcon.setLayoutY(y + 4);
				//					attackerButtonIcon.setVisible(true);
			}

			double yPos = y - offset - 5;

			attackerHeader.setLayoutX(x2);
			attackerHeader.setLayoutY(yPos);

			defenderHeader.setLayoutX(x3);
			defenderHeader.setLayoutY(yPos);

			ivAttackerWaiting.setLayoutY(y - 25);
			ivDefenderWaiting.setLayoutY(y - 25);

			paneCurrentScore.setLayoutY(yPos);

			btChoice1.setText(RPVarReplacer_DE.replaceVars(btChoice1.getText()));
			btChoice2.setText(RPVarReplacer_DE.replaceVars(btChoice2.getText()));
			btChoice3.setText(RPVarReplacer_DE.replaceVars(btChoice3.getText()));
			btChoice4.setText(RPVarReplacer_DE.replaceVars(btChoice4.getText()));
		}
	}

	/**
	 * Handle Actions
	 *
	 * @param action Action
	 * @param o      Action object
	 * @return true
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		if (anchorPane != null && !anchorPane.isVisible()) return true;
		logger.info("Flag for CharRP" + isCharRP);
		switch (action) {
			case CURRENT_ATTACK_IS_BROKEN:
				if (o.getObject() instanceof Long) {
					Long timerStart = (Long) o.getObject();
					Platform.runLater(() -> {
						labelError.setText("...");
						VBoxError.setVisible(true);
					});
				}
				break;

			case CURRENT_ATTACK_IS_BROKEN_WARNING:
				if (o.getObject() instanceof Long) {
					Long timerStart = (Long) o.getObject();
					Platform.runLater(() -> {
						labelError.setText("...");
						VBoxError.setVisible(true);
					});
				}
				break;

			case CURRENT_ATTACK_IS_BROKEN_KILLED:
				if (o.getObject() instanceof Long) {
					Long timerStart = (Long) o.getObject();
					Platform.runLater(() -> {
						labelError.setText("...");
						VBoxError.setVisible(true);
					});
				}
				break;

			case CURRENT_ATTACK_IS_HEALED:
				if (o.getObject() instanceof Long) {
					Long timerStart = (Long) o.getObject();
					Platform.runLater(() -> {
						labelError.setText("...");
						VBoxError.setVisible(false);
					});
				}
				break;

			case PANE_CREATION_BEGINS:
				ivStatic.setOpacity(0.45);
				ivStatic.setImage(imageStatic);
				paneCurrentScore.setVisible(false);
				break;

			case FINALIZE_ROUND:
				checkToCancelInvasion();
				break;

			case START_ROLEPLAY:
				if (ROLEPLAYENTRYTYPES.C3_RP_STEP_V9 == o.getObject()) {
					logger.info("RolePlayChoicePaneController -> START_ROLEPLAY");

					init();

					// set current step of story
					getStoryValues(getCurrentRP());
				}
				break;
			case MWO_DROPSTATS_RECEIVED:
				MWOMatchResult result = (MWOMatchResult) o.getObject();
				ResultAnalyzer.analyseAndStoreMWOResult(result, true);
				break;
			case UPDATE_USERS_FOR_ATTACK:
				statusUpdate();
				break;
			case ROLEPLAY_NEXT_STEP_CHANGE_PANE:
				Platform.runLater(() -> {
					Image imageSelected = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/checked_confirmed.png")));
					AttackDTO a = (AttackDTO) o.getObject();
					boolean attackerWon = false;
					boolean defenderWon = false;
					boolean iAmDefender = false;
					for (AttackCharacterDTO c : a.getAttackCharList()) {
						if (c.getSelectedAttackerWon() != null && c.getSelectedAttackerWon()) {
							attackerWon = true;
						} else if (c.getSelectedDefenderWon() != null && c.getSelectedDefenderWon()) {
							defenderWon = true;
						}

						if (Nexus.getCurrentUser().getCurrentCharacter().getId().equals(c.getCharacterID())) {
							// This is my own (logged in) user

							// 0L Attacker Warrior
							// 1L Attacker Commander
							// 2L Defender
							// 3L Defender Commander
							// 4L Supporter
							// 5L Attacker Supporter
							// 6L Defender Supporter
							if (c.getType() == 0L || c.getType() == 1L || c.getType() == 5L) {
								iAmDefender = false;
							} else if (c.getType() == 2L || c.getType() == 3L || c.getType() == 6L) {
								iAmDefender = true;
							}
						}
					}
					if (attackerWon) {
						confirmAttacker1.setImage(imageSelected);
						confirmDefender1.setImage(imageSelected);
						btChoice1.setDisable(true);
						btChoice2.setDisable(true);
						btChoice3.setDisable(true);
						btChoice4.setDisable(true);
					} else if (defenderWon) {
						confirmAttacker2.setImage(imageSelected);
						confirmDefender2.setImage(imageSelected);
						btChoice1.setDisable(true);
						btChoice2.setDisable(true);
						btChoice3.setDisable(true);
						btChoice4.setDisable(true);
					}

					RolePlayStoryDTO story = Nexus.getBoUniverse().getAttackStoriesByID(a.getStoryID());

					// Here we play the motivation voice for the last fight
					// Generic or faction specific
					int randomNum = 0;
					String sampleName = "";

					boolean defWon = story.getDefenderWins() != null && story.getDefenderWins();
					boolean attWon = story.getAttackerWins() != null && story.getAttackerWins();

					if (iAmDefender) { // I am defender
						logger.info("We are defender!");
						if (defenderWon) {
							logger.info("We won!");
							if (defWon) { // Invasion is over, defender (we) won the invasion
								randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);
								sampleName = "rp_sample_won_invasion_0" + randomNum + "_general.mp3";
							} else if (attWon) { // Invasion is over, attacker (the others) won the invasion
								randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);
								sampleName = "rp_sample_lost_invasion_0" + randomNum + "_general.mp3";
							} else { // no side did win the invasion yet, still fighting
								randomNum = ThreadLocalRandom.current().nextInt(1, 3 + 1);
								sampleName = "rp_sample_won_drop_0" + randomNum + "_general.mp3";
							}
						} else {
							logger.info("We lost!");
							if (defWon) { // Invasion is over, defender (we) lost the invasion
								randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);
								sampleName = "rp_sample_lost_invasion_0" + randomNum + "_general.mp3";
							} else if (attWon) { // Invasion is over, we won, attacker (the others) lost the invasion
								randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);
								sampleName = "rp_sample_won_invasion_0" + randomNum + "_general.mp3";
							} else { // no side did win the invasion yet, still fighting
								randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);
								sampleName = "rp_sample_lost_drop_0" + randomNum + "_general.mp3";
							}
						}
					} else { // I am attacker
						logger.info("We are attacker!");
						if (attackerWon) {
							logger.info("We won!");
							if (defWon) { // Invasion is over, defender (we) won the invasion
								randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);
								sampleName = "rp_sample_lost_invasion_0" + randomNum + "_general.mp3";
							} else if (attWon) { // Invasion is over, attacker (the others) won the invasion
								randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);
								sampleName = "rp_sample_won_invasion_0" + randomNum + "_general.mp3";
							} else { // no side did win the invasion yet, still fighting
								randomNum = ThreadLocalRandom.current().nextInt(1, 3 + 1);
								sampleName = "rp_sample_won_drop_0" + randomNum + "_general.mp3";
							}
						} else {
							logger.info("We lost!");
							if (defWon) { // Invasion is over, defender (we) lost the invasion
								randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);
								sampleName = "rp_sample_lost_invasion_0" + randomNum + "_general.mp3";
							} else if (attWon) { // Invasion is over, we won, attacker (the others) lost the invasion
								randomNum = ThreadLocalRandom.current().nextInt(1, 2 + 1);
								sampleName = "rp_sample_won_invasion_0" + randomNum + "_general.mp3";
							} else { // no side did win the invasion yet, still fighting
								randomNum = ThreadLocalRandom.current().nextInt(1, 4 + 1);
								sampleName = "rp_sample_lost_drop_0" + randomNum + "_general.mp3";
							}
						}
					}
					String finalSampleName = sampleName;
					double volume = C3SoundPlayer.getRpPlayer().getVolume();
					Timeline timeline = new Timeline(new KeyFrame(Duration.millis(750), new KeyValue(C3SoundPlayer.getRpPlayer().volumeProperty(), 0)));
					timeline.setOnFinished(event -> {
						C3SoundPlayer.getRpPlayer().stop();
						C3SoundPlayer.getRpPlayer().setVolume(volume);
						C3SoundPlayer.play("sound/voice/" + Internationalization.getLanguage() + "/rp_invasion/" + finalSampleName, false);
					});
					timeline.play();

					audioStartedOnce = false;
				});
				break;
			default:
				break;
		}
		return true;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		init();
	}

	public void scoreAnimation(int attackerWins, int defenderWins) {
		Platform.runLater(() -> {
			if (scoreCircles == null) {
				scoreCircles = new ArrayList<>();
				scoreCircles.add(circleScore01);
				scoreCircles.add(circleScore02);
				scoreCircles.add(circleScore03);
				scoreCircles.add(circleScore04);
				scoreCircles.add(circleScore05);
			}

			paneCurrentScore.setVisible(true);

			SequentialTransition sequentialTransition = new SequentialTransition();

			int count = 1;
			for (Circle c : scoreCircles) {
				FadeTransition fadeInTransition = new FadeTransition(Duration.millis(250), c);
				fadeInTransition.setFromValue(0.0);
				fadeInTransition.setToValue(1.0);
				fadeInTransition.setCycleCount(1);

				if (count <= attackerWins) {
					// color this circle red
					c.setStroke(Color.web("#a2270c"));
					c.setFill(Color.web("#511d14"));
				} else if (count > 5 - defenderWins) {
					// color this circle blue
					c.setStroke(Color.web("#6292a4"));
					c.setFill(Color.web("#113544"));
				} else {
					// color this circle gray
					c.setStroke(Color.web("#ffffff"));
					c.setFill(Color.web("#5d6165"));
				}
				sequentialTransition.getChildren().add(fadeInTransition);
				count++;
			}
			FadeTransition fadeInTransitionStatic = new FadeTransition(Duration.millis(2500), ivStatic);
			fadeInTransitionStatic.setFromValue(0.45);
			fadeInTransitionStatic.setToValue(0.0);
			fadeInTransitionStatic.setCycleCount(1);
			sequentialTransition.getChildren().add(fadeInTransitionStatic);

			sequentialTransition.setCycleCount(1);
			sequentialTransition.setOnFinished((ActionEvent event) -> {
				for (AttackCharacterDTO ac : Nexus.getCurrentAttackOfUser().getAttackCharList()) {
					if (ac.getCharacterID().equals(Nexus.getCurrentChar().getId())) {
						// this is my own attackchar
						if (ac.getType().equals(Constants.ROLE_ATTACKER_COMMANDER) || ac.getType().equals(Constants.ROLE_DEFENDER_COMMANDER)) {
							// I am defender commander or attacker commander
							btChoice1.setDisable(false);
							btChoice2.setDisable(false);
							btChoice3.setDisable(false);
							btChoice4.setDisable(false);
						} else {
							btChoice1.setTooltip(new Tooltip(Internationalization.getString("app_rp_invasion_waiting_for_confirmation")));
							btChoice2.setTooltip(new Tooltip(Internationalization.getString("app_rp_invasion_waiting_for_confirmation")));
							btChoice3.setTooltip(new Tooltip(Internationalization.getString("app_rp_invasion_waiting_for_confirmation")));
							btChoice4.setTooltip(new Tooltip(Internationalization.getString("app_rp_invasion_waiting_for_confirmation")));
						}
					}
				}
				ivAttackerWaiting.setVisible(true);
				ivDefenderWaiting.setVisible(true);
				rpImage.setImage(im2);

				statusUpdate();
			});

			// move location marker for some time and flash it 10 times at the end (on the planet image)
			SequentialTransition sequentialTransition2 = new SequentialTransition();
			Path path = new Path();
			MoveTo moveTo = new MoveTo(10.0f, 10.0f);
			path.getElements().add(moveTo);

			for (int i = 1; i < 4; i++) {
				int min = 0;
				int max = 50;
				Random random = new Random();
				int value1 = (random.nextInt(max + min) + min) - 25;
				int value2 = (random.nextInt(max + min) + min) - 25;
				LineTo lineTo = new LineTo(value1, value2);
				path.getElements().add(lineTo);
			}

			PathTransition pathTransition = new PathTransition();
			pathTransition.setDuration(Duration.millis(1000));
			pathTransition.setPath(path);
			pathTransition.setNode(ivLocation);
			pathTransition.setOrientation(PathTransition.OrientationType.NONE);
			pathTransition.setCycleCount(2);
			pathTransition.setAutoReverse(true);
			sequentialTransition2.getChildren().add(pathTransition);

			FadeTransition fadeInTransitionLocation = new FadeTransition(Duration.millis(120), ivLocation);
			fadeInTransitionLocation.setFromValue(1.0);
			fadeInTransitionLocation.setToValue(0.0);
			fadeInTransitionLocation.setCycleCount(12);
			fadeInTransitionLocation.setOnFinished(event -> ivLocation.setOpacity(1.0));
			sequentialTransition2.getChildren().add(fadeInTransitionLocation);

			sequentialTransition.play();
			sequentialTransition2.play();
		});
	}

	public void saveNextInvasionStep(Long rp, boolean attackerWon, boolean defenderWon) {
		BOAttack a = Nexus.getCurrentAttackOfUser();
		RolePlayCharacterDTO rpc = Nexus.getCurrentChar();

		//		public static final long ROLE_ATTACKER_WARRIOR = 0L; // 0L Attacker Warrior
		//		public static final long ROLE_ATTACKER_COMMANDER = 1L; // 1L Attacker Commander
		//		public static final long ROLE_DEFENDER_WARRIOR = 2L; // 2L Defender
		//		public static final long ROLE_DEFENDER_COMMANDER = 3L; // 3L Defender Commander
		//		public static final long ROLE_SUPPORTER = 4L; // 4L Attacker Supporter
		//		public static final long ROLE_ATTACKER_SUPPORTER = 5L; // 4L Attacker Supporter
		//		public static final long ROLE_DEFENDER_SUPPORTER = 6L; // 5L Defender Supporter

		Long myType = -1L;
		for (AttackCharacterDTO ac : a.getAttackCharList()) {
			if (ac.getCharacterID().equals(rpc.getId())) {
				ac.setSelectedAttackerWon(attackerWon);
				ac.setSelectedDefenderWon(defenderWon);
				ac.setNextStoryId(rp);
				myType = ac.getType();
				logger.info("Next story id saved for Character " + rpc.getName() + ": " + rp);
				break;
			}
		}
		for (AttackCharacterDTO ac : a.getAttackCharList()) {
			if (myType.equals(Constants.ROLE_ATTACKER_COMMANDER)) {
				if (ac.getType().equals(Constants.ROLE_ATTACKER_WARRIOR) || ac.getType().equals(Constants.ROLE_ATTACKER_SUPPORTER)) {
					ac.setNextStoryId(rp);
				}
			} else if (myType.equals(Constants.ROLE_DEFENDER_COMMANDER)) {
				if (ac.getType().equals(Constants.ROLE_DEFENDER_WARRIOR) || ac.getType().equals(Constants.ROLE_DEFENDER_SUPPORTER)) {
					ac.setNextStoryId(rp);
				}
			}
		}
		a.storeAttack();
	}

	/******************************** FXML ********************************/

	@FXML
	private void handleOnActionbtChoice1() {
		Long rp = getCurrentRP().getVar9ID().getOption1StoryID();
		saveNextInvasionStep(rp, true, false);
		logger.info("Choice1: Attacker won, defender lost. Target rp id: " + rp);
	}

	@FXML
	private void handleOnActionbtChoice2() {
		Long rp = getCurrentRP().getVar9ID().getOption2StoryID();
		saveNextInvasionStep(rp, false, true);
		logger.info("Choice2: Attacker lost, defender won. Target rp id: " + rp);
	}

	@FXML
	private void handleOnActionbtChoice3() {
		Long rp = getCurrentRP().getVar9ID().getOption3StoryID();
		saveNextInvasionStep(rp, false, false);
	}

	@FXML
	private void handleOnActionbtChoice4() {
		Long rp = getCurrentRP().getVar9ID().getOption4StoryID();
		saveNextInvasionStep(rp, false, false);
	}
}
