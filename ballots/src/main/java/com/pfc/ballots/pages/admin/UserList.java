package com.pfc.ballots.pages.admin;

import java.util.List;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Profile;

public class UserList {


	@SessionState
	private DataSession datasession;
	//****************************************Initialize DAO****************************//
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao dao =DB4O.getUsuarioDao(datasession.getDBName());
	
	
	@Property
	private Profile user;
	
	public List<Profile> getUsers()
	{
		return dao.RetrieveAllProfilesSortLastLog();
	}
}
