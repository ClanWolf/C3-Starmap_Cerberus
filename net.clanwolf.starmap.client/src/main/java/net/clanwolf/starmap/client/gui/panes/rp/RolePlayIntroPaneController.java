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
package net.clanwolf.starmap.client.gui.panes.rp;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayIntroPaneController extends AbstractC3RolePlayController implements ActionCallBackListener {

	private HashMap<Long, Boolean> animationPlayedMap = new HashMap<>();

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Label labHeader;

	@FXML
	private Button btPreview;

	@FXML
	private TextArea taStoryText;

	public RolePlayIntroPaneController() {
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.START_ROLEPLAY, this);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
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
		switch (action) {

		case START_ROLEPLAY:
			if(ROLEPLAYENTRYTYPES.C3_RP_STORY == o.getObject() ||
					ROLEPLAYENTRYTYPES.C3_RP_CHAPTER == o.getObject()) {
				C3Logger.debug("RolePlayIntroPaneController -> START_ROLEPLAY");

				// set current step of story
				getStoryValues(getCurrentRP());

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
			} else if(ROLEPLAYENTRYTYPES.C3_RP_STEP_V1 == o.getObject()){
				// set current step of story
				getStoryValues(getCurrentRP());
			}
			break;
		default:
			break;

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
	@FXML
	private void handleOnMouseClicked(){
		handleOnActionBtPreview();
	}

	@FXML
	private void handleOnActionBtPreview(){
		//TODO: Change the methods for C3_RP_STORY and C3_RP_CHAPTER of attack, otherwise it dosen't works
		RolePlayCharacterDTO currentChar = Nexus.getCurrentChar();
		if(getCurrentRP().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY ) {
			boRp.getNextChapterBySortOrder(currentChar, 1);
		} else if(getCurrentRP().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_CHAPTER ) {
			if(isCharRP) {
				boRp.getNextStepBySortOrder(currentChar, 1);
			} else {
				BORolePlayStory boRp = new BORolePlayStory();
				Long rp = boRp.getFirstStepOfChapter(getCurrentRP());
				saveNextStep(rp);
			}
		} else {
//		if(getCurrentRP().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1 ){
			Long rp = getCurrentRP().getNextStepID();
			saveNextStep(rp);
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

		// fade text from labHeader if step is story or chapter
		if(rpStory.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1){
			taStoryText.setVisible(true);
			labHeader.setVisible(false);

			taStoryText.setText(rpStory.getStoryText());

		} else {
			taStoryText.setVisible(false);

			labHeader.setEffect(new DropShadow(20, Color.BLACK));
			//Boolean animationPlayed = animationPlayedMap.get(Nexus.getCurrentChar().getStory().getId());
			Boolean animationPlayed = animationPlayedMap.get(getCurrentRP().getId());
			if (animationPlayed == null || !animationPlayed) {
				labHeader.setOpacity(0.0);
			}
			labHeader.setVisible(true);
			labHeader.setText(rpStory.getStoryName());
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
//			C3SoundPlayer.play(BORolePlayStory.getRPG_Soundfile(rpStory), false);
			C3SoundPlayer.playRPSound(BORolePlayStory.getRPG_Soundfile(rpStory));
		}
	} //getStoryValues
}
