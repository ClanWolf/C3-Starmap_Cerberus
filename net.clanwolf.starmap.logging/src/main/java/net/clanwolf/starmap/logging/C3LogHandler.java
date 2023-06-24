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
// * Copyright (c) 2001-2023, ClanWolf.net                            |
// * ---------------------------------------------------------------- |
// */
//package net.clanwolf.starmap.logging;
//
//import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.logging.Level;
//import java.util.logging.LogRecord;
//import java.util.logging.StreamHandler;
//
//public class C3LogHandler extends StreamHandler {
//	public static ConcurrentLinkedQueue<C3LogEntry> logHistory = new ConcurrentLinkedQueue<>();
//	private static int rowCounter = 1;
//
//	public C3LogHandler() {
//		setOutputStream(System.out);
//		setLevel(Level.ALL);
//	}
//
//	@Override
//	public void publish(LogRecord record) {
//		String logEntry = new java.util.logging.SimpleFormatter().format(record).replaceAll("\r\n", "");
//		//String logEntry = new net.clanwolf.starmap.logging.C3LogFormatter().format(record).replaceAll("\r\n", "");
//		String level = record.getLevel().getName();
//		if (!"WARNING".equalsIgnoreCase(level) && !"ERROR".equalsIgnoreCase(level) && !"SEVERE".equalsIgnoreCase(level)) {
//			level = "";
//		}
//		C3LogEntry entry = new C3LogEntry(rowCounter, level, logEntry);
//		logHistory.add(entry);
//		rowCounter++;
//		super.publish(record);
//		flush();
//	}
//
//	@Override
//	public void close() {
//		flush();
//	}
//}
