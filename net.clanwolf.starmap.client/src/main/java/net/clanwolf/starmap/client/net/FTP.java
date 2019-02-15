/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : http://www.clanwolf.net                            |
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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.net;

import net.clanwolf.starmap.logging.C3Logger;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Undertaker
 *
 */
public class FTP implements IFileTransfer {

	private FTPClient ftpClient;

	public FTP() {

	}

	private void connect() throws IOException {
		ftpClient = new FTPClient();

		ftpClient.connect(C3Properties.getProperty(C3PROPS.FTP_SERVER), Integer.parseInt(C3Properties.getProperty(C3PROPS.FTP_PORT)));
		C3Logger.info(ftpClient.getReplyString());
		ftpClient.login(C3Properties.getProperty(C3PROPS.FTP_USER), C3Properties.getProperty(C3PROPS.FTP_PASSWORD));
		C3Logger.info(ftpClient.getReplyString());
		ftpClient.enterLocalPassiveMode();
		C3Logger.info(ftpClient.getReplyString());
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE, FTPClient.BINARY_FILE_TYPE);
		ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
	}

	@Override
	public boolean upload(String localSourceFile, String remoteResultFile) {

		boolean ret = false;
		FileInputStream fis = null;

		try {
			if (ftpClient == null || !ftpClient.isConnected()) {
				connect();
			}

			fis = new FileInputStream(localSourceFile);
			ftpClient.storeFile("resources/" + remoteResultFile, fis);
			C3Logger.info(ftpClient.getReplyString());
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

			ftpClient.makeDirectory("resources/" + pathname);
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

			String[] filenameList = ftpClient.listNames("resources/" + subDir);

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
