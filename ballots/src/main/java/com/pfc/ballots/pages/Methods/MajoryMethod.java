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
import com.pfc.ballots.entities.MajoryText;
import com.pfc.ballots.dao.*;

/**
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
public class MajoryMethod {

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
	MajoryDaoDB4O majoryDao;
	
	@Persist
	@Property
	private String majoryText;
	
	@Inject
	private ComponentResources componentResources;

	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public MajoryMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		majoryDao = new MajoryDaoDB4O(null);
		majoryText= DB4O.getMajoryTextDao().getMajoryText().getMajoryText();	
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
			MajoryText temp = new MajoryText();
			temp.setMajoryText(request.getParameter("majoryText"));
			majoryDao.updateMajoryText(temp);
			return MajoryMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		MajoryText temp = new MajoryText();
		temp.setMajoryText(texto);
		majoryDao.updateMajoryText(temp);
		return MajoryMethod.class;
	}

	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("kemeny"))
		{
			page=KemenyMethod.class;
		}
		else if(section.equals("mejorpeor"))
		{
			page=MejorPeorMethod.class;
		}

		return page;
	}
	
}