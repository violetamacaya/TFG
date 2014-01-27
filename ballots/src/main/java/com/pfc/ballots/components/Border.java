package com.pfc.ballots.components;

import java.util.Locale;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.services.PersistentLocale;

public class Border {


	
	
	
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
}
