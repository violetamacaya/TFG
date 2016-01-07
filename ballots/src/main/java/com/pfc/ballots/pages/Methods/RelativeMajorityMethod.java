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

import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.RelativeMajorityText;
import com.pfc.ballots.dao.*;

/**Gives information about the method
 * @author Violeta Macaya Sánchez
 * @version 1.0 ENE-2015
 *
 */
public class RelativeMajorityMethod {

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
	RelativeMajorityDaoDB4O relativeMajorityDao;
	
	@Persist
	@Property
	private String relativeMajorityText;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public RelativeMajorityMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		relativeMajorityDao = new RelativeMajorityDaoDB4O(null);
		relativeMajorityText= DB4O.getRelativeMajorityTextDao().getRelativeMajorityText().getRelativeMajorityText();	
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
			RelativeMajorityText temp = new RelativeMajorityText();
			temp.setRelativeMajorityText(request.getParameter("relativeMajorityText"));
			relativeMajorityDao.updateRelativeMajorityText(temp);
			return RelativeMajorityMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		RelativeMajorityText temp = new RelativeMajorityText();
		temp.setRelativeMajorityText(texto);
		relativeMajorityDao.updateRelativeMajorityText(temp);
		return RelativeMajorityMethod.class;
	}


	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("mejorpeor"))
		{
			page=MejorPeorMethod.class;
		}
		else if(section.equals("kemeny"))
		{
			page=KemenyMethod.class;
		}

		return page;
	}
	
}