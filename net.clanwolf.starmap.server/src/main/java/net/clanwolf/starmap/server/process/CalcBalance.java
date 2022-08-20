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
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StarSystemDataDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StatsMwoDAO;
import net.clanwolf.starmap.server.persistence.pojos.StarSystemDataPOJO;
import net.clanwolf.starmap.server.persistence.pojos.StatsMwoPOJO;
import net.clanwolf.starmap.server.reporting.GenerateRoundReport;
import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;


/**
 * Diese Klasse berechnet die Reparaturkosten der jeweiligen Mechs.
 *
 * @author KERNREAKTOR
 * @version 1.0.5
 */
public class CalcBalance {

    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public CalcBalance(String mwoMatchID, GenerateRoundReport report) throws Exception {

        StatsMwoDAO statsMwoDAO = net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StatsMwoDAO.getInstance();
        StatsMwoPOJO statsMwoPOJO = statsMwoDAO.findByMWOGameId(String.valueOf(mwoMatchID));
        String rawJSONstatsData = statsMwoPOJO.getRawData();
        MWOMatchResult matchDetails = new Gson().fromJson(rawJSONstatsData, MWOMatchResult.class);

        matchDetails.setGameID(mwoMatchID);
        logger.info("Calculating balance...");

        BalanceUserInfo balanceUserInfo;
        balanceUserInfo = new BalanceUserInfo(matchDetails);
        report.createCalcReport(balanceUserInfo.GetAttackerInfo(), balanceUserInfo.GetDefenderInfo());
        logger.info("--- Balance calculating finished ---");

    }

    /**
     * Berechnet die Kosten, um den Planeten zu verteidigen.
     *
     * @param starSystemDataId Die StarSystemID, wo aktuell verteidigt wird.
     * @return Die Kosten werden zurückgegeben, wenn man diesen Planeten verteidigt.
     */
    public long getDefendCost(long starSystemDataId) {
        long cost = 0L;
        ArrayList<StarSystemDataPOJO> starSystemDataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();

        for (StarSystemDataPOJO starSystemData : starSystemDataListHH) {
            if (starSystemData.getStarSystemID().getId().equals(starSystemDataId)) {
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
     *
     * @param starSystemDataId Die StarSystemID, wo aktuell der angegriffen wird.
     * @return Die Kosten werden zurückgegeben, wenn man diesen Planeten angreift.
     */
    public long getAttackCost(long starSystemDataId) {

        long cost = 0L;
        ArrayList<StarSystemDataPOJO> starSystemDataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();

        for (StarSystemDataPOJO starSystemData : starSystemDataListHH) {
            if (starSystemData.getStarSystemID().getId().equals(starSystemDataId)) {
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
     *
     * @param userFactionId Die FactionID von dem jeweiligen Spieler.
     * @return Gibt die Einnahmen und Ausgaben zurück.
     */
    public long getIncome(long userFactionId) {

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
