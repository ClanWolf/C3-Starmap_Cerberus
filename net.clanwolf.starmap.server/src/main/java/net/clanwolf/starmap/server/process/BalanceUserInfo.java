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
package net.clanwolf.starmap.server.process;

import net.clanwolf.starmap.exceptions.MechItemIdNotFoundException;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.AttackStatsDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.C3GameConfigDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayCharacterStatsDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackStatsPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayCharacterStatsPOJO;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import net.clanwolf.starmap.transfer.mwo.MechIdInfo;
import net.clanwolf.starmap.transfer.mwo.UserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BalanceUserInfo {
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final MWOMatchResult mwomatchResult;
    private final Long defenderFactionID, attackerFactionID;
    /**
     * Der Benutzername, wie er in MWO angezeigt wird.
     */
    public String userName;
    public long rewardLossVictory;
    public String rewardLossVictoryDescription;
    public long rewardAssist;
    public long playerAssist;
    /**
     * Die Anzahl des Schadens, den Spieler mit dem Mech erzielt hat.
     */
    public long playerDamage;
    /**
     * Anzahl der Komponenten, die der Spieler zerstört hat.
     */
    public long playerComponentDestroyed;
    /**
     * Die Anzahl kills, die der Spieler erreicht hat.
     */
    public long playerKills;
    /**
     * Dia Anzahl an Match-score, den der Spieler erreicht hat.
     */
    public long playerMatchScore;
    /**
     * Der Gesundheit zustand des Mechs, am Ende des Kampfes.
     */
    public long playerMechHealth;
    /**
     * Mech-Informationen über den Spieler.
     */
    public String playerMechName;
    /**
     * Mech-Reparaturkosten in C-Bills.
     */
    public long mechRepairCost;
    /**
     * Die Belohnung für zerstöre Komponenten in C-Bills.
     */
    public long rewardComponentsDestroyed;
    /**
     * Die Belohnung für den Match-score in C-Bills.
     */
    public long rewardMatchScore;
    /**
     * Die Belohnung für Schaden in C-Bills
     */
    public long rewardDamage;
    /**
     * Die Bestrafung für den Spieler, dass er ein Teammitglied angeschossen hat in C-Bills.
     */
    public long rewardTeamDamage;
    /**
     * Den Schaden an einem Teammitglied, den der Spieler angerichtet hat.
     */
    public long playerTeamDamage;
    /**
     * Die Belohnung für die kills, den der Spieler erreicht hat in C-Bills.
     */
    public long rewardKill;
    /**
     * Die Summe aller Belohnungen und Kosten für den Spieler.
     */
    public Long subTotal;

    /**
     * Berechnet die kosten für den Angreifer und den Verteidiger.
     *
     * @param mwomatchResult MWOMachtResult
     */
    public BalanceUserInfo(MWOMatchResult mwomatchResult) {

        AttackStatsDAO attackStatsDAO = AttackStatsDAO.getInstance();
        AttackStatsPOJO attackStatsPOJO = attackStatsDAO.findByMatchId(mwomatchResult.getGameID());
        defenderFactionID = attackStatsPOJO.getDefenderFactionId();
        attackerFactionID = attackStatsPOJO.getAttackerFactionId();

        this.mwomatchResult = mwomatchResult;
    }

    private static void setMechName(UserDetail detail, BalanceUserInfo balanceUserInfo) throws ParserConfigurationException, IOException, SAXException {
        balanceUserInfo.playerMechName = "<UNKNOWN MECH>";
        try {
            balanceUserInfo.playerMechName = new MechIdInfo(detail.getMechItemID()).getFullName();
        } catch (MechItemIdNotFoundException e) {
            logger.error("The 'Mech's name could not be determined.");
        }
    }

    /**
     * Listet die kosten für den Angreifer auf.
     *
     * @return Gibt ein ArrayList für die kosten für den Angreifer zurück.
     */
    public List<BalanceUserInfo> GetAttackerInfo() throws ParserConfigurationException, IOException, SAXException {
        List<BalanceUserInfo> attacker = new ArrayList<>();

        for (UserDetail detail : mwomatchResult.getUserDetails()) {
            if (detail.getTeam() != null) {
                if (Objects.equals(getAttackerTeam(), detail.getTeam())) {
                    getUserInfo(attacker, detail);
                }
            }
        }
        return attacker;
    }

    /**
     * Listet die kosten für den Verteidiger auf.
     *
     * @return Gibt ein ArrayList für die kosten für den Verteidiger zurück.
     */
    public List<BalanceUserInfo> GetDefenderInfo() throws ParserConfigurationException, IOException, SAXException {
        List<BalanceUserInfo> defender = new ArrayList<>();

        for (UserDetail detail : mwomatchResult.getUserDetails()) {
            if (detail.getTeam() != null) {
                if (Objects.equals(getDefenderTeam(), detail.getTeam())) {
                    getUserInfo(defender, detail);
                }
            }
        }
        return defender;
    }

    private String getDefenderTeam() {

        RolePlayCharacterStatsDAO rolePlayCharacterStatsDAO = RolePlayCharacterStatsDAO.getInstance();
        ArrayList<RolePlayCharacterStatsPOJO> rolePlayCharacterStatsPOJO = rolePlayCharacterStatsDAO.findByMatchId(mwomatchResult.getGameID());
        String defTeam = null;

        for (RolePlayCharacterStatsPOJO rpChar : rolePlayCharacterStatsPOJO) {
            if (rpChar.getMwoTeam() != null) {
                if (Objects.equals(defenderFactionID, rpChar.getRoleplayCharacterFactionId())) {
                    defTeam = String.valueOf(rpChar.getMwoTeam());
                    break;
                }
            }
        }
        return defTeam;
    }

    private String getAttackerTeam() {

        RolePlayCharacterStatsDAO rolePlayCharacterStatsDAO = RolePlayCharacterStatsDAO.getInstance();
        ArrayList<RolePlayCharacterStatsPOJO> rolePlayCharacterStatsPOJO = rolePlayCharacterStatsDAO.findByMatchId(mwomatchResult.getGameID());
        String attTeam = null;

        for (RolePlayCharacterStatsPOJO rpChar : rolePlayCharacterStatsPOJO) {
            if (rpChar.getMwoTeam() != null) {
                if (Objects.equals(attackerFactionID, rpChar.getRoleplayCharacterFactionId())) {
                    attTeam = String.valueOf(rpChar.getMwoTeam());
                    break;
                }
            }
        }
        return attTeam;
    }

    public Long getMechCost(Integer mechItemID, Integer HealthPercentage) throws ParserConfigurationException, IOException, SAXException {

        long sumCost;

        //Kosten für die Mechvariante festlegen
        Long mechVariantCost = -1L;
        String mechVariant = "Unknown";
        try {
            mechVariant = new MechIdInfo(mechItemID).getMechVariantType();
        } catch (MechItemIdNotFoundException e) {
            logger.error("The variant of the mech could not be determined.");
        }
        switch (mechVariant) {
            case "SPECIAL", "FOUNDER", "PHOENIX", "SARAH" ->
                    mechVariantCost = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_VARIANT_IS_SPECIAL").getValue();
            case "HERO" ->
                    mechVariantCost = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_VARIANT_IS_HERO").getValue();
            case "CHAMPION" ->
                    mechVariantCost = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_VARIANT_IS_CHAMPION").getValue();
            case "STANDARD" ->
                    mechVariantCost = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_VARIANT_IS_STANDARD").getValue();
            case "Unknown" -> {
                mechVariantCost = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_VARIANT_IS_UNKNOWN").getValue();
                logger.error("Unknown Mech variant");
            }
        }

        //Kosten für die Mechklasse festlegen
        Long mechClass = -1L;
        MechIdInfo.EMechclass mechclass = MechIdInfo.EMechclass.UNKNOWN;
        try {
            mechclass = new MechIdInfo(mechItemID).getMechClass();
        } catch (MechItemIdNotFoundException e) {
            logger.error("The class of the mech could not be determined.");
        }

        switch (mechclass) {
            case LIGHT ->
                    mechClass = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_CLASS_IS_LIGHT").getValue();
            case MEDIUM ->
                    mechClass = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_CLASS_IS_MEDIUM").getValue();
            case HEAVY ->
                    mechClass = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_CLASS_IS_HEAVY").getValue();
            case ASSAULT ->
                    mechClass = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_CLASS_IS_ASSAULT").getValue();
            case UNKNOWN -> {

                mechClass = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_CLASS_IS_UNKNOWN").getValue();
                logger.error("Unknown mech class");
            }
        }


        //Sonderkosten
        Integer getTonnage = -1;
        Integer getMechMaxEngineRating = -1;
        Integer getMechMinEngineRating = -1;
        Double getMechBaseTons = -1.0;
        Integer getMechMaxJumpJets = -1;
        Integer getHP = -1;

        try {
            MechIdInfo mechInfo = new MechIdInfo(mechItemID);
            getTonnage = mechInfo.getTonnage();
            getMechMaxEngineRating = mechInfo.getMechMaxEngineRating();
            getMechMinEngineRating = mechInfo.getMechMinEngineRating();
            getMechBaseTons = mechInfo.getMechBaseTons();
            getMechMaxJumpJets = mechInfo.getMechMaxJumpJets();
            getHP = mechInfo.getHP();
        } catch (MechItemIdNotFoundException ignored) {

        }


        sumCost = ((getTonnage * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_OTHER_PER_TONS").getValue()) + mechVariantCost + mechClass);
        sumCost = sumCost + (getMechMaxEngineRating * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_OTHER_MAX_ENGINE_RATING").getValue());
        sumCost = sumCost + (getMechMinEngineRating * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_OTHER_MIN_ENGINE_RATING").getValue());
        sumCost = (long) (sumCost + (getMechBaseTons * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_OTHER_PER_BASE_TONS").getValue()));
        sumCost = sumCost + (getMechMaxJumpJets * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_OTHER_MAX_JUMP_JETS").getValue());
        sumCost = sumCost + (getHP * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_MECH_REPAIR_COST_OTHER_PER_HP").getValue());
        return (long) (100 - HealthPercentage) * sumCost / 100;
    }

    private void getUserInfo(List<BalanceUserInfo> userInfos, UserDetail detail) throws ParserConfigurationException, IOException, SAXException {
            BalanceUserInfo balanceUserInfo;
            String winningTeam = mwomatchResult.getMatchDetails().getWinningTeam();
            balanceUserInfo = new BalanceUserInfo(mwomatchResult);

            if (winningTeam != null) {
                if (Objects.equals(winningTeam, detail.getTeam())) {
                    balanceUserInfo.rewardLossVictory = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_VICTORY").getValue();
                    balanceUserInfo.rewardLossVictoryDescription = "Victory";
                } else {
                    balanceUserInfo.rewardLossVictory = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_LOSS").getValue();
                    balanceUserInfo.rewardLossVictoryDescription = "Loss";
                }
            } else {
                balanceUserInfo.rewardLossVictory = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_TIE").getValue();
                balanceUserInfo.rewardLossVictoryDescription = "Tie";
            }

            balanceUserInfo.rewardAssist = detail.getAssists() * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_ASSIST").getValue();
            balanceUserInfo.playerAssist = detail.getAssists();
            balanceUserInfo.userName = detail.getUsername();
            balanceUserInfo.playerDamage = detail.getDamage().longValue();
            balanceUserInfo.playerComponentDestroyed = detail.getComponentsDestroyed().longValue();
            balanceUserInfo.playerKills = detail.getKills().longValue();
            balanceUserInfo.playerMatchScore = detail.getMatchScore().longValue();
            balanceUserInfo.playerMechHealth = detail.getHealthPercentage().longValue();

            setMechName(detail, balanceUserInfo);

            balanceUserInfo.mechRepairCost = getMechCost(detail.getMechItemID(), detail.getHealthPercentage());
            balanceUserInfo.rewardComponentsDestroyed = detail.getComponentsDestroyed() * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_EACH_COMPONENT_DESTROYED").getValue();
            balanceUserInfo.rewardMatchScore = detail.getMatchScore() * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_EACH_MACHT_SCORE").getValue();
            balanceUserInfo.rewardDamage = detail.getDamage() * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_EACH_DAMAGE").getValue();

            if (detail.getTeamDamage() == 0) {
                balanceUserInfo.rewardTeamDamage = C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_NO_TEAM_DAMAGE").getValue();
            } else {
                balanceUserInfo.rewardTeamDamage = detail.getTeamDamage() * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_EACH_TEAM_DAMAGE").getValue();
            }

            balanceUserInfo.playerTeamDamage = detail.getTeamDamage().longValue();
            balanceUserInfo.rewardKill = detail.getKills() * C3GameConfigDAO.getInstance().findByKey(ServerNexus.END_ROUND_USERID, "C3_REWARD_EACH_KILL").getValue();
            balanceUserInfo.playerKills = detail.getKills().longValue();
            balanceUserInfo.subTotal = balanceUserInfo.rewardComponentsDestroyed + balanceUserInfo.rewardKill + balanceUserInfo.rewardDamage + balanceUserInfo.rewardTeamDamage + balanceUserInfo.rewardMatchScore + balanceUserInfo.mechRepairCost + balanceUserInfo.rewardLossVictory + balanceUserInfo.rewardAssist;

            userInfos.add(balanceUserInfo);
    }
}
