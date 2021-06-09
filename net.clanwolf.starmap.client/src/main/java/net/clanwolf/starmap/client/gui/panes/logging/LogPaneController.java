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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.clanwolf.starmap.client.action.ACTIONS;
import net.clanwolf.starmap.client.action.ActionCallBackListener;
import net.clanwolf.starmap.client.action.ActionManager;
import net.clanwolf.starmap.client.action.ActionObject;
import net.clanwolf.starmap.client.enums.PRIVILEGES;
import net.clanwolf.starmap.client.net.FTP;
import net.clanwolf.starmap.client.net.IP;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.security.Security;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.logging.LogEntry;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

// https://stackoverflow.com/questions/39366828/add-a-simple-row-to-javafx-tableview
// OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL

public class LogPaneController implements ActionCallBackListener {

	public static boolean instantRefresh = false;

	@FXML
	Label labelDescription, labelCountdown;

	@FXML
	TextField logURL;

	@FXML
	Tab tabClientLog;

	@FXML
	Tab tabServerLog;

	@FXML
	ComboBox<Level> cbLevel;

	@FXML
	Button btnReport, btnClose, btnRefresh;

	@FXML
	TableView<LogEntry> tableViewClientLog, tableViewServerLog;

	@FXML
	public void btnCloseClicked() {
		Stage stage = (Stage) btnClose.getScene().getWindow();
		stage.close();
		LogPane.isVisible = false;
	}

	@FXML
	public void btnRefreshClicked() {
		instantRefresh = true;
	}

	private String encodeValue(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replace("+", "%20");
	}

