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
import net.clanwolf.starmap.server.nexus2.Nexus;
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

		ArrayList<AttackPOJO> brokenAttacks = new ArrayList<>();
		ArrayList<Long> attacksToReset = new ArrayList<>();
		ArrayList<Long> currentlyOnlineCharacterIds = C3GameSessionHandler.getCurrentlyOnlineCharIds();

		int warning = 5;
		int kill = 10;
		if(Nexus.isDevelopmentPC){
			warning = 1;
			kill = 1;
		}

		// check if there is a countdown for any broken attack that is finished,
		// close the attack and remove the chars
		for (Long aid : Nexus.brokenAttackTimers.keySet()) {
			Long now = System.currentTimeMillis();
			Long timerstart = Nexus.brokenAttackTimers.get(aid);
			long diff = now - timerstart;
			if (diff > 1000 * 60 * warning) { // after 5 minutes send a warning
				// send broadcastmessage for broken attack where countdown has ended --> 5 Minute Warning
				GameState response = new GameState(GAMESTATEMODES.BROKEN_ATTACK_KILL_FIVE_MINUTE_WARNING);
				response.addObject(aid);
				response.addObject2(Nexus.brokenAttackTimers.get(aid));
				response.setAction_successfully(Boolean.TRUE);
				C3Room.sendBroadcastMessage(response);
			}
			if (diff > 1000 * 60 * kill) { // after 10 minutes
				// send broadcastmessage for broken attack where countdown has ended --> kill attack
				GameState response = new GameState(GAMESTATEMODES.BROKEN_ATTACK_KILL_AFTER_TIMEOUT);
				response.addObject(aid);
				response.addObject2(Nexus.brokenAttackTimers.get(aid));
				response.setAction_successfully(Boolean.TRUE);
				C3Room.sendBroadcastMessage(response);

				attacksToReset.add(aid); // remove all players from the lobby, reset started flag,...
			}
		}

		for (Long aid : attacksToReset) {
			Nexus.gmSessionHandler.resetAttack(aid);
			Nexus.brokenAttackTimers.remove(aid);
		}
		attacksToReset.clear();

		for (AttackPOJO a : allAttacksForRound) {
			boolean attackerCommanderFoundAndOnline = false;
			boolean defenderCommanderFoundAndOnline = false;

			if (a.getFactionID_Winner() == null) { // no winner yet -> open
				List<AttackCharacterPOJO> acl = a.getAttackCharList();
				for (AttackCharacterPOJO acp : acl) {
					if (acp.getType().equals(Constants.ROLE_ATTACKER_COMMANDER)) {
						if (currentlyOnlineCharacterIds.contains(acp.getCharacterID())) {
							attackerCommanderFoundAndOnline = true;
						}
					}
					if (acp.getType().equals(Constants.ROLE_DEFENDER_COMMANDER)) {
						if (currentlyOnlineCharacterIds.contains(acp.getCharacterID())) {
							defenderCommanderFoundAndOnline = true;
						}
					}
				}
				if (!(attackerCommanderFoundAndOnline && defenderCommanderFoundAndOnline) && a.getFightsStarted()) {
					// Attack is broken
					brokenAttacks.add(a);
				} else {
					// Attack is ok, remove countdown
					if (Nexus.brokenAttackTimers.remove(a.getId()) != null) { // a formerly broken attack was removed --> healed
						// send broadcastmessage for attack that has been healed
						GameState response = new GameState(GAMESTATEMODES.BROKEN_ATTACK_HEALED);
						response.addObject(a.getId());
						response.setAction_successfully(Boolean.TRUE);
						C3Room.sendBroadcastMessage(response);
					}
				}
			}
		}
		if (brokenAttacks.size() > 0) {
			logger.info("Found " + brokenAttacks.size() + " broken attacks (drops started but one or both dropleads are offline). Informing clients!");
			for (AttackPOJO a : brokenAttacks) {
				// check if there is a timer for this attack already
				// if not, create one
				Long timerStartTime = System.currentTimeMillis();
				if (!Nexus.brokenAttackTimers.containsKey(a.getId())) {
					Nexus.brokenAttackTimers.put(a.getId(), timerStartTime);
				}

				// send broadcastmessage for each broken attack
				GameState response = new GameState(GAMESTATEMODES.FOUND_BROKEN_ATTACK);
				response.addObject(a.getId());
				response.addObject2(timerStartTime);
				response.setAction_successfully(Boolean.TRUE);
				C3Room.sendBroadcastMessage(response);
			}
		} else {
			logger.info("No broken attacks found.");
		}
	}
}
