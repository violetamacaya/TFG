package com.pfc.ballots.dao;

import com.pfc.ballots.entities.NansonText;

/**
 * Dao Interface to retrieves Nanson entity
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public interface NansonDao {
	
	public NansonText getNansonText();
	public void deleteNansonText();
	public void updateNansonText(NansonText text);
	
}
