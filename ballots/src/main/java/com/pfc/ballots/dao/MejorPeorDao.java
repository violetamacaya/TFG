package com.pfc.ballots.dao;

import com.pfc.ballots.entities.MejorPeorText;

/**
 * Dao Interface to retrieves MejorPeor entity
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public interface MejorPeorDao {
	
	public MejorPeorText getMejorPeorText();
	public void deleteMejorPeorText();
	public void updateMejorPeorText(MejorPeorText text);
	
}
