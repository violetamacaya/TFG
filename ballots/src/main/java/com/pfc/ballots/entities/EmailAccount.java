package com.pfc.ballots.entities;

import org.apache.tapestry5.beaneditor.Validate;

/**
 * This email account is only for  account of the web-site
 * it isn't for users email!!
 * @author Mario
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
