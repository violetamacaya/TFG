package com.pfc.ballots.dao;

import com.pfc.ballots.entities.VotoAcumulativoText;

/**
 * Dao Interface to retrieves VotoAcumulativo entity
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public interface VotoAcumulativoDao {
	
	public VotoAcumulativoText getVotoAcumulativoText();
	public void deleteVotoAcumulativoText();
	public void updateVotoAcumulativoText(VotoAcumulativoText text);
	
}
