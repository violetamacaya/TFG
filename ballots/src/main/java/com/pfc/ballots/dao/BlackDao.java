package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.BlackText;
import com.pfc.ballots.entities.ballotdata.Black;

/**
 * Dao Interface to retrieves Black entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface BlackDao {
	
	public BlackText getBlackText();
	public void deleteBlackText();
	public void updateBlackText(BlackText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public List<Black> retrieveAll();
	public void update(Black updated);
	public void store(Black black);
	
	public Black getByBallotId(String idBallot);
	public Black getById(String id);
	
	
}
