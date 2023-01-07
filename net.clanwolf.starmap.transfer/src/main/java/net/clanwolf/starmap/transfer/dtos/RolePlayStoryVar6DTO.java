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
package net.clanwolf.starmap.transfer.dtos;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import net.clanwolf.starmap.transfer.Dto;

@JsonIdentityInfo(
		scope= RolePlayStoryVar6DTO.class,
		generator=ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class RolePlayStoryVar6DTO extends Dto {


	//@Column(name = "ID")
	private Long id;
	//@JoinColumn(name = "StoryID")
	private Long story;
	//@Column(name = "SecretCode")
	private String secretCode;
	//@Column(name = "Attempts")
	private Integer attempts;
	//@JoinColumn(name = "StoryIDSuccess")
	private Long storyIDSuccess;
	//@JoinColumn(name = "StoryIDFailure")
	private Long storyIDFailure;

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

	public String getSecretCode() {
		return secretCode;
	}

	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}

	public Integer getAttempts() {
		return attempts;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	public Long getStoryIDSuccess() {
		return storyIDSuccess;
	}

	public void setStoryIDSuccess(Long storyIDSuccess) {
		this.storyIDSuccess = storyIDSuccess;
	}

	public Long getStoryIDFailure() {
		return storyIDFailure;
	}

	public void setStoryIDFailure(Long storyIDFailure) {
		this.storyIDFailure = storyIDFailure;
	}
}
