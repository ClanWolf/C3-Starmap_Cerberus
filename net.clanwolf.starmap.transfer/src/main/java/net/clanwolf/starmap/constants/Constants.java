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
package net.clanwolf.starmap.constants;

import java.io.Serial;

public class Constants implements java.io.Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	public static final double REGULAR_SYSTEM_GENERAL_INCOME = 250;
	public static final double INDUSTRIAL_SYSTEM_GENERAL_INCOME = 1_500;
	public static final double CAPITAL_SYSTEM_GENERAL_INCOME = 5_000;

	public static final double REGULAR_SYSTEM_GENERAL_COST = 150;
	public static final double INDUSTRIAL_SYSTEM_GENERAL_COST = 1_000;
	public static final double CAPITAL_SYSTEM_GENERAL_COST = 2_000;

	public static final double REGULAR_SYSTEM_DEFEND_COST = 120;
	public static final double INDUSTRIAL_SYSTEM_DEFEND_COST = 300;
	public static final double CAPITAL_SYSTEM_DEFEND_COST = 500;

	public static final double REGULAR_SYSTEM_ATTACK_COST = 3_000;
	public static final double INDUSTRIAL_SYSTEM_ATTACK_COST = 6_000;
	public static final double CAPITAL_SYSTEM_ATTACK_COST = 10_000;
}