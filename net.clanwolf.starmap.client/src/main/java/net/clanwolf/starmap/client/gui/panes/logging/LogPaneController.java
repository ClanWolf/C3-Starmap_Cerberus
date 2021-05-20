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
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.enums.PRIVILEGES;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.logging.LogEntry;

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
	TableView<LogEntry> tableViewClientLog, tableViewServerLog;

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

	public static void addClientLine(LogEntry line) {
		if (instance != null) {
			Platform.runLater(() -> {
//				if (!instance.tableViewClientLog.getItems().contains(line)) {
					instance.tableViewClientLog.getItems().add(line);
//				}
			});
		}
	}

	public static void scrollClientDown() {
		if (instance != null) {
			Platform.runLater(() -> {
				instance.tableViewClientLog.scrollTo(instance.tableViewClientLog.getItems().size());
			});
		}
	}

	public static void addServerLine(LogEntry line) {
		if (instance != null) {
			Platform.runLater(() -> {
//				if (!instance.tableViewServerLog.getItems().contains(line)) {
					instance.tableViewServerLog.getItems().add(line);
//				}
			});
		}
	}

	public static void scrollServerDown() {
		if (instance != null) {
			Platform.runLater(() -> {
				instance.tableViewServerLog.scrollTo(instance.tableViewServerLog.getItems().size());
			});
		}
	}

	public static void clearServerLog() {
		instance.tableViewServerLog.getItems().clear();
	}

	public void init() {
		labelDescription.setText(Internationalization.getString("LogDescription"));
		tabClientLog.setText(Internationalization.getString("LogClientTab"));
		tabServerLog.setText(Internationalization.getString("LogServerTab"));
		btnReport.setText(Internationalization.getString("LogButtonReport"));
		btnClose.setText(Internationalization.getString("LogButtonClose"));

		Nexus.getLogWatcher().run();
		instance = this;

		// Client log
		TableColumn<LogEntry, Integer> clientLineNumberColumn = new TableColumn<>("");
		clientLineNumberColumn.setCellValueFactory(new PropertyValueFactory<>("lineNumber"));
		clientLineNumberColumn.setPrefWidth(50);
		TableColumn<LogEntry, String> clientLevelColumn = new TableColumn<>("");
		clientLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
		clientLevelColumn.setPrefWidth(50);
//		TableColumn<LogEntry, String> clientTimestampColumn = new TableColumn<>("");
//		clientTimestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
//      clientTimestampColumn.setPrefWidth(300);
		TableColumn<LogEntry, String> clientClassColumn = new TableColumn<>("");
		clientClassColumn.setCellValueFactory(new PropertyValueFactory<>("loggingClass"));
		clientClassColumn.setPrefWidth(250);
		TableColumn<LogEntry, String> clientMethodColumn = new TableColumn<>("");
		clientMethodColumn.setCellValueFactory(new PropertyValueFactory<>("loggingClassMethod"));
		clientMethodColumn.setPrefWidth(80);
		TableColumn<LogEntry, String> clientMessageColumn = new TableColumn<>("");
		clientMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
		clientMessageColumn.setPrefWidth(500);
		tableViewClientLog.getColumns().addAll(clientLineNumberColumn,
				clientLevelColumn,
//				clientTimestampColumn,
				clientClassColumn,
				clientMethodColumn,
				clientMessageColumn
		);

		// Server log
		TableColumn<LogEntry, Integer> serverLineNumberColumn = new TableColumn<>("");
		serverLineNumberColumn.setCellValueFactory(new PropertyValueFactory<>("lineNumber"));
		serverLineNumberColumn.setPrefWidth(50);
		TableColumn<LogEntry, String> serverLevelColumn = new TableColumn<>("");
		serverLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
		serverLevelColumn.setPrefWidth(50);
		TableColumn<LogEntry, String> serverTimestampColumn = new TableColumn<>("");
		serverTimestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		serverTimestampColumn.setPrefWidth(110);
		TableColumn<LogEntry, String> serverClassColumn = new TableColumn<>("");
		serverClassColumn.setCellValueFactory(new PropertyValueFactory<>("loggingClass"));
		serverClassColumn.setPrefWidth(250);
		TableColumn<LogEntry, String> serverMethodColumn = new TableColumn<>("");
		serverMethodColumn.setCellValueFactory(new PropertyValueFactory<>("loggingClassMethod"));
		serverMethodColumn.setPrefWidth(80);
		TableColumn<LogEntry, String> serverMessageColumn = new TableColumn<>("");
		serverMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
		serverMessageColumn.setPrefWidth(500);
		tableViewServerLog.getColumns().addAll(serverLineNumberColumn,
				serverLevelColumn,
				serverTimestampColumn,
				serverClassColumn,
				serverMethodColumn,
				serverMessageColumn
		);

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

			default:
				break;
		}
		return true;
	}
}
