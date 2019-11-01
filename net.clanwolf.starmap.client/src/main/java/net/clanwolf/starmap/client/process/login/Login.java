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
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.process.network.EventCommunications;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.JCrypt;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

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
					String encrypted_pass = JCrypt.crypt(C3Properties.getProperty("s"), password);
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
	 * @throws java.net.UnknownHostException
	 */
	public static void doLogin() throws Exception {
		// Upon login, get the values from the textfields and set the
		// properties. The database comes from the settings!

		C3Logger.info("Login");
		String used_username;
		String used_password;

		if ("true".equals(C3Properties.getProperty(C3PROPS.USE_GUEST_ACCOUNT))) {
			used_username = guest_user;
			used_password = JCrypt.crypt(C3Properties.getProperty("s"), guest_pass);
		} else {
			used_username = username;
			if (passwordEncrypted) {
				used_password = password;
			} else {
				used_password = JCrypt.crypt(C3Properties.getProperty("s"), password);
			}
		}

		C3Logger.info("Used username: " + used_username + " (enable output in source to debug credentials).");
//		C3Logger.debug("Used (encrypted) password: " + used_password);
//		C3Logger.debug("Used password: " + password);

		/*
		 * BEGIN Server Login
		 */
		String tcphostname = C3Properties.getProperty(C3PROPS.TCP_HOSTNAME);
		int tcpPort = Integer.parseInt(C3Properties.getProperty(C3PROPS.TCP_PORT));

		LoginBuilder builder;
		if(Nexus.isDevelopmentPC()){
			builder = new LoginHelper.LoginBuilder().username(used_username).password(used_password).connectionKey("C3GameRoomForNettyClient").nadronTcpHostName("localhost").tcpPort(18090);
		} else {
			//212.227.253.80
			builder = new LoginHelper.LoginBuilder().username(used_username).password(used_password).connectionKey("C3GameRoomForNettyClient").nadronTcpHostName(tcphostname).tcpPort(tcpPort);
		}

		LoginHelper loginHelper = builder.build();

		SessionFactory sessionFactory = new SessionFactory(loginHelper);
		sessionFactory.setLoginHelper(loginHelper);
		final Session session = sessionFactory.createSession();
		session.setReconnectPolicy(new ReconnectPolicy.ReconnectNTimes(2, 2000, loginHelper));

		// It is needed to send an Event to the server
		Nexus.setSession(session);

		// addDefaultHandlerToSession(session);

		// add handler for start event, and continue rest of game logic from
		// there.
		session.addHandler(new StartEventHandler(session) {
			@Override
			public void onEvent(Event event) {
				C3Logger.info("Going to Change to Object Protocol");
				session.resetProtocol(NettyObjectProtocol.INSTANCE);

				// create C3State objects send it to server.

				// C3GameState state = new C3GameState(1);
				// NetworkEvent networkEvent = Events.networkEvent(state);
				session.removeHandler(this);
				addDefaultHandlerToSession(session);
				// session.onEvent(networkEvent);
			}
		});

		// Connect the session, so that the above start event will be sent by
		// server.
		sessionFactory.connectSession(session);

		/*
		 * END Server Login
		 */
	}

	/*
	 * Handle Server Events
	 */
	private static void addDefaultHandlerToSession(Session session) {
		// we are only interested in data in, so override only that method.
		AbstractSessionEventHandler handler = new AbstractSessionEventHandler(session) {

			@Override
			public void onDisconnect(Event event) {
				// TODO: Implement a new Action for disconnect from server
				C3Logger.info("onDisconnect");
				super.onDisconnect(event);

			}

			@Override
			public void onLoginFailure(Event event) {
				// String result = "";
				super.onLoginFailure(event); // To change body of generated
				// methods, choose Tools |
				// Templates.

				C3Logger.info("onLoginFailure: " + "Check UserDTO or Password!");
				ActionManager.getAction(ACTIONS.LOGON_FINISHED_WITH_ERROR).execute();
			}

			@Override
			public void onLoginSuccess(Event event) {
				super.onLoginSuccess(event);
				C3Logger.info("onLoginSuccess: USER_REQUEST_LOGGED_IN_DATA");
				GameState state = new GameState(GAMESTATEMODES.USER_REQUEST_LOGGED_IN_DATA);

				NetworkEvent networkEvent = Events.networkEvent(state);
				session.onEvent(networkEvent);

				ActionManager.getAction(ACTIONS.LOGGED_ON).execute();
			}

			@Override
			public void onDataIn(Event event) {
				EventCommunications.onDataIn(session, event);
			}

		};

		session.addHandler(handler);
	}

	public static void login(String username, String password, String factionKey, boolean passwordEncrypted) throws Exception {

		Login.username = username;
		Login.password = password;
		Login.passwordEncrypted = passwordEncrypted;

		savePassword();
		doLogin();
	}
}
