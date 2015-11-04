package com.pfc.ballots.pages.Company;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import com.pfc.ballots.dao.CompanyDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Company;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
import com.pfc.ballots.util.Encryption;
import com.pfc.ballots.util.UUID;
/**
 * 
 * CreateCompany class is the controller for the CreateCompany page that
 * allow to create a company
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 * @author Violeta Macaya Sánchez
 * @version 1.1 OCT-2015
 */
public class CreateCompany {

	@SessionState
	private DataSession datasession;

	@Inject
	private ComponentResources componentResources;

	/// Form objects
	@Persist
	@Property
	private Company company;
	
	@Persist
	@Property
	private Profile profile;

	@Property
	private String password;
	
	@Property
	private String repeat;

	//Form booleans
	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean isnotPassOk;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean isnotCompanyNameAvalible;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean isnotDBNameAvalible;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean isBadChar;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean isBadName;

	@Property 
	@Persist(PersistenceConstants.FLASH)
	private boolean badSecurity;
	
	@Property 
	@Persist(PersistenceConstants.FLASH)
	private boolean badAlias;

	//FirstTime enter in the page
	@Persist
	private boolean isnotFirstTime;


	final String [] caracteresEspeciales={"!","¡","@","|","#","$","%","&","/","(",")","=","¿","?","*","+","-","_", "ñ"};
	//****************************************Initialize DAO****************************//
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	CompanyDao companyDao= DB4O.getCompanyDao();
	UserDao userDao =null;

	/**
	 * Initialize data
	 */
	void setupRender() 
	{
		if(!isnotFirstTime) // es la primera vez
		{
			profile=new Profile();
			company=new Company();
			company.setActive(true);
			isnotFirstTime=true;
		}
		else if(!isnotPassOk && !isnotCompanyNameAvalible && !isnotDBNameAvalible && !badAlias)
		{

			//profile=new Profile();
			//company=new Company();
			company.setActive(true);
		}
	}



	/**
	 * Stores the company
	 */
	Object onSuccess()
	{

		company.setCompanyName(company.getCompanyName().toLowerCase());
		if(company.getCompanyName().equals("login"))
		{
			isBadName=true;
		}

		if(companyDao.isCompanyRegistred(company.getCompanyName()))
		{
			isnotCompanyNameAvalible=true;
		}
		if(companyDao.isAliasRegistred(company.getAlias()))
		{
			badAlias=true;
		}
		company.setDBName(company.getDBName()+".dat");
		if(companyDao.isDBNameRegistred(company.getDBName()))
		{
			isnotDBNameAvalible=true;
		}
		if(!password.equals(repeat))
		{
			isnotPassOk=true;
		}


		badSecurity=true;
		for(String car:caracteresEspeciales)
		{
			if(password.contains(car))
			{
				badSecurity=false;
			}
		}
		//If all data are correct, store them in database
		if(!isnotPassOk && !isnotCompanyNameAvalible && !isnotDBNameAvalible && !isBadChar && !isBadName && !badSecurity && !badAlias)
		{
			userDao=DB4O.getUsuarioDao(company.getDBName());
			company.setId(UUID.generate());
			profile.setId(UUID.generate());
			company.setIdAdmin(profile.getId());
			company.setRegDatetoActual();
			profile.setRegDatetoActual();
			String encrypt=Encryption.getStringMessageDigest(password, Encryption.SHA1);
			profile.setAdmin(true);
			profile.setEmail(company.getAdminEmail());
			profile.setPassword(encrypt);

			profile.setOwner(true);
			company.setFirstName(company.getFirstName());
			company.setLastName(company.getLastName());
			userDao.store(profile);
			companyDao.store(company);
			componentResources.discardPersistentFieldChanges();
			
			return ListCompany.class;

		}
		return null;
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
