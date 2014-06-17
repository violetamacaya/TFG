package com.pfc.ballots.pages.Company;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
import com.pfc.ballots.pages.admin.AdminMail;

public class ListCompanyUsers {

	@SessionState
	private DataSession datasession;
	
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
	  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////// ON ACTIVATE //////////////////////////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 *  * return an int with the state of the session
		 * 		0->UserLogedIn;
		 * 		1->AdminLoged
		 * 		2->MainAdminLoged no email of the apliction configured
		 * 		3->not loged
		 * 		4->Session expired or kicked from server
		 */
	public Object onActivate()
	{
		switch(datasession.sessionState())
		{
			case 0:
				return UnauthorizedAttempt.class;
			case 1:
				if(datasession.isMainAdmin())
					{return null;}
				return UnauthorizedAttempt.class;
			case 2:
				return AdminMail.class;
			case 3:
				return Index.class;
			case 4:
				return SessionExpired.class;
			default:
				return Index.class;
		}
	
	}
}
