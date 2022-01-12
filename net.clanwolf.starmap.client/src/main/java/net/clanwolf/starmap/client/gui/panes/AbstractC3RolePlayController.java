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
 * Copyright (c) 2001-2022, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.transfer.dtos.AttackCharacterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.transfer.dtos.AttackDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstract JavaFX Controller
 *
 * @author Undertaker
 */
public abstract class AbstractC3RolePlayController implements Initializable, ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	protected BORolePlayStory boRp;
	//protected RolePlayStoryDTO rp;
	protected boolean isCharRP = true;

	@FXML
	private ImageView templateBackground;

	@FXML
	protected ImageView backgroundImage;

	/**
	 * Set strings
	 */
	public abstract void setStrings();

	public abstract void addActionCallBackListeners();

	public abstract void getStoryValues(RolePlayStoryDTO rpStory);

	public void checkToCancelInvasion() {
		if (isAttackRP()) { // this is not a character roleplay pane --> AttackPane
			logger.info("The round has been finalized. This roleplay session needs to be canceled.");
			ActionManager.getAction(ACTIONS.SWITCH_TO_MAP).execute();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		boRp = new BORolePlayStory();
	}

	/**
	 * Handle Action
	 *
	 * @param action
	 *            Action
	 * @param o
	 *            Object
	 * @return boolean
	 */
	@Override
	public abstract boolean handleAction(ACTIONS action, ActionObject o);

	protected void saveNextStep(Long rp){
		C3SoundPlayer.stopSpeechPlayer();
		if(isCharRP){
			boRp.saveRolePlayCharacter(Nexus.getCurrentChar(), rp);
		} else {
			BOAttack bo = Nexus.getCurrentAttackOfUser();
			AttackDTO attack = bo.getAttackDTO();
			attack.setStoryID(rp);
			bo.storeAttack();
		}
	}

	public void setIsCharRP(boolean isCharRP){
		this.isCharRP = isCharRP;
	}

	public boolean isCharRP() {
		return isCharRP;
	}

	public boolean isAttackRP() {
		return !isCharRP;
	}

	protected RolePlayStoryDTO getCurrentRP(){
		if(isCharRP){
			return Nexus.getCurrentChar().getStory();
		} else {
			BOAttack bo = Nexus.getCurrentAttackOfUser();
			if (bo == null) {
				bo = Nexus.getFinishedAttackInThisRoundForUser();
			}

			if (Nexus.getBoUniverse().getAttackStoriesByID(Long.valueOf(bo.getStoryId())) == null) {
				for (BOAttack b : Nexus.getBoUniverse().attackBOsAllInThisRound.values()) {
					if (b.getAttackDTO().getAttackCharList() != null) {
						for (AttackCharacterDTO ac : b.getAttackDTO().getAttackCharList()) {
							if (ac.getCharacterID().equals(Nexus.getCurrentUser().getCurrentCharacter().getId())) {
								return Nexus.getBoUniverse().getAttackStoriesByID(b.getAttackDTO().getStoryID());
							}
						}
					}
				}
			}

			return Nexus.getBoUniverse().getAttackStoriesByID(Long.valueOf(bo.getStoryId()));
		}
	}
}
