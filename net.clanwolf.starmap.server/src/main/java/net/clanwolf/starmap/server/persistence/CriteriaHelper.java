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

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Undertaker
 *
 */
@SuppressWarnings( { "rawtypes", "unchecked" } )
public class CriteriaHelper {

	private CriteriaBuilder cb;
	private CriteriaQuery query;	
	private Class clazz;
	private ArrayList<Predicate> alPredicate;
	private Root root;

	public CriteriaHelper(Class clazz){
		this.clazz = clazz;
		init();
	}
	
	private void init(){
		cb = EntityManagerHelper.getEntityManager().getCriteriaBuilder();
		query = cb.createQuery(clazz);
		root = query.from(clazz);
		alPredicate = new ArrayList<Predicate>();
	}
	
	public void addCriteria(String columnName, Object value){
		alPredicate.add(cb.equal(root.get(columnName), value));
	}
	
	public void addCriteriaOR(Predicate... predicate){
		alPredicate.add(cb.or(predicate));
	}
	
	public void addCriteriaIsNull(String columnName){
		alPredicate.add(cb.isNull(root.get(columnName)));
	}
	
	public void addCriteriaIsNotNull(String columnName){
		alPredicate.add(cb.isNotNull(root.get(columnName)));
	}
	
	public Predicate createPredicate(String columnName, Object value){
		return cb.equal(root.get(columnName), value);
	}
	
	public Object getSingleResult(){
		query.select(root);	
		
		Predicate[] predicateArr = new Predicate[alPredicate.size()];
		predicateArr = alPredicate.toArray(predicateArr);
		
		query.select(root).where(predicateArr);
		Query q = EntityManagerHelper.getEntityManager().createQuery(query);
		
		try {
			return q.getSingleResult();
			
		} catch (NoResultException e) {
			// No resultset given back
		}
		return null;
	}	
	
	public List<Object> getResultList(){
		query.select(root);	
		
		Predicate[] predicateArr = new Predicate[alPredicate.size()];
		predicateArr = alPredicate.toArray(predicateArr);
		
		query.select(root).where(predicateArr);
		Query q = EntityManagerHelper.getEntityManager().createQuery(query);
		
		try {
			return q.getResultList();
			
		} catch (NoResultException e) {
			// No resultset given back
		}
		
		return null;
	}	
}
