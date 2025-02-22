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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.transfer.dtos;

import net.clanwolf.starmap.transfer.Dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UniverseDTO extends Dto {
	public HashMap<Long, StarSystemDataDTO> starSystems = new HashMap<>();
	public HashMap<String, FactionDTO> factions = new HashMap<>();
	public ArrayList<DiplomacyDTO> diplomacy = new ArrayList<>();
	public HashMap<String, JumpshipDTO> jumpships = new HashMap<>();
	public ArrayList<AttackDTO> attacks = new ArrayList<>();
	public ArrayList<RoutePointDTO> routepoints = new ArrayList<>();
	public HashMap<Long, RolePlayStoryDTO> attackStorys = new HashMap<>();

	public Date currentSeasonStartDate;
	public Long currentSeasonStartDateRealYear;
	public LocalDateTime currentRoundStartDateTime;
	public LocalDateTime currentRoundEndDateTime;
	public String lastRoundResultProtocol;

	public Integer currentSeason;
	public Integer currentSeasonMetaPhase;
	public Integer currentRound;
	public Integer currentRoundPhase;
	public String currentDate;
	public Double numberOfDaysInRound;
	public Integer maxNumberOfRoundsForSeason;
}
