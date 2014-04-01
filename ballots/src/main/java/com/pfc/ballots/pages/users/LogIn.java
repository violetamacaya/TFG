package com.pfc.ballots.pages.users;



import java.util.Date;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.LogDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.dao.UserLogedDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.DataLog;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.entities.UserLoged;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.util.Encryption;





public class LogIn {

	@SessionState
	private DataSession datasession;
	
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
	FactoryDao DB4O = FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
    UserDao userDao = DB4O.getUsuarioDao(datasession.getDBName());
    LogDao logDao=DB4O.getLogDao();
    UserLogedDao logedDao=DB4O.getUserLogedDao();
	
	Object onSuccess()
	{
		
		//Check if the text fields are empties
		if(email==null || password==null)
		{
			authenticationFailure=true;
			//Record login attempt
			logDao.store(new DataLog(request.getRemoteHost()));
			return request.isXHR() ? logForm.getBody() : null;
		}
		if(email.trim().length()==0 || password.trim().length()==0)
		{
			authenticationFailure=true;
			//Record login attempt
			logDao.store(new DataLog(request.getRemoteHost()));
			return request.isXHR() ? logForm.getBody() : null;
		}
		
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
			logedDao.store(new UserLoged(email,request.getRemoteHost()));
			if(datasession==null)
			{
				datasession=new DataSession();
			}
			datasession.login(null, email);
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
