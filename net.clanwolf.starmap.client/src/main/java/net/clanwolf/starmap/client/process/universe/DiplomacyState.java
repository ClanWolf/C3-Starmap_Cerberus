package net.clanwolf.starmap.client.process.universe;

import javafx.scene.image.Image;
import net.clanwolf.starmap.client.util.Internationalization;

import java.util.Objects;

public class DiplomacyState{

	// int weRequestedAlliance = 1; allianceRequestedByThem = 2; allianceWaitingForNextRound = 3; allianceWaitingToBreakNextRound = 4;
	final int diplomacyState = DiplomacyState.NO_ALLIANCE_FOUND;
	public static final int NO_ALLIANCE_FOUND = 0;
	public static final int CURRENT_ALLIANCE_FOUND = 1;
	public static final int PLAYERS_FACTION_REQUEST_CURRENT_ROUND = 2;
	public static final int OTHER_FACTION_REQUEST_CURRENT_ROUND = 3;
	public static final int ALLIANCE_FOUND_FOR_NEXT_ROUND = 4;
	public static final int PLAYERS_FACTION_REQUEST_NEXT_ROUND = 5;
	public static final int OTHER_FACTION_REQUEST_NEXT_ROUND = 6;
	public static final int PLAYERS_FACTION_BREAK_ALLIANCE_NEXT_ROUND = 7;
	public static final int OTHER_FACTION_BREAK_ALLIANCE_NEXT_ROUND = 8;
	public static final int FACTIONS_AT_WAR = 9;

	private final Image diplomacyIconNone = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_enemies.png")));
	private final Image diplomacyIconLeft = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_left.png")));
	private final Image diplomacyIconRight = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_right.png")));
	private final Image diplomacyIconAllied = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_allies.png")));
	private final Image diplomacyIconAlliedWaiting = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_allies_waiting.png")));
	private final Image diplomacyIconAlliedBrokenWaiting = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/buttons/diplomacy_enemies_waiting.png")));
	private final Image diplomacyIconAtWar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/map/attack2.png")));


	Image diplomacyIcon;
	int stateValue = DiplomacyState.NO_ALLIANCE_FOUND;
	String diplomanyTheirStateText = "";

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

