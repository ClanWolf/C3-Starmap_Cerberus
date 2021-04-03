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
package net.clanwolf.starmap.client.process.universe;

import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.AttackDTO;
import net.clanwolf.starmap.transfer.dtos.RoutePointDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

public class BOAttack {

	private AttackDTO attackDTO;

	public BOAttack(AttackDTO attackDTO) {
		this.attackDTO = attackDTO;
	}

	public void storeAttack() {
		GameState saveAttackState = new GameState();
		saveAttackState.setMode(GAMESTATEMODES.ATTACK_SAVE);
		saveAttackState.addObject(attackDTO);
		Nexus.fireNetworkEvent(saveAttackState);
	}

	public Integer getSeason() { return attackDTO.getSeason(); }

	public Integer getRound() { return attackDTO.getRound(); }

	public Long getStarSystemId() { return attackDTO.getStarSystemId(); }

	public Long getAttackedFromStarSystem() { return attackDTO.getAttackedFromStarSystem(); }

	public Long getJumpshipId() { return attackDTO.getJumpshipId(); }
}
