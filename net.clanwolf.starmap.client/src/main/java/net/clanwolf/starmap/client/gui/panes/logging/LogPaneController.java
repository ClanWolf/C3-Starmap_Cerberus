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

import com.ircclouds.irc.api.domain.messages.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.enums.PRIVILEGES;
import net.clanwolf.starmap.client.gui.panes.chat.ChatPane;
import net.clanwolf.starmap.client.net.irc.IRCClient;
import net.clanwolf.starmap.client.net.irc.NickChangeObject;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.sound.C3SoundPlayer;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.transfer.GameState;
import net.clanwolf.starmap.transfer.dtos.UserDTO;
import net.clanwolf.starmap.transfer.enums.GAMESTATEMODES;

import java.util.*;



// https://stackoverflow.com/questions/39366828/add-a-simple-row-to-javafx-tableview



public class LogPaneController implements ActionCallBackListener {

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
	TextFlow textFlowClientLog, textFlowServerLog;

	@FXML
	public void btnCloseClicked() {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
		LogPane.isVisible = false;
	}

	@FXML
	public void btnReportClicked() {
		// TODO: Report error
	}

	private static LogPaneController instance = null;

	public static void addClientLine(String line) {
		if (instance != null) {
			Platform.runLater(() -> {
				Text t1 = new Text();
				t1.setWrappingWidth(0);
				t1.setStyle("-fx-fill:#000000;");
				if (line.contains("DEBUG")) {
					t1.setStyle("-fx-fill:#669c35;");
				} else if (line.contains("ERROR") || line.contains("EXCEPTION")) {
					t1.setStyle("-fx-fill:#b51a00;");
				}
				t1.setText(line + "\r\n");
				instance.textFlowClientLog.getChildren().add(t1);
			});
		}
	}

	public static void addServerLine(String line) {
		if (instance != null) {
			Platform.runLater(() -> {
				Text t1 = new Text();
				t1.setWrappingWidth(0);
				t1.setStyle("-fx-fill:#000000;");
				t1.setText(line + "\r\n");
				instance.textFlowServerLog.getChildren().add(t1);
			});
		}
	}

	public void init(Locale locale) {
		labelDescription.setText(Internationalization.getString("LogDescription"));
		tabClientLog.setText(Internationalization.getString("LogClientTab"));
		tabServerLog.setText(Internationalization.getString("LogServerTab"));
		btnReport.setText(Internationalization.getString("LogButtonReport"));
		btnClose.setText(Internationalization.getString("LogButtonClose"));

		Nexus.getLogWatcher().run();
		this.instance = this;

		// Client log
		srollPaneClientLog.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		srollPaneClientLog.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		srollPaneClientLog.vvalueProperty().bind(textFlowClientLog.heightProperty());
		textFlowClientLog.setPrefWidth(Region.USE_COMPUTED_SIZE);

		// Server log
		srollPaneServerLog.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		srollPaneServerLog.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		srollPaneServerLog.vvalueProperty().bind(textFlowServerLog.heightProperty());
		textFlowServerLog.setPrefWidth(Region.USE_COMPUTED_SIZE);

		tabServerLog.setDisable(Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN));

		addActionCallBackListeners();
	}

	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
	}

	public void setStrings() {
		labelDescription.setText(Internationalization.getString("LogDescription"));
		tabClientLog.setText(Internationalization.getString("LogClientTab"));
		tabServerLog.setText(Internationalization.getString("LogServerTab"));
		btnReport.setText(Internationalization.getString("LogButtonReport"));
		btnClose.setText(Internationalization.getString("LogButtonClose"));
	}

	/**
	 * Handles actions.
	 *
	 * @param action incoming action to be handled
	 * @param o      the action object passed along with the action
	 * @return wether the handling should continue (this should be true in general)
	 */
	@Override
	public boolean handleAction(ACTIONS action, ActionObject o) {
		switch (action) {
			case CHANGE_LANGUAGE:
				setStrings();
				break;

			case PANE_DESTROY_CURRENT:
				break;

			case PANE_CREATION_BEGINS:
				break;

			case PANE_CREATION_FINISHED:
				break;

			default:
				break;
		}
		return true;
	}
}
