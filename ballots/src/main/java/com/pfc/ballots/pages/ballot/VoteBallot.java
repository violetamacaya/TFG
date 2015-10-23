package com.pfc.ballots.pages.ballot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.services.StringValueEncoder;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;

import com.pfc.ballots.dao.ApprovalVotingDao;
import com.pfc.ballots.dao.BallotDao;
import com.pfc.ballots.dao.BordaDao;
import com.pfc.ballots.dao.BramsDao;
import com.pfc.ballots.dao.CensusDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.KemenyDao;
import com.pfc.ballots.dao.RangeVotingDao;
import com.pfc.ballots.dao.RelativeMajorityDao;
import com.pfc.ballots.dao.VoteDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.data.Method;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.entities.Vote;
import com.pfc.ballots.entities.ballotdata.ApprovalVoting;
import com.pfc.ballots.entities.ballotdata.Borda;
import com.pfc.ballots.entities.ballotdata.Brams;
import com.pfc.ballots.entities.ballotdata.Kemeny;
import com.pfc.ballots.entities.ballotdata.RangeVoting;
import com.pfc.ballots.entities.ballotdata.RelativeMajority;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
/**
 * 
 * VoteBallot class is the controller for the VoteBallot page that
 * allow to vote in a ballot
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUL-2014
 * @author Violeta Macaya Sánchez
 * @version 2.0 OCT-2015
 */
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

	@SessionAttribute
	private String contextResultBallotId;

	@SessionAttribute
	private Map<String,List<String>>publicVotes;


	@Property
	private final StringValueEncoder stringValueEncoder = new StringValueEncoder();

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
	@Persist
	BordaDao bordaDao;
	@Persist
	RangeVotingDao rangeDao;
	@Persist
	ApprovalVotingDao approvalDao;
	@Persist
	BramsDao bramsDao;	

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// INITIALIZE ///////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Initialize data
	 */
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();
		ballotDao=DB4O.getBallotDao(datasession.getDBName());
		ballot=ballotDao.getById(contextBallotId);

		voteDao=DB4O.getVoteDao(datasession.getDBName());
		vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());

		if(ballot.isPublica())
		{

			if(publicVotes==null)
			{
				publicVotes=new HashMap<String,List<String>>();
			}
		}
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
		if(ballot.getMethod()==Method.BORDA)
		{
			bordaDao=DB4O.getBordaDao(datasession.getDBName());
			borda=bordaDao.getByBallotId(contextBallotId);
			bordaVote=new LinkedList<String>();
			showErrorBorda=false;			
		}
		if(ballot.getMethod()==Method.RANGE_VOTING)
		{
			rangeDao=DB4O.getRangeVotingDao(datasession.getDBName());
			range=rangeDao.getByBallotId(contextBallotId);

			range0=String.valueOf(range.getMinValue());
			range1=String.valueOf(range.getMinValue());
			range2=String.valueOf(range.getMinValue());
			range3=String.valueOf(range.getMinValue());
			range4=String.valueOf(range.getMinValue());
			range5=String.valueOf(range.getMinValue());
			range6=String.valueOf(range.getMinValue());		
		}
		if(ballot.getMethod()==Method.APPROVAL_VOTING)
		{
			approvalDao=DB4O.getApprovalVotingDao(datasession.getDBName());
			approvalVoting=approvalDao.getByBallotId(contextBallotId);
			approvalVotingVote=approvalVoting.getOptions().get(0);
		}
		if(ballot.getMethod()==Method.BRAMS)
		{
			bramsDao=DB4O.getBramsDao(datasession.getDBName());
			brams=bramsDao.getByBallotId(contextBallotId);
			bramsVote=brams.getOptions().get(0);
			showErrorBrams=false;
		}	
	}


	public boolean isVoteCounted()
	{
		if(ballot.isPublica())
		{
			return alreadyVote();
		}
		else
			return vote.isCounted();
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


	/**
	 * Stores the majority relative vote
	 * @return
	 */
	public Object onSuccessFromRelativeMajorityForm()
	{

		if(!ballot.isPublica())
		{
			vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
			ballot=ballotDao.getById(contextBallotId);

			if(ballot!=null && !ballot.isEnded() && !vote.isCounted())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
			{
				vote.setCounted(true);
				System.out.println("SI");
				voteDao.updateVote(vote);
				relMay.addVote(relMayVote);
				relativeMajorityDao.update(relMay);
			}
		}
		else
		{
			ballot=ballotDao.getById(contextBallotId);
			if(ballot!=null && !ballot.isEnded() && !alreadyVote())
			{
				relMay.addVote(relMayVote);

				relativeMajorityDao.update(relMay);
				addPublicVote();
			}
			else
			{
				return null;
			}
		}

		contextResultBallotId=contextBallotId;
		return VoteCounted.class;
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
	@Persist
	private List<String> kemenyVote;

	/**
	 * Stores the kemeny vote
	 * @return
	 */
	public Object onSuccessFromKemenyForm()
	{
		if(request.isXHR())
		{
			showErrorKemeny=false;
			if(kemenyVote.size()<4)
			{
				showErrorKemeny=true;
				ajaxResponseRenderer.addRender("kemenyZone", kemenyZone);
			}

			if(ballot.isPublica())
			{
				ballot=ballotDao.getById(contextBallotId);
				if(ballot!=null && !ballot.isEnded() && !alreadyVote())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
				{					
					kemeny.addVote(kemenyVote);
					kemenyDao.update(kemeny);
					addPublicVote();
				}
			}
			else
			{
				vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
				ballot=ballotDao.getById(contextBallotId);

				if(ballot!=null && !ballot.isEnded() && !vote.isCounted())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
				{
					vote.setCounted(true);
					voteDao.updateVote(vote);

					kemeny.addVote(kemenyVote);
					kemenyDao.update(kemeny);
				}
			}
		}

		contextResultBallotId=contextBallotId;
		return VoteCounted.class;
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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////// BORDA /////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectComponent
	private Zone bordaZone;

	@Property
	@Persist
	private Borda borda;

	@Property
	@Persist
	private boolean showErrorBorda;

	@Persist
	@Property
	private List<String> bordaVote;


	public Object onSuccessFromBordaForm()
	{
		if(request.isXHR())
		{
			showErrorBorda=false;
			if(bordaVote.size()<borda.getBordaOptions().size())
			{
				showErrorBorda=true;
				ajaxResponseRenderer.addRender("bordaZone", bordaZone);
			}
			if(ballot.isPublica())
			{
				ballot=ballotDao.getById(contextBallotId);
				if(ballot!=null && !ballot.isEnded()&& !alreadyVote())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
				{
					borda.addVote(bordaVote);
					bordaDao.update(borda);
					addPublicVote();
				}
			}
			else
			{
				vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
				ballot=ballotDao.getById(contextBallotId);

				if(ballot!=null && !ballot.isEnded() && !vote.isCounted())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
				{
					vote.setCounted(true);
					voteDao.updateVote(vote);
					borda.addVote(bordaVote);
					bordaDao.update(borda);
				}
			}
		}

		contextResultBallotId=contextBallotId;
		return VoteCounted.class;
	}
	public boolean isShowBorda()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.BORDA)
		{
			return true;
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// RANGE VOTING /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectComponent
	private Zone rangeZone;


	@Persist
	@Property
	private RangeVoting range;


	@Persist
	@Property
	private String range0;
	@Persist
	@Property
	private String range1;
	@Persist
	@Property
	private String range2;
	@Persist
	@Property
	private String range3;
	@Persist
	@Property
	private String range4;
	@Persist
	@Property
	private String range5;
	@Persist
	@Property
	private String range6;

	@Persist
	@Property
	private boolean showRangeBadNumber;

	public boolean isShowRange()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.RANGE_VOTING)
		{
			return true;
		}
		return false;
	}

	public String getRangeOpt0()
	{
		return range.getOptions().get(0);
	}
	public String getRangeOpt1()
	{
		return range.getOptions().get(1);
	}
	public String getRangeOpt2()
	{
		return range.getOptions().get(2);
	}
	public String getRangeOpt3()
	{
		return range.getOptions().get(3);
	}
	public String getRangeOpt4()
	{
		return range.getOptions().get(4);
	}
	public String getRangeOpt5()
	{
		return range.getOptions().get(5);
	}
	public String getRangeOpt6()
	{
		return range.getOptions().get(6);
	}
	public boolean isShowRange2()
	{
		if(range.getOptions().size()>=3)
			return true;
		else
			return false;
	}
	public boolean isShowRange3()
	{
		if(range.getOptions().size()>=4)
			return true;
		else
			return false;
	}
	public boolean isShowRange4()
	{
		if(range.getOptions().size()>=5)
			return true;
		else
			return false;
	}
	public boolean isShowRange5()
	{
		if(range.getOptions().size()>=6)
			return true;
		else
			return false;
	}
	public boolean isShowRange6()
	{
		if(range.getOptions().size()>=7)
			return true;
		else
			return false;
	}

	public void onValidateFromRangeForm()
	{
		showRangeBadNumber=false;
		switch(range.getOptions().size())
		{
		case 7:
			if(range6==null){range6="0";}
			if(!isNumeric(range6)){showRangeBadNumber=true;}
			else if(Integer.parseInt(range6)<range.getMinValue() || Integer.parseInt(range6)>range.getMaxValue())
			{showRangeBadNumber=true;}

		case 6:
			if(range5==null){range5="0";}
			if(!isNumeric(range5)){showRangeBadNumber=true;}
			else if(Integer.parseInt(range5)<range.getMinValue() || Integer.parseInt(range5)>range.getMaxValue())
			{showRangeBadNumber=true;}

		case 5:
			if(range4==null){range4="0";}
			if(!isNumeric(range4)){showRangeBadNumber=true;}
			else if(Integer.parseInt(range4)<range.getMinValue() || Integer.parseInt(range4)>range.getMaxValue())
			{showRangeBadNumber=true;}

		case 4:
			if(range3==null){range3="0";}
			if(!isNumeric(range3)){showRangeBadNumber=true;}
			else if(Integer.parseInt(range3)<range.getMinValue() || Integer.parseInt(range3)>range.getMaxValue())
			{showRangeBadNumber=true;}

		case 3:
			if(range2==null){range2="0";}
			if(!isNumeric(range2)){showRangeBadNumber=true;}
			else if(Integer.parseInt(range2)<range.getMinValue() || Integer.parseInt(range2)>range.getMaxValue())
			{showRangeBadNumber=true;}

		case 2:
			if(range1==null){range1="0";}
			if(!isNumeric(range1)){showRangeBadNumber=true;}
			else if(Integer.parseInt(range1)<range.getMinValue() || Integer.parseInt(range1)>range.getMaxValue())
			{showRangeBadNumber=true;}
			if(range0==null){range0="0";}
			if(!isNumeric(range0)){showRangeBadNumber=true;}
			else if(Integer.parseInt(range6)<range.getMinValue() || Integer.parseInt(range6)>range.getMaxValue())
			{showRangeBadNumber=true;}
		}		
	}

	public Object onSuccessFromRangeForm()
	{
		if(request.isXHR())
		{
			if(showRangeBadNumber)
			{
				ajaxResponseRenderer.addRender("rangeZone",rangeZone);

			}
			List<String> voto=new LinkedList<String>();
			voto.add(range0);
			voto.add(range1);
			if(range.getOptions().size()>=3){voto.add(range2);}
			if(range.getOptions().size()>=4){voto.add(range3);}
			if(range.getOptions().size()>=5){voto.add(range4);}
			if(range.getOptions().size()>=6){voto.add(range5);}
			if(range.getOptions().size()>=7){voto.add(range6);}


			if(ballot.isPublica())
			{
				ballot=ballotDao.getById(contextBallotId);

				if(ballot!=null && !ballot.isEnded()&& !alreadyVote())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
				{
					range.addVote(voto);
					rangeDao.update(range);
					addPublicVote();
				}
			}
			else
			{
				vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
				ballot=ballotDao.getById(contextBallotId);

				if(ballot!=null && !ballot.isEnded() && !vote.isCounted())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
				{
					range.addVote(voto);
					vote.setCounted(true);
					voteDao.updateVote(vote);
					rangeDao.update(range);
				}
			}

		}
		contextResultBallotId=contextBallotId;
		return VoteCounted.class;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// APPROVAL VOTING /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



	@Persist
	@Property
	private ApprovalVoting approvalVoting;
	@Property
	private String approvalVotingVote;
	@Property
	private String approvalOption;


	/**
	 * Stores the Approval voting vote
	 * @return
	 */

	@Property  
	@Persist
	private List<String> selectedCheckList;  

	@Inject
	private Messages messages;

	public ValueEncoder<String> getStringEncoder() { 
		return new StringValueEncoder(); 
	}

	public List<String> getModel() {
		return approvalVoting.getOptions(); 
	}
	@Property
	private final StringValueEncoder encoder = new StringValueEncoder();

	@Property
	@Persist
	private List<String> selectedCheckListApproval;


	public Object onSuccessFromApprovalVotingForm()
	{
		if(!ballot.isPublica())
		{
			vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
			ballot=ballotDao.getById(contextBallotId);

			if(ballot!=null && !ballot.isEnded() && !vote.isCounted())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
			{
				vote.setCounted(true);
				voteDao.updateVote(vote);
				for(String option:selectedCheckListApproval){
					approvalVoting.addVote(option);
				}
				approvalDao.update(approvalVoting);
			}
		}
		else
		{
			ballot=ballotDao.getById(contextBallotId);
			if(ballot!=null && !ballot.isEnded() && !alreadyVote())
			{
				for(String option:selectedCheckListApproval){
					approvalVoting.addVote(option);
				}
				approvalDao.update(approvalVoting);
				addPublicVote();
			}
			else
			{
				return null;
			}
		}

		contextResultBallotId=contextBallotId;
		return VoteCounted.class;
	}
	public boolean isShowApprovalVoting()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.APPROVAL_VOTING)
		{
			return true;
		}
		return false;

	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// BRAMS /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



	@Persist
	@Property
	private Brams brams;
	@Property
	private String bramsVote;
	@Property
	private String bramsOption;
	@InjectComponent
	private Zone bramsZone;



	@Property
	@Persist
	private boolean showErrorBrams;

	/**
	 * Stores the Brams vote
	 * @return
	 */



	public ValueEncoder<String> getStringEncoderBrams() { 
		return new StringValueEncoder(); 
	}

	public List<String> getModelBrams() {
		return brams.getOptions(); 
	}
	@Property
	private final StringValueEncoder encoderBrams = new StringValueEncoder();

	@Property
	@Persist
	private List<String> selectedCheckListBrams;


	public Object onSuccessFromBramsForm()
	{
		System.out.println("La otra mierda de opciones: "+ brams.getOptions().size());

		if(request.isXHR())
		{
			showErrorBrams = false;
			System.out.println("Selected MIAU: "+ selectedCheckListBrams.size()+" La otra mierda de opciones: "+ brams.getOptions().size());
			if (selectedCheckListBrams.size() > (brams.getOptions().size() - 3)){
				//Ha seleccionado más opciones de las que debería
				showErrorBrams = true;
				ajaxResponseRenderer.addRender("bramsZone", bramsZone);

			}
			
				if(!ballot.isPublica())
				{
					vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
					ballot=ballotDao.getById(contextBallotId);

					if(ballot!=null && !ballot.isEnded() && !vote.isCounted())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
					{
						vote.setCounted(true);
						voteDao.updateVote(vote);
						for(String option:selectedCheckListBrams){
							brams.addVote(option);
						}
						bramsDao.update(brams);
					}
				}
				else
				{
					ballot=ballotDao.getById(contextBallotId);
					if(ballot!=null && !ballot.isEnded() && !alreadyVote())
					{
						for(String option:selectedCheckListBrams){
							brams.addVote(option);
						}
						bramsDao.update(brams);
						addPublicVote();
					}
					else
					{
						return null;
					}
				}
			
		}
		contextResultBallotId=contextBallotId;
		//return VoteCounted.class;
		return null;
	}
	public boolean isShowBrams()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.BRAMS)
		{
			return true;
		}
		return false;

	}		
	/////////////////////////////////////////////////// TOOLS //////////////////////////

	private void addPublicVote()
	{
		List<String> list=publicVotes.get(datasession.getIdSession());
		if(list==null)
		{
			list=new LinkedList<String>();
		}
		list.add(contextBallotId);
		publicVotes.put(datasession.getIdSession(), list);
	}
	private boolean alreadyVote()
	{
		if(publicVotes==null)
			return false;
		List<String> list=publicVotes.get(datasession.getIdSession());
		if(list==null)
		{
			return false;
		}
		else
		{
			for(String current:list)
			{
				if(current.equals(contextBallotId))
				{
					return true;
				}
			}
			return false;
		}
	}

	private boolean isNumeric(String cadena)
	{
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
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
		switch(datasession.sessionState())
		{
		case 0:
		case 1:
		case 2:
			if(contextBallotId==null)
				return Index.class;
			else
			{
				return null;
			}
		case 3:
			return SessionExpired.class;
		default:
			return Index.class;
		}

	}
}
