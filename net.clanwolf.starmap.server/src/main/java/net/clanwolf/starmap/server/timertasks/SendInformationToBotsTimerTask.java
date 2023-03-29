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

import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * @author Meldric
 */
public class SendInformationToBotsTimerTask extends TimerTask {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public SendInformationToBotsTimerTask() {
	}

	@Override
	public void run() {
		Long seasonId = GameServer.getCurrentSeason();
		Long roundId = RoundDAO.getInstance().findBySeasonId(seasonId).getRound();
		ArrayList<AttackPOJO> allAttacksForRound = AttackDAO.getInstance().getOpenAttacksOfASeasonForRound(seasonId, roundId.intValue());
		String fs_de = "";
		String fs_en = "";
		for (AttackPOJO a : allAttacksForRound) {
			StarSystemDataPOJO ssd = StarSystemDataDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getStarSystemDataID());
			StarSystemPOJO ss = StarSystemDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getStarSystemID());
			JumpshipPOJO js = JumpshipDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getJumpshipID());
			FactionPOJO defender = FactionDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getFactionID_Defender());
			FactionPOJO attacker = FactionDAO.getInstance().findById(ServerNexus.DUMMY_USERID, js.getJumpshipFactionID());

			fs_de += "- " + ss.getName() + " (" + defender.getShortName() + ") wird von " + attacker.getShortName() + " angegriffen!\r\n";
			fs_en += "- " + ss.getName() + " (" + defender.getShortName() + ") is attacked by " + attacker.getShortName() + "!\r\n";
		}
		fs_de += "Noch x Stunden in Runde " + roundId + " der Season " + seasonId + ".\r\n";
		fs_en += "x hours left in round " + roundId + " of season " + seasonId + ".\r\n";

		ServerNexus.getEci().sendExtCom("Round " + roundId + ", " + " open fights:\r\n" + fs_en, "en", true, true, true);
		ServerNexus.getEci().sendExtCom("Runde " + roundId + ", " + " offene Kämpfe:\r\n" + fs_de, "de", true, true, true);
	}
}
