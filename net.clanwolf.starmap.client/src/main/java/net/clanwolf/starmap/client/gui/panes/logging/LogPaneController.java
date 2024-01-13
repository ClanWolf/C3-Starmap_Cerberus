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
package net.clanwolf.starmap.client.gui.panes.logging;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import net.clanwolf.starmap.client.action.*;
import net.clanwolf.starmap.client.enums.C3FTPTYPES;
import net.clanwolf.starmap.client.net.FTP;
import net.clanwolf.starmap.client.net.IP;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import net.clanwolf.starmap.client.util.Internationalization;
import net.clanwolf.starmap.client.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class LogPaneController implements ActionCallBackListener {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@FXML
	Tab tabLog;
	@FXML
	TextArea taLog;
	@FXML
	Button btnReload, btnReport, btnClose, btnEditor;

	@FXML
	public void handleReloadClick() {
		loadLog();
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
	public void btnReportClicked() {
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
			String logfilenamelist = "";
			String logfilename0 = C3Properties.getProperty(C3PROPS.LOGFILE) + ".0";
			File logfile0 = new File(logfilename0);
			if (logfile0.isFile()) {
				ftpClient.upload(C3Properties.getProperty(C3PROPS.LOGFILE) + ".0", logfilename + ".0");
				logfilenamelist += "\n" + logfilename0;
			}
			String logfilename1 = C3Properties.getProperty(C3PROPS.LOGFILE) + ".1";
			File logfile1 = new File(logfilename1);
			if (logfile1.isFile()) {
				ftpClient.upload(C3Properties.getProperty(C3PROPS.LOGFILE) + ".1", logfilename + ".1");
				logfilenamelist += "\n" + logfilename1;
			}
			String logfilename2 = C3Properties.getProperty(C3PROPS.LOGFILE) + ".2";
			File logfile2 = new File(C3Properties.getProperty(C3PROPS.LOGFILE) + ".2");
			if (logfile2.isFile()) {
				ftpClient.upload(C3Properties.getProperty(C3PROPS.LOGFILE) + ".2", logfilename + ".2");
				logfilenamelist += "\n" + logfilename2;
			}

			String serverUrl = C3Properties.getProperty(C3PROPS.SERVER_URL);
			String formattedBody = "Error occured in C3 client.\n\nUploaded logs:\n" + serverUrl + "/errorlogs/" + logfilenamelist + "\n\nAdd description:\n\n";
			Tools.sendMailToAdminGroup(formattedBody);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception during ftp upload, trying to attach log to mail!");

			// Read the latest log and put the content into the mail body itself, because uploading failed
			ArrayList<File> logfiles = new ArrayList<>();
			File dir = new File(System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3");
			if (dir.exists() && dir.isDirectory()) {
				for (File f : Objects.requireNonNull(dir.listFiles())) {
					if (f.getAbsolutePath().contains("starmap.c3log")) {
						logfiles.add(f);
					}
				}
			}

			String formattedBody = "Error in C3 client!\n\n";
			Tools.sendMailToAdminGroup(formattedBody, logfiles);
		}
		ftpClient.disconnect();
	}

	public void loadLog() {
		Platform.runLater(() -> {
			String logfilename0 = C3Properties.getProperty(C3PROPS.LOGFILE) + ".0";
			File f = new File(logfilename0);
			String s = "...";
			if (f.isFile() && f.canRead()) {
				s = Tools.readLocalFileToString(logfilename0);
			}
			taLog.clear();
			taLog.appendText(s);
			taLog.setScrollTop(Double.MAX_VALUE);
		});
	}

	public void init() {
		setStrings();
		loadLog();
		addActionCallBackListeners();
	}

	public void addActionCallBackListeners() {
		ActionManager.addActionCallbackListener(ACTIONS.CHANGE_LANGUAGE, this);
	}

	public void setStrings() {
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
