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
 * Copyright (c) 2001-2024, ClanWolf.net                            |
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
import net.clanwolf.starmap.client.process.universe.BODiplomacy;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import net.clanwolf.starmap.client.process.universe.DiplomacyState;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
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
	ImageView ivMyFactionIcon;

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
	private final ArrayList<Boolean> allianceWaitingToBreakNextRoundList = new ArrayList<>();
	private final ArrayList<Long> factionsWeAreFriendlyWithNowForSaving = new ArrayList<>();
	private final ArrayList<DiplomacyState> diplomacyStateList = new ArrayList<>();
	private static DiplomacyPaneController instance = null;

	private final Image diplomacyIconNone = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_enemies.png")));

	@Override
	public void setStrings() {
		panelHeadline.setText(Internationalization.getString("app_diplomacy_infotext"));
		labelHeadlineTheirStatus.setText(Internationalization.getString("app_diplomacy_column_Status"));
		labelHeadlineEnemy.setText(Internationalization.getString("app_diplomacy_column_StatusEnemy"));
		labelHeadlineAlly.setText(Internationalization.getString("app_diplomacy_column_StatusAlly"));

		try {
			for (int i = 0; i < 8; i++) {
				labelTheirStatusList.get(i).setText(diplomacyStateList.get(i).getDiplomacyTheirStateText());
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
		ActionManager.addActionCallbackListener(ACTIONS.DIPLOMACY_SITUATION_CHANGED, this);

		// Added in AbstractC3Controller:
		// ActionManager.addActionCallbackListener(ACTIONS.ENABLE_DEFAULT_BUTTON, this);
		// ActionManager.addActionCallbackListener(ACTIONS.DISABLE_DEFAULT_BUTTON, this);
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

			case ENABLE_DEFAULT_BUTTON:
				enableDefaultButton(true);
				break;

			case DISABLE_DEFAULT_BUTTON:
				enableDefaultButton(false);
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
		ActionManager.getAction(ACTIONS.PANE_DESTROY_CURRENT).execute();
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
		boolean foundOneRequest = false;
		for (int i=0; i<8; i++) {
			boolean weFriendly = false;

			DiplomacyState state = diplomacyStateList.get(i);

			if (radioButtonsAllyList.get(i).isSelected()) {
				weFriendly = true;
				foundOneRequest = true;
				radioButtonsAllyList.get(i).setDisable(false);
				radioButtonsEnemyList.get(i).setDisable(false);
			} else {
				radioButtonsAllyList.get(i).setDisable(true);
				radioButtonsEnemyList.get(i).setDisable(true);
			}

			imageAlliedLogoList.get(i).setImage(state.getFutureStateIcon(weFriendly));
		}
		if(!foundOneRequest) {
			for (int i = 0; i < 8; i++) {
				radioButtonsAllyList.get(i).setDisable(false);
				radioButtonsEnemyList.get(i).setDisable(false);
			}
		}
	}

	private void init() {
		instance = this;
		ivMyFactionIcon.setImage(Nexus.getFactionLogo());

		ActionManager.getAction(ACTIONS.DIPLOMACY_STATUS_PENDING_HIDE).execute();

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
			allianceWaitingToBreakNextRoundList.clear();
			diplomacyStateList.clear();

			int cRound = Nexus.getBoUniverse().currentRound;

			BODiplomacy boDiplomacy = new BODiplomacy(Nexus.getBoUniverse().currentRound, Nexus.getBoUniverse().getDiplomacy());
			Long factionIDAllied = boDiplomacy.isFactionAllied(Nexus.getCurrentFaction().getID(), Nexus.getBoUniverse().factionBOs.values());

			for (int i = 0; i < labelShortNameList.size(); i++) {
				BOFaction otherFaction = factions.get(i);

				radioButtonsAllyList.get(i).setDisable(false);
				radioButtonsEnemyList.get(i).setDisable(false);

				if(factionIDAllied != null && factionIDAllied.longValue() != otherFaction.getID().longValue()) {
					radioButtonsAllyList.get(i).setDisable(true);
					radioButtonsEnemyList.get(i).setDisable(true);
				}

				labelTheirStatusList.get(i).setVisible(true);
				radioButtonsEnemyList.get(i).setVisible(true);
				radioButtonsAllyList.get(i).setVisible(true);
				imageAlliedLogoList.get(i).setVisible(true);
				imageAlliedLogoList.get(i).setImage(diplomacyIconNone);

				radioButtonsAllyList.get(i).setToggleGroup(toggleGroupsList.get(i));
				radioButtonsEnemyList.get(i).setToggleGroup(toggleGroupsList.get(i));

				DiplomacyState s = boDiplomacy.getDiplomacyState(Nexus.getCurrentFaction().getID(), otherFaction.getID());
				diplomacyStateList.add(s);
				imageAlliedLogoList.get(i).setImage(s.getDiplomacyIcon());
				labelTheirStatusList.get(i).setText(s.getDiplomacyTheirStateText());
				switch(s.getState()) {
					case DiplomacyState.NO_ALLIANCE_FOUND -> {
						radioButtonsAllyList.get(i).setSelected(false);
						radioButtonsEnemyList.get(i).setSelected(true);
					}
					case DiplomacyState.CURRENT_ALLIANCE_FOUND -> {
						radioButtonsAllyList.get(i).setSelected(true);
						radioButtonsEnemyList.get(i).setSelected(false);
					}
					case DiplomacyState.PLAYERS_FACTION_REQUEST_CURRENT_ROUND -> {
						radioButtonsAllyList.get(i).setSelected(true);
						radioButtonsEnemyList.get(i).setSelected(false);
					}
					case DiplomacyState.OTHER_FACTION_REQUEST_CURRENT_ROUND -> {
						radioButtonsAllyList.get(i).setSelected(false);
						radioButtonsEnemyList.get(i).setSelected(true);
						allianceRequestedByThemList.add(true);
					}
					case DiplomacyState.ALLIANCE_FOUND_FOR_NEXT_ROUND -> {
						radioButtonsAllyList.get(i).setSelected(true);
						radioButtonsEnemyList.get(i).setSelected(false);
						allianceWaitingForNextRoundList.add(true);
						radioButtonsAllyList.get(i).setDisable(true);
						radioButtonsEnemyList.get(i).setDisable(true);
					}
					case DiplomacyState.PLAYERS_FACTION_REQUEST_NEXT_ROUND -> {
						radioButtonsAllyList.get(i).setSelected(true);
						radioButtonsEnemyList.get(i).setSelected(false);
						allianceRequestedByThemList.add(true);
					}
					case DiplomacyState.OTHER_FACTION_REQUEST_NEXT_ROUND -> {
						radioButtonsAllyList.get(i).setSelected(false);
						radioButtonsEnemyList.get(i).setSelected(true);
					}
					case DiplomacyState.PLAYERS_FACTION_BREAK_ALLIANCE_NEXT_ROUND -> {
						radioButtonsAllyList.get(i).setSelected(false);
						radioButtonsEnemyList.get(i).setSelected(true);
						allianceWaitingToBreakNextRoundList.add(true);
						radioButtonsAllyList.get(i).setDisable(true);
						radioButtonsEnemyList.get(i).setDisable(true);
					}
					case DiplomacyState.OTHER_FACTION_BREAK_ALLIANCE_NEXT_ROUND -> {
						radioButtonsAllyList.get(i).setSelected(true);
						radioButtonsEnemyList.get(i).setSelected(false);
						allianceWaitingToBreakNextRoundList.add(true);
						radioButtonsAllyList.get(i).setDisable(true);
						radioButtonsEnemyList.get(i).setDisable(true);
					}
					case DiplomacyState.FACTIONS_AT_WAR -> {
						radioButtonsAllyList.get(i).setSelected(false);
						radioButtonsEnemyList.get(i).setSelected(true);
						radioButtonsAllyList.get(i).setDisable(true);
						radioButtonsEnemyList.get(i).setDisable(true);
					}
				}

				// Faction logo
				String logo = otherFaction.getLogo();
				Image imageFaction = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logos/factions/" + logo)));
				ImageView iv = imageFactionLogoList.get(i);
				iv.setImage(imageFaction);
				iv.setVisible(true);

				// Faction shortname
				Label l = labelShortNameList.get(i);
				l.setText(otherFaction.getShortName());
				l.setVisible(true);
			}
			buttonSave.setDisable(true);
		});
	}
}
