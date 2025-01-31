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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
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
import java.text.SimpleDateFormat;
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

		SeasonDAO seasonDAO = SeasonDAO.getInstance();
		SeasonPOJO season = (SeasonPOJO) seasonDAO.findById(SeasonPOJO.class, seasonId);
		Double daysInRound = season.getDaysInRound();
		long hoursInRound = (long) (daysInRound * 24);
		String seasonName = season.getName();
		String seasonDescription = season.getDescription();

		Timestamp d = Timestamp.valueOf(round.getCurrentRoundStartDateRealTime());
		Timestamp now = new Timestamp(System.currentTimeMillis());

		long maxRoundAgeMillis = hoursInRound * 60 * 60 * 1000;
		long roundageMillis = now.getTime() - d.getTime();
		long minutesLeft = ((maxRoundAgeMillis - roundageMillis) / 1000) / 60;
		long finalHoursLeft = minutesLeft / 60;
		long finalMinutesLeft = minutesLeft - finalHoursLeft * 60;

		long roundAgeInMinutes = roundageMillis / 1000 / 60;
		long finalHoursRoundAge = roundAgeInMinutes / 60;
		long finalMinutesRoundAge = roundAgeInMinutes - finalHoursRoundAge * 60;

		Timestamp dateInGame = new Timestamp((Timestamp.valueOf(round.getCurrentRoundStartDate()).getTime()) + roundageMillis);
		Timestamp endOfRound = new Timestamp(d.getTime() + maxRoundAgeMillis);

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

			String discordthreadname = "[S" + seasonId + "R" + roundId + "] " + attacker.getShortName() + " ⚔ " + ss.getName() + " (" + defender.getShortName() + ")";

			// Link example for discord: [test](<https://www.clanwolf.net/forum/viewthread.php?thread_id=5207>)
			// Channel link with id: <#1333360940883640323>

			//				Get the channel from the guild:
			//				const channel = message.guild.channels.find(channel => channel.name === 'Name of the channel');
			//				message.channel.send(`Please take a look at this Discord Server channel <#${channel.id}>`)

			// CW ⚔ Domain (CJF)
			//fs_de.append("- ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") wird von ").append(attacker.getShortName()).append(" angegriffen (<").append(forumLink).append(">)\r\n");
			//fs_en.append("- ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") is attacked by ").append(attacker.getShortName()).append(" (<").append(forumLink).append(">)\r\n");
			fs_de.append("- ").append("[CWG](<").append(forumLink).append(">)").append("{%%%").append(discordthreadname).append("%%%}").append("\r\n");
			fs_en.append("- ").append("[CWG](<").append(forumLink).append(">)").append("{%%%").append(discordthreadname).append("%%%}").append("\r\n");
			co++;
		}
		if (co == 0) {
			fs_de.append("- keine...\r\n");
			fs_en.append("- none...\r\n");
		}

		fs_de.append("\r\nRunde ").append(roundId).append(" ist ").append(finalHoursRoundAge).append(" Stunden und ").append(finalMinutesRoundAge).append(" Minuten alt").append(".\r\n");
		fs_en.append("\r\nRound ").append(roundId).append(" is ").append(finalHoursRoundAge).append(" hours and ").append(finalMinutesRoundAge).append(" minutes old").append(".\r\n");

		fs_de.append("Noch maximal ").append(finalHoursLeft).append(" Stunden und ").append(finalMinutesLeft).append(" Minuten in Runde ").append(roundId).append(" der Season ").append(seasonId).append(".\r\n");
		fs_en.append("Up to ").append(finalHoursLeft).append(" hours and ").append(finalMinutesLeft).append(" minutes left in round ").append(roundId).append(" of season ").append(seasonId).append(".\r\n");

		fs_de.append("Die Runde kann durch einen Admin beendet werden, wenn alle Kämpfe durchgeführt wurden!\r\n");
		fs_en.append("The Round may be finalized by an admin as soon as all fights have been finished!\r\n");

		if (!allAttacksForNextRound.isEmpty()) {
			int nextRound = roundId.intValue() + 1;
			fs_de.append("\r\n__Angriffe in der nächsten Runde (").append(nextRound).append("):__\r\n");
			fs_en.append("\r\n__Attacks in the next round (").append(nextRound).append("):__\r\n");
			for (AttackPOJO a : allAttacksForNextRound) {
				String forumLink = a.getForumThreadLink();
				StarSystemDataPOJO ssd = StarSystemDataDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getStarSystemDataID());
				StarSystemPOJO ss = StarSystemDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getStarSystemID());
				JumpshipPOJO js = JumpshipDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getJumpshipID());
				FactionPOJO defender = FactionDAO.getInstance().findById(ServerNexus.DUMMY_USERID, a.getFactionID_Defender());
				FactionPOJO attacker = FactionDAO.getInstance().findById(ServerNexus.DUMMY_USERID, js.getJumpshipFactionID());

				String discordthreadname = "[S" + seasonId + "R" + roundId + "] " + attacker.getShortName() + " ⚔ " + ss.getName() + " (" + defender.getShortName() + ")";

				// Link example for discord: [test](<https://www.clanwolf.net/forum/viewthread.php?thread_id=5207>)
				// Channel link with id: <#1333360940883640323>

