package net.clanwolf.starmap.server.process;

import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import net.clanwolf.starmap.transfer.mwo.MechIdInfo;
import net.clanwolf.starmap.transfer.mwo.UserDetail;

import java.util.ArrayList;
import java.util.List;

import static net.clanwolf.starmap.constants.Constants.*;

public class BalanceUserInfo {
    /**
     * Der Benutzername, wie er in MWO angezeigt wird.
     */
    public String userName;
    /**
     * Die Anzahl des Schadens, den Spieler mit dem Mech erzielt hat.
     */
    public long damage;
    /**
     * Anzahl der Komponenten, die der Spieler zerstört hat.
     */
    public long componentDestroyed;
    /**
     * Die Anzahl kills, die der Spieler erreicht hat.
     */
    public long kills;
    /**
     * Dia Anzahl an Match-score, den der Spieler erreicht hat.
     */
    public long matchScore;
    /**
     * Der Gesundheit zustand des Mechs, am Ende des Kampfes.
     */
    public long mechHealth;
    /**
     * Mech-Informationen über den Spieler.
     */
    public MechIdInfo mechName;
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
    public long teamDamage;
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
     *Berechnet die kosten für den Angreifer und den Verteidiger.
     * @param mwomatchResult MWOMachtResult
     */
    public BalanceUserInfo(MWOMatchResult mwomatchResult) {
        this.mwomatchResult = mwomatchResult;
    }

    /**
     * Listet die kosten für den Angreifer auf.
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

        balanceUserInfo = new BalanceUserInfo(mwomatchResult);
        balanceUserInfo.userName = (detail.getUsername());
        balanceUserInfo.damage = (detail.getDamage().longValue());
        balanceUserInfo.componentDestroyed = (detail.getComponentsDestroyed().longValue());
        balanceUserInfo.kills = (detail.getKills().longValue());
        balanceUserInfo.matchScore = (detail.getMatchScore().longValue());
        balanceUserInfo.mechHealth = (detail.getHealthPercentage().longValue());
        mechIdInfo = new MechIdInfo(detail.getMechItemID());
        balanceUserInfo.mechName = (mechIdInfo);
        balanceUserInfo.mechRepairCost = ((long) mechIdInfo.getRepairCost(detail.getHealthPercentage()));
        balanceUserInfo.rewardComponentsDestroyed = (detail.getComponentsDestroyed() * REWARD_EACH_COMPONENT_DESTROYED);
        balanceUserInfo.rewardMatchScore = (detail.getMatchScore() * REWARD_EACH_MACHT_SCORE);
        balanceUserInfo.rewardDamage = (detail.getDamage() * REWARD_EACH_DAMAGE);
        balanceUserInfo.rewardTeamDamage = (detail.getTeamDamage() * REWARD_EACH_TEAM_DAMAGE);
        balanceUserInfo.teamDamage = (detail.getTeamDamage().longValue());
        balanceUserInfo.rewardKill = (detail.getKills() * REWARD_EACH_KILL);
        balanceUserInfo.kills = (detail.getKills().longValue());
        balanceUserInfo.subTotal = balanceUserInfo.rewardComponentsDestroyed +
                balanceUserInfo.rewardKill +
                balanceUserInfo.rewardDamage +
                balanceUserInfo.rewardTeamDamage +
                balanceUserInfo.rewardMatchScore +
                balanceUserInfo.mechRepairCost;

        userInfos.add(balanceUserInfo);
    }
}
