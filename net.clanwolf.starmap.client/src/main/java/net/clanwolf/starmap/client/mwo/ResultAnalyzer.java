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
 * Copyright (c) 2001-2022, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.mwo;

import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.enums.C3MESSAGES;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;
import net.clanwolf.starmap.client.gui.messagepanes.C3Message;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.universe.BOAttackStats;
import net.clanwolf.starmap.client.process.universe.BORolePlayCharacterStats;
import net.clanwolf.starmap.client.process.universe.BOStatsMwo;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.mwo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ResultAnalyzer {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void analyseAndStoreMWOResult(MWOMatchResult result, boolean store) {
		MapInfo mapInfo = new MapInfo();

		MatchDetails md = result.getMatchDetails();
		logger.info("============================================================================================================");
		logger.info("Analyzing MWO game results from clipboard game id (requested by MWO-API)");

		String gameId = result.getGameID();
		String jsonString = result.getJsonString();
		String map = mapInfo.map.get(md.getMap());
		String mode = md.getGameMode();
		String winner = md.getWinningTeam();

		Integer team1NumberOfPilots = 0;
		Integer team2NumberOfPilots = 0;
		long team1Tonnage = 0L;
		long team2Tonnage = 0L;
		long team1LostTonnage = 0L;
		long team2LostTonnage = 0L;
		Long team1KillCount = 0L;
		Long team2KillCount = 0L;
		Integer team1SurvivingPercentage = 0;
		Integer team2SurvivingPercentage = 0;

		String attackerTeam = null;
		String defenderTeam = null;

		double hours = 0.0;
		try {
			String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ssXXX";
			DateFormat dateFormat = new SimpleDateFormat(isoDatePattern);
			Date parsedDate = dateFormat.parse(md.getCompleteTime());
			Timestamp timestampFromMWOGame = new java.sql.Timestamp(parsedDate.getTime());

			Timestamp timestampNow = Timestamp.from(Instant.now());
			long difference = timestampNow.getTime() - timestampFromMWOGame.getTime();
			hours = ((difference / 1000.0) / 60.0) / 60.0;
		} catch(Exception e) {
			e.printStackTrace();
		}

		//logger.info("Data         : " + jsonString);
		logger.info("Game-ID      : " + gameId);
		logger.info("Drop ended   : " + md.getCompleteTime());
		logger.info("Hours since  : " + hours);
		logger.info("Map          : " + map);
		logger.info("Mode         : " + mode);
		logger.info("Team 1 score : " + md.getTeam1Score());
		logger.info("Team 2 score : " + md.getTeam2Score());
		logger.info("Winner       : Team " + winner);
		logger.info("------------------------------------------------------------------------------------------------------------");
		logger.info("Team                  MWO Username      Unit           Mech (ton.)      K/A    Damage          C3 User");

		HashMap<UserDetail, RolePlayCharacterDTO> userMatchList = new HashMap<>();
		ArrayList<RolePlayCharacterStatsDTO> characterStatsList = new ArrayList<>();

		for (UserDetail ud : result.getUserDetails()) {
			MechIdInfo mechInfo = new MechIdInfo(ud.getMechItemID());

			String team = ud.getTeam() == null ? "/" : ud.getTeam();
			String userName = ud.getUsername();
			String mech = ud.getMechName();
			long mechItemId = ud.getMechItemID().longValue();
			boolean leadingPosition = false;

			if (ud.getIsSpectator()) {

			}

			String mechFullName = mechInfo.getFullName();
			Integer killsMostDamage = ud.getKillsMostDamage();
			Integer teamDamage = ud.getTeamDamage();
			Integer componentsDestroyed = ud.getComponentsDestroyed();

			int tonnage = mechInfo.getTonnage();
			if (tonnage == 999) {
				// Mech has not been found in MWO EChassis Enumeration --> Manual FIX!
				logger.error("Mech Chassis not found in Enumeration! MechItemId: " + mechItemId);
				if (mechItemId == 0) {
					logger.info("MechItemId=0 --> No Mech, Spectator!");
					tonnage = 0;
				} else {
					logger.info("Rawdata MWO API Stats:");
					logger.info("-------------------------------------------------------------------");
					logger.info(jsonString);
					logger.info("-------------------------------------------------------------------");
				}
			}
			String unit = ud.getUnitTag();
			Integer kills = ud.getKills();
			Integer assists = ud.getAssists();
			Integer damage = ud.getDamage();
			Integer matchScore = ud.getMatchScore();
			Integer healthPercentage = ud.getHealthPercentage();
			String userNameFormatted = String.format("%30s %n", userName);
			String unitFormatted = String.format("%6s %n", unit);
			String mechFormatted = mech != null ? String.format("%15s %n", mech) : String.format("%15s %n", "-");
			String killsFormatted = String.format("%10s %n", " (" + kills + "/" + assists + ")");
			String damageFormatted = String.format("%6s %n", damage);

			String foundUser = String.format("%15s %n", "-");
			if (Nexus.getCurrentAttackOfUser() != null) {
				for (AttackCharacterDTO ac : Nexus.getCurrentAttackOfUser().getAttackCharList()) {
					RolePlayCharacterDTO rpc = Nexus.getCharacterById(ac.getCharacterID());
					String mwoName = rpc.getMwoUsername();
					if (userName.equals(mwoName)) {
						foundUser = String.format("%15s %n", "(" + rpc.getName() + ")");
						userMatchList.put(ud, rpc);
						if (ac.getType().equals(Constants.ROLE_ATTACKER_COMMANDER)) {
							leadingPosition = true;
							attackerTeam = team;
						} else if (ac.getType().equals(Constants.ROLE_DEFENDER_COMMANDER)) {
							leadingPosition = true;
							defenderTeam = team;
						} else if (ac.getType().equals(Constants.ROLE_ATTACKER_WARRIOR)) {
							leadingPosition = false;
							attackerTeam = team;
						} else if (ac.getType().equals(Constants.ROLE_DEFENDER_WARRIOR)) {
							leadingPosition = false;
							defenderTeam = team;
						} else if (ac.getType().equals(Constants.ROLE_ATTACKER_SUPPORTER)) {
							leadingPosition = false;
							attackerTeam = team;
						} else if (ac.getType().equals(Constants.ROLE_DEFENDER_SUPPORTER)) {
							leadingPosition = false;
							defenderTeam = team;
						}
					}
				}
			}

			if (attackerTeam != null && attackerTeam.equals(defenderTeam)) {
				// Same teams cannot be right! Raise error
				C3Message m = new C3Message(C3MESSAGES.WARNING_BLACKBOX_TEAMS_INVALID);
				m.setType(C3MESSAGETYPES.CLOSE);
				m.setText("Teams seem to be ivalid!");
				ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(m);
			}

			if (team != null && team.equals("1")) {
				team1NumberOfPilots++;
				team1SurvivingPercentage += healthPercentage;
				team1Tonnage += tonnage;
				if (healthPercentage == 0) {
					team2LostTonnage += tonnage;
					team2KillCount++;
				}
			} else if (team != null && team.equals("2")) {
				team2NumberOfPilots++;
				team2SurvivingPercentage += healthPercentage;
				team2Tonnage += tonnage;
				if (healthPercentage == 0) {
					team1LostTonnage += tonnage;
					team1KillCount++;
				}
			}

			logger.info(("- " + team + " "
					+ userNameFormatted
					+ "[" + unitFormatted + "]"
					+ mechFormatted.toUpperCase()
					+ "(" + String.format("%2s %n", tonnage) + "t) "
					+ killsFormatted + " "
					+ damageFormatted + " "
					+ foundUser).replaceAll("\r\n", ""));

			RolePlayCharacterDTO rpchar = userMatchList.get(ud);
			if (rpchar != null) {
				RolePlayCharacterStatsDTO charStats = new RolePlayCharacterStatsDTO();
				charStats.setSeasonId(Nexus.getCurrentAttackOfUser().getSeason().longValue());
				charStats.setAttackId(Nexus.getCurrentAttackOfUser().getAttackDTO().getId());
				charStats.setRoleplayCharacterId(rpchar.getId());
				charStats.setRoleplayCharacterFactionId(rpchar.getFactionId().longValue());
				charStats.setMwoMatchId(gameId);
				charStats.setLeadingPosition(leadingPosition);
				charStats.setMwoMatchScore(matchScore.longValue());
				if ("1".equals(team)){
					charStats.setMwoTeam(1L);
				} else if ("2".equals(team)){
					charStats.setMwoTeam(2L);
				}
				charStats.setMwoDamage(damage.longValue());
				charStats.setMwoKills(kills.longValue());
				charStats.setMwoSurvivalPercentage(healthPercentage.longValue());
				charStats.setMechItemId(mechItemId);

				logger.info("Storing stats data for character: " + Nexus.getCurrentUser().getCurrentCharacter());
				characterStatsList.add(charStats);
			}

			// Tonnage by adding the mech weights from a lookup table?
			// XP = Kills * damage / number of players, weil das Team gewinnt! ????

			// Kosten für den Kampf von Surviving percentage
		}

		result.setTeam1NumberOfPilots(team1NumberOfPilots);
		result.setTeam2NumberOfPilots(team2NumberOfPilots);
		result.setTeam1SurvivingPercentage(team1SurvivingPercentage / team1NumberOfPilots);
		result.setTeam2SurvivingPercentage(team2SurvivingPercentage / team2NumberOfPilots);

		logger.info("Team 1 number of pilots: " + team1NumberOfPilots);
		logger.info("Team 2 number of pilots: " + team2NumberOfPilots);
		logger.info("Team 1 tonnage: " + team1Tonnage);
		logger.info("Team 2 tonnage: " + team2Tonnage);
		logger.info("Team 1 surviving percentage: " + team1SurvivingPercentage / team1NumberOfPilots);
		logger.info("Team 2 surviving percentage: " + team2SurvivingPercentage / team2NumberOfPilots);

		if (store) {
			if (attackerTeam != null && defenderTeam != null) {
				logger.info("Storing raw game stats to database...");
				try {
					StatsMwoDTO stats = new StatsMwoDTO();
					stats.setSeasonId(Nexus.getCurrentAttackOfUser().getSeason().longValue());
					stats.setAttackId(Nexus.getCurrentAttackOfUser().getAttackDTO().getId());
					stats.setGameId(gameId);
					stats.setRawData(jsonString);

					BOStatsMwo boStatsMwo = new BOStatsMwo(stats);
					boStatsMwo.storeStatsMwo();
					logger.info("... done.");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error while saving mwo game results.", e);
				}
				logger.info("Storing attack stats to database...");
				try {
					AttackStatsDTO attackStats = new AttackStatsDTO();
					attackStats.setSeasonId(Nexus.getCurrentAttackOfUser().getSeason().longValue());
					attackStats.setRoundId(Nexus.getCurrentAttackOfUser().getRound().longValue());
					attackStats.setAttackId(Nexus.getCurrentAttackOfUser().getAttackDTO().getId());
					attackStats.setStarSystemDataId(Nexus.getCurrentAttackOfUser().getStarSystemId());
					attackStats.setMwoMatchId(gameId);
					attackStats.setDropId("");
					attackStats.setMap(map);
					attackStats.setMode(mode);
					attackStats.setDropEnded(md.getCompleteTime());
					attackStats.setAttackerFactionId(Nexus.getCurrentAttackOfUser().getAttackerFactionId().longValue());
					attackStats.setDefenderFactionId(Nexus.getCurrentAttackOfUser().getDefenderFactionId().longValue());
					if ("1".equals(attackerTeam) && "2".equals(defenderTeam)) {
						attackStats.setAttackerNumberOfPilots(team1NumberOfPilots.longValue());
						attackStats.setDefenderNumberOfPilots(team2NumberOfPilots.longValue());
						attackStats.setAttackerTonnage(team1Tonnage);
						attackStats.setDefenderTonnage(team2Tonnage);
						attackStats.setAttackerLostTonnage(team1LostTonnage);
						attackStats.setDefenderLostTonnage(team2LostTonnage);
						attackStats.setAttackerKillCount(team1KillCount);
						attackStats.setDefenderKillCount(team2KillCount);
						if ("1".equals(winner)) { // Attacker won
							attackStats.setWinnerFactionId(Nexus.getCurrentAttackOfUser().getAttackerFactionId().longValue());
						} else if ("2".equals(winner)) { // Defender won
							attackStats.setWinnerFactionId(Nexus.getCurrentAttackOfUser().getDefenderFactionId().longValue());
						}
					} else if ("2".equals(attackerTeam) && "1".equals(defenderTeam)) {
						attackStats.setAttackerNumberOfPilots(team1NumberOfPilots.longValue());
						attackStats.setDefenderNumberOfPilots(team2NumberOfPilots.longValue());
						attackStats.setAttackerTonnage(team2Tonnage);
						attackStats.setDefenderTonnage(team1Tonnage);
						attackStats.setAttackerLostTonnage(team2LostTonnage);
						attackStats.setDefenderLostTonnage(team1LostTonnage);
						attackStats.setAttackerKillCount(team2KillCount);
						attackStats.setDefenderKillCount(team1KillCount);
						if ("1".equals(winner)) { // Defender won
							attackStats.setWinnerFactionId(Nexus.getCurrentAttackOfUser().getDefenderFactionId().longValue());
						} else if ("2".equals(winner)) { // Attacker won
							attackStats.setWinnerFactionId(Nexus.getCurrentAttackOfUser().getAttackerFactionId().longValue());
						}
					}

					BOAttackStats boAttackStats = new BOAttackStats(attackStats);
					boAttackStats.storeAttackStats();
					logger.info("... done.");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error while saving attack stats.", e);
				}
				logger.info("Storing roleplay character stats to database...");
				try {
					BORolePlayCharacterStats boRolePlayCharacterStats = new BORolePlayCharacterStats(characterStatsList);
					boRolePlayCharacterStats.storeRolePlayCharacterStats();
					logger.info("... done.");
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error while saving attack stats.", e);
				}
			} else {
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_InvalidBlackboxData"));
			}
		}

		logger.info("============================================================================================================");
	}
}