//				Get the channel from the guild:
//				const channel = message.guild.channels.find(channel => channel.name === 'Name of the channel');
//				message.channel.send(`Please take a look at this Discord Server channel <#${channel.id}>`)

				// CW ⚔ Domain (CJF)
				//fs_de.append("- ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") wird von ").append(attacker.getShortName()).append(" angegriffen (<").append(forumLink).append(">)\r\n");
				//fs_en.append("- ").append(ss.getName()).append(" (").append(defender.getShortName()).append(") is attacked by ").append(attacker.getShortName()).append(" (<").append(forumLink).append(">)\r\n");
				fs_de.append("- ").append("[CWG](<").append(forumLink).append(">) | ").append("{%%%").append(discordthreadname).append("%%%}").append("\r\n");
				fs_en.append("- ").append("[CWG](<").append(forumLink).append(">) | ").append("{%%%").append(discordthreadname).append("%%%}").append("\r\n");
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

		SimpleDateFormat format_de = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		SimpleDateFormat format_en = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat format_de_long = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat format_en_long = new SimpleDateFormat("MMMM dd, yyyy");

		String sd_de = format_de.format(d);
		String sd_en = format_en.format(d);
		String ed_de = format_de.format(endOfRound);
		String ed_en = format_en.format(endOfRound);
		String dig_de = "_" + format_de_long.format(dateInGame) + "_";
		String dig_en = "_" + format_en_long.format(dateInGame) + "_";

		StringBuilder finalString_en = new StringBuilder();
		StringBuilder finalString_de = new StringBuilder();

		finalString_en.append("--------------------------------\r\n\r\n# Status\r\n\r\n**Round ").append(roundId).append(" of season ").append(seasonId).append(" - ").append(seasonName).append("**\r\n").append(seasonDescription).append("\r\n").append(dig_en).append("\r\n\r\n").append("Started: ").append(sd_en).append("\r\n").append("Will end: ").append(ed_en).append(" (latest, ").append(daysInRound).append(" days in a round max)\r\n\r\n").append("__Open fights:__\r\n").append(fs_en);
		finalString_de.append("--------------------------------\r\n\r\n# Status\r\n\r\n**Runde ").append(roundId).append(" von Season ").append(seasonId).append(" - ").append(seasonName).append("**\r\n").append(seasonDescription).append("\r\n").append(dig_de).append("\r\n\r\n").append("Gestartet: ").append(sd_de).append("\r\n").append("Wird beendet: ").append(ed_de).append(" (spätestens, ").append(daysInRound).append(" Tage in einer Runde maximal)\r\n\r\n").append("__Offene Kämpfe:__\r\n").append(fs_de);

		ServerNexus.getEci().sendExtCom(finalString_en.toString(), "en", true, true, true);
		ServerNexus.getEci().sendExtCom(finalString_de.toString(), "de", true, true, true);
	}
}
