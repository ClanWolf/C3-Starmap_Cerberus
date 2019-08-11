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
package net.clanwolf.starmap.transfer.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.transfer.Dto;

@JsonIdentityInfo(
		scope= RolePlayStoryVar3DTO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RolePlayStoryVar3DTO extends Dto {

	//@Column(name = "ID")
	private Long id;
	//@JoinColumn(name = "StoryID")
	//private RolePlayStoryDTO story;
	private Long story;
	//@Column(name = "FormName")
	private String formName;
	//@JoinColumn(name = "NextStoryID")
	private Long nextStoryID;
	//@Column(name = "LabelText")
	private String labelText;
	//@JoinColumn(name = "NextStory2ID")
	private Long nextStory2ID;
	//@Column(name = "LabelText2")
	private String labelText2;
	//@JoinColumn(name = "NextStory3ID")
	private Long nextStory3ID;
	//@Column(name = "LabelText3")
	private String labelText3;
	//@JoinColumn(name = "NextStory4ID")
	private Long nextStory4ID;
	//@Column(name = "LabelText4")
	private String labelText4;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
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
	 * @return the nextStoryID
	 */
	public Long getNextStoryID() {
		return nextStoryID;
	}

	/**
	 * @param nextStoryID the nextStoryID to set
	 */
	public void setNextStoryID(Long nextStoryID) {
		this.nextStoryID = nextStoryID;
	}

	/**
	 * @return the labelText
	 */
	public String getLabelText() {
		return labelText;
	}

	/**
	 * @param labelText the labelText1 to set
	 */
	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	public Long getNextStory2ID() {
		return nextStory2ID;
	}

	public void setNextStory2ID(Long nextStory2ID) {
		this.nextStory2ID = nextStory2ID;
	}

	public String getLabelText2() {
		return labelText2;
	}

	public void setLabelText2(String labelText2) {
		this.labelText2 = labelText2;
	}

	public Long getNextStory3ID() {
		return nextStory3ID;
	}

	public void setNextStory3ID(Long nextStory3ID) {
		this.nextStory3ID = nextStory3ID;
	}

	public String getLabelText3() {
		return labelText3;
	}

	public void setLabelText3(String labelText3) {
		this.labelText3 = labelText3;
	}

	public Long getNextStory4ID() {
		return nextStory4ID;
	}

	public void setNextStory4ID(Long nextStory4ID) {
		this.nextStory4ID = nextStory4ID;
	}

	public String getLabelText4() {
		return labelText4;
	}

	public void setLabelText4(String labelText4) {
		this.labelText4 = labelText4;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}
}
