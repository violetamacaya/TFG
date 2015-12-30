package com.pfc.ballots.pages.ballot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.beaneditor.Width;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
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
import com.pfc.ballots.dao.EmailAccountDao;
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
import com.pfc.ballots.data.BallotKind;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.data.Method;
import com.pfc.ballots.encoder.CensusEncoder;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.entities.Census;
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
import com.pfc.ballots.pages.UnauthorizedAttempt;
import com.pfc.ballots.util.GenerateDocentVotes;
import com.pfc.ballots.util.UUID;

/**
 * 
 * EditBallot class is the controller for the page that
 * allow to edit an existing ballot
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 NOV-2015
 */

public class EditBallot {
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

		@Persist
		private Ballot ballot;

		@Persist
		@Property
		private Ballot newBallot;
		
		@Persist
		private String ballotId;
		
		@Property
		@Persist
		private Ballot oldBallot;
		
		@SessionAttribute
		private String ballotIdSesion;

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////// DAO //////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
		@Persist
		CensusDao censusDao;
		@Persist
		BallotDao ballotDao;
		VoteDao voteDao;
		RelativeMajorityDao relativeMajorityDao;
		ApprovalVotingDao approvalVotingDao;
		KemenyDao kemenyDao;
		BordaDao bordaDao;
		RangeVotingDao rangeDao;
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
		UserDao userDao;
		EmailAccountDao emailAccountDao;
	
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////// INITIALIZE ///////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/**
		 * Initialize values
		 */
		
		@SuppressWarnings("deprecation")
		public void setup(String id)
		{
			ballotDao = DB4O.getBallotDao();
			oldBallot = ballotDao.getById(id);
			this.ballotId=id;
			this.ballotName = oldBallot.getName();
			this.description=oldBallot.getDescription();
			this.endDate=oldBallot.getEndDate();
			this.startDate=oldBallot.getStartDate();
			cal_old=new GregorianCalendar();

			this.cal_old.setTime(oldBallot.getStartDate());
			
			this.method=oldBallot.getMethod();
			if(oldBallot.isPrivat()){
				this.ballotKind = BallotKind.PRIVADA;
			}
			else if(oldBallot.isPublica()){
				this.ballotKind = BallotKind.PUBLICA;
			}
			else if(oldBallot.isTeaching()){
				this.ballotKind = BallotKind.DOCENTE;
			}
			else
				this.ballotKind = BallotKind.SENSIBLE;

			int hora =startDate.getHours();
			String horaString;
			if(hora < 10)
				horaString = "0"+Integer.valueOf(hora).toString();
			else
				horaString = Integer.valueOf(hora).toString();
			
			int minutos =startDate.getMinutes();
			String minutosString;
			if(minutos < 10)
				minutosString = "0"+Integer.valueOf(minutos).toString();
			else
				minutosString = Integer.valueOf(minutos).toString();
			
			int segundos =startDate.getSeconds();
			String segundosString;
			if(segundos < 10)
				segundosString = "0"+Integer.valueOf(segundos).toString();
			else
				segundosString = Integer.valueOf(segundos).toString();
			
			this.startHour=horaString +":"+ minutosString +":"+ segundosString;
			

			hora =endDate.getHours();
			if(hora < 10)
				horaString = "0"+Integer.valueOf(hora).toString();
			else
				horaString = Integer.valueOf(hora).toString();
			
			minutos =endDate.getMinutes();
			if(minutos < 10)
				minutosString = "0"+Integer.valueOf(minutos).toString();
			else
				minutosString = Integer.valueOf(minutos).toString();
			
			segundos =endDate.getSeconds();
			if(segundos < 10)
				segundosString = "0"+Integer.valueOf(segundos).toString();
			else
				segundosString = Integer.valueOf(segundos).toString();
			
			this.endHour=horaString +":"+ minutosString +":"+ segundosString;
			
			this.method=oldBallot.getMethod();
			
			
			//Según el método que sea, ponerle el valor a numopciones y a cada una de las opciones.
			if(method==Method.MAYORIA_RELATIVA)
			{
				showType=false;
				showMayRel=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("mayRelZone",mayRelZone);
			}
			else if(method==Method.KEMENY)
			{
				showType=false;
				showKemeny=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("kemenyZone",kemenyZone);
			}
			else if(method==Method.BORDA)
			{
				showType=false;
				showBorda=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("bordaZone", bordaZone);
			}
			else if(method==Method.RANGE_VOTING)
			{
				showType=false;
				showRange=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("rangeZone",rangeZone);
			}
			else if(method==Method.APPROVAL_VOTING)
			{
				showType=false;
				showApproval=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("approvalZone",approvalZone);
			}
			else if(method==Method.BRAMS)
			{	
				bramsDao=DB4O.getBramsDao(datasession.getDBName());
				brams=bramsDao.getByBallotId(ballotId);
				int numOptBrams = brams.getOptions().size();
				
				if(numOptBrams >=7){
					bramsOp1 = brams.getOption(0);
					bramsOp2 = brams.getOption(1);
					bramsOp3 = brams.getOption(2);
					bramsOp4 = brams.getOption(3);
					bramsOp5 = brams.getOption(4);
					bramsOp6 = brams.getOption(5);
					bramsOp7 = brams.getOption(6);					
				}
				if(numOptBrams >=8)
					bramsOp8 = brams.getOption(7);
				if(numOptBrams >=9)
					bramsOp8 = brams.getOption(8);
				if(numOptBrams >=10)
					bramsOp8 = brams.getOption(9);
				if(numOptBrams >=11)
					bramsOp8 = brams.getOption(10);
				if(numOptBrams >=12)
					bramsOp8 = brams.getOption(11);
				if(numOptBrams >=13)
					bramsOp8 = brams.getOption(12);
				if(numOptBrams >=14)
					bramsOp8 = brams.getOption(13);
				if(numOptBrams >=15)
					bramsOp8 = brams.getOption(14);
			}
			else if(method==Method.VOTO_ACUMULATIVO)
			{
				showType=false;
				showVotoAcumulativo=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("votoAcumulativoZone",votoAcumulativoZone);
			}
			else if(method==Method.JUICIO_MAYORITARIO)
			{
				showType=false;
				showJuicioMayoritario=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("juicioMayoritarioZone",juicioMayoritarioZone);
			}
			else if(method==Method.CONDORCET)
			{
				showType=false;
				showCondorcet=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("condorcetZone",condorcetZone);
			}
			else if(method==Method.BLACK)
			{
				showType=false;
				showBlack=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("blackZone",blackZone);
			}
			else if(method==Method.DODGSON)
			{
				showType=false;
				showDodgson=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("dodgsonZone",dodgsonZone);
			}
			else if(method==Method.COPELAND)
			{
				showType=false;
				showCopeland=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("copelandZone",copelandZone);
			}
			else if(method==Method.SCHULZE)
			{
				showType=false;
				showSchulze=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("schulzeZone",schulzeZone);
			}
			else if(method==Method.SMALL)
			{
				showType=false;
				showSmall=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("smallZone",smallZone);
			}
			else if(method==Method.MEJOR_PEOR)
			{
				showType=false;
				showMejorPeor=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("mejorPeorZone",mejorPeorZone);
			}
			else if(method==Method.BUCKLIN)
			{
				showType=false;
				showBucklin=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("bucklinZone",bucklinZone);
			}
			else if(method==Method.NANSON)
			{
				showType=false;
				showNanson=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("nansonZone",nansonZone);
			}
			else if(method==Method.HARE)
			{
				showType=false;
				showHare=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("hareZone",hareZone);
			}
			else if(method==Method.COOMBS)
			{
				showType=false;
				showCoombs=true;
				ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("coombsZone", coombsZone);
			}

		}
		public void setupRender()
		{
			oldBallot=ballotDao.getById(ballotId);
			newBallot= new Ballot();
			newBallot.setDescription(oldBallot.getDescription());
			newBallot.setName(oldBallot.getName());
			newBallot.setStartDate(oldBallot.getStartDate());
			newBallot.setEndDate(oldBallot.getEndDate());

			initializeBooleans();

			cal_actual=new GregorianCalendar();
			cal_actual.setTime(new Date());
			ballotDao=DB4O.getBallotDao(datasession.getDBName());
			censusDao=DB4O.getCensusDao(datasession.getDBName());
			voteDao=DB4O.getVoteDao(datasession.getDBName());
			censuses=censusDao.getByOwnerId(datasession.getId());
			if(censuses.size()==0)
			{
				existCensus=false;
			}
			else
			{
				existCensus=true;
			}
			censusNormalModel=selectModelFactory.create(censuses, "censusName");

			censusModel=NO_CENSUS;
			
			
			newBallot = new Ballot();
			
		}
		public void initializeBooleans()
		{
			showGeneral=false;
			showErrorGeneral=false;
			showBadName=false;

			showType=true;
			showNormalCensus=false;
			showBadDate=false;
			showErrorType=false;

			showMayRel=false;
			showErrorMayRel=false;

			showApproval=false;
			showErrorApproval=false;

			showBrams=false;
			showErrorBrams=false;

			showVotoAcumulativo=false;
			showErrorVotoAcumulativo=false;

			showCondorcet=false;
			showErrorCondorcet=false;

			showBlack=false;
			showErrorBlack=false;

			showDodgson=false;
			showErrorDodgson=false;

			showCopeland=false;
			showErrorCopeland=false;

			showSchulze=false;
			showErrorSchulze=false;

			showSmall=false;
			showErrorSmall=false;

			showMejorPeor=false;
			showErrorMejorPeor=false;

			showJuicioMayoritario=false;
			showErrorJuicioMayoritario=false;		

			showKemeny=false;
			showErrorKemeny=false;
			showBadCharKemeny=false;

			showBorda=false;
			showBordaFill=false;
			showBordaRepeat=false;
			showBordaBadChar=false;

			showRange=false;
			showRangeMax=false;
			showRangeValues=false;
			showRangeFill=false;
			showRangeBadChar=false;
			showRangeRepeated=false;
			showRangeBadNum=false;
			
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////// GENERAL ZONE //////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		@InjectComponent
		private Zone generalZone;

		@Property
		@Persist
		private boolean showGeneral;
		@Property
		@Persist
		private boolean showErrorGeneral;
		@Property
		@Persist
		private boolean showBadName;
		@Persist
		@Property
		@Validate("minLength=5")
		private String ballotName;
		@Width(value = 75)
		@Persist
		@Property
		//@Validate("minLength=20")
		private String description;


		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////// Type ZONE /////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		@InjectComponent
		private Zone typeZone;
		@Property
		@Persist
		private boolean showType;

		static final private String[] NO_CENSUS = new String[] {};
		@Property
		private String dateInFormatStr = "dd/MM/yyyy";

		@Property
		@Persist
		private boolean showErrorType;
		@Property
		@Persist
		private boolean showBadDate;
		@Property
		@Persist
		private boolean showNormalCensus;
		@Property
		@Persist
		private boolean existCensus;
		@Property
		@Persist
		private Method method;

		@Property
		@Persist
		private BallotKind ballotKind;
		@Property
		@Persist
		private String[] censusModel;
		@Property
		@Persist
		private String census;

		@Property
		@Persist
		private SelectModel censusNormalModel;
		@Inject
		private SelectModelFactory selectModelFactory;
		@Persist
		private List<Census> censuses;
		@Property
		@Persist
		private Census censusNormal;

		@Property
		@Persist
		private String numVotes;

		@Property
		@Persist
		private boolean editable;

		public CensusEncoder getCensusNormalEncoder()
		{
			return new CensusEncoder(censuses);
		}
		@Property
		@Persist
		@Validate("regexp=^([0-2]?[0-9]|3[0-1])/([0-1][0-2]|0[0-9]|[0-9])/2[0-9][1-9][4-9]$")
		private Date startDate;
		@Property
		@Persist
		@Validate("regexp=^([0-1]?[0-9]|2[0-3])\\:[0-5][0-9]\\:[0-5][0-9]$")
		private String startHour;
		@Property
		@Persist
		@Validate("regexp=^([0-1]?[0-9]|2[0-3])\\:[0-5][0-9]\\:[0-5][0-9]$")
		private String actualHour;
		@Property
		@Persist
		@Validate("regexp=^([0-2]?[0-9]|3[0-1])/([0-1][0-2]|0[0-9]|[0-9])/2[0-9][1-9][4-9]$")
		private Date endDate;
		@Property
		@Persist
		@Validate("regexp=^([0-1]?[0-9]|2[0-3])\\:[0-5][0-9]\\:[0-5][0-9]$")
		private String endHour;



		public boolean isShowExist()
		{
			if(ballotKind==BallotKind.PUBLICA)
				return true;
			else
				return existCensus;
		}

		@Persist
		private Calendar cal_inicio;
		@Persist
		private Calendar cal_final;
		@Persist
		private Calendar cal_actual;
		@Persist
		private Calendar cal_old;

		/**
		 * Checks if the data of the ballot are correct
		 */
		@SuppressWarnings("deprecation")
		public void onValidateFromTypeForm()
		{
			cal_inicio=new GregorianCalendar();
			cal_final=new GregorianCalendar();


			showErrorType=false;
			showBadDate=false;

			if(method==null|| ballotKind==null)
			{
				System.out.println("Entro por aquí: "+method +" "+ballotKind);
				showErrorType=true;
			}
			else if(ballotKind==BallotKind.PRIVADA || ballotKind==BallotKind.PUBLICA)
			{

				if(censusNormal==null && ballotKind==BallotKind.PRIVADA)
				{
					showErrorType=true;
					System.out.println("NULL");
				}
				if(startDate==null)
				{
					Date date=new Date();
					cal_inicio.setTime(date);
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); // Set your date format
					String currentData = sdf.format(date);
					try
					{
						startDate= sdf.parse(currentData);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}

					if(startHour==null)
					{
						String hour=String.valueOf(cal_inicio.get(Calendar.HOUR_OF_DAY));
						System.out.println(hour);
						if(Integer.parseInt(hour)<10)
						{
							hour="0"+hour;
						}
						String min=String.valueOf(cal_inicio.get(Calendar.MINUTE));
						System.out.println(min);
						if(Integer.parseInt(min)<10)
						{
							min="0"+min;
						}
						String sec=String.valueOf(cal_inicio.get(Calendar.SECOND));
						System.out.println(sec);
						if(Integer.parseInt(sec)<10)
						{
							sec="0"+sec;
						}
						startHour=hour+":"+min+":"+sec;
					}
				}
				else
				{
					cal_inicio.setTime(startDate);


					if(startHour!=null)
					{
						cal_inicio.setTime(startDate);
						String [] temp= startHour.split(":");

						cal_inicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[0]));
						cal_inicio.set(Calendar.MINUTE, Integer.parseInt(temp[1]));
						cal_inicio.set(Calendar.SECOND, Integer.parseInt(temp[2]));

					}
					else{

						String hour=String.valueOf(cal_actual.get(Calendar.HOUR_OF_DAY));
						String min=String.valueOf(cal_actual.get(Calendar.MINUTE));
						String sec=String.valueOf(cal_actual.get(Calendar.SECOND));

						cal_inicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
						cal_inicio.set(Calendar.MINUTE, Integer.parseInt(min));
						cal_inicio.set(Calendar.SECOND, Integer.parseInt(sec));

					}
				}

