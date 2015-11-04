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
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.CensusDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.ProfileCensedInDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Census;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
import com.pfc.ballots.pages.admin.AdminMail;
import com.pfc.ballots.util.UUID;
/**
 * 
 * CreateCensus class is the controller for the CreateCensus page that
 * allow create a new census
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 */

public class CreateCensus {
	  ////////////////////////////////////////////////////////////////////////////////////////////////
	 ///////////////////////////////////////// GENERAL STUFF/////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Variables
	@Inject
	ComponentResources componentResources;
	@Inject
	private Request request;
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@SessionState
	private DataSession datasession;
	
	
	//////////////////////////////////////////////DAO ////////////////////////////////////////////
		
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	@Persist
	UserDao userDao;
	@Persist
	CensusDao censusDao;
	@Persist
	ProfileCensedInDao censedInDao;
		
	/**
	 * Initialize data
	 */
	public void setupRender()
	{
		userDao=DB4O.getUsuarioDao(datasession.getDBName());
		censusDao=DB4O.getCensusDao(datasession.getDBName());
		censedInDao=DB4O.getProfileCensedInDao(datasession.getDBName());
		
	}

	
	      ////////////////////////////////////////////////////////////////////////////////////////////////
		 ///////////////////////////////// CENSUS NAME FORM AND ZONE ////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
	
	//Variables
	@Property
	@Persist
	private boolean censusNameVisible;
	
	@InjectComponent
	private Zone censusNameZone;
	@Property
	@Persist
	private String censusName;
	@Property
	@Persist
	private boolean nameNotAvalible;
	
	/**
	 * Checks if censusName is available and shows the 
	 * next step of the wizard
	 */
	public void onSuccessFromCensusNameForm()
	{		
		if(censusDao.isNameInUse(censusName, datasession.getId()))
		{
			nameNotAvalible=true;
			if(request.isXHR())
				ajaxResponseRenderer.addRender("censusNameZone", censusNameZone);
		}
		else
		{
			nameNotAvalible=false;
			censusNameVisible=true;
			secondVisible=true;
			if(profile==null)
				profile=new Profile();
			
			if(request.isXHR())
			{
				ajaxResponseRenderer.addRender("censusNameZone",censusNameZone).addRender("searchTypeZone", searchTypeZone)
					.addRender("basicSearchZone", basicSearchZone).addRender("advancedSearchZone", advancedSearchZone)
					.addRender("searchListZone", searchListZone).addRender("censusListZone", censusListZone)
					.addRender("buttonsZone",buttonsZone);
			}
		}
	}
	
	      ////////////////////////////////////////////////////////////////////////////////////////////////
		 ////////////////////// SECOND PART OF WIZARD AND SWITCH SAERCH TYPES  //////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
	
	
	@InjectComponent
	private Zone searchTypeZone;
	
	@Property
	@Persist
	private boolean advancedSearch;
	
	@Property
	@Persist 
	private boolean secondVisible;
	
	@Property
	@Persist
	private Profile example;
	
