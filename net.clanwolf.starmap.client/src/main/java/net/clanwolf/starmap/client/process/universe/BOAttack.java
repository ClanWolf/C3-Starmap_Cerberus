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
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.util.Compressor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BOAttack {

	private AttackDTO attackDTO;

	@SuppressWarnings("unused")
	public BOAttack(AttackDTO attackDTO) {
		this.attackDTO = attackDTO;
	}

	@SuppressWarnings("unused")
	public void setAttackDTO(AttackDTO a) {
		this.attackDTO = a;
	}

	@SuppressWarnings("unused")
	public void storeAttack() {
		GameState saveAttackState = new GameState();
		saveAttackState.setMode(GAMESTATEMODES.ATTACK_SAVE);
		byte[] compressedAttackDTO = Compressor.compress(attackDTO);
		C3Logger.info("Compressed AttackDTO size: " + compressedAttackDTO.length);
		saveAttackState.addObject(compressedAttackDTO);
		Nexus.fireNetworkEvent(saveAttackState);
	}

	@SuppressWarnings("unused")
	public void storeAttackCharacters(AttackCharacterDTO attackCharacterDTO) {
		GameState saveAttackCharacterState = new GameState();
		saveAttackCharacterState.setMode(GAMESTATEMODES.ATTACK_CHARACTER_SAVE);
		byte[] compressedAttackCharacterDTO = Compressor.compress(attackCharacterDTO);
		C3Logger.info("Compressed AttackCharacterDTO size: " + compressedAttackCharacterDTO.length);
		saveAttackCharacterState.addObject(compressedAttackCharacterDTO);
		Nexus.fireNetworkEvent(saveAttackCharacterState);
	}

	@SuppressWarnings("unused")
	public static boolean charHasAnActiveAttack() {
		boolean charHasAnotherActiveAttack = false;
		for (BOAttack a : Nexus.getBoUniverse().attackBOs) {
			for (AttackCharacterDTO ac : a.getAttackCharList()) {
				if (ac.getCharacterID().equals(Nexus.getCurrentUser().getCurrentCharacter().getId())) {
					charHasAnotherActiveAttack = true;
				}
			}
		}
		return charHasAnotherActiveAttack;
	}

	@SuppressWarnings("unused")
	public AttackDTO getAttackDTO() {
		return attackDTO;
	}

	@SuppressWarnings("unused")
	public Integer getSeason() { return attackDTO.getSeason(); }

	@SuppressWarnings("unused")
	public Integer getRound() { return attackDTO.getRound(); }

	@SuppressWarnings("unused")
	public Long getStarSystemId() { return attackDTO.getStarSystemID(); }

	@SuppressWarnings("unused")
	public Long getAttackedFromStarSystem() { return attackDTO.getAttackedFromStarSystemID(); }

	@SuppressWarnings("unused")
	public Long getJumpshipId() { return attackDTO.getJumpshipID(); }

	@SuppressWarnings("unused")
	public Integer getAttackerFactionId() {
		BOJumpship j = Nexus.getBoUniverse().getJumpshipByID(attackDTO.getJumpshipID());
		Long id = j.getJumpshipFaction();
		return id.intValue();
	}

	@SuppressWarnings("unused")
	public Integer getDefenderFactionId() { return attackDTO.getFactionID_Defender().intValue(); }

	@SuppressWarnings("unused")
	public Integer getCharacterId() {
		if (attackDTO.getCharacterID() != null) {
			return attackDTO.getCharacterID().intValue();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unused")
	public Integer getStoryId() {
		if (attackDTO.getStoryID() != null) {
			return attackDTO.getStoryID().intValue();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unused")
	public List<AttackCharacterDTO> getAttackCharList() {
		return attackDTO.getAttackCharList();
	}
}
