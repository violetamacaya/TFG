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
import com.pfc.ballots.entities.NansonText;
import com.pfc.ballots.dao.*;

/**
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public class NansonMethod {

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
	NansonDaoDB4O nansonDao;
	
	@Persist
	@Property
	private String nansonText;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public NansonMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		nansonDao = new NansonDaoDB4O(null);
		nansonText= DB4O.getNansonTextDao().getNansonText().getNansonText();	
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
			NansonText temp = new NansonText();
			temp.setNansonText(request.getParameter("nansonText"));
			nansonDao.updateNansonText(temp);
			return NansonMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		NansonText temp = new NansonText();
		temp.setNansonText(texto);
		nansonDao.updateNansonText(temp);
		return NansonMethod.class;
	}
	
	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("mejorpeor"))
		{
			page=MejorPeorMethod.class;
		}
		else if(section.equals("rangevoting"))
		{
			page=RangeVotingMethod.class;
		}

		return page;
	}

	
	
}