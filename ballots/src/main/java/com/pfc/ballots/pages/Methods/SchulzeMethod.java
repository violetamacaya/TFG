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
import com.pfc.ballots.entities.SchulzeText;
import com.pfc.ballots.dao.*;

/**
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public class SchulzeMethod {

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
	SchulzeDaoDB4O schulzeDao;
	
	@Persist
	@Property
	private String schulzeText;
	
	@Inject
	private ComponentResources componentResources;
 
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public SchulzeMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		schulzeDao = new SchulzeDaoDB4O(null);
		schulzeText= DB4O.getSchulzeTextDao().getSchulzeText().getSchulzeText();	
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
			SchulzeText temp = new SchulzeText();
			temp.setSchulzeText(request.getParameter("schulzeText"));
			schulzeDao.updateSchulzeText(temp);
			return SchulzeMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		SchulzeText temp = new SchulzeText();
		temp.setSchulzeText(texto);
		schulzeDao.updateSchulzeText(temp);
		return SchulzeMethod.class;
	}

	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("rangevoting"))
		{
			page=RangeVotingMethod.class;
		}
		else if(section.equals("small"))
		{
			page=SmallMethod.class;
		}

		return page;
	}
	
}