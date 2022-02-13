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
import net.clanwolf.starmap.server.persistence.pojos.AttackStatsPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayCharacterStatsPOJO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class RolePlayCharacterStatsDAO extends GenericDAO {

	private static RolePlayCharacterStatsDAO instance;

	private RolePlayCharacterStatsDAO() {
		// Empty constructor
	}

	public static RolePlayCharacterStatsDAO getInstance() {
		if (instance == null) {
			instance = new RolePlayCharacterStatsDAO();
			instance.className = "RolePlayCharacterStatsPOJO";
		}
		return instance;
	}

	@Override
	public void delete(Long roleplayCharacterStatsId, Object entity) {
		super.delete(roleplayCharacterStatsId, entity, ((RolePlayCharacterStatsPOJO) entity).getId());
	}

	@Override
	public RolePlayCharacterStatsPOJO findById(Long userId, Long id) {
		return (RolePlayCharacterStatsPOJO) super.findById(userId, RolePlayCharacterStatsPOJO.class, id);
	}

	@Override
	public RolePlayCharacterStatsPOJO update(Long roleplayCharacterStatsId, Object entity) {
		return (RolePlayCharacterStatsPOJO) super.update(roleplayCharacterStatsId, entity);
	}

	public RolePlayCharacterStatsPOJO findbyCharIdAndMatchId(Long charId, String matchId) {
		CriteriaHelper crit = new CriteriaHelper(AttackStatsPOJO.class);

		crit.addCriteria("roleplayCharacterId", charId);
		crit.addCriteria("mwoMatchId", matchId);
		return (RolePlayCharacterStatsPOJO) crit.getSingleResult();
	}
}
