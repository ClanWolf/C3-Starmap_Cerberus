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

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryVar2DTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryVar9DTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayInvasionPaneController extends AbstractC3RolePlayController implements ActionCallBackListener {

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

	public RolePlayInvasionPaneController() {
	}

	private void init(){
		taRpText.setStyle("-fx-opacity: 1");
		taRpText.setEditable(false);

		btChoice1.setVisible(false);
		btChoice2.setVisible(false);
		btChoice3.setVisible(false);
		btChoice4.setVisible(false);

		confirmAttacker1.setVisible(false);
		confirmAttacker2.setVisible(false);
		confirmAttacker3.setVisible(false);
		confirmAttacker4.setVisible(false);

		confirmDefender1.setVisible(false);
		confirmDefender2.setVisible(false);
		confirmDefender3.setVisible(false);
		confirmDefender4.setVisible(false);

		attackerHeader.setVisible(true);
		defenderHeader.setVisible(true);

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
	}

	/******************************** THIS ********************************/

	@Override
	public void getStoryValues(RolePlayStoryDTO rpStory) {

			if (rpStory.getStoryIntro() == null) {
				//set background image
				Image im = BORolePlayStory.getRPG_Image(null);
				backgroundImage.setImage(im);

				//set story image
				Image im2 = BORolePlayStory.getRPG_Image(rpStory);
				rpImage.setImage(im2);
			}

			// play sound
			if (rpStory.getStoryMP3() != null) {
//				C3SoundPlayer.play(BORolePlayStory.getRPG_Soundfile(rpStory), false);
				C3SoundPlayer.playRPSound(BORolePlayStory.getRPG_Soundfile(rpStory));
			}

			//TODO: append single chars step by step until the whole text is displaying
			taRpText.setText(rpStory.getStoryText());

			if (rpStory.getVar9ID() != null) {

				RolePlayStoryVar9DTO rpVar9 = rpStory.getVar9ID();

				double x = 59;
				double y = 455;
				double offset = 40;

				double x2 = 742;
				double x3 = 791;


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
				}

				attackerHeader.setLayoutX(x2);
				attackerHeader.setLayoutY(y - offset - 10);

				defenderHeader.setLayoutX(x3);
				defenderHeader.setLayoutY(y - offset - 10);
			}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		init();

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
		C3Logger.debug("Flag for CharRP" + isCharRP);
		switch (action) {

		case FINALIZE_ROUND:
			checkToCancelInvasion();
			break;

		case START_ROLEPLAY:
			if(ROLEPLAYENTRYTYPES.C3_RP_STEP_V9 == o.getObject()) {
				C3Logger.debug("RolePlayChoicePaneController -> START_ROLEPLAY");

				init();

				// set current step of story
				getStoryValues(getCurrentRP());
			}
			break;
		default:
			break;
		}
		return true;
	}

	/******************************** FXML ********************************/

	@FXML
	private void handleOnActionbtChoice1(){
		Long rp = getCurrentRP().getVar9ID().getOption1StoryID();
		saveNextStep(rp);
	}

	@FXML
	private void handleOnActionbtChoice2(){
		Long rp = getCurrentRP().getVar9ID().getOption2StoryID();
		saveNextStep(rp);
	}

	@FXML
	private void handleOnActionbtChoice3(){
		Long rp = getCurrentRP().getVar9ID().getOption3StoryID();
		saveNextStep(rp);
	}

	@FXML
	private void handleOnActionbtChoice4(){
		Long rp = getCurrentRP().getVar9ID().getOption4StoryID();
		saveNextStep(rp);
	}
}
