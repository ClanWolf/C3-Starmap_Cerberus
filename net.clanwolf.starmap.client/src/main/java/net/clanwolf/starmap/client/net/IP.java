package net.clanwolf.starmap.client.net;

import net.clanwolf.starmap.logging.C3Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class IP {
	public static String getExternalIP() {
		URL ipAdress = null;
		String ip = "noip";
		try {
			ipAdress = new URL("http://myexternalip.com/raw");
			BufferedReader in = new BufferedReader(new InputStreamReader(ipAdress.openStream()));
			ip = in.readLine();
			C3Logger.info("External IP adress: " + ip);
		} catch (MalformedURLException e) {
			ip = "noip";
			C3Logger.warning("Error while getting external IP adress.");
			e.printStackTrace();
		} catch (IOException e) {
			ip = "noip";
			C3Logger.warning("Error while getting external IP adress.");
			e.printStackTrace();
		}
		return ip;
	}

	private IP() {
	}
}
