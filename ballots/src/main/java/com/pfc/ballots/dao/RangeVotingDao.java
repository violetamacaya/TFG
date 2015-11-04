package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.RangeVotingText;
import com.pfc.ballots.entities.ballotdata.RangeVoting;

public interface RangeVotingDao {
public void store(RangeVoting rangeVoting);
	
	public RangeVoting getByBallotId(String idBallot);
	public RangeVoting getById(String id);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	public List<RangeVoting> retrieveAll();
	public void update(RangeVoting updated);
	
	public RangeVotingText getRangeVotingText();
	public void deleteRangeVotingText();
	public void updateRangeVotingText(RangeVotingText text);
}
