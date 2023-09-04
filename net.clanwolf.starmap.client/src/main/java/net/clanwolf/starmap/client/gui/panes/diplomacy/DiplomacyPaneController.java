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
 * Copyright (c) 2001-2023, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes.diplomacy;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.DiplomacyDTO;
import net.clanwolf.starmap.transfer.dtos.FactionDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.util.Compressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.*;

public class DiplomacyPaneController extends AbstractC3Controller implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@FXML
	ImageView ivFaction01, ivFaction02, ivFaction03, ivFaction04, ivFaction05, ivFaction06, ivFaction07, ivFaction08;

	@FXML
	ImageView ivAllied01, ivAllied02, ivAllied03, ivAllied04, ivAllied05, ivAllied06, ivAllied07, ivAllied08;

	@FXML
	Label labelFactionShort01, labelFactionShort02, labelFactionShort03, labelFactionShort04, labelFactionShort05, labelFactionShort06, labelFactionShort07, labelFactionShort08;

	@FXML
	Label LabelTheirStatus01, LabelTheirStatus02, LabelTheirStatus03, LabelTheirStatus04, LabelTheirStatus05, LabelTheirStatus06, LabelTheirStatus07, LabelTheirStatus08;

	@FXML
	RadioButton checkbEnemy01, checkbEnemy02, checkbEnemy03, checkbEnemy04, checkbEnemy05, checkbEnemy06, checkbEnemy07, checkbEnemy08;

	@FXML
	RadioButton checkbAlly01, checkbAlly02, checkbAlly03, checkbAlly04, checkbAlly05, checkbAlly06, checkbAlly07, checkbAlly08;

	@FXML
	Button buttonSave, buttonCancel;

	@FXML
	Label panelHeadline, labelHeadlineTheirStatus, labelHeadlineEnemy, labelHeadlineAlly;

	private final ArrayList<BOFaction> factions = new ArrayList<>();
	private final ArrayList<Label> labelShortNameList = new ArrayList<>();
	private final ArrayList<Label> labelTheirStatusList = new ArrayList<>();
	private final ArrayList<ImageView> imageFactionLogoList = new ArrayList<>();
	private final ArrayList<ImageView> imageAlliedLogoList = new ArrayList<>();
	private final ArrayList<RadioButton> radioButtonsEnemyList = new ArrayList<>();
	private final ArrayList<RadioButton> radioButtonsAllyList = new ArrayList<>();
	private final ArrayList<ToggleGroup> toggleGroupsList = new ArrayList<>();
	private final ArrayList<Boolean> allianceRequestedByThemList = new ArrayList<>();
	private final ArrayList<Boolean> allianceWaitingForNextRoundList = new ArrayList<>();
	private final ArrayList<Long> factionsWeAreFriendlyWithNowForSaving = new ArrayList<>();
	private static DiplomacyPaneController instance = null;

	private final Image diplomacyIconNone = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_enemies.png")));
	private final Image diplomacyIconLeft = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_left.png")));
	private final Image diplomacyIconRight = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_right.png")));
	private final Image diplomacyIconAllied = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_allies.png")));
	private final Image diplomacyIconAlliedWaiting = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_allies_waiting.png")));


	@Override
	public void setStrings() {
		panelHeadline.setText(Internationalization.getString("app_diplomacy_infotext"));
		labelHeadlineTheirStatus.setText(Internationalization.getString("app_diplomacy_column_Status"));
		labelHeadlineEnemy.setText(Internationalization.getString("app_diplomacy_column_StatusEnemy"));
		labelHeadlineAlly.setText(Internationalization.getString("app_diplomacy_column_StatusAlly"));

		try {
			for (int i = 0; i < 8; i++) {
				if (allianceRequestedByThemList.get(i)) {
					labelTheirStatusList.get(i).setText(Internationalization.getString("app_diplomacy_column_StatusFriendly"));
				} else {
					labelTheirStatusList.get(i).setText(Internationalization.getString("app_diplomacy_column_StatusEnemy"));
				}
			}
		} catch (IndexOutOfBoundsException ignore) {
		}

		buttonSave.setText(Internationalization.getString("general_save"));
		buttonCancel.setText(Internationalization.getString("general_cancel"));
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_DESTROY_CURRENT, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_BEGINS, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGON_FINISHED_SUCCESSFULL, this);
		ActionManager.addActionCallbackListener(ACTIONS.TERMINAL_COMMAND, this);
		ActionManager.addActionCallbackListener(ACTIONS.DIPLOMACY_SITUATION_CHANGED, this);
	}

	/**
	 * Handles actions.
	 *
	 * @param action incoming action to be handled
	 * @param o      the action object passed along with the action
	 * @return wether the handling should continue (this should be true in general)
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case LOGON_FINISHED_SUCCESSFULL:
				break;

			case CHANGE_LANGUAGE:
				setStrings();
				break;

			case PANE_DESTROY_CURRENT:
				break;

			case PANE_CREATION_BEGINS:
				break;

			case PANE_CREATION_FINISHED:
				if (o.getObject().getClass() == DiplomacyPane.class) {
					logger.info("Diplomacy window opened.");
					init();
				}
				break;

			case DIPLOMACY_SITUATION_CHANGED:
				logger.info("Diplomacy status changed.");
				init();
				break;

			case TERMINAL_COMMAND:
				String com1 = o.getText();
				if (Nexus.isLoggedIn()) {
					if (Nexus.getCurrentlyOpenedPane() instanceof DiplomacyPane) {
						if (!com1.startsWith("*!!!*")) {
							handleCommand(com1);
						}
					}
				}
				break;

			default:
				break;
		}
		return true;
	}

	@FXML
	public void handleSaveButtonClick() {
		factionsWeAreFriendlyWithNowForSaving.clear();
		for (int i=0; i<8; i++) {
			if (radioButtonsAllyList.get(i).isSelected()) {
				factionsWeAreFriendlyWithNowForSaving.add(factions.get(i).getFactionDTO().getId());
			}
		}

		GameState saveDiplomacyState = new GameState();
		saveDiplomacyState.setMode(GAMESTATEMODES.DIPLOMACY_SAVE);
		saveDiplomacyState.addObject(factionsWeAreFriendlyWithNowForSaving);
		saveDiplomacyState.addObject2(Nexus.getCurrentChar().getFactionId().longValue());
		Nexus.fireNetworkEvent(saveDiplomacyState);
	}

	@Override
	public void setFocus() {
		Platform.runLater(() -> {
			//
		});
	}

	@Override
	public void warningOnAction() {
	}

	@Override
	public void warningOffAction() {
	}

	@FXML
	public void handleCancelButtonClick() {
		init();
	}

	/**
	 * Initializes the controller class.
	 *
	 * @param url Link
	 * @param rb ResourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
	}

	@FXML
	public void handleRadioButtonSelection() {
		buttonSave.setDisable(false);
		for (int i=0; i<8; i++) {
			BOFaction f = factions.get(i);

			boolean weFriendly = false;
			boolean theyFriendly = false;

			if (radioButtonsAllyList.get(i).isSelected()) {
				weFriendly = true;
			}

			if (labelTheirStatusList.get(i).getText().equals(Internationalization.getString("app_diplomacy_column_StatusFriendly"))) {
				theyFriendly = true;
			}

			imageAlliedLogoList.get(i).setImage(diplomacyIconNone);

			if (theyFriendly && !weFriendly) {
				imageAlliedLogoList.get(i).setImage(diplomacyIconLeft);
			}
			if (!theyFriendly && weFriendly) {
				imageAlliedLogoList.get(i).setImage(diplomacyIconRight);
			}
			if (theyFriendly && weFriendly) {
				if (allianceWaitingForNextRoundList.get(i)) {
					imageAlliedLogoList.get(i).setImage(diplomacyIconAlliedWaiting);
				} else {
					imageAlliedLogoList.get(i).setImage(diplomacyIconAllied);
				}
			}
		}
	}

	private void init() {
		instance = this;

		factions.clear();
		for (BOFaction fa : Nexus.getBoUniverse().getActiveFactions()) { // all factions that have a jumpship
			if (!fa.getFactionDTO().getId().equals(Nexus.getCurrentChar().getFactionId().longValue())) {
				factions.add(fa);
			}
		}
		Collections.sort(factions);
		Platform.runLater(() -> {
			labelFactionShort01.setText("");
			labelFactionShort02.setText("");
			labelFactionShort03.setText("");
			labelFactionShort04.setText("");
			labelFactionShort05.setText("");
			labelFactionShort06.setText("");
			labelFactionShort07.setText("");
			labelFactionShort08.setText("");

			LabelTheirStatus01.setText("");
			LabelTheirStatus02.setText("");
			LabelTheirStatus03.setText("");
			LabelTheirStatus04.setText("");
			LabelTheirStatus05.setText("");
			LabelTheirStatus06.setText("");
			LabelTheirStatus07.setText("");
			LabelTheirStatus08.setText("");

			labelTheirStatusList.clear();
			labelTheirStatusList.add(LabelTheirStatus01);
			labelTheirStatusList.add(LabelTheirStatus02);
			labelTheirStatusList.add(LabelTheirStatus03);
			labelTheirStatusList.add(LabelTheirStatus04);
			labelTheirStatusList.add(LabelTheirStatus05);
			labelTheirStatusList.add(LabelTheirStatus06);
			labelTheirStatusList.add(LabelTheirStatus07);
			labelTheirStatusList.add(LabelTheirStatus08);

			labelShortNameList.clear();
			labelShortNameList.add(labelFactionShort01);
			labelShortNameList.add(labelFactionShort02);
			labelShortNameList.add(labelFactionShort03);
			labelShortNameList.add(labelFactionShort04);
			labelShortNameList.add(labelFactionShort05);
			labelShortNameList.add(labelFactionShort06);
			labelShortNameList.add(labelFactionShort07);
			labelShortNameList.add(labelFactionShort08);

			imageFactionLogoList.clear();
			imageFactionLogoList.add(ivFaction01);
			imageFactionLogoList.add(ivFaction02);
			imageFactionLogoList.add(ivFaction03);
			imageFactionLogoList.add(ivFaction04);
			imageFactionLogoList.add(ivFaction05);
			imageFactionLogoList.add(ivFaction06);
			imageFactionLogoList.add(ivFaction07);
			imageFactionLogoList.add(ivFaction08);

			imageAlliedLogoList.clear();
			imageAlliedLogoList.add(ivAllied01);
			imageAlliedLogoList.add(ivAllied02);
			imageAlliedLogoList.add(ivAllied03);
			imageAlliedLogoList.add(ivAllied04);
			imageAlliedLogoList.add(ivAllied05);
			imageAlliedLogoList.add(ivAllied06);
			imageAlliedLogoList.add(ivAllied07);
			imageAlliedLogoList.add(ivAllied08);

			radioButtonsEnemyList.clear();
			radioButtonsEnemyList.add(checkbEnemy01);
			radioButtonsEnemyList.add(checkbEnemy02);
			radioButtonsEnemyList.add(checkbEnemy03);
			radioButtonsEnemyList.add(checkbEnemy04);
			radioButtonsEnemyList.add(checkbEnemy05);
			radioButtonsEnemyList.add(checkbEnemy06);
			radioButtonsEnemyList.add(checkbEnemy07);
			radioButtonsEnemyList.add(checkbEnemy08);

			radioButtonsAllyList.clear();
			radioButtonsAllyList.add(checkbAlly01);
			radioButtonsAllyList.add(checkbAlly02);
			radioButtonsAllyList.add(checkbAlly03);
			radioButtonsAllyList.add(checkbAlly04);
			radioButtonsAllyList.add(checkbAlly05);
			radioButtonsAllyList.add(checkbAlly06);
			radioButtonsAllyList.add(checkbAlly07);
			radioButtonsAllyList.add(checkbAlly08);

			ToggleGroup group01 = new ToggleGroup();
			ToggleGroup group02 = new ToggleGroup();
			ToggleGroup group03 = new ToggleGroup();
			ToggleGroup group04 = new ToggleGroup();
			ToggleGroup group05 = new ToggleGroup();
			ToggleGroup group06 = new ToggleGroup();
			ToggleGroup group07 = new ToggleGroup();
			ToggleGroup group08 = new ToggleGroup();

			toggleGroupsList.clear();
			toggleGroupsList.add(group01);
			toggleGroupsList.add(group02);
			toggleGroupsList.add(group03);
			toggleGroupsList.add(group04);
			toggleGroupsList.add(group05);
			toggleGroupsList.add(group06);
			toggleGroupsList.add(group07);
			toggleGroupsList.add(group08);

			allianceRequestedByThemList.clear();
			allianceWaitingForNextRoundList.clear();

			for (int i = 0; i < labelShortNameList.size(); i++) {
				BOFaction f = factions.get(i);

				labelTheirStatusList.get(i).setVisible(true);
				radioButtonsEnemyList.get(i).setVisible(true);
				radioButtonsAllyList.get(i).setVisible(true);
				imageAlliedLogoList.get(i).setVisible(true);
				imageAlliedLogoList.get(i).setImage(diplomacyIconNone);

				boolean weRequestedAlliance = false;
				boolean allianceRequestedByThem = false;
				boolean allianceWaitingForNextRound = false;

				int weRequestedAllianceForRound = -1;
				int allianceRequestedByThemForRound = -1;

				for (DiplomacyDTO d : Nexus.getBoUniverse().getDiplomacy()) {
					if (d.getFactionID_REQUEST().equals(f.getID()) && d.getFactionID_ACCEPTED().equals(Nexus.getCurrentChar().getFactionId().longValue())) {
						allianceRequestedByThem = true;
						allianceRequestedByThemForRound = d.getStartingInRound();
						labelTheirStatusList.get(i).setText(Internationalization.getString("app_diplomacy_column_StatusFriendly"));
					}
					if (d.getFactionID_REQUEST().equals(Nexus.getCurrentChar().getFactionId().longValue()) && d.getFactionID_ACCEPTED().equals(f.getID())) {
						radioButtonsAllyList.get(i).setSelected(true);
						radioButtonsEnemyList.get(i).setSelected(false);
						weRequestedAlliance = true;
						weRequestedAllianceForRound = d.getStartingInRound();
					}
				}
				if (!allianceRequestedByThem) {
					labelTheirStatusList.get(i).setText(Internationalization.getString("app_diplomacy_column_StatusEnemy"));
				}
				if (!weRequestedAlliance) {
					radioButtonsAllyList.get(i).setSelected(false);
					radioButtonsEnemyList.get(i).setSelected(true);
				}
				if (allianceRequestedByThem && !weRequestedAlliance) {
					imageAlliedLogoList.get(i).setImage(diplomacyIconLeft);
				}
				if (!allianceRequestedByThem && weRequestedAlliance) {
					imageAlliedLogoList.get(i).setImage(diplomacyIconRight);
				}

				int cRound = Nexus.getBoUniverse().currentRound;
				if (allianceRequestedByThem && weRequestedAlliance) {
					if ((allianceRequestedByThemForRound <= cRound) && (weRequestedAllianceForRound <= cRound)) {
						imageAlliedLogoList.get(i).setImage(diplomacyIconAllied);
						allianceWaitingForNextRound = false;
					} else {
						imageAlliedLogoList.get(i).setImage(diplomacyIconAlliedWaiting);
						allianceWaitingForNextRound = true;
					}
				}

				allianceWaitingForNextRoundList.add(allianceWaitingForNextRound);
				allianceRequestedByThemList.add(allianceRequestedByThem);

				radioButtonsAllyList.get(i).setToggleGroup(toggleGroupsList.get(i));
				radioButtonsEnemyList.get(i).setToggleGroup(toggleGroupsList.get(i));

				// Faction logo
				String logo = f.getLogo();
				Image imageFaction = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/factions/" + logo)));
				ImageView iv = imageFactionLogoList.get(i);
				iv.setImage(imageFaction);
				iv.setVisible(true);

				// Faction shortname
				Label l = labelShortNameList.get(i);
				l.setText(f.getShortName());
				l.setVisible(true);
			}
			buttonSave.setDisable(true);
		});
	}

	private void handleCommand(String com) {
		boolean sendingString = true;

		if (!com.startsWith("*!!!*")) {
			if (!"".equals(com)) {
				logger.info("Received command: '" + com + "'");
				String lastEntry = null;
				if (Nexus.commandHistory.size() > 0) {
					lastEntry = Nexus.commandHistory.getLast();
				}
				if (lastEntry == null) {
					Nexus.commandHistory.add(com);
				} else if (!Nexus.commandHistory.getLast().equals(com)) {
					Nexus.commandHistory.add(com);
				}
				if (Nexus.commandHistory.size() > 50) {
					Nexus.commandHistory.remove(0);
				}
				Nexus.commandHistoryIndex = Nexus.commandHistory.size();
			}
		}

		if ("*!!!*historyBack".equals(com)) {
			if (Nexus.commandHistoryIndex > 0) {
				Nexus.commandHistoryIndex--;
				logger.info("History back to index: " + Nexus.commandHistoryIndex);
				String histCom = Nexus.commandHistory.get(Nexus.commandHistoryIndex);
				ActionManager.getAction(ACTIONS.SET_TERMINAL_TEXT).execute(histCom);
			}
			sendingString = false;
		}

		if ("*!!!*historyForward".equals(com)) {
			if (Nexus.commandHistoryIndex < Nexus.commandHistory.size() - 1) {
				Nexus.commandHistoryIndex++;
				logger.info("History forward to index: " + Nexus.commandHistoryIndex);
				String histCom = Nexus.commandHistory.get(Nexus.commandHistoryIndex);
				ActionManager.getAction(ACTIONS.SET_TERMINAL_TEXT).execute(histCom);
			}
			sendingString = false;
		}
	}
}
