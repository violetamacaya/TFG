package com.pfc.ballots.pages.Company;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
import com.pfc.ballots.pages.admin.AdminMail;
import com.pfc.ballots.util.Encryption;
import com.pfc.ballots.util.UUID;

public class CreateCompanyUser {

	@SessionState
	private DataSession datasession;
	
	@Property
	@Persist
	private String DBName;
	@Property
	@Persist
	private String CompanyName;
	
	
	@Property
	@Persist
	private Profile profile;
	
	
	@Property
	private String password;
	@Property
	private String repeat;
	
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean isnotAvalible;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean isnotPassOk;
	
	//****************************** DAO **********************************//
	FactoryDao DB4O= FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao userDao=null;
	
	
	public void setup(String CompanyName,String DBName)
	{
		this.DBName=DBName;
		this.CompanyName=CompanyName;
		profile=new Profile();
	}
	
	public void onSuccess()
	{
		userDao=DB4O.getUsuarioDao(DBName);
		if(!password.equals(repeat))
		{
			isnotPassOk=true;
		}
		if(userDao.isProfileRegistred(profile.getEmail()))
		{
			isnotAvalible=true;
		}
		
		if(!isnotPassOk && !isnotAvalible)
		{
			//Encryption password,and store in database
			String encrypt=Encryption.getStringMessageDigest(password, Encryption.SHA1);
			profile.setPassword(encrypt);
			profile.setPlain(password);
			profile.setId(UUID.generate());
			profile.setRegDatetoActual();
			userDao.store(profile);
			profile=new Profile();
		}
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
				return null;
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
