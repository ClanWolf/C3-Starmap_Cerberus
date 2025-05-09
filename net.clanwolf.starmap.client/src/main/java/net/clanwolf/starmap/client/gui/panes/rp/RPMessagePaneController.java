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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
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
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RPMessagePaneController extends AbstractC3RolePlayController implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

	public RPMessagePaneController() {
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.START_ROLEPLAY, this);
		ActionManager.addActionCallbackListener(ACTIONS.FINALIZE_ROUND, this);
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

		case FINALIZE_ROUND:
			checkToCancelInvasion();
			break;

		case START_ROLEPLAY:
			if(ROLEPLAYENTRYTYPES.RP_HPG_MESSAGE == o.getObject()) {
				logger.info("RolePlayIntroPaneController -> START_ROLEPLAY");

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
		// TODO_C3: Get and save next step of the story
		saveNextStep(getCurrentRP().getVar7ID().getNextStepID());

	}

	/******************************** THIS ********************************/
	@Override
	public void getStoryValues(RolePlayStoryDTO rpStory){

		// set story image
		Image im = BORolePlayStory.getRPG_DefaultMessageImage();
		backgroundImage.setImage(im);

		//Fonts
		//labSenderFaction.setStyle("-fx-font-alignment:center;-fx-background-color:transparent;-fx-border-color:transparent;");
		//labServiceName.setStyle("-fx-font-alignment:center;-fx-background-color:transparent;-fx-border-color:transparent;");
		labSenderFaction.setAlignment(Pos.CENTER);
		labServiceName.setAlignment(Pos.CENTER);

		// set data
		taStoryText.setText(rpStory.getStoryText());
		labHeader.setText(rpStory.getVar7ID().getHeader());
		labDate.setText(rpStory.getVar7ID().getDate());
		labSender.setText(rpStory.getVar7ID().getSender());
		labSenderFaction.setText(Nexus.getBoUniverse().getFactionByID(rpStory.getVar7ID().getFaction()).getName());
		labServiceName.setText(rpStory.getVar7ID().getServiceName());

		BOFaction f = Nexus.getBoUniverse().getFactionByID(rpStory.getVar7ID().getFaction());
		ivSenderLogo.setImage(BORolePlayStory.getFactionImage(f));

		// play sound
		if (rpStory.getStoryMP3() != null) {
			C3SoundPlayer.playRPSound(Objects.requireNonNull(BORolePlayStory.getRPG_Soundfile(rpStory)), audioStartedOnce);
			audioStartedOnce = true;
		}
	} //getStoryValues
}
