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
import net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes.ROLEPLAYINPUTDATATYPES;

@JsonIdentityInfo(
		scope= RolePlayStoryVar3DTO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RolePlayStoryVar3DTO extends Dto {

	//@Column(name = "ID")
	private Long id;
	//@JoinColumn(name = "StoryID")
	private Long story;
	//@JoinColumn(name = "NextStoryID")
	private Long nextStoryID;

	//@Column(name = "DataSet1")
	private ROLEPLAYINPUTDATATYPES DataSet1;
	//@Column(name = "DataSet2")
	private ROLEPLAYINPUTDATATYPES DataSet2;
	//@Column(name = "DataSet3")
	private ROLEPLAYINPUTDATATYPES DataSet3;
	//@Column(name = "DataSet4")
	private ROLEPLAYINPUTDATATYPES DataSet4;
	//@Column(name = "DataSet5")
	private ROLEPLAYINPUTDATATYPES DataSet5;

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

	public ROLEPLAYINPUTDATATYPES getDataSet1() {
		return DataSet1;
	}

	public void setDataSet1(ROLEPLAYINPUTDATATYPES dataSet1) {
		DataSet1 = dataSet1;
	}

	public ROLEPLAYINPUTDATATYPES getDataSet2() {
		return DataSet2;
	}

	public void setDataSet2(ROLEPLAYINPUTDATATYPES dataSet2) {
		DataSet2 = dataSet2;
	}

	public ROLEPLAYINPUTDATATYPES getDataSet3() {
		return DataSet3;
	}

	public void setDataSet3(ROLEPLAYINPUTDATATYPES dataSet3) {
		DataSet3 = dataSet3;
	}

	public ROLEPLAYINPUTDATATYPES getDataSet4() {
		return DataSet4;
	}

	public void setDataSet4(ROLEPLAYINPUTDATATYPES dataSet4) {
		DataSet4 = dataSet4;
	}

	public ROLEPLAYINPUTDATATYPES getDataSet5() {
		return DataSet5;
	}

	public void setDataSet5(ROLEPLAYINPUTDATATYPES dataSet5) {
		DataSet5 = dataSet5;
	}
}
