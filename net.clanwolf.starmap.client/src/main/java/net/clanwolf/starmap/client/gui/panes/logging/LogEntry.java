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
package net.clanwolf.starmap.client.gui.panes.logging;

public class LogEntry {
    private Integer lineNumber;
    private String level;
    private String loggingClass;
    private String loggingClassMethod;
    private String message;

    public LogEntry(Integer lineNumber, String level, String loggingClass, String loggingClassMethod, String message) {
        this.lineNumber = lineNumber;
        this.level = level;
        this.loggingClass = loggingClass;
        this.loggingClassMethod = loggingClassMethod;
        this.message = message;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }
    public String getLevel() {
        return level;
    }
    public String getLoggingClass() {
        return loggingClass;
    }
    public String getLoggingClassMethod() {
        return loggingClassMethod;
    }
    public String getMessage() {
        return message;
    }
}
