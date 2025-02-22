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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes.rp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.roleplay.BOCharacter;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.transfer.enums.catalogObjects.ICatalogObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDatainputDTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;
import net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes.ROLEPLAYINPUTDATATYPES;
import net.clanwolf.starmap.transfer.util.CatalogLoader;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RPDataInputPaneController extends AbstractC3RolePlayController implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@FXML
	private AnchorPane anchorPane;

	//@FXML
	//private ImageView rpIBackgroundImage;

	//@FXML
	//private ImageView rpImage;

	@FXML
	private TextArea taStoryText;

	@FXML
	private Button btContinue;

	@FXML
	private VBox vBoxDataInput;

	private GridPane gvDataInput;

	private boolean bInit = false;

	public RPDataInputPaneController() {
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.START_ROLEPLAY, this);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		init();

	}

	private void init(){
		taStoryText.setStyle("-fx-opacity: 1");
		taStoryText.setEditable(false);



//		btContinue.setVisible(true);
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
		case START_ROLEPLAY:
			if(ROLEPLAYENTRYTYPES.RP_DATA_INPUT == o.getObject()) {
				logger.info("RolePlayChoicePaneController -> START_ROLEPLAY");

				init();

				// set current step of story
				getStoryValues(getCurrentRP());
			}
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

	/******************************** FXML ********************************/

	@FXML
	private void handleOnActionBtContinue(){
		Long rp = getCurrentRP().getVar3ID().getNextStoryID();
		//hier waren wir dran!!!!!!!!
		//Speicher des Datansatztypes in RPStory Var3
		BOCharacter boChar = new BOCharacter(Nexus.getCurrentChar());
		boChar.setVar3(getCurrentRP().getVar3ID());
		//boChar.saveCharacter();
		// hier werden jetzt alle Daten in BOCharacter geschrieben
		saveNextStep(rp);
	}

	/******************************** THIS ********************************/
	private void setField(ROLEPLAYINPUTDATATYPES t, int row) {
		if (t != null) {

			Label l = new Label();
//			l.setPrefWidth(400);

			gvDataInput.add(l, 1, row);

			l.setText(Internationalization.getString(t.toString()) + ": ");
			switch (t.datatype) {
				case String:
					TextField tf = new TextField();
					//tf.setPrefWidth(400);
					gvDataInput.add(tf,2, row);
					break;
				case SelectionSingle:
				case SelectionMulti:
					try {
						ComboBox<ICatalogObject> cb = new ComboBox<>();
						cb.setPadding(new Insets(2,2,8,2));

						ICatalogObject[] co = CatalogLoader.getList(t.classname);
						if (co != null) {
							for (ICatalogObject o : co) {
								String n = Internationalization.getString("app_rp_storyeditor_roleplayobjecttypes_" + t.classname + "_" + o.getName());
								o.setInternationalName(n);
								cb.getItems().add(o);
							}
						}

						//cb.setPrefWidth(400);
						gvDataInput.add(cb,2, row);
					} catch (Exception ignored){
						//nop
					}
					break;
			}
		}
	}

	@Override
	public void getStoryValues(RolePlayStoryDTO rpStory) {
		// set story image
		Image im = BORolePlayStory.getRPG_Image(rpStory);
		backgroundImage.setImage(im);

		gvDataInput = new GridPane();
		gvDataInput.setHgap(5);
		gvDataInput.setVgap(5);
		gvDataInput.setPadding(new Insets(3,3,3,3));

		// play sound
		if (rpStory.getStoryMP3() != null) {
			C3SoundPlayer.playRPSound(Objects.requireNonNull(BORolePlayStory.getRPG_Soundfile(rpStory)), audioStartedOnce);
			audioStartedOnce = true;
		}

		// TODO_C3: append single chars step by step until the whole text is displaying
		taStoryText.setText(rpStory.getStoryText());

		if(!bInit && rpStory.getVar3ID() != null) {
			RolePlayStoryDatainputDTO rpVar3 = rpStory.getVar3ID();

			setField(ROLEPLAYINPUTDATATYPES.getEnumForName(rpVar3.getDataSet1()),1);
			setField(ROLEPLAYINPUTDATATYPES.getEnumForName(rpVar3.getDataSet2()),2);
			setField(ROLEPLAYINPUTDATATYPES.getEnumForName(rpVar3.getDataSet3()),3);
			setField(ROLEPLAYINPUTDATATYPES.getEnumForName(rpVar3.getDataSet4()),4);
			setField(ROLEPLAYINPUTDATATYPES.getEnumForName(rpVar3.getDataSet5()),5);

			vBoxDataInput.getChildren().add(gvDataInput);

			bInit = true;
		}
	}
}
