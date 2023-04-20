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
import java.sql.Date;
import java.sql.Timestamp;
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
		RoundPOJO round = RoundDAO.getInstance().findBySeasonId(seasonId);
		Long roundId = round.getRound();

		Timestamp d = Timestamp.valueOf(round.getCurrentRoundStartDateRealTime());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		long dms = d.getTime();
		long nowms = now.getTime();
		long roundage = nowms - dms;
		long roundAgeHours = (((roundage / 1000) / 60) / 60);
		long finalHoursLeft = 84 - roundAgeHours;

		ArrayList<AttackPOJO> allAttacksForRound = AttackDAO.getInstance().getOpenAttacksOfASeasonForRound(seasonId, roundId.intValue());
		StringBuilder fs_de = new StringBuilder();
		StringBuilder fs_en = new StringBuilder();
		int co = 0;
		for (AttackPOJO a : allAttacksForRound) {
			String forumLink = a.getForumThreadLink();
			StarSystemDataPOJO ssd = StarSystemDataDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getStarSystemDataID());
			StarSystemPOJO ss = StarSystemDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getStarSystemID());
			JumpshipPOJO js = JumpshipDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getJumpshipID());
			FactionPOJO defender = FactionDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getFactionID_Defender());
			FactionPOJO attacker = FactionDAO.getInstance().findById(ServerNexus.DUMMY_USERID, js.getJumpshipFactionID());

			fs_de.append("- ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") wird von ").append(attacker.getShortName()).append(" angegriffen (<" + forumLink + ">)\r\n");
			fs_en.append("- ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") is attacked by ").append(attacker.getShortName()).append(" (<" + forumLink + ">)\r\n");
			co++;
		}
		if (co == 0) {
			fs_de.append("- keine ...\r\n");
			fs_en.append("- none ...\r\n");
		}
		fs_de.append("Noch ").append(finalHoursLeft).append(" Stunden in Runde ").append(roundId).append(" der Season ").append(seasonId).append(".\r\n");
		fs_en.append(finalHoursLeft).append(" hours left in round ").append(roundId).append(" of season ").append(seasonId).append(".\r\n");

		ServerNexus.getEci().sendExtCom("Round " + roundId + ",\r\n" + "open fights:\r\n" + fs_en, "en", true, true, true);
		ServerNexus.getEci().sendExtCom("Runde " + roundId + ",\r\n" + "offene Kämpfe:\r\n" + fs_de, "de", true, true, true);
	}
}
