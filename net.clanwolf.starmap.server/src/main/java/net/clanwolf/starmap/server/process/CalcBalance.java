package net.clanwolf.starmap.server.process;

import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayCharacterDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayCharacterStatsDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackStatsPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayCharacterPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayCharacterStatsPOJO;
import net.clanwolf.starmap.transfer.mwo.MechIdInfo;

import java.util.ArrayList;

public class CalcBalance {

    private double AttackerRepairCost;
    private double DefenderRepairCost;
    private ArrayList<RolePlayCharacterStatsPOJO> DefenderPlayerRepairCost;
    private ArrayList<RolePlayCharacterStatsPOJO> attackerPlayerRepaiCost;


    public double getDefenderCost() {
        return DefenderRepairCost;
    }

    public void setDefenderCost(double defenderCost) {
        DefenderRepairCost = defenderCost;
    }

    public double getAttackerCost() {
        return AttackerRepairCost;
    }

    public void setAttackerCost(double attackerCost) {
        AttackerRepairCost = attackerCost;
    }

    public   CalcBalance(AttackStatsPOJO rpcs){

        String mwomatchid = rpcs.getMwoMatchId();
        RolePlayCharacterStatsDAO DAO = RolePlayCharacterStatsDAO.getInstance();
        RolePlayCharacterDAO CharacterDAO = RolePlayCharacterDAO.getInstance();
        ArrayList<RolePlayCharacterStatsPOJO> list = DAO.findByMatchId(mwomatchid);

        for(RolePlayCharacterStatsPOJO pojo:list){

            RolePlayCharacterPOJO character = CharacterDAO.findById(Nexus.DUMMY_USERID, pojo.getRoleplayCharacterId());
            MechIdInfo MI = new MechIdInfo(Math.toIntExact(pojo.getMechItemId()));

            //Attacker
            if(rpcs.getAttackerFactionId() == character.getFactionId().longValue()){


                AttackerRepairCost = AttackerRepairCost + MI.getRepairCost(Math.toIntExact(pojo.getMwoSurvivalPercentage()));

            }

            //Defender
            if(rpcs.getDefenderFactionId() == character.getFactionId().longValue()){

                DefenderRepairCost = DefenderRepairCost + MI.getRepairCost(Math.toIntExact(pojo.getMwoSurvivalPercentage()));

            }
        }

    }

}
