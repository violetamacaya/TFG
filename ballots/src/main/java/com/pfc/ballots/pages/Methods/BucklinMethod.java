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
import com.pfc.ballots.entities.BucklinText;
import com.pfc.ballots.dao.*;

/**Gives information about the method
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
public class BucklinMethod {

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
	BucklinDaoDB4O bucklinDao;
	
	@Persist
	@Property
	private String bucklinText;
	
	@Inject
	private ComponentResources componentResources;

	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public BucklinMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		bucklinDao = new BucklinDaoDB4O(null);
		bucklinText= DB4O.getBucklinTextDao().getBucklinText().getBucklinText();	
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
			BucklinText temp = new BucklinText();
			temp.setBucklinText(request.getParameter("bucklinText"));
			bucklinDao.updateBucklinText(temp);
			return BucklinMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		BucklinText temp = new BucklinText();
		temp.setBucklinText(texto);
		bucklinDao.updateBucklinText(temp);
		return BucklinMethod.class;
	}


	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("brams"))
		{
			page=BramsMethod.class;
		}
		else if(section.equals("condorcet"))
		{
			page=CondorcetMethod.class;
		}

		return page;
	}
	
}