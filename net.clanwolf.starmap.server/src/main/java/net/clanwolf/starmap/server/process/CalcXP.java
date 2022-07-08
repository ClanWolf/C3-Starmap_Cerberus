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

import com.google.gson.Gson;
import net.clanwolf.starmap.server.Nexus.Nexus;

import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.FactionDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayCharacterDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StatsMwoDAO;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import net.clanwolf.starmap.transfer.mwo.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.invoke.MethodHandles;

import java.util.ArrayList;


import static net.clanwolf.starmap.constants.Constants.*;

/**
 * Diese Klasse berechnet die XP
 *
 * @author KERNREAKTOR
 * @version 1.0.1
 */
public class CalcXP {
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TextFormattingHelper tfh = new TextFormattingHelper();

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

    /**
     * Berechnet die XP der jeweiligen Spieler
     *
     * @param attackStats Die AttackStatsPOJO
     */
    public CalcXP(AttackStatsPOJO attackStats) throws RuntimeException {
        String mwoMatchID = attackStats.getMwoMatchId();
        StatsMwoDAO statsMwoDAO = net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StatsMwoDAO.getInstance();
        StatsMwoPOJO statsMwoPOJO = statsMwoDAO.findByMWOGameId(mwoMatchID);
        String rawJSONstatsData = statsMwoPOJO.getRawData();
        MWOMatchResult matchDetails = new Gson().fromJson(rawJSONstatsData, MWOMatchResult.class);

        RolePlayCharacterDAO characterDAO = RolePlayCharacterDAO.getInstance();
        ArrayList<RolePlayCharacterPOJO> allCharacter = characterDAO.getAllCharacter();

        FactionPOJO factionAttacker = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getAttackerFactionId());
        FactionPOJO factionDefender = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getDefenderFactionId());

        String columnWidthDefault = "%-20.20s";
        logger.info("--- Calculate the XP [" + factionAttacker.getShortName() + "] versus [" + factionDefender.getShortName() + "] (MatchID: " + mwoMatchID + " )---");

        tfh.startXPReport();
        tfh.addGameInfo(attackStats);

        boolean bFound;
        long currentUserXP;

        /*EntityTransaction transaction = EntityManagerHelper.getEntityManager(Nexus.DUMMY_USERID).getTransaction();
        try {
            transaction.begin();
            EntityManagerHelper.clear(Nexus.DUMMY_USERID);*/

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
                                logger.error(userDetail.getUsername() + " was there as a spectator and does not get XP.");
                                tfh.addAppendText(userDetail.getUsername() + " was there as a spectator and does not get XP.");

                            } else {

                                tfh.addAppendText("XP distribution for the user " + userDetail.getUsername());
                                if (matchDetails.getMatchDetails().getWinningTeam().equals(userDetail.getTeam())) {

                                    //Spieler befindet sich im Gewinner Team
                                    tfh.addTwoColumnsText("Victory", XP_REWARD_VICTORY + " XP");
                                    currentUserXP = currentUserXP + XP_REWARD_VICTORY;

                                } else {

                                    //Spieler befindet sich im Verlierer Team
                                    tfh.addTwoColumnsText("Loss", XP_REWARD_LOSS + " XP");
                                    currentUserXP = currentUserXP + XP_REWARD_LOSS;

                                }

                                tfh.addXPComponentDestroyed(userDetail.getComponentsDestroyed());
                                currentUserXP = currentUserXP + XP_REWARD_COMPONENT_DESTROYED * userDetail.getComponentsDestroyed();

                                tfh.addXPMatchScore(userDetail.getMatchScore());
                                currentUserXP = currentUserXP + CalcRange(userDetail.getMatchScore().longValue(), XP_REWARD_EACH_MATCH_SCORE_RANGE) * XP_REWARD_EACH_MATCH_SCORE;

                                tfh.addXPDamage(userDetail.getDamage());
                                currentUserXP = currentUserXP + CalcRange(userDetail.getDamage().longValue(), XP_REWARD_EACH_DAMAGE_RANGE);

                                tfh.addTwoColumnsText("XP total:", currentUserXP + " XP\r\n");

                                if (currentCharacter.getXp() != null) {

                                    currentUserXP = currentUserXP + currentCharacter.getXp();

                                }

                                currentCharacter.setXp((int) currentUserXP);
                                characterDAO.update(Nexus.END_ROUND, currentCharacter);

                            }
                        }
                    }
                }
            }

            if (!bFound) {

                logger.info("User " + userDetail.getUsername() + " does not receive XP because his MWO username was not found in the C3 database.");
                tfh.addAppendText("User " + userDetail.getUsername() + " does not receive XP because his MWO username was not found in the C3 database.\r\n");

            }
        }
            /*transaction.commit();

        } catch (RuntimeException re) {
            logger.error(re.getMessage());
            transaction.rollback();
        }*/
        tfh.addAppendText("---End from the report of XP distribution---");
    }
}
