package com.pfc.ballots.pages.profile;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.upload.services.UploadedFile;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;
import com.pfc.ballots.util.Encryption;
import com.pfc.ballots.util.ManipulateFiles;
import com.pfc.ballots.util.UUID;



public class ProfileByFile {
	 	
	String sep=System.getProperty("file.separator");
	String path=System.getProperty("user.home")+sep+"BallotsFiles"+sep+"uploadfiles"+sep;
	
	@SessionState
	private DataSession datasession;
	
	@Persist
	String access;
	@Persist
	String lastaccess;
	
	@Persist
	String finalpath;
	
	@Property
	@Persist
	private boolean fileupload;
	@Persist
	private boolean fileLoaded;
	@Property
	@Persist
	private boolean editing;
	@Persist
	private boolean newuser;
	
	@Property
	@Persist
	private List<Profile> persons;
	
	private List<Profile> added;
	
	@Property
	private Profile person;
	
	@Persist
	@Property
	private Profile profile;
	
	
	
	@Persist
	private String currentId;
	
	@Property
	private UploadedFile file;
	
	@InjectComponent
	private Zone gridZone;
	@InjectComponent
	private Zone editZone;
	@InjectComponent
	private Form editForm;
	
	
	@Inject
	private Request request;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	@Inject
    private ComponentResources componentResources;
	
	private enum Actions{
		SAVE,CANCEL
	};
	
	
	
	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao userDao =null;
	
	
	
	private Actions action;
	
	
	void setupRender()
	{
		
		if(!(lastaccess==null && access==null))
		{
			if(lastaccess==null && access!=null)
			{
				fileupload=false;
				fileLoaded=false;
				lastaccess=access;
			}
			else if(lastaccess!=null && access==null)
			{
				fileupload=false;
				fileLoaded=false;
				lastaccess=access;
			}
			else if(!lastaccess.equals(access))
			{
				fileupload=false;
				fileLoaded=false;
				lastaccess=access;
			}
		}
			
		if(fileupload)
		{
			newuser=false;
			editing=false;
			if(!fileLoaded)
			{
				fileLoaded=true;
				persons=ManipulateFiles.getProfilesFromFile(finalpath);
			}
		}
		else
		{
			currentId=null;
		}
		
	}
	
	public Object onActivate()
	{
		switch(datasession.sessionState())
		{
			case 0:
				System.out.println("LOGEADO");
				if(datasession.isAdmin())
				{
					return null;
				}
				return UnauthorizedAttempt.class;
			case 1:
				System.out.println("NO LOGEADO");
				return Index.class;
			case 2:
				System.out.println("SESION EXPIRADA");
				return SessionExpired.class;
			default:
				return Index.class;
		}
	}
	
	
	public void setup(String dname){
		System.out.println("NAME->"+dname);
		access=dname;
	}
	
	public void onSuccessFromUploadForm()
	{
		System.out.println("SUCCESSS");
		String[] namef=file.getFileName().split("\\.");
		finalpath=path+"user."+namef[namef.length-1];
		File copied= new File(finalpath);
		fileupload=true;
		fileLoaded=false;
		file.write(copied);
	}
	
