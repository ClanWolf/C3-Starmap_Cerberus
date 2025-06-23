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
package net.clanwolf.starmap.server.persistence.daos;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import net.clanwolf.starmap.server.servernexus.ServerNexus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.clanwolf.starmap.server.persistence.EntityManagerHelper;


import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Level;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public abstract class GenericDAO implements IDAO {
	private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	protected String className = "";
	private static EntityManager fem = null; // free entity manager

	protected EntityManager getEntityManager(Long userID) {
		return EntityManagerHelper.getEntityManager(userID);
	}

	@Override
	public synchronized void save(Long userID, Object entity) {
		logger.info("Saving instance (" + entity.getClass().getName() + ")");
		try {
			getEntityManager(userID).persist(entity);
			logger.info("Save successful");
		} catch (Exception re) {
			logger.error("Save failed", re);
			// getEntityManager().clear();
			throw re;
		}
	}

	@Override
	public synchronized Object update(Long userID, Object entity) {
//		logger.info("Updating instance (" + entity.getClass().getName() + ")");
		try {
			Object result = null;
			result = getEntityManager(userID).merge(entity);
//			logger.info("Update successful");
			return result;
		} catch (Exception re) {
			logger.error("Update failed", re);
			// getEntityManager().clear();
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized List<Object> findByProperty(Long userID, String propertyName, final Object value, final int... rowStartIdxAndCount) {
		logger.info("Finding instance (" + this.className + ") with property: " + propertyName + ", value: " + value);
		try {
			final String queryString = "select model from " + this.className + " model where model." + propertyName + "= :propertyValue order by sortOrder";
			Query query = getEntityManager(userID).createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (Exception re) {
			logger.error("Find by property name failed", re);
			// getEntityManager().clear();
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized List<Object> findAll(Long userID, final int... rowStartIdxAndCount) {
		logger.info("Finding all instances (" + this.className + ")");
		try {
			final String queryString = "select model from " + this.className + " model";
			Query query = getEntityManager(userID).createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (Exception re) {
			logger.error("Find all failed", re);
			// getEntityManager().clear();
			throw re;
		}
	}

	public synchronized Object findById(Class clazz, Long id) {
		return findById(null, clazz, id);
	}

	public synchronized void delete(Long userID, Object entity, Long id) {
		logger.info("Deleting instance (" + entity.getClass().getName() + ")");
		try {
			entity = getEntityManager(userID).getReference(entity.getClass(), id);
			getEntityManager(userID).remove(entity);
			logger.info("Delete successful");
		} catch (Exception re) {
			logger.error("Delete failed", re);
			// getEntityManager().clear();
			throw re;
		}
	}

	public synchronized void refresh(Long userID, Object entity) {
		logger.info("Refreshing instance (" + entity.getClass().getName() + ")");
		try {
			getEntityManager(userID).refresh(entity);
			logger.info("Refresh successful");
		} catch (Exception re) {
			logger.error("Refresh failed", re);
			throw re;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized Object findById(Long userID, Class clazz, Long id) {
//		logger.info("Finding object instance with id: " + id);
		try {
			return getEntityManager(userID).find(clazz, id);
		} catch (Exception re) {
			logger.error("Find failed", re);
			// getEntityManager().clear();
			throw re;
		}
	}
}
