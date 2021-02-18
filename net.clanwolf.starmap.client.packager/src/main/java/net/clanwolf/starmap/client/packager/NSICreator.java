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

			System.out.println("-------------------------------------------------------------");
			System.out.println("Writing upload script for this installer:");
			File fout2 = new File("NSIS\\upload_installer.script");
			FileOutputStream fos2 = new FileOutputStream(fout2);
			BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(fos2));
			BufferedReader br2 = new BufferedReader(new FileReader("NSIS\\upload_installer.script_template"));
			String line2;
			while ((line2 = br2.readLine()) != null) {
				if (line2.contains("###VERSION###")) {
					line2 = line2.replace("###VERSION###", version);
					writeLine(bw2, line2);
				} else {
					writeLine(bw2, line2);
				}
			}
			br2.close();
			bw2.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		NSICreator creator = new NSICreator();
		creator.createNsiFile(version);
	}
}
