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

import io.nadron.app.GameRoom;
import io.nadron.app.PlayerSession;
import io.nadron.app.impl.GameRoomSession;
import io.nadron.event.Event;
import io.nadron.event.Events;
import io.nadron.event.impl.SessionMessageHandler;
import io.nadron.service.GameStateManagerService;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.persistence.EntityConverter;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.AttackDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.JumpshipDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RoutePointDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.UserDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackPOJO;
import net.clanwolf.starmap.server.persistence.pojos.JumpshipPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RoutePointPOJO;
import net.clanwolf.starmap.server.persistence.pojos.UserPOJO;
import net.clanwolf.starmap.server.util.WebDataInterface;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.JumpshipDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Undertaker
 */
public class C3GameSessionHandler extends SessionMessageHandler {
	private GameRoom room;// not really required. It can be accessed as getSession() also.
	private GameState state;
	private GameRoomSession roomSession;

	public C3GameSessionHandler(GameRoomSession session) {
		super(session);
		this.room = session;
		this.roomSession = session;
		GameStateManagerService manager = room.getStateManager();
		state = (GameState) manager.getState();

		// Initialize the room state_login.
		state = new GameState();
		manager.setState(state); // set it back on the room
	}

	@Override
	public void onEvent(Event event) {
		C3Logger.debug("C3GameSessionHandler.onEvent");
		GameState state = null;

		if (event.getSource() instanceof GameState) {
			state = (GameState) event.getSource();
		}

		if (state != null && event.getEventContext().getSession() instanceof PlayerSession) {
			executeCommand((PlayerSession) event.getEventContext().getSession(), state);
		}
	}

	private void executeCommand(PlayerSession session, GameState state) {
		C3Logger.debug("C3GameSessionHandler.executeCommand: " + state.getMode().toString());
		EntityConverter.convertGameStateToPOJO(state);

		switch (state.getMode()) {
		case BROADCAST_SEND_NEW_PLAYERLIST:
			sendNewPlayerList(session, state);
			break;
		case USER_REQUEST_LOGGED_IN_DATA:
			getLoggedInUserData(session);
			break;
		case USER_CHECK_DOUBLE_LOGIN:
			checkDoubleLogin(session, room);
			break;
		case USER_LOG_OUT:
			session.getPlayer().logout(session);
			sendNewPlayerList(session, state);
			break;
		case ROLEPLAY_SAVE_STORY:
			C3GameSessionHandlerRoleplay.saveRolePlayStory(session, state);
			break;
		case ROLEPLAY_REQUEST_ALLSTORIES:
			C3GameSessionHandlerRoleplay.requestAllStories(session, state);
			break;
		case ROLEPLAY_DELETE_STORY:
			C3GameSessionHandlerRoleplay.deleteRolePlayStory(session, state);
			break;
		case ROLEPLAY_REQUEST_ALLCHARACTER:
			C3GameSessionHandlerRoleplay.requestAllCharacter(session, state);
			break;
		case USER_SAVE:
			saveUser(session, state);
			break;
		case PRIVILEGE_SAVE:
			savePrivileges(session, state);
			break;
		case JUMPSHIP_SAVE:
			saveJumpship(session, state);
			break;
		case ATTACK_SAVE:
			saveAttack(session, state);
			break;
		case ROLEPLAY_GET_CHAPTER_BYSORTORDER:
			C3GameSessionHandlerRoleplay.getChapterBySortOrder(session, state);
			break;
		case ROLEPLAY_GET_STEP_BYSORTORDER:
			C3GameSessionHandlerRoleplay.getStepBySortOrder(session, state);
			break;
		case GET_UNIVERSE_DATA:
			C3GameSessionHandlerUniverse.getUniverseData(session, room);
			break;
		case ROLEPLAY_SAVE_NEXT_STEP:
			C3GameSessionHandlerRoleplay.saveRolePlayCharacterNextStep(session, state);
			break;
		case CLIENT_READY_FOR_EVENTS:
			C3Logger.info("Setting flag 'Client is ready for data' for Session: " + session);
			roomSession.getSessionReadyMap().put(session, Boolean.TRUE);
			getLoggedInUserData(session);
			break;
		default:
			break;
		}
	}

