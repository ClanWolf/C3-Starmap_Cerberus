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
package net.clanwolf.starmap.transfer.enums;

/**
 * @author Undertaker
 */
public enum ROLEPLAYENTRYTYPES {
	RP_STORY("Story"),

	RP_CHAPTER("Chapter"),

	RP_SECTION("Step (Normal Story step)"),

	RP_CHOICE("Step (Path selection) without Image"),

	RP_DATA_INPUT("Step (Data input)"),

	RP_DICE("Step (Dice)"),

	RP_CHOICE_IMAGE_LEFT("Step (Path selection) Image left"),

	RP_KEYPAD("Step Keypad"),

	RP_HPG_MESSAGE("Step Message"),

	RP_PREPARE_BATTLE("Step prepare battle"),

	RP_INVASION("Step invasion drop");

	private final String label;

	ROLEPLAYENTRYTYPES(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

}
