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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.dtos.AttackCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayPrepareBattlePaneController extends AbstractC3RolePlayController implements ActionCallBackListener {

	private HashMap<Long, Boolean> animationPlayedMap = new HashMap<>();

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button btPreview;

	@FXML
	private ComboBox<RolePlayCharacterDTO> cbDropleadAttacker;

	@FXML
	private ComboBox<RolePlayCharacterDTO> cbDropleadDefender;

	@FXML
	private ListView<RolePlayCharacterDTO> lvAttacker;

	@FXML
	private ListView<RolePlayCharacterDTO> lvDefender;

	public RolePlayPrepareBattlePaneController() {
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.START_ROLEPLAY, this);
		ActionManager.addActionCallbackListener(ACTIONS.UPDATE_USERS_FOR_ATTACK, this);
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
			if(ROLEPLAYENTRYTYPES.C3_RP_STEP_V8 == o.getObject()) {
				C3Logger.debug("RolePlayIntroPaneController -> START_ROLEPLAY");

				// set current step of story
				//getStoryValues(Nexus.);
			}
			break;
		case UPDATE_USERS_FOR_ATTACK:
			C3Logger.info("The userlist has changed. Update information on the listboxes.");
			// TODO: Charactere updaten
			if (o.getObject() instanceof BOAttack) {
				BOAttack a = (BOAttack) o.getObject();
				for (AttackCharacterDTO ac : a.getAttackCharList()) {
					if (ac.getType().equals(1L) || ac.getType().equals(0L)) { // Droplead attacker
						cbDropleadAttacker.getItems().add(a.getRpCharByID(ac.getCharacterID()));

						if (ac.getType().equals(1L)){
							cbDropleadAttacker.getSelectionModel().select(a.getRpCharByID(ac.getCharacterID()));

						} else {
							lvAttacker.getItems().add(a.getRpCharByID(ac.getCharacterID()));
						}
					} else if (ac.getType().equals(3L) || ac.getType().equals(2L)) { // Droplead attacker
						cbDropleadDefender.getItems().add(a.getRpCharByID(ac.getCharacterID()));

						if (ac.getType().equals(3L)) {
							cbDropleadDefender.getSelectionModel().select(a.getRpCharByID(ac.getCharacterID()));

						} else {
							lvDefender.getItems().add(a.getRpCharByID(ac.getCharacterID()));
						}
					} else if (ac.getType().equals(4L)) {
						int defs = lvDefender.getItems().size();
						int atts = lvAttacker.getItems().size();
						if (atts > defs) {
							lvDefender.getItems().add(a.getRpCharByID(ac.getCharacterID()));
						} else if (atts < defs) {
							lvDefender.getItems().add(a.getRpCharByID(ac.getCharacterID()));
						} else {
							lvAttacker.getItems().add(a.getRpCharByID(ac.getCharacterID()));
						}
					}
				}
			}
			C3Logger.info("The userlist has changed. Update information on the listboxes. ");
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
		// play sound
		if (rpChar.getStory().getStoryMP3() != null) {
			C3SoundPlayer.play(BORolePlayStory.getRPG_Soundfile(rpChar.getStory()), false);
		}
	} //getStoryValues
}
