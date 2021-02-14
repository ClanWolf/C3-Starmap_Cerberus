package net.clanwolf.starmap.server.util;

import net.clanwolf.starmap.logging.C3Logger;

import java.util.Base64;

public class Encryptor {
	public static String getPasswordFromPair(String indicator, String passwordPair) {
		String pw = "";
		try {
			String[] pws = passwordPair.split("#");

			if (pws.length != 2) {
				C3Logger.warning("Wrong pw format. Only one of two values provided! Cannot log in!");
				return pw;
			}

			String pw1 = pws[0].replaceFirst("p1:", "");
			String pw2 = pws[1].replaceFirst("p2:", "");

			byte[] decodedBytes1 = Base64.getDecoder().decode(pw1);
			pw1 = new String(decodedBytes1);
			byte[] decodedBytes2 = Base64.getDecoder().decode(pw2);
			pw2 = new String(decodedBytes2);

			if ("first".equals(indicator)) {
				pw = pw1;
			} else if ("second".equals(indicator)) {
				pw = pw2;
			}
		} catch (Exception e) {
			e.printStackTrace();
			C3Logger.exception("UserLookup failed!", e);
		}
		return pw;
	}
}
