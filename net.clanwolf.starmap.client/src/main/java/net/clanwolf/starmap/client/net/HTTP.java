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
 * Copyright (c) 2001-2023, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.client.net;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.client.util.C3PROPS;
import net.clanwolf.starmap.client.util.C3Properties;

import javax.net.ssl.*;
import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Provides http functionality
 *
 * @author Meldric
 */
public abstract class HTTP {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

//	/**
//	 * set the hostadress of the http-server
//	 *
//	 * @param hostaddress
//	 *            adress
//	 * @throws BFException
//	 * @throws MalformedURLException
//	 *             exception
//	 */
//	 public final void setHost(String hostaddress) throws BFException {
//	 try {
//	 host = new URL(hostaddress);
//
//	 } catch (MalformedURLException e) {
//	 throw new BFException(10000, e);
//
//	 } // try
//	 }

	/**
	 * Get
	 *
	 * @param host The host
	 * @return String Hostname
	 */
	public static byte[] get(String host) throws IOException, MalformedURLException, URISyntaxException {
		return get(host, null);
	}

	/**
	 * Get
	 *
	 * @param host     The host
	 * @param encoding Encoding string
	 * @return byte[] The result
	 */
	public static byte[] get(String host, String encoding) throws IOException, MalformedURLException, URISyntaxException {
		URI uri = new URI(host);
		return get(uri.toURL(), encoding);
	}

	/**
	 * Get
	 *
	 * @param url The host
	 * @return byte[] The result
	 */
	public static byte[] get(URL url) throws IOException, MalformedURLException {
		return get(url, null);

	}

