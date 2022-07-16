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
 * Copyright (c) 2001-2022, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.StatsMwoPOJO;
import net.clanwolf.starmap.server.persistence.pojos.SysConfigPOJO;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class SysConfigDAO extends GenericDAO {

	private static SysConfigDAO instance;

	public static SysConfigDAO getInstance() {
		if (instance == null) {
			instance = new SysConfigDAO();
			instance.className = "SysConfigPOJO";
		}
		return instance;
	}

	private SysConfigDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((SysConfigPOJO) entity).getId());
	}

	@Override
	public SysConfigPOJO update(Long userID, Object entity) {
		return (SysConfigPOJO) super.update(userID, entity);
	}

	@Override
	public SysConfigPOJO findById(Long userID, Long id) {
		return (SysConfigPOJO) super.findById(userID, SysConfigPOJO.class, id);
	}

	public SysConfigPOJO findByKey(Long userID, String key) {
		CriteriaHelper crit = new CriteriaHelper(SysConfigPOJO.class);

		crit.addCriteria("key", key);
		return (SysConfigPOJO) crit.getSingleResult();
	}
}
