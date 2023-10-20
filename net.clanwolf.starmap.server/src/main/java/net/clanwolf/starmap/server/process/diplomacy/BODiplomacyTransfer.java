package net.clanwolf.starmap.server.process.diplomacy;

import net.clanwolf.starmap.server.persistence.pojos.DiplomacyPOJO;

import java.util.ArrayList;
import java.util.HashMap;

public class BODiplomacyTransfer {

	private HashMap<String, DiplomacyPOJO> diplomacyMap;

	public BODiplomacyTransfer(ArrayList<DiplomacyPOJO> diplomacyList){
		diplomacyMap = new HashMap();
		for(DiplomacyPOJO boDipl : diplomacyList){

			diplomacyMap.put(getKeyFromDTO(boDipl),boDipl);

		} // for
	}

	public ArrayList<DiplomacyPOJO> getDiplomacyStateForFactions(Long factionID1, Long factionID2){
		ArrayList<DiplomacyPOJO> list = new ArrayList<>();

		DiplomacyPOJO p1 = diplomacyMap.get(getKeyForOtherFaction(factionID1, factionID2));
		if (p1 != null) {
			list.add(p1);
		}
		DiplomacyPOJO p2 = diplomacyMap.get(getKeyForCurrentFaction(factionID1, factionID2));
		if (p2 != null) {
			list.add(p2);
		}

		return list;
	}

	public boolean factionsAreAllied(Long factionID1, Long factionID2, Long round){

		DiplomacyPOJO dip1 = diplomacyMap.get(getKeyForOtherFaction(factionID1, factionID2));
		DiplomacyPOJO dip2 = diplomacyMap.get(getKeyForCurrentFaction(factionID1, factionID2));

		if( dip1 != null && dip2 != null){
			if (dip1.getStartingInRound() > round || dip2.getStartingInRound() > round) {
				return false;
			}
			return true;
		}

		return false;
	}

	private String getKeyFromDTO(DiplomacyPOJO dipPOJO){

		String keyRequester = "0";
		String keyAccepter = "0";

		if(dipPOJO.getFactionID_REQUEST() != null){
			keyRequester = dipPOJO.getFactionID_REQUEST().toString();
		}

		if(dipPOJO.getFactionID_ACCEPTED() != null){
			keyAccepter = dipPOJO.getFactionID_ACCEPTED().toString();
		}

		return keyRequester + "_" + keyAccepter;
	}

	private String getKeyForOtherFaction(Long requesterID, Long accepterID){

		return requesterID.toString() + "_" + accepterID.toString();

	}

	private String getKeyForCurrentFaction(Long accepterID, Long requesterID){

		return requesterID.toString() + "_" + accepterID.toString();

	}
}
