package com.pfc.ballots.pages.Census;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	
	@Property
	private Profile user;
	@Property
	private Profile prof;
		
	@Persist
	private List<Profile> searchList;
	
	@Persist
	private List<Profile> censusList;
	
	@Persist
	private Map<String,Boolean> map;
	
	
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
	@InjectComponent
	private Zone censusListZone;
	             
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	

	
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao userDao=DB4O.getUsuarioDao();
	
	public CreateCensus()
	{
	
	}
	
	public List<Profile> getCensusList()
	{
		if(censusList!=null)
		{
			for(Profile temp:censusList)
			{
				System.out.println("Nombre->"+temp.getFirstName());
			}
		}
		return censusList;
	}
	public List<Profile> getUsers()
	{
		if(example==null)
		{
			System.out.println("return null");
			searchList=null;
			
		}
		else
		{
			System.out.println("BY Example");
			searchList= userDao.getByExample(example);
		}
		return searchList;
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
	
	public boolean isShowGrid()
	{
		return (example!=null) ? true:false; 
	}
	public boolean isShowCensus()
	{
		return (censusList!=null)? true:false;
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
	
	public void onSuccessFromAddForm()
	{
		ajaxResponseRenderer.addRender("searchListZone",searchListZone).addRender("censusListZone",censusListZone);
	}
	public void onSuccessFromRemoveForm()
	{
		ajaxResponseRenderer.addRender("searchListZone",searchListZone).addRender("censusListZone",censusListZone);
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
	
	public void setAdd(boolean add)
	{
		if(add)
		{
			if(map==null || censusList==null)
			{
				map=new HashMap<String,Boolean>();
				censusList=new LinkedList<Profile>();
				
			}
			if(map.get(user.getId())==null)
			{
				map.put(user.getId(), new Boolean(true));
				censusList.add(user);
			}
		
		}
		else
		{
			if(map!=null)
			{
				if(map.get(user.getId())!=null)
				{
					for(Profile temp:censusList)
					{
						if(temp.getId().equals(user.getId()))
						{
							censusList.remove(temp);
							map.remove(user.getId());
							
						}
					}
					if(censusList.size()==0)
					{
						map=null;
						censusList=null;
					}
				}
			}
				
		}
		
	}
	
	public boolean isAdd()
	{		
		if(map==null)
		 return false;
		if(map.get(user.getId())==null)
			return false;
		else
			return true;
	}

	public void setRemove(boolean remove)
	{
		if(remove)
		{
			map.remove(user.getId());
			if(censusList!=null)
			{
				for(int i=0;i<censusList.size();i++)
				{
					if(censusList.get(i).getId().equals(user.getId()))
						{censusList.remove(i);}
				}
			}
			
			
			if(map.size()==0)
			{
				map=null;
				censusList=null;
			}			
		}
	}
	public boolean isRemove()
	{
		return false;
	}
}
