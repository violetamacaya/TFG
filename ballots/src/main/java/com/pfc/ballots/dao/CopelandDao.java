package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.CopelandText;
import com.pfc.ballots.entities.ballotdata.Copeland;

/**
 * Dao Interface to retrieves Copeland entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface CopelandDao {
	
	public CopelandText getCopelandText();
	public void deleteCopelandText();
	public void updateCopelandText(CopelandText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public List<Copeland> retrieveAll();
	public void update(Copeland updated);
	public void store(Copeland copeland);
	
	public Copeland getByBallotId(String idBallot);
	public Copeland getById(String id);
	
	
}
