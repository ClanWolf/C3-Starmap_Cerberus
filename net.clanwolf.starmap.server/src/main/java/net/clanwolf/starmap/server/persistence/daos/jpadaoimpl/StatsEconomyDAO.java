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
 * Copyright (c) 2001-2024, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.StatsEconomyPOJO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class StatsEconomyDAO extends GenericDAO {

	private static StatsEconomyDAO instance;

	private StatsEconomyDAO() {
		// Empty constructor
	}

	public static StatsEconomyDAO getInstance() {
		if (instance == null) {
			instance = new StatsEconomyDAO();
			instance.className = "StatsEconomyPOJO";
		}
		return instance;
	}

	@Override
	public void delete(Long userId, Object entity) {
		super.delete(userId, entity, ((StatsEconomyPOJO) entity).getId());
	}

	@Override
	public StatsEconomyPOJO findById(Long userId, Long id) {
		return (StatsEconomyPOJO) super.findById(userId, StatsEconomyPOJO.class, id);
	}

	@Override
	public StatsEconomyPOJO update(Long userID, Object entity) {
		return (StatsEconomyPOJO) super.update(userID, entity);
	}

	public StatsEconomyPOJO findByMatchId(String matchId) {
		CriteriaHelper crit = new CriteriaHelper(StatsEconomyPOJO.class);
		crit.addCriteria("mwoMatchId", matchId);

		return (StatsEconomyPOJO) crit.getSingleResult();
	}

	public ArrayList<StatsEconomyPOJO> getStatisticsForAttack(Long seasonId, Long attackId) {
		CriteriaHelper crit = new CriteriaHelper(StatsEconomyPOJO.class);
		crit.addCriteria("seasonId", seasonId);
		crit.addCriteria("attackId", attackId);

		List<Object> res = crit.getResultList();

		Iterator<Object> iter = res.iterator();
		ArrayList<StatsEconomyPOJO> l = new ArrayList<>();
		while (iter.hasNext()) l.add((StatsEconomyPOJO) iter.next());

		return l;
	}
}
