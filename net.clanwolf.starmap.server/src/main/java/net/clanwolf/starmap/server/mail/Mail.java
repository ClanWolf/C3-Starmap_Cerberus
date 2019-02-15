/* ---------------------------------------------------------------- |
 * W-7 Research Group / C3                                          |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *          W-7 Facility / Research, Software Development           |
 *                    Tranquil (Mobile Division)                    |
 * __        __  _____   ____                               _       |
 * \ \      / / |___  | |  _ \ ___  ___  ___  __ _ _ __ ___| |__    |
 *  \ \ /\ / /____ / /  | |_) / _ \/ __|/ _ \/ _` | '__/ __| '_ \   |
 *   \ V  V /_____/ /   |  _ <  __/\__ \  __/ (_| | | | (__| | | |  |
 *    \_/\_/     /_/    |_| \_\___||___/\___|\__,_|_|  \___|_| |_|  |
 *                                                                  |
 *  W-7 is the production facility of Clan Wolf. The home base is   |
 *  Tranquil, but there are several mobile departments. In the      |
 *  development department there is a small group of developers and |
 *  designers busy to field new software products for battlefield   |
 *  commanders as well as for strategic dimensions of the clans     |
 *  operations. The department is led by a experienced StarColonel  |
 *  who fell out of active duty due to a wound he suffered during   |
 *  the battle on Tukkayid. His name and dossier are classified,    |
 *  get in contact through regular chain of command.                |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MkIII "Damien"                   |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 *  One of the products used to control the production and the      |
 *  transport of frontline troops is C3. C3 stands for              |
 *  "Communication - Command - Control".                            |
 *  Because there is a field based system to control the            |
 *  communication and data transfer of Mechs, this system is often  |
 *  refered to as "Big C3", however, the official name is           |
 *  "W-7 C3 / MkIII 'Damien'".                                      |
 *                                                                  |
 *  Licensing through W-7 Facility Central Office, Tranquil.        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  Info        : http://www.clanwolf.net                           |
 *  Forum       : http://www.clanwolf.net                           |
 *  Web         : http://c3.clanwolf.net                            |
 *  GitHub      : https://github.com/ClanWolf/C3-Java_Client        |
 *                                                                  |
 *  IRC         : starmap.clanwolf.net @ Quakenet                        |
 *                                                                  |
 *  Devs        : - Christian (Meldric)                    [active] |
 *                - Werner (Undertaker)                    [active] |
 *                - Thomas (xfirestorm)                    [active] |
 *                - Domenico (Nonnex)                     [retired] |
 *                - Dirk (kotzbroken2)                    [retired] |
 *                                                                  |
 *                  (see Wolfnet for up-to-date information)        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  C3 includes libraries and source code by various authors,       |
 *  for credits and info, see README.                               |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 * Copyright 2016 ClanWolf.net                                      |
 *                                                                  |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 *                                                                  |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 * C3-Java Client uses JxBrowser http://www.teamdev.com/jxbrowser,  |
 * which is a proprietary software. The use of JxBrowser is         |
 * governed by JxBrowser Product Licence Agreement                  |
 * http://www.teamdev.com/jxbrowser-licence-agreement.              |
 * If you would like to use JxBrowser in your development, please   |
 * contact TeamDev.                                                 |
 *                                                                  |
 * ---------------------------------------------------------------- |
 */

package net.clanwolf.starmap.server.mail;

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
