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
 * Copyright (c) 2001-2020, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayStoryPOJO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for RolePlayStoryPOJO entities. Transaction control of the save(), update() and delete() operations must be handled externally by senders of these methods or must be manually added to
 * each of these methods for data to be persisted to the JPA datastore.
 *
 * @author MyEclipse Persistence Tools
 */
public class RolePlayStoryDAO extends GenericDAO {

	private static RolePlayStoryDAO instance;

	public static RolePlayStoryDAO getInstance() {
		if (instance == null) {
			instance = new RolePlayStoryDAO();
			instance.className = "RolePlayStoryPOJO";
		}
		return instance;
	}

	private RolePlayStoryDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object rolePlayStory) {
		super.delete(userID, rolePlayStory, ((RolePlayStoryPOJO) rolePlayStory).getId());
	}

	@Override
	public RolePlayStoryPOJO update(Long userID, Object entity) {
		return (RolePlayStoryPOJO) super.update(userID, entity);
	}

	@Override
	public RolePlayStoryPOJO findById(Long userID, Long id) {
		return (RolePlayStoryPOJO) super.findById(userID, RolePlayStoryPOJO.class, id);
	}

	/**
	 * Returns all RolePlayStorys with the types C3_RP_STORY and C3_RP_CHAPTER
	 * 
	 * @return ArrayList<RolePlayStoryPOJO> *
	 */
	public ArrayList<RolePlayStoryPOJO> getAllStoriesAndChapters() {
		CriteriaHelper crit = new CriteriaHelper(RolePlayStoryPOJO.class);

		crit.addCriteriaOR(crit.createPredicate("type", ROLEPLAYENTRYTYPES.C3_RP_STORY), crit.createPredicate("type", ROLEPLAYENTRYTYPES.C3_RP_CHAPTER));

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<RolePlayStoryPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) {
			lRPS.add((RolePlayStoryPOJO) iter.next());
		}

		return lRPS;
	}

	/**
	 * Returns a chapter of a story with a given sort number
	 *
	 * @param rp RolePlayStoryPOJO
	 * @param sortOrder Integer
	 * @return RolePlayStoryPOJO
	 */
	public RolePlayStoryPOJO getChapterFromStoryBySortOrder(RolePlayStoryPOJO rp, Integer sortOrder){
		CriteriaHelper crit = getStepBySortOrder(rp, sortOrder);

		crit.addCriteria("type", ROLEPLAYENTRYTYPES.C3_RP_CHAPTER);

		return (RolePlayStoryPOJO)crit.getSingleResult();
	}

	/**
	 * Returns a step of a story of a story with a given sort number
	 *
	 * @param rp RolePlayStoryPOJO
	 * @param sortOrder Integer
	 * @return RolePlayStoryPOJO
	 */
	public RolePlayStoryPOJO getStepFromStoryBySortOrder(RolePlayStoryPOJO rp, Integer sortOrder){
		CriteriaHelper crit = getStepBySortOrder(rp, sortOrder);

		crit.addCriteriaOR(crit.createPredicate("type", ROLEPLAYENTRYTYPES.C3_RP_STEP_V1),
				crit.createPredicate("type", ROLEPLAYENTRYTYPES.C3_RP_STEP_V2),
				crit.createPredicate("type", ROLEPLAYENTRYTYPES.C3_RP_STEP_V3),
				crit.createPredicate("type", ROLEPLAYENTRYTYPES.C3_RP_STEP_V4));

		return (RolePlayStoryPOJO)crit.getSingleResult();
	}

	/**
	 * Returns a criteria for select a step of a story for a given sort number
	 * @param rp RolePlayStoryPOJO
	 * @param sortOrder Integer
	 * @return CriteriaHelper
	 */
	private CriteriaHelper getStepBySortOrder(RolePlayStoryPOJO rp, Integer sortOrder){
		CriteriaHelper crit = new CriteriaHelper(RolePlayStoryPOJO.class);

//		crit.addCriteria("story",rp);
		crit.addCriteriaOR(crit.createPredicate("story",rp),
				crit.createPredicate("parentStory",rp));
		crit.addCriteria("sortOrder", sortOrder);

		return crit;
	}

}