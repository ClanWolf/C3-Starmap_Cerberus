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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes;

import net.clanwolf.starmap.transfer.enums.DATATYPES;

//	System.out.println(Internationalization.getString(CHARACTER.CHARNAME.labelkey));

public enum ROLEPLAYINPUTDATATYPES {
	// CHARACTER
	CHARNAME( "charname", DATATYPES.String, ROLEPLAYOBJECTTYPES.CHARACTER),
	LASTNAME( "lastname", DATATYPES.String, ROLEPLAYOBJECTTYPES.CHARACTER),
	BIRTHDATE("birthdate", DATATYPES.Date, ROLEPLAYOBJECTTYPES.CHARACTER),

	// DROPSHIP
	SHIPNAME( "shipname", DATATYPES.String, ROLEPLAYOBJECTTYPES.DROPSHIP),
	SIZE( "size", DATATYPES.String, ROLEPLAYOBJECTTYPES.DROPSHIP);

	public final String labelkey;
	public final DATATYPES datatype;
	public final ROLEPLAYOBJECTTYPES types;

	ROLEPLAYINPUTDATATYPES(String labelkey, DATATYPES datatype, ROLEPLAYOBJECTTYPES types) {
		this.labelkey = labelkey;
		this.datatype = datatype;
		this.types = types;
	}

	@Override
	public String toString() {
		return "app_rp_storyeditor_roleplayinputdatatypes_" + ROLEPLAYOBJECTTYPES.CHARACTER + "_" + labelkey;
	}

}
