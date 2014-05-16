package com.pfc.ballots.data;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import org.apache.tapestry5.services.Request;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserLogedDao;
import com.pfc.ballots.entities.Company;
import com.pfc.ballots.entities.Profile;

public class DataSession {
	
	public static final long SESSION_TIME = 45;
	
	private String DBName;
	private String company;
	private String id;
	private String email;
	private Date logDate;
	private boolean admin;
	private boolean maker;
	private boolean loged;
	private FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	private UserLogedDao lgdDao=null;
	
	@Inject
	private Request request;
	
	
	public DataSession()
	{
		logout();
	}
	 ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////// GETTER AND SETTER //////////////////////////////////////////
   ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String getDBName() {
		return DBName;
	}
	
	public void setDBName(String dBName) {
		DBName = dBName;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Date getLogDate()
	{
		return logDate;
	}
	
	public void setLogDateCurrentTime()
	{
		logDate=new Date();
	}
	
	public boolean isLoged() {
		return loged;
	}

	public void setLoged(boolean loged) {
		this.loged = loged;
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public boolean isAdmin() {
		return admin;
	}
	
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public boolean isMaker() {
		return maker;
	}

	public void setMaker(boolean maker) {
		this.maker = maker;
	}

	 //////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////// LOGIN UTILITY /////////////////////////////////////////////
   //////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Normal login
	public void login(Profile profile)	
	{
		company="main";
		id=profile.getId();
		email=profile.getEmail();
		maker=profile.isMaker();
		admin=profile.isAdmin();
		this.DBName=null;//This means that is main database
		logDate=new Date();
		loged=true;
		lgdDao=DB4O.getUserLogedDao(DBName);
		lgdDao.clearSessions(SESSION_TIME);
	}
	//Login for companies
	public void login(Profile profile,Company company)
	{
		this.company=company.getCompanyName();
		id=profile.getId();
		email=profile.getEmail();
		maker=profile.isMaker();
		admin=profile.isAdmin();
		this.DBName=company.getDBName();
		logDate=new Date();
		loged=true;
		lgdDao=DB4O.getUserLogedDao(DBName);
		lgdDao.clearSessions(SESSION_TIME);
	}
	
	
	public void logout()
	{
		if(lgdDao!=null)
			lgdDao.clearSessions(SESSION_TIME);
		id=null;
		company=null;
		maker=false;
		admin=false;
		DBName=null;
		logDate=null;
		email=null;
		loged=false;
		lgdDao=null;
		
	}
	 ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////// SESSION STATE //////////////////////////////////////////////
   ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 
	 * return an int with the state of the session
	 * 		0->LogedIn;
	 * 		1->not loged
	 * 		2->Session expired or kicked from server
	 * if you are loged this method check if your session time had expired
	 */
	
	public int sessionState()
	{	
		if(!isLoged())
			return 1;
		else
		{
			Date actualDate=new Date();
			Calendar calActualDate=Calendar.getInstance();
			Calendar calLogDate=Calendar.getInstance();
			
			calActualDate.setTime(actualDate);
			calLogDate.setTime(logDate);
			long milisActualDate=calActualDate.getTimeInMillis();
			long milisLogDate=calLogDate.getTimeInMillis();
			long diff=milisActualDate-milisLogDate;
			long diffMin=diff/(60*1000);
			if(diffMin>SESSION_TIME)
			{
				lgdDao.delete(email);
				logout();
				request.getSession(true).invalidate();
				return 2;
			}
			else
			{
				if(lgdDao.isLogedIn(email))
				{
					return 0;
				}
				else
				{
					logout();
					request.getSession(true).invalidate();
					return 2;
				}
			}
		}
		
	}
	
	 ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////// SESSION UTILS //////////////////////////////////////////////
   ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean isCompanyUser()
	{
		if(DBName==null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public boolean isMainUser()
	{
		if(DBName==null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public boolean isMainAdmin(){
		if(isAdmin() && isMainUser())
			return true;
		return false;
	}

	
}
