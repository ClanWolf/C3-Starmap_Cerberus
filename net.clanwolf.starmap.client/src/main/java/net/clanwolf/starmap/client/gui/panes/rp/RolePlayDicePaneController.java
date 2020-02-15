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
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.io.InputStream;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayDicePaneController extends AbstractC3RolePlayController implements ActionCallBackListener {

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private ImageView rpImage;

	@FXML
	private ImageView ivDice1;

	@FXML
	private ImageView ivDice2;

	@FXML
	private TextArea taRpText;

	@FXML
	private Button btRollDice;

	@FXML
	private Button btNextStep;

	private Random pointGeneratorDice1, pointGeneratorDice2;

	private Image[] allDice;

	private int diceResult;

	public RolePlayDicePaneController() {
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

		btRollDice.setVisible(true);
		btNextStep.setVisible(false);

		pointGeneratorDice1 = new Random();
		pointGeneratorDice2 = new Random();

		Class<IconList> c = IconList.class;
		allDice = new Image[6];

//



		allDice[0] = new Image(this.getClass().getResourceAsStream("/images/dice/d6_1.png"));
		allDice[1] = new Image(this.getClass().getResourceAsStream("/images/dice/d6_2.png"));
		allDice[2] = new Image(this.getClass().getResourceAsStream("/images/dice/d6_3.png"));
		allDice[3] = new Image(this.getClass().getResourceAsStream("/images/dice/d6_4.png"));
		allDice[4] = new Image(this.getClass().getResourceAsStream("/images/dice/d6_5.png"));
		allDice[5] = new Image(this.getClass().getResourceAsStream("/images/dice/d6_6.png"));
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
			if(ROLEPLAYENTRYTYPES.C3_RP_STEP_V4 == o.getObject()) {
				C3Logger.debug("RolePlayDicePaneController -> START_ROLEPLAY");

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
	private void handleOnActionRollDice(){

		C3SoundPlayer.play("sound/fx/dice.mp3", false);

		int dice1 = pointGeneratorDice1.nextInt(6);
		int dice2 = pointGeneratorDice2.nextInt(6);

		ivDice1.setImage(allDice[dice1]);
		ivDice2.setImage(allDice[dice2]);

		diceResult = dice1 + 1 + dice2 + 1;

		btRollDice.setVisible(false);
		btNextStep.setVisible(true);
	}

	@FXML
	private void handleOnActionNextStep(){

//		RolePlayStoryDTO rp = null;
		Long rp = null;

		if(Nexus.getCurrentChar().getStory().getVar4ID().getScore() == diceResult){
			rp = Nexus.getCurrentChar().getStory().getVar4ID().getStoryIDScoreEqual();

		} else if(Nexus.getCurrentChar().getStory().getVar4ID().getScore() > diceResult){
			rp = Nexus.getCurrentChar().getStory().getVar4ID().getStoryIDScoreMore();

		} else if(Nexus.getCurrentChar().getStory().getVar4ID().getScore() < diceResult){
			rp = Nexus.getCurrentChar().getStory().getVar4ID().getStoryIDScoreLess();

		}
		saveNextStep(rp);
	}

	/******************************** THIS ********************************/
	@Override
	public void getStoryValues(RolePlayCharacterDTO rpChar) {
		if (rpChar.getStory().getStoryIntro() == null) {
			String imURL;

			// Check step for own image. If now own image available use default image
			if (rpChar.getStory().getStoryImage() != null) {
				imURL = BORolePlayStory.getRPG_ResourceURL() + "/" + rpChar.getStory().getId().toString() + "/" + rpChar.getStory().getStoryImage();
				Image im = new Image(imURL);
				rpImage.setImage(im);
			} else {
				InputStream isBackground = this.getClass().getResourceAsStream("/images/gui/default_Step.png");
				rpImage.setImage(new Image(isBackground));
			}
		}

		taRpText.setText(rpChar.getStory().getStoryText());
	}
}
