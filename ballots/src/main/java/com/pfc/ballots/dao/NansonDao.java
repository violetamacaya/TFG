package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.NansonText;
import com.pfc.ballots.entities.ballotdata.Nanson;

/**
 * Dao Interface to retrieves Nanson entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface NansonDao {
	
	public NansonText getNansonText();
	public void deleteNansonText();
	public void updateNansonText(NansonText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public List<Nanson> retrieveAll();
	public void update(Nanson updated);
	public void store(Nanson nanson);
	
	public Nanson getByBallotId(String idBallot);
	public Nanson getById(String id);
	
	
}
