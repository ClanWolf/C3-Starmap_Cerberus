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
 * Copyright (c) 2001-2025, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.packager;

import java.io.*;
import java.util.Arrays;

public class ContentLister {

	private String listDeleteFilesDuringUninstall = "";
	private String listDeleteFoldersDuringUninstall = "";

	public String getListDeleteFilesDuringUninstall() {
		return listDeleteFilesDuringUninstall;
	}

	public String getListDeleteFoldersDuringUninstall() {
		return listDeleteFoldersDuringUninstall;
	}

	private String stripPath(String pathIn) {
		return pathIn.substring(pathIn.indexOf("jlink-image\\") + 12);
	}

	private void writeEmptyLine(BufferedWriter bw) throws Exception {
		bw.newLine();
		System.out.println();
	}

	private void writeLine(BufferedWriter bw, String text) throws Exception {
		bw.write(text);
		bw.newLine();

		System.out.println(text);
	}

	void walk(BufferedWriter bw, String path) throws Exception {
		File root = new File(path);
		File[] list = root.listFiles();
		Arrays.sort(list, (f1, f2) -> {
			if (f1.isDirectory() && !f2.isDirectory()) {
				return 1;
			} else if (!f1.isDirectory() && f2.isDirectory()) {
				return -1;
			} else {
				return f2.compareTo(f1);
			}
		});

		if (list == null) return;

		for (File f : list) {
			if (f.isDirectory()) {
				String cPath = stripPath(f.getAbsoluteFile().getAbsolutePath());

				writeEmptyLine(bw);
				writeLine(bw,"\tCreateDirectory $INSTDIR\\" + cPath);
				writeLine(bw,"\tSetOutpath $INSTDIR\\" + cPath);

				listDeleteFoldersDuringUninstall = "\tRMDir \"$INSTDIR\\" + stripPath(f.getAbsoluteFile().getAbsolutePath()) + "\"\n" + listDeleteFoldersDuringUninstall;

				walk(bw, f.getAbsolutePath());
			} else {
				String cFile = "..\\net.clanwolf.starmap.client\\target\\jlink-image\\" + stripPath(f.getAbsoluteFile().getAbsolutePath());
				writeLine(bw,"\tFile /r \"" + cFile + "\"");

				listDeleteFilesDuringUninstall = listDeleteFilesDuringUninstall + "\tDelete $INSTDIR\\" + stripPath(f.getAbsoluteFile().getAbsolutePath()) + "\n";
			}
		}
	}

//	public static void main(String[] args) {
//		try {
//			ContentLister cl = new ContentLister();
//
//			File fout = new File("NSIS\\listed_contents.txt");
//			FileOutputStream fos = new FileOutputStream(fout);
//			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
//
//			cl.walk(bw, "C:\\C3\\projects\\C3-Starmap_Cerberus\\net.clanwolf.starmap.client\\target\\jlink-image");
//
//			bw.close();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
}
