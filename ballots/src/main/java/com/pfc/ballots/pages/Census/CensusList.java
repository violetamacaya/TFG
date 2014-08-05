package com.pfc.ballots.pages.Census;

import java.util.List;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

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
 * CensusList class is the controller for the CensusList page that
 * show the census of the loged user
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 */


public class CensusList {

	@SessionState
	private DataSession datasession;
	
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao userDao=null;
	CensusDao censusDao=null;
	ProfileCensedInDao censedInDao=null;
	
	
	@InjectPage
	private UsersCounted usersCounted;
	
	@Property
	private Census census;
	
	/**
	 * Initialize values
	 */
	public void setupRender()
	{	
		userDao=DB4O.getUsuarioDao(datasession.getDBName());
		censusDao= DB4O.getCensusDao(datasession.getDBName());
		censedInDao=DB4O.getProfileCensedInDao(datasession.getDBName());
	}
	
	public List<Census> getCensuses()
	{
		return censusDao.getByOwnerId(datasession.getId());
	}
	/**
	 * Delete a census
	 * @param idCensus
	 */
	public void onActionFromRemoveBut(String idCensus)
	{
		Census toDelete=censusDao.getById(idCensus);
		censedInDao.removeIdCensus(toDelete.getUsersCounted(), idCensus);
		censusDao.deleteById(idCensus);
	}
	
	/**
	 * Return the page where you can see the details of the census
	 * @param idCensus
	 * @return
	 */
	public Object onActionFromDetailsBut(String idCensus)
	{
		usersCounted.setup(idCensus);
		return usersCounted;
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
				else
				{
					return UnauthorizedAttempt.class;
				}
			case 2:
				return null;
			case 3:
				return SessionExpired.class;
			default:
				return Index.class;
		}
		
	}

}

