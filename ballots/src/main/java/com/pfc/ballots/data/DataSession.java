package com.pfc.ballots.data;

import java.util.Date;

public class DataSession {
	
	private String DBName;
	private String email;
	private Date logDate;
	private boolean loged;
	
	public DataSession()
	{
		DBName=null;
		email=null;
		logDate=null;
		loged=false;
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
	public void login(String DBName,String email)
	{
		this.email=email;
		this.DBName=DBName;
		logDate=new Date();
		loged=true;
	}
	public void logout()
	{
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

}
