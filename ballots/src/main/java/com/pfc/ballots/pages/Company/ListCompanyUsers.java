package com.pfc.ballots.pages.Company;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.entities.Profile;

public class ListCompanyUsers {

	@Property
	private Profile user;
	
	@InjectComponent
	private Zone usergrid;
	
	@Inject
	private Request request;
	
	
	@Property
	@Persist
	private String DBName;
	@Property
	@Persist
	private String companyName;
	
	//******************************************     DAO    ***************************************************//
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao userDao=null;
	
	
	
	public void setup(String companyName,String DBName)
	{
		this.DBName=DBName;
		this.companyName=companyName;
		
	}
	
	public List<Profile> getUsers()
	{
		userDao=DB4O.getUsuarioDao(DBName);
		return userDao.RetrieveAllProfiles();
	}
	public Object onActionFromDeleteuser(String email)
	{
		userDao=DB4O.getUsuarioDao(DBName);
		userDao.deleteByEmail(email);
		return request.isXHR() ? usergrid.getBody() : null;
	}
	
}
