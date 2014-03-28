package com.pfc.ballots.entities;

import java.util.Date;

public class UserLoged {

	private String email;
	private String IP;
	private Date date;
	
	public UserLoged()
	{
		
	}
	public UserLoged(String email)
	{
		this.email=email;
	}
	public UserLoged(String email,String IP)
	{
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
}
