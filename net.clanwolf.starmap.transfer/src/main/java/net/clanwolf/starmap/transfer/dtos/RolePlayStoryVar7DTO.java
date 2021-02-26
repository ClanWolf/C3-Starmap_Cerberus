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
package net.clanwolf.starmap.transfer.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.transfer.Dto;


@JsonIdentityInfo(
		scope= RolePlayStoryVar7DTO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RolePlayStoryVar7DTO extends Dto {


	//@Column(name = "ID")
	private Long id;

	//@JoinColumn(name = "StoryID")
	private Long story;

	//@Column(name = "Faction")
	private Long factionID;

	//@Column(name = "ServiceName")
	private String serviceName;

	//@Column(name = "Header")
	private String header;

	//@Column(name = "Sender")
	private String sender;

	//@Column(name = "Date")
	private String date;

	//@JoinColumn(name = "NextStepID")
	private Long nextStepID;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStory() {
		return story;
	}

	public void setStory(Long story) {
		this.story = story;
	}

	public Long getFaction() {
		return factionID;
	}

	public void setFaction(Long faction) {
		this.factionID = faction;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getNextStepID() {
		return nextStepID;
	}

	public void setNextStepID(Long nextStepID) {
		this.nextStepID = nextStepID;
	}
}
