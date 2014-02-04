package com.pfc.ballots.util;


import com.pfc.ballots.util.UserDao;
import com.pfc.ballots.util.DB4OFactoryDao;

public abstract class FactoryDao {

	
	public static final int DB4O_FACTORY = 1;
	public abstract UserDao getUsuarioDao();
	
	
	
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
