package com.pfc.ballots.pages.ballot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.BallotDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.dao.VoteDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.entities.Vote;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;

/**
 *  AddUsers class is the controller for the AddUsers page that
 * 	allow to add new voters to a ballot
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUL-2014
 */
public class AddUsers {

	@SessionState
	private DataSession datasession;
	
	@Inject
	ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	
	@Persist
	private boolean notDone;
	
	@Persist
	private String idBallot;
	@Persist
	Ballot ballot;
	
	@Persist
	Object page;
	
	////////////////////////////////////////////////////DAO  ////////////////////////////////////////////////
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	@Persist
	VoteDao voteDao;
	@Persist
	UserDao userDao;
	@Persist
	BallotDao ballotDao;
	
	
	/**
	 * Initializes the page
	 * @param idBallot
	 */
	public void setup(String idBallot,Object page)
	{
		notDone=true;
		this.idBallot=idBallot;
		this.page=page;
	}
	
	public void setupRender()
	{
		map=null;
		example=null;
		votersList=null;
		profile=new Profile();
		searchList=null;
		userDao=DB4O.getUsuarioDao(datasession.getDBName());
		voteDao=DB4O.getVoteDao(datasession.getDBName());
	}
	
	
	
    	  //////////////////////////////////////////////////////////////////////////////////////////////
		 //////////////////////////////////////// SWITCH SEARCH TYPE///////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////
		
	
	@InjectComponent
	private Zone searchTypeZone;
	
	@Property
	@Persist
	private boolean advancedSearch;
	
	@Property
	@Persist
	private Profile example;
	
	/**
	 * Switch the search mode
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
		
		  //////////////////////////////////////////////////////////////////////////////////////////////
		 /////////////////////////////////////////// BASIC SEARCH   ///////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
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
	
		
		  //////////////////////////////////////////////////////////////////////////////////////////////
		 //////////////////////////////////////// ADVANCED SEARCH /////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////
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
	 * @return the users of the search
	 */
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
	
	/**
	 * Show all users
	 */
	public void onActionFromShowAll()
	{
		example=new Profile();
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender("searchListZone",searchListZone);
		}
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
				ajaxResponseRenderer.addRender("votersZone", votersZone).addRender("buttonsZone",buttonsZone);
			}
		}
		
		public boolean isShowAdd()
		{
			if(voteDao.getVoteByIds(idBallot, user.getId())==null)
			{
				return true;
			}
			return false;
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
					votersList=new LinkedList<Profile>();
				}
				
				if(map.get(user.getId())==null)
				{
					map.put(user.getId(),user);
					votersList.add(user);
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
						votersList.remove(temp);
						if(map.size()==0)
						{
							map=null;
							votersList=null;
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
		 * Add all the users of the list that aren't in the ballot
		 */
		public void onActionFromAddAll()
		{
			if(request.isXHR())
			{
				if(map==null)
				{
					map=new HashMap<String,Profile>();
					votersList=new LinkedList<Profile>();
				}
				for(Profile current:searchList)
				{
					if(voteDao.getVoteByIds(idBallot, current.getId())==null)
					{
						if(!map.containsKey(current.getId()))
						{
							map.put(current.getId(), current);
							votersList.add(current);
						}
					}
				}
				ajaxResponseRenderer.addRender("votersZone", votersZone).addRender("searchListZone",searchListZone).addRender("buttonsZone",buttonsZone);
			}
		}
		
	  ///////////////////////////////////////////////////////////////////////////////////////////////
	 //////////////////////////////////////// VOTERS GRID //////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	@InjectComponent
	private Zone votersZone;
		
	@Persist
	@Property
	List<Profile> votersList;
	
	public boolean isShowVoters()
	{
		if(votersList==null)
		{
			return false;
		}
		return true;
	}
	
	
	/**
	 * Delete a user of the votersList
	 * @param remove
	 */
	public void setRemove(boolean remove) 
	{
		if(remove)
		{
			Profile temp=map.get(user.getId());
			map.remove(user.getId());
			votersList.remove(temp);
			if(map.size()==0)
			{
				map=null;
				votersList=null;
			}
		}
	}
	public boolean isRemove()
	{
		return false;
	}
	
	
	/**
	 * Allows to delete users in the list to add
	 */
	public void onSuccessFromRemoveForm()
	{
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender("searchListZone", searchListZone).addRender("votersZone", votersZone).addRender("buttonsZone",buttonsZone);
		}
	}
	/**
	 * Deletes all the users from the list to add
	 */
	public void onActionFromRemoveAll()
	{
		if(request.isXHR())
		{
			votersList=null;
			map=null;
			ajaxResponseRenderer.addRender("searchListZone", searchListZone).addRender("votersZone", votersZone).addRender("buttonsZone",buttonsZone);
		}
	}
	  ///////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////// BUTTONS ////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	@InjectComponent
	private Zone buttonsZone;
	
	/**
	 * Cancels the addition of users
	 * @return the page where the users came
	 */
	public Object onActionFromCancelBut()
	{
		return page;
	}
	public Object onActionFromEndBut()
	{
		List<Vote> voteList=new LinkedList<Vote>();
		for(Profile current:votersList)
		{
			Vote vote=new Vote(idBallot,current.getId());
			voteList.add(vote);
		}
		voteDao.store(voteList);
		return page;
	}
	

	  ///////////////////////////////////////////////////////////////////////////////////////////////
	 ///////////////////////////////////////// ACTIVATE ////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////
	/**
	* Controls if the user can enter in the page
	* @return another page if the user can't enter
	*/
	public Object onActivate()
	{
		if(!notDone)
		{
			return Index.class;
		}
		switch(datasession.sessionState())
		{
			case 0:
				return Index.class;
			case 1:
				if(datasession.isMaker() && idBallot!=null)
				{
					return null;
				}
				return UnauthorizedAttempt.class;
			case 2:
				if(idBallot==null)
				{
					return Index.class;
				}
				return null;
			case 3:
				return SessionExpired.class;
			default:
				return Index.class;
		}
		
	}
	
	
	
	
	
	
}
