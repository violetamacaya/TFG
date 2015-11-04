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
import com.pfc.ballots.entities.CondorcetText;
import com.pfc.ballots.dao.*;

/**
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
public class CondorcetMethod {

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
	CondorcetDaoDB4O condorcetDao;
	
	@Persist
	@Property
	private String condorcetText;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public CondorcetMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		condorcetDao = new CondorcetDaoDB4O(null);
		condorcetText= DB4O.getCondorcetTextDao().getCondorcetText().getCondorcetText();	
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
			CondorcetText temp = new CondorcetText();
			temp.setCondorcetText(request.getParameter("condorcetText"));
			condorcetDao.updateCondorcetText(temp);
			return CondorcetMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		CondorcetText temp = new CondorcetText();
		temp.setCondorcetText(texto);
		condorcetDao.updateCondorcetText(temp);
		return CondorcetMethod.class;
	}

	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("coombs"))
		{
			page=CoombsMethod.class;
		}
		else if(section.equals("bucklin"))
		{
			page=BucklinMethod.class;
		}

		return page;
	}
	
	
}