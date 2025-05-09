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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui;

import javafx.animation.FadeTransition;
import javafx.application.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.*;
import net.clanwolf.starmap.client.enums.C3MESSAGES;
import net.clanwolf.starmap.client.gui.messagepanes.C3Message;
import net.clanwolf.starmap.client.net.HTTP;
import net.clanwolf.starmap.client.net.Server;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.preloader.C3_Preloader;
import net.clanwolf.starmap.client.process.logout.Logout;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.Tools;
import net.clanwolf.starmap.logging.C3LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("restriction")
public class MainFrame extends Application implements EventHandler<WindowEvent>, ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private Image imageNormalCursor = null;
	private Image imageWaitCursor = null;
	private Image imageResizeCursor = null;
	private Image imageMoveCursor = null;
	final Delta dragDelta = new Delta();
	BooleanProperty ready = new SimpleBooleanProperty(false);
	private ImageCursor imageCursorNormal = null;
	private ImageCursor imageCursorWait = null;
	private ImageCursor imageCursorResize = null;
	private ImageCursor imageCursorMove = null;
	private Stage stage = null;
	private Scene scene = null;
	private boolean waitcursor = false;
	private boolean messageactive = false;
	private static boolean isDevelopmentPC = false;
	private static boolean isDevelopmentOffline = true;

	/**
	 * Horizontal position of the main window.
	 */
	private static double posX = 0;

	/**
	 * Vertical position of the main window.
	 */
	private static double posY = 0;

	/**
	 * Horizontal position of the mouse pointer.
	 */
	public static double mouseX = 0;

	/**
	 * Vertical position of the mouse pointer.
	 */
	public static double mouseY = 0;

	public static void prepareManual() {
		try {
			File dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "manual");
			boolean success = dir.mkdirs();
			if (success) {
				logger.info("Created manual folder");
			}

			// Manual
			InputStream source = MainFrame.class.getResourceAsStream("/C3_Manual_de.pdf");
			String destination = dir + File.separator + "C3_Manual_de.pdf";
			if (source != null) {
				Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
			}

			// Changelog
			InputStream source2 = MainFrame.class.getResourceAsStream("/changelog.txt");
			String destination2 = dir + File.separator + "changelog.txt";
			if (source2 != null) {
				Files.copy(source2, Paths.get(destination2), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			logger.info("Exception while copying the manual to local drive.");
			logger.error(null, e);
		}
	}

	// This needs to be done in the main class once at startup to set the file handler for the logger
	public static void prepareLogging(String logFileName) {
		C3LogUtil.loadConfigurationAndSetLogFile(logFileName);
	}

	private void openManual() {
		File file = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "manual" + File.separator + "C3_Manual_de.pdf");
		HostServices hostServices = getHostServices();
		hostServices.showDocument(file.getAbsolutePath());
	}

	private void openAttackThread() {
		if (Nexus.getCurrentAttackOfUser() != null) {
			String url = Nexus.getCurrentAttackOfUser().getAttackDTO().getForumThreadLink();
			HostServices hostServices = getHostServices();
			hostServices.showDocument(url);
		}
	}

	private void openPatreon() {
		HostServices hostServices = getHostServices();
		//hostServices.showDocument("https://www.patreon.com/bePatron?u=59537497&redirect_uri=https%3A%2F%2Fwww.clanwolf.net%2Fviewpage.php%3Fpage_id%3D300&utm_medium=widget");
		hostServices.showDocument("https://liberapay.com/WarWolfen");
	}

	private void openLSupportLibera() {
		HostServices hostServices = getHostServices();
		//hostServices.showDocument("https://www.patreon.com/bePatron?u=59537497&redirect_uri=https%3A%2F%2Fwww.clanwolf.net%2Fviewpage.php%3Fpage_id%3D300&utm_medium=widget");
		hostServices.showDocument("https://liberapay.com/WarWolfen");
	}

	public boolean newerVersionAvailable(String v1) {
		// return true if the given version is higher than the currently used one
		boolean givenLocalVersionIsHigherOrEqual = false;
		String v2 = Tools.getVersionNumber();

//		v1 = "6.7.3";
//		v2 = "6.7.3";

		if (v1.equals(v2)) {
			givenLocalVersionIsHigherOrEqual = true;
			return !givenLocalVersionIsHigherOrEqual;
		}

		String[] v1Parts = v1.split(Pattern.quote("."));
		String[] v2Parts = v2.split(Pattern.quote("."));
		for (int i=0; i < 3; i++) {
			int v1i = Integer.parseInt(v1Parts[i]);
			int v2i = Integer.parseInt(v2Parts[i]);
			if (v2i > v1i) {
				givenLocalVersionIsHigherOrEqual = true;
				break;
			}
			if (v2i < v1i) {
				givenLocalVersionIsHigherOrEqual = false;
				break;
			}
		}
		if (givenLocalVersionIsHigherOrEqual) {
			logger.info("Currently used " + v2 + " is higher than " + v1 + " available on serverside");
		} else {
			logger.info("Server version " + v1 + " is higher than the currently used " + v2);
		}
		return !givenLocalVersionIsHigherOrEqual;
	}

	/**
	 * Prepare the property file for each user.
	 * Values from the user properties do overwrite central properties.
	 */
	private static void prepareUserProperties() {
		try {
			File dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3");
			boolean showEditProperties = C3Properties.loadUserProperties(dir.toString());
			if (showEditProperties) {
				// TODO_C3: Show scene to initially edit settings
				logger.info("Showing first welcome.");
			}
		} catch (IOException e) {
			logger.error(null, e);
		}
	}

	public static void main(String[] args) {
		System.setProperty("javafx.preloader", C3_Preloader.class.getCanonicalName());
		System.setProperty("file.encoding", "UTF-8");
		System.setProperty("user.language", "en");

		// This is not written to a logger in order to see it even if the logger is not correctly initialized
		System.out.println(" ");
		System.out.println(" ");
		System.out.println("Starting C3 Client.");
		System.out.println("-----------------------------");
		System.out.println("Use the version with this console for testing purposes.");
		System.out.println("The logfile can be found here (read most recent file):");
		System.out.println("    C:/Users/<username>/.ClanWolf.net_C3/starmap.log.x");
		System.out.println("-----------------------------");
		System.out.println("Module main:         " + System.getProperty("jdk.module.main"));
		System.out.println("Module main class:   " + System.getProperty("jdk.module.main.class"));
		System.out.println("Module path:         " + System.getProperty("jdk.module.path"));
		System.out.println("Class path:          " + System.getProperty("java.class.path"));
		System.out.println("-----------------------------");
		System.out.println("Command line help:");
		System.out.println("- /IDE        (same as IDEOFFLINE) : Running in the development environment (local database).");
		System.out.println("- /IDEOFFLINE (same as IDE)        : Running in the development environment (local database).");
		System.out.println("- /IDEONLINE                       : Running in the development environment (REMOTE PRODUCTION database).");
		System.out.println("- /CLEARCACHE                      : Clear all cached files on startup.");
		System.out.println("-----------------------------");

		isDevelopmentPC = false;
		isDevelopmentOffline = true;
		boolean clearCache = false;

		if(args.length > 0) {
			for (String arg : args) {
				arg = arg.replaceAll("-", "");
				arg = arg.replaceAll("/", "");

				System.out.println("Starting C3 Client: " + arg);
				System.out.println("Detected commandline arg: " + arg);

				if (arg.equalsIgnoreCase("IDE") || arg.equalsIgnoreCase("IDEOFFLINE")) {
					isDevelopmentPC = true;
					isDevelopmentOffline = true;
				} else if (arg.equalsIgnoreCase("IDEONLINE")) {
					isDevelopmentPC = true;
					isDevelopmentOffline = false;
				}
				if (arg.equalsIgnoreCase("CLEARCACHE")) {
					clearCache = true;
				}
//				if (arg.equalsIgnoreCase("HELP")) {
//					System.out.println("Command line help:");
//					System.out.println("- /IDE        : Running in the development environment.");
//					System.out.println("- /CLEARCACHE : Clear all cached files on startup.");
//					System.out.println("- /HELP       : This help list.");
//				}
			}
		}
		Nexus.setIsDevelopmentPC(isDevelopmentPC);
		Nexus.setDevelopmentOffline(isDevelopmentOffline);
		Nexus.setClearCacheOnStart(clearCache);

		launch(MainFrame.class, args);
	}

	@Override
	public void init() throws Exception {
		notifyPreloader(new Preloader.ProgressNotification(0.1));
		Thread.sleep(50);

		notifyPreloader(new Preloader.ProgressNotification(0.2));
		Thread.sleep(50);

		notifyPreloader(new Preloader.ProgressNotification(0.3));
		Thread.sleep(50);

		notifyPreloader(new Preloader.ProgressNotification(0.4));
		Thread.sleep(50);

		// Logging
		File dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3");
		boolean res = dir.mkdirs();
		if (res || dir.exists()) {

			String logFileName = dir + File.separator + "starmap.c3log";
			prepareLogging(logFileName);

			C3Properties.setProperty(C3PROPS.LOGFILE, logFileName);
//			LogWatcher logWatcher = new LogWatcher(logFileName);
//			Nexus.setLogWatcher(logWatcher);

			notifyPreloader(new Preloader.ProgressNotification(0.5));
			Thread.sleep(50);

			logger.info("---------------------------------------------------------------------");
			logger.info("STARTING C3 (" + Tools.getVersionNumber() + ")");
			logger.info("---------------------------------------------------------------------");
			logger.info("Logfile : " + logFileName);

			notifyPreloader(new Preloader.ProgressNotification(0.6));
			Thread.sleep(50);

			notifyPreloader(new Preloader.ProgressNotification(0.7));
			Thread.sleep(50);

			// prepare the properties
			logger.info("Preparing user properties...");
			prepareUserProperties();
			prepareManual();

			notifyPreloader(new Preloader.ProgressNotification(0.8));
			Thread.sleep(50);

			if (Nexus.isClearCacheOnStart()) {
				clearCache();
			} else {
				cleanCache();
			}

			notifyPreloader(new Preloader.ProgressNotification(0.9));
			Thread.sleep(50);

			if (isDevelopmentPC) {
				logger.warn("----------------------------------");
				logger.warn("- THIS IS A DEVELOPMENT MACHINE! -");
				if (isDevelopmentOffline) {
					logger.warn("- MODE: OFFLINE                  -");
				} else {
					logger.warn("- MODE: ONLINE                   -");
				}
				logger.warn("----------------------------------");
				C3Properties.setProperty(C3PROPS.DEV_PC, "true", false);
			}

			notifyPreloader(new Preloader.ProgressNotification(1.0));
			Thread.sleep(50);

			try {
				String availableClientVersion = Server.checkLastAvailableClientVersion();
				logger.info("Latest available client version online: " + availableClientVersion);

				if ("${project.version}".equals(Tools.getVersionNumber())
					|| isDevelopmentPC) {
					logger.info("Currently used client version: *** Running in local debugger! *** (no version check online).");
				} else {
					logger.info("Currently used client version: " + Tools.getVersionNumber());

					// if (availableClientVersion.equals(Tools.getVersionNumber())) {
					if (!newerVersionAvailable(availableClientVersion)) {
						logger.info("Currently used client version is the latest.");
					} else {
						logger.info("Difference detected: Prompt to download new version.");
						Nexus.promptNewVersionInstall = true;
					}
				}
			} catch(Exception e) {
				logger.warn("Could not check latest available client version online!");
			}

			String commandFileName = dir + File.separator + "command.log";
			File commandLogFile = new File(commandFileName);
			Nexus.setCommandLogFile(commandLogFile);

			// fill command history from stored file
			try {
				Scanner input = new Scanner(commandLogFile);
				Nexus.commandHistoryIndex = 0;
				while (input.hasNextLine()) {
					Nexus.commandHistory.add(input.nextLine());
					Nexus.commandHistoryIndex++;
				}
			} catch (FileNotFoundException e) {
				// file has never been written yet
				//e.printStackTrace();
			}
		} else {
			logger.info("Could not create: " + dir.getAbsolutePath());
		}
	}

	/**
	 * The start method for the application.
	 *
	 * @param s the Stage
	 */
	@Override
	public void start(final Stage s) throws Exception {
		// check transparency
		if (Platform.isSupported(ConditionalFeature.TRANSPARENT_WINDOW)) {
			logger.info("Checking transparency: Full Window Transparency supported.");
		} else {
			logger.info("Checking transparency: Full Window Transparency NOT (!) supported.");
		}

		// Set authenticaton for the proxy
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				String user = C3Properties.getProperty(C3PROPS.PROXY_USER);
				String pwd = C3Properties.getProperty(C3PROPS.PROXY_PASSWORD);
				String domain = C3Properties.getProperty(C3PROPS.PROXY_DOMAIN);
				if (domain != null) {
					user += "@" + domain;
				}
				return new PasswordAuthentication(user, pwd.toCharArray());
			}
		});

		stage = s;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource("/fxml/MainFrame.fxml"));
		Parent root = loader.load();

		scene = new Scene(root);
		scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/styles/MainFrameStyle.css")).toExternalForm());
		// This event filter gets the mouse position even if mouse transparent is true
		scene.addEventFilter(MouseEvent.MOUSE_MOVED, (MouseEvent mouseEvent) -> {
			mouseX = mouseEvent.getSceneX();
			mouseY = mouseEvent.getSceneY();
			if (!messageactive) {
				if (waitcursor) {
					scene.setCursor(imageCursorWait);
				} else {
					scene.setCursor(imageCursorNormal);
				}
			}
			ActionManager.getAction(ACTIONS.MOUSE_MOVED).execute();
		});
		scene.setOnMousePressed((MouseEvent mouseEvent) -> {
			dragDelta.x = stage.getX() - mouseEvent.getScreenX();
			dragDelta.y = stage.getY() - mouseEvent.getScreenY();
			posX = stage.getX();
			posY = stage.getY();
		});
		scene.setOnMouseDragged((MouseEvent mouseEvent) -> {
			//if (mouseEvent.getTarget().toString().equals("AnchorPane[id=AnchorPane, styleClass=root]")) {
			if (mouseEvent.getTarget() instanceof Pane pane && pane.getId() != null && pane.getId().startsWith("paneWindowMoverHandle")) {
				stage.setX(mouseEvent.getScreenX() + dragDelta.x);
				stage.setY(mouseEvent.getScreenY() + dragDelta.y);
				posX = stage.getX();
				posY = stage.getY();
			}
		});

		InputStream isImageNormalCursor = this.getClass().getResourceAsStream("/images/C3_Mouse_Cursor.png");
		InputStream isImageWaitCursor = this.getClass().getResourceAsStream("/images/C3_Mouse_Cursor_WAIT.png");
		InputStream isImageResizeCursor = this.getClass().getResourceAsStream("/images/C3_Mouse_Cursor_RESIZE.png");
		InputStream isImageMoveCursor = this.getClass().getResourceAsStream("/images/C3_Mouse_Cursor_MOVE.png");
		assert isImageNormalCursor != null;
		assert isImageWaitCursor != null;
		assert isImageResizeCursor != null;
		assert isImageMoveCursor != null;
		imageNormalCursor = new Image(isImageNormalCursor);
		imageWaitCursor = new Image(isImageWaitCursor);
		imageResizeCursor = new Image(isImageResizeCursor);
		imageMoveCursor = new Image(isImageMoveCursor);
		imageCursorNormal = new ImageCursor(imageNormalCursor, 1, 1);
		imageCursorWait = new ImageCursor(imageWaitCursor, 1, 1);
		imageCursorResize = new ImageCursor(imageResizeCursor, 1, 1);
		imageCursorMove = new ImageCursor(imageMoveCursor, 1, 1);

		scene.getRoot().setCursor(imageCursorNormal);
		scene.setFill(Color.TRANSPARENT);
		scene.getRoot().setOpacity(0.0);

		FadeTransition FadeInTransition = new FadeTransition(Duration.millis(800), scene.getRoot());
		FadeInTransition.setFromValue(0.0);
		FadeInTransition.setToValue(1.0);
		FadeInTransition.setCycleCount(1);
		FadeInTransition.setOnFinished(event -> ActionManager.getAction(ACTIONS.APPLICATION_STARTUP).execute());

		InputStream is = this.getClass().getResourceAsStream("/icons/C3_Icon2.png");
		assert is != null;
		stage.getIcons().add(new Image(is));
		stage.setTitle(Internationalization.getString("app_headline"));
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setResizable(true);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.setOnCloseRequest(this);

		stage.setOnShown(event -> FadeInTransition.play());

		posX = stage.getX();
		posY = stage.getY();

		ActionManager.addActionCallbackListener(ACTIONS.APPLICATION_EXIT, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_NORMAL, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_WAIT, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_RESIZE, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_MOVE, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_NORMAL_MESSAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_WAIT_MESSAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.SHOW_MESSAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.OPEN_MANUAL, this);
		ActionManager.addActionCallbackListener(ACTIONS.OPEN_ATTACKTHREAT, this);
		ActionManager.addActionCallbackListener(ACTIONS.OPEN_PATREON, this);
		ActionManager.addActionCallbackListener(ACTIONS.OPEN_SUPPORT_LIBERA, this);
		ActionManager.addActionCallbackListener(ACTIONS.OPEN_CLIENTVERSION_DOWNLOADPAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.CLIENT_INSTALLER_DOWNLOAD_COMPLETE, this);
		ActionManager.addActionCallbackListener(ACTIONS.CLIENT_INSTALLER_DOWNLOAD_ERROR, this);

		ResizeHelper.addResizeListener(stage);
		stage.show();

		// Get all voice sample files from TTS (only do this at build time before release!)
		//Internationalization.getAllVoiceSamples();

		logger.info("Client mail dispatch disabled in source code!");
