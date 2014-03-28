package com.pfc.ballots.dao;



public class DB4OFactoryDao extends FactoryDao{

	

	@Override
	public UserDao getUsuarioDao() {
		
		return new UserDaoDB4O(null);
	}
	
	@Override
	public LogDao getLogDao() {
		return new LogDaoDB4O();
	}

	@Override
	public UserDao getUsuarioDao(String DBName) {
		
		return new UserDaoDB4O(DBName);
	}

	@Override
	public CompanyDao getCompanyDao() {
		// TODO Auto-generated method stub
		return new CompanyDaoDB4O();
	}

	@Override
	public UserLogedDao getUserLogedDao() {
		// TODO Auto-generated method stub
		return new UserLogedDaoDB4O(null);
	}

	@Override
	public UserLogedDao getUserLogedDao(String DBName) {
		// TODO Auto-generated method stub
		return new UserLogedDaoDB4O(DBName);
	}
	


	 
	
	 

}
