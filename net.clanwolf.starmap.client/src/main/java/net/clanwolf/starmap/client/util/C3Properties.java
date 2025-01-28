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
package net.clanwolf.starmap.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

/**
 * Get Stringresources from property file
 *
 * @author Meldric
 * @author kotzbrocken2
 * @version 1.0
 */
public class C3Properties {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static SecretKeySpec mSecretKey;
	private static File sUserPropertyFile;
	private static Cipher c;
	private static final String UNKNOWN = "unknown";
	private static final String S = "s";
	private static final String REGENERATION_WARNING = "File will be regenerated regularly!";
	private static final Properties sProperties = new Properties();

	/*
	 * Static initializer.
	 */
	static {
		try {
			c = Cipher.getInstance("AES");

			byte[] secretKey = new byte[16];

			secretKey[0] = 78;
			secretKey[1] = -44;
			secretKey[2] = -10;
			secretKey[3] = -42;
			secretKey[4] = -44;
			secretKey[5] = 106;
			secretKey[6] = -30;
			secretKey[7] = -1;
			secretKey[8] = -104;
			secretKey[9] = 105;
			secretKey[10] = 24;
			secretKey[11] = -103;
			secretKey[12] = -9;
			secretKey[13] = -81;
			secretKey[14] = -32;
			secretKey[15] = 95;

			mSecretKey = new SecretKeySpec(secretKey, "AES");
			sProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("c3.properties"));

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | IOException e) {
			logger.error("Cipher: " + e, e);
		}
	}

	/**
	 * Loads the userproperties from the given directory. The version of the property file and the version of the user-property-file will be compared. If they differ then the stored userproperties will be loaded, the user-property-file will be
	 * deleted and the valid userproperties will be restored within the new user-property-file. So we avoid version-conflicts by validating the user-properties.
	 *
	 * @param dir the directory where the user-property-file will be
	 * @return "true" if the user-properties would be changed or the user-property-file doesn't exist (this indicates a first start) and the property-dialog should be displayed
	 * @throws IOException if something goes wrong
	 */
	public static boolean loadUserProperties(String dir) throws IOException {
		sUserPropertyFile = new File(dir + "/C3.properties");

		Properties userProperties = new Properties();

		// if the user-property-file exist
		if (sUserPropertyFile.exists()) {
			try (FileInputStream fIn = new FileInputStream(sUserPropertyFile)) {
				userProperties.load(fIn);
			}

			String versionKey = C3PROPS.VERSION.name().toLowerCase();

			// look if the versions are identical and if so store the
			// userproperties into the property-object
			if (sProperties.get(versionKey).equals(userProperties.get(versionKey))) {
				sProperties.putAll(userProperties);
				updateSystemProperties();
				return false;
			}
		}

		resetUserProperties();

		userProperties.entrySet().stream().forEach((userProperty) -> {
			String key = (String) userProperty.getKey();
			if (sProperties.containsKey(key) && !C3PROPS.VERSION.name().toLowerCase().equals(key)) {
				setProperty(C3PROPS.valueOf(((String) userProperty.getKey()).toUpperCase()), (String) userProperty.getValue(), true, false);
			}
		});
		return true;
	}

	private static void updateSystemProperties() {
		if (getBoolean(C3PROPS.USE_PROXY)) {
			System.setProperty("proxy.host", getProperty(C3PROPS.PROXY_SERVER));
			System.setProperty("proxy.port", getProperty(C3PROPS.PROXY_PORT));

			if (C3Properties.getBoolean(C3PROPS.PROXY_NEEDS_AUTHENTICATION)) {
				System.setProperty("proxy.user", getProperty(C3PROPS.PROXY_USER));
				System.setProperty("proxy.password", getProperty(C3PROPS.PROXY_PASSWORD));
				System.setProperty("proxy.domain", getProperty(C3PROPS.PROXY_DOMAIN));

			} else {
				Properties properties = System.getProperties();

				properties.remove("proxy.user");
				properties.remove("proxy.password");
				properties.remove("proxy.domain");
			}
		} else {
			Properties properties = System.getProperties();
			properties.remove("proxy.host");
			properties.remove("proxy.port");
			properties.remove("proxy.user");
			properties.remove("proxy.password");
			properties.remove("proxy.domain");
		}
	}

	/**
	 * Resets user properties
	 *
	 * @throws IOException Exception
	 */
	public static void resetUserProperties() throws IOException {
		// create parent dirs if necessary
		sUserPropertyFile.getParentFile().mkdirs();

		// delete the file
		sUserPropertyFile.delete();

		// create the file
		sUserPropertyFile.createNewFile();

		setProperty(C3PROPS.VERSION, (String) sProperties.remove(C3PROPS.VERSION.name().toLowerCase()), true);
	}

	/**
	 * Sets a property without storing it
	 *
	 * @param key   C3PROPS
	 * @param value String
	 */
	public static void setProperty(C3PROPS key, String value) {
		setProperty(key, value, false);
	}

	public static void setProperty(C3PROPS key, String value, boolean store) {
		setProperty(key, value, store, true);
	}

	/**
	 * Sets a property and stores it
	 *
	 * @param key   C3PROPS
	 * @param value String
	 * @param store boolean
	 */
	public static void setProperty(C3PROPS key, String value, boolean store, boolean encrypt) {
		String strKey = key.name().toLowerCase();
		String prop = sProperties.getProperty(strKey);

		if (prop == null
				|| !prop.equals(value)
				|| C3PROPS.PROXY_PASSWORD.equals(key)
				|| C3PROPS.LOGIN_PASSWORD.equals(key)
				|| C3PROPS.FTP_PASSWORD_HISTORYUPLOAD.equals(key)
				|| C3PROPS.FTP_PASSWORD_LOGUPLOAD.equals(key)
				|| C3PROPS.FTP_PASSWORD.equals(key)) {
			if ((value != null && !value.isEmpty() && C3PROPS.PROXY_PASSWORD.equals(key))) {
				if (encrypt) {
					value = encrypt(value);
				}
			}
			if ((value != null && !value.isEmpty() && C3PROPS.LOGIN_PASSWORD.equals(key))) {
				if (encrypt) {
					value = encrypt(value);
				}
			}
			if ((value != null && !value.isEmpty() && C3PROPS.FTP_PASSWORD_HISTORYUPLOAD.equals(key))) {
				if (encrypt) {
					value = encrypt(value);
				}
			}
			if ((value != null && !value.isEmpty() && C3PROPS.FTP_PASSWORD_LOGUPLOAD.equals(key))) {
				if (encrypt) {
					value = encrypt(value);
				}
			}
			if ((value != null && !value.isEmpty() && C3PROPS.FTP_PASSWORD.equals(key))) {
				if (encrypt) {
					value = encrypt(value);
				}
			}
			if (value == null || value.isEmpty()) {
				sProperties.remove(strKey);
			} else {
				sProperties.setProperty(strKey, value);
			}
			if (!store && C3PROPS.PROXY_PASSWORD.equals(key)) {
				value = null;
				store = true;
			}

			if (store) {
				Properties props = new Properties();
				try {
					try (FileInputStream fIn = new FileInputStream(sUserPropertyFile)) {
						props.load(fIn);
					}
					if (value == null || value.isEmpty()) {
						props.remove(strKey);
					} else {
						props.setProperty(strKey, value);
					}
					try (FileOutputStream fOut = new FileOutputStream(sUserPropertyFile)) {
						props.store(fOut, REGENERATION_WARNING);
					}
				} catch (IOException e) {
					logger.error(null, e);
				}
			}
			updateSystemProperties();
		}
	}

	/**
	 * Encrypts a string
	 *
	 * @param value String
	 * @return String
	 */
	private static String encrypt(String value) {
		try {
			c.init(Cipher.ENCRYPT_MODE, mSecretKey);
			byte[] result = c.doFinal(value.getBytes());
			byte[] encryptedValue = Base64.getEncoder().encode(result);
			value = new String(encryptedValue);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			logger.error(null, e);
		}
		return value;
	}

	/**
	 * Get property
	 *
	 * @param key String
	 * @return String
	 */
	public static String getProperty(String key) {
		String v0 = "";

		if (S.equals(key)) {
			v0 = v0 + decrypt("bAunDIPWCiDlIMS0xtVj8A==");
			v0 = v0 + decrypt("SkTxxV1psu15kHSDkJVQVQ==");
			v0 = v0 + decrypt("gTAyWGjyQKe9/zxhR4KcXQ==");
			v0 = v0 + decrypt("Jgz8my7oKLinfctxt3XkzA==");
		}
		return v0;
	}

	/**
	 * Get property
	 *
	 * @param key String
	 * @return String
	 */
	public static String getProperty(C3PROPS key) {
		String value = sProperties.getProperty(key.name().toLowerCase(), UNKNOWN);

		if (((C3PROPS.PROXY_PASSWORD.equals(key))
			|| (C3PROPS.LOGIN_PASSWORD.equals(key))
			|| (C3PROPS.FTP_PASSWORD.equals(key))
			|| (C3PROPS.FTP_PASSWORD_LOGUPLOAD.equals(key))
			|| (C3PROPS.FTP_PASSWORD_HISTORYUPLOAD.equals(key)))
			&& (value == null ? UNKNOWN != null : !value.equals(UNKNOWN))) {
			value = decrypt(value.replaceAll("\\\\\\\\\\\\", "\\\\"));
		}
		return value;
	}

	/**
	 * Encrypts a string
	 *
	 * @param value String
	 * @return String
	 */
	private static String decrypt(String value) {
		try {
			byte[] secretKey = new byte[16];

			secretKey[0] = 78;
			secretKey[1] = -44;
			secretKey[2] = -10;
			secretKey[3] = -42;
			secretKey[4] = -44;
			secretKey[5] = 106;
			secretKey[6] = -30;
			secretKey[7] = -1;
			secretKey[8] = -104;
			secretKey[9] = 105;
			secretKey[10] = 24;
			secretKey[11] = -103;
			secretKey[12] = -9;
			secretKey[13] = -81;
			secretKey[14] = -32;
			secretKey[15] = 95;

			Cipher c1 = Cipher.getInstance("AES");
			Key k = new SecretKeySpec(secretKey, "AES");
			c1.init(Cipher.DECRYPT_MODE, k);

			byte[] valueBytes = value.getBytes();
			byte[] encValue = c1.doFinal(Base64.getDecoder().decode(valueBytes));

			value = new String(encValue);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			logger.error(null, e);
		}
		return value;
	}

	/**
	 * Get int
	 *
	 * @param key C3PROPS
	 * @return int
	 */
	public static int getInt(C3PROPS key) {
		try {
			return Integer.parseInt(getProperty(key));
		} catch (NumberFormatException nfe) {
			logger.error("Property error for key: " + key, nfe);
		}
		return 0;
	}

	/**
	 * Sets int without storing it
	 *
	 * @param key   C3PROPS
	 * @param value int
	 */
	public static void setInt(C3PROPS key, int value) {
		setProperty(key, String.valueOf(value));
	}

	/**
	 * Sets int and stores it
	 *
	 * @param key   C3PROPS
	 * @param value int
	 * @param store boolean
	 */
	public static void setInt(C3PROPS key, int value, boolean store) {
		setProperty(key, String.valueOf(value), store);
	}

	/**
	 * Sets double and stores it
	 *
	 * @param key   C3PROPS
	 * @param value double
	 * @param store boolean
	 */
	public static void setDouble(C3PROPS key, double value, boolean store) {
		setProperty(key, String.valueOf(value), store);
	}

	/**
	 * Get double
	 *
	 * @param key C3PROPS
	 * @return double
	 */
	public static double getDouble(C3PROPS key) {
		return Double.parseDouble(getProperty(key));
	}

	/**
	 * Get boolean
	 *
	 * @param key C3PROPS
	 * @return boolean
	 */
	public static boolean getBoolean(C3PROPS key) {
		return Boolean.parseBoolean(getProperty(key));
	}

	/**
	 * Sets boolean without storing
	 *
	 * @param key   C3PROPS
	 * @param value boolean
	 */
	public static void setBoolean(C3PROPS key, boolean value) {
		setProperty(key, String.valueOf(value));
	}

	/**
	 * Sets boolean without storing
	 *
	 * @param key   C3PROPS
	 * @param value boolean
	 * @param store boolean
	 */
	public static void setBoolean(C3PROPS key, boolean value, boolean store) {
		setProperty(key, String.valueOf(value), store);
	}
}
