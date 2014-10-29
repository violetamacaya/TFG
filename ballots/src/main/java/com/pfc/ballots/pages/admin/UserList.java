package com.pfc.ballots.pages.admin;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.CensusDao;
import com.pfc.ballots.dao.CompanyDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.ProfileCensedInDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.dao.UserLogedDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Census;
import com.pfc.ballots.entities.Company;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.entities.ProfileCensedIn;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
/**
 * 
 * UsersList class is the controller for the UsersList page that
 * provides the administration of the registered users
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 * @author Violeta Macaya Sánchez
 * @version 2.0 OCT-2014
 */
public class UserList {
   ///////////////////////////////////////////////// GENERAL ///////////////////////////////////////////////
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	@Inject
	private Request request;
	
	@SessionState
	private DataSession datasession;
	
	/**
	 *Enum to control the form state
	 */
	private enum Actions{
		SAVE,CANCEL
	};
	
	
	///////////////////////////////////////////////// DAO ///////////////////////////////////////////////////////////
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	@Persist
	UserDao userDao;
	@Persist
	UserLogedDao userLogedDao;
	@Persist
	ProfileCensedInDao censedInDao;
	@Persist
	CensusDao censusDao;
	@Persist
	CompanyDao companyDao;
	
	
	/////////////////////////////////////////// INITIALIZE ///////////////////////////////////////////////////////
	public void setupRender()
	{
		userDao=DB4O.getUsuarioDao(datasession.getDBName());
		userLogedDao=DB4O.getUserLogedDao(datasession.getDBName());
		censedInDao=DB4O.getProfileCensedInDao(datasession.getDBName());
		censusDao=DB4O.getCensusDao(datasession.getDBName());
		companyDao=DB4O.getCompanyDao();
		editing=false;
		
		users=userDao.RetrieveAllProfilesSortLastLog();
		nomails=userDao.getNoMailProfiles();
		if(nomails==null || nomails.size()==0)
		{showNoMail=false;}
		else
		{showNoMail=true;}
		editprof=null;
	}
	//////////////////////////////////////////////////// PAGE ISSUES /////////////////////
	@InjectComponent
	private Zone nomailgrid;
	@InjectComponent
	private Zone usergrid;
	@InjectComponent
	private Zone editZone;
	@InjectComponent
	private Zone detailsZone;
	
	@InjectComponent
	private Form editForm;
	
	@Property
	@Persist
	private List<Profile> users;
	@Property
	private Profile user;
	@Property
	@Persist
	private List<Profile> nomails;
	@Property
	private Profile nomail;
	@Property
	@Persist
	private boolean showNoMail;
	
	
	@Persist
	@Property
	private boolean editing;
	@Persist
	@Property	
	private boolean details;
	@Property
	private boolean nonavalible;
	@Persist
	private boolean normalEdit;
	@Persist
	@Property
	private Profile editprof;
	
	private Actions action;
	
	public boolean isOwner()
	{
		if(user.isOwner())
			return true;
		else
			return false;
	}
	/**
	 *Report that the save button has been pressed
	 */
	public void onSelectedFromSave()
	{
		action=Actions.SAVE;
	}
	
	/**
	 *Report that the cancel button has been pressed
	 */
	public void onSelectedFromCancel()
	{
		action=Actions.CANCEL;
	}
	/**
	 * Checks if the changes of the user are correct and store them
	 */
	void onSuccessFromEditForm()
	{
		boolean success=true;
		if(request.isXHR())
		{
			if(action==Actions.SAVE)
			{	
				user=lookforid(editprof.getId());
				nomail=lookforidnomail(editprof.getId());
				
				
					if(!editprof.equals(user))
					{
						if(!user.getEmail().equals(editprof.getEmail()))
						{
							if(userDao.isProfileRegistred(editprof.getEmail()))
							{
								nonavalible=true;
								success=false;
							}
							else
							{
								if(datasession.isCompanyUser())
								{				 
									Company company=datasession.isCompanyOwner(null);
									if(company!=null)
									{
										company.setAdminEmail(editprof.getEmail());
										companyDao.updateCompany(company);
									}
								}
								List<Census> censusOwner=censusDao.getByOwnerId(editprof.getId());
								censusDao.changeEmailOfCensus(censusOwner, editprof.getEmail());
							}
						}
						if(success)
						{
							userLogedDao.deleteByEmail(user.getEmail());
							userDao.UpdateById(editprof);
							user.copy(editprof);
							if(nomail!=null)
							{
								if(!editprof.getEmail().contains("@nomail"))
								{
									nomails.remove(nomail);
								}
							}
							else
							{
								if(editprof.getEmail().contains("@nomail"))
								{
									nomails.add(user);
								}
							}
							
							editing=false;
						}
						
					}
					else
					{
						editing=false;
					}
				
				
			
			}
			if(action==Actions.CANCEL)
			{
				editing=false;
			}
			
			
			ajaxResponseRenderer.addRender("usergrid",usergrid).addRender("editZone",editZone).addRender("nomailgrid",nomailgrid);
			System.out.println("SUCCESS");
		}
	}
	
