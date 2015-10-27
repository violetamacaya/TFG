package com.pfc.ballots.pages.ballot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
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
import com.pfc.ballots.dao.VotoAcumulativoDao;
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
import com.pfc.ballots.entities.ballotdata.VotoAcumulativo;
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
	@Persist
	VotoAcumulativoDao votoAcumulativoDao;	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// INITIALIZE ///////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Initialize data
	 */
	public void setupRender()
	{
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
			bramsVote=new LinkedList<String>();
		}	
		if(ballot.getMethod()==Method.VOTO_ACUMULATIVO)
		{
			votoAcumulativoDao=DB4O.getVotoAcumulativoDao(datasession.getDBName());
			votoAcumulativo=votoAcumulativoDao.getByBallotId(contextBallotId);
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
		componentResources.discardPersistentFieldChanges();
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

	/**
	 * Stores the Brams vote
	 * @return
	 */

	@InjectComponent
	private Zone bramsZone;

	@Persist
	@Property
	private Brams brams;

	@Persist
	@Property
	private List<String> bramsVote;

	@Property
	private String bramsOption;

	@Property
	@Persist
	private boolean showErrorBrams;


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
		if(request.isXHR())
		{
			System.out.println("Selected : "+ selectedCheckListBrams.size()+" Opciones: "+ brams.getOptions().size());
			if (selectedCheckListBrams.size() > (brams.getOptions().size() - 3)){
				System.out.println("Dentro del if de render  "+showErrorBrams);
				showErrorBrams = true;
				ajaxResponseRenderer.addRender("bramsZone",bramsZone);


			}
			else {
				if(ballot.isPublica())
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
				}
				else
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

				contextResultBallotId=contextBallotId;
				componentResources.discardPersistentFieldChanges();
				return VoteCounted.class;
			}

		}
		return this;
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
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// Voto acumulativo /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Stores the VotoAcumulativo vote
	 * @return
	 */


	@Persist
	@Property
	private VotoAcumulativo votoAcumulativo;

	@Property
	@Persist
	private boolean showErrorVotoAcumulativo;

	@Property
	@Persist
	private String votoAcumulativoRadio0;

	@Property
	@Persist
	private String votoAcumulativoRadio1;

	@Property
	@Persist
	private String votoAcumulativoRadio2;	

	@Property
	@Persist
	private String votoAcumulativoRadio3;

	@Property
	@Persist
	private String votoAcumulativoRadio4;

	@Property
	@Persist
	private String votoAcumulativoRadio5;	

	@Property
	@Persist
	private String votoAcumulativoRadio6;

	@Property
	@Persist
	private String votoAcumulativoRadio7;

	@Property
	@Persist
	private String votoAcumulativoRadio8;	

	@Property
	@Persist
	private String votoAcumulativoRadio9;

	@Property
	@Persist
	private String votoAcumulativoRadio10;

	@Property
	@Persist
	private String votoAcumulativoRadio11;	

	@Property
	@Persist
	private String votoAcumulativoRadio12;

	@Property
	@Persist
	private String votoAcumulativoRadio13;

	@Property
	@Persist
	private String votoAcumulativoRadio14;	

	@Property
	@Persist
	private String votoAcumulativoRadio15;	


	public boolean isShowVotoAcumulativo3()
	{
		if(votoAcumulativo.getOptions().size()>=4)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo4()
	{
		if(votoAcumulativo.getOptions().size()>=5)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo5()
	{
		if(votoAcumulativo.getOptions().size()>=6)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo6()
	{
		if(votoAcumulativo.getOptions().size()>=7)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo7()
	{
		if(votoAcumulativo.getOptions().size()>=6)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo8()
	{
		if(votoAcumulativo.getOptions().size()>=7)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo9()
	{
		if(votoAcumulativo.getOptions().size()>=8)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo10()
	{
		if(votoAcumulativo.getOptions().size()>=9)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo11()
	{
		if(votoAcumulativo.getOptions().size()>=10)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo12()
	{
		if(votoAcumulativo.getOptions().size()>=11)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo13()
	{
		if(votoAcumulativo.getOptions().size()>=12)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo14()
	{
		if(votoAcumulativo.getOptions().size()>=13)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo15()
	{
		if(votoAcumulativo.getOptions().size()>=14)
			return true;
		else
			return false;
	}


	public String getVotoAcumulativoOption0()
	{
		votoAcumulativoRadio0 = "0";
		return votoAcumulativo.getOptions().get(0);
	}
	public String getVotoAcumulativoOption1()
	{
		votoAcumulativoRadio1 = "0";
		return votoAcumulativo.getOptions().get(1);
	}
	public String getVotoAcumulativoOption2()
	{
		votoAcumulativoRadio2 = "0";
		return votoAcumulativo.getOptions().get(2);
	}
	public String getVotoAcumulativoOption3()
	{
		votoAcumulativoRadio3 = "0";
		return votoAcumulativo.getOptions().get(3);
	}
	public String getVotoAcumulativoOption4()
	{
		votoAcumulativoRadio4 = "0";
		return votoAcumulativo.getOptions().get(4);
	}
	public String getVotoAcumulativoOption5()
	{
		votoAcumulativoRadio5 = "0";
		return votoAcumulativo.getOptions().get(5);
	}
	public String getVotoAcumulativoOption6()
	{
		votoAcumulativoRadio6 = "0";
		return votoAcumulativo.getOptions().get(6);
	}
	public String getVotoAcumulativoOption7()
	{
		votoAcumulativoRadio7 = "0";
		return votoAcumulativo.getOptions().get(7);
	}
	public String getVotoAcumulativoOption8()
	{
		votoAcumulativoRadio8 = "0";
		return votoAcumulativo.getOptions().get(8);
	}
	public String getVotoAcumulativoOption9()
	{
		votoAcumulativoRadio9 = "0";
		return votoAcumulativo.getOptions().get(9);
	}
	public String getVotoAcumulativoOption10()
	{
		votoAcumulativoRadio10 = "0";
		return votoAcumulativo.getOptions().get(10);
	}
	public String getVotoAcumulativoOption11()
	{
		votoAcumulativoRadio11 = "0";
		return votoAcumulativo.getOptions().get(11);
	}
	public String getVotoAcumulativoOption12()
	{
		votoAcumulativoRadio12 = "0";
		return votoAcumulativo.getOptions().get(12);
	}
	public String getVotoAcumulativoOption13()
	{
		votoAcumulativoRadio13 = "0";
		return votoAcumulativo.getOptions().get(13);
	}
	public String getVotoAcumulativoOption14()
	{
		votoAcumulativoRadio14 = "0";
		return votoAcumulativo.getOptions().get(14);
	}

	public Object onSuccessFromVotoAcumulativoForm()
	{
		if(request.isXHR())
		{
			System.out.println("radio 0: "+votoAcumulativoRadio0);
			System.out.println("radio 1: "+votoAcumulativoRadio1);
			System.out.println("radio 2: "+votoAcumulativoRadio2);
			System.out.println("radio 3: "+votoAcumulativoRadio3);
			System.out.println("radio 4: "+votoAcumulativoRadio4);
			System.out.println("radio 5: "+votoAcumulativoRadio5);
			System.out.println("radio 6: "+votoAcumulativoRadio6);
			System.out.println("radio 7: "+votoAcumulativoRadio7);
			System.out.println("radio 8: "+votoAcumulativoRadio8);
			System.out.println("radio 9: "+votoAcumulativoRadio9);
			System.out.println("radio 10: "+votoAcumulativoRadio10);
			System.out.println("radio 11: "+votoAcumulativoRadio11);
			System.out.println("radio 12: "+votoAcumulativoRadio12);
			System.out.println("radio 13: "+votoAcumulativoRadio13);
			System.out.println("radio 14: "+votoAcumulativoRadio14);
			System.out.println("votoAcumulativo tiene: "+votoAcumulativo.getOptions().size());
			if(votoAcumulativo.getOptions().size()==2){
				System.out.println("He entrado por el case 2 con valores: "+Integer.parseInt(votoAcumulativoRadio0)+" y "+Integer.parseInt(votoAcumulativoRadio1));
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}

			else if(votoAcumulativo.getOptions().size()==3){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1)+ Integer.parseInt(votoAcumulativoRadio2))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}
			else if(votoAcumulativo.getOptions().size()==4){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}
			else if(votoAcumulativo.getOptions().size()==5){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3)+ Integer.parseInt(votoAcumulativoRadio4))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}
			else if(votoAcumulativo.getOptions().size()==6){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3)+ Integer.parseInt(votoAcumulativoRadio4)+ Integer.parseInt(votoAcumulativoRadio5))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}		
			else if(votoAcumulativo.getOptions().size()==7){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3)+ Integer.parseInt(votoAcumulativoRadio4)+ Integer.parseInt(votoAcumulativoRadio5)+ Integer.parseInt(votoAcumulativoRadio6))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}
			else if(votoAcumulativo.getOptions().size()==8){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3)+ Integer.parseInt(votoAcumulativoRadio4)+ Integer.parseInt(votoAcumulativoRadio5)+ Integer.parseInt(votoAcumulativoRadio6)+ Integer.parseInt(votoAcumulativoRadio7))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}
			else if(votoAcumulativo.getOptions().size()==9){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3)+ Integer.parseInt(votoAcumulativoRadio4)+ Integer.parseInt(votoAcumulativoRadio5)+ Integer.parseInt(votoAcumulativoRadio6)+ Integer.parseInt(votoAcumulativoRadio7)+ Integer.parseInt(votoAcumulativoRadio8))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}
			else if(votoAcumulativo.getOptions().size()==10){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3)+ Integer.parseInt(votoAcumulativoRadio4)+ Integer.parseInt(votoAcumulativoRadio5)+ Integer.parseInt(votoAcumulativoRadio6)+ Integer.parseInt(votoAcumulativoRadio7)+ Integer.parseInt(votoAcumulativoRadio8)+ Integer.parseInt(votoAcumulativoRadio9))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}
			else if(votoAcumulativo.getOptions().size()==11){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3)+ Integer.parseInt(votoAcumulativoRadio4)+ Integer.parseInt(votoAcumulativoRadio5)+ Integer.parseInt(votoAcumulativoRadio6)+ Integer.parseInt(votoAcumulativoRadio7)+ Integer.parseInt(votoAcumulativoRadio8)+ Integer.parseInt(votoAcumulativoRadio9)+ Integer.parseInt(votoAcumulativoRadio10))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}
			else if(votoAcumulativo.getOptions().size()==12){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3)+ Integer.parseInt(votoAcumulativoRadio4)+ Integer.parseInt(votoAcumulativoRadio5)+ Integer.parseInt(votoAcumulativoRadio6)+ Integer.parseInt(votoAcumulativoRadio7)+ Integer.parseInt(votoAcumulativoRadio8)+ Integer.parseInt(votoAcumulativoRadio9)+ Integer.parseInt(votoAcumulativoRadio10)+ Integer.parseInt(votoAcumulativoRadio11))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}
			else if(votoAcumulativo.getOptions().size()==13){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3)+ Integer.parseInt(votoAcumulativoRadio4)+ Integer.parseInt(votoAcumulativoRadio5)+ Integer.parseInt(votoAcumulativoRadio6)+ Integer.parseInt(votoAcumulativoRadio7)+ Integer.parseInt(votoAcumulativoRadio8)+ Integer.parseInt(votoAcumulativoRadio9)+ Integer.parseInt(votoAcumulativoRadio10)+ Integer.parseInt(votoAcumulativoRadio11)+ Integer.parseInt(votoAcumulativoRadio12))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}
			else if(votoAcumulativo.getOptions().size()==14){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3)+ Integer.parseInt(votoAcumulativoRadio4)+ Integer.parseInt(votoAcumulativoRadio5)+ Integer.parseInt(votoAcumulativoRadio6)+ Integer.parseInt(votoAcumulativoRadio7)+ Integer.parseInt(votoAcumulativoRadio8)+ Integer.parseInt(votoAcumulativoRadio9)+ Integer.parseInt(votoAcumulativoRadio10)+ Integer.parseInt(votoAcumulativoRadio11)+ Integer.parseInt(votoAcumulativoRadio12)+ Integer.parseInt(votoAcumulativoRadio13))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}		
			else if(votoAcumulativo.getOptions().size()==15){
				if((Integer.parseInt(votoAcumulativoRadio0) + Integer.parseInt(votoAcumulativoRadio1) + Integer.parseInt(votoAcumulativoRadio2)+ Integer.parseInt(votoAcumulativoRadio3)+ Integer.parseInt(votoAcumulativoRadio4)+ Integer.parseInt(votoAcumulativoRadio5)+ Integer.parseInt(votoAcumulativoRadio6)+ Integer.parseInt(votoAcumulativoRadio7)+ Integer.parseInt(votoAcumulativoRadio8)+ Integer.parseInt(votoAcumulativoRadio9)+ Integer.parseInt(votoAcumulativoRadio10)+ Integer.parseInt(votoAcumulativoRadio11)+ Integer.parseInt(votoAcumulativoRadio12)+ Integer.parseInt(votoAcumulativoRadio13)+ Integer.parseInt(votoAcumulativoRadio14))== 5)
				{showErrorVotoAcumulativo=false;}
				else {showErrorVotoAcumulativo=true;}
			}		


			if(showErrorVotoAcumulativo){
				return this;
			}
			else {
				if(ballot.isPublica())
				{
					ballot=ballotDao.getById(contextBallotId);
					if(ballot!=null && !ballot.isEnded() && !alreadyVote())
					{

						if(Integer.parseInt(votoAcumulativoRadio0) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio0); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(0));
						}
						if(Integer.parseInt(votoAcumulativoRadio1) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio1); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(1));
						}
						if(votoAcumulativo.getOptions().size()==3 && Integer.parseInt(votoAcumulativoRadio2) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio2); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(2));
						}
						if(votoAcumulativo.getOptions().size()==4 && Integer.parseInt(votoAcumulativoRadio3) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio3); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(3));
						}
						if(votoAcumulativo.getOptions().size()==5 && Integer.parseInt(votoAcumulativoRadio4) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio4); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(4));
						}
						if(votoAcumulativo.getOptions().size()==6 && Integer.parseInt(votoAcumulativoRadio5) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio5); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(5));
						}
						if(votoAcumulativo.getOptions().size()==7 && Integer.parseInt(votoAcumulativoRadio6) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio6); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(6));
						}
						if(votoAcumulativo.getOptions().size()==8 && Integer.parseInt(votoAcumulativoRadio7) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio7); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(7));
						}
						if(votoAcumulativo.getOptions().size()==9 && Integer.parseInt(votoAcumulativoRadio8) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio8); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(8));
						}
						if(votoAcumulativo.getOptions().size()==10 && Integer.parseInt(votoAcumulativoRadio9) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio9); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(9));
						}
						if(votoAcumulativo.getOptions().size()==11 && Integer.parseInt(votoAcumulativoRadio10) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio10); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(10));
						}
						if(votoAcumulativo.getOptions().size()==12 && Integer.parseInt(votoAcumulativoRadio11) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio11); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(11));
						}
						if(votoAcumulativo.getOptions().size()==13 && Integer.parseInt(votoAcumulativoRadio12) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio12); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(12));
						}
						if(votoAcumulativo.getOptions().size()==14 && Integer.parseInt(votoAcumulativoRadio13) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio13); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(13));
						}						
						if(votoAcumulativo.getOptions().size()==15 && Integer.parseInt(votoAcumulativoRadio14) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio14); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(14));
						}	
						votoAcumulativoDao.update(votoAcumulativo);
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

						if(Integer.parseInt(votoAcumulativoRadio0) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio0); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(0));
						}
						if(Integer.parseInt(votoAcumulativoRadio1) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio1); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(1));
						}
						if(Integer.parseInt(votoAcumulativoRadio2) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio2); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(2));
						}
						if(Integer.parseInt(votoAcumulativoRadio3) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio3); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(3));
						}
						if(Integer.parseInt(votoAcumulativoRadio4) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio4); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(4));
						}
						if(Integer.parseInt(votoAcumulativoRadio5) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio5); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(5));
						}
						if(Integer.parseInt(votoAcumulativoRadio6) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio6); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(6));
						}
						if(Integer.parseInt(votoAcumulativoRadio7) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio7); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(7));
						}
						if(Integer.parseInt(votoAcumulativoRadio8) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio8); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(8));
						}
						if(Integer.parseInt(votoAcumulativoRadio9) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio9); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(9));
						}
						if(Integer.parseInt(votoAcumulativoRadio10) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio10); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(10));
						}
						if(Integer.parseInt(votoAcumulativoRadio11) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio11); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(11));
						}
						if(Integer.parseInt(votoAcumulativoRadio12) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio12); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(12));
						}
						if(Integer.parseInt(votoAcumulativoRadio13) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio13); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(13));
						}						
						if(Integer.parseInt(votoAcumulativoRadio14) > 0){
							for(int i=0; i<Integer.parseInt(votoAcumulativoRadio14); i++)
							votoAcumulativo.addVote(votoAcumulativo.getOptions().get(14));
						}							votoAcumulativoDao.update(votoAcumulativo);
					}
				}

				contextResultBallotId=contextBallotId;
				componentResources.discardPersistentFieldChanges();
				return VoteCounted.class;
			}
		}
		return VoteCounted.class;


	}

	public boolean isShowVotoAcumulativo()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.VOTO_ACUMULATIVO)
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
		//		if(publicVotes==null)
		//			return false;
		//		List<String> list=publicVotes.get(datasession.getIdSession());
		//		if(list==null)
		//		{
		//			return false;
		//		}
		//		else
		//		{
		//			for(String current:list)
		//			{
		//				if(current.equals(contextBallotId))
		//				{
		//					return true;
		//				}
		//			}
		//			return false;vale
		//		}
		return false;
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
