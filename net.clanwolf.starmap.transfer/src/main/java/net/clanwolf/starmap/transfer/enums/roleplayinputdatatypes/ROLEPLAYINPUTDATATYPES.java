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
package net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes;

import net.clanwolf.starmap.transfer.enums.DATATYPES;
import net.clanwolf.starmap.transfer.enums.catalogObjects.ICatalogObject;
import net.clanwolf.starmap.transfer.util.CatalogLoader;

//	System.out.println(Internationalization.getString(CHARACTER.CHARNAME.labelkey));





/*
	Wenn ein neuer Eintrag gebraucht wird:
	1. json anlegen (resources/catalogs in transfer)
	2. Werte eintragen
	3. Klasse anlegen in catalogObjects (gleicher Dateiname wie der JSON Katalog!)
	4. Eintrag anlegen in ROLEPLAYINPUTDATATYPES (unten)
 */




public enum ROLEPLAYINPUTDATATYPES {
	// CHARACTER
	CHARNAME("char_name", DATATYPES.String, ROLEPLAYOBJECTTYPES.CHARACTER, null, true),
	LASTNAME("char_lastname", DATATYPES.String, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	HERITAGE("char_heritage", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, "CHAR_Heritage", true),
	BLOODHOUSE_SINGLE("char_bloodhouse", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, "CHAR_Bloodhouse", false),
	AGE("char_age", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, true),
	GENDER("char_gender", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, null, true),
	CHARIMAGE("char_image", DATATYPES.String, ROLEPLAYOBJECTTYPES.CHARACTER, null, true),
	PHENOTYPE("char_phenotype", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, "CHAR_Phenotype", false),
	HAIRCOLOR("char_haircolor", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	EYECOLOR("char_eyecolor", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	CHARBODYSIZE("char_bodysize", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	CHARBODYWEIGHT("char_weight", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	CHARBODYTYPE("char_bodytype", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	STR("char_strength", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	BOD("char_body", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	DEX("char_dexterity", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	RFL("char_reflexes", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	INT("char_intelligence", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	WIL("char_will", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	CHA("char_charisma", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	EDG("char_edge", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	BOOLEANTEST("bool_test", DATATYPES.Boolean, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),

	// ------------------------------------------------------------

	// DROPSHIP
	SHIPNAME("shipname", DATATYPES.String, ROLEPLAYOBJECTTYPES.DROPSHIP, null, false),
	SIZE("size", DATATYPES.String, ROLEPLAYOBJECTTYPES.DROPSHIP, null, false);

	public final String labelkey;
	public final DATATYPES datatype;
	public final ROLEPLAYOBJECTTYPES types;
	public final String classname;
	public final boolean mandatory;

	ROLEPLAYINPUTDATATYPES(String labelkey, DATATYPES datatype, ROLEPLAYOBJECTTYPES types, String classname, boolean mandatory) {
		this.labelkey = labelkey;
		this.datatype = datatype;
		this.types = types;
		this.classname = classname;
		this.mandatory = mandatory;
	}

	public ICatalogObject[] getList() throws Exception {
		if (classname != null) {
			return CatalogLoader.getList(classname);
		}
		return null;
	}

	@Override
	public String toString() {
		return types + "_" + labelkey;
	}

	public static ROLEPLAYINPUTDATATYPES getEnumForName(String name) {
		for (ROLEPLAYINPUTDATATYPES t : ROLEPLAYINPUTDATATYPES.values()) {
			if (t.name().equals(name)) {
				return t;
			}
		}
		return null;
	}
}
