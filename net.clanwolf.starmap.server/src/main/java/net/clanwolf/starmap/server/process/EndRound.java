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
package net.clanwolf.starmap.server.process;

import jakarta.persistence.EntityTransaction;
import net.clanwolf.starmap.mail.MailManager;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import net.clanwolf.starmap.server.beans.C3Room;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.server.reporting.GenerateRoundReport;
import net.clanwolf.starmap.server.util.AnimatedGifCreator;
import net.clanwolf.starmap.server.util.ForumDatabaseTools;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    private static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return new java.sql.Timestamp(dateToConvert.getTime()).toLocalDateTime();
    }

    // BOTH methods are needed!
    public static LocalDateTime addDaysToDate(LocalDateTime localDateTime, double daysToAdd) {
        double hoursToAdd = daysToAdd * 24;
        LocalDateTime newTime = localDateTime.plusHours((int) hoursToAdd); // casting to int removes anything behind decimal point -> rounded hours
        logger.info("Number of hours to add until end of next round: " + hoursToAdd);
        logger.info("New time for next round to end: " + newTime);
        return newTime;
    }

    // BOTH methods are needed!
    public static Date addDaysToDate(Date date, int daysToAdd) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, daysToAdd);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String newDateString = sdf.format(c.getTime());

        return Date.valueOf(newDateString);
    }

    public static LocalDateTime getRoundDate(Long seasonId, int additionalRounds) {
        SeasonDAO dao = SeasonDAO.getInstance();
        SeasonPOJO season = (SeasonPOJO) dao.findById(SeasonPOJO.class, seasonId);
        Date seasonStartDate = season.getStartDate();

        RoundDAO roundDAO = RoundDAO.getInstance();
        RoundPOJO roundPOJO = roundDAO.findBySeasonId(seasonId);

        LocalDateTime currentRoundStartDate = null;
        String currentRoundStartDateString = roundPOJO.getCurrentRoundStartDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ServerNexus.patternTimestamp);
        if (!currentRoundStartDateString.isEmpty()) {
            currentRoundStartDate = LocalDateTime.parse(currentRoundStartDateString, formatter);
        }


		/*if (currentRoundStartDate == null) {
			// this seems to be the first round in this season (?)
			logger.info("Round date for current round is null! Setting round date to season start date.");

			//roundPOJO.setCurrentRoundStartDate(Timestamp.valueOf(convertToLocalDateTime(seasonStartDate)));
			roundPOJO.setCurrentRoundStartDate( new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Timestamp.valueOf(convertToLocalDateTime(seasonStartDate))));


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
			currentRoundStartDate = convertToLocalDateTime(seasonStartDate);
		}*/

        double daysToAdd = additionalRounds * season.getDaysInRound();
        return addDaysToDate(currentRoundStartDate, daysToAdd); // this calculates hours and adds hours
    }

    public static LocalDateTime getCurrentRoundDate(Long seasonId) {
        return getRoundDate(seasonId, 0); // current round, no additional rounds
    }

    public static LocalDateTime getNextRoundDate(Long seasonId) {
        return getRoundDate(seasonId, 1); // adding one round (x days) to get the start of next round
    }

    private static boolean timeForThisRoundIsOver(Long seasonId) {
        DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern(ServerNexus.patternTimestamp);

        LocalDateTime translatedNowDateWithTime = null;
        LocalDateTime nextRoundDate = null;
        try {
            nextRoundDate = getNextRoundDate(seasonId);
            Date translatedNowDate = translateRealDateToSeasonDate(new Date(System.currentTimeMillis()), 1L);

            LocalDate localDate = new java.util.Date(translatedNowDate.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime now = LocalTime.now();
            translatedNowDateWithTime = LocalDateTime.of(localDate, now);

            logger.info("Check if round is over:");
            logger.info("Next round date: " + dateTimeformatter.format(nextRoundDate));
            logger.info("Translated now date: " + dateTimeformatter.format(translatedNowDateWithTime));
        } catch (Exception e) {
            logger.error("Error in date check.", e);
        }
        // round is officially over?
        if (nextRoundDate != null && translatedNowDateWithTime != null) {
            // the end of the round has not been reached on the calendar
            boolean result = nextRoundDate.isBefore(translatedNowDateWithTime);
            if (result) {
                logger.info(dateTimeformatter.format(nextRoundDate) + " is before " + dateTimeformatter.format(translatedNowDateWithTime));
            } else {
                logger.info(dateTimeformatter.format(nextRoundDate) + " is NOT before " + dateTimeformatter.format(translatedNowDateWithTime));
            }
            return result;
        } else {
            return false;
        }
    }

    public static Date translateRealDateToSeasonDate(Date date, Long seasonId) {
        SeasonDAO dao = SeasonDAO.getInstance();
        SeasonPOJO season = (SeasonPOJO) dao.findById(SeasonPOJO.class, seasonId);
        Date seasonStartDate = season.getStartDate();
        Long seasonStartDateRealYear = season.getStartDateRealYear();

        Calendar c = Calendar.getInstance();
        c.setTime(seasonStartDate);
        int seasonStartYear = c.get(Calendar.YEAR);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	    int diff = (int) Math.round((seasonStartYear - currentYear + (currentYear - seasonStartDateRealYear)) * 365.243); // Days in year + leap year factor

	    logger.info("Calculated diff in days from today to fictional round date: " + (seasonStartYear - currentYear + (currentYear - seasonStartDateRealYear)) * 365.243);
	    logger.info("Calculated diff in days from today to fictional round date (rounded): " + Math.round((seasonStartYear - currentYear + (currentYear - seasonStartDateRealYear)) * 365.243));
	    logger.info("Calculated diff in days from today to fictional round date (abs): " + Math.abs((seasonStartYear - currentYear + (currentYear - seasonStartDateRealYear)) * 365.243));

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
        logger.info("**********************************************************************************************");
        logger.info("Checking on end of round.");
        ArrayList<JumpshipPOJO> jumpshipList = JumpshipDAO.getInstance().getAllJumpships();
        ArrayList<AttackPOJO> openAttacksInRoundList = new ArrayList<>();
        ArrayList<AttackPOJO> allAttacksForRound = AttackDAO.getInstance().getAllAttacksOfASeasonForRound(seasonId, round);

        for (AttackPOJO attackPojoDummy : allAttacksForRound) {
            if (attackPojoDummy.getFactionID_Winner() == null) {
                openAttacksInRoundList.add(attackPojoDummy);
            }
        }

//		try {
//			DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
//			logger.info("Current date: " + new Date(System.currentTimeMillis()));
//			logger.info("Translated current date: " + translateRealDateToSeasonTime(new Date(System.currentTimeMillis()), 1L));
//			logger.info("Current round date: " + dateTimeformatter.format(getCurrentRoundDate(seasonId)));
//			logger.info("Next round date: " + dateTimeformatter.format(getNextRoundDate(seasonId)));
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			logger.error("Date conversion error.", e);
//		}

        boolean jumpshipsLeftToMove = false;
        int jscount = 0;
		int jsmovedcount = 0;

        for (JumpshipPOJO js : jumpshipList) {
            if (js.getAttackReady()) {
                jumpshipsLeftToMove = true;
                jscount++;
            } else {
				jsmovedcount++;
            }
        }

        boolean attacksLeftToResolveInRound = openAttacksInRoundList.size() > 0; // We are ignoring the open attacks for the next round here

        StringBuilder foughtAttacks = new StringBuilder();
        StringBuilder resolvedAttacks = new StringBuilder();
        StringBuilder movedJumpships = new StringBuilder();

        // ---------------------------------------------------------------------------------
        // Uncomment this only, if you need the end to be finalized anyway for local tests!
        // forceFinalize.set(true);
        // ---------------------------------------------------------------------------------

        logger.info("Checking if current round needs to be finalized.");
        if ((jumpshipsLeftToMove || attacksLeftToResolveInRound) && !(timeForThisRoundIsOver(seasonId)) && !forceFinalize.get()) {
            // round is still active
            logger.info("Round is still active:");
			logger.info("--- " + jsmovedcount + " jumpship(s) have moved.");
            logger.info("--- " + jscount + " jumpship(s) have not moved.");
            logger.info("--- " + openAttacksInRoundList.size() + " attacks still to be resolved.");
            logger.info("--- There is still time left to make moves for this round!");
        } else {
            boolean roundFinalizedByAdmin = forceFinalize.get();
            logger.info("Finalizing the round:");
            logger.info("Admin finalized this round: " + roundFinalizedByAdmin);
            forceFinalize.set(false);

			AnimatedGifCreator.createSeasonHistoryAnimation(seasonId);

            EntityTransaction transaction = EntityManagerHelper.getEntityManager(ServerNexus.END_ROUND_USERID).getTransaction();
            String exceptionWhileSaving = "";
            Long newRound = null;
            GameState endRoundInfo = new GameState(GAMESTATEMODES.FINALIZE_ROUND);
            ArrayList<String> factionStatistics = new ArrayList<>();

            try {
                transaction.begin();
                EntityManagerHelper.clear(ServerNexus.END_ROUND_USERID);
                // here is no ship left to move AND no attack left open OR the time for the round is up
                logger.info("--- There is NO time left for this round!");

                // set all open attacks to resolved (decide on a winner in the process!)
                ForumDatabaseTools databaseTools = new ForumDatabaseTools();
                logger.info("--- Resolved attacks that were still open:");
                for (AttackPOJO attackPOJO : openAttacksInRoundList) {
                    Long winnerId = findAWinner(attackPOJO);

                    JumpshipPOJO jsPojo = JumpshipDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, attackPOJO.getJumpshipID());
                    StarSystemPOJO ssPojo = StarSystemDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, attackPOJO.getStarSystemID());
                    FactionPOJO fWinnerPojo = FactionDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, winnerId);
                    FactionPOJO fJumpshipPOJO = FactionDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, jsPojo.getJumpshipFactionID());

                    resolvedAttacks.append("Jumpship '").append(jsPojo.getJumpshipName()).append("' (").append(jsPojo.getId()).append(") of ").append(fJumpshipPOJO.getShortName()).append(" (").append(fJumpshipPOJO.getId()).append(") attacked system '").append(ssPojo.getName()).append("' (").append(ssPojo.getId()).append(") ").append("--> resolved to winner: ").append(fWinnerPojo.getShortName() + " (" + attackPOJO.getFactionID_Winner() + ").").append("\r\n");

                    // Enter final post into attack thread in Forum
                    Long attackId = attackPOJO.getId();
                    String starsystemName = ssPojo.getName();
                    String attacker = fJumpshipPOJO.getShortName();
                    String winner = fWinnerPojo.getShortName();
                    databaseTools.createFinalizingEntryForAttack(attackId, seasonId, (long) round, starsystemName, attacker, winner, true);
                }
                openAttacksInRoundList.clear();

                // Delete all ticker entries for the round that was finalized
                databaseTools.clearTickerEntriesForRound((long) round);
                databaseTools.clearTickerEntriesForRound((long) round - 1);
                databaseTools.clearTickerEntriesForRound((long) round - 2);

                // list all attacks that were fought in this round
                logger.info("--- Attacks that have been fought in this round:");
                for (AttackPOJO attackPOJO : allAttacksForRound) {

                    RolePlayCharacterPOJO rpcPojo = RolePlayCharacterDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, attackPOJO.getCharacterID());
                    String rank = rpcPojo.getRank();
                    String name = rpcPojo.getName();
                    String rpCharacter = (rank != null ? rank + " " : "") + name;

                    logger.info("--- Attack: " + attackPOJO.getId() + "(StarSystem: " + attackPOJO.getStarSystemID() + " / Authorized by " + rpCharacter + ")");

                    Long winnerId = attackPOJO.getFactionID_Winner();

                    JumpshipPOJO jsPojo = JumpshipDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, attackPOJO.getJumpshipID());
                    StarSystemPOJO ssPojo = StarSystemDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, attackPOJO.getStarSystemID());
                    FactionPOJO fWinnerPojo = FactionDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, winnerId);
                    FactionPOJO fJumpshipPOJO = FactionDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, jsPojo.getJumpshipFactionID());

                    //				logger.info("1");
                    // Get statistics for this attack
                    ArrayList<AttackStatsPOJO> statisticsList = AttackStatsDAO.getInstance().getStatisticsForAttack(seasonId, attackPOJO.getId());
                    //				logger.info("2");
                    if (!statisticsList.isEmpty()) {
                        // log stats
                        logger.info("--- Statistics found for attackId: " + attackPOJO.getId());
                        //StringBuilder repairCostReport = new StringBuilder();
                        //StringBuilder xpReport = new StringBuilder();
                        //long balanceAttacker = 0L;
                        //long balanceDefender = 0L;
                        //DecimalFormat nf = new DecimalFormat();

                        GenerateRoundReport report = new GenerateRoundReport(attackPOJO);
                        long defenderSystemCost = 0L, defenderMechCost = 0L, defenderRewardFromMatch = 0L,
                                attackerSystemCost = 0L, attackerMechCost = 0L, attackerRewardFromMatch = 0L;
                        StatsEconomyDAO statsEconomyDAO = StatsEconomyDAO.getInstance();
                        StatsEconomyPOJO statsEconomyAttackerPOJO = new StatsEconomyPOJO();
                        StatsEconomyPOJO statsEconomyDefenderPOJO = new StatsEconomyPOJO();


                        for (AttackStatsPOJO asp : statisticsList) {

                            CalcXP calcXP = new CalcXP(asp, report);
                            CalcBalance calcB = new CalcBalance(asp.getMwoMatchId(), report);
                            long curAttackerCost = 0, curAttackerIncome = 0, curDefenderCost = 0, curDefenderIncome = 0;

                            if (statsEconomyAttackerPOJO.getFactionID() == null) {
                                statsEconomyAttackerPOJO.setFactionID(asp.getAttackerFactionId());
                                statsEconomyDefenderPOJO.setFactionID(asp.getDefenderFactionId());

                                //attackerIncome = attackerIncome + calcB.getIncome(asp.getAttackerFactionId(), asp.getStarSystemDataId());
                                attackerSystemCost = attackerSystemCost + calcB.getAttackCost(asp.getStarSystemDataId());

                                //defenderIncome = defenderIncome + calcB.getIncome(asp.getDefenderFactionId(), asp.getStarSystemDataId());
                                defenderSystemCost = defenderSystemCost + calcB.getDefendCost(asp.getStarSystemDataId());
                            }
                            for (BalanceUserInfo attackerPlayerInfo : calcB.getAttackerInfo()) {
                                curAttackerCost = curAttackerCost + attackerPlayerInfo.mechRepairCost + attackerPlayerInfo.playerTeamDamage;
                                curAttackerIncome = curAttackerIncome + attackerPlayerInfo.rewardAssist + attackerPlayerInfo.rewardDamage
                                        + attackerPlayerInfo.rewardKill + attackerPlayerInfo.rewardMatchScore + attackerPlayerInfo.rewardComponentsDestroyed
                                        + attackerPlayerInfo.rewardLossVictory;
                            }

                            //Einkommen und Ausgaben zuerst auf einem Spieler runter rechen und danach auf 12 Spieler hochrechnen.
                            attackerMechCost = attackerMechCost + ((curAttackerCost / asp.getAttackerNumberOfPilots()) * 12);
                            attackerRewardFromMatch = attackerRewardFromMatch + ((curAttackerIncome / asp.getAttackerNumberOfPilots()) * 12);

                            for (BalanceUserInfo defenderPlayerInfo : calcB.getDefenderInfo()) {
                                curDefenderCost = curDefenderCost + defenderPlayerInfo.mechRepairCost + defenderPlayerInfo.playerTeamDamage;
                                curDefenderIncome = curDefenderIncome + defenderPlayerInfo.rewardAssist + defenderPlayerInfo.rewardDamage
                                        + defenderPlayerInfo.rewardKill + defenderPlayerInfo.rewardMatchScore + defenderPlayerInfo.rewardComponentsDestroyed
                                        + defenderPlayerInfo.rewardLossVictory;
                            }
                            //Einkommen und Ausgaben zuerst auf einem Spieler runter rechen und danach auf 12 Spieler hochrechnen.
                            defenderMechCost = defenderMechCost + ((curDefenderCost / asp.getDefenderNumberOfPilots()) * 12);
                            defenderRewardFromMatch = defenderRewardFromMatch + ((curDefenderIncome / asp.getDefenderNumberOfPilots()) * 12);
                        }
                        //Update Attack Table
                        AttackDAO attackDAO = AttackDAO.getInstance();

                        attackPOJO.setAttackCostDefender(defenderMechCost + defenderRewardFromMatch);
                        attackPOJO.setAttackCostAttacker(attackerMechCost + attackerRewardFromMatch);

                        attackDAO.update(ServerNexus.END_ROUND_USERID, attackPOJO);

                        //Update Economy Table
                        FactionInfo defInfo = new FactionInfo(statsEconomyDefenderPOJO.getFactionID()),
                                attInfo = new FactionInfo(statsEconomyAttackerPOJO.getFactionID());

                        statsEconomyAttackerPOJO.setSeason(attackPOJO.getSeason());
                        statsEconomyAttackerPOJO.setRound(attackPOJO.getRound());
                        statsEconomyAttackerPOJO.setCost(attInfo.getSystemCost(attackPOJO.getStarSystemDataID()));
                        statsEconomyAttackerPOJO.setIncome(attInfo.getSystemIncome(attackPOJO.getStarSystemDataID()));
                        statsEconomyAttackerPOJO.setAttackID(attackPOJO.getId());
                        statsEconomyAttackerPOJO.setStarSystemDataID(attackPOJO.getStarSystemDataID());
                        statsEconomyAttackerPOJO.setCostEnhancement(0L);

                        statsEconomyDefenderPOJO.setSeason(attackPOJO.getSeason());
                        statsEconomyDefenderPOJO.setRound(attackPOJO.getRound());
                        statsEconomyDefenderPOJO.setCost(defInfo.getSystemCost(attackPOJO.getStarSystemDataID()));
                        statsEconomyDefenderPOJO.setIncome(defInfo.getSystemIncome(attackPOJO.getStarSystemDataID()));
                        statsEconomyDefenderPOJO.setAttackID(attackPOJO.getId());
                        statsEconomyDefenderPOJO.setStarSystemDataID(attackPOJO.getStarSystemDataID());
                        statsEconomyDefenderPOJO.setCostEnhancement(0L);

                        statsEconomyDAO.update(ServerNexus.END_ROUND_USERID, statsEconomyAttackerPOJO);
                        statsEconomyDAO.update(ServerNexus.END_ROUND_USERID, statsEconomyDefenderPOJO);
                        report.saveReport();
						/*String[] receivers = {"keshik@googlegroups.com"};
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
						}*/
                    } else {
                        // no statistics found
                        logger.info("--- no statistics found for attackId: " + attackPOJO.getId());
                    }
                }

                // Count the round indicator up once
                newRound = (long) (round + 1);
                logger.info("--- Finally increase the round indicator to: " + newRound);
                RoundPOJO roundPOJO = RoundDAO.getInstance().findBySeasonId(seasonId);
                roundPOJO.setRound(newRound);

				ServerNexus.getEci().sendExtCom("Round finalized.", "en", true, true, true);
				ServerNexus.getEci().sendExtCom("Runde beendet.", "de", true, true, true);

	            // Jumpships do not need to be moved, because the waypoints have a round indicator
	            // But it needs to be checked if a jumpship would end up on a system where already a jumpship is
	            // In this case, do not jump and abort the route (delete the waypoints?)
	            logger.info("--- Processing jumpships.");
