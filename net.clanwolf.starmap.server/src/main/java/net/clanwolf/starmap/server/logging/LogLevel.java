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
package net.clanwolf.starmap.server.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

//      DEBUG Level
//      This log4j level helps developer to debug application. Level of message logged will be focused on providing
//      support to a application developer.
//
//      INFO Level
//      This log4j level gives the progress and chosen state information. This level will be generally useful for end
//      user. This level is one level higher than DEBUG.
//
//      WARN Level
//      This log4j level gives a warning about an unexpected event to the user. The messages coming out of this level
//      may not halt the progress of the system.
//
//      ERROR Level
//      This log4j level gives information about a serious error which needs to be addressed and may result in unstable
//      state. This level is one level higher than WARN.
//
//      FATAL Level
//      This log4j level is straightforward and you do not get it quite often. Once you get this level and it indicates
//      application death.
//
//      ALL Level
//      This log4j level is used to turn on all levels of logging. Once this is configured and the levels are not
//      considered.
//
//      OFF Level
//      This log4j level is opposite to ALL level. It turns off all the logging.
//
//      TRACE Level
//      This log4j level gives more detailed information than the DEBUG level and sits top of the hierarchy. This level
//      is introduced from version 1.2.12 in log4j.

public class LogLevel {

	@SuppressWarnings("unused")
	public static void setSQLLevelOn()	{
		//Enable SQL logging
		Logger logger = LogManager.getLogger("org.hibernate.sql");
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(logger.getName());
		loggerConfig.setLevel(Level.INFO);
		ctx.updateLoggers();
	}

	@SuppressWarnings("unused")
	public static void setSQLLevelOff() {
		//Disable SQL logging
		Logger logger = LogManager.getLogger("org.hibernate.sql");
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		Configuration config = ctx.getConfiguration();
		LoggerConfig loggerConfig = config.getLoggerConfig(logger.getName());
		loggerConfig.setLevel(Level.OFF);
		ctx.updateLoggers();
	}
}
