package com.pfc.ballots.dao;

import com.pfc.ballots.entities.BramsText;
import com.pfc.ballots.entities.ballotdata.Brams;

/**
 * Dao Interface to retrieves Brams entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 OCT-2015
 *
 */
public interface BramsDao {
	
	public BramsText getBramsText();
	public void deleteBramsText();
	public void updateBramsText(BramsText text);
	
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	public void store(Brams brams);
	public Brams getByBallotId(String idBallot);
	public void update(Brams updated);
	
}
