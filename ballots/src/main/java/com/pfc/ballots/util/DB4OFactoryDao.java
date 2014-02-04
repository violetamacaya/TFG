package com.pfc.ballots.util;



public class DB4OFactoryDao extends FactoryDao{

	
	 @Override
	    public UserDao getUsuarioDao() {
	    	
	        return new UserDaoDB4O();
	    }

}
