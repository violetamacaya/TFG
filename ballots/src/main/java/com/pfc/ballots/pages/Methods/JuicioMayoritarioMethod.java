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
import com.pfc.ballots.entities.JuicioMayoritarioText;
import com.pfc.ballots.dao.*;

/**Gives information about the method
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public class JuicioMayoritarioMethod {

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
	JuicioMayoritarioDaoDB4O juicioMayoritarioDao;
	
	@Persist
	@Property
	private String juicioMayoritarioText;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public JuicioMayoritarioMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		juicioMayoritarioDao = new JuicioMayoritarioDaoDB4O(null);
		juicioMayoritarioText= DB4O.getJuicioMayoritarioTextDao().getJuicioMayoritarioText().getJuicioMayoritarioText();	
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
			JuicioMayoritarioText temp = new JuicioMayoritarioText();
			temp.setJuicioMayoritarioText(request.getParameter("juicioMayoritarioText"));
			juicioMayoritarioDao.updateJuicioMayoritarioText(temp);
			return JuicioMayoritarioMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		JuicioMayoritarioText temp = new JuicioMayoritarioText();
		temp.setJuicioMayoritarioText(texto);
		juicioMayoritarioDao.updateJuicioMayoritarioText(temp);
		return JuicioMayoritarioMethod.class;
	}


	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("hare"))
		{
			page=HareMethod.class;
		}
		else if(section.equals("kemeny"))
		{
			page=KemenyMethod.class;
		}

		return page;
	}
	
}