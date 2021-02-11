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
package net.clanwolf.starmap.client.gui;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.application.Preloader;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.net.Server;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.logout.Logout;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.Tools;
import net.clanwolf.starmap.client.preloader.C3_Preloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.logging.Level;

@SuppressWarnings("restriction")
public class MainFrame extends Application implements EventHandler<WindowEvent>, ActionCallBackListener {

	private Image imageNormalCursor = null;
	private Image imageWaitCursor = null;
	final Delta dragDelta = new Delta();
	BooleanProperty ready = new SimpleBooleanProperty(false);
	private ImageCursor imageCursorNormal = null;
	private ImageCursor imageCursorWait = null;
	private Stage stage = null;
	private Scene scene = null;
	private boolean waitcursor = false;
	private boolean messageactive = false;

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

	public void clearCache() {
		C3Logger.info("Clearing cache!");
		String cacheFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "cache";
		File f = new File(cacheFolderName);
		f.mkdirs();
		Tools.purgeDirectory(f);
		C3Logger.info("The following files were left over (could not be deleted):");
		C3Logger.info("--- [start]");
		Tools.listDirectory(f);
		C3Logger.info("--- [end]");
	}

	public void cleanCache() {
		long numberOfDays = 90;
		C3Logger.info("Cleaning cache (cleaning files older than " + numberOfDays + " days)!");
		String cacheFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "cache";
		File f = new File(cacheFolderName);
		f.mkdirs();
		Tools.cleanDirectory(f, numberOfDays);
		C3Logger.info("The following files were left over (could not be deleted or were younger than " + numberOfDays + " days):");
		C3Logger.info("--- [start]");
		Tools.listDirectory(f);
		C3Logger.info("--- [end]");
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
			C3Logger.info("Checking transparency: Full Window Transparency supported.");
		} else {
			C3Logger.info("Checking transparency: Full Window Transparency NOT (!) supported.");
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
		scene.getStylesheets().add(this.getClass().getResource("/styles/MainFrameStyle.css").toExternalForm());
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
			// Log.print(mouseEvent.getTarget().toString());
			if (mouseEvent.getTarget().toString().equals("AnchorPane[id=AnchorPane, styleClass=root]")) {
				stage.setX(mouseEvent.getScreenX() + dragDelta.x);
				stage.setY(mouseEvent.getScreenY() + dragDelta.y);
				posX = stage.getX();
				posY = stage.getY();
			}
			// TODO: Add check for the resize control and perform resize of the
			// window
			if (mouseEvent.getTarget().toString().equals("Label[id=ResizerControl, styleClassLabel]")) {
				C3Logger.info("ReSIZE");
			}
		});

		InputStream isImageNormalCursor = this.getClass().getResourceAsStream("/images/C3_Mouse_Cursor.png");
		InputStream isImageWaitCursor = this.getClass().getResourceAsStream("/images/C3_Mouse_Cursor_WAIT.png");
		imageNormalCursor = new Image(isImageNormalCursor);
		imageWaitCursor = new Image(isImageWaitCursor);
		imageCursorNormal = new ImageCursor(imageNormalCursor, 1, 1);
		imageCursorWait = new ImageCursor(imageWaitCursor, 1, 1);

		scene.setCursor(imageCursorNormal);
		scene.setFill(Color.TRANSPARENT);
		scene.getRoot().setOpacity(0.0);

		FadeTransition FadeInTransition = new FadeTransition(Duration.millis(800), scene.getRoot());
		FadeInTransition.setFromValue(0.0);
		FadeInTransition.setToValue(1.0);
		FadeInTransition.setCycleCount(1);
		FadeInTransition.setOnFinished(event -> ActionManager.getAction(ACTIONS.APPLICATION_STARTUP).execute());

		InputStream is = this.getClass().getResourceAsStream("/icons/C3_Icon2.png");
		stage.getIcons().add(new Image(is));
		stage.setTitle(Internationalization.getString("app_headline"));
		// TODO: Resize the window (outcomment the initStyle StageStyle)
		stage.initStyle(StageStyle.TRANSPARENT);
		// stage.setResizable(true);
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.setOnCloseRequest(this);

		stage.setOnShown(event -> FadeInTransition.play());

		posX = stage.getX();
		posY = stage.getY();

		ActionManager.addActionCallbackListener(ACTIONS.APPLICATION_EXIT, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_NORMAL, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_WAIT, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_NORMAL_MESSAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.CURSOR_REQUEST_WAIT_MESSAGE, this);
		ActionManager.addActionCallbackListener(ACTIONS.SHOW_MESSAGE, this);

		stage.show();

		C3Logger.info("Mail dispatch disabled in source code.");
