package com.pfc.ballots.dao;

import com.pfc.ballots.entities.HareText;

/**
 * Dao Interface to retrieves Hare entity
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public interface HareDao {
	
	public HareText getHareText();
	public void deleteHareText();
	public void updateHareText(HareText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
}
