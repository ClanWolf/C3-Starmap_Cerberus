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
package net.clanwolf.starmap.client.net;

import net.clanwolf.starmap.client.enums.C3FTPTYPES;
import net.clanwolf.starmap.client.nexus.Nexus;
import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Undertaker
 *
 */
public class FTP implements IFileTransfer {

	private FTPClient ftpClient;
	private C3FTPTYPES ftptype;

	public FTP() {
		this.ftptype = C3FTPTYPES.FTP_DEFAULT;
	}

	public FTP(C3FTPTYPES type) {
		this.ftptype = type;
	}

	private void connect() throws IOException {
		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");

		String user = "";
		String password = "";
		if (ftptype == C3FTPTYPES.FTP_LOGUPLOAD) {
			user = C3Properties.getProperty(C3PROPS.FTP_USER_LOGUPLOAD);
			password = C3Properties.getProperty(C3PROPS.FTP_PASSWORD_LOGUPLOAD);
		} else if (ftptype == C3FTPTYPES.FTP_HISTORYUPLOAD) {
			user = C3Properties.getProperty(C3PROPS.FTP_USER_HISTORYUPLOAD);
			password = C3Properties.getProperty(C3PROPS.FTP_PASSWORD_HISTORYUPLOAD);
		} else {
			user = C3Properties.getProperty(C3PROPS.FTP_USER);
			password = C3Properties.getProperty(C3PROPS.FTP_PASSWORD);
		}

		ftpClient.connect(C3Properties.getProperty(C3PROPS.FTP_SERVER), Integer.parseInt(C3Properties.getProperty(C3PROPS.FTP_PORT)));
		C3Logger.info(ftpClient.getReplyString().trim().replaceAll("(\\r|\\n)", ""));
		ftpClient.login(user, password);
		C3Logger.info(ftpClient.getReplyString().trim().replaceAll("(\\r|\\n)", ""));
		ftpClient.enterLocalPassiveMode();
		C3Logger.info(ftpClient.getReplyString().trim().replaceAll("(\\r|\\n)", ""));
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE, FTPClient.BINARY_FILE_TYPE);
		ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
	}

	private String getFTPSubPath(){
		String subPath = "resources/";
		if(Nexus.isDevelopmentPC()) {
			subPath = "dev/resources/";
		}
		return subPath;
	}

	public boolean upload(String localSourceFile, String remoteResultFile) {
		return upload(localSourceFile, remoteResultFile, "");
	}

	public boolean upload(String localSourceFile, String remoteResultFile, String subfolder) {
		boolean ret = false;
		FileInputStream fis = null;

		try {
			if (ftpClient == null || !ftpClient.isConnected()) {
				connect();
			}

			fis = new FileInputStream(localSourceFile);

			if (ftptype == C3FTPTYPES.FTP_DEFAULT) {
				ftpClient.storeFile(getFTPSubPath() + remoteResultFile, fis);
			} else if (ftptype == C3FTPTYPES.FTP_LOGUPLOAD) {
				ftpClient.storeFile("" + remoteResultFile, fis);
			} else if (ftptype == C3FTPTYPES.FTP_HISTORYUPLOAD) {
				boolean subfolderexists = false;
				FTPFile[] folders = ftpClient.listDirectories();
				for (FTPFile f : folders) {
					if (f.getName().equals(subfolder)) {
						subfolderexists = true;
					}
				}
				if (subfolderexists) {
					ftpClient.storeFile(subfolder + "/" + remoteResultFile, fis);
				} else {
					ftpClient.makeDirectory(subfolder);
					ftpClient.storeFile(subfolder + "/" + remoteResultFile, fis);
				}
			}

			C3Logger.info(ftpClient.getReplyString().trim().replaceAll("(\\r|\\n)", ""));
			ret = true;

		} catch (IOException ioe) {
			C3Logger.exception(null, ioe);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				ftpClient.disconnect();
			} catch (IOException e) {
				/* nothing to do */
			}
		}

		return ret;
	}

	@Override
	public boolean delete(String remoteResultFile) {

		boolean ret = false;
		try {
			if (ftpClient == null || !ftpClient.isConnected()) {
				connect();
			}

			ftpClient.deleteFile(remoteResultFile);
			C3Logger.info(ftpClient.getReplyString());
			ret = true;

		} catch (IOException ioe) {
			C3Logger.exception(null, ioe);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				/* nothing to do */
			}
		}

		return ret;
	}

	@Override
	public boolean disconnect() {

		boolean ret = false;
		try {
			ftpClient.logout();
			ftpClient.disconnect();
			ret = true;

		} catch (IOException ioe) {
			// do nothing
		}
		return ret;
	}

	@Override
	public boolean makeDir(String pathname) {

		boolean ret = false;
		try {
			if (ftpClient == null || !ftpClient.isConnected()) {
				connect();
			}

			ftpClient.makeDirectory(getFTPSubPath() + pathname);
			ret = true;
		} catch (IOException ioe) {
			C3Logger.exception(null, ioe);
		}
		return ret;
	}

	@Override
	public boolean deleteAllFiles(String subDir, String filter) {

		boolean ret = false;
		try {
			if (ftpClient == null || !ftpClient.isConnected()) {
				connect();
			}

			String[] filenameList = ftpClient.listNames(getFTPSubPath() + subDir);

			for (int i = 0; i < filenameList.length; i++) {
				C3Logger.info(filenameList[i]);
				if (filter == null || filenameList[i].contains(filter)) {
					C3Logger.info(filenameList[i] + " - delete");
					delete(filenameList[i]);
				}

			}

			ret = true;
		} catch (IOException ioe) {
			C3Logger.exception(null, ioe);
		}
		return ret;
	}
}
