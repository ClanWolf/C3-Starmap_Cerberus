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
 * Copyright (c) 2001-2023, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.action;

/**
 * Action Object.
 *
 * @author Meldric
 */
public class ActionObject {

	private Object mObject;
	private final C3Action mAction;
	private String mText = "";

	/**
	 * ActionObject constructor
	 *
	 * @param src C3Action
	 */
	public ActionObject(C3Action src) {
		mAction = src;
	}

	/**
	 * ActionObject constructor
	 *
	 * @param action C3Action
	 * @param o      Object
	 */
	public ActionObject(C3Action action, Object o) {
		mObject = o;
		mAction = action;
	}

	public ActionObject(C3Action action, String s) {
		mObject = null;
		mText = s;
		mAction = action;
	}

	/**
	 * Get object
	 *
	 * @return object
	 */
	public Object getObject() {
		return mObject;
	}

	/**
	 * Get source
	 *
	 * @return C3Action
	 */
	public C3Action getSource() {
		return mAction;
	}

	public String getText() { return mText; }
}
