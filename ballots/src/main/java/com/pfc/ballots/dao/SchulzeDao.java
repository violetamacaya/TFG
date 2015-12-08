package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.SchulzeText;
import com.pfc.ballots.entities.ballotdata.Schulze;

/**
 * Dao Interface to retrieves Schulze entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface SchulzeDao {
	
	public SchulzeText getSchulzeText();
	public void deleteSchulzeText();
	public void updateSchulzeText(SchulzeText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public List<Schulze> retrieveAll();
	public void update(Schulze updated);
	public void store(Schulze schulze);
	
	public Schulze getByBallotId(String idBallot);
	public Schulze getById(String id);
	
	
}
