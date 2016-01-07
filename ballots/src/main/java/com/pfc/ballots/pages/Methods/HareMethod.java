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
import com.pfc.ballots.entities.HareText;
import com.pfc.ballots.dao.*;

/**Gives information about the method
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public class HareMethod {

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
	HareDaoDB4O hareDao;
	
	@Persist
	@Property
	private String hareText;
	
	@Inject
	private ComponentResources componentResources;
	
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public HareMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		hareDao = new HareDaoDB4O(null);
		hareText= DB4O.getHareTextDao().getHareText().getHareText();	
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
			HareText temp = new HareText();
			temp.setHareText(request.getParameter("hareText"));
			hareDao.updateHareText(temp);
			return HareMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		HareText temp = new HareText();
		temp.setHareText(texto);
		hareDao.updateHareText(temp);
		return HareMethod.class;
	}

	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("dodgson"))
		{
			page=DodgsonMethod.class;
		}
		else if(section.equals("juiciomayoritario"))
		{
			page=JuicioMayoritarioMethod.class;
		}

		return page;
	}

	
}