	private void saveUser(PlayerSession session, GameState state) {
		UserDAO dao = UserDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.USER_SAVE);

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));

			UserPOJO user = (UserPOJO)state.getObject();
			user.setLastModified(new Timestamp(System.currentTimeMillis()));
			if(user.getUserId() == null) {
				dao.save(getC3UserID(session), state.getObject());
			} else {
				dao.update(getC3UserID(session), state.getObject());
			}

			EntityManagerHelper.commit(getC3UserID(session));

			response.addObject(null);
			response.setAction_successfully(Boolean.TRUE);
		} catch (RuntimeException re) {
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);

			C3Logger.error("User save", re);
		} finally {

		}
	}

	private void saveAttack(PlayerSession session, GameState state) {
		AttackDAO dao = AttackDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.ATTACK_SAVE);

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));

			AttackPOJO attack = (AttackPOJO) state.getObject();
			C3Logger.info("Saving attack: " + attack);
			C3Logger.info("-- Attacker (jumpshipID): " + attack.getJumpshipID());
			C3Logger.info("-- Attacking from: " + attack.getAttackedFromStarSystemID());
			C3Logger.info("-- Attacked system: " + attack.getStarSystemID());
			dao.save(getC3UserID(session), attack);

			EntityManagerHelper.commit(getC3UserID(session));
			response.addObject(null);
			response.setAction_successfully(Boolean.TRUE);
		} catch (RuntimeException re) {
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);

			C3Logger.error("Attack save", re);
		} finally {
//			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	private void saveJumpship(PlayerSession session, GameState state){
		JumpshipDAO dao = JumpshipDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.JUMPSHIP_SAVE);

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));
			JumpshipPOJO js =(JumpshipPOJO) state.getObject();

			dao.update(C3GameSessionHandler.getC3UserID(session),js);

			EntityManagerHelper.commit(getC3UserID(session));

		} catch (RuntimeException re) {
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);

			C3Logger.error("Jumpship save", re);
		} finally {
//			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	private void saveRoute(PlayerSession session, GameState state) {

		RoutePointDAO dao = RoutePointDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.JUMPSHIP_SAVE);

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));
			ArrayList<RoutePointPOJO> list = (ArrayList<RoutePointPOJO>) state.getObject();
			dao.deleteByJumpshipId(getC3UserID(session), ((RoutePointPOJO)list.get(0)).getJumpshipId());

			Iterator<RoutePointPOJO> iter = list.iterator();
			while(iter.hasNext()) {
				RoutePointPOJO routePoint = (RoutePointPOJO) iter.next();
				routePoint.setId(null);

				C3Logger.info("Saving: " + routePoint);
				dao.save(getC3UserID(session), routePoint);
			}

			JumpshipDAO jsDao = JumpshipDAO.getInstance();
			jsDao.setAttackReady(getC3UserID(session), ((RoutePointPOJO)list.get(0)).getJumpshipId(), false);

			EntityManagerHelper.commit(getC3UserID(session));

			response.addObject(null);
			response.setAction_successfully(Boolean.TRUE);
		} catch (RuntimeException re) {
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);

			C3Logger.error("Route save", re);
		} finally {
//			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	private void savePrivileges(PlayerSession session, GameState state) {
		UserDAO dao = UserDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.PRIVILEGE_SAVE);
		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));
			ArrayList<UserPOJO> list = (ArrayList<UserPOJO>) state.getObject();
			Iterator<UserPOJO> iter = list.iterator();
			while(iter.hasNext()) {
				UserPOJO user = (UserPOJO) iter.next();
				user.setLastModified(new Timestamp(System.currentTimeMillis()));
				if(user.getUserId() == null) {
					// C3Logger.info("Saving: " + user.getUserName() + " - Privs: " + user.getPrivileges());
					dao.save(getC3UserID(session), user);
				} else {
					// C3Logger.info("Updating: " + user.getUserName() + " - Privs: " + user.getPrivileges());
					dao.update(getC3UserID(session), user);
				}
			}

			EntityManagerHelper.commit(getC3UserID(session));

			response.addObject(null);
			response.setAction_successfully(Boolean.TRUE);
		} catch (RuntimeException re) {
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);

			C3Logger.error("Privilege save", re);
		} finally {
//			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	private void checkDoubleLogin(PlayerSession session, GameRoom gm) {
		C3Logger.debug("C3Room.afterSessionConnect");

		// get the actual user
		UserPOJO newUser = ((C3Player) session.getPlayer()).getUser();

		C3Logger.debug("C3Room.afterSessionConnect -> search wrong session");
		if (newUser != null) {
			for (PlayerSession plSession : gm.getSessions()) {

				Long userID = ((C3Player) plSession.getPlayer()).getUser().getUserId();

				// find a other session from the user of the actual player session and send an USER_LOGOUT_AFTER_DOUBLE_LOGIN event
				if (userID.equals(newUser.getUserId()) && session != plSession) {
					C3Logger.debug("C3Room.afterSessionConnect -> find wrong session");

					GameState state_broadcast_login = new GameState(GAMESTATEMODES.USER_LOGOUT_AFTER_DOUBLE_LOGIN);
					state_broadcast_login.setReceiver(plSession.getId());

//					gm.sendBroadcast(Events.networkEvent(state_broadcast_login));

					C3GameSessionHandler.sendBroadCast(gm,state_broadcast_login);

				}
			}
		}
	}

	private void getLoggedInUserData(PlayerSession session) {
		C3Logger.debug("C3GameSessionHandler.getLoggedInUserData");
		C3Logger.debug("C3GameSessionHandler.getLoggedInUserData.seesionID: -> " + session.getId());
		C3Logger.debug("Client is ready for data (" + session + "): " + roomSession.getSessionReadyMap().get(session));
		// C3Logger.debug("Map: " + roomSession.getSessionReadyMap().toString());

		// Create a new GameState with the UserPOJO for the client, if login was successful
		UserPOJO user = ((C3Player) session.getPlayer()).getUser();

		/*( new Thread() { public void run() {
			boolean ready = false;
			int counter = 50;
			do {
				ready = roomSession.getSessionReadyMap().containsKey(session) && roomSession.getSessionReadyMap().get(session);
				if (ready || counter == 0) {
					break;
				} else {
					try {
						C3Logger.debug("Waiting a moment before sending the login result event...");
						TimeUnit.MILLISECONDS.sleep(250);
						counter--;
					} catch (InterruptedException interruptedException) {
						interruptedException.printStackTrace();
					}
				}
			} while(!ready);

			if (counter > 0) {
				C3Logger.info("Client is ready for data (" + session + "): " + roomSession.getSessionReadyMap().get(session));

				C3Logger.info("---------------------------- Sending userdata back...");
				ArrayList<UserPOJO> userlist = UserDAO.getInstance().getUserList();

				GameState state_userdata = new GameState(GAMESTATEMODES.USER_LOGGED_IN_DATA);
				state_userdata.addObject(user);
				state_userdata.addObject2(WebDataInterface.getUniverse());
				state_userdata.addObject3(userlist);
				state_userdata.setReceiver(session.getId());
				C3GameSessionHandler.sendNetworkEvent(session, state_userdata);

				C3Logger.info("Event sent: " + state.getMode());
				C3Logger.info("---------------------------- Sending userdata done.");
			} else {
				C3Logger.info("Waiting for the client to be ready timed out... client did not respond in time!");
			}
		} } ).start();*/

		C3Logger.info("Client is ready for data (" + session + "): " + roomSession.getSessionReadyMap().get(session));

		C3Logger.info("---------------------------- Sending userdata back...");
		ArrayList<UserPOJO> userlist = UserDAO.getInstance().getUserList();

		GameState state_userdata = new GameState(GAMESTATEMODES.USER_LOGGED_IN_DATA);
		state_userdata.addObject(user);
		state_userdata.addObject2(WebDataInterface.getUniverse());
		state_userdata.addObject3(userlist);
		state_userdata.setReceiver(session.getId());
		C3GameSessionHandler.sendNetworkEvent(session, state_userdata);

		C3Logger.info("Event sent: " + state.getMode());
		C3Logger.info("---------------------------- Sending userdata done.");

		// Save last login date
		UserDAO dao = UserDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.USER_SAVE);
		try {
			EntityManagerHelper.beginTransaction(C3GameSessionHandler.getC3UserID(session));
			user.setLastLogin(new Timestamp(System.currentTimeMillis()));
			dao.update(getC3UserID(session), ((C3Player) session.getPlayer()).getUser());
			EntityManagerHelper.commit(C3GameSessionHandler.getC3UserID(session));
			C3Logger.info("Last login saved for User");
		} catch (RuntimeException re) {
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);

			C3Logger.error("User save", re);
		} finally {
//			C3GameSessionHandler.sendNetworkEvent(session, response);
		}

		// Reads characterlist and add it to the user
		//ArrayList<RolePlayCharacterPOJO> characterList = RolePlayCharacterDAO.getInstance().getCharactersOfUser(user);
		//user.setCharacterList(characterList);
	}

	/**
	 * Sends a actually list of players to all clients
	 * 
	 * @param session PlayerSession
	 * @param state GameState
	 */
	private void sendNewPlayerList(PlayerSession session, GameState state) {

		//TODO: Replace UserPOJO with UserPOJO-Transferobject
		Iterator<PlayerSession> iter = room.getSessions().iterator();
		ArrayList<UserPOJO> userList = new ArrayList<>();

		while (iter.hasNext()) {
			C3Player pl = (C3Player) iter.next().getPlayer();
			userList.add(pl.getUser());
		}

		GameState state_broadcast_login = new GameState(GAMESTATEMODES.USER_GET_NEW_PLAYERLIST);
		state_broadcast_login.addObject(userList);
//		room.sendBroadcast(Events.networkEvent(state_broadcast_login));

		C3GameSessionHandler.sendBroadCast(room,state_broadcast_login );
	}

	static Long getC3UserID(PlayerSession session) {
		return (Long) session.getPlayer().getId();
	}

	static public void sendNetworkEvent(PlayerSession session, GameState response) {

		EntityConverter.convertGameStateToDTO(response);
		/* and now we send a message to the client */
		Event e = Events.networkEvent(response);
		session.onEvent(e);
	}

	static public void sendBroadCast(GameRoom gm, GameState response){
		EntityConverter.convertGameStateToDTO(response);

		gm.sendBroadcast(Events.networkEvent(response));
	}

}
