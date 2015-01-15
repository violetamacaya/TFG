package com.pfc.ballots.dao;

import com.pfc.ballots.entities.CopelandText;

/**
 * Dao Interface to retrieves Copeland entity
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public interface CopelandDao {
	
	public CopelandText getCopelandText();
	public void deleteCopelandText();
	public void updateCopelandText(CopelandText text);
	
}