				if(endDate==null)
				{
					showBadDate=true;
					showErrorType=true;
				}
				else
				{
					cal_final.setTime(endDate);
					if(endHour!=null)
					{
						String temp[] =endHour.split(":");
						cal_final.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[0]));
						cal_final.set(Calendar.MINUTE, Integer.parseInt(temp[1]));
						cal_final.set(Calendar.SECOND, Integer.parseInt(temp[2]));
					}
				}

				if(!showBadDate)
				{
					if(cal_inicio.getTimeInMillis()< cal_actual.getTimeInMillis()){

						showBadDate=true;
						if(cal_inicio.getTime().getYear() == cal_actual.getTime().getYear() && 
								cal_inicio.getTime().getMonth() == cal_actual.getTime().getMonth() &&
								cal_inicio.getTime().getDay() == cal_actual.getTime().getDay())					
						{
							showBadDate=false;
						}
						if(cal_inicio.getTime().getYear() == cal_old.getTime().getYear() && 
								cal_inicio.getTime().getMonth() == cal_old.getTime().getMonth() &&
								cal_inicio.getTime().getDay() == cal_old.getTime().getDay())	{
							showBadDate=false;
						}
					}
					if(cal_final.getTimeInMillis()<cal_inicio.getTimeInMillis())
					{
						System.out.println("end < start");
						showBadDate=true;
					}
				}


			}
			else if(ballotKind==BallotKind.DOCENTE)
			{

				if(census==null)
				{
					showErrorType=true;
				}
				startDate=new Date();
				endDate=new Date();
			}


		}

		public boolean isTeaching()
		{
			if(ballotKind==BallotKind.DOCENTE)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		public boolean isShowPublic()
		{
			if(ballotKind==BallotKind.PUBLICA)
				return true;
			else
				return false;
		}
		/**
		 * If the data are correct, give access to the next
		 * step of the wizard 
		 */
		public void onSuccessFromTypeForm()
		{
			showBadName=false;
			if(request.isXHR())
			{
				if(ballotName==null|| description==null)
				{
					showErrorGeneral=true;
				}
				else
				{
					if(ballotDao.isNameInUse(ballotName))
					{
						showBadName=true;
						if(ballotName.equals(oldBallot.getName())){
							//Se permitirá que tenga el mismo nombre que la anterior.
							showBadName=false;
						}
					}
					else
					{
						showErrorGeneral=false;
						showGeneral=false;
					}
				}
				if(showErrorGeneral || showBadName)
				{
					ajaxResponseRenderer.addRender("typeZone", typeZone);
				}

				else if(ballotKind==BallotKind.SENSIBLE)
				{
					ajaxResponseRenderer.addRender("typeZone", typeZone);
				}
				else if(showErrorType||showBadDate)
					ajaxResponseRenderer.addRender("typeZone", typeZone);
				else
				{

					if(!valueOk(census))
					{
						ajaxResponseRenderer.addRender("typeZone", typeZone);
					}
					
					else if(method==Method.MAYORIA_RELATIVA)
					{
						showType=false;
						showMayRel=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("mayRelZone",mayRelZone);
					}
					else if(method==Method.KEMENY)
					{
						showType=false;
						showKemeny=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("kemenyZone",kemenyZone);
					}
					else if(method==Method.BORDA)
					{
						showType=false;
						showBorda=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("bordaZone", bordaZone);
					}
					else if(method==Method.RANGE_VOTING)
					{
						showType=false;
						showRange=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("rangeZone",rangeZone);
					}
					else if(method==Method.APPROVAL_VOTING)
					{
						showType=false;
						showApproval=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("approvalZone",approvalZone);
					}
					else if(method==Method.BRAMS)
					{
						showType=false;
						showBrams=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("bramsZone",bramsZone);
					}
					else if(method==Method.VOTO_ACUMULATIVO)
					{
						showType=false;
						showVotoAcumulativo=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("votoAcumulativoZone",votoAcumulativoZone);
					}
					else if(method==Method.JUICIO_MAYORITARIO)
					{
						showType=false;
						showJuicioMayoritario=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("juicioMayoritarioZone",juicioMayoritarioZone);
					}
					else if(method==Method.CONDORCET)
					{
						showType=false;
						showCondorcet=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("condorcetZone",condorcetZone);
					}
					else if(method==Method.BLACK)
					{
						showType=false;
						showBlack=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("blackZone",blackZone);
					}
					else if(method==Method.DODGSON)
					{
						showType=false;
						showDodgson=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("dodgsonZone",dodgsonZone);
					}
					else if(method==Method.COPELAND)
					{
						showType=false;
						showCopeland=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("copelandZone",copelandZone);
					}
					else if(method==Method.SCHULZE)
					{
						showType=false;
						showSchulze=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("schulzeZone",schulzeZone);
					}
					else if(method==Method.SMALL)
					{
						showType=false;
						showSmall=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("smallZone",smallZone);
					}
					else if(method==Method.MEJOR_PEOR)
					{
						showType=false;
						showMejorPeor=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("mejorPeorZone",mejorPeorZone);
					}
					else if(method==Method.BUCKLIN)
					{
						showType=false;
						showBucklin=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("bucklinZone",bucklinZone);
					}
					else if(method==Method.NANSON)
					{
						showType=false;
						showNanson=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("nansonZone",nansonZone);
					}
					else if(method==Method.HARE)
					{
						showType=false;
						showHare=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("hareZone",hareZone);
					}
					else if(method==Method.COOMBS)
					{
						showType=false;
						showCoombs=true;
						ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("coombsZone", coombsZone);
					}
				}
			}


		}

		@Persist
		@Property
		private boolean badValue;

		public boolean valueOk(String num)
		{
			if(ballotKind==BallotKind.DOCENTE)
			{
				if(isNumeric(num))
				{
					if(Integer.parseInt(num) < 100000)
					{
						return true;
					}
					else
					{
						badValue=true;
						return false;
					}
				}
				else
				{
					badValue=true;
					return false;
				}
			}
			else{
				return true;
			}

		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// MAYORIA REL ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone mayRelZone;
		@Property
		@Persist
		private boolean showMayRel;
		@Property
		@Persist
		private boolean showErrorMayRel;
		@Property
		@Persist
		private boolean showRepeatedMayRel;
		@Persist
		private RelativeMajority relativeMajority;

		@Property
		@Persist 
		@Validate("required")
		private String mayRelNumOp;
		@Persist
		private int numOpt;
		@Property
		@Persist 
		private String [] mayRelModel;

		@Property
		@Persist
		private String mayRelOp1;
		@Property
		@Persist
		private String mayRelOp2;
		@Property
		@Persist
		private String mayRelOp3;
		@Property
		@Persist
		private String mayRelOp4;
		@Property
		@Persist
		private String mayRelOp5;
		@Property
		@Persist
		private String mayRelOp6;
		@Property
		@Persist
		private String mayRelOp7;
		@Property
		@Persist
		private String mayRelOp8;
		@Property
		@Persist
		private String mayRelOp9;
		@Property
		@Persist
		private String mayRelOp10;
		@Property
		@Persist
		private String mayRelOp11;
		@Property
		@Persist
		private String mayRelOp12;
		@Property
		@Persist
		private String mayRelOp13;
		@Property
		@Persist
		private String mayRelOp14;
		@Property
		@Persist
		private String mayRelOp15;


		private String option;
		public String getOption() {
			System.out.println("get");
			return option;
		}
		public void setOption(String option) {
			System.out.println("set");
			this.option = option;
		}

		/**
		 * Controls the number of options for the relative majority
		 * @param str
		 */
		public void  onValueChangedFromMayRelSel(String str)
		{
			numOpt=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("mayRelZone", mayRelZone);
		}


		public boolean isShowMay3()
		{
			if(numOpt>=3)
				return true;
			return false;
		}
		public boolean isShowMay4()
		{
			if(numOpt>=4)
				return true;
			return false;
		}
		public boolean isShowMay5()
		{
			if(numOpt>=5)
				return true;
			return false;
		}
		public boolean isShowMay6()
		{
			if(numOpt>=6)
				return true;
			return false;
		}
		public boolean isShowMay7()
		{
			if(numOpt>=7)
				return true;
			return false;
		}
		public boolean isShowMay8()
		{
			if(numOpt>=8)
				return true;
			return false;
		}
		public boolean isShowMay9()
		{
			if(numOpt>=9)
				return true;
			return false;
		}
		public boolean isShowMay10()
		{
			if(numOpt>=10)
				return true;
			return false;
		}
		public boolean isShowMay11()
		{
			if(numOpt>=11)
				return true;
			return false;
		}
		public boolean isShowMay12()
		{
			if(numOpt>=12)
				return true;
			return false;
		}
		public boolean isShowMay13()
		{
			if(numOpt>=13)
				return true;
			return false;
		}
		public boolean isShowMay14()
		{
			if(numOpt>=14)
				return true;
			return false;
		}
		public boolean isShowMay15()
		{
			if(numOpt>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the relative majority
		 */
		public void onValidateFromMayRelForm()
		{
			showErrorMayRel=false;
			showRepeatedMayRel=false;
			if(mayRelOp1==null || mayRelOp2==null)
			{

				showErrorMayRel=true;
			}

			System.out.println("NUMOP->"+numOpt);
			switch(numOpt)
			{
			case 15:
				if(mayRelOp15==null){showErrorMayRel=true;}
			case 14:
				if(mayRelOp14==null){showErrorMayRel=true;}
			case 13:
				if(mayRelOp13==null){showErrorMayRel=true;}
			case 12:
				if(mayRelOp12==null){showErrorMayRel=true;}
			case 11:
				if(mayRelOp11==null){showErrorMayRel=true;}
			case 10:
				if(mayRelOp10==null){showErrorMayRel=true;}
			case 9:
				if(mayRelOp9==null){showErrorMayRel=true;}
			case 8:
				if(mayRelOp8==null){showErrorMayRel=true;}
			case 7:
				if(mayRelOp7==null){showErrorMayRel=true;}
			case 6:
				if(mayRelOp6==null){showErrorMayRel=true;}
			case 5:
				if(mayRelOp5==null){showErrorMayRel=true;}
			case 4:
				if(mayRelOp4==null){showErrorMayRel=true;}
			case 3:
				if(mayRelOp3==null){showErrorMayRel=true;}
				break;
			default:
				showErrorMayRel=false;
			}
			if(!showErrorMayRel)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(mayRelOp1);
				listOptions.add(mayRelOp2);
				if(numOpt>=3)
				{
					listOptions.add(mayRelOp3);
				}
				if(numOpt>=4)
				{
					listOptions.add(mayRelOp4);
				}
				if(numOpt>=5)
				{
					listOptions.add(mayRelOp5);
				}
				if(numOpt>=6)
				{
					listOptions.add(mayRelOp6);
				}
				if(numOpt>=7)
				{
					listOptions.add(mayRelOp7);
				}
				if(numOpt>=8)
				{
					listOptions.add(mayRelOp8);
				}
				if(numOpt>=9)
				{
					listOptions.add(mayRelOp9);
				}
				if(numOpt>=10)
				{
					listOptions.add(mayRelOp10);
				}
				if(numOpt>=11)
				{
					listOptions.add(mayRelOp11);
				}
				if(numOpt>=12)
				{
					listOptions.add(mayRelOp12);
				}
				if(numOpt>=13)
				{
					listOptions.add(mayRelOp13);
				}
				if(numOpt>=14)
				{
					listOptions.add(mayRelOp14);
				}
				if(numOpt>=15)
				{
					listOptions.add(mayRelOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedMayRel=true; 
						}

					}
				}
				relativeMajority=new RelativeMajority(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromMayRelForm()
		{
			if(request.isXHR())
			{
				if(showErrorMayRel || showRepeatedMayRel)
				{
					ajaxResponseRenderer.addRender("mayRelZone", mayRelZone);
				}
				else //No hay errores
				{			
					ballot=setBallotData();
					relativeMajority.setId(UUID.generate());
					ballot.setIdBallotData(relativeMajority.getId());
					relativeMajority.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					relativeMajorityDao=DB4O.getRelativeMajorityDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						relativeMajority.setVotes(GenerateDocentVotes.generateRelativeMajority(relativeMajority.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
						}

					}

					relativeMajorityDao.store(relativeMajority);
					ballotDao.store(ballot);

					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////// KEMENY ZONE //////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		@InjectComponent
		private Zone kemenyZone;

		@Property
		@Persist
		private boolean showKemeny;
		@Property
		@Persist
		private boolean showErrorKemeny;

		@Property
		@Persist
		private boolean showRepeatKemeny;
		@Property
		@Persist
		private boolean showBadCharKemeny;

		@Persist
		private Kemeny kemeny;

		@Persist
		@Property
		@Validate("minLength=3")
		private String cat1;
		@Persist
		@Property
		@Validate("minLength=3")
		private String cat2;

		@Persist
		@Property
		@Validate("minLength=3")
		private String cat1_op1;
		@Persist
		@Property
		@Validate("minLength=3")
		private String cat1_op2;

		@Persist
		@Property
		@Validate("minLength=3")
		private String cat2_op1;
		@Persist
		@Property
		@Validate("minLength=3")
		private String cat2_op2;
		/**
		 * Checks if Kameny options are correct
		 */
		public void onValidateFromKemenyForm()
		{
			showErrorKemeny=false;
			showRepeatKemeny=false;
			showBadCharKemeny=false;

			if(cat1==null || cat2==null || cat1_op1==null || cat1_op2==null || cat2_op1==null || cat2_op2==null )
			{
				showErrorKemeny=true;
			}
			if(cat1.toLowerCase().equals(cat2))
			{
				showRepeatKemeny=true;
			}
			if(cat1_op1.toLowerCase().equals(cat1_op2) || cat1_op1.toLowerCase().equals(cat2_op1) || cat1_op1.toLowerCase().equals(cat2_op2))
			{
				showRepeatKemeny=true;
			}
			if(cat1_op2.toLowerCase().equals(cat2_op1.toLowerCase()) || cat1_op2.toLowerCase().equals(cat2_op1.toLowerCase()) ||cat1_op2.toLowerCase().equals(cat2_op2.toLowerCase()))
			{
				showRepeatKemeny=true;

			}
			if(cat2_op1.toLowerCase().equals(cat2_op2.toLowerCase()))
			{
				showRepeatKemeny=true;
			}

			//Comprobación de caracter "-". No puede estar ya que lo utiliza la biblioteca de calculo para separar argumentos


			if(cat1_op1.contains("-")||cat1_op2.contains("-")||cat2_op1.contains("-")||cat2_op2.contains("-"))
			{
				showBadCharKemeny=true;
			}

			if(showErrorKemeny==false && showRepeatKemeny==false && showBadCharKemeny==false)
			{
				List<String> categories=new LinkedList<String>();
				categories.add(cat1);
				categories.add(cat2);
				List<String> option1=new LinkedList<String>();
				option1.add(cat1_op1);
				option1.add(cat1_op2);
				List<String> option2=new LinkedList<String>();
				option2.add(cat2_op1);
				option2.add(cat2_op2);
				List<List<String>>options=new LinkedList<List<String>>();
				options.add(option1);
				options.add(option2);


				kemeny=new Kemeny(options,categories);
			}
		} 

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromKemenyForm()
		{
			if(showErrorKemeny || showRepeatKemeny || showBadCharKemeny)
			{
				ajaxResponseRenderer.addRender("kemenyZone",kemenyZone);
			}
			else
			{
				ballot=setBallotData();
				kemeny.setId(UUID.generate());
				ballot.setIdBallotData(kemeny.getId());
				kemeny.setBallotId(ballot.getId());

				voteDao=DB4O.getVoteDao(datasession.getDBName());
				kemenyDao=DB4O.getKemenyDao(datasession.getDBName());

				if(ballot.isTeaching())
				{
					ballot.setIdCensus("none");
					kemeny.setVotes(GenerateDocentVotes.generateKemeny(kemeny.getOptionPairs(), Integer.parseInt(census)));
					ballot.setEnded(true);
					Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)

					ballot.setCounted(true);
					voteDao.store(vote);

				}else if(ballotKind==BallotKind.PUBLICA){
					ballot.setIdCensus("none");
				}
				else//Votacion Normal
				{
					boolean creatorInCensus=false;
					
					for(String idUser:censusNormal.getUsersCounted())
					{
						if(idUser.equals(datasession.getId())){creatorInCensus=true;}

						voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
					}
					if(!creatorInCensus)
					{
						// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
						// this.sendMail(datasession.getId(),ballot);
					}

				}

				kemenyDao.store(kemeny);
				ballotDao.store(ballot);
				ballotIdSesion = ballot.getId();
				if(ballotKind==BallotKind.DOCENTE)
					return BallotWasCreated.class;
				else
					return AddImages.class;

			}
			return null;
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////// BORDA ZONE ///////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone bordaZone;

		@InjectComponent
		private Zone bordaForm1Zone;

		@InjectComponent
		private Zone bordaForm2Zone;

		@Persist
		@Property
		private boolean showBorda;


		@Property
		@Persist 
		private String [] bordaModel;

		@Persist
		private int bordaOpt1;

		@Property
		@Persist
		@Validate("required")
		private String bordaNumOpt1;


		@Persist
		@Validate("required")
		private int bordaOpt2;


		@Property
		@Persist
		@Validate("required")
		private String bordaNumOpt2;

		@Property
		@Persist
		private String bcat1;
		@Property
		@Persist
		private String b1Opt1;
		@Property
		@Persist
		private String b1Opt2;
		@Property
		@Persist
		private String b1Opt3;
		@Property
		@Persist
		private String b1Opt4;
		@Property
		@Persist
		private String b1Opt5;
		@Property
		@Persist
		private String b1Opt6;
		@Property
		@Persist
		private String b1Opt7;

		@Persist
		@Property
		private boolean showBordaFill;

		@Persist
		@Property
		private boolean showBordaRepeat;

		@Persist
		@Property
		private boolean showBordaBadChar;

		//Primera zona

		public boolean isShowB1Opt3()
		{
			if (bordaOpt1>=3)
				return true;
			else
				return false;
		}
		public boolean isShowB1Opt4()
		{
			if (bordaOpt1>=4)
				return true;
			else
				return false;
		}
		public boolean isShowB1Opt5()
		{
			if (bordaOpt1>=5)
				return true;
			else
				return false;
		}
		public boolean isShowB1Opt6()
		{
			if (bordaOpt1>=6)
				return true;
			else
				return false;
		}
		public boolean isShowB1Opt7()
		{
			if (bordaOpt1>=7)
				return true;
			else
				return false;
		}
		@Property
		@Persist
		private String bcat2;
		@Property
		@Persist
		private String b2Opt1;
		@Property
		@Persist
		private String b2Opt2;
		@Property
		@Persist
		private String b2Opt3;
		@Property
		@Persist
		private String b2Opt4;
		@Property
		@Persist
		private String b2Opt5;
		@Property
		@Persist
		private String b2Opt6;
		@Property
		@Persist
		private String b2Opt7;

		@Persist
		private List<String> bordaList1;
		@Persist
		private List<String> bordaList2;

		public boolean isShowB2Opt3()
		{
			if (bordaOpt2>=3)
				return true;
			else
				return false;
		}
		public boolean isShowB2Opt4()
		{
			if (bordaOpt2>=4)
				return true;
			else
				return false;
		}
		public boolean isShowB2Opt5()
		{
			if (bordaOpt2>=5)
				return true;
			else
				return false;
		}
		public boolean isShowB2Opt6()
		{
			if (bordaOpt2>=6)
				return true;
			else
				return false;
		}
		public boolean isShowB2Opt7()
		{
			if (bordaOpt2>=7)
				return true;
			else
				return false;
		}

		public void  onValueChangedFromBordaSel1(String str)
		{
			bordaOpt1=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("bordaForm1Zone", bordaForm1Zone);
		}


		public void  onValueChangedFromBordaSel2(String str)
		{
			bordaOpt2=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("bordaForm2Zone", bordaForm2Zone);
		}


		public void onValidateFromBordaForm()
		{
			showBordaFill=false;
			showBordaRepeat=false;
			showBordaBadChar=false;
			bordaList1=new LinkedList<String>();
			bordaList2=new LinkedList<String>();
			if(bcat1==null || bcat2==null)
			{
				showBordaFill=true;
			}
			switch(bordaOpt1)
			{
			case 7:
				if(b1Opt7==null){showBordaFill=true;}
				else if(b1Opt7.contains("-")){showBordaBadChar=true;}
			case 6:
				if(b1Opt6==null){showBordaFill=true;}
				else if(b1Opt6.contains("-")){showBordaBadChar=true;}
			case 5:
				if(b1Opt5==null){showBordaFill=true;}
				else if(b1Opt5.contains("-")){showBordaBadChar=true;}
			case 4:
				if(b1Opt4==null){showBordaFill=true;}
				else if(b1Opt4.contains("-")){showBordaBadChar=true;}
			case 3:
				if(b1Opt3==null){showBordaFill=true;}
				else if(b1Opt3.contains("-")){showBordaBadChar=true;}
			case 2:
				if(b1Opt2==null){showBordaFill=true;}
				else if(b1Opt2.contains("-")){showBordaBadChar=true;}
				if(b1Opt1==null){showBordaFill=true;}
				else if(b1Opt1.contains("-")){showBordaBadChar=true;}
				break;
			default:
				showBordaFill=true;
			}
			switch(bordaOpt2)
			{
			case 7:
				if(b2Opt7==null){showBordaFill=true;}
				else if(b2Opt7.contains("-")){showBordaBadChar=true;}
			case 6:
				if(b2Opt6==null){showBordaFill=true;}
				else if(b2Opt6.contains("-")){showBordaBadChar=true;}
			case 5:
				if(b2Opt5==null){showBordaFill=true;}
				else if(b2Opt5.contains("-")){showBordaBadChar=true;}
			case 4:
				if(b2Opt4==null){showBordaFill=true;}
				else if(b2Opt4.contains("-")){showBordaBadChar=true;}
			case 3:
				if(b2Opt3==null){showBordaFill=true;}
				else if(b2Opt3.contains("-")){showBordaBadChar=true;}
			case 2:
				if(b2Opt2==null){showBordaFill=true;}
				else if(b2Opt2.contains("-")){showBordaBadChar=true;}
				if(b2Opt1==null){showBordaFill=true;}
				else if(b2Opt1.contains("-")){showBordaBadChar=true;}
				break;
			default:
				showBordaFill=true;
			}
			if(showBordaFill==false)
			{
				bordaList1.add(b1Opt1);
				bordaList1.add(b1Opt2);
				bordaList2.add(b2Opt1);
				bordaList2.add(b2Opt2);

				if(bordaOpt1>=3)
					bordaList1.add(b1Opt3);

				if(bordaOpt2>=3)
					bordaList2.add(b2Opt3);

				if(bordaOpt1>=4)
					bordaList1.add(b1Opt4);

				if(bordaOpt2>=4)
					bordaList2.add(b2Opt4);

				if(bordaOpt1>=5)
					bordaList1.add(b1Opt5);

				if(bordaOpt2>=5)
					bordaList2.add(b2Opt5);

				if(bordaOpt1>=6)
					bordaList1.add(b1Opt6);

				if(bordaOpt2>=6)
					bordaList2.add(b2Opt6);

				if(bordaOpt1>=7)
					bordaList1.add(b1Opt7);

				if(bordaOpt2>=7)
					bordaList2.add(b2Opt7);



				for(int x=0;x<bordaList1.size();x++)
				{
					for(int i=x+1;i<bordaList1.size();i++)
					{
						if(bordaList1.get(x).toLowerCase().equals(bordaList1.get(i).toLowerCase()))
							showBordaRepeat=true;
					}
					for(int i=0;i<bordaList2.size();i++)
					{
						if(bordaList1.get(x).toLowerCase().toLowerCase().equals(bordaList2.get(i).toLowerCase()))
							showBordaRepeat=true;
					}

				}
				for(int x=0;x<bordaList2.size();x++)
				{
					for(int i=x+1;i<bordaList2.size();i++)
					{
						if(bordaList2.get(x).toLowerCase().equals(bordaList2.get(i).toLowerCase()))
							showBordaRepeat=true;
					}
				}


			}


		}


		public Object onSuccessFromBordaForm()
		{
			if(request.isXHR())
			{
				if(showBordaRepeat || showBordaFill || showBordaBadChar)
				{
					ajaxResponseRenderer.addRender("bordaZone", bordaZone);
				}
				else
				{
					List<List<String>> listaOpciones=new LinkedList<List<String>>();
					listaOpciones.add(bordaList1);
					listaOpciones.add(bordaList2);
					Borda borda=new Borda(listaOpciones);
					borda.addCategory(bcat1);
					borda.addCategory(bcat2);
					ballot=setBallotData();
					borda.setId(UUID.generate());
					ballot.setIdBallotData(borda.getId());
					borda.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					bordaDao=DB4O.getBordaDao(datasession.getDBName());

					if(ballot.isTeaching())
					{
						ballot.setIdCensus("none");
						borda.setVotes(GenerateDocentVotes.generateBorda(borda.getBordaOptions(), Integer.parseInt(census)));
						ballot.setEnded(true);
						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
	
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else//Votacion Normal
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//this.sendMail(datasession.getId(),ballot);
						}

					}

					bordaDao.store(borda);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;

				}

			}
			return null;
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// RANGE ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone rangeZone;
		@Property
		@Persist
		private boolean showRange;

		@Property
		@Persist
		private RangeVoting range;


		@Property
		@Persist 
		private String [] rangeModel;


		@Persist
		@Property
		private int rangeOpt;

		@Property
		@Persist
		@Validate("required")
		private String rangeNumOpt;

		@Property
		@Persist
		private boolean showRangeMax;
		@Property
		@Persist
		private boolean showRangeValues;
		@Property
		@Persist
		private boolean showRangeFill;
		@Property
		@Persist
		private boolean showRangeBadChar;
		@Property
		@Persist
		private boolean showRangeBadNum;
		@Property
		@Persist
		private boolean showRangeRepeated;


		@Property
		@Persist
		private String maxRange;
		@Property
		@Persist
		private String minRange;

		@Property
		@Persist
		private String rangeOpt1;
		@Property
		@Persist
		private String rangeOpt2;
		@Property
		@Persist
		private String rangeOpt3;
		@Property
		@Persist
		private String rangeOpt4;
		@Property
		@Persist
		private String rangeOpt5;
		@Property
		@Persist
		private String rangeOpt6;
		@Property
		@Persist
		private String rangeOpt7;
		@Property
		@Persist
		private List<String> rangeOptions;

		public boolean isShowRange3()
		{
			if (rangeOpt>=3)
				return true;
			else
				return false;
		}
		public boolean isShowRange4()
		{
			if (rangeOpt>=4)
				return true;
			else
				return false;
		}
		public boolean isShowRange5()
		{
			if (rangeOpt>=5)
				return true;
			else
				return false;
		}
		public boolean isShowRange6()
		{
			if (rangeOpt>=6)
				return true;
			else
				return false;
		}
		public boolean isShowRange7()
		{
			if (rangeOpt>=7)
				return true;
			else
				return false;
		}
		public void  onValueChangedFromRangeSel(String str)
		{
			rangeOpt=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("rangeZone", rangeZone);
		}

		public void onValidateFromRangeForm()
		{
			showRangeMax=false;
			showRangeValues=false;
			showRangeRepeated=false;
			showRangeBadNum=false;
			showRangeBadNum=false;
			showRangeFill=false;

			if(minRange==null)
			{
				minRange="0";
			}
			else if(isNumeric(minRange))
			{
				showRangeBadNum=true;
			}

			if(maxRange==null)
			{
				showRangeMax=true;
			}
			else
			{
				if(isNumeric(maxRange))
				{
					showRangeBadNum=true;
				}
				else if(Integer.parseInt(minRange)>=Integer.parseInt(maxRange))
				{
					showRangeValues=true;
				}
			}
			switch(rangeOpt)
			{
			case 7:
				if(rangeOpt7==null){showRangeFill=true;}
				else if(rangeOpt7.contains("ñ")){showRangeBadChar=true;}
			case 6:
				if(rangeOpt6==null){showRangeFill=true;}
				else if(rangeOpt6.contains("ñ")){showRangeBadChar=true;}
			case 5:
				if(rangeOpt5==null){showRangeFill=true;}
				else if(rangeOpt5.contains("ñ")){showRangeBadChar=true;}
			case 4:
				if(rangeOpt4==null){showRangeFill=true;}
				else if(rangeOpt4.contains("ñ")){showRangeBadChar=true;}
			case 3:
				if(rangeOpt3==null){showRangeFill=true;}
				else if(rangeOpt3.contains("ñ")){showRangeBadChar=true;}
			case 2:
				if(rangeOpt2==null){showRangeFill=true;}
				else if(rangeOpt2.contains("ñ")){showRangeBadChar=true;}
				if(rangeOpt1==null){showRangeFill=true;}
				else if(rangeOpt1.contains("ñ")){showRangeBadChar=true;}
				break;
			default:
				showRangeFill=true;
			}

			if(showRangeFill==false)
			{
				rangeOptions=new LinkedList<String>();
				rangeOptions.add(rangeOpt1);
				rangeOptions.add(rangeOpt2);

				if(rangeOpt>=3)
					rangeOptions.add(rangeOpt3);

				if(rangeOpt>=4)
					rangeOptions.add(rangeOpt4);
				if(rangeOpt>=5)
					rangeOptions.add(rangeOpt5);
				if(rangeOpt>=6)
					rangeOptions.add(rangeOpt6);
				if(rangeOpt>=7)
					rangeOptions.add(rangeOpt7);

			}
			for(int x=0;x<rangeOptions.size();x++)
			{
				for(int i=x+1;i<rangeOptions.size();i++)
				{
					if(rangeOptions.get(x).toLowerCase().equals(rangeOptions.get(i).toLowerCase()))
					{
						showRangeRepeated=true;
					}
				}
			}

		}
		public Object onSuccessFromRangeForm()
		{
			if(request.isXHR())
			{
				if(showRangeBadChar || showRangeRepeated || showRangeFill || showRangeValues || showRangeMax)
				{
					ajaxResponseRenderer.addRender("rangeZone", rangeZone);
					return null;
				}
				range=new RangeVoting(rangeOptions);

				ballot=setBallotData();
				range.setId(UUID.generate());
				ballot.setIdBallotData(range.getId());
				range.setBallotId(ballot.getId());
				range.setMinValue(Integer.parseInt(minRange));

				range.setMaxValue(Integer.parseInt(maxRange));

				System.out.println("MINIMO->"+range.getMinValue());
				System.out.println("MAXIMO->"+range.getMaxValue());
				voteDao=DB4O.getVoteDao(datasession.getDBName());
				rangeDao=DB4O.getRangeVotingDao(datasession.getDBName());
				if(ballot.isTeaching())
				{
					ballot.setIdCensus("none");
					range.setVotes(GenerateDocentVotes.generateRangeVoting(range.getOptions(), Integer.parseInt(census),range.getMinValue(), range.getMaxValue()));
					ballot.setEnded(true);
					Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)

					ballot.setEnded(true);
					ballot.setCounted(true);
					voteDao.store(vote);
				}
				else if(ballotKind==BallotKind.PUBLICA){
					ballot.setIdCensus("none");
				}
				else//Votacion Normal
				{
					boolean creatorInCensus=false;
					
					for(String idUser:censusNormal.getUsersCounted())
					{
						if(idUser.equals(datasession.getId())){creatorInCensus=true;}

						voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
					}
					if(!creatorInCensus)
					{
						// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
						// this.sendMail(datasession.getId(),ballot);
					}

				}

				rangeDao.store(range);
				ballotDao.store(ballot);
				ballotIdSesion = ballot.getId();
				if(ballotKind==BallotKind.DOCENTE)
					return BallotWasCreated.class;
				else
					return AddImages.class;
			}
			return null;
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// Approval ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone approvalZone;
		@Property
		@Persist
		private boolean showApproval;
		@Property
		@Persist
		private boolean showErrorApproval;
		@Property
		@Persist
		private boolean showRepeatedApproval;
		@Persist
		private ApprovalVoting approvalVoting;

		@Property
		@Persist 
		@Validate("required")
		private String approvalNumOp;
		@Persist
		private int numOptApproval;
		@Property
		@Persist 
		private String [] approvalModel;

		@Property
		@Persist
		private String approvalOp1;
		@Property
		@Persist
		private String approvalOp2;
		@Property
		@Persist
		private String approvalOp3;
		@Property
		@Persist
		private String approvalOp4;
		@Property
		@Persist
		private String approvalOp5;
		@Property
		@Persist
		private String approvalOp6;
		@Property
		@Persist
		private String approvalOp7;
		@Property
		@Persist
		private String approvalOp8;
		@Property
		@Persist
		private String approvalOp9;
		@Property
		@Persist
		private String approvalOp10;
		@Property
		@Persist
		private String approvalOp11;
		@Property
		@Persist
		private String approvalOp12;
		@Property
		@Persist
		private String approvalOp13;
		@Property
		@Persist
		private String approvalOp14;
		@Property
		@Persist
		private String approvalOp15;


		private String optionApproval;
		public String getOptionApproval() {
			return optionApproval;
		}
		public void setOptionApproval(String optionApproval) {
			this.optionApproval = optionApproval;
		}

		/**
		 * Controls the number of options for the approval voting
		 * @param str
		 */
		public void  onValueChangedFromApprovalSel(String str)
		{
			numOptApproval=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("approvalZone", approvalZone);
		}


		public boolean isShowApproval3()
		{
			if(numOptApproval>=3)
				return true;
			return false;
		}
		public boolean isShowApproval4()
		{
			if(numOptApproval>=4)
				return true;
			return false;
		}
		public boolean isShowApproval5()
		{
			if(numOptApproval>=5)
				return true;
			return false;
		}
		public boolean isShowApproval6()
		{
			if(numOptApproval>=6)
				return true;
			return false;
		}
		public boolean isShowApproval7()
		{
			if(numOptApproval>=7)
				return true;
			return false;
		}
		public boolean isShowApproval8()
		{
			if(numOptApproval>=8)
				return true;
			return false;
		}
		public boolean isShowApproval9()
		{
			if(numOptApproval>=9)
				return true;
			return false;
		}
		public boolean isShowApproval10()
		{
			if(numOptApproval>=10)
				return true;
			return false;
		}
		public boolean isShowApproval11()
		{
			if(numOptApproval>=11)
				return true;
			return false;
		}
		public boolean isShowApproval12()
		{
			if(numOptApproval>=12)
				return true;
			return false;
		}
		public boolean isShowApproval13()
		{
			if(numOptApproval>=13)
				return true;
			return false;
		}
		public boolean isShowApproval14()
		{
			if(numOptApproval>=14)
				return true;
			return false;
		}
		public boolean isShowApproval15()
		{
			if(numOptApproval>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the approval voting
		 */
		public void onValidateFromApprovalForm()
		{
			showErrorApproval=false;
			showRepeatedApproval=false;
			if(approvalOp1==null || approvalOp2==null)
			{

				showErrorApproval=true;
			}

			System.out.println("NUMOP->"+numOptApproval);
			switch(numOptApproval)
			{
			case 15:
				if(approvalOp15==null){showErrorApproval=true;}
			case 14:
				if(approvalOp14==null){showErrorApproval=true;}
			case 13:
				if(approvalOp13==null){showErrorApproval=true;}
			case 12:
				if(approvalOp12==null){showErrorApproval=true;}
			case 11:
				if(approvalOp11==null){showErrorApproval=true;}
			case 10:
				if(approvalOp10==null){showErrorApproval=true;}
			case 9:
				if(approvalOp9==null){showErrorApproval=true;}
			case 8:
				if(approvalOp8==null){showErrorApproval=true;}
			case 7:
				if(approvalOp7==null){showErrorApproval=true;}
			case 6:
				if(approvalOp6==null){showErrorApproval=true;}
			case 5:
				if(approvalOp5==null){showErrorApproval=true;}
			case 4:
				if(approvalOp4==null){showErrorApproval=true;}
			case 3:
				if(approvalOp3==null){showErrorApproval=true;}
				break;
			default:
				showErrorApproval=false;
			}
			if(!showErrorApproval)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(approvalOp1);
				listOptions.add(approvalOp2);
				if(numOptApproval>=3)
				{
					listOptions.add(approvalOp3);
				}
				if(numOptApproval>=4)
				{
					listOptions.add(approvalOp4);
				}
				if(numOptApproval>=5)
				{
					listOptions.add(approvalOp5);
				}
				if(numOptApproval>=6)
				{
					listOptions.add(approvalOp6);
				}
				if(numOptApproval>=7)
				{
					listOptions.add(approvalOp7);
				}
				if(numOptApproval>=8)
				{
					listOptions.add(approvalOp8);
				}
				if(numOptApproval>=9)
				{
					listOptions.add(approvalOp9);
				}
				if(numOptApproval>=10)
				{
					listOptions.add(approvalOp10);
				}
				if(numOptApproval>=11)
				{
					listOptions.add(approvalOp11);
				}
				if(numOptApproval>=12)
				{
					listOptions.add(approvalOp12);
				}
				if(numOptApproval>=13)
				{
					listOptions.add(approvalOp13);
				}
				if(numOptApproval>=14)
				{
					listOptions.add(approvalOp14);
				}
				if(numOptApproval>=15)
				{
					listOptions.add(approvalOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedApproval=true; 
						}

					}
				}
				approvalVoting=new ApprovalVoting(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromApprovalForm()
		{
			if(request.isXHR())
			{
				if(showErrorApproval || showRepeatedApproval)
				{
					ajaxResponseRenderer.addRender("approvalZone", approvalZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					approvalVoting.setId(UUID.generate());
					ballot.setIdBallotData(approvalVoting.getId());
					approvalVoting.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					approvalVotingDao=DB4O.getApprovalVotingDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						approvalVoting.setVotes(GenerateDocentVotes.generateApprovalVoting(approvalVoting.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					approvalVotingDao.store(approvalVoting);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}



		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// BRAMS ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		@Persist
		Brams brams;
		@InjectComponent
		private Zone bramsZone;
		@Property
		@Persist
		private boolean showBrams;
		@Property
		@Persist
		private boolean showErrorBrams;
		@Property
		@Persist
		private boolean showRepeatedBrams;

		@Property
		@Persist 
		private String bramsNumOp;
		@Persist
		private int numOptBrams;
		@Property
		@Persist 
		private String [] bramsModel;

		@Property
		@Persist
		private String bramsOp1;
		@Property
		@Persist
		private String bramsOp2;
		@Property
		@Persist
		private String bramsOp3;
		@Property
		@Persist
		private String bramsOp4;
		@Property
		@Persist
		private String bramsOp5;
		@Property
		@Persist
		private String bramsOp6;
		@Property
		@Persist
		private String bramsOp7;
		@Property
		@Persist
		private String bramsOp8;
		@Property
		@Persist
		private String bramsOp9;
		@Property
		@Persist
		private String bramsOp10;
		@Property
		@Persist
		private String bramsOp11;
		@Property
		@Persist
		private String bramsOp12;
		@Property
		@Persist
		private String bramsOp13;
		@Property
		@Persist
		private String bramsOp14;
		@Property
		@Persist
		private String bramsOp15;


		private String optionBrams;
		public String getOptionBrams() {
			return optionBrams;
		}
		public void setOptionBrams(String optionBrams) {
			this.optionBrams = optionBrams;
		}
		public boolean isShowBrams8()
		{
			if(numOptBrams>=8)
				return true;
			return false;
		}
		public boolean isShowBrams9()
		{
			if(numOptBrams>=9)
				return true;
			return false;
		}
		public boolean isShowBrams10()
		{
			if(numOptBrams>=10)
				return true;
			return false;
		}
		public boolean isShowBrams11()
		{
			if(numOptBrams>=11)
				return true;
			return false;
		}
		public boolean isShowBrams12()
		{
			if(numOptBrams>=12)
				return true;
			return false;
		}
		public boolean isShowBrams13()
		{
			if(numOptBrams>=13)
				return true;
			return false;
		}
		public boolean isShowBrams14()
		{
			if(numOptBrams>=14)
				return true;
			return false;
		}
		public boolean isShowBrams15()
		{
			if(numOptBrams>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the brams voting
		 */
		public void onValidateFromBramsForm()
		{
			showErrorBrams=false;
			showRepeatedBrams=false;
			if(bramsOp1==null || bramsOp2==null || bramsOp3==null || bramsOp4==null || bramsOp5==null || bramsOp6==null || bramsOp7==null)
			{
				showErrorBrams=true;
			}

			switch(numOptBrams)
			{
			case 15:
				if(bramsOp15==null){showErrorBrams=true;}
			case 14:
				if(bramsOp14==null){showErrorBrams=true;}
			case 13:
				if(bramsOp13==null){showErrorBrams=true;}
			case 12:
				if(bramsOp12==null){showErrorBrams=true;}
			case 11:
				if(bramsOp11==null){showErrorBrams=true;}
			case 10:
				if(bramsOp10==null){showErrorBrams=true;}
			case 9:
				if(bramsOp9==null){showErrorBrams=true;}
			case 8:
				if(bramsOp8==null){showErrorBrams=true;}
				break;
			default:
				showErrorBrams=false;
			}
			if(!showErrorBrams)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(bramsOp1);
				listOptions.add(bramsOp2);
				listOptions.add(bramsOp3);
				listOptions.add(bramsOp4);
				listOptions.add(bramsOp5);
				listOptions.add(bramsOp6);
				listOptions.add(bramsOp7);

				if(numOptBrams>=8)
				{
					listOptions.add(bramsOp8);
				}
				if(numOptBrams>=9)
				{
					listOptions.add(bramsOp9);
				}
				if(numOptBrams>=10)
				{
					listOptions.add(bramsOp10);
				}
				if(numOptBrams>=11)
				{
					listOptions.add(bramsOp11);
				}
				if(numOptBrams>=12)
				{
					listOptions.add(bramsOp12);
				}
				if(numOptBrams>=13)
				{
					listOptions.add(bramsOp13);
				}
				if(numOptBrams>=14)
				{
					listOptions.add(bramsOp14);
				}
				if(numOptBrams>=15)
				{
					listOptions.add(bramsOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedBrams=true; 
						}

					}
				}
				brams.setOptions(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromBramsForm()
		{
			if(request.isXHR())
			{
				if(showErrorBrams || showRepeatedBrams)
				{
					ajaxResponseRenderer.addRender("bramsZone", bramsZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					brams.setId(brams.getBallotId());
					ballot.setIdBallotData(brams.getId());
					brams.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					bramsDao=DB4O.getBramsDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						List<List<String>> votosdocentes;
						System.out.println("Llamada a generar votos docentes con: "+Integer.parseInt(census)+" votos");

						votosdocentes = GenerateDocentVotes.generateBrams(brams.getOptions(), Integer.parseInt(census));
						brams.setVotes(votosdocentes);
						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);
						//
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}
					brams.setVotes(brams.getVotes());
					bramsDao.update(brams);
					ballotDao.updateBallot(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// VotoAcumulativo ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone votoAcumulativoZone;
		@Property
		@Persist
		private boolean showVotoAcumulativo;
		@Property
		@Persist
		private boolean showErrorVotoAcumulativo;
		@Property
		@Persist
		private boolean showRepeatedVotoAcumulativo;
		@Persist
		private VotoAcumulativo votoAcumulativo;

		@Property
		@Persist 
		@Validate("required")
		private String votoAcumulativoNumOp;
		@Persist
		private int numOptVotoAcumulativo;
		@Property
		@Persist 
		private String [] votoAcumulativoModel;

		@Property
		@Persist
		private String votoAcumulativoOp1;
		@Property
		@Persist
		private String votoAcumulativoOp2;
		@Property
		@Persist
		private String votoAcumulativoOp3;
		@Property
		@Persist
		private String votoAcumulativoOp4;
		@Property
		@Persist
		private String votoAcumulativoOp5;
		@Property
		@Persist
		private String votoAcumulativoOp6;
		@Property
		@Persist
		private String votoAcumulativoOp7;
		@Property
		@Persist
		private String votoAcumulativoOp8;
		@Property
		@Persist
		private String votoAcumulativoOp9;
		@Property
		@Persist
		private String votoAcumulativoOp10;
		@Property
		@Persist
		private String votoAcumulativoOp11;
		@Property
		@Persist
		private String votoAcumulativoOp12;
		@Property
		@Persist
		private String votoAcumulativoOp13;
		@Property
		@Persist
		private String votoAcumulativoOp14;
		@Property
		@Persist
		private String votoAcumulativoOp15;


		private String optionVotoAcumulativo;
		public String getOptionVotoAcumulativo() {
			return optionVotoAcumulativo;
		}
		public void setOptionVotoAcumulativo(String optionVotoAcumulativo) {
			this.optionVotoAcumulativo = optionVotoAcumulativo;
		}

		/**
		 * Controls the number of options for the votoAcumulativo voting
		 * @param str
		 */
		public void  onValueChangedFromVotoAcumulativoSel(String str)
		{
			numOptVotoAcumulativo=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("votoAcumulativoZone", votoAcumulativoZone);
		}


		public boolean isShowVotoAcumulativo3()
		{
			if(numOptVotoAcumulativo>=3)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo4()
		{
			if(numOptVotoAcumulativo>=4)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo5()
		{
			if(numOptVotoAcumulativo>=5)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo6()
		{
			if(numOptVotoAcumulativo>=6)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo7()
		{
			if(numOptVotoAcumulativo>=7)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo8()
		{
			if(numOptVotoAcumulativo>=8)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo9()
		{
			if(numOptVotoAcumulativo>=9)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo10()
		{
			if(numOptVotoAcumulativo>=10)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo11()
		{
			if(numOptVotoAcumulativo>=11)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo12()
		{
			if(numOptVotoAcumulativo>=12)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo13()
		{
			if(numOptVotoAcumulativo>=13)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo14()
		{
			if(numOptVotoAcumulativo>=14)
				return true;
			return false;
		}
		public boolean isShowVotoAcumulativo15()
		{
			if(numOptVotoAcumulativo>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the votoAcumulativo voting
		 */
		public void onValidateFromVotoAcumulativoForm()
		{
			showErrorVotoAcumulativo=false;
			showRepeatedVotoAcumulativo=false;
			if(votoAcumulativoOp1==null || votoAcumulativoOp2==null)
			{

				showErrorVotoAcumulativo=true;
			}

			switch(numOptVotoAcumulativo)
			{
			case 15:
				if(votoAcumulativoOp15==null){showErrorVotoAcumulativo=true;}
			case 14:
				if(votoAcumulativoOp14==null){showErrorVotoAcumulativo=true;}
			case 13:
				if(votoAcumulativoOp13==null){showErrorVotoAcumulativo=true;}
			case 12:
				if(votoAcumulativoOp12==null){showErrorVotoAcumulativo=true;}
			case 11:
				if(votoAcumulativoOp11==null){showErrorVotoAcumulativo=true;}
			case 10:
				if(votoAcumulativoOp10==null){showErrorVotoAcumulativo=true;}
			case 9:
				if(votoAcumulativoOp9==null){showErrorVotoAcumulativo=true;}
			case 8:
				if(votoAcumulativoOp8==null){showErrorVotoAcumulativo=true;}
			case 7:
				if(votoAcumulativoOp7==null){showErrorVotoAcumulativo=true;}
			case 6:
				if(votoAcumulativoOp6==null){showErrorVotoAcumulativo=true;}
			case 5:
				if(votoAcumulativoOp5==null){showErrorVotoAcumulativo=true;}
			case 4:
				if(votoAcumulativoOp4==null){showErrorVotoAcumulativo=true;}
			case 3:
				if(votoAcumulativoOp3==null){showErrorVotoAcumulativo=true;}
				break;
			default:
				showErrorVotoAcumulativo=false;
			}
			if(!showErrorVotoAcumulativo)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(votoAcumulativoOp1);
				listOptions.add(votoAcumulativoOp2);
				if(numOptVotoAcumulativo>=3)
				{
					listOptions.add(votoAcumulativoOp3);
				}
				if(numOptVotoAcumulativo>=4)
				{
					listOptions.add(votoAcumulativoOp4);
				}
				if(numOptVotoAcumulativo>=5)
				{
					listOptions.add(votoAcumulativoOp5);
				}
				if(numOptVotoAcumulativo>=6)
				{
					listOptions.add(votoAcumulativoOp6);
				}
				if(numOptVotoAcumulativo>=7)
				{
					listOptions.add(votoAcumulativoOp7);
				}
				if(numOptVotoAcumulativo>=8)
				{
					listOptions.add(votoAcumulativoOp8);
				}
				if(numOptVotoAcumulativo>=9)
				{
					listOptions.add(votoAcumulativoOp9);
				}
				if(numOptVotoAcumulativo>=10)
				{
					listOptions.add(votoAcumulativoOp10);
				}
				if(numOptVotoAcumulativo>=11)
				{
					listOptions.add(votoAcumulativoOp11);
				}
				if(numOptVotoAcumulativo>=12)
				{
					listOptions.add(votoAcumulativoOp12);
				}
				if(numOptVotoAcumulativo>=13)
				{
					listOptions.add(votoAcumulativoOp13);
				}
				if(numOptVotoAcumulativo>=14)
				{
					listOptions.add(votoAcumulativoOp14);
				}
				if(numOptVotoAcumulativo>=15)
				{
					listOptions.add(votoAcumulativoOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedVotoAcumulativo=true; 
						}

					}
				}
				votoAcumulativo=new VotoAcumulativo(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromVotoAcumulativoForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorVotoAcumulativo || showRepeatedVotoAcumulativo)
				{
					ajaxResponseRenderer.addRender("votoAcumulativoZone", votoAcumulativoZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					votoAcumulativo.setId(UUID.generate());
					ballot.setIdBallotData(votoAcumulativo.getId());
					votoAcumulativo.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					votoAcumulativoDao=DB4O.getVotoAcumulativoDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						votoAcumulativo.setVotes(GenerateDocentVotes.generateVotoAcumulativo(votoAcumulativo.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					votoAcumulativoDao.store(votoAcumulativo);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// JuicioMayoritario ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone juicioMayoritarioZone;
		@Property
		@Persist
		private boolean showJuicioMayoritario;
		@Property
		@Persist
		private boolean showErrorJuicioMayoritario;
		@Property
		@Persist
		private boolean showRepeatedJuicioMayoritario;
		@Persist
		private JuicioMayoritario juicioMayoritario;

		@Property
		@Persist 
		@Validate("required")
		private String juicioMayoritarioNumOptions;
		@Persist
		private int numOptJuicioMayoritario;
		@Property
		@Persist 
		private String [] juicioMayoritarioModel;

		@Property
		@Persist
		private String juicioMayoritarioOption1;
		@Property
		@Persist
		private String juicioMayoritarioOption2;
		@Property
		@Persist
		private String juicioMayoritarioOption3;
		@Property
		@Persist
		private String juicioMayoritarioOption4;
		@Property
		@Persist
		private String juicioMayoritarioOption5;
		@Property
		@Persist
		private String juicioMayoritarioOption6;
		@Property
		@Persist
		private String juicioMayoritarioOption7;
		@Property
		@Persist
		private String juicioMayoritarioOption8;
		@Property
		@Persist
		private String juicioMayoritarioOption9;
		@Property
		@Persist
		private String juicioMayoritarioOption10;
		@Property
		@Persist
		private String juicioMayoritarioOption11;
		@Property
		@Persist
		private String juicioMayoritarioOption12;
		@Property
		@Persist
		private String juicioMayoritarioOption13;
		@Property
		@Persist
		private String juicioMayoritarioOption14;
		@Property
		@Persist
		private String juicioMayoritarioOption15;


		private String optionJuicioMayoritario;
		public String getOptionJuicioMayoritario() {
			return optionJuicioMayoritario;
		}
		public void setOptionJuicioMayoritario(String optionJuicioMayoritario) {
			this.optionJuicioMayoritario = optionJuicioMayoritario;
		}

		/**
		 * Controls the number of options for the juicioMayoritario voting
		 * @param str
		 */
		public void  onValueChangedFromJuicioMayoritarioSel(String str)
		{
			numOptJuicioMayoritario=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("juicioMayoritarioZone", juicioMayoritarioZone);
		}

		public boolean isShowJuicioMayoritario8()
		{
			if(numOptJuicioMayoritario>=8)
				return true;
			return false;
		}
		public boolean isShowJuicioMayoritario9()
		{
			if(numOptJuicioMayoritario>=9)
				return true;
			return false;
		}
		public boolean isShowJuicioMayoritario10()
		{
			if(numOptJuicioMayoritario>=10)
				return true;
			return false;
		}
		public boolean isShowJuicioMayoritario11()
		{
			if(numOptJuicioMayoritario>=11)
				return true;
			return false;
		}
		public boolean isShowJuicioMayoritario12()
		{
			if(numOptJuicioMayoritario>=12)
				return true;
			return false;
		}
		public boolean isShowJuicioMayoritario13()
		{
			if(numOptJuicioMayoritario>=13)
				return true;
			return false;
		}
		public boolean isShowJuicioMayoritario14()
		{
			if(numOptJuicioMayoritario>=14)
				return true;
			return false;
		}
		public boolean isShowJuicioMayoritario15()
		{
			if(numOptJuicioMayoritario>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the juicioMayoritario voting
		 */
		public void onValidateFromJuicioMayoritarioForm()
		{
			showErrorJuicioMayoritario=false;
			showRepeatedJuicioMayoritario=false;
			if(juicioMayoritarioOption1==null || juicioMayoritarioOption2==null)
			{

				showErrorJuicioMayoritario=true;
			}

			switch(numOptJuicioMayoritario)
			{
			case 15:
				if(juicioMayoritarioOption15==null){showErrorJuicioMayoritario=true;}
			case 14:
				if(juicioMayoritarioOption14==null){showErrorJuicioMayoritario=true;}
			case 13:
				if(juicioMayoritarioOption13==null){showErrorJuicioMayoritario=true;}
			case 12:
				if(juicioMayoritarioOption12==null){showErrorJuicioMayoritario=true;}
			case 11:
				if(juicioMayoritarioOption11==null){showErrorJuicioMayoritario=true;}
			case 10:
				if(juicioMayoritarioOption10==null){showErrorJuicioMayoritario=true;}
			case 9:
				if(juicioMayoritarioOption9==null){showErrorJuicioMayoritario=true;}
			case 8:
				if(juicioMayoritarioOption8==null){showErrorJuicioMayoritario=true;}
				break;
			default:
				showErrorJuicioMayoritario=false;
			}
			if(!showErrorJuicioMayoritario)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(juicioMayoritarioOption1);
				listOptions.add(juicioMayoritarioOption2);
				listOptions.add(juicioMayoritarioOption3);
				listOptions.add(juicioMayoritarioOption4);
				listOptions.add(juicioMayoritarioOption5);
				listOptions.add(juicioMayoritarioOption6);
				listOptions.add(juicioMayoritarioOption7);

				if(numOptJuicioMayoritario>=8)
				{
					listOptions.add(juicioMayoritarioOption8);
				}
				if(numOptJuicioMayoritario>=9)
				{
					listOptions.add(juicioMayoritarioOption9);
				}
				if(numOptJuicioMayoritario>=10)
				{
					listOptions.add(juicioMayoritarioOption10);
				}
				if(numOptJuicioMayoritario>=11)
				{
					listOptions.add(juicioMayoritarioOption11);
				}
				if(numOptJuicioMayoritario>=12)
				{
					listOptions.add(juicioMayoritarioOption12);
				}
				if(numOptJuicioMayoritario>=13)
				{
					listOptions.add(juicioMayoritarioOption13);
				}
				if(numOptJuicioMayoritario>=14)
				{
					listOptions.add(juicioMayoritarioOption14);
				}
				if(numOptJuicioMayoritario>=15)
				{
					listOptions.add(juicioMayoritarioOption15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedJuicioMayoritario=true; 
						}

					}
				}
				juicioMayoritario=new JuicioMayoritario(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromJuicioMayoritarioForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorJuicioMayoritario || showRepeatedJuicioMayoritario)
				{
					ajaxResponseRenderer.addRender("juicioMayoritarioZone", juicioMayoritarioZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					juicioMayoritario.setId(UUID.generate());
					ballot.setIdBallotData(juicioMayoritario.getId());
					juicioMayoritario.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					juicioMayoritarioDao=DB4O.getJuicioMayoritarioDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						juicioMayoritario.setVotes(GenerateDocentVotes.generateJuicioMayoritario(juicioMayoritario.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					juicioMayoritarioDao.store(juicioMayoritario);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// MejorPeor ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone mejorPeorZone;
		@Property
		@Persist
		private boolean showMejorPeor;
		@Property
		@Persist
		private boolean showErrorMejorPeor;
		@Property
		@Persist
		private boolean showRepeatedMejorPeor;
		@Persist
		private MejorPeor mejorPeor;

		@Property
		@Persist 
		@Validate("required")
		private String mejorPeorNumOptions;
		@Persist
		private int numOptMejorPeor;
		@Property
		@Persist 
		private String [] mejorPeorModel;

		@Property
		@Persist
		private String mejorPeorOption1;
		@Property
		@Persist
		private String mejorPeorOption2;
		@Property
		@Persist
		private String mejorPeorOption3;
		@Property
		@Persist
		private String mejorPeorOption4;
		@Property
		@Persist
		private String mejorPeorOption5;
		@Property
		@Persist
		private String mejorPeorOption6;
		@Property
		@Persist
		private String mejorPeorOption7;
		@Property
		@Persist
		private String mejorPeorOption8;
		@Property
		@Persist
		private String mejorPeorOption9;
		@Property
		@Persist
		private String mejorPeorOption10;
		@Property
		@Persist
		private String mejorPeorOption11;
		@Property
		@Persist
		private String mejorPeorOption12;
		@Property
		@Persist
		private String mejorPeorOption13;
		@Property
		@Persist
		private String mejorPeorOption14;
		@Property
		@Persist
		private String mejorPeorOption15;


		private String optionMejorPeor;
		public String getOptionMejorPeor() {
			return optionMejorPeor;
		}
		public void setOptionMejorPeor(String optionMejorPeor) {
			this.optionMejorPeor = optionMejorPeor;
		}

		/**
		 * Controls the number of options for the mejorPeor voting
		 * @param str
		 */
		public void  onValueChangedFromMejorPeorSel(String str)
		{
			numOptMejorPeor=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("mejorPeorZone", mejorPeorZone);
		}

		public boolean isShowMejorPeor8()
		{
			if(numOptMejorPeor>=8)
				return true;
			return false;
		}
		public boolean isShowMejorPeor9()
		{
			if(numOptMejorPeor>=9)
				return true;
			return false;
		}
		public boolean isShowMejorPeor10()
		{
			if(numOptMejorPeor>=10)
				return true;
			return false;
		}
		public boolean isShowMejorPeor11()
		{
			if(numOptMejorPeor>=11)
				return true;
			return false;
		}
		public boolean isShowMejorPeor12()
		{
			if(numOptMejorPeor>=12)
				return true;
			return false;
		}
		public boolean isShowMejorPeor13()
		{
			if(numOptMejorPeor>=13)
				return true;
			return false;
		}
		public boolean isShowMejorPeor14()
		{
			if(numOptMejorPeor>=14)
				return true;
			return false;
		}
		public boolean isShowMejorPeor15()
		{
			if(numOptMejorPeor>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the mejorPeor voting
		 */
		public void onValidateFromMejorPeorForm()
		{
			showErrorMejorPeor=false;
			showRepeatedMejorPeor=false;
			if(mejorPeorOption1==null || mejorPeorOption2==null)
			{

				showErrorMejorPeor=true;
			}

			switch(numOptMejorPeor)
			{
			case 15:
				if(mejorPeorOption15==null){showErrorMejorPeor=true;}
			case 14:
				if(mejorPeorOption14==null){showErrorMejorPeor=true;}
			case 13:
				if(mejorPeorOption13==null){showErrorMejorPeor=true;}
			case 12:
				if(mejorPeorOption12==null){showErrorMejorPeor=true;}
			case 11:
				if(mejorPeorOption11==null){showErrorMejorPeor=true;}
			case 10:
				if(mejorPeorOption10==null){showErrorMejorPeor=true;}
			case 9:
				if(mejorPeorOption9==null){showErrorMejorPeor=true;}
			case 8:
				if(mejorPeorOption8==null){showErrorMejorPeor=true;}
				break;
			default:
				showErrorMejorPeor=false;
			}
			if(!showErrorMejorPeor)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(mejorPeorOption1);
				listOptions.add(mejorPeorOption2);
				listOptions.add(mejorPeorOption3);
				listOptions.add(mejorPeorOption4);
				listOptions.add(mejorPeorOption5);
				listOptions.add(mejorPeorOption6);
				listOptions.add(mejorPeorOption7);

				if(numOptMejorPeor>=8)
				{
					listOptions.add(mejorPeorOption8);
				}
				if(numOptMejorPeor>=9)
				{
					listOptions.add(mejorPeorOption9);
				}
				if(numOptMejorPeor>=10)
				{
					listOptions.add(mejorPeorOption10);
				}
				if(numOptMejorPeor>=11)
				{
					listOptions.add(mejorPeorOption11);
				}
				if(numOptMejorPeor>=12)
				{
					listOptions.add(mejorPeorOption12);
				}
				if(numOptMejorPeor>=13)
				{
					listOptions.add(mejorPeorOption13);
				}
				if(numOptMejorPeor>=14)
				{
					listOptions.add(mejorPeorOption14);
				}
				if(numOptMejorPeor>=15)
				{
					listOptions.add(mejorPeorOption15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedMejorPeor=true; 
						}

					}
				}
				mejorPeor=new MejorPeor(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromMejorPeorForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorMejorPeor || showRepeatedMejorPeor)
				{
					ajaxResponseRenderer.addRender("mejorPeorZone", mejorPeorZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					mejorPeor.setId(UUID.generate());
					ballot.setIdBallotData(mejorPeor.getId());
					mejorPeor.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					mejorPeorDao=DB4O.getMejorPeorDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						List<List<String>> docentes = GenerateDocentVotes.generateMejorPeor(mejorPeor.getOptions(), Integer.parseInt(census));
						mejorPeor.setVotesPos(docentes.get(0));
						mejorPeor.setVotesNeg(docentes.get(1));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					mejorPeorDao.store(mejorPeor);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////// BACK EVENT /////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		public void onBack(List<String> list)
		{
			String from=list.get(0);
			String to=list.get(1);
			if(to.equals("toGeneral"))
			{
				showType=false;
				showGeneral=true;
				ajaxResponseRenderer.addRender("generalZone", generalZone).addRender("typeZone", typeZone);
			}
			if(to.equals("toType"))
			{
				if(from.equals("fromMayRel"))
				{
					showType=true;
					showMayRel=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("mayRelZone",mayRelZone);
				}
				if(from.equals("fromKemeny"))
				{
					showType=true;
					showKemeny=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("kemenyZone",kemenyZone);
				}
				if(from.equals("fromBorda"))
				{
					showType=true;
					showBorda=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("bordaZone",bordaZone);
				}
				if(from.equals("fromRange"))
				{
					showType=true;
					showRange=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("rangeZone",rangeZone);
				}
				if(from.equals("fromApproval"))
				{
					showType=true;
					showApproval=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("approvalZone",approvalZone);
				}	
				if(from.equals("fromVotoAcumulativo"))
				{
					showType=true;
					showVotoAcumulativo=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("votoAcumulativoZone",votoAcumulativoZone);
				}	
				if(from.equals("fromJuicioMayoritario"))
				{
					showType=true;
					showJuicioMayoritario=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("juicioMayoritarioZone",juicioMayoritarioZone);
				}
				if(from.equals("fromBlack"))
				{
					showType=true;
					showBlack=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("blackZone",blackZone);
				}
				if(from.equals("fromBrams"))
				{
					showType=true;
					showBrams=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("bramsZone",bramsZone);
				}
				if(from.equals("fromBucklin"))
				{
					showType=true;
					showBucklin=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("bucklinZone",bucklinZone);
				}
				if(from.equals("fromCondorcet"))
				{
					showType=true;
					showCondorcet=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("condorcetZone",condorcetZone);
				}	
				if(from.equals("fromCoombs"))
				{
					showType=true;
					showCoombs=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("coombsZone",coombsZone);
				}	
				if(from.equals("fromCopeland"))
				{
					showType=true;
					showCopeland=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("copelandZone",copelandZone);
				}	
				if(from.equals("fromDodgson"))
				{
					showType=true;
					showDodgson=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("dodgsonZone",dodgsonZone);
				}
				if(from.equals("fromHare"))
				{
					showType=true;
					showHare=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("hareZone",hareZone);
				}
				if(from.equals("fromMejorPeor"))
				{
					showType=true;
					showMejorPeor=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("mejorPeorZone",mejorPeorZone);
				}
				if(from.equals("fromNanson"))
				{
					showType=true;
					showNanson=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("nansonZone",nansonZone);
				}
				if(from.equals("fromSchulze"))
				{
					showType=true;
					showSchulze=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("schulzeZone",schulzeZone);
				}	
				if(from.equals("fromSmall"))
				{
					showType=true;
					showSmall=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("smallZone",smallZone);
				}	
				if(from.equals("fromVotoAcumulativo"))
				{
					showType=true;
					showVotoAcumulativo=false;
					ajaxResponseRenderer.addRender("typeZone", typeZone).addRender("votoAcumulativoZone",votoAcumulativoZone);
				}	
			}
		}


		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// Condorcet ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone condorcetZone;
		@Property
		@Persist
		private boolean showCondorcet;
		@Property
		@Persist
		private boolean showErrorCondorcet;
		@Property
		@Persist
		private boolean showRepeatedCondorcet;
		@Persist
		private Condorcet condorcet;

		@Property
		@Persist 
		@Validate("required")
		private String condorcetNumOp;
		@Persist
		private int numOptCondorcet;
		@Property
		@Persist 
		private String [] condorcetModel;

		@Property
		@Persist
		private String condorcetOp1;
		@Property
		@Persist
		private String condorcetOp2;
		@Property
		@Persist
		private String condorcetOp3;
		@Property
		@Persist
		private String condorcetOp4;
		@Property
		@Persist
		private String condorcetOp5;
		@Property
		@Persist
		private String condorcetOp6;
		@Property
		@Persist
		private String condorcetOp7;
		@Property
		@Persist
		private String condorcetOp8;
		@Property
		@Persist
		private String condorcetOp9;
		@Property
		@Persist
		private String condorcetOp10;
		@Property
		@Persist
		private String condorcetOp11;
		@Property
		@Persist
		private String condorcetOp12;
		@Property
		@Persist
		private String condorcetOp13;
		@Property
		@Persist
		private String condorcetOp14;
		@Property
		@Persist
		private String condorcetOp15;


		private String optionCondorcet;
		public String getOptionCondorcet() {
			return optionCondorcet;
		}
		public void setOptionCondorcet(String optionCondorcet) {
			this.optionCondorcet = optionCondorcet;
		}

		/**
		 * Controls the number of options for the condorcet voting
		 * @param str
		 */
		public void  onValueChangedFromCondorcetSel(String str)
		{
			numOptCondorcet=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("condorcetZone", condorcetZone);
		}


		public boolean isShowCondorcet3()
		{
			if(numOptCondorcet>=3)
				return true;
			return false;
		}
		public boolean isShowCondorcet4()
		{
			if(numOptCondorcet>=4)
				return true;
			return false;
		}
		public boolean isShowCondorcet5()
		{
			if(numOptCondorcet>=5)
				return true;
			return false;
		}
		public boolean isShowCondorcet6()
		{
			if(numOptCondorcet>=6)
				return true;
			return false;
		}
		public boolean isShowCondorcet7()
		{
			if(numOptCondorcet>=7)
				return true;
			return false;
		}
		public boolean isShowCondorcet8()
		{
			if(numOptCondorcet>=8)
				return true;
			return false;
		}
		public boolean isShowCondorcet9()
		{
			if(numOptCondorcet>=9)
				return true;
			return false;
		}
		public boolean isShowCondorcet10()
		{
			if(numOptCondorcet>=10)
				return true;
			return false;
		}
		public boolean isShowCondorcet11()
		{
			if(numOptCondorcet>=11)
				return true;
			return false;
		}
		public boolean isShowCondorcet12()
		{
			if(numOptCondorcet>=12)
				return true;
			return false;
		}
		public boolean isShowCondorcet13()
		{
			if(numOptCondorcet>=13)
				return true;
			return false;
		}
		public boolean isShowCondorcet14()
		{
			if(numOptCondorcet>=14)
				return true;
			return false;
		}
		public boolean isShowCondorcet15()
		{
			if(numOptCondorcet>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the condorcet voting
		 */
		public void onValidateFromCondorcetForm()
		{
			showErrorCondorcet=false;
			showRepeatedCondorcet=false;
			if(condorcetOp1==null || condorcetOp2==null)
			{

				showErrorCondorcet=true;
			}

			switch(numOptCondorcet)
			{
			case 15:
				if(condorcetOp15==null){showErrorCondorcet=true;}
			case 14:
				if(condorcetOp14==null){showErrorCondorcet=true;}
			case 13:
				if(condorcetOp13==null){showErrorCondorcet=true;}
			case 12:
				if(condorcetOp12==null){showErrorCondorcet=true;}
			case 11:
				if(condorcetOp11==null){showErrorCondorcet=true;}
			case 10:
				if(condorcetOp10==null){showErrorCondorcet=true;}
			case 9:
				if(condorcetOp9==null){showErrorCondorcet=true;}
			case 8:
				if(condorcetOp8==null){showErrorCondorcet=true;}
			case 7:
				if(condorcetOp7==null){showErrorCondorcet=true;}
			case 6:
				if(condorcetOp6==null){showErrorCondorcet=true;}
			case 5:
				if(condorcetOp5==null){showErrorCondorcet=true;}
			case 4:
				if(condorcetOp4==null){showErrorCondorcet=true;}
			case 3:
				if(condorcetOp3==null){showErrorCondorcet=true;}
				break;
			default:
				showErrorCondorcet=false;
			}
			if(!showErrorCondorcet)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(condorcetOp1);
				listOptions.add(condorcetOp2);
				if(numOptCondorcet>=3)
				{
					listOptions.add(condorcetOp3);
				}
				if(numOptCondorcet>=4)
				{
					listOptions.add(condorcetOp4);
				}
				if(numOptCondorcet>=5)
				{
					listOptions.add(condorcetOp5);
				}
				if(numOptCondorcet>=6)
				{
					listOptions.add(condorcetOp6);
				}
				if(numOptCondorcet>=7)
				{
					listOptions.add(condorcetOp7);
				}
				if(numOptCondorcet>=8)
				{
					listOptions.add(condorcetOp8);
				}
				if(numOptCondorcet>=9)
				{
					listOptions.add(condorcetOp9);
				}
				if(numOptCondorcet>=10)
				{
					listOptions.add(condorcetOp10);
				}
				if(numOptCondorcet>=11)
				{
					listOptions.add(condorcetOp11);
				}
				if(numOptCondorcet>=12)
				{
					listOptions.add(condorcetOp12);
				}
				if(numOptCondorcet>=13)
				{
					listOptions.add(condorcetOp13);
				}
				if(numOptCondorcet>=14)
				{
					listOptions.add(condorcetOp14);
				}
				if(numOptCondorcet>=15)
				{
					listOptions.add(condorcetOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedCondorcet=true; 
						}

					}
				}
				condorcet=new Condorcet(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromCondorcetForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorCondorcet || showRepeatedCondorcet)
				{
					ajaxResponseRenderer.addRender("condorcetZone", condorcetZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					condorcet.setId(UUID.generate());
					ballot.setIdBallotData(condorcet.getId());
					condorcet.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					condorcetDao=DB4O.getCondorcetDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						condorcet.setVotes(GenerateDocentVotes.generateCondorcet(condorcet.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					condorcetDao.store(condorcet);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// Black ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone blackZone;
		@Property
		@Persist
		private boolean showBlack;
		@Property
		@Persist
		private boolean showErrorBlack;
		@Property
		@Persist
		private boolean showRepeatedBlack;
		@Persist
		private Black black;

		@Property
		@Persist 
		@Validate("required")
		private String blackNumOp;
		@Persist
		private int numOptBlack;
		@Property
		@Persist 
		private String [] blackModel;

		@Property
		@Persist
		private String blackOp1;
		@Property
		@Persist
		private String blackOp2;
		@Property
		@Persist
		private String blackOp3;
		@Property
		@Persist
		private String blackOp4;
		@Property
		@Persist
		private String blackOp5;
		@Property
		@Persist
		private String blackOp6;
		@Property
		@Persist
		private String blackOp7;
		@Property
		@Persist
		private String blackOp8;
		@Property
		@Persist
		private String blackOp9;
		@Property
		@Persist
		private String blackOp10;
		@Property
		@Persist
		private String blackOp11;
		@Property
		@Persist
		private String blackOp12;
		@Property
		@Persist
		private String blackOp13;
		@Property
		@Persist
		private String blackOp14;
		@Property
		@Persist
		private String blackOp15;


		private String optionBlack;
		public String getOptionBlack() {
			return optionBlack;
		}
		public void setOptionBlack(String optionBlack) {
			this.optionBlack = optionBlack;
		}

		/**
		 * Controls the number of options for the black voting
		 * @param str
		 */
		public void  onValueChangedFromBlackSel(String str)
		{
			numOptBlack=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("blackZone", blackZone);
		}


		public boolean isShowBlack3()
		{
			if(numOptBlack>=3)
				return true;
			return false;
		}
		public boolean isShowBlack4()
		{
			if(numOptBlack>=4)
				return true;
			return false;
		}
		public boolean isShowBlack5()
		{
			if(numOptBlack>=5)
				return true;
			return false;
		}
		public boolean isShowBlack6()
		{
			if(numOptBlack>=6)
				return true;
			return false;
		}
		public boolean isShowBlack7()
		{
			if(numOptBlack>=7)
				return true;
			return false;
		}
		public boolean isShowBlack8()
		{
			if(numOptBlack>=8)
				return true;
			return false;
		}
		public boolean isShowBlack9()
		{
			if(numOptBlack>=9)
				return true;
			return false;
		}
		public boolean isShowBlack10()
		{
			if(numOptBlack>=10)
				return true;
			return false;
		}
		public boolean isShowBlack11()
		{
			if(numOptBlack>=11)
				return true;
			return false;
		}
		public boolean isShowBlack12()
		{
			if(numOptBlack>=12)
				return true;
			return false;
		}
		public boolean isShowBlack13()
		{
			if(numOptBlack>=13)
				return true;
			return false;
		}
		public boolean isShowBlack14()
		{
			if(numOptBlack>=14)
				return true;
			return false;
		}
		public boolean isShowBlack15()
		{
			if(numOptBlack>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the black voting
		 */
		public void onValidateFromBlackForm()
		{
			showErrorBlack=false;
			showRepeatedBlack=false;
			if(blackOp1==null || blackOp2==null)
			{

				showErrorBlack=true;
			}

			switch(numOptBlack)
			{
			case 15:
				if(blackOp15==null){showErrorBlack=true;}
			case 14:
				if(blackOp14==null){showErrorBlack=true;}
			case 13:
				if(blackOp13==null){showErrorBlack=true;}
			case 12:
				if(blackOp12==null){showErrorBlack=true;}
			case 11:
				if(blackOp11==null){showErrorBlack=true;}
			case 10:
				if(blackOp10==null){showErrorBlack=true;}
			case 9:
				if(blackOp9==null){showErrorBlack=true;}
			case 8:
				if(blackOp8==null){showErrorBlack=true;}
			case 7:
				if(blackOp7==null){showErrorBlack=true;}
			case 6:
				if(blackOp6==null){showErrorBlack=true;}
			case 5:
				if(blackOp5==null){showErrorBlack=true;}
			case 4:
				if(blackOp4==null){showErrorBlack=true;}
			case 3:
				if(blackOp3==null){showErrorBlack=true;}
				break;
			default:
				showErrorBlack=false;
			}
			if(!showErrorBlack)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(blackOp1);
				listOptions.add(blackOp2);
				if(numOptBlack>=3)
				{
					listOptions.add(blackOp3);
				}
				if(numOptBlack>=4)
				{
					listOptions.add(blackOp4);
				}
				if(numOptBlack>=5)
				{
					listOptions.add(blackOp5);
				}
				if(numOptBlack>=6)
				{
					listOptions.add(blackOp6);
				}
				if(numOptBlack>=7)
				{
					listOptions.add(blackOp7);
				}
				if(numOptBlack>=8)
				{
					listOptions.add(blackOp8);
				}
				if(numOptBlack>=9)
				{
					listOptions.add(blackOp9);
				}
				if(numOptBlack>=10)
				{
					listOptions.add(blackOp10);
				}
				if(numOptBlack>=11)
				{
					listOptions.add(blackOp11);
				}
				if(numOptBlack>=12)
				{
					listOptions.add(blackOp12);
				}
				if(numOptBlack>=13)
				{
					listOptions.add(blackOp13);
				}
				if(numOptBlack>=14)
				{
					listOptions.add(blackOp14);
				}
				if(numOptBlack>=15)
				{
					listOptions.add(blackOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedBlack=true; 
						}

					}
				}
				black=new Black(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromBlackForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorBlack || showRepeatedBlack)
				{
					ajaxResponseRenderer.addRender("blackZone", blackZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					black.setId(UUID.generate());
					ballot.setIdBallotData(black.getId());
					black.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					blackDao=DB4O.getBlackDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						black.setVotes(GenerateDocentVotes.generateBlack(black.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					blackDao.store(black);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// Dodgson ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone dodgsonZone;
		@Property
		@Persist
		private boolean showDodgson;
		@Property
		@Persist
		private boolean showErrorDodgson;
		@Property
		@Persist
		private boolean showRepeatedDodgson;
		@Persist
		private Dodgson dodgson;

		@Property
		@Persist 
		@Validate("required")
		private String dodgsonNumOp;
		@Persist
		private int numOptDodgson;
		@Property
		@Persist 
		private String [] dodgsonModel;

		@Property
		@Persist
		private String dodgsonOp1;
		@Property
		@Persist
		private String dodgsonOp2;
		@Property
		@Persist
		private String dodgsonOp3;
		@Property
		@Persist
		private String dodgsonOp4;
		@Property
		@Persist
		private String dodgsonOp5;
		@Property
		@Persist
		private String dodgsonOp6;
		@Property
		@Persist
		private String dodgsonOp7;
		@Property
		@Persist
		private String dodgsonOp8;
		@Property
		@Persist
		private String dodgsonOp9;
		@Property
		@Persist
		private String dodgsonOp10;
		@Property
		@Persist
		private String dodgsonOp11;
		@Property
		@Persist
		private String dodgsonOp12;
		@Property
		@Persist
		private String dodgsonOp13;
		@Property
		@Persist
		private String dodgsonOp14;
		@Property
		@Persist
		private String dodgsonOp15;


		private String optionDodgson;
		public String getOptionDodgson() {
			return optionDodgson;
		}
		public void setOptionDodgson(String optionDodgson) {
			this.optionDodgson = optionDodgson;
		}

		/**
		 * Controls the number of options for the dodgson voting
		 * @param str
		 */
		public void  onValueChangedFromDodgsonSel(String str)
		{
			numOptDodgson=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("dodgsonZone", dodgsonZone);
		}


		public boolean isShowDodgson3()
		{
			if(numOptDodgson>=3)
				return true;
			return false;
		}
		public boolean isShowDodgson4()
		{
			if(numOptDodgson>=4)
				return true;
			return false;
		}
		public boolean isShowDodgson5()
		{
			if(numOptDodgson>=5)
				return true;
			return false;
		}
		public boolean isShowDodgson6()
		{
			if(numOptDodgson>=6)
				return true;
			return false;
		}
		public boolean isShowDodgson7()
		{
			if(numOptDodgson>=7)
				return true;
			return false;
		}
		public boolean isShowDodgson8()
		{
			if(numOptDodgson>=8)
				return true;
			return false;
		}
		public boolean isShowDodgson9()
		{
			if(numOptDodgson>=9)
				return true;
			return false;
		}
		public boolean isShowDodgson10()
		{
			if(numOptDodgson>=10)
				return true;
			return false;
		}
		public boolean isShowDodgson11()
		{
			if(numOptDodgson>=11)
				return true;
			return false;
		}
		public boolean isShowDodgson12()
		{
			if(numOptDodgson>=12)
				return true;
			return false;
		}
		public boolean isShowDodgson13()
		{
			if(numOptDodgson>=13)
				return true;
			return false;
		}
		public boolean isShowDodgson14()
		{
			if(numOptDodgson>=14)
				return true;
			return false;
		}
		public boolean isShowDodgson15()
		{
			if(numOptDodgson>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the dodgson voting
		 */
		public void onValidateFromDodgsonForm()
		{
			showErrorDodgson=false;
			showRepeatedDodgson=false;
			if(dodgsonOp1==null || dodgsonOp2==null)
			{

				showErrorDodgson=true;
			}

			switch(numOptDodgson)
			{
			case 15:
				if(dodgsonOp15==null){showErrorDodgson=true;}
			case 14:
				if(dodgsonOp14==null){showErrorDodgson=true;}
			case 13:
				if(dodgsonOp13==null){showErrorDodgson=true;}
			case 12:
				if(dodgsonOp12==null){showErrorDodgson=true;}
			case 11:
				if(dodgsonOp11==null){showErrorDodgson=true;}
			case 10:
				if(dodgsonOp10==null){showErrorDodgson=true;}
			case 9:
				if(dodgsonOp9==null){showErrorDodgson=true;}
			case 8:
				if(dodgsonOp8==null){showErrorDodgson=true;}
			case 7:
				if(dodgsonOp7==null){showErrorDodgson=true;}
			case 6:
				if(dodgsonOp6==null){showErrorDodgson=true;}
			case 5:
				if(dodgsonOp5==null){showErrorDodgson=true;}
			case 4:
				if(dodgsonOp4==null){showErrorDodgson=true;}
			case 3:
				if(dodgsonOp3==null){showErrorDodgson=true;}
				break;
			default:
				showErrorDodgson=false;
			}
			if(!showErrorDodgson)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(dodgsonOp1);
				listOptions.add(dodgsonOp2);
				if(numOptDodgson>=3)
				{
					listOptions.add(dodgsonOp3);
				}
				if(numOptDodgson>=4)
				{
					listOptions.add(dodgsonOp4);
				}
				if(numOptDodgson>=5)
				{
					listOptions.add(dodgsonOp5);
				}
				if(numOptDodgson>=6)
				{
					listOptions.add(dodgsonOp6);
				}
				if(numOptDodgson>=7)
				{
					listOptions.add(dodgsonOp7);
				}
				if(numOptDodgson>=8)
				{
					listOptions.add(dodgsonOp8);
				}
				if(numOptDodgson>=9)
				{
					listOptions.add(dodgsonOp9);
				}
				if(numOptDodgson>=10)
				{
					listOptions.add(dodgsonOp10);
				}
				if(numOptDodgson>=11)
				{
					listOptions.add(dodgsonOp11);
				}
				if(numOptDodgson>=12)
				{
					listOptions.add(dodgsonOp12);
				}
				if(numOptDodgson>=13)
				{
					listOptions.add(dodgsonOp13);
				}
				if(numOptDodgson>=14)
				{
					listOptions.add(dodgsonOp14);
				}
				if(numOptDodgson>=15)
				{
					listOptions.add(dodgsonOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedDodgson=true; 
						}

					}
				}
				dodgson=new Dodgson(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromDodgsonForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorDodgson || showRepeatedDodgson)
				{
					ajaxResponseRenderer.addRender("dodgsonZone", dodgsonZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					dodgson.setId(UUID.generate());
					ballot.setIdBallotData(dodgson.getId());
					dodgson.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					dodgsonDao=DB4O.getDodgsonDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						dodgson.setVotes(GenerateDocentVotes.generateDodgson(dodgson.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					dodgsonDao.store(dodgson);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}



		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// Copeland ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone copelandZone;
		@Property
		@Persist
		private boolean showCopeland;
		@Property
		@Persist
		private boolean showErrorCopeland;
		@Property
		@Persist
		private boolean showRepeatedCopeland;
		@Persist
		private Copeland copeland;

		@Property
		@Persist 
		@Validate("required")
		private String copelandNumOp;
		@Persist
		private int numOptCopeland;
		@Property
		@Persist 
		private String [] copelandModel;

		@Property
		@Persist
		private String copelandOp1;
		@Property
		@Persist
		private String copelandOp2;
		@Property
		@Persist
		private String copelandOp3;
		@Property
		@Persist
		private String copelandOp4;
		@Property
		@Persist
		private String copelandOp5;
		@Property
		@Persist
		private String copelandOp6;
		@Property
		@Persist
		private String copelandOp7;
		@Property
		@Persist
		private String copelandOp8;
		@Property
		@Persist
		private String copelandOp9;
		@Property
		@Persist
		private String copelandOp10;
		@Property
		@Persist
		private String copelandOp11;
		@Property
		@Persist
		private String copelandOp12;
		@Property
		@Persist
		private String copelandOp13;
		@Property
		@Persist
		private String copelandOp14;
		@Property
		@Persist
		private String copelandOp15;


		private String optionCopeland;
		public String getOptionCopeland() {
			return optionCopeland;
		}
		public void setOptionCopeland(String optionCopeland) {
			this.optionCopeland = optionCopeland;
		}

		/**
		 * Controls the number of options for the copeland voting
		 * @param str
		 */
		public void  onValueChangedFromCopelandSel(String str)
		{
			numOptCopeland=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("copelandZone", copelandZone);
		}


		public boolean isShowCopeland3()
		{
			if(numOptCopeland>=3)
				return true;
			return false;
		}
		public boolean isShowCopeland4()
		{
			if(numOptCopeland>=4)
				return true;
			return false;
		}
		public boolean isShowCopeland5()
		{
			if(numOptCopeland>=5)
				return true;
			return false;
		}
		public boolean isShowCopeland6()
		{
			if(numOptCopeland>=6)
				return true;
			return false;
		}
		public boolean isShowCopeland7()
		{
			if(numOptCopeland>=7)
				return true;
			return false;
		}
		public boolean isShowCopeland8()
		{
			if(numOptCopeland>=8)
				return true;
			return false;
		}
		public boolean isShowCopeland9()
		{
			if(numOptCopeland>=9)
				return true;
			return false;
		}
		public boolean isShowCopeland10()
		{
			if(numOptCopeland>=10)
				return true;
			return false;
		}
		public boolean isShowCopeland11()
		{
			if(numOptCopeland>=11)
				return true;
			return false;
		}
		public boolean isShowCopeland12()
		{
			if(numOptCopeland>=12)
				return true;
			return false;
		}
		public boolean isShowCopeland13()
		{
			if(numOptCopeland>=13)
				return true;
			return false;
		}
		public boolean isShowCopeland14()
		{
			if(numOptCopeland>=14)
				return true;
			return false;
		}
		public boolean isShowCopeland15()
		{
			if(numOptCopeland>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the copeland voting
		 */
		public void onValidateFromCopelandForm()
		{
			showErrorCopeland=false;
			showRepeatedCopeland=false;
			if(copelandOp1==null || copelandOp2==null)
			{

				showErrorCopeland=true;
			}

			switch(numOptCopeland)
			{
			case 15:
				if(copelandOp15==null){showErrorCopeland=true;}
			case 14:
				if(copelandOp14==null){showErrorCopeland=true;}
			case 13:
				if(copelandOp13==null){showErrorCopeland=true;}
			case 12:
				if(copelandOp12==null){showErrorCopeland=true;}
			case 11:
				if(copelandOp11==null){showErrorCopeland=true;}
			case 10:
				if(copelandOp10==null){showErrorCopeland=true;}
			case 9:
				if(copelandOp9==null){showErrorCopeland=true;}
			case 8:
				if(copelandOp8==null){showErrorCopeland=true;}
			case 7:
				if(copelandOp7==null){showErrorCopeland=true;}
			case 6:
				if(copelandOp6==null){showErrorCopeland=true;}
			case 5:
				if(copelandOp5==null){showErrorCopeland=true;}
			case 4:
				if(copelandOp4==null){showErrorCopeland=true;}
			case 3:
				if(copelandOp3==null){showErrorCopeland=true;}
				break;
			default:
				showErrorCopeland=false;
			}
			if(!showErrorCopeland)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(copelandOp1);
				listOptions.add(copelandOp2);
				if(numOptCopeland>=3)
				{
					listOptions.add(copelandOp3);
				}
				if(numOptCopeland>=4)
				{
					listOptions.add(copelandOp4);
				}
				if(numOptCopeland>=5)
				{
					listOptions.add(copelandOp5);
				}
				if(numOptCopeland>=6)
				{
					listOptions.add(copelandOp6);
				}
				if(numOptCopeland>=7)
				{
					listOptions.add(copelandOp7);
				}
				if(numOptCopeland>=8)
				{
					listOptions.add(copelandOp8);
				}
				if(numOptCopeland>=9)
				{
					listOptions.add(copelandOp9);
				}
				if(numOptCopeland>=10)
				{
					listOptions.add(copelandOp10);
				}
				if(numOptCopeland>=11)
				{
					listOptions.add(copelandOp11);
				}
				if(numOptCopeland>=12)
				{
					listOptions.add(copelandOp12);
				}
				if(numOptCopeland>=13)
				{
					listOptions.add(copelandOp13);
				}
				if(numOptCopeland>=14)
				{
					listOptions.add(copelandOp14);
				}
				if(numOptCopeland>=15)
				{
					listOptions.add(copelandOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedCopeland=true; 
						}

					}
				}
				copeland=new Copeland(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromCopelandForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorCopeland || showRepeatedCopeland)
				{
					ajaxResponseRenderer.addRender("copelandZone", copelandZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					copeland.setId(UUID.generate());
					ballot.setIdBallotData(copeland.getId());
					copeland.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					copelandDao=DB4O.getCopelandDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						copeland.setVotes(GenerateDocentVotes.generateCopeland(copeland.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					copelandDao.store(copeland);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}


		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// Schulze ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone schulzeZone;
		@Property
		@Persist
		private boolean showSchulze;
		@Property
		@Persist
		private boolean showErrorSchulze;
		@Property
		@Persist
		private boolean showRepeatedSchulze;
		@Persist
		private Schulze schulze;

		@Property
		@Persist 
		@Validate("required")
		private String schulzeNumOp;
		@Persist
		private int numOptSchulze;
		@Property
		@Persist 
		private String [] schulzeModel;

		@Property
		@Persist
		private String schulzeOp1;
		@Property
		@Persist
		private String schulzeOp2;
		@Property
		@Persist
		private String schulzeOp3;
		@Property
		@Persist
		private String schulzeOp4;
		@Property
		@Persist
		private String schulzeOp5;
		@Property
		@Persist
		private String schulzeOp6;
		@Property
		@Persist
		private String schulzeOp7;
		@Property
		@Persist
		private String schulzeOp8;
		@Property
		@Persist
		private String schulzeOp9;
		@Property
		@Persist
		private String schulzeOp10;
		@Property
		@Persist
		private String schulzeOp11;
		@Property
		@Persist
		private String schulzeOp12;
		@Property
		@Persist
		private String schulzeOp13;
		@Property
		@Persist
		private String schulzeOp14;
		@Property
		@Persist
		private String schulzeOp15;


		private String optionSchulze;
		public String getOptionSchulze() {
			return optionSchulze;
		}
		public void setOptionSchulze(String optionSchulze) {
			this.optionSchulze = optionSchulze;
		}

		/**
		 * Controls the number of options for the schulze voting
		 * @param str
		 */
		public void  onValueChangedFromSchulzeSel(String str)
		{
			numOptSchulze=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("schulzeZone", schulzeZone);
		}


		public boolean isShowSchulze3()
		{
			if(numOptSchulze>=3)
				return true;
			return false;
		}
		public boolean isShowSchulze4()
		{
			if(numOptSchulze>=4)
				return true;
			return false;
		}
		public boolean isShowSchulze5()
		{
			if(numOptSchulze>=5)
				return true;
			return false;
		}
		public boolean isShowSchulze6()
		{
			if(numOptSchulze>=6)
				return true;
			return false;
		}
		public boolean isShowSchulze7()
		{
			if(numOptSchulze>=7)
				return true;
			return false;
		}
		public boolean isShowSchulze8()
		{
			if(numOptSchulze>=8)
				return true;
			return false;
		}
		public boolean isShowSchulze9()
		{
			if(numOptSchulze>=9)
				return true;
			return false;
		}
		public boolean isShowSchulze10()
		{
			if(numOptSchulze>=10)
				return true;
			return false;
		}
		public boolean isShowSchulze11()
		{
			if(numOptSchulze>=11)
				return true;
			return false;
		}
		public boolean isShowSchulze12()
		{
			if(numOptSchulze>=12)
				return true;
			return false;
		}
		public boolean isShowSchulze13()
		{
			if(numOptSchulze>=13)
				return true;
			return false;
		}
		public boolean isShowSchulze14()
		{
			if(numOptSchulze>=14)
				return true;
			return false;
		}
		public boolean isShowSchulze15()
		{
			if(numOptSchulze>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the schulze voting
		 */
		public void onValidateFromSchulzeForm()
		{
			showErrorSchulze=false;
			showRepeatedSchulze=false;
			if(schulzeOp1==null || schulzeOp2==null)
			{

				showErrorSchulze=true;
			}

			switch(numOptSchulze)
			{
			case 15:
				if(schulzeOp15==null){showErrorSchulze=true;}
			case 14:
				if(schulzeOp14==null){showErrorSchulze=true;}
			case 13:
				if(schulzeOp13==null){showErrorSchulze=true;}
			case 12:
				if(schulzeOp12==null){showErrorSchulze=true;}
			case 11:
				if(schulzeOp11==null){showErrorSchulze=true;}
			case 10:
				if(schulzeOp10==null){showErrorSchulze=true;}
			case 9:
				if(schulzeOp9==null){showErrorSchulze=true;}
			case 8:
				if(schulzeOp8==null){showErrorSchulze=true;}
			case 7:
				if(schulzeOp7==null){showErrorSchulze=true;}
			case 6:
				if(schulzeOp6==null){showErrorSchulze=true;}
			case 5:
				if(schulzeOp5==null){showErrorSchulze=true;}
			case 4:
				if(schulzeOp4==null){showErrorSchulze=true;}
			case 3:
				if(schulzeOp3==null){showErrorSchulze=true;}
				break;
			default:
				showErrorSchulze=false;
			}
			if(!showErrorSchulze)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(schulzeOp1);
				listOptions.add(schulzeOp2);
				if(numOptSchulze>=3)
				{
					listOptions.add(schulzeOp3);
				}
				if(numOptSchulze>=4)
				{
					listOptions.add(schulzeOp4);
				}
				if(numOptSchulze>=5)
				{
					listOptions.add(schulzeOp5);
				}
				if(numOptSchulze>=6)
				{
					listOptions.add(schulzeOp6);
				}
				if(numOptSchulze>=7)
				{
					listOptions.add(schulzeOp7);
				}
				if(numOptSchulze>=8)
				{
					listOptions.add(schulzeOp8);
				}
				if(numOptSchulze>=9)
				{
					listOptions.add(schulzeOp9);
				}
				if(numOptSchulze>=10)
				{
					listOptions.add(schulzeOp10);
				}
				if(numOptSchulze>=11)
				{
					listOptions.add(schulzeOp11);
				}
				if(numOptSchulze>=12)
				{
					listOptions.add(schulzeOp12);
				}
				if(numOptSchulze>=13)
				{
					listOptions.add(schulzeOp13);
				}
				if(numOptSchulze>=14)
				{
					listOptions.add(schulzeOp14);
				}
				if(numOptSchulze>=15)
				{
					listOptions.add(schulzeOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedSchulze=true; 
						}

					}
				}
				schulze=new Schulze(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromSchulzeForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorSchulze || showRepeatedSchulze)
				{
					ajaxResponseRenderer.addRender("schulzeZone", schulzeZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					schulze.setId(UUID.generate());
					ballot.setIdBallotData(schulze.getId());
					schulze.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					schulzeDao=DB4O.getSchulzeDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						schulze.setVotes(GenerateDocentVotes.generateSchulze(schulze.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					schulzeDao.store(schulze);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// Small ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone smallZone;
		@Property
		@Persist
		private boolean showSmall;
		@Property
		@Persist
		private boolean showErrorSmall;
		@Property
		@Persist
		private boolean showRepeatedSmall;
		@Persist
		private Small small;

		@Property
		@Persist 
		@Validate("required")
		private String smallNumOp;
		@Persist
		private int numOptSmall;
		@Property
		@Persist 
		private String [] smallModel;

		@Property
		@Persist
		private String smallOp1;
		@Property
		@Persist
		private String smallOp2;
		@Property
		@Persist
		private String smallOp3;
		@Property
		@Persist
		private String smallOp4;
		@Property
		@Persist
		private String smallOp5;
		@Property
		@Persist
		private String smallOp6;
		@Property
		@Persist
		private String smallOp7;
		@Property
		@Persist
		private String smallOp8;
		@Property
		@Persist
		private String smallOp9;
		@Property
		@Persist
		private String smallOp10;
		@Property
		@Persist
		private String smallOp11;
		@Property
		@Persist
		private String smallOp12;
		@Property
		@Persist
		private String smallOp13;
		@Property
		@Persist
		private String smallOp14;
		@Property
		@Persist
		private String smallOp15;


		private String optionSmall;
		public String getOptionSmall() {
			return optionSmall;
		}
		public void setOptionSmall(String optionSmall) {
			this.optionSmall = optionSmall;
		}

		/**
		 * Controls the number of options for the small voting
		 * @param str
		 */
		public void  onValueChangedFromSmallSel(String str)
		{
			numOptSmall=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("smallZone", smallZone);
		}


		public boolean isShowSmall3()
		{
			if(numOptSmall>=3)
				return true;
			return false;
		}
		public boolean isShowSmall4()
		{
			if(numOptSmall>=4)
				return true;
			return false;
		}
		public boolean isShowSmall5()
		{
			if(numOptSmall>=5)
				return true;
			return false;
		}
		public boolean isShowSmall6()
		{
			if(numOptSmall>=6)
				return true;
			return false;
		}
		public boolean isShowSmall7()
		{
			if(numOptSmall>=7)
				return true;
			return false;
		}
		public boolean isShowSmall8()
		{
			if(numOptSmall>=8)
				return true;
			return false;
		}
		public boolean isShowSmall9()
		{
			if(numOptSmall>=9)
				return true;
			return false;
		}
		public boolean isShowSmall10()
		{
			if(numOptSmall>=10)
				return true;
			return false;
		}
		public boolean isShowSmall11()
		{
			if(numOptSmall>=11)
				return true;
			return false;
		}
		public boolean isShowSmall12()
		{
			if(numOptSmall>=12)
				return true;
			return false;
		}
		public boolean isShowSmall13()
		{
			if(numOptSmall>=13)
				return true;
			return false;
		}
		public boolean isShowSmall14()
		{
			if(numOptSmall>=14)
				return true;
			return false;
		}
		public boolean isShowSmall15()
		{
			if(numOptSmall>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the small voting
		 */
		public void onValidateFromSmallForm()
		{
			showErrorSmall=false;
			showRepeatedSmall=false;
			if(smallOp1==null || smallOp2==null)
			{

				showErrorSmall=true;
			}

			switch(numOptSmall)
			{
			case 15:
				if(smallOp15==null){showErrorSmall=true;}
			case 14:
				if(smallOp14==null){showErrorSmall=true;}
			case 13:
				if(smallOp13==null){showErrorSmall=true;}
			case 12:
				if(smallOp12==null){showErrorSmall=true;}
			case 11:
				if(smallOp11==null){showErrorSmall=true;}
			case 10:
				if(smallOp10==null){showErrorSmall=true;}
			case 9:
				if(smallOp9==null){showErrorSmall=true;}
			case 8:
				if(smallOp8==null){showErrorSmall=true;}
			case 7:
				if(smallOp7==null){showErrorSmall=true;}
			case 6:
				if(smallOp6==null){showErrorSmall=true;}
			case 5:
				if(smallOp5==null){showErrorSmall=true;}
			case 4:
				if(smallOp4==null){showErrorSmall=true;}
			case 3:
				if(smallOp3==null){showErrorSmall=true;}
				break;
			default:
				showErrorSmall=false;
			}
			if(!showErrorSmall)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(smallOp1);
				listOptions.add(smallOp2);
				if(numOptSmall>=3)
				{
					listOptions.add(smallOp3);
				}
				if(numOptSmall>=4)
				{
					listOptions.add(smallOp4);
				}
				if(numOptSmall>=5)
				{
					listOptions.add(smallOp5);
				}
				if(numOptSmall>=6)
				{
					listOptions.add(smallOp6);
				}
				if(numOptSmall>=7)
				{
					listOptions.add(smallOp7);
				}
				if(numOptSmall>=8)
				{
					listOptions.add(smallOp8);
				}
				if(numOptSmall>=9)
				{
					listOptions.add(smallOp9);
				}
				if(numOptSmall>=10)
				{
					listOptions.add(smallOp10);
				}
				if(numOptSmall>=11)
				{
					listOptions.add(smallOp11);
				}
				if(numOptSmall>=12)
				{
					listOptions.add(smallOp12);
				}
				if(numOptSmall>=13)
				{
					listOptions.add(smallOp13);
				}
				if(numOptSmall>=14)
				{
					listOptions.add(smallOp14);
				}
				if(numOptSmall>=15)
				{
					listOptions.add(smallOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedSmall=true; 
						}

					}
				}
				small=new Small(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromSmallForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorSmall || showRepeatedSmall)
				{
					ajaxResponseRenderer.addRender("smallZone", smallZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					small.setId(UUID.generate());
					ballot.setIdBallotData(small.getId());
					small.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					smallDao=DB4O.getSmallDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						small.setVotes(GenerateDocentVotes.generateSmall(small.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					smallDao.store(small);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}


		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// Bucklin ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone bucklinZone;
		@Property
		@Persist
		private boolean showBucklin;
		@Property
		@Persist
		private boolean showErrorBucklin;
		@Property
		@Persist
		private boolean showRepeatedBucklin;
		@Persist
		private Bucklin bucklin;

		@Property
		@Persist 
		@Validate("required")
		private String bucklinNumOp;
		@Persist
		private int numOptBucklin;
		@Property
		@Persist 
		private String [] bucklinModel;

		@Property
		@Persist
		private String bucklinOp1;
		@Property
		@Persist
		private String bucklinOp2;
		@Property
		@Persist
		private String bucklinOp3;
		@Property
		@Persist
		private String bucklinOp4;
		@Property
		@Persist
		private String bucklinOp5;
		@Property
		@Persist
		private String bucklinOp6;
		@Property
		@Persist
		private String bucklinOp7;
		@Property
		@Persist
		private String bucklinOp8;
		@Property
		@Persist
		private String bucklinOp9;
		@Property
		@Persist
		private String bucklinOp10;
		@Property
		@Persist
		private String bucklinOp11;
		@Property
		@Persist
		private String bucklinOp12;
		@Property
		@Persist
		private String bucklinOp13;
		@Property
		@Persist
		private String bucklinOp14;
		@Property
		@Persist
		private String bucklinOp15;


		private String optionBucklin;
		public String getOptionBucklin() {
			return optionBucklin;
		}
		public void setOptionBucklin(String optionBucklin) {
			this.optionBucklin = optionBucklin;
		}

		/**
		 * Controls the number of options for the bucklin voting
		 * @param str
		 */
		public void  onValueChangedFromBucklinSel(String str)
		{
			numOptBucklin=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("bucklinZone", bucklinZone);
		}


		public boolean isShowBucklin3()
		{
			if(numOptBucklin>=3)
				return true;
			return false;
		}
		public boolean isShowBucklin4()
		{
			if(numOptBucklin>=4)
				return true;
			return false;
		}
		public boolean isShowBucklin5()
		{
			if(numOptBucklin>=5)
				return true;
			return false;
		}
		public boolean isShowBucklin6()
		{
			if(numOptBucklin>=6)
				return true;
			return false;
		}
		public boolean isShowBucklin7()
		{
			if(numOptBucklin>=7)
				return true;
			return false;
		}
		public boolean isShowBucklin8()
		{
			if(numOptBucklin>=8)
				return true;
			return false;
		}
		public boolean isShowBucklin9()
		{
			if(numOptBucklin>=9)
				return true;
			return false;
		}
		public boolean isShowBucklin10()
		{
			if(numOptBucklin>=10)
				return true;
			return false;
		}
		public boolean isShowBucklin11()
		{
			if(numOptBucklin>=11)
				return true;
			return false;
		}
		public boolean isShowBucklin12()
		{
			if(numOptBucklin>=12)
				return true;
			return false;
		}
		public boolean isShowBucklin13()
		{
			if(numOptBucklin>=13)
				return true;
			return false;
		}
		public boolean isShowBucklin14()
		{
			if(numOptBucklin>=14)
				return true;
			return false;
		}
		public boolean isShowBucklin15()
		{
			if(numOptBucklin>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the bucklin voting
		 */
		public void onValidateFromBucklinForm()
		{
			showErrorBucklin=false;
			showRepeatedBucklin=false;
			if(bucklinOp1==null || bucklinOp2==null)
			{

				showErrorBucklin=true;
			}

			switch(numOptBucklin)
			{
			case 15:
				if(bucklinOp15==null){showErrorBucklin=true;}
			case 14:
				if(bucklinOp14==null){showErrorBucklin=true;}
			case 13:
				if(bucklinOp13==null){showErrorBucklin=true;}
			case 12:
				if(bucklinOp12==null){showErrorBucklin=true;}
			case 11:
				if(bucklinOp11==null){showErrorBucklin=true;}
			case 10:
				if(bucklinOp10==null){showErrorBucklin=true;}
			case 9:
				if(bucklinOp9==null){showErrorBucklin=true;}
			case 8:
				if(bucklinOp8==null){showErrorBucklin=true;}
			case 7:
				if(bucklinOp7==null){showErrorBucklin=true;}
			case 6:
				if(bucklinOp6==null){showErrorBucklin=true;}
			case 5:
				if(bucklinOp5==null){showErrorBucklin=true;}
			case 4:
				if(bucklinOp4==null){showErrorBucklin=true;}
			case 3:
				if(bucklinOp3==null){showErrorBucklin=true;}
				break;
			default:
				showErrorBucklin=false;
			}
			if(!showErrorBucklin)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(bucklinOp1);
				listOptions.add(bucklinOp2);
				if(numOptBucklin>=3)
				{
					listOptions.add(bucklinOp3);
				}
				if(numOptBucklin>=4)
				{
					listOptions.add(bucklinOp4);
				}
				if(numOptBucklin>=5)
				{
					listOptions.add(bucklinOp5);
				}
				if(numOptBucklin>=6)
				{
					listOptions.add(bucklinOp6);
				}
				if(numOptBucklin>=7)
				{
					listOptions.add(bucklinOp7);
				}
				if(numOptBucklin>=8)
				{
					listOptions.add(bucklinOp8);
				}
				if(numOptBucklin>=9)
				{
					listOptions.add(bucklinOp9);
				}
				if(numOptBucklin>=10)
				{
					listOptions.add(bucklinOp10);
				}
				if(numOptBucklin>=11)
				{
					listOptions.add(bucklinOp11);
				}
				if(numOptBucklin>=12)
				{
					listOptions.add(bucklinOp12);
				}
				if(numOptBucklin>=13)
				{
					listOptions.add(bucklinOp13);
				}
				if(numOptBucklin>=14)
				{
					listOptions.add(bucklinOp14);
				}
				if(numOptBucklin>=15)
				{
					listOptions.add(bucklinOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedBucklin=true; 
						}

					}
				}
				bucklin=new Bucklin(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromBucklinForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorBucklin || showRepeatedBucklin)
				{
					ajaxResponseRenderer.addRender("bucklinZone", bucklinZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					bucklin.setId(UUID.generate());
					ballot.setIdBallotData(bucklin.getId());
					bucklin.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					bucklinDao=DB4O.getBucklinDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						bucklin.setVotes(GenerateDocentVotes.generateBucklin(bucklin.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					bucklinDao.store(bucklin);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}


		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// Nanson ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone nansonZone;
		@Property
		@Persist
		private boolean showNanson;
		@Property
		@Persist
		private boolean showErrorNanson;
		@Property
		@Persist
		private boolean showRepeatedNanson;
		@Persist
		private Nanson nanson;

		@Property
		@Persist 
		@Validate("required")
		private String nansonNumOp;
		@Persist
		private int numOptNanson;
		@Property
		@Persist 
		private String [] nansonModel;

		@Property
		@Persist
		private String nansonOp1;
		@Property
		@Persist
		private String nansonOp2;
		@Property
		@Persist
		private String nansonOp3;
		@Property
		@Persist
		private String nansonOp4;
		@Property
		@Persist
		private String nansonOp5;
		@Property
		@Persist
		private String nansonOp6;
		@Property
		@Persist
		private String nansonOp7;
		@Property
		@Persist
		private String nansonOp8;
		@Property
		@Persist
		private String nansonOp9;
		@Property
		@Persist
		private String nansonOp10;
		@Property
		@Persist
		private String nansonOp11;
		@Property
		@Persist
		private String nansonOp12;
		@Property
		@Persist
		private String nansonOp13;
		@Property
		@Persist
		private String nansonOp14;
		@Property
		@Persist
		private String nansonOp15;


		private String optionNanson;
		public String getOptionNanson() {
			return optionNanson;
		}
		public void setOptionNanson(String optionNanson) {
			this.optionNanson = optionNanson;
		}

		/**
		 * Controls the number of options for the nanson voting
		 * @param str
		 */
		public void  onValueChangedFromNansonSel(String str)
		{
			numOptNanson=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("nansonZone", nansonZone);
		}


		public boolean isShowNanson3()
		{
			if(numOptNanson>=3)
				return true;
			return false;
		}
		public boolean isShowNanson4()
		{
			if(numOptNanson>=4)
				return true;
			return false;
		}
		public boolean isShowNanson5()
		{
			if(numOptNanson>=5)
				return true;
			return false;
		}
		public boolean isShowNanson6()
		{
			if(numOptNanson>=6)
				return true;
			return false;
		}
		public boolean isShowNanson7()
		{
			if(numOptNanson>=7)
				return true;
			return false;
		}
		public boolean isShowNanson8()
		{
			if(numOptNanson>=8)
				return true;
			return false;
		}
		public boolean isShowNanson9()
		{
			if(numOptNanson>=9)
				return true;
			return false;
		}
		public boolean isShowNanson10()
		{
			if(numOptNanson>=10)
				return true;
			return false;
		}
		public boolean isShowNanson11()
		{
			if(numOptNanson>=11)
				return true;
			return false;
		}
		public boolean isShowNanson12()
		{
			if(numOptNanson>=12)
				return true;
			return false;
		}
		public boolean isShowNanson13()
		{
			if(numOptNanson>=13)
				return true;
			return false;
		}
		public boolean isShowNanson14()
		{
			if(numOptNanson>=14)
				return true;
			return false;
		}
		public boolean isShowNanson15()
		{
			if(numOptNanson>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the nanson voting
		 */
		public void onValidateFromNansonForm()
		{
			showErrorNanson=false;
			showRepeatedNanson=false;
			if(nansonOp1==null || nansonOp2==null)
			{

				showErrorNanson=true;
			}

			switch(numOptNanson)
			{
			case 15:
				if(nansonOp15==null){showErrorNanson=true;}
			case 14:
				if(nansonOp14==null){showErrorNanson=true;}
			case 13:
				if(nansonOp13==null){showErrorNanson=true;}
			case 12:
				if(nansonOp12==null){showErrorNanson=true;}
			case 11:
				if(nansonOp11==null){showErrorNanson=true;}
			case 10:
				if(nansonOp10==null){showErrorNanson=true;}
			case 9:
				if(nansonOp9==null){showErrorNanson=true;}
			case 8:
				if(nansonOp8==null){showErrorNanson=true;}
			case 7:
				if(nansonOp7==null){showErrorNanson=true;}
			case 6:
				if(nansonOp6==null){showErrorNanson=true;}
			case 5:
				if(nansonOp5==null){showErrorNanson=true;}
			case 4:
				if(nansonOp4==null){showErrorNanson=true;}
			case 3:
				if(nansonOp3==null){showErrorNanson=true;}
				break;
			default:
				showErrorNanson=false;
			}
			if(!showErrorNanson)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(nansonOp1);
				listOptions.add(nansonOp2);
				if(numOptNanson>=3)
				{
					listOptions.add(nansonOp3);
				}
				if(numOptNanson>=4)
				{
					listOptions.add(nansonOp4);
				}
				if(numOptNanson>=5)
				{
					listOptions.add(nansonOp5);
				}
				if(numOptNanson>=6)
				{
					listOptions.add(nansonOp6);
				}
				if(numOptNanson>=7)
				{
					listOptions.add(nansonOp7);
				}
				if(numOptNanson>=8)
				{
					listOptions.add(nansonOp8);
				}
				if(numOptNanson>=9)
				{
					listOptions.add(nansonOp9);
				}
				if(numOptNanson>=10)
				{
					listOptions.add(nansonOp10);
				}
				if(numOptNanson>=11)
				{
					listOptions.add(nansonOp11);
				}
				if(numOptNanson>=12)
				{
					listOptions.add(nansonOp12);
				}
				if(numOptNanson>=13)
				{
					listOptions.add(nansonOp13);
				}
				if(numOptNanson>=14)
				{
					listOptions.add(nansonOp14);
				}
				if(numOptNanson>=15)
				{
					listOptions.add(nansonOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedNanson=true; 
						}

					}
				}
				nanson=new Nanson(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromNansonForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorNanson || showRepeatedNanson)
				{
					ajaxResponseRenderer.addRender("nansonZone", nansonZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					nanson.setId(UUID.generate());
					ballot.setIdBallotData(nanson.getId());
					nanson.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					nansonDao=DB4O.getNansonDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						nanson.setVotes(GenerateDocentVotes.generateNanson(nanson.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					nansonDao.store(nanson);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}


		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// Hare ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone hareZone;
		@Property
		@Persist
		private boolean showHare;
		@Property
		@Persist
		private boolean showErrorHare;
		@Property
		@Persist
		private boolean showRepeatedHare;
		@Persist
		private Hare hare;

		@Property
		@Persist 
		@Validate("required")
		private String hareNumOp;
		@Persist
		private int numOptHare;
		@Property
		@Persist 
		private String [] hareModel;

		@Property
		@Persist
		private String hareOp1;
		@Property
		@Persist
		private String hareOp2;
		@Property
		@Persist
		private String hareOp3;
		@Property
		@Persist
		private String hareOp4;
		@Property
		@Persist
		private String hareOp5;
		@Property
		@Persist
		private String hareOp6;
		@Property
		@Persist
		private String hareOp7;
		@Property
		@Persist
		private String hareOp8;
		@Property
		@Persist
		private String hareOp9;
		@Property
		@Persist
		private String hareOp10;
		@Property
		@Persist
		private String hareOp11;
		@Property
		@Persist
		private String hareOp12;
		@Property
		@Persist
		private String hareOp13;
		@Property
		@Persist
		private String hareOp14;
		@Property
		@Persist
		private String hareOp15;


		private String optionHare;
		public String getOptionHare() {
			return optionHare;
		}
		public void setOptionHare(String optionHare) {
			this.optionHare = optionHare;
		}

		/**
		 * Controls the number of options for the hare voting
		 * @param str
		 */
		public void  onValueChangedFromHareSel(String str)
		{
			numOptHare=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("hareZone", hareZone);
		}


		public boolean isShowHare3()
		{
			if(numOptHare>=3)
				return true;
			return false;
		}
		public boolean isShowHare4()
		{
			if(numOptHare>=4)
				return true;
			return false;
		}
		public boolean isShowHare5()
		{
			if(numOptHare>=5)
				return true;
			return false;
		}
		public boolean isShowHare6()
		{
			if(numOptHare>=6)
				return true;
			return false;
		}
		public boolean isShowHare7()
		{
			if(numOptHare>=7)
				return true;
			return false;
		}
		public boolean isShowHare8()
		{
			if(numOptHare>=8)
				return true;
			return false;
		}
		public boolean isShowHare9()
		{
			if(numOptHare>=9)
				return true;
			return false;
		}
		public boolean isShowHare10()
		{
			if(numOptHare>=10)
				return true;
			return false;
		}
		public boolean isShowHare11()
		{
			if(numOptHare>=11)
				return true;
			return false;
		}
		public boolean isShowHare12()
		{
			if(numOptHare>=12)
				return true;
			return false;
		}
		public boolean isShowHare13()
		{
			if(numOptHare>=13)
				return true;
			return false;
		}
		public boolean isShowHare14()
		{
			if(numOptHare>=14)
				return true;
			return false;
		}
		public boolean isShowHare15()
		{
			if(numOptHare>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the hare voting
		 */
		public void onValidateFromHareForm()
		{
			showErrorHare=false;
			showRepeatedHare=false;
			if(hareOp1==null || hareOp2==null)
			{

				showErrorHare=true;
			}

			switch(numOptHare)
			{
			case 15:
				if(hareOp15==null){showErrorHare=true;}
			case 14:
				if(hareOp14==null){showErrorHare=true;}
			case 13:
				if(hareOp13==null){showErrorHare=true;}
			case 12:
				if(hareOp12==null){showErrorHare=true;}
			case 11:
				if(hareOp11==null){showErrorHare=true;}
			case 10:
				if(hareOp10==null){showErrorHare=true;}
			case 9:
				if(hareOp9==null){showErrorHare=true;}
			case 8:
				if(hareOp8==null){showErrorHare=true;}
			case 7:
				if(hareOp7==null){showErrorHare=true;}
			case 6:
				if(hareOp6==null){showErrorHare=true;}
			case 5:
				if(hareOp5==null){showErrorHare=true;}
			case 4:
				if(hareOp4==null){showErrorHare=true;}
			case 3:
				if(hareOp3==null){showErrorHare=true;}
				break;
			default:
				showErrorHare=false;
			}
			if(!showErrorHare)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(hareOp1);
				listOptions.add(hareOp2);
				if(numOptHare>=3)
				{
					listOptions.add(hareOp3);
				}
				if(numOptHare>=4)
				{
					listOptions.add(hareOp4);
				}
				if(numOptHare>=5)
				{
					listOptions.add(hareOp5);
				}
				if(numOptHare>=6)
				{
					listOptions.add(hareOp6);
				}
				if(numOptHare>=7)
				{
					listOptions.add(hareOp7);
				}
				if(numOptHare>=8)
				{
					listOptions.add(hareOp8);
				}
				if(numOptHare>=9)
				{
					listOptions.add(hareOp9);
				}
				if(numOptHare>=10)
				{
					listOptions.add(hareOp10);
				}
				if(numOptHare>=11)
				{
					listOptions.add(hareOp11);
				}
				if(numOptHare>=12)
				{
					listOptions.add(hareOp12);
				}
				if(numOptHare>=13)
				{
					listOptions.add(hareOp13);
				}
				if(numOptHare>=14)
				{
					listOptions.add(hareOp14);
				}
				if(numOptHare>=15)
				{
					listOptions.add(hareOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedHare=true; 
						}

					}
				}
				hare=new Hare(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromHareForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorHare || showRepeatedHare)
				{
					ajaxResponseRenderer.addRender("hareZone", hareZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					hare.setId(UUID.generate());
					ballot.setIdBallotData(hare.getId());
					hare.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					hareDao=DB4O.getHareDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						hare.setVotes(GenerateDocentVotes.generateHare(hare.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							// voteDao.store(new Vote(ballot.getId(),datasession.getId()));
							//
						}

					}

					hareDao.store(hare);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}


		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////// Coombs ZONE /////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		@InjectComponent
		private Zone coombsZone;
		@Property
		@Persist
		private boolean showCoombs;
		@Property
		@Persist
		private boolean showErrorCoombs;
		@Property
		@Persist
		private boolean showRepeatedCoombs;
		@Persist
		private Coombs coombs;

		@Property
		@Persist 
		@Validate("required")
		private String coombsNumOp;
		@Persist
		private int numOptCoombs;
		@Property
		@Persist 
		private String [] coombsModel;

		@Property
		@Persist
		private String coombsOp1;
		@Property
		@Persist
		private String coombsOp2;
		@Property
		@Persist
		private String coombsOp3;
		@Property
		@Persist
		private String coombsOp4;
		@Property
		@Persist
		private String coombsOp5;
		@Property
		@Persist
		private String coombsOp6;
		@Property
		@Persist
		private String coombsOp7;
		@Property
		@Persist
		private String coombsOp8;
		@Property
		@Persist
		private String coombsOp9;
		@Property
		@Persist
		private String coombsOp10;
		@Property
		@Persist
		private String coombsOp11;
		@Property
		@Persist
		private String coombsOp12;
		@Property
		@Persist
		private String coombsOp13;
		@Property
		@Persist
		private String coombsOp14;
		@Property
		@Persist
		private String coombsOp15;


		private String optionCoombs;
		public String getOptionCoombs() {
			return optionCoombs;
		}
		public void setOptionCoombs(String optionCoombs) {
			this.optionCoombs = optionCoombs;
		}

		/**
		 * Controls the number of options for the coombs voting
		 * @param str
		 */
		public void  onValueChangedFromCoombsSel(String str)
		{
			numOptCoombs=Integer.parseInt(str);
			ajaxResponseRenderer.addRender("coombsZone", coombsZone);
		}


		public boolean isShowCoombs3()
		{
			if(numOptCoombs>=3)
				return true;
			return false;
		}
		public boolean isShowCoombs4()
		{
			if(numOptCoombs>=4)
				return true;
			return false;
		}
		public boolean isShowCoombs5()
		{
			if(numOptCoombs>=5)
				return true;
			return false;
		}
		public boolean isShowCoombs6()
		{
			if(numOptCoombs>=6)
				return true;
			return false;
		}
		public boolean isShowCoombs7()
		{
			if(numOptCoombs>=7)
				return true;
			return false;
		}
		public boolean isShowCoombs8()
		{
			if(numOptCoombs>=8)
				return true;
			return false;
		}
		public boolean isShowCoombs9()
		{
			if(numOptCoombs>=9)
				return true;
			return false;
		}
		public boolean isShowCoombs10()
		{
			if(numOptCoombs>=10)
				return true;
			return false;
		}
		public boolean isShowCoombs11()
		{
			if(numOptCoombs>=11)
				return true;
			return false;
		}
		public boolean isShowCoombs12()
		{
			if(numOptCoombs>=12)
				return true;
			return false;
		}
		public boolean isShowCoombs13()
		{
			if(numOptCoombs>=13)
				return true;
			return false;
		}
		public boolean isShowCoombs14()
		{
			if(numOptCoombs>=14)
				return true;
			return false;
		}
		public boolean isShowCoombs15()
		{
			if(numOptCoombs>=15)
				return true;
			return false;
		}
		/**
		 * Checks the options of the coombs voting
		 */
		public void onValidateFromCoombsForm()
		{
			showErrorCoombs=false;
			showRepeatedCoombs=false;
			if(coombsOp1==null || coombsOp2==null)
			{

				showErrorCoombs=true;
			}

			switch(numOptCoombs)
			{
			case 15:
				if(coombsOp15==null){showErrorCoombs=true;}
			case 14:
				if(coombsOp14==null){showErrorCoombs=true;}
			case 13:
				if(coombsOp13==null){showErrorCoombs=true;}
			case 12:
				if(coombsOp12==null){showErrorCoombs=true;}
			case 11:
				if(coombsOp11==null){showErrorCoombs=true;}
			case 10:
				if(coombsOp10==null){showErrorCoombs=true;}
			case 9:
				if(coombsOp9==null){showErrorCoombs=true;}
			case 8:
				if(coombsOp8==null){showErrorCoombs=true;}
			case 7:
				if(coombsOp7==null){showErrorCoombs=true;}
			case 6:
				if(coombsOp6==null){showErrorCoombs=true;}
			case 5:
				if(coombsOp5==null){showErrorCoombs=true;}
			case 4:
				if(coombsOp4==null){showErrorCoombs=true;}
			case 3:
				if(coombsOp3==null){showErrorCoombs=true;}
				break;
			default:
				showErrorCoombs=false;
			}
			if(!showErrorCoombs)//añadir las opciones
			{
				List<String> listOptions=new LinkedList<String>();
				listOptions.add(coombsOp1);
				listOptions.add(coombsOp2);
				if(numOptCoombs>=3)
				{
					listOptions.add(coombsOp3);
				}
				if(numOptCoombs>=4)
				{
					listOptions.add(coombsOp4);
				}
				if(numOptCoombs>=5)
				{
					listOptions.add(coombsOp5);
				}
				if(numOptCoombs>=6)
				{
					listOptions.add(coombsOp6);
				}
				if(numOptCoombs>=7)
				{
					listOptions.add(coombsOp7);
				}
				if(numOptCoombs>=8)
				{
					listOptions.add(coombsOp8);
				}
				if(numOptCoombs>=9)
				{
					listOptions.add(coombsOp9);
				}
				if(numOptCoombs>=10)
				{
					listOptions.add(coombsOp10);
				}
				if(numOptCoombs>=11)
				{
					listOptions.add(coombsOp11);
				}
				if(numOptCoombs>=12)
				{
					listOptions.add(coombsOp12);
				}
				if(numOptCoombs>=13)
				{
					listOptions.add(coombsOp13);
				}
				if(numOptCoombs>=14)
				{
					listOptions.add(coombsOp14);
				}
				if(numOptCoombs>=15)
				{
					listOptions.add(coombsOp15);
				}
				for(int x=0;x<listOptions.size();x++)
				{
					for(int i=x+1;i<listOptions.size();i++)
					{
						if(listOptions.get(x).toLowerCase().equals(listOptions.get(i).toLowerCase()))
						{
							showRepeatedCoombs=true; 
						}

					}
				}
				coombs=new Coombs(listOptions);
			}
		}

		/**
		 * Stores all the necessary data of the ballot
		 * @return
		 */
		public Object onSuccessFromCoombsForm()
		{
			if(request.isXHR())
			{//añadir las opciones

				if(showErrorCoombs || showRepeatedCoombs)
				{
					ajaxResponseRenderer.addRender("coombsZone", coombsZone);
				}
				else //No hay errores
				{
					ballot=setBallotData();
					coombs.setId(UUID.generate());
					ballot.setIdBallotData(coombs.getId());
					coombs.setBallotId(ballot.getId());

					voteDao=DB4O.getVoteDao(datasession.getDBName());
					coombsDao=DB4O.getCoombsDao(datasession.getDBName());

					if(ballot.isTeaching())//Votacion Docente
					{
						//Genera votos aleatoriamente para la votacion docente
						ballot.setIdCensus("none");
						coombs.setVotes(GenerateDocentVotes.generateCoombs(coombs.getOptions(), Integer.parseInt(census)));

						Vote vote=new Vote(ballot.getId(),datasession.getId(),true);//Almacena vote para docente(solo el creador)
						
						ballot.setEnded(true);
						ballot.setCounted(true);
						voteDao.store(vote);
					}
					else if(ballotKind==BallotKind.PUBLICA){
						ballot.setIdCensus("none");
					}
					else
					{
						boolean creatorInCensus=false;
						
						for(String idUser:censusNormal.getUsersCounted())
						{
							if(idUser.equals(datasession.getId())){creatorInCensus=true;}

							voteDao.store(new Vote(ballot.getId(),idUser));//Almacena vote con ids de users censados
						}
						if(!creatorInCensus)
						{
							voteDao.store(new Vote(ballot.getId(),datasession.getId()));

						}

					}

					coombsDao.store(coombs);
					ballotDao.store(ballot);
					ballotIdSesion = ballot.getId();
					if(ballotKind==BallotKind.DOCENTE)
						return BallotWasCreated.class;
					else
						return AddImages.class;
				}
			}
			return null;
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////// TOOLS /////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		private Ballot setBallotData()
		{

			Ballot newBallot=new Ballot();
			newBallot.setId(oldBallot.getId());
			newBallot.setName(ballotName);
			newBallot.setEditable(oldBallot.isEditable());
			newBallot.setDescription(description);
			newBallot.setIdOwner(datasession.getId());
			if(ballotKind==BallotKind.PRIVADA)
			{
				newBallot.setNotStarted(true);
				newBallot.setIdCensus(censusNormal.getId());
			}
			else if(ballotKind==BallotKind.DOCENTE)
			{
				newBallot.setTeaching(true);
			}
			else
			{
				newBallot.setPublica(true);
			}
			newBallot.setMethod(method);
			newBallot.setStartDate(cal_inicio.getTime());
			newBallot.setEndDate(cal_final.getTime());
			return newBallot;
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
				return Index.class;
			case 1:
				if(datasession.isMaker())
				{
					return null;
				}
				return UnauthorizedAttempt.class;
			case 2:
				return null;
			case 3:
				return SessionExpired.class;
			case 4: 
				if(datasession.isTeacher()){
					return null;
				}
				return UnauthorizedAttempt.class;
			default:
				return Index.class;
			}

		}

	}

