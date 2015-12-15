package com.pfc.ballots.pages.profile;

import com.pfc.ballots.pages.users.ForgottenPassword;
import com.pfc.ballots.pages.users.LogIn;

public class SuccessPassChange {
	Object onBreadCrumbs(String section)
	{									
		Object page=null;
		if(section.equals("login"))//This handle the lateral menu
		{
			page=LogIn.class;
		}
		else if(section.equals("forgot"))//This handle the lateral menu
		{
			page=ForgottenPassword.class;
		}

		return page;
	}
}
