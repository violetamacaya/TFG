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
import com.pfc.ballots.entities.MejorPeorText;
import com.pfc.ballots.dao.*;

/**
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public class MejorPeorMethod {

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
	MejorPeorDaoDB4O mejorPeorDao;
	
	@Persist
	@Property
	private String mejorPeorText;
	
	@Inject
	private ComponentResources componentResources;

	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public MejorPeorMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		mejorPeorDao = new MejorPeorDaoDB4O(null);
		mejorPeorText= DB4O.getMejorPeorTextDao().getMejorPeorText().getMejorPeorText();	
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
			MejorPeorText temp = new MejorPeorText();
			temp.setMejorPeorText(request.getParameter("mejorPeorText"));
			mejorPeorDao.updateMejorPeorText(temp);
			return MejorPeorMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		MejorPeorText temp = new MejorPeorText();
		temp.setMejorPeorText(texto);
		mejorPeorDao.updateMejorPeorText(temp);
		return MejorPeorMethod.class;
	}

	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("majory"))
		{
			page=MajoryMethod.class;
		}
		else if(section.equals("nanson"))
		{
			page=NansonMethod.class;
		}

		return page;
	}
	
}