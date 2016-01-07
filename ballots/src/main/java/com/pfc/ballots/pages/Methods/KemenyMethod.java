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

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.KemenyDaoDB4O;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.KemenyText;

/**Gives information about the method
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
public class KemenyMethod {

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
	KemenyDaoDB4O kemenyDao;
	
	@Persist
	@Property
	private String kemenyText;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public KemenyMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		kemenyDao = new KemenyDaoDB4O(null);
		kemenyText= DB4O.getKemenyTextDao().getKemenyText().getKemenyText();	
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
			KemenyText temp = new KemenyText();
			temp.setKemenyText(request.getParameter("kemenyText"));
			kemenyDao.updateKemenyText(temp);
			return KemenyMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		KemenyText temp = new KemenyText();
		temp.setKemenyText(texto);
		kemenyDao.updateKemenyText(temp);
		return KemenyMethod.class;
	}


	Object onMenu(String section)
	{
		Object page=null;
		if(section.equals("juiciomayoritario"))
		{
			page=JuicioMayoritarioMethod.class;
		}
		else if(section.equals("majory"))
		{
			page=RelativeMajorityMethod.class;
		}

		return page;
	}
	
}