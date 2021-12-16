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
package net.clanwolf.starmap.client.gui.panes.chat;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ChatEntry {
	private final StringProperty color;
    private final StringProperty chatTime;
    private final StringProperty chatUser;
    private final StringProperty chatText;

    public ChatEntry(String color, String chatTime, String chatUser, String chatText) {
    	this.color = new SimpleStringProperty(color);
        this.chatTime = new SimpleStringProperty(chatTime);
        this.chatUser = new SimpleStringProperty(chatUser);
        this.chatText = new SimpleStringProperty(chatText);
    }

	@SuppressWarnings("unused")
	public StringProperty getColor() { return this.color; }

	@SuppressWarnings("unused")
    public StringProperty getChatTime() {
        return this.chatTime;
    }

	@SuppressWarnings("unused")
    public StringProperty getChatUser() {
        return this.chatUser;
    }

	@SuppressWarnings("unused")
    public StringProperty getChatText() {
        return this.chatText;
    }
}
