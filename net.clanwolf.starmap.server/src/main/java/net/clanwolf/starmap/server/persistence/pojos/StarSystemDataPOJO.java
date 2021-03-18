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
 * Copyright (c) 2001-2021, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.pojos;

import net.clanwolf.starmap.server.persistence.Pojo;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "_HH_STARSYSTEMDATA", catalog = "C3")
public class StarSystemDataPOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "StarSystemID")
	private Long starSystemID;

	@Column(name = "FactionID")
	private Long factionID;

	@Column(name = "FactionID_Start")
	private Long factionID_Start;

	@Column(name = "Infrastructure")
	private Long infrastructure;

	@Column(name = "Wealth")
	private Long wealth;

	@Column(name = "Veternacy")
	private Long veternacy;

	//@Column(name = "JumpshipName")
	//Class	int(11)
	@Column(name = "Type")
	private Long type;

	@Column(name = "Description")
	private String description;

	@Column(name = "S1_Map01ID")
	private Long s1_Map01ID;

	@Column(name = "S1_Map02ID")
	private Long s1_Map02ID;

	@Column(name = "S1_Map03ID")
	private Long s1_Map03ID;

	@Column(name = "S2_Map01ID")
	private Long s2_Map01ID;

	@Column(name = "S2_Map02ID")
	private Long s2_Map02ID;

	@Column(name = "S2_Map03ID")
	private Long s2_Map03ID;

	@Column(name = "S3_Map01ID")
	private Long s3_Map01ID;

	@Column(name = "S3_Map02ID")
	private Long s3_Map02ID;

	@Column(name = "S3_Map03ID")
	private Long s3_Map03ID;

	@Column(name = "Active")
	private Boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStarSystemID() {
		return starSystemID;
	}

	public void setStarSystemID(Long starSystemID) {
		this.starSystemID = starSystemID;
	}

	public Long getFactionID() {
		return factionID;
	}

	public void setFactionID(Long factionID) {
		this.factionID = factionID;
	}

	public Long getFactionID_Start() {
		return factionID_Start;
	}

	public void setFactionID_Start(Long factionID_Start) {
		this.factionID_Start = factionID_Start;
	}

	public Long getInfrastructure() {
		return infrastructure;
	}

	public void setInfrastructure(Long infrastructure) {
		this.infrastructure = infrastructure;
	}

	public Long getWealth() {
		return wealth;
	}

	public void setWealth(Long wealth) {
		this.wealth = wealth;
	}

	public Long getVeternacy() {
		return veternacy;
	}

	public void setVeternacy(Long veternacy) {
		this.veternacy = veternacy;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getS1_Map01ID() {
		return s1_Map01ID;
	}

	public void setS1_Map01ID(Long s1_Map01ID) {
		this.s1_Map01ID = s1_Map01ID;
	}

	public Long getS1_Map02ID() {
		return s1_Map02ID;
	}

	public void setS1_Map02ID(Long s1_Map02ID) {
		this.s1_Map02ID = s1_Map02ID;
	}

	public Long getS1_Map03ID() {
		return s1_Map03ID;
	}

	public void setS1_Map03ID(Long s1_Map03ID) {
		this.s1_Map03ID = s1_Map03ID;
	}

	public Long getS2_Map01ID() {
		return s2_Map01ID;
	}

	public void setS2_Map01ID(Long s2_Map01ID) {
		this.s2_Map01ID = s2_Map01ID;
	}

	public Long getS2_Map02ID() {
		return s2_Map02ID;
	}

	public void setS2_Map02ID(Long s2_Map02ID) {
		this.s2_Map02ID = s2_Map02ID;
	}

	public Long getS2_Map03ID() {
		return s2_Map03ID;
	}

	public void setS2_Map03ID(Long s2_Map03ID) {
		this.s2_Map03ID = s2_Map03ID;
	}

	public Long getS3_Map01ID() {
		return s3_Map01ID;
	}

	public void setS3_Map01ID(Long s3_Map01ID) {
		this.s3_Map01ID = s3_Map01ID;
	}

	public Long getS3_Map02ID() {
		return s3_Map02ID;
	}

	public void setS3_Map02ID(Long s3_Map02ID) {
		this.s3_Map02ID = s3_Map02ID;
	}

	public Long getS3_Map03ID() {
		return s3_Map03ID;
	}

	public void setS3_Map03ID(Long s3_Map03ID) {
		this.s3_Map03ID = s3_Map03ID;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
