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
 * Copyright (c) 2001-2021, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.transfer.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.clanwolf.starmap.transfer.enums.catalogObjects.CHAR_Bloodhouse;
import net.clanwolf.starmap.transfer.enums.catalogObjects.ICatalogObject;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CatalogLoader {

	private static CatalogLoader instance = null;

	public static CatalogLoader getInstance() {
		if (instance == null) {
			instance = new CatalogLoader();
		}
		return instance;
	}

	public static ICatalogObject[] getList(String classname) throws Exception {
		URI uri = ((CatalogLoader) getInstance()).getClass().getResource("/catalogs/" + classname + ".json").toURI();
		byte[] data = Files.readAllBytes(Paths.get(uri));

		switch (classname) {
			case "CHAR_Bloodhouse":
				ObjectMapper objectMapper = new ObjectMapper();
				ICatalogObject[] l = objectMapper.readValue(data, CHAR_Bloodhouse[].class);
				return l;
		}
		return null;
	}
}
