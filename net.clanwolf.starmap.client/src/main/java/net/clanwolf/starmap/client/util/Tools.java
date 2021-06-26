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
package net.clanwolf.starmap.client.util;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.gui.panes.map.PannableCanvas;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.net.FTP;
import net.clanwolf.starmap.client.net.IFileTransfer;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

	public static String getRomanNumber(int n) {
		String seasonMetaPhase = "";
		if (n > 20) {
			throw new RuntimeException("Season number is too high!");
		}
		seasonMetaPhase = RomanNumber.toRoman(n);
		return seasonMetaPhase;
	}

	public static void saveMapScreenshot(int width, int height, PannableCanvas canvas) {
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute();
		WritableImage wi = new WritableImage(width, height);
		try {
			File file1 = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "history" + File.separator + "C3_Season" + Nexus.getCurrentSeason() + "_map.png");
			File file2 = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "history" + File.separator + "C3_Season" + Nexus.getCurrentSeason() + "_Round" + Nexus.getCurrentRound() + "_" + Nexus.getCurrentChar() + "_map_history.png");
			file1.mkdirs();
			file2.mkdirs();
			BufferedImage bi = SwingFXUtils.fromFXImage(canvas.snapshot(null, wi), null);

			final int screenshotWidth = 2500;       // For map dimension of 4000 x 4000
			final int screenshotHeight = 2000;      // For map dimension of 4000 x 4000
			final BufferedImage finaleImage = new BufferedImage(screenshotWidth, screenshotHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = finaleImage.getGraphics();
			g.drawImage(finaleImage, (bi.getWidth() - screenshotWidth) / 2,(bi.getHeight() - screenshotHeight) / 2, screenshotWidth, screenshotHeight,null);
			g.drawImage(bi, -(bi.getWidth() - screenshotWidth) / 2, -200, bi.getWidth(), bi.getHeight(),null);
			g.dispose();

			Image c3Icon = new Image(Tools.class.getResourceAsStream("/icons/C3_Icon2.png"));
			Image hhIcon = new Image(Tools.class.getResourceAsStream("/icons/hammerhead.png"));
			Graphics2D g2d = finaleImage.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.drawImage(SwingFXUtils.fromFXImage(c3Icon, null), 50, 1850, 100,100, null);
			g2d.drawImage(SwingFXUtils.fromFXImage(hhIcon, null), 2350, 1850, 100,100, null);

			String s1 = "C3 / Hammerhead - Map Export";
			String s2 = "Season:";
			String s3 = "Round:";
			String s4 = "Metaphase:";
			String s5 = "User:";

			String sv2 = "" + Nexus.getCurrentSeason();
			String sv3 = "" + Nexus.getCurrentRound();
			String sv4 = "" + Nexus.getCurrentSeasonMetaPhase();
			String sv5 = Nexus.getCurrentChar().getName() + " (" + Nexus.getCurrentUser().getUserName() + ")" ;

			SimpleDateFormat formatter= new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			Date date = new Date(System.currentTimeMillis());
			String sd = formatter.format(date);

			FontMetrics fm = g2d.getFontMetrics();
			g2d.setFont(new Font("Arial", Font.BOLD, 45));
			g2d.setPaint(Color.WHITE);
			g2d.drawString(s1, 50, 100);

			g2d.setFont(new Font("Arial", Font.PLAIN, 26));
			g2d.drawString(s2, 50, 170);
			g2d.drawString(s3, 50, 200);
			g2d.drawString(s4, 50, 230);
			g2d.drawString(s5, 50, 300);
			g2d.drawString(Nexus.getCurrentDate(), 2100,100);
			g2d.drawString(sd, 2100,130);

			g2d.setPaint(Color.GREEN);
			g2d.drawString(sv2, 200, 170);
			g2d.drawString(sv3, 200, 200);
			g2d.drawString(sv4, 200, 230);
			g2d.drawString(sv5, 200, 300);
			g2d.dispose();

			ImageIO.write(finaleImage ,"png", file1);
			ImageIO.write(finaleImage, "png", file2);
		} catch (IOException e) {
			e.printStackTrace();
			C3Logger.error("Could not save map screenshot!");
		}
		ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute();
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
		C3SoundPlayer.play("/sound/fx/button_clicked_01.mp3", false);
	}

	public static void playButtonClickSound() {
		C3SoundPlayer.play("/sound/fx/button_clicked_02.mp3", false);
	}

	public static void playAttentionSound() { C3SoundPlayer.play("/sound/fx/attention.mp3", false); }

	public static void playGUICreationSound() {
		C3SoundPlayer.play("/sound/fx/slide_01.mp3", false);
	}

	public static void playGUIDestructionSound() {
		C3SoundPlayer.play("/sound/fx/slide_02.mp3", false);
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

	public static void cleanDirectory(File dir, long numberOfDays) {
		for (File file: dir.listFiles()) {
			if (file.isDirectory()) {
				cleanDirectory(file, numberOfDays);
			}

			long diff = new Date().getTime() - file.lastModified();

//			System.out.println("Age: " + new Date().getTime());
//			System.out.println("Last modified: " + file.lastModified());
//			System.out.println(numberOfDays + " days in milliseconds: " + numberOfDays * 24 * 60 *60 * 1000);
//			System.out.println("diff: " + diff);

			if (numberOfDays * 24 * 60 * 60 * 1000 < diff) {
				file.delete();
			} else {
				// file will be left
			}
		}
	}
	public static void purgeDirectory(File dir) {
		for (File file: dir.listFiles()) {
			if (file.isDirectory()) {
				purgeDirectory(file);
			}
			file.delete();
		}
	}
	public static void listDirectory(File dir) {
		for (File file: dir.listFiles()) {
			if (file.isDirectory()) {
				listDirectory(file);
			}
			C3Logger.info(file.getAbsolutePath());
		}
	}
}
