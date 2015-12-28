package com.pfc.ballots.pages.ballot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.services.StringValueEncoder;
import org.apache.tapestry5.ioc.Messages;
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
import com.pfc.ballots.dao.MejorPeorDao;
import com.pfc.ballots.dao.NansonDao;
import com.pfc.ballots.dao.RangeVotingDao;
import com.pfc.ballots.dao.RelativeMajorityDao;
import com.pfc.ballots.dao.SchulzeDao;
import com.pfc.ballots.dao.SmallDao;
import com.pfc.ballots.dao.VoteDao;
import com.pfc.ballots.dao.VotoAcumulativoDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.data.Method;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.entities.Vote;
import com.pfc.ballots.entities.ballotdata.ApprovalVoting;
import com.pfc.ballots.entities.ballotdata.Black;
import com.pfc.ballots.entities.ballotdata.Borda;
import com.pfc.ballots.entities.ballotdata.Brams;
import com.pfc.ballots.entities.ballotdata.Bucklin;
import com.pfc.ballots.entities.ballotdata.Condorcet;
import com.pfc.ballots.entities.ballotdata.Coombs;
import com.pfc.ballots.entities.ballotdata.Copeland;
import com.pfc.ballots.entities.ballotdata.Dodgson;
import com.pfc.ballots.entities.ballotdata.Hare;
import com.pfc.ballots.entities.ballotdata.JuicioMayoritario;
import com.pfc.ballots.entities.ballotdata.Kemeny;
import com.pfc.ballots.entities.ballotdata.MejorPeor;
import com.pfc.ballots.entities.ballotdata.Nanson;
import com.pfc.ballots.entities.ballotdata.RangeVoting;
import com.pfc.ballots.entities.ballotdata.RelativeMajority;
import com.pfc.ballots.entities.ballotdata.Schulze;
import com.pfc.ballots.entities.ballotdata.Small;
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
 * @version 2.0 DIC-2015
 */

