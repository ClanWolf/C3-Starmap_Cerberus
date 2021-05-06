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
package net.clanwolf.starmap.server.process;

import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.JumpshipDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.SeasonDAO;
import net.clanwolf.starmap.server.persistence.pojos.JumpshipPOJO;
import net.clanwolf.starmap.server.persistence.pojos.SeasonPOJO;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

public class EndRound {

	private static int DAYSINAROUND = 7;

	public static Date addDaysToDate(Date date, int daysToAdd) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, daysToAdd);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String newDateString = sdf.format(c.getTime());
		Date newDate = Date.valueOf(newDateString);

		return newDate;
	}

	public static Date getRoundDate(Long seasonId, int round, int additionalRounds) {
		SeasonDAO dao = SeasonDAO.getInstance();
		SeasonPOJO season = (SeasonPOJO) dao.findById(SeasonPOJO.class, seasonId);

		int daysToAdd = (round + additionalRounds) * DAYSINAROUND;
		Date roundDate = addDaysToDate(season.getStartDate(), daysToAdd);

		return roundDate;
	}

	public static Date getCurrentRoundDate(Long seasonId, int currentRound) {
		return getRoundDate(seasonId, currentRound, 0); // current round, no additional rounds
	}

	public static Date getNextRoundDate(Long seasonId, int currentRound) {
		return getRoundDate(seasonId, currentRound, 1); // adding one round (7 days) to get the start of next round
	}

	private static boolean timeForThisRoundIsOver(Long seasonId, int round) {
		Date nextRoundDate = getNextRoundDate(seasonId, round);
		Date nowDate = new Date(System.currentTimeMillis());

		if(nextRoundDate.after(nowDate)){
			return false; // the end of the round has not been reached on the calendar
		} else {
			return true; // round is officially over
		}
	}

	public static Date translateRealDateToSeasonTime(Date date, Long seasonId) {
		SeasonDAO dao = SeasonDAO.getInstance();
		SeasonPOJO season = (SeasonPOJO) dao.findById(SeasonPOJO.class, seasonId);
		Date seasonStartDate = season.getStartDate();

		Calendar c = Calendar.getInstance();
		c.setTime(seasonStartDate);
		int seasonStartYear = c.get(Calendar.YEAR);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);

		int diff = (int) Math.abs((seasonStartYear - currentYear) * 365.243);

//		LocalDateTime date1 = new Timestamp(seasonStartDate.getTime()).toLocalDateTime();
//		LocalDateTime date2 = new Timestamp(date.getTime()).toLocalDateTime();
//		Long diff = Duration.between(date1, date2).toDays();

		return addDaysToDate(date, diff);
	}

	public static void finalizeRound(Long seasonId, int round) {
		C3Logger.info("Checking on end of round.");
		ArrayList<JumpshipPOJO> list = JumpshipDAO.getInstance().getAllJumpships();

		C3Logger.debug("Current date: " + new Date(System.currentTimeMillis()));
		C3Logger.debug("Translated current date: " + translateRealDateToSeasonTime(new Date(System.currentTimeMillis()), 1L));
		C3Logger.debug("Current round date: " + getCurrentRoundDate(seasonId, round));
		C3Logger.debug("Next round date: " + getNextRoundDate(seasonId, round));

		boolean jumpshipsLeftToMove = false;
		for (JumpshipPOJO js : list) {
			if (js.getAttackReady()) {
				jumpshipsLeftToMove = true;
			}
		}

		if (jumpshipsLeftToMove && !(timeForThisRoundIsOver(seasonId, round))) {
			// round is still active
			C3Logger.info("Round is still active.");
		} else {
			// here is no ship left to move OR the time for the round is up
			C3Logger.info("Finalizing the round.");
		}
	}

	public static void main(String[] args) {
		// 3052 - 2021 = 1031 Jahre Differenz
		// 1031 Jahre * 365,25 Tage = 376.572,75 Tage Differenz
		int currentYear = 2021;
		int seasonStartYear = 3052;
		int diff = (int) Math.abs((seasonStartYear - currentYear) * 365.243); // Schaltjahr factor

		System.out.println("Current date: " + new Date(System.currentTimeMillis()));
		System.out.println("Translated: " + addDaysToDate(new Date(System.currentTimeMillis()), diff));

//		Current date: 2021-05-06
//		Translated: 3052-05-06
	}
}
