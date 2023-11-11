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
package net.clanwolf.starmap.client.nexus;

import io.nadron.client.app.Session;
import io.nadron.client.event.Events;
import io.nadron.client.event.NetworkEvent;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Pane;
//import net.clanwolf.starmap.client.gui.panes.logging.LogWatcher;
import net.clanwolf.starmap.client.process.login.Login;
import net.clanwolf.starmap.client.process.universe.*;
import net.clanwolf.starmap.transfer.dtos.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.transfer.GameState;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Nexus is a central place to hold general information like the currently logged in user and such.
 *
 * @author Meldric
 */
@SuppressWarnings("WeakerAccess")
public class Nexus {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static Double initialWidth = null;
	private static Double initialHeight = null;

	private static UserDTO currentUser;
	private static RolePlayCharacterDTO currentChar;
	private static Object myPlayerSessionID;
	private static Session session;
	private static boolean mainFrameEnabled;
	private static BOUniverse boUniverse;
	private static BOStarSystem boSelectedStarSystem;
	private static boolean loggedIn = false;

	private static BOAttack currentAttackOfUser;
	private static ArrayList<BOAttack> finishedAttacksOfUser;
	private static ArrayList<UserDTO> userList;
	private static ArrayList<UserDTO> currentlyOnlineUserList;
	private static HashMap<Long, RolePlayCharacterDTO> characterList;
//	private static LogWatcher logWatcher;

	private static Timer serverHeartBeatTimer = null;

	private static BOFaction currentFaction;
	private static int currentSeason;
	private static int currentRound;
	private static int currentSeasonMetaPhase;
	private static String currentDate;

	private static long hoursLeftInThisRound;
	private static String lastAvailableClientVersion = "not checked yet";

	private static AbstractC3Pane currentlyOpenedPane = null;
	private static boolean isDevelopmentPC = false;
	private static boolean clearCacheOnStart = false;

	private static BOStarSystem terra = null;
	private static BOStarSystem homeworld = null;
	private static BOStarSystem currentlySelectedStarSystem = null;
	private static BOJumpship currentlySelectedJumphip = null;

	public static LinkedList<String> commandHistory = new LinkedList<>();
	public static int commandHistoryIndex = 0;
	public static File commandLogFile = null;
	public static boolean promptNewVersionInstall = false;

	public static Long storyID_beforeSavingRespones;
	public static Long lastSavedAttackStarSystemDataId;
	public static Slider mainframeVolumeSlider = null;

	public static java.sql.Timestamp lastServerHeartbeatTimestamp = null;

	public static void setMainFrameVolumeSlider(Slider slider) {
		mainframeVolumeSlider = slider;
	}

	public static boolean mwoCheckingActive = false;
	public static Timer checkSystemClipboardForMWOResultTimer = null;

	public static Timer getCheckSystemClipboardForMWOResultTimer() {
		return checkSystemClipboardForMWOResultTimer;
	}

	public static Image factionLogo;
	public static Image loggedOnUserImage;

	public static AtomicBoolean oneOrMoreMessageOpen = new AtomicBoolean(false);

	public static void setCheckSystemClipboardForMWOResultTimer(Timer checkSystemClipboardForMWOResultTimerV) {
		checkSystemClipboardForMWOResultTimer = checkSystemClipboardForMWOResultTimerV;
	}

	/**
	 * Private constructor to prevent instantiation
	 */
	private Nexus() {

	}

	public static Long getLastSavedAttackStarSystemDataId() {
		return lastSavedAttackStarSystemDataId;
	}

	public static void setLastSavedAttackStarSystemDataId(Long id) {
		lastSavedAttackStarSystemDataId = id;
	}

	public static Image getFactionLogo() {
		return factionLogo;
	}

	public static Image getCurrentCharImage() {
		return loggedOnUserImage;
	}

	public static void setFactionLogo(Image logo) {
		factionLogo = logo;
	}

	public static void setLoggedOnUserImage(Image charImage) { loggedOnUserImage = charImage; }

	public static void setMWOCheckingActive(boolean value) {
		mwoCheckingActive = value;
	}

	public static boolean isMwoCheckingActive() {
		return mwoCheckingActive;
	}

