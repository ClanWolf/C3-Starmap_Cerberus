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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackCharacterPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RoutePointPOJO;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class AttackCharacterDAO extends GenericDAO {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static AttackCharacterDAO instance;

	public static AttackCharacterDAO getInstance() {
		if (instance == null) {
			instance = new AttackCharacterDAO();
			instance.className = "ATTACKCHARACTER";
		}
		return instance;
	}

	private AttackCharacterDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((AttackCharacterPOJO) entity).getId());
	}

	@Override
	public AttackCharacterPOJO update(Long userID, Object entity) {
		return (AttackCharacterPOJO) super.update(userID, entity);
	}

	@Override
	public AttackCharacterPOJO findById(Long userID, Long id) {
		return (AttackCharacterPOJO) super.findById(userID, AttackCharacterPOJO.class, id);
	}


	/*
	 * Give all open attacks of a season back
	 */
	public ArrayList<AttackCharacterPOJO> getCharactersFromAttack(Long attackID){
		CriteriaHelper crit = new CriteriaHelper(AttackCharacterPOJO.class);
		crit.addCriteria("attackID", attackID );

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<AttackCharacterPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((AttackCharacterPOJO) iter.next());

		return lRPS;
	}



	public void deleteByAttackId(Long userID) {
		CriteriaHelper crit = new CriteriaHelper(AttackCharacterPOJO.class);
		crit.addCriteriaIsNull("attackID");

		List<Object> objectList = crit.getResultList(userID);

		Iterator i = objectList.iterator();
		while (i.hasNext()) {
			AttackCharacterPOJO p = (AttackCharacterPOJO) i.next();
			logger.info("Deleting: " + p.getId());
			delete(userID, p);
		}
	}
}
