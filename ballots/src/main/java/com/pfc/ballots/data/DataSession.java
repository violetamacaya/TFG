package com.pfc.ballots.data;

import java.util.Date;

import com.pfc.ballots.entities.Profile;

public class DataSession {
	
	private String DBName;
	private String email;
	private Date logDate;
	private boolean admin;
	private boolean maker;
	private boolean loged;
	
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
		
		email=profile.getEmail();
		maker=profile.isMaker();
		admin=profile.isAdmin();
		this.DBName=DBName;
		logDate=new Date();
		loged=true;
	}
	public void logout()
	{
		maker=false;
		admin=false;
		DBName=null;
		logDate=null;
		email=null;
		loged=false;
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

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
}
