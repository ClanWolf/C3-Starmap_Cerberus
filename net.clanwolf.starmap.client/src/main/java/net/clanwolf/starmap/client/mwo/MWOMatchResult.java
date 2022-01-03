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

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MWOMatchResult {

    @SerializedName("MatchDetails")
    @Expose
    private MatchDetails matchDetails;
    @SerializedName("UserDetails")
    @Expose
    private List<UserDetail> userDetails = null;
	private String gameID;
	private String jsonString;

	Integer team1NumberOfPilots = 0;
	Integer team2NumberOfPilots = 0;
	Integer team1SurvivingPercentage = 0;
	Integer team2SurvivingPercentage = 0;

	public String getGameID() {
		return gameID;
	}

	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public MatchDetails getMatchDetails() {
        return matchDetails;
    }

    public void setMatchDetails(MatchDetails matchDetails) {
        this.matchDetails = matchDetails;
    }

    public List<UserDetail> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<UserDetail> userDetails) {
        this.userDetails = userDetails;
    }

	public Integer getTeam1NumberOfPilots() {
		return team1NumberOfPilots;
	}

	public void setTeam1NumberOfPilots(Integer team1NumberOfPilots) {
		this.team1NumberOfPilots = team1NumberOfPilots;
	}

	public Integer getTeam2NumberOfPilots() {
		return team2NumberOfPilots;
	}

	public void setTeam2NumberOfPilots(Integer team2NumberOfPilots) {
		this.team2NumberOfPilots = team2NumberOfPilots;
	}

	public Integer getTeam1SurvivingPercentage() {
		return team1SurvivingPercentage;
	}

	public void setTeam1SurvivingPercentage(Integer team1SurvivingPercentage) {
		this.team1SurvivingPercentage = team1SurvivingPercentage;
	}

	public Integer getTeam2SurvivingPercentage() {
		return team2SurvivingPercentage;
	}

	public void setTeam2SurvivingPercentage(Integer team2SurvivingPercentage) {
		this.team2SurvivingPercentage = team2SurvivingPercentage;
	}
}
