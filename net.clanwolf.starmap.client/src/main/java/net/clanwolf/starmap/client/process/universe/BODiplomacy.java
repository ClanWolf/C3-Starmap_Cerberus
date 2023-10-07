package net.clanwolf.starmap.client.process.universe;

import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.transfer.dtos.DiplomacyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;

public class BODiplomacy {

	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private int currentRound;
	private int nextRound;
	private Long currentFactionID;

	// Key -> RequesterFactionID_AccepterFactionID
	private HashMap<String, DiplomacyDTO> diplomacyMap;

	public BODiplomacy(int currentRound, ArrayList<DiplomacyDTO> diplomacyList){

		this.currentRound = currentRound;
		this.nextRound = currentRound + 1;
		this.currentFactionID = Nexus.getCurrentFaction().getID();

		diplomacyMap = new HashMap();
		for(DiplomacyDTO boDipl : diplomacyList){

			diplomacyMap.put(getKeyFromDTO(boDipl),boDipl);

		} // for

	}

	public DiplomacyState getDiplomacyState(Long currentFactionID, Long otherFactionID){

		DiplomacyState ds = new DiplomacyState();
		ds = getDiplomacyStateNextRound(currentFactionID, otherFactionID);
		if(ds.getState() == DiplomacyState.NO_ALLIANCE_FOUND){
			ds = getDiplomacyStateCurrentRound(currentFactionID, otherFactionID);
		}
		return ds;
	}

	public DiplomacyState getDiplomacyStateCurrentRound(Long factionID){
		return getDiplomacyStateCurrentRound(Nexus.getCurrentFaction().getID(), factionID);
	}

	public DiplomacyState getDiplomacyStateNextRound(Long factionID){
		return getDiplomacyStateNextRound(Nexus.getCurrentFaction().getID(), factionID);
	}

	public boolean factionsAtWar(Long currentFactionID, Long otherFactionID){
		boolean factionsAtWar = false;
		ArrayList<BOAttack> attackList = new ArrayList<>(Nexus.getBoUniverse().attackBOsOpenInThisRound.values());

		for(BOAttack attack : attackList){
			if((attack.getAttackerFactionId().longValue() == currentFactionID && attack.getAttackDTO().getFactionID_Defender().equals(otherFactionID))
			|| (attack.getAttackerFactionId().longValue() == otherFactionID && attack.getAttackDTO().getFactionID_Defender().equals(currentFactionID))
			){
				factionsAtWar = true;
				break;
			}
		}
		return factionsAtWar;
	}

	public DiplomacyState getDiplomacyStateCurrentRound(Long currentFactionID, Long otherFactionID){

		this.currentFactionID = currentFactionID;

		if(factionsAtWar(currentFactionID, otherFactionID)){
			DiplomacyState dsAtWar = new DiplomacyState();
			dsAtWar.setState(DiplomacyState.FACTIONS_AT_WAR);
			return dsAtWar;
		}

		boolean currentFactionAllied = false;
		boolean otherFactionAllied = false;

		String keyOtherFaction = getKeyForOtherFaction(otherFactionID);
		String keyCurrentFaction = getKeyForCurrentFaction(otherFactionID);

		DiplomacyDTO otherFactionDTO = diplomacyMap.get(keyOtherFaction);
		DiplomacyDTO currentFactionDTO = diplomacyMap.get(keyCurrentFaction);

		if(currentFactionDTO != null && currentFactionDTO.getStartingInRound() <= currentRound){
			currentFactionAllied = true;
		}

		if(otherFactionDTO != null && otherFactionDTO.getStartingInRound() <= currentRound){
			otherFactionAllied = true;
		}

		DiplomacyState ds = new DiplomacyState();

		if(currentFactionAllied && otherFactionAllied) {
			ds.setState(DiplomacyState.CURRENT_ALLIANCE_FOUND);
			logger.info("Diplomacy state -> " + otherFactionID + " / CURRENT_ALLIANCE_FOUND");

		} else if(currentFactionAllied && !otherFactionAllied) {
			ds.setState(DiplomacyState.PLAYERS_FACTION_REQUEST_CURRENT_ROUND);
			logger.info("Diplomacy state -> " + otherFactionID + " / CURRENT_FACTION_REQUEST_WITHOUT_ANSWER");

		} else if(!currentFactionAllied && otherFactionAllied) {
			//request from own faction witout accept from the other one
			ds.setState(DiplomacyState.OTHER_FACTION_REQUEST_CURRENT_ROUND);
			logger.info("Diplomacy state -> " + otherFactionID + " / OTHER_FACTION_REQUEST");

		} else {
			ds.setState(DiplomacyState.NO_ALLIANCE_FOUND);
			logger.info("Diplomacy state -> " + otherFactionID + " / NO_ALLIANCE_FOUND");
		}

		return ds;
	}

