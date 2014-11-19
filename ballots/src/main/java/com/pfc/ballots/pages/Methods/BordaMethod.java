/**
 * 
 */
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
import com.pfc.ballots.entities.BordaText;
import com.pfc.ballots.entities.ballotdata.Borda;
import com.pfc.ballots.dao.*;

/**
 * @author violeta
 *
 */
public class BordaMethod {

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
	BordaDaoDB4O bordaDao;
	
	@Persist
	@Property
	private String bordaText;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public BordaMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		bordaDao = new BordaDaoDB4O(null);
		bordaText= DB4O.getBordaTextDao().getBordaText().getBordaText();	
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
			BordaText temp = new BordaText();
			temp.setBordaText(request.getParameter("bordaText"));
			bordaDao.updateBordaText(temp);
			return BordaMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		BordaText temp = new BordaText();
		temp.setBordaText(texto);
		bordaDao.updateBordaText(temp);
		return BordaMethod.class;
	}

	
	
}