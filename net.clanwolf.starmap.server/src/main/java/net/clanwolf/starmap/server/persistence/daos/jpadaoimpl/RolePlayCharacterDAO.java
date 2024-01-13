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

import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayCharacterPOJO;
import net.clanwolf.starmap.server.persistence.pojos.UserPOJO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RolePlayCharacterDAO extends GenericDAO {

	private static RolePlayCharacterDAO instance;

	public static RolePlayCharacterDAO getInstance() {
		if (instance == null) {
			instance = new RolePlayCharacterDAO();
			instance.className = "RolePlayCharacterPOJO";
		}
		return instance;
	}

	private RolePlayCharacterDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object rolePlayCharacter) {
		super.delete(userID, rolePlayCharacter, ((RolePlayCharacterPOJO) rolePlayCharacter).getId());
	}

	@Override
	public RolePlayCharacterPOJO update(Long userID, Object entity) {
		return (RolePlayCharacterPOJO) super.update(userID, entity);
	}

	@Override
	public RolePlayCharacterPOJO findById(Long userID, Long id) {
		return (RolePlayCharacterPOJO) super.findById(userID, RolePlayCharacterPOJO.class, id);
	}

	public ArrayList<RolePlayCharacterPOJO> getAllCharacter(){
		CriteriaHelper crit = new CriteriaHelper(RolePlayCharacterPOJO.class);
		crit.addCriteriaIsNotNull("id");

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<RolePlayCharacterPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) {
			lRPS.add((RolePlayCharacterPOJO) iter.next());
		}

		return lRPS;
	}

	public ArrayList<RolePlayCharacterPOJO> getCharactersOfUser(UserPOJO user) {
		CriteriaHelper crit = new CriteriaHelper(RolePlayCharacterPOJO.class);
		crit.addCriteria("user", user);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<RolePlayCharacterPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) {
			lRPS.add((RolePlayCharacterPOJO) iter.next());
		}

		return lRPS;
	}
}
