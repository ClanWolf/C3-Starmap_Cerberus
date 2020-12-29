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
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
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
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
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
	private Button btKey0,btKey1,btKey2,btKey3,btKey4,btKey5,btKey6,btKey7,btKey8,btKey9,btReset, btEnterKey;

	@FXML
	private Button btPreview;

	private String sDisplay;

	private int maxDigits;
	private int attempts;
	private int currentAttempt;
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
	private void handlebtReset(){ setDigit("-1", true);	}

	@FXML
	private void handlePressEnterKey(){

		if( attempts > 0 ) {
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

			default:
				imagePath = "/images/digi/Digit_empty.png";
				break;
		}

		return new Image(this.getClass().getResourceAsStream(imagePath));
	}

	private void setDigit(String digit, boolean playSound){
		// Play sound
		if(playSound) C3SoundPlayer.play("sound/fx/beep_02.wav", false);

		/*Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};

		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				//label.setText("Hello World");
			}
		});
		new Thread(sleeper).start();*/

		if(sDisplay != null && sDisplay.length() >= 0 && sDisplay.length() < maxDigits){
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

	/**
	 *
	 */
	private boolean checkSecretCode(){
		String sStatusString = "";

		int digitsOK = 0;
		int digitsOK2 = 0;

		if(sDisplay.equals(sSecretCode)){
			sStatusString = taStoryText.getText() + "Der eingegebene Code ist korrekt! \n";
			//TODO: set next step
			taStoryText.setText(sStatusString);
			return true;

		} else if (sDisplay.length() != sSecretCode.length()) {
			sStatusString = taStoryText.getText() + "Der eingegebene Code hat zu wenige Stellen \n";

		} else {
			String sHlpDisplay="";
			String sHlpSecretCode="";
			// digit is in the right place
			for(int i = 0; i<sDisplay.length(); i++) {
				String sCheckedDigit = sDisplay.substring(i,i+ 1);

				if (sCheckedDigit.equals(sSecretCode.substring(i, i+1))) {
					//sStatusString = sStatusString + "Stelle " + i + " ok! \n";
					digitsOK = digitsOK + 1;
				}  else {
					sHlpDisplay = sHlpDisplay + sCheckedDigit;
					sHlpSecretCode = sHlpSecretCode + sSecretCode.substring(i, i+1);
				}
			}
			sStatusString = "Es sind " + digitsOK + " Zahlen an der richtigen Stelle!";

			// digit is in the false place
			for(int i = 0; i<sHlpDisplay.length(); i++) {
				String sCheckedDigit = sHlpDisplay.substring(i,i+ 1);
				boolean breakFor = false;
				for(int j=0;j<sHlpSecretCode.length();j++){
					if (sCheckedDigit.equals(sHlpSecretCode.substring(j, j+1))) {
						digitsOK2 = digitsOK2 + 1;
						// remove found digit
						sHlpSecretCode = sHlpSecretCode.replaceFirst(sCheckedDigit,"");
						C3Logger.debug(sHlpSecretCode);
						breakFor = true;
					}
					// stop for if digit was found
					if(breakFor) break;
				}
			}
			sStatusString = sStatusString + "\n" + digitsOK2 + " Zahlen sind korrekt aber an der falschen Stelle! \n";
		}

		taStoryText.setText(sStatusString);
		return false;
	}

	@Override
	public void getStoryValues(RolePlayCharacterDTO rpChar) {
		sSecretCode = rpChar.getStory().getVar6ID().getSecretCode();
		attempts = rpChar.getStory().getVar6ID().getAttempts();
		maxDigits = sSecretCode.length();

		if (rpChar.getStory().getStoryImage() != null) {
			String imURL;
			imURL = BORolePlayStory.getRPG_ResourceURL() + "/" + rpChar.getStory().getId().toString() + "/" + rpChar.getStory().getStoryImage();
			Image im = new Image(imURL);
			rpIBackgroundImage.setImage(im);
		} else {
			InputStream isBackground = this.getClass().getResourceAsStream("/images/gui/default_Step.png");
			rpIBackgroundImage.setImage(new Image(isBackground));
		}

	}
}