//	            ArrayList<RoutePointPOJO> allRoutepoints = RoutePointDAO.getInstance().getAllRoutePointsForSeasonForRound(seasonId, newRound.intValue());
//				for (RoutePointPOJO routePoint : allRoutepoints) { // all routepoints for this season and the new round
//				}

                //remove not needed diplomacy entries
                logger.info("--- Remove not needed diplomacy entries!");
                DiplomacyDAO.getInstance().deleteEntrieForRound(ServerNexus.END_ROUND_USERID,GameServer.getCurrentSeason(), roundPOJO.getRound());

                logger.info("--- Remove unanswered requests!");
                DiplomacyDAO.getInstance().removeAllRequestAfterWaitingPeriod(ServerNexus.END_ROUND_USERID, GameServer.getCurrentSeason(), roundPOJO.getRound());

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

                            String rpCharacter = "-- no data --";
                            if (p.getCharacterID() != null) {
                                RolePlayCharacterPOJO rpcPojo = RolePlayCharacterDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, p.getCharacterID());
                                String rank = rpcPojo.getRank();
                                String name = rpcPojo.getName();
                                rpCharacter = (rank != null ? rank + " " : "") + name;
                            }

                            StarSystemPOJO ssPojo = StarSystemDAO.getInstance().findById(ServerNexus.END_ROUND_USERID, p.getSystemId());
                            movedJumpships.append("Jumpship '").append(js.getJumpshipName()).append("' moved to ").append(ssPojo.getName()).append(" (").append(ssPojo.getId()).append(" / Authorized by ").append(rpCharacter).append(").\r\n");
                        }
                    }
                }

                Timestamp realNow = new Timestamp(System.currentTimeMillis());
                Date d = translateRealDateToSeasonDate(new Date(System.currentTimeMillis()), seasonId);
                LocalDate localDate = new java.util.Date(d.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.HOURS);
                LocalDateTime translatedNowDateWithTime = LocalDateTime.of(localDate, now);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ServerNexus.patternTimestamp);
                Timestamp c3Now = Timestamp.valueOf(formatter.format(translatedNowDateWithTime));

                String realNowString = new SimpleDateFormat(ServerNexus.patternTimestamp).format(realNow);
                String c3NowString = new SimpleDateFormat(ServerNexus.patternTimestamp).format(c3Now);

                logger.info("Timestamp c3Now: " + c3Now);
                logger.info("Translated Now date with time: " + translatedNowDateWithTime);
                logger.info("Now date with time RealTime: " + realNowString);
                logger.info("Formatted now c3NowString: " + c3NowString);

                roundPOJO.setCurrentRoundStartDateRealTime(realNowString);
                roundPOJO.setCurrentRoundStartDate(c3NowString);

                // Save everything to the database
                AttackDAO attackDAO = AttackDAO.getInstance();
                JumpshipDAO jumpshipDAO = JumpshipDAO.getInstance();
                RoundDAO roundDAO = RoundDAO.getInstance();
                StarSystemDataDAO ssdDAO = StarSystemDataDAO.getInstance();
                FactionDAO fDAO = FactionDAO.getInstance();

                roundDAO.update(ServerNexus.END_ROUND_USERID, roundPOJO);
                for (JumpshipPOJO jumpshipPOJO : jumpshipList) {
                    // jumpshipDAO.refresh(Nexus.END_ROUND, jumpshipPOJO);
                    jumpshipDAO.update(ServerNexus.END_ROUND_USERID, jumpshipPOJO);
                }
                //for (AttackPOJO attackPOJO : openAttacksInRoundList) {
                //	attackDAO.update(Nexus.END_ROUND, attackPOJO);
                //}
                for (AttackPOJO attackPOJO : allAttacksForRound) {
                    Long winnerId = attackPOJO.getFactionID_Winner();

                    StarSystemDataPOJO ssdPojo = ssdDAO.findById(ServerNexus.END_ROUND_USERID, attackPOJO.getStarSystemDataID());
                    FactionPOJO fPojo = fDAO.findById(ServerNexus.END_ROUND_USERID, winnerId);
                    ssdPojo.setFactionID(fPojo);
                    logger.info("**** Storing winner for attack " + attackPOJO.getId() + " to be " + winnerId + ".");

                    ssdDAO.update(ServerNexus.END_ROUND_USERID, ssdPojo);
                    attackDAO.update(ServerNexus.END_ROUND_USERID, attackPOJO);
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
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Finalize round", e);
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
            message.append("Current date is: ").append(getCurrentRoundDate(seasonId)).append(" ");
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

        // Create and send the new version of the universe to all clients that are currently open
        ServerNexus.sendUniverseToClients = true;

	    return foughtAttacks + "\r\n" + resolvedAttacks + "\r\n" + movedJumpships;
    }

