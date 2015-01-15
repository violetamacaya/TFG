package com.pfc.ballots.dao;

import com.pfc.ballots.entities.BlackText;

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
	
}
