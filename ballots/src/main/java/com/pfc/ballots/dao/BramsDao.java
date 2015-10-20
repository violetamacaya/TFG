package com.pfc.ballots.dao;

import com.pfc.ballots.entities.BramsText;

/**
 * Dao Interface to retrieves Brams entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface BramsDao {
	
	public BramsText getBramsText();
	public void deleteBramsText();
	public void updateBramsText(BramsText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
}
