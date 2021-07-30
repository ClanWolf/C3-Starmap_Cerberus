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
 * Copyright (c) 2001-2021, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.nexus;

import io.nadron.client.app.Session;
import io.nadron.client.event.Events;
import io.nadron.client.event.NetworkEvent;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Pane;
import net.clanwolf.starmap.client.gui.panes.logging.LogWatcher;
import net.clanwolf.starmap.client.process.login.Login;
import net.clanwolf.starmap.client.process.universe.BOAttack;
import net.clanwolf.starmap.client.process.universe.BOJumpship;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.AttackCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.UniverseDTO;
import net.clanwolf.starmap.transfer.dtos.UserDTO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Nexus is a central place to hold general information like the currently logged in user and such.
 *
 * @author Meldric
 */
@SuppressWarnings("WeakerAccess")
public class Nexus {
	private static UserDTO currentUser;
	private static RolePlayCharacterDTO currentChar;
	private static Object myPlayerSessionID;
	private static Session session;
	private static boolean mainFrameEnabled;
	private static UniverseDTO universeDTO;
	private static BOUniverse boUniverse;
	private static BOStarSystem boSelectedStarSystem;
	private static boolean loggedIn = false;

	private static BOAttack currentAttackOfUser;
	private static ArrayList<UserDTO> userList;
	private static HashMap<Long, RolePlayCharacterDTO> characterList;
	private static LogWatcher logWatcher;

	private static int currentSeason;
	private static int currentRound;
	private static int currentSeasonMetaPhase;
	private static String currentDate;

	private static String lastAvailableClientVersion = "not checked yet";

	private static AbstractC3Pane currentlyOpenedPane = null;
	private static boolean isDevelopmentPC = false;
	private static boolean clearCacheOnStart = false;

	private static BOStarSystem terra = null;
	private static BOStarSystem homeworld = null;
	private static BOStarSystem currentlySelectedStarSystem = null;
	private static BOJumpship currentlySelectedJumphip = null;

	/**
	 * Private constructor to prevent instantiation
	 */
	private Nexus() {

	}

	@SuppressWarnings("unused")
	public static void setUserList(ArrayList list) {
		userList = list;
	}

	@SuppressWarnings("unused")
	public static ArrayList getUserList() {
		return userList;
	}

	@SuppressWarnings("unused")
	public static void setTerra(BOStarSystem s) {
		terra = s;
	}

	@SuppressWarnings("unused")
	public static void setHomeWorld(BOStarSystem s) {
		homeworld = s;
	}

	@SuppressWarnings("unused")
	public static BOStarSystem getTerra() {
		return terra;
	}

	@SuppressWarnings("unused")
	public static BOStarSystem getHomeworld() { return homeworld; }

	@SuppressWarnings("unused")
	public static void setCurrentlySelectedStarSystem(BOStarSystem s) {
		currentlySelectedStarSystem = s;
	}

	@SuppressWarnings("unused")
	public static BOStarSystem getCurrentlySelectedStarSystem() {
		return currentlySelectedStarSystem;
	}

	@SuppressWarnings("unused")
	public static void setCurrentlySelectedJumpship(BOJumpship j) {
		currentlySelectedJumphip = j;
	}

	@SuppressWarnings("unused")
	public static BOJumpship getCurrentlySelectedJumpship() {
		return currentlySelectedJumphip;
	}

	@SuppressWarnings("unused")
	public static void setSelectedStarSystem(BOStarSystem s) {
		boSelectedStarSystem = s;
	}

	@SuppressWarnings("unused")
	public BOStarSystem getSelectedStarSystem() {
		return boSelectedStarSystem;
	}

	@SuppressWarnings("unused")
	public static void setCurrentSeason(int v) {
		currentSeason = v;
	}

	@SuppressWarnings("unused")
	public static void setCurrentRound(int v) {
		currentRound = v;
	}

	@SuppressWarnings("unused")
	public static void setCurrentDate(String v) {
		currentDate = v;
	}

	@SuppressWarnings("unused")
	public static int getCurrentSeason() { return currentSeason; }

	@SuppressWarnings("unused")
	public static void setCurrentSeasonMetaPhase(int v) { currentSeasonMetaPhase = v; }

	@SuppressWarnings("unused")
	public static int getCurrentSeasonMetaPhase() { return currentSeasonMetaPhase; }

	@SuppressWarnings("unused")
	public static int getCurrentRound() { return currentRound; }

	@SuppressWarnings("unused")
	public static String getCurrentDate() { return currentDate; }

	@SuppressWarnings("unused")
	public static BOUniverse getBoUniverse() {
		return boUniverse;
	}

	@SuppressWarnings("unused")
	public static void setBOUniverse(BOUniverse boUni) {
		boUniverse = boUni;
	}

	@SuppressWarnings("unused")
	public static UniverseDTO getUniverseDTO() {
		return universeDTO;
	}

	@SuppressWarnings("unused")
	public static void setUniverseDTO(UniverseDTO uni) {
		universeDTO = uni;
	}

