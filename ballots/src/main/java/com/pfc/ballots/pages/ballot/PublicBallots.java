package com.pfc.ballots.pages.ballot;

import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.pfc.ballots.dao.BallotDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.VoteDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.data.Method;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;

public class PublicBallots {
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 //////////////////////////////////////////////////// GENERAL STUFF //////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SessionState
	private DataSession datasession;
	
	@Inject
	private ComponentResources componentResources;
	
	
	  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////// DAO ///////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	@Persist
	BallotDao ballotDao;
	@Persist
	VoteDao voteDao;

	
	
	  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ///////////////////////////////////////////////////// INITIALIZE //////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();
			ballotDao=DB4O.getBallotDao(datasession.getDBName());
			voteDao=DB4O.getVoteDao(datasession.getDBName());
			
			
			publicNonActiveBallots=new LinkedList<Ballot>();
			publicActiveBallots=new LinkedList<Ballot>();
			publicEndedBallots=new LinkedList<Ballot>();
			publicCurrentBallots= ballotDao.getPublics(publicNonActiveBallots, publicActiveBallots, publicEndedBallots);
	
	}
	

	
	
	  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////// PAGE //////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Property
	@Persist
	private List<Ballot> publicNonActiveBallots;
	@Property
	@Persist
	private List<Ballot> publicActiveBallots;
	@Property
	@Persist
	private List<Ballot> publicEndedBallots;
	@Property
	@Persist
	private List<Ballot> publicCurrentBallots;
	@Property
	private Ballot ballot;
	
	
	
	public String getMetodo()
	{
		if(ballot.getMethod()==Method.MAYORIA_RELATIVA)
		{
			return "MAYORIA RELATIVA";
		}
		else if(ballot.getMethod()==Method.KEMENY)
		{
			return "KEMENY";
		}
		else if(ballot.getMethod()==Method.BORDA)
		{
			return "BORDA";
		}
		else if(ballot.getMethod()==Method.RANGE_VOTING)
		{
			return "RANGE VOTING";
		}
		return null;
	}
	

	public boolean isShowCurrent()
	{
		if(publicCurrentBallots.size()==0)
			return true;
		return false;
	}
	
	public boolean isShowPublicActive()
	{
		if(publicActiveBallots.size()==0)
			return false;
		return true;
	}
	public boolean isShowPublicEnded()
	{
		if(publicEndedBallots.size()==0)
			return false;
		return true;
	}
	
   @SessionAttribute
	private String contextBallotId;
	public Object onActionFromVotePublicBallot(String id)
	{
		contextBallotId=id;
		return VoteBallot.class;
	}
	
	@SessionAttribute
	private String contextResultBallotId;
	public Object onActionFromResultPublicBallot(String id)
	{
		contextResultBallotId=id;
		return ResultBallot.class;
	}
	

	  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////// ON ACTIVATE //////////////////////////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	* Controls if the user can enter in the page
	* @return another page if the user can't enter
	*/
	public Object onActivate()
	{
		
		switch(datasession.sessionState())
		{
			case 0:
				return null;
			case 1:
				return Index.class;
			case 2:
				return Index.class;
			case 3:
				return SessionExpired.class;
			default:
				return Index.class;
		}
		
	}
}
