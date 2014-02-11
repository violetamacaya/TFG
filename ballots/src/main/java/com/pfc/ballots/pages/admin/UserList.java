package com.pfc.ballots.pages.admin;

import java.util.List;

import org.apache.tapestry5.annotations.Property;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.entities.Profile;

public class UserList {

	//****************************************Initialize DAO****************************//
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao dao =DB4O.getUsuarioDao();
	
	
	@Property
	private Profile user;
	
	public List<Profile> getUsers()
	{
		return dao.RetrieveAllProfiles();
	}
}
