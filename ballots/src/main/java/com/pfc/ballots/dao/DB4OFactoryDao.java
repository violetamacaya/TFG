package com.pfc.ballots.dao;



public class DB4OFactoryDao extends FactoryDao{

	

	@Override
	public UserDao getUsuarioDao() 
	{
		return new UserDaoDB4O(null);
	}
	
	@Override
	public UserDao getUsuarioDao(String DBName)
	{
		return new UserDaoDB4O(DBName);
	}
	
	
	@Override
	public LogDao getLogDao() 
	{
		return new LogDaoDB4O();
	}

	

	@Override
	public CompanyDao getCompanyDao() 
	{
		return new CompanyDaoDB4O();
	}

	@Override
	public UserLogedDao getUserLogedDao() 
	{
		return new UserLogedDaoDB4O(null);
	}

	@Override
	public UserLogedDao getUserLogedDao(String DBName) 
	{
		return new UserLogedDaoDB4O(DBName);
	}

	@Override
	public CensusDao getCensusDao() 
	{
		return new CensusDaoDB4O(null);
	}

	@Override
	public CensusDao getCensusDao(String DBName) 
	{
		return new CensusDaoDB4O(DBName);
	}
	
	@Override 
	public EmailAccountDao getEmailAccountDao()
	{
		return new EmailAccountDaoDB4O(null);
	}
	@Override 
	public EmailAccountDao getEmailAccountDao(String DBName)
	{
		return new EmailAccountDaoDB4O(DBName);
	}
	
	


	 
	
	 

}
