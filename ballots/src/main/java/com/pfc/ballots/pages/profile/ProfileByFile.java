package com.pfc.ballots.pages.profile;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.upload.services.UploadedFile;

import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.util.ManipulateFiles;



public class ProfileByFile {
	 	@Property
	    private UploadedFile file;
	 
	 
	 	@Property
	 	@Persist
	 	private boolean showGrid;
	 	@Persist
	 	private int state;
	 	
	 	@Persist
	 	@Property
	 	private boolean editing;
	 	String sep=System.getProperty("file.separator");
	 	String path=System.getProperty("user.home")+sep+"BallotsFiles"+sep+"uploadfiles"+sep;
	 	
	 	//table
		@Property
	 	private List<Profile> persons;
	 	
	 	@Property
	 	private Profile person;
	 	
	 	@InjectComponent
	 	private Zone rowZone;
	 	
	 	@Persist
	 	String finalpath;
	 	
	 	@Inject
	 	private AjaxResponseRenderer ajaxResponseRenderer;
	 	
	 	
	 	
	 	
	 	private enum Actions{
	 		EDIT,CANCEL,SAVE;
	 	}
	 	private Actions action;
	 	
	 	void setupRender()
	 	{
	 		
	 		persons=ManipulateFiles.getProfilesFromFile(finalpath);
	 		/*persons=null;
	 		if(state==0 && showGrid)
	 		{
	 			state=1;
	 			persons=ManipulateFiles.getProfilesFromFile(finalpath);
	 		}*/
	 		
	 	}
	 	void cleanupRender()
	 	{/*
	 		if(state==1)
	 		{
	 			showGrid=false;
	 			state=0;
	 		}*/
	 	}
	 	
	 	
	    public void onSuccessFromUploadForm()
	    {
	        System.out.println("SUCCESS ");
	        showGrid=true;
	        state=0;
	        String[] temp=file.getFileName().split("\\.");
	        finalpath=new String(path+"user"+"."+temp[temp.length-1]);
	    	File copied = new File(finalpath);
	         
	        file.write(copied);
	    }
	    public void onFaiureFromUploadForm()
	    {
	        System.out.println("FAILURE");
	    }
	    public void onSuccessFromPersonForm()
	    {
	    	System.out.println("SUCCESS PERSON FORM");
	    }
	    public void onFailureFromPersonForm()
	    {
	    	System.out.println("FAILURE PERSON FORM");
	    }
	    public void onPrepareForRenderFromPersonForm()
	    {
	    	
	    }
	    public void onPrepareForSubmitFromPersonForm()
	    {
	    	if(editing)
	    	{
	    		editing=false;
	    	}
	    	else
	    	{
	    		editing=true;
	    	}
	    	ajaxResponseRenderer.addRender(rowZone);
	    }
	  
	    public String getCurrentRowZoneId()
	    {
	    	return "rowZone_"+person.getId();
	    }
	   public void onActionFromEditMode()
	   {
		   
	   }
}
