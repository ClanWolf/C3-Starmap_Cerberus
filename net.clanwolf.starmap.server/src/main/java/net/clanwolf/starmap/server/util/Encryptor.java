package net.clanwolf.starmap.server.util;

import java.util.Base64;

public class Encryptor {
	public static String getPasswordFromPair(String indicator, String passwordPair) {
		String pw = "";
		String[] pws = passwordPair.split("#");
		String pw1 = pws[0].replaceFirst("p1:", "");
		String pw2 = pws[1].replaceFirst("p2:", "");;

		try {
			byte[] decodedBytes1 = Base64.getDecoder().decode(pw1);
			pw1 = new String(decodedBytes1);
			byte[] decodedBytes2 = Base64.getDecoder().decode(pw2);
			pw2 = new String(decodedBytes2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ("first".equals(indicator)) {
			pw = pw1;
		} else if ("second".equals(indicator)) {
			pw = pw2;
		}
		return pw;
	}
}
