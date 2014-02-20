package com.pfc.ballots.pages.users;



import java.util.Date;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.LogDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.entities.DataLog;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.util.Encryption;





public class LogIn {


	@Property
	private String email;
	
	@Property 
	private String password;
	
	@Property
	private boolean authenticationFailure;

	@Inject
	private Request request;
	
	@InjectComponent
	private Zone logForm;
	
	/******************************************* Initialize DAO *******************************************************/
	FactoryDao DB4ODao = FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
    UserDao userDao = DB4ODao.getUsuarioDao();
    LogDao logDao=DB4ODao.getLogDao();
	
	Object onSuccess()
	{
		
		//Check if the text fields are empties
		if(email==null || password==null)
			{authenticationFailure=true;
			//Record login attempt
			logDao.store(new DataLog(request.getRemoteHost()));
			return request.isXHR() ? logForm.getBody() : null;}
		if(email.trim().length()==0 || password.trim().length()==0)
			{authenticationFailure=true;
			//Record login attempt
			logDao.store(new DataLog(request.getRemoteHost()));
			return request.isXHR() ? logForm.getBody() : null;}
		email=email.toLowerCase();
		//Check if Email is in DB and if the password is correct
		
		Profile temp=userDao.getProfileByEmail(email.toLowerCase());
		if(temp==null)
		{
			authenticationFailure=true;
			//Record login attempt
			logDao.store(new DataLog(request.getRemoteHost()));
			return request.isXHR() ? logForm.getBody() : null;
		}
		String encryptedPass=Encryption.getStringMessageDigest(password, Encryption.SHA1);
		if(encryptedPass.equals(temp.getPassword()))
		{
			//Update Profile last Successful login
			Profile updatedProfile=new Profile(userDao.getProfileByEmail(email));
			updatedProfile.setLogtoactual();
			userDao.UpdateByEmail(updatedProfile);
			//Record successful login in users log
			logDao.store(new DataLog(email,request.getRemoteHost(),true));
			System.out.println("LOGIN CORRECTO");
			return Index.class;
		}
		else
		{
			//Record login attempt
			logDao.store(new DataLog(email,request.getRemoteHost(),false));
			System.out.println("Login Incorrecto");
			return request.isXHR() ? logForm.getBody() : null;
		}
	}
	
}
