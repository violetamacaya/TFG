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
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.dao.UserLogedDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.DataLog;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.entities.UserLoged;

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
	private DataLog log;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	
	void setupRender()
	{
		userLogedDao=DB4O.getUserLogedDao(datasession.getDBName());		
	}
	
	public List<DataLog> getLogs()
	{
		return logDao.retrieve();
	}
	
	public List<UserLoged> getUsersLoged()
	{
		return userLogedDao.retrieveAll();
	}
	
	public void onActionFromKickbut(String email)
	{
		if(request.isXHR())
		{
			userLogedDao.delete(email);
			ajaxResponseRenderer.addRender(logedZone);
		}
		
	}
}
