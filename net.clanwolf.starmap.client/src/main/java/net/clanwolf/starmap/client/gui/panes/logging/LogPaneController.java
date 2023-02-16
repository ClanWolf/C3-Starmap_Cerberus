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
 * Copyright (c) 2001-2023, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.gui.panes.logging;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.clanwolf.starmap.client.action.*;
import net.clanwolf.starmap.client.enums.C3FTPTYPES;
import net.clanwolf.starmap.client.gui.panes.AbstractC3Pane;
import net.clanwolf.starmap.client.gui.panes.rp.RolePlayBasicPane;
import net.clanwolf.starmap.client.net.FTP;
import net.clanwolf.starmap.client.net.IP;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.Tools;
import net.clanwolf.starmap.logging.C3LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

// https://stackoverflow.com/questions/39366828/add-a-simple-row-to-javafx-tableview

public class LogPaneController implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	public static boolean instantRefresh = false;
	public static boolean clientScrolledDown = false;
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
	Button btnReport, btnClose, btnRefresh, btnEditor;

	public static boolean logAutoscrolldown = true;
	private static LogPaneController instance = null;

	@FXML
	TableView<C3LogEntry> tableViewClientLog, tableViewServerLog;

	@FXML
	CheckBox cbAutoScroll;

	public static void setLogURL(String url) {
		if (instance != null) {
			Platform.runLater(() -> instance.logURL.setText(url));
		}
	}

	public static Level getLevel() {
		if (instance != null) {
			Level l = instance.cbLevel.getSelectionModel().getSelectedItem();
			if (l == null) {
				return Level.INFO;
			} else {
				return l;
			}
		}
		return Level.INFO;
	}

	public static void setCountdownValue(int v) {
		if (instance != null) {
			Platform.runLater(() -> instance.labelCountdown.setText("" + v));
		}
	}

	public static void addClientLine(C3LogEntry line) {
		if (instance != null) {
			Platform.runLater(() -> instance.tableViewClientLog.getItems().add(line));
		}
	}

	public static void scrollClientDown() {
		if (instance != null) {
			Platform.runLater(() -> instance.tableViewClientLog.scrollTo(instance.tableViewClientLog.getItems().size()));
		}
	}

	public static void addServerLine(C3LogEntry line) {
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

	@FXML
	public void onServerLogScrollToggle() {
		logAutoscrolldown = cbAutoScroll.isSelected();
	}

	@FXML
	public void btnEditLogFile() {
		File logFile = new File(C3Properties.getProperty(C3PROPS.LOGFILE) + ".0");
		try {
			Desktop.getDesktop().open(logFile);
		} catch (IOException e) {
			logger.error("Could not open local log file for editing!", e);
		}
	}

	@FXML
	public void btnCloseClicked() {
		if (btnClose.getScene() != null) {
			Stage stage = (Stage) btnClose.getScene().getWindow();
			stage.close();
		}
		LogPane.isVisible = false;
	}

	@FXML
	public void btnRefreshClicked() {
		instantRefresh = true;
	}

	@FXML
	public void btnReportClicked() {
		// TODO_C3: Report error to gitHub (?)
		// curl -X POST -u <UserName>:<Generated-Token>-d “{\”title\”: \”New welcome page\”,\”body\”: \”To design a new page\”,\”labels\”: [\”enhancement\”],\”milestone\”: \”3\”,\”assignees\”: [\”<user-name1>\”,\”<user-name2\”],\”state\”: \”open\”}” https://api.github.com/repos/<user-name>/<repo-name>/issues
		// https://www.softwaretestinghelp.com/github-rest-api-tutorial/#Labels_Milestones_Issues
		// https://stackoverflow.com/questions/13324052/github-issue-creation-api

		String serverName = "";
		try {
			serverName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		String ip = IP.getExternalIP();
		String formattedTimestamp = new SimpleDateFormat("yyyyMMdd-HH:mm:ss").format(System.currentTimeMillis());
		String username = "";
		if (Nexus.getCurrentUser() != null) {
			username = Nexus.getCurrentUser().getUserName();
		}
		String logfilename = "";
		if (!"".equals(username)) {
			logfilename = formattedTimestamp + "_" + serverName + "(" + ip + ")-" + username + ".c3log";
		} else {
			logfilename = formattedTimestamp + "_" + serverName + "(" + ip + ").c3log";
		}

		StatusTextEntryActionObject o = new StatusTextEntryActionObject("Uploading logfile...", false);
		ActionManager.getAction(ACTIONS.SET_STATUS_TEXT).execute(o);
		FTP ftpClient = new FTP(C3FTPTYPES.FTP_LOGUPLOAD);
		try {
			ftpClient.upload(C3Properties.getProperty(C3PROPS.LOGFILE) + ".0", logfilename);

			String serverUrl = C3Properties.getProperty(C3PROPS.SERVER_URL);
			String formattedBody = "Error occured in C3 client.\n\nUploaded log:\n" + serverUrl + "/errorlogs/" + logfilename + "\n\nAdd description:\n\n";
			Tools.sendMailToAdminGroup(formattedBody);
		} catch(Exception e) {
			e.printStackTrace();
			logger.info("Exception during ftp upload, trying to attach log to mail!");

			// Read the latest log and put the content into the mail body itself, because uploading failed
			ArrayList<File> logfiles = new ArrayList<>();
			File dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3");
			if (dir.exists() && dir.isDirectory()) {
				for (File f : Objects.requireNonNull(dir.listFiles())) {
					if (f.getAbsolutePath().contains("starmap.log")) {
						logfiles.add(f);
					}
				}
			}

			String formattedBody = "Error in C3 client!\n\n";
			Tools.sendMailToAdminGroup(formattedBody, logfiles);
		}
		ftpClient.disconnect();
	}

	public void init() {
		setStrings();

		Nexus.getLogWatcher().run();
		instance = this;

		// Client log
		TableColumn<C3LogEntry, Integer> clientLogLinenumberColumn = new TableColumn<>("");
		clientLogLinenumberColumn.setCellValueFactory(new PropertyValueFactory<>("lineNumber"));
		clientLogLinenumberColumn.setPrefWidth(60);
		TableColumn<C3LogEntry, Integer> clientLogLevelColumn = new TableColumn<>("");
		clientLogLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
		clientLogLevelColumn.setPrefWidth(120);
		TableColumn<C3LogEntry, Integer> clientLogMessageColumn = new TableColumn<>("");
		clientLogMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
		clientLogMessageColumn.setPrefWidth(3000);

		clientLogLinenumberColumn.setResizable(false);
		clientLogLevelColumn.setResizable(false);
		clientLogMessageColumn.setResizable(true);

		tableViewClientLog.getColumns().addAll(clientLogLinenumberColumn, clientLogLevelColumn, clientLogMessageColumn);

		tableViewClientLog.setRowFactory(tableViewClientLog -> new TableRow<>() {
			private void doUpdateItem(C3LogEntry item) {
				// actually do the update and styling
				String style = "-fx-font-size:15px;-fx-font-family:'Consolas';-fx-border-width:0px;-fx-padding: 1 1 1 1;";
				if (item != null) {
					int i = 0;
					for (Node n : getChildren()) {
						if (i == 0) {                   // column 0
							switch (item.getLevel()) {
								case "WARNING" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:darkgray;" : "-fx-background-color:#ffe6c5;-fx-text-fill:darkgray;");
								case "SEVERE", "ERROR" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:darkgray;" : "-fx-background-color:#ff6547;-fx-text-fill:darkgray;");
								default -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:darkgray;" : "-fx-background-color:#e0f7fe;-fx-text-fill:darkgray;");
							}
						} else if (i == 1) {            // column 1
							switch (item.getLevel()) {
								case "WARNING" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:purple;" : "-fx-background-color:#ffe6c5;-fx-text-fill:purple;");
								case "SEVERE", "ERROR" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:purple;" : "-fx-background-color:#ff6547;-fx-text-fill:purple;");
								default -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:purple;" : "-fx-background-color:#e0f7fe;-fx-text-fill:purple;");
							}
						} else if (i == 2) {            // column 2
							switch (item.getLevel()) {
								case "WARNING" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:black;" : "-fx-background-color:#ffe6c5;-fx-text-fill:black;");
								case "SEVERE", "ERROR" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:purple;" : "-fx-background-color:#ff6547;-fx-text-fill:purple;");
								default -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:black;" : "-fx-background-color:#e0f7fe;-fx-text-fill:black;");
							}
						}
						n.setStyle(style);
						i++;
					}
				}
			}

			@Override
			public void updateIndex(int i) {
				super.updateIndex(i);
				doUpdateItem(getItem());
			}

			@Override
			protected void updateItem(C3LogEntry item, boolean empty) {
				super.updateItem(item, empty);
				doUpdateItem(item);
			}
		});

		// Server log
		TableColumn<C3LogEntry, Integer> serverLogLinenumberColumn = new TableColumn<>("");
		serverLogLinenumberColumn.setCellValueFactory(new PropertyValueFactory<>("lineNumber"));
		serverLogLinenumberColumn.setPrefWidth(60);
		TableColumn<C3LogEntry, Integer> serverLogLevelColumn = new TableColumn<>("");
		serverLogLevelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
		serverLogLevelColumn.setPrefWidth(120);
		TableColumn<C3LogEntry, Integer> serverLogMessageColumn = new TableColumn<>("");
		serverLogMessageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));
		serverLogMessageColumn.setPrefWidth(3000);

		serverLogLinenumberColumn.setResizable(false);
		serverLogMessageColumn.setResizable(true);

		tableViewServerLog.getColumns().addAll(serverLogLinenumberColumn, serverLogLevelColumn, serverLogMessageColumn);

		tableViewServerLog.setRowFactory(tableViewServerLog -> new TableRow<>() {
			private void doUpdateItem(C3LogEntry item) {
				// actually do the update and styling
				String style = "-fx-font-size:15px;-fx-font-family:'Consolas';-fx-border-width:0px;-fx-padding: 1 1 1 1;";
				if (item != null) {
					int i = 0;
					for (Node n : getChildren()) {
						if (i == 0) {                   // column 0
							switch (item.getLevel()) {
								case "WARNING" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:darkgray;" : "-fx-background-color:#ffe6c5;-fx-text-fill:darkgray;");
								case "SEVERE", "ERROR" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:darkgray;" : "-fx-background-color:#ff6547;-fx-text-fill:darkgray;");
								default -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:darkgray;" : "-fx-background-color:#e0f7fe;-fx-text-fill:darkgray;");
							}
						} else if (i == 1) {            // column 1
							switch (item.getLevel()) {
								case "WARNING" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:purple;" : "-fx-background-color:#ffe6c5;-fx-text-fill:purple;");
								case "SEVERE", "ERROR" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:purple;" : "-fx-background-color:#ff6547;-fx-text-fill:purple;");
								default -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:purple;" : "-fx-background-color:#e0f7fe;-fx-text-fill:purple;");
							}
						} else if (i == 2) {            // column 2
							switch (item.getLevel()) {
								case "WARNING" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:black;" : "-fx-background-color:#ffe6c5;-fx-text-fill:black;");
								case "SEVERE", "ERROR" -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:purple;" : "-fx-background-color:#ff6547;-fx-text-fill:purple;");
								default -> style += (isSelected() ? "-fx-background-color:#96d35f;-fx-text-fill:black;" : "-fx-background-color:#e0f7fe;-fx-text-fill:black;");
							}
						}
						n.setStyle(style);
						i++;
					}
				}
			}

			@Override
			public void updateIndex(int i) {
				super.updateIndex(i);
				doUpdateItem(getItem());
			}

			@Override
			protected void updateItem(C3LogEntry item, boolean empty) {
				super.updateItem(item, empty);
				doUpdateItem(item);
			}
		});

		//	TRACE – log events with this level are the most fine-grained and are usually not needed unless you need to have the full visibility of what is happening in your application and inside the third-party libraries that you use. You can expect the TRACE logging level to be very verbose.
		//	DEBUG – less granular compared to the TRACE level, but still more than you will need in everyday use. The DEBUG log level should be used for information that may be needed for deeper diagnostics and troubleshooting.
		//	INFO – the standard log level indicating that something happened, application processed a request, etc. The information logged using the INFO log level should be purely informative and not looking into them on a regular basis shouldn’t result in missing any important information.
		//	WARN – the log level that indicates that something unexpected happened in the application. For example a problem, or a situation that might disturb one of the processes, but the whole application is still working.
		//	ERROR – the log level that should be used when the application hits an issue preventing one or more functionalities from properly functioning. The ERROR log level can be used when one of the payment systems is not available, but there is still the option to check out the basket in the e-commerce application or when your social media logging option is not working for some reason. You can also see the ERROR log level associated with exceptions.

		cbLevel.getItems().add(Level.INFO);
		cbLevel.getItems().add(Level.WARN);
		cbLevel.getItems().add(Level.ERROR);
		cbLevel.getSelectionModel().select(Level.INFO);

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
		btnEditor.setText(Internationalization.getString("LogButtonEditorCaption"));
		btnEditor.setTooltip(new Tooltip(Internationalization.getString("LogButtonEditorToolTip")));
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
