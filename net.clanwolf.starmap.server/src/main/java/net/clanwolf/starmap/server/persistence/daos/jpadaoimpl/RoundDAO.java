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
package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.RoundPOJO;
import net.clanwolf.starmap.server.persistence.pojos.SeasonPOJO;
import net.clanwolf.starmap.server.persistence.pojos.UserPOJO;
import net.clanwolf.starmap.server.util.Encryptor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class RoundDAO extends GenericDAO {

	private static RoundDAO instance;

	public static RoundDAO getInstance() {
		if (instance == null) {
			instance = new RoundDAO();
			instance.className = "RoundPOJO";
		}
		return instance;
	}

	private RoundDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long roundID, Object entity) {
		super.delete(roundID, entity, ((RoundPOJO) entity).getId());
	}

	@Override
	public RoundPOJO update(Long userID, Object entity) {
		return (RoundPOJO) super.update(userID, entity);
	}

	@Override
	public RoundPOJO findById(Long userId, Long id) {
		if (userId == null) {
			return (RoundPOJO) super.findById(RoundPOJO.class, id);
		} else {
			return (RoundPOJO) super.findById(userId, RoundPOJO.class, id);
		}
	}

	// Find the round pojo that belongs to that season (there is only one per season)
	public RoundPOJO findBySeasonId(Long seasonId) {
		CriteriaHelper crit1 = new CriteriaHelper(RoundPOJO.class);
		crit1.addCriteria("season", seasonId);

		Object o = crit1.getSingleResult();

		return (RoundPOJO) o;
	}

	/*
	 * Give all rounds
	 */
	public ArrayList<RoundPOJO> getAllRounds(){
		CriteriaHelper crit = new CriteriaHelper(RoundPOJO.class);
		crit.addCriteriaIsNotNull("id");

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<RoundPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) {
			lRPS.add((RoundPOJO) iter.next());
		}

		return lRPS;
	}
}
