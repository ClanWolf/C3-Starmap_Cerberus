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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.JumpshipDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayCharacterDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayStoryDAO;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;
import net.clanwolf.starmap.transfer.enums.roleplayinputdatatypes.ROLEPLAYINPUTDATATYPES;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class C3GameSessionHandlerRoleplay {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * Save a RolpPlayStory into the database
	 *
	 * @param session PlayerSession
	 * @param state GameState
	 */
	@SuppressWarnings("unchecked")
	static void saveRolePlayStory(PlayerSession session, GameState state) {
		RolePlayStoryPOJO rp = (RolePlayStoryPOJO) state.getObject();

		RolePlayStoryDAO dao = RolePlayStoryDAO.getInstance();
		RolePlayCharacterDAO daoChar = RolePlayCharacterDAO.getInstance();

		ArrayList<RolePlayCharacterPOJO> changeCharList = new ArrayList<>();
		GameState response = new GameState(GAMESTATEMODES.ROLEPLAY_SAVE_STORY);

		try {
			EntityManagerHelper.beginTransaction(C3GameSessionHandler.getC3UserID(session));

			//TODO_C3: Save RolePlayCharacter
			if(rp.getVariante() == ROLEPLAYENTRYTYPES.C3_RP_STORY) {

				for (Long aLong1 : rp.getNewCharIDs()) {
					RolePlayCharacterPOJO pojo = daoChar.findById(C3GameSessionHandler.getC3UserID(session), aLong1);

					pojo.setStory(rp);
					daoChar.update(C3GameSessionHandler.getC3UserID(session), pojo);
					changeCharList.add(pojo);
				}


				for (Long aLong : rp.getRemovedCharIDs()) {
					RolePlayCharacterPOJO pojo = daoChar.findById(C3GameSessionHandler.getC3UserID(session), aLong);
					pojo.setStory(null);
					daoChar.update(C3GameSessionHandler.getC3UserID(session), pojo);
					changeCharList.add(pojo);
				}
			}

			/* Save RolePlayStoryPOJO */
			if (rp.getId() == null) {
				dao.save(C3GameSessionHandler.getC3UserID(session), rp);
			} else {
				dao.update(C3GameSessionHandler.getC3UserID(session), rp);
			}

			// Save objects whit changed sortorder
			ArrayList<RolePlayStoryPOJO> lstSortOrder = (ArrayList<RolePlayStoryPOJO>) state.getObject2();
			if (lstSortOrder.size() > 0) {
				Iterator<RolePlayStoryPOJO> iterSortOrder = lstSortOrder.iterator();
				while (iterSortOrder.hasNext()) {
					dao.update(C3GameSessionHandler.getC3UserID(session), iterSortOrder.next());
				}
			}

			EntityManagerHelper.commit(C3GameSessionHandler.getC3UserID(session));

			/* send object back to client */
			response.addObject(rp);
			response.addObject2(changeCharList);
			response.setAction_successfully(Boolean.TRUE);

		} catch (Exception re) {
			re.printStackTrace();

			/* if a error occurs, we must do a rollback */
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);

			logger.error("saveRolePlayStory",re);
			// throw re;
		} finally {
			/* and now we send a message to the client */
//			Event e = Events.networkEvent(response);
//			session.onEvent(e);
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	/**
	 * Deletes a RolePlayStoryPOJO object on the database and it adapt the sort order of the other RolePlayStories that must be changed
	 *
	 * @param session PlayerSession
	 * @param state GameState
	 */
	@SuppressWarnings("unchecked")
	static void deleteRolePlayStory(PlayerSession session, GameState state) {
		RolePlayStoryPOJO rp = (RolePlayStoryPOJO) state.getObject();

		RolePlayStoryDAO dao = RolePlayStoryDAO.getInstance();

		GameState response = new GameState(GAMESTATEMODES.ROLEPLAY_DELETE_STORY);

		try {
			EntityManagerHelper.beginTransaction(C3GameSessionHandler.getC3UserID(session));

			dao.delete(C3GameSessionHandler.getC3UserID(session), rp);

			// Save objects with changed sortorder
			ArrayList<RolePlayStoryPOJO> lstSortOrder = (ArrayList<RolePlayStoryPOJO>) state.getObject2();
			if (lstSortOrder.size() > 0) {
				Iterator<RolePlayStoryPOJO> iterSortOrder = lstSortOrder.iterator();
				while (iterSortOrder.hasNext()) {
					dao.update(C3GameSessionHandler.getC3UserID(session), iterSortOrder.next());
				}
			}

			EntityManagerHelper.commit(C3GameSessionHandler.getC3UserID(session));

			response.addObject(null);
			response.setAction_successfully(Boolean.TRUE);

		} catch (RuntimeException re) {

			/* if a error occurs, we must do a rollback */
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);

			logger.error("saveRolePlayStory",re);
		} finally {

			/* and now we send a message to the client */
//			Event e = Events.networkEvent(response);
//			session.onEvent(e);

			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	/**
	 *
	 * Returns a list of all stories of the given author
	 *
	 * @param session PlayerSession
	 * @param state GameSTate
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static void requestAllStories(PlayerSession session, GameState state) {
		RolePlayStoryDAO dao = RolePlayStoryDAO.getInstance();

		RolePlayStoryPOJO rpMainStory = (RolePlayStoryPOJO) state.getObject();
		ArrayList<RolePlayStoryPOJO> mylist = new ArrayList();

		GameState response = null;

		if(rpMainStory == null){
			mylist = dao.getAllMainStories();
			response = new GameState(GAMESTATEMODES.ROLEPLAY_GET_ALLSTORIES);
		}
		else {
			mylist = dao.getAllStoriesByStory(rpMainStory);
			response = new GameState(GAMESTATEMODES.ROLEPLAY_GET_STEPSBYSTORY);
		}

		response.addObject(mylist);
		C3GameSessionHandler.sendNetworkEvent(session, response);
	}

	/**
	 * Returns a list of all RolePlayCharacters
	 *
	 * @param session PlayerSession
	 * @param state GameState
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static void requestAllCharacter(PlayerSession session, GameState state) {
		RolePlayCharacterDAO dao = RolePlayCharacterDAO.getInstance();
		List<RolePlayCharacterPOJO> lrp = (List) dao.findAll(C3GameSessionHandler.getC3UserID(session), null);
		ArrayList<RolePlayCharacterPOJO> mylist = new ArrayList(lrp);

		GameState response = new GameState(GAMESTATEMODES.ROLEPLAY_GET_ALLCHARACTER);
		response.addObject(mylist);

//		Event e = Events.networkEvent(reponse);
//		session.onEvent(e);

		C3GameSessionHandler.sendNetworkEvent(session, response);
	}

	/**
	 * Returns a chapter of a story with a given sort number
	 *
	 * @param session PlayerSession
	 * @param state GameState
	 */
	static void getChapterBySortOrder(PlayerSession session, GameState state){
		RolePlayStoryDAO dao = RolePlayStoryDAO.getInstance();
		RolePlayCharacterDAO daoRPC = RolePlayCharacterDAO.getInstance();

		RolePlayCharacterPOJO rpChar = (RolePlayCharacterPOJO)state.getObject();
		RolePlayStoryPOJO story = rpChar.getStory();
		Integer sortOrder = (Integer)state.getObject2();

		GameState response = new GameState(GAMESTATEMODES.ROLEPLAY_GET_CHAPTER_BYSORTORDER);

		try {
			RolePlayStoryPOJO newStep = dao.getChapterFromStoryBySortOrder(story, sortOrder);
			rpChar.setStory(newStep);

			//TODO_C3: Save RolePlayCharacterPOJO

			EntityManagerHelper.beginTransaction(C3GameSessionHandler.getC3UserID(session));
			daoRPC.update(C3GameSessionHandler.getC3UserID(session), rpChar);
			EntityManagerHelper.commit(C3GameSessionHandler.getC3UserID(session));

			response.addObject(rpChar);
			response.setAction_successfully(Boolean.TRUE);

		} catch (RuntimeException re){
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);

			logger.error("saveRolePlayStory",re);
		} finally {
			/* and now we send a message to the client */
//			Event e = Events.networkEvent(response);
//			session.onEvent(e);
			// throw re;
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}

	}

	/**
	 *
	 * eturns a step of a story of a story with a given sort number
	 *
	 * @param session PlayerSession
	 * @param state GameState
	 */
	static void getStepBySortOrder(PlayerSession session, GameState state){
		RolePlayStoryDAO dao = RolePlayStoryDAO.getInstance();
		RolePlayCharacterDAO daoRPC = RolePlayCharacterDAO.getInstance();

		RolePlayCharacterPOJO rpChar = (RolePlayCharacterPOJO)state.getObject();
		RolePlayStoryPOJO story = rpChar.getStory();
		Integer sortOrder = (Integer)state.getObject2();

		GameState response = new GameState(GAMESTATEMODES.ROLEPLAY_GET_STEP_BYSORTORDER);

		try {
			RolePlayStoryPOJO newStep = dao.getStepFromStoryBySortOrder(story, sortOrder);
			rpChar.setStory(newStep);

			EntityManagerHelper.beginTransaction(C3GameSessionHandler.getC3UserID(session));
			daoRPC.update(C3GameSessionHandler.getC3UserID(session), rpChar);
			EntityManagerHelper.commit(C3GameSessionHandler.getC3UserID(session));

			response.addObject(rpChar);
			response.setAction_successfully(Boolean.TRUE);

		} catch (RuntimeException re){
			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);

			logger.error("saveRolePlayStory",re);
		} finally {
			/* and now we send a message to the client */
//			Event e = Events.networkEvent(response);
//			session.onEvent(e);
			// throw re;
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	/**
	 *
	 * returns a step of a story of a story with a given sort number
	 *
	 * @param session PlayerSession
	 * @param state GameState
	 */
	static void saveRolePlayCharacterNextStep(PlayerSession session, GameState state){
		RolePlayCharacterDAO daoRPC = RolePlayCharacterDAO.getInstance();
		RolePlayStoryDAO daoStory = RolePlayStoryDAO.getInstance();

		RolePlayCharacterPOJO rpChar = (RolePlayCharacterPOJO)state.getObject();
		Long storyID = (Long)state.getObject2();

		GameState response = new GameState(GAMESTATEMODES.ROLEPLAY_SAVE_NEXT_STEP);

		try {

			//TODO_C3: Save RolePlayCharacterPOJO
			EntityManagerHelper.beginTransaction(C3GameSessionHandler.getC3UserID(session));
			RolePlayStoryPOJO storyPojo = daoStory.findById(C3GameSessionHandler.getC3UserID(session), storyID);
			rpChar.setStory(storyPojo);

			daoRPC.update(C3GameSessionHandler.getC3UserID(session), rpChar);
			EntityManagerHelper.commit(C3GameSessionHandler.getC3UserID(session));

			response.addObject(rpChar);
			response.setAction_successfully(Boolean.TRUE);

		} catch (RuntimeException re){

			/* if a error occurs, we must do a rollback */
			EntityManagerHelper.rollback(C3GameSessionHandler.getC3UserID(session));

			response.addObject(re.getMessage());
			response.setAction_successfully(Boolean.FALSE);

			logger.error("saveRolePlayStory",re);
		} finally {
			/* and now we send a message to the client */
//			Event e = Events.networkEvent(response);
//			session.onEvent(e);
			// throw re;
			C3GameSessionHandler.sendNetworkEvent(session, response);
		}
	}

	/*static void requestJumpshipTest(PlayerSession session, GameState state) {
		JumpshipDAO dao = JumpshipDAO.getInstance();
		List<JumpshipPOJO> lrp = (List) dao.findAll(C3GameSessionHandler.getC3UserID(session), null);
		ArrayList<JumpshipPOJO> mylist = new ArrayList(lrp);

		System.out.println(mylist);

		RoutePointPOJO point = new RoutePointPOJO();
		point.setJumpshipId(1L);
		point.setRoundId(1L);
		point.setSeasonId(1L);
		point.setSystemId(100L);

		JumpshipPOJO j = mylist.get(1);
		j.getRoutepointList().add(point);
		dao.update(C3GameSessionHandler.getC3UserID(session),j );

		logger.info("Hier hat er hoffentlich alles gespeichert");
	}*/
}
