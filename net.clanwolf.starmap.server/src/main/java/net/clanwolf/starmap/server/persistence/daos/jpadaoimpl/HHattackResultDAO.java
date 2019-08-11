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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.HHattackresultPOJO;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities. Transaction control of the save(), update() and delete() operations must be handled externally by senders of these methods or must be manually added to each of
 * these methods for data to be persisted to the JPA datastore.

 * @author Undertaker
 */
public class HHattackResultDAO extends GenericDAO {

	private static HHattackResultDAO instance;

	public static HHattackResultDAO getInstance() {
		if (instance == null) {
			instance = new HHattackResultDAO();
			instance.className = "UserPOJO";
		}
		return instance;
	}

	private HHattackResultDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((HHattackresultPOJO) entity).getId());
	}

	@Override
	public HHattackresultPOJO update(Long userID, Object entity) {
		return (HHattackresultPOJO) super.update(userID, entity);
	}

	@Override
	public HHattackresultPOJO findById(Long userID, Long id) {
		return (HHattackresultPOJO) super.findById(userID, HHattackresultPOJO.class, id);
	}
}
