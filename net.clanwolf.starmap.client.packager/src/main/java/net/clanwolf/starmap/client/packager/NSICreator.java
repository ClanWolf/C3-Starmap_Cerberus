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
package net.clanwolf.starmap.client.packager;

import java.io.*;

public class NSICreator {
	public static void main(String[] args) {
		String version = null;
		String java_version_path = "19.0.2"; // this is the java version installed on the server for the path in scripts!

		try {
			BufferedReader brProps = new BufferedReader(new FileReader("C:\\C3\\projects\\C3-Starmap_Cerberus\\net.clanwolf.starmap.client\\target\\classes\\c3.properties"));
			String lineProps;
			while ((lineProps = brProps.readLine()) != null) {
				if (lineProps.startsWith("version=")) {
					String[] p1 = lineProps.split("=");
					version = p1[1];
				}
			}

			System.out.println("Version: " + version);
			System.out.println("Java-Version-Path: " + java_version_path);

			if (version == null || java_version_path == null) {
				throw new Exception("Version could not be found in target folder (!) properties.");
			}

			NSICreator creator = new NSICreator();
			creator.createNsiFile(version, java_version_path);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void writeLine(BufferedWriter bw, String text) throws Exception {
		bw.write(text);
		bw.newLine();

		System.out.println(text);
	}

	private void writeLineLinux(BufferedWriter bw, String text) throws Exception {
		bw.write(text);
		bw.write("\n");

		System.out.println(text);
	}

	private void createNsiFile(String version, String java_version_path) {
		try {
			File fout = new File("NSIS\\c3-client.nsi");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			BufferedReader br = new BufferedReader(new FileReader("NSIS\\templates\\c3-client.nsi_template"));
			String line;
			ContentLister cl = new ContentLister();
			while ((line = br.readLine()) != null) {
				if (line.contains("###FILELIST###")) {
					cl.walk(bw,"C:\\C3\\projects\\C3-Starmap_Cerberus\\net.clanwolf.starmap.client\\target\\jlink-image");
				}
				if (line.contains("###VERSION###")) {
					line = line.replace("###VERSION###", version);
				}
				if (line.contains("###FILELISTTOREMOVE###")) {
					line = line.replace("###FILELISTTOREMOVE###", cl.getListDeleteFilesDuringUninstall());
				}
				if (line.contains("###DIRECTORYLISTTOREMOVE###")) {
					line = line.replace("###DIRECTORYLISTTOREMOVE###", cl.getListDeleteFoldersDuringUninstall());
				}
				if (line.contains("###TIMESTAMP###")) {
					line = line.replace("###TIMESTAMP###", System.currentTimeMillis() + "");
				}
				writeLine(bw, line);
			}
			br.close();
			bw.close();

			System.out.println("-------------------------------------------------------------");
			System.out.println("Writing upload script for the installer:");
			File fout2 = new File("NSIS\\scripts\\upload_installer.script");
			FileOutputStream fos2 = new FileOutputStream(fout2);
			BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(fos2));
			BufferedReader br2 = new BufferedReader(new FileReader("NSIS\\templates\\upload_installer.script_template"));
			String line2;
			while ((line2 = br2.readLine()) != null) {
				if (line2.contains("###VERSION###")) {
					line2 = line2.replace("###VERSION###", version);
				}
				if (line2.contains("###JAVA_VERSION_PATH###")) {
					line2 = line2.replace("###JAVA_VERSION_PATH###", java_version_path);
				}
				writeLine(bw2, line2);
			}
			br2.close();
			bw2.close();

			System.out.println("-------------------------------------------------------------");
			System.out.println("Writing upload script for the the server:");
			File fout3 = new File("NSIS\\scripts\\upload_server.script");
			FileOutputStream fos3 = new FileOutputStream(fout3);
			BufferedWriter bw3 = new BufferedWriter(new OutputStreamWriter(fos3));
			BufferedReader br3 = new BufferedReader(new FileReader("NSIS\\templates\\upload_server.script_template"));
			String line3;
			while ((line3 = br3.readLine()) != null) {
				if (line3.contains("###VERSION###")) {
					line3 = line3.replace("###VERSION###", version);
				}
				if (line3.contains("###JAVA_VERSION_PATH###")) {
					line3 = line3.replace("###JAVA_VERSION_PATH###", java_version_path);
				}
				writeLine(bw3, line3);
			}
			br3.close();
			bw3.close();

			System.out.println("-------------------------------------------------------------");
			System.out.println("Writing build script:");
			File fout4 = new File("NSIS\\c3-client_createInstaller.cmd");
			FileOutputStream fos4 = new FileOutputStream(fout4);
			BufferedWriter bw4 = new BufferedWriter(new OutputStreamWriter(fos4));
			BufferedReader br4 = new BufferedReader(new FileReader("NSIS\\templates\\c3-client_createInstaller.script_template"));
			String line4;
			while ((line4 = br4.readLine()) != null) {
				if (line4.contains("###VERSION###")) {
					line4 = line4.replace("###VERSION###", version);
				}
				if (line4.contains("###JAVA_VERSION_PATH###")) {
					line4 = line4.replace("###JAVA_VERSION_PATH###", java_version_path);
				}
				writeLine(bw4, line4);
			}
			br4.close();
			bw4.close();

			System.out.println("-------------------------------------------------------------");
			System.out.println("Writing start script:");
			File fout5 = new File("net.clanwolf.starmap.server\\src\\main\\shell\\checkprocess_c3server.sh");
			FileOutputStream fos5 = new FileOutputStream(fout5);
			BufferedWriter bw5 = new BufferedWriter(new OutputStreamWriter(fos5));
			BufferedReader br5 = new BufferedReader(new FileReader("NSIS\\templates\\checkprocess_c3server.script_template"));
			String line5;
			while ((line5 = br5.readLine()) != null) {
				if (line5.contains("###VERSION###")) {
					line5 = line5.replace("###VERSION###", version);
				}
				if (line5.contains("###JAVA_VERSION_PATH###")) {
					line5 = line5.replace("###JAVA_VERSION_PATH###", java_version_path);
				}
				writeLineLinux(bw5, line5);
			}
			br5.close();
			bw5.close();

			System.out.println("-------------------------------------------------------------");
			System.out.println("Writing ircbot start script:");
			File fout6 = new File("net.clanwolf.starmap.server\\src\\main\\shell\\checkprocess_ircbot.sh");
			FileOutputStream fos6 = new FileOutputStream(fout6);
			BufferedWriter bw6 = new BufferedWriter(new OutputStreamWriter(fos6));
			BufferedReader br6 = new BufferedReader(new FileReader("NSIS\\templates\\checkprocess_ircbot.script_template"));
			String line6;
			while ((line6 = br6.readLine()) != null) {
				if (line6.contains("###VERSION###")) {
					line6 = line6.replace("###VERSION###", version);
				}
				if (line6.contains("###JAVA_VERSION_PATH###")) {
					line6 = line6.replace("###JAVA_VERSION_PATH###", java_version_path);
				}
				writeLineLinux(bw6, line6);
			}
			br6.close();
			bw6.close();

			System.out.println("-------------------------------------------------------------");
			System.out.println("Writing ts3bot start script:");
			File fout61 = new File("net.clanwolf.starmap.server\\src\\main\\shell\\checkprocess_ts3bot.sh");
			FileOutputStream fos61 = new FileOutputStream(fout61);
			BufferedWriter bw61 = new BufferedWriter(new OutputStreamWriter(fos61));
			BufferedReader br61 = new BufferedReader(new FileReader("NSIS\\templates\\checkprocess_ts3bot.script_template"));
			String line61;
			while ((line61 = br61.readLine()) != null) {
				if (line61.contains("###VERSION###")) {
					line61 = line61.replace("###VERSION###", version);
				}
				if (line61.contains("###JAVA_VERSION_PATH###")) {
					line61 = line61.replace("###JAVA_VERSION_PATH###", java_version_path);
				}
				writeLineLinux(bw61, line61);
			}
			br61.close();
			bw61.close();

			System.out.println("-------------------------------------------------------------");
			System.out.println("Writing version update script:");
			File fout7 = new File("net.clanwolf.starmap.server\\src\\main\\php\\insert_new_version.php");
			FileOutputStream fos7 = new FileOutputStream(fout7);
			BufferedWriter bw7 = new BufferedWriter(new OutputStreamWriter(fos7));
			BufferedReader br7 = new BufferedReader(new FileReader("NSIS\\templates\\insert_new_version.php_template"));
			String line7;
			while ((line7 = br7.readLine()) != null) {
				if (line7.contains("###VERSION###")) {
					line7 = line7.replace("###VERSION###", version);
				}
				writeLine(bw7, line7);
			}
			br7.close();
			bw7.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
