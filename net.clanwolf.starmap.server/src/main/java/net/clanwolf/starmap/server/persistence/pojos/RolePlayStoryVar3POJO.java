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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.server.persistence.Pojo;
import net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes.ROLEPLAYINPUTDATATYPES;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@JsonIdentityInfo(
		scope= RolePlayStoryVar3POJO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
@Entity
@Table(name = "ROLEPLAY_STORY_VAR3", catalog = "C3")
public class RolePlayStoryVar3POJO extends Pojo {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "StoryID")
	private RolePlayStoryPOJO story;

	@JoinColumn(name = "NextStoryID")
	private Long nextStoryID;

	@Column(name = "DataSet1")
	private Enum<ROLEPLAYINPUTDATATYPES> dataSet1;

	@Column(name = "DataSet2")
	private Enum<ROLEPLAYINPUTDATATYPES> dataSet2;

	@Column(name = "DataSet3")
	private Enum<ROLEPLAYINPUTDATATYPES> dataSet3;

	@Column(name = "DataSet4")
	private Enum<ROLEPLAYINPUTDATATYPES> dataSet4;

	@Column(name = "DataSet5")
	private Enum<ROLEPLAYINPUTDATATYPES> dataSet5;

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
	public RolePlayStoryPOJO getStory() {
		return story;
	}

	/**
	 * @param story the story to set
	 */
	public void setStory(RolePlayStoryPOJO story) {
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

	public Enum<ROLEPLAYINPUTDATATYPES> getDataSet1() {
		return dataSet1;
	}

	public void setDataSet1(Enum<ROLEPLAYINPUTDATATYPES> dataSet1) {
		this.dataSet1 = dataSet1;
	}

	public Enum<ROLEPLAYINPUTDATATYPES> getDataSet2() {
		return dataSet2;
	}

	public void setDataSet2(Enum<ROLEPLAYINPUTDATATYPES> dataSet2) {
		this.dataSet2 = dataSet2;
	}

	public Enum<ROLEPLAYINPUTDATATYPES> getDataSet3() {
		return dataSet3;
	}

	public void setDataSet3(Enum<ROLEPLAYINPUTDATATYPES> dataSet3) {
		this.dataSet3 = dataSet3;
	}

	public Enum<ROLEPLAYINPUTDATATYPES> getDataSet4() {
		return dataSet4;
	}

	public void setDataSet4(Enum<ROLEPLAYINPUTDATATYPES> dataSet4) {
		this.dataSet4 = dataSet4;
	}

	public Enum<ROLEPLAYINPUTDATATYPES> getDataSet5() {
		return dataSet5;
	}

	public void setDataSet5(Enum<ROLEPLAYINPUTDATATYPES> dataSet5) {
		this.dataSet5 = dataSet5;
	}
}
