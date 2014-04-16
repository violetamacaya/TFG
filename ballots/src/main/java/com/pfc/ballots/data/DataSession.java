package com.pfc.ballots.data;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import org.apache.tapestry5.services.Request;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserLogedDao;
import com.pfc.ballots.entities.Profile;

public class DataSession {
	
	public static final long SESSION_TIME = 45;
	
	private String DBName;
	private String company;
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
	public void login(String DBName,Profile profile)
	{
		company="main";
		email=profile.getEmail();
		maker=profile.isMaker();
		admin=profile.isAdmin();
		this.DBName=DBName;
		logDate=new Date();
		loged=true;
		lgdDao=DB4O.getUserLogedDao(DBName);
		lgdDao.clearSessions(SESSION_TIME);
	}
	
	public void login(String DBName,Profile profile,String company)
	{
		
		
		this.company=company;
		email=profile.getEmail();
		maker=profile.isMaker();
		admin=profile.isAdmin();
		this.DBName=DBName;
		logDate=new Date();
		loged=true;
		lgdDao=DB4O.getUserLogedDao(DBName);
		lgdDao.clearSessions(SESSION_TIME);
	}
	public void logout()
	{
		if(lgdDao!=null)
			lgdDao.clearSessions(SESSION_TIME);
		
		company=null;
		maker=false;
		admin=false;
		DBName=null;
		logDate=null;
		email=null;
		loged=false;
		lgdDao=null;
		
	}

	public int sessionState()
	{
		/**
		 * return an int with the state of the session
		 * 		0->LogedIn;
		 * 		1->not loged
		 * 		2->Session expired or kicked from server
		 */
		
		
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
	public boolean isLoged() {
		return loged;
	}

	public void setLoged(boolean loged) {
		this.loged = loged;
	}
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

	public boolean isMaker() {
		return maker;
	}

	public void setMaker(boolean maker) {
		this.maker = maker;
	}

	public boolean isAdmin() {
		return admin;
	}
	public boolean isMainAdmin(){
		if(isAdmin() && isMainUser())
			return true;
		return false;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
