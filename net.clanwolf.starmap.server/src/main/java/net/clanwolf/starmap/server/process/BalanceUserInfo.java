package net.clanwolf.starmap.server.process;

import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import net.clanwolf.starmap.transfer.mwo.MechIdInfo;
import net.clanwolf.starmap.transfer.mwo.UserDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.clanwolf.starmap.constants.Constants.*;

public class BalanceUserInfo {
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
    public MechIdInfo playerMechName;
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
    private final MWOMatchResult mwomatchResult;

    /**
     * Berechnet die kosten für den Angreifer und den Verteidiger.
     *
     * @param mwomatchResult MWOMachtResult
     */
    public BalanceUserInfo(MWOMatchResult mwomatchResult) {
        this.mwomatchResult = mwomatchResult;
    }

    /**
     * Listet die kosten für den Angreifer auf.
     *
     * @return Gibt ein ArrayList für die kosten für den Angreifer zurück.
     */
    public List<BalanceUserInfo> GetAttackerInfo() {
        List<BalanceUserInfo> attacker = new ArrayList<>();

        for (UserDetail detail : mwomatchResult.getUserDetails()) {
            if ("2".equals(detail.getTeam()) && detail.getTeam() != null) {

                getUserInfo(attacker, detail);
            }
        }
        return attacker;
    }

    /**
     * Listet die kosten für den Verteidiger auf.
     *
     * @return Gibt ein ArrayList für die kosten für den Verteidiger zurück.
     */
    public List<BalanceUserInfo> GetDefenderInfo() {
        List<BalanceUserInfo> defender = new ArrayList<>();

        for (UserDetail detail : mwomatchResult.getUserDetails()) {
            if ("1".equals(detail.getTeam()) && detail.getTeam() != null) {

                getUserInfo(defender, detail);
            }
        }
        return defender;
    }

    private void getUserInfo(List<BalanceUserInfo> userInfos, UserDetail detail) {
        BalanceUserInfo balanceUserInfo;
        MechIdInfo mechIdInfo;

        String winningTeam = mwomatchResult.getMatchDetails().getWinningTeam();

        balanceUserInfo = new BalanceUserInfo(mwomatchResult);
        if (Objects.equals(winningTeam, detail.getTeam())) {
            balanceUserInfo.rewardLossVictory = REWARD_VICTORY;
            balanceUserInfo.rewardLossVictoryDescription = "Victory";
        } else {
            balanceUserInfo.rewardLossVictory = REWARD_LOSS;
            balanceUserInfo.rewardLossVictoryDescription = "Loss";
        }

        balanceUserInfo.rewardAssist = detail.getAssists() * REWARD_ASSIST;
        balanceUserInfo.playerAssist = detail.getAssists();
        balanceUserInfo.userName = detail.getUsername();
        balanceUserInfo.playerDamage = detail.getDamage().longValue();
        balanceUserInfo.playerComponentDestroyed = detail.getComponentsDestroyed().longValue();
        balanceUserInfo.playerKills = detail.getKills().longValue();
        balanceUserInfo.playerMatchScore = detail.getMatchScore().longValue();
        balanceUserInfo.playerMechHealth = detail.getHealthPercentage().longValue();
        mechIdInfo = new MechIdInfo(detail.getMechItemID());
        balanceUserInfo.playerMechName = mechIdInfo;
        balanceUserInfo.mechRepairCost = ((long) mechIdInfo.getRepairCost(detail.getHealthPercentage()));
        balanceUserInfo.rewardComponentsDestroyed = detail.getComponentsDestroyed() * REWARD_EACH_COMPONENT_DESTROYED;
        balanceUserInfo.rewardMatchScore = detail.getMatchScore() * REWARD_EACH_MACHT_SCORE;
        balanceUserInfo.rewardDamage = detail.getDamage() * REWARD_EACH_DAMAGE;

        if (detail.getTeamDamage() == 0) {
            balanceUserInfo.rewardTeamDamage = REWARD_NO_TEAM_DAMAGE;
        } else {
            balanceUserInfo.rewardTeamDamage = detail.getTeamDamage() * REWARD_EACH_TEAM_DAMAGE;
        }

        balanceUserInfo.playerTeamDamage = detail.getTeamDamage().longValue();
        balanceUserInfo.rewardKill = detail.getKills() * REWARD_EACH_KILL;
        balanceUserInfo.playerKills = detail.getKills().longValue();
        balanceUserInfo.subTotal = balanceUserInfo.rewardComponentsDestroyed +
                balanceUserInfo.rewardKill +
                balanceUserInfo.rewardDamage +
                balanceUserInfo.rewardTeamDamage +
                balanceUserInfo.rewardMatchScore +
                balanceUserInfo.mechRepairCost +
                balanceUserInfo.rewardLossVictory +
                balanceUserInfo.rewardAssist;

        userInfos.add(balanceUserInfo);
    }
}
