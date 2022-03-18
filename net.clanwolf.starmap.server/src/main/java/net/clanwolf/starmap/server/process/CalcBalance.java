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

import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayCharacterDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayCharacterStatsDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackStatsPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayCharacterPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayCharacterStatsPOJO;
import net.clanwolf.starmap.transfer.mwo.MechIdInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Diese Klasse berechnet die Reparaturkosten der jeweiligen Mechs.
 *
 * @author KERNREAKTOR
 * @version 1.0.2
 */
public class CalcBalance {

    private double attackerRepairCost;
    private double defenderRepairCost;
    private final Map<RolePlayCharacterStatsPOJO, Double> defenderPlayerRepairCost = new HashMap<>();
    private final Map<RolePlayCharacterStatsPOJO, Double> attackerPlayerRepairCost = new HashMap<>();

    /**
     * Es werden alle Reparaturkosten für den Angreifer wird zurückgegeben,
     * die bei {@link #CalcBalance(AttackStatsPOJO rpcs)} berechnet wurde.
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
        this.attackerRepairCost = attackerRepairCost;
    }

    /**
     * Es werden alle Reparaturkosten für den Verteidiger wird zurückgegeben,
     * die bei {@link #CalcBalance(AttackStatsPOJO rpcs)} berechnet wurde.
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
        this.defenderRepairCost = defenderRepairCost;
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
        Long attackerTeam = 0L;
        Long defenderTeam =0L;

        for(RolePlayCharacterStatsPOJO pojo : list){

            RolePlayCharacterPOJO character = characterDAO.findById(Nexus.DUMMY_USERID, pojo.getRoleplayCharacterId());

            //Get Attacker Droplead
            if(rpcs.getAttackerFactionId() == character.getFactionId().longValue()) {
                if(pojo.getLeadingPosition()){
                    attackerTeam = pojo.getMwoTeam();
                }
            }

            //Get Defender Droplead
            if(rpcs.getDefenderFactionId() == character.getFactionId().longValue()){
                if(pojo.getLeadingPosition()){
                    defenderTeam = pojo.getMwoTeam();
                }
            }
        }

        if(!(attackerTeam == 0L) && !(defenderTeam == 0L)){

            for(RolePlayCharacterStatsPOJO pojo : list){
                RolePlayCharacterPOJO character = characterDAO.findById(Nexus.DUMMY_USERID, pojo.getRoleplayCharacterId());
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
        }
    }
}
