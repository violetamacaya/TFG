package com.pfc.ballots.pages.profile;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;

import com.pfc.ballots.entities.Profile;

public class CreateProfile {

	@Inject
    private ComponentResources componentResources;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private Profile profile;
	
	@Validate("required")
	@Property
	private String password;
	@Validate("required")
	@Property
	private String repeat;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean isPassOk;
	

	void onSuccess(){
		
		if(password.equals(repeat))
		{
			isPassOk=false;
			componentResources.discardPersistentFieldChanges();
			
		}
		else
		{
			isPassOk=true;
		}
		
	}
}
