package net.clanwolf.starmap.server.persistence.daos.jpadaoimpl;

import net.clanwolf.starmap.server.process.diplomacy.BODiplomacyTransfer;
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

	public ArrayList<DiplomacyPOJO> getDiplomacyForSeason(Long seasonID){

		CriteriaHelper crit = new CriteriaHelper(DiplomacyPOJO.class);
		crit.addCriteria("seasonID", seasonID);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<DiplomacyPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) {
			DiplomacyPOJO pojo = (DiplomacyPOJO) iter.next();
			//if(pojo.getStartingInRound() != null && pojo.getStartingInRound() <= roundId.intValue()) {
				lRPS.add(pojo);
			//}
		}

		return lRPS;
	}

	public ArrayList<DiplomacyPOJO> getAllRequestsForFactions(Long seasonID, Long factionID){

		CriteriaHelper crit = new CriteriaHelper(DiplomacyPOJO.class);
		crit.addCriteria("seasonID", seasonID);
		crit.addCriteria("factionID_REQUEST", factionID);

		List<Object> lRes = crit.getResultList();

		Iterator<Object> iter = lRes.iterator();
		ArrayList<DiplomacyPOJO> lRPS = new ArrayList<>();

		while (iter.hasNext()) {
			DiplomacyPOJO pojo = (DiplomacyPOJO) iter.next();
			//if(pojo.getStartingInRound() != null && pojo.getStartingInRound() <= roundId) {
				lRPS.add(pojo);
			//}
		}

		return lRPS;
	}

	public void deleteEntrieForRound(Long userID, Long seasonID, Long round){


		ArrayList<DiplomacyPOJO> allEntries = getDiplomacyForSeason(seasonID);
		BODiplomacyTransfer dipTransfer = new BODiplomacyTransfer(allEntries);

		for(DiplomacyPOJO dip1 : allEntries){
			if(dip1.getEndingInRound() != null && dip1.getEndingInRound() == round.intValue()){
				delete(userID, dip1);
			}
		}
	}

	public void removeAllRequestAfterWaitingPeriod(Long userID, Long seasonID, Long round){

		ArrayList<DiplomacyPOJO> allDiplomacyEntries = getDiplomacyForSeason(seasonID);

		BODiplomacyTransfer boDipTransfer = new BODiplomacyTransfer(allDiplomacyEntries);

		for(DiplomacyPOJO dip: allDiplomacyEntries){

			if(dip.getStartingInRound() <= round - 2
				&& dip.getEndingInRound() == null
				&& !boDipTransfer.factionsAreAllied(dip.getFactionID_REQUEST(), dip.getFactionID_ACCEPTED(), round)){

				DiplomacyDAO.getInstance().delete(userID, dip);
			}

		}
	}

	public void removeAllRequestAfterAttack(Long userID, Long seasonID, Long round, Long factionID1, Long factionID2){

		ArrayList<DiplomacyPOJO> allDiplomacyEntries = getDiplomacyForSeason(seasonID);

		BODiplomacyTransfer boDipTransfer = new BODiplomacyTransfer(allDiplomacyEntries);

		if(!boDipTransfer.factionsAreAllied(factionID1, factionID2, round)) {
			// delete all entries
			for(DiplomacyPOJO pojo: boDipTransfer.getDiplomacyStateForFactions(factionID1,factionID2)) {
				DiplomacyDAO.getInstance().delete(userID,pojo );
			}
		}



	}

}
