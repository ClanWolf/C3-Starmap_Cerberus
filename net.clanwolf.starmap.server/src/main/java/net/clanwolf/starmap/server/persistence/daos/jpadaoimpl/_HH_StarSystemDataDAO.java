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
 * Copyright (c) 2001-2021, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackPOJO;
import net.clanwolf.starmap.server.persistence.pojos._HH_StarSystemDataPOJO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class _HH_StarSystemDataDAO extends GenericDAO {

	private static _HH_StarSystemDataDAO instance;

	public static _HH_StarSystemDataDAO getInstance() {
		if (instance == null) {
			instance = new _HH_StarSystemDataDAO();
			instance.className = "_HH_StarSystemDataPOJO";
		}
		return instance;
	}

	private _HH_StarSystemDataDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((AttackPOJO) entity).getId());
	}

	@Override
	public _HH_StarSystemDataPOJO update(Long userID, Object entity) {
		return (_HH_StarSystemDataPOJO) super.update(userID, entity);
	}

	@Override
	public _HH_StarSystemDataPOJO findById(Long userID, Long id) {
		return (_HH_StarSystemDataPOJO) super.findById(userID, AttackPOJO.class, id);
	}

	/*
	 * Give all open attacks of a season back
	 */
	public ArrayList<_HH_StarSystemDataPOJO> getAll_HH_StarSystemData(){
		CriteriaHelper crit = new CriteriaHelper(_HH_StarSystemDataPOJO.class);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<_HH_StarSystemDataPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((_HH_StarSystemDataPOJO) iter.next());

		return lRPS;
	}
}
