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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchDetails {

    @SerializedName("Map")
    @Expose
    private String map;
    @SerializedName("ViewMode")
    @Expose
    private String viewMode;
    @SerializedName("TimeOfDay")
    @Expose
    private String timeOfDay;
    @SerializedName("GameMode")
    @Expose
    private String gameMode;
    @SerializedName("Region")
    @Expose
    private String region;
    @SerializedName("MatchTimeMinutes")
    @Expose
    private String matchTimeMinutes;
    @SerializedName("UseStockLoadout")
    @Expose
    private Boolean useStockLoadout;
    @SerializedName("NoMechQuirks")
    @Expose
    private Boolean noMechQuirks;
    @SerializedName("NoMechEfficiencies")
    @Expose
    private Boolean noMechEfficiencies;
    @SerializedName("WinningTeam")
    @Expose
    private String winningTeam;
    @SerializedName("Team1Score")
    @Expose
    private Integer team1Score;
    @SerializedName("Team2Score")
    @Expose
    private Integer team2Score;
    @SerializedName("MatchDuration")
    @Expose
    private String matchDuration;
    @SerializedName("CompleteTime")
    @Expose
    private String completeTime;

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMatchTimeMinutes() {
        return matchTimeMinutes;
    }

    public void setMatchTimeMinutes(String matchTimeMinutes) {
        this.matchTimeMinutes = matchTimeMinutes;
    }

    public Boolean getUseStockLoadout() {
        return useStockLoadout;
    }

    public void setUseStockLoadout(Boolean useStockLoadout) {
        this.useStockLoadout = useStockLoadout;
    }

    public Boolean getNoMechQuirks() {
        return noMechQuirks;
    }

    public void setNoMechQuirks(Boolean noMechQuirks) {
        this.noMechQuirks = noMechQuirks;
    }

    public Boolean getNoMechEfficiencies() {
        return noMechEfficiencies;
    }

    public void setNoMechEfficiencies(Boolean noMechEfficiencies) {
        this.noMechEfficiencies = noMechEfficiencies;
    }

    public String getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(String winningTeam) {
        this.winningTeam = winningTeam;
    }

    public Integer getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(Integer team1Score) {
        this.team1Score = team1Score;
    }

    public Integer getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(Integer team2Score) {
        this.team2Score = team2Score;
    }

    public String getMatchDuration() {
        return matchDuration;
    }

    public void setMatchDuration(String matchDuration) {
        this.matchDuration = matchDuration;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

}
