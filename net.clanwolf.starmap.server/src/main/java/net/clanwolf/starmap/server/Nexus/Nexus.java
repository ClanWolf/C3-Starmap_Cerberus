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
package net.clanwolf.starmap.server.Nexus;

import net.clanwolf.starmap.server.beans.C3GameSessionHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class Nexus {
	public static Long currentSeason = 1L;
	public static Long DUMMY_USERID = -1L;
	public static Long END_ROUND_USERID = -2L;
	public static volatile boolean sendUniverseToClients = false;
	public static String patternTimestamp = "yyyy-MM-dd HH:mm:ss";
	public static String mailServer = "";
	public static String mailUser = "";
	public static String mailPw = "";

	public static C3GameSessionHandler gmSessionHandler;


}
