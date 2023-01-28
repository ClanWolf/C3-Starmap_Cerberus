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

import io.nadron.app.GameRoom;
import io.nadron.app.PlayerSession;
import io.nadron.app.impl.GameRoomSession;
import io.nadron.event.Event;
import io.nadron.event.Events;
import io.nadron.event.impl.SessionMessageHandler;
import io.nadron.service.GameStateManagerService;
import net.clanwolf.client.mail.MailManager;
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.Nexus.Nexus;
import net.clanwolf.starmap.server.util.ForumDatabaseTools;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.persistence.EntityConverter;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.server.process.EndRound;
import net.clanwolf.starmap.server.util.HeartBeatTimer;
import net.clanwolf.starmap.server.util.WebDataInterface;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.util.Compressor;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.util.*;

import static net.clanwolf.starmap.constants.Constants.*;

/**
 *
 * @author Undertaker
 */
public class C3GameSessionHandler extends SessionMessageHandler {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static GameRoom staticRoom; // used for broadcast messages from places where the room is not known
	private GameRoom room;
	private GameState state;
	private GameRoomSession roomSession;

	public C3GameSessionHandler(GameRoomSession session) {
		super(session);
		staticRoom = session;
		this.room = session;
		this.roomSession = session;
		GameStateManagerService manager = room.getStateManager();

		// TODO_C3: Die beiden States hier wurden nie benutzt (auch in alten Versionen der Klasse nicht)!
		//		state = (GameState) manager.getState();
		//		// Initialize the room state_login.
		//		state = new GameState();
		state = new GameState();
		manager.setState(state); // set it back on the room
	}

	@Override
	public void onEvent(Event event) {
		logger.info("C3GameSessionHandler.onEvent");
		GameState state = null;

		if (event.getSource() instanceof GameState) {
			state = (GameState) event.getSource();
		}

		if (state != null && event.getEventContext().getSession() instanceof PlayerSession) {
			executeCommand((PlayerSession) event.getEventContext().getSession(), state);
		}
	}

	static public void sendBroadCast(GameState response){
		EntityConverter.convertGameStateToDTO(response);
		staticRoom.sendBroadcast(Events.networkEvent(response));
	}

	private void storeUserSession(PlayerSession session, String clientVersion, String ipAdressSender) {
		storeUserSession(session, clientVersion, ipAdressSender, false);
	}

	private void storeUserSession(PlayerSession session, String clientVersion, String ipAdressSender, boolean logout) {
		UserSessionDAO dao = UserSessionDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.USER_SESSION_SAVE);

