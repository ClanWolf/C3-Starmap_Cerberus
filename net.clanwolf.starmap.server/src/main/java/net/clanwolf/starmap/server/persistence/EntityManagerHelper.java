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
package net.clanwolf.starmap.server.persistence;

import net.clanwolf.starmap.logging.C3Logger;

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

/**
 * @author MyEclipse Persistence Tools
 */
public class EntityManagerHelper {

	private static final EntityManagerFactory emf;
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
		properties.put("hibernate.show_sql", "false");
		properties.put("hibernate.format_sql", "true");

		emf = Persistence.createEntityManagerFactory("starmap", properties);
	}

	public static EntityManager getNewEntityManager() {
//		C3Logger.info("Create free EntityManager");
		return emf.createEntityManager();
	}

	public static EntityManager getEntityManager(Long userID) {
		EntityManager manager = emMap.get(userID);

		if (manager == null || !manager.isOpen()) {
			C3Logger.info("Create new EntityManager for UserPOJO ID: " + userID);

			manager = emf.createEntityManager();
			emMap.put(userID, manager);

		} else {
			C3Logger.info("Find EntityManager for UserPOJO ID: " + userID);
		}
		return manager;
	}

	public static void closeEntityManager(Long userID) {
		EntityManager manager = emMap.get(userID);
		if (manager != null) {
			C3Logger.info("Close EntityManager for UserPOJO ID: " + userID);
			// manager.unwrap(Session.class).close();
			manager.close();
			emMap.remove(userID);
		}
	}

	@SuppressWarnings("unused")
	public static void beginTransaction(Long userID) {
		getEntityManager(userID).getTransaction().begin();
	}

	@SuppressWarnings("unused")
	public static void commit(Long userID) {
		getEntityManager(userID).getTransaction().commit();
	}

	@SuppressWarnings("unused")
	public static void rollback(Long userID) {
		getEntityManager(userID).getTransaction().rollback();
	}

	@SuppressWarnings("unused")
	public static Query createQuery(Long userID, String query) {
		return getEntityManager(userID).createQuery(query);
	}
}
