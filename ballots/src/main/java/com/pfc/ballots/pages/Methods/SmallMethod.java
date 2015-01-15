package com.pfc.ballots.pages.Methods;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.SmallText;
import com.pfc.ballots.dao.*;

/**
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public class SmallMethod {

	@Inject
	private Messages messages;
	@SessionState
	private DataSession datasession;


	@InjectComponent
	private Zone editZone;
	@InjectComponent
	private Zone methodZone;
	
	@Persist
	@Property
	private boolean showText;

	
	@Persist 
	SmallDaoDB4O smallDao;
	
	@Persist
	@Property
	private String smallText;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public SmallMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		smallDao = new SmallDaoDB4O(null);
		smallText= DB4O.getSmallTextDao().getSmallText().getSmallText();	
	}
	
	public boolean isTeacher()
	{
		return datasession.isTeacher();
	}
	public boolean isAdmin()
	{
		return datasession.isAdmin();
	}
	public boolean isAdminOrTeacher()
	{
		return datasession.isAdmin() || datasession.isTeacher();
	}

	
	public Object onSuccessFromEditForm()
	{
		if(request.isXHR())
		{
			SmallText temp = new SmallText();
			temp.setSmallText(request.getParameter("smallText"));
			smallDao.updateSmallText(temp);
			return SmallMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		SmallText temp = new SmallText();
		temp.setSmallText(texto);
		smallDao.updateSmallText(temp);
		return SmallMethod.class;
	}

	
	
}