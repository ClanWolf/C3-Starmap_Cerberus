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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
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
import net.clanwolf.starmap.constants.Constants;
import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.persistence.EntityConverter;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.server.process.EndRound;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import net.clanwolf.starmap.server.timertasks.HeartBeatTimerTask;
import net.clanwolf.starmap.server.util.ForumDatabaseTools;
import net.clanwolf.starmap.server.util.WebDataInterface;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.UniverseDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;
import net.clanwolf.starmap.transfer.util.Compressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;

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

		logger.info("Creating NEW C3GameSessionHandler. This should not happen, there should be only one at a time!");

		staticRoom = session;
		this.room = session;
		this.roomSession = session;
		GameStateManagerService manager = room.getStateManager();

		// TODO_C3: Dieser State hier wurde nie benutzt (auch in alten Versionen der Klasse nicht)!
		// state = (GameState) manager.getState();

		// Initialize the room state_login.
		state = new GameState();
		manager.setState(state); // set it back on the room

		ServerNexus.gmSessionHandler = this;
	}

	@Override
	public void onEvent(Event event) {
//		logger.info("C3GameSessionHandler.onEvent");
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
		RolePlayCharacterStatsDAO daoRPCStats = RolePlayCharacterStatsDAO.getInstance();
		AttackStatsDAO daoAttackStats = AttackStatsDAO.getInstance();

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
			boolean gameRepeated = false;
			StatsMwoPOJO checkStats2 = dao.findByAttackIdAndRoleplayID(statsMwo.getAttackId(), statsMwo.getRoleplayId());
			if (checkStats2 != null) {
				// The drop was repeated here, so we update the mwo stats entry, and DELETE both other statistics
				// They will be written again by the following methods
				ArrayList<RolePlayCharacterStatsPOJO> rpcStatsList = daoRPCStats.findByMatchId(checkStats2.getGameId());
				for (RolePlayCharacterStatsPOJO p : rpcStatsList) {
					daoRPCStats.delete(getC3UserID(session), p);
				}

				// The drop was repeated here, so we update the mwo stats entry, and DELETE both other statistics
				// They will be written again by the following methods
				AttackStatsPOJO attackStats = daoAttackStats.findByMatchId(checkStats2.getGameId());
				daoAttackStats.delete(getC3UserID(session), attackStats);

				checkStats2.setGameId(statsMwo.getGameId());
				checkStats2.setRawData(statsMwo.getRawData());
				statsMwo = checkStats2;
				gameRepeated = true;
			}

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
			response.addObject2(gameRepeated);
			C3GameSessionHandler.sendBroadCast(room, response);
		} catch (RuntimeException re) {
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			GameState response = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);

			logger.error("Stats MWO save", re);
		}
	}

	public synchronized void resetAttack(Long attackId) {
		try {
			EntityManagerHelper.beginTransaction(ServerNexus.DUMMY_USERID);
			EntityManagerHelper.clear(ServerNexus.DUMMY_USERID);

			// Remove all characters from attack and reset the "fights started" flag
			RolePlayStoryDAO rpsdao = RolePlayStoryDAO.getInstance();

			AttackDAO adao = AttackDAO.getInstance();
			AttackPOJO ap = adao.findById(ServerNexus.DUMMY_USERID, attackId);

			RolePlayStoryPOJO lobbyRPreset = rpsdao.getLobbyRPFromAttackRP(ap.getStoryID());

			ap.setFightsStarted(false);
			ap.getAttackCharList().clear();
			ap.setStoryID(lobbyRPreset.getId());
			ap.setLastStoryID(null);

			adao.update(ServerNexus.DUMMY_USERID, ap);

			EntityManagerHelper.commit(ServerNexus.DUMMY_USERID);

			// Send the reset attack to the clients
			GameState response = new GameState(GAMESTATEMODES.ATTACK_SAVE_RESPONSE);
			response.addObject(ap);
			response.addObject3(lobbyRPreset);

			response.setAction_successfully(Boolean.TRUE);
			C3GameSessionHandler.sendBroadCast(room, response);
		} catch (Exception e) {
			logger.error("Error reseting attack", e);
		}
	}

	public synchronized void saveDiplomacy(PlayerSession session, GameState state) {
		if (state.getObject() instanceof ArrayList<?>) {
			ArrayList<Long> factionsThePlayerIsNowFriendlyWithList = (ArrayList<Long>) state.getObject();
			Long factionID = (Long)state.getObject2();

			RoundPOJO currentRound = RoundDAO.getInstance().findBySeasonId(GameServer.getCurrentSeason());
			ArrayList<DiplomacyPOJO> entriesToDelete = DiplomacyDAO.getInstance().getAllRequestsForFactions(GameServer.getCurrentSeason(), factionID);
			ArrayList<DiplomacyPOJO> entriesToDeletedNextRound = new ArrayList<>();

			try {
				EntityManagerHelper.beginTransaction(getC3UserID(session));

				for (DiplomacyPOJO d1 : entriesToDelete) {
					boolean entryFound = false;
					for (Long fid2 : factionsThePlayerIsNowFriendlyWithList) {
						if (Objects.equals(d1.getFactionID_ACCEPTED(), fid2)) {
							entryFound = true;
							break;
						}
					}
					if (!entryFound) {
						// d1 needs to get a endRoundValue of cRound + 1
						if (d1.getStartingInRound() < currentRound.getRound().intValue() + 1) {
							// This is not stored if it has a starting roundnumber for next round
							d1.setEndingInRound(currentRound.getRound().intValue() + 1);
							DiplomacyDAO.getInstance().update(getC3UserID(session), d1);
							entriesToDeletedNextRound.add(d1);
						}
					}
				}

				entriesToDelete.removeAll(entriesToDeletedNextRound);

				// remove all current entries for the player faction
				// but keep all the entries the requesting faction was already allied with
				for (DiplomacyPOJO dipPojo : entriesToDelete) {
					if (!factionsThePlayerIsNowFriendlyWithList.contains(dipPojo.getFactionID_ACCEPTED())) {
						DiplomacyDAO.getInstance().delete(getC3UserID(session), dipPojo);
					} else {
						ArrayList<Long> cleanedList = new ArrayList<>();
						for (Long l : factionsThePlayerIsNowFriendlyWithList) {
							if (l.equals(dipPojo.getFactionID_ACCEPTED())) {
								// Do not add this one to be removed, because we were already allied
								// So, this one will just stay in the list with the old round number
								logger.info("Faction " + l + " was already allied. Leaving it alone.");
							} else {
								cleanedList.add(l);
							}
						}
						factionsThePlayerIsNowFriendlyWithList = cleanedList;
					}
				}

				for (Long l : factionsThePlayerIsNowFriendlyWithList) {
					logger.info("Saving Diplomacy state for faction " + l);

					DiplomacyPOJO dipPojo = new DiplomacyPOJO();
					dipPojo.setFactionID_REQUEST(factionID);
					dipPojo.setFactionID_ACCEPTED(l);
					dipPojo.setSeasonID(ServerNexus.currentSeason);
					dipPojo.setStartingInRound(currentRound.getRound().intValue() + 1);
					dipPojo.setEndingInRound(null);

					DiplomacyDAO.getInstance().update(getC3UserID(session), dipPojo);
				}

				EntityManagerHelper.commit(getC3UserID(session));

				GameState response = new GameState(GAMESTATEMODES.DIPLOMACY_SAVE_RESPONSE);
				response.setAction_successfully(Boolean.TRUE);
				response.addObject(DiplomacyDAO.getInstance().getDiplomacyForSeason(ServerNexus.currentSeason));


				C3GameSessionHandler.sendBroadCast(room, response);

			} catch (RuntimeException re) {
				EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

				GameState response = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
				response.addObject(re.getMessage());
				response.setAction_successfully(Boolean.FALSE);
				C3GameSessionHandler.sendNetworkEvent(session, response);

				logger.error("Diplomacy save", re);
			}
		}
	}

	public synchronized void saveAttack(PlayerSession session, GameState state) {
		saveAttack(session, state, false);
	}

	// https://stackoverflow.com/questions/27777254/synchronized-method-does-not-work-as-expected
	public synchronized void saveAttack(PlayerSession session, GameState state, boolean storeAttackCharacters) {
		Long sessionId = getC3UserID(session);

		logger.info("SaveAttack was called by: {}", session.getPlayer().getName());
		logger.info("GameSessionHandler-Instance: " + this.toString());

		AttackDAO dao = AttackDAO.getInstance();
		AttackCharacterDAO daoAC = AttackCharacterDAO.getInstance();
		FactionDAO daoFaction = FactionDAO.getInstance();
		StarSystemDataDAO daoSS = StarSystemDataDAO.getInstance();
		JumpshipDAO daoJJ = JumpshipDAO.getInstance();
		Long attackType = null;

		try {
			EntityManagerHelper.beginTransaction(sessionId);

			AttackPOJO existingAttack = null;
			AttackPOJO attack = (AttackPOJO) state.getObject();

			StarSystemDataPOJO starsystemData = StarSystemDataDAO.getInstance().findById(sessionId, attack.getStarSystemDataID());
			StarSystemPOJO starSystem = StarSystemDAO.getInstance().findById(sessionId, attack.getStarSystemID());
			starsystemData.setLockedUntilRound(attack.getRound() + Constants.ROUNDS_TO_LOCK_SYSTEM_AFTER_ATTACK);
			daoSS.update(sessionId, starsystemData);

			FactionPOJO originalFaction = starsystemData.getFaction();

			logger.info("Saving attack: " + attack);
			logger.info("-- Attacker (jumpshipID): " + attack.getJumpshipID());
			logger.info("-- Attacking from: " + attack.getAttackedFromStarSystemID());
			logger.info("-- Attacked system: " + attack.getStarSystemID() + " / " + starSystem.getName());
			logger.info("-- System used to belong to: " + originalFaction.getShortName());

			Long acpId = -1L;
			if (state.getObject3() != null) {
				acpId = ((AttackCharacterPOJO) state.getObject3()).getId();
			}

			if (!attack.getFightsStarted()) {
				checkAttackForMissingDropleads(session, attack);
			}

			ArrayList<AttackCharacterPOJO> newAttackCharacters = new ArrayList<>();
//			if (storeAttackCharacters) {
				if (attack.getAttackCharList() != null) {
					for (AttackCharacterPOJO p : attack.getAttackCharList()) {
						if (!p.getType().equals(ROLE_DROPLEAD_LEFT)) {
							//AttackCharacterPOJO pnew = (AttackCharacterPOJO) EntityConverter.cloneCopyPojoEmptyId(p);
							newAttackCharacters.add(p);
						}
					}
					attack.getAttackCharList().clear();
				}
//			}
//			else {
//				newAttackCharacters = attack.getAttackCharList();
//			}

			// -1L: Clan vs IS
			// -2L: Clan vs Clan
			// -3L: IS vs Clan
			// -4L: IS vs IS
			attackType = (Long) state.getObject2();

			RolePlayStoryPOJO rpPojo = null;
			RolePlayStoryPOJO rpLastPojo = null;
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
					rpPojo = RolePlayStoryDAO.getInstance().findById(sessionId, attackerCommanderNextStoryId);

					if(rpPojo.getVariante() == ROLEPLAYENTRYTYPES.RP_SECTION) {

						JumpshipPOJO jpWinner = JumpshipDAO.getInstance().findById(sessionId, attack.getJumpshipID());
						long unitXP = 0;

						if (rpPojo.getAttackerWins()) {
							attack.setFactionID_Winner(jpWinner.getJumpshipFactionID());

							attack.setScoreAttackerVictories(3L);

							String planet = starSystem.getName();
							FactionPOJO winnerFaction = daoFaction.findById(sessionId, jpWinner.getJumpshipFactionID());
							ServerNexus.getEci().sendExtCom("Invasion of " + planet + " has been decided. " + winnerFaction.getShortName() + " conquered the system!", "en", true, true, true);
							ServerNexus.getEci().sendExtCom("Angriff auf " + planet + " wurde entschieden. " + winnerFaction.getShortName() + " hat das System erobert!", "de", true, true, true);

							unitXP = Constants.JUMPSHIP_XP_ATTACK_VICTORY;

						} else if (rpPojo.getDefenderWins()) {
							attack.setFactionID_Winner(attack.getFactionID_Defender());

							attack.setScoreDefenderVictories(3L);

							String planet = starSystem.getName();
							FactionPOJO winnerFaction = daoFaction.findById(sessionId, attack.getFactionID_Defender());
							ServerNexus.getEci().sendExtCom("Invasion of " + planet + " has been decided. " + winnerFaction.getShortName() + " has defended the system!", "en", true, true, true);
							ServerNexus.getEci().sendExtCom("Angriff auf " + planet + " wurde entschieden. " + winnerFaction.getShortName() + " hat das System verteidigt!", "de", true, true, true);

							unitXP = Constants.JUMPSHIP_XP_ATTACK_DEFEAT;
						}

						jpWinner.setUnitXP(jpWinner.getUnitXP() + unitXP);
						daoJJ.save(sessionId, jpWinner);
					}
				} else {
					rpPojo = RolePlayStoryDAO.getInstance().findById(sessionId, attack.getStoryID());
				}
				if(rpPojo.getVariante() != ROLEPLAYENTRYTYPES.RP_PREPARE_BATTLE) {
					attack.setLastStoryID(rpPojo.getId());
				}
				if(rpPojo.getVariante() == ROLEPLAYENTRYTYPES.RP_INVASION) {
					attack.setScoreAttackerVictories(rpPojo.getVar9ID().getAttackerDropVictories().longValue());
					attack.setScoreDefenderVictories(rpPojo.getVar9ID().getDefenderDropVictories().longValue());
				}
				attack.setStoryID(rpPojo.getId());
			} else {
				Long rpID = null;
				String[] storyIDs = new String[1];
				String[] storyIdPartsGame;
				String storyIdsString = "";
				AttackTypesPOJO at = AttackTypesDAO.getInstance().findByShortName(sessionId, "PA");

				storyIdsString = switch ((int) attackType.longValue()) {
					case -1 -> at.getCLAN_IS_StoryIds();
					case -2 -> at.getCLAN_vs_CLAN_StoryIds();
					case -3 -> at.getIS_vs_CLAN_StoryIds();
					case -4 -> at.getIS_vs_IS_StoryIds();
					default -> storyIdsString;
				};

				storyIdPartsGame = storyIdsString.split("#");
				for (String p : storyIdPartsGame) {
					if (p.startsWith("MWO:") && (attack.getAttackGame() != null && attack.getAttackGame().equals("MWO"))) {
						String c = p.replace("MWO:", "");
						storyIDs = c.split(";");
					} else if (p.startsWith("TT:") && (attack.getAttackGame() != null && attack.getAttackGame().equals("TT"))) {
						String c = p.replace("TT:", "");
						storyIDs  = c.split(";");
					}
				}
				int num = (int) (Math.random() * storyIDs.length);
				if ("".equals(storyIDs[num])) {
					logger.info("!!!!!!!!!!!!!!!!! Missing story-ID in _HH_ATTACK_TYPE for Attack Type IS vs IS");
				}
				if (storyIDs[num] != null) {
					rpID = Long.parseLong(storyIDs[num]);
					logger.info("++++++++++++++++++++++++++++++++++++ TT StoryId: " + rpID + " (" + storyIdsString + ")");

					rpPojo = RolePlayStoryDAO.getInstance().findById(sessionId, rpID);
				}
			}
			if (rpPojo != null) {
				if (rpPojo.getVariante() != ROLEPLAYENTRYTYPES.RP_PREPARE_BATTLE) {
					attack.setLastStoryID(rpPojo.getId());
				}
				attack.setStoryID(rpPojo.getId());
			}

			if(attack.getId() != null) {
				dao.update(sessionId, attack);
			} else {
				// Check if attack exits
				existingAttack = dao.findOpenAttackByRound(sessionId,attack.getJumpshipID(), attack.getSeason(), attack.getRound());

				if(existingAttack == null) {
					JumpshipPOJO js = daoJJ.findById(sessionId, attack.getJumpshipID());
					FactionPOJO factionAttacker = FactionDAO.getInstance().findById(sessionId, js.getJumpshipFactionID());
					FactionPOJO factionDefender = FactionDAO.getInstance().findById(sessionId, starsystemData.getFaction().getId());
					ServerNexus.getEci().sendExtCom(starSystem.getName() + " is attacked by '" + js.getJumpshipName() + "' (" + factionAttacker.getShortName() + ") in round " + attack.getRound() + "!", "en",true, true, true);
					ServerNexus.getEci().sendExtCom(starSystem.getName() + " wird in Runde " + attack.getRound() + " von '" + js.getJumpshipName() + "' (" + factionAttacker.getShortName() + ") angegriffen!", "de",true, true, true);

					dao.save(sessionId, attack);

					String command = "@@@DISCORD-CMD:CREATE_ATTACK_THREAD";
					command += "@@@" + attack.getSeason();
					command += "@@@" + attack.getRound();
					command += "@@@" + starSystem.getName();
					command += "@@@" + factionAttacker.getShortName();
					command += "@@@" + factionDefender.getShortName();
					command += "@@@" + attack.getId();
					ServerNexus.getEci().sendExtCom(command, "en", false, false, true);
					ServerNexus.getEci().sendExtCom(command, "de", false, false, true);

					DiplomacyDAO.getInstance().removeAllRequestAfterAttack(sessionId,GameServer.getCurrentSeason(),attack.getRound(),factionAttacker.getId(), starsystemData.getFaction().getId());
				} else {
					attack = existingAttack;
				}
			}

			// and set new attack character
			// remove old will be done in finalize round to avoid hibernate cache issues
			// REMOVED and moved to finalize round: daoAC.deleteByAttackId(sessionId);
			if(!newAttackCharacters.isEmpty()) {
				attack.setAttackCharList(newAttackCharacters);
			}
			dao.update(sessionId, attack);

			EntityManagerHelper.commit(sessionId);

			attack = dao.findById(sessionId, attack.getId());
			dao.refresh(sessionId, attack);

