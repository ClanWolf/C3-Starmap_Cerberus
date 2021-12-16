package net.clanwolf.starmap.client.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;

public class IP {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static String getExternalIP() {
		URL ipAdress = null;
		String ip = "noip";
		try {
			ipAdress = new URL("http://myexternalip.com/raw");
			BufferedReader in = new BufferedReader(new InputStreamReader(ipAdress.openStream()));
			ip = in.readLine();
			logger.info("External IP adress: " + ip);
		} catch (MalformedURLException e) {
			ip = "noip";
			logger.warn("Error while getting external IP adress.");
			e.printStackTrace();
		} catch (IOException e) {
			ip = "noip";
			logger.warn("Error while getting external IP adress.");
			e.printStackTrace();
		}
		return ip;
	}

	private IP() {
	}
}
