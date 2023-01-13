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
import net.clanwolf.starmap.server.persistence.pojos.StarSystemDataPOJO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class StarSystemDataDAO extends GenericDAO {

	private static StarSystemDataDAO instance;

	public static StarSystemDataDAO getInstance() {
		if (instance == null) {
			instance = new StarSystemDataDAO();
			instance.className = "StarSystemDataPOJO";
		}
		return instance;
	}

	private StarSystemDataDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((StarSystemDataPOJO) entity).getId());
	}

	@Override
	public StarSystemDataPOJO update(Long userID, Object entity) {
		return (StarSystemDataPOJO) super.update(userID, entity);
	}

	@Override
	public StarSystemDataPOJO findById(Long userID, Long id) {
		return (StarSystemDataPOJO) super.findById(userID, StarSystemDataPOJO.class, id);
	}

	/*
	 * Give all open attacks of a season back
	 */
	public ArrayList<StarSystemDataPOJO> getAll_HH_StarSystemData(){
		CriteriaHelper crit = new CriteriaHelper(StarSystemDataPOJO.class);

		crit.addCriteriaIsNotNull("id");

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<StarSystemDataPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((StarSystemDataPOJO) iter.next());

		return lRPS;
	}

	public ArrayList<Long> getAll_HH_FactionIds(){
		CriteriaHelper crit = new CriteriaHelper(StarSystemDataPOJO.class);

		crit.addCriteriaIsNotNull("id");

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<Long> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add(((StarSystemDataPOJO) iter.next()).getFactionID().getId());

		return lRPS;
	}
}
