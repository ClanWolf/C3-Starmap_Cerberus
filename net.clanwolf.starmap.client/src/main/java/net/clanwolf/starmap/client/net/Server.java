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
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides methods to check the server situation.
 *
 * @author Meldric
 */
public class Server {

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
			URL url = new URL(serverURL + "server/php/C3-LatestClientVersion.php");
			value = new String(HTTP.get(url));
//			C3Logger.debug("Connection URL: " + url);
//			C3Logger.debug("Connection Result: " + value);
		} catch (MalformedURLException e) {
			C3Logger.exception(null, e);
		} catch (IOException ioe) {
			C3Logger.exception( null,ioe);
		}
		C3Logger.info("Client version check done.");
//		C3Logger.info("Client version available: " + value);
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
			if (C3Properties.getProperty(C3PROPS.CHECK_ONLINE_STATUS) == "OFFLINE") {

			} else if (C3Properties.getProperty(C3PROPS.CHECK_ONLINE_STATUS) == "ONLINE") {
				online = true;
				// Server online check ok, testing db
				try {
					C3Logger.debug(serverURL + "server/php/C3-OnlineStatus_Database.php?p1=" + C3Properties.getProperty(C3PROPS.LOGIN_DATABASE));
					URL url = new URL(serverURL + "server/php/C3-OnlineStatus_Database.php?p1=" + C3Properties.getProperty(C3PROPS.LOGIN_DATABASE));
					value = new String(HTTP.get(url));
					C3Logger.debug("Connection URL: " + url);
					C3Logger.debug("Connection Result: " + value);
					// use "endswith" here, in case debugging in PHP is enabled!
					r = value.equals("online");
				} catch (MalformedURLException e) {
					C3Logger.exception(null, e);
				} catch (IOException ioe) {
					C3Logger.exception(null, ioe);
				}
				C3Logger.info("Database connection check done.");
				if (r) {
					C3Logger.info("Database connection check: database accessible!");
					C3Properties.setProperty(C3PROPS.CHECK_CONNECTION_STATUS, "ONLINE");
				} else {
					C3Logger.info("Database connection check: database in-accessible, check values!");
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
			URL url = new URL(u);
			C3Logger.debug(url + "");
			checkresult = new String(HTTP.get(url));
		} catch (MalformedURLException e) {
			C3Logger.exception(null, e);
		} catch (IOException ioe) {
			C3Logger.exception( null,ioe);
		}
		checkresult = checkresult.replaceAll("\\n", "");
		checkresult = checkresult.replaceAll("\\r", "");

		String[] resultLines = checkresult.split("<br/>");
		for (String resultLine : resultLines) {
			if (resultLine.startsWith("[r:")) {
				finalResult = resultLine.substring(3, resultLine.lastIndexOf("]"));
			}
		}
		C3Logger.debug("Result: " + finalResult);
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
			URL url = new URL(serverURL + "/server/php/C3-OnlineStatus_Server.php");
			value = new String(HTTP.get(url));
			r = "online".equals(value);
		} catch (MalformedURLException e) {
			C3Logger.exception(null, e);
		} catch (IOException ioe) {
			C3Logger.exception( null,ioe);
		}
		if (r) {
			C3Logger.info("Onlinestatus: online");
		} else {
			C3Logger.info("Onlinestatus: offline");
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
