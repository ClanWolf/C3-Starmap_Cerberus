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
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.clanwolf.starmap.client.process.universe.BOUniverse;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.UniverseDTO;
import net.clanwolf.starmap.transfer.dtos.UserDTO;

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

	private static int currentSeason;
	private static int currentRound;
	private static String currentDate;

	private static String lastAvailableClientVersion = "not checked yet";

	private static AbstractC3Pane currentlyOpenedPane = null;
	private static boolean isDevelopmentPC = false;
	private static boolean clearCacheOnStart = false;

	private static BOStarSystem terra = null;
	private static BOStarSystem currentlySelectedStarSystem = null;

	/**
	 * Private constructor to prevent instantiation
	 */
	private Nexus() {

	}

	public static void setTerra(BOStarSystem s) {
		terra = s;
	}

	public static BOStarSystem getTerra() {
		return terra;
	}

	public static void setCurrentlySelectedStarSystem(BOStarSystem s) {
		currentlySelectedStarSystem = s;
	}

	public static BOStarSystem getCurrentlySelectedStarSystem() {
		return currentlySelectedStarSystem;
	}

	public static void setSelectedStarSystem(BOStarSystem s) {
		boSelectedStarSystem = s;
	}
	public BOStarSystem getSelectedStarSystem() {
		return boSelectedStarSystem;
	}

	public static void setCurrentSeason(int v) {
		currentSeason = v;
	}
	public static void setCurrentRound(int v) {
		currentRound = v;
	}
	public static void setCurrentDate(String v) {
		currentDate = v;
	}

	public static int getCurrentSeason() { return currentSeason; }
	public static int getCurrentRound() { return currentRound; }
	public static String getCurrentDate() { return currentDate; }

	public static BOUniverse getBoUniverse() {
		return boUniverse;
	}

	public static void setBOUniverse(BOUniverse boUni) {
		boUniverse = boUni;
	}

	public static UniverseDTO getUniverseDTO() {
		return universeDTO;
	}

	public static void setUniverseDTO(UniverseDTO uni) {
		universeDTO = uni;
	}

	/**
	 * Set the currently logged in user
	 *
	 * @param user user
	 */
	public static void setUser(UserDTO user) {
		currentUser = user;
	}

	public static void setChar(RolePlayCharacterDTO character) {
		currentChar = character;
	}

	/**
	 * Returns whether the client is logged in or not
	 *
	 * @return login state
	 */
	public static boolean isLoggedIn() {
		return loggedIn && (currentUser != null);
	}

	public static void setLoggedInStatus(boolean value) {
		loggedIn = value;
	}

	public static void resetAfterLogout() {
		setUser(null);
		setMyPlayerSessionID(null);
		setSession(null);
	}

	public static AbstractC3Pane getCurrentlyOpenedPane() {
		return currentlyOpenedPane;
	}

	public static void setCurrentlyOpenedPane(AbstractC3Pane currentlyOpenedPane) {
		Nexus.currentlyOpenedPane = currentlyOpenedPane;
	}

	/**
	 * Get the currently logged in user
	 *
	 * @return UserDTO
	 */
	public static UserDTO getCurrentUser() {
		return currentUser;
	}

	/**
	 * Get the currently selected char for the user
	 *
	 * @return char
	 */
	public static RolePlayCharacterDTO getCurrentChar() {
		currentChar = currentUser.getCurrentCharacter();
		return currentChar;
	}

	/**
	 * @return the myPlayerSessionID
	 */
	public static Object getMyPlayerSessionID() {
		return myPlayerSessionID;
	}

	/**
	 * @param myPlayerSessionID the myPlayerSessionID to set
	 */
	public static void setMyPlayerSessionID(Object myPlayerSessionID) {
		Nexus.myPlayerSessionID = myPlayerSessionID;
	}

	/**
	 * @return the session
	 */
	@SuppressWarnings("unused")
	public static Session getSession() {
		return session;
	}

	/**
	 * @param session the session to set
	 */
	public static void setSession(Session session) {
		Nexus.session = session;
	}

	/**
	 * Send a network event with the given gamestate to the server
	 *
	 * @param gameState gamestate
	 */
	public static void fireNetworkEvent(GameState gameState) {
		NetworkEvent networkEvent = Events.networkEvent(gameState);
		if (session != null) {
			Nexus.session.onEvent(networkEvent);
		}
	}

	/**
	 * @return the mainFrameEnabled
	 */
	@SuppressWarnings("unused")
	public static boolean isMainFrameEnabled() {
		return mainFrameEnabled;
	}

	/**
	 * @param mainFrameEnabled the mainFrameEnabled to set
	 */
	public static void setMainFrameEnabled(boolean mainFrameEnabled) {
		Nexus.mainFrameEnabled = mainFrameEnabled;
	}

	public static boolean isDevelopmentPC(){
		return isDevelopmentPC;
	}
	public static void setIsDevelopmentPC(boolean devPC){
		isDevelopmentPC = devPC;
	}
	public static void setClearCacheOnStart(boolean v) {
		clearCacheOnStart = v;
	}
	public static boolean isClearCacheOnStart() {
		return clearCacheOnStart;
	}
	public static void setLastAvailableClientVersion(String v) {
		lastAvailableClientVersion = v;
	}
	public static String getLastAvailableClientVersion() {
		return lastAvailableClientVersion;
	}
}
