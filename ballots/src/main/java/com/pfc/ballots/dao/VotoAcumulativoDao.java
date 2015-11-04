package com.pfc.ballots.dao;

import com.pfc.ballots.entities.VotoAcumulativoText;
import com.pfc.ballots.entities.ballotdata.VotoAcumulativo;

/**
 * Dao Interface to retrieves VotoAcumulativo entity
 * @author Violeta Macaya Sánchez
 * @version 1.0 OCT-2015
 *
 */
public interface VotoAcumulativoDao {
	
	public VotoAcumulativoText getVotoAcumulativoText();
	public void deleteVotoAcumulativoText();
	public void updateVotoAcumulativoText(VotoAcumulativoText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public void store(VotoAcumulativo votoAcumulativo);
	public VotoAcumulativo getByBallotId(String idBallot);
	public void update(VotoAcumulativo updated);
	
}
