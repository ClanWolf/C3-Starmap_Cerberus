/* ---------------------------------------------------------------- |
 * W-7 Research Group / C3                                          |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *          W-7 Facility / Research, Software Development           |
 *                    Tranquil (Mobile Division)                    |
 * __        __  _____   ____                               _       |
 * \ \      / / |___  | |  _ \ ___  ___  ___  __ _ _ __ ___| |__    |
 *  \ \ /\ / /____ / /  | |_) / _ \/ __|/ _ \/ _` | '__/ __| '_ \   |
 *   \ V  V /_____/ /   |  _ <  __/\__ \  __/ (_| | | | (__| | | |  |
 *    \_/\_/     /_/    |_| \_\___||___/\___|\__,_|_|  \___|_| |_|  |
 *                                                                  |
 *  W-7 is the production facility of Clan Wolf. The home base is   |
 *  Tranquil, but there are several mobile departments. In the      |
 *  development department there is a small group of developers and |
 *  designers busy to field new software products for battlefield   |
 *  commanders as well as for strategic dimensions of the clans     |
 *  operations. The department is led by a experienced StarColonel  |
 *  who fell out of active duty due to a wound he suffered during   |
 *  the battle on Tukkayid. His name and dossier are classified,    |
 *  get in contact through regular chain of command.                |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MkIII "Damien"                   |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 *  One of the products used to control the production and the      |
 *  transport of frontline troops is C3. C3 stands for              |
 *  "Communication - Command - Control".                            |
 *  Because there is a field based system to control the            |
 *  communication and data transfer of Mechs, this system is often  |
 *  refered to as "Big C3", however, the official name is           |
 *  "W-7 C3 / MkIII 'Damien'".                                      |
 *                                                                  |
 *  Licensing through W-7 Facility Central Office, Tranquil.        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  Info        : http://www.clanwolf.net                           |
 *  Forum       : http://www.clanwolf.net                           |
 *  Web         : http://c3.clanwolf.net                            |
 *  GitHub      : https://github.com/ClanWolf/C3-Java_Client        |
 *                                                                  |
 *  IRC         : starmap.clanwolf.net @ Quakenet                        |
 *                                                                  |
 *  Devs        : - Christian (Meldric)                    [active] |
 *                - Werner (Undertaker)                    [active] |
 *                - Thomas (xfirestorm)                    [active] |
 *                - Domenico (Nonnex)                     [retired] |
 *                - Dirk (kotzbroken2)                    [retired] |
 *                                                                  |
 *                  (see Wolfnet for up-to-date information)        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  C3 includes libraries and source code by various authors,       |
 *  for credits and info, see README.                               |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 * Copyright 2016 ClanWolf.net                                      |
 *                                                                  |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 *                                                                  |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.beans;

import io.nadron.app.PlayerSession;
import io.nadron.app.impl.GameRoomSession;
import io.nadron.event.Event;
import io.nadron.event.Events;
import io.nadron.event.impl.DefaultEventContext;
import io.nadron.event.impl.DefaultSessionEventHandler;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;

import java.util.ArrayList;

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

		if (((C3Player) playerSession.getPlayer()).getUser() == null) {

			// Send error message if user is null			
			C3Logger.debug("C3Room.onLogin: no user found -> send Events.LOG_IN_FAILURE");
			Event e = Events.event(null, Events.LOG_IN_FAILURE);
			playerSession.onEvent(e);

		} else {

			// Create a new GameState with the UserPOJO for the client, if login was successful
			C3Logger.debug("C3Room.onLogin: -> " + playerSession.getId());

			// send event LOG_IN_SUCCESS to client
			C3Logger.debug("C3Room.onLogin: -> send LOG_IN_SUCCESS Event");
			Event e = Events.event(null, Events.LOG_IN_SUCCESS);
			playerSession.onEvent(e);

			// check for wrong sessions from this player

		} // end if

	}

	@Override
	public synchronized boolean disconnectSession(PlayerSession playerSession) {
		C3Logger.debug("disconnectSession");
		C3Logger.info("disconnectSession: disconnected session -> " + playerSession.getId());
		// remove player session from list with the wrong sessions
		wrongPlayerSessions.remove(playerSession.getId());

		// EntityManager for room session must be closed on disconnect
		if(playerSession.getPlayer().getId() != null) {
			EntityManagerHelper.closeEntityManager((Long) playerSession.getPlayer().getId());
		}
			
		return super.disconnectSession(playerSession);
	}
}
