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

import net.clanwolf.client.mail.MailManager;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.beans.C3Room;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import javax.persistence.EntityTransaction;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class EndRound {

	private static AtomicBoolean forceFinalize = new AtomicBoolean(false);

	public static synchronized void setForceFinalize(boolean v) {
		forceFinalize.set(v);
	}

	public static Date addDaysToDate(Date date, int daysToAdd) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, daysToAdd);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String newDateString = sdf.format(c.getTime());

		return Date.valueOf(newDateString);
	}

	public static Date getRoundDate(Long seasonId, int additionalRounds) {
		SeasonDAO dao = SeasonDAO.getInstance();
		SeasonPOJO season = (SeasonPOJO) dao.findById(SeasonPOJO.class, seasonId);
		Date seasonStartDate = season.getStartDate();

		RoundDAO roundDAO = RoundDAO.getInstance();
		RoundPOJO roundPOJO = roundDAO.findBySeasonId(seasonId);
		Date currentRoundStartDate = roundPOJO.getCurrentRoundStartDate();

		if (currentRoundStartDate == null) {
			// this seems to be the first round in this season (?)
			C3Logger.info("Round date for current round is null! Setting round date to season start date.");

			roundPOJO.setCurrentRoundStartDate(seasonStartDate);

			EntityTransaction transaction = EntityManagerHelper.getEntityManager(Nexus.DUMMY_USERID).getTransaction();
			try {
				transaction.begin();
				roundDAO.update(Nexus.DUMMY_USERID, roundPOJO);
				transaction.commit();
			} catch (RuntimeException re) {
				transaction.rollback();
				C3Logger.error("Setting round date to season start date", re);
			}
			currentRoundStartDate = seasonStartDate;
		}

		int daysToAdd = additionalRounds * season.getDaysInRound().intValue();
		return addDaysToDate(currentRoundStartDate, daysToAdd);
	}

	public static Date getCurrentRoundDate(Long seasonId) {
		return getRoundDate(seasonId, 0); // current round, no additional rounds
	}

	public static Date getNextRoundDate(Long seasonId) {
		return getRoundDate(seasonId, 1); // adding one round (7 days) to get the start of next round
	}

	private static boolean timeForThisRoundIsOver(Long seasonId) {
		Date nextRoundDate = getNextRoundDate(seasonId);
		Date translatedNowDate = translateRealDateToSeasonTime(new Date(System.currentTimeMillis()), 1L);

		// round is officially over
		return !nextRoundDate.after(translatedNowDate); // the end of the round has not been reached on the calendar
	}

	public static Date translateRealDateToSeasonTime(Date date, Long seasonId) {
		SeasonDAO dao = SeasonDAO.getInstance();
		SeasonPOJO season = (SeasonPOJO) dao.findById(SeasonPOJO.class, seasonId);
		Date seasonStartDate = season.getStartDate();

		Calendar c = Calendar.getInstance();
		c.setTime(seasonStartDate);
		int seasonStartYear = c.get(Calendar.YEAR);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);

		int diff = (int) Math.abs((seasonStartYear - currentYear) * 365.243); // Days in year and Schaltjahr factor

