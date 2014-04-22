package com.pfc.ballots.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;

public class Company {

	
	
	@NonVisual
	private String Id;
	
	@Validate("required")
	private String CompanyName;
	@Validate("required,minLength=6")
	private String DBName;
	@Validate("required,regexp=^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	private String AdminEmail;
	private Date RegDate;
	
	
	public Company(){
		
	}
	public Company(String CompanyName)
	{
		this.CompanyName=CompanyName;
	}
	
	public void setId(String Id)
	{
		this.Id=Id;
	}
	
	public String getId()
	{
		return this.Id;
	}
	public void setCompanyName(String CompanyName)
	{
		this.CompanyName=CompanyName;
	}
	public String getCompanyName()
	{
		return this.CompanyName;
	}
	public void setDBName(String DBName)
	{
		this.DBName=DBName;
	}
	public String getDBName()
	{
		return this.DBName;
	}
	public void setAdminEmail(String ContactEmail)
	{
		this.AdminEmail=ContactEmail;
	}
	public String getAdminEmail()
	{
		return AdminEmail;
	}
	public Date getRegDate()
	{
		return RegDate;
	}
	public void setRegDatetoActual()
	{
		RegDate=new Date();
	}
}
