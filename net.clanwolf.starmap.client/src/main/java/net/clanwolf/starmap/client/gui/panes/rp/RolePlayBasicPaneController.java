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

	private ROLEPLAYENTRYTYPES myType;
	private HashMap<ROLEPLAYENTRYTYPES, Pane> storyPanes;
	private HashMap<ROLEPLAYENTRYTYPES, AbstractC3RolePlayController> storyController;

	private boolean isCharacterPane = true;

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
		storyController = new HashMap<>();
	}

	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case PANE_CREATION_FINISHED:
				C3Logger.debug("PANE_CREATION_FINISHED Object: " + o.getObject().toString());
				if(o.getObject() instanceof RolePlayBasicPane){
					RolePlayBasicPane rpbp = (RolePlayBasicPane)o.getObject();
					// do this for the right pane
					if(paneName.equals(rpbp.getPaneName())) {
						loadScreen();
					}
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
		// here is nothing to do
	}

	@Override
	public void warningOnAction() {
		// here is nothing to do
	}

	@Override
	public void warningOffAction() {
		//
	}

	//******************************** FXML ********************************/


	//******************************** THIS ********************************/

	private void loadScreen(){
		// get the correct ROLEPLAYENTRYTYPE from the right story
		if (paneName.equals("AttackPane") && Nexus.getCurrentAttackOfUser() != null) {
			isCharacterPane = false;
			myType = Nexus.getBoUniverse().getAttackStories().get(Nexus.getCurrentAttackOfUser().getAttackDTO().getStoryID()).getVariante();
		} else if (paneName.equals("CharacterPane")){
			isCharacterPane = true;
			myType = Nexus.getCurrentChar().getStory().getVariante();
		}

		C3Logger.debug("Pane: " + this + " Flag isCharacterPane: " + isCharacterPane);

		if( myType != null) {
			switch (myType) {
				case C3_RP_STORY:
					changePaneAndController(ROLEPLAYENTRYTYPES.C3_RP_STORY, "/fxml/RolePlayIntroPane.fxml");
					break;
				case C3_RP_CHAPTER:
					changePaneAndController(ROLEPLAYENTRYTYPES.C3_RP_CHAPTER, "/fxml/RolePlayIntroPane.fxml");
					break;
				case C3_RP_STEP_V1:
					changePaneAndController(ROLEPLAYENTRYTYPES.C3_RP_STEP_V1, "/fxml/RolePlayIntroPane.fxml");
					break;
				case C3_RP_STEP_V2:
					changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(), "/fxml/RolePlayChoicePane.fxml");
					break;
				case C3_RP_STEP_V3:
					changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(), "/fxml/RolePlayDataInputPane.fxml");
					break;
				case C3_RP_STEP_V4:
					changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(), "/fxml/RolePlayDicePane.fxml");
					break;
				case C3_RP_STEP_V5:
					changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(), "/fxml/RolePlayChoicePaneImageLeft.fxml");
					break;
				case C3_RP_STEP_V6:
					changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(), "/fxml/RolePlayKeypadPane.fxml");
					break;
				case C3_RP_STEP_V7:
					changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(), "/fxml/RolePlayMessagePane.fxml");
					break;
				case C3_RP_STEP_V8:
					changePaneAndController(Nexus.getCurrentChar().getStory().getVariante(), "/fxml/RolePlayPrepareBattlePane.fxml");
					break;
				default:
					break;
			}
		}
	}

	private void changePaneAndController(ROLEPLAYENTRYTYPES type, String fxmlPane){

		Pane myPane = storyPanes.get(myType);

		if(myPane == null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPane));

						Pane pane = loader.load();

						//AbstractC3Controller controller = loader.getController();
						AbstractC3RolePlayController controller = loader.getController();
						// It is nessassary to make a difference to save a char RP or an attack rp in the controller
						controller.setIsCharRP(isCharacterPane);
						controller.addActionCallBackListeners();

						anchorPane.getChildren().clear();
						anchorPane.getChildren().setAll(pane);

						storyPanes.put(myType, pane);
						storyController.put(myType,controller);
						C3Logger.debug("changePaneAndController -> create new Pane: " + pane);

						ActionManager.getAction(ACTIONS.START_ROLEPLAY).execute(myType);
					} catch (IOException ioe) {
						C3Logger.exception(ioe.getMessage(),ioe);
					}
				}
			});
		} else {
			Platform.runLater(() -> {
				C3Logger.debug("changePaneAndController -> found Pane: " + myPane);
				anchorPane.getChildren().clear();

				AbstractC3RolePlayController rpController = storyController.get(myType);
				rpController.setIsCharRP(isCharacterPane);

				anchorPane.getChildren().setAll(myPane);

				ActionManager.getAction(ACTIONS.START_ROLEPLAY).execute(myType);
			});
		}
	} //
}
