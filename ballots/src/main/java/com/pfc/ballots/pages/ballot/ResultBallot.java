package com.pfc.ballots.pages.ballot;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

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
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.dao.VoteDao;
import com.pfc.ballots.dao.VotoAcumulativoDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.data.Method;
import com.pfc.ballots.entities.Ballot;
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
 *ResultBallot class is the controller for the page that
 * allow to see the results of an existing ballot
 * 
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2015
 */

@Import(library = "context:js/charts.js")
public class ResultBallot {

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// GENERAL STUFF //////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SessionState
	private DataSession datasession;

	@Inject
	private ComponentResources componentResources;


	@SessionAttribute
	private String contextResultBallotId;



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
	BordaDao bordaDao;
	RangeVotingDao rangeDao;
	ApprovalVotingDao approvalVotingDao;
	BramsDao bramsDao;
	BlackDao blackDao;
	BucklinDao bucklinDao;
	CondorcetDao condorcetDao;
	CoombsDao coombsDao;
	CopelandDao copelandDao;
	DodgsonDao dodgsonDao;
	HareDao hareDao;
	JuicioMayoritarioDao juicioMayoritarioDao;
	MejorPeorDao mejorPeorDao;
	NansonDao nansonDao;
	SchulzeDao schulzeDao;
	SmallDao smallDao;
	VotoAcumulativoDao votoAcumulativoDao;	




	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////// INITIALIZE ///////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Initialize values
	 */
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();

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


			if(!ballot.isEnded())
			{
				relMay.calcularMayoriaRelativa();
			}
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

