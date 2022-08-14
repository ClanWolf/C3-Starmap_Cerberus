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
package net.clanwolf.starmap.constants;

import java.io.Serial;

public class Constants implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //XP rewards during an invasion
    public static final long XP_REWARD_VICTORY = 5;
    public static final long XP_REWARD_LOSS = 2;
    public static final long XP_REWARD_COMPONENT_DESTROYED = 1;
    public static final long XP_REWARD_EACH_MATCH_SCORE = 1;
    public static final long XP_REWARD_EACH_MATCH_SCORE_RANGE = 150;
    public static final long XP_REWARD_EACH_DAMAGE = 1;
    public static final long XP_REWARD_EACH_DAMAGE_RANGE = 200;
    public static final long XP_REWARD_EACH_SURVIVAL_PERCENTAGE = 1;
    public static final long XP_REWARD_EACH_SURVIVAL_PERCENTAGE_RANGE = 10;
    public static final long XP_REWARD_EACH_TEAM_DAMAGE = 1;
    public static final long XP_REWARD_EACH_TEAM_DAMAGE_RANGE = 1;


    //Reward payments when calculating the cost
    public static final long REWARD_VICTORY = 250_000;
    public static final long REWARD_LOSS = 125_000;
    public static final long REWARD_ASSIST = 75_000;
    public static final long REWARD_EACH_COMPONENT_DESTROYED = 250_000;
    public static final long REWARD_EACH_MACHT_SCORE = 2_500;
    public static final long REWARD_EACH_DAMAGE = 1_500;
    public static final long REWARD_EACH_TEAM_DAMAGE = -10_000;
    public static final long REWARD_NO_TEAM_DAMAGE = 100_000;
    public static final long REWARD_EACH_KILL = 450_000;

    // Cost
    public static final long REGULAR_SYSTEM_GENERAL_INCOME = 250;
    public static final long INDUSTRIAL_SYSTEM_GENERAL_INCOME = 1_500;
    public static final long CAPITAL_SYSTEM_GENERAL_INCOME = 5_000;
    public static final long FACTION_CAPITAL_SYSTEM_GENERAL_INCOME = 25_000;
    public static final long TERRA_GENERAL_INCOME = 55_000;

    public static final long REGULAR_SYSTEM_GENERAL_COST = -150;
    public static final long INDUSTRIAL_SYSTEM_GENERAL_COST = -1_000;
    public static final long CAPITAL_SYSTEM_GENERAL_COST = -2_000;
    public static final long FACTION_CAPITAL_SYSTEM_GENERAL_COST = -17_000;
    public static final long TERRA_GENERAL_COST = -22_000;

    public static final long REGULAR_SYSTEM_DEFEND_COST = -120;
    public static final long INDUSTRIAL_SYSTEM_DEFEND_COST = -300;
    public static final long CAPITAL_SYSTEM_DEFEND_COST = -500;
    public static final long FACTION_CAPITAL_SYSTEM_DEFEND_COST = -1500;
    public static final long TERRA_DEFEND_COST = -11500;

    public static final long REGULAR_SYSTEM_ATTACK_COST = -3_000;
    public static final long INDUSTRIAL_SYSTEM_ATTACK_COST = -6_000;
    public static final long CAPITAL_SYSTEM_ATTACK_COST = -10_000;
    public static final long FACTION_CAPITAL_SYSTEM_ATTACK_COST = -20_000;
    public static final long TERRA_ATTACK_COST = -100_000;

    // Game - Meta
    public static final long ROUNDS_TO_LOCK_SYSTEM_AFTER_ATTACK = 3;
    public static final long MINIMUM_PILOTS_PER_SIDE_IN_INVASION_DROP = 1;

    // Game - Invasion
    public static final long ROLE_ATTACKER_WARRIOR = 0L; // 0L Attacker Warrior
    public static final long ROLE_ATTACKER_COMMANDER = 1L; // 1L Attacker Commander
    public static final long ROLE_DEFENDER_WARRIOR = 2L; // 2L Defender
    public static final long ROLE_DEFENDER_COMMANDER = 3L; // 3L Defender Commander
    public static final long ROLE_SUPPORTER = 4L; // 4L Attacker Supporter
    public static final long ROLE_ATTACKER_SUPPORTER = 5L; // 4L Attacker Supporter
    public static final long ROLE_DEFENDER_SUPPORTER = 6L; // 5L Defender Supporter

    // Game - Jumpship XP
    public static final long JUMPSHIP_XP_ATTACK_VICTORY = 100;
    public static final long JUMPSHIP_XP_ATTACK_DEFEAT = 50;
    public static final long JUMPSHIP_XP_ATTACK_VICTORY_AUTOMATION = 10;
    public static final long JUMPSHIP_XP_ATTACK_DEFEAT_AUTOMATION = 5;

}
