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

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.net.HTTP;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;
import java.util.TimerTask;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * @author Meldric
 */
public class CheckClipboardForMwoApi extends TimerTask {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static String previousContent;
	private static String currentContent;

	public CheckClipboardForMwoApi() {

	}

	public static MWOMatchResult getMWOGameStats(String gameid) {
		MWOMatchResult matchDetails = null;
		final Properties auth = new Properties();
		try {
			final String authFileName = "auth.properties";
			InputStream inputStream = CheckClipboardForMwoApi.class.getClassLoader().getResourceAsStream(authFileName);
			if (inputStream != null) {
				auth.load(inputStream);
			} else {
				throw new FileNotFoundException("Auth-Property file '" + authFileName + "' not found in classpath.");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		//logger.info("Trying to request game info.");
		String url = "https://mwomercs.com/api/v1/matches/" + gameid + "?api_token=" + auth.getProperty("mwo_api_key");
		try {
			// Example
			// game-id: 312013716556150
			// result:
			// String matchDetailString = "{\"MatchDetails\":{\"Map\":\"PolarHighlands\",\"ViewMode\":\"FirstPersonOnly\",\"TimeOfDay\":\"Day\",\"GameMode\":\"Domination\",\"Region\":\"NorthAmerica\",\"MatchTimeMinutes\":\"15\",\"UseStockLoadout\":false,\"NoMechQuirks\":false,\"NoMechEfficiencies\":false,\"WinningTeam\":\"2\",\"Team1Score\":6,\"Team2Score\":60,\"MatchDuration\":\"245\",\"CompleteTime\":\"2018-03-30T01:41:51+00:00\"},\"UserDetails\":[{\"Username\":\"hordes1ayer2\",\"IsSpectator\":true,\"Team\":null,\"Lance\":null,\"MechItemID\":0,\"MechName\":null,\"SkillTier\":null,\"HealthPercentage\":0,\"Kills\":0,\"KillsMostDamage\":0,\"Assists\":0,\"ComponentsDestroyed\":0,\"MatchScore\":0,\"Damage\":0,\"TeamDamage\":0,\"UnitTag\":\"\"},{\"Username\":\"Jay Z\",\"IsSpectator\":true,\"Team\":null,\"Lance\":null,\"MechItemID\":0,\"MechName\":null,\"SkillTier\":null,\"HealthPercentage\":0,\"Kills\":0,\"KillsMostDamage\":0,\"Assists\":0,\"ComponentsDestroyed\":0,\"MatchScore\":0,\"Damage\":0,\"TeamDamage\":0,\"UnitTag\":\"\"},{\"Username\":\"Nickredace\",\"IsSpectator\":false,\"Team\":\"2\",\"Lance\":\"2\",\"MechItemID\":337,\"MechName\":\"wlf-1a\",\"SkillTier\":1,\"HealthPercentage\":72,\"Kills\":1,\"KillsMostDamage\":1,\"Assists\":7,\"ComponentsDestroyed\":5,\"MatchScore\":225,\"Damage\":299,\"TeamDamage\":10,\"UnitTag\":\"-SA-\"},{\"Username\":\"Xylog\",\"IsSpectator\":false,\"Team\":\"2\",\"Lance\":\"2\",\"MechItemID\":368,\"MechName\":\"hbk-iic-a\",\"SkillTier\":2,\"HealthPercentage\":53,\"Kills\":2,\"KillsMostDamage\":0,\"Assists\":6,\"ComponentsDestroyed\":4,\"MatchScore\":158,\"Damage\":289,\"TeamDamage\":46,\"UnitTag\":\"-SA-\"},{\"Username\":\"Blackwater Social Media\",\"IsSpectator\":false,\"Team\":\"2\",\"Lance\":\"2\",\"MechItemID\":335,\"MechName\":\"wlf-2\",\"SkillTier\":1,\"HealthPercentage\":76,\"Kills\":0,\"KillsMostDamage\":2,\"Assists\":8,\"ComponentsDestroyed\":0,\"MatchScore\":258,\"Damage\":404,\"TeamDamage\":5,\"UnitTag\":\"-SA-\"},{\"Username\":\"MechWarrior1283\",\"IsSpectator\":false,\"Team\":\"2\",\"Lance\":\"2\",\"MechItemID\":595,\"MechName\":\"mlx-g\",\"SkillTier\":2,\"HealthPercentage\":51,\"Kills\":3,\"KillsMostDamage\":2,\"Assists\":5,\"ComponentsDestroyed\":5,\"MatchScore\":233,\"Damage\":287,\"TeamDamage\":21,\"UnitTag\":\"-SA-\"},{\"Username\":\"RED Smith\",\"IsSpectator\":false,\"Team\":\"2\",\"Lance\":\"1\",\"MechItemID\":292,\"MechName\":\"ach-a\",\"SkillTier\":2,\"HealthPercentage\":0,\"Kills\":0,\"KillsMostDamage\":1,\"Assists\":8,\"ComponentsDestroyed\":0,\"MatchScore\":154,\"Damage\":198,\"TeamDamage\":3,\"UnitTag\":\"-SA-\"},{\"Username\":\"DJDizzyG\",\"IsSpectator\":false,\"Team\":\"2\",\"Lance\":\"1\",\"MechItemID\":128,\"MechName\":\"grf-3m\",\"SkillTier\":2,\"HealthPercentage\":0,\"Kills\":0,\"KillsMostDamage\":0,\"Assists\":8,\"ComponentsDestroyed\":0,\"MatchScore\":64,\"Damage\":1,\"TeamDamage\":3,\"UnitTag\":\"-SA-\"},{\"Username\":\"DOMV2\",\"IsSpectator\":false,\"Team\":\"2\",\"Lance\":\"1\",\"MechItemID\":460,\"MechName\":\"hmn-c\",\"SkillTier\":1,\"HealthPercentage\":0,\"Kills\":1,\"KillsMostDamage\":1,\"Assists\":7,\"ComponentsDestroyed\":4,\"MatchScore\":199,\"Damage\":261,\"TeamDamage\":10,\"UnitTag\":\"-SA-\"},{\"Username\":\"Morticia Mellian\",\"IsSpectator\":false,\"Team\":\"2\",\"Lance\":\"1\",\"MechItemID\":268,\"MechName\":\"scr-a\",\"SkillTier\":1,\"HealthPercentage\":0,\"Kills\":1,\"KillsMostDamage\":1,\"Assists\":6,\"ComponentsDestroyed\":1,\"MatchScore\":149,\"Damage\":149,\"TeamDamage\":0,\"UnitTag\":\"-SA-\"},{\"Username\":\"DecoyTheDad\",\"IsSpectator\":false,\"Team\":\"1\",\"Lance\":\"3\",\"MechItemID\":368,\"MechName\":\"hbk-iic-a\",\"SkillTier\":1,\"HealthPercentage\":0,\"Kills\":1,\"KillsMostDamage\":1,\"Assists\":2,\"ComponentsDestroyed\":3,\"MatchScore\":172,\"Damage\":296,\"TeamDamage\":2,\"UnitTag\":\"228\"},{\"Username\":\"The0nlyRazor\",\"IsSpectator\":false,\"Team\":\"1\",\"Lance\":\"3\",\"MechItemID\":595,\"MechName\":\"mlx-g\",\"SkillTier\":2,\"HealthPercentage\":0,\"Kills\":1,\"KillsMostDamage\":1,\"Assists\":3,\"ComponentsDestroyed\":3,\"MatchScore\":159,\"Damage\":261,\"TeamDamage\":13,\"UnitTag\":\"228\"},{\"Username\":\"T0furk3y\",\"IsSpectator\":false,\"Team\":\"1\",\"Lance\":\"3\",\"MechItemID\":291,\"MechName\":\"ach-prime\",\"SkillTier\":2,\"HealthPercentage\":0,\"Kills\":0,\"KillsMostDamage\":0,\"Assists\":4,\"ComponentsDestroyed\":0,\"MatchScore\":80,\"Damage\":117,\"TeamDamage\":12,\"UnitTag\":\"CCdn\"},{\"Username\":\"Vipes\",\"IsSpectator\":false,\"Team\":\"1\",\"Lance\":\"3\",\"MechItemID\":334,\"MechName\":\"wlf-2r\",\"SkillTier\":3,\"HealthPercentage\":0,\"Kills\":1,\"KillsMostDamage\":0,\"Assists\":3,\"ComponentsDestroyed\":2,\"MatchScore\":66,\"Damage\":120,\"TeamDamage\":20,\"UnitTag\":\"\"},{\"Username\":\"Dimento Graven\",\"IsSpectator\":false,\"Team\":\"1\",\"Lance\":\"2\",\"MechItemID\":111,\"MechName\":\"shd-2d2\",\"SkillTier\":1,\"HealthPercentage\":0,\"Kills\":0,\"KillsMostDamage\":0,\"Assists\":4,\"ComponentsDestroyed\":1,\"MatchScore\":161,\"Damage\":233,\"TeamDamage\":1,\"UnitTag\":\"228\"},{\"Username\":\"nuttyrat\",\"IsSpectator\":false,\"Team\":\"1\",\"Lance\":\"2\",\"MechItemID\":460,\"MechName\":\"hmn-c\",\"SkillTier\":1,\"HealthPercentage\":0,\"Kills\":1,\"KillsMostDamage\":1,\"Assists\":3,\"ComponentsDestroyed\":1,\"MatchScore\":140,\"Damage\":230,\"TeamDamage\":11,\"UnitTag\":\"CCdn\"},{\"Username\":\"Lokamis\",\"IsSpectator\":false,\"Team\":\"1\",\"Lance\":\"2\",\"MechItemID\":111,\"MechName\":\"shd-2d2\",\"SkillTier\":1,\"HealthPercentage\":0,\"Kills\":0,\"KillsMostDamage\":1,\"Assists\":4,\"ComponentsDestroyed\":0,\"MatchScore\":93,\"Damage\":134,\"TeamDamage\":1,\"UnitTag\":\"228\"},{\"Username\":\"RjBass3\",\"IsSpectator\":false,\"Team\":\"1\",\"Lance\":\"2\",\"MechItemID\":169,\"MechName\":\"adr-d\",\"SkillTier\":1,\"HealthPercentage\":0,\"Kills\":0,\"KillsMostDamage\":0,\"Assists\":4,\"ComponentsDestroyed\":0,\"MatchScore\":114,\"Damage\":268,\"TeamDamage\":35,\"UnitTag\":\"228\"},{\"Username\":\"M E M E M A C H I N E\",\"IsSpectator\":false,\"Team\":\"1\",\"Lance\":\"1\",\"MechItemID\":478,\"MechName\":\"nva-bk\",\"SkillTier\":1,\"HealthPercentage\":0,\"Kills\":0,\"KillsMostDamage\":1,\"Assists\":0,\"ComponentsDestroyed\":0,\"MatchScore\":0,\"Damage\":0,\"TeamDamage\":43,\"UnitTag\":\"BEER\"},{\"Username\":\"Bensien\",\"IsSpectator\":false,\"Team\":\"1\",\"Lance\":\"1\",\"MechItemID\":368,\"MechName\":\"hbk-iic-a\",\"SkillTier\":1,\"HealthPercentage\":0,\"Kills\":0,\"KillsMostDamage\":0,\"Assists\":0,\"ComponentsDestroyed\":0,\"MatchScore\":11,\"Damage\":0,\"TeamDamage\":0,\"UnitTag\":\"228\"},{\"Username\":\"Kajihn\",\"IsSpectator\":false,\"Team\":\"1\",\"Lance\":\"1\",\"MechItemID\":190,\"MechName\":\"whk-primei\",\"SkillTier\":2,\"HealthPercentage\":0,\"Kills\":0,\"KillsMostDamage\":0,\"Assists\":0,\"ComponentsDestroyed\":0,\"MatchScore\":7,\"Damage\":0,\"TeamDamage\":0,\"UnitTag\":\"228\"}]}";
			String matchDetailString = new String(HTTP.get(url), StandardCharsets.UTF_8);
			matchDetails = new Gson().fromJson(matchDetailString, MWOMatchResult.class);
			matchDetails.setJsonString(matchDetailString);
			//System.out.println(matchDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchDetails;
	}

	@Override
	public void run() {
		ActionManager.getAction(ACTIONS.FLASH_MWO_LOGO_ONCE).execute();
		Platform.runLater(() -> {
			Clipboard cb = Clipboard.getSystemClipboard();
			currentContent = cb.getString();
			if (previousContent == null) {
				previousContent = "";
			}
			if (!previousContent.equals(currentContent)) {
				if (currentContent != null) {
					if (!"".equals(currentContent)) {
						//logger.info("From clipboard: " + currentContent + " (Length: " + currentContent.length() + ")");
						if (currentContent.matches("[0-9]+") // && currentContent.length() == 15
						) {
							MWOMatchResult results = getMWOGameStats(currentContent);
							if (results != null) {
								results.setGameID(currentContent);
								ActionManager.getAction(ACTIONS.MWO_DROPSTATS_RECEIVED).execute(results);
							} else {
								logger.info("The content did not get a valid response. Ignoring.");
							}
						} else {
//							logger.info("The content does not fit the pattern. Ignoring.");
						}
					}
				}
			} else {
//				logger.info("The content has been copied to clipboard before. Ignoring.");
			}
			previousContent = currentContent;
		});
	}

	public static void main(String[] args) {
		// 420332911230604
		// 421333638615793
		// 419705846001647

		// 421604221556762
		// 421179019792407
		// 421492552406500
		// 421879099465036

		MWOMatchResult results = getMWOGameStats("421333638615793");
		ResultAnalyzer.analyseAndStoreMWOResult(results, false);
	}
}
