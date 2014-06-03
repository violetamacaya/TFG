package com.pfc.ballots.entities;

import java.util.Date;

public class UserLoged {

	private String email;
	private String IP;
	private String idSession;
	private Date date;
	
	public UserLoged()
	{
		
	}
	public UserLoged(String idSession)
	{
		this.idSession=idSession;
	}

	public UserLoged(String email,String IP,String idSession)
	{
		this.idSession=idSession;
		this.email=email;
		this.IP=IP;
		date= new Date();
	}
	
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String IP) {
		this.IP = IP;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getIdSession() {
		return idSession;
	}
	public void setIdSession(String idSession) {
		this.idSession = idSession;
	}
}
