package com.pfc.ballots.pages;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ComponentResources;

import com.pfc.ballots.dao.BallotDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.VoteDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.data.Method;
import com.pfc.ballots.entities.Ballot;

import com.pfc.ballots.pages.ballot.ResultBallot;
import com.pfc.ballots.pages.ballot.VoteBallot;

/**
 * Start page of application ballots.
 */
public class Index
{

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
			if(datasession.isLoged())
			{
				ballotDao=DB4O.getBallotDao(datasession.getDBName());
				voteDao=DB4O.getVoteDao(datasession.getDBName());
			
				nonActiveBallots=new LinkedList<Ballot>();
				activeBallots=new LinkedList<Ballot>();
				endedBallots=new LinkedList<Ballot>();
				publicNonActiveBallots=new LinkedList<Ballot>();
				publicActiveBallots=new LinkedList<Ballot>();
				publicEndedBallots=new LinkedList<Ballot>();
				currentBallots=ballotDao.getById(voteDao.getBallotsWithParticipation(datasession.getId()),nonActiveBallots,activeBallots,endedBallots);
				publicCurrentBallots= ballotDao.getPublics(publicNonActiveBallots, publicActiveBallots, publicEndedBallots);
			}
		}
		

		
		
		  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 /////////////////////////////////////////////////////// PAGE //////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		@Property
		@Persist
		private List<Ballot> nonActiveBallots;
		@Property
		@Persist
		private List<Ballot> activeBallots;
		@Property
		@Persist
		private List<Ballot> endedBallots;
		@Property
		@Persist
		private List<Ballot> currentBallots;
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
		
		public boolean isShowRegistred()
		{
			if(datasession.isLoged())
				{return true;}
			return false;
		}
		
		public String getMetodo()
		{
			if(ballot.getMethod()==Method.MAYORIA_RELATIVA)
			{
				return "MAYORÍA RELATIVA";
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
			else if(ballot.getMethod()==Method.APPROVAL_VOTING)
			{
				return "APPROVAL VOTING";
			}
			else if(ballot.getMethod()==Method.BLACK)
			{
				return "BLACK";
			}
			else if(ballot.getMethod()==Method.BRAMS)
			{
				return "BRAMS";
			}
			else if(ballot.getMethod()==Method.BUCKLIN)
			{
				return "BUCKLIN";
			}
			else if(ballot.getMethod()==Method.CONDORCET)
			{
				return "CONDORCET";
			}
			else if(ballot.getMethod()==Method.COOMBS)
			{
				return "COOMBS";
			}
			else if(ballot.getMethod()==Method.COPELAND)
			{
				return "COPELAND";
			}
			else if(ballot.getMethod()==Method.DODGSON)
			{
				return "DODGSON";
			}
			else if(ballot.getMethod()==Method.HARE)
			{
				return "HARE";
			}
			else if(ballot.getMethod()==Method.JUICIO_MAYORITARIO)
			{
				return "JUICIO MAYORITARIO";
			}
			else if(ballot.getMethod()==Method.MAJORY)
			{
				return "MAJORY";
			}
			else if(ballot.getMethod()==Method.MEJOR_PEOR)
			{
				return "MEJOR PEOR";
			}
			else if(ballot.getMethod()==Method.NANSON)
			{
				return "NANSON";
			}
			else if(ballot.getMethod()==Method.SCHULZE)
			{
				return "SCHULZE";
			}
			else if(ballot.getMethod()==Method.SMALL)
			{
				return "SMALL";
			}
			else if(ballot.getMethod()==Method.VOTO_ACUMULATIVO)
			{
				return "VOTO ACUMULATIVO";
			}
			return null;
		}
		
		public String getStartDate()
		{
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return format.format(ballot.getStartDate());
		}
		public String getEndDate()
		{
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return format.format(ballot.getEndDate());
		}
		
		
		
		
		
		public boolean isShowNotStarted()
		{
			if(nonActiveBallots.size()==0)
				return false;
			return true;
		}
		public boolean isShowActive()
		{
			if(activeBallots.size()==0)
				return false;
			return true;
		}
		public boolean isShowEnded()
		{
			if(endedBallots.size()==0)
				return false;
			return true;
		}
		public boolean isShowCurrent()
		{
			if(currentBallots.size()==0 && publicCurrentBallots.size()==0)
				return false;
			return true;
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
		public Object onVoteBallot(String id)
		{
			contextBallotId=id;
			return VoteBallot.class;
		}
		public Object onVotePublicBallot(String id)
		{
			contextBallotId=id;
			return VoteBallot.class;
		}
		@SessionAttribute
		private String contextResultBallotId;
		public Object onResultBallot(String id)
		{
			contextResultBallotId=id;
			return ResultBallot.class;
		}
		public Object onResultPublicBallot(String id)
		{
			contextResultBallotId=id;
			return ResultBallot.class;
		}
		
		
		
}
