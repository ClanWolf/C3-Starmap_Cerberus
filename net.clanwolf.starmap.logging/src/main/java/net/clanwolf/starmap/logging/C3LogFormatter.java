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
package net.clanwolf.starmap.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class C3LogFormatter extends Formatter {

	private final SimpleDateFormat dt1 = new SimpleDateFormat("[yyMMdd HH:mm:ss]");

	private String cutLen(String s, int maxLength) {
		if (s.length() > maxLength) {
			s = s.replace("…", "");
			s = "…" + s.substring(s.length() - maxLength + 1);
		} else {
			s = " ".repeat(Math.max(0, (maxLength - s.length()))) + s;
		}
		return s;
	}

	@Override
	public String format(LogRecord record) {
		return dt1.format(new Date(record.getMillis()))
			+ " "
			+ cutLen(record.getLevel().getName(), 7)
			+ " "
			+ cutLen(record.getSourceClassName().replace("net.clanwolf.starmap.", "…"), 25)
			+ " / "
			+ cutLen(record.getSourceMethodName(), 20)
			+ " > "
			+ record.getMessage()
			+ "\n";
	}
}
