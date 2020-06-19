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
 * Copyright (c) 2001-2020, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.transfer.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.transfer.Dto;

@JsonIdentityInfo(
		scope= RolePlayStoryVar4DTO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RolePlayStoryVar4DTO extends Dto {

	//@Column(name = "ID")
	private Long id;
	//@JoinColumn(name = "StoryID")
	private RolePlayStoryDTO story;
	//@Column(name = "Score")
	private Integer score;
	//@JoinColumn(name = "StoryIDScoreLess")
	private Long storyIDScoreLess;
	//@JoinColumn(name = "StoryIDScoreEqual")
	private Long storyIDScoreEqual;
	//@JoinColumn(name = "StoryIDScoreMore")
	private Long storyIDScoreMore;

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
	public RolePlayStoryDTO getStory() {
		return story;
	}

	/**
	 * @param story the story to set
	 */
	public void setStory(RolePlayStoryDTO story) {
		this.story = story;
	}

	/**
	 * @return the storyIDScoreLess
	 */
	public Long getStoryIDScoreLess() {
		return storyIDScoreLess;
	}

	/**
	 * @param storyIDScoreLess the storyIDScoreLess to set
	 */
	public void setStoryIDScoreLess(Long storyIDScoreLess) {
		this.storyIDScoreLess = storyIDScoreLess;
	}

	/**
	 * @return the storyIDScoreEqual
	 */
	public Long getStoryIDScoreEqual() {
		return storyIDScoreEqual;
	}

	/**
	 * @param storyIDScoreEqual the storyIDScoreEqual to set
	 */
	public void setStoryIDScoreEqual(Long storyIDScoreEqual) {
		this.storyIDScoreEqual = storyIDScoreEqual;
	}

	/**
	 * @return the storyIDScoreMore
	 */
	public Long getStoryIDScoreMore() {
		return storyIDScoreMore;
	}

	/**
	 * @param storyIDScoreMore the storyIDScoreMore to set
	 */
	public void setStoryIDScoreMore(Long storyIDScoreMore) {
		this.storyIDScoreMore = storyIDScoreMore;
	}

	/**
	 * @return the score
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(Integer score) {
		this.score = score;
	}


}
