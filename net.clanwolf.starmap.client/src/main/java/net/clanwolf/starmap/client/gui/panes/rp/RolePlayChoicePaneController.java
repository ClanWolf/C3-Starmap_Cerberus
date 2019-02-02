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
import net.clanwolf.starmap.client.logging.C3Logger;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryVar2DTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayChoicePaneController extends AbstractC3RolePlayController implements ActionCallBackListener {

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private ImageView rpImage;

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
	private Button buttonClose;

	public RolePlayChoicePaneController() {
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.START_ROLEPLAY, this);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		init();

	}

	private void init(){
		taRpText.setStyle("-fx-opacity: 1");
		taRpText.setEditable(false);

		btChoice1.setVisible(false);
		btChoice2.setVisible(false);
		btChoice3.setVisible(false);
		btChoice4.setVisible(false);

		buttonClose.setVisible(false);
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
			if(ROLEPLAYENTRYTYPES.C3_RP_STEP_V2 == o.getObject()) {
				C3Logger.debug("RolePlayChoicePaneController -> START_ROLEPLAY");

				init();

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
	private void handleOnActionbtChoice1(){

//		RolePlayStoryDTO rp = Nexus.getCurrentChar().getStory().getVar2ID().getOption1StoryID();
		Long rp = Nexus.getCurrentChar().getStory().getVar2ID().getOption1StoryID();
		saveNextStep(rp);

	}

	@FXML
	private void handleOnActionbtChoice2(){
//		RolePlayStoryDTO rp = Nexus.getCurrentChar().getStory().getVar2ID().getOption2StoryID();
		Long rp = Nexus.getCurrentChar().getStory().getVar2ID().getOption2StoryID();
		saveNextStep(rp);
	}

	@FXML
	private void handleOnActionbtChoice3(){
//		RolePlayStoryDTO rp = Nexus.getCurrentChar().getStory().getVar2ID().getOption3StoryID();
		Long rp = Nexus.getCurrentChar().getStory().getVar2ID().getOption3StoryID();
		saveNextStep(rp);
	}

	@FXML
	private void handleOnActionbtChoice4(){
//		RolePlayStoryDTO rp = Nexus.getCurrentChar().getStory().getVar2ID().getOption4StoryID();
		Long rp = Nexus.getCurrentChar().getStory().getVar2ID().getOption4StoryID();
		saveNextStep(rp);
	}

	/******************************** THIS ********************************/
	@Override
	public void getStoryValues(RolePlayCharacterDTO rpChar) {
		if (rpChar.getStory().getStoryIntro() == null) {
			String imURL;

			// Check step for own image. If now own image availabale use default image
			if (rpChar.getStory().getStoryImage() != null) {
				imURL = BORolePlayStory.URL_RPG_RESOURSES + "/" + rpChar.getStory().getId().toString() + "/" + rpChar.getStory().getStoryImage();

			} else {
				imURL = BORolePlayStory.URL_RPG_BASIC + "/defaultImage.png";

			}

			Image im = new Image(imURL);
			rpImage.setImage(im);
		}

		//TODO: append single chars step by step until the whole text is displaying
		taRpText.setText(rpChar.getStory().getStoryText());

		if(rpChar.getStory().getVar2ID() != null){
			RolePlayStoryVar2DTO rpVar2 = rpChar.getStory().getVar2ID();

			double x = 59;
			double y = 455;
			double offset = 40;

			if(rpVar2.getOption4StoryID() != null){
				btChoice4.setVisible(true);

				btChoice4.setLayoutX(x);
				btChoice4.setLayoutY(y);

				y = y - offset;

				btChoice4.setText(rpVar2.getOption4Text());
			}

			if(rpVar2.getOption3StoryID() != null){
				btChoice3.setVisible(true);

				btChoice3.setLayoutX(x);
				btChoice3.setLayoutY(y);

				y = y - offset;

				btChoice3.setText(rpVar2.getOption3Text());
			}

			if(rpVar2.getOption2StoryID() != null){
				btChoice2.setVisible(true);

				btChoice2.setLayoutX(x);
				btChoice2.setLayoutY(y);

				y = y - offset;

				btChoice2.setText(rpVar2.getOption2Text());
			}

			if(rpVar2.getOption1StoryID() != null){
				btChoice1.setVisible(true);

				btChoice1.setLayoutX(x);
				btChoice1.setLayoutY(y);

				btChoice1.setText(rpVar2.getOption1Text());
			}
		}
	}
}
