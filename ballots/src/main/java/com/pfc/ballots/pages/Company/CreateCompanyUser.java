package com.pfc.ballots.pages.Company;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.util.Encryption;
import com.pfc.ballots.util.UUID;

public class CreateCompanyUser {

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
	
}
