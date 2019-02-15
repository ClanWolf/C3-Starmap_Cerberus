/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : http://www.clanwolf.net                            |
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
package net.clanwolf.starmap.client.gui.panes.rp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayIntroPaneController extends AbstractC3RolePlayController implements ActionCallBackListener {

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Label labHeader;

	@FXML
	private Button btPreview;

	@FXML
	private ImageView ivIntro;

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
				getStoryValues(Nexus.getCurrentChar());
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
	public void handleOnMouseEnteredOnBtPreview(){
		C3Logger.debug("handleOnMouseEnteredOnBtPreview");
		btPreview.setStyle("-fx-opacity: 1");
	}

	@FXML
	public void handleOnMouseExitOnBtPreview(){
		C3Logger.debug("handleOnMouseExitOnBtPreview");
		btPreview.setStyle("-fx-opacity: 0.2");
	}

	@FXML
	private void handleOnActionBtPreview(){
		//TODO: Get and save next step of the story
		RolePlayCharacterDTO currentChar = Nexus.getCurrentChar();
		if(currentChar.getStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY ) {
			boRp.getNextChapterBySortOrder(currentChar, 1);
		}
		if(currentChar.getStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_CHAPTER ) {
			boRp.getNextStepBySortOrder(currentChar, 1);
		}
		if(currentChar.getStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1 ){
//			RolePlayStoryDTO rp = Nexus.getCurrentChar().getStory().getNextStepID();
			Long rp = Nexus.getCurrentChar().getStory().getNextStepID().getId();
			saveNextStep(rp);
		}

	}

	/******************************** THIS ********************************/

	@Override
	public void getStoryValues(RolePlayCharacterDTO rpChar){

		if(rpChar.getStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1){
			taStoryText.setVisible(true);
			labHeader.setVisible(false);

			taStoryText.setText(rpChar.getStory().getStoryText());

		} else {

			taStoryText.setVisible(false);

			if(rpChar.getStory().getStoryImage() == null || rpChar.getStory().getStoryImage().isEmpty()) {
				labHeader.setVisible(true);

				labHeader.setText(rpChar.getStory().getStoryName());
			} else {
				labHeader.setVisible(false);

			}
		}

		if (rpChar.getStory().getStoryIntro() == null) {


			String imURL;

			// Check step for own image. If now own image availabale use default image
			if (rpChar.getStory().getStoryImage() != null) {
				imURL = BORolePlayStory.URL_RPG_RESOURSES + "/" + rpChar.getStory().getId().toString() + "/" + rpChar.getStory().getStoryImage();

			} else {
				imURL = BORolePlayStory.URL_RPG_BASIC + "/defaultImage.png";

			}

			Image im = new Image(imURL);
			ivIntro.setImage(im);

			if (rpChar.getStory().getStoryMP3() != null) {
				try {
					URL url = new URL(BORolePlayStory.URL_RPG_RESOURSES + "/" + rpChar.getStory().getId().toString() + "/" + rpChar.getStory().getStoryMP3());
					//C3SoundPlayer.play(url, false);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}


			}

		} else {
			//TODO: show and play intro
		}
	}
}
