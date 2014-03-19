package com.pfc.ballots.pages.profile;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.util.ManipulateFiles;

public class filesrow {

	String sep=System.getProperty("file.separator");
	String path=System.getProperty("user.home")+sep+"BallotsFiles"+sep+"uploadfiles"+sep+"user.txt";
	
	@Property
	@Persist
	private List<Profile> persons;
	
	@Property
	private Profile person;
	
	
	@InjectComponent
	private Zone rowZone;
				 
	@Property
	@Persist
	private List<Profile> profiles;
	
	@Property
	private Profile profile;

    @Component
    private Form personForm;
	
	@Property
	private boolean editing;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	@Persist
	private String currentid;

	private enum Actions{
		EDIT,SAVE,CANCEL;
	}
	
	Actions action;
	
	public void onSelectedFromEdit()
	{
		action=Actions.EDIT;
	}
	public void onSelectedFromCancel()
	{
		action=Actions.CANCEL;
	}
	public void onSelectedFromSave()
	{
		action=Actions.SAVE;
	}
	
	public void onSuccessFromPersonform(String personid)
	{
		person=buscarperson(personid);
		if(action==Actions.EDIT)
		{
			editing=true;
			profile=new Profile(person);
			profiles.add(profile);
			ajaxResponseRenderer.addRender("rowZone_"+person.getId(),rowZone);

		}
		if(action==Actions.CANCEL)
		{
			editing=false;
			profile=buscarperson(personid,profiles);
			profiles.remove(profile);
			ajaxResponseRenderer.addRender("rowZone_"+person.getId(),rowZone);
		}
		if(action==Actions.SAVE)
		{
			editing=false;
			profile=buscarperson(personid,profiles);
			person=buscarperson(personid);
			person.copy(profile);
			profiles.remove(profile);
			
			ajaxResponseRenderer.addRender("rowZone_"+person.getId(),rowZone);
			
		}
		System.out.println("SUCCESS_"+personid);
	}
	public void onFailureFromPersonform(String personid)
	{
		System.out.println("FAILURE_"+personid);
		if(action==Actions.CANCEL)
		{
			editing=false;
			profile=buscarperson(personid,profiles);
			profiles.remove(profile);
			ajaxResponseRenderer.addRender("rowZone_"+person.getId(),rowZone);
		}
	}
	
	
	public void onPrepareForSubmitFromPersonForm(String personid)
	{
		person=buscarperson(personid);
		profile=buscarperson(personid,profiles);

	}
	public void onPrepareForRenderFromPersonForm(String personid)
	{
		person=buscarperson(personid);
	
	}
	
	public void onRowevent(String personId)
	{
		System.out.println("EVENT "+personId);
		if(currentid!=null)
		{
			person=buscarperson(currentid);
			editing=false;
			ajaxResponseRenderer.addRender("rowZone_"+person.getId(),rowZone);
		}
		editing=true;
		person=buscarperson(personId);
		currentid=personId;
		ajaxResponseRenderer.addRender("rowZone_"+person.getId(),rowZone);
	}
	
	public String getCurrentRowId()
	{
		return "rowZone_"+person.getId();
	}
	
 	void setupRender()
 	{	
 		currentid=null;
 		persons=ManipulateFiles.getProfilesFromFile(path);
 		profiles=new LinkedList<Profile>();
 	}

 	private Profile buscarperson(String id)
 	{
 		for(int i=0;i<persons.size();i++)
 		{
 			if(persons.get(i).getId().equals(id))
 			{
 				return persons.get(i);
 			}
 		}
 		System.out.println("reuturn null");
 		return null;
 	}
 	private Profile buscarperson(String id,List<Profile> list)
 	{
 		for(int i=0;i<list.size();i++)
 		{
 			if(list.get(i).getId().equals(id))
 			{
 				return list.get(i);
 			}
 		}
 		System.out.println("reuturn null");
 		return null;
 	}
}
