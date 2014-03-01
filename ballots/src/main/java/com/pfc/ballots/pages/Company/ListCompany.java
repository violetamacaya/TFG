package com.pfc.ballots.pages.Company;

import java.util.List;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import com.pfc.ballots.dao.CompanyDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Company;

public class ListCompany {

	
	@SessionState
	private DataSession datasession;
	
	@InjectPage
	private ListCompanyUsers listCompanyUsers;
	
	
	//****************************************Initialize DAO****************************//
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	CompanyDao companyDao=DB4O.getCompanyDao();
	
	@Property
	private Company company;
	
	public List<Company> getCompanies()
	{
		return companyDao.RetrieveAllCompanies();
	}
	
	public Object onActionFromVer(String CompanyName,String DBName)
	{
		System.out.println(CompanyName+" "+DBName);
		listCompanyUsers.setup(CompanyName,DBName);
		return listCompanyUsers;
	}
	public void onActionFromDelete(String CompanyName)
	{
		System.out.println(CompanyName);
		companyDao.deleteCompanyByEmail(CompanyName);
	}
}
