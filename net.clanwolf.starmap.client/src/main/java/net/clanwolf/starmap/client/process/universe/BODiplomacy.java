package net.clanwolf.starmap.client.process.universe;

import javafx.scene.image.Image;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.transfer.dtos.DiplomacyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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

	public DiplomacyState getDiplomacyStateCurrentRound(Long factionID){
		return getDiplomacyStateCurrentRound(Nexus.getCurrentFaction().getID(), factionID);
	}

	public DiplomacyState getDiplomacyStateNextRound(Long factionID){
		return getDiplomacyStateNextRound(Nexus.getCurrentFaction().getID(), factionID);
	}

	public DiplomacyState getDiplomacyStateCurrentRound(Long currentFactionID, Long otherFactionID){

		this.currentFactionID = currentFactionID;
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
			ds.setState(DiplomacyState.CURRENT_FACTION_REQUEST);
			logger.info("Diplomacy state -> " + otherFactionID + " / CURRENT_FACTION_REQUEST_WITHOUT_ANSWER");

		} else if(!currentFactionAllied && otherFactionAllied) {
			//request from own faction witout accept from the other one
			ds.setState(DiplomacyState.OTHER_FACTION_REQUEST);
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

		String keyOtherFaction = getKeyForOtherFaction(otherFactionID);
		String keyCurrentFaction = getKeyForCurrentFaction(otherFactionID);

		DiplomacyDTO otherFactionDTO = diplomacyMap.get(keyOtherFaction);
		DiplomacyDTO currentFactionDTO = diplomacyMap.get(keyCurrentFaction);

		if(currentFactionDTO != null && currentFactionDTO.getStartingInRound() >= nextRound){
			currentFactionAlliedNextRound = true;
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

		if(currentFactionAlliedNextRound && otherFactionAlliedNextRound) {
			ds.setState(DiplomacyState.CURRENT_ALLIANCE_FOUND_NEXT_ROUND);
			logger.info("Diplomacy state next round -> " + otherFactionID + " / CURRENT_ALLIANCE_FOUND_NEXT_ROUND");

		} else if(currentFactionAlliedNextRound && !otherFactionAlliedNextRound) {
			ds.setState(DiplomacyState.CURRENT_FACTION_REQUEST_NEXT_ROUND);
			logger.info("Diplomacy state  next round -> " + otherFactionID + " / CURRENT_FACTION_REQUEST_NEXT_ROUND");

		} else if(!currentFactionAlliedNextRound && otherFactionAlliedNextRound) {
			ds.setState(DiplomacyState.OTHER_FACTION_REQUEST_NEXT_ROUND);
			logger.info("Diplomacy state next round -> " + otherFactionID + " / OTHER_FACTION_REQUEST_NEXT_ROUND");

		} else if(currentFactionBreakAlliedNextRound) {
			ds.setState(DiplomacyState.CURRENT_FACTION_BREAK_ALLIANCE_NEXT_ROUND);
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

	class DiplomacyState{

		// int weRequestedAlliance = 1; allianceRequestedByThem = 2; allianceWaitingForNextRound = 3; allianceWaitingToBreakNextRound = 4;
		final int diplomacyState = DiplomacyState.NO_ALLIANCE_FOUND;

		public static final int NO_ALLIANCE_FOUND = 0;
		public static final int CURRENT_ALLIANCE_FOUND = 1;
		public static final int CURRENT_FACTION_REQUEST = 2;
		public static final int OTHER_FACTION_REQUEST = 3;
		public static final int CURRENT_ALLIANCE_FOUND_NEXT_ROUND = 4;
		public static final int CURRENT_FACTION_REQUEST_NEXT_ROUND = 5;
		public static final int OTHER_FACTION_REQUEST_NEXT_ROUND = 6;
		public static final int CURRENT_FACTION_BREAK_ALLIANCE_NEXT_ROUND = 7;
		public static final int OTHER_FACTION_BREAK_ALLIANCE_NEXT_ROUND = 8;

		private final Image diplomacyIconNone = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_enemies.png")));
		private final Image diplomacyIconLeft = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_left.png")));
		private final Image diplomacyIconRight = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_right.png")));
		private final Image diplomacyIconAllied = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_allies.png")));
		private final Image diplomacyIconAlliedWaiting = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_allies_waiting.png")));
		private final Image diplomacyIconAlliedBrokenWaiting = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_enemies_waiting.png")));


		Image diplomacyIcon;
		int stateValue = DiplomacyState.NO_ALLIANCE_FOUND;

		public DiplomacyState(){

		}

		public boolean getFactionsAreAllied(){
			return stateValue == DiplomacyState.CURRENT_ALLIANCE_FOUND;
		}

		public void setState(int state){
			stateValue = state;
		}

		public int getState(){ return stateValue;}

		public Image getDiplomacyIcon(){

			boolean retValue = false;

			if(stateValue == DiplomacyState.CURRENT_ALLIANCE_FOUND){
				diplomacyIcon = null;
			} else if(stateValue == DiplomacyState.CURRENT_FACTION_REQUEST){
				diplomacyIcon = null;
			} else if(stateValue == DiplomacyState.OTHER_FACTION_REQUEST){
				diplomacyIcon = null;
			}

			return diplomacyIcon;
		}

	}
}
