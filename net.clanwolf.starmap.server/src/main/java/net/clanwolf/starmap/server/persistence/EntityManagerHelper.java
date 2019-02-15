/* ---------------------------------------------------------------- |
 * W-7 Research Group / C3                                          |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *          W-7 Facility / Research, Software Development           |
 *                    Tranquil (Mobile Division)                    |
 * __        __  _____   ____                               _       |
 * \ \      / / |___  | |  _ \ ___  ___  ___  __ _ _ __ ___| |__    |
 *  \ \ /\ / /____ / /  | |_) / _ \/ __|/ _ \/ _` | '__/ __| '_ \   |
 *   \ V  V /_____/ /   |  _ <  __/\__ \  __/ (_| | | | (__| | | |  |
 *    \_/\_/     /_/    |_| \_\___||___/\___|\__,_|_|  \___|_| |_|  |
 *                                                                  |
 *  W-7 is the production facility of Clan Wolf. The home base is   |
 *  Tranquil, but there are several mobile departments. In the      |
 *  development department there is a small group of developers and |
 *  designers busy to field new software products for battlefield   |
 *  commanders as well as for strategic dimensions of the clans     |
 *  operations. The department is led by a experienced StarColonel  |
 *  who fell out of active duty due to a wound he suffered during   |
 *  the battle on Tukkayid. His name and dossier are classified,    |
 *  get in contact through regular chain of command.                |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *    ____ _____                                                    |
 *   / ___|___ /                   Communicate - Command - Control  |
 *  | |     |_ \                   MkIII "Damien"                   |
 *  | |___ ___) |                                                   |
 *   \____|____/                                                    |
 *                                                                  |
 *  One of the products used to control the production and the      |
 *  transport of frontline troops is C3. C3 stands for              |
 *  "Communication - Command - Control".                            |
 *  Because there is a field based system to control the            |
 *  communication and data transfer of Mechs, this system is often  |
 *  refered to as "Big C3", however, the official name is           |
 *  "W-7 C3 / MkIII 'Damien'".                                      |
 *                                                                  |
 *  Licensing through W-7 Facility Central Office, Tranquil.        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  Info        : http://www.clanwolf.net                           |
 *  Forum       : http://www.clanwolf.net                           |
 *  Web         : http://c3.clanwolf.net                            |
 *  GitHub      : https://github.com/ClanWolf/C3-Java_Client        |
 *                                                                  |
 *  IRC         : starmap.clanwolf.net @ Quakenet                        |
 *                                                                  |
 *  Devs        : - Christian (Meldric)                    [active] |
 *                - Werner (Undertaker)                    [active] |
 *                - Thomas (xfirestorm)                    [active] |
 *                - Domenico (Nonnex)                     [retired] |
 *                - Dirk (kotzbroken2)                    [retired] |
 *                                                                  |
 *                  (see Wolfnet for up-to-date information)        |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 *  C3 includes libraries and source code by various authors,       |
 *  for credits and info, see README.                               |
 *                                                                  |
 * ---------------------------------------------------------------- |
 *                                                                  |
 * Copyright 2016 ClanWolf.net                                      |
 *                                                                  |
 * Licensed under the Apache License, Version 2.0 (the "License");  |
 * you may not use this file except in compliance with the License. |
 * You may obtain a copy of the License at                          |
 *                                                                  |
 * http://www.apache.org/licenses/LICENSE-2.0                       |
 *                                                                  |
 * Unless required by applicable law or agreed to in writing,       |
 * software distributed under the License is distributed on an "AS  |
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  |
 * express or implied. See the License for the specific language    |
 * governing permissions and limitations under the License.         |
 *                                                                  |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author MyEclipse Persistence Tools
 */
public class EntityManagerHelper {

	private static final EntityManagerFactory emf;
	private static final Logger logger;
	private static final HashMap<Object, EntityManager> emMap = new HashMap<>();

	static {
		final Properties auth = new Properties();
		try {
			final String authFileName = "auth.properties";
			InputStream inputStream = EntityManagerHelper.class.getClassLoader().getResourceAsStream(authFileName);
			if (inputStream != null) {
				auth.load(inputStream);
			} else {
				throw new FileNotFoundException("Auth-Property file '" + authFileName + "' not found in classpath.");
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		Map<String, String> properties = new HashMap<>();
		properties.put("hibernate.connection.autoReconnect", "true");
		properties.put("hibernate.connection.autoReconnectForPools", "true");
		properties.put("hibernate.connection.is-connection-validation-required", "true");
		properties.put("hibernate.connection.user", auth.getProperty("user"));
		properties.put("hibernate.connection.password", auth.getProperty("password"));
		properties.put("hibernate.show_sql", "true");
		properties.put("hibernate.format_sql", "true");

		emf = Persistence.createEntityManagerFactory("starmap", properties);

		logger = Logger.getLogger("starmap");
		logger.setLevel(Level.SEVERE);
	}

	public static EntityManager getEntityManager() {
		Logger.getGlobal().info("Create free EntityManager");
		EntityManager manager = emf.createEntityManager();
		return manager;
	}

	public static EntityManager getEntityManager(Long userID) {
		EntityManager manager = emMap.get(userID);

		if (manager == null || !manager.isOpen()) {
			Logger.getGlobal().info("Create new EntiyManager for UserPOJO ID: " + userID);

			manager = emf.createEntityManager();
			emMap.put(userID, manager);

		} else {
			Logger.getGlobal().info("Find EntiyManager for UserPOJO ID: " + userID);

		}
		return manager;
	}

	public static void closeEntityManager(Long userID) {
		EntityManager manager = emMap.get(userID);
		if (manager != null) {
			Logger.getGlobal().info("Close EntiyManager for UserPOJO ID: " + userID);
			// manager.unwrap(Session.class).close();
			manager.close();
			emMap.remove(userID);
		}
	}

	public static void beginTransaction(Long userID) {
		getEntityManager(userID).getTransaction().begin();
	}

	public static void commit(Long userID) {
		getEntityManager(userID).getTransaction().commit();
	}

	public static void rollback(Long userID) {
		getEntityManager(userID).getTransaction().rollback();
	}

	public static Query createQuery(Long userID, String query) {
		return getEntityManager(userID).createQuery(query);
	}

	public static void log(String info, Level level, Throwable ex) {
		logger.log(level, info, ex);
	}
}
