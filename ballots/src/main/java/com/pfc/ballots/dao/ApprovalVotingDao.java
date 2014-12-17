package com.pfc.ballots.dao;

import com.pfc.ballots.entities.ApprovalVotingText;

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
	
}
