package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.CondorcetText;
import com.pfc.ballots.entities.ballotdata.Condorcet;

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
	
	public void store(Condorcet Condorcet);
	public Condorcet getByBallotId(String idBallot);
	public Condorcet getById(String id);
	public List<Condorcet> retrieveAll();
	public void update(Condorcet updated);
	
}
