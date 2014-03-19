package com.pfc.ballots.pages.admin;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Profile;

public class UserList {


	@InjectComponent
	private Zone usergrid;
	
	@Inject
	private Request request;
	
	@SessionState
	private DataSession datasession;
	//****************************************Initialize DAO****************************//
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao userDao =DB4O.getUsuarioDao(datasession.getDBName());
	
	
	@Property
	private Profile user;
	
	public List<Profile> getUsers()
	{
		return userDao.RetrieveAllProfilesSortLastLog();
	}
	
	public Object onActionFromDeleteuser(String email)
	{
		userDao.deleteByEmail(email);
		return request.isXHR() ? usergrid.getBody() : null;
	}
}
