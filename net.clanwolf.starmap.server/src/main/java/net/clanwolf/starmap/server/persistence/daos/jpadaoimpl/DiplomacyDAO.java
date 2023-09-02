package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import net.clanwolf.starmap.server.persistence.CriteriaHelper;
import net.clanwolf.starmap.server.persistence.daos.GenericDAO;
import net.clanwolf.starmap.server.persistence.pojos.DiplomacyPOJO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DiplomacyDAO extends GenericDAO {

	private static DiplomacyDAO instance;

	public static DiplomacyDAO getInstance() {
		if (instance == null) {
			instance = new DiplomacyDAO();
			instance.className = "DiplomacyPOJO";
		}
		return instance;
	}

	private DiplomacyDAO() {
		// Empty constructor
	}

	@Override
	public void delete(Long userID, Object entity) {
		super.delete(userID, entity, ((DiplomacyPOJO) entity).getId());
	}

	@Override
	public DiplomacyPOJO update(Long userID, Object entity) {
		return (DiplomacyPOJO) super.update(userID, entity);
	}

	@Override
	public DiplomacyPOJO findById(Long userID, Long id) {
		return (DiplomacyPOJO) super.findById(userID, DiplomacyPOJO.class, id);
	}

	public DiplomacyPOJO findByKey(Long userID, String key) {
		CriteriaHelper crit = new CriteriaHelper(DiplomacyPOJO.class);
		crit.addCriteria("key", key);

		return (DiplomacyPOJO) crit.getSingleResult();
	}

	public ArrayList<DiplomacyPOJO> getDiplomacyForSeason(Long seasonID, Long roundId){

		CriteriaHelper crit = new CriteriaHelper(DiplomacyPOJO.class);
		crit.addCriteria("seasonID", seasonID);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<DiplomacyPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) {
			DiplomacyPOJO pojo = (DiplomacyPOJO) iter.next();
			if(pojo.getStartingInRound() <= roundId) {
				lRPS.add(pojo);
			}
		}

		return lRPS;
	}

	public ArrayList<DiplomacyPOJO> getAllRequestsForFactions(Long factionID, Long roundId){

		CriteriaHelper crit = new CriteriaHelper(DiplomacyPOJO.class);
		crit.addCriteria("factionID_REQUEST", factionID);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<DiplomacyPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) {
			DiplomacyPOJO pojo = (DiplomacyPOJO) iter.next();
			if(pojo.getStartingInRound() <= roundId) {
				lRPS.add(pojo);
			}
		}

		return lRPS;
	}
}
