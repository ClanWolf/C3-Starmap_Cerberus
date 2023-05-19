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
package net.clanwolf.starmap.client.process.login;

import io.nadron.client.app.Session;
import io.nadron.client.app.impl.SessionFactory;
import io.nadron.client.communication.ReconnectPolicy;
import io.nadron.client.event.Event;
import io.nadron.client.event.Events;
import io.nadron.client.event.NetworkEvent;
import io.nadron.client.event.impl.AbstractSessionEventHandler;
import io.nadron.client.event.impl.StartEventHandler;
import io.nadron.client.protocol.impl.NettyObjectProtocol;
import io.nadron.client.util.LoginHelper;
import io.nadron.client.util.LoginHelper.LoginBuilder;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.enums.C3MESSAGES;
import net.clanwolf.starmap.client.enums.C3MESSAGETYPES;
import net.clanwolf.starmap.client.gui.messagepanes.C3Message;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.process.logout.Logout;
import net.clanwolf.starmap.client.process.network.EventCommunications;
import net.clanwolf.starmap.client.util.*;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * @author Meldric
 */
public class Login {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static String username = "";
	private static String password = "";

	private static boolean passwordEncrypted = false;
	private static boolean guestLogin = "true".equals(C3Properties.getProperty(C3PROPS.USE_GUEST_ACCOUNT));
	private static boolean storePassword = "true".equals(C3Properties.getProperty(C3PROPS.STORE_LOGIN_PASSWORD));

	private static final String guest_user = "Guest";
	private static final String guest_pass = "waV9WKWtHnRmU2c9g6SLqhh";

	private static Session session = null;

	public static boolean loginInProgress = false;

	private Login() {
		// Prevent Instantiation
	}

	public static void savePassword(boolean registerMode) {
		if (!guestLogin) {
			if (!registerMode) {
				C3Properties.setProperty(C3PROPS.LOGIN_USER, username, true);
				if (storePassword) {
					if (passwordEncrypted) {
						C3Properties.setProperty(C3PROPS.LOGIN_PASSWORD, password, true);
					} else {
						String encrypted_pass = Encryptor.createPasswordPair(password);
						C3Properties.setProperty(C3PROPS.LOGIN_PASSWORD, encrypted_pass, true);
					}
				} else {
					C3Properties.setProperty(C3PROPS.LOGIN_PASSWORD, "", true);
				}
			}
		}
	}

	/**
	 * Do the login itself.
	 *
	 * @throws Exception
	 */
	public static void doLogin(boolean registerMode) throws Exception {
		// Upon login, get the values from the textfields and set the
		// properties. The database comes from the settings!

		logger.info("Login");
		String used_username;
		String used_password;

		if ("true".equals(C3Properties.getProperty(C3PROPS.USE_GUEST_ACCOUNT))) {
			used_username = guest_user;
			used_password = Encryptor.createPasswordPair(guest_pass);
		} else {
			used_username = username;
			if (passwordEncrypted) {
				used_password = password;
			} else {
				if (!registerMode) {
					// Normal operation -> login
					used_password = Encryptor.createPasswordPair(password);
				} else {
					// This is registration process, send only 2nd pw
					used_password = Encryptor.createSinglePassword(password);
				}
			}
		}

		logger.info("Used username: " + used_username + " (enable output in source to debug credentials).");
		//		logger.info("Used password: " + password);
		//		logger.info("Used (encrypted) password: " + used_password);
		//		logger.info("PW1: " + Encryptor.getPasswordFromPair("first", used_password));
		//		logger.info("PW2: " + Encryptor.getPasswordFromPair("second", used_password));

		/*
		 * BEGIN Server Login
		 */
		String tcphostname = C3Properties.getProperty(C3PROPS.TCP_HOSTNAME);
		int tcpPort = Integer.parseInt(C3Properties.getProperty(C3PROPS.TCP_PORT));

		LoginBuilder builder;
		if (Nexus.isDevelopmentPC()) {
			builder = new LoginHelper.LoginBuilder().username(used_username).password(used_password).connectionKey("C3GameRoomForNettyClient").nadronTcpHostName("localhost").tcpPort(18090);
		} else {
			// neu: 82.165.244.122 (alt: 217.160.60.129)
			builder = new LoginHelper.LoginBuilder().username(used_username).password(used_password).connectionKey("C3GameRoomForNettyClient").nadronTcpHostName(tcphostname).tcpPort(tcpPort);
		}
		LoginHelper loginHelper = builder.build();
		SessionFactory sessionFactory = new SessionFactory(loginHelper);
		sessionFactory.setLoginHelper(loginHelper);

		if (session != null) {
			session.close();
			session = null;
		}

		session = sessionFactory.createSession();
		logger.info("Session created: " + session + " (Session-ID: " + session.getId() + ")");

		StartEventHandler startEventHandler = new StartEventHandler(session) {
			@Override
			public void onEvent(Event event) {
				// logger.info("Event: " + event.toString() + ". Change to Object Protocol.");
				if (event.getSource() instanceof GameState) {
					// 0x1a START Event
					GameState state = (GameState) event.getSource();
					// logger.info("Event gamestate mode: " + state.getMode());
				} else {
					// logger.info("Event source: " + event.getSource());
				}

				session.resetProtocol(NettyObjectProtocol.INSTANCE);
				session.removeHandler(this); // Removing startEventHandler
				addDefaultHandlerToSession();
			}
		};

		session.addHandler(startEventHandler);

		session.setReconnectPolicy(new ReconnectPolicy.ReconnectNTimes(50, 5000, loginHelper));
		Nexus.setSession(session);

		sessionFactory.connectSession(session);
	}

