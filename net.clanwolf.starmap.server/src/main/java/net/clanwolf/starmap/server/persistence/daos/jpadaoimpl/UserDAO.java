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

import io.nadron.util.Credentials;
import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.UserPOJO;

import javax.persistence.EntityManager;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities. Transaction control of the save(), update() and delete() operations must be handled externally by senders of these methods or must be manually added to each of
 * these methods for data to be persisted to the JPA datastore.
 */
public class UserDAO extends GenericDAO {

	private static UserDAO instance;

	public static UserDAO getInstance() {
		if (instance == null) {
			instance = new UserDAO();
			instance.className = "UserPOJO";
		}
		return instance;
	}

	private UserDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((UserPOJO) entity).getUserId());
	}

	@Override
	public UserPOJO update(Long userID, Object entity) {
		return (UserPOJO) super.update(userID, entity);
	}

	@Override
	public UserPOJO findById(Long userID, Long id) {
		return (UserPOJO) super.findById(userID, UserPOJO.class, id);
	}

	public UserPOJO findByCredentials(EntityManager em, Credentials c) {
		CriteriaHelper crit = new CriteriaHelper(UserPOJO.class);
		crit.addCriteria("userName", c.getUsername());
		crit.addCriteria("userPassword", c.getPassword());

		Object o = crit.getSingleResult();

		return (UserPOJO) o;
	}

}
