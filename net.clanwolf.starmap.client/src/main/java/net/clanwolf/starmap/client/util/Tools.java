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
package net.clanwolf.starmap.client.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Transform;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.StatusTextEntryActionObject;
import net.clanwolf.starmap.client.enums.C3FTPTYPES;
import net.clanwolf.starmap.client.gui.panes.map.starmap.StarmapConfig;
import net.clanwolf.starmap.client.gui.panes.map.starmap.StarmapPannableCanvas;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.universe.BOFaction;
import net.clanwolf.starmap.client.process.universe.BOStarSystem;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.client.net.FTP;
import net.clanwolf.starmap.client.net.IFileTransfer;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Different methods for miscellanious tasks.
 *
 * @author Meldric
 * @author kotzbrocken2
 * @version 1.0
 */
public final class Tools {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	static {
		try {
			System.getProperties().load(Thread.currentThread().getContextClassLoader().getResourceAsStream("version.number"));
		} catch (IOException e) {
			logger.info("Cannot load version-informations: " + e);
		} // try
	}

	/**
	 * Empty contructor
	 */
	private Tools() {
		// do nothing
	}

	public static String encodeValue(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replace("+", "%20");
	}

	public static String addUserName(String subject) {
		String userName = "";
		if (Nexus.getCurrentUser() != null) {
			userName = Nexus.getCurrentUser().getUserName();
		}
		if (!"".equals(userName)) {
			return subject + " (" + userName + ")";
		} else {
			return subject;
		}
	}

	@SuppressWarnings("unused")
	public static String readLocalFileToString(String file) {
		String r = "...";
		File f = new File(file);
		if (f.isFile() && f.canRead()) {
			Path filePath = Path.of(file);
			try {
				r = Files.readString(filePath);
			} catch(IOException e) {
				r = "ERROR READING LOG FILE";
			}
		}
		return r;
	}

	@SuppressWarnings("unused")
	public static void sendMailToAdminGroup(String message, ArrayList<File> logfiles) {
		sendMailToAdminGroup("C3 Message", message, logfiles);
	}

	@SuppressWarnings("unused")
	public static void sendMailToAdminGroup(String message) {
		sendMailToAdminGroup("C3 Message", message, null);
	}

	@SuppressWarnings("unused")
	public static void sendMailToAdminGroup(String subject, String message, ArrayList<File> logfiles) {
		try {
			Desktop desktop;
			if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
				URI mailto;
				String content = "mailto:c3@clanwolf.net?subject=" + encodeValue(addUserName(subject)) + "&body=" + encodeValue(message);

				File latestFile = null;
				if (logfiles != null && !logfiles.isEmpty()) {
					for (File f : logfiles) {
						if (f.isFile()) {
							if (latestFile == null) {
								latestFile = f;
							}
							if (f.lastModified() > latestFile.lastModified()) {
								latestFile = f;
							}
						}
					}
				}
				if (latestFile != null) {
					Path path = latestFile.toPath();
					String log = Files.readString(path);
					content = content + encodeValue(log);
				}

				mailto = new URI(content);
				desktop.mail(mailto);
			} else {
				logger.warn("Desktop does not support mailto!");
				throw new RuntimeException("Desktop does not support mailto!");
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			logger.error("Error while reporting suspicious situation to admins.");
		}
	}

	@SuppressWarnings("unused")
	public static String getJavaVersion() {
		return (System.getProperty("java.version"));
	}

	public static String getVersionNumber() {
		return System.getProperty("version", "unknown");
	}