		// https://www.lima-city.de/thread/mysql-tabellen-datensaetze-anzahl-begrenzen-und-alte-loeschen

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));

			UserPOJO user = ((C3Player) session.getPlayer()).getUser();
			UserSessionPOJO userSessionPOJO = dao.getUserSessionByUserId(user.getUserId());

			if (userSessionPOJO == null) {
				userSessionPOJO = new UserSessionPOJO();
				userSessionPOJO.setUserId(user.getUserId());
				if (clientVersion != null) {
					userSessionPOJO.setClientVersion(clientVersion);
				}
				userSessionPOJO.setIp(ipAdressSender);
				userSessionPOJO.setLastActivity(new Timestamp(System.currentTimeMillis()));
				if (logout) {
					userSessionPOJO.setLoginTime(new Timestamp(System.currentTimeMillis()));
					userSessionPOJO.setLogoutTime(new Timestamp(System.currentTimeMillis()));
				} else {
					userSessionPOJO.setLoginTime(new Timestamp(System.currentTimeMillis()));
					userSessionPOJO.setLogoutTime(null);
				}
				dao.save(getC3UserID(session), userSessionPOJO);
			} else {
				if (clientVersion != null) {
					userSessionPOJO.setClientVersion(clientVersion);
				}
				userSessionPOJO.setIp(ipAdressSender);
				userSessionPOJO.setLastActivity(new Timestamp(System.currentTimeMillis()));
				if (logout) {
					// leave logintime alone here
					userSessionPOJO.setLogoutTime(new Timestamp(System.currentTimeMillis()));
				} else {
					userSessionPOJO.setLoginTime(new Timestamp(System.currentTimeMillis()));
					userSessionPOJO.setLogoutTime(null);
				}
				dao.update(getC3UserID(session), userSessionPOJO);
			}
			EntityManagerHelper.commit(getC3UserID(session));

			response.addObject(null);
			response.setAction_successfully(Boolean.TRUE);
		} catch (RuntimeException re) {
			logger.error("User session save", re);
			re.printStackTrace();
			EntityManagerHelper.rollback(getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			sendNetworkEvent(session, response);
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
			logger.error("User save", re);
			re.printStackTrace();
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	private synchronized void saveCharacterStats(PlayerSession session, GameState state) {
		RolePlayCharacterStatsDAO dao = RolePlayCharacterStatsDAO.getInstance();

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));

			Long attackId = null;

			ArrayList<RolePlayCharacterStatsPOJO> list = (ArrayList<RolePlayCharacterStatsPOJO>)state.getObject();
			for (RolePlayCharacterStatsPOJO charStats : list) {
				logger.info("Saving character stats for game id: " + charStats.getMwoMatchId() + ", rpCharId: " + charStats.getRoleplayCharacterId());

				attackId = charStats.getAttackId();
				RolePlayCharacterStatsPOJO p = dao.findbyCharIdAndMatchId(charStats.getRoleplayCharacterId(), charStats.getMwoMatchId());
				if (p != null) {
					break;
				}

				if(charStats.getId() != null) {
					logger.info("rolePlayCharacterStats.getId() != null");
					dao.update(getC3UserID(session), charStats);
				} else {
					dao.save(getC3UserID(session), charStats);
				}
			}

			EntityManagerHelper.commit(getC3UserID(session));

			GameState response = new GameState(GAMESTATEMODES.CHARACTER_STATS_SAVE_RESPONSE);
			response.setAction_successfully(Boolean.TRUE);
			response.addObject(attackId);
			C3GameSessionHandler.sendBroadCast(room, response);
		} catch (RuntimeException re) {
			re.printStackTrace();
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			GameState response = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);

			logger.error("CharacterStats save", re);
		}
	}

	private synchronized void saveAttackStats(PlayerSession session, GameState state) {
		AttackStatsDAO dao = AttackStatsDAO.getInstance();

		try {
			AttackStatsPOJO attackStats = (AttackStatsPOJO) state.getObject();
			logger.info("Saving attack stats for game id: " + attackStats.getMwoMatchId());

			AttackStatsPOJO checkStats = dao.findByMatchId(attackStats.getMwoMatchId());
			if (checkStats != null) {
				// already saved, do nothing
				logger.info("Stats with the given MWO Match-ID have been detected, DO NOTHING!");
				return;
			}

			EntityManagerHelper.beginTransaction(getC3UserID(session));
			if(attackStats.getId() != null) {
				logger.info("attackStats.getId() != null");
				dao.update(getC3UserID(session), attackStats);
			} else {
				dao.save(getC3UserID(session), attackStats);
			}
			EntityManagerHelper.commit(getC3UserID(session));

			GameState response = new GameState(GAMESTATEMODES.ATTACK_STATS_SAVE_RESPONSE);
			response.setAction_successfully(Boolean.TRUE);
			response.addObject(attackStats.getAttackId());
			C3GameSessionHandler.sendBroadCast(room, response);
		} catch (RuntimeException re) {
			re.printStackTrace();
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			GameState response = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);

			logger.error("AttackStats save", re);
		}
	}

	private synchronized void saveStatsMwo(PlayerSession session, GameState state) {
		StatsMwoDAO dao = StatsMwoDAO.getInstance();

		try {
			StatsMwoPOJO statsMwo = (StatsMwoPOJO) state.getObject();
			logger.info("Saving MWO stats for game id: " + statsMwo.getGameId());

			StatsMwoPOJO checkStats = dao.findByMWOGameId(statsMwo.getGameId());
			if (checkStats != null) {
				// already saved, do nothing
				logger.info("Stats with the given MWO Match-ID have been detected, DO NOTHING!");
				return;
			}

			EntityManagerHelper.beginTransaction(getC3UserID(session));
			if (statsMwo.getId() != null) {
				logger.info("attack.getId() != null");
				dao.update(getC3UserID(session), statsMwo);
			} else {
				dao.save(getC3UserID(session), statsMwo);
			}
			EntityManagerHelper.commit(getC3UserID(session));

			GameState response = new GameState(GAMESTATEMODES.STATS_MWO_SAVE_RESPONSE);
			response.setAction_successfully(Boolean.TRUE);
			response.addObject(statsMwo.getAttackId());
			C3GameSessionHandler.sendBroadCast(room, response);
		} catch (RuntimeException re) {
			re.printStackTrace();
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			GameState response = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);

			logger.error("Stats MWO save", re);
		}
	}

	private synchronized void saveAttack(PlayerSession session, GameState state) {
		AttackDAO dao = AttackDAO.getInstance();
		AttackCharacterDAO daoAC = AttackCharacterDAO.getInstance();
		StarSystemDataDAO daoSS = StarSystemDataDAO.getInstance();
		JumpshipDAO daoJJ = JumpshipDAO.getInstance();
		Long attackType = null;

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));

			AttackPOJO existingAttack = null;
			AttackPOJO attack = (AttackPOJO) state.getObject();

			logger.info("Saving attack: " + attack);
			logger.info("-- Attacker (jumpshipID): " + attack.getJumpshipID());
			logger.info("-- Attacking from: " + attack.getAttackedFromStarSystemID());
			logger.info("-- Attacked system: " + attack.getStarSystemID());

			StarSystemDataPOJO s = StarSystemDataDAO.getInstance().findById(getC3UserID(session), attack.getStarSystemDataID());
			StarSystemPOJO starSystem = StarSystemDAO.getInstance().findById(getC3UserID(session), attack.getStarSystemID());
			s.setLockedUntilRound(attack.getRound() + Constants.ROUNDS_TO_LOCK_SYSTEM_AFTER_ATTACK);
			daoSS.update(getC3UserID(session), s);

			Long acpId = -1L;
			if (state.getObject3() != null) {
				acpId = ((AttackCharacterPOJO) state.getObject3()).getId();
			}

			ArrayList<AttackCharacterPOJO> newAttackCharacters = new ArrayList<>();
			if( attack.getAttackCharList() != null) {
				for (AttackCharacterPOJO p : attack.getAttackCharList()) {
					if (!Objects.equals(p.getId(), acpId)) {
						newAttackCharacters.add(p);
					}
				}
				attack.getAttackCharList().clear();
			}

			// -1L: Clan vs IS
			// -2L: Clan vs Clan
			// -3L: IS vs Clan
			// -4L: IS vs IS
			attackType = (Long) state.getObject2();

			RolePlayStoryPOJO rpPojo = null;
			Long attackerCommanderNextStoryId = null;
			Long defenderCommanderNextStoryId = null;
			if(attack.getStoryID() != null) {
				for (AttackCharacterPOJO ac : newAttackCharacters) {
					if (ac.getType().equals(ROLE_ATTACKER_COMMANDER)) {
						attackerCommanderNextStoryId = ac.getNextStoryId();
					}
					if (ac.getType().equals(ROLE_DEFENDER_COMMANDER)) {
						defenderCommanderNextStoryId = ac.getNextStoryId();
					}
				}
				if (attackerCommanderNextStoryId != null &&	attackerCommanderNextStoryId.equals(defenderCommanderNextStoryId)) {
					rpPojo = RolePlayStoryDAO.getInstance().findById(getC3UserID(session), attackerCommanderNextStoryId);

					if(rpPojo.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STEP_V1) {

						JumpshipPOJO jpWinner = JumpshipDAO.getInstance().findById(getC3UserID(session),attack.getJumpshipID());
						long unitXP = 0;

						if( rpPojo.getAttackerWins()){
							attack.setFactionID_Winner(jpWinner.getJumpshipFactionID());

							unitXP = Constants.JUMPSHIP_XP_ATTACK_VICTORY;

						} else if ( rpPojo.getDefenderWins()){
							attack.setFactionID_Winner(attack.getFactionID_Defender());

							unitXP = Constants.JUMPSHIP_XP_ATTACK_DEFEAT;
						}

						jpWinner.setUnitXP(jpWinner.getUnitXP() + unitXP);
						daoJJ.save(getC3UserID(session), jpWinner);
					}
				} else {
					rpPojo = RolePlayStoryDAO.getInstance().findById(getC3UserID(session), attack.getStoryID());
				}
				attack.setStoryID(rpPojo.getId());
			} else {
				AttackTypesPOJO at = AttackTypesDAO.getInstance().findByShortName(getC3UserID(session), "PA");
				Long rpID = null;
				if (attackType.equals(-1L)) {
					String ids = at.getCLAN_IS_StoryIds();
					String[] s1 = ids.split(";");
					Integer num = (int) (Math.random() * s1.length);
					if ("".equals(s1[num])) { logger.info("!!!!!!!!!!!!!!!!! Missing story-ID in _HH_ATTACK_TYPE for Attack Type CLAN vs IS"); }
					rpID = Long.parseLong(s1[num]);
				} else if (attackType.equals(-2L)) {
					String ids = at.getCLAN_vs_CLAN_StoryIds();
					String[] s1 = ids.split(";");
					Integer num = (int) (Math.random() * s1.length);
					if ("".equals(s1[num])) { logger.info("!!!!!!!!!!!!!!!!! Missing story-ID in _HH_ATTACK_TYPE for Attack Type CLAN vs CLAN"); }
					rpID = Long.parseLong(s1[num]);
				} else if (attackType.equals(-3L)) {
					String ids = at.getIS_vs_CLAN_StoryIds();
					String[] s1 = ids.split(";");
					Integer num = (int) (Math.random() * s1.length);
					if ("".equals(s1[num])) { logger.info("!!!!!!!!!!!!!!!!! Missing story-ID in _HH_ATTACK_TYPE for Attack Type IS vs CLAN"); }
					rpID = Long.parseLong(s1[num]);
				} else if (attackType.equals(-4L)) {
					String ids = at.getIS_vs_IS_StoryIds();
					String[] s1 = ids.split(";");
					Integer num = (int) (Math.random() * s1.length);
					if ("".equals(s1[num])) { logger.info("!!!!!!!!!!!!!!!!! Missing story-ID in _HH_ATTACK_TYPE for Attack Type IS vs IS"); }
					rpID = Long.parseLong(s1[num]);
				}

				rpPojo = RolePlayStoryDAO.getInstance().findById(getC3UserID(session), rpID);
			}
			attack.setStoryID(rpPojo.getId());

			if(attack.getId() != null) {
				logger.info("attack.getId() != null");
				dao.update(getC3UserID(session), attack);
			} else {
				// Check if attack exits
				//existingAttack = dao.findOpenAttackByRound(getC3UserID(session),attack.getJumpshipID(), attack.getSeason(), attack.getRound());
				existingAttack = dao.findOpenAttackByRound(getC3UserID(session),attack.getJumpshipID(), attack.getSeason(), attack.getRound());

				if(existingAttack == null){
					logger.info("SAVE: if(existingAttack == null)");
					dao.save(getC3UserID(session), attack);
				} else {
					logger.info("ELSE -> if(existingAttack == null)");
					attack = existingAttack;
				}
			}

			// remove old and set new attack character
			daoAC.deleteByAttackId(getC3UserID(session));
			if(newAttackCharacters.size() > 0) {
				attack.setAttackCharList(newAttackCharacters);
			}
			dao.update(getC3UserID(session), attack);

			EntityManagerHelper.commit(getC3UserID(session));

			attack = dao.findById(getC3UserID(session), attack.getId());
			dao.refresh(getC3UserID(session), attack);

