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

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Pane;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.*;

/**
 * @author Undertaker
 */
public class RolePlayPrepareBattlePaneController extends AbstractC3RolePlayController implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

//	private HashMap<Long, Boolean> animationPlayedMap = new HashMap<>();
	private final RolePlayCharacterDTO dummy = new RolePlayCharacterDTO();
	private boolean iamdroplead = false;
	private final HashMap<Long, AttackCharacterDTO> characterRoleMap = new HashMap<>();
	private boolean firstCreationDone = false;
	private boolean creating = false;
	private boolean bOnlyOneSave = true;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button btNext, btnKick, btnToRight, btnToLeft, btnPromote, btnLeave;

	@FXML
	private ListView<RolePlayCharacterDTO> lvDropleadAttacker;

	@FXML
	private ListView<RolePlayCharacterDTO> lvDropleadDefender;

	@FXML
	private ListView<RolePlayCharacterDTO> lvAttacker;

	@FXML
	private ListView<RolePlayCharacterDTO> lvDefender;

	@FXML
	private ImageView backgroundImage, ivAttackerLogo, ivDefenderLogo, ivPlanet, ivAttackerRank, ivDefenderRank;

	@FXML
	private VBox vbLeft, vbRight;

	@FXML
	private HBox hbButtons;

	@FXML
	private AnchorPane apCenter;

	@FXML
	private Label lSystemName, lAttacker, lDefender, lAttackerShortname, lDefenderShortname;

	@FXML
	public void handleAttackerListMouseClick() {
		lvDefender.getSelectionModel().clearSelection();
		lvDropleadAttacker.getSelectionModel().clearSelection();
		lvDropleadDefender.getSelectionModel().clearSelection();
		btnPromote.setText(Internationalization.getString("C3_Lobby_Promote"));

		RolePlayCharacterDTO selectedChar = lvAttacker.getSelectionModel().getSelectedItem();
		if (selectedChar != null && !selectedChar.getName().equals("...")) {
			Long role = (characterRoleMap.get(selectedChar.getId())).getType();
			boolean iAmAttackerCommander = characterRoleMap.get(Nexus.getCurrentChar().getId()).getType() == Constants.ROLE_ATTACKER_COMMANDER;
			boolean clickedWarriorIsSameFaction = role == Constants.ROLE_ATTACKER_WARRIOR;
			boolean clickedWarriorIsOnline = Nexus.getUserIsOnline(selectedChar.getId());

			btnToLeft.setDisable(true);
			btnToRight.setDisable(!iAmAttackerCommander);
			btnKick.setDisable(!iAmAttackerCommander);
			btnPromote.setDisable(!(iAmAttackerCommander && clickedWarriorIsSameFaction && clickedWarriorIsOnline)); // No promotion for players from 3rd factions

			if (selectedChar.getName().equals(Nexus.getCurrentChar().getName())) {
				btnKick.setDisable(true); // Can not kick myself
			}
		} else {
			btnToLeft.setDisable(true);
			btnToRight.setDisable(true);
			btnKick.setDisable(true);
			btnPromote.setDisable(true);
		}
		if (selectedChar == Nexus.getCurrentChar()) {
			btnKick.setDisable(true);
		}
	}

	@FXML
	public void handleAttackerDropleadMouseClick() {
		lvAttacker.getSelectionModel().clearSelection();
		lvDefender.getSelectionModel().clearSelection();
		lvDropleadDefender.getSelectionModel().clearSelection();
		//btnPromote.setText(Internationalization.getString("C3_Lobby_Demote"));

		RolePlayCharacterDTO selectedChar = lvDropleadAttacker.getSelectionModel().getSelectedItem();
		if (selectedChar != null && !selectedChar.getName().equals("...")) {
//			Long role = characterRoleMap.get(selectedChar.getId());
			btnToLeft.setDisable(true); // the droplead can not be switched while droplead, degrade first
			btnToRight.setDisable(true); // the droplead can not be switched while droplead, degrade first
			btnKick.setDisable(true); // the droplead can not be kicked while droplead, degrade first
//			btnPromote.setDisable(!iamdroplead);
			btnPromote.setDisable(true);
			if (selectedChar.getName().equals(Nexus.getCurrentChar().getName())) {
				btnKick.setDisable(true); // Can not kick myself
			}
		} else {
			btnToLeft.setDisable(true);
			btnToRight.setDisable(true);
			btnKick.setDisable(true);
			btnPromote.setDisable(true);
		}
	}

	@FXML
	public synchronized void handleToLeftButtonClick() {
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("1");
//		btnToLeft.setDisable(true);
//		lvAttacker.getItems().add(dummy);
//		lvDefender.getItems().add(dummy);
		RolePlayCharacterDTO selectedChar = lvDefender.getSelectionModel().getSelectedItem();
//		lvDefender.getItems().remove(selectedChar);
//		lvAttacker.getItems().add(selectedChar);
//		lvDefender.getSelectionModel().clearSelection();
//		lvAttacker.getSelectionModel().clearSelection();
		btnKick.setDisable(true);
		btnPromote.setDisable(true);

		BOAttack a = Nexus.getCurrentAttackOfUser();
		AttackCharacterDTO ac = characterRoleMap.get(selectedChar.getId());
		RolePlayCharacterDTO c = Nexus.getCharacterById(ac.getCharacterID());

		if (c.getFactionId().equals(a.getAttackerFactionId())) {
			ac.setType(Constants.ROLE_ATTACKER_WARRIOR);
		} else {
			ac.setType(Constants.ROLE_ATTACKER_SUPPORTER);
		}
		saveAttack();
		checkConditionsToStartDrop(ac);
	}

	@FXML
	public synchronized void handleToRightButtonClick() {
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("2");
//		btnToRight.setDisable(true);
//		lvAttacker.getItems().add(dummy);
//		lvDefender.getItems().add(dummy);
		RolePlayCharacterDTO selectedChar = lvAttacker.getSelectionModel().getSelectedItem();
//		lvAttacker.getItems().remove(selectedChar);
//		lvDefender.getItems().add(selectedChar);
//		lvDefender.getSelectionModel().clearSelection();
//		lvAttacker.getSelectionModel().clearSelection();
		btnKick.setDisable(true);
		btnPromote.setDisable(true);

		BOAttack a = Nexus.getCurrentAttackOfUser();
		AttackCharacterDTO ac = characterRoleMap.get(selectedChar.getId());
		RolePlayCharacterDTO c = Nexus.getCharacterById(ac.getCharacterID());

		if (c.getFactionId().equals(a.getDefenderFactionId())) {
			ac.setType(Constants.ROLE_DEFENDER_WARRIOR);
		} else {
			ac.setType(Constants.ROLE_DEFENDER_SUPPORTER);
		}
		saveAttack();
		checkConditionsToStartDrop(ac);
	}

	@FXML
	public void handlePromoteButtonClick() {
		RolePlayCharacterDTO selectedChar = lvAttacker.getSelectionModel().getSelectedItem();
		if (selectedChar != null) { // selected Char is attacker
			if (characterRoleMap.get(Nexus.getCurrentChar().getId()).getType() == Constants.ROLE_ATTACKER_COMMANDER) {
				AttackCharacterDTO ac = characterRoleMap.get(selectedChar.getId());
				ac.setType(Constants.ROLE_ATTACKER_COMMANDER);
				checkConditionsToStartDrop(ac);
				AttackCharacterDTO ac2 = characterRoleMap.get(lvDropleadAttacker.getItems().get(0).getId()); // this must be me (if the button was enabled)
				ac2.setType(Constants.ROLE_ATTACKER_WARRIOR);
				iamdroplead = false;
				checkConditionsToStartDrop(ac2);

				saveAttack();
			}
		}

		selectedChar = lvDefender.getSelectionModel().getSelectedItem();
		if (selectedChar != null) {
			if (characterRoleMap.get(Nexus.getCurrentChar().getId()).getType() == Constants.ROLE_DEFENDER_COMMANDER) {
				selectedChar = lvDefender.getSelectionModel().getSelectedItem();
				AttackCharacterDTO ac = characterRoleMap.get(selectedChar.getId());
				ac.setType(Constants.ROLE_DEFENDER_COMMANDER);
				checkConditionsToStartDrop(ac);
				AttackCharacterDTO ac2 = characterRoleMap.get(lvDropleadDefender.getItems().get(0).getId()); // this must be me (if the button was enabled)
				ac2.setType(Constants.ROLE_DEFENDER_WARRIOR);
				iamdroplead = false;
				checkConditionsToStartDrop(ac2);

				saveAttack();
			}
		}
	}

	@FXML
	public synchronized void handleKickButtonClick() {
		RolePlayCharacterDTO selectedChar = lvAttacker.getSelectionModel().getSelectedItem();
		if (selectedChar == null) {
			selectedChar = lvDefender.getSelectionModel().getSelectedItem();
		}
		AttackCharacterDTO ac = characterRoleMap.get(selectedChar.getId());
		ac.setType(null);
		saveAttack();
		checkConditionsToStartDrop(ac);
	}

	@FXML
	public synchronized void handleLeaveButtonClick() {
		AttackCharacterDTO ac = characterRoleMap.get(Nexus.getCurrentChar().getId());
		ac.setType(null);
		saveAttack();
		checkConditionsToStartDrop(ac);
		Nexus.setCurrentAttackOfUserToNull();
		ActionManager.getAction(ACTIONS.SWITCH_TO_MAP).execute();
	}

	@FXML
	public void handleContinueButtonClick() {
		RolePlayStoryDTO nextStep = Nexus.getBoUniverse().getAttackStoriesByID(Nexus.getCurrentAttackOfUser().getStoryId().longValue());
		Nexus.setStoryBeforeSaving(Nexus.getCurrentAttackOfUser().getStoryId().longValue());

		// Drops have been started here! Mark the attack as locked for other users to join
		BOAttack a = Nexus.getCurrentAttackOfUser();
		a.setAttackFightsHaveBeenStarted(true);
		logger.info("Attack drops have started, planet is now locked for new users to join!");

		saveNextStep(nextStep.getNextStepID());
	}

	public synchronized void saveAttack() {
		BOAttack a = Nexus.getCurrentAttackOfUser();

		if (a != null) {
			ArrayList<AttackCharacterDTO> charList = new ArrayList<AttackCharacterDTO>();

			for (RolePlayCharacterDTO attacker : lvAttacker.getItems()) {
				if (!attacker.getName().equals("...")) {
					AttackCharacterDTO acDTO = new AttackCharacterDTO();
					acDTO.setCharacterID(attacker.getId());
					acDTO.setAttackID(a.getAttackDTO().getId());
					acDTO.setType(characterRoleMap.get(attacker.getId()).getType());
					if (acDTO.getType() != null) {
						charList.add(acDTO);
					}
				}
			}

			for (RolePlayCharacterDTO defender : lvDefender.getItems()) {
				if (!defender.getName().equals("...")) {
					AttackCharacterDTO acDTO = new AttackCharacterDTO();
					acDTO.setCharacterID(defender.getId());
					acDTO.setAttackID(a.getAttackDTO().getId());
					acDTO.setType(characterRoleMap.get(defender.getId()).getType());
					if (acDTO.getType() != null) {
						charList.add(acDTO);
					}
				}
			}

			if (lvDropleadAttacker.getItems().size() > 0) {
				if (!lvDropleadAttacker.getItems().get(0).getName().equals("...")) {
					AttackCharacterDTO acDTODropLeadAttacker = new AttackCharacterDTO();
					acDTODropLeadAttacker.setCharacterID(lvDropleadAttacker.getItems().get(0).getId());
					acDTODropLeadAttacker.setAttackID(a.getAttackDTO().getId());
					acDTODropLeadAttacker.setType(characterRoleMap.get(lvDropleadAttacker.getItems().get(0).getId()).getType());
					if (acDTODropLeadAttacker.getType() != null) {
						charList.add(acDTODropLeadAttacker);
					}
				}
			} else {
				logger.error("This list should never be empty!");
			}

			if (lvDropleadDefender.getItems().size() > 0) {
				if (!lvDropleadDefender.getItems().get(0).getName().equals("...")) {
					AttackCharacterDTO acDTODropLeadDefender = new AttackCharacterDTO();
					acDTODropLeadDefender.setCharacterID(lvDropleadDefender.getItems().get(0).getId());
					acDTODropLeadDefender.setAttackID(a.getAttackDTO().getId());
					acDTODropLeadDefender.setType(characterRoleMap.get(lvDropleadDefender.getItems().get(0).getId()).getType());
					if (acDTODropLeadDefender.getType() != null) {
						charList.add(acDTODropLeadDefender);
					}
				}
			} else {
				logger.error("This list should never be empty!");
			}

			a.getAttackDTO().setAttackCharList(charList);
			a.storeAttack();
		}
	}

	public synchronized void checkConditionsToStartDrop(AttackCharacterDTO ac) {
		C3SoundPlayer.play("sound/fx/button_clicked_05.mp3", false);

		btnToRight.setDisable(true);
		btnToLeft.setDisable(true);
		btnKick.setDisable(true);
		btnPromote.setDisable(true);

		//BOAttack a = Nexus.getCurrentAttackOfUser();
		/*if (ac != null) {
			a.storeAttackCharacters(ac, (ac.getType() == null));
		}*/

		boolean attackersOnline = true;
		boolean defendersOnline = true;
		boolean allOnline = false;

		for (RolePlayCharacterDTO rpc : lvAttacker.getItems()) {
			boolean online = false;
			for (UserDTO u : Nexus.getCurrentlyOnlineUserList()) {
				if (u.getCurrentCharacter().getId().equals(rpc.getId())) {
					online = true;
					break;
				}
			}
			if (!online) {
				attackersOnline = false;
				break;
			}
		}

		for (RolePlayCharacterDTO rpc : lvDefender.getItems()) {
			if (rpc.getName().endsWith("(offline)")) {
				defendersOnline = false;
				break;
			}
		}

		if (!lvDropleadAttacker.getItems().get(0).getName().endsWith("(offline)")
			&& !lvDropleadDefender.getItems().get(0).getName().endsWith("(offline)")
			&& attackersOnline
			&& defendersOnline) {
			allOnline = true;
		}

		logger.debug("Droplead Attacker: " + (lvDropleadAttacker.getItems().size() == 1 && !"...".equals(lvDropleadAttacker.getItems().get(0).getName())));
		logger.debug("Droplead Defender: " + (lvDropleadDefender.getItems().size() == 1 && !"...".equals(lvDropleadDefender.getItems().get(0).getName())));
		logger.debug("Count Attacker: " + (lvAttacker.getItems().size() >= 2));
		logger.debug("Count Defender: " + (lvDefender.getItems().size() >= 2));
		logger.debug("Equal pilot count: " + (lvAttacker.getItems().size() == lvDefender.getItems().size() && lvAttacker.getItems().size() >= 2));
		logger.debug("Everybody online: " + allOnline);

		// Check conditions
		Platform.runLater(() -> btNext.setDisable(true));

		int attackerCount = 0;
		for (RolePlayCharacterDTO c : lvAttacker.getItems()) {
			if (!"...".equals(c.getName())) {
				attackerCount++;
			}
		}
		int defenderCount = 0;
		for (RolePlayCharacterDTO c : lvDefender.getItems()) {
			if (!"...".equals(c.getName())) {
				defenderCount++;
			}
		}

		if (lvDropleadAttacker.getItems().size() == 1
				&& lvDropleadDefender.getItems().size() == 1
				&& !"...".equals(lvDropleadAttacker.getItems().get(0).getName())
				&& !"...".equals(lvDropleadDefender.getItems().get(0).getName())
				&& attackerCount >= Constants.MINIMUM_PILOTS_PER_SIDE_IN_INVASION_DROP - 1 // Check the minimum number of fighting pilots, may be disabled for testing, -1 because of the droplead
				&& defenderCount >= Constants.MINIMUM_PILOTS_PER_SIDE_IN_INVASION_DROP - 1 // Check the minimum number of fighting pilots, may be disabled for testing, -1 because of the droplead
				&& attackerCount == defenderCount
		) {
			// Enable "continue"
			BOAttack a = Nexus.getCurrentAttackOfUser();
			RolePlayCharacterDTO rpc = Nexus.getCurrentChar();
			for (AttackCharacterDTO atc : a.getAttackCharList()) {
				if (atc.getCharacterID().equals(rpc.getId())) {
					if (atc.getType().equals(Constants.ROLE_ATTACKER_COMMANDER)) {
						Platform.runLater(() -> btNext.setDisable(false));
						break;
					}
				}
			}
		}

//		Platform.runLater(() -> btNext.setDisable(false)); // this ALWAYS enabled the next button for testing purposes

		Platform.runLater(() -> lvAttacker.requestFocus());
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("1");
	}

	@FXML
	public void handleDefenderListMouseClick() {
		lvAttacker.getSelectionModel().clearSelection();
		lvDropleadAttacker.getSelectionModel().clearSelection();
		lvDropleadDefender.getSelectionModel().clearSelection();
		btnPromote.setText(Internationalization.getString("C3_Lobby_Promote"));

		RolePlayCharacterDTO selectedChar = lvDefender.getSelectionModel().getSelectedItem();
		if (selectedChar != null && !selectedChar.getName().equals("...")) {
			Long role = (characterRoleMap.get(selectedChar.getId())).getType();
			boolean iAmDefenderCommander = characterRoleMap.get(Nexus.getCurrentChar().getId()).getType() == Constants.ROLE_DEFENDER_COMMANDER;
			boolean iAmAttackerCommander = characterRoleMap.get(Nexus.getCurrentChar().getId()).getType() == Constants.ROLE_ATTACKER_COMMANDER;
			boolean clickedWarriorIsSameFaction = role == Constants.ROLE_DEFENDER_WARRIOR;
			boolean clickedWarriorIsOnline = Nexus.getUserIsOnline(selectedChar.getId());

			boolean mayMoveDefender = iAmDefenderCommander || iAmAttackerCommander; // Attacker commander may move everyone

			btnToLeft.setDisable(!mayMoveDefender);
			btnToRight.setDisable(true);
			btnKick.setDisable(!iAmDefenderCommander);
			btnKick.setDisable(characterRoleMap.get(Nexus.getCurrentChar().getId()).getType() != Constants.ROLE_DEFENDER_COMMANDER && !iAmAttackerCommander);
			btnPromote.setDisable(!(iAmDefenderCommander && clickedWarriorIsSameFaction && clickedWarriorIsOnline)); // No promotion for players from 3rd factions

			if (selectedChar.getName().equals(Nexus.getCurrentChar().getName())) {
				btnKick.setDisable(true); // Can not kick myself
			}
		} else {
			btnToLeft.setDisable(true);
			btnToRight.setDisable(true);
			btnKick.setDisable(true);
			btnPromote.setDisable(true);
			btnPromote.setDisable(true);
		}
		if (selectedChar == Nexus.getCurrentChar()) {
			btnKick.setDisable(true);
		}
	}

	@FXML
	public void handleDefenderDropleadMouseClick() {
		lvAttacker.getSelectionModel().clearSelection();
		lvDefender.getSelectionModel().clearSelection();
		lvDropleadAttacker.getSelectionModel().clearSelection();
//		btnPromote.setText(Internationalization.getString("C3_Lobby_Demote"));

		RolePlayCharacterDTO selectedChar = lvDropleadDefender.getSelectionModel().getSelectedItem();
		if (selectedChar != null && !selectedChar.getName().equals("...")) {
//			Long role = characterRoleMap.get(selectedChar.getId());
			btnToLeft.setDisable(true); // the droplead can not be switched while droplead, degrade first
			btnToRight.setDisable(true); // the droplead can not be switched while droplead, degrade first
			btnKick.setDisable(true); // the droplead can not be kicked while droplead, degrade first
			btnPromote.setDisable(true);
			if (selectedChar.getName().equals(Nexus.getCurrentChar().getName())) {
				btnKick.setDisable(true); // Can not kick myself
			}
		} else {
			btnToLeft.setDisable(true);
			btnToRight.setDisable(true);
			btnKick.setDisable(true);
			btnPromote.setDisable(true);
		}
	}

	public RolePlayPrepareBattlePaneController() {
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.START_ROLEPLAY, this);
		ActionManager.addActionCallbackListener(ACTIONS.UPDATE_USERS_FOR_ATTACK, this);
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_DESTROY_CURRENT, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_BEGINS, this);
		ActionManager.addActionCallbackListener(ACTIONS.NEW_PLAYERLIST_RECEIVED, this);
		ActionManager.addActionCallbackListener(ACTIONS.FINALIZE_ROUND, this);
	}

	private void buildGuiEffect() {
		if (!creating) {
			creating = true;
			// Fade in transition 01 (Background)
			FadeTransition fadeInTransition_01 = new FadeTransition(Duration.millis(80), backgroundImage);
			fadeInTransition_01.setFromValue(0.0);
			fadeInTransition_01.setToValue(1.0);
			fadeInTransition_01.setCycleCount(3);

			// Fade in transition 02 (LEFT)
			FadeTransition fadeInTransition_02 = new FadeTransition(Duration.millis(40), vbLeft);
			fadeInTransition_02.setFromValue(0.0);
			fadeInTransition_02.setToValue(1.0);
			fadeInTransition_02.setCycleCount(3);

			// Fade in transition 03 (RIGHT)
			FadeTransition fadeInTransition_03 = new FadeTransition(Duration.millis(40), vbRight);
			fadeInTransition_03.setFromValue(0.0);
			fadeInTransition_03.setToValue(1.0);
			fadeInTransition_03.setCycleCount(3);

			// Fade in transition 04 (CENTER)
			FadeTransition fadeInTransition_04 = new FadeTransition(Duration.millis(40), apCenter);
			fadeInTransition_04.setFromValue(0.0);
			fadeInTransition_04.setToValue(1.0);
			fadeInTransition_04.setCycleCount(3);

			// Fade in transition 05 (Buttons)
			FadeTransition fadeInTransition_05 = new FadeTransition(Duration.millis(40), hbButtons);
			fadeInTransition_05.setFromValue(0.0);
			fadeInTransition_05.setToValue(1.0);
			fadeInTransition_05.setCycleCount(3);

			// Transition sequence
			SequentialTransition sequentialTransition = new SequentialTransition();
			sequentialTransition.getChildren().addAll(fadeInTransition_01,
					fadeInTransition_02,
					fadeInTransition_03,
					fadeInTransition_04,
					fadeInTransition_05);
			sequentialTransition.setCycleCount(1);
			sequentialTransition.setOnFinished(event -> {
				creating = false;
				firstCreationDone = true;
			});
			sequentialTransition.play();
			C3SoundPlayer.play("sound/fx/cursor_click_11.mp3", false);
		}
	}

	/******************************** THIS ********************************/
	@Override
	public void getStoryValues(RolePlayStoryDTO rpStory){
		// play sound
		if (rpStory != null && rpStory.getStory().getStoryMP3() != null) {
			C3SoundPlayer.playRPSound(Objects.requireNonNull(BORolePlayStory.getRPG_Soundfile(rpStory)), audioStartedOnce);
			audioStartedOnce = true;
		}
	} //getStoryValues

	public synchronized void updateLists(BOAttack a) {
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

		HashMap<AttackCharacterDTO, RolePlayCharacterDTO> potentialDropleadersAttacker = new HashMap<>();
		HashMap<AttackCharacterDTO, RolePlayCharacterDTO> potentialDropleadersDefender = new HashMap<>();

		characterRoleMap.clear();

		for (AttackCharacterDTO ac : a.getAttackCharList()) {
			String l = "###### ";
			RolePlayCharacterDTO c = Nexus.getCharacterById(ac.getCharacterID());

			l = l + c.getName() + " (Type: " + ac.getType() + ") ";
			if (a.getAttackerFactionId().equals(c.getFactionId())) {
				// this user belongs to the attacker faction
				potentialDropleadersAttacker.put(ac, c);
				l = l + "(potential droplead for attacker) ";
			}
			if (a.getDefenderFactionId().equals(Nexus.getCharacterById(ac.getCharacterID()).getFactionId())) {
				// this user belongs to the defender faction
				potentialDropleadersDefender.put(ac, c);
				l = l + "(potential droplead for defender) ";
			}
			logger.debug(l);

			characterRoleMap.put(ac.getCharacterID(), ac);

//			if (c.getId().equals(Nexus.getCurrentChar().getId())) {
//				if (ac.getType().equals(Constants.ROLE_ATTACKER_COMMANDER) || ac.getType().equals(Constants.ROLE_DEFENDER_COMMANDER)) {
//					iamdroplead = true;
//				}
//			}

			if (ac.getType().equals(Constants.ROLE_ATTACKER_COMMANDER) || ac.getType().equals(Constants.ROLE_ATTACKER_WARRIOR) || ac.getType().equals(Constants.ROLE_ATTACKER_SUPPORTER)) { // Attacker
				if (ac.getType().equals(Constants.ROLE_ATTACKER_COMMANDER)) { // Droplead
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
			} else if (ac.getType().equals(Constants.ROLE_DEFENDER_COMMANDER) || ac.getType().equals(Constants.ROLE_DEFENDER_WARRIOR) || ac.getType().equals(Constants.ROLE_DEFENDER_SUPPORTER)) {
				if (ac.getType().equals(Constants.ROLE_DEFENDER_COMMANDER)) { // Droplead
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
			} else if (ac.getType().equals(Constants.ROLE_SUPPORTER)) {
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
					ac.setType(Constants.ROLE_DEFENDER_SUPPORTER);
				} else if (atts < defs) {
					lvAttacker.getItems().add(Nexus.getCharacterById(ac.getCharacterID()));
					lvAttacker.getItems().remove(dummy);
					ac.setType(Constants.ROLE_ATTACKER_SUPPORTER);
				} else {
					lvAttacker.getItems().add(Nexus.getCharacterById(ac.getCharacterID()));
					lvAttacker.getItems().remove(dummy);
					ac.setType(Constants.ROLE_ATTACKER_SUPPORTER);
				}
			}
		}

		// are attacker droplead still empty?
		if (lvDropleadAttacker.getItems().size() == 1 && "...".equals(lvDropleadAttacker.getItems().get(0).getName())) {
			for (AttackCharacterDTO ac : potentialDropleadersAttacker.keySet()) {
				lvDropleadAttacker.getItems().remove(0);
				lvDropleadAttacker.getItems().add(potentialDropleadersAttacker.get(ac));
				lvAttacker.getItems().remove(potentialDropleadersAttacker.get(ac));
				lvDefender.getItems().remove(potentialDropleadersAttacker.get(ac));
				if (lvAttacker.getItems().size() == 0) {
					lvAttacker.getItems().add(dummy);
				}
				if (lvDefender.getItems().size() == 0) {
					lvDefender.getItems().add(dummy);
				}
			}
		}
		// are defender droplead still empty?
		logger.debug("Size" + lvDropleadDefender.getItems().size());
		if (lvDropleadDefender.getItems().size() == 1 && "...".equals(lvDropleadDefender.getItems().get(0).getName())) {
			for (AttackCharacterDTO ac : potentialDropleadersDefender.keySet()) {
				lvDropleadDefender.getItems().remove(0);
				lvDropleadDefender.getItems().add(potentialDropleadersDefender.get(ac));
				lvDefender.getItems().remove(potentialDropleadersDefender.get(ac));
				lvAttacker.getItems().remove(potentialDropleadersDefender.get(ac));
				if (lvDefender.getItems().size() == 0) {
					lvDefender.getItems().add(dummy);
				}
				if (lvAttacker.getItems().size() == 0) {
					lvAttacker.getItems().add(dummy);
				}
			}
		}

		btnToRight.setDisable(true);
		btnToLeft.setDisable(true);
		btnKick.setDisable(true);
		btnPromote.setDisable(true);

		lvDropleadAttacker.getSelectionModel().clearSelection();
		lvDropleadDefender.getSelectionModel().clearSelection();
		lvAttacker.getSelectionModel().clearSelection();
		lvDefender.getSelectionModel().clearSelection();

		checkConditionsToStartDrop(null);

		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("2");
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
			case CHANGE_LANGUAGE:
				setStrings();
				break;

			case START_ROLEPLAY:
				if(ROLEPLAYENTRYTYPES.C3_RP_STEP_V8 == o.getObject()) {
					logger.debug("RolePlayIntroPaneController -> START_ROLEPLAY");
					if(bOnlyOneSave) {
						bOnlyOneSave = false;
						saveAttack();
					}
					// set current step of story
					getStoryValues(getCurrentRP());
				}
				break;

			case PANE_DESTROY_CURRENT:
			case PANE_CREATION_BEGINS:
				Platform.runLater(() -> {
					backgroundImage.setOpacity(0.0f);
					vbLeft.setOpacity(0.0f);
					vbRight.setOpacity(0.0f);
					apCenter.setOpacity(0.0f);
					hbButtons.setOpacity(0.0f);
				});
				break;

			case PANE_CREATION_FINISHED:
				if (o.getObject() instanceof RolePlayBasicPane) {
					AbstractC3Pane p = (AbstractC3Pane) o.getObject();
					if ("AttackPane".equals(p.getPaneName())) {
//						if (!firstCreationDone) {
							Platform.runLater(this::buildGuiEffect);

//						}
					}
				}
				break;

			case FINALIZE_ROUND:
				checkToCancelInvasion();
				break;

			case UPDATE_USERS_FOR_ATTACK:
				logger.info("The userlist has changed. Update information on the listboxes.");
				logger.debug("##### Userlist update event received.");
				if (Nexus.getCurrentAttackOfUser() != null) {
					logger.debug("##### I have an attack.");
					List<AttackCharacterDTO> l = Nexus.getCurrentAttackOfUser().getAttackCharList();
					boolean kicked = true;
					for (AttackCharacterDTO ac : l) {
						if (ac.getCharacterID().equals(Nexus.getCurrentChar().getId())) {
							kicked = false;
						}
					}
					if (kicked) {
						// I have been kicked from the lobby, need to change the currently displayed pane
						logger.debug("##### I have been kicked...");
						ActionManager.getAction(ACTIONS.SWITCH_TO_MAP).execute();
					} else {
						logger.debug("##### Updating lists...");
						updateLists(Nexus.getCurrentAttackOfUser());
					}
				} else {
					logger.debug("##### I do NOT have an attack.");
				}
				break;

			case NEW_PLAYERLIST_RECEIVED:
				// This should happen in UPDATE_USERS_FOR_ATTACK
				// TODO: Remove listener and this case here if it is not needed!
				// updateLists(Nexus.getCurrentAttackOfUser());
				break;

			default:
				break;
		}
		return true;
	}

	@Override
	public void setStrings() {
		Platform.runLater(() -> {
			// clear selection
			lvAttacker.getSelectionModel().clearSelection();
			lvDefender.getSelectionModel().clearSelection();
			lvDropleadAttacker.getSelectionModel().clearSelection();
			lvDropleadDefender.getSelectionModel().clearSelection();

			// set strings
			btnLeave.setText(Internationalization.getString("C3_Lobby_Leave"));
			btNext.setText(Internationalization.getString("C3_Lobby_Next"));
			btnPromote.setText(Internationalization.getString("C3_Lobby_Promote"));

			BOAttack a = Nexus.getCurrentAttackOfUser();
			String attackerShortName = Nexus.getBoUniverse().getFactionByID(a.getAttackerFactionId().longValue()).getShortName();
			String defenderShortName = Nexus.getBoUniverse().getFactionByID(a.getDefenderFactionId().longValue()).getShortName();
			lAttacker.setText(a.getAttackerFactionName());
			lAttackerShortname.setText("(" + attackerShortName + ")");
			lDefender.setText(a.getDefenderFactionName());
			lDefenderShortname.setText("(" + defenderShortName + ")");
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
		/*
		if(currentChar.getStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1 ){
			Long rp = getCurrentRP().getNextStepID();
			saveNextStep(rp);
		}*/
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		dummy.setName("...");

		BOAttack a = Nexus.getCurrentAttackOfUser();

		if (a != null) {
			Nexus.setStoryBeforeSaving(a.getStoryId().longValue());
			updateLists(a);
			lvAttacker.getSelectionModel().clearSelection();
			lvDefender.getSelectionModel().clearSelection();
			btnKick.setDisable(true);
			btnPromote.setDisable(true);
			btnToLeft.setDisable(true);
			btnToRight.setDisable(true);
			btNext.setDisable(true);

			backgroundImage.setOpacity(0.0f);
			vbLeft.setOpacity(0.0f);
			vbRight.setOpacity(0.0f);
			apCenter.setOpacity(0.0f);
			hbButtons.setOpacity(0.0f);

			lSystemName.setText(a.getStarSystemName());
			String attackerShortName = Nexus.getBoUniverse().getFactionByID(a.getAttackerFactionId().longValue()).getShortName();
			String defenderShortName = Nexus.getBoUniverse().getFactionByID(a.getDefenderFactionId().longValue()).getShortName();
			lAttacker.setText(a.getAttackerFactionName());
			lAttackerShortname.setText("(" + attackerShortName + ")");
			lDefender.setText(a.getDefenderFactionName());
			lDefenderShortname.setText("(" + defenderShortName + ")");

			String attackerlogo = Nexus.getBoUniverse().getFactionByID(a.getAttackerFactionId().longValue()).getLogo();
			Image imageAttackerLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/factions/" + attackerlogo)));
			String defenderlogo = Nexus.getBoUniverse().getFactionByID(a.getDefenderFactionId().longValue()).getLogo();
			Image imageDefenderLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/factions/" + defenderlogo)));
			ivAttackerLogo.setImage(imageAttackerLogo);
			ivDefenderLogo.setImage(imageDefenderLogo);
			ivPlanet.setImage(Nexus.getBoUniverse().starSystemBOs.get(a.getStarSystemId()).getSystemImage());

			String attackerRankName = "";
			if (a.getAttackerFactionId().equals(36)) { // CW
				attackerRankName = attackerRankName + "CW/SCapt.png";
			} else if (a.getAttackerFactionId().equals(30)) { // CGB
				attackerRankName = attackerRankName + "CGB/SCapt.png";
			} else if (a.getAttackerFactionId().equals(32)) { // CJF
				attackerRankName = attackerRankName + "CJF/SCapt.png";
			} else if (a.getAttackerFactionId().equals(11)) { // LA
				attackerRankName = attackerRankName + "LA/Hauptmann.png";
			} else if (a.getAttackerFactionId().equals(7)) { // FRR
				attackerRankName = attackerRankName + "FRR/Kapten.png";
			} else if (a.getAttackerFactionId().equals(5)) { // DC
				attackerRankName = attackerRankName + "DC/Tai-i.png";
			}

			String defenderRankName = "";
			if (a.getDefenderFactionId().equals(36)) { // CW
				defenderRankName = defenderRankName + "CW/SCapt.png";
			} else if (a.getDefenderFactionId().equals(30)) { // CGB
				defenderRankName = defenderRankName + "CGB/SCapt.png";
			} else if (a.getDefenderFactionId().equals(32)) { // CJF
				defenderRankName = defenderRankName + "CJF/SCapt.png";
			} else if (a.getDefenderFactionId().equals(9)) { // LA
				defenderRankName = defenderRankName + "LA/Hauptmann.png";
			} else if (a.getDefenderFactionId().equals(7)) { // FRR
				defenderRankName = defenderRankName + "FRR/Kapten.png";
			} else if (a.getDefenderFactionId().equals(5)) { // DC
				defenderRankName = defenderRankName + "DC/Tai-i.png";
			}

			if (!"".equals(attackerRankName)) {
				Image attackerRank = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ranks/" + attackerRankName)));
				ivAttackerRank.setImage(attackerRank);
			}
			if (!"".equals(defenderRankName)) {
				Image defenderRank = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ranks/" + defenderRankName)));
				ivDefenderRank.setImage(defenderRank);
			}

			Callback<ListView<RolePlayCharacterDTO>, ListCell<RolePlayCharacterDTO>> renderer = new Callback<>() {
				@Override
				public ListCell<RolePlayCharacterDTO> call(ListView<RolePlayCharacterDTO> param) {
					return new ListCell<>() {
						@Override
						protected void updateItem(RolePlayCharacterDTO item, boolean empty) {
							super.updateItem(item, empty);
							if (item != null) {
								boolean online = Nexus.getUserIsOnline(item.getId());

								if (online) {
									Platform.runLater(() -> {
										BOFaction faction = Nexus.getBoUniverse().getFactionByID(item.getFactionId().longValue());
										String t = "[" + faction.getShortName() + "] " + item.getName();
										if (t.length() > 25) {
											t = t.substring(1, 22) + "...";
										}
										setText(t);
										setTooltip(null);
										setStyle("-fx-text-fill: white;");

										for (Node n : getChildren()) {
											if (n instanceof Text) {
												n.setStyle("-fx-strikethrough:false;");
											}
										}
									});
								} else {
									if (!"...".equals(item.getName())) {
										Platform.runLater(() -> {
											BOFaction faction = Nexus.getBoUniverse().getFactionByID(item.getFactionId().longValue());
											String t = "[" + faction.getShortName() + "] " + item.getName() + ""; // offline
											if (t.length() > 25) {
												t = t.substring(1, 22) + "...";
											}
											setText(t);
											setTooltip(new Tooltip("Offline"));
											setStyle("-fx-text-fill:cyan;");

											for (Node n : getChildren()) {
												if (n instanceof Text) {
													n.setStyle("-fx-strikethrough:true;");
												}
											}
										});
									} else {
										Platform.runLater(() -> {
											String t = item.getName();
											if (t.length() > 25) {
												t = t.substring(1, 22) + "...";
											}
											setText(t);
											setTooltip(null);
											setStyle("-fx-text-fill:white;");
										});
									}
								}
							} else {
								Platform.runLater(() -> {
									setText(null);
									setGraphic(null);
								});
							}
						}
					};
				}
			};

			lvAttacker.setCellFactory(renderer);
			lvDefender.setCellFactory(renderer);
			lvDropleadAttacker.setCellFactory(renderer);
			lvDropleadDefender.setCellFactory(renderer);

			setStrings();
			buildGuiEffect();
			checkConditionsToStartDrop(null);
		}
	}
}
