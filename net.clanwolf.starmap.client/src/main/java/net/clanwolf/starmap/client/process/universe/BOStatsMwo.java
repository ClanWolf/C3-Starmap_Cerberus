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
package net.clanwolf.starmap.client.process.universe;

import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.*;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;
import net.clanwolf.starmap.transfer.util.Compressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class BOStatsMwo {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private StatsMwoDTO statsMwoDTO;

	public BOStatsMwo(StatsMwoDTO stats) {
		this.statsMwoDTO = stats;
	}

	@SuppressWarnings("unused")
	public void storeStatsMwo() {
		GameState saveStatsMwoState = new GameState();
		saveStatsMwoState.setMode(GAMESTATEMODES.STATS_MWO_SAVE);
		byte[] compressedStatsMwoDTO = Compressor.compress(statsMwoDTO);
		logger.info("Compressed statsMwoDTO size: " + compressedStatsMwoDTO.length);
		saveStatsMwoState.addObject(compressedStatsMwoDTO);
		Nexus.fireNetworkEvent(saveStatsMwoState);
	}
}
