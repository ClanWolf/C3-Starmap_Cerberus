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
package net.clanwolf.starmap.transfer.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.transfer.Dto;

/**
 * @author Undertaker
 *
 */
@JsonIdentityInfo(
		scope= RolePlayCharacterDTO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RolePlayCharacterDTO extends Dto {

    //@Column(name = "ID")
    public Long id;
    //@Column(name = "CharName")
    private String name;
    //@JoinColumn(name = "UserID")
    private UserDTO user;
    //@JoinColumn(name = "StoryID")
    private RolePlayStoryDTO story;
	//@Column(name = "FactionID")
	private Integer factionId;
	//@Column(name = "StarsystemID")
	private Integer starSystemId;
	//@Column(name = "JumpshipID")
	private Integer jumpshipId;

	public RolePlayCharacterDTO(){
		 
	}

	/**
	 * @return the id
	 */
	@SuppressWarnings("unused")
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@SuppressWarnings("unused")
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	@SuppressWarnings("unused")
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	@SuppressWarnings("unused")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the user
	 */
	@SuppressWarnings("unused")
	public UserDTO getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	@SuppressWarnings("unused")
	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	/**
	 * @return the story
	 */
	@SuppressWarnings("unused")
	public RolePlayStoryDTO getStory() {
		return story;
	}

	/**
	 * @param story the story to set
	 */
	@SuppressWarnings("unused")
	public void setStory(RolePlayStoryDTO story) {
		this.story = story;
	}

	@SuppressWarnings("unused")
	public Integer getFactionId() {
		return factionId;
	}

	@SuppressWarnings("unused")
	public void setFactionId(Integer factionId) {
		this.factionId = factionId;
	}

	@SuppressWarnings("unused")
	public Integer getStarSystemId() {
		return starSystemId;
	}

	@SuppressWarnings("unused")
	public void setStarSystemId(Integer starSystemId) {
		this.starSystemId = starSystemId;
	}

	@SuppressWarnings("unused")
	public Integer getJumpshipId() {
		return jumpshipId;
	}

	@SuppressWarnings("unused")
	public void setJumpshipId(Integer jumpshipId) {
		this.jumpshipId = jumpshipId;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
