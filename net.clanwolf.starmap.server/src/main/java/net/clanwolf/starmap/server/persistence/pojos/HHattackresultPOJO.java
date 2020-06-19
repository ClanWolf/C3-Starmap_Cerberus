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
 * Copyright (c) 2001-2020, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.pojos;

import net.clanwolf.starmap.server.persistence.Pojo;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "_HH_ATTACKRESULT", catalog = "C3")
public class HHattackresultPOJO extends Pojo {


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Season")
	private Long season;

	@Column(name = "Round")
	private Long round;

	@Column(name = "AttackID")
	private Long attackID;

	@Column(name = "FactionID_Winner")
	private Long factionID_Winner;

	//@Column(name = "Season")
	//private Long Outcome	enum('NOTFOUGHT','CONQUERED','DEFENDED')
	//@Column(name = "Season")
	//private Long MatchSize	enum('-','1vs1','2vs2','3vs3','4vs4','5vs5','6vs6','7vs7','8vs8','9vs9','10vs10','11vs11','12vs12')

	@Column(name = "LostMechs_Attacker")
	private Long lostMechs_Attacker;

	@Column(name = "LostMechs_Defender")
	private Long lostMechs_Defender;

	@Column(name = "API_ID_1")
	private String API_ID_1;

	@Column(name = "API_ID_2")
	private String API_ID_2;

	@Column(name = "API_ID_3")
	private String API_ID_3;

	@Column(name = "Match_Details_1")
	private String match_Details_1;

	@Column(name = "Match_Details_2")
	private String match_Details_2;

	@Column(name = "Match_Details_3")
	private String match_Details_3;

	@Column(name = "ResignedLastDrop")
	private Boolean resignedLastDrop;

	@Column(name = "Zellbrigen")
	private Boolean zellbrigen;

	@Column(name = "Remarks")
	private String remarks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSeason() {
		return season;
	}

	public void setSeason(Long season) {
		this.season = season;
	}

	public Long getRound() {
		return round;
	}

	public void setRound(Long round) {
		this.round = round;
	}

	public Long getAttackID() {
		return attackID;
	}

	public void setAttackID(Long attackID) {
		this.attackID = attackID;
	}

	public Long getFactionID_Winner() {
		return factionID_Winner;
	}

	public void setFactionID_Winner(Long factionID_Winner) {
		this.factionID_Winner = factionID_Winner;
	}

	public Long getLostMechs_Attacker() {
		return lostMechs_Attacker;
	}

	public void setLostMechs_Attacker(Long lostMechs_Attacker) {
		this.lostMechs_Attacker = lostMechs_Attacker;
	}

	public Long getLostMechs_Defender() {
		return lostMechs_Defender;
	}

	public void setLostMechs_Defender(Long lostMechs_Defender) {
		this.lostMechs_Defender = lostMechs_Defender;
	}

	public String getAPI_ID_1() {
		return API_ID_1;
	}

	public void setAPI_ID_1(String API_ID_1) {
		this.API_ID_1 = API_ID_1;
	}

	public String getAPI_ID_2() {
		return API_ID_2;
	}

	public void setAPI_ID_2(String API_ID_2) {
		this.API_ID_2 = API_ID_2;
	}

	public String getAPI_ID_3() {
		return API_ID_3;
	}

	public void setAPI_ID_3(String API_ID_3) {
		this.API_ID_3 = API_ID_3;
	}

	public String getMatch_Details_1() {
		return match_Details_1;
	}

	public void setMatch_Details_1(String match_Details_1) {
		this.match_Details_1 = match_Details_1;
	}

	public String getMatch_Details_2() {
		return match_Details_2;
	}

	public void setMatch_Details_2(String match_Details_2) {
		this.match_Details_2 = match_Details_2;
	}

	public String getMatch_Details_3() {
		return match_Details_3;
	}

	public void setMatch_Details_3(String match_Details_3) {
		this.match_Details_3 = match_Details_3;
	}

	public Boolean getResignedLastDrop() {
		return resignedLastDrop;
	}

	public void setResignedLastDrop(Boolean resignedLastDrop) {
		this.resignedLastDrop = resignedLastDrop;
	}

	public Boolean getZellbrigen() {
		return zellbrigen;
	}

	public void setZellbrigen(Boolean zellbrigen) {
		this.zellbrigen = zellbrigen;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
