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

    public static void calc(AttackStatsPOJO rpcs){
        String mwomatchid = rpcs.getMwoMatchId();
        RolePlayCharacterStatsDAO DAO = RolePlayCharacterStatsDAO.getInstance();
        RolePlayCharacterDAO CharacterDAO =RolePlayCharacterDAO.getInstance();
        ArrayList<RolePlayCharacterStatsPOJO> list = DAO.findByMatchId(mwomatchid);


        for(RolePlayCharacterStatsPOJO pojo:list){


            RolePlayCharacterPOJO character = CharacterDAO.findById(Nexus.DUMMY_USERID, pojo.getRoleplayCharacterId());

            //Attacker
            if(rpcs.getAttackerFactionId()== character.getFactionId().longValue()){

                //pojo.getMechItemId()
                //pojo.getMwoSurvivalPercentage()

            }

            //Defnder
            if(rpcs.getDefenderFactionId()== character.getFactionId().longValue()){

                //pojo.getMechItemId()
                //pojo.getMwoSurvivalPercentage()

            }
        }

        // rpcs.getMechItemId()
        //rpcs.getMwoSurvivalPercentage()

    }

}
