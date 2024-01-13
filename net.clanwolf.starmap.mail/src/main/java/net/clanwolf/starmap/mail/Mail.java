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
package net.clanwolf.starmap.mail;

/**
 * Represents a mail to be sent.
 *
 * @author Christian
 *
 */
public class Mail {
	private String from = "";
	private String replyTo = "";
	private String to = "";
	private int priority = 3;
	private String sjct = "";
	private String txt = "";
	private String html = "";

	public String sender() {
		return from;
	}

	public String replyTo() {
		return replyTo;
	}

	public String recipients() {
		return to;
	}

	public int priority() {
		return priority;
	}

	public String subject() {
		return sjct;
	}

	public String text() {
		return txt;
	}

	public String html() {
		return html;
	}

	@SuppressWarnings("unused")
	public String setSender(String sender) {
		return from = sender;
	}

	@SuppressWarnings("unused")
	public String setReplyTo(String replyTo) {
		return this.replyTo = replyTo;
	}

	@SuppressWarnings("unused")
	public String addRecipient(String recipient) {
		if (!to.equals(""))
			to += ";";
		to += recipient;
		return to;
	}

	@SuppressWarnings("unused")
	public String setRecipient(String recipient) {
		return to = recipient;
	}

	@SuppressWarnings("unused")
	public int setPriority(int priority) {
		return this.priority = priority;
	}

	@SuppressWarnings("unused")
	public String setSubject(String subject) {
		return sjct = subject;
	}

	@SuppressWarnings("unused")
	public String setText(String text) {
		return txt = text;
	}

	@SuppressWarnings("unused")
	public String setHTML(String html) {
		return this.html = html;
	}
}
