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
 * Copyright (c) 2001-2024, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.transfer.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.transfer.Dto;

@JsonIdentityInfo(
		scope= RolePlayStoryVar9DTO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RolePlayStoryVar9DTO extends Dto {

	//@Column(name = "ID")
	private Long id;
	//@JoinColumn(name = "StoryID")
	//private RolePlayStoryDTO story;
	private Long story;
	//@JoinColumn(name = "Option1StoryID")
	private Long option1StoryID;
	//@Column(name = "Option1Text")
	private String option1Text;
	//@JoinColumn(name = "Option2StoryID")
	private Long option2StoryID;
	//@Column(name = "Option2Text")
	private String option2Text;
	//@JoinColumn(name = "Option3StoryID")
	private Long option3StoryID;
	//@Column(name = "Option3Text")
	private String option3Text;
	//@JoinColumn(name = "Option4StoryID")
	private Long option4StoryID;
	//@Column(name = "Option4Text")
	private String option4Text;
	//@Column(name = "DefenderDropVictories")
	private Integer defenderDropVictories;
	//@Column(name = "AttackerDropVictories")
	private Integer attackerDropVictories;

	/* -- Getter -- */
	public Long getId() {
		return id;

	} // getId

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the story
	 */
	public Long getStory() {
		return story;
	}

	/**
	 * @param story the story to set
	 */
	public void setStory(Long story) {
		this.story = story;
	}

	/**
	 * @return the option1StoryID
	 */
	public Long getOption1StoryID() {
		return option1StoryID;
	}

	/**
	 * @param option1StoryID the option1StoryID to set
	 */
	public void setOption1StoryID(Long option1StoryID) {
		this.option1StoryID = option1StoryID;
	}

	/**
	 * @return the option1Text
	 */
	public String getOption1Text() {
		return option1Text;
	}

	/**
	 * @param option1Text the option1Text to set
	 */
	public void setOption1Text(String option1Text) {
		this.option1Text = option1Text;
	}

	/**
	 * @return the option2StoryID
	 */
	public Long getOption2StoryID() {
		return option2StoryID;
	}

	/**
	 * @param option2StoryID the option2StoryID to set
	 */
	public void setOption2StoryID(Long option2StoryID) {
		this.option2StoryID = option2StoryID;
	}

	/**
	 * @return the option2Text
	 */
	public String getOption2Text() {
		return option2Text;
	}

	/**
	 * @param option2Text the option2Text to set
	 */
	public void setOption2Text(String option2Text) {
		this.option2Text = option2Text;
	}

	/**
	 * @return the option3StoryID
	 */
	public Long getOption3StoryID() {
		return option3StoryID;
	}

	/**
	 * @param option3StoryID the option3StoryID to set
	 */
	public void setOption3StoryID(Long option3StoryID) {
		this.option3StoryID = option3StoryID;
	}

	/**
	 * @return the option3Text
	 */
	public String getOption3Text() {
		return option3Text;
	}

	/**
	 * @param option3Text the option3Text to set
	 */
	public void setOption3Text(String option3Text) {
		this.option3Text = option3Text;
	}

	/**
	 * @return the option4StoryID
	 */
	public Long getOption4StoryID() {
		return option4StoryID;
	}

	/**
	 * @param option4StoryID the option4StoryID to set
	 */
	public void setOption4StoryID(Long option4StoryID) {
		this.option4StoryID = option4StoryID;
	}

	/**
	 * @return the option4Text
	 */
	public String getOption4Text() {
		return option4Text;
	}

	/**
	 * @param option4Text the option4Text to set
	 */
	public void setOption4Text(String option4Text) {
		this.option4Text = option4Text;
	}

	public Integer getDefenderDropVictories() {
		return defenderDropVictories;
	}

	public void setDefenderDropVictories(Integer defenderDropVictories) {
		this.defenderDropVictories = defenderDropVictories;
	}

	public Integer getAttackerDropVictories() {
		return attackerDropVictories;
	}

	public void setAttackerDropVictories(Integer attackerDropVictories) {
		this.attackerDropVictories = attackerDropVictories;
	}
}
