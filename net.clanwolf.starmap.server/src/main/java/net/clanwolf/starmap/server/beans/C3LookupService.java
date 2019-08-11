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
package net.clanwolf.starmap.server.beans;

import io.nadron.app.GameRoom;
import io.nadron.app.Player;
import io.nadron.service.impl.SimpleLookupService;
import io.nadron.util.Credentials;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;
import net.clanwolf.starmap.server.persistence.pojos.UserPOJO;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author domenico.colucci
 */
public class C3LookupService extends SimpleLookupService {

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

		C3Logger.debug("C3LookupService.playerLookup");
		C3Player player = new C3Player();
		EntityManager em = EntityManagerHelper.getEntityManager();

		// Database UserPOJO auth check.
		UserPOJO user = UserLogin.login(em, c);
		
		em.close();

		if (user != null) {
			player.setUser(user);
			EntityManagerHelper.getEntityManager((Long)player.getId());
			C3Logger.info("C3LookupService.playerLookup->LOG_IN_SUCCESSFUL->user: " + c.getUsername());

		} else {
			player.setUser(null);
		}

		return player;
	}

}