//		String[] receivers = {"warwolfen@gmail.com", "werner.kewenig@arcor.de"};
//		boolean sent = false;
//		sent = MailManager.sendMail("starmap@clanwolf.net", receivers, "C3 Client (" + Tools.getVersionNumber() + ")", "C3 Client (" + Tools.getVersionNumber() + ") was successfully started.", false, false);
//		if (sent) {
//			// sent
//			C3Logger.info("Mail sent.");
//		} else {
//			// error during email sending
//			C3Logger.info("Error during mail dispatch.");
//		}
	}

	@Override
	public void init() {
		notifyPreloader(new Preloader.ProgressNotification(10.0));

		// Logging
		File dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3");
		boolean res = dir.mkdirs();
		if (res || dir.exists()) {
			String logFileName = dir + File.separator + "starmap.log";
			C3Properties.setProperty(C3PROPS.LOGFILE, logFileName);

			notifyPreloader(new Preloader.ProgressNotification(50.0));

			C3Logger.setC3Logfile(logFileName);
			C3Logger.setC3LogLevel(Level.FINEST);

			C3Logger.info("---------------------------------------------------------------------");
			C3Logger.info("STARTING C3 (" + Tools.getVersionNumber() + ")");
			C3Logger.info("---------------------------------------------------------------------");
			C3Logger.info("Logfile : " + logFileName);
			C3Logger.info("Loglevel: " + C3Logger.getC3LogLevel());

			notifyPreloader(new Preloader.ProgressNotification(70.0));

			// prepare the properties
			C3Logger.info("Preparing user properties...");
			prepareUserProperties();

			if (Nexus.isClearCacheOnStart()) {
				clearCache();
			} else {
				cleanCache();
			}

			notifyPreloader(new Preloader.ProgressNotification(100.0));

			try {
				String availableClientVersion = Server.checkLastAvailableClientVersion();
				C3Logger.info("Latest available client version online: " + availableClientVersion);

				if ("${project.version}".equals(Tools.getVersionNumber())) {
					C3Logger.info("Currently used client version: *** Running in local debugger! *** (no version check online).");
				} else {
					C3Logger.info("Currently used client version: " + Tools.getVersionNumber());

					if (availableClientVersion.equals(Tools.getVersionNumber())) {
						C3Logger.info("Currently used client version is the latest.");
					} else {
						C3Logger.info("Difference detected: Prompt to download new version.");
						// TODO: Generate prompt to download latest version
					}
				}
			} catch(Exception e) {
				C3Logger.warning("Could not check latest available client version online!");
			}
		} else {
			C3Logger.info("Could not create: " + dir.getAbsolutePath());
		}
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
				// TODO: Show scene to initially edit settings
				C3Logger.info("Showing first welcome.");
			}
		} catch (IOException e) {
			C3Logger.exception(null, e);
		}
	}

	public static void main(String[] args) {
		System.setProperty("javafx.preloader", C3_Preloader.class.getCanonicalName());

		boolean isDevelopmentPC = false;
		boolean clearCache = false;
		if(args.length > 0) {
			for (String arg : args) {
				arg = arg.replaceAll("-", "");
				arg = arg.replaceAll("/", "");

				System.out.println("Starting C3 Client: " + arg);
				System.out.println("Detected commandline arg: " + arg);

				if (arg.toUpperCase().equals("IDE")) {
					isDevelopmentPC = true;
				}
				if (arg.toUpperCase().equals("CLEARCACHE")) {
					clearCache = true;
				}
				if (arg.toUpperCase().equals("HELP")) {
					System.out.println("Command line help:");
					System.out.println("- /IDE        : Running in the development environment.");
					System.out.println("- /CLEARCACHE : Clear all cached files on startup.");
					System.out.println("- /HELP       : This help list.");
				}
			}
		}
		Nexus.setIsDevelopmentPC(isDevelopmentPC);
		Nexus.setClearCacheOnStart(clearCache);

		launch(MainFrame.class, args);
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
					C3Logger.info("Application exit: logging off server");
					Logout.doLogout(false);
				}

				C3Logger.shutdown();
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
				scene.setCursor(imageCursorWait);
				waitcursor = true;
				break;
			case CURSOR_REQUEST_NORMAL:
				scene.setCursor(imageCursorNormal);
				waitcursor = false;
				break;
			case CURSOR_REQUEST_WAIT_MESSAGE:
				scene.setCursor(imageCursorWait);
				break;
			case CURSOR_REQUEST_NORMAL_MESSAGE:
				scene.setCursor(imageCursorNormal);
				break;
			case SHOW_MESSAGE:
				messageactive = true;
				break;
			case SHOW_MESSAGE_WAS_ANSWERED:
				messageactive = false;
				break;
			default:
				break;
		}
		return true;
	}
}
