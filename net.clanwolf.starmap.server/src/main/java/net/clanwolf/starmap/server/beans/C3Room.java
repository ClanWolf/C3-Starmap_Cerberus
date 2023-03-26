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
package net.clanwolf.starmap.server.beans;

import io.nadron.app.PlayerSession;
import io.nadron.app.impl.GameRoomSession;
import io.nadron.event.Event;
import io.nadron.event.Events;
import io.nadron.event.impl.DefaultEventContext;
import io.nadron.event.impl.DefaultSessionEventHandler;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.server.nexus2.Nexus;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.AttackDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackCharacterPOJO;
import net.clanwolf.starmap.server.persistence.pojos.AttackPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayCharacterPOJO;
import net.clanwolf.starmap.server.persistence.pojos.UserPOJO;
import net.clanwolf.starmap.server.timertasks.HeartBeatTimerTask;
import net.clanwolf.starmap.transfer.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * The onLogin method is overriden so that incoming player sessions can be initialzied with event handlers to do user logic. In this scenario, the only thing the handler does is to patch incoming messages to the GameRoom which in turn has the game
 * logic and state_login. In more real-world scenarios, the session handler can have its own logic, for e.g. say validation to prevent cheating, filtering, pre-processing of event etc.
 *
 * @author Werner Kewenig
 */
public class C3Room extends GameRoomSession {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static ArrayList<Object> wrongPlayerSessions;
	private static ArrayList<C3Room> c3Rooms = new ArrayList<C3Room>();

	public C3Room(GameRoomSessionBuilder builder) {
		super(builder);
		wrongPlayerSessions = new ArrayList<>();
		addHandler(new C3GameSessionHandler(this));
		c3Rooms.add(this);
	}

	public static void sendBroadcastMessage(GameState state) {
		for (C3Room r : c3Rooms) {
			C3GameSessionHandler.sendBroadCast(r, state);
		}
	}

	static Long getC3UserID(PlayerSession session) {
		return (Long) session.getPlayer().getId();
	}

	@Override
	public void onLogin(final PlayerSession playerSession) {
		logger.info("C3Room.onLogin");

		// Add a session handler to player session. So that it can receive events.
		playerSession.addHandler(new DefaultSessionEventHandler(playerSession) {

			@Override
			protected void onDataIn(Event event) {
//				logger.info("C3Room.onLogin.DefaultSessionEventHandler.onDataIn - PlayerSessionID -> " + playerSession.getId());

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
				logger.info("C3Room.onLogin.DefaultSessionEventHandler.onLoginFailure");
				playerSession.getPlayer().logout(playerSession);
			}

			@Override
			protected void onDisconnect(Event event) {
				super.onDisconnect(event);
				logger.info("C3Room.onLogin.DefaultSessionEventHandler.onDisconnect");
				playerSession.getPlayer().logout(playerSession);
			}

			@Override
			protected void onLogout(Event event) {
				super.onLogout(event);
				logger.info("C3Room.onLogin.DefaultSessionEventHandler.onLogout");
			}
		});

		new Thread(() -> {
			boolean ready;
			int counter = 30;

			do {
				ready = getSessionReadyMap().containsKey(playerSession.getId().toString()) && getSessionReadyMap().get(playerSession.getId().toString());
				try {
					logger.info("Waiting a moment before send the login result event...");
//						logger.info("Session map cointains key: " + getSessionReadyMap().containsKey(playerSession.getId().toString()));
//						logger.info("Session is ready: " + getSessionReadyMap().get(playerSession.getId().toString()));
//						logger.info("Ready eval: " + (getSessionReadyMap().containsKey(playerSession.getId().toString()) && getSessionReadyMap().get(playerSession.getId().toString())));
//						logger.info("Ready var: " + ready);

					TimeUnit.MILLISECONDS.sleep(100);
					counter--;
				} catch (InterruptedException interruptedException) {
					interruptedException.printStackTrace();
				}
			} while (!ready && counter > 0);

			Event e;
			if (((C3Player) playerSession.getPlayer()).getUser() == null) {
				// Send error message if user is null
				logger.info("C3Room.onLogin: no user found -> send Events.LOG_IN_FAILURE");

				e = Events.event(null, Events.LOG_IN_FAILURE);
			} else {
				logger.info("C3Room.onLogin: -> sending LOG_IN_SUCCESS Event. Session: " + playerSession.getId());
				e = Events.event(null, Events.LOG_IN_SUCCESS);
			}

			//			Iterator it = getSessionReadyMap().keySet().iterator();
			//			while(it.hasNext()) {
			//				String s = (String)it.next();
			//				logger.info("Session in sessionReadyMap: " + s + " (Value: " + getSessionReadyMap().get(s) + ")");
			//			}

			playerSession.onEvent(e);
			getSessionReadyMap().remove(playerSession.getId().toString());
		}).start();
	}

	@Override
	public synchronized boolean disconnectSession(PlayerSession playerSession) {
		logger.info("disconnectSession: disconnected session -> " + playerSession.getId());

		// remove player session from list with the wrong sessions
		wrongPlayerSessions.remove(playerSession.getId());
		getSessionReadyMap().remove(playerSession.getId().toString());

		C3Player p = (C3Player) playerSession.getPlayer();
		UserPOJO u = p.getUser();

		if (u != null) {
			RolePlayCharacterPOJO character = u.getCurrentCharacter();

			Nexus.getEci().sendExtCom(p.getName() + " lost connection to C3-Client (disconnected)", "en", true, true, false);
			Nexus.getEci().sendExtCom(p.getName() + " hat Verbindung zum C3-Client abgebrochen (disconnected)", "de", true, true, false);

			AttackDAO attackDAO = AttackDAO.getInstance();
			ArrayList<AttackPOJO> openAttacks = attackDAO.getOpenAttacksOfASeason(Nexus.currentSeason);

			boolean savedChanges = false;
			for (AttackPOJO ap : openAttacks) {
				if (!ap.getFightsStarted()) {
					for (AttackCharacterPOJO acp : ap.getAttackCharList()) {
						logger.info("Is the disconnecting char the lobbyowner? " + acp.getCharacterID() + " : " + character.getId());
						if (Objects.equals(acp.getCharacterID(), character.getId())) {
							if (acp.getType() == Constants.ROLE_ATTACKER_COMMANDER || acp.getType() == Constants.ROLE_DEFENDER_COMMANDER) {
								logger.info("Lobbyowner left, will be removed!");
								acp.setType(Constants.ROLE_DROPLEAD_LEFT);

								GameState s = new GameState();
								s.addObject(ap);
								s.addObject2(ap.getAttackTypeID());
								s.addObject3(acp);

								Nexus.gmSessionHandler.saveAttack(playerSession, s);
								savedChanges = true;
								break;
							}
						}
					}
				}
			}

			if (savedChanges) {
				Timer serverHeartBeat;
				serverHeartBeat = new Timer();
				serverHeartBeat.schedule(new HeartBeatTimerTask(false, null), 0);
			}
		}

		boolean ret = super.disconnectSession(playerSession);

		// EntityManager for room session must be closed on disconnect
		if (playerSession.getPlayer().getId() != null) {
			EntityManagerHelper.closeEntityManager((Long) playerSession.getPlayer().getId());
		}

		return ret;
	}
}
