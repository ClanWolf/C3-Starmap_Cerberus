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
package net.clanwolf.starmap.server.persistence.pojos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;

import javax.persistence.*;
import java.sql.Date;

import static javax.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= RolePlayCharacterLevelPOJO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "level")

@Entity
@Table(name = "ROLEPLAY_CHARACTER_LEVEL", catalog = "C3")
public class RolePlayCharacterLevelPOJO extends Pojo {

	@Id
	@Column(name = "Level")
	private Long level;

	@Column(name = "XP")
	private Long xp;

	@SuppressWarnings("unused")
	public Long getLevel() {
		return level;
	}

	@SuppressWarnings("unused")
	public void setLevel(Long level) {
		this.level = level;
	}

	@SuppressWarnings("unused")
	public Long getXp() {
		return xp;
	}

	@SuppressWarnings("unused")
	public void setXp(Long xp) {
		this.xp = xp;
	}
}