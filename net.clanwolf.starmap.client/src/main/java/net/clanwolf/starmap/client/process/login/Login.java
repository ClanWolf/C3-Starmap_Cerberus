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
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.util.*;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.process.network.EventCommunications;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.util.concurrent.TimeUnit;

/**
 * @author Meldric
 */
public class Login {

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

	public static void savePassword() {
		if (!guestLogin) {
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

	/**
	 * Do the login itself.
	 *
	 * @throws Exception
	 */
	public static void doLogin() throws Exception {
		// Upon login, get the values from the textfields and set the
		// properties. The database comes from the settings!

		C3Logger.info("Login");
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
				used_password = Encryptor.createPasswordPair(password);
			}
		}

		C3Logger.debug("Used username: " + used_username + " (enable output in source to debug credentials).");
//		C3Logger.debug("Used password: " + password);
//		C3Logger.debug("Used (encrypted) password: " + used_password);
//		C3Logger.debug("PW1: " + Encryptor.getPasswordFromPair("first", used_password));
//		C3Logger.debug("PW2: " + Encryptor.getPasswordFromPair("second", used_password));

		/*
		 * BEGIN Server Login
		 */
		String tcphostname = C3Properties.getProperty(C3PROPS.TCP_HOSTNAME);
		int tcpPort = Integer.parseInt(C3Properties.getProperty(C3PROPS.TCP_PORT));

		LoginBuilder builder;
		if(Nexus.isDevelopmentPC()){
			builder = new LoginHelper.LoginBuilder().username(used_username).password(used_password).connectionKey("C3GameRoomForNettyClient").nadronTcpHostName("localhost").tcpPort(18090);
		} else {
			// 217.160.60.129
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
		C3Logger.info("Session created: " + session + " (Session-ID: " + session.getId() + ")");

		StartEventHandler startEventHandler = new StartEventHandler(session) {
			@Override
			public void onEvent(Event event) {
				// C3Logger.info("Event: " + event.toString() + ". Change to Object Protocol.");
				if (event.getSource() instanceof GameState) {
					// 0x1a START Event
					GameState state = (GameState) event.getSource();
					// C3Logger.info("Event gamestate mode: " + state.getMode());
				} else {
					// C3Logger.info("Event source: " + event.getSource());
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
			public void onGameRoomJoin(Event event) {
				super.onGameRoomJoin(event);
				C3Logger.debug("##### Gameroom join!");
			}

			@Override
			public void onChangeAttribute(Event event) {
				super.onChangeAttribute(event);
				C3Logger.debug("##### Attribute changed!");
			}

			@Override
			public synchronized void onException(Event event) {
				super.onException(event);
				C3Logger.debug("##### EXCEPTION!");
			}

			@Override
			public void onStop(Event event) {
				super.onStop(event);
				C3Logger.debug("##### Session stopped!");
			}

			@Override
			public void onStart(Event event) {
				super.onStart(event);
				C3Logger.debug("##### Session started!");
			}

			@Override
			public void onConnectFailed(Event event) {
				super.onConnectFailed(event);
				C3Logger.debug("##### Connect failed!");
			}

			@Override
			public void onDisconnect(Event event) {
				// TODO: Implement a new Action for disconnect from server
				super.onDisconnect(event);
				C3Logger.info("onDisconnect");
				loginInProgress = false;
			}

			@Override
			public void onLoginFailure(Event event) {
				super.onLoginFailure(event);
				loginInProgress = false;
				C3Logger.info("Login failed!");
				C3Logger.info("onLoginFailure: Check Username and/or Password!");
				ActionManager.getAction(ACTIONS.LOGON_FINISHED_WITH_ERROR).execute();
			}

			@Override
			public void onLoginSuccess(Event event) {
				super.onLoginSuccess(event);
				loginInProgress = false;
				C3Logger.info("Successfully logged in. Sending: USER_REQUEST_LOGGED_IN_DATA");
				GameState state = new GameState(GAMESTATEMODES.USER_REQUEST_LOGGED_IN_DATA);
				NetworkEvent networkEvent = Events.networkEvent(state);
				session.onEvent(networkEvent);
				ActionManager.getAction(ACTIONS.LOGGED_ON).execute();
			}

			@Override
			public void onNetworkMessage(NetworkEvent networkEvent) {
				super.onNetworkMessage(networkEvent);
				C3Logger.info("Event: " + networkEvent.getType() + ". Source: " + ((GameState)networkEvent.getSource()).getModeString());
			}

			@Override
			public void onDataIn(Event event) {
				C3Logger.info("OnDataIn source: " + ((GameState)event.getSource()).getModeString());
				EventCommunications.onDataIn(session, event);
			}
		};
		C3Logger.info("Adding SessionEventHandler to session.");
		session.addHandler(handler);

		C3Logger.info("Client is ready for events.");
		GameState s = new GameState(GAMESTATEMODES.CLIENT_READY_FOR_EVENTS);
		Nexus.fireNetworkEvent(s);
	}

	public static void login(String username, String password, String factionKey, boolean passwordEncrypted) throws Exception {

		if (loginInProgress) {
			C3Logger.info("Login rejected, already logging in...");
			return;
		} else {
			C3Logger.info("Login initiated.");
			loginInProgress = true;
		}

		Login.username = username;
		Login.password = password;
		Login.passwordEncrypted = passwordEncrypted;

		savePassword();
		doLogin();
	}
}
