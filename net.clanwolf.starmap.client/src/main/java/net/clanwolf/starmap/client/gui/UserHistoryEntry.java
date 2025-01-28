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
package net.clanwolf.starmap.client.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserHistoryEntry {
	private final StringProperty user;
    private final StringProperty faction;
    private final StringProperty version;
    private final StringProperty time;
	private final StringProperty inFightForPlanet;

    public UserHistoryEntry(String user, String faction, String version, String time, String inFightForPlanet) {
    	this.user = new SimpleStringProperty(user);
        this.faction = new SimpleStringProperty(faction);
        this.version = new SimpleStringProperty(version);
        this.time = new SimpleStringProperty(time);
	    this.inFightForPlanet = new SimpleStringProperty(inFightForPlanet);
    }

	@SuppressWarnings("unused")
	public StringProperty getUser() { return this.user; }

	@SuppressWarnings("unused")
    public StringProperty getFaction() {
        return this.faction;
    }

	@SuppressWarnings("unused")
    public StringProperty getVersion() {
        return this.version;
    }

	@SuppressWarnings("unused")
    public StringProperty getTime() {
        return this.time;
    }

	@SuppressWarnings("unused")
	public StringProperty getInFightForPlanet() {
		if (this.inFightForPlanet == null) {
			this.inFightForPlanet.toString();
		}
		return this.inFightForPlanet;
	}
}
