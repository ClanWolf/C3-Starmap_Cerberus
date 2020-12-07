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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Undertaker
 */
public class RolePlayKeypadPaneController extends AbstractC3RolePlayController implements ActionCallBackListener {

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private ImageView rpIBackgroundImage;

	@FXML
	private TextArea taStoryText;

	@FXML
	private ImageView ivDigit1, ivDigit2, ivDigit3, ivDigit4, ivDigit5, ivDigit6, ivDigit7, ivDigit8;

	@FXML
	private Button btKey0,btKey1,btKey2,btKey3,btKey4,btKey5,btKey6,btKey7,btKey8,btKey9,btReset;

	@FXML
	private Button btPreview;

	private String sDisplay;

	public RolePlayKeypadPaneController() {
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
		/*taRpText.setStyle("-fx-opacity: 1");
		taRpText.setEditable(false);

		btChoice1.setVisible(false);
		btChoice2.setVisible(false);
		btChoice3.setVisible(false);
		btChoice4.setVisible(false);*/

		//sDisplay = "";
		setDigit("-1");
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
			if(ROLEPLAYENTRYTYPES.C3_RP_STEP_V2 == o.getObject() ||
					ROLEPLAYENTRYTYPES.C3_RP_STEP_V5 == o.getObject()) {
				C3Logger.debug("RolePlayChoicePaneController -> START_ROLEPLAY");

				init();

				// set current step of story
				getStoryValues(Nexus.getCurrentChar());
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
	private void handlePressKey0(){
		setDigit("0");
	}

	@FXML
	private void handlePressKey1(){
		setDigit("1");
	}

	@FXML
	private void handlePressKey2(){
		setDigit("2");
	}

	@FXML
	private void handlePressKey3(){
		setDigit("3");
	}

	@FXML
	private void handlePressKey4(){
		setDigit("4");
	}

	@FXML
	private void handlePressKey5(){
		setDigit("5");
	}

	@FXML
	private void handlePressKey6(){
		setDigit("6");
	}

	@FXML
	private void handlePressKey7(){
		setDigit("7");
	}

	@FXML
	private void handlePressKey8(){
		setDigit("8");
	}

	@FXML
	private void handlePressKey9(){
		setDigit("9");
	}

	@FXML
	private void handlebtReset(){
		setDigit("-1");
	}

	/*@FXML
	private void handleOnActionbtChoice1(){
		Long rp = Nexus.getCurrentChar().getStory().getVar2ID().getOption1StoryID();
		saveNextStep(rp);

	}


	/******************************** THIS ********************************/

	private Image loadDigit( String digit){
		String imagePath;

		switch (digit){
			case "0":
				imagePath = "/images/digi/Digit0.png";
				break;

			case "1":
				imagePath = "/images/digi/Digit1.png";
				break;

			case "2":
				imagePath = "/images/digi/Digit2.png";
				break;

			case "3":
				imagePath = "/images/digi/Digit3.png";
				break;

			case "4":
				imagePath = "/images/digi/Digit4.png";
				break;

			case "5":
				imagePath = "/images/digi/Digit5.png";
				break;

			case "6":
				imagePath = "/images/digi/Digit6.png";
				break;

			case "7":
				imagePath = "/images/digi/Digit7.png";
				break;

			case "8":
				imagePath = "/images/digi/Digit8.png";
				break;

			case "9":
				imagePath = "/images/digi/Digit9.png";
				break;

			default:
				imagePath = "/images/digi/Digit_empty.png";
				break;
		}

		return new Image(this.getClass().getResourceAsStream(imagePath));
	}
	private void setDigit(String digit){

		//sDisplay = "01234567";
		if(sDisplay != null && sDisplay.length() >= 0 && sDisplay.length() < 8){
			sDisplay = sDisplay + digit;
		}

		if(digit.equals("-1")){
			sDisplay = "";
		}

		//C3Logger.info(sDisplay.substring(1,2));

		switch (sDisplay.length()){
			case 1:
				ivDigit1.setImage(loadDigit(sDisplay.substring(0,1)));
				break;
			case 2:
				ivDigit1.setImage(loadDigit(sDisplay.substring(1,2)));
				ivDigit2.setImage(loadDigit(sDisplay.substring(0,1)));
				break;
			case 3:
				ivDigit1.setImage(loadDigit(sDisplay.substring(2,3)));
				ivDigit2.setImage(loadDigit(sDisplay.substring(1,2)));
				ivDigit3.setImage(loadDigit(sDisplay.substring(0,1)));
				break;
			case 4:
				ivDigit1.setImage(loadDigit(sDisplay.substring(3,4)));
				ivDigit2.setImage(loadDigit(sDisplay.substring(2,3)));
				ivDigit3.setImage(loadDigit(sDisplay.substring(1,2)));
				ivDigit4.setImage(loadDigit(sDisplay.substring(0,1)));
				break;
			case 5:
				ivDigit1.setImage(loadDigit(sDisplay.substring(4,5)));
				ivDigit2.setImage(loadDigit(sDisplay.substring(3,4)));
				ivDigit3.setImage(loadDigit(sDisplay.substring(2,3)));
				ivDigit4.setImage(loadDigit(sDisplay.substring(1,2)));
				ivDigit5.setImage(loadDigit(sDisplay.substring(0,1)));
				break;
			case 6:
				ivDigit1.setImage(loadDigit(sDisplay.substring(5,6)));
				ivDigit2.setImage(loadDigit(sDisplay.substring(4,5)));
				ivDigit3.setImage(loadDigit(sDisplay.substring(3,4)));
				ivDigit4.setImage(loadDigit(sDisplay.substring(2,3)));
				ivDigit5.setImage(loadDigit(sDisplay.substring(1,2)));
				ivDigit6.setImage(loadDigit(sDisplay.substring(0,1)));
				break;
			case 7:
				ivDigit1.setImage(loadDigit(sDisplay.substring(6,7)));
				ivDigit2.setImage(loadDigit(sDisplay.substring(5,6)));
				ivDigit3.setImage(loadDigit(sDisplay.substring(4,5)));
				ivDigit4.setImage(loadDigit(sDisplay.substring(3,4)));
				ivDigit5.setImage(loadDigit(sDisplay.substring(2,3)));
				ivDigit6.setImage(loadDigit(sDisplay.substring(1,2)));
				ivDigit7.setImage(loadDigit(sDisplay.substring(0,1)));
				break;
			case 8:
				ivDigit1.setImage(loadDigit(sDisplay.substring(7,8)));
				ivDigit2.setImage(loadDigit(sDisplay.substring(6,7)));
				ivDigit3.setImage(loadDigit(sDisplay.substring(5,6)));
				ivDigit4.setImage(loadDigit(sDisplay.substring(4,5)));
				ivDigit5.setImage(loadDigit(sDisplay.substring(3,4)));
				ivDigit6.setImage(loadDigit(sDisplay.substring(2,3)));
				ivDigit7.setImage(loadDigit(sDisplay.substring(1,2)));
				ivDigit8.setImage(loadDigit(sDisplay.substring(0,1)));
				break;
			default:
				ivDigit1.setImage(loadDigit("-1"));
				ivDigit2.setImage(loadDigit("-1"));
				ivDigit3.setImage(loadDigit("-1"));
				ivDigit4.setImage(loadDigit("-1"));
				ivDigit5.setImage(loadDigit("-1"));
				ivDigit6.setImage(loadDigit("-1"));
				ivDigit7.setImage(loadDigit("-1"));
				ivDigit8.setImage(loadDigit("-1"));
				break;
		}

		//ivDigit1.setImage(loadDigit(digit));
	}

	@Override
	public void getStoryValues(RolePlayCharacterDTO rpChar) {
		/*if (rpChar.getStory().getStoryIntro() == null) {
			String imURL;
			InputStream isBackground = this.getClass().getResourceAsStream("/images/gui/default_Step.png");
			rpIBackgroundImage.setImage(new Image(isBackground));

			// Check step for own image. If now own image availabale use default image
			if (rpChar.getStory().getStoryImage() != null) {
				imURL = BORolePlayStory.getRPG_ResourceURL() + "/" + rpChar.getStory().getId().toString() + "/" + rpChar.getStory().getStoryImage();
				Image im = new Image(imURL);
				rpImage.setImage(im);
			}
		}*/
	}
}
