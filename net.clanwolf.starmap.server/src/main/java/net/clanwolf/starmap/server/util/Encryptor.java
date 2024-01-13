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
package net.clanwolf.starmap.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Base64;

public class Encryptor {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static String getPasswordFromPair(String indicator, String passwordPair) {
		String pw = "";
		try {
			String[] pws = passwordPair.split("#");

			if (pws.length != 2) {
				logger.warn("Wrong pw format. Only one of two values provided! Cannot log in!");
				return pw;
			}

			String pw1 = pws[0].replaceFirst("p1:", "");
			String pw2 = pws[1].replaceFirst("p2:", "");

			byte[] decodedBytes1 = Base64.getDecoder().decode(pw1);
			pw1 = new String(decodedBytes1);
			byte[] decodedBytes2 = Base64.getDecoder().decode(pw2);
			pw2 = new String(decodedBytes2);

			if ("first".equals(indicator)) {
				pw = pw1;
			} else if ("second".equals(indicator)) {
				pw = pw2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("UserLookup failed!", e);
		}
		return pw;
	}
}
