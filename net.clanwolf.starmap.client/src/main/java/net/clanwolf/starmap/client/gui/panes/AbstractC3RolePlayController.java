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
package net.clanwolf.starmap.client.gui.panes;

import javafx.fxml.Initializable;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.process.roleplay.BORolePlayStory;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayStoryDTO;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstract JavaFX Controller
 *
 * @author Undertaker
 */
public abstract class AbstractC3RolePlayController implements Initializable, ActionCallBackListener {

	protected BORolePlayStory boRp;
	protected RolePlayStoryDTO rp;

	/**
	 * Set strings
	 */
	public abstract void setStrings();

	public abstract void addActionCallBackListeners();

	public abstract void getStoryValues(RolePlayCharacterDTO rpChar);

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
//		Nexus.getCurrentChar().setStory(boRp.getStoryByID(rp));
		boRp.saveRolePlayCharacter(Nexus.getCurrentChar(), rp);
	}
}
