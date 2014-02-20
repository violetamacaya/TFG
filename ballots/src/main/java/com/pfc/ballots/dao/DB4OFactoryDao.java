package com.pfc.ballots.dao;



public class DB4OFactoryDao extends FactoryDao{

	

	@Override
	public UserDao getUsuarioDao() {
		
		return new UserDaoDB4O();
	}
	
	@Override
	public LogDao getLogDao() {
		return new LogDaoDB4O();
	}

	 
	
	 

}