	@FXML
	public void btnReportClicked() {
		// TODO: Report error to gitHub (?)
		// curl -X POST -u <UserName>:<Generated-Token>-d “{\”title\”: \”New welcome page\”,\”body\”: \”To design a new page\”,\”labels\”: [\”enhancement\”],\”milestone\”: \”3\”,\”assignees\”: [\”<user-name1>\”,\”<user-name2\”],\”state\”: \”open\”}” https://api.github.com/repos/<user-name>/<repo-name>/issues
		// https://www.softwaretestinghelp.com/github-rest-api-tutorial/#Labels_Milestones_Issues
		// https://stackoverflow.com/questions/13324052/github-issue-creation-api

		try {
			String ip = IP.getExternalIP();
			String timestamp = "" + System.currentTimeMillis();
			String logfilename = "clientlog_" + ip + "-" + timestamp + ".log";

			FTP ftpClient = new FTP(true);
			ftpClient.upload(C3Properties.getProperty(C3PROPS.LOGFILE) + ".0", logfilename, true);
			String serverUrl = C3Properties.getProperty(C3PROPS.SERVER_URL);

			String formattedBody = "Es ist ein Fehler in C3 aufgetreten!\n\nHochgeladene Log-Datei:\n" + serverUrl + "/errorlogs/" + logfilename + "\n\nHier Beschreibung ergänzen:\n\n";

			Desktop desktop;
			if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
				URI mailto;
				mailto = new URI("mailto:c3@clanwolf.net?subject=C3%20Fehler-Report&body=" + encodeValue(formattedBody));
				desktop.mail(mailto);
			} else {
				C3Logger.warning("Desktop does not support mailto!");
				throw new RuntimeException("Desktop does not support mailto!");
			}

			ftpClient.disconnect();
		} catch (URISyntaxException | IOException e) {
			// TODO: Handle error here
			e.printStackTrace();
			C3Logger.error("Error while uploading client log file during report process.");
		}
	}

	private static LogPaneController instance = null;

	public static void setLogURL(String url) {
		if (instance != null) {
			Platform.runLater(() -> instance.logURL.setText(url));
		}
	}

	public static Level getLevel() {
		if (instance != null) {
			Level l = instance.cbLevel.getSelectionModel().getSelectedItem();
			if (l == null) {
				return Level.ALL;
			} else {
				return l;
			}
		}
		return Level.ALL;
	}

	public static void setCountdownValue(int v) {
		if (instance != null) {
			Platform.runLater(() -> instance.labelCountdown.setText("" + v));
		}
	}

	public static void addClientLine(LogEntry line) {
		if (instance != null) {
			Platform.runLater(() -> instance.tableViewClientLog.getItems().add(line));
		}
	}

	public static void scrollClientDown() {
		if (instance != null) {
			Platform.runLater(() -> instance.tableViewClientLog.scrollTo(instance.tableViewClientLog.getItems().size()));
		}
	}

	public static void addServerLine(LogEntry line) {
		if (instance != null) {
			Platform.runLater(() -> instance.tableViewServerLog.getItems().add(line));
		}
	}

	public static void scrollServerDown() {
		if (instance != null) {
			Platform.runLater(() -> instance.tableViewServerLog.scrollTo(instance.tableViewServerLog.getItems().size()));
		}
	}

	public static void clearServerLog() {
		if (instance != null) {
			Platform.runLater(() -> instance.tableViewServerLog.getItems().clear());
		}
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
		clientLineNumberColumn.setMaxWidth(50);
		clientLineNumberColumn.setMinWidth(50);
		TableColumn<LogEntry, String> clientLevelColumn = new TableColumn<>("");
		clientLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
		clientLevelColumn.setPrefWidth(50);
		clientLevelColumn.setMaxWidth(50);
		clientLevelColumn.setMinWidth(50);
//		TableColumn<LogEntry, String> clientTimestampColumn = new TableColumn<>("");
//		clientTimestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
//      clientTimestampColumn.setPrefWidth(300);
		TableColumn<LogEntry, String> clientClassColumn = new TableColumn<>("");
		clientClassColumn.setCellValueFactory(new PropertyValueFactory<>("loggingClass"));
		clientClassColumn.setPrefWidth(250);
		clientClassColumn.setMaxWidth(250);
		clientClassColumn.setMinWidth(250);
		TableColumn<LogEntry, String> clientMethodColumn = new TableColumn<>("");
		clientMethodColumn.setCellValueFactory(new PropertyValueFactory<>("loggingClassMethod"));
		clientMethodColumn.setPrefWidth(80);
		clientMethodColumn.setMaxWidth(80);
		clientMethodColumn.setMinWidth(80);
		TableColumn<LogEntry, String> clientMessageColumn = new TableColumn<>("");
		clientMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
		clientMessageColumn.setPrefWidth(500);
		tableViewClientLog.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableViewClientLog.getColumns().addAll(clientLineNumberColumn,
				clientLevelColumn,
//				clientTimestampColumn,
				clientClassColumn,
				clientMethodColumn,
				clientMessageColumn
		);

		tableViewClientLog.setRowFactory(tableViewClientLog -> new TableRow<LogEntry>() {
			@Override
			protected void updateItem(LogEntry item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || item.getLevel() == null)
					setStyle("");
				else if (item.getLevel().equals("SEVERE"))
					setStyle("-fx-background-color: #ff856d;");
				else if (item.getLevel().equals("WARNING"))
					setStyle("-fx-background-color: #f9f9a5;");
				else
					setStyle("");
			}
		});

		// Server log
		TableColumn<LogEntry, Integer> serverLineNumberColumn = new TableColumn<>("");
		serverLineNumberColumn.setCellValueFactory(new PropertyValueFactory<>("lineNumber"));
		serverLineNumberColumn.setPrefWidth(50);
		serverLineNumberColumn.setMinWidth(50);
		serverLineNumberColumn.setMaxWidth(50);
		TableColumn<LogEntry, String> serverLevelColumn = new TableColumn<>("");
		serverLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
		serverLevelColumn.setPrefWidth(50);
		serverLevelColumn.setMaxWidth(50);
		serverLevelColumn.setMinWidth(50);
		TableColumn<LogEntry, String> serverTimestampColumn = new TableColumn<>("");
		serverTimestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
		serverTimestampColumn.setPrefWidth(110);
		serverTimestampColumn.setMaxWidth(110);
		serverTimestampColumn.setMinWidth(110);
		TableColumn<LogEntry, String> serverClassColumn = new TableColumn<>("");
		serverClassColumn.setCellValueFactory(new PropertyValueFactory<>("loggingClass"));
		serverClassColumn.setPrefWidth(250);
		serverClassColumn.setMaxWidth(250);
		serverClassColumn.setMinWidth(250);
		TableColumn<LogEntry, String> serverMethodColumn = new TableColumn<>("");
		serverMethodColumn.setCellValueFactory(new PropertyValueFactory<>("loggingClassMethod"));
		serverMethodColumn.setPrefWidth(80);
		serverMethodColumn.setMaxWidth(80);
		serverMethodColumn.setMinWidth(80);
		TableColumn<LogEntry, String> serverMessageColumn = new TableColumn<>("");
		serverMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
		serverMessageColumn.setPrefWidth(500);
		tableViewServerLog.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		tableViewServerLog.getColumns().addAll( serverLineNumberColumn,
												serverLevelColumn,
												serverTimestampColumn,
												serverClassColumn,
												serverMethodColumn,
												serverMessageColumn
		);

		tableViewServerLog.setRowFactory(tableViewServerLog -> new TableRow<LogEntry>() {
			@Override
			protected void updateItem(LogEntry item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || item.getLevel() == null)
					setStyle("");
				else if (item.getLevel().equals("SEVERE"))
					setStyle("-fx-background-color: #ff856d;");
				else if (item.getLevel().equals("WARNING"))
					setStyle("-fx-background-color: #f9f9a5;");
				else
					setStyle("");
			}
		});

		// OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL
		cbLevel.getItems().add(Level.OFF);
		cbLevel.getItems().add(Level.SEVERE);
		cbLevel.getItems().add(Level.WARNING);
		cbLevel.getItems().add(Level.INFO);
		cbLevel.getItems().add(Level.CONFIG);
		cbLevel.getItems().add(Level.FINE);
		cbLevel.getItems().add(Level.FINEST);
		cbLevel.getItems().add(Level.ALL);
		cbLevel.getSelectionModel().select(Level.ALL);

//		tabServerLog.setDisable(!Security.hasPrivilege(Nexus.getCurrentUser(), PRIVILEGES.ADMIN_IS_GOD_ADMIN));

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
