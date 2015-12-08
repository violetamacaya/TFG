package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.HareText;
import com.pfc.ballots.entities.ballotdata.Hare;

/**
 * Dao Interface to retrieves Hare entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface HareDao {
	
	public HareText getHareText();
	public void deleteHareText();
	public void updateHareText(HareText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public List<Hare> retrieveAll();
	public void update(Hare updated);
	public void store(Hare hare);
	
	public Hare getByBallotId(String idBallot);
	public Hare getById(String id);
	
	
}
