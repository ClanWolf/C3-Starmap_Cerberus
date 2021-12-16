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
package net.clanwolf.starmap.transfer;

import net.clanwolf.starmap.transfer.dtos.UserDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.io.Serializable;

/**
 * The state of a game room is held in this object. Multiple remote client
 * connections to a GameRoom will share this state.
 *
 * @author Werner Kewenig
 */
public class GameState implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Object obj;
	private Object obj2;
	private Object obj3;
	private GAMESTATEMODES mode = GAMESTATEMODES.NO_MODE;

	// It is a unique id to detected the same GameState on Server and Client
	private Integer actionID;
	// Is true if the operation was succsessful
	private Boolean action_successfully;

	// Normaly it is a string contains the playersession (DefaultSession1 or DefaultSession2 and so on)
	private Object receiverID;

	public GameState() {
		super();
	}

	public GameState(GAMESTATEMODES mode) {
		super();
		this.mode = mode;
	}

	public GameState(GAMESTATEMODES mode, UserDTO user) {
		super();
		this.mode = mode;
		this.obj = user;
	}

	public GAMESTATEMODES getMode() {
		return mode;
	}

	public void setMode(GAMESTATEMODES mode) {
		this.mode = mode;
	}

	/**
	 * Returns the C3GameState-Mode as string
	 *
	 * @return
	 */
	public String getModeString() {

		return mode.name();

	}

	public void addObject(Object o) {
		this.obj = o;
	}

	public Object getObject() {
		return obj;
	}

	public void addObject2(Object o) {
		this.obj2 = o;
	}

	public Object getObject2() {
		return obj2;
	}

	public void addObject3(Object o) {
		this.obj3 = o;
	}

	public Object getObject3() {
		return obj3;
	}

	/**
	 * @return the receiver
	 */
	public Object getReceiver() {
		return receiverID;
	}

	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(Object receiver) {
		this.receiverID = receiver;
	}

	/**
	 * @return the actionID
	 */
	public Integer getActionID() {
		return actionID;
	}

	/**
	 * @param actionID the actionID to set
	 */
	public void setActionID(Integer actionID) {
		this.actionID = actionID;
	}

	/**
	 * @return the action_successfully
	 */
	public Boolean isAction_successfully() {
		return action_successfully;
	}

	/**
	 * @param action_successfully the action_successfully to set
	 */
	public void setAction_successfully(Boolean action_successfully) {
		this.action_successfully = action_successfully;
	}

}
