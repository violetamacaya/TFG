package com.pfc.ballots.pages.Company;

import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.entities.Profile;

public class ListCompanyUsers {

	@Property
	private Profile user;
	
	
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
	
	
}
