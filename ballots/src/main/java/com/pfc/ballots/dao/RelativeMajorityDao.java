package com.pfc.ballots.dao;

import com.pfc.ballots.entities.ballotdata.RelativeMajority;

public interface RelativeMajorityDao {
	
	public void store(RelativeMajority relativeMajority);
	
	public RelativeMajority getByBallotId(String idBallot);
	public RelativeMajority getById(String id);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);

	public void update(RelativeMajority updated);
}
