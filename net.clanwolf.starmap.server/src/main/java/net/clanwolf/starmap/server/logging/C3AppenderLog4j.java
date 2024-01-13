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
//package net.clanwolf.starmap.server.logging;
//
//import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.logging.log4j.core.Appender;
//import org.apache.logging.log4j.core.Core;
//import org.apache.logging.log4j.core.Filter;
//import org.apache.logging.log4j.core.LogEvent;
//import org.apache.logging.log4j.core.appender.AbstractAppender;
//import org.apache.logging.log4j.core.config.plugins.Plugin;
//import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
//import org.apache.logging.log4j.core.config.plugins.PluginElement;
//import org.apache.logging.log4j.core.config.plugins.PluginFactory;
//
//import java.time.Instant;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//@Deprecated
//@Plugin(name = "C3AppenderLog4j", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
//public class C3AppenderLog4j extends AbstractAppender {
//
//	private ConcurrentMap<String, LogEvent> eventMap = new ConcurrentHashMap<>();
//
//	protected C3AppenderLog4j(String name, Filter filter) {
//		super(name, filter, null);
//	}
//
//	@PluginFactory
//	public static C3AppenderLog4j createAppender(
//			@PluginAttribute("name") String name,
//			@PluginElement("Filter") Filter filter) {
//		return new C3AppenderLog4j(name, filter);
//	}
//
//	@Override
//	public void append(LogEvent event) {
//		eventMap.put(Instant.now().toString(), event);
//		logger.info(event.getMessage().getFormattedMessage());
//	}
//}
//
//// Changed in december 2021 due to global alarm of log4j vulnarability
//
////public class C3AppenderLog4j extends AppenderSkeleton {
////
////	@Override
////	protected void append(LoggingEvent event) {
////		logger.info(event.getRenderedMessage());
////	}
////
////	public void close() {
////
////	}
////
////	public boolean requiresLayout() {
////		return false;
////	}
////}
