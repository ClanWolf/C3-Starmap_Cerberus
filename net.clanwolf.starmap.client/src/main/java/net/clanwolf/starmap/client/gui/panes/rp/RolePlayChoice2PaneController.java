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
 * Copyright (c) 2001-2022, ClanWolf.net                            |
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
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryVar3DTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayChoice2PaneController extends AbstractC3RolePlayController implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private ImageView rpIBackgroundImage;

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

	public RolePlayChoice2PaneController() {
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
			if(ROLEPLAYENTRYTYPES.C3_RP_STEP_V3 == o.getObject()) {
				logger.debug("RolePlayChoice2PaneController -> START_ROLEPLAY");

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

	@Override
	public void setStrings() {
		Platform.runLater(() -> {
			// set strings
		});
	}

	/******************************** FXML ********************************/

	@FXML
	private void handleOnActionbtChoice1(){

		Long rp = getCurrentRP().getVar3ID().getNextStoryID();
		saveNextStep(rp);

	}

	@FXML
	private void handleOnActionbtChoice2(){
//		Long rp = Nexus.getCurrentChar().getStory().getVar3ID().getNextStory2ID();
//		saveNextStep(rp);
	}

	@FXML
	private void handleOnActionbtChoice3(){
//		Long rp = Nexus.getCurrentChar().getStory().getVar3ID().getNextStory3ID();
//		saveNextStep(rp);
	}

	@FXML
	private void handleOnActionbtChoice4(){
//		Long rp = Nexus.getCurrentChar().getStory().getVar3ID().getNextStory4ID();
//		saveNextStep(rp);
	}

	/******************************** THIS ********************************/

	@Override
	public void getStoryValues(RolePlayStoryDTO rpStory) {
		if (rpStory.getStoryIntro() == null) {
			String imURL;

			if(rpIBackgroundImage != null) {
				InputStream isBackground = this.getClass().getResourceAsStream("/images/gui/default_Step.png");
				rpIBackgroundImage.setImage(new Image(isBackground));
			}

			// Check step for own image. If now own image availabale use default image
			if (rpStory.getStoryImage() != null) {
				imURL = BORolePlayStory.getRPG_ResourceURL() + "/" + rpStory.getId().toString() + "/" + rpStory.getStoryImage();
				Image im = new Image(imURL);
				rpImage.setImage(im);

			}
			// play sound
			if (rpStory.getStoryMP3() != null) {
				C3SoundPlayer.playRPSound(Objects.requireNonNull(BORolePlayStory.getRPG_Soundfile(rpStory)), audioStartedOnce);
				audioStartedOnce = true;
			}
		}

		//TODO: append single chars step by step until the whole text is displaying
		taRpText.setText(rpStory.getStoryText());

		if(rpStory.getVar3ID() != null){

			double x = 59;
			double y = 455;
			double offset = 40;

			RolePlayStoryVar3DTO rpVar3 = rpStory.getVar3ID();

//			// rpVar3
//			if(rpVar3.getNextStory4ID() != null){
//				btChoice4.setVisible(true);
//
//				btChoice4.setLayoutX(x);
//				btChoice4.setLayoutY(y);
//
//				y = y - offset;
//
//				btChoice4.setText(rpVar3.getLabelText4());
//			}
//
//			if(rpVar3.getNextStory3ID() != null){
//				btChoice3.setVisible(true);
//
//				btChoice3.setLayoutX(x);
//				btChoice3.setLayoutY(y);
//
//				y = y - offset;
//
//				btChoice3.setText(rpVar3.getLabelText3());
//			}
//
//			if(rpVar3.getNextStory2ID() != null){
//				btChoice2.setVisible(true);
//
//				btChoice2.setLayoutX(x);
//				btChoice2.setLayoutY(y);
//
//				y = y - offset;
//
//				btChoice2.setText(rpVar3.getLabelText2());
//			}
//
//			if(rpVar3.getNextStoryID() != null){
//				btChoice1.setVisible(true);
//
//				btChoice1.setLayoutX(x);
//				btChoice1.setLayoutY(y);
//
//				btChoice1.setText(rpVar3.getLabelText());
//			}
		}
		/*btChoice1.setVisible(false);
		btChoice2.setVisible(false);
		btChoice3.setVisible(false);
		btChoice4.setVisible(false);*/
	}
}
