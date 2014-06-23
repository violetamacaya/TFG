package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.Company;

public interface CompanyDao {
	public void store(Company company);
	public Company getCompanyByName(String companyName);
	public List<Company> RetrieveAllCompanies();
	public void deleteCompanyByEmail(String email);
	public void updateCompany(Company company);
	public boolean isCompanyRegistred(String CompanyName);
	public boolean isDBNameRegistred(String DBName);
}