	@SuppressWarnings("unused")
	public static void setUser(UserDTO user) {
		currentUser = user;
	}

	@SuppressWarnings("unused")
	public static void setChar(RolePlayCharacterDTO character) {
		currentChar = character;
	}

	@SuppressWarnings("unused")
	public static boolean isLoggedIn() {
		return loggedIn && (currentUser != null);
	}

	@SuppressWarnings("unused")
	public static void setLoggedInStatus(boolean value) {
		loggedIn = value;
	}

	@SuppressWarnings("unused")
	public static void resetAfterLogout() {
		setUser(null);
		setMyPlayerSessionID(null);
		setSession(null);
		Login.loginInProgress = false;
	}

	@SuppressWarnings("unused")
	public static AbstractC3Pane getCurrentlyOpenedPane() {
		return currentlyOpenedPane;
	}

	@SuppressWarnings("unused")
	public static void setCurrentlyOpenedPane(AbstractC3Pane currentlyOpenedPane) {
		Nexus.currentlyOpenedPane = currentlyOpenedPane;
	}

	@SuppressWarnings("unused")
	public static UserDTO getCurrentUser() {
		return currentUser;
	}

	@SuppressWarnings("unused")
	public static RolePlayCharacterDTO getCurrentChar() {
		currentChar = currentUser.getCurrentCharacter();
		return currentChar;
	}

	@SuppressWarnings("unused")
	public static boolean userHasAttack() {
		boolean userHasAttack = false;
		if (Nexus.getBoUniverse() != null) {
			for (BOAttack a : Nexus.getBoUniverse().attackBOs) {
				if (a.getAttackCharList() != null) {
					for (AttackCharacterDTO ac : a.getAttackCharList()) {
						if (ac.getCharacterID().equals(Nexus.getCurrentUser().getCurrentCharacter().getId())) {
							// The user currently logged in has joined an attack that was not resolved yet
							userHasAttack = true;
							currentAttackOfUser = a;
							break;
						}
					}
				}
			}
		}
		return userHasAttack;
	}

	@SuppressWarnings("unused")
	public static BOAttack getCurrentAttackOfUser() {
		if (currentAttackOfUser == null) {
			userHasAttack(); // to set currentAttack to user
		}
		return currentAttackOfUser;
	}

	@SuppressWarnings("unused")
	public static Object getMyPlayerSessionID() {
		return myPlayerSessionID;
	}

	@SuppressWarnings("unused")
	public static void setMyPlayerSessionID(Object myPlayerSessionID) {
		Nexus.myPlayerSessionID = myPlayerSessionID;
	}

	@SuppressWarnings("unused")
	public static Session getSession() {
		return session;
	}

	@SuppressWarnings("unused")
	public static void setSession(Session session) {
		Nexus.session = session;
	}

	@SuppressWarnings("unused")
	public static void fireNetworkEvent(GameState gameState) {
		NetworkEvent networkEvent = Events.networkEvent(gameState);
		if (session != null) {
			Nexus.session.onEvent(networkEvent);
		} else {
			C3Logger.info("Session is null! Event NOT sent!");
		}
	}

	@SuppressWarnings("unused")
	public static boolean isMainFrameEnabled() {
		return mainFrameEnabled;
	}

	@SuppressWarnings("unused")
	public static void setMainFrameEnabled(boolean mainFrameEnabled) {
		Nexus.mainFrameEnabled = mainFrameEnabled;
	}

	@SuppressWarnings("unused")
	public static boolean isDevelopmentPC(){
		return isDevelopmentPC;
	}

	@SuppressWarnings("unused")
	public static void setIsDevelopmentPC(boolean devPC){
		isDevelopmentPC = devPC;
	}

	@SuppressWarnings("unused")
	public static void setClearCacheOnStart(boolean v) {
		clearCacheOnStart = v;
	}

	@SuppressWarnings("unused")
	public static boolean isClearCacheOnStart() {
		return clearCacheOnStart;
	}

	@SuppressWarnings("unused")
	public static void setLastAvailableClientVersion(String v) {
		lastAvailableClientVersion = v;
	}

	@SuppressWarnings("unused")
	public static String getLastAvailableClientVersion() {
		return lastAvailableClientVersion;
	}

	@SuppressWarnings("unused")
	public static LogWatcher getLogWatcher() {
		return logWatcher;
	}

	@SuppressWarnings("unused")
	public static void setLogWatcher(LogWatcher logWatcher) {
		Nexus.logWatcher = logWatcher;
	}

	@SuppressWarnings("unused")
	public static RolePlayCharacterDTO getCharacterById(Long id) {
		return characterList.get(id);
	}

	@SuppressWarnings("unused")
	public static HashMap<Long, RolePlayCharacterDTO> getCharacterList() {
		return characterList;
	}

	@SuppressWarnings("unused")
	public static void setCharacterList(HashMap<Long, RolePlayCharacterDTO> characterList) {
		Nexus.characterList = characterList;
	}
}
