package com.pfc.ballots.pages.Census;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.entities.Profile;

public class CreateCensus {

	@Persist
	@Property
	private String email; 
	
	@Persist
	@Property
	private String firstName;
	@Persist
	@Property
	private String lastName;

	
	@Property
	@Persist
	private Profile profile;
	
	@Persist
	private Profile example;
	
	@Persist
	@Property
	private boolean advancedSearch;
	
	@Inject
	ComponentResources componentResources;
	
	
	@InjectComponent
	private Zone searchListZone;
	@InjectComponent
	private Zone searchTypeZone;
	@InjectComponent
	private Zone advancedSearchZone;
	@InjectComponent
	private Zone basicSearchZone;
	
	             
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	

	
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao userDao=DB4O.getUsuarioDao();
	
	public CreateCensus()
	{
	
	}
	
	public List<Profile> getUsers()
	{
		if(example==null)
		{
			System.out.println("return null");
			return null;
		}
		else
		{
			System.out.println("BY Example");
			return userDao.getByExample(example);
		}
	}

	public void onTypeSearch()
	{	
		reset();
		if(advancedSearch)
			advancedSearch=false;
		else
			advancedSearch=true;

		ajaxResponseRenderer.addRender("searchTypeZone",searchTypeZone).addRender("basicSearchZone",basicSearchZone).addRender("advancedSearchZone", advancedSearchZone);
	}
	
	public void onActionFromShowAll()
	{
		example=new Profile();
		ajaxResponseRenderer.addRender("searchListZone",searchListZone);
	}
	
	public void onReset()
	{
		reset();
		ajaxResponseRenderer.addRender("basicSearchZone",basicSearchZone).addRender("advancedSearchZone", advancedSearchZone);
		
		System.out.println("Reset");
	}
	private void reset()
	{
		profile=new Profile();
		example=null;
		email=new String();
		firstName=new String();
		lastName=new String();
	}
	public void onSuccessFromBasicSearchForm()
	{
		if(email!=null)
		{
			if(!(email.trim().length()==0))
			{
				example=new Profile();
				example.setEmail(email);
			}
			else 
				example=null;
		}
		else
			example=null;
		ajaxResponseRenderer.addRender("searchListZone",searchListZone);
	}
	
	public void onValidateFromAdvancedSearchForm()
	{
		if(firstName!=null)
		{
			if(firstName.trim().length()==0)
				firstName=null;
		}
		profile.setFirstName(firstName);
		if(lastName!=null)
		{
			if(lastName.trim().length()==0)
				lastName=null;
		}
		profile.setLastName(lastName);
		if(profile.getUniversity()!=null)
		{
			if(profile.getUniversity().trim().length()==0)
				profile.setUniversity(null);
		}
		if(profile.getCity()!=null)
		{
			if(profile.getCity().trim().length()==0)
				profile.setCity(null);
		}
		if(profile.getCountry()!=null)
		{
			if(profile.getCountry().trim().length()==0)
				profile.setCountry(null);
		}
	}
	public void onSuccessFromAdvancedSearchForm()
	{		
		example=new Profile(profile);
		ajaxResponseRenderer.addRender("searchListZone",searchListZone);
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();
		reset();
		
	}
	public void cleanupRender()
	{
		componentResources.discardPersistentFieldChanges();
		reset();
	}


}
