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
import com.pfc.ballots.entities.BramsText;
import com.pfc.ballots.dao.*;

/**Gives information about the method
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
public class BramsMethod {

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
	BramsDaoDB4O bramsDao;
	
	@Persist
	@Property
	private String bramsText;
	
	@Inject
	private ComponentResources componentResources;

	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public BramsMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		bramsDao = new BramsDaoDB4O(null);
		bramsText= DB4O.getBramsTextDao().getBramsText().getBramsText();	
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
			BramsText temp = new BramsText();
			temp.setBramsText(request.getParameter("bramsText"));
			bramsDao.updateBramsText(temp);
			return BramsMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		BramsText temp = new BramsText();
		temp.setBramsText(texto);
		bramsDao.updateBramsText(temp);
		return BramsMethod.class;
	}

	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("borda"))
		{
			page=BordaMethod.class;
		}
		else if(section.equals("bucklin"))
		{
			page=BucklinMethod.class;
		}

		return page;
	}
	
}