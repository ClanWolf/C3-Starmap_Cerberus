package net.clanwolf.starmap.client.packager;

import java.io.*;

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

		if (list == null) return;

		for (File f : list) {
			if (f.isDirectory()) {
				String cPath = stripPath(f.getAbsoluteFile().getAbsolutePath());

				writeEmptyLine(bw);
				writeLine(bw,"\tCreateDirectory $INSTDIR\\" + cPath);
				writeLine(bw,"\tSetOutpath $INSTDIR\\" + cPath);

				listDeleteFoldersDuringUninstall = listDeleteFoldersDuringUninstall + "\tRMDir \"$INSTDIR\\" + stripPath(f.getAbsoluteFile().getAbsolutePath()) + "\"\n";

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
