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
import net.clanwolf.starmap.server.persistence.pojos.C3GameConfigPOJO;
import net.clanwolf.starmap.server.persistence.pojos.JumpshipPOJO;
import net.clanwolf.starmap.server.persistence.pojos.SysConfigPOJO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class C3GameConfigDAO extends GenericDAO {

	private static C3GameConfigDAO instance;

	public static C3GameConfigDAO getInstance() {
		if (instance == null) {
			instance = new C3GameConfigDAO();
			instance.className = "C3GameConfigPOJO";
		}
		return instance;
	}

	private C3GameConfigDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((C3GameConfigPOJO) entity).getId());
	}

	@Override
	public C3GameConfigPOJO update(Long userID, Object entity) {
		return (C3GameConfigPOJO) super.update(userID, entity);
	}

	@Override
	public C3GameConfigPOJO findById(Long userID, Long id) {
		return (C3GameConfigPOJO) super.findById(userID, C3GameConfigPOJO.class, id);
	}

	public C3GameConfigPOJO findByKey(Long userID, String key) {
		CriteriaHelper crit = new CriteriaHelper(C3GameConfigPOJO.class);
		crit.addCriteria("key", key);

		return (C3GameConfigPOJO) crit.getSingleResult();
	}

	public ArrayList<C3GameConfigPOJO> getAllGameConfigValues(){
		CriteriaHelper crit = new CriteriaHelper(C3GameConfigPOJO.class);
		crit.addCriteriaIsNotNull("id");

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<C3GameConfigPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) {
			lRPS.add((C3GameConfigPOJO) iter.next());
		}

		return lRPS;
	}
}
