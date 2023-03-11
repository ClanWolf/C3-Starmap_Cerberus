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
package net.clanwolf.starmap.bots.util;

import net.clanwolf.starmap.bots.ircclient.IRCBot;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class Internationalization {

	private static ResourceBundle sMessages;
	private static Locale sCurrentLocale;

	private static IRCBot ircBot = null;

	static {
		Locale l = Locale.GERMAN;
		try {
			setLocale(l);
		} catch (MissingResourceException mre) {
			setLocale(Locale.ENGLISH);
		}
	}

	public static void setLocale(Locale locale) throws MissingResourceException {
		sMessages = ResourceBundle.getBundle("MessagesBundle", locale);
		sCurrentLocale = locale;
		Locale.setDefault(sCurrentLocale);
	}

	public static void setBot(IRCBot b) {
		ircBot = b;
	}

	public static Locale getLocale() {
		return sCurrentLocale;
	}

	public static String getLanguage() {
		return sCurrentLocale.getLanguage();
	}

	public static String getString(String key) {
		return getStringSaveFromBundle(key, sMessages);
	}

	public static String getString(String key, String... args) {
		String msg = getStringSaveFromBundle(key, sMessages);
		if (msg != null) {
			MessageFormat formatter = new MessageFormat("");
			formatter.applyPattern(msg);
			return formatter.format(args);
		}
		return null;
	}

	private static String getStringSaveFromBundle(String key, ResourceBundle bundle) {
		try {
			if (key == null) {
				return null;
			}
			return bundle.getString(key);
		} catch (MissingResourceException mre) {
			if (ircBot != null) {
				ircBot.send(Internationalization.getString("resourceNotFound", key, "" + bundle)); // [e001]
			}
		}
		return key;
	}
}