	public void onActionFromEditBut(String id)
	{
		
		System.out.println("EDIT->"+id);
		currentId=id;
		person=lookforid(id);
		profile=new Profile(person);
		editing=true;
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender(editZone).addRender(gridZone);
		}
	}
	public Object onActionFromEndBut()
	{
		fileupload=false;
		ManipulateFiles.deletefile(finalpath);
		persons=null;
		fileLoaded=false;
		componentResources.discardPersistentFieldChanges();
		return Index.class;
		
	}

	public void onActionFromAddRowBut()
	{
		profile=new Profile();
		profile.setId(nextId());
		newuser=true;
		editing=true;
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender(editZone).addRender(gridZone);
		}
		
	}
	
	public void onSelectedFromSave()
	{
		action=Actions.SAVE;
	}
	public void onSelectedFromCancel()
	{
		action=Actions.CANCEL;
	}
	
	public void onSuccessFromEditForm()
	{
		System.out.println("SUCCESS");
		if(action==Actions.SAVE)
		{
			if(newuser)
			{
				persons.add(profile);
			}
			else
			{
				person=lookforid(currentId);
				person.copy(profile);
			}
		}
		editing=false;
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender(editZone).addRender(gridZone);
		}
	}
	public void onFailureFromEditForm()
	{
		if(action==Actions.CANCEL)
		{
			editing=false;
			newuser=false;
			if(request.isXHR())
			{
				ajaxResponseRenderer.addRender(editZone).addRender(gridZone);
			}
		}
		editForm.clearErrors();
	}
	public boolean isAvalible()
	{
		userDao=DB4O.getUsuarioDao(access);
		if(person.getEmail()==null)
			return false;
		return !(userDao.isProfileRegistred(person.getEmail().toLowerCase()));
		
	}
	public boolean isComplete()
	{
		if(person.getEmail()==null || person.getPassword()==null || person.getFirstName()==null || person.getLastName()==null)
		{
			return false;
		}
		if(person.getEmail().trim().length()==0 || person.getPassword().trim().length()<=5 || person.getFirstName().trim().length()==0 || person.getLastName().trim().length()==0)
		{
			return false;
		} 
		 Pattern pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	     Matcher mat = pat.matcher(person.getEmail());
	     if(mat.find())
	     {
	    	 return true;
	     }
	     return false;
		
	}
	public boolean isOkemail()
	{
		if(person.getEmail()==null)
			return false;
		Pattern pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	     Matcher mat = pat.matcher(person.getEmail());
	     if(mat.find())
	     {
	    	 return true;
	     }
	     return false;
		
	}
	public boolean isOkpass()
	{
		if(person.getPassword()==null)
			return false;
		if(person.getPassword().trim().length()<=5)
		{
			return false;
		}
		return true;
	}
	public boolean isOkfirstname()
	{
		if(person.getFirstName()==null)
			return false;
		if(person.getFirstName().trim().length()==0)
			return false;
					
		return true;
	}
	public boolean isOklastname()
	{
		if(person.getLastName()==null)
			return false;
		if(person.getLastName().trim().length()==0)
			return false;
		
		return true;
	}
	public void onActionFromAddavaliblesbut()
	{
		userDao=DB4O.getUsuarioDao(access);
		added=new LinkedList<Profile>();
		for(int i=0;i<persons.size();i++)
		{
			System.out.println("ITERACION-->"+i);
			person=persons.get(i);
			if(isAvalible())
			{
				Profile temp=new Profile(person);
				added.add(person);
				temp.setId(UUID.generate());
				temp.setRegDatetoActual();
				
				temp.setPassword(Encryption.getStringMessageDigest(person.getPassword(), Encryption.SHA1));
				temp.setPlain(person.getPassword());
				userDao.store(temp);
			}
			else
			{
				System.out.println(person.getId()+"->NO");
			}
		}
		for(int i=0;i<added.size();i++)
		{
			persons.remove(added.get(i));
		}
		added.clear();
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender(editZone).addRender(gridZone);
		}
	}
	public void onActionFromAddbut(String id)
	{
		userDao=DB4O.getUsuarioDao(access);
		System.out.println("ADD ID->"+id);
		person=lookforid(id);
		
		
		Profile temp=new Profile(person);
		persons.remove(person);
		temp.setId(UUID.generate());
		temp.setRegDatetoActual();
		
		temp.setPassword(Encryption.getStringMessageDigest(person.getPassword(), Encryption.SHA1));
		temp.setPlain(person.getPassword());
		userDao.store(temp);
		if(request.isXHR())
		{
			ajaxResponseRenderer.addRender(editZone).addRender(gridZone);
		}
	}

	
	
	private Profile lookforid(String id)
	{
		for(int i=0;i<=persons.size();i++)
		{
			if(persons.get(i).getId().equals(id))
			{
				return persons.get(i);
			}
		}
		return null;
		
	}
	private String nextId()
	{
		Integer temp;
		temp =persons.size();
		return temp.toString();
	}
	
}
