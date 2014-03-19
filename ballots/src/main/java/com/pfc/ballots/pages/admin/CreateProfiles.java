package com.pfc.ballots.pages.admin;

import java.io.File;








import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.upload.services.UploadedFile;

import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.pages.Index;

public class CreateProfiles {
	
	
	
	@SessionState
	private DataSession sesion;

	
	@Persist
	private String DBNtemp;
	
	
	@Persist
	private int state;
	
	@Property
	private UploadedFile file;
	
	@Inject
    private ComponentResources componentResources;
	
	void onSuccess()
	{
		sesion.getDBName();
		String path=System.getProperty("user.home")+System.getProperty("file.separator")+
				"BallotsFiles"+System.getProperty("file.separator")+"uploadfiles"+System.getProperty("file.separator")+"users.txt";
		File copied=new File(path);
		file.write(copied);
		componentResources.discardPersistentFieldChanges();
	}
	
	public void setup(String DBNtemp)
	{
		componentResources.discardPersistentFieldChanges();
		this.DBNtemp=DBNtemp;
		state=1;
	}
	Object onActivate()
	{
		if(state==0)
		{
			return Index.class;
		}
		else
		{
			return null;
		}
	}
	void cleanupRender()
	{
		state=0;
	}
	
	

}