	/**
	 * Get
	 */
	public static byte[] get(URL url, String encoding) throws IOException {
		HttpsURLConnection conn = null;

		// ERROR: java 11 ssl handshake failure
		// https://stackoverflow.com/questions/57601284/java-11-and-12-ssl-sockets-fail-on-a-handshake-failure-error-with-tlsv1-3-enable
		// Solution:
		//      had to add
		//      requires jdk.crypto.ec;
		//      requires jdk.crypto.cryptoki;
		//      into module-info.java

		// Solution:
		//      https://stackoverflow.com/questions/54770538/received-fatal-alert-handshake-failure-in-jlinked-jre

		// Install the all-trusting trust manager
		try {
			// configure the SSLContext with a TrustManager
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
			SSLContext.setDefault(ctx);

			Properties props = System.getProperties();
			props.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");

			if (C3Properties.getBoolean(C3PROPS.USE_PROXY)) {
				//logger.warn("Using proxy.");
				String proxyHost = C3Properties.getProperty(C3PROPS.PROXY_SERVER);
				int proxyPort = C3Properties.getInt(C3PROPS.PROXY_PORT);
				Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
				conn = (HttpsURLConnection) url.openConnection(p);
			} else {
				//logger.warn("Using NO proxy.");
				conn = (HttpsURLConnection) url.openConnection();
			}

			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false); // get info fresh from server
			conn.setRequestProperty("Content-type", "text/plain");

			// connect to Server
			//logger.warn("Connecting.");
			conn.connect();

			checkResponseCode(conn);

			ByteArrayOutputStream baos;
			try (InputStream is = conn.getInputStream()) {
				byte[] buffer = new byte[10 * 1024];
				int read = is.read(buffer);
				baos = new ByteArrayOutputStream();
				while (read != -1) {
					baos.write(buffer, 0, read);
					read = is.read(buffer);
				}
			}

			checkResponseCode(conn);
			return baos.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	private static void checkResponseCode(HttpURLConnection connection) throws IOException {
		if (connection.getResponseCode() / 100 != 2) {
			logger.warn("WARNING: " + connection.getResponseCode());
		} else {
			//logger.warn("Connection response: " + connection.getResponseCode());
		}
	}

	/**
	 * Download
	 *
	 * @param address       The adress of the file to be loaded
	 * @param localFileName The filename to store the file to
	 */
	public static void download(String address, String localFileName) throws Exception {
		Runnable runnable = () -> {
			OutputStream out = null;
			InputStream in = null;
			URLConnection conn;

			try {
				logger.info("Downloading: " + address);

				URI uri = new URI(address);
				URL url = uri.toURL();

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

				File f = new File(localFileName);
				boolean success = f.getParentFile().mkdirs();
				if (!success) {
					logger.info("Dirs could not be created or existed already.");
				}

				out = new BufferedOutputStream(new FileOutputStream(localFileName));
				while ((numRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, numRead);
					// numWritten += numRead;
				}
			} catch (IOException | URISyntaxException ioe) {
				logger.error(null, ioe);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
				} catch (IOException ioe) {
					logger.error(null, ioe);
				}
			}
		};

		try (ExecutorService es = Executors.newCachedThreadPool()) {
			es.execute(runnable);
			es.shutdown();
			boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Download error", e);
		}

		File f = new File(localFileName);
		if (f.isFile() && f.canRead()) {
			logger.info("Download ok: " + localFileName);
		} else {
			throw new RuntimeException("Download error");
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
				logger.error(null, e);
			}
		} else {
			logger.error("Could not figure out local file name for " + address);
		}
	}

	public static Image getCachedImage(String s, String subPath) throws Exception {
		logger.info("Looking for image for string: " + s);

		String cacheFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "cache" + File.separator + "image" + File.separator + subPath;
		File cacheFolder = new File(cacheFolderName);
		if (!cacheFolder.isDirectory()) {
			boolean success = cacheFolder.mkdirs();
			logger.info("Creating cache folder for image files: " + success);
		}

		String imageFileName = cacheFolderName + File.separator + s;
		File f1 = new File(imageFileName);

		if (!f1.isFile()) {
			String serverUrl = C3Properties.getProperty(C3PROPS.SERVER_URL);
			if (!s.startsWith("/")) {
				s = "/" + s;
			}
			if (!subPath.startsWith("/")) {
				subPath = "/" + subPath;
			}

			HTTP.download(serverUrl + subPath + s, imageFileName);
		}
		return new Image(String.valueOf(f1.toURI()));
	}

	public static Media getCachedVideo(String s, String subPath) throws Exception {
		logger.info("Looking for video for string " + s + " in " + subPath + ".");

		String cacheFolderName = System.getProperty("user.home") + File.separator + ".ClanWolf.net_C3" + File.separator + "cache" + File.separator + "video" + File.separator + subPath;
		File cacheFolder = new File(cacheFolderName);
		if (!cacheFolder.isDirectory()) {
			boolean success = cacheFolder.mkdirs();
			logger.info("Creating cache folder for video files: " + success);
		}

		String videoFileName = (cacheFolderName + File.separator + s).replace("/", "\\");
		File f1 = new File(videoFileName);
		logger.info("Looking for video file: " + f1.getAbsolutePath());

		if (!f1.isFile()) {
			String serverUrl = C3Properties.getProperty(C3PROPS.SERVER_URL);
			if (!s.startsWith("/")) {
				s = "/" + s;
			}
			if (!subPath.startsWith("/")) {
				subPath = "/" + subPath;
			}

			logger.info("Downloading video file: " + serverUrl + subPath + s + " to local target: " + videoFileName);
			HTTP.download(serverUrl + subPath + s, videoFileName);
		} else {
			logger.info("Video file " + f1.getAbsolutePath() + "found.");
		}
		return new Media(f1.toURI().toURL().toExternalForm());
	}

	public static void main(String[] args) {
		try {
			download("https://www.clanwolf.net/images/wolf/clanwolf_logo.png", "c:\\temp\\test.png");
			//Image i = getCachedImage("rpg/resources/1/CWG_Rekrutierung_Titel_01.png");
			//System.out.println(i.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class DefaultTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
