package com.pfc.ballots.dao;

import com.pfc.ballots.entities.DodgsonText;

/**
 * Dao Interface to retrieves Dodgson entity
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public interface DodgsonDao {
	
	public DodgsonText getDodgsonText();
	public void deleteDodgsonText();
	public void updateDodgsonText(DodgsonText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
}
