package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.BucklinText;
import com.pfc.ballots.entities.ballotdata.Bucklin;

/**
 * Dao Interface to retrieves Bucklin entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface BucklinDao {
	
	public BucklinText getBucklinText();
	public void deleteBucklinText();
	public void updateBucklinText(BucklinText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public List<Bucklin> retrieveAll();
	public void update(Bucklin updated);
	public void store(Bucklin bucklin);
	
	public Bucklin getByBallotId(String idBallot);
	public Bucklin getById(String id);
	
	
}
