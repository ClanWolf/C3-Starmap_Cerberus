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

import net.clanwolf.starmap.logging.C3Logger;
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
	private EntityManager fem = null; // free entity manager

	protected EntityManager getFreeEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	protected EntityManager getEntityManager(Long userID) {
		return EntityManagerHelper.getEntityManager(userID);
	}

	@Override
	public void save(Long userID, Object entity) {
		C3Logger.info("Saving instance (" + entity.getClass().getName() + ")");
		try {
			getEntityManager(userID).persist(entity);
			C3Logger.info("Save successful");
		} catch (RuntimeException re) {
			C3Logger.info("Save failed");
			re.printStackTrace();
			// getEntityManager().clear();
			throw re;
		} catch (Exception e) {
			C3Logger.info("Save failed");
			e.printStackTrace();
			// getEntityManager().clear();
			throw e;
		}
	}

	@Override
	public Object update(Long userID, Object entity) {
		C3Logger.info("Updating instance (" + entity.getClass().getName() + ")");
		try {
			Object result = getEntityManager(userID).merge(entity);
			C3Logger.info("Update successful");
			return result;
		} catch (RuntimeException re) {
			C3Logger.info("Update failed");
			re.printStackTrace();
			// getEntityManager().clear();
			throw re;
		} catch (Exception e) {
			C3Logger.info("Update failed");
			e.printStackTrace();
			// getEntityManager().clear();
			throw e;
		}
	}

	public void delete(Long userID, Object entity, Long id) {
		C3Logger.info("Deleting instance (" + entity.getClass().getName() + ")");
		try {
			entity = getEntityManager(userID).getReference(entity.getClass(), id);
			getEntityManager(userID).remove(entity);
			C3Logger.info("Delete successful");
		} catch (RuntimeException re) {
			C3Logger.info("Delete failed");
			re.printStackTrace();
			// getEntityManager().clear();
			throw re;
		} catch (Exception e) {
			C3Logger.info("Delete failed");
			e.printStackTrace();
			// getEntityManager().clear();
			throw e;
		}
	}

	public Object findById(Class clazz, Long id) {
		return findById(null, clazz, id);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object findById(Long userID, Class clazz, Long id) {
//		C3Logger.info("Finding object instance with id: " + id);
		try {
			if (userID != null) {
				return getEntityManager(userID).find(clazz, id);
			} else {
				if (fem == null) {
					fem = getFreeEntityManager();
					return fem.find(clazz, id);
				} else {
					return fem.find(clazz, id);
				}
			}
		} catch (RuntimeException re) {
			C3Logger.info("Find failed");
			re.printStackTrace();
			// getEntityManager().clear();
			throw re;
		} catch (Exception e) {
			C3Logger.info("Find failed");
			e.printStackTrace();
			// getEntityManager().clear();
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findByProperty(Long userID, String propertyName, final Object value, final int... rowStartIdxAndCount) {
		C3Logger.info("Finding instance (" + this.className + ") with property: " + propertyName + ", value: " + value);
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
			C3Logger.info("Find by property name failed");
			re.printStackTrace();
			// getEntityManager().clear();
			throw re;
		} catch (Exception e) {
			C3Logger.info("Find by property name failed");
			// getEntityManager().clear();
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> findAll(Long userID, final int... rowStartIdxAndCount) {
		C3Logger.info("Finding all instances (" + this.className + ")");
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
			C3Logger.info("Find all failed");
			re.printStackTrace();
			// getEntityManager().clear();
			throw re;
		} catch (Exception e) {
			C3Logger.info("Find all failed");
			e.printStackTrace();
			// getEntityManager().clear();
			throw e;
		}
	}
}
