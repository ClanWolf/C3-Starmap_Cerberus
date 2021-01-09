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
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Controller;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayBasicPaneController extends AbstractC3Controller implements ActionCallBackListener {

	@FXML
	private AnchorPane anchorPane;

	private HashMap<ROLEPLAYENTRYTYPES, Pane> storyPanes;

	public RolePlayBasicPaneController() {
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.PANE_CREATION_FINISHED, this);
		ActionManager.addActionCallbackListener(ACTIONS.ROLEPLAY_NEXT_STEP_CHANGE_PANE, this);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		storyPanes = new HashMap<>();
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
		switch (action) {
		/*case CHANGE_LANGUAGE:
			setStrings();
			break;*/

		case PANE_CREATION_FINISHED:
			C3Logger.debug(o.getObject().toString());
			if(o.getObject() instanceof RolePlayBasicPane){
				loadScreen();
			}
			break;

		case ROLEPLAY_NEXT_STEP_CHANGE_PANE:
			C3Logger.debug("Choose now the next step of story!");
			loadScreen();
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

	//******************************** FXML ********************************/


	//******************************** THIS ********************************/

	private void loadScreen(){

		switch(Nexus.getCurrentChar().getStory().getVariante()){
			case C3_RP_STORY:
			case C3_RP_CHAPTER:
			case C3_RP_STEP_V1:
				changePaneAndController(ROLEPLAYENTRYTYPES.C3_RP_STORY,"/fxml/RolePlayIntroPane.fxml");
				break;
			case C3_RP_STEP_V2:
				changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(),"/fxml/RolePlayChoicePane.fxml");
				break;
			case C3_RP_STEP_V3:
				changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(),"/fxml/RolePlayDataInputPane.fxml");
				break;
			case C3_RP_STEP_V4:
				changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(),"/fxml/RolePlayDicePane.fxml");
				break;
			case C3_RP_STEP_V5:
				changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(), "/fxml/RolePlayChoicePaneImageLeft.fxml");
				break;
			case C3_RP_STEP_V6:
				changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(), "/fxml/RolePlayKeypadPane.fxml");
				break;

			default:
				break;
		}
	}

	private void changePaneAndController(ROLEPLAYENTRYTYPES type, String fxmlPane){

		Pane myPane = storyPanes.get(type);

		if(myPane == null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPane));

						Pane pane = loader.load();

						//AbstractC3Controller controller = loader.getController();
						AbstractC3RolePlayController controller = loader.getController();
						controller.addActionCallBackListeners();

						anchorPane.getChildren().clear();
						anchorPane.getChildren().setAll(pane);

						storyPanes.put(type, pane);
						ActionManager.getAction(ACTIONS.START_ROLEPLAY).execute(type);
					} catch (IOException ioe) {

						C3Logger.exception(ioe.getMessage(),ioe);
					}
				}
			});
		} else {
			Platform.runLater(() -> {

				anchorPane.getChildren().clear();
				anchorPane.getChildren().setAll(myPane);
				ActionManager.getAction(ACTIONS.START_ROLEPLAY).execute(type);

			});
		}
	} //
}
