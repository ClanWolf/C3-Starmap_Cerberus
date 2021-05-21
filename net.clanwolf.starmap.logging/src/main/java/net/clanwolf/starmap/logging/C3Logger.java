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
package net.clanwolf.starmap.logging;

import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.*;

public class C3Logger {
	private static boolean initialized = false;
	private static boolean initializing = false;
	private static final Logger logger = Logger.getLogger(C3Logger.class.getName());
	private static FileHandler fileHandler;
	private static ConsoleHandler consoleHandler;
	private static String c3LogFileName = "";
	private static Level c3Loglevel = Level.FINEST;
	private static final int FILE_SIZE = 2*1024*1024;

	private static int rowCounter = 1;

	public static ConcurrentLinkedQueue<LogEntry> logHistory = new ConcurrentLinkedQueue<>();

	public static void print(String message) {
		info(message);
	}

	public static void info(String message) {
		logprint(Level.INFO, message, null);
	}

	public static void debug(String message) {
		logprint(Level.INFO, "DEBUG: " + message, null);
	}

	public static void warning(String message) {
		logprint(Level.WARNING, message, null);
	}

	public static void error(String message) { logprint(Level.SEVERE, "ERROR: " + message, null); }

	public static void error(String message, Throwable th) { logprint(Level.SEVERE, "ERROR: " + message, th); }

	public static void exception(String message, Throwable throwable) {
		if (message == null) {
			message = "Excepion occured: ";
		}
		logprint(Level.SEVERE, message, throwable);
		throwable.printStackTrace();
	}

	public static void setC3Logfile(String filename) {
		c3LogFileName = filename;
	}

	private static boolean prepareLogfile() {
		File c3LogFile;
		c3LogFile = new File(c3LogFileName);
		if (c3LogFile.isFile()) {
			return true;
		} else {
			try {
				boolean result = c3LogFile.createNewFile();
				if (result) {
					return true;
				} else {
					throw new Exception("Could not create log file!");
				}
			} catch(Exception e) {
				C3Logger.exception("Log file could not be created.", e);
			}
		}
		return false;
	}

	private static void logprint(Level level, String message, Throwable throwable) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String callerClassName = stackTraceElements[3].getClassName();
		String callerMethodName = stackTraceElements[3].getMethodName();

		if (!initialized && !initializing) {
			prepare();
		}

		if (throwable == null) {
			logger.logp(level, callerClassName, callerMethodName, message);
		} else {
			logger.logp(level, callerClassName, callerMethodName, message, throwable);
		}

		if (fileHandler != null) {
			fileHandler.flush();
		}

		LogEntry logEntry = new LogEntry(rowCounter, level.toString(), "", callerClassName, callerMethodName, message);
		logHistory.add(logEntry);
		rowCounter++;
	}

	public static String fixedLengthString(String string, int length) {
		return String.format("%1$" + length + "s", string);
	}

	public static void setC3LogLevel(Level level) {
		c3Loglevel = level;
	}

	public static Level getC3LogLevel() {
		return c3Loglevel;
	}

	private static void prepare() {
		initializing = true;
		// Logger setup
		try {
			LogManager.getLogManager().readConfiguration(C3Logger.class.getClassLoader().getResourceAsStream("log.properties"));
			C3Formatter c3formatter = new C3Formatter();

			logger.setLevel(c3Loglevel);
			logger.setUseParentHandlers(false);

			consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(c3formatter);
			consoleHandler.setLevel(Level.FINEST);
			logger.addHandler(consoleHandler);

			if (prepareLogfile()) {
				// fileHandler = new FileHandler(c3LogFileName, FILE_SIZE, 5, true); // appending to the previous logs
				fileHandler = new FileHandler(c3LogFileName, FILE_SIZE, 5, false) {  // a new file for every start
					@Override
					public synchronized void publish(final LogRecord record) {
						super.publish(record);
						super.flush();
					}
				};
				fileHandler.setFormatter(c3formatter);
				fileHandler.setEncoding("UTF-8");
				fileHandler.setLevel(Level.FINEST);
				logger.addHandler(fileHandler);
			}

			System.setErr(new PrintStream(new C3OutputStream(logger, Level.FINEST)));
			System.setOut(new PrintStream(new C3OutputStream(logger, Level.FINEST)));

			initialized = true;
		} catch (Exception e) {
			System.out.println("C3Logger initialization failed: ");
			e.printStackTrace();
		}
	}

	public static void shutdown() {
		// Closed in shutdown hook of main app
		try {
			if (fileHandler != null) {
				fileHandler.close();
			}
			if (consoleHandler != null) {
				consoleHandler.close();
			}
		} catch (Exception e) {
			// do nothing, shutting down
		}
	}
}
