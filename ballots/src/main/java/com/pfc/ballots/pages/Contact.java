package com.pfc.ballots.pages;

import java.util.Random;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.beaneditor.Width;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.pfc.ballots.dao.EmailAccountDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.entities.EmailAccount;
import com.pfc.ballots.util.Mail;

public class Contact
{
	
	//////////////////////////////////////////////////////////// DAO ///////////////////////////////////////////////////////////
	
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	EmailAccountDao emailAccountDao=null;
	@Inject
    private ComponentResources componentResources;
	@Property
	@Persist(PersistenceConstants.FLASH)
	private boolean badCaptcha;
	
	@Property
	@Persist
	private String captcha1;
	@Property
	@Persist
	private String captcha2;
	@Property
	@Persist
	private String captchaValue;
	
	////////////////////////////////////////////////////////// INTIALIZE //////////////////////////////////////////////////////////////
	
	public void setupRender()
	{
		showEmailProblem=false;
		emailAccountDao=DB4O.getEmailAccountDao();
		emailAccount=emailAccountDao.getAccount();
		
		if(!Mail.checkAccount(emailAccount))
		{
			showEmailProblem=true;
			System.out.println("--------------------\nHOLA");
		}
		Random r=new Random();
		captcha1=String.valueOf(r.nextInt(101));
		r=new Random();
		captcha2=String.valueOf(r.nextInt(101));
		
	}
	
	@Property
	@Persist
	EmailAccount emailAccount;
	
	@Property
	private boolean showEmailProblem;

	@Property
	@Validate("required,regexp=^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
	@Persist(PersistenceConstants.FLASH)
	private String remitente;
	
	@Width(value = 75)
	@Property
	@Validate("minLength=20")
	@Persist(PersistenceConstants.FLASH)
	private String text;
	
	public Object onSuccessFromContact()
	{
		badCaptcha=false;
		if(isNumeric(captchaValue))
		{
			if(Integer.parseInt(captchaValue)!=Integer.parseInt(captcha1)+Integer.parseInt(captcha2))
			{
				badCaptcha=true;
				captchaValue=null;
				return null;
			}
		}
		else
		{
			captchaValue=null;
			badCaptcha=true;
			return null;
		}
		String asunto="Contact: "+remitente;
		String asuntoRemit="NoReply-copia: "+emailAccount.getEmail();
		if(Mail.sendMail(emailAccount.getEmail(), emailAccount.getPassword(), emailAccount.getEmail(), asunto, text))
		{
			Mail.sendMail(emailAccount.getEmail(), emailAccount.getPassword(), remitente, asuntoRemit, text);
			System.out.println("ENVIADO");
		}
		else
		{
			System.out.println("ERROR AL ENVIAR");
		}
		componentResources.discardPersistentFieldChanges();
		return Index.class;
		
	}
	
	private boolean isNumeric(String cadena)
	{
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}
	
	
}
