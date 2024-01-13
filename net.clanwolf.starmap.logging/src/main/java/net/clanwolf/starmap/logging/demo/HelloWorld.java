///* ---------------------------------------------------------------- |
// *    ____ _____                                                    |
// *   / ___|___ /                   Communicate - Command - Control  |
// *  | |     |_ \                   MK V "Cerberus"                  |
// *  | |___ ___) |                                                   |
// *   \____|____/                                                    |
// *                                                                  |
// * ---------------------------------------------------------------- |
// * Info        : https://www.clanwolf.net                           |
// * GitHub      : https://github.com/ClanWolf                        |
// * ---------------------------------------------------------------- |
// * Licensed under the Apache License, Version 2.0 (the "License");  |
// * you may not use this file except in compliance with the License. |
// * You may obtain a copy of the License at                          |
// * http://www.apache.org/licenses/LICENSE-2.0                       |
// *                                                                  |
// * Unless required by applicable law or agreed to in writing,       |
// * software distributed under the License is distributed on an "AS  |
// * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
// * express or implied. See the License for the specific language    |
// * governing permissions and limitations under the License.         |
// *                                                                  |
// * C3 includes libraries and source code by various authors.        |
// * Copyright (c) 2001-2024, ClanWolf.net                            |
// * ---------------------------------------------------------------- |
// */
//package net.clanwolf.starmap.logging.demo;
//
//import java.io.File;
//import java.lang.invoke.MethodHandles;
//
//import net.clanwolf.starmap.logging.C3LogUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Logging framework to redirect several legacy apis to one logging target
// *
// * http://www.slf4j.org/legacy.html
// *
// * @author CHBARTEL
// *
// */
//public class HelloWorld {
//
////	TRACE – log events with this level are the most fine-grained and are usually not needed unless you need to have the full visibility of what is happening in your application and inside the third-party libraries that you use. You can expect the TRACE logging level to be very verbose.
////	DEBUG – less granular compared to the TRACE level, but still more than you will need in everyday use. The DEBUG log level should be used for information that may be needed for deeper diagnostics and troubleshooting.
////	INFO – the standard log level indicating that something happened, application processed a request, etc. The information logged using the INFO log level should be purely informative and not looking into them on a regular basis shouldn’t result in missing any important information.
////	WARN – the log level that indicates that something unexpected happened in the application. For example a problem, or a situation that might disturb one of the processes, but the whole application is still working.
////	ERROR – the log level that should be used when the application hits an issue preventing one or more functionalities from properly functioning. The ERROR log level can be used when one of the payment systems is not available, but there is still the option to check out the basket in the e-commerce application or when your social media logging option is not working for some reason. You can also see the ERROR log level associated with exceptions.
//
//	// Add parameter to JVM in order for logging.properties to be found:
//	// Djava.util.logging.config.file=logging.properties
//	// UPDATE: Needed -Djava.util.logging.config.file=src/main/resources/logging.properties to work
//
//	// This needs to be done in every class
//	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//
//	// This needs to be done in the main class once at startup to set the file handler for the logger
//	public static void prepareLogging() {
//		File dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3");
//		boolean res = dir.mkdirs();
//		if (res || dir.exists()) {
//			String logFileName = dir + File.separator + "starmap.log";
//			C3LogUtil.loadConfigurationAndSetLogFile(logFileName);
//		}
//	}
//
//	public static void main(String[] args) {
//
//		prepareLogging();
//
////		logger.trace("sdjkj");
////		logger.info("Temperature set to {}. Old temperature was {}.", 1, 12);
////		logger.info("Hello World");
////		logger.warn("test");
////		logger.error("jkjkjkj");
//	}
//}
