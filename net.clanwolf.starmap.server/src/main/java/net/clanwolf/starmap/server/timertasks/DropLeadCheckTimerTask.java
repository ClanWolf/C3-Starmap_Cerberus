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
package net.clanwolf.starmap.server.timertasks;

import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.beans.C3GameSessionHandler;
import net.clanwolf.starmap.server.beans.C3Room;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.AttackDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RoundDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackCharacterPOJO;
import net.clanwolf.starmap.server.persistence.pojos.AttackPOJO;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * @author Meldric
 */
public class DropLeadCheckTimerTask extends TimerTask {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public DropLeadCheckTimerTask() {
	}

	@Override
	public void run() {
		Long seasonId = GameServer.getCurrentSeason();
		Long roundId = RoundDAO.getInstance().findBySeasonId(seasonId).getRound();
		ArrayList<AttackPOJO> allAttacksForRound = AttackDAO.getInstance().getAllAttacksOfASeasonForRound(seasonId, roundId);

		boolean attackerCommanderFound = false;
		boolean defenderCommanderFound = false;

		ArrayList<AttackPOJO> brokenAttacks = new ArrayList<>();

		for (AttackPOJO a : allAttacksForRound) {
			if (a.getFactionID_Winner() == null) { // no winner yet -> open
				List<AttackCharacterPOJO> acl = a.getAttackCharList();
				for (AttackCharacterPOJO acp : acl) {
					if (acp.getType().equals(Constants.ROLE_ATTACKER_COMMANDER)) {
						attackerCommanderFound = true;
					}
					if (acp.getType().equals(Constants.ROLE_DEFENDER_COMMANDER)) {
						defenderCommanderFound = true;
					}
				}
				if (!(attackerCommanderFound && defenderCommanderFound)) {
					// Attack is broken
					brokenAttacks.add(a);
				}
			}
		}
		if (brokenAttacks.size() > 0) {
			logger.info("Found " + brokenAttacks.size() + " broken attacks. Informing clients!");
			for (AttackPOJO a : brokenAttacks) {
				// send broadcastmessage for each broken attack
				GameState response = new GameState(GAMESTATEMODES.FOUND_BROKEN_ATTACK);
				response.addObject(a);
				response.setAction_successfully(Boolean.TRUE);
				C3Room.sendBroadcastMessage(response);
			}
		} else {
			logger.info("No broken attacks found.");
		}
	}
}
