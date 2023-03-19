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

import com.google.gson.Gson;
import net.clanwolf.starmap.server.nexus2.Nexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.C3GameConfigDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.FactionDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayCharacterDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StatsMwoDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackStatsPOJO;
import net.clanwolf.starmap.server.persistence.pojos.FactionPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayCharacterPOJO;
import net.clanwolf.starmap.server.persistence.pojos.StatsMwoPOJO;
import net.clanwolf.starmap.server.reporting.GenerateRoundReport;
import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import net.clanwolf.starmap.transfer.mwo.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;

/**
 * Diese Klasse berechnet die XP
 *
 * @author KERNREAKTOR
 * @version 1.0.2
 */
public class CalcXP {
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TextFormattingHelper tfh = new TextFormattingHelper();

    private String userName;
    private Long userXPVictoryLoss;
    private Long userXPComponentsDestroyed;
    private String descriptionComponentDestroyed;
    private Long userXPMatchScore;
    private String descriptionMatchScore;
    private Long userXPDamage;
    private String descriptionDamage;

    public String getDescriptionComponentDestroyed() {
        descriptionComponentDestroyed = C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_COMPONENT_DESTROYED").getValue() * userDetail.getComponentsDestroyed() +
                " XP (" + getUserXPComponentsDestroyed() +
                " Component destroyed)";
        return descriptionComponentDestroyed;
    }

    public void setDescriptionComponentDestroyed(String descriptionComponentDestroyed) {
        this.descriptionComponentDestroyed = descriptionComponentDestroyed;
    }

    public String getDescriptionMatchScore() {
        descriptionMatchScore = getUserXPMatchScore() + " XP (Match-score: " + userDetail.getMatchScore() + ")";
        return descriptionMatchScore;
    }

    public void setDescriptionMatchScore(String descriptionMatchScore) {
        this.descriptionMatchScore = descriptionMatchScore;
    }

    public String getDescriptionDamage() {
        descriptionDamage = getUserXPDamage() + " XP (Damage: " + userDetail.getDamage() + ")";
        return descriptionDamage;
    }

    public void setDescriptionDamage(String descriptionDamage) {
        this.descriptionDamage = descriptionDamage;
    }

    private UserDetail userDetail;
    private MWOMatchResult matchResult;
    private Long userXPBeforeCalc;
    private Long userXPCurrent = 0L;
    private Long userXPTotal;
    private RolePlayCharacterPOJO currentCharacter;

    public Long getUserXPCurrent() {
        return userXPCurrent;
    }

    public void setUserXPCurrent(Long userXPCurrent) {
        this.userXPCurrent = userXPCurrent;
    }

    public Long getUserXPTotal() {
        userXPTotal = userXPCurrent + userXPBeforeCalc;
        return userXPTotal;
    }

    public void setUserXPTotal(Long userXPTotal) {
        this.userXPTotal = userXPTotal;
    }

    public Long getUserXPBeforeCalc() {
        if (currentCharacter.getXp() == null) {
            userXPBeforeCalc = 0L;
        } else {
            userXPBeforeCalc = Long.valueOf(currentCharacter.getXp());
        }
        return userXPBeforeCalc;
    }

    public void setUserXPBeforeCalc(Long userXPBeforeCalc) {
        this.userXPBeforeCalc = userXPBeforeCalc;
    }

    public String getUserName() {
        userName = userDetail.getUsername();
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Berechnet die XP der jeweiligen Spieler
     *
     * @param attackStats Die AttackStatsPOJO
     */
    public CalcXP(AttackStatsPOJO attackStats, GenerateRoundReport report) throws Exception {
        String mwoMatchID = attackStats.getMwoMatchId();
        StatsMwoDAO statsMwoDAO = net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StatsMwoDAO.getInstance();
        StatsMwoPOJO statsMwoPOJO = statsMwoDAO.findByMWOGameId(mwoMatchID);
        String rawJSONstatsData = statsMwoPOJO.getRawData();
        MWOMatchResult matchDetails = new Gson().fromJson(rawJSONstatsData, MWOMatchResult.class);

        RolePlayCharacterDAO characterDAO = RolePlayCharacterDAO.getInstance();
        ArrayList<RolePlayCharacterPOJO> allCharacter = characterDAO.getAllCharacter();

        FactionPOJO factionAttacker = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getAttackerFactionId());
        FactionPOJO factionDefender = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getDefenderFactionId());

        logger.info("--- Calculate the XP [" + factionAttacker.getShortName() + "] versus [" + factionDefender.getShortName() + "] (MatchID: " + mwoMatchID + " )---");

        report.addGameInfo(attackStats, matchDetails);

        boolean bFound;
        long currentUserXP;

