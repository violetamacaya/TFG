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
import com.pfc.ballots.entities.CoombsText;
import com.pfc.ballots.dao.*;

/**Gives information about the method
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public class CoombsMethod {

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
	CoombsDaoDB4O coombsDao;
	
	@Persist
	@Property
	private String coombsText;
	
	@Inject
	private ComponentResources componentResources;
	
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public CoombsMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		coombsDao = new CoombsDaoDB4O(null);
		coombsText= DB4O.getCoombsTextDao().getCoombsText().getCoombsText();	
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
			CoombsText temp = new CoombsText();
			temp.setCoombsText(request.getParameter("coombsText"));
			coombsDao.updateCoombsText(temp);
			return CoombsMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		CoombsText temp = new CoombsText();
		temp.setCoombsText(texto);
		coombsDao.updateCoombsText(temp);
		return CoombsMethod.class;
	}
	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("condorcet"))
		{
			page=CondorcetMethod.class;
		}
		else if(section.equals("copeland"))
		{
			page=CopelandMethod.class;
		}

		return page;
	}
	
	
}