	void onFailureFromEditForm()
	{
		
		if(request.isXHR())
		{
			if(action==Actions.CANCEL)
			{
			editing=false;
			ajaxResponseRenderer.addRender("usergrid",usergrid).addRender("editZone",editZone);
			}
			System.out.println("FAILURE");
		}
		editForm.clearErrors();
	}
	
	/**
	 * Show a edition form for the user with the corresponding id
	 * @param id
	 */
	public void onActionFromEditbut(String id)
	{
		if(request.isXHR())
		{
			editing=true;
			normalEdit=true;
			nonavalible=false;
			user=lookforid(id);
			editprof=new Profile(user);
			ajaxResponseRenderer.addRender("usergrid",usergrid).addRender("nomailgrid",nomailgrid).addRender("editZone",editZone);
		}
	}
	
	/**
	 * Show the details for the user with the corresponding id
	 * @param id
	 */
	public void onActionFromDetails(String id){
		
		details=false;
		if(request.isXHR())
		{
			details=true;
			user=lookforid(id);
			ajaxResponseRenderer.addRender("usergrid",usergrid).addRender("nomailgrid",nomailgrid).addRender("detailsZone", detailsZone);
		}
	}
	public void onActionFromLess(){
		
		if(request.isXHR())
		{
			details=false;
			ajaxResponseRenderer.addRender("usergrid",usergrid).addRender("nomailgrid",nomailgrid).addRender("detailsZone", detailsZone);

		}
	}
	
	/**
	 * Show a edition form for the user with the corresponding id
	 * @param id
	 */
	public void onActionFromEditnomail(String id)
	{
		if(request.isXHR())
		{
			editing=true;
			normalEdit=false;
			nonavalible=false;
			user=lookforid(id);
			editprof=new Profile(user);
			ajaxResponseRenderer.addRender("usergrid",usergrid).addRender("nomailgrid",nomailgrid).addRender("editZone",editZone);
		}
	}
	/**
	 * Delete a user and all the unnecessary data
	 * @param idUser
	 */
	public void onActionFromDeleteuser(String idUser)
	{
		if(request.isXHR())
		{
			Profile toDelete=userDao.getProfileById(idUser);			
			ProfileCensedIn censedIn=censedInDao.getProfileCensedIn(idUser);
			
			censusDao.removeUserCountedOfCensus(censedIn.getInCensus(), idUser);
			censedInDao.delete(idUser);
			
			userDao.deleteByEmail(toDelete.getEmail());
			userLogedDao.deleteByEmail(toDelete.getEmail());
			if(toDelete.getEmail().contains("@nomail"))
			{
			nomail=lookfornomail(toDelete.getEmail());
			nomails.remove(nomail);
			}
			user=lookforemail(toDelete.getEmail());
			users.remove(user);
			ajaxResponseRenderer.addRender("nomailgrid",nomailgrid).addRender("usergrid",usergrid);
		}
	}
	/**
	 * Delete a user and all the unnecessary data
	 * @param idUser
	 */
	public void onActionFromDeletenomail(String idUser)
	{
		if(request.isXHR())
		{
			Profile toDelete=userDao.getProfileById(idUser);
			ProfileCensedIn censedIn=censedInDao.getProfileCensedIn(idUser);
			
			censusDao.removeUserCountedOfCensus(censedIn.getInCensus(), idUser);
			censedInDao.delete(idUser);

			userDao.deleteByEmail(toDelete.getEmail());
			userLogedDao.deleteByEmail(toDelete.getEmail());
			nomail=lookfornomail(toDelete.getEmail());
			nomails.remove(nomail);
			user=lookforemail(toDelete.getEmail());
			users.remove(user);
			ajaxResponseRenderer.addRender("nomailgrid",nomailgrid).addRender("usergrid",usergrid);
		}
	}
	private Profile lookforemail(String email)
	{
		for(int i=0;i<users.size();i++)
		{
			if(users.get(i).getEmail().equals(email))
			{
				return users.get(i);
			}
		}
		return null;
		
	}
	private Profile lookfornomail(String email)
	{
		for(int i=0;i<nomails.size();i++)
		{
			if(nomails.get(i).getEmail().equals(email))
			{
				return nomails.get(i);
			}
		}
		return null;
		
	}
	private Profile lookforidnomail(String id)
	{
		for(int i=0;i<nomails.size();i++)
		{
			if(nomails.get(i).getId().equals(id))
			{
				return nomails.get(i);
			}
		}
		
		return null;
	}
	private Profile lookforid(String id)
	{
		for(int i=0;i<users.size();i++)
		{
			if(users.get(i).getId().equals(id))
			{
				return users.get(i);
			}
		}
		
		return null;
	}
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////// ON ACTIVATE /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
				return UnauthorizedAttempt.class;
			case 2:
				return null;
			case 3:
				return SessionExpired.class;
			default:
				return Index.class;
		}
	}
	public boolean isNotMaker()
	{
		if(datasession.isMaker())
		{
			return true;
		}
		return false;
	}

}
