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

import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.UserSessionPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserSessionPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class UserSessionDAO extends GenericDAO {

	private static UserSessionDAO instance;

	private UserSessionDAO() {
		// Empty constructor
	}

	public static UserSessionDAO getInstance() {
		if (instance == null) {
			instance = new UserSessionDAO();
			instance.className = "UserSessionPOJO";
		}
		return instance;
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((UserSessionPOJO) entity).getUserId());
	}

	@Override
	public UserSessionPOJO findById(Long userID, Long id) {
		return (UserSessionPOJO) super.findById(userID, UserSessionPOJO.class, id);
	}

	public UserSessionPOJO getUserSessionByUserId(Long userId) {
		CriteriaHelper crit = new CriteriaHelper(UserSessionPOJO.class);
		crit.addCriteria("userId", userId);
		return (UserSessionPOJO) crit.getSingleResult();
	}

	@Override
	public UserSessionPOJO update(Long userID, Object entity) {
		return (UserSessionPOJO) super.update(userID, entity);
	}

	public ArrayList<UserSessionPOJO> getUserSessionList() {
		CriteriaHelper crit = new CriteriaHelper(UserSessionPOJO.class);
		crit.addCriteriaIsNotNull("id");

		List<Object> lRes = crit.getResultList();

		ArrayList<UserSessionPOJO> lUserSessions = new ArrayList<>();
		for (Object lRe : lRes) {
			UserSessionPOJO us = (UserSessionPOJO) lRe;
			lUserSessions.add(us);
		}

		return lUserSessions;
	}
}
