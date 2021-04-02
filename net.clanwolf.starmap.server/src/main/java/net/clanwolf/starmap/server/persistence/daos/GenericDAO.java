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
package net.clanwolf.starmap.server.persistence.daos;

import net.clanwolf.starmap.server.persistence.EntityManagerHelper;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Level;

/**
 * A data access object (DAO) providing persistence and search support for UserPOJO entities.
 * Transaction control of the save(), update() and delete() operations must be handled externally
 * by senders of these methods or must be manually added to each of these methods for data to be
 * persisted to the JPA datastore.
 */
public abstract class GenericDAO implements IDAO {

	protected String className = "";

	protected EntityManager getEntityManager(Long userID) {
		return EntityManagerHelper.getEntityManager(userID);
	}

	@Override
	public void save(Long userID, Object entity) {
		EntityManagerHelper.log("Saving instance (" + entity.getClass() + ")", Level.INFO, null);
		try {
			getEntityManager(userID).persist(entity);
			EntityManagerHelper.log("Save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("Save failed", Level.SEVERE, re);
			// getEntityManager().clear();
			throw re;
		}
	}

	@Override
	public Object update(Long userID, Object entity) {
		EntityManagerHelper.log("Updating instance (" + entity.getClass() + ")", Level.INFO, null);

		try {
			Object result = getEntityManager(userID).merge(entity);
			EntityManagerHelper.log("Update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("Update failed", Level.SEVERE, re);
			// getEntityManager().clear();
			throw re;
		}
	}

	public void delete(Long userID, Object entity, Long id) {
		EntityManagerHelper.log("Deleting instance (" + entity.getClass()  + ")", Level.INFO, null);

		try {
			entity = getEntityManager(userID).getReference(entity.getClass(), id);
			getEntityManager(userID).remove(entity);
			EntityManagerHelper.log("Delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("Delete failed", Level.SEVERE, re);
			// getEntityManager().clear();
			throw re;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object findById(Long userID, Class clazz, Long id) {
		EntityManagerHelper.log("Finding object instance with id: " + id, Level.INFO, null);
		try {
			return getEntityManager(userID).find(clazz, id);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("Find failed", Level.SEVERE, re);
			// getEntityManager().clear();
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findByProperty(Long userID, String propertyName, final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("Finding instance (" + this.className + ") with property: " + propertyName + ", value: " + value, Level.INFO, null);
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
		} catch (RuntimeException re) {
			EntityManagerHelper.log("Find by property name failed", Level.SEVERE, re);
			// getEntityManager().clear();
			throw re;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findAll(Long userID, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("Finding all instances (" + this.className + ")", Level.INFO, null);
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
		} catch (RuntimeException re) {
			EntityManagerHelper.log("Find all failed", Level.SEVERE, re);
			// getEntityManager().clear();
			throw re;
		}
	}
}
