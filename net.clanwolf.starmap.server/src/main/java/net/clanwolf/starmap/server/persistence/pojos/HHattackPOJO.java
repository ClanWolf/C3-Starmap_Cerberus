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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.pojos;

import net.clanwolf.starmap.server.persistence.Pojo;
import net.clanwolf.starmap.transfer.enums.GAMES;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "_HH_ATTACK", catalog = "C3")
public class HHattackPOJO extends Pojo{


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Season")
	private Long season;

	@Column(name = "Round")
	private Long round;

	@Column(name = "Priority")
	private Long priority;

	@Column(name = "DeclaredBy_UserID_C3")
	private Long  declaredBy_UserID_C3;

	@Column(name = "DeclaredBy_UserID_CWG")
	private Long  declaredBy_UserID_CWG;

	@Column(name = "StarSystemID")
	private Long  starSystemID;

	@Column(name = "StarSystemDataID")
	private Long  starSystemDataID;

	@Column(name = "AttackedFromStarSystemID")
	private Long  attackedFromStarSystemID;

	@Column(name = "AttackTypeID")
	private Long  attackTypeID;

	@Column(name = "FactionID_Attacker")
	private Long  factionID_Attacker;

	@Column(name = "FactionID_Defender")
	private Long  factionID_Defender;

	@Column(name = "AttackerTeam")
	private Long  attackerTeam;

	@Column(name = "DefenderTeam")
	private Long  defenderTeam;

	@Column(name = "Game")
	@Enumerated(EnumType.STRING)
	private GAMES game;

	@Column(name = "Map1ID")
	private Long  map1ID;

	@Column(name = "Map2ID")
	private Long  map2ID;

	@Column(name = "Map3ID")
	private Long  map3ID;

	@Column(name = "TonnageFactor")
	private Double  tonnageFactor;

	@Column(name = "01vs01_TonnageDefender")
	private Long  TonnageDefender01vs01;

	@Column(name = "02vs02_TonnageDefender")
	private Long  TonnageDefender02vs02;

	@Column(name = "03vs03_TonnageDefender")
	private Long  TonnageDefender03vs03;

	@Column(name = "04vs04_TonnageDefender")
	private Long  TonnageDefender04vs04;

	@Column(name = "05vs05_TonnageDefender")
	private Long  TonnageDefender05vs05;

	@Column(name = "06vs06_TonnageDefender")
	private Long  TonnageDefender06vs06;

	@Column(name = "07vs07_TonnageDefender")
	private Long  TonnageDefender07vs07;

	@Column(name = "08vs08_TonnageDefender")
	private Long  TonnageDefender08vs08;

	@Column(name = "09vs09_TonnageDefender")
	private Long  TonnageDefender09vs09;

	@Column(name = "10vs10_TonnageDefender")
	private Long  TonnageDefender10vs10;

	@Column(name = "11vs11_TonnageDefender")
	private Long  TonnageDefender11vs11;

	@Column(name = "12vs12_TonnageDefender")
	private Long  TonnageDefender12vs12;

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

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public Long getDeclaredBy_UserID_C3() {
		return declaredBy_UserID_C3;
	}

	public void setDeclaredBy_UserID_C3(Long declaredBy_UserID_C3) {
		this.declaredBy_UserID_C3 = declaredBy_UserID_C3;
	}

	public Long getDeclaredBy_UserID_CWG() {
		return declaredBy_UserID_CWG;
	}

	public void setDeclaredBy_UserID_CWG(Long declaredBy_UserID_CWG) {
		this.declaredBy_UserID_CWG = declaredBy_UserID_CWG;
	}

	public Long getStarSystemID() {
		return starSystemID;
	}

	public void setStarSystemID(Long starSystemID) {
		this.starSystemID = starSystemID;
	}

	public Long getStarSystemDataID() {
		return starSystemDataID;
	}

	public void setStarSystemDataID(Long starSystemDataID) {
		this.starSystemDataID = starSystemDataID;
	}

	public Long getAttackedFromStarSystemID() {
		return attackedFromStarSystemID;
	}

	public void setAttackedFromStarSystemID(Long attackedFromStarSystemID) {
		this.attackedFromStarSystemID = attackedFromStarSystemID;
	}

	public Long getAttackTypeID() {
		return attackTypeID;
	}

	public void setAttackTypeID(Long attackTypeID) {
		this.attackTypeID = attackTypeID;
	}

	public Long getFactionID_Attacker() {
		return factionID_Attacker;
	}

