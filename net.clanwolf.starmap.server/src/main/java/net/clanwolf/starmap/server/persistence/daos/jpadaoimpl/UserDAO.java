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

import io.nadron.util.Credentials;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayStoryPOJO;
import net.clanwolf.starmap.server.persistence.pojos.UserPOJO;
import net.clanwolf.starmap.server.util.Encryptor;
import net.clanwolf.starmap.transfer.enums.ROLEPLAYENTRYTYPES;


import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public class UserDAO extends GenericDAO {

	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static UserDAO instance;

	public static UserDAO getInstance() {
		if (instance == null) {
			instance = new UserDAO();
			instance.className = "UserPOJO";
		}
		return instance;
	}

	private UserDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((UserPOJO) entity).getUserId());
	}

	@Override
	public UserPOJO update(Long userID, Object entity) {
		return (UserPOJO) super.update(userID, entity);
	}

	@Override
	public UserPOJO findById(Long userID, Long id) {
		return (UserPOJO) super.findById(userID, UserPOJO.class, id);
	}

	public ArrayList<UserPOJO> getUserList() {
		CriteriaHelper crit = new CriteriaHelper(UserPOJO.class);
		crit.addCriteriaIsNotNull("id");
		List<Object> lRes = crit.getResultList();

		ArrayList<UserPOJO> lUser = new ArrayList<>();
		for (Object lRe : lRes) {
			UserPOJO up = (UserPOJO) lRe;
			lUser.add(up);
		}

		return lUser;
	}

	public UserPOJO findByCredentials(EntityManager em, Credentials c) {

		String pw1 = "";
		String pw2 = "";

		// Nur bei Regestrierung sinnvoll
		if(!c.getPassword().contains("#")){
			pw2 = c.getPassword();
		} else {
			pw1 = Encryptor.getPasswordFromPair("first", c.getPassword());
			pw2 = Encryptor.getPasswordFromPair("second", c.getPassword());
		}


//		logger.info("---- User: " + c.getUsername());
//		logger.info("---- Complete PW String: " + c.getPassword());
//		logger.info("---- PW1: " + pw1);
//		logger.info("---- PW2: " + pw2);

		CriteriaHelper crit1 = new CriteriaHelper(UserPOJO.class);
		crit1.addCriteria("userName", c.getUsername());
		crit1.addCriteria("userPassword", pw1);
		//crit1.addCriteria("active",1);

		Object o = crit1.getSingleResult();

		if (o == null) {
			CriteriaHelper crit2 = new CriteriaHelper(UserPOJO.class);
			crit2.addCriteria("userName", c.getUsername());
			crit2.addCriteria("userPasswordWebsite", pw2);

			o = crit2.getSingleResult();
		}

		return (UserPOJO) o;
	}

	public boolean userNameExists(EntityManager em, String userName){
		CriteriaHelper crit1 = new CriteriaHelper(UserPOJO.class);
		crit1.addCriteria("userName", userName);

		Object o = crit1.getSingleResult();

		return o != null;
	}

	public boolean userMailExists(EntityManager em, String userMail){
		CriteriaHelper crit1 = new CriteriaHelper(UserPOJO.class);
		crit1.addCriteria("userEMail", userMail);

		Object o = crit1.getSingleResult();

		return o != null;
	}
}
