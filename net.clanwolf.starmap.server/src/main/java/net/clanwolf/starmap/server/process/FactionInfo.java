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

import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.server.nexus2.Nexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.FactionDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayCharacterStatsDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StarSystemDataDAO;
import net.clanwolf.starmap.server.persistence.pojos.FactionPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayCharacterStatsPOJO;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Objects;

import net.clanwolf.starmap.server.persistence.pojos.StarSystemDataPOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactionInfo {

    private final Long myFactionId;
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public FactionInfo(Long FactionId) {

        myFactionId = FactionId;

    }

    public String getFactionLongName_en() {

        FactionPOJO factionName;
        factionName = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, myFactionId);

        return factionName.getName_en();
    }

    public String getFactionLongName_de() {

        FactionPOJO factionName;
        factionName = FactionDAO.getInstance().findById(Nexus.DUMMY_USERID, myFactionId);

        return factionName.getName_de();
    }

    public String getTeam(String MWOMatchID) {

        RolePlayCharacterStatsDAO rolePlayCharacterStatsDAO = RolePlayCharacterStatsDAO.getInstance();
        ArrayList<RolePlayCharacterStatsPOJO> rolePlayCharacterStatsPOJO = rolePlayCharacterStatsDAO.findByMatchId(MWOMatchID);
        String factionTeam = null;

        for (RolePlayCharacterStatsPOJO rpChar : rolePlayCharacterStatsPOJO) {

            if (rpChar.getRoleplayCharacterFactionId().equals(myFactionId)) {
                factionTeam = String.valueOf(rpChar.getMwoTeam());
                break;
            }

        }

        if (factionTeam == null) {

            factionTeam = "";
            logger.error("No team could be assigned for the Faction " + getFactionLongName_en());
        }

        return factionTeam;
    }

    public long getSystemIncome(Long currentAttackedSystem) {

        long income = 0L;
        ArrayList<StarSystemDataPOJO> starSystemDataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();

        for (StarSystemDataPOJO starSystemData : starSystemDataListHH) {
            if (starSystemData.getFactionID().getId().equals(myFactionId) && !Objects.equals(currentAttackedSystem, starSystemData.getStarSystemID().getId())) {

                switch (starSystemData.getLevel().intValue()) {
                    case 1 -> // Regular
                            income = income + Constants.REGULAR_SYSTEM_GENERAL_INCOME;
                    case 2 -> // Industrial
                            income = income + Constants.INDUSTRIAL_SYSTEM_GENERAL_INCOME;
                    case 3 -> // Capital
                            income = income + Constants.CAPITAL_SYSTEM_GENERAL_INCOME;
                }
            }
        }
        return income * 1000;
    }

    public long getSystemCost(Long currentAttackedSystem) {

        long cost = 0L;

        ArrayList<StarSystemDataPOJO> starSystemDataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();
        for (StarSystemDataPOJO starSystemData : starSystemDataListHH) {
            if (starSystemData.getFactionID().getId().equals(myFactionId) && !Objects.equals(currentAttackedSystem, starSystemData.getStarSystemID().getId())) {

                switch (starSystemData.getLevel().intValue()) {
                    case 1 -> // Regular
                            cost = cost + Constants.REGULAR_SYSTEM_GENERAL_COST;
                    case 2 -> // Industrial
                            cost = cost + Constants.INDUSTRIAL_SYSTEM_GENERAL_COST;
                    case 3 -> // Capital
                            cost = cost + Constants.CAPITAL_SYSTEM_GENERAL_COST;
                }
            }
        }
        return cost * 1000;
    }
}