	public static void stopMWOTimer() {
		if (isMwoCheckingActive()) {
			Timer t = getCheckSystemClipboardForMWOResultTimer();
			t.cancel();
			t.purge();
			setMWOCheckingActive(false);
		}
	}

	@SuppressWarnings("unused")
	public static BOFaction getCurrentFaction() {
		return currentFaction;
	}

	@SuppressWarnings("unused")
	public static void setCurrentFaction(BOFaction currentFaction) {
		Nexus.currentFaction = currentFaction;
	}

	@SuppressWarnings("unused")
	public static void setCommandLogFile(File file) {
		commandLogFile = file;
	}

	@SuppressWarnings("unused")
	public static File getCommandLogFile() {
		return commandLogFile;
	}

	@SuppressWarnings("unused")
	public synchronized static void storeCommandHistory() {
		// store command history to file
		if (commandLogFile != null) {
			try (PrintWriter out = new PrintWriter(commandLogFile, StandardCharsets.UTF_8)) {
				String oldCommand = "";
				for (String l : commandHistory) {
					if (!l.equals(oldCommand)) {
						out.write(l + "\r\n");
						oldCommand = l;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Could not save command history file!");
			}
		}
	}

	public static boolean getUserIsOnline(Long id) {
		for (UserDTO u : Nexus.getCurrentlyOnlineUserList()) {
			if (u.getCurrentCharacter().getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	public static void setCurrentlyOnlineUserList(ArrayList<UserDTO> list) {
		currentlyOnlineUserList = list;
	}

	@SuppressWarnings("unused")
	public static ArrayList<UserDTO> getCurrentlyOnlineUserList() {
		return currentlyOnlineUserList;
	}

	@SuppressWarnings("unused")
	public static void setUserList(ArrayList<UserDTO> list) {
		userList = list;
	}

	@SuppressWarnings("unused")
	public static ArrayList<UserDTO> getUserList() {
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
	public static synchronized void injectNewUniverseDTO(UniverseDTO uniDTO) {
		boUniverse.setUniverseDTO(uniDTO);
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
		if (currentUser != null) {
			currentChar = currentUser.getCurrentCharacter();
		}
		return currentChar;
	}

	@SuppressWarnings("unused")
	public static boolean userHasAttack() {
		boolean userHasAttack = false;
		if (Nexus.getBoUniverse() != null) {
			for (BOAttack a : Nexus.getBoUniverse().attackBOsOpenInThisRound.values()) {
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
	public static void userHasFinishedAttacksInCurrentRound() {
		if (finishedAttacksOfUser == null) {
			finishedAttacksOfUser = new ArrayList<BOAttack>();
		}
		finishedAttacksOfUser.clear();

		if (Nexus.getBoUniverse() != null) {
			for (BOAttack a : Nexus.getBoUniverse().attackBOsFinishedInThisRound.values()) {
				if (a.getAttackCharList() != null) {
					for (AttackCharacterDTO ac : a.getAttackCharList()) {
						if (ac.getCharacterID().equals(Nexus.getCurrentUser().getCurrentCharacter().getId())) {
							// The user currently logged in was part of an attack that is resolved
							finishedAttacksOfUser.add(a);
							break;
						}
					}
				}
			}
			if (finishedAttacksOfUser.size() > 1) {
				logger.info("ATTENTION: User has multiple attacks finished in this current round!");
			}
		}
	}

	@SuppressWarnings("unused")
	public static BOAttack getCurrentAttackOfUser() {
		if (currentAttackOfUser == null || currentAttackOfUser.getAttackDTO().getFactionID_Winner() != null) {
			userHasAttack(); // to set currentAttack to user
		}
		return currentAttackOfUser;
	}

	@SuppressWarnings("unused")
	public static ArrayList<BOAttack> getFinishedAttacksInThisRoundForUser() {
		if (finishedAttacksOfUser == null) {
			userHasFinishedAttacksInCurrentRound(); // to set currentAttack to user
		}
		return finishedAttacksOfUser;
	}

	@SuppressWarnings("unused")
	public static BOAttack getLatestFinishedAttackInThisRoundForUser() {
		BOAttack latestAttack = null;
		ArrayList<BOAttack> attackBOs = getFinishedAttacksInThisRoundForUser();
		if (attackBOs.size() > 1) {
			logger.info("ATTENTION: User has multiple attacks finished in this current round!");

			for (BOAttack a : attackBOs) {
				if (latestAttack == null) {
					latestAttack = a;
				}
				if (a.getAttackDTO().getUpdated().getTime() > latestAttack.getAttackDTO().getUpdated().getTime()) {
					latestAttack = a;
				}
			}
		} else if (attackBOs.size() == 1) {
			latestAttack = attackBOs.get(0);
		}
		return latestAttack;
	}

	public static AttackDTO getCurrentOpenAttackForUser(UserDTO user) {
		AttackDTO r = null;

		RolePlayCharacterDTO ch = null;
		for (RolePlayCharacterDTO rpc : Nexus.characterList.values()) {
			if (rpc.getUser().getUserId().equals(user.getUserId())) {
				ch = rpc;
				break;
			}
		}

		if (ch != null) {
			for (BOAttack a : boUniverse.attackBOsOpenInThisRound.values()) {
				for (AttackCharacterDTO ac : a.getAttackCharList()) {
					if (ac.getCharacterID().equals(ch.getId())) {
						r = a.getAttackDTO();
						break;
					}
				}
			}
		}
		return r;
	}

	public static AttackDTO getCurrentAttackForUser(UserDTO user) {
		AttackDTO r = null;

		RolePlayCharacterDTO ch = null;
		for (RolePlayCharacterDTO rpc : Nexus.characterList.values()) {
			if (rpc.getUser().getUserId().equals(user.getUserId())) {
				ch = rpc;
				break;
			}
		}

		if (ch != null) {
			for (BOAttack a : boUniverse.attackBOsAllInThisRound.values()) {
				for (AttackCharacterDTO ac : a.getAttackCharList()) {
					if (ac.getCharacterID().equals(ch.getId())) {
						r = a.getAttackDTO();
						break;
					}
				}
			}
		}
		return r;
	}

	@SuppressWarnings("unused")
	public static void setCurrentAttackOfUserToNull() {
		currentAttackOfUser = null;
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
			logger.info("Session is null! Event NOT sent!");
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

//	@SuppressWarnings("unused")
//	public static LogWatcher getLogWatcher() {
//		return logWatcher;
//	}
//
//	@SuppressWarnings("unused")
//	public static void setLogWatcher(LogWatcher logWatcher) {
//		Nexus.logWatcher = logWatcher;
//	}

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

	@SuppressWarnings("unused")
	public static void setStoryBeforeSaving(Long storyID) {
		storyID_beforeSavingRespones = storyID;
	}

	@SuppressWarnings("unused")
	public static Long getStoryBeforeSaving(){
		return storyID_beforeSavingRespones;
	}

	@SuppressWarnings("unused")
	public static Timestamp getLastServerHeartbeatTimestamp() {
		return lastServerHeartbeatTimestamp;
	}

	@SuppressWarnings("unused")
	public static void setLastServerHeartbeatTimestamp(java.sql.Timestamp lastServerHeartbeatTimestamp) {
		Nexus.lastServerHeartbeatTimestamp = lastServerHeartbeatTimestamp;
	}

	@SuppressWarnings("unused")
	public static Timer getServerHeartBeatTimer() {
		return serverHeartBeatTimer;
	}

	@SuppressWarnings("unused")
	public static void setServerHeartBeatTimer(Timer serverHeartBeatTimer) {
		Nexus.serverHeartBeatTimer = serverHeartBeatTimer;
	}

	@SuppressWarnings("unused")
	public static long getHoursLeftInThisRound() {
		return hoursLeftInThisRound;
	}

	@SuppressWarnings("unused")
	public static void setHoursLeftInThisRound(long hoursLeftInThisRoundV) {
		hoursLeftInThisRound = hoursLeftInThisRoundV;
	}

	@SuppressWarnings("unused")
	public static Double getInitialWidth() {
		return initialWidth;
	}

	@SuppressWarnings("unused")
	public static void setInitialWidth(Double initialWidth) {
		Nexus.initialWidth = initialWidth;
	}

	@SuppressWarnings("unused")
	public static Double getInitialHeight() {
		return initialHeight;
	}

	@SuppressWarnings("unused")
	public static void setInitialHeight(Double initialHeight) {
		Nexus.initialHeight = initialHeight;
	}
}
