/* ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MK V "Cerberus"                  |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 * ---------------------------------------------------------------- |
 * Info        : http://www.clanwolf.net                            |
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
 * Copyright (c) 2001-2019, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.net;

import net.clanwolf.starmap.client.logging.C3Logger;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;

import java.io.*;
import java.net.*;

/**
 * <p>
 * Title: <b>HTTPReader</b><br>
 * Description: <i>Provides a connection to the net and handles it</i><br>
 * Copyright: Copyright (c) 2004<br>
 * Company: BattleForge<br>
 * </p>
 *
 * @author Meldric
 * @version 1.0
 */
public abstract class HTTP {

	/**
	 * set the hostadress of the http-server
	 *
	 * @param hostaddress
	 *            adress
	 * @throws BFException
	 * @throws MalformedURLException
	 *             exception
	 */
	// public final void setHost(String hostaddress) throws BFException {
	// try {
	// host = new URL(hostaddress);
	//
	// } catch (MalformedURLException e) {
	// throw new BFException(10000, e);
	//
	// } // try
	// }

	/**
	 * Get
	 *
	 * @param host The host
	 * @return String Hostname
	 */
	public static byte[] get(String host) throws IOException, MalformedURLException {
		return get(host, null);
	}

	/**
	 * Get
	 *
	 * @param host     The host
	 * @param encoding Encoding string
	 * @return byte[] The result
	 */
	public static byte[] get(String host, String encoding) throws IOException, MalformedURLException {
		return get(new URL(host), encoding);
	}

	/**
	 * Get
	 *
	 * @param host The host
	 * @return byte[] The result
	 */
	public static byte[] get(URL host) throws IOException, MalformedURLException {
		return get(host, null);

	}

	/**
	 * Get
	 *
	 * @param host     The host
	 * @param encoding Encoding string
	 * @return byte[] The result
	 */
	public static byte[] get(URL host, String encoding) throws IOException {
		HttpURLConnection connection = null;

		try {
			if (C3Properties.getBoolean(C3PROPS.USE_PROXY)) {
				String proxyHost = C3Properties.getProperty(C3PROPS.PROXY_SERVER);
				int proxyPort = C3Properties.getInt(C3PROPS.PROXY_PORT);
				Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
				connection = (HttpURLConnection) host.openConnection(p);
				// Log.info("Using proxy");
			} else {
				connection = (HttpURLConnection) host.openConnection();
				// Log.info("Using NO proxy");
			}

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false); // get info fresh from server
			connection.setRequestProperty("Content-type", "text/plain");

			// connect to Server
			connection.connect();

			checkResponseCode(connection);
			ByteArrayOutputStream baos;
			try (InputStream is = connection.getInputStream()) {
				byte[] buffer = new byte[10 * 1024];
				int read = is.read(buffer);
				baos = new ByteArrayOutputStream();
				while (read != -1) {
					baos.write(buffer, 0, read);
					read = is.read(buffer);
				}
			}

			checkResponseCode(connection);
			return baos.toByteArray();

		} catch (IOException e) {
			throw e;
		} finally {
			if (connection != null) {
				connection.disconnect();
			} // if
		} // try
	}

	private static void checkResponseCode(HttpURLConnection connection) throws IOException {
		if (connection.getResponseCode() / 100 != 2) {
			C3Logger.warning("WARNING: " + connection.getResponseCode());
		}
	}

	/**
	 * Download
	 *
	 * @param address       The adress of the file to be loaded
	 * @param localFileName The filename to store the file to
	 */
	public static void download(String address, String localFileName) throws Exception {
		OutputStream out = null;
		InputStream in = null;
		URLConnection conn;

		try {
			URL url = new URL(address);

			if (C3Properties.getBoolean(C3PROPS.USE_PROXY)) {
				String proxyHost = C3Properties.getProperty(C3PROPS.PROXY_SERVER);
				int proxyPort = C3Properties.getInt(C3PROPS.PROXY_PORT);
				Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
				conn = url.openConnection(p);
			} else {
				conn = url.openConnection();
			}
			// conn.setRequestProperty("referer",
			// "https://translate.google.com/");
			conn.setRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");

			in = conn.getInputStream();
			byte[] buffer = new byte[1024];

			int numRead;
			// long numWritten = 0;

			out = new BufferedOutputStream(new FileOutputStream(localFileName));
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				// numWritten += numRead;
			}
		} catch (Exception e) {
			C3Logger.exception(null, e);
			throw (e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
				C3Logger.exception(null, ioe);
			}
		}
	}

	/**
	 * Download
	 *
	 * @param address The adress of the file to be loaded
	 */
	public static void download(String address) {
		int lastSlashIndex = address.lastIndexOf('/');
		if (lastSlashIndex >= 0 && lastSlashIndex < address.length() - 1) {
			try {
				download(address, address.substring(lastSlashIndex + 1));
			} catch (Exception e) {
				C3Logger.exception(null, e);
			}
		} else {
			System.err.println("Could not figure out local file name for " + address);
		}
	}
}