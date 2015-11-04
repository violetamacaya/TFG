package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.entities.ballotdata.RelativeMajority;
/**
 * Dao Interface to retrieves RelativeMajority entity
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUL-2014
 *
 */
public interface RelativeMajorityDao {
	
	public void store(RelativeMajority relativeMajority);
	
	public RelativeMajority getByBallotId(String idBallot);
	public RelativeMajority getById(String id);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	public List<RelativeMajority> retrieveAll();
	public void update(RelativeMajority updated);
}
