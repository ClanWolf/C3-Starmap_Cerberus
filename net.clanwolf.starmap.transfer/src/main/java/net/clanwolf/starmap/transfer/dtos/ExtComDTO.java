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

import net.clanwolf.starmap.transfer.Dto;

public class ExtComDTO extends Dto {
	private Long id;
	private String text;
	private Boolean processedIRC;
	private Boolean processedTS3;

	@SuppressWarnings("unused")
	public Long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	public void setId(Long id) {
		this.id = id;
	}

	@SuppressWarnings("unused")
	public String getText() {
		return text;
	}

	@SuppressWarnings("unused")
	public void setText(String text) {
		this.text = text;
	}

	@SuppressWarnings("unused")
	public Boolean getProcessedIRC() {
		return processedIRC;
	}

	@SuppressWarnings("unused")
	public void setProcessedIRC(Boolean processedIRC) {
		this.processedIRC = processedIRC;
	}

	@SuppressWarnings("unused")
	public Boolean getProcessedTS3() {
		return processedTS3;
	}

	@SuppressWarnings("unused")
	public void setProcessedTS3(Boolean processedTS3) {
		this.processedTS3 = processedTS3;
	}
}
