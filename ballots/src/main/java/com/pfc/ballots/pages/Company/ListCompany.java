package com.pfc.ballots.pages.Company;

import java.util.List;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import com.pfc.ballots.dao.CompanyDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Company;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
import com.pfc.ballots.pages.admin.AdminMail;
import com.pfc.ballots.pages.profile.ProfileByFile;

public class ListCompany {

	
	@SessionState
	private DataSession datasession;
	
	@InjectPage
	private ListCompanyUsers listCompanyUsers;
	
	@InjectPage
	private CreateCompanyUser createCompanyUser;
	
	@InjectPage
	private ProfileByFile profileByFile;
	
	
	//****************************************Initialize DAO****************************//
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	CompanyDao companyDao=DB4O.getCompanyDao();
	
	@Property
	private Company company;
	

	
	public List<Company> getCompanies()
	{
		//check owner email and update them in the Main Database
		companyDao.updateAdminData();
		return companyDao.RetrieveAllCompanies();
	}
	
	public Object onActionFromVer(String CompanyName,String DBName)
	{
		System.out.println(CompanyName+" "+DBName);
		listCompanyUsers.setup(CompanyName,DBName);
		return listCompanyUsers;
	}
	
	public Object onActionFromAddbyfile(String DBName)
	{
		System.out.println(DBName);
		profileByFile.setup(DBName);
		return profileByFile;
	}
	public void onActionFromDelete(String CompanyName)
	{
		System.out.println(CompanyName);
		companyDao.deleteCompanyByEmail(CompanyName);
		
	}
	public Object onActionFromAddusers(String CompanyName,String DBName)
	{
		createCompanyUser.setup(CompanyName, DBName);
		return createCompanyUser;
	}
	/*
	public Path onActionFromDownload(String DBName)
	{
		String sep=System.getProperty("file.separator");
		String PATH=System.getProperty("user.home")+sep+"BallotsFiles"+sep+DBName;
		Path ruta=Paths.get(PATH);
		return ruta;
	}*/
	
	  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////// ON ACTIVATE //////////////////////////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 *  * return an int with the state of the session
		 * 		0->UserLogedIn;
		 * 		1->AdminLoged
		 * 		2->MainAdminLoged no email of the apliction configured
		 * 		3->not loged
		 * 		4->Session expired or kicked from server
		 */
	public Object onActivate()
	{
		switch(datasession.sessionState())
		{
			case 0:
				return UnauthorizedAttempt.class;
			case 1:
				if(datasession.isMainAdmin())
					{return null;}
				return UnauthorizedAttempt.class;
			case 2:
				return AdminMail.class;
			case 3:
				return Index.class;
			case 4:
				return SessionExpired.class;
			default:
				return Index.class;
		}
	}
}
