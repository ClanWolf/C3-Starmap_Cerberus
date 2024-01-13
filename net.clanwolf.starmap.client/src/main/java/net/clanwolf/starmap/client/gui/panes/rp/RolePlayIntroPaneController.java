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
 * Copyright (c) 2001-2024, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes.rp;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.RPVarReplacer_DE;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.transfer.dtos.AttackCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayIntroPaneController extends AbstractC3RolePlayController implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private HashMap<Long, Boolean> animationPlayedMap = new HashMap<>();

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Label labHeader;

	@FXML
	private Button btPreview;

	@FXML
	private TextArea taStoryText;

	@FXML
	private MediaView backgroundMediaView;

	private boolean buttonPressed = false;

	public RolePlayIntroPaneController() {
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.START_ROLEPLAY, this);
		ActionManager.addActionCallbackListener(ACTIONS.FINALIZE_ROUND, this);
	}


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		buttonPressed = false;
	}

	private void fadeInContent() {
		Platform.runLater(() -> {
			Boolean animationPlayed = animationPlayedMap.get(getCurrentRP().getId());
			if (animationPlayed == null || !animationPlayed) {
				FadeTransition fadeInTransition_01 = new FadeTransition(Duration.millis(800), backgroundImage);
				fadeInTransition_01.setFromValue(0.0);
				fadeInTransition_01.setToValue(1.0);
				fadeInTransition_01.setCycleCount(1);

				FadeTransition fadeInTransition_03 = new FadeTransition(Duration.millis(1500), labHeader);
				fadeInTransition_03.setFromValue(0.0);
				fadeInTransition_03.setToValue(1.0);
				fadeInTransition_03.setCycleCount(1);

				SequentialTransition sequentialTransition = new SequentialTransition();
				sequentialTransition.getChildren().addAll(fadeInTransition_01, fadeInTransition_03);
				sequentialTransition.setCycleCount(1);
				sequentialTransition.play();

				animationPlayedMap.put(getCurrentRP().getId(), true);
			}
		});
	}
	/**
	 * Handle Actions
	 *
	 * @param action
	 *            Action
	 * @param o
	 *            Action object
	 * @return true
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		if(anchorPane != null && !anchorPane.isVisible()) return true;

		if (!buttonPressed) {
			if (ROLEPLAYENTRYTYPES.C3_RP_STEP_V1 != o.getObject()) { // NOT a Normal story step (so likely intro or invasion pane, outro of an invasion)
				if (!isCharRP) {
					BOAttack attack = Nexus.getCurrentAttackOfUser();
					if (attack != null) {
						if (attack.getAttackCharList() != null) {
							for (AttackCharacterDTO c : attack.getAttackCharList()) {
								if (c.getCharacterID().equals(Nexus.getCurrentChar().getId())) {
									btPreview.setDisable(!c.getType().equals(Constants.ROLE_ATTACKER_COMMANDER));
								}

								// This could happen outside the loop at the very start (?)
								// This controller is used for the first pane of an invasion (intro) AND the last one
								// (outro). In the first, the continue button may only be enabled for dropleadAttacker
								// in the last, it should be enabled for everyone... so everyone can decide for themselves
								// to leave the current invasion.
								if ((getCurrentRP().getAttackerWins() != null && getCurrentRP().getAttackerWins()) || (getCurrentRP().getDefenderWins() != null && getCurrentRP().getDefenderWins())) {
									logger.info("Enabling the 'Next'-Button on current RP pane!");
									btPreview.setDisable(false);
								}
							}
						}
					}
				} else {
					btPreview.setDisable(false);
				}
			} else if (getCurrentRP() != null && getCurrentRP().getNextStepID() == null) {
				btPreview.setDisable(false);
			}
		}

		switch (action) {
			case START_ROLEPLAY -> {
				if (ROLEPLAYENTRYTYPES.C3_RP_STORY == o.getObject() || ROLEPLAYENTRYTYPES.C3_RP_CHAPTER == o.getObject()) {
					logger.info("RolePlayIntroPaneController -> START_ROLEPLAY");
					// set current step of story
					getStoryValues(getCurrentRP());
					fadeInContent();
				} else if (ROLEPLAYENTRYTYPES.C3_RP_STEP_V1 == o.getObject()) {
					// set current step of story
					getStoryValues(getCurrentRP());
					fadeInContent();
				}
				buttonPressed = false;
			}
			case FINALIZE_ROUND -> checkToCancelInvasion();

			case ROLEPLAY_NEXT_STEP_CHANGE_PANE -> {
				// This event is not handled here, motivation sounds are played in RolePlayInvasionPaneController !!!
			}

			default -> {
			}
		}
		return true;
	}

	@Override
	public void setStrings() {
		Platform.runLater(() -> {
			// set strings
		});
	}

	/******************************** FXML ********************************/
