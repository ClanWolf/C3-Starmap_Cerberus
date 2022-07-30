package net.clanwolf.starmap.server.process;

import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.StarSystemDataDAO;
import net.clanwolf.starmap.server.persistence.pojos.StarSystemDataPOJO;

import java.util.ArrayList;

public class BalanceUserInfo {
    public String userName;
    public long damage;
    public long componentDestroyed;
    public long kills;
    public long matchScore;
    public long mechHealth;
    public String mechName;
    public long mechRepairCost;
    public long rewardComponentsDestroyed;
    public long rewardMatchScore;
    public long rewardDamage;
    public long rewardTeamDamage;
    public long teamDamage;
    public long rewardKill;
    public Long subTotal;
    public Faction faction;

    BalanceUserInfo(long factionID) {
        faction = new Faction(factionID);
    }

    static class Faction {

        public Faction(long factionID) {
            long income = 0L;
            long cost = 0L;

            ArrayList<StarSystemDataPOJO> starSystemDataListHH = StarSystemDataDAO.getInstance().getAll_HH_StarSystemData();
            for (StarSystemDataPOJO starSystemData : starSystemDataListHH) {
                if (starSystemData.getFactionID().getId().equals(factionID)) {

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
            income = (income + cost) * 1000;
        }

        private long income;
    }
}
