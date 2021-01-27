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
 * Copyright (c) 2001-2021, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {

	private static final String ALGO = "AES/CFB/NoPadding";

	public static String encrypt(String key, String initVector, String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);
			value = initVector + value;
			byte[] encrypted = cipher.doFinal(value.getBytes());
			String str = new String(encrypted, StandardCharsets.UTF_8);
			return Base64.getUrlEncoder().encodeToString(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String key, String initVector, String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.getUrlDecoder().decode(encrypted));
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String hash(String s) {
		StringBuffer myHash = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(s.getBytes());
			byte[] digest = md.digest();
			for (int i = 0; i < digest.length; i++) {
				if ((0xff & digest[i]) < 0x10) {
					myHash.append("0" + Integer.toHexString((0xFF & digest[i])));
				} else {
					myHash.append(Integer.toHexString(0xFF & digest[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return myHash.toString();
	}

	public static String createPasswordPair(String pw) {
		String used_password = "";
		String p1 = JCrypt.crypt(C3Properties.getProperty("s"), pw);
		String p2 = Encryptor.hash(Encryptor.hash(pw));

		used_password = used_password + "p1:" + Base64.getEncoder().encodeToString(p1.getBytes());
		used_password = used_password + "#";
		used_password = used_password + "p2:" + Base64.getEncoder().encodeToString(p2.getBytes());

		return used_password;
	}

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

	public static void main(String[] args) throws Exception {
		String key = "201ACC3548C74444";
		String message = "TestTextToEncrypt";
		String initVector = "4AFFBD22D04CABDB";
		String encryptedMessage = encrypt(key, initVector, message);
		System.out.println(encryptedMessage);
	}
}