	/*
	 * Handle Server Events
	 */
	private static void addDefaultHandlerToSession() {
		AbstractSessionEventHandler handler = new AbstractSessionEventHandler(session) {
			@Override
			public void onDataIn(Event event) {
				logger.info("OnDataIn source: " + ((GameState) event.getSource()).getModeString());
				EventCommunications.onDataIn(session, event);
			}

			@Override
			public void onNetworkMessage(NetworkEvent networkEvent) {
				super.onNetworkMessage(networkEvent);
				//				logger.info("Event: " + networkEvent.getType() + ". Source: " + ((GameState)networkEvent.getSource()).getModeString());
			}

			@Override
			public void onLoginSuccess(Event event) {
				super.onLoginSuccess(event);
				loginInProgress = false;
				logger.info("Successfully logged in. Sending: USER_REQUEST_LOGGED_IN_DATA");
				GameState state = new GameState(GAMESTATEMODES.USER_REQUEST_LOGGED_IN_DATA);
				state.addObject(Tools.getVersionNumber());
				NetworkEvent networkEvent = Events.networkEvent(state);
				session.onEvent(networkEvent);
				ActionManager.getAction(ACTIONS.LOGGED_ON).execute();
			}

			@Override
			public void onGameRoomJoin(Event event) {
				super.onGameRoomJoin(event);
				logger.info("##### Gameroom join!");
			}

			@Override
			public void onLoginFailure(Event event) {
				super.onLoginFailure(event);
				loginInProgress = false;
				logger.info("Login failed!");
				logger.info("onLoginFailure: Check Username and/or Password!");
				ActionManager.getAction(ACTIONS.LOGON_FINISHED_WITH_ERROR).execute();
			}

			@Override
			public void onStart(Event event) {
				super.onStart(event);
				logger.info("##### Session started!");
			}

			@Override
			public void onStop(Event event) {
				super.onStop(event);
				logger.info("##### Session stopped!");
			}

			@Override
			public void onConnectFailed(Event event) {
				super.onConnectFailed(event);
				logger.info("##### Connect failed!");
			}

			@Override
			public void onDisconnect(Event event) {
				// TODO_C3: Implement a new Action for disconnect from server
				super.onDisconnect(event);
				logger.info("onDisconnect");
				loginInProgress = false;
			}

			@Override
			public void onChangeAttribute(Event event) {
				super.onChangeAttribute(event);
				logger.info("##### Attribute changed!");
			}

			@Override
			public synchronized void onException(Event event) {
				super.onException(event);
				logger.info("##### EXCEPTION (in connection)!");
				logger.info("Event type: " + event.getType());
				logger.info("Event source: " + event.getSource());
				logger.info("Event time: " + event.getTimeStamp());
				ActionManager.getAction(ACTIONS.SERVER_CONNECTION_LOST).execute();
			}

			@Override
			public void onRegistrationFailedUserName(Event event) {
				super.onRegistrationFailedUserName(event);
				loginInProgress = false;
				// User is in registration, can not be logged in yet
				C3Message messageUserIsInRegistration = new C3Message(C3MESSAGES.ERROR_USER_IS_IN_REGISTRATION);
				String m = Internationalization.getString("general_user_registration_username_error");
				messageUserIsInRegistration.setText(m);
				messageUserIsInRegistration.setType(C3MESSAGETYPES.CLOSE);

				ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(messageUserIsInRegistration);
			}

			@Override
			public void onRegistrationFailedUserMail(Event event) {
				super.onRegistrationFailedUserMail(event);
				loginInProgress = false;
				// User is in registration, can not be logged in yet
				C3Message messageUserIsInRegistration = new C3Message(C3MESSAGES.ERROR_USER_IS_IN_REGISTRATION);
				String m = Internationalization.getString("general_user_registration_usermail_error");
				messageUserIsInRegistration.setText(m);
				messageUserIsInRegistration.setType(C3MESSAGETYPES.CLOSE);

				ActionManager.getAction(ACTIONS.SHOW_MESSAGE).execute(messageUserIsInRegistration);
			}
		};
		logger.info("Adding SessionEventHandler to session.");
		session.addHandler(handler);

		logger.info("Client is ready for events.");
		GameState s = new GameState(GAMESTATEMODES.CLIENT_READY_FOR_EVENTS);
		Nexus.fireNetworkEvent(s);
	}

	public static void login(String username, String password, String factionKey, boolean passwordEncrypted, boolean registerMode) throws Exception {

		if (loginInProgress) {
			logger.info("Login rejected, already logging in...");
			return;
		} else {
			logger.info("Login initiated.");
			loginInProgress = true;
		}

		Login.username = username;
		Login.password = password;
		Login.passwordEncrypted = passwordEncrypted;

		//		logger.info("username: " + username);
		//		logger.info("password: " + password);
		//		logger.info("encrypted:" + passwordEncrypted);

		savePassword(registerMode);
		doLogin(registerMode);
	}
}
