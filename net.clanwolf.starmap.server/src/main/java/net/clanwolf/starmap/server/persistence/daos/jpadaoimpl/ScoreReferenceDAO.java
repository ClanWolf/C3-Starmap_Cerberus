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
 * Copyright (c) 2001-2026, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackPOJO;
import net.clanwolf.starmap.server.persistence.pojos.ScorePOJO;
import net.clanwolf.starmap.server.persistence.pojos.ScoreReferencePOJO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class ScoreReferenceDAO extends GenericDAO {

	private static ScoreReferenceDAO instance;

	public ScoreReferenceDAO() {
		// Empty constructor
	}

	public static ScoreReferenceDAO getInstance() {
		if (instance == null) {
			instance = new ScoreReferenceDAO();
			instance.className = "ScoreReferencePOJO";
		}
		return instance;
	}

	@Override
	public void delete(Long sessionID, Object entity) {
		super.delete(sessionID, entity, ((ScoreReferencePOJO) entity).getId());
	}

	@Override
	public ScoreReferencePOJO findById(Long userId, Long id) {
		if (userId == null) {
			return (ScoreReferencePOJO) super.findById(ScoreReferencePOJO.class, id);
		} else {
			return (ScoreReferencePOJO) super.findById(userId, ScoreReferencePOJO.class, id);
		}
	}

	@Override
	public ScoreReferencePOJO update(Long sessionID, Object entity) {
		return (ScoreReferencePOJO) super.update(sessionID, entity);
	}

	/*
	 * Give back all score references of a season
	 */
	public ArrayList<ScoreReferencePOJO> getScoreReferencesForSeason(Long seasonId){
		CriteriaHelper crit = new CriteriaHelper(ScoreReferencePOJO.class);
		crit.addCriteria("seasonId", seasonId);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<ScoreReferencePOJO> lsr = new ArrayList<>();
		while (iter.hasNext()) lsr.add((ScoreReferencePOJO) iter.next());

		return lsr;
	}
}
