package com.pfc.ballots.entities;

import java.util.Date;

public class DataLog {
	
	private String email;
	private String IP;
	private Date date;
	private String accessTo;
	private boolean success;
	
	public DataLog(String email,String IP,Date date,boolean success)
	{
		this.IP=IP;
		this.setDate(date);
		this.email=email;
		this.success=success;
		accessTo="main";
	}
	public DataLog(String email,String IP,Date date,boolean success,String company)
	{
		this.IP=IP;
		this.setDate(date);
		this.email=email;
		this.success=success;
		accessTo=company;
	}
	public DataLog(String email,String IP,boolean success)
	{
		this.IP=IP;
		setCurrentTime();
		this.email=email;
		this.success=success;
		accessTo="main";
	}
	public DataLog(String email,String IP,boolean success,String company)
	{
		this.IP=IP;
		setCurrentTime();
		this.email=email;
		this.success=success;
		accessTo=company;
	}
	public DataLog(String IP)
	{
		this.IP=IP;
		setCurrentTime();
		this.email="Annon";
		this.success=false;
		accessTo="main";
	}
	public DataLog(String IP,String company)
	{
		this.IP=IP;
		setCurrentTime();
		this.email="Annon";
		this.success=false;
		accessTo=company;
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
	public String getAccessTo() {
		return accessTo;
	}
	public void setAccessTo(String accessTo) {
		this.accessTo = accessTo;
	}

}
