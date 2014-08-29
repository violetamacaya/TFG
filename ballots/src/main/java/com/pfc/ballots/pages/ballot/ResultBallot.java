package com.pfc.ballots.pages.ballot;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.pfc.ballots.dao.BallotDao;
import com.pfc.ballots.dao.CensusDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.KemenyDao;
import com.pfc.ballots.dao.RelativeMajorityDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.dao.VoteDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.data.Method;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.entities.ballotdata.Kemeny;
import com.pfc.ballots.entities.ballotdata.RelativeMajority;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;


@Import(library = "context:js/charts.js")
public class ResultBallot {

	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 //////////////////////////////////////////////////// GENERAL STUFF //////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SessionState
	private DataSession datasession;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@SessionAttribute
	private String contextResultBallotId;
	
	@Inject
	private Request request;
	
	@Persist
	@Property
	private Ballot ballot;
	
	@Property 
	private boolean noBallot;
	
	@Inject
    private JavaScriptSupport javaScriptSupport;
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////////// DAO //////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	@Persist
	CensusDao censusDao;
	@Persist
	UserDao userDao;
	@Persist
	BallotDao ballotDao;
	@Persist
	VoteDao voteDao;
	RelativeMajorityDao relMayDao;
	KemenyDao kemenyDao;
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////// INITIALIZE ///////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Initialize values
	 */
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();
		
		System.out.println("SET UP");
		ballotDao=DB4O.getBallotDao(datasession.getDBName());
		ballot=ballotDao.getById(contextResultBallotId);
		if(ballot==null)
		{
			noBallot=true;
		}
		else if(ballot.getMethod()==Method.MAYORIA_RELATIVA)
		{
			relMayDao= DB4O.getRelativeMajorityDao(datasession.getDBName());
			relMay=relMayDao.getByBallotId(ballot.getId());
			
			if(ballot.isEnded()==true && ballot.isCounted()==false && relMay!=null)
			{
				relMay.calcularMayoriaRelativa();
				ballot.setCounted(true);
				relMayDao.update(relMay);
				ballotDao.updateBallot(ballot);
			}
		}
		else if(ballot.getMethod()==Method.KEMENY)
		{
			kemenyDao=DB4O.getKemenyDao(datasession.getDBName());
			kemeny=kemenyDao.getByBallotId(contextResultBallotId);
			if(ballot.isEnded()==true && ballot.isCounted()==false && kemeny!=null)
			{
				kemeny.calcularKemeny();
				ballot.setCounted(true);
				kemenyDao.update(kemeny);
				ballotDao.updateBallot(ballot);
			}
			
		}
		
		
		
		
	}
	/**
	 * JavaScript controller
	 */
	public void afterRender()
	{
		System.out.println("AFTER");
		JSONArray array=new JSONArray();
	
		JSONObject obj=new JSONObject();
		obj.put("title", ballot.getName());
		array.put(obj);
		
	
		if(ballot.getMethod()==Method.MAYORIA_RELATIVA)
		{
			
			for(String option:relMay.getOptions())
			{
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", relMay.getResultOption(option));
				array.put(obj);
			}
			
			System.out.println("tamaño->"+array.toString());
			javaScriptSupport.addInitializerCall("charts_may",array.toString());
		}
		if(ballot.getMethod()==Method.KEMENY)
		{
			for(String permutation:kemeny.getPermutations())
			{
				obj=new JSONObject();
				obj.put("option", permutation);
				obj.put("value", kemeny.getResult(permutation));
				array.put(obj);
			}
			javaScriptSupport.addInitializerCall("charts_kem",array.toString());
		}
		
	}
	
	  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////// MAYORIA RELATIVA //////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Relative Majority Data
	 */
	@Persist
	@Property
	RelativeMajority relMay;
	
	@Property
	private String option;
	
	public String getRelMayVote()
	{
		return String.valueOf(relMay.getResultOption(option));
	}
	public boolean getShowRelMay()
	{
		if(ballot!=null && ballot.getMethod()==Method.MAYORIA_RELATIVA)
			{return true;}
		return false;
	}

	  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////// MAYORIA RELATIVA //////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Kemeney Data
	 */
	@Property
	@Persist
	Kemeny kemeny;
	
	@Property
	private String permutation;
	public boolean getShowKemeny()
	{
		if(ballot!=null && ballot.getMethod()==Method.KEMENY)
			{return true;}
		return false;
	}
	public String getKemenyVote()
	{
		return String.valueOf(kemeny.getResult(permutation));
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
		if(contextResultBallotId==null)
		{
			return Index.class;
		}
		switch(datasession.sessionState())
		{
			case 0:
				return Index.class;
			case 1:
				return null;
			case 2:
				return null;
			case 3:
				return SessionExpired.class;
			default:
				return Index.class;
		}
		
	}
	
}
