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
import javafx.scene.control.CheckBox;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.nexus.Nexus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

public class DiplomacyPaneController extends AbstractC3Controller implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static DiplomacyPaneController instance = null;

	@Override
	public void setStrings() {

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

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_DESTROY_CURRENT, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_BEGINS, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.LOGON_FINISHED_SUCCESSFULL, this);
		ActionManager.addActionCallbackListener(ACTIONS.TERMINAL_COMMAND, this);
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

	private void init() {
		instance = this;
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