	/**
	 * Returns the client sysdate
	 *
	 * @return sysdate as string
	 */
	@SuppressWarnings("unused")
	public static String getClientSysdate() {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getTimeZone("ECT"));
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		return df.format(cal.getTime());
	}

	public static String getRomanNumber(int n) {
		if (n > 20) {
			throw new RuntimeException("Season number is too high!");
		}
		return RomanNumber.toRoman(n);
	}

	public static void getAttackScreenShot(StarmapPannableCanvas canvas, BOStarSystem attackedSystem, Long attackId) {
		if (!Nexus.isDevelopmentPC()) {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("666");

			String currentSeason = "" + Nexus.getCurrentSeason();
			int currentRound = Nexus.getCurrentRound();
			int nextRound = currentRound + 1;
			double w = StarmapConfig.MAP_WIDTH;
			double h = StarmapConfig.MAP_HEIGHT;
			int attackSubImageWidth = 900;
			int attackSubImageHeight = 400;

			WritableImage wi = new WritableImage((int) w, (int) h);

			StatusTextEntryActionObject o1 = new StatusTextEntryActionObject("Creating attack screenshot...", false);
			ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(o1);

			try {
				File file1 = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "attacks" + File.separator + "C3_S" + Nexus.getCurrentSeason() + "_R" + nextRound + "_" + attackId + ".png");
				if (!file1.mkdirs()) {
					// logger.error("Could not create attack image folder or it already existed!");
				}

				BOStarSystem ss = Nexus.getBoUniverse().starSystemBOs.get(attackedSystem.getStarSystemId());
				double startPositionX = (w / 2 + ss.getX() * StarmapConfig.MAP_COORDINATES_MULTIPLICATOR);
				double startPositionY = (h / 2 - ss.getY() * StarmapConfig.MAP_COORDINATES_MULTIPLICATOR);

				boolean starsShowing = canvas.is3DStarsShowing();
				if (starsShowing) {
					canvas.show3DStars(false);
				}

				SnapshotParameters snapshotParameters = new SnapshotParameters();
				snapshotParameters.setFill(javafx.scene.paint.Color.TRANSPARENT);
				BufferedImage tmp = SwingFXUtils.fromFXImage(canvas.snapshot(snapshotParameters, null), null);

				double snapshotWidth = tmp.getWidth();
				double snapshotHeight = tmp.getHeight();
				double factorX = snapshotWidth / w;
				double factorY = snapshotHeight / h;

				int finalAttackSubImageWidth = (int) (attackSubImageWidth * factorX);
				int finalAttackSubImageHeight = (int) (attackSubImageHeight * factorY);
				int finalStartPositionX = (int) ((startPositionX * factorX) - (finalAttackSubImageWidth / 2));
				int finalStartPositionY = (int) ((startPositionY * factorY) - (finalAttackSubImageHeight / 2) + 70 * factorY);

				BufferedImage attackSubImage = Thumbnails.of(tmp).sourceRegion(finalStartPositionX, finalStartPositionY, finalAttackSubImageWidth, finalAttackSubImageHeight).size(attackSubImageWidth, attackSubImageHeight).asBufferedImage();

				final BufferedImage finaleImage = new BufferedImage(attackSubImageWidth, attackSubImageHeight, BufferedImage.TYPE_INT_RGB);
				Graphics g = finaleImage.getGraphics();
				g.drawImage(attackSubImage, 0, 0, attackSubImageWidth, attackSubImageHeight, null);
				g.dispose();

				if (starsShowing) {
					canvas.show3DStars(true);
				}

				ImageIO.write(finaleImage, "png", file1);

				Runnable runnable = () -> {
					StatusTextEntryActionObject o2 = new StatusTextEntryActionObject("Uploading attack images...", false);
					ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(o2);

					try {
						FTP ftpClient = new FTP(C3FTPTYPES.FTP_HISTORYUPLOAD);
						ftpClient.upload(file1.getAbsolutePath(), file1.getName(), "S" + currentSeason + "/" + "AttackImages");
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("Exception during ftp upload.", e);
					}
				};
				Thread t = new Thread(runnable);
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Could not save attack screenshot!");
			}

			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("666");
		} else {
			logger.error("Skipping attack screenshot because this is a dev machine.");
		}
	}

	public static void saveMapScreenshot(int width, int height, StarmapPannableCanvas canvas) {
		if(C3Properties.getBoolean(C3PROPS.GENERALS_SCREENSHOT_HISTORY)) {
			if (!Nexus.isDevelopmentPC()) {
				ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute("14");
				WritableImage wi = new WritableImage(width, height);

				StatusTextEntryActionObject o1 = new StatusTextEntryActionObject("Creating history images...", false);
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(o1);

				try {
					BOFaction faction = Nexus.getCurrentFaction();
//					Image imageFaction = new Image(Objects.requireNonNull(Tools.class.getResourceAsStream("/images/logos/factions/" + faction.getLogo())));

					File file1 = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "history" + File.separator + "C3_S" + Nexus.getCurrentSeason() + "_map.png");
					File file2 = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "history" + File.separator + "C3_S" + Nexus.getCurrentSeason() + "_R" + Nexus.getCurrentRound() + "_map_history.png");
					File file3 = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "history" + File.separator + "C3_S" + Nexus.getCurrentSeason() + "_R" + Nexus.getCurrentRound() + "_map_history_preview.png");
					if (!file1.mkdirs()) {
						// logger.error("Could not create history folder or it already existed!");
					}
					BufferedImage bi = SwingFXUtils.fromFXImage(canvas.snapshot(null, wi), null);

					final int screenshotWidth = 2500;       // For map dimension of 4000 x 4000
					final int screenshotHeight = 2000;      // For map dimension of 4000 x 4000
					final BufferedImage finaleImage = new BufferedImage(screenshotWidth, screenshotHeight, BufferedImage.TYPE_INT_RGB);
					Graphics g = finaleImage.getGraphics();
					g.drawImage(finaleImage, (bi.getWidth() - screenshotWidth) / 2, (bi.getHeight() - screenshotHeight) / 2, screenshotWidth, screenshotHeight, null);
					g.drawImage(bi, -(bi.getWidth() - screenshotWidth) / 2, -200, bi.getWidth(), bi.getHeight(), null);
					g.dispose();

					Image c3Icon = new Image(Objects.requireNonNull(Tools.class.getResourceAsStream("/icons/C3_Icon2.png")));
					Image hhIcon = new Image(Objects.requireNonNull(Tools.class.getResourceAsStream("/icons/hammerhead.png")));
					Graphics2D g2d = finaleImage.createGraphics();
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2d.drawImage(SwingFXUtils.fromFXImage(c3Icon, null), 50, 1850, 100, 100, null);
					g2d.drawImage(SwingFXUtils.fromFXImage(hhIcon, null), 2350, 1850, 100, 100, null);
					//g2d.drawImage(SwingFXUtils.fromFXImage(imageFaction, null), 50,350, 150,150, null);

					String s1 = "C3 / Hammerhead - Season history map";
					String s2 = "Season:";
					String s3 = "Round:";
					String s4 = "Metaphase:";
					//String s5 = "User:";

					String sv2 = "" + Nexus.getCurrentSeason();
					String sv3 = "" + Nexus.getCurrentRound();
					String sv4 = "" + Nexus.getCurrentSeasonMetaPhase();
					//String sv5 = Nexus.getCurrentChar().getName() + " (" + Nexus.getCurrentUser().getUserName() + ")";

					SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
					Date date = new Date(System.currentTimeMillis());
					String sd = formatter.format(date);

					g2d.setFont(new Font("Arial", Font.BOLD, 45));
					g2d.setPaint(Color.WHITE);
					g2d.drawString(s1, 50, 100);

					g2d.setFont(new Font("Arial", Font.PLAIN, 26));
					g2d.drawString(s2, 50, 170);
					g2d.drawString(s3, 50, 200);
					g2d.drawString(s4, 50, 230);
					//g2d.drawString(s5, 50, 300);
					g2d.drawString(Nexus.getCurrentDate(), 2100, 100);
					g2d.drawString(sd, 2100, 130);

					g2d.setPaint(Color.GREEN);
					g2d.drawString(sv2, 200, 170);
					g2d.drawString(sv3, 200, 200);
					g2d.drawString(sv4, 200, 230);
					//g2d.drawString(sv5, 200, 300);

					g2d.setFont(new Font("Arial", Font.BOLD, 200));
					g2d.setPaint(Color.CYAN);
					g2d.drawString(Nexus.getCurrentRound() + "", 2100, 400);

					// Add a border
					g2d.setPaint(Color.GRAY);
					g2d.setStroke(new BasicStroke(20));
					g2d.drawLine(0, 0, 2500, 0);
					g2d.drawLine(0, 0, 0, 2000);
					g2d.drawLine(2500, 0, 2500, 2000);
					g2d.drawLine(0, 2000, 2500, 2000);

					g2d.dispose();

					ImageIO.write(finaleImage, "png", file1);
					ImageIO.write(finaleImage, "png", file2);
					// https://github.com/coobird/thumbnailator
					ImageIO.write(Thumbnails.of(finaleImage).scale(0.10).asBufferedImage(), "png", file3);

					// DO NOT use a thread here!
					// Otherwise things like the nebula image end up on the screenshot (should not be there)

					Runnable runnable = () -> {
						StatusTextEntryActionObject o2 = new StatusTextEntryActionObject("Uploading history images...", false);
						ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(o2);

						try {
							FTP ftpClient = new FTP(C3FTPTYPES.FTP_HISTORYUPLOAD);
							ftpClient.upload(file1.getAbsolutePath(), file1.getName(), "S" + sv2);
							ftpClient.upload(file2.getAbsolutePath(), file2.getName(), "S" + sv2);
							ftpClient.upload(file3.getAbsolutePath(), file3.getName(), "S" + sv2);
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("Exception during ftp upload.", e);
						}
					};
					Thread t = new Thread(runnable);
					t.start();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("Could not save map screenshot!");
				}
				ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute("14");
			} else {
				logger.error("Skipping map screenshot because this is a dev machine.");
			}
		} else {
			logger.info(Internationalization.getString("app_CreateMapScreenshot"));
		}
	}

	// Bad image quality, has been replaced by https://github.com/coobird/thumbnailator
	@SuppressWarnings("unused")
	private static BufferedImage scale(BufferedImage before, double scale) {
		int w = before.getWidth();
		int h = before.getHeight();
		int w2 = (int) (w * scale);
		int h2 = (int) (h * scale);
		BufferedImage after = new BufferedImage(w2, h2, before.getType());
		AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_BICUBIC);

		Graphics2D g2 = (Graphics2D) after.getGraphics();
		g2.drawImage(before, scaleOp, 0, 0);
		g2.dispose();
		return after;
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
			logger.error(null, e);
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
	@SuppressWarnings("unused")
	public static String addDayToDate(String date, int days) {
		GregorianCalendar c = Tools.getCalendar(date);
		c.add(Calendar.DATE, days);
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(c.getTime());
	}

	@SuppressWarnings("unused")
	public static void startBrowser(String url) {
		try {
			URI uri = new URI(url);
			startBrowser(uri.toURL());
		} catch (MalformedURLException | URISyntaxException e) {
			logger.error(null, e);
		}
	}

	public static void startBrowser(URL url) {
		try {
			startBrowser(url.toURI());
		} catch (URISyntaxException e) {
			logger.error(null, e);
		}
	}

	public static void startBrowser(URI uri) {
		try {
			Desktop.getDesktop().browse(uri);
		} catch (IOException e) {
			logger.error(null, e);
		}
	}

	public static void playButtonHoverSound() {
		C3SoundPlayer.play("/sound/fx/button_hover_02.mp3", false);
	}

	@SuppressWarnings("unused")
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
//		dialogPane.getScene().getStylesheets().add("/styles/C3Alerts.css");

		return alert;
	}

	public static IFileTransfer getFileTransfer() {
		return new FTP();
	}

	public static javafx.scene.control.Label getLevelLabel(String text, String color) {
		javafx.scene.control.Label label = new javafx.scene.control.Label(text);
		label.setMinSize(8, 10);
		label.setMaxSize(8, 10);
		label.setPrefSize(8, 10);
		label.setAlignment(Pos.CENTER);
		label.setFont(new javafx.scene.text.Font("Arial", 6));
		label.setStyle("-fx-background-color:" + color + ";-fx-border-color:black;-fx-text-fill:black;-fx-padding: -1 0 0 0;");
		label.setWrapText(false);
		return label;
	}

	public static void cleanDirectory(File dir, long numberOfDays) {
		for (File file: Objects.requireNonNull(dir.listFiles())) {
			if (file.isDirectory()) {
				cleanDirectory(file, numberOfDays);
			}

			long diff = new Date().getTime() - file.lastModified();

//			System.out.println("Age: " + new Date().getTime());
//			System.out.println("Last modified: " + file.lastModified());
//			System.out.println(numberOfDays + " days in milliseconds: " + numberOfDays * 24 * 60 *60 * 1000);
//			System.out.println("diff: " + diff);

			if (numberOfDays * 24 * 60 * 60 * 1000 < diff) {
				if (!file.getName().endsWith(".mp4")) {
					boolean success = file.delete();
					if (!success) {
						logger.info("File could not be deleted: " + file.getAbsolutePath());
					}
				} else {
					logger.info("Video files are not deleted in cache to save traffic. To empty the cache use purge!");
				}
			} else {
				// file will be left
			}
		}
	}

	public static void purgeDirectory(File dir) {
		for (File file: Objects.requireNonNull(dir.listFiles())) {
			if (file.isDirectory()) {
				purgeDirectory(file);
			}
			boolean success = file.delete();
			if (!success) {
				logger.info("File could not be deleted: " + file.getAbsolutePath());
			}
		}
	}

	public static void listDirectory(File dir) {
		for (File file: Objects.requireNonNull(dir.listFiles())) {
			if (file.isDirectory()) {
				listDirectory(file);
			}
			logger.info(file.getAbsolutePath());
		}
	}

	public static void downloadFile(String link) throws Exception {
		Runnable runnable = () -> {
			ActionManager.getAction(ACTIONS.CURSOR_REQUEST_WAIT).execute(Internationalization.getString("general_download_client_installer"));

			String[] bits = link.split("/");
			String updateName = bits[bits.length - 1];

			File dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "update");
			boolean success = dir.mkdirs();
			if (success) {
				logger.info("Created updates folder");
			}

			// Remove old update files (save disk space, they are not needed)
			File[] dirList = dir.listFiles();
			assert dirList != null;
			for (File f : dirList) {
				logger.info("###### Checking for installer to delete: " + f.getName());
				if (f.getName().startsWith("C3-Client-") && f.getName().endsWith("_install.exe") && !f.getName().equalsIgnoreCase(updateName)) {
					// This is an old update to be deleted from the cache folder
					logger.info("###### Deleting old setup file: " + f.getName());
					boolean d = f.delete();
					if (d) {
					   logger.info("###### Deleted");
					} else {
					   logger.info("###### Could not delete older setup files!");
					}
				}
			}

			File existingUpdate = new File(dir + File.separator + updateName);
			if (existingUpdate.exists() && existingUpdate.isFile() && existingUpdate.canWrite()) {
				boolean deleted = existingUpdate.delete();
				if (!deleted) {
					logger.info("Could not delete old version of this update!");
					return; // ends the thread
				}
			}

			boolean exceptionOccured = false;
			//try (BufferedInputStream in = new BufferedInputStream(new URL(link).openStream()); FileOutputStream fileOutputStream = new FileOutputStream(dir + File.separator + updateName)) {
			try (BufferedInputStream in = new BufferedInputStream((new URI(link)).toURL().openStream()); FileOutputStream fileOutputStream = new FileOutputStream(dir + File.separator + updateName)) {
				byte[] dataBuffer = new byte[1024];
				int counter = 0;
				int bytesRead;
				//int filesize =  new URL(link).openConnection().getContentLength() / 1024;
				int filesize =  (new URI(link)).toURL().openConnection().getContentLength() / 1024;
				String downloadprogress;

				TxtProgressBar tpb = new TxtProgressBar( filesize,"□","■");

				while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
					fileOutputStream.write(dataBuffer, 0, bytesRead);
					counter++;

					downloadprogress = Internationalization.getString("app_downloadInstaller");
					downloadprogress = downloadprogress.replace("{tpb}",tpb.getcurprogress(counter));
					downloadprogress = downloadprogress.replace("{curmb}",String.valueOf(counter / 1024));
					downloadprogress = downloadprogress.replace("{maxmb}",String.valueOf(filesize / 1024));
					downloadprogress = downloadprogress.replace("{curpercent}",tpb.getCurrentpercentvalue());
					StatusTextEntryActionObject o = new StatusTextEntryActionObject(downloadprogress,false, "#555555");
					o.setJustUpdate(true);
					ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(o);
				}
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
				logger.error("Downloading installer.exe failed!", e);
				exceptionOccured = true;
			}
			if (!exceptionOccured) {
				ActionManager.getAction(ACTIONS.CLIENT_INSTALLER_DOWNLOAD_COMPLETE).execute();
			} else {
				// Client installer not found
				logger.error("Downloading installer error");
				ActionManager.getAction(ACTIONS.CLIENT_INSTALLER_DOWNLOAD_ERROR).execute();
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
}
