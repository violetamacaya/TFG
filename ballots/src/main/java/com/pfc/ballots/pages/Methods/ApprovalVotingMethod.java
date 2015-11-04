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
import com.pfc.ballots.entities.ApprovalVotingText;
import com.pfc.ballots.dao.*;

/**
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
public class ApprovalVotingMethod {

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
	ApprovalVotingDaoDB4O approvalVotingDao;
	
	@Persist
	@Property
	private String approvalVotingText;
	
	@Inject
	private ComponentResources componentResources;
	
	
	@Inject
	private Request request;
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);

	
	public ApprovalVotingMethod(){

		
	}
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();		
		approvalVotingDao = new ApprovalVotingDaoDB4O(null);
		approvalVotingText= DB4O.getApprovalVotingTextDao().getApprovalVotingText().getApprovalVotingText();	
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
			ApprovalVotingText temp = new ApprovalVotingText();
			temp.setApprovalVotingText(request.getParameter("approvalVotingText"));
			approvalVotingDao.updateApprovalVotingText(temp);
			return ApprovalVotingMethod.class;
		}
		return null;
	}
	
	public Object onActionFromsetDefault(){
		String texto = messages.get("content");
		ApprovalVotingText temp = new ApprovalVotingText();
		temp.setApprovalVotingText(texto);
		approvalVotingDao.updateApprovalVotingText(temp);
		return ApprovalVotingMethod.class;
	}
	Object onMenu(String section)
	{
		Object page=null;
		
		if(section.equals("black"))
		{
			page=BlackMethod.class;
		}
	return page;
	}
	
}