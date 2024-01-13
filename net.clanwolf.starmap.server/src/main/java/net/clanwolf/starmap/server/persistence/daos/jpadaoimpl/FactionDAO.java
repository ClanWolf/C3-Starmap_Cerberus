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
 * Copyright (c) 2001-2024, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import jakarta.persistence.EntityManager;
import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackPOJO;
import net.clanwolf.starmap.server.persistence.pojos.FactionPOJO;
import net.clanwolf.starmap.server.persistence.pojos.UserPOJO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class FactionDAO extends GenericDAO {

	private static FactionDAO instance;

	public static FactionDAO getInstance() {
		if (instance == null) {
			instance = new FactionDAO();
			instance.className = "FactionPOJO";
		}
		return instance;
	}

	private FactionDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((FactionPOJO) entity).getId());
	}

	@Override
	public FactionPOJO update(Long userID, Object entity) {
		return (FactionPOJO) super.update(userID, entity);
	}

	@Override
	public FactionPOJO findById(Long userID, Long id) {
		return (FactionPOJO) super.findById(userID, FactionPOJO.class, id);
	}

	/*
	 * Give all open attacks of a season back
	 */
	public ArrayList<FactionPOJO> getAllFactions(){
		CriteriaHelper crit = new CriteriaHelper(FactionPOJO.class);

		crit.addCriteriaIsNotNull("id");
		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<FactionPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((FactionPOJO) iter.next());

		return lRPS;
	}

	public ArrayList<FactionPOJO> getAll_HH_Factions(){
		CriteriaHelper crit = new CriteriaHelper(FactionPOJO.class);
		crit.addCriteriaIsNotNull("id");

		ArrayList<Long> factionIdsHH = StarSystemDataDAO.getInstance().getAll_HH_FactionIds();
		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<FactionPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) {
			FactionPOJO f = (FactionPOJO) iter.next();
			if (factionIdsHH.contains(f.getId())) {
				lRPS.add(f);
			}
		}

		return lRPS;
	}

	public FactionPOJO getFactionByShortText(String shortText){

		CriteriaHelper crit1 = new CriteriaHelper(FactionPOJO.class);
		crit1.addCriteria("shortName", shortText);

		Object o = crit1.getSingleResult();

		return (FactionPOJO)o;

	}
}