	public void setFactionID_Attacker(Long factionID_Attacker) {
		this.factionID_Attacker = factionID_Attacker;
	}

	public Long getFactionID_Defender() {
		return factionID_Defender;
	}

	public void setFactionID_Defender(Long factionID_Defender) {
		this.factionID_Defender = factionID_Defender;
	}

	public Long getAttackerTeam() {
		return attackerTeam;
	}

	public void setAttackerTeam(Long attackerTeam) {
		this.attackerTeam = attackerTeam;
	}

	public Long getDefenderTeam() {
		return defenderTeam;
	}

	public void setDefenderTeam(Long defenderTeam) {
		this.defenderTeam = defenderTeam;
	}

	public Long getMap1ID() {
		return map1ID;
	}

	public void setMap1ID(Long map1ID) {
		this.map1ID = map1ID;
	}

	public Long getMap2ID() {
		return map2ID;
	}

	public void setMap2ID(Long map2ID) {
		this.map2ID = map2ID;
	}

	public Long getMap3ID() {
		return map3ID;
	}

	public void setMap3ID(Long map3ID) {
		this.map3ID = map3ID;
	}

	public Double getTonnageFactor() {
		return tonnageFactor;
	}

	public void setTonnageFactor(Double tonnageFactor) {
		this.tonnageFactor = tonnageFactor;
	}

	public Long getTonnageDefender01vs01() {
		return TonnageDefender01vs01;
	}

	public void setTonnageDefender01vs01(Long tonnageDefender01vs01) {
		TonnageDefender01vs01 = tonnageDefender01vs01;
	}

	public Long getTonnageDefender02vs02() {
		return TonnageDefender02vs02;
	}

	public void setTonnageDefender02vs02(Long tonnageDefender02vs02) {
		TonnageDefender02vs02 = tonnageDefender02vs02;
	}

	public Long getTonnageDefender03vs03() {
		return TonnageDefender03vs03;
	}

	public void setTonnageDefender03vs03(Long tonnageDefender03vs03) {
		TonnageDefender03vs03 = tonnageDefender03vs03;
	}

	public Long getTonnageDefender04vs04() {
		return TonnageDefender04vs04;
	}

	public void setTonnageDefender04vs04(Long tonnageDefender04vs04) {
		TonnageDefender04vs04 = tonnageDefender04vs04;
	}

	public Long getTonnageDefender05vs05() {
		return TonnageDefender05vs05;
	}

	public void setTonnageDefender05vs05(Long tonnageDefender05vs05) {
		TonnageDefender05vs05 = tonnageDefender05vs05;
	}

	public Long getTonnageDefender06vs06() {
		return TonnageDefender06vs06;
	}

	public void setTonnageDefender06vs06(Long tonnageDefender06vs06) {
		TonnageDefender06vs06 = tonnageDefender06vs06;
	}

	public Long getTonnageDefender07vs07() {
		return TonnageDefender07vs07;
	}

	public void setTonnageDefender07vs07(Long tonnageDefender07vs07) {
		TonnageDefender07vs07 = tonnageDefender07vs07;
	}

	public Long getTonnageDefender08vs08() {
		return TonnageDefender08vs08;
	}

	public void setTonnageDefender08vs08(Long tonnageDefender08vs08) {
		TonnageDefender08vs08 = tonnageDefender08vs08;
	}

	public Long getTonnageDefender09vs09() {
		return TonnageDefender09vs09;
	}

	public void setTonnageDefender09vs09(Long tonnageDefender09vs09) {
		TonnageDefender09vs09 = tonnageDefender09vs09;
	}

	public Long getTonnageDefender10vs10() {
		return TonnageDefender10vs10;
	}

	public void setTonnageDefender10vs10(Long tonnageDefender10vs10) {
		TonnageDefender10vs10 = tonnageDefender10vs10;
	}

	public Long getTonnageDefender11vs11() {
		return TonnageDefender11vs11;
	}

	public void setTonnageDefender11vs11(Long tonnageDefender11vs11) {
		TonnageDefender11vs11 = tonnageDefender11vs11;
	}

	public Long getTonnageDefender12vs12() {
		return TonnageDefender12vs12;
	}

	public void setTonnageDefender12vs12(Long tonnageDefender12vs12) {
		TonnageDefender12vs12 = tonnageDefender12vs12;
	}

	public GAMES getGame() {
		return game;
	}

	public void setGame(GAMES game) {
		this.game = game;
	}
}
