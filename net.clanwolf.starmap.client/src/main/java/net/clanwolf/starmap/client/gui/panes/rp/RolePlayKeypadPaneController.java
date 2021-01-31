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

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.gui.panes.AbstractC3RolePlayController;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

/**
 * @author Undertaker
 */
public class RolePlayKeypadPaneController extends AbstractC3RolePlayController implements ActionCallBackListener {

	@FXML
	private AnchorPane anchorPane;

	//@FXML
	//private ImageView rpIBackgroundImage;

	@FXML
	private TextArea taStoryText;

	@FXML
	private ImageView ivDigit1, ivDigit2, ivDigit3, ivDigit4, ivDigit5, ivDigit6, ivDigit7, ivDigit8;

	@FXML
	private Button btKey0,btKey1,btKey2,btKey3,btKey4,btKey5,btKey6,btKey7,btKey8,btKey9,btReset, btEnterKey;

	@FXML
	private Button btPreview;

	private String sDisplay;

	private int maxDigits;
	private int attempts;
	private int currentAttempt;
	private boolean noAttemptsFree;
	private boolean needReset;
	private String sSecretCode;
	private boolean codeOK;

	public RolePlayKeypadPaneController() {
	}

	@Override
	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.START_ROLEPLAY, this);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		//init();

	}

	private void init(){
		currentAttempt = 0;
		codeOK = false;
		btPreview.setVisible(false);
		btEnterKey.setVisible(true);
		btReset.setVisible(true);

		// Reset display
		setDigit("-1", false);
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
			if(ROLEPLAYENTRYTYPES.C3_RP_STEP_V6 == o.getObject()) {
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
	private void handlePressKey0(){	setDigit("0", true); }

	@FXML
	private void handlePressKey1(){
		setDigit("1", true);
	}

	@FXML
	private void handlePressKey2(){
		setDigit("2", true);
	}

	@FXML
	private void handlePressKey3(){
		setDigit("3", true);
	}

	@FXML
	private void handlePressKey4(){
		setDigit("4", true);
	}

	@FXML
	private void handlePressKey5(){
		setDigit("5", true);
	}

	@FXML
	private void handlePressKey6(){
		setDigit("6", true);
	}

	@FXML
	private void handlePressKey7(){
		setDigit("7", true);
	}

	@FXML
	private void handlePressKey8(){
		setDigit("8", true);
	}

	@FXML
	private void handlePressKey9(){
		setDigit("9", true);
	}

	@FXML
	private void handlebtReset(){
		needReset = false;
		setDigit("-1", true);
	}

	@FXML
	private void handlePressEnterKey(){

		if( attempts > 0) {
			if(!codeOK && currentAttempt <= attempts) {
				codeOK = checkSecretCode();
				currentAttempt++;
			}

		} else if(!codeOK){
			codeOK = checkSecretCode();
		}

		if (codeOK || ( currentAttempt == attempts && attempts > 0)) {
			btEnterKey.setVisible(false);
			btReset.setVisible(false);
			btPreview.setVisible(true);
			noAttemptsFree = true;
		}
	}

	@FXML
	private void handleOnActionbtPreview(){

		Long rp = null;

		if(!codeOK && Nexus.getCurrentChar().getStory().getVar6ID().getStoryIDFailure() != null){
			rp = Nexus.getCurrentChar().getStory().getVar6ID().getStoryIDFailure();
		} else {
			rp = Nexus.getCurrentChar().getStory().getVar6ID().getStoryIDSuccess();
		}

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

			case "g":
				imagePath = "/images/digi/Digit green.png";
				break;

			case "r":
				imagePath = "/images/digi/Digit red.png";
				break;

			case "o":
				imagePath = "/images/digi/Digit orange.png";
				break;

			default:
				imagePath = "/images/digi/Digit_empty.png";
				break;
		}

		return new Image(this.getClass().getResourceAsStream(imagePath));
	}

	private void setDigit(String digit, boolean playSound) {
		if (!noAttemptsFree && !needReset) {
			// Play sound
			if (playSound) C3SoundPlayer.play("sound/fx/beep_02.wav", false);

			if (sDisplay != null && sDisplay.length() >= 0 && sDisplay.length() < maxDigits) {
				sDisplay = sDisplay + digit;
			}

			if (digit.equals("-1")) {
				sDisplay = "";
			}

			addDigitToDisplay(sDisplay);
		}
	}

	private void addDigitToDisplay(String code){

		switch (code.length()){
			case 1:
				ivDigit1.setImage(loadDigit(code.substring(0,1)));
				break;
			case 2:
				ivDigit1.setImage(loadDigit(code.substring(1,2)));
				ivDigit2.setImage(loadDigit(code.substring(0,1)));
				break;
			case 3:
				ivDigit1.setImage(loadDigit(code.substring(2,3)));
				ivDigit2.setImage(loadDigit(code.substring(1,2)));
				ivDigit3.setImage(loadDigit(code.substring(0,1)));
				break;
			case 4:
				ivDigit1.setImage(loadDigit(code.substring(3,4)));
				ivDigit2.setImage(loadDigit(code.substring(2,3)));
				ivDigit3.setImage(loadDigit(code.substring(1,2)));
				ivDigit4.setImage(loadDigit(code.substring(0,1)));
				break;
			case 5:
				ivDigit1.setImage(loadDigit(code.substring(4,5)));
				ivDigit2.setImage(loadDigit(code.substring(3,4)));
				ivDigit3.setImage(loadDigit(code.substring(2,3)));
				ivDigit4.setImage(loadDigit(code.substring(1,2)));
				ivDigit5.setImage(loadDigit(code.substring(0,1)));
				break;
			case 6:
				ivDigit1.setImage(loadDigit(code.substring(5,6)));
				ivDigit2.setImage(loadDigit(code.substring(4,5)));
				ivDigit3.setImage(loadDigit(code.substring(3,4)));
				ivDigit4.setImage(loadDigit(code.substring(2,3)));
				ivDigit5.setImage(loadDigit(code.substring(1,2)));
				ivDigit6.setImage(loadDigit(code.substring(0,1)));
				break;
			case 7:
				ivDigit1.setImage(loadDigit(code.substring(6,7)));
				ivDigit2.setImage(loadDigit(code.substring(5,6)));
				ivDigit3.setImage(loadDigit(code.substring(4,5)));
				ivDigit4.setImage(loadDigit(code.substring(3,4)));
				ivDigit5.setImage(loadDigit(code.substring(2,3)));
				ivDigit6.setImage(loadDigit(code.substring(1,2)));
				ivDigit7.setImage(loadDigit(code.substring(0,1)));
				break;
			case 8:
				ivDigit1.setImage(loadDigit(code.substring(7,8)));
				ivDigit2.setImage(loadDigit(code.substring(6,7)));
				ivDigit3.setImage(loadDigit(code.substring(5,6)));
				ivDigit4.setImage(loadDigit(code.substring(4,5)));
				ivDigit5.setImage(loadDigit(code.substring(3,4)));
				ivDigit6.setImage(loadDigit(code.substring(2,3)));
				ivDigit7.setImage(loadDigit(code.substring(1,2)));
				ivDigit8.setImage(loadDigit(code.substring(0,1)));
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
	}

	/**
	 *
	 */
	private boolean checkSecretCode(){
		String sStatusString = "";

		int digitsInTheRightPlace = 0;
		int digitsAvailable = 0;

		if(currentAttempt > 0) sStatusString = sStatusString + "------------------------------------------------------------- \n";


		if(sDisplay.equals(sSecretCode)){
			// secretCode is correkt
			taStoryText.appendText("Der eingegebene Code ist korrekt! \n");

			setResultDigit(maxDigits,0);
			return true;

		} else if (sDisplay.length() != sSecretCode.length()) {
			// secretCode has not enough digits
			taStoryText.appendText("Der eingegebene Code hat zu wenige Stellen \n");

		} else {
			// Check if the digits are in the right place
			int hlpCurrentAttemp = currentAttempt + 1;
			sStatusString = sStatusString + "Versuch Nummer " + hlpCurrentAttemp + ". Der eingegebene Code lautet: " + sDisplay + "\n";
			String sHlpDisplay="";
			String sHlpSecretCode="";
			// check digits in the right place
			for(int i = 0; i<sDisplay.length(); i++) {
				String sCheckedDigit = sDisplay.substring(i,i+ 1);

				if (sCheckedDigit.equals(sSecretCode.substring(i, i+1))) {
					digitsInTheRightPlace = digitsInTheRightPlace + 1;
				}  else {
					// Digit is not in the right place
					// note digit on the display
					sHlpDisplay = sHlpDisplay + sCheckedDigit;
					// note the digit in the secret code
					sHlpSecretCode = sHlpSecretCode + sSecretCode.substring(i, i+1);
				}
			}
			sStatusString = sStatusString + "Es sind " + digitsInTheRightPlace + " Zahlen an der richtigen Stelle!\n";

			// check if the digits are available
			for(int i = 0; i<sHlpDisplay.length(); i++) {
				String sCheckedDigit = sHlpDisplay.substring(i,i+ 1);
				boolean breakFor = false;
				for(int j=0;j<sHlpSecretCode.length();j++){
					if (sCheckedDigit.equals(sHlpSecretCode.substring(j, j+1))) {
						digitsAvailable = digitsAvailable + 1;
						// remove the found digit
						sHlpSecretCode = sHlpSecretCode.replaceFirst(sCheckedDigit,"");

						breakFor = true;
					}
					// stop for if digit was found
					if(breakFor) break;
				}
			}
			sStatusString = sStatusString + digitsAvailable + " Zahlen sind korrekt aber an der falschen Stelle! \n";
		}

		taStoryText.appendText(sStatusString);

		setResultDigit(digitsInTheRightPlace, digitsAvailable);
		needReset = true;
		return false;
	}

	/**
	display the result of the check
	 */
	private void setResultDigit(int placeOK, int placeNotOk) {
		String sResultCode = "";

		// hide all digits
		ImageView[] digitViews = new ImageView[] { ivDigit1, ivDigit2, ivDigit3, ivDigit4, ivDigit5,ivDigit6,ivDigit7,ivDigit8 };
		for (ImageView iv : digitViews) {
			iv.setOpacity(0.0f);
		}

		// build result string, digits are right
		for(int i=0;i<placeOK;i++){
			sResultCode = sResultCode + "g";
			addDigitToDisplay(sResultCode);
		}

		// build result string, digits are right but on false place
		for(int j=0;j<placeNotOk;j++){
			sResultCode = sResultCode + "o";
			addDigitToDisplay(sResultCode);
		}

		// build result string, digits are false
		for(int k = sResultCode.length();k<maxDigits;k++){
			sResultCode = "r" + sResultCode;
			addDigitToDisplay(sResultCode);
		}

		// fade digits on display
		C3SoundPlayer.play("sound/fx/PremiumBeat_0045_alarm_system_keypad.wav", false);

		SequentialTransition sequentialTransition = new SequentialTransition();
		int resultDigits = 1;
		for (ImageView iv : digitViews) {
			if(resultDigits <= sResultCode.length()) {
				FadeTransition ft = new FadeTransition(Duration.millis(200), iv);
				ft.setFromValue(0.0f);
				ft.setToValue(1.0f);
				ft.setCycleCount(1);
				sequentialTransition.getChildren().add(ft);
			}
			resultDigits++;
		}
		sequentialTransition.setCycleCount(1);
		sequentialTransition.play();
	}

	@Override
	public void getStoryValues(RolePlayCharacterDTO rpChar) {
		sSecretCode = rpChar.getStory().getVar6ID().getSecretCode();
		attempts = rpChar.getStory().getVar6ID().getAttempts();
		maxDigits = sSecretCode.length();

		// set story image
		Image im = BORolePlayStory.getRPG_Image(rpChar.getStory());
		backgroundImage.setImage(im);

		// play sound
		if (rpChar.getStory().getStoryMP3() != null) {
			C3SoundPlayer.play(BORolePlayStory.getRPG_Soundfile(rpChar.getStory()), false);
		}
	}
}