		switch(stateValue){
			case DiplomacyState.CURRENT_ALLIANCE_FOUND:
				diplomacyIcon = diplomacyIconAllied;
				break;
			case DiplomacyState.PLAYERS_FACTION_REQUEST_CURRENT_ROUND:
			case DiplomacyState.PLAYERS_FACTION_REQUEST_NEXT_ROUND:
				diplomacyIcon = diplomacyIconRight;
				break;
			case DiplomacyState.OTHER_FACTION_REQUEST_CURRENT_ROUND:
			case DiplomacyState.OTHER_FACTION_REQUEST_NEXT_ROUND:
				diplomacyIcon = diplomacyIconLeft;
				break;
			case DiplomacyState.ALLIANCE_FOUND_FOR_NEXT_ROUND:
				diplomacyIcon = diplomacyIconAlliedWaiting;
				break;
			case DiplomacyState.PLAYERS_FACTION_BREAK_ALLIANCE_NEXT_ROUND:
			case DiplomacyState.OTHER_FACTION_BREAK_ALLIANCE_NEXT_ROUND:
				diplomacyIcon = diplomacyIconAlliedBrokenWaiting;
				break;
			case DiplomacyState.FACTIONS_AT_WAR:
				diplomacyIcon = diplomacyIconAtWar;
				break;
			default:
				diplomacyIcon = diplomacyIconNone;
		}
		return diplomacyIcon;
	}

	public String getDiplomacyTheirStateText(){

		boolean retValue = false;

		switch(stateValue){
			//case DiplomacyState.PLAYERS_FACTION_REQUEST_CURRENT_ROUND:
			//case DiplomacyState.PLAYERS_FACTION_REQUEST_NEXT_ROUND:
			case DiplomacyState.CURRENT_ALLIANCE_FOUND:
			case DiplomacyState.OTHER_FACTION_REQUEST_CURRENT_ROUND:
			case DiplomacyState.OTHER_FACTION_REQUEST_NEXT_ROUND:
			case DiplomacyState.ALLIANCE_FOUND_FOR_NEXT_ROUND:
			case DiplomacyState.PLAYERS_FACTION_BREAK_ALLIANCE_NEXT_ROUND:
			case DiplomacyState.OTHER_FACTION_BREAK_ALLIANCE_NEXT_ROUND:
				diplomanyTheirStateText = Internationalization.getString("app_diplomacy_column_StatusFriendly");
				break;
			case DiplomacyState.FACTIONS_AT_WAR:
				diplomanyTheirStateText = "Krieg erklärt";
				break;
			default:
				diplomanyTheirStateText = Internationalization.getString("app_diplomacy_column_StatusEnemy");
		}
		return diplomanyTheirStateText;
	}

	public  Image getFutureStateIcon(boolean weFriendly){

		switch(this.getState()) {
			case DiplomacyState.NO_ALLIANCE_FOUND -> {
				// theyFriendly = false;
				if (weFriendly) {
					diplomacyIcon = diplomacyIconRight;
				} else {
					diplomacyIcon = diplomacyIconNone;
				}
			}
			case DiplomacyState.CURRENT_ALLIANCE_FOUND -> {
				// theyFriendly = true;
				if (weFriendly) {
					diplomacyIcon = diplomacyIconAllied;
				} else {
					diplomacyIcon = diplomacyIconAlliedBrokenWaiting;
				}
			}
			case DiplomacyState.PLAYERS_FACTION_REQUEST_CURRENT_ROUND -> {
				// theyFriendly = false;
				if (weFriendly) {
					diplomacyIcon = diplomacyIconRight;
				} else {
					diplomacyIcon = diplomacyIconNone;
				}
			}
			case DiplomacyState.OTHER_FACTION_REQUEST_CURRENT_ROUND -> {
				// theyFriendly = true;
				if (weFriendly) {
					diplomacyIcon = diplomacyIconAlliedWaiting;
				} else {
					diplomacyIcon = diplomacyIconLeft;
				}
			}
			case DiplomacyState.ALLIANCE_FOUND_FOR_NEXT_ROUND -> {
				// theyFriendly = true;
				if (weFriendly) {
					diplomacyIcon = diplomacyIconAlliedWaiting;
				} else {
					diplomacyIcon = diplomacyIconAlliedWaiting;
				}
			}
			case DiplomacyState.PLAYERS_FACTION_REQUEST_NEXT_ROUND -> {
				// theyFriendly = false;
				if (weFriendly) {
					diplomacyIcon = diplomacyIconRight;
				} else {
					diplomacyIcon = diplomacyIconNone;
				}
			}
			case DiplomacyState.OTHER_FACTION_REQUEST_NEXT_ROUND -> {
				// theyFriendly = true;
				if (weFriendly) {
					diplomacyIcon = diplomacyIconAlliedWaiting;
				} else {
					diplomacyIcon = diplomacyIconLeft;
				}
			}
			case DiplomacyState.PLAYERS_FACTION_BREAK_ALLIANCE_NEXT_ROUND -> {
				// theyFriendly = true;
				if (weFriendly) {
					diplomacyIcon = diplomacyIconAlliedBrokenWaiting;
				} else {
					diplomacyIcon = diplomacyIconAlliedBrokenWaiting;
				}
			}
			case DiplomacyState.OTHER_FACTION_BREAK_ALLIANCE_NEXT_ROUND -> {
				// theyFriendly = false;
				if (weFriendly) {
					diplomacyIcon = diplomacyIconAlliedBrokenWaiting;
				} else {
					diplomacyIcon = diplomacyIconAlliedBrokenWaiting;
				}
			}
			case DiplomacyState.FACTIONS_AT_WAR -> {
				// theyFriendly = false;
				if (weFriendly) {
					diplomacyIcon = diplomacyIconAtWar;
				} else {
					diplomacyIcon = diplomacyIconAtWar;
				}
			}
		}

		return diplomacyIcon;
	}
}
