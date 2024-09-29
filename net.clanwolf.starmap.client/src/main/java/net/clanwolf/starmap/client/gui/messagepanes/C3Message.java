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
 * Copyright (c) 2001-2024, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.messagepanes;

import net.clanwolf.starmap.client.enums.C3MESSAGERESULTS;
import net.clanwolf.starmap.client.enums.C3MESSAGES;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;

/**
 * @author Christian
 *
 */
public class C3Message {
	private String textKey = "";
	private String text = "";
	private C3MESSAGETYPES type;
	private C3MESSAGES message;
	private C3MESSAGERESULTS result;

	public C3Message(C3MESSAGES m) {
		this.message = m;
		this.textKey = m.getTextKey();
		this.type = m.getType();
		this.text = m.getText();
	}

	public C3Message(C3MESSAGES m, String additionalText) {
		this.message = m;
		this.textKey = m.getTextKey();
		this.type = m.getType();
		this.text = m.getText() + " [" + additionalText + "]";
	}

	// hg

	/**
	 * @return the textKey
	 */
	public String getTextKey() {
		return textKey;
	}

	/**
	 * @param textKey
	 *            the textKey to set
	 */
	public void setTextKey(String textKey) {
		this.textKey = textKey;
	}

	/**
	 * @return the message
	 */
	public C3MESSAGES getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the type to set
	 */
	public void setType(C3MESSAGES message) {
		this.message = message;
	}

	/**
	 * @return the type
	 */
	public C3MESSAGETYPES getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(C3MESSAGETYPES type) {
		this.type = type;
	}

	/**
	 * @return the result
	 */
	public C3MESSAGERESULTS getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(C3MESSAGERESULTS result) {
		this.result = result;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
