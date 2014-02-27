package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.Company;

public interface CompanyDao {
	public void store(Company company);
	public List<Company> RetrieveAllCompanies();
	public boolean isCompanyRegistred(String CompanyName);
	public boolean isDBNameRegistred(String DBName);
}
