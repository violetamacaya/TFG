package com.pfc.ballots.components;

import java.util.Locale;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.admin.UserList;
import com.pfc.ballots.pages.profile.CreateProfile;

public class Border {

	/***************************************** Ajax menu stuff *******************************************************************/
	
	@InjectComponent
	private Zone userZone;
	@InjectComponent
	private Zone ballotZone;
	
	@SessionAttribute
	@Property
	private Boolean visibilityUser;
	
	@SessionAttribute
	@Property
	private Boolean visibilityBallot;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	 
	/****************************************  Constructor **********************************************************************/
	public Border()
	{
		if(visibilityUser==null || visibilityBallot==null)
		{
			visibilityUser=new Boolean("false");
			visibilityBallot=new Boolean("false");
			
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
	
	Object onMenu(String section)//Section is the context submitted by the eventlink to identify
	{
		Object page=null;
		System.out.println(section);
		
		if(section.equals("userz"))		//This handle the lateral menu
		{
			visibilityBallot=false;
			if(visibilityUser)
				{visibilityUser=false;}
			else
				{visibilityUser=true;}
		}
		else if(section.equals("ballotz"))
		{
			visibilityUser=false;
			if(visibilityBallot)
				{visibilityBallot=false;}
			else
				{visibilityBallot=true;}
		}
		else if(section.equals("new-user2"))
			{page=CreateProfile.class;}
		else if(section.equals("user-list"))
			{page=UserList.class;}
		else							//This handle the upper menu
		{
			visibilityUser=false;
			visibilityBallot=false;
			if(section.equals("index"))
				{page=Index.class;}
			if(section.equals("new-user1"))
				{page=CreateProfile.class;}
		}
		ajaxResponseRenderer.addRender("userZone", userZone).addRender("ballotZone", ballotZone);
		
		return page;
	}
}
