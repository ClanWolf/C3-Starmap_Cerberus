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
 * Copyright (c) 2001-2024, ClanWolf.net                            |
 * ---------------------------------------------------------------- |
 */
package net.clanwolf.starmap.server.persistence;


import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;

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
	private ArrayList<Order> alOrderBy;

	public CriteriaHelper(Class clazz){
		this.clazz = clazz;
		init();
	}

	private void init(){
		cb = EntityManagerHelper.getNewEntityManager().getCriteriaBuilder();
		query = cb.createQuery(clazz);
		root = query.from(clazz);
		alPredicate = new ArrayList<Predicate>();
		alOrderBy = new ArrayList<Order>();
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

	public void addOrderByAsc(String columnName){
		//orderBy = cb.asc(root.get(columnName));
		alOrderBy.add(cb.asc(root.get(columnName)));
	};

	public void addOrderByDesc(String columnName){
		//orderBy = cb.desc(root.get(columnName));
		alOrderBy.add(cb.desc(root.get(columnName)));
	};

	@SuppressWarnings("unused")
	public Object getSingleResult(){

		//return getSingleResult(null);
		query.select(root);

		Predicate[] predicateArr = new Predicate[alPredicate.size()];
		predicateArr = alPredicate.toArray(predicateArr);

		query.select(root).where(predicateArr);
		Query q = EntityManagerHelper.getNewEntityManager().createQuery(query);

		try {
			return q.getSingleResult();

		} catch (NoResultException e) {
			// No resultset given back
		}
		return null;
	}

	@SuppressWarnings("unused")
	public Object getSingleResult(Long userID){
		return getSingleResult();
	}

	@SuppressWarnings("unused")
	public List<Object> getResultList(){
		return getResultList(null);	}

	public List<Object> getResultList(Long userID){
		query.select(root);

		Predicate[] predicateArr = new Predicate[alPredicate.size()];
		predicateArr = alPredicate.toArray(predicateArr);
		Query q = null;

		if(alOrderBy.size() == 0){
			query.select(root).where(predicateArr);
		} else {
			query.select(root).where(predicateArr).orderBy(alOrderBy);
		}

		if (userID != null) {
			q = EntityManagerHelper.getEntityManager(userID).createQuery(query);
		} else {
			q = EntityManagerHelper.getNewEntityManager().createQuery(query);
		}

		try {
			return q.getResultList();

		} catch (NoResultException e) {
			// No resultset given back
			e.printStackTrace();
		}
		return null;
	}

	/*
	public List<Object> getResultList(Long userID, String sortColumn){
		query.select(root);

		Predicate[] predicateArr = new Predicate[alPredicate.size()];
		predicateArr = alPredicate.toArray(predicateArr);
		Query q = null;
		query.select(root).where(predicateArr);
		query.select(root).where(predicateArr).orderBy(orderBy);
		if (userID != null) {
			q = EntityManagerHelper.getEntityManager(userID).createQuery(query);
		} else {
			q = EntityManagerHelper.getNewEntityManager().createQuery(query);
		}

		try {
			return q.getResultList();

		} catch (NoResultException e) {
			// No resultset given back
			e.printStackTrace();
		}
		return null;
	}
	*/
}
