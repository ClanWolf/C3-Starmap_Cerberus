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

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.dtos.AttackCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO;
import net.clanwolf.starmap.transfer.dtos.UserDTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayPrepareBattlePaneController extends AbstractC3RolePlayController implements ActionCallBackListener {

//	private HashMap<Long, Boolean> animationPlayedMap = new HashMap<>();
	private final RolePlayCharacterDTO dummy = new RolePlayCharacterDTO();
	private boolean iamdroplead = false;
	private final HashMap<Long, Long> characterRoleMap = new HashMap<>();
	private boolean firstCreationDone = false;
	private boolean creating = false;

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
//			Long role = characterRoleMap.get(selectedChar.getId());
			btnToLeft.setDisable(true);
			btnToRight.setDisable(!iamdroplead);
			btnKick.setDisable(!iamdroplead);
			btnPromote.setDisable(!iamdroplead);
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
	public void handleAttackerDropleadMouseClick() {
		lvAttacker.getSelectionModel().clearSelection();
		lvDefender.getSelectionModel().clearSelection();
		btnPromote.setText(Internationalization.getString("C3_Lobby_Demote"));

		RolePlayCharacterDTO selectedChar = lvDropleadAttacker.getSelectionModel().getSelectedItem();
		if (selectedChar != null && !selectedChar.getName().equals("...")) {
//			Long role = characterRoleMap.get(selectedChar.getId());
			btnToLeft.setDisable(true); // the droplead can not be switched while droplead, degrade first
			btnToRight.setDisable(true); // the droplead can not be switched while droplead, degrade first
			btnKick.setDisable(true); // the droplead can not be kicked while droplead, degrade first
			btnPromote.setDisable(!iamdroplead);
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
	public void handleDefenderListMouseClick() {
		lvAttacker.getSelectionModel().clearSelection();
		lvDropleadAttacker.getSelectionModel().clearSelection();
		lvDropleadDefender.getSelectionModel().clearSelection();
		btnPromote.setText(Internationalization.getString("C3_Lobby_Promote"));

		RolePlayCharacterDTO selectedChar = lvDefender.getSelectionModel().getSelectedItem();
		if (selectedChar != null && !selectedChar.getName().equals("...")) {
			Long role = characterRoleMap.get(selectedChar.getId());
			btnToLeft.setDisable(!iamdroplead);
			btnToRight.setDisable(true);
			btnKick.setDisable(!iamdroplead);
			btnPromote.setDisable(!iamdroplead || role == 4L); // No promotion for players from 3rd factions
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
	}

	@FXML
	public void handleDefenderDropleadMouseClick() {
		lvAttacker.getSelectionModel().clearSelection();
		lvDefender.getSelectionModel().clearSelection();
		btnPromote.setText(Internationalization.getString("C3_Lobby_Demote"));

		RolePlayCharacterDTO selectedChar = lvDropleadDefender.getSelectionModel().getSelectedItem();
		if (selectedChar != null && !selectedChar.getName().equals("...")) {
//			Long role = characterRoleMap.get(selectedChar.getId());
			btnToLeft.setDisable(true); // the droplead can not be switched while droplead, degrade first
			btnToRight.setDisable(true); // the droplead can not be switched while droplead, degrade first
			btnKick.setDisable(true); // the droplead can not be kicked while droplead, degrade first
			btnPromote.setDisable(!iamdroplead);
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
				ListCell<RolePlayCharacterDTO> cell = new ListCell<RolePlayCharacterDTO>() {
					@Override
					protected void updateItem(RolePlayCharacterDTO item, boolean empty) {
						super.updateItem(item, empty);
						if (!empty || item != null) {
							boolean online = false;
							for (UserDTO u : Nexus.getCurrentlyOnlineUserList()) {
								if (u.getCurrentCharacter().getId().equals(item.getId())) {
									online = true;
									break;
								}
							}
							if (online) {
								Platform.runLater(() -> setText(item.getName()));
							} else {
								if (!"...".equals(item.getName())) {
									Platform.runLater(() -> setText(item.getName() + " (offline)"));
								} else {
									Platform.runLater(() -> setText(item.getName()));
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
				return cell;
			}
		};

		lvAttacker.setCellFactory(renderer);
		lvDefender.setCellFactory(renderer);
		lvDropleadAttacker.setCellFactory(renderer);
		lvDropleadDefender.setCellFactory(renderer);

		setStrings();
		buildGuiEffect();
		C3Logger.info("Init ^^^^^------------------------------------------------------------------ WIESO KOMMEN WIR HIER ZWEIMAL REIN ---" + a.getAttackDTO().getId());
	}

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
		lvDropleadAttacker.getSelectionModel().clearSelection();
		lvDropleadDefender.getSelectionModel().clearSelection();
		lvAttacker.getSelectionModel().clearSelection();
		lvDefender.getSelectionModel().clearSelection();
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
					C3Logger.debug("RolePlayIntroPaneController -> START_ROLEPLAY");

					// set current step of story
					//getStoryValues(getCurrentRP());
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

			case UPDATE_USERS_FOR_ATTACK:
				C3Logger.info("The userlist has changed. Update information on the listboxes.");
				if (o.getObject() instanceof BOAttack) {
					BOAttack a = (BOAttack) o.getObject();
					updateLists(a);
				}
				break;

			case NEW_PLAYERLIST_RECEIVED:
				updateLists(Nexus.getCurrentAttackOfUser());
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

	/******************************** THIS ********************************/
	@Override
	public void getStoryValues(RolePlayStoryDTO rpChar){
		// play sound
		if (rpChar.getStory().getStoryMP3() != null) {
			C3SoundPlayer.play(BORolePlayStory.getRPG_Soundfile(rpChar.getStory()), false);
		}
	} //getStoryValues
}
