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
package net.clanwolf.starmap.client.net;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.enums.C3MESSAGERESULTS;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;
import net.clanwolf.starmap.client.gui.messagepanes.C3Message;
import net.clanwolf.starmap.client.nexus.Nexus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

/**
 * Provides methods to check the server situation.
 *
 * @author Meldric
 */
public class Server {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static String serverURL = "";
	private static String username = "";
	private static String password = "";
	private static String database = "";

	/**
	 * Constructor Prepares variables for server access.
	 */
	public Server() {
		// Constructor
	}

	public static String checkLastAvailableClientVersion() {
		serverURL = C3Properties.getProperty(C3PROPS.SERVER_URL);
		// Add a trailing slash if not present
		if (!serverURL.endsWith("/")) {
			serverURL = serverURL + "/";
		}
		String value = "not found";
		try {
			URI uri = new URI(serverURL + "server/php/C3-LatestClientVersion.php");
			URL url = uri.toURL();
			value = new String(HTTP.get(url));
//			logger.info("Connection URL: " + url);
//			logger.info("Connection Result: " + value);
		} catch (IOException | URISyntaxException e) {
			logger.error(null, e);
		}
		logger.info("Client version check done.");
//		logger.info("Client version available: " + value);
		Nexus.setLastAvailableClientVersion(value);
		return value;
	}

