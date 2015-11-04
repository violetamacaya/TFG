package com.pfc.ballots.entities;

import org.apache.tapestry5.beaneditor.Validate;

/**
 * EmailAccount entity that contains the information of the email account
 * of the web-site
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUN-2014
 *
 */
public class EmailAccount {
	@Validate("required,regexp=^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	private String email;
	
	private String password;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString()
	{
		return "Email->"+email+" Password->"+password;
	}
	
}
