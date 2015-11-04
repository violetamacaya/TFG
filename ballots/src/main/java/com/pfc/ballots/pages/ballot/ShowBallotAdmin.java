package com.pfc.ballots.pages.ballot;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.ApprovalVotingDao;
import com.pfc.ballots.dao.BallotDao;
import com.pfc.ballots.dao.BlackDao;
import com.pfc.ballots.dao.BordaDao;
import com.pfc.ballots.dao.BramsDao;
import com.pfc.ballots.dao.BucklinDao;
import com.pfc.ballots.dao.CensusDao;
import com.pfc.ballots.dao.CondorcetDao;
import com.pfc.ballots.dao.CoombsDao;
import com.pfc.ballots.dao.CopelandDao;
import com.pfc.ballots.dao.DodgsonDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.HareDao;
import com.pfc.ballots.dao.JuicioMayoritarioDao;
import com.pfc.ballots.dao.KemenyDao;
import com.pfc.ballots.dao.MajoryDao;
import com.pfc.ballots.dao.MejorPeorDao;
import com.pfc.ballots.dao.NansonDao;
import com.pfc.ballots.dao.RangeVotingDao;
import com.pfc.ballots.dao.RelativeMajorityDao;
import com.pfc.ballots.dao.SchulzeDao;
import com.pfc.ballots.dao.SmallDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.dao.VoteDao;
import com.pfc.ballots.dao.VotoAcumulativoDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.data.Method;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.entities.Census;
import com.pfc.ballots.entities.ballotdata.Kemeny;
import com.pfc.ballots.entities.ballotdata.RelativeMajority;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
/**
 * 
 * ShowBallotAdmin class is the controller for the ShowBallotAdmin page that
 * provides the administration of the ballots
 * 
 * @author Mario Temprano Martin
 * @version 2.0 JUL-2014
 */
public class ShowBallotAdmin {
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
	@Persist
	RelativeMajorityDao relMayDao;
	@Persist
	KemenyDao kemenyDao;
	@Persist
	BordaDao bordaDao;
	@Persist
	RangeVotingDao rangeDao;
	@Persist
	ApprovalVotingDao approvalVotingDao;
	@Persist
	BlackDao blackDao;
	@Persist
	BramsDao bramsDao;
	@Persist
	BucklinDao bucklinDao;
	@Persist
	CondorcetDao condorcetDao;
	@Persist
	CoombsDao coombsDao;
	@Persist
	CopelandDao copelandDao;
	@Persist
	DodgsonDao dodgsonDao;
	@Persist
	HareDao hareDao;
	@Persist
	JuicioMayoritarioDao juicioMayoritarioDao;
	@Persist
	MajoryDao majoryDao;
	@Persist
	MejorPeorDao mejorpeorDao;
	@Persist
	NansonDao nansonDao;
	@Persist
	SchulzeDao schulzeDao;
	@Persist
	SmallDao smallDao;
	@Persist
	VotoAcumulativoDao votoAcumulativoDao;	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////// INITIALIZE ///////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Initialize data
	 * (check if the ballots are ended and non counted and calculate the results)
	 */
	public void setupRender()
	{
		showAreuSure=false;
		showGrid=true;
		ballotDao=DB4O.getBallotDao(datasession.getDBName());
		voteDao=DB4O.getVoteDao(datasession.getDBName());
		userDao=DB4O.getUsuarioDao(datasession.getDBName());
		censusDao=DB4O.getCensusDao(datasession.getDBName());
		ballots=ballotDao.retrieveAll();
		kemenyDao=DB4O.getKemenyDao(datasession.getDBName());
		relMayDao=DB4O.getRelativeMajorityDao(datasession.getDBName());
		bordaDao=DB4O.getBordaDao(datasession.getDBName());
		rangeDao=DB4O.getRangeVotingDao(datasession.getDBName());
		approvalVotingDao=DB4O.getApprovalVotingDao(datasession.getDBName());
		blackDao=DB4O.getBlackDao(datasession.getDBName());
		bramsDao=DB4O.getBramsDao(datasession.getDBName());
		bucklinDao=DB4O.getBucklinDao(datasession.getDBName());
		condorcetDao=DB4O.getCondorcetDao(datasession.getDBName());
		coombsDao=DB4O.getCoombsDao(datasession.getDBName());
		copelandDao=DB4O.getCopelandDao(datasession.getDBName());
		dodgsonDao=DB4O.getDodgsonDao(datasession.getDBName());
		hareDao=DB4O.getHareDao(datasession.getDBName());
		juicioMayoritarioDao=DB4O.getJuicioMayoritarioDao(datasession.getDBName());
		majoryDao=DB4O.getMajoryDao(datasession.getDBName());
		mejorpeorDao=DB4O.getMejorPeorDao(datasession.getDBName());
		nansonDao=DB4O.getNansonDao(datasession.getDBName());
		schulzeDao=DB4O.getSchulzeDao(datasession.getDBName());
		smallDao=DB4O.getSmallDao(datasession.getDBName());
		votoAcumulativoDao=DB4O.getVotoAcumulativoDao(datasession.getDBName());
	}
	
	
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////// GRID ZONE ///////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectPage
	private AddUsers addUsers;
	
	
	
	@InjectComponent
	private Zone gridZone;
	
	@Property
	@Persist
	private boolean showGrid;
	
	@Persist
	@Property
	private List<Ballot> ballots;
	
	@Property
	private Ballot ballot;
	

