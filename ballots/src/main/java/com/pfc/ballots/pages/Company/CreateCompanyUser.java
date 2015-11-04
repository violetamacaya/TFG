package com.pfc.ballots.pages.Company;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.ProfileCensedInDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.entities.ProfileCensedIn;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
import com.pfc.ballots.pages.admin.AdminMail;
import com.pfc.ballots.util.Encryption;
import com.pfc.ballots.util.UUID;
/**
 * 
 * CreateCompanyUser class is the controller for the CreateCompanyUser page that
 * allow to create a user for a company
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUN-2014
 */
public class CreateCompanyUser {

	@SessionState
	private DataSession datasession;
	
	
	
	//****************************** DAO **********************************//
	FactoryDao DB4O= FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao userDao=null;
	ProfileCensedInDao censedInDao=null;
	
	/**
	 * Set the values store a user in the correct DB
	 * @param CompanyName
	 * @param DBName
	 */
	public void setup(String CompanyName,String DBName)
	{
		this.DBName=DBName;
		this.CompanyName=CompanyName;
		profile=new Profile();
	}
	
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
	/**
	 * Create a user for a company
	 */
	 Object onSuccess()
	{
		userDao=DB4O.getUsuarioDao(DBName);
		censedInDao=DB4O.getProfileCensedInDao(DBName);
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
			
			profile.setId(UUID.generate());
			profile.setRegDatetoActual();
			ProfileCensedIn censedIn =new ProfileCensedIn(profile.getId());
			userDao.store(profile);
			censedInDao.store(censedIn);
			profile=new Profile();
		}
		else{
			return null;
		}
		return ListCompanyUsers.class;
	}
	
	  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////// ON ACTIVATE //////////////////////////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	* Controls if the user can enter in the page
	* @return another page if the user can't enter
	*/
	public Object onActivate()
	{
		switch(datasession.sessionState())
		{
			case 0:
				return Index.class;
			case 1:
				return UnauthorizedAttempt.class;
			case 2:
				if(datasession.isMainUser())
				{
					return null;
				}
				return UnauthorizedAttempt.class;
			case 3:
				return SessionExpired.class;
			default:
				return Index.class;
		}
		
	}
}
