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
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import net.clanwolf.starmap.transfer.mwo.MechIdInfo;
import net.clanwolf.starmap.transfer.mwo.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static net.clanwolf.starmap.constants.Constants.*;


/**
 * Diese Klasse berechnet die Reparaturkosten der jeweiligen Mechs.
 *
 * @author KERNREAKTOR
 * @version 1.0.5
 */
public class CalcBalance {

    private double attackerRepairCost;
    private double defenderRepairCost;

    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    /**
 * Erstellt einen Reparaturbericht, die als Nachricht versendet werden kann.
 * @return Der (StringBuilder) Reparaturbericht wird zurückgegeben.
 */
    public StringBuilder getMailMessage() {
        return mailMessage;
    }

    private final StringBuilder mailMessage = new StringBuilder();

    /**
     * Es werden alle Reparaturkosten für den Angreifer zurückgegeben,
     * die bei {@link #CalcBalance(AttackStatsPOJO rpcs)} berechnet wurden.
     * @return Gibt einen (double) Wert zurück.
     */
    public double getAttackerRepairCost() {
        return attackerRepairCost;
    }

    /**
     * Legt einen neuen Wert fest für die gesamten Reparaturkosten für die Angreifer.
     * @param attackerRepairCost Der Neue (double) Wert für die gesamten Reparaturkosten der Angreifer.
     */
    public void setAttackerRepairCost(double attackerRepairCost) {
        this.attackerRepairCost = this.attackerRepairCost + attackerRepairCost;
    }

    /**
     * Es werden alle Reparaturkosten für den Verteidiger zurückgegeben,
     * die bei {@link #CalcBalance(AttackStatsPOJO rpcs)} berechnet wurden.
     * @return Gibt einen (double) Wert zurück.
     */
    public double getDefenderRepairCost() {
        return defenderRepairCost;
    }

    /**
     * Legt einen neuen Wert fest für die gesamten Reparaturkosten für die Verteidiger.
     * @param defenderRepairCost Der Neue (double) Wert für die gesamten Reparaturkosten der Verteidiger.
     */
    public void setDefenderRepairCost(double defenderRepairCost) {
        this.defenderRepairCost = this.defenderRepairCost + defenderRepairCost;
    }

