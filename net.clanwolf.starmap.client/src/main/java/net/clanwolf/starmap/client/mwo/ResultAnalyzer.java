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

import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.transfer.dtos.AttackCharacterDTO;
import net.clanwolf.starmap.transfer.dtos.RolePlayCharacterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

public class ResultAnalyzer {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void analyseMWOResult(MWOMatchResult result) {
		//TODO: Handle MWO Game result

		MatchDetails md = result.getMatchDetails();
		logger.info("============================================================================================================");
		logger.info("Analyzing MWO game results from clipboard game id (requested by MWO-API)");

		String gameId = result.getGameID();
		String jsonString = result.getJsonString();
		String map = md.getMap();
		String mode = md.getGameMode();
		String winner = md.getWinningTeam();

		Integer team1NumberOfPilots = 0;
		Integer team2NumberOfPilots = 0;
		Integer team1SurvivingPercentage = 0;
		Integer team2SurvivingPercentage = 0;

		// 2018-03-30T01:41:51+00:00
		Timestamp timestampFromMWOGame = null;
		double hours = 0.0;
		try {
			String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ssXXX";
			DateFormat dateFormat = new SimpleDateFormat(isoDatePattern);
			Date parsedDate = dateFormat.parse(md.getCompleteTime());
			timestampFromMWOGame = new java.sql.Timestamp(parsedDate.getTime());

			Timestamp timestampNow = Timestamp.from(Instant.now());
			long difference = timestampNow.getTime() - timestampFromMWOGame.getTime();
			hours = ((difference / 1000) / 60) / 60;
		} catch(Exception e) {
			e.printStackTrace();
		}

		logger.info("Game-ID     : " + gameId);
		logger.info("Drop ended  : " + md.getCompleteTime());
		logger.info("Hours since : " + hours);
		logger.info("Data        : " + jsonString);
		logger.info("Map         : " + map);
		logger.info("Mode        : " + mode);
		logger.info("Team 1 score: " + md.getTeam1Score());
		logger.info("Team 2 score: " + md.getTeam2Score());
		logger.info("Winner      : Team " + winner);
		logger.info("------------------------------------------------------------------------------------------------------------");
		logger.info("Team                  MWO Username      Unit           Mech        K/A  Damage          C3 User");

		HashMap<UserDetail, RolePlayCharacterDTO> userMatchList = new HashMap<>();

		for (UserDetail ud : result.getUserDetails()) {
			String team = ud.getTeam() == null ? "/" : ud.getTeam();
			String userName = ud.getUsername();
			String mech = ud.getMechName();
			String unit = ud.getUnitTag();
			Integer kills = ud.getKills();
			Integer killsMostDamage = ud.getKillsMostDamage();
			Integer assists = ud.getAssists();
			Integer damage = ud.getDamage();
			Integer teamDamage = ud.getTeamDamage();
			Integer componentsDestroyed = ud.getComponentsDestroyed();
			Integer matchScore = ud.getMatchScore();
			Integer healthPercentage = ud.getHealthPercentage();

			String userNameFormatted = String.format("%30s %n", userName);
			String unitFormatted = String.format("%6s %n", unit);
			String mechFormatted = mech != null ? String.format("%15s %n", mech) : String.format("%15s %n", "-");
			String killsFormatted = String.format("%10s %n", " (" + kills + "/" + assists + ")");
			String damageFormatted = String.format("%6s %n", damage);

			String foundUser = String.format("%15s %n", "-");
			for (AttackCharacterDTO ac : Nexus.getCurrentAttackOfUser().getAttackCharList()) {
				RolePlayCharacterDTO rpc = Nexus.getCharacterById(ac.getCharacterID());
				String mwoName = rpc.getMwoUsername();
				if (userName.equals(mwoName)) {
					foundUser = String.format("%15s %n", "(" + rpc.getName() + ")");
					userMatchList.put(ud, rpc);
					break;
				}
			}

			if (team.equals("1")) {
				team1NumberOfPilots++;
				team1SurvivingPercentage += healthPercentage;
			} else if (team.equals("2")) {
				team2NumberOfPilots++;
				team2SurvivingPercentage += healthPercentage;
			}

			logger.info(("- " + team + " "
					+ userNameFormatted
					+ "[" + unitFormatted + "]"
					+ mechFormatted.toUpperCase()
					+ killsFormatted + " "
					+ damageFormatted + " "
					+ foundUser).replaceAll("\r\n", ""));

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

		logger.info("Team 1 surviving percentage: " + team1SurvivingPercentage / team1NumberOfPilots);
		logger.info("Team 2 surviving percentage: " + team2SurvivingPercentage / team2NumberOfPilots);

		logger.info("============================================================================================================");
	}
}
