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
import net.clanwolf.starmap.server.persistence.pojos.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class StatsMwoDAO extends GenericDAO {

	private static StatsMwoDAO instance;

	private StatsMwoDAO() {
		// Empty constructor
	}

	public static StatsMwoDAO getInstance() {
		if (instance == null) {
			instance = new StatsMwoDAO();
			instance.className = "StatsMwoPOJO";
		}
		return instance;
	}

	@Override
	public void delete(Long statsMwoId, Object entity) {
		super.delete(statsMwoId, entity, ((StatsMwoPOJO) entity).getId());
	}

	@Override
	public StatsMwoPOJO findById(Long userId, Long id) {
		return (StatsMwoPOJO) super.findById(userId, StatsMwoPOJO.class, id);
	}

	@Override
	public StatsMwoPOJO update(Long statsMwoId, Object entity) {
		return (StatsMwoPOJO) super.update(statsMwoId, entity);
	}

	public StatsMwoPOJO findByMWOGameId(String matchId) {
		CriteriaHelper crit = new CriteriaHelper(StatsMwoPOJO.class);

		crit.addCriteria("gameId", matchId);
		return (StatsMwoPOJO) crit.getSingleResult();
	}
}
