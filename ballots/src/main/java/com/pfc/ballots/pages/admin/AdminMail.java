package com.pfc.ballots.pages.admin;

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
import com.pfc.ballots.util.Encryption;

public class AdminMail {

	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	@Persist
	UserDao userDao;
	@Persist
	EmailAccountDao emailAccountDao;
	
	@SessionState
	private DataSession datasession;
	
	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Inject
	private Request request;
	
	@Persist 
	@Property
	private EmailAccount emailAccount;
	
	public void setupRender()
	{
		componentResources.discardPersistentFieldChanges();
		emailAccountDao=DB4O.getEmailAccountDao();
		userDao=DB4O.getUsuarioDao();
		emailAccount=emailAccountDao.getAccount();
		emailform=new EmailAccount();
		settings=false;
		change=false;
		badPass=false;
		badMatch=false;
		changeAccount=false;
	}
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ////////////////////////////////////////////////////// NEW EMAIL ZONE ///////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@InjectComponent
	private Zone newEmailZone;
	
	@Property
	@Persist
	private EmailAccount emailform;
	
	@Property
	@Persist
	@Validate("required,minLength=5")
	private String pass1;
	
	@Property
	@Persist
	@Validate("required,minLength=5")
	private String pass2;
	
	@Property
	@Persist
	private boolean showBadPassNew;
	
	public boolean isShowNewZone()
	{
		if(emailAccount==null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public void onSuccessFromNewForm()
	{
		if(request.isXHR())
		{
			if(!pass1.equals(pass2))
			{
				showBadPassNew=true;
				ajaxResponseRenderer.addRender("newEmailZone", newEmailZone);
			}
			else
			{
				showBadPassNew=false;
				emailform.setPassword(pass1);
				emailAccount=emailform;
				emailAccountDao.setEmailAccount(emailform);
				ajaxResponseRenderer.addRender("newEmailZone", newEmailZone);
			}
		}
	}
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////// EMAIL SETTINGS ZONE /////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@InjectComponent
	private Zone emailSettingsZone;
	
	@Persist
	private boolean settings;
	
	public boolean isShowSettings()
	{
		if(isShowNewZone())
		{
			return false;
		}
		else
		{
			if(settings==false)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public void onActionFromChangeAccount()
	{
		System.out.println("CHANGE ACCOUNT");
		if(request.isXHR())
		{
			changeAccount=true;
			settings=true;
			badPass=false;
			badMatch=false;
			emailform=new EmailAccount();
			ajaxResponseRenderer.addRender("emailSettingsZone", emailSettingsZone).addRender("changeAccountZone", changeAccountZone);
			
		}
	}
	
	public void onActionFromChangePass()
	{
		System.out.println("CHANGE PASS");
		if(request.isXHR())
		{
			settings=true;
			change=true;
			badPass=false;
			badMatch=false;
			ajaxResponseRenderer.addRender("emailSettingsZone", emailSettingsZone).addRender("changePassZone", changePassZone);
		}
	}
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 ///////////////////////////////////////////////////// CHANGE PASS ZONE //////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@InjectComponent
	private Zone changePassZone;
	
	@Persist
	private boolean change;
	@Property
	@Validate("required,minLength=5")
	private String adminPass;
	
	@Property
	@Persist
	private boolean badPass;
	@Property
	@Persist
	private boolean badMatch;
	
	@Persist
	@Property
	private String newPass1;
	@Persist
	@Property
	private String newPass2;
	
	
	public boolean isShowChange()
	{
		if(isShowNewZone())
		{
			return false;
		}
		else
		{
			if(change==true)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public void onSuccessFromChangePassForm()
	{
		if(request.isXHR())
		{
			String pass=userDao.getProfileById(datasession.getId()).getPassword();
			String pass2=Encryption.getStringMessageDigest(adminPass, Encryption.SHA1);
			if(pass.equals(pass2))
			{
				System.out.println("CORRECTO");
				badPass=false;
				if(newPass1.equals(newPass2))
				{
					badMatch=false;
					change=false;
					settings=false;
					emailAccount.setPassword(newPass1);
					emailAccountDao.updateEmailAccount(emailAccount);
					ajaxResponseRenderer.addRender("changePassZone", changePassZone).addRender("emailSettingsZone", emailSettingsZone);
				}
				else
				{
					badMatch=true;
					ajaxResponseRenderer.addRender("changePassZone", changePassZone);
				}
				
			}
			else
			{
				badPass=true;
				badMatch=false;
				ajaxResponseRenderer.addRender("changePassZone", changePassZone);
			}
		}
			
	}
	
	public void onActionFromCancelChangePass()
	{
		if(request.isXHR())
		{
			change=false;
			settings=false;
			ajaxResponseRenderer.addRender("changePassZone", changePassZone).addRender("emailSettingsZone", emailSettingsZone);
		}
	}
	
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////////////////////// CHANGE ACCOUNT ZONE /////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Persist
	private boolean changeAccount;
	
	@InjectComponent
	private Zone changeAccountZone;
	
	@Property
	@Validate("required,minLength=5")
	private String adminPassword;
	
	@Property
	@Validate("required,minLength=5")
	private String password1;
	@Property
	@Validate("required,minLength=5")
	private String password2;
	
	
	public boolean isShowChangeAccount()
	{
		if(isShowNewZone())
		{
			return false;
		}
		else
		{
			if(changeAccount==true)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public void onSuccessFromChangeAccountForm()
	{
		if(request.isXHR())
		{
			String pass=userDao.getProfileById(datasession.getId()).getPassword();
			String pass2=Encryption.getStringMessageDigest(adminPassword, Encryption.SHA1);
			if(pass.equals(pass2))
			{
				System.out.println("CORRECTO");
				badPass=false;
				if(password1.equals(password2))
				{
					badMatch=false;
					emailform.setPassword(password1);
					changeAccount=false;
					settings=false;
					emailAccountDao.updateEmailAccount(emailform);
					emailAccount=emailform;
					ajaxResponseRenderer.addRender("changeAccountZone", changeAccountZone).addRender("emailSettingsZone", emailSettingsZone);
				}
				else
				{
					badMatch=true;
					ajaxResponseRenderer.addRender("changeAccountZone", changeAccountZone);
				}
				
			}
			else
			{
				badPass=true;
				badMatch=false;
				ajaxResponseRenderer.addRender("changeAccountZone", changeAccountZone);
			}
		}
	}
	
	public void onActionFromCancelChangeAccount()
	{
		if(request.isXHR())
		{
			changeAccount=false;
			settings=false;
			ajaxResponseRenderer.addRender("changeAccountZone", changeAccountZone).addRender("emailSettingsZone", emailSettingsZone);

			
		}
	}
}
