package com.pfc.ballots.pages.admin;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.LogDao;
import com.pfc.ballots.dao.UserLogedDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.DataLog;
import com.pfc.ballots.entities.UserLoged;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;

public class LogList {

	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	LogDao logDao =DB4O.getLogDao();
	@Persist
	UserLogedDao userLogedDao;
	@InjectComponent
	private Zone logedZone;
	
	@SessionState
	private DataSession datasession;
	
	@Property
	private UserLoged userLoged;
	
	@Property
	@Persist
	private boolean showAll;
	
	@Property
	private DataLog log;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	
	void setupRender()
	{
		
		userLogedDao=DB4O.getUserLogedDao(datasession.getDBName());
		userLogedDao.clearSessions(DataSession.SESSION_TIME);
	}

	public List<DataLog> getLogs()
	{
		if(showAll)
		{
			return logDao.retrieve();
		}
		else
		{
			return logDao.retrieve(datasession.getCompany());
		}
	}
	
	public List<UserLoged> getUsersLoged()
	{
		return userLogedDao.retrieveAll();
	}
	
	public void onActionFromKickbut(String idSession)
	{
		if(request.isXHR())
		{
			userLogedDao.delete(idSession);
			ajaxResponseRenderer.addRender(logedZone);
		}
		
	}
	public boolean isMain()
	{
		if(datasession.isMainUser())
			return true;
		return false;
	}
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////// ON ACTIVATE /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 *  * return an int with the state of the session
	 * 		0->UserLogedIn;
	 * 		1->AdminLoged
	 * 		2->MainAdminLoged no email of the apliction configured
	 * 		3->not loged
	 * 		4->Session expired or kicked from server
	 */
	public Object onActivate()
	{
		switch(datasession.sessionState())
		{
			case 0:
				return UnauthorizedAttempt.class;
			case 1:
				return null;
			case 2:
				return AdminMail.class;
			case 3:
				return Index.class;
			case 4:
				return SessionExpired.class;
			default:
				return Index.class;
		}
		
	}
}
