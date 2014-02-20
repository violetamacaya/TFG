package com.pfc.ballots.entities;

import java.util.Date;

public class DataLog {
	
	private String email;
	private String IP;
	private Date date;
	private boolean success;
	
	public DataLog(String email,String IP,Date date,boolean success)
	{
		this.IP=IP;
		this.setDate(date);
		this.email=email;
		this.success=success;
	}
	public DataLog(String email,String IP,boolean success)
	{
		this.IP=IP;
		setCurrentTime();
		this.email=email;
		this.success=success;
	}
	public DataLog(String IP)
	{
		this.IP=IP;
		setCurrentTime();
		this.email="Annon";
		this.success=false;
	}
	
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}

	public void setCurrentTime()
	{
		setDate(new Date());
		
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