    /**
     * Es werden die Reparaturkosten der jeweiligen Spieler,
     * die auf der Angreifer Seite sind zurückgegeben,
     * die bei {@link #CalcBalance(AttackStatsPOJO rpcs)} berechnet wurde.
     * @return Gibt einen (HashMap(RolePlayCharacterPOJO, Double)) Wert zurück.

    public Map<String, Double> getAttackerPlayerRepairCost() {
        return attackerPlayerRepairCost;
    }*/
    public FactionPOJO getPlayerFaction(Long factionID){

        return FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, factionID);
    }

    /**
     * Berechnet die Reparaturkosten der jeweiligen Seite.
     * @param attackStats AttackStatsPOJO
     */
    public   CalcBalance(AttackStatsPOJO attackStats){

        String mwoMatchID = attackStats.getMwoMatchId();
        RolePlayCharacterStatsDAO dao = RolePlayCharacterStatsDAO.getInstance();
        RolePlayCharacterDAO characterDAO = RolePlayCharacterDAO.getInstance();
		StatsMwoDAO statsMwoDAO = net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StatsMwoDAO.getInstance();
        ArrayList<RolePlayCharacterStatsPOJO> list = dao.findByMatchId(mwoMatchID);

        FactionPOJO factionAttacker = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getAttackerFactionId());
        FactionPOJO factionDefender = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getDefenderFactionId());
        FactionPOJO factionWinner = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, attackStats.getWinnerFactionId());

	    StatsMwoPOJO statsMwoPOJO = statsMwoDAO.findByMWOGameId(mwoMatchID);
	    String rawJSONstatsData = statsMwoPOJO.getRawData();

	    MWOMatchResult matchDetails = new Gson().fromJson(rawJSONstatsData, MWOMatchResult.class);


        Long attackerTeam = 0L;
        Long defenderTeam =0L;
        double defenderMechValues = 0;
        double attackerMechValue = 0;

        String columnWidthDefault = "%-35.35s";

        DecimalFormat nf = new DecimalFormat();

        String attackerDropleadName = "";
        String defenderDropleadName = "";


        //Es werden Spieler gesucht, die Droplead sind
        for(RolePlayCharacterStatsPOJO pojo : list){

            RolePlayCharacterPOJO character = characterDAO.findById(Nexus.DUMMY_USERID, pojo.getRoleplayCharacterId());


            if(pojo.getLeadingPosition()){

                //Wenn Droplead als Spectator anwesend ist, dann eine Fehlermeldung ausgeben
                if (pojo.getMwoTeam() == null){

                    logger.error("Droplead " + character.getMwoUsername() + " was in spectator mode.");

                    mailMessage.append("An error occurred while calculating the costs.").append("\r\n");
                    mailMessage.append("Droplead ").append(character.getMwoUsername()).append(" was in spectator mode.").append("\r\n");
                    mailMessage.append("But it is tried to perform the calculation.").append("\r\n\r\n");

                    if(attackStats.getDefenderFactionId().equals(character.getFactionId().longValue())){

                        defenderDropleadName = character.getMwoUsername();
                    }
                    if(attackStats.getAttackerFactionId().equals(character.getFactionId().longValue())){

                        attackerDropleadName = character.getMwoUsername();
                    }

                    }else{

                    if(attackStats.getDefenderFactionId().equals(character.getFactionId().longValue())){
                        defenderTeam = pojo.getMwoTeam();
                        defenderDropleadName = character.getMwoUsername();
                    }
                    if(attackStats.getAttackerFactionId().equals(character.getFactionId().longValue())){
                        attackerTeam = pojo.getMwoTeam();
                        attackerDropleadName = character.getMwoUsername();
                    }
                }
            }
        }

        if(attackerTeam == 0L){
            if(defenderTeam == 1L){
                attackerTeam = 2L;
            }
            if(defenderTeam == 2L){
                attackerTeam = 1L;
            }
        }

        if(defenderTeam == 0L){
            if(attackerTeam == 1L){
                defenderTeam = 2L;
            }
            if(attackerTeam == 2L){
                defenderTeam = 1L;
            }
        }

        //Suche Attacker Droplead, wenn er noch nicht gefunden wurde.
        if(attackerTeam == 0L && defenderTeam > 0){
            for(RolePlayCharacterStatsPOJO pojo : list){

                RolePlayCharacterPOJO character = characterDAO.findById(Nexus.DUMMY_USERID, pojo.getRoleplayCharacterId());

                if (pojo.getMwoTeam()!= null){
                    if(pojo.getLeadingPosition() && !(defenderTeam.equals(pojo.getMwoTeam()))){

                        attackerTeam = pojo.getMwoTeam();
                        attackerDropleadName = character.getMwoUsername();
                        break;
                    }
                }
            }
        }

        //Suche Defender Droplead, wenn er noch nicht gefunden wurde.
        if(defenderTeam == 0L && attackerTeam > 0){
            for(RolePlayCharacterStatsPOJO pojo : list){

                RolePlayCharacterPOJO character = characterDAO.findById(Nexus.DUMMY_USERID, pojo.getRoleplayCharacterId());

                if (pojo.getMwoTeam() != null){
                    if(pojo.getLeadingPosition() && !(attackerTeam.equals(pojo.getMwoTeam()))){

                        defenderTeam = pojo.getMwoTeam();
                        defenderDropleadName = character.getMwoUsername();
                        break;
                    }
                }

            }
        }

        //Wenn der Droplead immer noch nicht gefunden wurde, dann soll anhand der FactionID in der Tabelle roleplay_character_stats gesucht werden.
        if(defenderTeam == 0L && attackerTeam == 0L) {
            for (RolePlayCharacterStatsPOJO pojo : list) {

                RolePlayCharacterPOJO character = characterDAO.findById(Nexus.DUMMY_USERID, pojo.getRoleplayCharacterId());


                if (pojo.getMwoTeam() != null) {
                    //Get Droplead
                    if (pojo.getLeadingPosition()) {

                        if (attackStats.getDefenderFactionId().equals(pojo.getRoleplayCharacterFactionId())) {
                            defenderTeam = pojo.getMwoTeam();
                            defenderDropleadName = character.getMwoUsername();
                        }
                        if (attackStats.getAttackerFactionId().equals(pojo.getRoleplayCharacterFactionId())) {
                            attackerTeam = pojo.getMwoTeam();
                            attackerDropleadName = character.getMwoUsername();
                        }
                    }
                }
            }
        }

        //
        if(!(attackerTeam == 0L) && !(defenderTeam == 0L)) {
            mailMessage.append("---Start of repair cost report---").append("\r\n\r\n");
            mailMessage.append("Repair cost evaluations between the attacker ");
            mailMessage.append(factionAttacker.getName_en());
            mailMessage.append(" and the defender ");
            mailMessage.append(factionDefender.getName_en());
            mailMessage.append(".\r\n\r\n");

            mailMessage.append(String.format(columnWidthDefault, "Round Id: ")).append(attackStats.getRoundId()).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault, "Season Id: ")).append(attackStats.getSeasonId()).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault, "Attack Id: ")).append(attackStats.getAttackId()).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault, "Id: ")).append(attackStats.getId()).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault, "Drop Id: ")).append(attackStats.getDropId()).append("\r\n");
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

            //Wert der Mechs für die Angreifer und die Verteidiger ermitteln
            for (UserDetail detail : matchDetails.getUserDetails()) {
                if (detail.getTeam() != null) {

                    MechIdInfo mechInfo = new MechIdInfo(Math.toIntExact(detail.getMechItemID()));

                    if (detail.getTeam().equals(defenderTeam.toString())) {

                        defenderMechValues = defenderMechValues + (0 - mechInfo.getRepairCost(0));

                    }
                    if (detail.getTeam().equals(attackerTeam.toString())) {

                        attackerMechValue = attackerMechValue + (0- mechInfo.getRepairCost(0));

                    }
                }
            }

            mailMessage.append("─".repeat(105)).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault,""));
            mailMessage.append(String.format(columnWidthDefault,"Attacker"));
            mailMessage.append(String.format(columnWidthDefault,"Defender")).append("\r\n");


            mailMessage.append("─".repeat(105)).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault,"Tonnage:"));
            mailMessage.append(String.format(columnWidthDefault,attackStats.getAttackerTonnage()));
            mailMessage.append(String.format(columnWidthDefault,attackStats.getDefenderTonnage())).append("\r\n");

            mailMessage.append("─".repeat(105)).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault,"Lost Tonnage:"));
            mailMessage.append(String.format(columnWidthDefault,attackStats.getAttackerLostTonnage()));
            mailMessage.append(String.format(columnWidthDefault,attackStats.getDefenderLostTonnage())).append("\r\n");

            mailMessage.append("─".repeat(105)).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault,"Kills:"));
            mailMessage.append(String.format(columnWidthDefault,attackStats.getAttackerKillCount()));
            mailMessage.append(String.format(columnWidthDefault,attackStats.getDefenderKillCount())).append("\r\n");

            mailMessage.append("─".repeat(105)).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault,"Number of pilots:"));
            mailMessage.append(String.format(columnWidthDefault,attackStats.getAttackerNumberOfPilots()));
            mailMessage.append(String.format(columnWidthDefault,attackStats.getDefenderNumberOfPilots())).append("\r\n");

            mailMessage.append("─".repeat(105)).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault, "Droplead:"));

            mailMessage.append(String.format(columnWidthDefault, attackerDropleadName));
            mailMessage.append(String.format(columnWidthDefault, defenderDropleadName)).append("\r\n");

            mailMessage.append("─".repeat(105)).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault,"Total value of the mechs:"));
            mailMessage.append(String.format(columnWidthDefault,nf.format(attackerMechValue) + " C-Bills"));
            mailMessage.append(String.format(columnWidthDefault,nf.format(defenderMechValues) + " C-Bills")).append("\r\n");

            mailMessage.append("─".repeat(105)).append("\r\n\r\n");


            String columnWidthMWOUsername = "%-45.45s";
            String columnWidthMechName = "%-30.30s";
            String columnWidthPercentToRepair = "%-15.15s";
            String columnWidthRepairCost = "%20.20s";
            String columnWidthTotal = "%-75.75s";

            mailMessage.append("Repair cost for defender ").append(factionDefender.getName_en()).append(":\r\n\r\n");
            mailMessage.append("─".repeat(110));
            mailMessage.append("\r\n");
            mailMessage.append(String.format(columnWidthMWOUsername, "Username"));
            mailMessage.append(String.format(columnWidthMechName, "Description"));
            mailMessage.append(String.format(columnWidthPercentToRepair, " "));
            mailMessage.append(String.format(columnWidthRepairCost, "Income expenses"));
            mailMessage.append("\r\n");

            //Berechne die kosten für den Verteidiger
            for (UserDetail detail : matchDetails.getUserDetails()) {
                if(detail.getTeam() !=null) {
                    if (detail.getTeam().equals(defenderTeam.toString())) {

                        double sumSinglePlayerPayout;
                        mailMessage.append("─".repeat(110));
                        mailMessage.append("\r\n");
                        mailMessage.append(String.format(columnWidthMWOUsername, detail.getUsername()));

                        MechIdInfo mechInfo = new MechIdInfo(Math.toIntExact(detail.getMechItemID()));

                        defenderRepairCost = defenderRepairCost + mechInfo.getRepairCost(Math.toIntExact(detail.getHealthPercentage()));
                        sumSinglePlayerPayout =  mechInfo.getRepairCost(Math.toIntExact(detail.getHealthPercentage()));
                        mailMessage.append(String.format(columnWidthMechName, mechInfo.getShortname()));
                        mailMessage.append(String.format(columnWidthPercentToRepair, "Repair(" + (100L - detail.getHealthPercentage() + "%)")));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(mechInfo.getRepairCost(detail.getHealthPercentage())) + " C-Bills"));
                        mailMessage.append("\r\n");

                        defenderRepairCost = defenderRepairCost + detail.getComponentsDestroyed() * REWARD_EACH_COMPONENT_DESTROYED;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + detail.getComponentsDestroyed() * REWARD_EACH_COMPONENT_DESTROYED;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward components destroyed" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getComponentsDestroyed() + " * " + nf.format( REWARD_EACH_COMPONENT_DESTROYED)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(detail.getComponentsDestroyed() * REWARD_EACH_COMPONENT_DESTROYED) + " C-Bills"));
                        mailMessage.append("\r\n");

                        defenderRepairCost = defenderRepairCost + detail.getMatchScore() * REWARD_EACH_MACHT_SCORE;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + detail.getMatchScore() * REWARD_EACH_MACHT_SCORE;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward match score" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getMatchScore() + " * " + nf.format( REWARD_EACH_MACHT_SCORE)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(detail.getMatchScore() * REWARD_EACH_MACHT_SCORE) + " C-Bills"));
                        mailMessage.append("\r\n");

                        defenderRepairCost = defenderRepairCost + detail.getDamage() * REWARD_EACH_DAMAGE;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + detail.getDamage() * REWARD_EACH_DAMAGE;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward damage" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getDamage() + " * " + nf.format( REWARD_EACH_DAMAGE)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(detail.getDamage() * REWARD_EACH_DAMAGE) + " C-Bills"));
                        mailMessage.append("\r\n");

                        defenderRepairCost = defenderRepairCost + detail.getTeamDamage() * REWARD_EACH_TEAM_DAMAGE;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + detail.getTeamDamage() * REWARD_EACH_TEAM_DAMAGE;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward team damage" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getTeamDamage() + " * " + nf.format( REWARD_EACH_TEAM_DAMAGE)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(detail.getTeamDamage() * REWARD_EACH_TEAM_DAMAGE) + " C-Bills"));
                        mailMessage.append("\r\n");

                        /*defenderRepairCost = defenderRepairCost + detail.getKills() * REWARD_EACH_KILL;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + detail.getKills() * REWARD_EACH_KILL;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward each kill" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getKills() + " * " + nf.format( REWARD_EACH_KILL)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(detail.getKills() * REWARD_EACH_KILL) + " C-Bills"));
                        mailMessage.append("\r\n");*/

                        defenderRepairCost = defenderRepairCost + (detail.getKills() * (attackerMechValue/attackStats.getAttackerNumberOfPilots()))/2;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + (detail.getKills() * (attackerMechValue/attackStats.getAttackerNumberOfPilots()))/2;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward each kill" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getKills() + " * " + nf.format( (attackerMechValue/attackStats.getAttackerNumberOfPilots())/2)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format((detail.getKills() * (attackerMechValue/attackStats.getAttackerNumberOfPilots()))/2) + " C-Bills"));
                        mailMessage.append("\r\n");

                        mailMessage.append("─".repeat(110));
                        mailMessage.append("\r\n");

                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Subtotal" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, ""));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(sumSinglePlayerPayout) + " C-Bills"));
                        mailMessage.append("\r\n");
                        mailMessage.append("═".repeat(110));
                        mailMessage.append("\r\n");
                    }
                }
            }

            mailMessage.append(String.format(columnWidthMWOUsername, getPlayerFaction(attackStats.getDefenderFactionId()).getName_en()));
            mailMessage.append(String.format(columnWidthMechName, "Income" ));
            mailMessage.append(String.format(columnWidthPercentToRepair, " "));
            mailMessage.append(String.format(columnWidthRepairCost, nf.format(getIncome(attackStats.getDefenderFactionId())) + " C-Bills"));
            mailMessage.append("\r\n");
            setDefenderRepairCost(getIncome(attackStats.getDefenderFactionId()));

            mailMessage.append(String.format(columnWidthMWOUsername, getPlayerFaction(attackStats.getDefenderFactionId()).getName_en()));
            mailMessage.append(String.format(columnWidthMechName, "Defend cost"));
            mailMessage.append(String.format(columnWidthPercentToRepair, " "));
            mailMessage.append(String.format(columnWidthRepairCost, nf.format(getDefendCost(attackStats.getStarSystemDataId())) + " C-Bills"));
            mailMessage.append("\r\n");
            setDefenderRepairCost(getDefendCost(attackStats.getStarSystemDataId()));

            mailMessage.append("─".repeat(110));
            mailMessage.append("\r\n");

            mailMessage.append(String.format(columnWidthMWOUsername, ""));
            mailMessage.append(String.format(columnWidthMechName, ""));
            mailMessage.append(String.format(columnWidthPercentToRepair, "Total"));
            mailMessage.append(String.format(columnWidthRepairCost, nf.format((long) getDefenderRepairCost()) + " C-Bills"));
            mailMessage.append("\r\n");
            mailMessage.append("═".repeat(110));
            mailMessage.append("\r\n\r\n");

            mailMessage.append("Repair cost for attacker ").append(factionAttacker.getName_en()).append(":\r\n\r\n");
            mailMessage.append("─".repeat(110));
            mailMessage.append("\r\n");
            mailMessage.append(String.format(columnWidthMWOUsername, "Username"));
            mailMessage.append(String.format(columnWidthMechName, "Description"));
            mailMessage.append(String.format(columnWidthPercentToRepair, " "));
            mailMessage.append(String.format(columnWidthRepairCost, "Income expenses"));
            mailMessage.append("\r\n");

            //Berechne die Kosten für den Angreifer
            for (UserDetail detail : matchDetails.getUserDetails()) {
                if(detail.getTeam() !=null){
                    if( detail.getTeam().equals(attackerTeam.toString())) {

                        double sumSinglePlayerPayout;
                        mailMessage.append("─".repeat(110));
                        mailMessage.append("\r\n");
                        mailMessage.append(String.format(columnWidthMWOUsername, detail.getUsername()));

                        MechIdInfo mechInfo = new MechIdInfo(Math.toIntExact(detail.getMechItemID()));

                        attackerRepairCost = attackerRepairCost + mechInfo.getRepairCost(Math.toIntExact(detail.getHealthPercentage()));
                        sumSinglePlayerPayout =  mechInfo.getRepairCost(Math.toIntExact(detail.getHealthPercentage()));
                        mailMessage.append(String.format(columnWidthMechName, mechInfo.getShortname()));
                        mailMessage.append(String.format(columnWidthPercentToRepair, "Repair(" + (100L - detail.getHealthPercentage() + "%)")));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(mechInfo.getRepairCost(detail.getHealthPercentage())) + " C-Bills"));
                        mailMessage.append("\r\n");

                        attackerRepairCost = attackerRepairCost + detail.getComponentsDestroyed() * REWARD_EACH_COMPONENT_DESTROYED;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + detail.getComponentsDestroyed() * REWARD_EACH_COMPONENT_DESTROYED;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward components destroyed" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getComponentsDestroyed() + " * " + nf.format( REWARD_EACH_COMPONENT_DESTROYED)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(detail.getComponentsDestroyed() * REWARD_EACH_COMPONENT_DESTROYED) + " C-Bills"));
                        mailMessage.append("\r\n");

                        attackerRepairCost = attackerRepairCost + detail.getMatchScore() * REWARD_EACH_MACHT_SCORE;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + detail.getMatchScore() * REWARD_EACH_MACHT_SCORE;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward match score" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getMatchScore() + " * " + nf.format( REWARD_EACH_MACHT_SCORE)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(detail.getMatchScore() * REWARD_EACH_MACHT_SCORE) + " C-Bills"));
                        mailMessage.append("\r\n");

                        attackerRepairCost = attackerRepairCost + detail.getDamage() * REWARD_EACH_DAMAGE;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + detail.getDamage() * REWARD_EACH_DAMAGE;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward damage" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getDamage() + " * " + nf.format( REWARD_EACH_DAMAGE)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(detail.getDamage() * REWARD_EACH_DAMAGE) + " C-Bills"));
                        mailMessage.append("\r\n");

                        attackerRepairCost = attackerRepairCost + detail.getTeamDamage() * REWARD_EACH_TEAM_DAMAGE;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + detail.getTeamDamage() * REWARD_EACH_TEAM_DAMAGE;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward team damage" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getTeamDamage() + " * " + nf.format( REWARD_EACH_TEAM_DAMAGE)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(detail.getTeamDamage() * REWARD_EACH_TEAM_DAMAGE) + " C-Bills"));
                        mailMessage.append("\r\n");

                        /*attackerRepairCost = attackerRepairCost + detail.getKills() * REWARD_EACH_KILL;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + detail.getKills() * REWARD_EACH_KILL;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward each kill" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getKills() + " * " + nf.format( REWARD_EACH_KILL)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(detail.getKills() * REWARD_EACH_KILL) + " C-Bills"));
                        mailMessage.append("\r\n");*/

                        attackerRepairCost = attackerRepairCost + (detail.getKills() * (defenderMechValues/attackStats.getDefenderNumberOfPilots()))/2;
                        sumSinglePlayerPayout = sumSinglePlayerPayout + (detail.getKills() * (defenderMechValues/attackStats.getDefenderNumberOfPilots()))/2;
                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Reward each kill" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, detail.getKills() + " * " + nf.format( (defenderMechValues/attackStats.getDefenderNumberOfPilots())/2)));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format((detail.getKills() * (defenderMechValues/attackStats.getDefenderNumberOfPilots()))/2) + " C-Bills"));
                        mailMessage.append("\r\n");

                        mailMessage.append("─".repeat(110));
                        mailMessage.append("\r\n");

                        mailMessage.append(String.format(columnWidthMWOUsername, ""));
                        mailMessage.append(String.format(columnWidthMechName, "Subtotal" ));
                        mailMessage.append(String.format(columnWidthPercentToRepair, ""));
                        mailMessage.append(String.format(columnWidthRepairCost, nf.format(sumSinglePlayerPayout) + " C-Bills"));
                        mailMessage.append("\r\n");
                        mailMessage.append("═".repeat(110));
                        mailMessage.append("\r\n");
                    }
                }

            }

            mailMessage.append(String.format(columnWidthMWOUsername, getPlayerFaction(attackStats.getAttackerFactionId()).getName_en()));
            mailMessage.append(String.format(columnWidthMechName, "Income" ));
            mailMessage.append(String.format(columnWidthPercentToRepair, " "));
            mailMessage.append(String.format(columnWidthRepairCost, nf.format(getIncome(attackStats.getAttackerFactionId())) + " C-Bills"));
            mailMessage.append("\r\n");
            setAttackerRepairCost(getIncome(attackStats.getAttackerFactionId()));

            mailMessage.append(String.format(columnWidthMWOUsername, getPlayerFaction(attackStats.getAttackerFactionId()).getName_en()));
            mailMessage.append(String.format(columnWidthMechName, "Attack cost"));
            mailMessage.append(String.format(columnWidthPercentToRepair, " "));
            mailMessage.append(String.format(columnWidthRepairCost, nf.format(getAttackCost(attackStats.getStarSystemDataId())) + " C-Bills"));
            mailMessage.append("\r\n");
            setAttackerRepairCost(getAttackCost(attackStats.getStarSystemDataId()));

            mailMessage.append("─".repeat(110));
            mailMessage.append("\r\n");

            mailMessage.append(String.format(columnWidthMWOUsername, ""));
            mailMessage.append(String.format(columnWidthMechName, ""));
            mailMessage.append(String.format(columnWidthPercentToRepair, "Total"));
            mailMessage.append(String.format(columnWidthRepairCost, nf.format((long) getAttackerRepairCost()) + " C-Bills"));
            mailMessage.append("\r\n");

            mailMessage.append("═".repeat(110));
            mailMessage.append("\r\n\r\n");

            mailMessage.append("---End of repair cost report---");
            mailMessage.append("\r\n\r\n");
        }
    }

    /**
     * Berechnet die Kosten, um den Planeten zu verteidigen.
     * @param starSystemDataId Die StarSystemID, wo aktuell verteidigt wird.
     * @return Die Kosten werden zurückgegeben, wenn man diesen Planeten verteidigt.
     */
    public long getDefendCost(long starSystemDataId){
        long cost = 0L;
        ArrayList<StarSystemDataPOJO> starSystemDataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();

        for (StarSystemDataPOJO starSystemData : starSystemDataListHH) {
            if(starSystemData.getStarSystemID().getId().equals(starSystemDataId)){
                switch (starSystemData.getLevel().intValue()) {
                    case 1 -> // Regular world
                            cost = cost + Constants.REGULAR_SYSTEM_DEFEND_COST;
                    case 2 -> // Industrial world
                            cost = cost + Constants.INDUSTRIAL_SYSTEM_DEFEND_COST;
                    case 3 -> // Capital world
                            cost = cost + Constants.CAPITAL_SYSTEM_DEFEND_COST;
                }
                break;
            }
        }
        return cost * 1000;
    }

    /**
     * Berechnet die Kosten, für die Angreifer.
     * @param starSystemDataId Die StarSystemID, wo aktuell der angegriffen wird.
     * @return Die Kosten werden zurückgegeben, wenn man diesen Planeten angreift.
     */
    public long getAttackCost(long starSystemDataId){

        long cost = 0L;
        ArrayList<StarSystemDataPOJO> starSystemDataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();

        for (StarSystemDataPOJO starSystemData : starSystemDataListHH) {
            if(starSystemData.getStarSystemID().getId().equals(starSystemDataId)){
                switch (starSystemData.getLevel().intValue()) {
                    case 1 -> // Regular world
                            cost = cost + Constants.REGULAR_SYSTEM_ATTACK_COST;
                    case 2 -> // Industrial world
                            cost = cost + Constants.INDUSTRIAL_SYSTEM_ATTACK_COST;
                    case 3 -> // Capital world
                            cost = cost + Constants.CAPITAL_SYSTEM_ATTACK_COST;
                }
                break;
            }
        }
        return cost * 1000;
    }

    /**
     * Berechnet die Einnahmen und Ausgaben von jedem Planeten, den man erobert hat.
     * @param userFactionId Die FactionID von dem jeweiligen Spieler.
     * @return Gibt die Einnahmen und Ausgaben zurück.
     */
    public long getIncome(long userFactionId){

        long income = 0L;
        long cost = 0L;

        ArrayList<StarSystemDataPOJO> starSystemDataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();
        for (StarSystemDataPOJO starSystemData : starSystemDataListHH) {
            if (starSystemData.getFactionID().getId().equals(userFactionId)) {

                switch (starSystemData.getLevel().intValue()) {
                    case 1 -> { // Regular

                        income = income + Constants.REGULAR_SYSTEM_GENERAL_INCOME;
                        cost = cost + Constants.REGULAR_SYSTEM_GENERAL_COST;
                    }
                    case 2 -> { // Industrial

                        income = income + Constants.INDUSTRIAL_SYSTEM_GENERAL_INCOME;
                        cost = cost + Constants.INDUSTRIAL_SYSTEM_GENERAL_COST;
                    }
                    case 3 -> { // Capital

                        income = income + Constants.CAPITAL_SYSTEM_GENERAL_INCOME;
                        cost = cost + Constants.CAPITAL_SYSTEM_GENERAL_COST;
                    }
                }
            }
        }
        return (income + cost) * 1000;
    }
}
