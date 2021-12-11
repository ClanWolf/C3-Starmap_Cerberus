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
package net.clanwolf.starmap.client.util;

import javafx.application.Platform;
import javafx.scene.input.Clipboard;
import java.util.TimerTask;

/**
 * @author Meldric
 */
public class CheckClipboardForMwoApi extends TimerTask {

	// https://mwomercs.com/api/v1/matches/312013716556150
	// game id: 312013716556150

	private String previousContent = "";
	private String currentContent = "";

	public CheckClipboardForMwoApi() {
		previousContent = "";
		currentContent = "";
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
						// TODO: Do something with the copied game-id
						System.out.println("From clipboard: " + currentContent);
					}
				}
			}

			previousContent = currentContent;
		});
	}
}
