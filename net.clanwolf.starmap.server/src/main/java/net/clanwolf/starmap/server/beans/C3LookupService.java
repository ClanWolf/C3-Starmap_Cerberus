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
package net.clanwolf.starmap.server.beans;

import io.nadron.app.GameRoom;
import io.nadron.app.Player;
import io.nadron.service.impl.SimpleLookupService;
import io.nadron.util.Credentials;
import jakarta.persistence.EntityManager;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.AttackDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.RolePlayStoryDAO;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.UserDAO;
import net.clanwolf.starmap.server.persistence.pojos.AttackPOJO;
import net.clanwolf.starmap.server.persistence.pojos.RolePlayStoryPOJO;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.pojos.UserPOJO;


import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author domenico.colucci
 */
public class C3LookupService extends SimpleLookupService {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private final Map<String, GameRoom> refKeyGameRoomMap;

	public C3LookupService() {
		refKeyGameRoomMap = new HashMap<>();
	}

	public C3LookupService(Map<String, GameRoom> refKeyGameRoomMap) {
		super();
		this.refKeyGameRoomMap = refKeyGameRoomMap;
	}

	@Override
	public GameRoom gameRoomLookup(Object gameContextKey) {
		return refKeyGameRoomMap.get(gameContextKey);
	}

	@Override
	public Player playerLookup(Credentials c) {

		logger.info("C3LookupService.playerLookup");
		C3Player player = new C3Player();
		EntityManager em = EntityManagerHelper.getNewEntityManager();

		// check register mode
		if (c.getUsername().contains("#")) {
			// 0: Username
			// 1: Mail
			// 2: Username Länge
			// 3: Faction short (CW, CJF, FRR,...)

			String[] p = c.getUsername().split("#");
			String sUsername = p[0];
			String sMail = p[1];
			String sLengthUsername = p[2];
			String sFactionShort = p[3];

			int lengthUsernameReal = c.getUsername().indexOf("#");
			int lengthUsernameTransmitted = Integer.parseInt(sLengthUsername);

			String pw1 = "Ba9cW5uZC48Lo";
			String pw2 = c.getPassword();

			logger.info("++++ Registering new user ++++");
			logger.info(sUsername);
			// logger.info(pw1);
			// logger.info(pw2);
			logger.info(sMail);
			logger.info(sFactionShort);
			logger.info("++++++++++++++++++++++++++++++");

			// Save user
			try {
				UserDAO userDAO = UserDAO.getInstance();
				EntityManagerHelper.beginTransaction(ServerNexus.DUMMY_USERID);

				UserPOJO u = new UserPOJO();
				u.setActive(0);
				u.setPrivileges(0);
				u.setUserName(sUsername);
				u.setUserEMail(sMail);
				u.setUserPassword(pw1);
				u.setUserPasswordWebsite(pw2);

				//userDAO.save(ServerNexus.DUMMY_USERID, u);
				EntityManagerHelper.commit(ServerNexus.DUMMY_USERID);
			} catch (Exception e) {
				logger.error("Exception while saving new user.", e);
			}

			return null;
		}

//		int lengthRegisterMail = c.getUsername().lastIndexOf("###");
//		if(lengthRegisterMail > 1) {
//
//			String faction = c.getUsername().substring(c.getUsername().lastIndexOf("#"));
//
//			int lengthUsername = c.getUsername().lastIndexOf("#");
//
//			String sUsername = c.getUsername().substring(0, lengthRegisterMail);
//			String sMail = c.getUsername().substring(lengthRegisterMail + 3, lengthUsername);
//			String pw1 = "Ba9cW5uZC48Lo";
//			logger.info(sMail);
//			// User in DB
//			return null;
//		}

		// Database UserPOJO auth check.
		UserPOJO user = UserLogin.login(em, c);

		em.close();

		if (user != null) {
			player.setUser(user);
			EntityManagerHelper.getEntityManager((Long)player.getId());
			logger.info("C3LookupService.playerLookup->LOG_IN_SUCCESSFUL->user: " + c.getUsername());

		} else {
			player.setUser(null);
			//player = null;
		}

		return player;
	}

}
