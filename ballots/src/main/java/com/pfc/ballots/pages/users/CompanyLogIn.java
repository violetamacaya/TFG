package com.pfc.ballots.pages.users;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.pfc.ballots.dao.CompanyDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.LogDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.dao.UserLogedDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Company;
import com.pfc.ballots.entities.DataLog;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.entities.UserLoged;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.util.Encryption;

public class CompanyLogIn {
	
	
	@SessionState
	private DataSession datasession;
	
	@Property
	private String companyName;
	@Property
	private String email;
	@Property
	private String password;

	
	
	@Inject
	private Request request;
	@InjectComponent
	private Zone logForm;
	
	@Property
	private boolean companyFailure;
	@Property 
	private boolean authenticationFailure;
	@Property
	private boolean fillFields;
	
	//*****************************************    DAO ************************************************//
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	CompanyDao companyDao=DB4O.getCompanyDao();
    LogDao logDao=DB4O.getLogDao();
	UserDao userDao=null;
	UserLogedDao logedDao=null;
	
	private void setBoolFalse()
	{
		fillFields=false;
		companyFailure=false;
		authenticationFailure=false;
	}
	
	Object onSuccess()
	{
		System.out.println("onSuccess");
		if(companyName==null || email==null || password==null)
		{
			setBoolFalse();
			fillFields=true;
			logDao.store(new DataLog(request.getRemoteHost()));
			return request.isXHR() ? logForm.getBody() : null;
		}
		if(companyName.trim().length()==0 || email.trim().length()==0 || password.trim().length()==0)
		{
			setBoolFalse();
			fillFields=true;
			logDao.store(new DataLog(request.getRemoteHost()));
			return request.isXHR() ? logForm.getBody() : null;
		}
		email=email.toLowerCase();
		companyName=companyName.toLowerCase();
		
		String encryptedPass=Encryption.getStringMessageDigest(password, Encryption.SHA1);
		
		//Verify Company and Load DBName
		Company company=companyDao.getCompanyByName(companyName);
		if(company==null)
		{
			setBoolFalse();
			companyFailure=true;
			System.out.println("compañia incorrecto");
			logDao.store(new DataLog(request.getRemoteHost()));
			return request.isXHR() ? logForm.getBody() : null;
		}
		//Verify if user is registred
		userDao=DB4O.getUsuarioDao(company.getDBName());
		Profile profile = userDao.getProfileByEmail(email);
		if(profile==null)
		{
			setBoolFalse();
			authenticationFailure=true;
			System.out.println("password incorrecto");
			logDao.store(new DataLog(request.getRemoteHost()));
			return request.isXHR() ? logForm.getBody() : null;
		}
		
		//Verify password
		if(!profile.getPassword().equals(encryptedPass))
		{
			setBoolFalse();
			authenticationFailure=true;
			System.out.println("password incorrecto");
			System.out.println(profile.getPassword());
			System.out.println(encryptedPass);
			
			logDao.store(new DataLog(email,request.getRemoteHost(),false));
			return request.isXHR() ? logForm.getBody() : null;
		}
		else
		{
			//Login Successful
			setBoolFalse();
			
			logedDao=DB4O.getUserLogedDao(company.getDBName());
			
			logedDao.store(new UserLoged(email,request.getRemoteHost()));
			if(datasession==null)
			{
				datasession=new DataSession();
			}
			
			
			//Update Profile last Successful login
			Profile updatedProfile=new Profile(userDao.getProfileByEmail(email));
			updatedProfile.setLogtoactual();
			userDao.UpdateByEmail(updatedProfile);
			datasession.login(company.getDBName(), updatedProfile);
			//Record successful login in users log
			logDao.store(new DataLog(email,request.getRemoteHost(),true));
			System.out.println("LOGIN CORRECTO");
			return Index.class;
		}
		
	}
	
}