//			JumpshipPOJO jsHelp =daoJJ.findById(sessionId, attack.getJumpshipID());
//			daoJJ.refresh(sessionId, jsHelp );

			RoundPOJO currentRound = RoundDAO.getInstance().findBySeasonId(GameServer.getCurrentSeason());
			long currentRoundId = currentRound.getRound();
			if (attack.getRound().equals(currentRoundId)) {
				boolean lobbyOpened = false;
				boolean invasionFinished = false;
				boolean foundDropleadAttacker = false;
				boolean foundDropleadDefender = false;
				int numberOfPilots = 0;
				if (attack.getAttackCharList() != null) {
					if (!attack.getAttackCharList().isEmpty()) {
						lobbyOpened = true;
						numberOfPilots = attack.getAttackCharList().size();
						for (AttackCharacterPOJO ac : attack.getAttackCharList()) {
							if (ac.getType().equals(ROLE_ATTACKER_COMMANDER)) {
								if (getCurrentlyOnlineCharIds().contains(ac.getCharacterID())) {
									foundDropleadAttacker = true;
								}
							}
							if (ac.getType().equals(ROLE_DEFENDER_COMMANDER)) {
								if (getCurrentlyOnlineCharIds().contains(ac.getCharacterID())) {
									foundDropleadDefender = true;
								}
							}
						}
					}
				}
				if (attack.getFactionID_Winner() != null) {
					invasionFinished = true;
				}

				boolean attackBroken = !foundDropleadAttacker || !foundDropleadDefender;

				if (attack.getFightsStarted() && attack.getFactionID_Winner() == null) {
					if (!attackBroken) {
						if (attack.getScoreAttackerVictories() == null || attack.getScoreDefenderVictories() == null) {
							ServerNexus.getEci().sendExtCom("Attack on " + starSystem.getName() + " is rolling", "en", true, true, true);
							ServerNexus.getEci().sendExtCom("Angriff auf " + starSystem.getName() + " läuft", "de", true, true, true);
						} else {
							JumpshipPOJO js = JumpshipDAO.getInstance().findById(sessionId, attack.getJumpshipID());
							FactionPOJO attackerFaction = FactionDAO.getInstance().findById(sessionId, js.getJumpshipFactionID());

							ServerNexus.getEci().sendExtCom("Attack on " + starSystem.getName() + " is rolling (" + attackerFaction.getShortName() + " " + attack.getScoreAttackerVictories() + " : " + attack.getScoreDefenderVictories() + " " + originalFaction.getShortName() + ")", "en", true, true, true);
							ServerNexus.getEci().sendExtCom("Angriff auf " + starSystem.getName() + " läuft (" +  attackerFaction.getShortName() + " " + attack.getScoreAttackerVictories() + " : " + attack.getScoreDefenderVictories() + " " + originalFaction.getShortName() + ")", "de", true, true, true);
						}
					} else {
						ServerNexus.getEci().sendExtCom("Attack on " + starSystem.getName() + " is missing one of the dropleaders. Waiting...", "en", true, true, true);
						ServerNexus.getEci().sendExtCom("Angriff auf " + starSystem.getName() + " hat einen oder beide Dropleader verloren. Wartet...", "de", true, true, true);
					}
				} else {
					if (lobbyOpened && !invasionFinished) {
						ServerNexus.getEci().sendExtCom("Attack on " + starSystem.getName() + " is in preparation, lobby open (" + numberOfPilots + ").", "en", true, true, true);
						ServerNexus.getEci().sendExtCom("Angriff auf " + starSystem.getName() + " wird vorbereitet, Lobby ist offen (" + numberOfPilots + ").", "de", true, true, true);
					} else {
						if (!invasionFinished) {
							ServerNexus.getEci().sendExtCom("Attack on " + starSystem.getName() + " is waiting for lobby to be opened.", "en", true, true, true);
							ServerNexus.getEci().sendExtCom("Angriff auf " + starSystem.getName() + " wartet auf die Lobby.", "de", true, true, true);
						}
					}
				}
			}

			// Create forum thread for this attack
			ForumDatabaseTools t = new ForumDatabaseTools();
			long season = attack.getSeason();
			long round = attack.getRound();

			// -1L: Clan vs IS
			// -2L: Clan vs Clan
			// -3L: IS vs Clan
			// -4L: IS vs IS
			JumpshipPOJO attackerJumpship = JumpshipDAO.getInstance().findById(sessionId, attack.getJumpshipID());
			FactionPOJO defender = FactionDAO.getInstance().findById(sessionId, attack.getFactionID_Defender());
			FactionPOJO attacker = FactionDAO.getInstance().findById(sessionId, attackerJumpship.getJumpshipFactionID());
			RolePlayCharacterPOJO rpChar = RolePlayCharacterDAO.getInstance().findById(sessionId, attack.getCharacterID());

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
					FactionPOJO winner = FactionDAO.getInstance().findById(sessionId, attack.getFactionID_Winner());
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
			EntityManagerHelper.rollback(sessionId);

			GameState response = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);

			logger.error("Exception during Attack save", re);
		}
	}

	private synchronized void saveJumpship(PlayerSession session, GameState state) {
		JumpshipDAO daoJS = JumpshipDAO.getInstance();
		RoutePointDAO daoRP = RoutePointDAO.getInstance();
		GameState response = new GameState(GAMESTATEMODES.JUMPSHIP_SAVE);

		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));
			JumpshipPOJO js = (JumpshipPOJO)state.getObject();

			ArrayList<RoutePointPOJO> newRoute = new ArrayList<RoutePointPOJO>(js.getRoutepointList());

			js.getRoutepointList().clear();
			js.setStarSystemHistory(newRoute.get(0).getSystemId() + "");
			daoJS.update(C3GameSessionHandler.getC3UserID(session), js);

			daoRP.deleteByJumpshipId(getC3UserID(session));

			if(!newRoute.isEmpty()) {
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
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));
			GameState errormessage = new GameState(GAMESTATEMODES.JUMPSHIP_SAVE);
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	private synchronized void saveFactionChanges(PlayerSession session, GameState state) {
		FactionDAO factionDAO = FactionDAO.getInstance();

		GameState response = new GameState(GAMESTATEMODES.FACTION_SAVE);
		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));

			if (state.getObject() != null && state.getObject() instanceof FactionPOJO factionPOJO) {
				factionDAO.update(getC3UserID(session), factionPOJO);
				EntityManagerHelper.commit(getC3UserID(session));

				response.setAction_successfully(Boolean.TRUE);
				C3GameSessionHandler.sendNetworkEvent(session, response);
			}
		} catch (RuntimeException re) {
			logger.error("Faction save", re);
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

//	private synchronized void saveUserChangesFactionChange(PlayerSession session, Long givenFactionId, String givenFactionKey) {
//		GameState response = new GameState(GAMESTATEMODES.USERDATA_SAVE);
//
//		try {
//			EntityManagerHelper.beginTransaction(getC3UserID(session));
//			FactionDAO factionDAO = FactionDAO.getInstance();
//			JumpshipDAO jsDAO = JumpshipDAO.getInstance();
//			RolePlayCharacterDAO rpDAO = RolePlayCharacterDAO.getInstance();
//
//			if (givenFactionId != null) {
//				// The requested faction for the user who saved this
//				UserPOJO userRequestingFactionChange = ((C3Player) session.getPlayer()).getUser();
//				FactionPOJO newFaction = factionDAO.findById(getC3UserID(session), givenFactionId);
//				JumpshipPOJO js = jsDAO.getJumpshipsForFaction(givenFactionId).get(0);
//
//				if (newFaction.getFactionKey() != null && newFaction.getFactionKey().equals(givenFactionKey)) {
//					// Change faction for all chars of that user
//					ArrayList<RolePlayCharacterPOJO> charList = rpDAO.getCharactersOfUser(userRequestingFactionChange);
//					for (RolePlayCharacterPOJO ch : charList) {
//						ch.setFactionId(newFaction.getId().intValue());
//						ch.setFactionTypeId(newFaction.getFactionTypeID().intValue());
//						ch.setJumpshipId(js.getId().intValue());
//						rpDAO.update(getC3UserID(session), ch);
//					}
//					response = new GameState(GAMESTATEMODES.CLIENT_LOGOUT_AFTER_FACTION_CHANGE);
//				} else {
//					logger.info("No characters have changed factions, faction key wrong or missing!");
//				}
//			}
//			EntityManagerHelper.commit(getC3UserID(session));
//
//			response.setAction_successfully(Boolean.TRUE);
//			C3GameSessionHandler.sendNetworkEvent(session, response);
//		} catch (RuntimeException re) {
//			logger.error("Error while user faction save", re);
//			re.printStackTrace();
//			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));
//
//			response.addObject(re.getMessage());
//			response.setAction_successfully(Boolean.FALSE);
//			C3GameSessionHandler.sendNetworkEvent(session, response);
//		}
//	}

//	private synchronized void saveUserChangesSaveCharacter(PlayerSession session, RolePlayCharacterPOJO characterToSavePOJO) {
//		try {
//			EntityManagerHelper.beginTransaction(getC3UserID(session));
//			RolePlayCharacterDAO rpDAO = RolePlayCharacterDAO.getInstance();
//			if (characterToSavePOJO != null) {
//				// save character changes
//				rpDAO.update(getC3UserID(session), characterToSavePOJO);
//			}
//			EntityManagerHelper.commit(getC3UserID(session));
//		} catch (RuntimeException re) {
//			logger.error("Error while user character save", re);
//			re.printStackTrace();
//			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));
//		}
//	}

//	private synchronized void saveUserChangesUser(PlayerSession session, ArrayList<UserDTO> usersToSave) {
//		try {
//			EntityManagerHelper.beginTransaction(getC3UserID(session));
//
//			UserDAO dao = UserDAO.getInstance();
//			RolePlayCharacterDAO rpDAO = RolePlayCharacterDAO.getInstance();
//
//			for (UserDTO userDTO : usersToSave) {
//				UserPOJO user = EntityConverter.convertdto2pojo(userDTO, UserPOJO.class);
//				if (user != null) {
//					user.setLastModified(new Timestamp(System.currentTimeMillis()));
//
//					ArrayList<RolePlayCharacterPOJO> charList = rpDAO.getCharactersOfUser(user);
//					for (RolePlayCharacterPOJO ch : charList) {
//						ch.setMwoUsername(user.getMwoUsername());
//						rpDAO.update(getC3UserID(session), ch);
//					}
//					if (user.getUserId() == null) {
//						// logger.info("Saving: " + user.getUserName() + " - Privs: " + user.getPrivileges());
//						dao.save(getC3UserID(session), user);
//					} else {
//						// logger.info("Updating: " + user.getUserName() + " - Privs: " + user.getPrivileges());
//						dao.update(getC3UserID(session), user);
//					}
//				}
//			}
//			EntityManagerHelper.commit(getC3UserID(session));
//		} catch (RuntimeException re) {
//			logger.error("Error while user save", re);
//			re.printStackTrace();
//			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));
//		}
//	}

//	private synchronized void saveUserChanges(PlayerSession session, GameState state) {
//		if (state.getObject() instanceof UsereditorSaveObject incomingSaveObject) {
//			Long givenFactionId = null;
//			String givenFactionKey = null;
//			RolePlayCharacterPOJO characterToSavePOJO = null;
//
////			if (incomingSaveObject.getRpCharacter() != null) {
////				RolePlayCharacterDTO characterToSaveDTO = incomingSaveObject.getRpCharacter();
////				characterToSavePOJO = EntityConverter.convertdto2pojo(characterToSaveDTO, RolePlayCharacterPOJO.class);
////			}
//			if (state.get.getFactionToChangeTo() != null) {
//				givenFactionId = incomingSaveObject.getFactionToChangeTo();
//			}
//			if (incomingSaveObject.getFactionKey() != null) {
//				givenFactionKey = incomingSaveObject.getFactionKey();
//			}
//
//			saveUserChangesFactionChange(session, givenFactionId, givenFactionKey);
//			saveUserChangesSaveCharacter(session, characterToSavePOJO);
//			saveUserChangesUser(session, incomingSaveObject.getUsersToSave());
//		}
//	}

	private synchronized void saveUserChanges(PlayerSession session, GameState state) {
		UserDAO dao = UserDAO.getInstance();
		RolePlayCharacterDAO rpDAO = RolePlayCharacterDAO.getInstance();
		FactionDAO factionDAO = FactionDAO.getInstance();
		JumpshipDAO jsDAO = JumpshipDAO.getInstance();

		GameState response = new GameState(GAMESTATEMODES.USERDATA_SAVE);
		try {
			EntityManagerHelper.beginTransaction(getC3UserID(session));

			ArrayList<UserPOJO> list = (ArrayList<UserPOJO>) state.getObject();
			String givenFactionKey = null;
			if (state.getObject3() != null && state.getObject3() instanceof String) {
				givenFactionKey = (String) state.getObject3();
			}

			if (state.getObject2() != null && state.getObject2() instanceof Long factionId) {
				// The requested faction for the user who saved this
				UserPOJO userRequestingFactionChange = ((C3Player) session.getPlayer()).getUser();
				FactionPOJO newFaction = factionDAO.findById(getC3UserID(session), factionId);
				JumpshipPOJO js = jsDAO.getJumpshipsForFaction(factionId).get(0);

				if (newFaction.getFactionKey() != null && newFaction.getFactionKey().equals(givenFactionKey)) {
					// Change faction for all chars of that user
					ArrayList<RolePlayCharacterPOJO> charList = rpDAO.getCharactersOfUser(userRequestingFactionChange);
					for (RolePlayCharacterPOJO ch : charList) {
						ch.setFactionId(newFaction.getId().intValue());
						ch.setFactionTypeId(newFaction.getFactionTypeID().intValue());
						ch.setJumpshipId(js.getId().intValue());
						rpDAO.update(getC3UserID(session), ch);
					}
					response = new GameState(GAMESTATEMODES.CLIENT_LOGOUT_AFTER_FACTION_CHANGE);
				} else {
					logger.info("No characters have changed factions, faction key wrong or missing!");
				}
			}

			for (UserPOJO user : list) {
				user.setLastModified(new Timestamp(System.currentTimeMillis()));

				RolePlayCharacterPOJO rpCharPojoFromGameState = user.getCurrentCharacter();

				ArrayList<RolePlayCharacterPOJO> charList = rpDAO.getCharactersOfUser(user);
				for (RolePlayCharacterPOJO ch : charList) {
					if (ch.getId().equals(rpCharPojoFromGameState.getId())) {
						// This is the character that was sent in the gamestate... may have changed
						rpCharPojoFromGameState.setMwoUsername(user.getMwoUsername());
						logger.info("+++++++++++++++ Saving character (sent in as changed): " + rpCharPojoFromGameState.getName());
						rpDAO.update(getC3UserID(session), rpCharPojoFromGameState);
					} else {
						ch.setMwoUsername(user.getMwoUsername());
						logger.info("+++++++++++++++ Saving character (selected from db): " + ch.getName());
						rpDAO.update(getC3UserID(session), ch);
					}
				}
				if (user.getUserId() == null) {
					// logger.info("Saving: " + user.getUserName() + " - Privs: " + user.getPrivileges());
					dao.save(getC3UserID(session), user);
				} else {
					// logger.info("Updating: " + user.getUserName() + " - Privs: " + user.getPrivileges());
					dao.update(getC3UserID(session), user);
				}
			}
			EntityManagerHelper.commit(getC3UserID(session));

			response.setAction_successfully(Boolean.TRUE);
			C3GameSessionHandler.sendNetworkEvent(session, response);

		} catch (RuntimeException re) {
			logger.error("Privilege save", re);
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

		//		for (UserPOJO u : userlist) {
		//			logger.info("+++++++++++++++ Found user: " + u.getUserName());
		//		}

		// Trigger the creation of a new universe... but wait until it is ready before
		// sending it back
		final CountDownLatch latch = new CountDownLatch(1);
		Timer serverHeartBeat = new Timer();
		serverHeartBeat.schedule(new HeartBeatTimerTask(false, latch), 0);
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("Error in getLoggedInUserData", e);
		}
		UniverseDTO uni = WebDataInterface.getUniverse();

		byte[] compressedUniverse = Compressor.compress(uni);
//		byte[] compressedUserList = Compressor.compress(userlist);
		logger.info("Compressed universe size: " + compressedUniverse.length + " byte.");
//		logger.info("Compressed userlist size: " + compressedUserList.length + " byte.");

		GameState state_userdata = new GameState(GAMESTATEMODES.USER_LOGGED_IN_DATA);
		state_userdata.addObject(user);
		state_userdata.addObject2(compressedUniverse);
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

			ServerNexus.getEci().sendExtCom(user.getUserName() + " logged into C3-Client.", "en",true, true, false);
			ServerNexus.getEci().sendExtCom(user.getUserName() + " hat sich im C3-Client angemeldet.", "de",true, true, false);

//			if( !GameServer.isDevelopmentPC) {
//				boolean sent = false;
//				String[] receivers = {"keshik@googlegroups.com"};
//				sent = MailManager.sendMail("c3@clanwolf.net", receivers, user.getUserName() + " logged into C3 client", "User logged into C3 client.", false);
//				if (sent) {
//					// sent
//					logger.info("User logged in information mail sent. [4]");
//				} else {
//					// error during email sending
//					logger.info("Error during mail dispatch. [4]");
//				}
//			}
//			logger.info("--------------------");
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
	public synchronized void sendNewPlayerList(PlayerSession session) {
		ArrayList<UserPOJO> userList = new ArrayList<>();
		for (PlayerSession playerSession : room.getSessions()) {
			C3Player pl = (C3Player) playerSession.getPlayer();
			userList.add(pl.getUser());
			if (pl.getUser() == null) {
				String name = "";
				try {
					name = pl.getName();
				} catch(Exception e) {
					logger.error("Error in PlayerSession." , e);
				}
				logger.info("----------------------- Found C3Player with NO USER: " + name);
			}
		}

		GameState stateNewPlayerList = new GameState(GAMESTATEMODES.USER_GET_NEW_PLAYERLIST);
		stateNewPlayerList.addObject(userList);

		C3GameSessionHandler.sendBroadCast(room, stateNewPlayerList);
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

	public void continueCommands() {
//		notify();
	}

	private void executeCommand(PlayerSession session, GameState state) {
		logger.info("C3GameSessionHandler.executeCommand: " + state.getMode().toString());
		EntityConverter.convertGameStateToPOJO(state);

		Timer serverHeartBeat;
		String ipAdressSender = state.getIpAdressSender();

		switch (state.getMode()) {
			case ROLL_RANDOM_MAP:
				if (state.getObject() instanceof Long) {
					Long attackId = (Long) state.getObject();

					Random ran = new Random();
					int d1 = ran.nextInt(6) + 1;
					int d2 = ran.nextInt(6) + 1;

					GameState response = new GameState(GAMESTATEMODES.RETURN_ROLLED_RANDOM_MAP_FOR_INVASION);
					response.addObject(attackId);
					response.addObject2(String.valueOf(d1) + d2);
					C3GameSessionHandler.sendBroadCast(room, response);
				}
				break;
			case PLAY_SOUNDBOARD_SOUND_EVENT_01:
				if (state.getObject() instanceof Long && state.getObject2() instanceof Long) {
					logger.info("Play soundboard sound 01.");

					Long charId = (Long) state.getObject();
					Long attackId = (Long) state.getObject2();

					GameState response = new GameState(GAMESTATEMODES.PLAY_SOUNDBOARD_SOUND_EVENT_EXECUTE_01);
					response.addObject(charId);
					response.addObject2(attackId);
					C3GameSessionHandler.sendBroadCast(room, response);
				}
				break;
			case PLAY_SOUNDBOARD_SOUND_EVENT_02:
				if (state.getObject() instanceof Long && state.getObject2() instanceof Long) {
					logger.info("Play soundboard sound 02.");

					Long charId = (Long) state.getObject();
					Long attackId = (Long) state.getObject2();

					GameState response = new GameState(GAMESTATEMODES.PLAY_SOUNDBOARD_SOUND_EVENT_EXECUTE_02);
					response.addObject(charId);
					response.addObject2(attackId);
					C3GameSessionHandler.sendBroadCast(room, response);
				}
				break;
			case PLAY_SOUNDBOARD_SOUND_EVENT_03:
				if (state.getObject() instanceof Long && state.getObject2() instanceof Long) {
					logger.info("Play soundboard sound 03.");

					Long charId = (Long) state.getObject();
					Long attackId = (Long) state.getObject2();

					GameState response = new GameState(GAMESTATEMODES.PLAY_SOUNDBOARD_SOUND_EVENT_EXECUTE_03);
					response.addObject(charId);
					response.addObject2(attackId);
					C3GameSessionHandler.sendBroadCast(room, response);
				}
				break;
			case ATTACK_TEAM_ERROR:
				if (state.getObject() instanceof AttackPOJO a
						&& state.getObject2() instanceof Long attackType
						&& state.getObject3() instanceof String username) {

					Long starsystemId =  a.getStarSystemID();
					StarSystemPOJO ss = StarSystemDAO.getInstance().findById(getC3UserID(session), starsystemId);

					logger.info("----------------------------------------------");
					logger.info("Attack: " + ss.getName());
					logger.info("Type: " + attackType);
					logger.info("Teams seem to be invalid!");
					logger.info("User: " + username + " switched teams?");
					logger.info(username + " is in one team in the lobby, but in another in MWO (?)");
					logger.info("Users may not switch sides in the middle of an invasion!");
					logger.info("----------------------------------------------");

					GameState response = new GameState(GAMESTATEMODES.ATTACK_TEAM_ERROR_RESPONSE);
					response.addObject(username);
					response.addObject2(a.getId());
					response.addObject3(attackType);
					C3GameSessionHandler.sendBroadCast(room, response);

					// TODO_C3: Push attack back to lobby here?
				}
				break;
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
				ServerNexus.getEci().sendExtCom(session.getPlayer().getName() + " left C3-Client", "en",true, true, false);
				ServerNexus.getEci().sendExtCom(session.getPlayer().getName() + " hat den C3-Client verlassen", "de",true, true, false);
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
			case USERDATA_SAVE:
				saveUserChanges(session, state);
				break;
			case FACTION_SAVE:
				saveFactionChanges(session, state);
				break;
			case JUMPSHIP_SAVE:
				saveJumpship(session, state);
				serverHeartBeat = new Timer();
				serverHeartBeat.schedule(new HeartBeatTimerTask(true, null), 0);
				break;
			case ATTACK_SAVE:

				logger.info("Calling saveAttack for C3GameSessionHamdler.");

				saveAttack(session, state);
				serverHeartBeat = new Timer();
				serverHeartBeat.schedule(new HeartBeatTimerTask(true, null), 100);
				break;
			case ATTACK_SAVE_WITH_CHARACTERS:

				logger.info("Calling saveAttack with AttackCharacters for C3GameSessionHamdler.");

				saveAttack(session, state, true);
				serverHeartBeat = new Timer();
				serverHeartBeat.schedule(new HeartBeatTimerTask(true, null), 100);
				break;
			case DIPLOMACY_SAVE:
				saveDiplomacy(session, state);
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
				serverHeartBeat.schedule(new HeartBeatTimerTask(true, null), 0);
				break;
			case FORCE_NEW_UNIVERSE:
				serverHeartBeat = new Timer();
				serverHeartBeat.schedule(new HeartBeatTimerTask(true, null), 0);
				break;
			case RESET_FIGHT:
				// Manually reset a fight (by command in terminal) in case a pilot left and all needs to be restarted
				if (state.getObject() instanceof Long attackIdFromState) {
					logger.info("A reset of attack " + attackIdFromState + " was requested by admin");
					resetAttack(attackIdFromState);
				}
				break;
			case RESTART_SERVER:
				// Trigger shutdown / restart of server
				File restartFlagFile = new File(ServerNexus.getServerBaseDir() + File.separator + "C3-Server_restart.flag");
				try {
					if (!restartFlagFile.createNewFile()) {
						throw new Exception("Error creating shutdown flag file");
					}
				} catch (Exception e) {
					logger.error("Could not create shutdown flag file", e);
				}
				break;
			default:
				break;
		}
	}

	static public void sendErrorMessageToClient(PlayerSession session, Exception re){
		GameState gsErrorMessage = new GameState(GAMESTATEMODES.ERROR_MESSAGE);
		gsErrorMessage.addObject(re.getMessage());
		gsErrorMessage.setAction_successfully(Boolean.FALSE);
		C3GameSessionHandler.sendNetworkEvent(session, gsErrorMessage);
	}

	public static synchronized ArrayList<Long> getCurrentlyOnlineCharIds() {
		ArrayList<Long> characterOnlineList = new ArrayList<>();
		for (PlayerSession playerSession : staticRoom.getSessions()) {
			C3Player pl = (C3Player) playerSession.getPlayer();
			if (pl.getUser() != null) {
				characterOnlineList.add(pl.getUser().getCurrentCharacter().getId());
			}
		}
		return characterOnlineList;
	}

	private synchronized void checkAttackForMissingDropleads(PlayerSession session, AttackPOJO attack) {

		RolePlayCharacterDAO rpDAO = RolePlayCharacterDAO.getInstance();
		JumpshipDAO jsDAO = JumpshipDAO.getInstance();
		FactionDAO fDAO = FactionDAO.getInstance();

		// all online characters
		ArrayList<Long> characterOnlineList = getCurrentlyOnlineCharIds();

		ArrayList<AttackCharacterPOJO> acpl = null;
		if (attack.getAttackCharList() != null) {
			acpl = new ArrayList<>(attack.getAttackCharList());

			AttackCharacterPOJO dropLeadA = null;
			AttackCharacterPOJO dropLeadD = null;
			AttackCharacterPOJO dropLeadCandidateA = null;
			AttackCharacterPOJO dropLeadCandidateD = null;

			for (AttackCharacterPOJO acp : acpl) {

				if (acp.getType().equals(Constants.ROLE_ATTACKER_COMMANDER)) {
					dropLeadA = acp;
					dropLeadCandidateA = null;
				}
				if (acp.getType().equals(Constants.ROLE_DEFENDER_COMMANDER)) {
					dropLeadD = acp;
					dropLeadCandidateD = null;
				}

				RolePlayCharacterPOJO character = rpDAO.findById(getC3UserID(session), acp.getCharacterID());
				JumpshipPOJO attackerJumpship = jsDAO.findById(getC3UserID(session), attack.getJumpshipID());
				FactionPOJO attackerFaction = fDAO.findById(getC3UserID(session), attackerJumpship.getJumpshipFactionID());

				if(characterOnlineList.contains(character.getId())) {
					if (dropLeadA == null && acp.getType().equals(ROLE_ATTACKER_WARRIOR) && !acp.getType().equals(ROLE_DROPLEAD_LEFT)) {
						if (character.getFactionId() == attackerFaction.getId().intValue()) {
							dropLeadCandidateA = acp;
						}
					}
					if (dropLeadD == null && acp.getType().equals(ROLE_DEFENDER_WARRIOR) && !acp.getType().equals(ROLE_DROPLEAD_LEFT)) {
						if (character.getFactionId() == attack.getFactionID_Defender().intValue()) {
							dropLeadCandidateD = acp;
						}
					}
				}
			}

			if(dropLeadCandidateA != null){
				dropLeadCandidateA.setType(Constants.ROLE_ATTACKER_COMMANDER);
			}

			if(dropLeadCandidateD != null){
				dropLeadCandidateD.setType(Constants.ROLE_DEFENDER_COMMANDER);
			}
		}
	}
}
