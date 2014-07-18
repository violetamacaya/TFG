package com.pfc.ballots.pages.ballot;

import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.services.StringValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.BallotDao;
import com.pfc.ballots.dao.CensusDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.KemenyDao;
import com.pfc.ballots.dao.RelativeMajorityDao;
import com.pfc.ballots.dao.VoteDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.data.Method;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.entities.Vote;
import com.pfc.ballots.entities.ballotdata.Kemeny;
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
	@Persist
	KemenyDao kemenyDao;
	
	
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
		if(ballot.getMethod()==Method.KEMENY)
		{
			kemenyDao=DB4O.getKemenyDao(datasession.getDBName());
			kemeny=kemenyDao.getByBallotId(contextBallotId);
			kemenyVote=new LinkedList<String>();
			showErrorKemeny=false;
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
		ballot=ballotDao.getById(contextBallotId);
		
		if(ballot!=null||!ballot.isEnded()||!vote.isCounted())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
		{
			vote.setCounted(true);
			System.out.println("SI");
			voteDao.updateVote(vote);
			relMay.addVote(relMayVote);
			relativeMajorityDao.update(relMay);
		}
		
		return Index.class;
	}
	public boolean isShowRelativeMajority()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.MAYORIA_RELATIVA)
		{
			return true;
		}
		return false;
			
	}
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 //////////////////////////////////////////////////////// KEMENY /////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@InjectComponent
	private Zone kemenyZone;
	
	
	@Property
	@Persist
	private Kemeny kemeny;
	
	@Property
	@Persist
	private boolean showErrorKemeny;
	
	@Property
	private final StringValueEncoder stringValueEncoder = new StringValueEncoder();
	
	@Property
	@Persist
	private List<String> kemenyVote;
	
	public Object onSuccessFromKemenyForm()
	{
		showErrorKemeny=false;
		if(kemenyVote.size()<4)
		{
			showErrorKemeny=true;
			ajaxResponseRenderer.addRender("kemenyZone", kemenyZone);
			return null;
		}
		for(String temp:kemenyVote)
		{
			System.out.println(temp);
		}
		
		vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
		ballot=ballotDao.getById(contextBallotId);
		
		if(ballot!=null||!ballot.isEnded()||!vote.isCounted())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
		{
			vote.setCounted(true);
			voteDao.updateVote(vote);
			
			kemeny.addVote(kemenyVote);
			kemenyDao.update(kemeny);
		}
		
		return Index.class;//SUSTITUIR POR INDEX
	}
	
	public boolean isShowKemeny()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.KEMENY)
		{
			return true;
		}
		return false;
			
	}
}
