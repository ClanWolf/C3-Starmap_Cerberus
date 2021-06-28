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
	private RolePlayCharacterDTO dummy = new RolePlayCharacterDTO();
	private boolean iamdroplead = false;
	private HashMap<Long, Long> characterRoleMap = new HashMap<>();

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button btPreview, btnKick, btnToRight, btnToLeft, btnPromote;

	@FXML
	private ListView<RolePlayCharacterDTO> lvDropleadAttacker;

	@FXML
	private ListView<RolePlayCharacterDTO> lvDropleadDefender;

	@FXML
	private ListView<RolePlayCharacterDTO> lvAttacker;

	@FXML
	private ListView<RolePlayCharacterDTO> lvDefender;

	@FXML
	public void handleAttackerListMouseClick() {
		lvDefender.getSelectionModel().clearSelection();
		lvDropleadAttacker.getSelectionModel().clearSelection();
		lvDropleadDefender.getSelectionModel().clearSelection();
		btnPromote.setText("Promote");

		RolePlayCharacterDTO selectedChar = lvAttacker.getSelectionModel().getSelectedItem();
		if (selectedChar != null && !selectedChar.getName().equals("...")) {
			btnToLeft.setDisable(true);
			btnToRight.setDisable(false);
			btnKick.setDisable(false);
			btnPromote.setDisable(false);
		} else {
			btnToLeft.setDisable(true);
			btnToRight.setDisable(true);
			btnKick.setDisable(true);
			btnPromote.setDisable(true);
		}
		C3Logger.debug("Attackerlist Clicked");
	}

	@FXML
	public void handleAttackerDropleadMouseClick() {
		lvAttacker.getSelectionModel().clearSelection();
		lvDefender.getSelectionModel().clearSelection();
		btnPromote.setText("Degrade");

		RolePlayCharacterDTO selectedChar = lvDropleadAttacker.getSelectionModel().getSelectedItem();
		if (selectedChar != null && !selectedChar.getName().equals("...")) {
			btnPromote.setDisable(!iamdroplead);
			btnToLeft.setDisable(true);
			btnToRight.setDisable(true);
			btnKick.setDisable(true);
		} else {
			btnToLeft.setDisable(true);
			btnToRight.setDisable(true);
			btnKick.setDisable(true);
			btnPromote.setDisable(true);
		}
		C3Logger.debug("Attacker droplead Clicked");
	}

	@FXML
	public void handleDefenderListMouseClick() {
		lvAttacker.getSelectionModel().clearSelection();
		lvDropleadAttacker.getSelectionModel().clearSelection();
		lvDropleadDefender.getSelectionModel().clearSelection();
		btnPromote.setText("Promote");

		RolePlayCharacterDTO selectedChar = lvDefender.getSelectionModel().getSelectedItem();
		if (selectedChar != null && !selectedChar.getName().equals("...")) {
			btnToLeft.setDisable(false);
			btnToRight.setDisable(true);
			btnKick.setDisable(false);
			btnPromote.setDisable(false);
		} else {
			btnToLeft.setDisable(true);
			btnToRight.setDisable(true);
			btnKick.setDisable(true);
			btnPromote.setDisable(true);
			btnPromote.setDisable(true);
		}
		C3Logger.debug("DefenderList Clicked");
	}

	@FXML
	public void handleDefenderDropleadMouseClick() {
		lvAttacker.getSelectionModel().clearSelection();
		lvDefender.getSelectionModel().clearSelection();
		btnPromote.setText("Degrade");

		RolePlayCharacterDTO selectedChar = lvDropleadDefender.getSelectionModel().getSelectedItem();
		if (selectedChar != null && !selectedChar.getName().equals("...")) {
			btnPromote.setDisable(!iamdroplead);
			btnToLeft.setDisable(true);
			btnToRight.setDisable(true);
			btnKick.setDisable(true);
		} else {
			btnToLeft.setDisable(true);
			btnToRight.setDisable(true);
			btnKick.setDisable(true);
			btnPromote.setDisable(true);
		}
		C3Logger.debug("Defender droplead Clicked");
	}

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
		dummy.setName("...");

		BOAttack a = Nexus.getCurrentAttackOfUser();
		updateLists(a);
		lvAttacker.getSelectionModel().clearSelection();
		lvDefender.getSelectionModel().clearSelection();
		btnKick.setDisable(true);
		btnPromote.setDisable(true);

		C3Logger.info("Init ^^^^^------------------------------------------------------------------ WIESO KOMMEN WIR HIER ZWEIMAL REIN ---" + a.getAttackDTO().getId());
	}

	public void updateLists(BOAttack a) {
		lvDropleadAttacker.getItems().clear();
		lvDropleadDefender.getItems().clear();
		lvDefender.getItems().clear();
		lvAttacker.getItems().clear();
		lvDropleadAttacker.getItems().add(dummy);
		lvDropleadDefender.getItems().add(dummy);
		lvDefender.getItems().add(dummy);
		lvAttacker.getItems().add(dummy);
		lvDropleadAttacker.getSelectionModel().clearSelection();
		lvDropleadDefender.getSelectionModel().clearSelection();

		characterRoleMap.clear();

		for (AttackCharacterDTO ac : a.getAttackCharList()) {
			characterRoleMap.put(ac.getCharacterID(), ac.getType());

			if (ac.getType().equals(1L) || ac.getType().equals(0L)) { // Attacker
				if (ac.getType().equals(1L)) { // Droplead
					// Put this dropleader into upper list
					lvDropleadAttacker.getItems().clear();
					lvDropleadAttacker.getItems().add(Nexus.getCharacterById(ac.getCharacterID()));
					lvDropleadAttacker.getItems().remove(dummy);
					lvDropleadAttacker.getSelectionModel().clearSelection();
					if (ac.getCharacterID().equals(Nexus.getCurrentChar().getId())) {
						iamdroplead = true;
					}
				} else { // Warrior
					// Put this warrior into lower list (not a droplead)
					lvAttacker.getItems().add(Nexus.getCharacterById(ac.getCharacterID()));
					lvAttacker.getItems().remove(dummy);
				}
			} else if (ac.getType().equals(3L) || ac.getType().equals(2L)) {
				if (ac.getType().equals(3L)) { // Droplead
					// Put this dropleader into upper list
					lvDropleadDefender.getItems().clear();
					lvDropleadDefender.getItems().add(Nexus.getCharacterById(ac.getCharacterID()));
					lvDropleadAttacker.getItems().remove(dummy);
					lvDropleadDefender.getSelectionModel().clearSelection();
				} else { // Warrior
					// Put this warrior into lower list (not a droplead)
					lvDefender.getItems().add(Nexus.getCharacterById(ac.getCharacterID()));
					lvDefender.getItems().remove(dummy);
				}
			} else if (ac.getType().equals(4L)) {
				// This warrior will help out, not coming from attacker OR defender factions
				// Cannot be a droplead on either side
				// Will be assigned to the side with fewer pilots or the attacker in case of same number of pilots
				int atts = lvAttacker.getItems().size();
				int defs = lvDefender.getItems().size();

				if (lvDropleadAttacker.getItems().size() > 0 && !lvDropleadAttacker.getItems().get(0).getName().equals("...")) {
					atts++;
				}
				if (lvDropleadDefender.getItems().size() > 0 && !lvDropleadDefender.getItems().get(0).getName().equals("...")) {
					defs++;
				}

				if (atts > defs) {
					lvDefender.getItems().add(Nexus.getCharacterById(ac.getCharacterID()));
					lvDefender.getItems().remove(dummy);
				} else if (atts < defs) {
					lvDefender.getItems().add(Nexus.getCharacterById(ac.getCharacterID()));
					lvDefender.getItems().remove(dummy);
				} else {
					lvAttacker.getItems().add(Nexus.getCharacterById(ac.getCharacterID()));
					lvAttacker.getItems().remove(dummy);
				}
			}
		}
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
				if (o.getObject() instanceof BOAttack) {
					BOAttack a = (BOAttack) o.getObject();
					updateLists(a);
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
