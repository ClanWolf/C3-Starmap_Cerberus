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
package net.clanwolf.starmap.server.timertasks;

import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.security.Security;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.transfer.enums.PRIVILEGES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

		long maxRoundAgeMillis = 84 * 60 * 60 * 1000;
		long roundageMillis = now.getTime() - d.getTime();
		long minutesLeft = ((maxRoundAgeMillis - roundageMillis) / 1000) / 60;
		long finalHoursLeft = minutesLeft / 60;
		long finalMinutesLeft = minutesLeft - finalHoursLeft * 60;

		ArrayList<AttackPOJO> allAttacksForRound = AttackDAO.getInstance().getOpenAttacksOfASeasonForRound(seasonId, roundId.intValue());
		ArrayList<AttackPOJO> allAttacksForNextRound = AttackDAO.getInstance().getOpenAttacksOfASeasonForRound(seasonId, roundId.intValue() + 1);
		ArrayList<RolePlayCharacterPOJO> allCharacters = RolePlayCharacterDAO.getInstance().getAllCharacter();

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

			// CW ⚔ Domain (CJF)
			//fs_de.append("- ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") wird von ").append(attacker.getShortName()).append(" angegriffen (<").append(forumLink).append(">)\r\n");
			//fs_en.append("- ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") is attacked by ").append(attacker.getShortName()).append(" (<").append(forumLink).append(">)\r\n");
			fs_de.append("- ").append(attacker.getShortName()).append(" ⚔ ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") - <").append(forumLink).append(">\r\n");
			fs_en.append("- ").append(attacker.getShortName()).append(" ⚔ ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") - <").append(forumLink).append(">\r\n");
			co++;
		}
		if (co == 0) {
			fs_de.append("- keine...\r\n");
			fs_en.append("- none...\r\n");
		}
		fs_de.append("\r\nNoch ").append(finalHoursLeft).append(" Stunden und ").append(finalMinutesLeft).append(" Minuten in Runde ").append(roundId).append(" der Season ").append(seasonId).append(".\r\n");
		fs_en.append("\r\n").append(finalHoursLeft).append(" hours and ").append(finalMinutesLeft).append(" minutes left in round ").append(roundId).append(" of season ").append(seasonId).append(".\r\n");

		if (allAttacksForNextRound.size() > 0) {
			int nextRound = roundId.intValue() + 1;
			fs_de.append("\r\nAngriffe in der nächsten Runde (").append(nextRound).append("): \r\n");
			fs_en.append("\r\nAttacks in the next round (").append(nextRound).append("): \r\n");
			for (AttackPOJO a : allAttacksForNextRound) {
				String forumLink = a.getForumThreadLink();
				StarSystemDataPOJO ssd = StarSystemDataDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getStarSystemDataID());
				StarSystemPOJO ss = StarSystemDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getStarSystemID());
				JumpshipPOJO js = JumpshipDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getJumpshipID());
				FactionPOJO defender = FactionDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getFactionID_Defender());
				FactionPOJO attacker = FactionDAO.getInstance().findById(ServerNexus.DUMMY_USERID, js.getJumpshipFactionID());

				// CW ⚔ Domain (CJF)
				//fs_de.append("- ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") wird von ").append(attacker.getShortName()).append(" angegriffen (<").append(forumLink).append(">)\r\n");
				//fs_en.append("- ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") is attacked by ").append(attacker.getShortName()).append(" (<").append(forumLink).append(">)\r\n");
				fs_de.append("- ").append(attacker.getShortName()).append(" ⚔ ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") - <").append(forumLink).append(">\r\n");
				fs_en.append("- ").append(attacker.getShortName()).append(" ⚔ ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") - <").append(forumLink).append(">\r\n");
			}
		}

//		fs_de.append("\r\nFraktionskontakte:");
//		fs_en.append("\r\nFaction contacts:");
//		for (RolePlayCharacterPOJO c : allCharacters) {
//			UserPOJO u = UserDAO.getInstance().findById(ServerNexus.DUMMY_USERID, c.getUser().getUserId());
//			if (Security.hasPrivilege(u, PRIVILEGES.FACTIONLEAD_HAS_ROLE)) {
//				FactionPOJO f = FactionDAO.getInstance().findById(ServerNexus.DUMMY_USERID, c.getFactionId().longValue());
//
//				if (Security.isGodAdmin(c.getUser())) {
//					fs_de.append("\r\n- ADMIN: ").append(" ").append(c.getName()).append(" (").append(f.getShortName()).append(")");
//					fs_en.append("\r\n- ADMIN: ").append(" ").append(c.getName()).append(" (").append(f.getShortName()).append(")");
//				} else {
//					fs_de.append("\r\n- ").append(f.getShortName()).append(": ").append(c.getRank()).append(" ").append(c.getName());
//					fs_en.append("\r\n- ").append(f.getShortName()).append(": ").append(c.getRank()).append(" ").append(c.getName());
//				}
//			}
//		}

		ServerNexus.getEci().sendExtCom("Round " + roundId + "\r\n" + "Open fights:\r\n" + fs_en, "en", true, true, true);
		ServerNexus.getEci().sendExtCom("Runde " + roundId + "\r\n" + "Offene Kämpfe:\r\n" + fs_de, "de", true, true, true);
	}
}
