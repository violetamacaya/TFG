package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.ApprovalVotingText;
import com.pfc.ballots.entities.ballotdata.ApprovalVoting;

/**
 * Dao Interface to retrieves ApprovalVoting entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface ApprovalVotingDao {
	
	public ApprovalVotingText getApprovalVotingText();
	public void deleteApprovalVotingText();
	public void updateApprovalVotingText(ApprovalVotingText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public void store(ApprovalVoting approvalVoting);
	public ApprovalVoting getByBallotId(String idBallot);
	public ApprovalVoting getById(String id);
	public void update(ApprovalVoting updated);
	public List<ApprovalVoting> retrieveAll();


	
}
