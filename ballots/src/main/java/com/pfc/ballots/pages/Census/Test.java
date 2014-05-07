package com.pfc.ballots.pages.Census;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.entities.Profile;

public class Test {


	@InjectComponent
	private Zone gridZone;
	
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao userDao=DB4O.getUsuarioDao();
	
	@Inject
	ComponentResources componentResources;
	
	@Persist
	private Map<String,Boolean> map;
	
	@Property
	private Profile person;
	
	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;
	
	private boolean add;
	
	public void setAdd(boolean add)
	{
		if(add)
		{
			if(map==null)
			{
				map=new HashMap<String,Boolean>();
			}
			map.put(person.getId(), new Boolean(true));
		}
		else
		{
			if(map!=null)
			{
				if(map.get(person.getId())!=null)
					{map.remove(person.getId());}
			}
		}
		
			
		/*if(add)
		{
			if(map==null)
				{map=new HashMap<String,Boolean>();}
			
			map.put(person.getId(), new Boolean(true));
		}*/
		this.add=add;
		
	}
	public boolean isAdd()
	{
		if(map!=null)
		{
			if(map.get(person.getId())!=null)
				{return true;}
			return false;
		}
		else
			return false;		
		
	}
	
	public List<Profile> getUsers()
	{
		
			
		List <Profile> list=userDao.RetrieveAllProfilesSortLastLog();
		if(map!=null)
			for(Profile temp:list)
			{	
				if(map.get(temp.getId())!=null)
					list.remove(temp);
			}
		return list;
	}
	
	public void onSuccess()
	{
		System.out.println("ON SUCCESS");
		ajaxResponseRenderer.addRender("gridZone", gridZone);
	}
	public void onFailure()
	{
		System.out.println("onFAILURE");
	}
	public void setupRender()
	{
		//map=null;
	}
	public void cleanupRender()
	{
		
		//componentResources.discardPersistentFieldChanges();
	}
}

