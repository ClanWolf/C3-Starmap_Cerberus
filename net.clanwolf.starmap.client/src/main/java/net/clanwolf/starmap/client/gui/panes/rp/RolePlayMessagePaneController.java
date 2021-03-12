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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayMessagePaneController extends AbstractC3RolePlayController implements ActionCallBackListener {

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button btPreview;

	@FXML
	private TextArea taStoryText;

	@FXML
	private Label labHeader;

	@FXML
	private Label labDate;

	@FXML
	private Label labSender;

	@FXML
	private Label labSenderFaction;

	@FXML
	private Label labServiceName;

	@FXML
	private ImageView ivSenderLogo;

	public RolePlayMessagePaneController() {
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
			if(ROLEPLAYENTRYTYPES.C3_RP_STEP_V7 == o.getObject()) {
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
	private void handleOnMouseClicked(){
		handleOnActionBtPreview();
	}

	@FXML
	private void handleOnActionBtPreview(){
		//TODO: Get and save next step of the story
		/*RolePlayCharacterDTO currentChar = Nexus.getCurrentChar();
		if(currentChar.getStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY ) {
			boRp.getNextChapterBySortOrder(currentChar, 1);
		}
		if(currentChar.getStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_CHAPTER ) {
			boRp.getNextStepBySortOrder(currentChar, 1);
		}
		if(currentChar.getStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1 ){
			Long rp = Nexus.getCurrentChar().getStory().getNextStepID();
			saveNextStep(rp);
		}*/
	}

	/******************************** THIS ********************************/

	@Override
	public void getStoryValues(RolePlayCharacterDTO rpChar){

		// set story image
		Image im = BORolePlayStory.getRPG_DefaultMessageImage();
		backgroundImage.setImage(im);

		//Fonts
		//labSenderFaction.setStyle("-fx-font-alignment:center;-fx-background-color:transparent;-fx-border-color:transparent;");
		//labServiceName.setStyle("-fx-font-alignment:center;-fx-background-color:transparent;-fx-border-color:transparent;");
		labSenderFaction.setAlignment(Pos.CENTER);
		labServiceName.setAlignment(Pos.CENTER);

		// set data
		taStoryText.setText(rpChar.getStory().getStoryText());
		labHeader.setText(rpChar.getStory().getVar7ID().getHeader());
		labDate.setText(rpChar.getStory().getVar7ID().getDate());
		labSender.setText(rpChar.getStory().getVar7ID().getSender());
		labSenderFaction.setText(Nexus.getBoUniverse().getFactionByID(rpChar.getStory().getVar7ID().getFaction()).getName());
		labServiceName.setText(rpChar.getStory().getVar7ID().getServiceName());

		BOFaction f = Nexus.getBoUniverse().getFactionByID(rpChar.getStory().getVar7ID().getFaction());
		ivSenderLogo.setImage(BORolePlayStory.getFactionImage(f));

		// play sound
		if (rpChar.getStory().getStoryMP3() != null) {
			C3SoundPlayer.play(BORolePlayStory.getRPG_Soundfile(rpChar.getStory()), false);
		}

	} //getStoryValues
}
