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

public class UserDetail {

    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("IsSpectator")
    @Expose
    private Boolean isSpectator;
    @SerializedName("Team")
    @Expose
    private String team;
    @SerializedName("Lance")
    @Expose
    private String lance;
    @SerializedName("MechItemID")
    @Expose
    private Integer mechItemID;
    @SerializedName("MechName")
    @Expose
    private String mechName;
    @SerializedName("SkillTier")
    @Expose
    private Integer skillTier;
    @SerializedName("HealthPercentage")
    @Expose
    private Integer healthPercentage;
    @SerializedName("Kills")
    @Expose
    private Integer kills;
    @SerializedName("KillsMostDamage")
    @Expose
    private Integer killsMostDamage;
    @SerializedName("Assists")
    @Expose
    private Integer assists;
    @SerializedName("ComponentsDestroyed")
    @Expose
    private Integer componentsDestroyed;
    @SerializedName("MatchScore")
    @Expose
    private Integer matchScore;
    @SerializedName("Damage")
    @Expose
    private Integer damage;
    @SerializedName("TeamDamage")
    @Expose
    private Integer teamDamage;
    @SerializedName("UnitTag")
    @Expose
    private String unitTag;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getIsSpectator() {
        return isSpectator;
    }

    public void setIsSpectator(Boolean isSpectator) {
        this.isSpectator = isSpectator;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getLance() {
        return lance;
    }

    public void setLance(String lance) {
        this.lance = lance;
    }

    public Integer getMechItemID() {
        return mechItemID;
    }

    public void setMechItemID(Integer mechItemID) {
        this.mechItemID = mechItemID;
    }

    public String getMechName() {
        return mechName;
    }

    public void setMechName(String mechName) {
        this.mechName = mechName;
    }

    public Integer getSkillTier() {
        return skillTier;
    }

    public void setSkillTier(Integer skillTier) {
        this.skillTier = skillTier;
    }

    public Integer getHealthPercentage() {
        return healthPercentage;
    }

    public void setHealthPercentage(Integer healthPercentage) {
        this.healthPercentage = healthPercentage;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getKillsMostDamage() {
        return killsMostDamage;
    }

    public void setKillsMostDamage(Integer killsMostDamage) {
        this.killsMostDamage = killsMostDamage;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getComponentsDestroyed() {
        return componentsDestroyed;
    }

    public void setComponentsDestroyed(Integer componentsDestroyed) {
        this.componentsDestroyed = componentsDestroyed;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Integer getTeamDamage() {
        return teamDamage;
    }

    public void setTeamDamage(Integer teamDamage) {
        this.teamDamage = teamDamage;
    }

    public String getUnitTag() {
        return unitTag;
    }

    public void setUnitTag(String unitTag) {
        this.unitTag = unitTag;
    }

}
