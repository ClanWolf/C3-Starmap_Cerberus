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
 * Copyright (c) 2001-2022, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.beans;

import io.nadron.app.GameRoom;
import io.nadron.app.PlayerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.util.WebDataInterface;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.UniverseDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.util.Compressor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;

class C3GameSessionHandlerUniverse {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	static void getUniverseData(PlayerSession session, GameRoom gm) {
		logger.info("Getting universe data on request of a client.");
		UniverseDTO universe = WebDataInterface.getUniverse();

		GameState state_universe = new GameState(GAMESTATEMODES.GET_UNIVERSE_DATA);
		state_universe.addObject(Compressor.compress(universe));
		C3GameSessionHandler.sendBroadCast(gm, state_universe);
	}
}
