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
package net.clanwolf.starmap.server.beans;

import io.nadron.app.Player;
import io.nadron.app.PlayerSession;
import io.nadron.app.impl.GameRoomSession;
import io.nadron.event.Event;
import io.nadron.event.Events;
import io.nadron.event.impl.DefaultEventContext;
import io.nadron.event.impl.DefaultSessionEventHandler;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * The onLogin method is overriden so that incoming player sessions can be initialzied with event handlers to do user logic. In this scenario, the only thing the handler does is to patch incoming messages to the GameRoom which in turn has the game
 * logic and state_login. In more real-world scenarios, the session handler can have its own logic, for e.g. say validation to prevent cheating, filtering, pre-processing of event etc.
 *
 * @author Werner Kewenig
 *
 */
public class C3Room extends GameRoomSession {
	private static ArrayList<Object> wrongPlayerSessions;



	public C3Room(GameRoomSessionBuilder builder) {
		super(builder);
		wrongPlayerSessions = new ArrayList<>();
		addHandler(new C3GameSessionHandler(this));
	}

	@Override
	public void onLogin(final PlayerSession playerSession) {
		C3Logger.debug("C3Room.onLogin");

		// Add a session handler to player session. So that it can receive events.
		playerSession.addHandler(new DefaultSessionEventHandler(playerSession) {

			@Override
			protected void onDataIn(Event event) {
				C3Logger.debug("C3Room.onLogin.DefaultSessionEventHandler: onDataIn");
				C3Logger.info("C3Room.onLogin.DefaultSessionEventHandler: PlayerSessionID -> " + playerSession.getId());

				if (null != event.getSource()) {
					// Pass the player session in the event context so that the
					// game room knows which player session send the message.
					event.setEventContext(new DefaultEventContext(playerSession, null));

					// pass the event to the game room
					playerSession.getGameRoom().send(event);
				}
			}

			@Override
			protected void onLoginFailure(Event event) {
				super.onLoginFailure(event);

				C3Logger.debug("C3Room.onLogin.DefaultSessionEventHandler.onLoginFailure");
				playerSession.getPlayer().logout(playerSession);
			}

			@Override
			protected void onDisconnect(Event event) {
				super.onDisconnect(event);

				C3Logger.debug("C3Room.onLogin.DefaultSessionEventHandler.onDisconnect");
				playerSession.getPlayer().logout(playerSession);
			}

			@Override
			protected void onLogout(Event event) {
				super.onLogout(event);
				C3Logger.debug("C3Room.onLogin.DefaultSessionEventHandler.onLogout");
			}
		});

		Event e;
		if (((C3Player) playerSession.getPlayer()).getUser() == null) {
			// Send error message if user is null
			C3Logger.debug("C3Room.onLogin: no user found -> send Events.LOG_IN_FAILURE");
			e = Events.event(null, Events.LOG_IN_FAILURE);
		} else {
			// Create a new GameState with the UserPOJO for the client, if login was successful
			C3Logger.debug("C3Room.onLogin: -> sending LOG_IN_SUCCESS Event. Session: " + playerSession.getId());
			e = Events.event(null, Events.LOG_IN_SUCCESS);
		}

		/*( new Thread() { public void run() {
			boolean ready;
			int counter = 10;
			do {
				ready = getSessionReadyMap().containsKey(playerSession) && getSessionReadyMap().get(playerSession);
				if (ready || counter == 0) {
					break;
				} else {
					try {
						C3Logger.debug("Waiting a moment before send the login result event...");
						TimeUnit.MILLISECONDS.sleep(250);
						counter--;
					} catch (InterruptedException interruptedException) {
						interruptedException.printStackTrace();
					}
				}
			} while(!ready);

			C3Logger.debug("C3Room.onLogin: -> adding Event to PlayerSession");
			playerSession.onEvent(e);
			C3Logger.debug("C3Room.onLogin: -> LOG_IN_SUCCESS Event sent");
		} } ).start();*/

		C3Logger.debug("C3Room.onLogin: -> adding Event to PlayerSession");
		playerSession.onEvent(e);
		C3Logger.debug("C3Room.onLogin: -> LOG_IN_SUCCESS Event sent");
	}

	@Override
	public synchronized boolean disconnectSession(PlayerSession playerSession) {
		C3Logger.debug("disconnectSession");
		C3Logger.info("disconnectSession: disconnected session -> " + playerSession.getId());
		// remove player session from list with the wrong sessions
		wrongPlayerSessions.remove(playerSession.getId());
		getSessionReadyMap().remove(playerSession);

		// EntityManager for room session must be closed on disconnect
		if(playerSession.getPlayer().getId() != null) {
			EntityManagerHelper.closeEntityManager((Long) playerSession.getPlayer().getId());
		}
			
		return super.disconnectSession(playerSession);
	}
}
