package com.pfc.ballots.pages.ballot;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.plastic.Cache;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.CensusDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.data.BallotKind;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.data.Method;
import com.pfc.ballots.encoder.CensusEncoder;
import com.pfc.ballots.entities.Census;

public class CreateBallot {
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



	@InjectComponent
	private Zone mainZone;

	static final private String[] NUMBERS2_7 = new String[] { "2", "3", "4","5","6","7" };
	
	private enum Actions{
		S_MAYREL,C_MAYREL
	}
	private Actions action;
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////////// DAO //////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	@Persist
	CensusDao censusDao;
	
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////// INITIALIZE ///////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();
		
		censusDao=DB4O.getCensusDao(datasession.getDBName());
		censuses=censusDao.getByOwnerId(datasession.getId());
		showLoopMayRel=false;
		showCensus=false;
		showError=false;
		if(censuses.size()==0)
		{
			existCensus=false;
		}
		else
		{
			existCensus=true;
		}
		selectedKind=false;
		censusModel=selectModelFactory.create(censuses, "censusName");
	}
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////// BASIC ZONE ///////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Property
	@Persist
	@Validate("minLength=5")
	private String ballotName;
	@Property
	@Persist
	private Method method;
	@Property
	@Persist
	private BallotKind ballotKind;
	
	@Persist
	private List<Census> censuses;
	@Property
	@Persist
	private Census census;
	
	@Property
	@Persist
	private SelectModel censusModel;
	@Inject
	private SelectModelFactory selectModelFactory;
	@Property
    private String dateInFormatStr = "dd/MM/yyyy";
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
	@Validate("regexp=^([0-2]?[0-9]|3[0-1])/([0-1][0-2]|0[0-9]|[0-9])/2[0-9][1-9][4-9]$")
	private Date endDate;
	@Property
	@Persist
	@Validate("regexp=^([0-1]?[0-9]|2[0-3])\\:[0-5][0-9]\\:[0-5][0-9]$")
	private String endHour;
	public CensusEncoder getCensusEncoder()
	{
	  return new CensusEncoder(censuses);
	}

	@Property
	@Persist
	private boolean showCensus;
	@Property
	@Persist
	private boolean existCensus;
	@Property
	@Persist
	private boolean selectedKind;
	@Property
	@Persist
	private boolean showError;
	
	
	
	//Restore the values
	private void notShowZones()
	{
		showMayoriaRel=false;
		showKemeny=false;
	}
	
	public void onValueChangedFromMethod(Method temp)
	{
		System.out.println("CHANGED->"+temp);
		notShowZones();
		if(temp==null)
		{
			
		}
		else if(temp==Method.MAYORIA_RELATIVA)
		{
			showMayoriaRel=true;
			mayRelOpModel=NUMBERS2_7;
			mayRelNum=0;
		}
		else if(temp==Method.KEMENY)
		{
			showKemeny=true;
		}
		
		
		ajaxResponseRenderer.addRender("mainZone",mainZone);
	}
		
	 public void onValueChangedFromCensusSelect(Census temp)
	 {
		
	 }
	 
	 public void onValueChangedFromKind(BallotKind kind)
	 {
		 System.out.println("CHANGED->"+kind);
		 
		 //Comprueba si hay una opcion elegida
		 if(kind==null)
		 {
			 selectedKind=false;
		 }
		 else
		 {
			selectedKind=true;			
	 	 }
		 
		 //Comprueba que tipo de opcion esta elegida y si hay censos
		 if(kind==BallotKind.NORMAL)
		 {
			 if(existCensus==false)
			 {
				 selectedKind=false;
			 }
			 showCensus=true;
		 }
		 else 
		 {
			 showCensus=false;
		 }
			ajaxResponseRenderer.addRender("mainZone",mainZone);
	 }
	 
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////// MAYORIA ZONE /////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 
	@Persist
	@Property
	private boolean showMayoriaRel;
	 
	
	@Persist
	@Property
	private boolean showLoopMayRel;
	@Persist
	@Property
	private boolean showBadDate;
	@Property
	@Persist
	private  String [] mayRelOpModel;

	
	@Persist
	@Property
	private String mayRelOpNum;
	@Property
	@Persist
	private int mayRelNum;
	@Property
	@Persist
	private String [] mayRelOps;
	
	private String mayRelOp;
	
	public String getMayRelOp()
	{
		return this.mayRelOp;
	}
	public void setMayRelOp(String mayRelOp)
	{
		System.out.println("aqui->"+mayRelOp+"num->"+mayRelOps.length);
		this.mayRelOp=mayRelOp;
	}
	
	public void onValueChangedFromMayRelNumSelect(String temp)
	{
		if(temp==null)
		{
			showLoopMayRel=false;
		}
		else
		{
			showLoopMayRel=true;
			mayRelNum=Integer.parseInt(temp);
			
			mayRelOps=new String[mayRelNum];
			
		}	
		System.out.println("NUM->"+mayRelNum);
		ajaxResponseRenderer.addRender("mainZone",mainZone);
	}
	public void onSelectedFromSaveMayRel()
	{
		action=Actions.S_MAYREL;
	}
	public void onSelectedFromCancelMayRel()
	{
		action=Actions.C_MAYREL;
	}

  	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////// KEMENY ZONE //////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Persist
	@Property
	private boolean showKemeny;
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////// VALIDATE AND SUCCESS /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void onValidateFromTotalForm()
	{
		showError=false;
		showBadDate=false;
		if(ballotName==null)
		{
			showError=true;
		}
		if(ballotKind==BallotKind.NORMAL)
		{
			Calendar cal1=new GregorianCalendar();;
			Calendar cal2=new GregorianCalendar();;
			Calendar cal3=new GregorianCalendar();;
			if(census==null)
			{
				showError=true;
			}
			if(startDate==null)
			{
				startDate=new Date();
			}
			if(startHour!=null)
			{
				String [] temp=startHour.split(":");
				
				
				cal1.setTime(startDate);
				int hora=cal1.get(Calendar.HOUR_OF_DAY);
				int min=cal1.get(Calendar.MINUTE);
				int sec=cal1.get(Calendar.SECOND);
				
				cal1.add(Calendar.HOUR, Integer.parseInt(temp[0])-hora);
				cal1.add(Calendar.MINUTE,Integer.parseInt(temp[1])-min);
				cal1.add(Calendar.SECOND, Integer.parseInt(temp[2])-sec);
				
				startDate=cal1.getTime();
				
			}
			if(endDate==null)
			{
				showError=true;
			}
			else if(endHour!=null)
			{
				String [] temp=startHour.split(":");
				cal1.setTime(startDate);
				cal2.setTime(endDate);
				cal2.add(Calendar.HOUR, Integer.parseInt(temp[0]));
				cal2.add(Calendar.MINUTE,Integer.parseInt(temp[1]));
				cal2.add(Calendar.SECOND, Integer.parseInt(temp[2]));
				cal3.setTime(new Date());
				
				if(cal2.before(cal1))
				{
					showBadDate=true;
					
				}
				else if(cal2.before(cal3))
				{
					showBadDate=true;
				}
				
			}
			else
			{
				cal1.setTime(startDate);
				cal2.setTime(endDate);
				cal3.setTime(new Date());
				if(cal2.before(cal1))
				{
					showBadDate=true;
				}
				if(cal2.before(cal3))
				{
					showBadDate=true;
				}
			}
		}
		else//VOTACION DOCENTE
		{
			startDate=new Date();
			endDate=new Date();
		}
		
		//COMPROBACIONES MAYORIA RELATIVA
		if(method==Method.MAYORIA_RELATIVA)
		{
			for(String temp:mayRelOps)
			{
				System.out.println("CHECk"+temp);
				if(temp==null)
				{
					showError=true;
				}
			}
		}
		
	}
	
	
	public void onSuccess()
	{
		if(showError|| showBadDate)
		{
			ajaxResponseRenderer.addRender("mainZone",mainZone);
		}
	}

	
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////// ONACTIVATE ///////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
		
	public void onActivate()
	{
		
	}
}