        for (UserDetail userDetail : matchDetails.getUserDetails()) {

            bFound = false;
            currentUserXP = 0L;

            for (RolePlayCharacterPOJO currentCharacter : allCharacter) {
                if (bFound) {
                    break;
                } else {
                    if (currentCharacter.getMwoUsername() != null) {
                        if (userDetail.getUsername().equals(currentCharacter.getMwoUsername())) {

                            bFound = true;

                            if (userDetail.getTeam() == null) {

                                //Spieler befindet sich im Spectator
                                logger.info(userDetail.getUsername() + " was there as a spectator and does not get XP.");
                                report.addXPWarning(userDetail.getUsername() + " was there as a spectator and does not get XP.");

                            } else {

                                report.addXPForTeam(userDetail, matchDetails, currentCharacter);

                                if (matchDetails.getMatchDetails().getWinningTeam() != null) {
                                    if (matchDetails.getMatchDetails().getWinningTeam().equals(userDetail.getTeam())) {

                                        //Spieler befindet sich im Gewinner Team
                                        currentUserXP = currentUserXP + C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_VICTORY").getValue();
                                    } else {

                                        //Spieler befindet sich im Verlierer Team
                                        currentUserXP = currentUserXP + C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_LOSS").getValue();
                                    }
                                } else {
                                    currentUserXP = currentUserXP + C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_TIE").getValue();
                                }

                                currentUserXP = currentUserXP + C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_COMPONENT_DESTROYED").getValue() * userDetail.getComponentsDestroyed();
                                currentUserXP = currentUserXP + CalcRange(userDetail.getMatchScore().longValue(), C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_EACH_MATCH_SCORE_RANGE").getValue()) *
                                        C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_EACH_MATCH_SCORE").getValue();
                                currentUserXP = currentUserXP + CalcRange(userDetail.getDamage().longValue(), C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_EACH_DAMAGE_RANGE").getValue());

                                if (currentCharacter.getXp() != null) {
                                    // ****************************************************************************************
                                    // Klasse nur für Testzwecke eingebaut
                                    // UserXPInfo calcLevel = new UserXPInfo(currentCharacter.getXp());
                                    // logger.info(currentCharacter.getName() + " current xp: " + currentCharacter.getXp() + " current Level: " + calcLevel.getPlayerLevel());
                                    //*****************************************************************************************
                                    currentUserXP = currentUserXP + currentCharacter.getXp();

                                }

                                currentCharacter.setXp((int) currentUserXP);
                                characterDAO.update(Nexus.END_ROUND_USERID, currentCharacter);

                            }
                        }
                    }
                }
            }
            if (!bFound) {

                logger.info("User " + userDetail.getUsername() + " does not receive XP because his MWO username was not found in the C3 database.");
                report.addXPWarning("User " + userDetail.getUsername() + " does not receive XP because his MWO username was not found in the C3 database.");

            }
        }
        report.finishXPReport();
        logger.info("✅ XP calculating finished");
    }

    public void setUserXPVictoryLoss(Long userXPVictoryLoss) {
        this.userXPVictoryLoss = userXPVictoryLoss;
    }

    public Long getUserXPComponentsDestroyed() {
        userXPComponentsDestroyed = C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_COMPONENT_DESTROYED").getValue() * userDetail.getComponentsDestroyed();
        userXPCurrent = userXPCurrent + userXPComponentsDestroyed;
        return userXPComponentsDestroyed;
    }

    public void setUserXPComponentsDestroyed(Long userXPComponentsDestroyed) {
        this.userXPComponentsDestroyed = userXPComponentsDestroyed;
    }

    public Long getUserXPMatchScore() {
        userXPMatchScore = CalcRange(userDetail.getMatchScore().longValue(), C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_EACH_MATCH_SCORE_RANGE").getValue()) *
                C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_EACH_MATCH_SCORE").getValue();
        userXPCurrent = userXPCurrent + userXPMatchScore;
        return userXPMatchScore;
    }

    public void setUserXPMatchScore(Long userXPMatchScore) {
        this.userXPMatchScore = userXPMatchScore;
    }

    public Long getUserXPDamage() {
        userXPDamage = CalcRange(userDetail.getDamage().longValue(), C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_EACH_DAMAGE_RANGE").getValue());
        userXPCurrent = userXPCurrent + userXPDamage;
        return userXPDamage;
    }

    public void setUserXPDamage(Long userXPDamage) {
        this.userXPDamage = userXPDamage;
    }

    /**
     * Erstellt einen Bericht, die als Nachricht versendet werden kann.
     *
     * @return Der (StringBuilder) Bericht wird zurückgegeben.
     */
    public StringBuilder getMailMessage() {
        return tfh.getMailMessage();
    }

    /**
     * Berechnet, wie viele zahlen in eine Zahl reinpassen.
     *
     * @param value Die gesamte Zahl.
     * @param range Die Konstante, deren Wert RANGE enthält.
     * @return Gibt den Wert zurück, wie viele Zahlen reinpassen.
     */
    public Long CalcRange(Long value, Long range) {

        Long rest = value % range;

        return (value - rest) / range;
    }

    public CalcXP(UserDetail userDetail, MWOMatchResult matchResult, RolePlayCharacterPOJO currentCharacter) {
        this.userDetail = userDetail;
        this.matchResult = matchResult;
        this.currentCharacter = currentCharacter;
    }

    public Long getUserXPVictoryLoss() {
        if (this.matchResult.getMatchDetails().getWinningTeam() != null) {
            if (this.matchResult.getMatchDetails().getWinningTeam().equals(userDetail.getTeam())) {
                //Spieler befindet sich im Gewinner Team
                userXPVictoryLoss = C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_VICTORY").getValue();
            } else {
                //Spieler befindet sich im Verlierer Team
                userXPVictoryLoss = C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_LOSS").getValue();
            }
        } else {
            userXPVictoryLoss = C3GameConfigDAO.getInstance().findByKey(Nexus.END_ROUND_USERID, "C3_XP_REWARD_TIE").getValue();
        }
        userXPCurrent = userXPCurrent + userXPVictoryLoss;
        return userXPVictoryLoss;
    }
}
