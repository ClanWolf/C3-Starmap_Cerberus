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
import net.clanwolf.starmap.server.persistence.pojos.FactionTypePOJO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class FactionTypeDAO extends GenericDAO {

	private static FactionTypeDAO instance;

	private FactionTypeDAO() {
		// Empty constructor
	}

	public static FactionTypeDAO getInstance() {
		if (instance == null) {
			instance = new FactionTypeDAO();
			instance.className = "FactionTypePOJO";
		}
		return instance;
	}

	@Override
	public void delete(Long userID, Object entity) {
		// NOT ALLOWED
		// super.delete(userID, entity, ((FactionTypePOJO) entity).getId());
	}

	@Override
	public FactionTypePOJO findById(Long userID, Long id) {
		return (FactionTypePOJO) super.findById(userID, FactionTypePOJO.class, id);
	}

	@Override
	public FactionTypePOJO update(Long userID, Object entity){
		// NOT ALLOWED
		return null;
	}

	/*
	 * Give all FactionTypes
	 */
	public ArrayList<FactionTypePOJO> getAllFactionTypes(){
		CriteriaHelper crit = new CriteriaHelper(FactionTypePOJO.class);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<FactionTypePOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((FactionTypePOJO) iter.next());

		return lRPS;
	}
}
