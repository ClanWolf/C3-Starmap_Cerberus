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
package net.clanwolf.starmap.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class C3LogFormatter extends Formatter {

	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("[yyMMdd HH:mm:ss]");

	private String cutLen(String s, int maxLength, boolean left) {
		if (s.length() > maxLength) {
			s = s.replace("...", "");
			s = "..." + s.substring(s.length() - maxLength + 3);
		} else {
			if (left) {
				s = s + " ".repeat(Math.max(0, (maxLength - s.length())));
			} else {
				s = " ".repeat(Math.max(0, (maxLength - s.length()))) + s;
			}
		}
		return s;
	}

	@Override
	public String format(LogRecord record) {
		StringBuilder message = new StringBuilder();

		message
				.append(simpleDateFormat.format(new Date(record.getMillis())))
				.append(" ")
				.append(cutLen(record.getLevel().getName(), 7, false))
				.append(" ")
				.append(cutLen(record.getSourceClassName().replace("net.clanwolf.starmap.", "..."), 40, false))
				.append(" / ")
				.append(cutLen(record.getSourceMethodName(), 35, true))
				.append(" > ")
				.append(record.getMessage())
				.append(System.lineSeparator());

		if (record.getThrown() != null) {
			if (!message.isEmpty()) {
				message.append(" - ");
			}
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			record.getThrown().printStackTrace(printWriter);
			message.append(System.lineSeparator()).append(stringWriter.toString());
		}
		return message.toString();
	}
}
