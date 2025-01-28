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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayStoryPOJO;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class AttackDAO extends GenericDAO {

	private static AttackDAO instance;

	public static AttackDAO getInstance() {
		if (instance == null) {
			instance = new AttackDAO();
			instance.className = "AttackPOJO";
		}
		return instance;
	}

	private AttackDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((AttackPOJO) entity).getId());
	}

	@Override
	public AttackPOJO update(Long userID, Object entity) {
		return (AttackPOJO) super.update(userID, entity);
	}

	@Override
	public AttackPOJO findById(Long userID, Long id) {
		return (AttackPOJO) super.findById(userID, AttackPOJO.class, id);
	}

	public AttackPOJO findOpenAttackByRound(Long userID, Long jumpShipID, Long season, Long round) {
		CriteriaHelper crit = new CriteriaHelper(AttackPOJO.class);
		crit.addCriteria("jumpshipID",jumpShipID);
		crit.addCriteria("round", round);
		crit.addCriteria("season", season);
		crit.addCriteriaIsNull("factionID_Winner");

		return (AttackPOJO)crit.getSingleResult(userID);
	}

	/*
	 * Give all attacks back of a season back and a round
	 */
	public ArrayList<AttackPOJO> getAllAttacksOfASeasonForRound(Long season, int round){
		CriteriaHelper crit = new CriteriaHelper(AttackPOJO.class);
		crit.addCriteria("season", season );
		crit.addCriteria("round", round);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<AttackPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((AttackPOJO) iter.next());

		return lRPS;
	}

	/*
	 * Give all open attacks back of a season back and a round
	 */
	public ArrayList<AttackPOJO> getOpenAttacksOfASeasonForRound(Long season, int round){
		CriteriaHelper crit = new CriteriaHelper(AttackPOJO.class);
		crit.addCriteriaIsNull("factionID_Winner");
		crit.addCriteria("season", season );
		crit.addCriteria("round", round);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<AttackPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((AttackPOJO) iter.next());

		return lRPS;
	}

	/*
	 * Give all open attacks of a season back
	 */
	public ArrayList<AttackPOJO> getOpenAttacksOfASeason(Long season){
		CriteriaHelper crit = new CriteriaHelper(AttackPOJO.class);
		crit.addCriteriaIsNull("factionID_Winner");
		crit.addCriteria("season", season );

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<AttackPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((AttackPOJO) iter.next());

		return lRPS;
	}

	public ArrayList<AttackPOJO> getAllAttacksOfASeasonForRound(Long season, Long round){
		CriteriaHelper crit = new CriteriaHelper(AttackPOJO.class);
		crit.addCriteria("season", season );
		crit.addCriteria("round", round);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<AttackPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((AttackPOJO) iter.next());

		return lRPS;
	}

	public ArrayList<AttackPOJO> getAllAttacksOfASeasonForNextRound(Long season, Long round){
		CriteriaHelper crit = new CriteriaHelper(AttackPOJO.class);
		crit.addCriteria("season", season);
		crit.addCriteria("round", round + 1);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<AttackPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) lRPS.add((AttackPOJO) iter.next());

		return lRPS;
	}
}