	public String getUserName()
	{
		return userDao.getEmailById(ballot.getIdOwner());
	}

	
	public String getCensusName()
	{
		Census temp=censusDao.getById(ballot.getIdCensus());
		if(temp==null)
		{
			return "DOCENTE";
		}
		else
		{
			return temp.getCensusName();
		}
			
	}
	/**
	 * Delete a ballot 
	 * @param idBallot
	 * @return
	 */
	public Object onActionFromDeleteBallot(String idBallot)
	{
		if(request.isXHR())
		{
			showGrid=false;
			showAreuSure=true;
			optionDelete=true;
			ballotSure=ballotDao.getById(idBallot);
			ajaxResponseRenderer.addRender("gridZone",gridZone ).addRender("areuSureZone",areuSureZone);
			return null;
		}
		else
		{
			return Index.class;
		}
		
		
	}
	
	
	public boolean isShowAdd()
	{
		if(ballot.isEnded())
			return false;
		if(ballot.isPublica())
			return false;
		return true;
	}
	/**
	 * Redirects to a page for add users
	 * @param idBallot
	 * @return 
	 */
	public Object onActionFromAddUsersBut(String idBallot)
	{
		addUsers.setup(idBallot,ShowBallotAdmin.class);
		return addUsers;
	}
	/**
	 * Finish a ballot and calculate the result
	 * @param idBallot
	 * @return
	 */
	public Object onActionFromFinishBallot(String idBallot)
	{
		if(request.isXHR())
		{
			showGrid=false;
			showAreuSure=true;
			optionDelete=false;
			ballotSure=ballotDao.getById(idBallot);
			ajaxResponseRenderer.addRender("gridZone",gridZone ).addRender("areuSureZone",areuSureZone);
			return null;
		}
		else
		{
			return Index.class;
		}
	}
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 //////////////////////////////////////////////////// AREUSURE ZONE //////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@InjectComponent
	private Zone areuSureZone;
	@Persist
	@Property
	private Ballot ballotSure;
	
	@Property
	@Persist
	private boolean showAreuSure;
	@Property
	@Persist
	private boolean optionDelete;
	
	public String getUserSure()
	{
		return userDao.getEmailById(ballotSure.getIdOwner());
	}
	
	/**
	 * Controls the actions to take when is sure to delete/finish 
	 * a ballot in the dialog "is sure delete/finish a ballot"
	 */
	public void onActionFromIsSure()
	{
		if(request.isXHR())
		{
			if(optionDelete==true)
			{
				ballotDao.deleteBallotById(ballotSure.getId());
				voteDao.deleteVoteOfBallot(ballotSure.getId());
				if(ballotSure.getMethod()==Method.MAYORIA_RELATIVA)
				{
					relMayDao.deleteByBallotId(ballotSure.getId());
					//relMays=relMayDao.retrieveAll();
				}
				if(ballotSure.getMethod()==Method.KEMENY)
				{
					kemenyDao.deleteByBallotId(ballotSure.getId());
					//kemenys=kemenyDao.retrieveAll();
				}
				if(ballotSure.getMethod()==Method.BORDA)
				{
					bordaDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.RANGE_VOTING)
				{
					rangeDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.APPROVAL_VOTING)
				{
					approvalVotingDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.BLACK)
				{
					blackDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.BRAMS)
				{
					bramsDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.BUCKLIN)
				{
					bucklinDao.deleteByBallotId(ballotSure.getId());
				}		
				
				if(ballotSure.getMethod()==Method.CONDORCET)
				{
					condorcetDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.COOMBS)
				{
					coombsDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.COPELAND)
				{
					copelandDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.DODGSON)
				{
					dodgsonDao.deleteByBallotId(ballotSure.getId());
				}		
				if(ballotSure.getMethod()==Method.HARE)
				{
					hareDao.deleteByBallotId(ballotSure.getId());
					//relMays=relMayDao.retrieveAll();
				}
				if(ballotSure.getMethod()==Method.JUICIO_MAYORITARIO)
				{
					juicioMayoritarioDao.deleteByBallotId(ballotSure.getId());
					//kemenys=kemenyDao.retrieveAll();
				}
				if(ballotSure.getMethod()==Method.MEJOR_PEOR)
				{
					mejorpeorDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.NANSON)
				{
					nansonDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.MAJORY)
				{
					majoryDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.SCHULZE)
				{
					schulzeDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.SMALL)
				{
					smallDao.deleteByBallotId(ballotSure.getId());
				}
				if(ballotSure.getMethod()==Method.VOTO_ACUMULATIVO)
				{
					votoAcumulativoDao.deleteByBallotId(ballotSure.getId());
				}
				ballots=ballotDao.retrieveAll();
			}
			else
			{
				ballotSure.setEnded(true);		
				ballotSure.setActive(false);
				ballotDao.updateBallot(ballotSure);
				ballots=ballotDao.retrieveAll();
		
			}
				
			
			
			showGrid=true;
			showAreuSure=false;
			ajaxResponseRenderer.addRender("gridZone",gridZone ).addRender("areuSureZone",areuSureZone);
		}
		
	}
	/**
	 * Cancel the dialog "is sure delete/finish a ballot"
	 */
	public void onActionFromNotSure()
	{
		if(request.isXHR())
		{
			showGrid=true;
			showAreuSure=false;
			ajaxResponseRenderer.addRender("gridZone",gridZone ).addRender("areuSureZone",areuSureZone);			
		}
	}
	
	  ////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////// ON ACTIVATE //////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////
	/**
	* Controls if the user can enter in the page
	* @return another page if the user can't enter
	*/
	public Object onActivate()
	{
		return Index.class;
		/*switch(datasession.sessionState())
		{
			case 0:
				return Index.class;
			case 1:
				return UnauthorizedAttempt.class;
			case 2:
				return null;
			case 3:
				return SessionExpired.class;
			default:
				return Index.class;
		}*/
		
	}
}
