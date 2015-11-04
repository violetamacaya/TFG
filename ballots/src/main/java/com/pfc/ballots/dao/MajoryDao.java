package com.pfc.ballots.dao;

import com.pfc.ballots.entities.MajoryText;

/**
 * Dao Interface to retrieves Majory entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface MajoryDao {
	
	public MajoryText getMajoryText();
	public void deleteMajoryText();
	public void updateMajoryText(MajoryText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
}
