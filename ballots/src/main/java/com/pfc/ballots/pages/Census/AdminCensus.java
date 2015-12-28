package com.pfc.ballots.pages.Census;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
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
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
import com.pfc.ballots.pages.admin.AdminMail;
/**
 * 
 * AdminCensus class is the controller for the AdminCensus page that
 * provides the census administration
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 */


public class AdminCensus {

	
	
	
	@SessionState
	private DataSession datasession;
	
	@Inject
	ComponentResources componentResources;
	
	@InjectPage
	private UsersCounted usersCounted;
	
	@InjectComponent
	private Zone censusGridZone;
	
	@Inject
	private Request request;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
		
	
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
		componentResources.discardPersistentFieldChanges();
		userDao=DB4O.getUsuarioDao(datasession.getDBName());
		censusDao= DB4O.getCensusDao(datasession.getDBName());
		censedInDao=DB4O.getProfileCensedInDao(datasession.getDBName());
		searched=false;
	}
	
	@Property
	private Census census;
	
	@Persist
	private List<Census> censuses;
	
	
	public List<Census> getCensuses()
	{
		if(censuses==null)
		{
			censuses=censusDao.retrieveAll();
		}
		if(searched)
		{
			String id=userDao.getIdByEmail(email);
			if(id==null)
			{
				censuses=null;
			}
			else
			{
				censuses=censusDao.getByOwnerId(id);
			}
		}
		return censuses;
	}
	/**
	 * Return the page where you can see the details of the census
	 * @param idCensus
	 * @return
	 */
	public Object onActionFromDetailsbut(String idCensus)
	{
		usersCounted.setup(idCensus);
		return usersCounted;
	}
	/**
	 * Delete a census
	 * @param idCensus
	 */
	public void onActionFromRemovebut(String idCensus)
	{
		if(request.isXHR())
		{
			for(Census temp:censuses)
			{
				if(temp.getId().equals(idCensus))
				{
					censedInDao.removeIdCensus(temp.getUsersCounted(), temp.getId());
					censuses.remove(temp);
					
				}
			}
			censusDao.deleteById(idCensus);
			ajaxResponseRenderer.addRender("censusGridZone", censusGridZone);
		}
	}
	
	

	  ////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////// SEARCH ZONE //////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////
	
	@Property
	@Persist
	private String email;

	@Persist
	private boolean searched;
	
	public void onValidateFromSearchForm()
	{
		email=email.toLowerCase();
	}
	/**
	 * Set the email for the census search
	 */
	public void onSuccessFromSearchForm()
	{
		System.out.println("Email->"+email);
		if(request.isXHR())
		{
			searched=true;
			ajaxResponseRenderer.addRender("censusGridZone", censusGridZone);
			
		}
	}
	/**
	 * Report to show all censuses
	 */
	/*public void onActionFromShowAll()
	{
		if(request.isXHR())
		{
			censuses=null;
			searched=false;
			ajaxResponseRenderer.addRender("censusGridZone", censusGridZone);
		}
	}*/
	
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
