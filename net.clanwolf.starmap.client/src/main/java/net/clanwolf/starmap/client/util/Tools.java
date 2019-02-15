/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : http://www.clanwolf.net                            |
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

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.net.FTP;
import net.clanwolf.starmap.client.net.IFileTransfer;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Different methods for miscellanious tasks.
 *
 * @author Meldric
 * @author kotzbrocken2
 * @version 1.0
 */
public final class Tools {

	static {
		try {
			System.getProperties().load(Thread.currentThread().getContextClassLoader().getResourceAsStream("version.number"));
		} catch (IOException e) {
			C3Logger.info("Cannot load version-informations: " + e);
		} // try
	}

	/**
	 * Empty contructor
	 */
	private Tools() {
		// do nothing
	}

	public static String getJavaVersion() {
		return (System.getProperty("java.version"));
	}

	public static String getVersionNumber() {
		String version = System.getProperty("version", "unknown");
		return version;
	}

	/**
	 * Returns the client sysdate
	 *
	 * @return sysdate as string
	 */
	public static String getClientSysdate() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getTimeZone("ECT"));
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		return df.format(cal.getTime());
	}

	/**
	 * Creates a gregorian calendar
	 *
	 * @param date dd.mm.yyyy
	 * @return GregorianCalender
	 */
	private static GregorianCalendar getCalendar(String date) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(df.parse(date));
			return calendar;
		} catch (ParseException e) {
			C3Logger.exception(null, e);
		}
		// we shouldn't get here
		return new GregorianCalendar();
	}

	/**
	 * Adds days to a date
	 *
	 * @param date Date
	 * @param days Days
	 * @return String
	 */
	public static String addDayToDate(String date, int days) {
		GregorianCalendar c = Tools.getCalendar(date);
		c.add(Calendar.DATE, days);
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(c.getTime());
	}

	public static void startBrowser(String url) {
		try {
			startBrowser(new URL(url));
		} catch (MalformedURLException e) {
			C3Logger.exception(null, e);
		}
	}

	public static void startBrowser(URL url) {
		try {
			startBrowser(url.toURI());
		} catch (URISyntaxException e) {
			C3Logger.exception(null, e);
		}
	}

	public static void startBrowser(URI uri) {
		try {
			Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			C3Logger.exception(null, e);
		}
	}

	public static void playButtonHoverSound() {
		C3SoundPlayer.play("/sound/fx/button_hover_02.mp3", false);
	}

	public static void playButtonHoverSound3() {
		C3SoundPlayer.play("/sound/fx/button_clicked_01.wav", false);
	}

	public static void playButtonClickSound() {
		C3SoundPlayer.play("/sound/fx/button_clicked_02.wav", false);
	}

	public static void playAttentionSound() { C3SoundPlayer.play("/sound/fx/attention.wav", false); }

	public static void playGUICreationSound() {
		C3SoundPlayer.play("/sound/fx/slide_01.wav", false);
	}

	public static void playGUIDestructionSound() {
		C3SoundPlayer.play("/sound/fx/slide_02.wav", false);
	}

	public static Alert C3Dialog(AlertType alertType, String title, String header, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getScene().getStylesheets().clear();
		dialogPane.getScene().getStylesheets().add("/styles/C3Alerts.css");

		return alert;
	}

	public static IFileTransfer getFileTransfer() {
		return new FTP();
	}
}
