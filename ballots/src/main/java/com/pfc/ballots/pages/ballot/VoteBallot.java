package com.pfc.ballots.pages.ballot;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.BallotDao;
import com.pfc.ballots.dao.CensusDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.RelativeMajorityDao;
import com.pfc.ballots.dao.VoteDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.data.Method;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.entities.Vote;
import com.pfc.ballots.entities.ballotdata.RelativeMajority;
import com.pfc.ballots.pages.Index;

public class VoteBallot {
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 //////////////////////////////////////////////////// GENERAL STUFF //////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SessionState
	private DataSession datasession;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	
	@Property
	@Persist
	private Ballot ballot;
	@Persist
	@Property
	private Vote vote;

	
	@Property
    @SessionAttribute
	private String contextBallotId;
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////////// DAO //////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	@Persist
	CensusDao censusDao;
	@Persist
	BallotDao ballotDao;
	@Persist
	VoteDao voteDao;
	@Persist
	RelativeMajorityDao relativeMajorityDao;
	
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////// INITIALIZE ///////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setupRender()
	{
		System.out.println("HOOLA");
		componentResources.discardPersistentFieldChanges();
		ballotDao=DB4O.getBallotDao(datasession.getDBName());
		ballot=ballotDao.getById(contextBallotId);
		
		voteDao=DB4O.getVoteDao(datasession.getDBName());
		vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
		
		if(ballot.getMethod()==Method.MAYORIA_RELATIVA)
		{
			relativeMajorityDao=DB4O.getRelativeMajorityDao(datasession.getDBName());
			relMay=relativeMajorityDao.getByBallotId(contextBallotId);
			relMayVote=relMay.getOptions().get(0);
		}
	}

	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////// MAYORIA RELATIVA /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Persist
	@Property
	private RelativeMajority relMay;
	@Property
	private String relMayVote;
	@Property
	private String relMayOption;
	
	
	public Object onSuccessFromRelativeMajorityForm()
	{
		vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
		if(!vote.isCounted())
		{
			vote.setCounted(true);
			System.out.println("SI");
			voteDao.updateVote(vote);
			relMay.addVote(relMayOption);
			relativeMajorityDao.update(relMay);
		}
		
		return Index.class;
	}
}
