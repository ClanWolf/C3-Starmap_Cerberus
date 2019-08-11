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
package net.clanwolf.starmap.client.util;

import net.clanwolf.starmap.logging.C3Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Get internationalized strings for C3
 *
 * @author Meldric
 * @author kotzbrocken2
 * @version 1.0
 */
public abstract class Internationalization {

	private static ResourceBundle sMessages;
	private static ResourceBundle sEventMessages;
	private static ResourceBundle sPrivilegeMessages;
	private static Locale sCurrentLocale;

	/**
	 * The german locale.
	 */
	public final static Locale GERMAN = Locale.GERMAN;

	/**
	 * The english locale.
	 */
	public final static Locale ENGLISH = Locale.ENGLISH;

	static {
		Locale l = "de".equals(C3Properties.getProperty(C3PROPS.LANGUAGE)) ? GERMAN : ENGLISH;
		try {
			setLocale(l);
		} catch (MissingResourceException mre) {
			C3Logger.exception(null, mre);
			C3Logger.warning("Cannot find resource-bundle for locale: " + l);
			C3Logger.warning("Falling back to english!");
			setLocale(ENGLISH);
		}
	}

	/**
	 * Sets the locale.
	 *
	 * @param locale Locale
	 */
	public static void setLocale(Locale locale) throws MissingResourceException {
		sMessages = ResourceBundle.getBundle("MessagesBundle", locale);
		sEventMessages = ResourceBundle.getBundle("MessagesEventBundle", locale);
		sPrivilegeMessages = ResourceBundle.getBundle("MessagesPrivilegeBundle", locale);

		sCurrentLocale = locale;

		Locale.setDefault(sCurrentLocale);
	}

	/**
	 * Returns the current locale.
	 *
	 * @return Locale
	 */
	public static Locale getLocale() {
		return sCurrentLocale;
	}

	/**
	 * Returns the current language.
	 *
	 * @return String
	 */
	public static String getLanguage() {
		return sCurrentLocale.getLanguage();
	}

	/**
	 * Returns the associated message.
	 *
	 * @param key String
	 * @return String
	 */
	public static String getString(String key) {
		return getStringSaveFromBundle(key, sMessages);
	}

	/**
	 * Get the event message.
	 *
	 * @param key The key.
	 * @return String Message
	 */
	public static String getEventMessage(String key) {
		return getStringSaveFromBundle(key, sEventMessages);
	}

	private static String getStringSaveFromBundle(String key, ResourceBundle bundle) {
		try {
			if (key == null) {
				return null;
			}
			return bundle.getString(key);
		} catch (MissingResourceException mre) {
			C3Logger.warning("Resource <" + key + "> can't be found in bundle: " + bundle);
		}
		return key;
	}
}
