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
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.FactionDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayCharacterDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StatsMwoDAO;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import net.clanwolf.starmap.transfer.mwo.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityTransaction;
import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static net.clanwolf.starmap.constants.Constants.*;
/**
 * Diese Klasse berechnet die XP
 *
 * @author KERNREAKTOR
 * @version 1.0.0
 */
public class CalcXP {
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final StringBuilder mailMessage = new StringBuilder();
    /**
     * Erstellt einen Bericht, die als Nachricht versendet werden kann.
     * @return Der (StringBuilder) Bericht wird zurückgegeben.
     */
    public StringBuilder getMailMessage() {
        return mailMessage;
    }

    /**
     * Berechnet, wie viele zahlen in eine Zahl reinpassen.
     * @param value
     * @param range
     * @return Gibt den Wert zurück, wie viele Zahlen reinpassen.
     */
    public Long CalcRange(Long value, Long range){

        Long rest = value % range;

        return (value - rest) / range;
    }

    /**
     * Berechnet die XP der jeweiligen Spieler
     * @param attackStats
     */
    public   CalcXP(AttackStatsPOJO attackStats){
        String mwoMatchID = attackStats.getMwoMatchId();
        StatsMwoDAO statsMwoDAO = net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StatsMwoDAO.getInstance();
        StatsMwoPOJO statsMwoPOJO = statsMwoDAO.findByMWOGameId(mwoMatchID);
        String rawJSONstatsData = statsMwoPOJO.getRawData();
        MWOMatchResult matchDetails = new Gson().fromJson(rawJSONstatsData, MWOMatchResult.class);

        RolePlayCharacterDAO characterDAO = RolePlayCharacterDAO.getInstance();
        ArrayList<RolePlayCharacterPOJO> allCharacter = characterDAO.getAllCharacter();

        FactionPOJO factionAttacker = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getAttackerFactionId());
        FactionPOJO factionDefender = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getDefenderFactionId());
        FactionPOJO factionWinner = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getWinnerFactionId());

        String columnWidthDefault = "%-20.20s";
        logger.info("--- Calculate the XP [" + factionAttacker.getShortName() + "] versus [" + factionDefender.getShortName() + "] (MatchID: " + mwoMatchID + " )---");

        mailMessage.append("---Begin from the report of XP distribution---").append("\r\n\r\n");
        mailMessage.append("Name of the faction that attacks: ");
        mailMessage.append(factionAttacker.getName_en());
        mailMessage.append("\r\n");
        mailMessage.append("Name of the faction that defends: ");
        mailMessage.append(factionDefender.getName_en());
        mailMessage.append("\r\n\r\n");

        mailMessage.append(String.format(columnWidthDefault, "Round Id: ")).append(attackStats.getRoundId()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Season Id: ")).append(attackStats.getSeasonId()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Attack Id: ")).append(attackStats.getAttackId()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Id: ")).append(attackStats.getId()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "MWO Match Id: ")).append(attackStats.getMwoMatchId()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Map: ")).append(attackStats.getMap()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Game mode: ")).append(attackStats.getMode()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Winner: ")).append(factionWinner.getName_en()).append("\r\n");

        try {

            String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ssXXX";
            DateFormat dateFormat = new SimpleDateFormat(isoDatePattern);
            Date parsedDate = dateFormat.parse(attackStats.getDropEnded());

            mailMessage.append(String.format(columnWidthDefault, "Drop ended: ")).append(parsedDate).append("\r\n\r\n");

        }
        catch(Exception e) {

            logger.error(e.getMessage());

        }

        boolean bFound;
        long currentUserXP;

        for (UserDetail userDetail : matchDetails.getUserDetails()) {

            bFound = false;
            currentUserXP =0L;

            for(RolePlayCharacterPOJO currentCharacter : allCharacter){
                if (bFound){
                    break;
                }else {
                    if(currentCharacter.getMwoUsername() !=null){
                        if(userDetail.getUsername().equals(currentCharacter.getMwoUsername())){

                            bFound = true;

                            if(userDetail.getTeam() == null){

                                //Spieler befindet sich im Spectator
                                logger.error(userDetail.getUsername() + " was there as a spectator and does not get XP.");
                                mailMessage.append(userDetail.getUsername()).append(" was there as a spectator and does not get XP.").append("\r\n\r\n");

                            } else {
                                mailMessage.append("XP distribution for the user ");
                                mailMessage.append(userDetail.getUsername()).append("\r\n");
                                if(matchDetails.getMatchDetails().getWinningTeam().equals(userDetail.getTeam())){

                                    //Spieler befindet sich im Gewinner Team
                                    mailMessage.append(String.format(columnWidthDefault, "Victory")).append(XP_REWARD_VICTORY).append(" XP").append("\r\n");
                                    currentUserXP = currentUserXP + XP_REWARD_VICTORY;

                                }else{

                                    //Spieler befindet sich im Verlierer Team
                                    mailMessage.append(String.format(columnWidthDefault, "Loss")).append(XP_REWARD_LOSS).append(" XP").append("\r\n");
                                    currentUserXP = currentUserXP + XP_REWARD_LOSS;

                                }

                                //
                                mailMessage.append(String.format(columnWidthDefault, "Component destroyed"));
                                mailMessage.append(XP_REWARD_COMPONENT_DESTROYED * userDetail.getComponentsDestroyed());
                                mailMessage.append(" XP ");
                                mailMessage.append("(").append(XP_REWARD_COMPONENT_DESTROYED).append(" XP * ");
                                mailMessage.append(userDetail.getComponentsDestroyed()).append(" Component destroyed)").append("\r\n");
                                currentUserXP = currentUserXP + XP_REWARD_COMPONENT_DESTROYED * userDetail.getComponentsDestroyed();


                                mailMessage.append(String.format(columnWidthDefault, "Match score"));
                                mailMessage.append(CalcRange(userDetail.getMatchScore().longValue(),XP_REWARD_EACH_MATCH_SCORE_RANGE) * XP_REWARD_EACH_MATCH_SCORE);
                                mailMessage.append(" XP ");
                                mailMessage.append("(").append(XP_REWARD_EACH_MATCH_SCORE).append(" XP * ");
                                mailMessage.append(CalcRange(userDetail.getMatchScore().longValue(),XP_REWARD_EACH_MATCH_SCORE_RANGE));
                                mailMessage.append(" per reached ").append(XP_REWARD_EACH_MATCH_SCORE_RANGE);
                                mailMessage.append(" Match score");
                                mailMessage.append(" [User match score: ").append(userDetail.getMatchScore()).append("]) ").append("\r\n");
                                currentUserXP = currentUserXP +CalcRange(userDetail.getMatchScore().longValue(),XP_REWARD_EACH_MATCH_SCORE_RANGE) * XP_REWARD_EACH_MATCH_SCORE;

                                mailMessage.append(String.format(columnWidthDefault, "Damage"));
                                mailMessage.append(CalcRange(userDetail.getDamage().longValue(),XP_REWARD_EACH_DAMAGE_RANGE) * XP_REWARD_EACH_DAMAGE);
                                mailMessage.append(" XP ");
                                mailMessage.append("(").append(XP_REWARD_EACH_DAMAGE).append(" XP * ");
                                mailMessage.append(CalcRange(userDetail.getDamage().longValue(),XP_REWARD_EACH_DAMAGE_RANGE));
                                mailMessage.append(" per reached ").append(XP_REWARD_EACH_DAMAGE_RANGE);
                                mailMessage.append(" Damage");
                                mailMessage.append(" [User Damage: ").append(userDetail.getDamage()).append("]) ").append("\r\n");
                                currentUserXP = currentUserXP + CalcRange(userDetail.getDamage().longValue(),XP_REWARD_EACH_DAMAGE_RANGE);

                                mailMessage.append(String.format(columnWidthDefault, "XP total:"));
                                mailMessage.append(currentUserXP);
                                mailMessage.append(" XP ");
                                mailMessage.append("\r\n");

                                mailMessage.append("\r\n");

                                if(currentCharacter.getXp() != null){

                                    currentUserXP = currentUserXP + currentCharacter.getXp();

                                }

                                EntityTransaction transaction = EntityManagerHelper.getEntityManager(Nexus.DUMMY_USERID).getTransaction();
                                try {
                                    transaction.begin();
                                    EntityManagerHelper.clear(Nexus.DUMMY_USERID);

                                    currentCharacter.setXp((int) currentUserXP);
                                    characterDAO.update(Nexus.DUMMY_USERID,currentCharacter);

                                    transaction.commit();

                                }catch (RuntimeException re) {
                                    logger.error(re.getMessage());
                                }
                            }
                        }
                    }
                }
            }

            if (!bFound){

                logger.info("User " + userDetail.getUsername() + " does not receive XP because his MWO username was not found in the C3 database.");

            }
        }
        mailMessage.append("---End from the report of XP distribution---").append("\r\n");
    }
}
