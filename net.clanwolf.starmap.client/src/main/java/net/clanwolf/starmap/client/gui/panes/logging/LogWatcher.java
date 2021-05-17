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
package net.clanwolf.starmap.client.gui.panes.logging;

import net.clanwolf.starmap.logging.C3Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Pattern;

public class LogWatcher {
	Path logFile;
	private int lines = 0;
	private int characters = 0;

	private LogWatcher() {

	}

	public LogWatcher(String logFileName) {
		logFile = Paths.get(logFileName);
	}

	public void run() {
		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();

			try (BufferedReader in = new BufferedReader(new FileReader(logFile.toFile()))) {
				String line;
				while ((line = in.readLine()) != null) {
					lines++;
					characters += line.length() + System.lineSeparator().length();
				}
			}

			logFile.toAbsolutePath().getParent().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);

			new Thread("watcherThread") {
				public void run() {
					try {
						do {
							WatchKey key = watcher.take();
							C3Logger.info("Scanning for changes in log file...");
							for (WatchEvent<?> event : key.pollEvents()) {
								WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
								Path path = pathEvent.context();
								C3Logger.info("-------------------------------------------------------- Path: " + path);
								if (path.equals(logFile)) {
									try (BufferedReader in = new BufferedReader(new FileReader(pathEvent.context().toFile()))) {
										String line;
										Pattern p = Pattern.compile("WARN|ERROR");
										in.skip(characters);
										while ((line = in.readLine()) != null) {
											lines++;
											characters += line.length() + System.lineSeparator().length();
											if (p.matcher(line).find()) {
												// Do something
												C3Logger.info("ääääääääääääääääää" + line);
											} else {
												C3Logger.info("ääääääääääääääääää222222" + line);
											}
										}
									}
								}
							}
							key.reset();
//							Thread.sleep(5000);
						} while (true);
					} catch (IOException | InterruptedException ex) {
						ex.printStackTrace();
						C3Logger.error("Exception while scanning logfile [3761].", ex);
					}
				}
			}.start();
		} catch(IOException ioe) {
			C3Logger.error("Exception while scanning logfile [3645].", ioe);
		}
	}
}