@Import(library = { "context:js/jquery-min.js", "context:js/jquery.fancybox.pack.js"})
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


	@InjectComponent
	private Zone imagesZone;

	@Property
	@SessionAttribute
	private String contextBallotId;

	@SessionAttribute
	private String contextResultBallotId;

	@SessionAttribute
	private Map<String,List<String>>publicVotes;


	@Property
	private final StringValueEncoder stringValueEncoder = new StringValueEncoder();

	@Property
	private String imagen;

	@Property
	@Persist
	ArrayList<String> ballotImages;

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
	@Persist
	JuicioMayoritarioDao juicioMayoritarioDao;	
	@Persist
	CondorcetDao condorcetDao;	
	@Persist
	CopelandDao copelandDao;	
	@Persist
	SchulzeDao schulzeDao;	
	@Persist
	SmallDao smallDao;	
	@Persist
	DodgsonDao dodgsonDao;	
	@Persist
	BlackDao blackDao;
	@Persist
	BucklinDao bucklinDao;	
	@Persist
	NansonDao nansonDao;	
	@Persist
	HareDao hareDao;	
	@Persist
	CoombsDao coombsDao;	
	@Persist
	MejorPeorDao mejorPeorDao;


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

		ballotImages= ballot.getImagenes();

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
			bramsAux=new LinkedList<String>();
			bramsVote = new LinkedList<String>();
			bramsModel=NUMBERS1_10;
			bramsSel0 = bramsSel1=bramsSel2=bramsSel3=bramsSel4=bramsSel5=bramsSel6=bramsSel7=bramsSel8=bramsSel9=bramsSel10=bramsSel11=bramsSel12=bramsSel13=bramsSel14="0";
		}	
		if(ballot.getMethod()==Method.VOTO_ACUMULATIVO)
		{
			votoAcumulativoDao=DB4O.getVotoAcumulativoDao(datasession.getDBName());
			votoAcumulativo=votoAcumulativoDao.getByBallotId(contextBallotId);
		}	
		if(ballot.getMethod()==Method.JUICIO_MAYORITARIO)
		{
			juicioMayoritarioDao=DB4O.getJuicioMayoritarioDao(datasession.getDBName());
			juicioMayoritario=juicioMayoritarioDao.getByBallotId(contextBallotId);
			juicioMayoritarioVote = new LinkedList<String>();
		}	
		if(ballot.getMethod()==Method.CONDORCET)
		{
			condorcetDao=DB4O.getCondorcetDao(datasession.getDBName());
			condorcet=condorcetDao.getByBallotId(contextBallotId);
			radiobuttonsize = new String[condorcet.getOptions().size()];
			for (int i = 0; i<condorcet.getOptions().size(); i++){
				radiobuttonsize[i] = String.valueOf(i);
			}
			condorcetVote=new LinkedList<String>();
		}	
		if(ballot.getMethod()==Method.BLACK)
		{
			blackDao=DB4O.getBlackDao(datasession.getDBName());
			black=blackDao.getByBallotId(contextBallotId);
			radiobuttonsizeBlack = new String[black.getOptions().size()];
			for (int i = 0; i<black.getOptions().size(); i++){
				radiobuttonsizeBlack[i] = String.valueOf(i);
			}
			blackVote=new LinkedList<String>();
		}	
		if(ballot.getMethod()==Method.DODGSON)
		{
			dodgsonDao=DB4O.getDodgsonDao(datasession.getDBName());
			dodgson=dodgsonDao.getByBallotId(contextBallotId);
			radiobuttonsizeDodgson = new String[dodgson.getOptions().size()];
			for (int i = 0; i<dodgson.getOptions().size(); i++){
				radiobuttonsizeDodgson[i] = String.valueOf(i);
			}
			dodgsonVote=new LinkedList<String>();
		}	
		if(ballot.getMethod()==Method.COPELAND)
		{
			copelandDao=DB4O.getCopelandDao(datasession.getDBName());
			copeland=copelandDao.getByBallotId(contextBallotId);
			radiobuttonsizeCopeland = new String[copeland.getOptions().size()];
			for (int i = 0; i<copeland.getOptions().size(); i++){
				radiobuttonsizeCopeland[i] = String.valueOf(i);
			}
			copelandVote=new LinkedList<String>();
		}	
		if(ballot.getMethod()==Method.SCHULZE)
		{
			schulzeDao=DB4O.getSchulzeDao(datasession.getDBName());
			schulze=schulzeDao.getByBallotId(contextBallotId);
			radiobuttonsizeSchulze = new String[schulze.getOptions().size()];
			for (int i = 0; i<schulze.getOptions().size(); i++){
				radiobuttonsizeSchulze[i] = String.valueOf(i);
			}
			schulzeVote=new LinkedList<String>();
		}	
		if(ballot.getMethod()==Method.SMALL)
		{
			smallDao=DB4O.getSmallDao(datasession.getDBName());
			small=smallDao.getByBallotId(contextBallotId);
			radiobuttonsizeSmall = new String[small.getOptions().size()];
			for (int i = 0; i<small.getOptions().size(); i++){
				radiobuttonsizeSmall[i] = String.valueOf(i);
			}
			smallVote=new LinkedList<String>();
		}	
		if(ballot.getMethod()==Method.MEJOR_PEOR)
		{
			mejorPeorDao=DB4O.getMejorPeorDao(datasession.getDBName());
			mejorPeor=mejorPeorDao.getByBallotId(contextBallotId);
		}	
		if(ballot.getMethod()==Method.BUCKLIN)
		{
			bucklinDao=DB4O.getBucklinDao(datasession.getDBName());
			bucklin=bucklinDao.getByBallotId(contextBallotId);
			bucklinVote=new LinkedList<String>();
		}
		if(ballot.getMethod()==Method.NANSON)
		{
			nansonDao=DB4O.getNansonDao(datasession.getDBName());
			nanson=nansonDao.getByBallotId(contextBallotId);
			nansonVote=new LinkedList<String>();
		}
		if(ballot.getMethod()==Method.HARE)
		{
			hareDao=DB4O.getHareDao(datasession.getDBName());
			hare=hareDao.getByBallotId(contextBallotId);
			hareVote=new LinkedList<String>();
		}
		if(ballot.getMethod()==Method.COOMBS)
		{
			coombsDao=DB4O.getCoombsDao(datasession.getDBName());
			coombs=coombsDao.getByBallotId(contextBallotId);
			coombsVote=new LinkedList<String>();
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
	private List<String> bramsAux;

	@Persist
	@Property
	private List<String> bramsVote;

	@Property
	@Persist
	private boolean showErrorBrams;

	static final private String[] NUMBERS1_10 = new String[] { "1","2", "3", "4","5","6","7","8", "9", "10" };


	@Property
	@Persist 
	private String bramsValues;

	@Property
	@Persist 
	private String [] bramsModel;

	@Property
	@Persist 
	private String bramsSel0;

	@Property
	@Persist 
	private String bramsSel1;

	@Property
	@Persist 
	private String bramsSel2;

	@Property
	@Persist
	private String bramsSel3;

	@Property
	@Persist 
	private String bramsSel4;

	@Property
	@Persist 
	private String bramsSel5;

	@Property
	@Persist 
	private String bramsSel6;

	@Property
	@Persist 
	private String bramsSel7;

	@Property
	@Persist 
	@Validate("required")
	private String bramsSel8;

	@Property
	@Persist 
	@Validate("required")
	private String bramsSel9;

	@Property
	@Persist 
	@Validate("required")
	private String bramsSel10;

	@Property
	@Persist 
	@Validate("required")
	private String bramsSel11;

	@Property
	@Persist 
	@Validate("required")
	private String bramsSel12;

	@Property
	@Persist 
	@Validate("required")
	private String bramsSel13;

	@Property
	@Persist 
	@Validate("required")
	private String bramsSel14;


	public boolean isShowBrams8()
	{
		if(brams.getOptions().size()>7)
			return true;
		else
			return false;
	}
	public boolean isShowBrams9()
	{
		if(brams.getOptions().size()>8)
			return true;
		else
			return false;
	}
	public boolean isShowBrams10()
	{
		if(brams.getOptions().size()>9)
			return true;
		else
			return false;
	}
	public boolean isShowBrams11()
	{
		if(brams.getOptions().size()>10)
			return true;
		else
			return false;
	}
	public boolean isShowBrams12()
	{
		if(brams.getOptions().size()>11)
			return true;
		else
			return false;
	}
	public boolean isShowBrams13()
	{
		if(brams.getOptions().size()>12)
			return true;
		else
			return false;
	}
	public boolean isShowBrams14()
	{
		if(brams.getOptions().size()>13)
			return true;
		else
			return false;
	}
	public boolean isShowBrams15()
	{
		if(brams.getOptions().size()>=14)
			return true;
		else
			return false;
	}


	public String getBramsOption0()
	{
		return brams.getOptions().get(0);
	}
	public String getBramsOption1()
	{
		return brams.getOptions().get(1);
	}
	public String getBramsOption2()
	{
		return brams.getOptions().get(2);
	}
	public String getBramsOption3()
	{
		return brams.getOptions().get(3);
	}
	public String getBramsOption4()
	{
		return brams.getOptions().get(4);
	}
	public String getBramsOption5()
	{
		return brams.getOptions().get(5);
	}
	public String getBramsOption6()
	{
		return brams.getOptions().get(6);
	}
	public String getBramsOption7()
	{
		return brams.getOptions().get(7);
	}
	public String getBramsOption8()
	{
		return brams.getOptions().get(8);
	}
	public String getBramsOption9()
	{
		return brams.getOptions().get(9);
	}
	public String getBramsOption10()
	{
		return brams.getOptions().get(10);
	}
	public String getBramsOption11()
	{
		return brams.getOptions().get(11);
	}
	public String getBramsOption12()
	{
		return brams.getOptions().get(12);
	}
	public String getBramsOption13()
	{
		return brams.getOptions().get(13);
	}
	public String getBramsOption14()
	{
		return brams.getOptions().get(14);
	}

	public void  onValueChangedFromBramsSel0(String str)
	{
		bramsSel0=str;
	}
	public void  onValueChangedFromBramsSel1(String str)
	{
		bramsSel1=str;
	}
	public void  onValueChangedFromBramsSel2(String str)
	{
		bramsSel2=str;
	}
	public void  onValueChangedFromBramsSel3(String str)
	{
		bramsSel3=str;
	}
	public void  onValueChangedFromBramsSel4(String str)
	{
		bramsSel4=str;
	}
	public void  onValueChangedFromBramsSel5(String str)
	{
		bramsSel5=str;
	}
	public void  onValueChangedFromBramsSel6(String str)
	{
		bramsSel6=str;
	}
	public void  onValueChangedFromBramsSel7(String str)
	{
		bramsSel7=str;
	}
	public void  onValueChangedFromBramsSel8(String str)
	{
		bramsSel8=str;
	}
	public void  onValueChangedFromBramsSel9(String str)
	{
		bramsSel9=str;
	}
	public void  onValueChangedFromBramsSel10(String str)
	{
		bramsSel10=str;
	}
	public void  onValueChangedFromBramsSel11(String str)
	{
		bramsSel11=str;
	}
	public void  onValueChangedFromBramsSel12(String str)
	{
		bramsSel12=str;
	}
	public void  onValueChangedFromBramsSel13(String str)
	{
		bramsSel13=str;
	}
	public void  onValueChangedFromBramsSel14(String str)
	{
		bramsSel14=str;
	}

	public Object onSuccessFromBramsForm()
	{
		if(request.isXHR())
		{
			if (!bramsSel0.equals("0")){
				bramsAux.add(bramsSel0);
			}
			if (!bramsSel1.equals("0")){
				bramsAux.add(bramsSel1);
			}
			if (!bramsSel2.equals("0")){
				bramsAux.add(bramsSel2);
			}
			if (!bramsSel3.equals("0")){
				bramsAux.add(bramsSel3);
			}
			if (!bramsSel4.equals("0")){
				bramsAux.add(bramsSel4);
			}
			if (!bramsSel5.equals("0")){
				bramsAux.add(bramsSel5);
			}
			if (!bramsSel6.equals("0")){
				bramsAux.add(bramsSel6);
			}
			if (isShowBrams8()){
				if (!bramsSel7.equals("0")){
					bramsAux.add(bramsSel7);
				}
			}
			if (isShowBrams9()){
				if (!bramsSel7.equals("0")){
					bramsAux.add(bramsSel8);
				}
			}
			if (isShowBrams10()){
				if (!bramsSel7.equals("0")){
					bramsAux.add(bramsSel9);
				}
			}
			if (isShowBrams11()){
				if (!bramsSel7.equals("0")){
					bramsAux.add(bramsSel10);
				}
			}
			if (isShowBrams12()){
				if (!bramsSel7.equals("0")){
					bramsAux.add(bramsSel11);
				}
			}
			if (isShowBrams13()){
				if (!bramsSel7.equals("0")){
					bramsAux.add(bramsSel12);
				}
			}
			if (isShowBrams14()){
				if (!bramsSel7.equals("0")){
					bramsAux.add(bramsSel13);
				}
			}
			if (isShowBrams15()){
				if (!bramsSel7.equals("0")){
					bramsAux.add(bramsSel14);
				}
			}

			if (bramsAux.size() > (brams.getOptions().size() - 3)){
				showErrorBrams = true;
				bramsAux = new LinkedList<String>();
				ajaxResponseRenderer.addRender("bramsZone",bramsZone);
			}
			else {
				if(ballot.isPublica())
				{
					ballot=ballotDao.getById(contextBallotId);
					if(ballot!=null && !ballot.isEnded() && !alreadyVote())
					{
						System.out.println("BramsSel0 "+bramsSel0);
						bramsVote.add(bramsSel0);
						bramsVote.add(bramsSel1);
						bramsVote.add(bramsSel2);
						bramsVote.add(bramsSel3);
						bramsVote.add(bramsSel4);
						bramsVote.add(bramsSel5);
						bramsVote.add(bramsSel6);
						if (isShowBrams8()){
							bramsVote.add(bramsSel7);
						}
						if (isShowBrams9()){
							bramsVote.add(bramsSel8);
						}
						if (isShowBrams10()){
							bramsVote.add(bramsSel9);
						}
						if (isShowBrams11()){
							bramsVote.add(bramsSel10);

						}
						if (isShowBrams12()){
							bramsVote.add(bramsSel11);
						}
						if (isShowBrams13()){
							bramsVote.add(bramsSel12);

						}
						if (isShowBrams14()){
							bramsVote.add(bramsSel13);
						}
						if (isShowBrams15()){
							bramsVote.add(bramsSel14);
						}



						brams.addVote(bramsVote);
						bramsDao.update(brams);
						addPublicVote();
					}
					System.out.println(bramsVote);
				}
				else
				{

					vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
					ballot=ballotDao.getById(contextBallotId);

					if(ballot!=null && !ballot.isEnded() && !vote.isCounted())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
					{
						bramsVote.add(bramsSel0);
						bramsVote.add(bramsSel1);
						bramsVote.add(bramsSel2);
						bramsVote.add(bramsSel3);
						bramsVote.add(bramsSel4);
						bramsVote.add(bramsSel5);
						bramsVote.add(bramsSel6);
						if (isShowBrams8()){
							bramsVote.add(bramsSel7);
						}
						if (isShowBrams9()){
							bramsVote.add(bramsSel8);
						}
						if (isShowBrams10()){
							bramsVote.add(bramsSel9);
						}
						if (isShowBrams11()){
							bramsVote.add(bramsSel10);

						}
						if (isShowBrams12()){
							bramsVote.add(bramsSel11);
						}
						if (isShowBrams13()){
							bramsVote.add(bramsSel12);

						}
						if (isShowBrams14()){
							bramsVote.add(bramsSel13);
						}
						if (isShowBrams15()){
							bramsVote.add(bramsSel14);
						}
						vote.setCounted(true);
						voteDao.updateVote(vote);
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
		if(votoAcumulativo.getOptions().size()>2)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo4()
	{
		if(votoAcumulativo.getOptions().size()>3)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo5()
	{
		if(votoAcumulativo.getOptions().size()>=4)
			return true;
		else
			return false;
	}
	public boolean isShowVotoAcumulativo6()
	{
		if(votoAcumulativo.getOptions().size()>=5)
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
			if(votoAcumulativo.getOptions().size()==2){
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

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////Juicio Mayoritario /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Stores the JuicioMayoritario vote
	 * @return
	 */


	@Persist
	@Property
	private JuicioMayoritario juicioMayoritario;

	@Property
	@Persist
	private boolean showErrorJuicioMayoritario;

	@Property
	@Persist
	private String juicioMayoritarioRadio0;

	@Property
	@Persist
	private String juicioMayoritarioRadio1;

	@Property
	@Persist
	private String juicioMayoritarioRadio2;	

	@Property
	@Persist
	private String juicioMayoritarioRadio3;

	@Property
	@Persist
	private String juicioMayoritarioRadio4;

	@Property
	@Persist
	private String juicioMayoritarioRadio5;	

	@Property
	@Persist
	private String juicioMayoritarioRadio6;

	@Property
	@Persist
	private String juicioMayoritarioRadio7;

	@Property
	@Persist
	private String juicioMayoritarioRadio8;	

	@Property
	@Persist
	private String juicioMayoritarioRadio9;

	@Property
	@Persist
	private String juicioMayoritarioRadio10;

	@Property
	@Persist
	private String juicioMayoritarioRadio11;	

	@Property
	@Persist
	private String juicioMayoritarioRadio12;

	@Property
	@Persist
	private String juicioMayoritarioRadio13;

	@Property
	@Persist
	private String juicioMayoritarioRadio14;	

	@Property
	@Persist
	private String juicioMayoritarioRadio15;	

	@Persist
	@Property
	private List<String> juicioMayoritarioVote;

	public boolean isShowJuicioMayoritario8()
	{
		System.out.println("num opciones: "+juicioMayoritario.getOptions().size());
		if(juicioMayoritario.getOptions().size()>=8)
			return true;
		else
			return false;
	}
	public boolean isShowJuicioMayoritario9()
	{
		if(juicioMayoritario.getOptions().size()>=9)
			return true;
		else
			return false;
	}
	public boolean isShowJuicioMayoritario10()
	{
		if(juicioMayoritario.getOptions().size()>=10)
			return true;
		else
			return false;
	}
	public boolean isShowJuicioMayoritario11()
	{
		if(juicioMayoritario.getOptions().size()>=11)
			return true;
		else
			return false;
	}
	public boolean isShowJuicioMayoritario12()
	{
		if(juicioMayoritario.getOptions().size()>=12)
			return true;
		else
			return false;
	}
	public boolean isShowJuicioMayoritario13()
	{
		if(juicioMayoritario.getOptions().size()>=13)
			return true;
		else
			return false;
	}
	public boolean isShowJuicioMayoritario14()
	{
		if(juicioMayoritario.getOptions().size()>=14)
			return true;
		else
			return false;
	}
	public boolean isShowJuicioMayoritario15()
	{
		if(juicioMayoritario.getOptions().size()>=15)
			return true;
		else
			return false;
	}


	public String getJuicioMayoritarioOption0()
	{
		juicioMayoritarioRadio0 = "1";
		return juicioMayoritario.getOptions().get(0);
	}
	public String getJuicioMayoritarioOption1()
	{
		juicioMayoritarioRadio1 = "1";
		return juicioMayoritario.getOptions().get(1);
	}
	public String getJuicioMayoritarioOption2()
	{
		juicioMayoritarioRadio2 = "1";
		return juicioMayoritario.getOptions().get(2);
	}
	public String getJuicioMayoritarioOption3()
	{
		juicioMayoritarioRadio3 = "1";
		return juicioMayoritario.getOptions().get(3);
	}
	public String getJuicioMayoritarioOption4()
	{
		juicioMayoritarioRadio4 = "1";
		return juicioMayoritario.getOptions().get(4);
	}
	public String getJuicioMayoritarioOption5()
	{
		juicioMayoritarioRadio5 = "1";
		return juicioMayoritario.getOptions().get(5);
	}
	public String getJuicioMayoritarioOption6()
	{
		juicioMayoritarioRadio6 = "1";
		return juicioMayoritario.getOptions().get(6);
	}
	public String getJuicioMayoritarioOption7()
	{
		juicioMayoritarioRadio7 = "1";
		return juicioMayoritario.getOptions().get(7);
	}
	public String getJuicioMayoritarioOption8()
	{
		juicioMayoritarioRadio8 = "1";
		return juicioMayoritario.getOptions().get(8);
	}
	public String getJuicioMayoritarioOption9()
	{
		juicioMayoritarioRadio9 = "1";
		return juicioMayoritario.getOptions().get(9);
	}
	public String getJuicioMayoritarioOption10()
	{
		juicioMayoritarioRadio10 = "1";
		return juicioMayoritario.getOptions().get(10);
	}
	public String getJuicioMayoritarioOption11()
	{
		juicioMayoritarioRadio11 = "1";
		return juicioMayoritario.getOptions().get(11);
	}
	public String getJuicioMayoritarioOption12()
	{
		juicioMayoritarioRadio12 = "1";
		return juicioMayoritario.getOptions().get(12);
	}
	public String getJuicioMayoritarioOption13()
	{
		juicioMayoritarioRadio13 = "1";
		return juicioMayoritario.getOptions().get(13);
	}
	public String getJuicioMayoritarioOption14()
	{
		juicioMayoritarioRadio14 = "1";
		return juicioMayoritario.getOptions().get(14);
	}

	public Object onSuccessFromJuicioMayoritarioForm()
	{
		if(request.isXHR())
		{
			if(showErrorJuicioMayoritario){
				return this;
			}
			else {
				if(ballot.isPublica())
				{
					ballot=ballotDao.getById(contextBallotId);
					if(ballot!=null && !ballot.isEnded() && !alreadyVote())
					{
						juicioMayoritarioVote.add(juicioMayoritarioRadio0);
						juicioMayoritarioVote.add(juicioMayoritarioRadio1);
						juicioMayoritarioVote.add(juicioMayoritarioRadio2);
						juicioMayoritarioVote.add(juicioMayoritarioRadio3);
						juicioMayoritarioVote.add(juicioMayoritarioRadio4);
						juicioMayoritarioVote.add(juicioMayoritarioRadio5);
						juicioMayoritarioVote.add(juicioMayoritarioRadio6);

						if(!(juicioMayoritarioRadio7 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio7);
						}
						if(!(juicioMayoritarioRadio8 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio8);
						}
						if(!(juicioMayoritarioRadio9 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio9);
						}
						if(!(juicioMayoritarioRadio10 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio10);
						}
						if(!(juicioMayoritarioRadio11 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio11);
						}
						if(!(juicioMayoritarioRadio12 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio12);
						}
						if(!(juicioMayoritarioRadio13 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio13);
						}
						if(!(juicioMayoritarioRadio14 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio14);
						}
					}
					juicioMayoritario.addVote(juicioMayoritarioVote);
					juicioMayoritarioDao.update(juicioMayoritario);
					addPublicVote();
				}

				else
				{

					vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
					ballot=ballotDao.getById(contextBallotId);

					if(ballot!=null && !ballot.isEnded() && !vote.isCounted())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
					{
						vote.setCounted(true);
						voteDao.updateVote(vote);

						juicioMayoritarioVote.add(juicioMayoritarioRadio0);
						juicioMayoritarioVote.add(juicioMayoritarioRadio1);
						juicioMayoritarioVote.add(juicioMayoritarioRadio2);
						juicioMayoritarioVote.add(juicioMayoritarioRadio3);
						juicioMayoritarioVote.add(juicioMayoritarioRadio4);
						juicioMayoritarioVote.add(juicioMayoritarioRadio5);
						juicioMayoritarioVote.add(juicioMayoritarioRadio6);

						if(!(juicioMayoritarioRadio7 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio7);
						}
						if(!(juicioMayoritarioRadio8 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio8);
						}
						if(!(juicioMayoritarioRadio9 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio9);
						}
						if(!(juicioMayoritarioRadio10 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio10);
						}
						if(!(juicioMayoritarioRadio11 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio11);
						}
						if(!(juicioMayoritarioRadio12 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio12);
						}
						if(!(juicioMayoritarioRadio13 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio13);
						}
						if(!(juicioMayoritarioRadio14 == null)){
							juicioMayoritarioVote.add(juicioMayoritarioRadio14);
						}
						juicioMayoritario.addVote(juicioMayoritarioVote);
						juicioMayoritarioDao.update(juicioMayoritario);
					}
				}

				contextResultBallotId=contextBallotId;
				componentResources.discardPersistentFieldChanges();
				return VoteCounted.class;
			}
		}
		return VoteCounted.class;


	}

	public boolean isShowJuicioMayoritario()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.JUICIO_MAYORITARIO)
		{
			return true;
		}
		return false;

	}		


	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// Condorcet /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Stores the Condorcet vote
	 * @return
	 */


	@Persist
	@Property
	private Condorcet condorcet;

	@Property
	@Persist
	private boolean showErrorCondorcet;

	@Property
	@Persist
	private String condorcetRadio0;

	@Property
	@Persist
	private String condorcetRadio1;

	@Property
	@Persist
	private String condorcetRadio2;	

	@Property
	@Persist
	private String condorcetRadio3;

	@Property
	@Persist
	private String condorcetRadio4;

	@Property
	@Persist
	private String condorcetRadio5;	

	@Property
	@Persist
	private String condorcetRadio6;

	@Property
	@Persist
	private String condorcetRadio7;

	@Property
	@Persist
	private String condorcetRadio8;	

	@Property
	@Persist
	private String condorcetRadio9;

	@Property
	@Persist
	private String condorcetRadio10;

	@Property
	@Persist
	private String condorcetRadio11;	

	@Property
	@Persist
	private String condorcetRadio12;

	@Property
	@Persist
	private String condorcetRadio13;

	@Property
	@Persist
	private String condorcetRadio14;	

	@Property
	@Persist
	private String condorcetRadio15;	

	@Property
	@Persist
	private String[] radiobuttonsize;

	@Property
	@Persist
	private String radioSize;

	@Persist
	@Property
	private List<String> condorcetVote;


	public boolean isShowCondorcet3()
	{
		if(condorcet.getOptions().size()>=3)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet4()
	{
		if(condorcet.getOptions().size()>=4)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet5()
	{
		if(condorcet.getOptions().size()>=5)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet6()
	{
		if(condorcet.getOptions().size()>=6)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet7()
	{
		if(condorcet.getOptions().size()>=7)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet8()
	{
		if(condorcet.getOptions().size()>=8)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet9()
	{
		if(condorcet.getOptions().size()>=9)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet10()
	{
		if(condorcet.getOptions().size()>=10)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet11()
	{
		if(condorcet.getOptions().size()>=11)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet12()
	{
		if(condorcet.getOptions().size()>=12)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet13()
	{
		if(condorcet.getOptions().size()>=13)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet14()
	{
		if(condorcet.getOptions().size()>=14)
			return true;
		else
			return false;
	}
	public boolean isShowCondorcet15()
	{
		if(condorcet.getOptions().size()>=15)
			return true;
		else
			return false;
	}


	public String getCondorcetOption0()
	{
		condorcetRadio0 = "0";
		return condorcet.getOptions().get(0);
	}
	public String getCondorcetOption1()
	{
		condorcetRadio1 = "0";
		return condorcet.getOptions().get(1);
	}
	public String getCondorcetOption2()
	{
		condorcetRadio2 = "0";
		return condorcet.getOptions().get(2);
	}
	public String getCondorcetOption3()
	{
		condorcetRadio3 = "0";
		return condorcet.getOptions().get(3);
	}
	public String getCondorcetOption4()
	{
		condorcetRadio4 = "0";
		return condorcet.getOptions().get(4);
	}
	public String getCondorcetOption5()
	{
		condorcetRadio5 = "0";
		return condorcet.getOptions().get(5);
	}
	public String getCondorcetOption6()
	{
		condorcetRadio6 = "0";
		return condorcet.getOptions().get(6);
	}
	public String getCondorcetOption7()
	{
		condorcetRadio7 = "0";
		return condorcet.getOptions().get(7);
	}
	public String getCondorcetOption8()
	{
		condorcetRadio8 = "0";
		return condorcet.getOptions().get(8);
	}
	public String getCondorcetOption9()
	{
		condorcetRadio9 = "0";
		return condorcet.getOptions().get(9);
	}
	public String getCondorcetOption10()
	{
		condorcetRadio10 = "0";
		return condorcet.getOptions().get(10);
	}
	public String getCondorcetOption11()
	{
		condorcetRadio11 = "0";
		return condorcet.getOptions().get(11);
	}
	public String getCondorcetOption12()
	{
		condorcetRadio12 = "0";
		return condorcet.getOptions().get(12);
	}
	public String getCondorcetOption13()
	{
		condorcetRadio13 = "0";
		return condorcet.getOptions().get(13);
	}
	public String getCondorcetOption14()
	{
		condorcetRadio14 = "0";
		return condorcet.getOptions().get(14);
	}

	public Object onSuccessFromCondorcetForm()
	{
		if(request.isXHR())
		{
			if(showErrorCondorcet){
				return this;
			}
			else {
				if(ballot.isPublica())
				{
					ballot=ballotDao.getById(contextBallotId);
					if(ballot!=null && !ballot.isEnded() && !alreadyVote())
					{
						condorcetVote.add(condorcetRadio0);
						System.out.println("El valor en el radio  0 es de : "+Integer.parseInt(condorcetRadio0));
						condorcetVote.add(condorcetRadio1);
						System.out.println("El valor en el radio  1 es de : "+Integer.parseInt(condorcetRadio1));
						System.out.println("Tamaño de esto: "+condorcet.getOptions().size());
						if(condorcet.getOptions().size()>=3){
							condorcetVote.add(condorcetRadio2);
							System.out.println("El valor en el radio  2 es de : "+Integer.parseInt(condorcetRadio2));
						}
						if(condorcet.getOptions().size()>=4 ){
							condorcetVote.add(condorcetRadio3);
						}
						if(condorcet.getOptions().size()>=5){
							condorcetVote.add(condorcetRadio4);
						}
						if(condorcet.getOptions().size()>=6){
							condorcetVote.add(condorcetRadio5);
						}
						if(condorcet.getOptions().size()>=7){
							condorcetVote.add(condorcetRadio6);
						}
						if(condorcet.getOptions().size()>=8 ){
							condorcetVote.add(condorcetRadio7);
						}
						if(condorcet.getOptions().size()>=9){
							condorcetVote.add(condorcetRadio8);
						}
						if(condorcet.getOptions().size()>=10){
							condorcetVote.add(condorcetRadio9);
						}
						if(condorcet.getOptions().size()>=11){
							condorcetVote.add(condorcetRadio10);
						}
						if(condorcet.getOptions().size()>=12){
							condorcetVote.add(condorcetRadio11);
						}
						if(condorcet.getOptions().size()>=13){
							condorcetVote.add(condorcetRadio12);
						}
						if(condorcet.getOptions().size()>=14){
							condorcetVote.add(condorcetRadio13);
						}						
						if(condorcet.getOptions().size()>=15){
							condorcetVote.add(condorcetRadio14);
						}	
						condorcet.addVote(condorcetVote);
						System.out.println("el valor de condorcetVote en votar es: "+condorcetVote);
						condorcetDao.update(condorcet);
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
						condorcetVote.add(condorcetRadio0);
						condorcetVote.add(condorcetRadio1);
						if(condorcet.getOptions().size()>=3){
							condorcetVote.add(condorcetRadio2);
						}
						if(condorcet.getOptions().size()>=4 ){
							condorcetVote.add(condorcetRadio3);
						}
						if(condorcet.getOptions().size()>=5){
							condorcetVote.add(condorcetRadio4);
						}
						if(condorcet.getOptions().size()>=6){
							condorcetVote.add(condorcetRadio5);
						}
						if(condorcet.getOptions().size()>=7){
							condorcetVote.add(condorcetRadio6);
						}
						if(condorcet.getOptions().size()>=8 ){
							condorcetVote.add(condorcetRadio7);
						}
						if(condorcet.getOptions().size()>=9){
							condorcetVote.add(condorcetRadio8);
						}
						if(condorcet.getOptions().size()>=10){
							condorcetVote.add(condorcetRadio9);
						}
						if(condorcet.getOptions().size()>=11){
							condorcetVote.add(condorcetRadio10);
						}
						if(condorcet.getOptions().size()>=12){
							condorcetVote.add(condorcetRadio11);
						}
						if(condorcet.getOptions().size()>=13){
							condorcetVote.add(condorcetRadio12);
						}
						if(condorcet.getOptions().size()>=14){
							condorcetVote.add(condorcetRadio13);
						}						
						if(condorcet.getOptions().size()>=15){
							condorcetVote.add(condorcetRadio14);
						}	
						condorcet.addVote(condorcetVote);
						condorcetDao.update(condorcet);
					}
				}

				contextResultBallotId=contextBallotId;
				componentResources.discardPersistentFieldChanges();
				return VoteCounted.class;
			}
		}
		return VoteCounted.class;


	}

	public boolean isShowCondorcet()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.CONDORCET)
		{
			return true;
		}
		return false;

	}		



	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// Copeland /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Stores the Copeland vote
	 * @return
	 */


	@Persist
	@Property
	private Copeland copeland;

	@Property
	@Persist
	private boolean showErrorCopeland;

	@Property
	@Persist
	private String copelandRadio0;

	@Property
	@Persist
	private String copelandRadio1;

	@Property
	@Persist
	private String copelandRadio2;	

	@Property
	@Persist
	private String copelandRadio3;

	@Property
	@Persist
	private String copelandRadio4;

	@Property
	@Persist
	private String copelandRadio5;	

	@Property
	@Persist
	private String copelandRadio6;

	@Property
	@Persist
	private String copelandRadio7;

	@Property
	@Persist
	private String copelandRadio8;	

	@Property
	@Persist
	private String copelandRadio9;

	@Property
	@Persist
	private String copelandRadio10;

	@Property
	@Persist
	private String copelandRadio11;	

	@Property
	@Persist
	private String copelandRadio12;

	@Property
	@Persist
	private String copelandRadio13;

	@Property
	@Persist
	private String copelandRadio14;	

	@Property
	@Persist
	private String copelandRadio15;	

	@Property
	@Persist
	private String[] radiobuttonsizeCopeland;

	@Property
	@Persist
	private String radioSizeCopeland;

	@Persist
	@Property
	private List<String> copelandVote;


	public boolean isShowCopeland3()
	{
		if(copeland.getOptions().size()>2)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland4()
	{
		if(copeland.getOptions().size()>3)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland5()
	{
		if(copeland.getOptions().size()>4)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland6()
	{
		if(copeland.getOptions().size()>5)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland7()
	{
		if(copeland.getOptions().size()>6)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland8()
	{
		if(copeland.getOptions().size()>7)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland9()
	{
		if(copeland.getOptions().size()>8)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland10()
	{
		if(copeland.getOptions().size()>9)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland11()
	{
		if(copeland.getOptions().size()>10)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland12()
	{
		if(copeland.getOptions().size()>11)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland13()
	{
		if(copeland.getOptions().size()>12)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland14()
	{
		if(copeland.getOptions().size()>13)
			return true;
		else
			return false;
	}
	public boolean isShowCopeland15()
	{
		if(copeland.getOptions().size()>=14)
			return true;
		else
			return false;
	}


	public String getCopelandOption0()
	{
		copelandRadio0 = "0";
		return copeland.getOptions().get(0);
	}
	public String getCopelandOption1()
	{
		copelandRadio1 = "0";
		return copeland.getOptions().get(1);
	}
	public String getCopelandOption2()
	{
		copelandRadio2 = "0";
		return copeland.getOptions().get(2);
	}
	public String getCopelandOption3()
	{
		copelandRadio3 = "0";
		return copeland.getOptions().get(3);
	}
	public String getCopelandOption4()
	{
		copelandRadio4 = "0";
		return copeland.getOptions().get(4);
	}
	public String getCopelandOption5()
	{
		copelandRadio5 = "0";
		return copeland.getOptions().get(5);
	}
	public String getCopelandOption6()
	{
		copelandRadio6 = "0";
		return copeland.getOptions().get(6);
	}
	public String getCopelandOption7()
	{
		copelandRadio7 = "0";
		return copeland.getOptions().get(7);
	}
	public String getCopelandOption8()
	{
		copelandRadio8 = "0";
		return copeland.getOptions().get(8);
	}
	public String getCopelandOption9()
	{
		copelandRadio9 = "0";
		return copeland.getOptions().get(9);
	}
	public String getCopelandOption10()
	{
		copelandRadio10 = "0";
		return copeland.getOptions().get(10);
	}
	public String getCopelandOption11()
	{
		copelandRadio11 = "0";
		return copeland.getOptions().get(11);
	}
	public String getCopelandOption12()
	{
		copelandRadio12 = "0";
		return copeland.getOptions().get(12);
	}
	public String getCopelandOption13()
	{
		copelandRadio13 = "0";
		return copeland.getOptions().get(13);
	}
	public String getCopelandOption14()
	{
		copelandRadio14 = "0";
		return copeland.getOptions().get(14);
	}

	public Object onSuccessFromCopelandForm()
	{
		if(request.isXHR())
		{
			if(showErrorCopeland){
				return this;
			}
			else {
				if(ballot.isPublica())
				{
					ballot=ballotDao.getById(contextBallotId);
					if(ballot!=null && !ballot.isEnded() && !alreadyVote())
					{
						copelandVote.add(copelandRadio0);
						copelandVote.add(copelandRadio1);

						if(copeland.getOptions().size()>=3){
							copelandVote.add(copelandRadio2);
						}
						if(copeland.getOptions().size()>=4 ){
							copelandVote.add(copelandRadio3);
						}
						if(copeland.getOptions().size()>=5){
							copelandVote.add(copelandRadio4);
						}
						if(copeland.getOptions().size()>=6){
							copelandVote.add(copelandRadio5);
						}
						if(copeland.getOptions().size()>=7){
							copelandVote.add(copelandRadio6);
						}
						if(copeland.getOptions().size()>=8 ){
							copelandVote.add(copelandRadio7);
						}
						if(copeland.getOptions().size()>=9){
							copelandVote.add(copelandRadio8);
						}
						if(copeland.getOptions().size()>=10){
							copelandVote.add(copelandRadio9);
						}
						if(copeland.getOptions().size()>=11){
							copelandVote.add(copelandRadio10);
						}
						if(copeland.getOptions().size()>=12){
							copelandVote.add(copelandRadio11);
						}
						if(copeland.getOptions().size()>=13){
							copelandVote.add(copelandRadio12);
						}
						if(copeland.getOptions().size()>=14){
							copelandVote.add(copelandRadio13);
						}						
						if(copeland.getOptions().size()>=15){
							copelandVote.add(copelandRadio14);
						}	
						copeland.addVote(copelandVote);
						copelandDao.update(copeland);
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
						copelandVote.add(copelandRadio0);
						copelandVote.add(copelandRadio1);

						if(copeland.getOptions().size()>=3){
							copelandVote.add(copelandRadio2);
						}
						if(copeland.getOptions().size()>=4 ){
							copelandVote.add(copelandRadio3);
						}
						if(copeland.getOptions().size()>=5){
							copelandVote.add(copelandRadio4);
						}
						if(copeland.getOptions().size()>=6){
							copelandVote.add(copelandRadio5);
						}
						if(copeland.getOptions().size()>=7){
							copelandVote.add(copelandRadio6);
						}
						if(copeland.getOptions().size()>=8 ){
							copelandVote.add(copelandRadio7);
						}
						if(copeland.getOptions().size()>=9){
							copelandVote.add(copelandRadio8);
						}
						if(copeland.getOptions().size()>=10){
							copelandVote.add(copelandRadio9);
						}
						if(copeland.getOptions().size()>=11){
							copelandVote.add(copelandRadio10);
						}
						if(copeland.getOptions().size()>=12){
							copelandVote.add(copelandRadio11);
						}
						if(copeland.getOptions().size()>=13){
							copelandVote.add(copelandRadio12);
						}
						if(copeland.getOptions().size()>=14){
							copelandVote.add(copelandRadio13);
						}						
						if(copeland.getOptions().size()>=15){
							copelandVote.add(copelandRadio14);
						}	
						copeland.addVote(copelandVote);
						copelandDao.update(copeland);
					}
				}

				contextResultBallotId=contextBallotId;
				componentResources.discardPersistentFieldChanges();
				return VoteCounted.class;
			}
		}
		return VoteCounted.class;


	}

	public boolean isShowCopeland()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.COPELAND)
		{
			return true;
		}
		return false;

	}	


	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// Schulze /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Stores the Schulze vote
	 * @return
	 */


	@Persist
	@Property
	private Schulze schulze;

	@Property
	@Persist
	private boolean showErrorSchulze;

	@Property
	@Persist
	private String schulzeRadio0;

	@Property
	@Persist
	private String schulzeRadio1;

	@Property
	@Persist
	private String schulzeRadio2;	

	@Property
	@Persist
	private String schulzeRadio3;

	@Property
	@Persist
	private String schulzeRadio4;

	@Property
	@Persist
	private String schulzeRadio5;	

	@Property
	@Persist
	private String schulzeRadio6;

	@Property
	@Persist
	private String schulzeRadio7;

	@Property
	@Persist
	private String schulzeRadio8;	

	@Property
	@Persist
	private String schulzeRadio9;

	@Property
	@Persist
	private String schulzeRadio10;

	@Property
	@Persist
	private String schulzeRadio11;	

	@Property
	@Persist
	private String schulzeRadio12;

	@Property
	@Persist
	private String schulzeRadio13;

	@Property
	@Persist
	private String schulzeRadio14;	

	@Property
	@Persist
	private String schulzeRadio15;	

	@Property
	@Persist
	private String[] radiobuttonsizeSchulze;

	@Property
	@Persist
	private String radioSizeSchulze;

	@Persist
	@Property
	private List<String> schulzeVote;


	public boolean isShowSchulze3()
	{
		if(schulze.getOptions().size()>2)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze4()
	{
		if(schulze.getOptions().size()>3)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze5()
	{
		if(schulze.getOptions().size()>4)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze6()
	{
		if(schulze.getOptions().size()>5)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze7()
	{
		if(schulze.getOptions().size()>6)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze8()
	{
		if(schulze.getOptions().size()>7)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze9()
	{
		if(schulze.getOptions().size()>8)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze10()
	{
		if(schulze.getOptions().size()>9)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze11()
	{
		if(schulze.getOptions().size()>10)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze12()
	{
		if(schulze.getOptions().size()>11)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze13()
	{
		if(schulze.getOptions().size()>12)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze14()
	{
		if(schulze.getOptions().size()>13)
			return true;
		else
			return false;
	}
	public boolean isShowSchulze15()
	{
		if(schulze.getOptions().size()>14)
			return true;
		else
			return false;
	}


	public String getSchulzeOption0()
	{
		schulzeRadio0 = "0";
		return schulze.getOptions().get(0);
	}
	public String getSchulzeOption1()
	{
		schulzeRadio1 = "0";
		return schulze.getOptions().get(1);
	}
	public String getSchulzeOption2()
	{
		schulzeRadio2 = "0";
		return schulze.getOptions().get(2);
	}
	public String getSchulzeOption3()
	{
		schulzeRadio3 = "0";
		return schulze.getOptions().get(3);
	}
	public String getSchulzeOption4()
	{
		schulzeRadio4 = "0";
		return schulze.getOptions().get(4);
	}
	public String getSchulzeOption5()
	{
		schulzeRadio5 = "0";
		return schulze.getOptions().get(5);
	}
	public String getSchulzeOption6()
	{
		schulzeRadio6 = "0";
		return schulze.getOptions().get(6);
	}
	public String getSchulzeOption7()
	{
		schulzeRadio7 = "0";
		return schulze.getOptions().get(7);
	}
	public String getSchulzeOption8()
	{
		schulzeRadio8 = "0";
		return schulze.getOptions().get(8);
	}
	public String getSchulzeOption9()
	{
		schulzeRadio9 = "0";
		return schulze.getOptions().get(9);
	}
	public String getSchulzeOption10()
	{
		schulzeRadio10 = "0";
		return schulze.getOptions().get(10);
	}
	public String getSchulzeOption11()
	{
		schulzeRadio11 = "0";
		return schulze.getOptions().get(11);
	}
	public String getSchulzeOption12()
	{
		schulzeRadio12 = "0";
		return schulze.getOptions().get(12);
	}
	public String getSchulzeOption13()
	{
		schulzeRadio13 = "0";
		return schulze.getOptions().get(13);
	}
	public String getSchulzeOption14()
	{
		schulzeRadio14 = "0";
		return schulze.getOptions().get(14);
	}

	public Object onSuccessFromSchulzeForm()
	{
		if(request.isXHR())
		{
			if(showErrorSchulze){
				return this;
			}
			else {
				if(ballot.isPublica())
				{
					ballot=ballotDao.getById(contextBallotId);
					if(ballot!=null && !ballot.isEnded() && !alreadyVote())
					{
						schulzeVote.add(schulzeRadio0);
						System.out.println("El valor en el radio  0 es de : "+Integer.parseInt(schulzeRadio0));
						schulzeVote.add(schulzeRadio1);
						System.out.println("El valor en el radio  1 es de : "+Integer.parseInt(schulzeRadio1));

						if(schulze.getOptions().size()>=3){
							schulzeVote.add(schulzeRadio2);
							System.out.println("El valor en el radio  2 es de : "+Integer.parseInt(schulzeRadio2));
						}
						if(schulze.getOptions().size()>=4 ){
							schulzeVote.add(schulzeRadio3);
						}
						if(schulze.getOptions().size()>=5){
							schulzeVote.add(schulzeRadio4);
						}
						if(schulze.getOptions().size()>=6){
							schulzeVote.add(schulzeRadio5);
						}
						if(schulze.getOptions().size()>=7){
							schulzeVote.add(schulzeRadio6);
						}
						if(schulze.getOptions().size()>=8 ){
							schulzeVote.add(schulzeRadio7);
						}
						if(schulze.getOptions().size()>=9){
							schulzeVote.add(schulzeRadio8);
						}
						if(schulze.getOptions().size()>=10){
							schulzeVote.add(schulzeRadio9);
						}
						if(schulze.getOptions().size()>=11){
							schulzeVote.add(schulzeRadio10);
						}
						if(schulze.getOptions().size()>=12){
							schulzeVote.add(schulzeRadio11);
						}
						if(schulze.getOptions().size()>=13){
							schulzeVote.add(schulzeRadio12);
						}
						if(schulze.getOptions().size()>=14){
							schulzeVote.add(schulzeRadio13);
						}						
						if(schulze.getOptions().size()>=15){
						}	
						schulze.addVote(schulzeVote);
						schulzeDao.update(schulze);
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

						if(Integer.parseInt(schulzeRadio0) > 0){
							schulzeVote.add(schulzeRadio0);
						}
						if(Integer.parseInt(schulzeRadio1) > 0){
							schulzeVote.add(schulzeRadio1);
						}
						if(Integer.parseInt(schulzeRadio2) > 0){
							schulzeVote.add(schulzeRadio2);
						}
						if(Integer.parseInt(schulzeRadio3) > 0){
							schulzeVote.add(schulzeRadio3);
						}
						if(Integer.parseInt(schulzeRadio4) > 0){
							schulzeVote.add(schulzeRadio4);
						}
						if(Integer.parseInt(schulzeRadio5) > 0){
							schulzeVote.add(schulzeRadio5);
						}
						if(Integer.parseInt(schulzeRadio6) > 0){
							schulzeVote.add(schulzeRadio6);
						}
						if(Integer.parseInt(schulzeRadio7) > 0){
							schulzeVote.add(schulzeRadio7);
						}
						if(Integer.parseInt(schulzeRadio8) > 0){
							schulzeVote.add(schulzeRadio8);
						}
						if(Integer.parseInt(schulzeRadio9) > 0){
							schulzeVote.add(schulzeRadio9);
						}
						if(Integer.parseInt(schulzeRadio10) > 0){
							schulzeVote.add(schulzeRadio10);
						}
						if(Integer.parseInt(schulzeRadio11) > 0){
							schulzeVote.add(schulzeRadio11);
						}
						if(Integer.parseInt(schulzeRadio12) > 0){
							schulzeVote.add(schulzeRadio12);
						}
						if(Integer.parseInt(schulzeRadio13) > 0){
							schulzeVote.add(schulzeRadio13);
						}						
						if(Integer.parseInt(schulzeRadio14) > 0){
							schulzeVote.add(schulzeRadio14);
						}
						System.out.println("schulzeVote vale: " +schulzeVote);
						schulze.addVote(schulzeVote);
						schulzeDao.update(schulze);
					}
				}

				contextResultBallotId=contextBallotId;
				componentResources.discardPersistentFieldChanges();
				return VoteCounted.class;
			}
		}
		return VoteCounted.class;


	}

	public boolean isShowSchulze()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.SCHULZE)
		{
			return true;
		}
		return false;

	}	


	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// Small /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Stores the Small vote
	 * @return
	 */


	@Persist
	@Property
	private Small small;

	@Property
	@Persist
	private boolean showErrorSmall;

	@Property
	@Persist
	private String smallRadio0;

	@Property
	@Persist
	private String smallRadio1;

	@Property
	@Persist
	private String smallRadio2;	

	@Property
	@Persist
	private String smallRadio3;

	@Property
	@Persist
	private String smallRadio4;

	@Property
	@Persist
	private String smallRadio5;	

	@Property
	@Persist
	private String smallRadio6;

	@Property
	@Persist
	private String smallRadio7;

	@Property
	@Persist
	private String smallRadio8;	

	@Property
	@Persist
	private String smallRadio9;

	@Property
	@Persist
	private String smallRadio10;

	@Property
	@Persist
	private String smallRadio11;	

	@Property
	@Persist
	private String smallRadio12;

	@Property
	@Persist
	private String smallRadio13;

	@Property
	@Persist
	private String smallRadio14;	

	@Property
	@Persist
	private String smallRadio15;	

	@Property
	@Persist
	private String[] radiobuttonsizeSmall;

	@Property
	@Persist
	private String radioSizeSmall;

	@Persist
	@Property
	private List<String> smallVote;


	public boolean isShowSmall3()
	{
		if(small.getOptions().size()>2)
			return true;
		else
			return false;
	}
	public boolean isShowSmall4()
	{
		if(small.getOptions().size()>3)
			return true;
		else
			return false;
	}
	public boolean isShowSmall5()
	{
		if(small.getOptions().size()>4)
			return true;
		else
			return false;
	}
	public boolean isShowSmall6()
	{
		if(small.getOptions().size()>5)
			return true;
		else
			return false;
	}
	public boolean isShowSmall7()
	{
		if(small.getOptions().size()>6)
			return true;
		else
			return false;
	}
	public boolean isShowSmall8()
	{
		if(small.getOptions().size()>7)
			return true;
		else
			return false;
	}
	public boolean isShowSmall9()
	{
		if(small.getOptions().size()>8)
			return true;
		else
			return false;
	}
	public boolean isShowSmall10()
	{
		if(small.getOptions().size()>9)
			return true;
		else
			return false;
	}
	public boolean isShowSmall11()
	{
		if(small.getOptions().size()>10)
			return true;
		else
			return false;
	}
	public boolean isShowSmall12()
	{
		if(small.getOptions().size()>11)
			return true;
		else
			return false;
	}
	public boolean isShowSmall13()
	{
		if(small.getOptions().size()>12)
			return true;
		else
			return false;
	}
	public boolean isShowSmall14()
	{
		if(small.getOptions().size()>13)
			return true;
		else
			return false;
	}
	public boolean isShowSmall15()
	{
		if(small.getOptions().size()>14)
			return true;
		else
			return false;
	}


	public String getSmallOption0()
	{
		smallRadio0 = "0";
		return small.getOptions().get(0);
	}
	public String getSmallOption1()
	{
		smallRadio1 = "0";
		return small.getOptions().get(1);
	}
	public String getSmallOption2()
	{
		smallRadio2 = "0";
		return small.getOptions().get(2);
	}
	public String getSmallOption3()
	{
		smallRadio3 = "0";
		return small.getOptions().get(3);
	}
	public String getSmallOption4()
	{
		smallRadio4 = "0";
		return small.getOptions().get(4);
	}
	public String getSmallOption5()
	{
		smallRadio5 = "0";
		return small.getOptions().get(5);
	}
	public String getSmallOption6()
	{
		smallRadio6 = "0";
		return small.getOptions().get(6);
	}
	public String getSmallOption7()
	{
		smallRadio7 = "0";
		return small.getOptions().get(7);
	}
	public String getSmallOption8()
	{
		smallRadio8 = "0";
		return small.getOptions().get(8);
	}
	public String getSmallOption9()
	{
		smallRadio9 = "0";
		return small.getOptions().get(9);
	}
	public String getSmallOption10()
	{
		smallRadio10 = "0";
		return small.getOptions().get(10);
	}
	public String getSmallOption11()
	{
		smallRadio11 = "0";
		return small.getOptions().get(11);
	}
	public String getSmallOption12()
	{
		smallRadio12 = "0";
		return small.getOptions().get(12);
	}
	public String getSmallOption13()
	{
		smallRadio13 = "0";
		return small.getOptions().get(13);
	}
	public String getSmallOption14()
	{
		smallRadio14 = "0";
		return small.getOptions().get(14);
	}

	public Object onSuccessFromSmallForm()
	{
		if(request.isXHR())
		{
			if(showErrorSmall){
				return this;
			}
			else {
				if(ballot.isPublica())
				{
					ballot=ballotDao.getById(contextBallotId);
					if(ballot!=null && !ballot.isEnded() && !alreadyVote())
					{
						smallVote.add(smallRadio0);
						System.out.println("El valor en el radio  0 es de : "+Integer.parseInt(smallRadio0));
						smallVote.add(smallRadio1);
						System.out.println("El valor en el radio  1 es de : "+Integer.parseInt(smallRadio1));

						if(small.getOptions().size()>=3){
							smallVote.add(smallRadio2);
							System.out.println("El valor en el radio  2 es de : "+Integer.parseInt(smallRadio2));
						}
						if(small.getOptions().size()>=4 ){
							smallVote.add(smallRadio3);
						}
						if(small.getOptions().size()>=5){
							smallVote.add(smallRadio4);
						}
						if(small.getOptions().size()>=6){
							smallVote.add(smallRadio5);
						}
						if(small.getOptions().size()>=7){
							smallVote.add(smallRadio6);
						}
						if(small.getOptions().size()>=8 ){
							smallVote.add(smallRadio7);
						}
						if(small.getOptions().size()>=9){
							smallVote.add(smallRadio8);
						}
						if(small.getOptions().size()>=10){
							smallVote.add(smallRadio9);
						}
						if(small.getOptions().size()>=11){
							smallVote.add(smallRadio10);
						}
						if(small.getOptions().size()>=12){
							smallVote.add(smallRadio11);
						}
						if(small.getOptions().size()>=13){
							smallVote.add(smallRadio12);
						}
						if(small.getOptions().size()>=14){
							smallVote.add(smallRadio13);
						}						
						if(small.getOptions().size()>=15){
						}	
						small.addVote(smallVote);
						smallDao.update(small);
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

						if(Integer.parseInt(smallRadio0) > 0){
							smallVote.add(smallRadio0);
						}
						if(Integer.parseInt(smallRadio1) > 0){
							smallVote.add(smallRadio1);
						}
						if(Integer.parseInt(smallRadio2) > 0){
							smallVote.add(smallRadio2);
						}
						if(Integer.parseInt(smallRadio3) > 0){
							smallVote.add(smallRadio3);
						}
						if(Integer.parseInt(smallRadio4) > 0){
							smallVote.add(smallRadio4);
						}
						if(Integer.parseInt(smallRadio5) > 0){
							smallVote.add(smallRadio5);
						}
						if(Integer.parseInt(smallRadio6) > 0){
							smallVote.add(smallRadio6);
						}
						if(Integer.parseInt(smallRadio7) > 0){
							smallVote.add(smallRadio7);
						}
						if(Integer.parseInt(smallRadio8) > 0){
							smallVote.add(smallRadio8);
						}
						if(Integer.parseInt(smallRadio9) > 0){
							smallVote.add(smallRadio9);
						}
						if(Integer.parseInt(smallRadio10) > 0){
							smallVote.add(smallRadio10);
						}
						if(Integer.parseInt(smallRadio11) > 0){
							smallVote.add(smallRadio11);
						}
						if(Integer.parseInt(smallRadio12) > 0){
							smallVote.add(smallRadio12);
						}
						if(Integer.parseInt(smallRadio13) > 0){
							smallVote.add(smallRadio13);
						}						
						if(Integer.parseInt(smallRadio14) > 0){
							smallVote.add(smallRadio14);
						}
						small.addVote(smallVote);
						smallDao.update(small);
					}
				}

				contextResultBallotId=contextBallotId;
				componentResources.discardPersistentFieldChanges();
				return VoteCounted.class;
			}
		}
		return VoteCounted.class;


	}

	public boolean isShowSmall()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.SMALL)
		{
			return true;
		}
		return false;

	}	


	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// Dodgson /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Stores the Dodgson vote
	 * @return
	 */


	@Persist
	@Property
	private Dodgson dodgson;

	@Property
	@Persist
	private boolean showErrorDodgson;

	@Property
	@Persist
	private String dodgsonRadio0;

	@Property
	@Persist
	private String dodgsonRadio1;

	@Property
	@Persist
	private String dodgsonRadio2;	

	@Property
	@Persist
	private String dodgsonRadio3;

	@Property
	@Persist
	private String dodgsonRadio4;

	@Property
	@Persist
	private String dodgsonRadio5;	

	@Property
	@Persist
	private String dodgsonRadio6;

	@Property
	@Persist
	private String dodgsonRadio7;

	@Property
	@Persist
	private String dodgsonRadio8;	

	@Property
	@Persist
	private String dodgsonRadio9;

	@Property
	@Persist
	private String dodgsonRadio10;

	@Property
	@Persist
	private String dodgsonRadio11;	

	@Property
	@Persist
	private String dodgsonRadio12;

	@Property
	@Persist
	private String dodgsonRadio13;

	@Property
	@Persist
	private String dodgsonRadio14;	

	@Property
	@Persist
	private String dodgsonRadio15;	

	@Property
	@Persist
	private String[] radiobuttonsizeDodgson;

	@Property
	@Persist
	private String radioSizeDodgson;

	@Persist
	@Property
	private List<String> dodgsonVote;


	public boolean isShowDodgson3()
	{
		if(dodgson.getOptions().size()>2)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson4()
	{
		if(dodgson.getOptions().size()>3)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson5()
	{
		if(dodgson.getOptions().size()>4)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson6()
	{
		if(dodgson.getOptions().size()>5)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson7()
	{
		if(dodgson.getOptions().size()>6)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson8()
	{
		if(dodgson.getOptions().size()>7)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson9()
	{
		if(dodgson.getOptions().size()>8)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson10()
	{
		if(dodgson.getOptions().size()>9)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson11()
	{
		if(dodgson.getOptions().size()>10)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson12()
	{
		if(dodgson.getOptions().size()>11)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson13()
	{
		if(dodgson.getOptions().size()>12)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson14()
	{
		if(dodgson.getOptions().size()>13)
			return true;
		else
			return false;
	}
	public boolean isShowDodgson15()
	{
		if(dodgson.getOptions().size()>14)
			return true;
		else
			return false;
	}


	public String getDodgsonOption0()
	{
		dodgsonRadio0 = "0";
		return dodgson.getOptions().get(0);
	}
	public String getDodgsonOption1()
	{
		dodgsonRadio1 = "0";
		return dodgson.getOptions().get(1);
	}
	public String getDodgsonOption2()
	{
		dodgsonRadio2 = "0";
		return dodgson.getOptions().get(2);
	}
	public String getDodgsonOption3()
	{
		dodgsonRadio3 = "0";
		return dodgson.getOptions().get(3);
	}
	public String getDodgsonOption4()
	{
		dodgsonRadio4 = "0";
		return dodgson.getOptions().get(4);
	}
	public String getDodgsonOption5()
	{
		dodgsonRadio5 = "0";
		return dodgson.getOptions().get(5);
	}
	public String getDodgsonOption6()
	{
		dodgsonRadio6 = "0";
		return dodgson.getOptions().get(6);
	}
	public String getDodgsonOption7()
	{
		dodgsonRadio7 = "0";
		return dodgson.getOptions().get(7);
	}
	public String getDodgsonOption8()
	{
		dodgsonRadio8 = "0";
		return dodgson.getOptions().get(8);
	}
	public String getDodgsonOption9()
	{
		dodgsonRadio9 = "0";
		return dodgson.getOptions().get(9);
	}
	public String getDodgsonOption10()
	{
		dodgsonRadio10 = "0";
		return dodgson.getOptions().get(10);
	}
	public String getDodgsonOption11()
	{
		dodgsonRadio11 = "0";
		return dodgson.getOptions().get(11);
	}
	public String getDodgsonOption12()
	{
		dodgsonRadio12 = "0";
		return dodgson.getOptions().get(12);
	}
	public String getDodgsonOption13()
	{
		dodgsonRadio13 = "0";
		return dodgson.getOptions().get(13);
	}
	public String getDodgsonOption14()
	{
		dodgsonRadio14 = "0";
		return dodgson.getOptions().get(14);
	}

	public Object onSuccessFromDodgsonForm()
	{
		if(request.isXHR())
		{
			if(showErrorDodgson){
				return this;
			}
			else {
				if(ballot.isPublica())
				{
					ballot=ballotDao.getById(contextBallotId);
					if(ballot!=null && !ballot.isEnded() && !alreadyVote())
					{
						dodgsonVote.add(dodgsonRadio0);
						System.out.println("El valor en el radio  0 es de : "+Integer.parseInt(dodgsonRadio0));
						dodgsonVote.add(dodgsonRadio1);
						System.out.println("El valor en el radio  1 es de : "+Integer.parseInt(dodgsonRadio1));

						if(dodgson.getOptions().size()==3){
							dodgsonVote.add(dodgsonRadio2);
							System.out.println("El valor en el radio  2 es de : "+Integer.parseInt(dodgsonRadio2));
						}
						if(dodgson.getOptions().size()>=4 ){
							dodgsonVote.add(dodgsonRadio3);
						}
						if(dodgson.getOptions().size()>=5){
							dodgsonVote.add(dodgsonRadio4);
						}
						if(dodgson.getOptions().size()>=6){
							dodgsonVote.add(dodgsonRadio5);
						}
						if(dodgson.getOptions().size()>=7){
							dodgsonVote.add(dodgsonRadio6);
						}
						if(dodgson.getOptions().size()>=8 ){
							dodgsonVote.add(dodgsonRadio7);
						}
						if(dodgson.getOptions().size()>=9){
							dodgsonVote.add(dodgsonRadio8);
						}
						if(dodgson.getOptions().size()>=10){
							dodgsonVote.add(dodgsonRadio9);
						}
						if(dodgson.getOptions().size()>=11){
							dodgsonVote.add(dodgsonRadio10);
						}
						if(dodgson.getOptions().size()>=12){
							dodgsonVote.add(dodgsonRadio11);
						}
						if(dodgson.getOptions().size()>=13){
							dodgsonVote.add(dodgsonRadio12);
						}
						if(dodgson.getOptions().size()>=14){
							dodgsonVote.add(dodgsonRadio13);
						}						
						if(dodgson.getOptions().size()>=15){
						}	
						dodgson.addVote(dodgsonVote);
						dodgsonDao.update(dodgson);
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

						if(Integer.parseInt(dodgsonRadio0) > 0){
							dodgsonVote.add(dodgsonRadio0);
						}
						if(Integer.parseInt(dodgsonRadio1) > 0){
							dodgsonVote.add(dodgsonRadio1);
						}
						if(Integer.parseInt(dodgsonRadio2) > 0){
							dodgsonVote.add(dodgsonRadio2);
						}
						if(Integer.parseInt(dodgsonRadio3) > 0){
							dodgsonVote.add(dodgsonRadio3);
						}
						if(Integer.parseInt(dodgsonRadio4) > 0){
							dodgsonVote.add(dodgsonRadio4);
						}
						if(Integer.parseInt(dodgsonRadio5) > 0){
							dodgsonVote.add(dodgsonRadio5);
						}
						if(Integer.parseInt(dodgsonRadio6) > 0){
							dodgsonVote.add(dodgsonRadio6);
						}
						if(Integer.parseInt(dodgsonRadio7) > 0){
							dodgsonVote.add(dodgsonRadio7);
						}
						if(Integer.parseInt(dodgsonRadio8) > 0){
							dodgsonVote.add(dodgsonRadio8);
						}
						if(Integer.parseInt(dodgsonRadio9) > 0){
							dodgsonVote.add(dodgsonRadio9);
						}
						if(Integer.parseInt(dodgsonRadio10) > 0){
							dodgsonVote.add(dodgsonRadio10);
						}
						if(Integer.parseInt(dodgsonRadio11) > 0){
							dodgsonVote.add(dodgsonRadio11);
						}
						if(Integer.parseInt(dodgsonRadio12) > 0){
							dodgsonVote.add(dodgsonRadio12);
						}
						if(Integer.parseInt(dodgsonRadio13) > 0){
							dodgsonVote.add(dodgsonRadio13);
						}						
						if(Integer.parseInt(dodgsonRadio14) > 0){
							dodgsonVote.add(dodgsonRadio14);
						}
						System.out.println("dodgsonVote vale: " +dodgsonVote);
						dodgson.addVote(dodgsonVote);
						dodgsonDao.update(dodgson);
					}
				}

				contextResultBallotId=contextBallotId;
				componentResources.discardPersistentFieldChanges();
				return VoteCounted.class;
			}
		}
		return VoteCounted.class;


	}

	public boolean isShowDodgson()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.DODGSON)
		{
			return true;
		}
		return false;

	}	



	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////// Black /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Stores the Black vote
	 * @return
	 */


	@Persist
	@Property
	private Black black;

	@Property
	@Persist
	private boolean showErrorBlack;

	@Property
	@Persist
	private String blackRadio0;

	@Property
	@Persist
	private String blackRadio1;

	@Property
	@Persist
	private String blackRadio2;	

	@Property
	@Persist
	private String blackRadio3;

	@Property
	@Persist
	private String blackRadio4;

	@Property
	@Persist
	private String blackRadio5;	

	@Property
	@Persist
	private String blackRadio6;

	@Property
	@Persist
	private String blackRadio7;

	@Property
	@Persist
	private String blackRadio8;	

	@Property
	@Persist
	private String blackRadio9;

	@Property
	@Persist
	private String blackRadio10;

	@Property
	@Persist
	private String blackRadio11;	

	@Property
	@Persist
	private String blackRadio12;

	@Property
	@Persist
	private String blackRadio13;

	@Property
	@Persist
	private String blackRadio14;	

	@Property
	@Persist
	private String blackRadio15;	

	@Property
	@Persist
	private String[] radiobuttonsizeBlack;

	@Property
	@Persist
	private String radioSizeBlack;

	@Persist
	@Property
	private List<String> blackVote;


	public boolean isShowBlack3()
	{
		if(black.getOptions().size()>3)
			return true;
		else
			return false;
	}
	public boolean isShowBlack4()
	{
		if(black.getOptions().size()>4)
			return true;
		else
			return false;
	}
	public boolean isShowBlack5()
	{
		if(black.getOptions().size()>=5)
			return true;
		else
			return false;
	}
	public boolean isShowBlack6()
	{
		if(black.getOptions().size()>=6)
			return true;
		else
			return false;
	}
	public boolean isShowBlack7()
	{
		if(black.getOptions().size()>=7)
			return true;
		else
			return false;
	}
	public boolean isShowBlack8()
	{
		if(black.getOptions().size()>=8)
			return true;
		else
			return false;
	}
	public boolean isShowBlack9()
	{
		if(black.getOptions().size()>=9)
			return true;
		else
			return false;
	}
	public boolean isShowBlack10()
	{
		if(black.getOptions().size()>=10)
			return true;
		else
			return false;
	}
	public boolean isShowBlack11()
	{
		if(black.getOptions().size()>=11)
			return true;
		else
			return false;
	}
	public boolean isShowBlack12()
	{
		if(black.getOptions().size()>=12)
			return true;
		else
			return false;
	}
	public boolean isShowBlack13()
	{
		if(black.getOptions().size()>=13)
			return true;
		else
			return false;
	}
	public boolean isShowBlack14()
	{
		if(black.getOptions().size()>=14)
			return true;
		else
			return false;
	}
	public boolean isShowBlack15()
	{
		if(black.getOptions().size()>=15)
			return true;
		else
			return false;
	}


	public String getBlackOption0()
	{
		blackRadio0 = "0";
		return black.getOptions().get(0);
	}
	public String getBlackOption1()
	{
		blackRadio1 = "0";
		return black.getOptions().get(1);
	}
	public String getBlackOption2()
	{
		blackRadio2 = "0";
		return black.getOptions().get(2);
	}
	public String getBlackOption3()
	{
		blackRadio3 = "0";
		return black.getOptions().get(3);
	}
	public String getBlackOption4()
	{
		blackRadio4 = "0";
		return black.getOptions().get(4);
	}
	public String getBlackOption5()
	{
		blackRadio5 = "0";
		return black.getOptions().get(5);
	}
	public String getBlackOption6()
	{
		blackRadio6 = "0";
		return black.getOptions().get(6);
	}
	public String getBlackOption7()
	{
		blackRadio7 = "0";
		return black.getOptions().get(7);
	}
	public String getBlackOption8()
	{
		blackRadio8 = "0";
		return black.getOptions().get(8);
	}
	public String getBlackOption9()
	{
		blackRadio9 = "0";
		return black.getOptions().get(9);
	}
	public String getBlackOption10()
	{
		blackRadio10 = "0";
		return black.getOptions().get(10);
	}
	public String getBlackOption11()
	{
		blackRadio11 = "0";
		return black.getOptions().get(11);
	}
	public String getBlackOption12()
	{
		blackRadio12 = "0";
		return black.getOptions().get(12);
	}
	public String getBlackOption13()
	{
		blackRadio13 = "0";
		return black.getOptions().get(13);
	}
	public String getBlackOption14()
	{
		blackRadio14 = "0";
		return black.getOptions().get(14);
	}

	public Object onSuccessFromBlackForm()
	{
		if(request.isXHR())
		{
			if(showErrorBlack){
				return this;
			}
			else {
				if(ballot.isPublica())
				{
					ballot=ballotDao.getById(contextBallotId);
					if(ballot!=null && !ballot.isEnded() && !alreadyVote())
					{
						blackVote.add(blackRadio0);
						System.out.println("El valor en el radio  0 es de : "+Integer.parseInt(blackRadio0));
						blackVote.add(blackRadio1);
						System.out.println("El valor en el radio  1 es de : "+Integer.parseInt(blackRadio1));

						if(black.getOptions().size()>=3){
							blackVote.add(blackRadio2);
							System.out.println("El valor en el radio  2 es de : "+Integer.parseInt(blackRadio2));
						}
						if(black.getOptions().size()>=4 ){
							blackVote.add(blackRadio3);
						}
						if(black.getOptions().size()>=5){
							blackVote.add(blackRadio4);
						}
						if(black.getOptions().size()>=6){
							blackVote.add(blackRadio5);
						}
						if(black.getOptions().size()>=7){
							blackVote.add(blackRadio6);
						}
						if(black.getOptions().size()>=8 ){
							blackVote.add(blackRadio7);
						}
						if(black.getOptions().size()>=9){
							blackVote.add(blackRadio8);
						}
						if(black.getOptions().size()>=10){
							blackVote.add(blackRadio9);
						}
						if(black.getOptions().size()>=11){
							blackVote.add(blackRadio10);
						}
						if(black.getOptions().size()>=12){
							blackVote.add(blackRadio11);
						}
						if(black.getOptions().size()>=13){
							blackVote.add(blackRadio12);
						}
						if(black.getOptions().size()>=14){
							blackVote.add(blackRadio13);
						}						
						if(black.getOptions().size()>=15){
							blackVote.add(blackRadio14);
						}	
						black.addVote(blackVote);
						blackDao.update(black);
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

						blackVote.add(blackRadio0);
						blackVote.add(blackRadio1);
						if(black.getOptions().size()==3){
							blackVote.add(blackRadio2);
							System.out.println("El valor en el radio  2 es de : "+Integer.parseInt(blackRadio2));
						}
						if(black.getOptions().size()==4 ){
							blackVote.add(blackRadio3);
						}
						if(black.getOptions().size()==5){
							blackVote.add(blackRadio4);
						}
						if(black.getOptions().size()==6){
							blackVote.add(blackRadio5);
						}
						if(black.getOptions().size()==7){
							blackVote.add(blackRadio6);
						}
						if(black.getOptions().size()==8 ){
							blackVote.add(blackRadio7);
						}
						if(black.getOptions().size()==9){
							blackVote.add(blackRadio8);
						}
						if(black.getOptions().size()==10){
							blackVote.add(blackRadio9);
						}
						if(black.getOptions().size()==11){
							blackVote.add(blackRadio10);
						}
						if(black.getOptions().size()==12){
							blackVote.add(blackRadio11);
						}
						if(black.getOptions().size()==13){
							blackVote.add(blackRadio12);
						}
						if(black.getOptions().size()==14){
							blackVote.add(blackRadio13);
						}						
						if(black.getOptions().size()==15){
							blackVote.add(blackRadio14);
						}	
						System.out.println("blackVote vale: " +blackVote);
						black.addVote(blackVote);
						blackDao.update(black);
					}
				}

				contextResultBallotId=contextBallotId;
				componentResources.discardPersistentFieldChanges();
				return VoteCounted.class;
			}
		}
		return VoteCounted.class;


	}

	public boolean isShowBlack()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.BLACK)
		{
			return true;
		}
		return false;

	}	

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////Mejor-Peor /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Stores the MejorPeor vote
	 * @return
	 */


	@Persist
	@Property
	private MejorPeor mejorPeor;

	@Property
	@Persist
	private boolean showErrorMejorPeor;

	@Property
	@Persist
	private String mejorPeorRadio0;

	@Property
	@Persist
	private String mejorPeorRadio1;

	@Property
	@Persist
	private String mejorPeorRadio2;	

	@Property
	@Persist
	private String mejorPeorRadio3;

	@Property
	@Persist
	private String mejorPeorRadio4;

	@Property
	@Persist
	private String mejorPeorRadio5;	

	@Property
	@Persist
	private String mejorPeorRadio6;

	@Property
	@Persist
	private String mejorPeorRadio7;

	@Property
	@Persist
	private String mejorPeorRadio8;	

	@Property
	@Persist
	private String mejorPeorRadio9;

	@Property
	@Persist
	private String mejorPeorRadio10;

	@Property
	@Persist
	private String mejorPeorRadio11;	

	@Property
	@Persist
	private String mejorPeorRadio12;

	@Property
	@Persist
	private String mejorPeorRadio13;

	@Property
	@Persist
	private String mejorPeorRadio14;	

	@Property
	@Persist
	private String mejorPeorRadio15;	


	public boolean isShowMejorPeor8()
	{
		if(mejorPeor.getOptions().size()>=8)
			return true;
		else
			return false;
	}
	public boolean isShowMejorPeor9()
	{
		if(mejorPeor.getOptions().size()>=9)
			return true;
		else
			return false;
	}
	public boolean isShowMejorPeor10()
	{
		if(mejorPeor.getOptions().size()>=10)
			return true;
		else
			return false;
	}
	public boolean isShowMejorPeor11()
	{
		if(mejorPeor.getOptions().size()>=11)
			return true;
		else
			return false;
	}
	public boolean isShowMejorPeor12()
	{
		if(mejorPeor.getOptions().size()>=12)
			return true;
		else
			return false;
	}
	public boolean isShowMejorPeor13()
	{
		if(mejorPeor.getOptions().size()>=13)
			return true;
		else
			return false;
	}
	public boolean isShowMejorPeor14()
	{
		if(mejorPeor.getOptions().size()>=14)
			return true;
		else
			return false;
	}
	public boolean isShowMejorPeor15()
	{
		if(mejorPeor.getOptions().size()>=15)
			return true;
		else
			return false;
	}


	public String getMejorPeorOption0()
	{
		mejorPeorRadio0 = "0";
		return mejorPeor.getOptions().get(0);
	}
	public String getMejorPeorOption1()
	{
		mejorPeorRadio1 = "0";
		return mejorPeor.getOptions().get(1);
	}
	public String getMejorPeorOption2()
	{
		mejorPeorRadio2 = "0";
		return mejorPeor.getOptions().get(2);
	}
	public String getMejorPeorOption3()
	{
		mejorPeorRadio3 = "0";
		return mejorPeor.getOptions().get(3);
	}
	public String getMejorPeorOption4()
	{
		mejorPeorRadio4 = "0";
		return mejorPeor.getOptions().get(4);
	}
	public String getMejorPeorOption5()
	{
		mejorPeorRadio5 = "0";
		return mejorPeor.getOptions().get(5);
	}
	public String getMejorPeorOption6()
	{
		mejorPeorRadio6 = "0";
		return mejorPeor.getOptions().get(6);
	}
	public String getMejorPeorOption7()
	{
		mejorPeorRadio7 = "0";
		return mejorPeor.getOptions().get(7);
	}
	public String getMejorPeorOption8()
	{
		mejorPeorRadio8 = "0";
		return mejorPeor.getOptions().get(8);
	}
	public String getMejorPeorOption9()
	{
		mejorPeorRadio9 = "0";
		return mejorPeor.getOptions().get(9);
	}
	public String getMejorPeorOption10()
	{
		mejorPeorRadio10 = "0";
		return mejorPeor.getOptions().get(10);
	}
	public String getMejorPeorOption11()
	{
		mejorPeorRadio11 = "0";
		return mejorPeor.getOptions().get(11);
	}
	public String getMejorPeorOption12()
	{
		mejorPeorRadio12 = "0";
		return mejorPeor.getOptions().get(12);
	}
	public String getMejorPeorOption13()
	{
		mejorPeorRadio13 = "0";
		return mejorPeor.getOptions().get(13);
	}
	public String getMejorPeorOption14()
	{
		mejorPeorRadio14 = "0";
		return mejorPeor.getOptions().get(14);
	}

	public Object onSuccessFromMejorPeorForm()
	{
		if(request.isXHR())
		{
			if(mejorPeor.getOptions().size()==7){
				if((Integer.parseInt(mejorPeorRadio0) + Integer.parseInt(mejorPeorRadio1) + Integer.parseInt(mejorPeorRadio2)+ Integer.parseInt(mejorPeorRadio3)+ Integer.parseInt(mejorPeorRadio4)+ Integer.parseInt(mejorPeorRadio5)+ Integer.parseInt(mejorPeorRadio6))== 3)
				{showErrorMejorPeor=false;}
				else {showErrorMejorPeor=true;}
			}
			else if(mejorPeor.getOptions().size()==8){
				if((Integer.parseInt(mejorPeorRadio0) + Integer.parseInt(mejorPeorRadio1) + Integer.parseInt(mejorPeorRadio2)+ Integer.parseInt(mejorPeorRadio3)+ Integer.parseInt(mejorPeorRadio4)+ Integer.parseInt(mejorPeorRadio5)+ Integer.parseInt(mejorPeorRadio6)+ Integer.parseInt(mejorPeorRadio7))== 3)
				{showErrorMejorPeor=false;}
				else {showErrorMejorPeor=true;}
			}
			else if(mejorPeor.getOptions().size()==9){
				if((Integer.parseInt(mejorPeorRadio0) + Integer.parseInt(mejorPeorRadio1) + Integer.parseInt(mejorPeorRadio2)+ Integer.parseInt(mejorPeorRadio3)+ Integer.parseInt(mejorPeorRadio4)+ Integer.parseInt(mejorPeorRadio5)+ Integer.parseInt(mejorPeorRadio6)+ Integer.parseInt(mejorPeorRadio7)+ Integer.parseInt(mejorPeorRadio8))== 3)
				{showErrorMejorPeor=false;}
				else {showErrorMejorPeor=true;}
			}
			else if(mejorPeor.getOptions().size()==10){
				if((Integer.parseInt(mejorPeorRadio0) + Integer.parseInt(mejorPeorRadio1) + Integer.parseInt(mejorPeorRadio2)+ Integer.parseInt(mejorPeorRadio3)+ Integer.parseInt(mejorPeorRadio4)+ Integer.parseInt(mejorPeorRadio5)+ Integer.parseInt(mejorPeorRadio6)+ Integer.parseInt(mejorPeorRadio7)+ Integer.parseInt(mejorPeorRadio8)+ Integer.parseInt(mejorPeorRadio9))== 3)
				{showErrorMejorPeor=false;}
				else {showErrorMejorPeor=true;}
			}
			else if(mejorPeor.getOptions().size()==11){
				if((Integer.parseInt(mejorPeorRadio0) + Integer.parseInt(mejorPeorRadio1) + Integer.parseInt(mejorPeorRadio2)+ Integer.parseInt(mejorPeorRadio3)+ Integer.parseInt(mejorPeorRadio4)+ Integer.parseInt(mejorPeorRadio5)+ Integer.parseInt(mejorPeorRadio6)+ Integer.parseInt(mejorPeorRadio7)+ Integer.parseInt(mejorPeorRadio8)+ Integer.parseInt(mejorPeorRadio9)+ Integer.parseInt(mejorPeorRadio10))== 3)
				{showErrorMejorPeor=false;}
				else {showErrorMejorPeor=true;}
			}
			else if(mejorPeor.getOptions().size()==12){
				if((Integer.parseInt(mejorPeorRadio0) + Integer.parseInt(mejorPeorRadio1) + Integer.parseInt(mejorPeorRadio2)+ Integer.parseInt(mejorPeorRadio3)+ Integer.parseInt(mejorPeorRadio4)+ Integer.parseInt(mejorPeorRadio5)+ Integer.parseInt(mejorPeorRadio6)+ Integer.parseInt(mejorPeorRadio7)+ Integer.parseInt(mejorPeorRadio8)+ Integer.parseInt(mejorPeorRadio9)+ Integer.parseInt(mejorPeorRadio10)+ Integer.parseInt(mejorPeorRadio11))== 3)
				{showErrorMejorPeor=false;}
				else {showErrorMejorPeor=true;}
			}
			else if(mejorPeor.getOptions().size()==13){
				if((Integer.parseInt(mejorPeorRadio0) + Integer.parseInt(mejorPeorRadio1) + Integer.parseInt(mejorPeorRadio2)+ Integer.parseInt(mejorPeorRadio3)+ Integer.parseInt(mejorPeorRadio4)+ Integer.parseInt(mejorPeorRadio5)+ Integer.parseInt(mejorPeorRadio6)+ Integer.parseInt(mejorPeorRadio7)+ Integer.parseInt(mejorPeorRadio8)+ Integer.parseInt(mejorPeorRadio9)+ Integer.parseInt(mejorPeorRadio10)+ Integer.parseInt(mejorPeorRadio11)+ Integer.parseInt(mejorPeorRadio12))== 3)
				{showErrorMejorPeor=false;}
				else {showErrorMejorPeor=true;}
			}
			else if(mejorPeor.getOptions().size()==14){
				if((Integer.parseInt(mejorPeorRadio0) + Integer.parseInt(mejorPeorRadio1) + Integer.parseInt(mejorPeorRadio2)+ Integer.parseInt(mejorPeorRadio3)+ Integer.parseInt(mejorPeorRadio4)+ Integer.parseInt(mejorPeorRadio5)+ Integer.parseInt(mejorPeorRadio6)+ Integer.parseInt(mejorPeorRadio7)+ Integer.parseInt(mejorPeorRadio8)+ Integer.parseInt(mejorPeorRadio9)+ Integer.parseInt(mejorPeorRadio10)+ Integer.parseInt(mejorPeorRadio11)+ Integer.parseInt(mejorPeorRadio12)+ Integer.parseInt(mejorPeorRadio13))== 3)
				{showErrorMejorPeor=false;}
				else {showErrorMejorPeor=true;}
			}		
			else if(mejorPeor.getOptions().size()==15){
				if((Integer.parseInt(mejorPeorRadio0) + Integer.parseInt(mejorPeorRadio1) + Integer.parseInt(mejorPeorRadio2)+ Integer.parseInt(mejorPeorRadio3)+ Integer.parseInt(mejorPeorRadio4)+ Integer.parseInt(mejorPeorRadio5)+ Integer.parseInt(mejorPeorRadio6)+ Integer.parseInt(mejorPeorRadio7)+ Integer.parseInt(mejorPeorRadio8)+ Integer.parseInt(mejorPeorRadio9)+ Integer.parseInt(mejorPeorRadio10)+ Integer.parseInt(mejorPeorRadio11)+ Integer.parseInt(mejorPeorRadio12)+ Integer.parseInt(mejorPeorRadio13)+ Integer.parseInt(mejorPeorRadio14))== 3)
				{showErrorMejorPeor=false;}
				else {showErrorMejorPeor=true;}
			}	

			if(showErrorMejorPeor){
				return this;
			}
			else {
				if(ballot.isPublica())
				{
					ballot=ballotDao.getById(contextBallotId);
					if(ballot!=null && !ballot.isEnded() && !alreadyVote())
					{
						if(Integer.parseInt(mejorPeorRadio0)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(0));
						}
						else if(Integer.parseInt(mejorPeorRadio0)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(0));
						}

						if(Integer.parseInt(mejorPeorRadio1)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(1));
						}
						else if(Integer.parseInt(mejorPeorRadio1)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(1));
						}

						if(Integer.parseInt(mejorPeorRadio2)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(2));
						}
						else if(Integer.parseInt(mejorPeorRadio2)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(2));
						}
						if(Integer.parseInt(mejorPeorRadio3)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(3));
						}
						else if(Integer.parseInt(mejorPeorRadio3)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(3));
						}
						if(Integer.parseInt(mejorPeorRadio4)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(4));
						}
						else if(Integer.parseInt(mejorPeorRadio4)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(4));
						}
						if(Integer.parseInt(mejorPeorRadio5)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(5));
						}
						else if(Integer.parseInt(mejorPeorRadio5)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(5));
						}
						if(Integer.parseInt(mejorPeorRadio6)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(6));
						}
						else if(Integer.parseInt(mejorPeorRadio6)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(6));
						}

						if(!(mejorPeorRadio7 == null)){
							if(Integer.parseInt(mejorPeorRadio7)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(7));
							}
							else if(Integer.parseInt(mejorPeorRadio7)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(7));
							}
						}
						if(!(mejorPeorRadio8 == null)){
							if(Integer.parseInt(mejorPeorRadio8)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(8));
							}
							else if(Integer.parseInt(mejorPeorRadio8)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(8));
							}
						}
						if(!(mejorPeorRadio9 == null)){
							if(Integer.parseInt(mejorPeorRadio8)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(8));
							}
							else if(Integer.parseInt(mejorPeorRadio8)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(8));
							}
						}
						if(!(mejorPeorRadio10 == null)){
							if(Integer.parseInt(mejorPeorRadio10)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(10));
							}
							else if(Integer.parseInt(mejorPeorRadio10)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(10));
							}							
						}

						if(!(mejorPeorRadio11 == null)){
							if(Integer.parseInt(mejorPeorRadio11)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(11));
							}
							else if(Integer.parseInt(mejorPeorRadio11)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(11));
							}
						}
						if(!(mejorPeorRadio12 == null)){
							if(Integer.parseInt(mejorPeorRadio12)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(12));
							}
							else if(Integer.parseInt(mejorPeorRadio12)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(12));
							}
						}
						if(!(mejorPeorRadio13 == null)){
							if(Integer.parseInt(mejorPeorRadio13)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(13));
							}
							else if(Integer.parseInt(mejorPeorRadio13)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(13));
							}
						}
						if(!(mejorPeorRadio14 == null)){
							if(Integer.parseInt(mejorPeorRadio14)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(14));
							}
							else if(Integer.parseInt(mejorPeorRadio14)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(14));
							}
						}

					}
					mejorPeorDao.update(mejorPeor);
					addPublicVote();
				}

				else
				{

					vote=voteDao.getVoteByIds(contextBallotId, datasession.getId());
					ballot=ballotDao.getById(contextBallotId);

					if(ballot!=null && !ballot.isEnded() && !vote.isCounted())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
					{
						vote.setCounted(true);
						voteDao.updateVote(vote);
						if(Integer.parseInt(mejorPeorRadio0)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(0));
						}
						else if(Integer.parseInt(mejorPeorRadio0)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(0));
						}

						if(Integer.parseInt(mejorPeorRadio1)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(1));
						}
						else if(Integer.parseInt(mejorPeorRadio1)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(1));
						}

						if(Integer.parseInt(mejorPeorRadio2)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(2));
						}
						else if(Integer.parseInt(mejorPeorRadio2)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(2));
						}
						if(Integer.parseInt(mejorPeorRadio3)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(3));
						}
						else if(Integer.parseInt(mejorPeorRadio3)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(3));
						}
						if(Integer.parseInt(mejorPeorRadio4)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(4));
						}
						else if(Integer.parseInt(mejorPeorRadio4)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(4));
						}
						if(Integer.parseInt(mejorPeorRadio5)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(5));
						}
						else if(Integer.parseInt(mejorPeorRadio5)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(5));
						}
						if(Integer.parseInt(mejorPeorRadio6)==2){
							mejorPeor.addVotePos(mejorPeor.getOptions().get(6));
						}
						else if(Integer.parseInt(mejorPeorRadio6)==1){
							mejorPeor.addVoteNeg(mejorPeor.getOptions().get(6));
						}

						if(!(mejorPeorRadio7 == null)){
							if(Integer.parseInt(mejorPeorRadio7)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(7));
							}
							else if(Integer.parseInt(mejorPeorRadio7)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(7));
							}
						}
						if(!(mejorPeorRadio8 == null)){
							if(Integer.parseInt(mejorPeorRadio8)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(8));
							}
							else if(Integer.parseInt(mejorPeorRadio8)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(8));
							}
						}
						if(!(mejorPeorRadio9 == null)){
							if(Integer.parseInt(mejorPeorRadio8)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(8));
							}
							else if(Integer.parseInt(mejorPeorRadio8)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(8));
							}
						}
						if(!(mejorPeorRadio10 == null)){
							if(Integer.parseInt(mejorPeorRadio10)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(10));
							}
							else if(Integer.parseInt(mejorPeorRadio10)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(10));
							}							
						}

						if(!(mejorPeorRadio11 == null)){
							if(Integer.parseInt(mejorPeorRadio11)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(11));
							}
							else if(Integer.parseInt(mejorPeorRadio11)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(11));
							}
						}
						if(!(mejorPeorRadio12 == null)){
							if(Integer.parseInt(mejorPeorRadio12)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(12));
							}
							else if(Integer.parseInt(mejorPeorRadio12)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(12));
							}
						}
						if(!(mejorPeorRadio13 == null)){
							if(Integer.parseInt(mejorPeorRadio13)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(13));
							}
							else if(Integer.parseInt(mejorPeorRadio13)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(13));
							}
						}
						if(!(mejorPeorRadio14 == null)){
							if(Integer.parseInt(mejorPeorRadio14)==2){
								mejorPeor.addVotePos(mejorPeor.getOptions().get(14));
							}
							else if(Integer.parseInt(mejorPeorRadio14)==1){
								mejorPeor.addVoteNeg(mejorPeor.getOptions().get(14));
							}
						}

						mejorPeorDao.update(mejorPeor);
					}
				}

				contextResultBallotId=contextBallotId;
				componentResources.discardPersistentFieldChanges();
				return VoteCounted.class;
			}
		}
		return VoteCounted.class;


	}

	public boolean isShowMejorPeor()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.MEJOR_PEOR)
		{
			return true;
		}
		return false;

	}		

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////// BUCKLIN /////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectComponent
	private Zone bucklinZone;

	@Property
	@Persist
	private Bucklin bucklin;

	@Property
	@Persist
	private boolean showErrorBucklin;

	@Persist
	@Property
	private List<String> bucklinVote;


	public Object onSuccessFromBucklinForm()
	{
		if(request.isXHR())
		{

			if(bucklinVote.size()<bucklin.getOptions().size())
			{
				showErrorBucklin=true;
				return this;
			}
			else{
				showErrorBucklin=false;
			}
			if(ballot.isPublica())
			{
				ballot=ballotDao.getById(contextBallotId);
				if(ballot!=null && !ballot.isEnded()&& !alreadyVote())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
				{
					bucklin.addVote(bucklinVote);
					bucklinDao.update(bucklin);
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
					bucklin.addVote(bucklinVote);
					bucklinDao.update(bucklin);
				}
			}
		}

		contextResultBallotId=contextBallotId;
		return VoteCounted.class;
	}
	public boolean isShowBucklin()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.BUCKLIN)
		{
			return true;
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////// Nanson /////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectComponent
	private Zone nansonZone;

	@Property
	@Persist
	private Nanson nanson;

	@Property
	@Persist
	private boolean showErrorNanson;

	@Persist
	@Property
	private List<String> nansonVote;


	public Object onSuccessFromNansonForm()
	{
		if(request.isXHR())
		{
			showErrorNanson=false;
			if(nansonVote.size()<nanson.getOptions().size())
			{
				showErrorNanson=true;
				return this;
			}
			else{
				showErrorNanson=false;
			}
			if(ballot.isPublica())
			{
				ballot=ballotDao.getById(contextBallotId);
				if(ballot!=null && !ballot.isEnded()&& !alreadyVote())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
				{
					nanson.addVote(nansonVote);
					nansonDao.update(nanson);
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
					nanson.addVote(nansonVote);
					nansonDao.update(nanson);
				}
			}
		}

		contextResultBallotId=contextBallotId;
		return VoteCounted.class;
	}
	public boolean isShowNanson()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.NANSON)
		{
			return true;
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////// Hare /////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectComponent
	private Zone hareZone;

	@Property
	@Persist
	private Hare hare;

	@Property
	@Persist
	private boolean showErrorHare;

	@Persist
	@Property
	private List<String> hareVote;


	public Object onSuccessFromHareForm()
	{
		if(request.isXHR())
		{
			showErrorHare=false;
			if(hareVote.size()<hare.getOptions().size())
			{
				showErrorHare=true;
				return this;
			}
			else{
				showErrorHare=false;
			}
			if(ballot.isPublica())
			{
				ballot=ballotDao.getById(contextBallotId);
				if(ballot!=null && !ballot.isEnded()&& !alreadyVote())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
				{
					hare.addVote(hareVote);
					hareDao.update(hare);
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
					hare.addVote(hareVote);
					hareDao.update(hare);
				}
			}
		}

		contextResultBallotId=contextBallotId;
		return VoteCounted.class;
	}
	public boolean isShowHare()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.HARE)
		{
			return true;
		}
		return false;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////// Coombs /////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectComponent
	private Zone coombsZone;

	@Property
	@Persist
	private Coombs coombs;

	@Property
	@Persist
	private boolean showErrorCoombs;

	@Persist
	@Property
	private List<String> coombsVote;


	public Object onSuccessFromCoombsForm()
	{
		if(request.isXHR())
		{
			showErrorCoombs=false;
			if(coombsVote.size()<coombs.getOptions().size())
			{
				showErrorCoombs=true;
				return this;
			}
			else{
				showErrorCoombs=false;
			}
			if(ballot.isPublica())
			{
				ballot=ballotDao.getById(contextBallotId);
				if(ballot!=null && !ballot.isEnded()&& !alreadyVote())//comprueba si la votacion existe,si no ha terminado y si no ha votado el usuario
				{
					coombs.addVote(coombsVote);
					coombsDao.update(coombs);
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
					coombs.addVote(coombsVote);
					coombsDao.update(coombs);
				}
			}
		}

		contextResultBallotId=contextBallotId;
		return VoteCounted.class;
	}
	public boolean isShowCoombs()
	{
		if(ballot==null)
		{
			return false;
		}
		if(ballot.getMethod()==Method.COOMBS)
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
		//			return false;
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
