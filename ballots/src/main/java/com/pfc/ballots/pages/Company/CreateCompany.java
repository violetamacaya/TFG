package com.pfc.ballots.pages.Company;

import org.apache.tapestry5.annotations.Property;

import com.pfc.ballots.entities.Company;
import com.pfc.ballots.entities.Profile;

public class CreateCompany {
	
	@Property
	private Company company;
	@Property
	private Profile profile;
	
	@Property
	private String password;
	@Property
	private String repeat;

	
	@Property 
	private boolean isnotPassOk;
	@Property
	private boolean isnotCompanyNameAvalible;
	@Property
	private boolean isnotDBNameAvalible;
	

	public CreateCompany()
	{
		profile=new Profile();
	}
	
	
}
