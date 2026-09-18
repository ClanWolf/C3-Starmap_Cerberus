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
import net.clanwolf.starmap.server.persistence.pojos.ScorePOJO;
import net.clanwolf.starmap.server.persistence.pojos.SeasonPOJO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class ScoreDAO extends GenericDAO {

	private static ScoreDAO instance;

	public ScoreDAO() {
		// Empty constructor
	}

	public static ScoreDAO getInstance() {
		if (instance == null) {
			instance = new ScoreDAO();
			instance.className = "ScorePOJO";
		}
		return instance;
	}

	@Override
	public void delete(Long sessionID, Object entity) {
		super.delete(sessionID, entity, ((ScorePOJO) entity).getId());
	}

	@Override
	public ScorePOJO findById(Long userId, Long id) {
		if (userId == null) {
			return (ScorePOJO) super.findById(ScorePOJO.class, id);
		} else {
			return (ScorePOJO) super.findById(userId, ScorePOJO.class, id);
		}
	}

	@Override
	public ScorePOJO update(Long sessionID, Object entity) {
		return (ScorePOJO) super.update(sessionID, entity);
	}
}
