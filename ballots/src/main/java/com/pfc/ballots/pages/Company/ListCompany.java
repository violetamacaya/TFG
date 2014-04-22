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
	
	public Object onActivate()
	{
		switch(datasession.sessionState())
		{
			case 0:
				System.out.println("LOGEADO");
				if(datasession.isMainAdmin())
				{
					return null;
				}
				return UnauthorizedAttempt.class;
			case 1:
				System.out.println("NO LOGEADO");
				return Index.class;
			case 2:
				System.out.println("SESION EXPIRADA");
				return SessionExpired.class;
			default:
				return Index.class;
		}
	}
	
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
}
