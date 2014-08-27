package com.pfc.ballots.pages.users;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.EmailAccountDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.EmailAccount;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.admin.AdminMail;
import com.pfc.ballots.pages.profile.SuccessPassChange;
import com.pfc.ballots.util.Encryption;
import com.pfc.ballots.util.Mail;
import com.pfc.ballots.util.PasswordGenerator;

public class ForgottenPassword {

	
	private FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	@Persist
	private UserDao userDao;
	@Persist
	private EmailAccountDao emailAccountDao;
	
	@SessionState
	private DataSession datasession;
	
	@Inject
	private Request request;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();
		userDao=DB4O.getUsuarioDao();
		emailAccountDao=DB4O.getEmailAccountDao();
		badMail=false;
		badSend=false;
	}
	
	
	  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 //////////////////////////////////////////////////////// FORM ZONE  //////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Property
	@Persist
	private boolean badMail;
	@Property
	@Persist
	private boolean badSend;
	
	@InjectComponent
	private Zone formZone;
	
	@Property
	@Validate("required,regexp=^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	private String email;
	
	
	/**
	 * 
	 * sends an email if the email is in the database
	 * @return Index page
	 */
	
	public Object onSuccessFromForgottenForm()
	{
		if(request.isXHR())
		{
			Profile profile=userDao.getProfileByEmail(email);
			if(profile==null)
			{
				badMail=true;
				ajaxResponseRenderer.addRender("formZone", formZone);
				
			}
			else
			{
				badMail=false;
				EmailAccount account=emailAccountDao.getAccount();
				String newPass=PasswordGenerator.getPassword();
				profile.setPassword(Encryption.getStringMessageDigest(newPass, Encryption.SHA1));
				String msg="Su nueva contraseña es :"+newPass;
				if(Mail.sendMail(account.getEmail(), account.getPassword(), email, "NewPass:NoReply", msg))
				{
					badSend=false;
					userDao.UpdateByEmail(profile);
					ajaxResponseRenderer.addRender("formZone", formZone);
					return SuccessPassChange.class;
				}
				else
				{
					badSend=true;
					ajaxResponseRenderer.addRender("formZone", formZone);
				}
				
				
			}
		}
		return null;
	}
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////////// ON ACTIVATE /////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	* Controls if the user can enter in the page
	* @return another page if the user can't enter
	*/
    
    public Object onActivate()
	{
 
		switch(datasession.sessionState())
		{
			case 0:
				return null;
			case 1:
				return Index.class;
			case 2:
				return Index.class; 
			case 3:
				return SessionExpired.class;
			default:
				return Index.class;
		}
	}
}