	/**
	 * Method to check if the given database is accessible.
	 *
	 * @return boolean result
	 * @author Meldric
	 */
	public static boolean checkDatabaseConnection() {
		serverURL = C3Properties.getProperty(C3PROPS.SERVER_URL);
		// Add a trailing slash if not present
		if (!serverURL.endsWith("/")) {
			serverURL = serverURL + "/";
		}
		String value;
		boolean r = false;
		boolean online = false;

		while(!online) {
			if (Objects.equals(C3Properties.getProperty(C3PROPS.CHECK_ONLINE_STATUS), "OFFLINE")) {
				logger.info("offline [8732]");
			} else if (Objects.equals(C3Properties.getProperty(C3PROPS.CHECK_ONLINE_STATUS), "ONLINE")) {
				online = true;
				// Server online check ok, testing db
				try {
					logger.info(serverURL + "server/php/C3-OnlineStatus_Database.php?p1=" + C3Properties.getProperty(C3PROPS.LOGIN_DATABASE));
					URI uri = new URI(serverURL + "server/php/C3-OnlineStatus_Database.php?p1=" + C3Properties.getProperty(C3PROPS.LOGIN_DATABASE));
					URL url = uri.toURL();
					value = new String(HTTP.get(url));
					logger.info("Connection URL: " + url);
					logger.info("Connection Result: " + value);
					// use "endswith" here, in case debugging in PHP is enabled!
					r = value.equals("online");
				} catch (IOException | URISyntaxException e) {
					logger.error("Error while checking online database status.", e);
				}
				logger.info("Database connection check done.");
				if (r) {
					logger.info("Database connection check: database accessible!");
					C3Properties.setProperty(C3PROPS.CHECK_CONNECTION_STATUS, "ONLINE");
				} else {
					logger.info("Database connection check: database in-accessible, check values!");
					C3Properties.setProperty(C3PROPS.CHECK_CONNECTION_STATUS, "OFFLINE");
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				//
			}
		}
		return r;
	}

	/**
	 * This method encapsulates the checkServerStatus routine into a task in order to run it as a parallel task.
	 *
	 * @author Meldric
	 */
	public static void checkDatabaseConnectionTask() {
		final Task<Boolean> checkDatabaseConnectionTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws IOException, InterruptedException {
				return checkDatabaseConnection();
			}
		};

		Thread t = new Thread(checkDatabaseConnectionTask);
		t.setDaemon(true);

		checkDatabaseConnectionTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				ActionManager.getAction(ACTIONS.DATABASECONNECTIONCHECK_FINISHED).execute(checkDatabaseConnectionTask.getValue());
			}
		});

		ActionManager.getAction(ACTIONS.DATABASECONNECTIONCHECK_STARTED).execute();
		t.start();
	}

	/**
	 * Does a pre-check on the database if the given user exists and can connect.
	 *
	 * @param user The user.
	 * @param pass The password.
	 * @return int
	 * @author Meldric
	 */
	public static int doCheckLogin(String user, String pass) {
		// 0 : Everything is fine, user found
		// -101 : Database not accessible / valid
		// -102 : Username / Password wrong

		username = user;
		password = pass;
		database = C3Properties.getProperty(C3PROPS.LOGIN_DATABASE);
		serverURL = C3Properties.getProperty(C3PROPS.SERVER_URL);

		// Add a trailing slash if not present
		if (!serverURL.endsWith("/")) {
			serverURL = serverURL + "/";
		}

		String checkresult = "";
		String finalResult = "";
		// int result = 0;

		try {
			String u = serverURL + "C3_CheckUserLogin.php?cps1=" + username + "&cus4=" + password + "&db_database=" + database;
			URI uri = new URI(u);
			URL url = uri.toURL();
			logger.info("URL: " + url);
			checkresult = new String(HTTP.get(url));
		} catch (IOException | URISyntaxException e) {
			logger.error("Error while checking user login.", e);
		}
		checkresult = checkresult.replaceAll("\\n", "");
		checkresult = checkresult.replaceAll("\\r", "");

		String[] resultLines = checkresult.split("<br/>");
		for (String resultLine : resultLines) {
			if (resultLine.startsWith("[r:")) {
				finalResult = resultLine.substring(3, resultLine.lastIndexOf("]"));
			}
		}
		logger.info("Result: " + finalResult);
		return Integer.parseInt(finalResult);
	}

	/**
	 * This method encapsulates the checkServerStatus routine into a task in order to run it as a parallel task.
	 *
	 * @param user The user
	 * @param pass The password
	 * @author Meldric
	 */
	public static void doCheckLoginTask(String user, String pass) {
		final Task<Integer> doCheckLoginTask = new Task<Integer>() {
			@Override
			protected Integer call() throws IOException, InterruptedException {
				return doCheckLogin(user, pass);
			}
		};

		Thread t = new Thread(doCheckLoginTask);
		t.setDaemon(true);

		doCheckLoginTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				ActionManager.getAction(ACTIONS.LOGINCHECK_FINISHED).execute(doCheckLoginTask.getValue());
			}
		});

		ActionManager.getAction(ACTIONS.LOGINCHECK_STARTED).execute();
		t.start();
	}

	/**
	 * Checks if the server is responding and php is working.
	 *
	 * @return boolean server status
	 * @author Meldric
	 */
	public static boolean checkServerStatus() {
		serverURL = C3Properties.getProperty(C3PROPS.SERVER_URL);
		// Add a trailing slash if not present
		if (!serverURL.endsWith("/")) {
			serverURL = serverURL + "/";
		}

		String value;
		boolean r = false;
		try {
			URI uri = new URI(serverURL + "server/php/C3-OnlineStatus_Server.php");
			URL url = uri.toURL();
			logger.info("Checking: " + url.toString());
			value = new String(HTTP.get(url));
			r = "online".equals(value);
		} catch (IOException | URISyntaxException e) {
			logger.error("Error while checking online status.", e);
		}
		if (r) {
			logger.info("Onlinestatus: online");
		} else {
			logger.info("Onlinestatus: offline");
		}
		return r;
	}

	/**
	 * This method encapsulates the checkServerStatus routine into a task in order to run it as a parallel task.
	 *
	 * @author Meldric
	 */
	public static void checkServerStatusTask() {
		final Task<Boolean> checkServerStatusTask = new Task<Boolean>() {
			@Override
			protected Boolean call() throws IOException, InterruptedException {
				return checkServerStatus();
			}
		};

		Thread t = new Thread(checkServerStatusTask);
		t.setDaemon(true);

		checkServerStatusTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				ActionManager.getAction(ACTIONS.ONLINECHECK_FINISHED).execute(checkServerStatusTask.getValue());
			}
		});

		ActionManager.getAction(ACTIONS.ONLINECHECK_STARTED).execute();
		t.start();
	}
}
