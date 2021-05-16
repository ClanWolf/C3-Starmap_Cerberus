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

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.clanwolf.starmap.client.enums.PRIVILEGES;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.UserDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.util.*;

public class LogPaneController {

	@FXML
	Label labelDescription;

	@FXML
	Tab tabClientLog;

	@FXML
	Tab tabServerLog;

	@FXML
	Button btnReport, btnClose;

	@FXML
	ScrollPane srollPaneClientLog, srollPaneServerLog;

	@FXML
	public void btnCloseClicked() {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
	}

	public void init(Locale locale) {

//		labelDescription.setText(Internationalization.getString("AdminSecurityDescription"));
//		tabPrivileges.setText(Internationalization.getString("AdminSecurityTabPrivileges"));
//		labelUser.setText(Internationalization.getString("AdminSecurityUserLabel"));
//		btnSave.setText(Internationalization.getString("AdminSecurityButtonSave"));
//		btnCancel.setText(Internationalization.getString("AdminSecurityButtonCancel"));

		VBox root = new VBox();

		// Client log
		srollPaneClientLog.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		srollPaneClientLog.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		srollPaneClientLog.setContent(root);

		// Server log
		srollPaneServerLog.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		srollPaneServerLog.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		srollPaneServerLog.setContent(root);

	}
}