			if(!ballot.isEnded())
			{
				kemeny.calcularKemeny();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && kemeny!=null)
			{
				kemeny.calcularKemeny();
				ballot.setCounted(true);
				kemenyDao.update(kemeny);
				ballotDao.updateBallot(ballot);
			}

		}
		else if(ballot.getMethod()==Method.BORDA)
		{
			bordaDao=DB4O.getBordaDao(datasession.getDBName());
			borda=bordaDao.getByBallotId(contextResultBallotId);

			if(!ballot.isEnded())
			{
				borda.calcularBorda();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && borda!=null)
			{
				borda.calcularBorda();
				ballot.setCounted(true);
				bordaDao.update(borda);
				ballotDao.updateBallot(ballot);
			}
		}
		else if(ballot.getMethod()==Method.RANGE_VOTING)
		{
			rangeDao=DB4O.getRangeVotingDao();
			range=rangeDao.getByBallotId(contextResultBallotId);

			if(!ballot.isEnded())
			{
				range.calcularRangeVoting();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && range!=null)
			{
				range.calcularRangeVoting();
				ballot.setCounted(true);
				rangeDao.update(range);
				ballotDao.updateBallot(ballot);
			}
		}
		else if(ballot.getMethod()==Method.APPROVAL_VOTING)
		{
			approvalVotingDao= DB4O.getApprovalVotingDao(datasession.getDBName());
			approvalVoting=approvalVotingDao.getByBallotId(ballot.getId());


			if(!ballot.isEnded())
			{
				approvalVoting.calcularApprovalVoting();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && approvalVoting!=null)
			{
				approvalVoting.calcularApprovalVoting();
				ballot.setCounted(true);
				approvalVotingDao.update(approvalVoting);
				ballotDao.updateBallot(ballot);
			}
		}
		else if(ballot.getMethod()==Method.BRAMS)
		{
			bramsDao= DB4O.getBramsDao(datasession.getDBName());
			brams=bramsDao.getByBallotId(ballot.getId());


			if(!ballot.isEnded())
			{
				brams.calcularBrams();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && brams!=null)
			{
				brams.calcularBrams();
				ballot.setCounted(true);
				bramsDao.update(brams);
				ballotDao.updateBallot(ballot);
			}
		}
		else if(ballot.getMethod()==Method.VOTO_ACUMULATIVO)
		{
			votoAcumulativoDao= DB4O.getVotoAcumulativoDao(datasession.getDBName());
			votoAcumulativo=votoAcumulativoDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				votoAcumulativo.calcularVotoAcumulativo();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && votoAcumulativo!=null)
			{
				votoAcumulativo.calcularVotoAcumulativo();
				ballot.setCounted(true);
				votoAcumulativoDao.update(votoAcumulativo);
				ballotDao.updateBallot(ballot);
			}
		}		
		else if(ballot.getMethod()==Method.JUICIO_MAYORITARIO)
		{
			juicioMayoritarioDao= DB4O.getJuicioMayoritarioDao(datasession.getDBName());
			juicioMayoritario=juicioMayoritarioDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				juicioMayoritario.calcularJuicioMayoritario();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && juicioMayoritario!=null)
			{
				juicioMayoritario.calcularJuicioMayoritario();
				ballot.setCounted(true);
				juicioMayoritarioDao.update(juicioMayoritario);
				ballotDao.updateBallot(ballot);
			}
		}	
		else if(ballot.getMethod()==Method.CONDORCET)
		{
			condorcetDao= DB4O.getCondorcetDao(datasession.getDBName());
			condorcet=condorcetDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				condorcet.calcularCondorcet();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && condorcet!=null)
			{
				condorcet.calcularCondorcet();
				ballot.setCounted(true);
				condorcetDao.update(condorcet);
				ballotDao.updateBallot(ballot);
			}
		}	

		else if(ballot.getMethod()==Method.COPELAND)
		{
			copelandDao= DB4O.getCopelandDao(datasession.getDBName());
			copeland=copelandDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				copeland.calcularCopeland();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && copeland!=null)
			{
				copeland.calcularCopeland();
				ballot.setCounted(true);
				copelandDao.update(copeland);
				ballotDao.updateBallot(ballot);
			}
		}	
		else if(ballot.getMethod()==Method.BLACK)
		{
			blackDao= DB4O.getBlackDao(datasession.getDBName());
			black=blackDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				black.calcularBlack();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && black!=null)
			{
				black.calcularBlack();
				ballot.setCounted(true);
				blackDao.update(black);
				ballotDao.updateBallot(ballot);
			}
		}	

		else if(ballot.getMethod()==Method.DODGSON)
		{
			dodgsonDao= DB4O.getDodgsonDao(datasession.getDBName());
			dodgson=dodgsonDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				dodgson.calcularDodgson();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && dodgson!=null)
			{
				dodgson.calcularDodgson();
				ballot.setCounted(true);
				dodgsonDao.update(dodgson);
				ballotDao.updateBallot(ballot);
			}
		}	

		else if(ballot.getMethod()==Method.SMALL)
		{
			smallDao= DB4O.getSmallDao(datasession.getDBName());
			small=smallDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				small.calcularSmall();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && small!=null)
			{
				small.calcularSmall();
				ballot.setCounted(true);
				smallDao.update(small);
				ballotDao.updateBallot(ballot);
			}
		}	
		else if(ballot.getMethod()==Method.SCHULZE)
		{
			schulzeDao= DB4O.getSchulzeDao(datasession.getDBName());
			schulze=schulzeDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				schulze.calcularSchulze();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && schulze!=null)
			{
				schulze.calcularSchulze();
				ballot.setCounted(true);
				schulzeDao.update(schulze);
				ballotDao.updateBallot(ballot);
			}
		}	
		else if(ballot.getMethod()==Method.MEJOR_PEOR)
		{
			mejorPeorDao= DB4O.getMejorPeorDao(datasession.getDBName());
			mejorPeor=mejorPeorDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				mejorPeor.calcularMejorPeor();
				System.out.println("Resultados: "+mejorPeor.getResults());
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && mejorPeor!=null)
			{
				mejorPeor.calcularMejorPeor();
				ballot.setCounted(true);
				mejorPeorDao.update(mejorPeor);
				ballotDao.updateBallot(ballot);
			}
		}	
		else if(ballot.getMethod()==Method.BUCKLIN)
		{
			bucklinDao= DB4O.getBucklinDao(datasession.getDBName());
			bucklin=bucklinDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				bucklin.calcularBucklin();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && bucklin!=null)
			{
				bucklin.calcularBucklin();
				ballot.setCounted(true);
				bucklinDao.update(bucklin);
				ballotDao.updateBallot(ballot);
			}
		}	
		else if(ballot.getMethod()==Method.HARE)
		{
			hareDao= DB4O.getHareDao(datasession.getDBName());
			hare=hareDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				hare.calcularHare();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && hare!=null)
			{
				hare.calcularHare();
				ballot.setCounted(true);
				hareDao.update(hare);
				ballotDao.updateBallot(ballot);
			}
		}	
		else if(ballot.getMethod()==Method.COOMBS)
		{
			coombsDao= DB4O.getCoombsDao(datasession.getDBName());
			coombs=coombsDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				coombs.calcularCoombs();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && coombs!=null)
			{
				coombs.calcularCoombs();
				ballot.setCounted(true);
				coombsDao.update(coombs);
				ballotDao.updateBallot(ballot);
			}
		}	
		else if(ballot.getMethod()==Method.NANSON)
		{
			coombsDao= DB4O.getCoombsDao(datasession.getDBName());
			coombs=coombsDao.getByBallotId(ballot.getId());

			if(!ballot.isEnded())
			{
				coombs.calcularCoombs();
			}
			if(ballot.isEnded()==true && ballot.isCounted()==false && coombs!=null)
			{
				coombs.calcularCoombs();
				ballot.setCounted(true);
				coombsDao.update(coombs);
				ballotDao.updateBallot(ballot);
			}
		}	


	}
	/**
	 * JavaScript controller
	 */
	public void afterRender()
	{
		JSONArray array=new JSONArray();

		JSONObject obj=new JSONObject();
		obj.put("title", ballot.getName());
		array.put(obj);


		if(ballot.getMethod()==Method.MAYORIA_RELATIVA)
		{
			List<String> options=getRelMayOptions();
			for(String option:options)
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
			List<String> permutations=getKemenyPermutations();
			for(String permutation:permutations)
			{
				obj=new JSONObject();
				obj.put("option", permutation);
				obj.put("value", kemeny.getResult(permutation));
				array.put(obj);
			}
			javaScriptSupport.addInitializerCall("charts_kem",array.toString());

		}
		if(ballot.getMethod()==Method.BORDA)
		{
			List<String> bordaOptions=getBordaOptions();
			for(String bordOpt:bordaOptions)
			{
				obj=new JSONObject();
				obj.put("option", bordOpt);
				obj.put("value", borda.getResult(bordOpt));
				array.put(obj);
			}
			System.out.println("tamaño->"+array.toString());
			javaScriptSupport.addInitializerCall("charts_bor",array.toString());
		}
		if(ballot.getMethod()==Method.RANGE_VOTING)
		{
			List<String> rangeOptions=getRangeOptions();
			for(String rangeOption:rangeOptions)
			{
				obj=new JSONObject();
				obj.put("option", rangeOption);
				obj.put("value",  range.getResult(rangeOption));
				array.put(obj);
			}
			System.out.println("tamaño->"+array.toString());
			javaScriptSupport.addInitializerCall("charts_range",array.toString());
		}
		if(ballot.getMethod()==Method.APPROVAL_VOTING)
		{
			List<String> options=getApprovalVotingOptions();
			for(String option:options)
			{
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", approvalVoting.getResultOption(option));
				array.put(obj);
			}

			System.out.println("tamaño->"+array.toString());
			javaScriptSupport.addInitializerCall("charts_approval",array.toString());
		}
		if(ballot.getMethod()==Method.BRAMS)
		{
			List<String> options=getBramsOptions();
			for(String option:options)
			{
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", brams.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_brams",array.toString());
		}

		if(ballot.getMethod()==Method.VOTO_ACUMULATIVO)
		{
			List<String> options=getVotoAcumulativoOptions();
			for(String option:options)
			{
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", votoAcumulativo.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_votoAcumulativo",array.toString());
		}
		if(ballot.getMethod()==Method.JUICIO_MAYORITARIO)
		{
			List<String> options=getJuicioMayoritarioOptions();
			for(String option:options)
			{
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", juicioMayoritario.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_juicioMayoritario",array.toString());
		}
		if(ballot.getMethod()==Method.CONDORCET)
		{
			List<String> options=getCondorcetOptions();
			for(String option:options)
			{
				System.out.println(option);
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", condorcet.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_condorcet",array.toString());
		}

		if(ballot.getMethod()==Method.BLACK)
		{
			List<String> options=getBlackOptions();
			for(String option:options)
			{
				System.out.println(option);
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", black.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_black",array.toString());
		}
		if(ballot.getMethod()==Method.SCHULZE)
		{
			List<String> options=getSchulzeOptions();
			for(String option:options)
			{
				System.out.println(option);
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", schulze.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_schulze",array.toString());
		}
		if(ballot.getMethod()==Method.SMALL)
		{
			List<String> options=getSmallOptions();
			for(String option:options)
			{
				System.out.println(option);
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", small.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_small",array.toString());
		}
		if(ballot.getMethod()==Method.DODGSON)
		{
			List<String> options=getDodgsonOptions();
			for(String option:options)
			{
				System.out.println(option);
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", dodgson.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_dodgson",array.toString());
		}
		if(ballot.getMethod()==Method.COPELAND)
		{
			List<String> options=getCopelandOptions();
			for(String option:options)
			{
				System.out.println(option);
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", copeland.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_copeland",array.toString());
		}
		if(ballot.getMethod()==Method.MEJOR_PEOR)
		{
			List<String> options=getMejorPeorOptions();
			for(String option:options)
			{
				System.out.println(option);
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", mejorPeor.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_mejorPeor",array.toString());
		}
		if(ballot.getMethod()==Method.BUCKLIN)
		{
			List<String> options=getBucklinOptions();
			for(String option:options)
			{
				System.out.println(option);
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", bucklin.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_bucklin",array.toString());
		}
		if(ballot.getMethod()==Method.HARE)
		{
			List<String> options=getHareOptions();
			for(String option:options)
			{
				System.out.println(option);
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", hare.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_hare",array.toString());
		}
		if(ballot.getMethod()==Method.NANSON)
		{
			List<String> options=getNansonOptions();
			for(String option:options)
			{
				System.out.println(option);
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", nanson.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_nanson",array.toString());
		}
		if(ballot.getMethod()==Method.COOMBS)
		{
			List<String> options=getCoombsOptions();
			for(String option:options)
			{
				System.out.println(option);
				obj=new JSONObject();
				obj.put("option", option);
				obj.put("value", coombs.getResultOption(option));
				array.put(obj);
			}

			javaScriptSupport.addInitializerCall("charts_coombs",array.toString());
		}
	}
	public boolean getExistVotes()
	{
		
		if(ballot.getMethod()==Method.MAYORIA_RELATIVA)
		{
			if(relMay.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.KEMENY)
		{
			if(kemeny.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.BORDA)
		{
			if(borda.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.RANGE_VOTING)
		{
			if(range.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.APPROVAL_VOTING)
		{
			if(approvalVoting.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.BRAMS)
		{
			if(brams.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}

		if(ballot.getMethod()==Method.VOTO_ACUMULATIVO)
		{
			if(votoAcumulativo.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.JUICIO_MAYORITARIO)
		{
			if(juicioMayoritario.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.CONDORCET)
		{
			if(condorcet.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}

		if(ballot.getMethod()==Method.BLACK)
		{
			if(black.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.SCHULZE)
		{
			if(schulze.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.SMALL)
		{
			if(small.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.DODGSON)
		{
			if(dodgson.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.COPELAND)
		{
			if(copeland.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.MEJOR_PEOR)
		{
			if(mejorPeor.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.BUCKLIN)
		{
			if(bucklin.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.HARE)
		{
			if(hare.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.NANSON)
		{
			if(nanson.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		if(ballot.getMethod()==Method.COOMBS)
		{
			if(coombs.getVotes().size() >0){
				return true;
			}
			else{
				return false;
			}
		}
		return false;
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

	public List<String> getRelMayOptions()
	{
		List<String> lista=relMay.getOptions();

		for(int i=0;i<lista.size()-1;i++)
		{
			for(int j=0;j<lista.size()-i-1;j++)
			{
				if(relMay.getResultOption(lista.get(j+1))>relMay.getResultOption(lista.get(j)))
				{
					String aux=lista.get(j+1);
					lista.set(j+1, lista.get(j));
					lista.set(j, aux);
				}
			}
		}

		return lista;
	}

	public boolean getShowRelMay()
	{
		if(ballot!=null && ballot.getMethod()==Method.MAYORIA_RELATIVA)
		{return true;}
		return false;
	}

	public String getRelMayNum()
	{
		return String.valueOf(relMay.getVotes().size());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////// KEMENY ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Kemeny Data
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

	public List<String> getKemenyPermutations()
	{
		List<String> lista=kemeny.getPermutations();

		for(int i=0;i<lista.size()-1;i++)
		{
			for(int j=0;j<lista.size()-i-1;j++)
			{
				if(kemeny.getResult(lista.get(j+1))>kemeny.getResult(lista.get(j)))
				{
					String aux=lista.get(j+1);
					lista.set(j+1, lista.get(j));
					lista.set(j, aux);
				}
			}
		}

		return lista;
	}

	public String getKemenyVote()
	{
		return String.valueOf(kemeny.getResult(permutation));
	}
	public String getKemNum()
	{
		return String.valueOf(kemeny.getVotes().size());
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// BORDA ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Borda Data
	 */
	@Property
	@Persist
	Borda borda;

	@Property
	private String bordaOpt;

	public boolean getShowBorda()
	{
		if(ballot!=null && ballot.getMethod()==Method.BORDA)
		{return true;}
		return false;
	}

	public List<String> getBordaOptions()
	{
		List<String> lista=borda.getBordaOptions();

		for(int i=0;i<lista.size()-1;i++)
		{
			for(int j=0;j<lista.size()-i-1;j++)
			{
				if(borda.getResult(lista.get(j+1))>borda.getResult(lista.get(j)))
				{
					String aux=lista.get(j+1);
					lista.set(j+1, lista.get(j));
					lista.set(j, aux);
				}
			}
		}
		return lista;
	}


	public String getBordaVote()
	{
		return String.valueOf(borda.getResult(bordaOpt));
	}
	public String getBordaNum()
	{
		return String.valueOf(borda.getVotes().size());
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// RANGE ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * RangeVoting Data
	 */
	@Property
	@Persist
	RangeVoting range;

	@Property
	private String rangeOpt;

	public boolean getShowRange()
	{
		if(ballot!=null && ballot.getMethod()==Method.RANGE_VOTING)
		{return true;}
		return false;
	}


	public List<String> getRangeOptions()
	{
		List<String> lista=range.getOptions();
		for(int i=0;i<lista.size()-1;i++)
		{
			for(int j=0;j<lista.size()-i-1;j++)
			{
				if(range.getResult(lista.get(j+1))>range.getResult(lista.get(j)))
				{
					String aux=lista.get(j+1);
					lista.set(j+1, lista.get(j));
					lista.set(j, aux);
				}
			}
		}
		return lista;
	}


	public String getRangeVote()
	{
		return String.valueOf(range.getResult(rangeOpt));
	}
	public String getRangeNum()
	{
		return String.valueOf(range.getVotes().size());
	}
	public String getMaxValue()
	{
		return String.valueOf(range.getMaxValue());
	}
	public String getMinValue()
	{
		return String.valueOf(range.getMinValue());
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////// APPROVAL VOTING //////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Approval voting Data
	 */
	@Persist
	@Property
	ApprovalVoting approvalVoting;

	@Property
	private String optionApproval;

	public String getApprovalVotingVote()
	{
		return String.valueOf(approvalVoting.getResultOption(option));
	}

	public List<String> getApprovalVotingOptions()
	{
		List<String> lista=approvalVoting.getOptions();

		for(int i=0;i<lista.size()-1;i++)
		{
			for(int j=0;j<lista.size()-i-1;j++)
			{
				if(approvalVoting.getResultOption(lista.get(j+1))>approvalVoting.getResultOption(lista.get(j)))
				{
					String aux=lista.get(j+1);
					lista.set(j+1, lista.get(j));
					lista.set(j, aux);
				}
			}
		}

		return lista;
	}

	public boolean getShowApprovalVoting()
	{
		if(ballot!=null && ballot.getMethod()==Method.APPROVAL_VOTING)
		{return true;}
		return false;
	}

	public String getApprovalVotingNum()
	{
		return String.valueOf(approvalVoting.getVotes().size());
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////// BRAMS //////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Brams Data
	 */
	@Persist
	@Property
	Brams brams;
	@Property
	private String optionBrams;

	public String getBramsVote()
	{
		return String.valueOf(brams.getResultOption(option));
	}

	public List<String> getBramsOptions()
	{
		List<String> lista=brams.getOptions();

		for(int i=0;i<lista.size()-1;i++)
		{
			for(int j=0;j<lista.size()-i-1;j++)
			{
				if(brams.getResultOption(lista.get(j+1))>brams.getResultOption(lista.get(j)))
				{
					String aux=lista.get(j+1);
					lista.set(j+1, lista.get(j));
					lista.set(j, aux);
				}
			}
		}

		return lista;
	}

	public boolean getShowBrams()
	{
		if(ballot!=null && ballot.getMethod()==Method.BRAMS)
		{return true;}
		return false;
	}

	public String getBramsNum()
	{
		return String.valueOf(brams.getVotes().size());
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////// VotoAcumulativo  //////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * VotoAcumulativo Data
	 */
	@Persist
	@Property
	VotoAcumulativo votoAcumulativo;
	@Property
	private String optionVotoAcumulativo;

	public String getVotoAcumulativoVote()
	{
		return String.valueOf(votoAcumulativo.getResultOption(option));
	}

	public List<String> getVotoAcumulativoOptions()
	{
		List<String> lista=votoAcumulativo.getOptions();

		for(int i=0;i<lista.size()-1;i++)
		{
			for(int j=0;j<lista.size()-i-1;j++)
			{
				if(votoAcumulativo.getResultOption(lista.get(j+1))>votoAcumulativo.getResultOption(lista.get(j)))
				{
					String aux=lista.get(j+1);
					lista.set(j+1, lista.get(j));
					lista.set(j, aux);
				}
			}
		}

		return lista;
	}

	public boolean getShowVotoAcumulativo()
	{
		if(ballot!=null && ballot.getMethod()==Method.VOTO_ACUMULATIVO)
		{return true;}
		return false;
	}

	public String getVotoAcumulativoNum()
	{
		return String.valueOf(votoAcumulativo.getVotes().size());
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////// JUICIO MAYORITARIO  //////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * VotoAcumulativo Data
	 */
	@Persist
	@Property
	JuicioMayoritario juicioMayoritario;
	@Property
	private String optionJuicioMayoritario;

	public String getJuicioMayoritarioVote()
	{
		return String.valueOf(juicioMayoritario.getResultOption(option));
	}

	public List<String> getJuicioMayoritarioOptions()
	{
		List<String> lista=juicioMayoritario.getOptions();

		for(int i=0;i<lista.size()-1;i++)
		{
			for(int j=0;j<lista.size()-i-1;j++)
			{
				if(juicioMayoritario.getResultOption(lista.get(j+1))>juicioMayoritario.getResultOption(lista.get(j)))
				{
					String aux=lista.get(j+1);
					lista.set(j+1, lista.get(j));
					lista.set(j, aux);
				}
			}
		}

		return lista;
	}

	public boolean getShowJuicioMayoritario()
	{
		if(ballot!=null && ballot.getMethod()==Method.JUICIO_MAYORITARIO)
		{return true;}
		return false;
	}

	public String getJuicioMayoritarioNum()
	{
		return String.valueOf(juicioMayoritario.getVotes().size());
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// CONDORCET ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Condorcet Data
	 */
	@Property
	@Persist
	Condorcet condorcet;

	@Property
	private String condorcetOpt;

	public boolean getShowCondorcet()
	{
		if(ballot!=null && ballot.getMethod()==Method.CONDORCET)
		{return true;}
		return false;
	}

	public List<String> getCondorcetOptions()
	{
		List<String> opciones=condorcet.getOptions();
		return opciones;
	}


	public String getCondorcetVote()
	{
		return String.valueOf(condorcet.getResultOption(option));
	}
	public String getCondorcetNum()
	{
		return String.valueOf(condorcet.getVotes().size());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// Schulze ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Schulze Data
	 */
	@Property
	@Persist
	Schulze schulze;

	@Property
	private String schulzeOpt;

	public boolean getShowSchulze()
	{
		if(ballot!=null && ballot.getMethod()==Method.SCHULZE)
		{return true;}
		return false;
	}

	public List<String> getSchulzeOptions()
	{
		List<String> opciones=schulze.getOptions();
		return opciones;
	}


	public String getSchulzeVote()
	{
		return String.valueOf(schulze.getResultOption(option));
	}
	public String getSchulzeNum()
	{
		return String.valueOf(schulze.getVotes().size());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// Small ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Small Data
	 */
	@Property
	@Persist
	Small small;

	@Property
	private String smallOpt;

	public boolean getShowSmall()
	{
		if(ballot!=null && ballot.getMethod()==Method.SMALL)
		{return true;}
		return false;
	}

	public List<String> getSmallOptions()
	{
		List<String> opciones=small.getOptions();
		return opciones;
	}


	public String getSmallVote()
	{
		return String.valueOf(small.getResultOption(option));
	}
	public String getSmallNum()
	{
		return String.valueOf(small.getVotes().size());
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// Copeland ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Copeland Data
	 */
	@Property
	@Persist
	Copeland copeland;

	@Property
	private String copelandOpt;

	public boolean getShowCopeland()
	{
		if(ballot!=null && ballot.getMethod()==Method.COPELAND)
		{return true;}
		return false;
	}

	public List<String> getCopelandOptions()
	{
		List<String> opciones=copeland.getOptions();
		return opciones;
	}


	public String getCopelandVote()
	{
		return String.valueOf(copeland.getResultOption(option));
	}
	public String getCopelandNum()
	{
		return String.valueOf(copeland.getVotes().size());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// Dodgson ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Dodgson Data
	 */
	@Property
	@Persist
	Dodgson dodgson;

	@Property
	private String dodgsonOpt;

	public boolean getShowDodgson()
	{
		if(ballot!=null && ballot.getMethod()==Method.DODGSON)
		{return true;}
		return false;
	}

	public List<String> getDodgsonOptions()
	{
		List<String> opciones=dodgson.getOptions();
		return opciones;
	}


	public String getDodgsonVote()
	{
		return String.valueOf(dodgson.getResultOption(option));
	}
	public String getDodgsonNum()
	{
		return String.valueOf(dodgson.getVotes().size());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// Black ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Black Data
	 */
	@Property
	@Persist
	Black black;

	@Property
	private String blackOpt;

	public boolean getShowBlack()
	{
		if(ballot!=null && ballot.getMethod()==Method.BLACK)
		{return true;}
		return false;
	}

	public List<String> getBlackOptions()
	{
		List<String> opciones=black.getOptions();
		return opciones;
	}


	public String getBlackVote()
	{
		return String.valueOf(black.getResultOption(option));
	}
	public String getBlackNum()
	{
		return String.valueOf(black.getVotes().size());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// MEJOR_PEOR ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Mejor peor Data
	 */
	@Property
	@Persist
	MejorPeor mejorPeor;

	@Property
	private String mejorPeorOpt;

	public boolean getShowMejorPeor()
	{
		if(ballot!=null && ballot.getMethod()==Method.MEJOR_PEOR)
		{return true;}
		return false;
	}

	public List<String> getMejorPeorOptions()
	{
		List<String> opciones=mejorPeor.getOptions();
		return opciones;
	}


	public String getMejorPeorVote()
	{
		return String.valueOf(mejorPeor.getResultOption(option));
	}
	public String getMejorPeorNum()
	{
		return String.valueOf(mejorPeor.getVotesNeg().size());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// Nanson ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Nanson Data
	 */
	@Property
	@Persist
	Nanson nanson;

	@Property
	private String nansonOpt;

	public boolean getShowNanson()
	{
		if(ballot!=null && ballot.getMethod()==Method.NANSON)
		{return true;}
		return false;
	}

	public List<String> getNansonOptions()
	{
		List<String> opciones=nanson.getOptions();
		return opciones;
	}


	public String getNansonVote()
	{
		return String.valueOf(nanson.getResultOption(option));
	}
	public String getNansonNum()
	{
		return String.valueOf(nanson.getVotes().size());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// Hare ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Hare Data
	 */
	@Property
	@Persist
	Hare hare;

	@Property
	private String hareOpt;

	public boolean getShowHare()
	{
		if(ballot!=null && ballot.getMethod()==Method.HARE)
		{return true;}
		return false;
	}

	public List<String> getHareOptions()
	{
		List<String> opciones=hare.getOptions();
		return opciones;
	}


	public String getHareVote()
	{
		return String.valueOf(hare.getResultOption(option));
	}
	public String getHareNum()
	{
		return String.valueOf(hare.getVotes().size());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// Coombs ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Coombs Data
	 */
	@Property
	@Persist
	Coombs coombs;

	@Property
	private String coombsOpt;

	public boolean getShowCoombs()
	{
		if(ballot!=null && ballot.getMethod()==Method.COOMBS)
		{return true;}
		return false;
	}

	public List<String> getCoombsOptions()
	{
		List<String> opciones=coombs.getOptions();
		return opciones;
	}


	public String getCoombsVote()
	{
		return String.valueOf(coombs.getResultOption(option));
	}
	public String getCoombsNum()
	{
		return String.valueOf(coombs.getVotes().size());
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////// Bucklin ////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Bucklin Data
	 */
	@Property
	@Persist
	Bucklin bucklin;

	@Property
	private String bucklinOpt;

	public boolean getShowBucklin()
	{
		if(ballot!=null && ballot.getMethod()==Method.COOMBS)
		{return true;}
		return false;
	}

	public List<String> getBucklinOptions()
	{
		List<String> opciones=bucklin.getOptions();
		return opciones;
	}


	public String getBucklinVote()
	{
		return String.valueOf(bucklin.getResultOption(option));
	}
	public String getBucklinNum()
	{
		return String.valueOf(bucklin.getVotes().size());
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
			return null;
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
