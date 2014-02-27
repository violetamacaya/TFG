package com.pfc.ballots.dao;


import com.pfc.ballots.dao.DB4OFactoryDao;
import com.pfc.ballots.dao.UserDao;

public abstract class FactoryDao {

	
	public static final int DB4O_FACTORY = 1;
	public abstract UserDao getUsuarioDao();
	public abstract UserDao getUsuarioDao(String DBName);
	public abstract LogDao getLogDao();
	public abstract CompanyDao getCompanyDao();
	
	

	
	
	public static FactoryDao getFactory(int claveFactory) {
		
		switch (claveFactory) {
	
			case DB4O_FACTORY:
			
				return new DB4OFactoryDao();
				
			//any new kind of db would go here, for example
			//case SQL_FACTORY (would be number 2 up there)

			default:
			
				throw new IllegalArgumentException();
		}
	}
	
}
