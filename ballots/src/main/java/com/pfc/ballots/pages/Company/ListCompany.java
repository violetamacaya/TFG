package com.pfc.ballots.pages.Company;

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

import com.pfc.ballots.dao.CompanyDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserLogedDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Company;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
import com.pfc.ballots.pages.admin.AdminMail;
import com.pfc.ballots.pages.profile.ProfileByFile;
/**
 * 
 * ListCompany class is the controller for the ListCompany page that
 * shows the list of companies and allow to administrate them
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUN-2014
 */
public class ListCompany {

	
	@SessionState
	private DataSession datasession;
	
	@InjectPage
	private ListCompanyUsers listCompanyUsers;
	
	@InjectPage
	private CreateCompanyUser createCompanyUser;
	
	@InjectPage
	private UpdateCompany updateCompany;
	
	@InjectPage
	private ProfileByFile profileByFile;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	
	
	//**************************************** DAO ****************************//
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	CompanyDao companyDao=DB4O.getCompanyDao();
	UserLogedDao userLogedDao=null;
	@Property
	private Company company;
	

	  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
     ////////////////////////////////////////////////////// INITIALIZE PAGE /////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Initialize data
	 */
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();
		companies=null;
		showSure=false;
	}
	
	
	  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ///////////////////////////////////////////////////// COMPANY GRID ZONE ////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@InjectComponent
	private Zone companyGridZone;
	
	@Persist
	private List<Company> companies;
	
	
	public List<Company> getCompanies()
	{
		if(companies==null)
		{
			companies=companyDao.RetrieveAllCompanies();
		}
		return companies;
	}
	/**
	 * returns a page where you can edit the company
	 * @param CompanyName
	 * @param DBName
	 * @return
	 */
	public Object onActionFromEditBut(String CompanyName)
	{
		
		updateCompany.setup(CompanyName);
		return updateCompany;
	}
	/**
	 * returns a page where you can see the users in the company
	 * @param CompanyName
	 * @param DBName
	 * @return
	 */
	public Object onActionFromVer(String CompanyName,String DBName)
	{
		System.out.println(CompanyName+" "+DBName);
		listCompanyUsers.setup(CompanyName,DBName);
		return listCompanyUsers;
	}
	/**
	 * returns a page where you can upload a file to create users
	 * @param DBName
	 * @return
	 */
	public Object onActionFromAddbyfile(String DBName)
	{
		System.out.println(DBName);
		profileByFile.setup(DBName);
		return profileByFile;
	}
	/**
	 * Deletes a company
	 * @param CompanyName
	 */
	public void onActionFromDelete(String CompanyName)
	{
		System.out.println(CompanyName);
		companyDao.deleteCompanyByName(CompanyName);
		
	}
	/**
	 * returns a page where you can add users to the company
	 * @param CompanyName
	 * @param DBName
	 * @return
	 */
	public Object onActionFromAddusers(String CompanyName,String DBName)
	{
		createCompanyUser.setup(CompanyName, DBName);
		return createCompanyUser;
	}
	/**
	 * Deactivate a company
	 * @param companyName
	 */
	public void onActionFromDeactivateBut(String companyName)
	{
		if(request.isXHR())
		{
			status=false;
			showSure=true;
			toCheck=lookforname(companyName);
			
			ajaxResponseRenderer.addRender("companyGridZone", companyGridZone).addRender("areuSureZone",areuSureZone);
		}
	}
	/**
	 * Activate a company
	 * @param companyName
	 */
	public void onActionFromActivateBut(String companyName)
	{
		if(request.isXHR())
		{
			status=true;
			showSure=true;
			toCheck=lookforname(companyName);
			
			ajaxResponseRenderer.addRender("companyGridZone", companyGridZone).addRender("areuSureZone",areuSureZone);

		}
	}
	
	  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////// AREUSURE ZONE //////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@InjectComponent
	private Zone areuSureZone;
	
	@Persist
	@Property
	private boolean showSure;
	@Persist
	@Property
	Company toCheck;
	@Persist
	@Property
	private boolean status;//false = desactivar compañia
						   //true  = activar compañia
	
	public void onActionFromIsSureBut()
	{
		if(request.isXHR())
		{
			if(status)
			{
				toCheck.setActive(true);
				companyDao.updateCompany(toCheck);
			}
			else
			{
				toCheck.setActive(false);
				userLogedDao=DB4O.getUserLogedDao(toCheck.getDBName());
				companyDao.updateCompany(toCheck);
				
				
			}
			showSure=false;
			ajaxResponseRenderer.addRender("companyGridZone", companyGridZone).addRender("areuSureZone",areuSureZone);
		}
	}
	public void onActionFromIsNotSureBut()
	{
		if(request.isXHR())
		{
			showSure=false;
			ajaxResponseRenderer.addRender("companyGridZone", companyGridZone).addRender("areuSureZone",areuSureZone);
		}
	}
	
	  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////////// UTILS /////////////////////////////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Company lookforname(String companyName)
	{
		for(int i=0;i<companies.size();i++)
		{
			if(companies.get(i).getCompanyName().equals(companyName))
			{
				return companies.get(i);
			}
		}
		return null;
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
