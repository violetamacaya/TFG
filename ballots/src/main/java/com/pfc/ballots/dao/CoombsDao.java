package com.pfc.ballots.dao;

import com.pfc.ballots.entities.CoombsText;

/**
 * Dao Interface to retrieves Coombs entity
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public interface CoombsDao {
	
	public CoombsText getCoombsText();
	public void deleteCoombsText();
	public void updateCoombsText(CoombsText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
}
