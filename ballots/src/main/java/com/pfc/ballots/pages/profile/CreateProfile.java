package com.pfc.ballots.pages.profile;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.beaneditor.Validate;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
import com.pfc.ballots.util.Encryption;
import com.pfc.ballots.util.UUID;

public class CreateProfile {

	@SessionState
	private DataSession datasession;
	
	@Inject
    private ComponentResources componentResources;

	@Property
	@Persist
	private Profile profile;
	
	@Validate("required")
	@Property
	private String password;
	@Validate("required")
	@Property
	private String repeat;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean isnotPassOk;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean isnotAvalible;
	
	@Persist
	private boolean isnotFirstTime;
	//****************************************Initialize DAO****************************//
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	@Persist
	UserDao dao;
	
	
	
	
	
	/**
	 * 
	 *  Pre-render page method for initialice and erase(in that case) 
	 *  the variables with persistence. 
	 * 
	 */
	
	void setupRender() 
	{
		dao=DB4O.getUsuarioDao(datasession.getDBName());
		if(!isnotFirstTime)
		{
			profile=new Profile();
			isnotFirstTime=true;
		}
		else if(!isnotPassOk && !isnotAvalible)
		{
			componentResources.discardPersistentFieldChanges();
			profile=new Profile();
		}
		
		
	}
	public Object onActivate()
	{
		switch(datasession.sessionState())
		{
			case 0:
				System.out.println("LOGEADO");
				if(datasession.isAdmin())
				{
					return null;
				}
				return UnauthorizedAttempt.class;
			case 1:
				System.out.println("NO LOGEADO");
				return null;
			case 2:
				System.out.println("SESION EXPIRADA");
				return SessionExpired.class;
			default:
				return Index.class;
		}
	}
	void onSuccess()
	{
		if(!password.equals(repeat))
		{
			isnotPassOk=true;
		}
		if(dao.isProfileRegistred(profile.getEmail()))
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
			dao.store(profile);
			componentResources.discardPersistentFieldChanges();
		}
			
	}
}
