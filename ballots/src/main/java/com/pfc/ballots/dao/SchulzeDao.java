package com.pfc.ballots.dao;

import com.pfc.ballots.entities.SchulzeText;

/**
 * Dao Interface to retrieves Schulze entity
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public interface SchulzeDao {
	
	public SchulzeText getSchulzeText();
	public void deleteSchulzeText();
	public void updateSchulzeText(SchulzeText text);
	
}
