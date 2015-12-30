package com.pfc.ballots.pages.Company;


import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.CompanyDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserLogedDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Company;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
/**
 * UpdateCompany class is the controller for the UpdateCompany page that
 * allow to change the name of a company
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUN-2014
 * @author Violeta Macaya Sánchez
 * @version 2.0 OCT-2015
 */
public class UpdateCompany {

	@SessionState
	private DataSession datasession;


	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private Request request;

	@Persist
	private boolean notDone;

	@Persist
	private String companyName;

	@Persist
	private String companyAlias;

	@Persist
	private String companyEmail;

	@Persist
	private String companyFirstName;

	@Persist
	private String companyLastName;

	@Persist
	private String companyAddress;

	@Persist
	private String companyCP;

	@Persist
	private String companyLocalidad;
	
	@Persist
	private String companyProvincia;

	@Persist
	private String companyPais;

	@Persist
	private String companyUrl;
	@Persist
	@Property
	private Company oldCompany;
	@Persist
	@Property
	private Company newCompany;

	@InjectComponent
	private Zone formZone;

	@Persist
	@Property
	private boolean showForm;

	@Persist
	@Property
	private boolean showNameInUse;

	@Persist
	@Property
	private boolean showBadChar;
	@Persist
	@Property
	private boolean showBadName;


	//****************************************Initialize DAO****************************//
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	@Persist
	CompanyDao companyDao;
	UserLogedDao lgdDao=null;


	/**
	 * Initializes the page
	 * @param companyName
	 */
	public void setup(String companyName)
	{
		notDone=true;
		this.companyName=companyName;
		companyDao=DB4O.getCompanyDao();
		oldCompany=companyDao.getCompanyByName(companyName);


		this.companyAlias=oldCompany.getAlias();
		this.companyEmail=oldCompany.getAdminEmail();
		this.companyFirstName=oldCompany.getFirstName();
		this.companyLastName=oldCompany.getLastName();
		this.companyAddress=oldCompany.getAddress();
		this.companyCP=oldCompany.getCp();
		this.companyLocalidad=oldCompany.getLocalidad();
		this.companyProvincia=oldCompany.getProvincia();
		this.companyPais=oldCompany.getPais();
		this.companyUrl = oldCompany.getUrl();

	}

	public void setupRender()
	{

		oldCompany=companyDao.getCompanyByName(companyName);
		newCompany= new Company();
		newCompany.setCompanyName(oldCompany.getCompanyName());
		newCompany.setAlias(oldCompany.getAlias());
		newCompany.setAdminEmail(oldCompany.getAdminEmail());
		newCompany.setFirstName(oldCompany.getFirstName());
		newCompany.setLastName(oldCompany.getLastName());
		newCompany.setAddress(oldCompany.getAddress());
		newCompany.setCp(oldCompany.getCp());
		newCompany.setProvincia(oldCompany.getProvincia());
		newCompany.setLocalidad(oldCompany.getLocalidad());
		newCompany.setPais(oldCompany.getPais());
		newCompany.seturl(oldCompany.getUrl());
		


		showForm=true;
		showNameInUse=false;

		showBadChar=false;
		showBadName=false;

	}


	/**
	 * Validates the data of the the UpdateCompanyForm
	 */
	public void onValidateFromUpdateCompanyForm()
	{
		newCompany.setCompanyName(newCompany.getCompanyName().toLowerCase());
		newCompany.setAlias(newCompany.getAlias().toLowerCase());
		newCompany.setAdminEmail(newCompany.getAdminEmail().toLowerCase());
		newCompany.setFirstName(newCompany.getFirstName().toLowerCase());
		newCompany.setLastName(newCompany.getLastName().toLowerCase());
		newCompany.setAddress(newCompany.getAddress().toLowerCase());
		newCompany.setCp(newCompany.getCp().toLowerCase());
		newCompany.setLocalidad(newCompany.getLocalidad().toLowerCase());
		newCompany.setProvincia(newCompany.getProvincia().toLowerCase());
		newCompany.setPais(newCompany.getPais().toLowerCase());
		newCompany.seturl(newCompany.getUrl().toLowerCase());
		if(newCompany.getCompanyName().equals("login") )
		{
			showBadName=true;
		}
		if(companyDao.isCompanyRegistred(newCompany.getCompanyName()))
		{
			if(newCompany.getCompanyName().equals(oldCompany.getCompanyName())){
				showNameInUse=false;
			}
			else{
			showNameInUse=true;
			}
		}
	}


	/**
	 * Handle the success of the UpdateCompanyForm
	 */
	public Object onSuccessFromUpdateCompanyForm()
	{
		if(request.isXHR())
		{

			if(showBadChar || showBadName || showNameInUse)
			{
				ajaxResponseRenderer.addRender("formZone", formZone);
				return null;
			}

			oldCompany.setCompanyName(newCompany.getCompanyName());
			oldCompany.setAlias(newCompany.getAlias());
			oldCompany.setAdminEmail(newCompany.getAdminEmail());
			oldCompany.setFirstName(newCompany.getFirstName());
			oldCompany.setLastName(newCompany.getLastName());
			oldCompany.setAddress(newCompany.getAddress());
			oldCompany.setCp(newCompany.getCp());
			oldCompany.setLocalidad(newCompany.getLocalidad());
			oldCompany.setProvincia(newCompany.getProvincia());
			oldCompany.setPais(newCompany.getPais());
			oldCompany.seturl(newCompany.getUrl());
			
			companyDao.updateCompany(oldCompany);
			notDone=false;
			return ListCompany.class;
		}
		return null;
	}

	/**
	 * Cancels the update of the name
	 * @return listComany page
	 */
	public Object onActionFromCancelBut()
	{
		notDone=false;
		return ListCompany.class;
	}
	/**
	 * Controls if the user can enter in the page
	 * @return another page if the user can't enter
	 */
	public Object onActivate()
	{
		if(notDone=false)
		{
			return ListCompany.class;
		}
		switch(datasession.sessionState())
		{
		case 0:
			return Index.class;
		case 1:
			return UnauthorizedAttempt.class;
		case 2:
			if(datasession.isMainUser())
			{
				return null;
			}
			return UnauthorizedAttempt.class;
		case 3:
			return SessionExpired.class;
		default:
			return Index.class;
		}

	}


}
