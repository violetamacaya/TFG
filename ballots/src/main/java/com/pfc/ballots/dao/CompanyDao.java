package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.Company;


/**
 * Dao Interface to retrieves Company entity
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 *
 */

public interface CompanyDao {
	public void store(Company company);
	public Company getCompanyByName(String companyName);
	public Company getCompanyByAlias(String alias);
	public List<Company> RetrieveAllCompanies();
	public void deleteCompanyByName(String companyName);
	public void updateCompany(Company company);
	public boolean isCompanyRegistred(String CompanyName);
	public boolean isAliasRegistred(String alias);
	public boolean isDBNameRegistred(String DBName);
	public boolean isActive(String companyName);
	
}
