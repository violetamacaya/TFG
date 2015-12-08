package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.CoombsText;
import com.pfc.ballots.entities.ballotdata.Coombs;

/**
 * Dao Interface to retrieves Coombs entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface CoombsDao {
	
	public CoombsText getCoombsText();
	public void deleteCoombsText();
	public void updateCoombsText(CoombsText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public List<Coombs> retrieveAll();
	public void update(Coombs updated);
	public void store(Coombs coombs);
	
	public Coombs getByBallotId(String idBallot);
	public Coombs getById(String id);
	
	
}
