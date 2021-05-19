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

import net.clanwolf.starmap.client.net.HTTP;
import net.clanwolf.starmap.logging.C3Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class LogWatcher {
	Path logFile;
	private int lines = 0;
	private int characters = 0;
	private int startLine = 0;

	private Thread clientLogwatcherThread;
	private Thread serverLogwatcherThread;

	private LogWatcher() {

	}

	public LogWatcher(String logFileName) {
		logFile = Paths.get(logFileName);
	}

	public void run() {
		if (clientLogwatcherThread == null) {
			clientLogwatcherThread = new Thread("watcherThread") {
				public void run() {
					try {
						do {
							String entry = C3Logger.logHistory.poll();
							if (entry != null) {
								LogPaneController.addClientLine(entry);
							} else {
								Thread.sleep(800);
							}
							Thread.sleep(20);
						} while (!this.isInterrupted());
						clientLogwatcherThread = null;
					} catch (Exception ex) {
						ex.printStackTrace();
						C3Logger.error("Exception while scanning log entries [3761].", ex);
					}
				}
			};
		}
		clientLogwatcherThread.start();

		if (serverLogwatcherThread == null) {
			serverLogwatcherThread = new Thread("watcherThread") {
				public void run() {
					try {
						do {
							byte[] serverLog = HTTP.get("https://www.clanwolf.net/apps/C3/server/log/C3-Server.log.0");
							List<byte[]> lines = split(serverLog, "\\n".getBytes(StandardCharsets.UTF_8));
							for (byte[] line : lines) {
								String s = new String(line, StandardCharsets.UTF_8);
								if (s != null) {
									LogPaneController.addServerLine(s);
								}
							}
							Thread.sleep(10 * 1000);
						} while (!this.isInterrupted());
						serverLogwatcherThread = null;
					} catch (Exception ex) {
						ex.printStackTrace();
						C3Logger.error("Exception while scanning log entries [3761].", ex);
					}
				}
			};
		}
		serverLogwatcherThread.start();
	}

	public void stop() {
		clientLogwatcherThread.interrupt();
		serverLogwatcherThread.interrupt();
	}

	private List<byte[]> split(byte[] array, byte[] delimiter) {
		List<byte[]> byteArrays = new LinkedList<byte[]>();
		if (delimiter.length == 0) {
			return byteArrays;
		}
		int begin = 0;

		outer: for (int i = 0; i < array.length - delimiter.length + 1; i++) {
			for (int j = 0; j < delimiter.length; j++) {
				if (array[i + j] != delimiter[j]) {
					continue outer;
				}
			}

			// If delimiter is at the beginning then there will not be any data.
			if (begin != i)
				byteArrays.add(Arrays.copyOfRange(array, begin, i));
			begin = i + delimiter.length;
		}

		// delimiter at the very end with no data following?
		if (begin != array.length)
			byteArrays.add(Arrays.copyOfRange(array, begin, array.length));

		return byteArrays;
	}
}
