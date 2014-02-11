package com.pfc.ballots.pages.profile;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.util.Encryption;
import com.pfc.ballots.util.UUID;

public class CreateProfile {

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
	UserDao dao =DB4O.getUsuarioDao();
	
	
	/**
	 * 
	 *  Pre-render page method for initialice and erase(in that case) 
	 *  the variables with persistence. 
	 * 
	 */
	
	void setupRender() 
	{
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
			//Encryption password and store in database
			String encrypt=Encryption.getStringMessageDigest(password, Encryption.SHA1);
			profile.setPassword(encrypt);
			profile.setId(UUID.generate());
			dao.store(profile);
			componentResources.discardPersistentFieldChanges();
		}
			
	}
}
