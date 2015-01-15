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
import com.pfc.ballots.entities.BlackText;
import com.pfc.ballots.dao.*;

/**
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
public class BlackMethod {

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
	BlackDaoDB4O blackDao;
	
	@Persist
	@Property
	private String blackText;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public BlackMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		blackDao = new BlackDaoDB4O(null);
		blackText= DB4O.getBlackTextDao().getBlackText().getBlackText();	
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
			BlackText temp = new BlackText();
			temp.setBlackText(request.getParameter("blackText"));
			blackDao.updateBlackText(temp);
			return BlackMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		BlackText temp = new BlackText();
		temp.setBlackText(texto);
		blackDao.updateBlackText(temp);
		return BlackMethod.class;
	}

	
	
}