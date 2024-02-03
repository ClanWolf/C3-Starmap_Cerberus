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
package net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes;

import net.clanwolf.starmap.transfer.enums.DATATYPES;
import net.clanwolf.starmap.transfer.enums.catalogObjects.ICatalogObject;
import net.clanwolf.starmap.transfer.util.CatalogLoader;

//	System.out.println(Internationalization.getString(CHARACTER.CHARNAME.labelkey));

public enum ROLEPLAYINPUTDATATYPES {
	// CHARACTER
	CHARNAME("charname", DATATYPES.String, ROLEPLAYOBJECTTYPES.CHARACTER, null, true),
	LASTNAME("lastname", DATATYPES.String, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	HERITAGE("heritage", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, null, true),
	BLOODHOUSE_SINGLE("bloodhouse", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, "CHAR_Bloodhouse", false),
	AGE("age", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, true),
	GENDER("gender", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, null, true),
	CHARIMAGE("charimage", DATATYPES.String, ROLEPLAYOBJECTTYPES.CHARACTER, null, true),
	PHENOTYPE("phenotype", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	HAIRCOLOR("haircolor", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	EYECOLOR("eyecolor", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	CHARBODYSIZE("charsize", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	CHARBODYWEIGHT("charweight", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	CHARBODYTYPE("charbodytype", DATATYPES.SelectionSingle, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	STR("charstrength", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	BOD("charbody", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	DEX("chardexterity", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	RFL("charreflexes", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	INT("charintelligence", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	WIL("charwill", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	CHA("charcharisma", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),
	EDG("charedge", DATATYPES.Number, ROLEPLAYOBJECTTYPES.CHARACTER, null, false),

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

}
