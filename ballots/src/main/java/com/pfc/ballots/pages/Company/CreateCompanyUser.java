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
	public Object onActivate()
	{
		switch(datasession.sessionState())
		{
			case 0:
				System.out.println("LOGEADO");
				if(datasession.isMainAdmin())
				{
					return null;
				}
				return UnauthorizedAttempt.class;
			case 1:
				System.out.println("NO LOGEADO");
				return Index.class;
			case 2:
				System.out.println("SESION EXPIRADA");
				return SessionExpired.class;
			default:
				return Index.class;
		}
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
	
}
