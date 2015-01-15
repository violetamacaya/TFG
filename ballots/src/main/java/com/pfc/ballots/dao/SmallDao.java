package com.pfc.ballots.dao;

import com.pfc.ballots.entities.SmallText;

/**
 * Dao Interface to retrieves Small entity
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public interface SmallDao {
	
	public SmallText getSmallText();
	public void deleteSmallText();
	public void updateSmallText(SmallText text);
	
}
