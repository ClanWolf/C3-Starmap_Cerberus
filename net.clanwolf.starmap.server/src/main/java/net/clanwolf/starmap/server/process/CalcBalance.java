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

import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.transfer.mwo.MechIdInfo;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Diese Klasse berechnet die Reparaturkosten der jeweiligen Mechs.
 *
 * @author KERNREAKTOR
 * @version 1.0.4
 */
public class CalcBalance {

    private double attackerRepairCost;
    private double defenderRepairCost;
    private final Map<RolePlayCharacterStatsPOJO, Double> defenderPlayerRepairCost = new HashMap<>();
    private final Map<RolePlayCharacterStatsPOJO, Double> attackerPlayerRepairCost = new HashMap<>();
    private final AttackStatsPOJO aSPOJO;

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
     * die auf der Verteidiger Seite sind zurückgegeben,
     * die bei {@link #CalcBalance(AttackStatsPOJO rpcs)} berechnet wurde.
     * @return Gibt einen (HashMap(RolePlayCharacterPOJO, Double)) Wert zurück.
     */
    public Map<RolePlayCharacterStatsPOJO, Double> getDefenderPlayerRepairCost() {
        return defenderPlayerRepairCost;
    }

    /**
     * Es werden die Reparaturkosten der jeweiligen Spieler,
     * die auf der Angreifer Seite sind zurückgegeben,
     * die bei {@link #CalcBalance(AttackStatsPOJO rpcs)} berechnet wurde.
     * @return Gibt einen (HashMap(RolePlayCharacterPOJO, Double)) Wert zurück.
     */
    public Map<RolePlayCharacterStatsPOJO, Double> getAttackerPlayerRepairCost() {
        return attackerPlayerRepairCost;
    }

    /**
     * Berechnet die Reparaturkosten der jeweiligen Seite,
     * die bei {@link #getAttackerPlayerRepairCost()}, {@link #getDefenderPlayerRepairCost()},
     * {@link #getAttackerRepairCost()} und {@link #getDefenderRepairCost()} abgefragt
     * werden können.
     * @param rpcs AttackStatsPOJO
     */
    public   CalcBalance(AttackStatsPOJO rpcs){

        String mwomatchid = rpcs.getMwoMatchId();
        RolePlayCharacterStatsDAO dao = RolePlayCharacterStatsDAO.getInstance();
        RolePlayCharacterDAO characterDAO = RolePlayCharacterDAO.getInstance();
        ArrayList<RolePlayCharacterStatsPOJO> list = dao.findByMatchId(mwomatchid);

        FactionPOJO factionAttacker = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, rpcs.getAttackerFactionId());
        FactionPOJO factionDefender = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, rpcs.getDefenderFactionId());
        FactionPOJO factionWinner = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, rpcs.getWinnerFactionId());

        aSPOJO = rpcs;

        Long attackerTeam = 0L;
        Long defenderTeam =0L;

        String columnWidthDefault = "%-20.20s";

        DecimalFormat nf = new DecimalFormat();

        mailMessage.append("---Start of repair cost report---").append("\r\n\r\n");
        mailMessage.append("Repair cost evaluations between the attacker ");
        mailMessage.append(factionAttacker.getName_en());
        mailMessage.append(" and the defender ");
        mailMessage.append(factionDefender.getName_en());
        mailMessage.append(".\r\n\r\n");

        mailMessage.append(String.format(columnWidthDefault, "Round Id: ")).append(rpcs.getRoundId()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Season Id: ")).append(rpcs.getSeasonId()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Attack Id: ")).append(rpcs.getAttackId()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Id: ")).append(rpcs.getId()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Drop Id: ")).append(rpcs.getDropId()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "MWO Match Id: ")).append(rpcs.getMwoMatchId()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Map: ")).append(rpcs.getMap()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Game mode: ")).append(rpcs.getMode()).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault, "Winner: ")).append(factionWinner.getName_en()).append("\r\n");

        try {

            String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ssXXX";
            DateFormat dateFormat = new SimpleDateFormat(isoDatePattern);
            Date parsedDate = dateFormat.parse(rpcs.getDropEnded());

            mailMessage.append(String.format(columnWidthDefault, "Drop ended: ")).append(parsedDate).append("\r\n\r\n");

        }
        catch(Exception e) {

            e.printStackTrace();

        }

        mailMessage.append("─".repeat(60)).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault,""));
        mailMessage.append(String.format(columnWidthDefault,"Attacker"));
        mailMessage.append(String.format(columnWidthDefault,"Defender")).append("\r\n");


        mailMessage.append("─".repeat(60)).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault,"Tonnage:"));
        mailMessage.append(String.format(columnWidthDefault,rpcs.getAttackerTonnage()));
        mailMessage.append(String.format(columnWidthDefault,rpcs.getDefenderTonnage())).append("\r\n");

        mailMessage.append("─".repeat(60)).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault,"Lost Tonnage:"));
        mailMessage.append(String.format(columnWidthDefault,rpcs.getAttackerLostTonnage()));
        mailMessage.append(String.format(columnWidthDefault,rpcs.getDefenderLostTonnage())).append("\r\n");

        mailMessage.append("─".repeat(60)).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault,"Kills:"));
        mailMessage.append(String.format(columnWidthDefault,rpcs.getAttackerKillCount()));
        mailMessage.append(String.format(columnWidthDefault,rpcs.getDefenderKillCount())).append("\r\n");

        mailMessage.append("─".repeat(60)).append("\r\n");
        mailMessage.append(String.format(columnWidthDefault,"Number of pilots:"));
        mailMessage.append(String.format(columnWidthDefault,rpcs.getAttackerNumberOfPilots()));
        mailMessage.append(String.format(columnWidthDefault,rpcs.getDefenderNumberOfPilots())).append("\r\n");


        String attackerDropleadName = "";
        String defenderDropleadName = "";

        for(RolePlayCharacterStatsPOJO pojo : list){

            RolePlayCharacterPOJO character = characterDAO.findById(Nexus.DUMMY_USERID, pojo.getRoleplayCharacterId());

            //Get Attacker Droplead
            if(pojo.getLeadingPosition()){
                if(rpcs.getDefenderFactionId().equals(character.getFactionId().longValue())){
                    defenderTeam = pojo.getMwoTeam();
                    defenderDropleadName = character.getMwoUsername();
                }
                if(rpcs.getAttackerFactionId().equals(character.getFactionId().longValue())){
                    attackerTeam = pojo.getMwoTeam();
                    attackerDropleadName = character.getMwoUsername();
                }
            }
        }

        //Wenn noch kein Attacker Droplead gefunden wurde.
        if(attackerTeam == 0L && defenderTeam > 0){
            for(RolePlayCharacterStatsPOJO pojo : list){

                RolePlayCharacterPOJO character = characterDAO.findById(Nexus.DUMMY_USERID, pojo.getRoleplayCharacterId());

                if(pojo.getLeadingPosition() && !(defenderTeam.equals(pojo.getMwoTeam()))){

                        attackerTeam = pojo.getMwoTeam();
                        attackerDropleadName = character.getMwoUsername();
                        break;
                }
            }
        }

        //Wenn noch kein Defender Droplead gefunden wurde.
        if(defenderTeam == 0L && attackerTeam > 0){
            for(RolePlayCharacterStatsPOJO pojo : list){

                RolePlayCharacterPOJO character = characterDAO.findById(Nexus.DUMMY_USERID, pojo.getRoleplayCharacterId());

                if(pojo.getLeadingPosition() && !(attackerTeam.equals(pojo.getMwoTeam()))){

                    defenderTeam = pojo.getMwoTeam();
                    defenderDropleadName = character.getMwoUsername();
                    break;
                }
            }
        }
        if(!(attackerTeam == 0L) && !(defenderTeam == 0L)){

            mailMessage.append("─".repeat(60)).append("\r\n");
            mailMessage.append(String.format(columnWidthDefault,"Droplead:"));
            mailMessage.append(String.format(columnWidthDefault,attackerDropleadName));
            mailMessage.append(String.format(columnWidthDefault,defenderDropleadName)).append("\r\n");

            mailMessage.append("─".repeat(60)).append("\r\n\r\n");

            for(RolePlayCharacterStatsPOJO pojo : list){

                MechIdInfo MI = new MechIdInfo(Math.toIntExact(pojo.getMechItemId()));

                if(attackerTeam.equals(pojo.getMwoTeam())){
                    attackerPlayerRepairCost.put(pojo, MI.getRepairCost(Math.toIntExact(pojo.getMwoSurvivalPercentage())));
                    attackerRepairCost = attackerRepairCost + MI.getRepairCost(Math.toIntExact(pojo.getMwoSurvivalPercentage()));
                }

                if(defenderTeam.equals(pojo.getMwoTeam())){
                    defenderPlayerRepairCost.put(pojo, MI.getRepairCost(Math.toIntExact(pojo.getMwoSurvivalPercentage())));
                    defenderRepairCost = defenderRepairCost + MI.getRepairCost(Math.toIntExact(pojo.getMwoSurvivalPercentage()));
                }
            }

            String columnWidthMWOUsername = "%-30.30s";
            String columnWidthMechname = "%-30.30s";
            String columnWidthPercentToRepair = "%-15.15s";
            String columnWidthRepairCost = "%20.20s";
            String columnWidthTotal = "%-75.75s";

            mailMessage.append("Repair cost for defender " + factionDefender.getName_en() +":\r\n\r\n");
            mailMessage.append("─".repeat(95));
            mailMessage.append("\r\n");
            mailMessage.append(String.format(columnWidthMWOUsername,"MWO Username"));
            mailMessage.append(String.format(columnWidthMechname,"Mechname"));
            mailMessage.append(String.format(columnWidthPercentToRepair,"% to repair"));
            mailMessage.append(String.format(columnWidthRepairCost,"Repaircost"));
            mailMessage.append("\r\n");
            mailMessage.append("─".repeat(95));
            mailMessage.append("\r\n");

            for(Map.Entry<RolePlayCharacterStatsPOJO, Double> defuser : getDefenderPlayerRepairCost().entrySet()){

                RolePlayCharacterPOJO character = characterDAO.findById(Nexus.DUMMY_USERID, defuser.getKey().getRoleplayCharacterId());

                mailMessage.append(String.format(columnWidthMWOUsername,character.getMwoUsername()));

                MechIdInfo MI = new MechIdInfo(Math.toIntExact(defuser.getKey().getMechItemId()));

                mailMessage.append(String.format(columnWidthMechname,MI.getFullname()));
                mailMessage.append(String.format(columnWidthPercentToRepair,(100L - defuser.getKey().getMwoSurvivalPercentage() + "%")));
                mailMessage.append(String.format(columnWidthRepairCost,nf.format(defuser.getValue().longValue()) + " C-Bills"));
                mailMessage.append("\r\n");

            }
            mailMessage.append("─".repeat(95));
            mailMessage.append("\r\n");
            mailMessage.append(String.format(columnWidthTotal,"Defend cost:"));
            mailMessage.append(String.format(columnWidthRepairCost,nf.format(getDefendCost(rpcs.getStarSystemDataId())) + " C-Bills"));
            mailMessage.append("\r\n");

            setDefenderRepairCost(getDefendCost(rpcs.getStarSystemDataId()) );

            mailMessage.append("─".repeat(95));
            mailMessage.append("\r\n");
            mailMessage.append(String.format(columnWidthTotal,"Income:"));
            mailMessage.append(String.format(columnWidthRepairCost,nf.format(getIncome(factionDefender.getId())) + " C-Bills"));
            mailMessage.append("\r\n");

            setDefenderRepairCost(getIncome(factionDefender.getId()));

            mailMessage.append("─".repeat(95));
            mailMessage.append("\r\n");
            mailMessage.append(String.format(columnWidthTotal,"Total:"));
            mailMessage.append(String.format(columnWidthRepairCost,nf.format((long) getDefenderRepairCost()) + " C-Bills"));
            mailMessage.append("\r\n");
            mailMessage.append("═".repeat(95));
            mailMessage.append("\r\n\r\n");

            mailMessage.append("Repair cost for attacker " + factionAttacker.getName_en() +":\r\n\r\n");
            mailMessage.append("─".repeat(95));
            mailMessage.append("\r\n");
            mailMessage.append(String.format(columnWidthMWOUsername,"MWO Username"));
            mailMessage.append(String.format(columnWidthMechname,"Mechname"));
            mailMessage.append(String.format(columnWidthPercentToRepair,"% to repair"));
            mailMessage.append(String.format(columnWidthRepairCost,"Repaircost"));
            mailMessage.append("\r\n");
            mailMessage.append("─".repeat(95));
            mailMessage.append("\r\n");

            for(Map.Entry<RolePlayCharacterStatsPOJO, Double> attuser : getAttackerPlayerRepairCost().entrySet()){

                RolePlayCharacterPOJO character = characterDAO.findById(Nexus.DUMMY_USERID, attuser.getKey().getRoleplayCharacterId());

                mailMessage.append(String.format(columnWidthMWOUsername,character.getMwoUsername()));
                MechIdInfo MI = new MechIdInfo(Math.toIntExact(attuser.getKey().getMechItemId()));
                mailMessage.append(String.format(columnWidthMechname,MI.getFullname()));
                mailMessage.append(String.format(columnWidthPercentToRepair,(100L - attuser.getKey().getMwoSurvivalPercentage()) + "%"));
                mailMessage.append(String.format(columnWidthRepairCost,nf.format(attuser.getValue().longValue()) + " C-Bills"));
                mailMessage.append("\r\n");

            }

            mailMessage.append("─".repeat(95));
            mailMessage.append("\r\n");
            mailMessage.append(String.format(columnWidthTotal,"Attack cost:"));
            mailMessage.append(String.format(columnWidthRepairCost,nf.format(getAttackCost(rpcs.getStarSystemDataId())) + " C-Bills"));
            mailMessage.append("\r\n");

            setAttackerRepairCost(getAttackCost(rpcs.getStarSystemDataId()) );


            mailMessage.append("─".repeat(95));
            mailMessage.append("\r\n");
            mailMessage.append(String.format(columnWidthTotal,"Income:"));
            mailMessage.append(String.format(columnWidthRepairCost,nf.format(getIncome(factionAttacker.getId())) + " C-Bills"));
            mailMessage.append("\r\n");

            setAttackerRepairCost(getIncome(factionAttacker.getId()));

            mailMessage.append("─".repeat(95));
            mailMessage.append("\r\n");
            mailMessage.append(String.format(columnWidthTotal,"Total:"));
            mailMessage.append(String.format(columnWidthRepairCost,nf.format((long) getAttackerRepairCost()) + " C-Bills"));
            mailMessage.append("\r\n");
            mailMessage.append("═".repeat(95));
            mailMessage.append("\r\n");
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
        ArrayList<StarSystemDataPOJO> starsystemdataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();

        for (StarSystemDataPOJO starsystemdata : starsystemdataListHH) {
            if(starsystemdata.getStarSystemID().getId().equals(starSystemDataId)){
                switch (starsystemdata.getLevel().intValue()) {
                    case 1 -> // Regular world
                            cost = cost + Constants.REGULAR_SYSTEM_DEFEND_COST;
                    case 2 -> // Industrial world
                            cost = cost + Constants.INDUSTRIAL_SYSTEM_DEFEND_COST;
                    case 3 -> // Captial world
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
        ArrayList<StarSystemDataPOJO> starsystemdataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();

        for (StarSystemDataPOJO starsystemdata : starsystemdataListHH) {
            if(starsystemdata.getStarSystemID().getId().equals(starSystemDataId)){
                switch (starsystemdata.getLevel().intValue()) {
                    case 1 -> // Regular world
                            cost = cost + Constants.REGULAR_SYSTEM_ATTACK_COST;
                    case 2 -> // Industrial world
                            cost = cost + Constants.INDUSTRIAL_SYSTEM_ATTACK_COST;
                    case 3 -> // Captial world
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

        ArrayList<StarSystemDataPOJO> starsystemdataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();
        for (StarSystemDataPOJO starsystemdata : starsystemdataListHH) {
            if (starsystemdata.getFactionID().getId().equals(userFactionId)) {

                switch (starsystemdata.getLevel().intValue()) {
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
