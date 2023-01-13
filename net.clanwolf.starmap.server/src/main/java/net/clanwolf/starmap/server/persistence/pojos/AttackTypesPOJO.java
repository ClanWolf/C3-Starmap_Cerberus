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
package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import jakarta.persistence.*;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * @author Undertaker
 *
 */
@JsonIdentityInfo(
		scope= AttackTypesPOJO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "_HH_ATTACK_TYPE", catalog = "C3")
public class AttackTypesPOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	public Long id;

	@Column(name = "AttackTypeShort")
	private String attackTypeShort;

	@Column(name = "AttackTypeName")
	private String attackTypeName;

	@Column(name = "CLAN_IS_StoryIds")
	private String CLAN_IS_StoryIds;

	@Column(name = "CLAN_vs_CLAN_StoryIds")
	private String CLAN_vs_CLAN_StoryIds;

	@Column(name = "IS_vs_CLAN_StoryIds")
	private String IS_vs_CLAN_StoryIds;

	@Column(name = "IS_vs_IS_StoryIds")
	private String IS_vs_IS_StoryIds;

	public AttackTypesPOJO() {
		//
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttackTypeShort() {
		return attackTypeShort;
	}

	public void setAttackTypeShort(String attackTypeShort) {
		this.attackTypeShort = attackTypeShort;
	}

	public String getAttackTypeName() {
		return attackTypeName;
	}

	public void setAttackTypeName(String attackTypeName) {
		this.attackTypeName = attackTypeName;
	}

	public String getCLAN_IS_StoryIds() {
		return CLAN_IS_StoryIds;
	}

	public void setCLAN_IS_StoryIds(String CLAN_IS_StoryIds) {
		this.CLAN_IS_StoryIds = CLAN_IS_StoryIds;
	}

	public String getCLAN_vs_CLAN_StoryIds() {
		return CLAN_vs_CLAN_StoryIds;
	}

	public void setCLAN_vs_CLAN_StoryIds(String CLAN_vs_CLAN_StoryIds) {
		this.CLAN_vs_CLAN_StoryIds = CLAN_vs_CLAN_StoryIds;
	}

	public String getIS_vs_CLAN_StoryIds() {
		return IS_vs_CLAN_StoryIds;
	}

	public void setIS_vs_CLAN_StoryIds(String IS_vs_CLAN_StoryIds) {
		this.IS_vs_CLAN_StoryIds = IS_vs_CLAN_StoryIds;
	}

	public String getIS_vs_IS_StoryIds() {
		return IS_vs_IS_StoryIds;
	}

	public void setIS_vs_IS_StoryIds(String IS_vs_IS_StoryIds) {
		this.IS_vs_IS_StoryIds = IS_vs_IS_StoryIds;
	}
}