	/**
	 * Change the search type
	 */
	public void onSwitchSearch()
	{
		advancedSearch= (advancedSearch) ? false:true;
		
		
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender("searchTypeZone", searchTypeZone).addRender("basicSearchZone", basicSearchZone)
				.addRender("advancedSearchZone", advancedSearchZone);
		}
	}
	/**
	 * Resets the search values
	 */
	public void onReset()
	{
		email=null;
		profile=new Profile();
		firstName=null;
		lastName=null;
		
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender("searchTypeZone", searchTypeZone).addRender("basicSearchZone", basicSearchZone)
				.addRender("advancedSearchZone", advancedSearchZone);
		}
	}
	    
	  ////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////// BASIC SEARCH /////////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////
	
	@InjectComponent
	private Zone basicSearchZone;
	
	@Property
	@Persist
	private String email;
	/**
	 * Check if the basic search values are correct
	 */
	public void onValidateFromBasicForm()
	{
		example=null;
		if(email!=null)
		{
			if(email.trim().length()==0)
			{
				email=null;
				
			}
			else
			{
				example=new Profile();
				example.setEmail(email);
			}
		}
	}
	/**
	 * Allow the basic search
	 */
	public void onSuccessFromBasicForm()
	{
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender("searchListZone", searchListZone);
		}
	}
	
	
	
	  ////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////// ADVANCED SEARCH //////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////
	
	@InjectComponent
	private Zone advancedSearchZone;
	
	@Persist
	@Property
	private String firstName;
	
	@Persist
	@Property
	private String lastName;
	
	@Property
	@Persist
	private Profile profile;
	
	/**
	 * Checks if the advanced search data are correct
	 */
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
	/**
	 * Allow the advanced search
	 */
	public void onSuccessFromAdvancedSearchForm()
	{
		example=new Profile(profile);
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender("searchListZone", searchListZone);
		}
	}
	
	
	  ////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////// SEARCH LIST GRID /////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////
	
	@InjectComponent
	private Zone searchListZone;
	
	@Property
	@Persist
	private Profile user;
	

	@Persist
	private List<Profile> searchList;
	
	public boolean isShowSearchList()
	{
		return (example==null)? false:true;
	}
	/**
	 * Show all the census
	 */
	public void onActionFromShowAll()
	{
		example=new Profile();
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender("searchListZone",searchListZone);
		}
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
	   
	
	  ////////////////////////////////////////////////////////////////////////////////////
	 //////////////////////////////// ADD FORM STUFF //////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////
	
	@Persist
	private Map<String,Profile> map;
	/**
	 * Allow to add the users selected
	 */
	public void onSuccessFromAddForm()
	{
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender("censusListZone", censusListZone).addRender("buttonsZone",buttonsZone);
		}
	}
	/**
	 * add the selected user
	 * @param add
	 */
	public void setAdd(boolean add)
	{
				
		if(add)
		{
			if(map==null)
			{
				map=new HashMap<String,Profile>();
				censusList=new LinkedList<Profile>();
			}
			
			if(map.get(user.getId())==null)
			{
				map.put(user.getId(),user);
				censusList.add(user);
				System.out.println("ADED");
			}
		}
		else
		{
			if(map!=null)
			{
				Profile temp=map.get(user.getId());
				if(temp!=null)
				{
					map.remove(user.getId());
					censusList.remove(temp);
					if(map.size()==0)
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
		if(map!=null)
		{
			if(map.get(user.getId())==null)
				return false;
			else
				return true;
		}
		return false;
	}
	/**
	 * add all the users
	 */
	public void onActionFromAddAll()
	{
		if(request.isXHR())
		{
			if(map==null)
			{
				map=new HashMap<String,Profile>();
				censusList=new LinkedList<Profile>();
			}
			for(Profile temp:searchList)
			{
				if(map.get(temp.getId())==null)
				{
					map.put(temp.getId(), temp);
					censusList.add(temp);
				}
			}
			
			ajaxResponseRenderer.addRender("searchListZone", searchListZone).addRender("censusListZone", censusListZone)
				.addRender("buttonsZone",buttonsZone);
		}
		
	}
	
	  ////////////////////////////////////////////////////////////////////////////////////
	 //////////////////////////////// CENSUS LIST GRID ////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////
	
	@InjectComponent
	private Zone censusListZone;
	
	@Persist
	@Property
	private List<Profile> censusList;
	
	
	public boolean isShowCensusList()
	{
		if(censusList==null)
			return false;
		else
			return true;
	}
	  ////////////////////////////////////////////////////////////////////////////////////
	 //////////////////////////////// REMOVE FORM STUFF ///////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Allow to delete  users of the census
	 */
	public void onSuccessFromRemoveForm()
	{
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender("searchListZone", searchListZone).addRender("censusListZone", censusListZone)
				.addRender("buttonsZone",buttonsZone);
		}
	}
	
	/**
	 * Delete a user of the census
	 * @param remove
	 */
	public void setRemove(boolean remove) 
	{
		if(remove)
		{
			Profile temp=map.get(user.getId());
			map.remove(user.getId());
			censusList.remove(temp);
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
	/**
	 * Delete all the users of the census
	 */
	public void onActionFromRemoveAll()
	{
		if(request.isXHR())
		{
			map=null;
			censusList=null;
			ajaxResponseRenderer.addRender("searchListZone", searchListZone).addRender("censusListZone", censusListZone)
				.addRender("buttonsZone",buttonsZone);
		}		
	}
	  ////////////////////////////////////////////////////////////////////////////////////
	 //////////////////////////// BACK AND NEXT BUTTONS ///////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////
	
	@InjectComponent
	private Zone buttonsZone;
	
	/**
	 * shows the first part of the wizard
	 */
	public void onActionFromBackbut()
	{
		censusNameVisible=false;
		secondVisible=false;
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender("censusNameZone",censusNameZone).addRender("searchTypeZone", searchTypeZone)
				.addRender("basicSearchZone", basicSearchZone).addRender("advancedSearchZone", advancedSearchZone)
				.addRender("searchListZone", searchListZone).addRender("censusListZone", censusListZone)
				.addRender("buttonsZone",buttonsZone);
		}
	}
	/**
	 *Stores the census
	 */
	public Object onActionFromEndbut()
	{
		Census census=new Census();
		
		census.setCensusName(censusName);
		census.setId(UUID.generate());
		census.setIdOwner(datasession.getId());
		census.setEmail(datasession.getEmail());
		for(Profile temp:censusList)
		{
			census.addIdToUsersCounted(temp.getId());
		}
		censedInDao.addIdCensus(census.getUsersCounted(), census.getId());
		censusDao.store(census);
		componentResources.discardPersistentFieldChanges();

		return CensusList.class;
	}
	
	
	  ////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////// ON ACTIVATE //////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////
	/**
	* Controls if the user can enter in the page
	* @return another page if the user can't enter
	*/
	public Object onActivate()
	{
		switch(datasession.sessionState())
		{
			case 0:
				return Index.class;
			case 1:
				if(datasession.isMaker())
				{
					return null;
				}
				return UnauthorizedAttempt.class;
			case 2:
				return null;
			case 3:
				return SessionExpired.class;
			default:
				return Index.class;
		}
		
	}

}
