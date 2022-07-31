package net.clanwolf.starmap.server.process;

import net.clanwolf.starmap.transfer.mwo.MWOMatchResult;
import net.clanwolf.starmap.transfer.mwo.MechIdInfo;
import net.clanwolf.starmap.transfer.mwo.UserDetail;

import java.util.ArrayList;
import java.util.List;

import static net.clanwolf.starmap.constants.Constants.*;

public class BalanceUserInfo {
    public String userName;
    public long damage;
    public long componentDestroyed;
    public long kills;
    public long matchScore;
    public long mechHealth;
    public String mechName;
    public long mechRepairCost;
    public long rewardComponentsDestroyed;
    public long rewardMatchScore;
    public long rewardDamage;
    public long rewardTeamDamage;
    public long teamDamage;
    public long rewardKill;
    public Long subTotal;
    private final MWOMatchResult mwomatchResult;

    public BalanceUserInfo(MWOMatchResult mwomatchResult) {
        this.mwomatchResult = mwomatchResult;
    }

    public List<BalanceUserInfo> GetAttackerInfo() {
        List<BalanceUserInfo> attacker = new ArrayList<>();

        for (UserDetail detail : mwomatchResult.getUserDetails()) {
            if ("2".equals(detail.getTeam()) && detail.getTeam() != null) {

                getUserInfo(attacker, detail);
            }
        }
        return attacker;
    }

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
        balanceUserInfo.mechName = (mechIdInfo.getShortname());
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
