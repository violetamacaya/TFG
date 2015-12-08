package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.MejorPeorText;
import com.pfc.ballots.entities.ballotdata.MejorPeor;

/**
 * Dao Interface to retrieves MejorPeor entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface MejorPeorDao {
	
	public MejorPeorText getMejorPeorText();
	public void deleteMejorPeorText();
	public void updateMejorPeorText(MejorPeorText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public List<MejorPeor> retrieveAll();
	public void update(MejorPeor updated);
	public void store(MejorPeor mejorPeor);
	
	public MejorPeor getByBallotId(String idBallot);
	public MejorPeor getById(String id);
	
	
}
