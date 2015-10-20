package com.pfc.ballots.dao;

import com.pfc.ballots.entities.CondorcetText;

/**
 * Dao Interface to retrieves Condorcet entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface CondorcetDao {
	
	public CondorcetText getCondorcetText();
	public void deleteCondorcetText();
	public void updateCondorcetText(CondorcetText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
}
