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
		scope= SeasonPOJO.class,
		generator= ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "_HH_SEASON", catalog = "C3")
public class SeasonPOJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "Name")
	private String name;

	@Column(name = "Ended")
	private Boolean ended;

	@Column(name = "MetaPhase")
	private Long metaPhase;

	@Column(name = "TFSArrivalRound")
	private Long serpentArrivalRound;

	@Column(name = "Description")
	private String description;

	@Column(name = "StartDate")
	private Date startDate;

	@Column(name = "DaysInRound")
	private Long daysInRound;

	@SuppressWarnings("unused")
	public Long getDaysInRound() {
		return daysInRound;
	}

	@SuppressWarnings("unused")
	public void setDaysInRound(Long daysInRound) {
		this.daysInRound = daysInRound;
	}

	@SuppressWarnings("unused")
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	public void setId(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
	public String getName() {
		return name;
	}

	@SuppressWarnings("unused")
	public void setName(String name) {
		this.name = name;
	}

	@SuppressWarnings("unused")
	public Boolean getEnded() {
		return ended;
	}

	@SuppressWarnings("unused")
	public void setEnded(Boolean ended) {
		this.ended = ended;
	}

	@SuppressWarnings("unused")
	public String getDescription() {
		return description;
	}

	@SuppressWarnings("unused")
	public void setDescription(String description) {
		this.description = description;
	}

	@SuppressWarnings("unused")
	public Date getStartDate() {
		return startDate;
	}

	@SuppressWarnings("unused")
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@SuppressWarnings("unused")
	public Long getMetaPhase() {
		return metaPhase;
	}

	@SuppressWarnings("unused")
	public void setMetaPhase(Long metaPhase) {
		this.metaPhase = metaPhase;
	}

	@SuppressWarnings("unused")
	public Long getSerpentArrivalRound() {
		return serpentArrivalRound;
	}

	@SuppressWarnings("unused")
	public void setSerpentArrivalRound(Long serpentArrivalRound) {
		this.serpentArrivalRound = serpentArrivalRound;
	}
}
