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
package net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.clanwolf.starmap.transfer.enums.catalogObjects.CHAR_Bloodhouse;
import net.clanwolf.starmap.transfer.enums.DATATYPES;
import net.clanwolf.starmap.transfer.enums.catalogObjects.ICatalogObject;
import net.clanwolf.starmap.transfer.util.CatalogLoader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

//	System.out.println(Internationalization.getString(CHARACTER.CHARNAME.labelkey));

public enum ROLEPLAYINPUTDATATYPES {
	// CHARACTER
	CHARNAME("charname", DATATYPES.String, ROLEPLAYOBJECTTYPES.CHARACTER, null),
	LASTNAME("lastname", DATATYPES.String, ROLEPLAYOBJECTTYPES.CHARACTER, null),
	BIRTHDATE("birthdate", DATATYPES.Date, ROLEPLAYOBJECTTYPES.CHARACTER, null),
	BLOODHOUSE_SINGLE("bloodhouse", DATATYPES.Class, ROLEPLAYOBJECTTYPES.CHARACTER, "CHAR_Bloodhouse"),
	BLOODHOUSE_MULTI("bloodhouse", DATATYPES.Class, ROLEPLAYOBJECTTYPES.CHARACTER, "CHAR_Bloodhouse"),

	// DROPSHIP
	SHIPNAME("shipname", DATATYPES.String, ROLEPLAYOBJECTTYPES.DROPSHIP, null),
	SIZE("size", DATATYPES.String, ROLEPLAYOBJECTTYPES.DROPSHIP, null);

	public final String labelkey;
	public final DATATYPES datatype;
	public final ROLEPLAYOBJECTTYPES types;
	public final String classname;

	ROLEPLAYINPUTDATATYPES(String labelkey, DATATYPES datatype, ROLEPLAYOBJECTTYPES types, String classname) {
		this.labelkey = labelkey;
		this.datatype = datatype;
		this.types = types;
		this.classname = classname;
	}

	public ICatalogObject[] getList() throws Exception {
		if (classname != null) {
			return CatalogLoader.getList(classname);
		}
		return null;
	}

	@Override
	public String toString() {
		return "app_rp_storyeditor_roleplayinputdatatypes_" + ROLEPLAYOBJECTTYPES.CHARACTER + "_" + labelkey;
	}

}
