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
import net.clanwolf.starmap.mail.MailManager;
import net.clanwolf.starmap.server.GameServer;
import net.clanwolf.starmap.server.persistence.daos.jpadaoimpl.*;
import net.clanwolf.starmap.server.persistence.pojos.*;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;


import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
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

			FactionDAO fDAO = FactionDAO.getInstance();
			FactionPOJO factionPOJO = fDAO.getFactionByShortText(sFactionShort);

			int lengthUsernameReal = c.getUsername().indexOf("#");
			int lengthUsernameTransmitted = Integer.parseInt(sLengthUsername);

			String pw1 = ServerNexus.authProperties("c3Admin");
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

				if (userDAO.userNameExists(em, sUsername)) {
					// User with the same username already exists
					player.setErrorCode(1);
				} else if (userDAO.userMailExists(em, sMail)) {
					// User with the same mailadress already exists
					player.setErrorCode(2);
				}

				if( player.getErrorCode() == 0) {

					EntityManagerHelper.beginTransaction(ServerNexus.DUMMY_USERID);

					UserPOJO u = new UserPOJO();
					u.setActive(0);
					u.setPrivileges(0);
					u.setUserName(sUsername);
					u.setUserEMail(sMail);
					u.setUserPassword(pw1);
					u.setUserPasswordWebsite(pw2);

					userDAO.save(ServerNexus.DUMMY_USERID, u);

					RolePlayCharacterDAO rpCharDAO = RolePlayCharacterDAO.getInstance();

					RolePlayCharacterPOJO rpChar = new RolePlayCharacterPOJO();
					rpChar.setName(sUsername);
					rpChar.setUser(u);
					rpChar.setFactionId(factionPOJO.getId().intValue());

					JumpshipDAO jsDAO = JumpshipDAO.getInstance();
					ArrayList<JumpshipPOJO> js = jsDAO.getJumpshipsForFaction(factionPOJO.getId());

					if (js.size() > 0) {
						rpChar.setJumpshipId(js.get(0).getId().intValue());
					}

					rpCharDAO.save(ServerNexus.DUMMY_USERID, rpChar);

					u.setCurrentCharacter(rpChar);
					userDAO.save(ServerNexus.DUMMY_USERID, u);

					EntityManagerHelper.commit(ServerNexus.DUMMY_USERID);

					c.setUsername(u.getUserName());

					if (!ServerNexus.isDevelopmentPC) {
						sendMail(u);

						StringBuilder fs_de = new StringBuilder();
						StringBuilder fs_en = new StringBuilder();
						fs_de.append("Neuer Benutzer ").append(u.getUserName()).append(" (").append(factionPOJO.getShortName()).append(") hat sich registriert! Erwartet Bestätigung...\r\n");
						fs_en.append("New user ").append(u.getUserName()).append(" (").append(factionPOJO.getShortName()).append(") has registered! Confirmation pending...\r\n");

						ServerNexus.getEci().sendExtCom(fs_en.toString(), "en", true, true, true);
						ServerNexus.getEci().sendExtCom(fs_de.toString(), "de", true, true, true);
					}
				}
			} catch (Exception e) {
				logger.error("Exception while saving new user.", e);
				EntityManagerHelper.rollback(ServerNexus.DUMMY_USERID);
			}
		}

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

	private void sendMail(UserPOJO u){
		if(!GameServer.isDevelopmentPC) {
			logger.info("Sending info mail.");
			String[] receivers = {"keshik@googlegroups.com"};
			boolean sent = false;
			sent = MailManager.sendMail("c3@clanwolf.net", receivers, "New C3 user ", u.getUserName() + " is waiting for activation.", false);
			if (sent) {
				// sent
				logger.info("Mail sent. [3]");
			} else {
				// error during email sending
				logger.info("Error during mail dispatch. [3]");
			}
		}
	}
}