//	@FXML
//	private void handleOnMouseClicked(){
//		handleOnActionBtPreview();
//	}

	@FXML
	private void handleOnActionBtPreview(){
		btPreview.setDisable(true);
		buttonPressed = true;

		// TODO_C3: Change the methods for C3_RP_STORY and C3_RP_CHAPTER of attack, otherwise it dosen't work
		RolePlayCharacterDTO currentChar = Nexus.getCurrentChar();
		if (getCurrentRP() != null) {
			if (getCurrentRP().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY) {
				boRp.getNextChapterBySortOrder(currentChar, 1);
			} else if (getCurrentRP().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_CHAPTER) {
				if (isCharRP) {
					boRp.getNextStepBySortOrder(currentChar, 1);
				} else {
					BORolePlayStory boRp = new BORolePlayStory();
					Long rp = boRp.getFirstStepOfChapter(getCurrentRP());
					saveNextStep(rp);
				}
			} else {
				//		if(getCurrentRP().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1 ){
				Long rp = getCurrentRP().getNextStepID();
				if (rp != null) {
					saveNextStep(rp);
				} else {
					ActionManager.getAction(ACTIONS.SWITCH_TO_MAP).execute();
				}
			}
		} else {
			ActionManager.getAction(ACTIONS.SWITCH_TO_MAP).execute();
		}
	}

	/******************************** THIS ********************************/

	@Override
	public void getStoryValues(RolePlayStoryDTO rpStory){

		double xOffset = 62;
		double yOffset = 14;

		// set postion and size from textarea
		if(rpStory.getxPosText() != null &&
				rpStory.getyPosText() != null &&
				rpStory.getWidthText() != null &&
				rpStory.getHeightText() != null){

			taStoryText.setLayoutX(rpStory.getxPosText().doubleValue() + xOffset);
			taStoryText.setLayoutY(rpStory.getyPosText().doubleValue() + yOffset);
			taStoryText.setPrefSize(rpStory.getWidthText().doubleValue(),rpStory.getHeightText().doubleValue());

		} else {

			taStoryText.setLayoutX(xOffset);
			taStoryText.setLayoutY(yOffset);
			taStoryText.setPrefSize(778,438);
		}

		String buttonText = rpStory.getButtonText();
		if (buttonText != null) {
			btPreview.setText(buttonText);
		} else {
			btPreview.setText(Internationalization.getString("C3_Lobby_Next"));
		}

		// fade text from labHeader if step is story or chapter
		if(rpStory.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1){
			taStoryText.setVisible(true);
			labHeader.setVisible(false);

			taStoryText.setText(RPVarReplacer_DE.replaceVars(rpStory.getStoryText()));
		} else {
			taStoryText.setVisible(false);

			labHeader.setEffect(new DropShadow(20, Color.BLACK));
			//Boolean animationPlayed = animationPlayedMap.get(Nexus.getCurrentChar().getStory().getId());
			Boolean animationPlayed = animationPlayedMap.get(getCurrentRP().getId());
			if (animationPlayed == null || !animationPlayed) {
				labHeader.setOpacity(0.0);
			}
			labHeader.setVisible(true);

			String storyName = rpStory.getStoryName();
			if (storyName != null) {
				storyName = RPVarReplacer_DE.replaceVars(storyName);
			}

			labHeader.setText(storyName);
		}

		// set story image
		Image im = BORolePlayStory.getRPG_Image(rpStory);
		backgroundImage.setImage(im);

		//Boolean animationPlayed = animationPlayedMap.get(Nexus.getCurrentChar().getStory().getId());
		Boolean animationPlayed = animationPlayedMap.get(getCurrentRP().getId());
		if (animationPlayed == null || !animationPlayed) {
			backgroundImage.setOpacity(0.0);
		}

		// play sound
		if (rpStory.getStoryMP3() != null) {
			C3SoundPlayer.playRPSound(Objects.requireNonNull(BORolePlayStory.getRPG_Soundfile(rpStory)), audioStartedOnce);
			audioStartedOnce = true;
		}

		if (rpStory.getStoryIntro() != null) {
			Platform.runLater(() -> {
				try {
					Media video = BORolePlayStory.getRPG_Videofile(rpStory);
					if (video != null) {
						logger.info("Found video for playback!");

						MediaPlayer mp = new MediaPlayer(video);
						mp.setOnError(()-> logger.error("Media error: " + mp.getError().toString()));

						backgroundMediaView.setMediaPlayer(mp);
						backgroundMediaView.setFitHeight(video.getHeight());
						backgroundMediaView.toFront();
						backgroundMediaView.setVisible(true);

						labHeader.toFront();
						taStoryText.toFront();

						// mp.statusProperty().addListener((observable, oldValue, newValue) -> infoLabel.setText(newValue.toString()));
						mp.setOnEndOfMedia(() -> {
							FadeTransition FadeOutTransition = new FadeTransition(Duration.millis(800), backgroundMediaView);
							FadeOutTransition.setFromValue(1.0);
							FadeOutTransition.setToValue(0.0);
							FadeOutTransition.setCycleCount(1);
							FadeOutTransition.setOnFinished((ActionEvent event) -> {
								backgroundMediaView.setVisible(false);
								backgroundMediaView.setMediaPlayer(null);
								backgroundMediaView.toBack();
								backgroundMediaView.setOpacity(1.0);
								logger.info("Stopped video playback");
							});
							FadeOutTransition.play();
						});
						mp.play();
					} else {
						logger.info("Video file download attempt resulted in null (no video).");
					}
				} catch (Exception e) {
					logger.error("Error while downloading video file.", e);
					backgroundMediaView.toBack();
					backgroundMediaView.setVisible(false);
					backgroundMediaView.setMediaPlayer(null);
				}
			});
		} else {
			Platform.runLater(() -> {
				backgroundMediaView.toBack();
				backgroundMediaView.setVisible(false);
				backgroundMediaView.setMediaPlayer(null);
				backgroundImage.toFront();
				taStoryText.toFront();
				labHeader.toFront();
			});
		}
	} //getStoryValues
}