//		String[] receivers = {"warwolfen@gmail.com", "werner.kewenig@arcor.de"};
//		boolean sent = false;
//		sent = MailManager.sendMail("starmap@clanwolf.net", receivers, "C3 Client (" + Tools.getVersionNumber() + ")", "C3 Client (" + Tools.getVersionNumber() + ") was successfully started.", false, false);
//		if (sent) {
//			// sent
//			logger.info("Mail sent. [2]");
//		} else {
//			// error during email sending
//			logger.info("Error during mail dispatch. [2]");
//		}
	}

	private void precacheVideos(String cacheFolderName) throws Exception {
		logger.info("Triggering pre-caching of resources:");

		HashMap<String, ArrayList<Integer>> resourcesToCache = new HashMap<String, ArrayList<Integer>>();
		resourcesToCache.put("Invasion_Intro.mp4", new ArrayList<>( Arrays.asList(21, 50, 75, 100)));
		//server_url=https://www.clanwolf.net/apps/C3
		String serverUrl = C3Properties.getProperty(C3PROPS.SERVER_URL);

		for (String res : resourcesToCache.keySet()) {
			for (Integer folder : resourcesToCache.get(res)) {
				String fn = cacheFolderName + File.separator + "video" + File.separator+ "rpg" + File.separator + "resources" + File.separator + folder + File.separator + res;
				File f = new File(fn);
				if (!f.isFile()) {
					logger.info(fn + " NOT found in cache.");
					HTTP.download(serverUrl +"/rpg/resources/" + folder + "/" + res, fn, false);
				} else {
					logger.info(fn + " found in cache.");
				}
			}
		}
	}
	public void cleanCache() throws Exception {
		long numberOfDays = 90;
		logger.info("Cleaning cache (cleaning files older than " + numberOfDays + " days)!");
		String cacheFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "cache";
		File f = new File(cacheFolderName);
		boolean success = f.mkdirs();
		if (!success) {
			logger.info("Folder " + f.getAbsolutePath() + " could not be created!");
		}
		Tools.cleanDirectory(f, numberOfDays);
		logger.info("The following files were left over (could not be deleted or were younger than " + numberOfDays + " days):");
		logger.info("--- [start]");
		Tools.listDirectory(f);
		logger.info("--- [end]");

		precacheVideos(cacheFolderName);
	}

	public void clearCache() throws Exception {
		logger.info("Clearing cache!");
		String cacheFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "cache";
		File f = new File(cacheFolderName);
		boolean success = f.mkdirs();
		if (!success) {
			logger.info("Folder " + f.getAbsolutePath() + " could not be created!");
		}
		Tools.purgeDirectory(f);
		logger.info("The following files were left over (could not be deleted):");
		logger.info("--- [start]");
		Tools.listDirectory(f);
		logger.info("--- [end]");

		// Clear downloaded updates folder
		String updateFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "update";
		File u = new File(updateFolderName);
		boolean success2 = u.mkdirs();
		if (!success2) {
			logger.info("Folder " + f.getAbsolutePath() + " could not be created!");
		}
		Tools.purgeDirectory(u);
		logger.info("The following update installers were left over (could not be deleted):");
		logger.info("--- [start]");
		Tools.listDirectory(u);
		logger.info("--- [end]");

		precacheVideos(cacheFolderName);
	}

	// Records relative x and y coordinates.
	static class Delta {
		double x, y;
	}

	/**
	 * @param event event
	 */
	@Override
	public void handle(final WindowEvent event) {
		event.consume(); // Consuming the event by default.
		ActionManager.getAction(ACTIONS.APPLICATION_EXIT_REQUEST).execute();
	}

	@Override
	public void stop() {
		ActionManager.getAction(ACTIONS.APPLICATION_EXIT_REQUEST).execute();
	}

	/**
	 * Handle Actions
	 *
	 * @param action action
	 * @param o o
	 * @return boolean (ignore)
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case APPLICATION_EXIT:

				if (Nexus.isLoggedIn()) {
					logger.info("Application exit: logging off server");
					Logout.doLogout(false);
				}

				C3SoundPlayer.killMediaPlayer();
				Platform.runLater(() -> {
					C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_goodbye_message"));

					FadeTransition FadeOutTransition = new FadeTransition(Duration.millis(1500), scene.getRoot());
					FadeOutTransition.setFromValue(1.0);
					FadeOutTransition.setToValue(0.0);
					FadeOutTransition.setCycleCount(1);
					FadeOutTransition.setOnFinished((ActionEvent event) -> {
						stage.close(); // Closing the mainframe.
						System.exit(0);
					});
					FadeOutTransition.play();
				});
				break;

			case CURSOR_REQUEST_WAIT:
				Platform.runLater(() -> scene.getRoot().setCursor(imageCursorWait));
				waitcursor = true;
				break;
			case CURSOR_REQUEST_NORMAL:
				Platform.runLater(() -> scene.getRoot().setCursor(imageCursorNormal));
				waitcursor = false;
				break;
			case CURSOR_REQUEST_RESIZE:
				Platform.runLater(() -> scene.getRoot().setCursor(imageCursorResize));
				break;
			case CURSOR_REQUEST_MOVE:
				Platform.runLater(() -> scene.getRoot().setCursor(imageCursorMove));
				break;
			case CURSOR_REQUEST_WAIT_MESSAGE:
				Platform.runLater(() -> scene.getRoot().setCursor(imageCursorWait));
				break;
			case CURSOR_REQUEST_NORMAL_MESSAGE:
				Platform.runLater(() -> scene.getRoot().setCursor(imageCursorNormal));
				break;
			case SHOW_MESSAGE:
				messageactive = true;
				break;
			case SHOW_MESSAGE_WAS_ANSWERED:
				messageactive = false;
				break;
			case OPEN_MANUAL:
				openManual();
				break;
			case OPEN_ATTACKTHREAT:
				openAttackThread();
				break;
			case OPEN_PATREON:
				openPatreon();
				break;
			case OPEN_SUPPORT_LIBERA:
				openLSupportLibera();
				break;
			case OPEN_CLIENTVERSION_DOWNLOADPAGE:
				Platform.runLater(() -> {
					try {
						String directDownloadUrl = C3Properties.getProperty(C3PROPS.AUTOMATIC_DOWNLOAD_CLIENT_URL);
						directDownloadUrl = directDownloadUrl.replace("##v##", Nexus.getLastAvailableClientVersion());
						logger.info("Downloading: " + directDownloadUrl);
						Tools.downloadFile(directDownloadUrl);
					} catch (Exception e) {
						logger.info("Exception during download of client update. Redirecting to manual download...");
						e.printStackTrace();
						String url = C3Properties.getProperty(C3PROPS.MANUAL_DOWNLOAD_CLIENT_URL);
						HostServices hostServices = getHostServices();
						hostServices.showDocument(url);
					}
				});
				break;

			case CLIENT_INSTALLER_DOWNLOAD_ERROR:
				ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute();
				ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(new StatusTextEntryActionObject(Internationalization.getString("general_download_client_error"), false));
				C3SoundPlayer.getTTSFile(Internationalization.getString("C3_Speech_Failure"));
				ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(new C3Message(C3MESSAGES.ERROR_CLIENT_DOWNLOAD_FAILED));
				break;

			case CLIENT_INSTALLER_DOWNLOAD_COMPLETE:
				ActionManager.getAction(ACTIONS.CURSOR_REQUEST_NORMAL).execute();

				try {
					File dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "update");

					String installerName = "";
					if (dir.exists() && dir.isDirectory()) {
						File[] files = Objects.requireNonNull(dir.listFiles());
						Arrays.sort(files, (File a, File b) -> -Long.compare(a.lastModified(), b.lastModified()));

						for (File f : files) {
							if (f.getName().startsWith("C3-Client-") && f.getName().endsWith("_install.exe")) {
								installerName = f.getName();
								break;
							}
						}
						if (!"".equals(installerName)) {
							logger.info("Trying to install new version. If this fails, try manually to run:");
							logger.info("cmd.exe "
									+ "/c "
									//+ "start "
									+ dir + File.separator + installerName
									+ " > "
									+ System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "update" + File.separator + installerName + ".log"
									//+ " 2>&1");
									+ " > nul 2>&1");
							ProcessBuilder processBuilder = new ProcessBuilder(
									"cmd.exe ",
									"/c ",
									//"start ",
									dir + File.separator + installerName,
									" > ",
									System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "update" + File.separator + installerName + ".log",
									//" 2>&1");
									" > nul 2>&1");
							Process process = processBuilder.start();

//							logger.info("--- Output start (may be empty, if exe is not printing anything) ---");
//							BufferedReader reader =	new BufferedReader(new InputStreamReader(process.getInputStream()));
//							StringBuilder builder = new StringBuilder();
//							String line;
//							while ( (line = reader.readLine()) != null) {
//								builder.append(line);
//								builder.append(System.getProperty("line.separator"));
//							}
//							String result = builder.toString();
//							logger.info(result);
//							logger.info("--- Output end ---");

							logger.info("Closing C3 client for installation...");
							System.exit(0);
						} else {
							logger.info("No installer found.");
						}
					}
				} catch (IOException e) {
					logger.error("IOException", e);
					e.printStackTrace();
				}
				break;

			default:
				break;
		}
		return true;
	}
}
