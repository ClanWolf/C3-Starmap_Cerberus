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
package net.clanwolf.starmap.client.process.universe;

import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.util.Compressor;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class BOAttack {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
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
		logger.info("Compressed AttackDTO size: " + compressedAttackDTO.length);
		saveAttackState.addObject(compressedAttackDTO);
		saveAttackState.addObject2(getAttackType());
		Nexus.fireNetworkEvent(saveAttackState);
	}

	@SuppressWarnings("unused")
	@Deprecated
	public void storeAttackCharacters(AttackCharacterDTO attackCharacterDTO, Boolean bRemoveAttackChar) {
		GameState saveAttackCharacterState = new GameState();
		saveAttackCharacterState.setMode(GAMESTATEMODES.ATTACK_CHARACTER_SAVE);

		byte[] compressedAttackCharacterDTO = Compressor.compress(attackCharacterDTO);
		logger.info("Compressed AttackCharacterDTO size: " + compressedAttackCharacterDTO.length);
		saveAttackCharacterState.addObject(compressedAttackCharacterDTO);
		saveAttackCharacterState.addObject2(bRemoveAttackChar);
		Nexus.fireNetworkEvent(saveAttackCharacterState);
	}


	@SuppressWarnings("unused")
	public static boolean charHasAnActiveAttack() {
		boolean charHasAnotherActiveAttack = false;
		for (BOAttack a : Nexus.getBoUniverse().attackBOsOpenInThisRound.values()) {
			for (AttackCharacterDTO ac : a.getAttackCharList()) {
				if (ac.getCharacterID().equals(Nexus.getCurrentUser().getCurrentCharacter().getId())) {
					charHasAnotherActiveAttack = true;
					break;
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
	public String getStarSystemName() { return Nexus.getBoUniverse().starSystemBOs.get(attackDTO.getStarSystemID()).getName(); }

	@SuppressWarnings("unused")
	public String getAttackerFactionName() { return Nexus.getBoUniverse().getFactionByID((getAttackerFactionId().longValue())).getLocalizedName(); }

	@SuppressWarnings("unused")
	public String getAttackerFactionShortName() { return Nexus.getBoUniverse().getFactionByID((getAttackerFactionId().longValue())).getShortName(); }

	@SuppressWarnings("unused")
	public String getDefenderFactionName() { return Nexus.getBoUniverse().getFactionByID(attackDTO.getFactionID_Defender()).getLocalizedName(); }

	@SuppressWarnings("unused")
	public String getDefenderFactionShortName() { return Nexus.getBoUniverse().getFactionByID(attackDTO.getFactionID_Defender()).getShortName(); }

	@SuppressWarnings("unused")
	public Long getAttackedFromStarSystem() { return attackDTO.getAttackedFromStarSystemID(); }

	@SuppressWarnings("unused")
	public Long getJumpshipId() { return attackDTO.getJumpshipID(); }

	@SuppressWarnings("unused")
	public Integer getAttackerFactionId() {
		BOJumpship j = Nexus.getBoUniverse().getJumpshipByID(attackDTO.getJumpshipID());
		long id = j.getJumpshipFaction();
		return (int) id;
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
	public boolean attackLobbyHasBeenStarted() {
		boolean attackHasBeenStarted = false;
		for (AttackCharacterDTO a : attackDTO.getAttackCharList()) {
			if (a.getType().equals(Constants.ROLE_ATTACKER_COMMANDER)) {
				attackHasBeenStarted = true;
				break;
			}
		}
		return attackHasBeenStarted;
	}

	@SuppressWarnings("unused")
	public void setAttackFightsHaveBeenStarted(boolean value) {
		attackDTO.setFightsStarted(value);
	}

	@SuppressWarnings("unused")
	public boolean attackFightsHaveBeenStarted() {
		return attackDTO.getFightsStarted();
	}

	@SuppressWarnings("unused")
	public List<AttackCharacterDTO> getAttackCharList() {
		return attackDTO.getAttackCharList();
	}

	private Long getAttackType(){
		Long factionTypeAttacker = Nexus.getBoUniverse().getFactionType(this.getAttackerFactionId().longValue());
		Long factionTypeDefender = Nexus.getBoUniverse().getFactionType(this.getDefenderFactionId().longValue());

		if (factionTypeAttacker == 2 && factionTypeDefender == 1) { // Attacker = 2: Clan, Defender = 1: IS
			 return -1L; // Clan vs IS
		} else if (factionTypeAttacker == 2 && factionTypeDefender == 2) { // Attacker = 2: Clan, Defender = 2: Clan
			return -2L; // Clan vs Clan
		} else if (factionTypeAttacker == 1 && factionTypeDefender == 2) { // Attacker = 1: IS, Defender = 2: Clan
			return -3L; // IS vs Clan
		} else if (factionTypeAttacker == 1 && factionTypeDefender == 1) { // Attacker = 1: IS, Defender = 1: IS
			return -4L; // IS vs IS
		}
		return -1L; // TODO_C3: Sollte nochmal überdacht werden
	}
}
