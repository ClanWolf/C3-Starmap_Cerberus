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

	@SuppressWarnings("unused")
	public void addCriteria(String columnName, Object value){
		alPredicate.add(cb.equal(root.get(columnName), value));
	}

	@SuppressWarnings("unused")
	public void addCriteriaOR(Predicate... predicate){
		alPredicate.add(cb.or(predicate));
	}

	@SuppressWarnings("unused")
	public void addCriteriaIsNull(String columnName){
		alPredicate.add(cb.isNull(root.get(columnName)));
	}

	@SuppressWarnings("unused")
	public void addCriteriaIsNotNull(String columnName){
		alPredicate.add(cb.isNotNull(root.get(columnName)));
	}

	@SuppressWarnings("unused")
	public Predicate createPredicate(String columnName, Object value){
		return cb.equal(root.get(columnName), value);
	}

	@SuppressWarnings("unused")
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

	@SuppressWarnings("unused")
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