	public DiplomacyState getDiplomacyStateNextRound(Long cueentFactionID, Long otherFactionID){

		this.currentFactionID = currentFactionID;

		boolean currentFactionAlliedNextRound = false;
		boolean otherFactionAlliedNextRound = false;
		boolean currentFactionBreakAlliedNextRound = false;
		boolean otherFactionBreakAlliedNextRound = false;
		boolean currentFactionAlliedCurrentRound = false;
		boolean otherFactionAlliedCurrentRound = false;

		String keyOtherFaction = getKeyForOtherFaction(otherFactionID);
		String keyCurrentFaction = getKeyForCurrentFaction(otherFactionID);

		DiplomacyDTO otherFactionDTO = diplomacyMap.get(keyOtherFaction);
		DiplomacyDTO currentFactionDTO = diplomacyMap.get(keyCurrentFaction);

		if(currentFactionDTO != null && currentFactionDTO.getStartingInRound() >= nextRound ){
			currentFactionAlliedNextRound = true;
		}
		if(currentFactionDTO != null &&  currentFactionDTO.getStartingInRound() < nextRound){
			currentFactionAlliedCurrentRound = true;
		}
		if(otherFactionDTO != null && otherFactionDTO.getStartingInRound() < nextRound){
			otherFactionAlliedCurrentRound = true;
		}
		if(currentFactionDTO != null && currentFactionDTO.getEndingInRound() != null && currentFactionDTO.getEndingInRound() != null && currentFactionDTO.getEndingInRound() >= nextRound){
			currentFactionBreakAlliedNextRound = true;
		};
		if(otherFactionDTO != null && otherFactionDTO.getStartingInRound() >= nextRound){
			otherFactionAlliedNextRound = true;
		}
		if(otherFactionDTO != null && otherFactionDTO.getEndingInRound() != null && otherFactionDTO.getEndingInRound()>= nextRound){
			otherFactionBreakAlliedNextRound = true;
		};


		DiplomacyState ds = new DiplomacyState();

		if((currentFactionAlliedNextRound && otherFactionAlliedCurrentRound) ||
				(currentFactionAlliedCurrentRound  && otherFactionAlliedNextRound) ||
				(currentFactionAlliedNextRound && otherFactionAlliedNextRound)) {
			ds.setState(DiplomacyState.ALLIANCE_FOUND_FOR_NEXT_ROUND);
			logger.info("Diplomacy state next round -> " + otherFactionID + " / CURRENT_ALLIANCE_FOUND_NEXT_ROUND");

		} else if(currentFactionAlliedNextRound && !otherFactionAlliedNextRound && !otherFactionAlliedCurrentRound) {
			ds.setState(DiplomacyState.PLAYERS_FACTION_REQUEST_NEXT_ROUND);
			logger.info("Diplomacy state  next round -> " + otherFactionID + " / CURRENT_FACTION_REQUEST_NEXT_ROUND");

		} else if(!currentFactionAlliedNextRound && !currentFactionAlliedCurrentRound && otherFactionAlliedNextRound) {
			ds.setState(DiplomacyState.OTHER_FACTION_REQUEST_NEXT_ROUND);
			logger.info("Diplomacy state next round -> " + otherFactionID + " / OTHER_FACTION_REQUEST_NEXT_ROUND");

		} else if(currentFactionBreakAlliedNextRound) {
			ds.setState(DiplomacyState.PLAYERS_FACTION_BREAK_ALLIANCE_NEXT_ROUND);
			logger.info("Diplomacy state next round -> " + otherFactionID + " / CURRENT_FACTION_BREAK_ALLIANCE_NEXT_ROUND");

		} else if(otherFactionBreakAlliedNextRound) {
			ds.setState(DiplomacyState.OTHER_FACTION_BREAK_ALLIANCE_NEXT_ROUND);
			logger.info("Diplomacy state next round -> " + otherFactionID + " / OTHER_FACTION_BREAK_ALLIANCE_NEXT_ROUND");

		} else {
			ds.setState(DiplomacyState.NO_ALLIANCE_FOUND);
			logger.info("Diplomacy state next round -> " + otherFactionID + " / NO_ALLIANCE_FOUND");
		}

		return ds;
	}

	private String getKeyFromDTO(DiplomacyDTO dipDTO){

		String keyRequester = "0";
		String keyAccepter = "0";

		if(dipDTO.getFactionID_REQUEST() != null){
			keyRequester = dipDTO.getFactionID_REQUEST().toString();
		}

		if(dipDTO.getFactionID_ACCEPTED() != null){
			keyAccepter = dipDTO.getFactionID_ACCEPTED().toString();
		}

		return keyRequester + "_" + keyAccepter;

	}

	private String getKeyForOtherFaction(Long requesterID){

		return requesterID.toString() + "_" + currentFactionID.toString();

	}

	private String getKeyForCurrentFaction(Long accepterID){

		return currentFactionID.toString() + "_" + accepterID.toString();

	}
}
