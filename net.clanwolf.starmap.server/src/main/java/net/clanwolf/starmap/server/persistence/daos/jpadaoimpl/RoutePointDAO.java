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
package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import net.clanwolf.starmap.server.persistence.pojos.AttackPOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.RoutePointPOJO;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class RoutePointDAO extends GenericDAO {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static RoutePointDAO instance;

	public static RoutePointDAO getInstance() {
		if (instance == null) {
			instance = new RoutePointDAO();
			instance.className = "RoutePointPOJO";
		}
		return instance;
	}

	private RoutePointDAO() {
		// Empty constructor
	}

	public void deleteByJumpshipId(Long userID, Long jumpshipId) {
		CriteriaHelper crit = new CriteriaHelper(RoutePointPOJO.class);
		crit.addCriteria("jumpshipId", jumpshipId);

		List<Object> objectList = crit.getResultList();

		for (Object o : objectList) {
			RoutePointPOJO p = (RoutePointPOJO) o;
			logger.info("Deleting: " + p.getId());
			delete(userID, p);
		}
	}

	public void deleteByJumpshipId(Long userID) {
		CriteriaHelper crit = new CriteriaHelper(RoutePointPOJO.class);
		crit.addCriteriaIsNull("jumpshipId");

		List<Object> objectList = crit.getResultList();

		for (Object o : objectList) {
			RoutePointPOJO p = (RoutePointPOJO) o;
			logger.info("Deleting: " + p.getId());
			delete(userID, p);
		}
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((RoutePointPOJO) entity).getId());
	}

	@Override
	public RoutePointPOJO update(Long userID, Object entity) {
		return (RoutePointPOJO) super.update(userID, entity);
	}

	@Override
	public RoutePointPOJO findById(Long userID, Long id) {
		return (RoutePointPOJO) super.findById(userID, RoutePointPOJO.class, id);
	}


	/*
	 * Give all attacks back of a season back and a round
	 */
	public ArrayList<RoutePointPOJO> getAllRoutePointsForSeasonForRound(Long season, int round){
		CriteriaHelper crit = new CriteriaHelper(RoutePointPOJO.class);
		crit.addCriteria("season", season );
		crit.addCriteria("round", round);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<RoutePointPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((RoutePointPOJO) iter.next());

		return lRPS;
	}
}
