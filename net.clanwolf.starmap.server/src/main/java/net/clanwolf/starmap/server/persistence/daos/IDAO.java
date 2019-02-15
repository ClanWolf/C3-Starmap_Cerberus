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
package net.clanwolf.starmap.server.persistence.daos;

import java.util.List;

/**
 * Interface for UserDAO.
 * 
 * @author Meldric
 */

public interface IDAO {
	/**
	 * Perform an initial save of a previously unsaved UserPOJO entity. All subsequent persist actions of this entity should use the #update() method. This operation must be performed within the a database transaction context for the entity's data to be
	 * permanently saved to the persistence store, i.e., database. This method uses the {@link javax.persistence.EntityManager#persist(Object) EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * IUserDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            UserPOJO entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Long userID, Object entity);

	/**
	 * Delete a persistent UserPOJO entity. This operation must be performed within the a database transaction context for the entity's data to be permanently deleted from the persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#remove(Object) EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * IKundeDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            UserPOJO entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Long userID, Object entity);

	/**
	 * Persist a previously saved UserPOJO entity and return it or a copy of it to the sender. A copy of the UserPOJO entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. This operation must be
	 * performed within the a database transaction context for the entity's data to be permanently saved to the persistence store, i.e., database. This method uses the {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = IKundeDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            UserPOJO entity to update
	 * @return UserPOJO the persisted UserPOJO entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Object update(Long userID, Object entity);

	public Object findById(Long userID, Long id);

	/**
	 * Find all UserPOJO entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the UserPOJO property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the row index in the query result-set to begin collecting the results. rowStartIdxAndCount[1] specifies the the maximum count of results to return.
	 * @return List<Kunde> found by query
	 */
	public List<Object> findByProperty(Long userID, String propertyName, Object value, int... rowStartIdxAndCount);

	/**
	 * Find all UserPOJO entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the row index in the query result-set to begin collecting the results. rowStartIdxAndCount[1] specifies the the maximum count of results to return.
	 * @return List<Kunde> all Kunde entities
	 */
	public List<Object> findAll(Long userID, int... rowStartIdxAndCount);
}
