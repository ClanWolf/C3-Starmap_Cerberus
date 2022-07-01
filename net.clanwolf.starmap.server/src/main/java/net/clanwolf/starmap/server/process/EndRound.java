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
 * Copyright (c) 2001-2022, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.process;

import net.clanwolf.client.mail.MailManager;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.beans.C3Room;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityTransaction;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class EndRound {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
			logger.info("Round date for current round is null! Setting round date to season start date.");

			roundPOJO.setCurrentRoundStartDate(seasonStartDate);

			EntityTransaction transaction = EntityManagerHelper.getEntityManager(Nexus.DUMMY_USERID).getTransaction();
			try {
				transaction.begin();
				//EntityManagerHelper.clear(Nexus.DUMMY_USERID);
				roundDAO.update(Nexus.DUMMY_USERID, roundPOJO);
				transaction.commit();
			} catch (RuntimeException re) {
				transaction.rollback();
				logger.error("Setting round date to season start date", re);
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
		return getRoundDate(seasonId, 1); // adding one round (x days) to get the start of next round
	}

	private static boolean timeForThisRoundIsOver(Long seasonId) {
		Date nextRoundDate = getNextRoundDate(seasonId);
		Date translatedNowDate = translateRealDateToSeasonTime(new Date(System.currentTimeMillis()), 1L);

		SimpleDateFormat dateFormatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
		logger.info("nextRoundDate: " + dateFormatter.format(nextRoundDate));
		logger.info("translatedNowDate: " + dateFormatter.format(translatedNowDate));

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

	public synchronized static String finalizeRound(Long seasonId, int round) {
		logger.info("Checking on end of round.");
		ArrayList<JumpshipPOJO> jumpshipList = JumpshipDAO.getInstance().getAllJumpships();
		ArrayList<AttackPOJO> openAttacksInRoundList = new ArrayList<>();
		ArrayList<AttackPOJO> allAttacksForRound = AttackDAO.getInstance().getAllAttacksOfASeasonForRound(seasonId, round);

		for (AttackPOJO attackPojoDummy : allAttacksForRound) {
			if (attackPojoDummy.getFactionID_Winner() == null) {
				openAttacksInRoundList.add(attackPojoDummy);
			}
		}

		logger.debug("Current date: " + new Date(System.currentTimeMillis()));
		logger.debug("Translated current date: " + translateRealDateToSeasonTime(new Date(System.currentTimeMillis()), 1L));
		logger.debug("Current round date: " + getCurrentRoundDate(seasonId));
		logger.debug("Next round date: " + getNextRoundDate(seasonId));

		boolean jumpshipsLeftToMove = false;
		int jscount = 0;

		for (JumpshipPOJO js : jumpshipList) {
			if (js.getAttackReady()) {
				jumpshipsLeftToMove = true;
				jscount++;
			}
		}

		boolean attacksLeftToResolveInRound = openAttacksInRoundList.size() > 0; // We are ignoring the open attacks for the next round here

		StringBuilder foughtAttacks = new StringBuilder();
		StringBuilder resolvedAttacks = new StringBuilder();
		StringBuilder movedJumpships = new StringBuilder();
		forceFinalize.set(true);

		if ((jumpshipsLeftToMove || attacksLeftToResolveInRound) && !(timeForThisRoundIsOver(seasonId)) && !forceFinalize.get()) {
			// round is still active
			logger.info("Round is still active.");
			logger.info("--- " + jscount + " jumpship have not moved.");
			logger.info("--- " + openAttacksInRoundList.size() + " attacks still to be resolved.");
			logger.info("--- There is still time left to make moves for this round!");
		} else {
			forceFinalize.set(false);

			EntityTransaction transaction = EntityManagerHelper.getEntityManager(Nexus.END_ROUND).getTransaction();
			String exceptionWhileSaving = "";
			Long newRound = null;
			GameState endRoundInfo = new GameState(GAMESTATEMODES.FINALIZE_ROUND);
			ArrayList<String> factionStatistics = new ArrayList<>();

			try {
				transaction.begin();
				EntityManagerHelper.clear(Nexus.END_ROUND);
			// here is no ship left to move AND no attack left open OR the time for the round is up
			logger.info("Finalizing the round:");
			logger.info("--- There is NO time left for this round!");

			// set all open attacks to resolved (decide on a winner in the process!)
			logger.info("--- Resolved attacks that were still open:");
			for (AttackPOJO attackPOJO : openAttacksInRoundList) {
				Long winnerId = findAWinner(attackPOJO);

				JumpshipPOJO jsPojo = JumpshipDAO.getInstance().findById(Nexus.END_ROUND, attackPOJO.getJumpshipID());
				StarSystemPOJO ssPojo = StarSystemDAO.getInstance().findById(Nexus.END_ROUND, attackPOJO.getStarSystemID());
				FactionPOJO fWinnerPojo = FactionDAO.getInstance().findById(Nexus.END_ROUND, winnerId);
				FactionPOJO fJumpshipPOJO = FactionDAO.getInstance().findById(Nexus.END_ROUND, jsPojo.getJumpshipFactionID());

				resolvedAttacks.append("Jumpship '").append(jsPojo.getJumpshipName()).append("' (").append(jsPojo.getId()).append(") of ").append(fJumpshipPOJO.getShortName()).append(" (").append(fJumpshipPOJO.getId()).append(") attacked system '").append(ssPojo.getName()).append("' (").append(ssPojo.getId()).append(") ").append("--> resolved to winner: ").append(fWinnerPojo.getShortName() + " (" + attackPOJO.getFactionID_Winner() + ").").append("\r\n");
			}
			openAttacksInRoundList.clear();

			// list all attacks that were fought in this round
			logger.info("--- Attacks that have been fought in this round:");
			for (AttackPOJO attackPOJO : allAttacksForRound) {
				logger.info("--- Attack: " + attackPOJO.getId() + "(StarSystem: " + attackPOJO.getStarSystemID() + ")");

				Long winnerId = attackPOJO.getFactionID_Winner();

				JumpshipPOJO jsPojo = JumpshipDAO.getInstance().findById(Nexus.END_ROUND, attackPOJO.getJumpshipID());
				StarSystemPOJO ssPojo = StarSystemDAO.getInstance().findById(Nexus.END_ROUND, attackPOJO.getStarSystemID());
				FactionPOJO fWinnerPojo = FactionDAO.getInstance().findById(Nexus.END_ROUND, winnerId);
				FactionPOJO fJumpshipPOJO = FactionDAO.getInstance().findById(Nexus.END_ROUND, jsPojo.getJumpshipFactionID());

//				logger.info("1");
				// Get statistics for this attack
				ArrayList<AttackStatsPOJO> statisticsList = AttackStatsDAO.getInstance().getStatisticsForAttack(seasonId, attackPOJO.getId());
//				logger.info("2");
				if (!statisticsList.isEmpty()) {
					// log stats
					logger.info("--- Statistics found for attackId: " + attackPOJO.getId());
					StringBuilder repairCostReport = new StringBuilder();
					StringBuilder xpReport = new StringBuilder();
					long balanceAttacker = 0L;
					long balanceDefender = 0L;
					DecimalFormat nf = new DecimalFormat();

					for (AttackStatsPOJO asp : statisticsList) {
						FactionPOJO factionAttacker = FactionDAO.getInstance().findById(Nexus.END_ROUND, asp.getAttackerFactionId());
						FactionPOJO factionDefender = FactionDAO.getInstance().findById(Nexus.END_ROUND, asp.getDefenderFactionId());


						logger.info("--- Calculate the balance costs [" + factionAttacker.getShortName() + "] versus [" + factionDefender.getShortName() + "] (MatchID: " + asp.getMwoMatchId() + " )---");

						CalcBalance calcB = new CalcBalance(asp);

						balanceAttacker += calcB.getAttackerRepairCost();
						balanceDefender += calcB.getDefenderRepairCost();
						repairCostReport.append(calcB.getMailMessage());
						logger.info("Current balance attacker[" + factionAttacker.getShortName() +"]:" + nf.format(balanceAttacker) +
								" --- Current balance defender[ " + factionDefender.getShortName()+ "]" + nf.format(balanceDefender));
						CalcXP calcXP = new CalcXP(asp);
						xpReport.append(calcXP.getMailMessage());

					}
					String[] receivers = {"keshik@googlegroups.com"};
					boolean sent;
					StringBuilder subject = new StringBuilder();
					subject.append("Repair cost calculation for attackId ").append(attackPOJO.getId());

					if (!GameServer.isDevelopmentPC) {
						sent = MailManager.sendMail("c3@clanwolf.net", receivers, subject.toString(), repairCostReport.toString(), false);

						if (sent) {
							// sent
							logger.info("Mail sent. [5]");
						} else {
							// error during email sending
							logger.info("Error during mail dispatch. [5]");
						}
					} else {
						logger.info("Mail was not sent out because this is a dev computer.");
					}
					if (!GameServer.isDevelopmentPC) {
						sent = MailManager.sendMail("c3@clanwolf.net", receivers, "XP calculation for attackId " + attackPOJO.getId(), xpReport.toString(), false);

						if (sent) {
							// sent
							logger.info("Mail sent. [5]");
						} else {
							// error during email sending
							logger.info("Error during mail dispatch. [5]");
						}
					} else {
						logger.info("Mail was not sent out because this is a dev computer.");
					}
				} else {
					// no statistics found
					logger.info("--- no statistics found for attackId: " + attackPOJO.getId());
				}
			}

			// move all jumpships to their next waypoint
			logger.info("--- Moving all jumpships to their next waypoints.");
			// Jumpships do not need to be moved, because the waypoints have a round indicator

			// Count the round indicator up once
			 newRound = (long) (round + 1);
			logger.info("--- Finally increase the round indicator to: " + newRound);
			RoundPOJO roundPOJO = RoundDAO.getInstance().findBySeasonId(seasonId);
			roundPOJO.setRound(newRound);

			// Set all jumpships to attackReady again
			// Add the next system (according to the new round) from the current route to StarSystemHistory column
			logger.info("--- Setting all jumpships to attackReady again.");
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

						StarSystemPOJO ssPojo = StarSystemDAO.getInstance().findById(Nexus.END_ROUND, p.getSystemId());
						movedJumpships.append("Jumpship '").append(js.getJumpshipName()).append("' moved to ").append(ssPojo.getName()).append(" (").append(ssPojo.getId()).append(").\r\n");
					}
				}
			}

			//			roundPOJO.setCurrentRoundStartDate(getNextRoundDate(seasonId));
			// TODO: Check this! Set start date of a new round always to NOW, not a calculated date in future, because there the dates run apart over time
			roundPOJO.setCurrentRoundStartDate(translateRealDateToSeasonTime(new Date(System.currentTimeMillis()), seasonId));

			// Save everything to the database



			AttackDAO attackDAO = AttackDAO.getInstance();
			JumpshipDAO jumpshipDAO = JumpshipDAO.getInstance();
			RoundDAO roundDAO = RoundDAO.getInstance();
			StarSystemDataDAO ssdDAO = StarSystemDataDAO.getInstance();
			FactionDAO fDAO = FactionDAO.getInstance();



				roundDAO.update(Nexus.END_ROUND, roundPOJO);
				for (JumpshipPOJO jumpshipPOJO : jumpshipList) {
					//					jumpshipDAO.refresh(Nexus.END_ROUND, jumpshipPOJO);
					jumpshipDAO.update(Nexus.END_ROUND, jumpshipPOJO);
				}
				//for (AttackPOJO attackPOJO : openAttacksInRoundList) {
				//	attackDAO.update(Nexus.END_ROUND, attackPOJO);
				//}
				for (AttackPOJO attackPOJO : allAttacksForRound) {
					Long winnerId = attackPOJO.getFactionID_Winner();

					StarSystemDataPOJO ssdPojo = ssdDAO.findById(Nexus.END_ROUND, attackPOJO.getStarSystemDataID());
					FactionPOJO fPojo = fDAO.findById(Nexus.END_ROUND, winnerId);
					ssdPojo.setFactionID(fPojo);
					logger.debug("**** Storing winner for attack " + attackPOJO.getId() + " to be " + winnerId + ".");

					ssdDAO.update(Nexus.END_ROUND, ssdPojo);
					attackDAO.update(Nexus.END_ROUND, attackPOJO);
				}
				transaction.commit();

				// ---------------------------------------------------------------------------

				// Generate faction statistic data
				logger.info("Start to generate statistics...");
				ArrayList<FactionPOJO> factionListHH = FactionDAO.getInstance().getAll_HH_Factions();
				ArrayList<StarSystemDataPOJO> starsystemdataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();

				HashMap<Long, Long> systemCostMap = new HashMap<>();
				HashMap<Long, Long> systemIncomeMap = new HashMap<>();
				HashMap<Long, Integer> systemCountAll = new HashMap<>();
				HashMap<Long, Integer> systemCountAttacking = new HashMap<>();
				HashMap<Long, Integer> systemCountDefending = new HashMap<>();
				HashMap<Long, Integer> systemCountRegular = new HashMap<>();
				HashMap<Long, Integer> systemCountIndustrial = new HashMap<>();
				HashMap<Long, Integer> systemCountCapital = new HashMap<>();
				for (FactionPOJO faction : factionListHH) {
					long cost = 0;
					long income = 0;
					int countAll = 0;
					int countAttacking = 0;
					int countDefending = 0;
					int countRegular = 0;
					int countIndustrial = 0;
					int countCapital = 0;
					for (StarSystemDataPOJO starsystemdata : starsystemdataListHH) {
						if (starsystemdata.getFactionID().getId().equals(faction.getId())) {
							countAll++;
							switch (starsystemdata.getLevel().intValue()) {
								case 1 -> { // Regular
									countRegular++;
									income = income + Constants.REGULAR_SYSTEM_GENERAL_INCOME;
									cost = cost + Constants.REGULAR_SYSTEM_GENERAL_COST;
								}
								case 2 -> { // Industrial
									countIndustrial++;
									income = income + Constants.INDUSTRIAL_SYSTEM_GENERAL_INCOME;
									cost = cost + Constants.INDUSTRIAL_SYSTEM_GENERAL_COST;
								}
								case 3 -> { // Capital
									countCapital++;
									income = income + Constants.CAPITAL_SYSTEM_GENERAL_INCOME;
									cost = cost + Constants.CAPITAL_SYSTEM_GENERAL_COST;
								}
							}
						}
						for (AttackPOJO a : AttackDAO.getInstance().getAllAttacksOfASeasonForRound(seasonId, round)) {
							if (a.getFactionID_Defender().equals(faction.getId())) {
								countDefending++;
								switch (starsystemdata.getLevel().intValue()) {
									case 1 -> // Regular world
											cost = cost + Constants.REGULAR_SYSTEM_DEFEND_COST;
									case 2 -> // Industrial world
											cost = cost + Constants.INDUSTRIAL_SYSTEM_DEFEND_COST;
									case 3 -> // Captial world
											cost = cost + Constants.CAPITAL_SYSTEM_DEFEND_COST;
								}
							}
							for (JumpshipPOJO jumpshipPOJO : jumpshipList) {
								if (jumpshipPOJO.getId().equals(a.getJumpshipID())) {
									countAttacking++;
									switch (starsystemdata.getLevel().intValue()) {
										case 1 -> // Regular world
												cost = cost + Constants.REGULAR_SYSTEM_ATTACK_COST;
										case 2 -> // Industrial world
												cost = cost + Constants.INDUSTRIAL_SYSTEM_ATTACK_COST;
										case 3 -> // Captial world
												cost = cost + Constants.CAPITAL_SYSTEM_ATTACK_COST;
									}
								}
							}
						}
					}

					String s = "";
					s += "\r\n" + faction.getName_en() + " (" + faction.getShortName() + ")\r\n";
					s += "--------------------------------\r\n";
					s += "Systems: " + countAll + "\r\n";
					s += "- Regular: " + countRegular + "\r\n";
					s += "- Industrial: " + countIndustrial + "\r\n";
					s += "- Capital: " + countCapital + "\r\n";
					s += "Attacking: " + countAttacking + "\r\n";
					s += "Defending: " + countDefending + "\r\n";
					s += "Income: " + income + " k₵\r\n";
					s += "Cost: " + cost + " k₵\r\n";
					s += "Balance: " + (income + cost) + " k₵\r\n";
					factionStatistics.add(s);

					systemCostMap.put(faction.getId(), cost);
					systemIncomeMap.put(faction.getId(), income);
					systemCountAll.put(faction.getId(), countAll);
					systemCountAttacking.put(faction.getId(), countAttacking);
					systemCountDefending.put(faction.getId(), countDefending);
					systemCountRegular.put(faction.getId(), countRegular);
					systemCountIndustrial.put(faction.getId(), countIndustrial);
					systemCountCapital.put(faction.getId(), countCapital);
				}
				logger.info("... statistics finished.");

				endRoundInfo.addObject(null);
				endRoundInfo.setAction_successfully(Boolean.TRUE);
			} catch (RuntimeException re) {
				logger.error("Finalize round", re);
				re.printStackTrace();

				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				re.printStackTrace(pw);
				exceptionWhileSaving = sw.toString();

				transaction.rollback();

				endRoundInfo.addObject(re.getMessage());
				endRoundInfo.setAction_successfully(Boolean.FALSE);
			} finally {
				C3Room.sendBroadcastMessage(endRoundInfo);
			}

			// Sending mail with information about the last round
			logger.info("Sending mail about finalized round.");
			String[] receivers = {"keshik@googlegroups.com"};
			boolean sent;

			StringBuilder subject = new StringBuilder();
			subject.append("C3 Server finalized round ").append(round).append(" of season ").append(seasonId).append(". New round is ").append(newRound);

			StringBuilder message = new StringBuilder();

			if (!"".equals(exceptionWhileSaving)) {
				message.append("\r\n\r\n-------------------------------------------------------------------");
				message.append("\r\n!!! ERROR, Attention required !!!");
				message.append("\r\n\r\nException occured while ending round:");
				message.append(exceptionWhileSaving);
				message.append("-------------------------------------------------------------------");
				message.append("\r\n\r\n\r\n\r\n");
			}

			message.append("Round ").append(round).append(" finalized.\r\n\r\n");
			message.append("Current date is: ").append(getCurrentRoundDate(seasonId));
			message.append("The new round ").append(newRound).append(" will last until ").append(getNextRoundDate(seasonId)).append(".\r\n\r\n");
			message.append("Resolved attacks:\r\n");
			message.append(resolvedAttacks).append("\r\n");
			message.append("Moved jumpships:\r\n");
			message.append(movedJumpships).append("\r\n");

			for (String s : factionStatistics) {
				message.append(s);
			}

			if (!GameServer.isDevelopmentPC) {
				sent = MailManager.sendMail("c3@clanwolf.net", receivers, subject.toString(), message.toString(), false);

				if (sent) {
					// sent
					logger.info("Mail sent. [5]");
				} else {
					// error during email sending
					logger.info("Error during mail dispatch. [5]");
				}
			} else {
				logger.info("Mail was not sent out because this is a dev computer.");
			}
		}
		return foughtAttacks + "\r\n" + resolvedAttacks + "\r\n" + movedJumpships;
	}

	/*public static void main(String[] args) {
		ArrayList<AttackStatsPOJO> statisticsList = AttackStatsDAO.getInstance().getStatisticsForAttack(1L, 92L);

		if (!statisticsList.isEmpty()) {
			// log stats
			for (AttackStatsPOJO asp : statisticsList) {

				CalcBalance calcB = new CalcBalance(asp);
				System.out.println(calcB.getMailMessage().toString());

			}

		}
	}*/
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
