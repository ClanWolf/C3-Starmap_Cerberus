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
package net.clanwolf.starmap.client.mwo;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.TimerTask;

/**
 * @author Meldric
 */
public class CheckClipboardForMwoApi extends TimerTask {

	private String previousContent = "";
	private String currentContent = "";

	public CheckClipboardForMwoApi() {
		previousContent = "";
		currentContent = "";
	}

	public void getMWOGameStats(String gameid) {
		final Properties auth = new Properties();
		try {
			final String authFileName = "auth.properties";
			InputStream inputStream = CheckClipboardForMwoApi.class.getClassLoader().getResourceAsStream(authFileName);
			if (inputStream != null) {
				auth.load(inputStream);
			} else {
				throw new FileNotFoundException("Auth-Property file '" + authFileName + "' not found in classpath.");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		String url = "https://mwomercs.com/api/v1/matches/" + gameid + "?api_token=" + auth.getProperty("mwo_api_key");
	}

	@Override
	public void run() {
		Platform.runLater(() -> {
			Clipboard cb = Clipboard.getSystemClipboard();
			currentContent = cb.getString();

			if (previousContent == null) {
				previousContent = "";
			}

			if (!previousContent.equals(currentContent)) {
				if (currentContent != null) {
					if (currentContent.contains("")) {
						System.out.println("From clipboard: " + currentContent + " (Length: " + currentContent.length() + ")");

						if (currentContent.length() == 15) {
							getMWOGameStats(currentContent);
						}
					}
				}
			}

			previousContent = currentContent;
		});
	}
}
