package com.pfc.ballots.pages.ballot;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.SelectModelFactory;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.CensusDao;
import com.pfc.ballots.dao.FactoryDao;
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
	
	@Property
	@Persist
	private boolean showPage;
	@InjectComponent
	private Zone mainZone;

	static final private String[] NUMBERS2_7 = new String[] { "2", "3", "4","5","6","7" };
	
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
	
		if(censuses.size()==0)
		{
			showPage=false;
		}
		else
		{
			showPage=true;
		}
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
	private Date startDate;
	@Property
	@Persist
	@Validate("regexp=([0-9]|[0-1][0-9]|2[0-3])\\:[0-5][0-9]\\:[0-5][0-9]")
	private String startHour;
	@Property
	@Persist
	
	private Date endDate;
	@Property
	@Persist
	@Validate("regexp=([0-9]|[0-1][0-9]|2[0-3])\\:[0-5][0-9]\\:[0-5][0-9]")
	private String endHour;
	public CensusEncoder getCensusEncoder()
	{
	  return new CensusEncoder(censuses);
	}
	
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
		 System.out.println("CHANGED->"+temp.getCensusName());
	 }
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////// MAYORIA ZONE /////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 
	@Persist
	@Property
	private boolean showMayoriaRel;
	 
	
	@Property
	@Persist
	private  String [] mayRelOpModel;

	
	
	@Property
	private String mayRelOpNum;
	@Property
	@Persist
	private int mayRelNum;
	@Property
	@Persist
	private String [] mayRelOps;
	@Property
	private String mayRelOp;
	
	public void onValueChangedFromMayRelNumSelect(String temp)
	{
		mayRelNum=Integer.parseInt(temp);
		mayRelOps=new String[mayRelNum];
		for(int i=0;i<mayRelOps.length;i++)
		{
			mayRelOps[i]=String.valueOf(i);
		}
			
		System.out.println("NUM->"+mayRelNum);
		ajaxResponseRenderer.addRender("mainZone",mainZone);
	}
	
	
  	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////// KEMENY ZONE //////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Persist
	@Property
	private boolean showKemeny;
}
