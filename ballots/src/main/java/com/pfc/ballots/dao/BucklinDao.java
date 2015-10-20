package com.pfc.ballots.dao;

import com.pfc.ballots.entities.BucklinText;

/**
 * Dao Interface to retrieves Bucklin entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface BucklinDao {
	
	public BucklinText getBucklinText();
	public void deleteBucklinText();
	public void updateBucklinText(BucklinText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
}
