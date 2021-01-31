package net.clanwolf.starmap.client.packager;

import java.io.*;

public class NSICreator {

	private static String version = "5.2.0";

	private void writeLine(BufferedWriter bw, String text) throws Exception {
		bw.write(text);
		bw.newLine();

		System.out.println(text);
	}

	private void createNsiFile(String version) {
		try {
			File fout = new File("NSIS\\c3-client.nsi");
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			BufferedReader br = new BufferedReader(new FileReader("NSIS\\c3-client.nsi_template"));
			String line;
			ContentLister cl = new ContentLister();
			while ((line = br.readLine()) != null) {
				if (line.contains("###FILELIST###")) {
					cl.walk(bw,"C:\\C3\\projects\\C3-Starmap_Cerberus\\net.clanwolf.starmap.client\\target\\jlink-image");
				} else if (line.contains("###VERSION###")) {
					line = line.replace("###VERSION###", version);
					writeLine(bw, line);
				} else if (line.contains("###FILELISTTOREMOVE###")) {
					line = line.replace("###FILELISTTOREMOVE###", cl.getListDeleteFilesDuringUninstall());
					writeLine(bw, line);
				} else if (line.contains("###DIRECTORYLISTTOREMOVE###")) {
					line = line.replace("###DIRECTORYLISTTOREMOVE###", cl.getListDeleteFoldersDuringUninstall());
					writeLine(bw, line);
				} else {
					writeLine(bw, line);
				}
			}

			br.close();
			bw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		NSICreator creator = new NSICreator();
		creator.createNsiFile(version);
	}
}
