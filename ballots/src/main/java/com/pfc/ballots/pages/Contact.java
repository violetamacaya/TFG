package com.pfc.ballots.pages;


import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.beaneditor.Width;
import org.apache.tapestry5.ioc.annotations.Inject;

import se.unbound.tapestry.breadcrumbs.BreadCrumb;
import se.unbound.tapestry.breadcrumbs.BreadCrumbReset;

import com.pfc.ballots.dao.EmailAccountDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.entities.EmailAccount;
import com.pfc.ballots.util.Mail;


@BreadCrumb(titleKey="contact.title")
@BreadCrumbReset(ignorePages = {Index.class})
public class Contact
{
	
	//////////////////////////////////////////////////////////// DAO ///////////////////////////////////////////////////////////
	
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	EmailAccountDao emailAccountDao=null;
	@Inject
    private ComponentResources componentResources;


	
	
	////////////////////////////////////////////////////////// INTIALIZE //////////////////////////////////////////////////////////////
	
	public void setupRender()
	{
		showEmailProblem=false;
		emailAccountDao=DB4O.getEmailAccountDao();
		emailAccount=emailAccountDao.getAccount();
		
		if(!Mail.checkAccount(emailAccount))
		{
			showEmailProblem=true;
		}

		
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
	
	public void onSuccessFromContact()
	{

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
		
		
	}
}
