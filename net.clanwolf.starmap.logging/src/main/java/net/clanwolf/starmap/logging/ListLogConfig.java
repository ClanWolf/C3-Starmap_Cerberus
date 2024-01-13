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
 * Copyright (c) 2001-2024, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.logging;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ListLogConfig {

	private static final Logger log = Logger.getLogger("test");

	public static void main(String[] a) {
		log.log(Level.FINEST, "Finest");
		log.log(Level.FINER, "FINER");
		log.log(Level.FINE, "FINE");
		log.log(Level.CONFIG, "CONFIG");
		log.log(Level.INFO, "INFO");
		log.log(Level.WARNING, "WARNING");
		log.log(Level.SEVERE, "SEVERE");
		log.finest("Finest Log");
		log.finer("Finer Log");
		log.fine("Fine Log");
		log.config("Config Log");
		log.info("Info Log");
		log.warning("Warning Log");
		log.severe("Severe Log");
		printConfig(System.err);
	}

	private static void printConfig(PrintStream ps) {
		String cname = System.getProperty("java.util.logging.config.class");
		if (cname != null) {
			try {
				ClassLoader sys = ClassLoader.getSystemClassLoader();
				Class<?> c = Class.forName(cname, false, sys);
				ps.println(sys.getClass().getName() +" found log configuration class " + c.getName());
			} catch (LinkageError | ClassNotFoundException | RuntimeException cnfe) {
				ps.println("Unable to load " + cname);
				cnfe.printStackTrace(ps);
			}
		} else {
			ps.println("java.util.logging.config.class was null");
		}

		String file = System.getProperty("java.util.logging.config.file");
		if (file != null) {
			ps.println("java.util.logging.config.file=" + file);
			try {
				ps.println("CanonicalPath=" + new File(file).getCanonicalPath());
			} catch (RuntimeException | IOException ioe) {
				ps.println("Unable to resolve path for " + file);
				ioe.printStackTrace(ps);
			}

			try {
				Path p = Paths.get(file);
				if (Files.isReadable(p)) {
					ps.println(file + " is readable and has size " + Files.size(p));
				} else {
					if (Files.exists(p)) {
						ps.println(file + " exists for " + System.getProperty("user.name") + " but is not readable.");
					} else {
						ps.println(file + " doesn't exist for " + System.getProperty("user.name"));
					}
				}
			} catch (RuntimeException | IOException ioe) {
				ps.println("Unable to read " + file);
				ioe.printStackTrace(ps);
			}
		} else {
			ps.println("java.util.logging.config.file was null");
		}

		LogManager lm = LogManager.getLogManager();
		ps.append("LogManager=").println(lm.getClass().getName());
		synchronized (lm) {
			Enumeration<String> e = lm.getLoggerNames();
			while (e.hasMoreElements()) {
				Logger l = lm.getLogger(e.nextElement());
				if (l != null) {
					print(l, ps);
				}
			}
		}
	}

	private static void print(Logger l, PrintStream ps) {
		String scn = l.getClass().getSimpleName();
		ps.append("scn=").append(scn).append(", n=").append(l.getName())
				.append(", uph=").append(String.valueOf(l.getUseParentHandlers()))
				.append(", l=").append(String.valueOf(l.getLevel()))
				.append(", fl=").println(l.getFilter());
		for (Handler h : l.getHandlers()) {
			ps.append("\t").append(l.getName()).append("->")
					.append(h.getClass().getName()).append(", h=")
					.append(String.valueOf(h.getLevel())).append(", fl=")
					.append(String.valueOf(h.getFilter())).println();
		}
	}
}
