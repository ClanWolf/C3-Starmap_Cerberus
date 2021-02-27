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
 * Copyright (c) 2001-2021, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.beans;

import io.nadron.app.GameRoom;
import io.nadron.app.PlayerSession;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.server.util.WebDataInterface;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.UniverseDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.util.Iterator;

class C3GameSessionHandlerUniverse {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static void getUniverseData(PlayerSession session, GameRoom gm) {
		C3Logger.print("Getting universe data on request of a client.");
		UniverseDTO universe = WebDataInterface.getUniverse();

		// TODO: Broadcast new UniverseDTO to logged in (HH) clients
		Iterator<PlayerSession> pl = gm.getSessions().iterator();
		while (pl.hasNext()) {
			PlayerSession plSession = pl.next();
			GameState state_universe = new GameState(GAMESTATEMODES.GET_UNIVERSE_DATA);
			state_universe.addObject(universe);
			// state_universe.setReceiver();

//			gm.sendBroadcast(Events.networkEvent(state_universe));
			C3GameSessionHandler.sendBroadCast(gm, state_universe);
		}
	}
}
