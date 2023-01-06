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
package net.clanwolf.starmap.client.net;

import net.clanwolf.starmap.client.enums.C3FTPTYPES;
import net.clanwolf.starmap.client.nexus.Nexus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * @author Undertaker
 *
 */
public class FTP implements IFileTransfer {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private FTPClient ftpClient;
	private C3FTPTYPES ftptype;

	public FTP() {
		this.ftptype = C3FTPTYPES.FTP_DEFAULT;
	}

	public FTP(C3FTPTYPES type) {
		this.ftptype = type;
	}

	private void connect() throws Exception {
		ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");

		String user = "";
		String password = "";
		int ftp_port;

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

		try {
			ftp_port = Integer.parseInt(C3Properties.getProperty(C3PROPS.FTP_PORT));
		} catch(NumberFormatException nfe) {
			//nfe.printStackTrace();
			logger.info("FTP Port could not be taken from properties, setting to 21!");
			ftp_port = 21;
		}

		if ("".equals(C3Properties.getProperty(C3PROPS.FTP_SERVER)) || "unknown".equals(C3Properties.getProperty(C3PROPS.FTP_SERVER))) {
			throw new Exception("No server specified!");
		}

		try {
			if (!"".equals(user) && !"".equals(password)) {
//				logger.info("#########################################################");
//				logger.info("Trying to connect to FTP");
//				logger.info("Server   : " + C3Properties.getProperty(C3PROPS.FTP_SERVER));
//				logger.info("Port     : " + ftp_port);
//				logger.info("User     : " + user);
//				logger.info("Password : " + "****" + password.substring(password.length() - 2));
//				logger.info("#########################################################");

				ftpClient.connect(C3Properties.getProperty(C3PROPS.FTP_SERVER), ftp_port);
				logger.info(ftpClient.getReplyString().trim().replaceAll("(\\r|\\n)", ""));
				ftpClient.login(user, password);
				logger.info(ftpClient.getReplyString().trim().replaceAll("(\\r|\\n)", ""));
				ftpClient.enterLocalPassiveMode();
				logger.info(ftpClient.getReplyString().trim().replaceAll("(\\r|\\n)", ""));
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE, FTPClient.BINARY_FILE_TYPE);
				ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
			} else {
				throw new Exception("No userdata!");
			}
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("Exception while ftp file transfer!", e);
			throw e;
		}
	}

	private String getFTPSubPath(){
		String subPath = "resources/";
		if(Nexus.isDevelopmentPC()) {
			subPath = "dev/resources/";
		}
		return subPath;
	}

	public boolean upload(String localSourceFile, String remoteResultFile) throws Exception {
		return upload(localSourceFile, remoteResultFile, "");
	}

	@Override
	public boolean delete(String remoteResultFile) throws Exception {

		boolean ret = false;
		try {
			if (ftpClient == null || !ftpClient.isConnected()) {
				connect();
			}

			ftpClient.deleteFile(remoteResultFile);
			logger.info(ftpClient.getReplyString());
			ret = true;

		} catch (IOException ioe) {
			logger.error(null, ioe);
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
	public boolean makeDir(String pathname) throws Exception {

		boolean ret = false;
		try {
			if (ftpClient == null || !ftpClient.isConnected()) {
				connect();
			}

			ftpClient.makeDirectory(getFTPSubPath() + pathname);
			ret = true;
		} catch (IOException ioe) {
			logger.error(null, ioe);
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
	public boolean deleteAllFiles(String subDir, String filter) throws Exception {

		boolean ret = false;
		try {
			if (ftpClient == null || !ftpClient.isConnected()) {
				connect();
			}

			String[] filenameList = ftpClient.listNames(getFTPSubPath() + subDir);

			for (int i = 0; i < filenameList.length; i++) {
				logger.info(filenameList[i]);
				if (filter == null || filenameList[i].contains(filter)) {
					logger.info(filenameList[i] + " - delete");
					delete(filenameList[i]);
				}

			}

			ret = true;
		} catch (IOException ioe) {
			logger.error(null, ioe);
		}
		return ret;
	}

	public boolean upload(String localSourceFile, String remoteResultFile, String subfolder) throws Exception {
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

			logger.info(ftpClient.getReplyString().trim().replaceAll("(\\r|\\n)", ""));
			ret = true;

		} catch (IOException ioe) {
			logger.error(null, ioe);
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
}
