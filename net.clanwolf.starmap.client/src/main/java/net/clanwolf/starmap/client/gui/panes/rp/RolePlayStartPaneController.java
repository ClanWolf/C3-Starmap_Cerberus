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
 * Copyright (c) 2001-2020, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes.rp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Meldric
 */
public class RolePlayStartPaneController extends AbstractC3Controller implements ActionCallBackListener {

//	private BrowserPane browserPane = null;
	private String currentPage = "";
	private BORolePlayStory rp;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button buttonClose;

	public RolePlayStartPaneController() {
		// browserPane = new BrowserPane(64, 16, 773, 422);
//		browserPane = new BrowserPane(64, 16, 773, 438);
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.START_ROLEPLAY, this);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		rp = new BORolePlayStory();

		buttonClose.setVisible(true);
		buttonClose.setText(">>> SKIP >>>");

	}

//	private void loadPage(String filename) {
//		if (!filename.equals(currentPage)) {anchorPane.getChildren().add(browserPane);
//			try {
//				currentPage = filename;
//				URL url = new URL(currentPage);
////				browserPane.loadURL(url);
//			} catch (Exception e) {
//				Log.exception(this, e);
//			}
//		}
//	}

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
		switch (action) {
		case CHANGE_LANGUAGE:
			setStrings();
			break;

		case PANE_CREATION_FINISHED:
			//loadPage("http://www.clanwolf.net/apps/C3/rpg/generichtml.php?storyname=Test%20im%20Client");
			break;

		case START_ROLEPLAY:
			// loadPage("http://localhost/test/generichtml.php?storyname=Test im Client");
			//String url = rp.getURLForStoryCall(Nexus.getCurrentUser().getCurrentCharacter().getStory());
			//loadPage(url);
			//loadPage("http://www.clanwolf.net/apps/C3/rpg/generichtml.php?storyname=Test%20im%20Client");
			break;

		default:
			break;

		}
		return true;
	}

	@Override
	public void setStrings() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// set strings
			}
		});
	}

	/**
	 * 
	 */
	@Override
	public void warningOnAction() {
		//
	}

	/**
	 * 
	 */
	@Override
	public void warningOffAction() {
		//
	}

	/******************************** FXML ********************************/
	@FXML
	private void handleOnActionButtonClose(){
		/*
		* if currentRolePlay == story or chapter or Step with type C3_RP_STEP_V1 than call ROLEPLAY_GET_CHAPTER_BYSORTORDER
		* if currentRolpPlay == step than call RolePlay.getNextStep
		*
		 */

		/*if(Nexus.getCurrentUser().getCurrentCharacter().getStory().getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY) {
			rp.getNextChapterBySortOrder(Nexus.getCurrentUser().getCurrentCharacter().getStory(), 1);
		}*/
	}

}