//			JumpshipPOJO jsHelp =daoJJ.findById(getC3UserID(session), attack.getJumpshipID());
//			daoJJ.refresh(getC3UserID(session), jsHelp);

			// Create forum thread for this attack
			ForumDatabaseTools t = new ForumDatabaseTools();
			long season = attack.getSeason();
			long round = attack.getRound();

			// -1L: Clan vs IS
			// -2L: Clan vs Clan
			// -3L: IS vs Clan
			// -4L: IS vs IS
			JumpshipPOJO attackerJumpship = JumpshipDAO.getInstance().findById(getC3UserID(session), attack.getJumpshipID());
			FactionPOJO defender = FactionDAO.getInstance().findById(getC3UserID(session), attack.getFactionID_Defender());
			FactionPOJO attacker = FactionDAO.getInstance().findById(getC3UserID(session), attackerJumpship.getJumpshipFactionID());
			RolePlayCharacterPOJO rpChar = RolePlayCharacterDAO.getInstance().findById(getC3UserID(session), attack.getCharacterID());

			String[] dropships = attackerJumpship.getDropshipNames().split(",");
			int rnd = new Random().nextInt(dropships.length);
			String dropshipName = dropships[rnd].trim();

			// nur einmal eintragen, nicht bei jedem Speichern
			String rank = rpChar.getRank();
			if (rank == null || "null".equalsIgnoreCase(rank)) {
				rank = "[kein Rang]";
			}
			if (attack.getForumThreadId() == null) {
				logger.info("Inserting new invasion thread");
				t.createNewAttackEntries(season,
					round,
					starSystem.getName(),
					attacker.getShortName(),
					defender.getShortName(),
					attackType, attack.getId(),
					starSystem.getSystemImageName(),
					rank,
					rpChar.getName(),
					attackerJumpship.getUnitName(),
					dropshipName
				);
			} else {
				if (attack.getFactionID_Winner() != null) {
					logger.info("Finalizing invasion thread");
					// Enter final post into attack thread in Forum
					// Only if the RP thread is not closed yet
					// and the close the thread
					FactionPOJO winner = FactionDAO.getInstance().findById(getC3UserID(session), attack.getFactionID_Winner());
					t.createFinalizingEntryForAttack(attack.getId(),
							season,
							round,
							starSystem.getName(),
							attacker.getShortName(),
							winner.getShortName(),
							false);
				}
			}

			GameState response = new GameState(GAMESTATEMODES.ATTACK_SAVE_RESPONSE);
			response.addObject(attack);
			if(existingAttack != null) {
				response.addObject2(session.getId());
			}
			response.addObject3(rpPojo);

			response.setAction_successfully(Boolean.TRUE);
			C3GameSessionHandler.sendBroadCast(room, response);
		} catch (RuntimeException re) {
			re.printStackTrace();
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			GameState response = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);

			logger.error("Attack save", re);
		}
	}

	/*private synchronized void saveAttackCharacter(PlayerSession session, GameState state) {
		AttackCharacterDAO dao = AttackCharacterDAO.getInstance();

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));
			AttackCharacterPOJO attackCharacter = (AttackCharacterPOJO) state.getObject();

			if((Boolean) state.getObject2()) {
				dao.delete(getC3UserID(session), attackCharacter);
			} else {
				if (attackCharacter.getId() != null) {
					logger.info("??? updating attackcharacter (id: " + attackCharacter.getId() + ")");
					dao.update(getC3UserID(session), attackCharacter);
				} else {
					logger.info("??? saving new attackcharacter (id: " + attackCharacter.getId() + ")");
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

			logger.error("Attack character save", re);
		}
	}*/

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
			logger.error("Jumpship save", re);
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
					// logger.info("Saving: " + user.getUserName() + " - Privs: " + user.getPrivileges());
					dao.save(getC3UserID(session), user);
				} else {
					// logger.info("Updating: " + user.getUserName() + " - Privs: " + user.getPrivileges());
					dao.update(getC3UserID(session), user);
				}
			}

			EntityManagerHelper.commit(getC3UserID(session));

			response.addObject(null);
			response.setAction_successfully(Boolean.TRUE);
		} catch (RuntimeException re) {
			logger.error("Privilege save", re);
			re.printStackTrace();
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	private synchronized void checkDoubleLogin(PlayerSession session, GameRoom gm) {
		logger.info("C3Room.afterSessionConnect");

		// get the actual user
		UserPOJO newUser = ((C3Player) session.getPlayer()).getUser();

		logger.info("C3Room.afterSessionConnect -> search wrong session");
		if (newUser != null) {
			for (PlayerSession plSession : gm.getSessions()) {

				Long userID = ((C3Player) plSession.getPlayer()).getUser().getUserId();

				// find a other session from the user of the actual player session and send an USER_LOGOUT_AFTER_DOUBLE_LOGIN event
				if (userID.equals(newUser.getUserId()) && session != plSession) {
					logger.info("C3Room.afterSessionConnect -> find wrong session");

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

		logger.info("Sending userdata/universe back after login...");
		ArrayList<UserPOJO> userlist = UserDAO.getInstance().getUserList();

		UniverseDTO uni = WebDataInterface.getUniverse();

		byte[] myByte = Compressor.compress(uni);
		logger.info("Size of UniverseDTO: " + myByte.length + " byte.");

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
			logger.info("Last login saved for User:");
			logger.info("Name: " + user.getUserName());
			logger.info("Timestamp: " + new Timestamp(System.currentTimeMillis()));
			logger.info("--------------------");
			if( !GameServer.isDevelopmentPC) {

				boolean sent = false;
				String[] receivers = {"keshik@googlegroups.com"};
				sent = MailManager.sendMail("c3@clanwolf.net", receivers, user.getUserName() + " logged into C3 client", "User logged into C3 client.", false);
				if (sent) {
					// sent
					logger.info("User logged in information mail sent. [4]");
				} else {
					// error during email sending
					logger.info("Error during mail dispatch. [4]");
				}
			}
			logger.info("--------------------");
			EntityManagerHelper.commit(C3GameSessionHandler.getC3UserID(session));
		} catch (Exception re) {
			logger.error("User save", re);
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
	private synchronized void sendNewPlayerList(PlayerSession session) {
		ArrayList<UserPOJO> userList = new ArrayList<>();
		ArrayList<Long> userIdList = new ArrayList<>();
		for (PlayerSession playerSession : room.getSessions()) {
			C3Player pl = (C3Player) playerSession.getPlayer();
			userList.add(pl.getUser());
			userIdList.add(pl.getUser().getUserId());
		}

		checkAttackerDropleaderIsOffline(session, userList, userIdList);

		GameState state_broadcast_login = new GameState(GAMESTATEMODES.USER_GET_NEW_PLAYERLIST);
		state_broadcast_login.addObject(userList);

		C3GameSessionHandler.sendBroadCast(room, state_broadcast_login);
	}

	private synchronized void checkAttackerDropleaderIsOffline(PlayerSession session, ArrayList<UserPOJO> userList, ArrayList<Long> userIdList) {

		// TODO_C3: missing droplead, kill lobby
		// Ist bei den Usern einer dabei, der gerade in einem Kampf Droplead/Attacker ist?

		AttackDAO attackDAO = AttackDAO.getInstance();
		ArrayList<AttackPOJO> openAttacks = attackDAO.getOpenAttacksOfASeason(Nexus.currentSeason);

		for (AttackPOJO ap : openAttacks) {
			AttackCharacterDAO dao = AttackCharacterDAO.getInstance();
			ArrayList<AttackCharacterPOJO> acpl = dao.getCharactersFromAttack(ap.getId());
			for (AttackCharacterPOJO acp : acpl) {
				if (acp.getType().equals(Constants.ROLE_ATTACKER_COMMANDER)) {
					RolePlayCharacterDAO rpDAO = RolePlayCharacterDAO.getInstance();
					RolePlayCharacterPOJO character = rpDAO.findById(getC3UserID(session), acp.getCharacterID());
					UserPOJO user = character.getUser();

					boolean userContainedInList = false;
					for (Long l : userIdList) {
						if (Objects.equals(l, user.getUserId())) {
							userContainedInList = true;
							break;
						}
					}

					if (!userContainedInList) {
						logger.info("Lobby owner (" + user.getUserName() + ") is offline. Attacker commander left!");
						logger.info(user.getUserName() + " has role " + acp.getType());

						acp.setType(null);

						GameState s = new GameState();
						s.addObject(ap);
						s.addObject2(ap.getAttackTypeID());
						s.addObject3(acp);

						saveAttack(session, s);
					}
				}
			}
		}
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

	private void executeCommand(PlayerSession session, GameState state) {
		logger.info("C3GameSessionHandler.executeCommand: " + state.getMode().toString());
		EntityConverter.convertGameStateToPOJO(state);

		Timer serverHeartBeat;
		String ipAdressSender = state.getIpAdressSender();

		switch (state.getMode()) {
			case BROADCAST_SEND_NEW_PLAYERLIST:
				sendNewPlayerList(session);
				break;
			case USER_REQUEST_LOGGED_IN_DATA:
				String clientVersion = "-";
				if (state.getObject() instanceof String) {
					clientVersion = (String) state.getObject();
				}
				storeUserSession(session, clientVersion, ipAdressSender);
				getLoggedInUserData(session);
				break;
			case USER_CHECK_DOUBLE_LOGIN:
				checkDoubleLogin(session, room);
				break;
			case USER_LOG_OUT:
				session.getPlayer().logout(session);
				storeUserSession(session, null, ipAdressSender, true);
				sendNewPlayerList(session);
				break;
			case ROLEPLAY_SAVE_STORY:
				C3GameSessionHandlerRoleplay.saveRolePlayStory(session, state);
				break;
			case ROLEPLAY_REQUEST_ALLSTORIES:
			case ROLEPLAY_REQUEST_STEPSBYSTORY:
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
				serverHeartBeat.schedule(new HeartBeatTimer(true), 1000);
				break;
			case STATS_MWO_SAVE:
				saveStatsMwo(session, state);
				break;
			case CHARACTER_STATS_SAVE:
				saveCharacterStats(session, state);
				break;
			case ATTACK_STATS_SAVE:
				saveAttackStats(session, state);
				break;
			case ATTACK_CHARACTER_SAVE:
				//saveAttackCharacter(session, state);
				//serverHeartBeat = new Timer();
				//serverHeartBeat.schedule(new HeartBeatTimer(true), 10);
				break;
			case ATTACK_CHARACTER_SAVE_WITHOUT_NEW_UNIVERSE:
				//saveAttackCharacter(session, state);
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
				logger.info("Setting 'Client is ready for data' for session: " + session.getId().toString());
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

	static public void sendErrorMessageToClient(PlayerSession session, Exception re){
		EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));
		GameState gsErrorMessage = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
		gsErrorMessage.addObject(re.getMessage());
		gsErrorMessage.setAction_successfully(Boolean.FALSE);
		C3GameSessionHandler.sendNetworkEvent(session, gsErrorMessage);
	}
}
