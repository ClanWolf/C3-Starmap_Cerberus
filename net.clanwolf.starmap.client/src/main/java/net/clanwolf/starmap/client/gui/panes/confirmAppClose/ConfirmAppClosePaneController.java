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
package net.clanwolf.starmap.client.gui.panes.confirmAppClose;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.util.Internationalization;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Meldric
 */
public class ConfirmAppClosePaneController extends AbstractC3Controller implements ActionCallBackListener {

	@FXML
	private Label panelHeadline;
	@FXML
	private Button buttonYes;
	@FXML
	private Button buttonNo;
	@FXML
	private TextArea shutdownMessageArea;

	@FXML
	private void handleCancelButtonClick() {
		ActionManager.getAction(ACTIONS.PANE_DESTROY_CURRENT).execute();
	}

	@FXML
	private void handleConfirmButtonClick() {
		buttonYes.setDisable(true);
		buttonNo.setDisable(true);
		ActionManager.getAction(ACTIONS.APPLICATION_EXIT).execute();
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
	}

	/**
	 * Initializes the controller class.
	 *
	 * @param url
	 *            The url
	 * @param rb
	 *            The ResourceBundle
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		panelHeadline.setText(Internationalization.getString("app_pane_confirm_close_headline"));
		shutdownMessageArea.setText(Internationalization.getString("app_pane_confirm_close_message"));
		buttonNo.setText(Internationalization.getString("general_no"));
		buttonYes.setText(Internationalization.getString("general_yes"));
	}

	/**
	 * Set the strings on the gui (on initialize and on language change).
	 */
	@Override
	public void setStrings() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				panelHeadline.setText(Internationalization.getString("app_pane_confirm_close_headline"));
				shutdownMessageArea.setText(Internationalization.getString("app_pane_confirm_close_message"));
				buttonNo.setText(Internationalization.getString("general_no"));
				buttonYes.setText(Internationalization.getString("general_yes"));
			}
		});
	}

	/**
	 * Handle Actions
	 *
	 * @param action
	 *            The action to be handled.
	 * @param o
	 *            The ActionObject that has been passed with the Action when it was fired.
	 * @return Boolean to continue processing (may not be false!).
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
		case CHANGE_LANGUAGE:
			setStrings();
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 
	 */
	@Override
	public void warningOnAction() {
	}

	/**
	 * 
	 */
	@Override
	public void warningOffAction() {
	}
}
