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
 * Copyright (c) 2001-2020, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.net;

/**
 * This class provides ongoing checks whether the connection is still available
 * and valid in order to prevent an action from facing a broken online or
 * database connection.
 *
 * @author Meldric
 */
public class ConnectionChecker {

	/**
	 * This class runs an endless loop and checks the connection status every
	 * couple of seconds (maybe about 45-60 seconds). It calls the check methods
	 * of the Server class and sets properties according to the results.
	 *
	 * It could also check if the credentials in the settings file has been
	 * changed during the runtime of the application. This could be an indicator
	 * for intrusion attempts.
	 */
	public ConnectionChecker() {

	}

	/**
	 * Method to start the connection checker loop.
	 */
	public void startConnectionChecker() {

	}

	/**
	 * Method to stop the connection checker loop.
	 */
	public void stopConnectionChecker() {

	}
}
