package com.pfc.ballots.components;

import java.util.Locale;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.MethodsInfo;
import com.pfc.ballots.pages.Company.CreateCompany;
import com.pfc.ballots.pages.Company.ListCompany;
import com.pfc.ballots.pages.admin.LogList;
import com.pfc.ballots.pages.admin.UserList;
import com.pfc.ballots.pages.profile.CreateProfile;
import com.pfc.ballots.pages.profile.ProfileByFile;
import com.pfc.ballots.pages.profile.filesrow;
import com.pfc.ballots.pages.users.CompanyLogIn;
import com.pfc.ballots.pages.users.LogIn;

public class Border {

	/***************************************** Ajax menu stuff *******************************************************************/
	
	@SessionState
	private DataSession datasession;
	@InjectComponent
	private Zone userZone;
	@InjectComponent
	private Zone ballotZone;
	
	@InjectComponent
	private Zone companyZone;
	
	

	
	@SessionAttribute
	@Property
	private Boolean visibilityUser;
	
	@SessionAttribute
	@Property
	private Boolean visibilityBallot;
	@SessionAttribute
	@Property
	private Boolean visibilityCompany;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	 
	/****************************************  Constructor **********************************************************************/
	public Border()
	{
		if(visibilityUser==null || visibilityBallot==null || visibilityCompany==null)
		{
			visibilityUser=new Boolean("false");
			visibilityBallot=new Boolean("false");
			visibilityCompany=new Boolean("false");
		}
		if(datasession==null)
		{
			datasession=new DataSession();
		}


	}
	/************************************* locale (languages stuff) **************************************************************/
	
	@Inject
	private PersistentLocale persistentLocale;
	@Inject
	private Locale currentLocale;
	
	@Persist
	private String localeLabel;
	
	
	//get locale label ------------------------------------------------------------------------------------------------------------
	public String getLocaleLabel()
	{	
		if (localeLabel == null)
		{
			if (currentLocale.equals(Locale.ENGLISH)) {
				
				localeLabel = new Locale("es").getDisplayName(Locale.getDefault());
			}
		}
		else
		{
			localeLabel = new Locale("en").getDisplayName(Locale.ENGLISH);	
		} 
		
		return localeLabel;
	}
	
	//on action from switch locale ------------------------------------------------------------------------------------------------
	void onActionFromSwitchlocale()
	{
		localeLabel = currentLocale.getDisplayName(currentLocale);
		
		if (currentLocale.equals(Locale.ENGLISH))
		{
			persistentLocale.set(Locale.getDefault());
		}
		else
		{
			persistentLocale.set(Locale.ENGLISH);
		}
	}
	
	/***************************************** Ajax menu Event Handler **********************************************************/
	
	/*
	 * Section is the context submitted by the eventlink to identify each link
	 * in the menu of the border component for close or open drop menus. 
	 */
	Object onMenu(String section)
	{							
		
		
		Object page=null;		
		if(section.equals("userz"))		//This handle the lateral menu
		{
			visibilityBallot=false;
			visibilityCompany=false;
			if(visibilityUser)
				{visibilityUser=false;}
			else
				{visibilityUser=true;}
		}
		else if(section.equals("ballotz"))
		{
			visibilityCompany=false;
			visibilityUser=false;
			if(visibilityBallot)
				{visibilityBallot=false;}
			else
				{visibilityBallot=true;}
		}
		else if(section.equals("companyz"))
		{
			visibilityUser=false;
			visibilityBallot=false;
			if(visibilityCompany)
				{visibilityCompany=false;}
			else
				{visibilityCompany=true;}
		}
		else if(section.equals("new-user2"))
			{page=CreateProfile.class;}
		else if(section.equals("user-list"))
			{page=UserList.class;}
		else if(section.equals("user-file"))
			{page=filesrow.class;}
		else if(section.equals("log-list"))
			{page=LogList.class;}
		else if(section.equals("new-company"))
			{page=CreateCompany.class;}
		else if(section.equals("list-company"))
			{page=ListCompany.class;}
		else							//This handle the upper menu
		{
			visibilityCompany=false;
			visibilityUser=false;
			visibilityBallot=false;
			if(section.equals("index"))
				{page=Index.class;}
			if(section.equals("new-user1"))
				{page=CreateProfile.class;}
			if(section.equals("LogIn"))
				{page=LogIn.class;}
			if(section.equals("methods"))
				{page=MethodsInfo.class;}
			if(section.equals("company-login"))
				{page=CompanyLogIn.class;}
		}
		ajaxResponseRenderer.addRender("userZone", userZone).addRender("ballotZone", ballotZone).addRender("companyZone", companyZone);
		
		return page;
	}
}
