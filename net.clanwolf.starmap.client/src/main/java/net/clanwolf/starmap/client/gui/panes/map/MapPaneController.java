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
package net.clanwolf.starmap.client.gui.panes.map;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.UniverseDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.enums.UNIVERSECONTEXT;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the starmap panel.
 *
 * @author Meldric
 */
public class MapPaneController extends AbstractC3Controller implements ActionCallBackListener {

	private BOUniverse boUniverse = null;

	@FXML
	AnchorPane anchorPane;

	/**
	 * Adds action callback listeners.
	 */
	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.NEW_UNIVERSE_RECEIVED, this);
	}

	/**
	 * Initializes the pane controller.
	 *
	 * @param url
	 *          the url.
	 * @param rb
	 *          the resource bundle containing the language strings.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
	}

	/**
	 * Initializes the universe star map from the universe business object.
	 */
	private void initializeMap() {
		if (boUniverse != null) {
			C3Logger.info("Beginning to build the star map from received universe data.");

			// TODO: Build the starmap

			C3Logger.info("Finished to build the star map.");
		}
	}

	/**
	 * Handles actions.
	 *
	 * @param action
	 *            incoming action to be handled
	 * @param o
	 *            the action object passed along with the action
	 * @return
	 *            wether the handling should continue (this should be true in general)
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case NEW_UNIVERSE_RECEIVED:
				// A new universeDTO has been broadcasted by the server
				UniverseDTO universeDTO = Nexus.getUniverseDTO();
				boUniverse = new BOUniverse(universeDTO);
				Nexus.setBOUniverse(boUniverse);

				initializeMap();

				break;

			case CHANGE_LANGUAGE:
				setStrings();
				break;

			case PANE_CREATION_FINISHED:
				GameState state = new GameState();
				state.setMode(GAMESTATEMODES.GET_UNIVERSE_DATA);
				state.addObject(UNIVERSECONTEXT.HH);
				Nexus.fireNetworkEvent(state);
				break;

			default:
				break;
		}
		return true;
	}

	/**
	 * Sets strings for the current gui panel. This is done when the user switches languages.
	 */
	@Override
	public void setStrings() {
//		Platform.runLater(() -> {
//			//
//		});
	}

	/**
	 * If a warning is active, this is what happens if the warning is on.
	 */
	@Override
	public void warningOnAction() {
		//
	}

	/**
	 * If a warning is active, this is what happens if the warning is off.
	 */
	@Override
	public void warningOffAction() {
		//
	}
}
