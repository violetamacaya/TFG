package com.pfc.ballots.pages.Company;

import java.io.File;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.CompanyDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.dao.UserLogedDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Company;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
/**
 * UpdateCompany class is the controller for the UpdateCompany page that
 * allow to change the name of a company
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUN-2014
 */
public class UpdateCompany {

	@SessionState
	private DataSession datasession;
	
	@Inject
    private ComponentResources componentResources;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	
	
	
	@Persist
	private boolean notDone;
	
	@Persist
	private String companyName;
	
	//****************************************Initialize DAO****************************//
		FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
		@Persist
		CompanyDao companyDao;
		UserLogedDao lgdDao=null;
	
	
	/**
	 * Initializes the page
	 * @param companyName
	 */
	public void setup(String companyName)
	{
		notDone=true;
		this.companyName=companyName;
	}
	
	public void setupRender()
	{
		companyDao=DB4O.getCompanyDao();
		oldCompany=companyDao.getCompanyByName(companyName);
		newCompany= new Company();
		newCompany.setCompanyName(oldCompany.getCompanyName());
		
	
		showForm=true;
		showNameInUse=false;
		
		showBadChar=false;
		showBadName=false;
		
		noChanges=true;
	}

	@Persist
	@Property
	private Company oldCompany;
	@Persist
	@Property
	private Company newCompany;
	
	@InjectComponent
	private Zone formZone;
	
	@Persist
	@Property
	private boolean showForm;
	
	@Persist
	@Property
	private boolean showNameInUse;

	@Persist
	@Property
	private boolean showBadChar;
	@Persist
	@Property
	private boolean showBadName;
	@Persist
	private boolean noChanges;
	
	/**
	 * Validates the data of the the UpdateCompanyForm
	 */
	public void onValidateFromUpdateCompanyForm()
	{
		newCompany.setCompanyName(newCompany.getCompanyName().toLowerCase());
		if(oldCompany.getCompanyName().equals(newCompany.getCompanyName()))
		{
			noChanges=false;
		}
		else
		{
			if(newCompany.getCompanyName().contains("ñ"))
			{
				showBadChar=true;
			}
			if(newCompany.getCompanyName().equals("login") )
			{
				showBadName=true;
			}
			if(companyDao.isCompanyRegistred(newCompany.getCompanyName()))
			{
				showNameInUse=true;
			}
		}
			
		
	}
	
	/**
	 * Handle the success of the UpdateCompanyForm
	 */
	public Object onSuccessFromUpdateCompanyForm()
	{
		if(request.isXHR())
		{
			if(!noChanges)
			{
				return ListCompany.class;
			}
			if(showBadChar || showBadName || showNameInUse)
			{
				ajaxResponseRenderer.addRender("formZone", formZone);
				return null;
			}
			oldCompany.setCompanyName(newCompany.getCompanyName());
			companyDao.updateCompany(oldCompany);
			notDone=false;
			return ListCompany.class;
		}
		return null;
	}
	
	/**
	 * Cancels the update of the name
	 * @return listComany page
	 */
	public Object onActionFromCancelBut()
	{
		notDone=false;
		return ListCompany.class;
	}
	/**
	* Controls if the user can enter in the page
	* @return another page if the user can't enter
	*/
	public Object onActivate()
	{
		if(notDone=false)
		{
			return ListCompany.class;
		}
		switch(datasession.sessionState())
		{
			case 0:
				return Index.class;
			case 1:
				return UnauthorizedAttempt.class;
			case 2:
				if(datasession.isMainUser())
				{
					return null;
				}
				return UnauthorizedAttempt.class;
			case 3:
				return SessionExpired.class;
			default:
				return Index.class;
		}
		
	}
	
	
}