//	public static void main(String[] args) {
//		ArrayList<AttackStatsPOJO> statisticsList = AttackStatsDAO.getInstance().getStatisticsForAttack(1L, 92L);
//
//		if (!statisticsList.isEmpty()) {
//			// log stats
//			for (AttackStatsPOJO asp : statisticsList) {
//				CalcBalance calcB = new CalcBalance(asp);
//				System.out.println(calcB.getMailMessage().toString());
//			}
//		}
//	}

//		public static void main(String[] args) {
////			// 3052 - 2021 = 1031 Jahre Differenz
////			// 1031 Jahre * 365,25 Tage = 376.572,75 Tage Differenz
////			int currentYear = 2021;
////			Long season = 1L;
////			int round = 1;
////
////			int seasonStartYear = 3060;
////			int diff = (int) Math.abs((seasonStartYear - currentYear) * 365.243); // Days in year and Schaltjahr factor
////
////			System.out.println("Current date: " + new Date(System.currentTimeMillis()));
////			System.out.println("Translated: " + addDaysToDate(new Date(System.currentTimeMillis()), diff));
////			// Current date: 2021-05-06
////			// Translated:   3052-05-06
//
//
//			Date date = new Date(System.currentTimeMillis());
//			//Date date = Date.valueOf("2022-12-30");
//			Date seasonStartDate = Date.valueOf("3060-01-01");
//			Integer seasonStartDateRealYear = 2022;
//
//			Calendar c = Calendar.getInstance();
//			c.setTime(seasonStartDate);
//			int seasonStartYear = c.get(Calendar.YEAR);
//			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//			int diff = (int) Math.abs((seasonStartYear - currentYear + (currentYear - seasonStartDateRealYear)) * 365.243); // Days in year + leap year factor
//
//			Date result = addDaysToDate(date, diff);
//
//			System.out.println(result);
//		}
}
