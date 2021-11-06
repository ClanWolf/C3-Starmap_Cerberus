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
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.server.process.EndRound;
import net.clanwolf.starmap.server.util.HeartBeatTimer;
import net.clanwolf.starmap.server.util.WebDataInterface;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.UniverseDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.util.Compressor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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

		// TODO: Die beiden States hier wurden nie benutzt (auch in alten Versionen der Klasse nicht)!
		//		state = (GameState) manager.getState();
		//		// Initialize the room state_login.
		//		state = new GameState();
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

		Timer serverHeartBeat;

		switch (state.getMode()) {
			case BROADCAST_SEND_NEW_PLAYERLIST:
				sendNewPlayerList();
				break;
			case USER_REQUEST_LOGGED_IN_DATA:
				getLoggedInUserData(session);
				break;
			case USER_CHECK_DOUBLE_LOGIN:
				checkDoubleLogin(session, room);
				break;
			case USER_LOG_OUT:
				session.getPlayer().logout(session);
				sendNewPlayerList();
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
			case USER_SAVE_LAST_LOGIN_DATE:
				saveUser(session, state, true, false);
				break;
			case USER_SAVE:
				saveUser(session, state);
				break;
			case PRIVILEGE_SAVE:
				savePrivileges(session, state);
				break;
			case JUMPSHIP_SAVE:
				saveJumpship(session, state);
				serverHeartBeat = new Timer();
				serverHeartBeat.schedule(new HeartBeatTimer(true), 10);
				break;
			case ATTACK_SAVE:
				saveAttack(session, state);
				serverHeartBeat = new Timer();
				serverHeartBeat.schedule(new HeartBeatTimer(true), 10);
				break;
			case ATTACK_CHARACTER_SAVE:
				saveAttackCharacter(session, state);
				serverHeartBeat = new Timer();
				serverHeartBeat.schedule(new HeartBeatTimer(true), 10);
				break;
			case ATTACK_CHARACTER_SAVE_WITHOUT_NEW_UNIVERSE:
				saveAttackCharacter(session, state);
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
				C3Logger.info("Setting 'Client is ready for data' for session: " + session.getId().toString());
				roomSession.getSessionReadyMap().put(session.getId().toString(), Boolean.TRUE);
				break;
			case FORCE_FINALIZE_ROUND:
				EndRound.setForceFinalize(true);
				serverHeartBeat = new Timer();
				serverHeartBeat.schedule(new HeartBeatTimer(true), 10);
				break;
			case FORCE_NEW_UNIVERSE:
				serverHeartBeat = new Timer();
				serverHeartBeat.schedule(new HeartBeatTimer(true), 10);
				break;
			default:
				break;
		}
	}

	private synchronized void saveUser(PlayerSession session, GameState state) {
		saveUser(session, state, false, false);
	}

	private synchronized void saveUser(PlayerSession session, GameState state, boolean updateloggedInTime, boolean modified) {
		UserDAO dao = UserDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.USER_SAVE);

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));

			UserPOJO user = (UserPOJO)state.getObject();

			if (modified) {
				user.setLastModified(new Timestamp(System.currentTimeMillis()));
			}
			if (updateloggedInTime) {
				user.setLastLogin(new Timestamp(System.currentTimeMillis()));
			}
			if(user.getUserId() == null) {
				dao.save(getC3UserID(session), state.getObject());
			} else {
				dao.update(getC3UserID(session), state.getObject());
			}

			EntityManagerHelper.commit(getC3UserID(session));

			response.addObject(null);
			response.setAction_successfully(Boolean.TRUE);
		} catch (RuntimeException re) {
			C3Logger.error("User save", re);
			re.printStackTrace();
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	private synchronized void saveAttack(PlayerSession session, GameState state) {
		AttackDAO dao = AttackDAO.getInstance();

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));

			AttackPOJO existingAttack = null;
			AttackPOJO attack = (AttackPOJO) state.getObject();
			C3Logger.debug("Saving attack: " + attack);
			C3Logger.debug("-- Attacker (jumpshipID): " + attack.getJumpshipID());
			C3Logger.debug("-- Attacking from: " + attack.getAttackedFromStarSystemID());
			C3Logger.debug("-- Attacked system: " + attack.getStarSystemID());

			if(attack.getId() != null) {
				C3Logger.debug("attack.getId() != null");
				dao.update(getC3UserID(session), attack);
			} else {
				// Check if attack exits
				existingAttack = dao.findOpenAttackByRound(getC3UserID(session),attack.getJumpshipID(), attack.getSeason(), attack.getRound());

				if(existingAttack == null){
					C3Logger.debug("SAVE: if(existingAttack == null)");
					dao.save(getC3UserID(session), attack);
				} else {
					C3Logger.debug("ELSE -> if(existingAttack == null)");
					attack = existingAttack;
				}
			}

			EntityManagerHelper.commit(getC3UserID(session));

			GameState response = new GameState(GAMESTATEMODES.ATTACK_SAVE_RESPONSE);
			response.addObject(attack);
			if(existingAttack != null) {
				response.addObject2(session.getId());
			}
			response.setAction_successfully(Boolean.TRUE);
			C3GameSessionHandler.sendBroadCast(room, response);
		} catch (RuntimeException re) {
			re.printStackTrace();
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			GameState response = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);

			C3Logger.error("Attack save", re);
		}
	}

	private synchronized void saveAttackCharacter(PlayerSession session, GameState state) {
		AttackCharacterDAO dao = AttackCharacterDAO.getInstance();

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));
			AttackCharacterPOJO attackCharacter = (AttackCharacterPOJO) state.getObject();

			if((Boolean) state.getObject2()) {
				dao.delete(getC3UserID(session), attackCharacter);
			} else {
				if (attackCharacter.getId() != null) {
					C3Logger.debug("??? updating attackcharacter (id: " + attackCharacter.getId() + ")");
					dao.update(getC3UserID(session), attackCharacter);
				} else {
					C3Logger.debug("??? saving new attackcharacter (id: " + attackCharacter.getId() + ")");
					dao.save(getC3UserID(session), attackCharacter);
				}
			}

			EntityManagerHelper.commit(getC3UserID(session));

			EntityManagerHelper.clear(getC3UserID(session));

			AttackDAO attackDAO = AttackDAO.getInstance();
			AttackPOJO attackPOJO = attackDAO.findById(getC3UserID(session), attackCharacter.getAttackID());

			AttackDAO daoAttack = AttackDAO.getInstance();
			daoAttack.refresh(C3GameSessionHandler.getC3UserID(session), attackPOJO);

			GameState response = new GameState(GAMESTATEMODES.ATTACK_CHARACTER_SAVE_RESPONSE);
			response.addObject(attackPOJO);
			response.setAction_successfully(Boolean.TRUE);
			C3GameSessionHandler.sendBroadCast(room, response);

		} catch (RuntimeException re) {
			re.printStackTrace();
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			GameState response = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);

			C3Logger.error("Attack character save", re);
		}
	}

	private synchronized void saveJumpship(PlayerSession session, GameState state) {
		JumpshipDAO daoJS = JumpshipDAO.getInstance();
		RoutePointDAO daoRP = RoutePointDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.JUMPSHIP_SAVE);

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));
			JumpshipPOJO js = (JumpshipPOJO)state.getObject();

			ArrayList<RoutePointPOJO> newRoute = new ArrayList<RoutePointPOJO>();
			newRoute.addAll(js.getRoutepointList());

			js.getRoutepointList().clear();
			js.setStarSystemHistory(newRoute.get(0).getSystemId() + "");
			daoJS.update(C3GameSessionHandler.getC3UserID(session), js);

			daoRP.deleteByJumpshipId(getC3UserID(session));

			if(newRoute.size() > 0) {
				js.setRoutepointList(newRoute);
				daoJS.update(C3GameSessionHandler.getC3UserID(session), js);
			}

			EntityManagerHelper.commit(getC3UserID(session));

			JumpshipPOJO jsHelp = daoJS.findById(C3GameSessionHandler.getC3UserID(session), js.getId());
			daoJS.refresh(C3GameSessionHandler.getC3UserID(session), jsHelp);

			// https://stackoverflow.com/questions/5832415/entitymanager-refresh
			//entityManager().flush(); // -> maybe not the best idea, better not use this
			//entityManager.clear();
			
			// https://stackoverflow.com/questions/27905148/force-hibernate-to-read-database-and-not-return-cached-entity
			//session.refresh(entity); // -> hibernate session!

		} catch (RuntimeException re) {
			C3Logger.error("Jumpship save", re);
			re.printStackTrace();
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));
			GameState errormessage = new GameState(GAMESTATEMODES.JUMPSHIP_SAVE);
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	private synchronized void savePrivileges(PlayerSession session, GameState state) {
		UserDAO dao = UserDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.PRIVILEGE_SAVE);
		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));
			ArrayList<UserPOJO> list = (ArrayList<UserPOJO>) state.getObject();
			for (UserPOJO user : list) {
				user.setLastModified(new Timestamp(System.currentTimeMillis()));
				if (user.getUserId() == null) {
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
			C3Logger.error("Privilege save", re);
			re.printStackTrace();
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	private synchronized void checkDoubleLogin(PlayerSession session, GameRoom gm) {
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

	private synchronized void getLoggedInUserData(PlayerSession session) {
		UserPOJO user = ((C3Player) session.getPlayer()).getUser();

		C3Logger.info("Sending userdata/universe back after login...");
		ArrayList<UserPOJO> userlist = UserDAO.getInstance().getUserList();

		UniverseDTO uni = WebDataInterface.getUniverse();

		byte[] myByte = Compressor.compress(uni);
		C3Logger.debug("Size of UniverseDTO: " + myByte.length + " byte.");

		GameState state_userdata = new GameState(GAMESTATEMODES.USER_LOGGED_IN_DATA);
		state_userdata.addObject(user);
		//state_userdata.addObject2(uni);
		state_userdata.addObject2(myByte);
		state_userdata.addObject3(userlist);
		state_userdata.setReceiver(session.getId());
		C3GameSessionHandler.sendNetworkEvent(session, state_userdata);

		//Object myUni = Compressor.deCompress(myByte);

		// ACHTUNG:
		// Wenn das Event hier geschickt wird, aber im Client nichts ankommt und nirgends eine Fehlermeldung
		// auftaucht, dann ist wahrscheinlich das UniverseDTO zu groß für Netty (Paketgröße 65kB).
		// Dann wird entweder das UniverseDTO immer größer, weil irgendwo ein .clear() fehlt (Mai 2021), oder
		// es sind zu viele Daten in dem Objekt, weil das Spiel an sich zu groß geworden ist.
		// Lösung:
		// - Das Universe darf nicht durch ein fehlendes clear() immer weiter wachsen!
		// - Die Daten müssen aufgeteilt werden, bis sie wieder in die Pakete passen!

		// Save last login date
		UserDAO dao = UserDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.USER_SAVE);
		try {
			EntityManagerHelper.beginTransaction(C3GameSessionHandler.getC3UserID(session));
			user.setLastLogin(new Timestamp(System.currentTimeMillis()));
			dao.update(C3GameSessionHandler.getC3UserID(session), user);
			C3Logger.info("Last login saved for User:");
			C3Logger.info("Name: " + user.getUserName());
			C3Logger.info("Timestamp: " + new Timestamp(System.currentTimeMillis()));
			C3Logger.info("--------------------");
			EntityManagerHelper.commit(C3GameSessionHandler.getC3UserID(session));
		} catch (Exception re) {
			C3Logger.error("User save", re);
			//sendErrorMessageToClient(session, re);
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	/**
	 * Sends a list of players to all clients
	 */
	private void sendNewPlayerList() {
		ArrayList<UserPOJO> userList = new ArrayList<>();
		for (PlayerSession playerSession : room.getSessions()) {
			C3Player pl = (C3Player) playerSession.getPlayer();
			userList.add(pl.getUser());
		}

		GameState state_broadcast_login = new GameState(GAMESTATEMODES.USER_GET_NEW_PLAYERLIST);
		state_broadcast_login.addObject(userList);

		C3GameSessionHandler.sendBroadCast(room, state_broadcast_login);
	}

	static Long getC3UserID(PlayerSession session) {
		return (Long) session.getPlayer().getId();
	}

	static public void sendNetworkEvent(PlayerSession session, GameState response) {
		EntityConverter.convertGameStateToDTO(response);
		Event e = Events.networkEvent(response);
		session.onEvent(e);
	}

	static public void sendBroadCast(GameRoom gm, GameState response){
		EntityConverter.convertGameStateToDTO(response);
		gm.sendBroadcast(Events.networkEvent(response));
	}

	static public void sendErrorMessageToClient(PlayerSession session, Exception re){
		EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));
		GameState gsErrorMessage = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
		gsErrorMessage.addObject(re.getMessage());
		gsErrorMessage.setAction_successfully(Boolean.FALSE);
		C3GameSessionHandler.sendNetworkEvent(session, gsErrorMessage);
	}
}
