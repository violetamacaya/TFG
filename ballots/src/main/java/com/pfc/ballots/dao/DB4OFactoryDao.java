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
		// TODO Auto-generated method stub
		return new UserDaoDB4O(DBName);
	}


	 
	
	 

}
