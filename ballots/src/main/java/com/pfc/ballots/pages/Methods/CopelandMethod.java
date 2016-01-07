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
import com.pfc.ballots.entities.CopelandText;
import com.pfc.ballots.dao.*;

/**Gives information about the method
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public class CopelandMethod {

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
	CopelandDaoDB4O copelandDao;
	
	@Persist
	@Property
	private String copelandText;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public CopelandMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		copelandDao = new CopelandDaoDB4O(null);
		copelandText= DB4O.getCopelandTextDao().getCopelandText().getCopelandText();	
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
			CopelandText temp = new CopelandText();
			temp.setCopelandText(request.getParameter("copelandText"));
			copelandDao.updateCopelandText(temp);
			return CopelandMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		CopelandText temp = new CopelandText();
		temp.setCopelandText(texto);
		copelandDao.updateCopelandText(temp);
		return CopelandMethod.class;
	}

	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("coombs"))
		{
			page=CoombsMethod.class;
		}
		else if(section.equals("dodgson"))
		{
			page=DodgsonMethod.class;
		}

		return page;
	}
	
}