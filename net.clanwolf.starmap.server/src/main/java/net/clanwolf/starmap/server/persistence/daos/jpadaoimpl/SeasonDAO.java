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
import net.clanwolf.starmap.server.persistence.pojos.FactionPOJO;
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
public class SeasonDAO extends GenericDAO {

	private static SeasonDAO instance;

	public static SeasonDAO getInstance() {
		if (instance == null) {
			instance = new SeasonDAO();
			instance.className = "SeasonPOJO";
		}
		return instance;
	}

	private SeasonDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long seasonID, Object entity) {
		super.delete(seasonID, entity, ((SeasonPOJO) entity).getId());
	}

	@Override
	public SeasonPOJO update(Long seasonID, Object entity) {
		return (SeasonPOJO) super.update(seasonID, entity);
	}

	@Override
	public SeasonPOJO findById(Long userId, Long id) {
		if (userId == null) {
			return (SeasonPOJO) super.findById(SeasonPOJO.class, id);
		} else {
			return (SeasonPOJO) super.findById(userId, SeasonPOJO.class, id);
		}
	}

	/*
	 * Give all open seasons in the system
	 */
	public ArrayList<SeasonPOJO> getAllSeasons(){
		CriteriaHelper crit = new CriteriaHelper(SeasonPOJO.class);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<SeasonPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((SeasonPOJO) iter.next());

		return lRPS;
	}
}