//		LocalDateTime date1 = new Timestamp(seasonStartDate.getTime()).toLocalDateTime();
//		LocalDateTime date2 = new Timestamp(date.getTime()).toLocalDateTime();
//		Long diff = Duration.between(date1, date2).toDays();

		return addDaysToDate(date, diff);
	}

	public static Long findAWinner(AttackPOJO attackPOJO) {
		Long jumpshipId = attackPOJO.getJumpshipID();
		JumpshipPOJO jumpship = JumpshipDAO.getInstance().getJumpshipForId(jumpshipId);
		Long attackerFactionId = jumpship.getJumpshipFactionID();
		Long defenderFactionId = attackPOJO.getFactionID_Defender();

		// it is 50/50 who wins this fight if it has not been resolved in a game / series of games
		Long winnerId = Math.random() > 0.5 ? attackerFactionId : defenderFactionId;
		attackPOJO.setFactionID_Winner(winnerId);
		return winnerId;
	}

	public static String finalizeRound(Long seasonId, int round) {
		C3Logger.info("Checking on end of round.");
		ArrayList<JumpshipPOJO> jumpshipList = JumpshipDAO.getInstance().getAllJumpships();
		ArrayList<AttackPOJO> openAttacksInRoundList = AttackDAO.getInstance().getOpenAttacksOfASeasonForRound(seasonId, round);

		C3Logger.debug("Current date: " + new Date(System.currentTimeMillis()));
		C3Logger.debug("Translated current date: " + translateRealDateToSeasonTime(new Date(System.currentTimeMillis()), 1L));
		C3Logger.debug("Current round date: " + getCurrentRoundDate(seasonId));
		C3Logger.debug("Next round date: " + getNextRoundDate(seasonId));

		boolean jumpshipsLeftToMove = false;
		int jscount = 0;
		for (JumpshipPOJO js : jumpshipList) {
			if (js.getAttackReady()) {
				jumpshipsLeftToMove = true;
				jscount++;
			}
		}

		boolean attacksLeftToResolveInRound = openAttacksInRoundList.size() > 0; // We are ignoring the open attacks for the next round here

		StringBuilder resolvedAttacks = new StringBuilder();
		StringBuilder movedJumpships = new StringBuilder();

		if ((jumpshipsLeftToMove || attacksLeftToResolveInRound) && !(timeForThisRoundIsOver(seasonId)) && !forceFinalize.get()) {
			// round is still active
			C3Logger.info("Round is still active.");
			C3Logger.info("--- " + jscount + " jumpship have not moved.");
			C3Logger.info("--- " + openAttacksInRoundList.size() + " attacks still to be resolved.");
			C3Logger.info("--- There is still time left to make moves for this round!");
		} else {
			forceFinalize.set(false);

			// here is no ship left to move AND no attack left open OR the time for the round is up
			C3Logger.info("Finalizing the round:");
			C3Logger.info("--- There is NO time left for this round!");

			// move all jumpships to their next waypoint
			C3Logger.info("--- Moving all jumpships to their next waypoints.");
			// Jumpships do not need to be moved, because the waypoints have a round indicator

			// set all open attacks to resolved (decide on a winner in the process!)
			C3Logger.info("--- Resolve all attacks that are still open.");
			for (AttackPOJO attackPOJO : openAttacksInRoundList) {
				Long winnerId = findAWinner(attackPOJO);

				JumpshipPOJO jsPojo = JumpshipDAO.getInstance().findById(Nexus.DUMMY_USERID, attackPOJO.getJumpshipID());
				StarSystemPOJO ssPojo = StarSystemDAO.getInstance().findById(Nexus.DUMMY_USERID, attackPOJO.getStarSystemID());
				FactionPOJO fWinnerPojo = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackPOJO.getFactionID_Winner());
				FactionPOJO fJumpshipPOJO = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, jsPojo.getJumpshipFactionID());

				ssPojo.setFactionID(winnerId);

				resolvedAttacks.append("Jumpship '" + jsPojo.getJumpshipName() + "' (" + jsPojo.getId() + ") of " + fJumpshipPOJO.getShortName() + " (").append(attackPOJO.getJumpshipID()).append(") attacked system '" + ssPojo.getName() + "' (" + ssPojo.getId() + ") ").append("--> resolved to winner: ").append(fWinnerPojo.getShortName() + " (" + attackPOJO.getFactionID_Winner() + ").").append("\r\n");
			}

			// Count the round indicator up once
			Long newRound = (long) (round + 1);
			C3Logger.info("--- Finally increase the round indicator to: " + newRound);
			RoundPOJO roundPOJO = RoundDAO.getInstance().findBySeasonId(seasonId);
			roundPOJO.setRound(newRound);

			// Set all jumpships to attackReady again
			// Add the next system (according to the new round) from the current route to StarSystemHistory column
			C3Logger.info("--- Setting all jumpships to attackReady again.");
			for (JumpshipPOJO js : jumpshipList) {
				boolean jumpshipHasAnOpenAttack = false;
				for (AttackPOJO a : AttackDAO.getInstance().getOpenAttacksOfASeasonForRound(GameServer.getCurrentSeason(), newRound.intValue())) {
					if (a.getJumpshipID().equals(js.getId())) {
						jumpshipHasAnOpenAttack = true;
						break;
					}
				}
				js.setAttackReady(!jumpshipHasAnOpenAttack);

				for (RoutePointPOJO p : js.getRoutepointList()) {
					if (p.getRoundId().equals(newRound)) {
						String ssh = js.getStarSystemHistory();
						ssh = ssh + ";" + p.getSystemId();
						js.setStarSystemHistory(ssh);

						StarSystemPOJO ssPojo = StarSystemDAO.getInstance().findById(Nexus.DUMMY_USERID, p.getSystemId());

						movedJumpships.append("Jumpship '").append(js.getJumpshipName()).append("' moved to ").append(ssPojo.getName()).append(" (" + ssPojo.getId() + ").\r\n");
					}
				}
			}

			roundPOJO.setCurrentRoundStartDate(getNextRoundDate(seasonId));

			// Save everything to the database
			AttackDAO attackDAO = AttackDAO.getInstance();
			JumpshipDAO jumpshipDAO = JumpshipDAO.getInstance();
			RoundDAO roundDAO = RoundDAO.getInstance();
			StarSystemDataDAO ssdDAO = StarSystemDataDAO.getInstance();
			FactionDAO fDAO = FactionDAO.getInstance();

			GameState endRoundInfo = new GameState(GAMESTATEMODES.FINALIZE_ROUND);
			EntityTransaction transaction = EntityManagerHelper.getEntityManager(Nexus.DUMMY_USERID).getTransaction();
			try {
				transaction.begin();
				roundDAO.update(Nexus.DUMMY_USERID, roundPOJO);
				for (JumpshipPOJO jumpshipPOJO : jumpshipList) {
					jumpshipDAO.update(Nexus.DUMMY_USERID, jumpshipPOJO);
				}
				for (AttackPOJO attackPOJO : openAttacksInRoundList) {
					attackDAO.update(Nexus.DUMMY_USERID, attackPOJO);
				}
				for (AttackPOJO attackPOJO : AttackDAO.getInstance().getAllAttacksOfASeasonForRound(seasonId, round)) {
					StarSystemDataPOJO ssdPojo = ssdDAO.findById(Nexus.DUMMY_USERID, attackPOJO.getStarSystemDataID());
//					FactionPOJO fPojo = fDAO.findById(Nexus.DUMMY_USERID, attackPOJO.getFactionID_Winner());
					FactionPOJO fPojo = fDAO.findById(Nexus.DUMMY_USERID, 32L);
					ssdPojo.setFactionID(fPojo);

					ssdDAO.update(Nexus.DUMMY_USERID, ssdPojo);
				}
				transaction.commit();

				endRoundInfo.addObject(null);
				endRoundInfo.setAction_successfully(Boolean.TRUE);
			} catch (RuntimeException re) {
				transaction.rollback();

				endRoundInfo.addObject(re.getMessage());
				endRoundInfo.setAction_successfully(Boolean.FALSE);

				C3Logger.error("Finalize round", re);
			} finally {
				C3Room.sendBroadcastMessage(endRoundInfo);
			}

			// Sending mail with information about the last round
			C3Logger.info("Sending mail about finalized round.");
			String[] receivers = { "keshik@googlegroups.com" };
			boolean sent;

			StringBuilder subject = new StringBuilder();
			subject.append("C3 Server finalized round ").append(round).append(" of season ").append(seasonId).append(". New round is ").append(newRound);

			StringBuilder message = new StringBuilder();
			message.append("Round ").append(round).append(" finalized.\r\n\r\n");
			message.append("The new round ").append(newRound).append(" will last until ").append(getNextRoundDate(seasonId)).append(".\r\n\r\n");
			message.append("Resolved attacks:\r\n");
			message.append(resolvedAttacks).append("\r\n");
			message.append("Moved jumpships:\r\n");
			message.append(movedJumpships).append("\r\n");

			sent = MailManager.sendMail("c3@clanwolf.net", receivers, subject.toString(), message.toString(), false);
			if (sent) {
				// sent
				C3Logger.info("Mail sent.");
			} else {
				// error during email sending
				C3Logger.info("Error during mail dispatch.");
			}
		}
		return resolvedAttacks.toString() + "\r\n" + movedJumpships.toString();
	}

//	public static void main(String[] args) {
//		// 3052 - 2021 = 1031 Jahre Differenz
//		// 1031 Jahre * 365,25 Tage = 376.572,75 Tage Differenz
//		int currentYear = 2021;
//		Long season = 1L;
//		int round = 1;
//
//		int seasonStartYear = 3052;
//		int diff = (int) Math.abs((seasonStartYear - currentYear) * 365.243); // Days in year and Schaltjahr factor
//
//		System.out.println("Current date: " + new Date(System.currentTimeMillis()));
//		System.out.println("Translated: " + addDaysToDate(new Date(System.currentTimeMillis()), diff));
//		// Current date: 2021-05-06
//		// Translated:   3052-05-06
//	}
}
