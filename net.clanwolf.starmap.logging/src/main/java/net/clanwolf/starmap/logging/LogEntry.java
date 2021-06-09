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
package net.clanwolf.starmap.logging;

public class LogEntry {
    private final Integer lineNumber;
    private final String level;
    private final String timestamp;
    private final String loggingClass;
    private final String loggingClassMethod;
    private final String message;

    public LogEntry(Integer lineNumber, String level, String timestamp, String loggingClass, String loggingClassMethod, String message) {
        this.lineNumber = lineNumber;
        this.timestamp = timestamp;
        this.level = level;
        this.loggingClass = loggingClass;
        this.loggingClassMethod = loggingClassMethod;
        this.message = message;
    }

	@SuppressWarnings("unused")
    public Integer getLineNumber() {
        return lineNumber;
    }

	@SuppressWarnings("unused")
    public String getTimestamp() { return timestamp; }

	@SuppressWarnings("unused")
    public String getLevel() {
        return level;
    }

	@SuppressWarnings("unused")
    public String getLoggingClass() {
        return loggingClass;
    }

	@SuppressWarnings("unused")
    public String getLoggingClassMethod() {
        return loggingClassMethod;
    }

	@SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }
}
