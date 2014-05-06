package com.pfc.ballots.dao;

import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.Profile;


public class UserDaoDB4O implements UserDao{

	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public UserDaoDB4O(String DBName)
	{
		if(DBName==null)
		{
			PATH=ruta+"DB4Obbdd.dat";
		}
		else
		{
			PATH=ruta+DBName;
		}
		System.out.println(ruta);
	}
				   
	
	//*************************************************Store******************************************************
	public void store(Profile profile)
	{
		open();
		try
		{
			if(!testEmail(profile.getEmail()))
			{
				DB.store(profile);
				System.out.println("[DB4O]Profile was Stored");
			}
			else
			{
				System.out.println("[DB4O]Warning:Email was already in use");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Profile could not be stored");
		}
		finally
		{
			close();
		}
		
	}
	public void store(List<Profile> profiles)
	{
		open();
		try
		{
			for(int i=0;i<profiles.size();i++)
			{
				if(!testEmail(profiles.get(i).getEmail()))
				{
					DB.store(profiles.get(i));
					System.out.println("[DB4O]Profile was stored");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Profile could not be stored");
		}
		finally
		{
			close();
		}
	}
	
	
	//******************************************************Retrievers*********************************//
	
	@SuppressWarnings("rawtypes")
	public List<Profile> getByExample(Profile example)
	{
		List<Profile> list=new ArrayList<Profile>();
		open();
		try
		{
			ObjectSet result=DB.queryByExample(example);
			while(result.hasNext())
			{
				list.add((Profile)result.next());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			list.clear();
		}
		finally
		{
			close();
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public Profile getProfileByEmail(String Email)
	{
		open();
		try
		{
			ObjectSet result=DB.queryByExample(new Profile(Email));
			
			if(result.hasNext())
			{
				System.out.println("[DB4O]Profile was retrieved");
				return (Profile) result.next();
			}
			else
			{
				System.out.println("[DB4O]Profile does't exist in Database");
				return null;
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close();
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public Profile getProfileById(String Id){

		Profile temp=new Profile();
		temp.setId(Id);
		open();
		try
		{
			
			ObjectSet result=DB.queryByExample(temp);
			
			if(result.hasNext())
			{
				System.out.println("[DB4O]Profile was retrieved");
				return (Profile) result.next();
			}
			else
			{
				System.out.println("[DB4O]Profile does't exist in Database");
				return null;
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close();
		}
		return null;	
	}
	
	@SuppressWarnings("rawtypes")
	public String getEmailById(String Id)
	{
		
		Profile temp=new Profile();
		temp.setId(Id);
		open();
		try
		{
			
			ObjectSet result=DB.queryByExample(temp);
			
			if(result.hasNext())
			{
				System.out.println("[DB4O]Profile was retrieved");
				return ((Profile) result.next()).getEmail();
			}
			else
			{
				System.out.println("[DB4O]Profile does't exist in Database");
				return null;
			}
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close();
		}
		return null;	
	}
	
	//*******************************************Retrieves all****************************************//
	
	@SuppressWarnings("rawtypes")
	public List<Profile> RetrieveAllProfiles() {
		
		List<Profile> profiles=new ArrayList<Profile>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Profile.class);
				ObjectSet resultado = query.execute();
				
				while(resultado.hasNext())
				{
					profiles.add((Profile)resultado.next());
				}
				System.out.println("[DB4O]All profiles was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All profiles could not be retrieved");
			profiles.clear();
			return profiles;
		}
		finally
		{
			close();
		}
		
		return profiles;
	}
	@SuppressWarnings("rawtypes")
	public List<Profile> RetrieveAllProfilesSortLastLog() {
		List<Profile> profiles=new ArrayList<Profile>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Profile.class);
				query.descend("lastLog").orderDescending();
				ObjectSet resultado = query.execute();
				
				while(resultado.hasNext())
				{
					profiles.add((Profile)resultado.next());
				}
				System.out.println("[DB4O]All profiles was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All profiles could not be retrieved");
			profiles.clear();
			return profiles;
		}
		finally
		{
			close();
		}
		
		return profiles;
	}
	

	
	
	//***************************************************IsRegistred************************************************//
	
	public boolean isProfileRegistred(String Email)
	{
		boolean temp=true;
		open();
		try
		{
			temp=testEmail(Email);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR: Could not look for Profile");
		}
		finally
		{
			close();
		}
		return temp;
	}
	
	//*********************************************** Updates ************************************************************//
	
	public void UpdateByEmail(String Email, Profile updatedProfile) {
		
		Profile temp=null;
		
		open();
		try
		{
			temp=getByEmail(Email);
			DB.delete(temp);
			DB.store(updatedProfile);
			System.out.println("[DB4O]Profile was updated");

		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Profile could not be updated");
		}finally
		{
			close();
		}
	}

	public void UpdateByEmail(Profile updatedProfile) {
		
		Profile temp=null;
		
		open();
		try
		{
			temp=getByEmail(updatedProfile.getEmail());
			DB.delete(temp);
			DB.store(updatedProfile);
			System.out.println("[DB4O]Profile was updated");

		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Profile could not be updated");
		}finally
		{
			close();
		}
	}
	public void UpdateById(Profile updatedProfile)
	{
		Profile temp=null;
		open();
		try
		{
			temp=getById(updatedProfile.getId());
			DB.delete(temp);
			DB.store(updatedProfile);
			
					
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			close();
		}
		
	}
	
	//**********************************************   Delete    **************************************************//
	public void deleteByEmail(String Email)
	{
		Profile temp=null;
		open();
		try
		{
			temp=getByEmail(Email);
			DB.delete(temp);
			System.out.println("[DB4O]Profile was erased");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]Profile could not be erased");
		}
		finally
		{
			close();
		}
		
	}
	
	
	
	//************************************************Util(without open or close)***********************************//
	
	@SuppressWarnings("rawtypes")
	private boolean testEmail(String Email)
	{
		
		ObjectSet result = DB.queryByExample(new Profile(Email));
		if(result.hasNext())
		{
			return true;
		}
		return false;
		
	}
	@SuppressWarnings("rawtypes")
	private Profile getById(String id)
	{
		Profile temp=new Profile();
		temp.setId(id);
		ObjectSet result = DB.queryByExample(temp);
		if(result.hasNext())
		{
			return (Profile)result.next();
		}
		
		return null;
	}
	@SuppressWarnings("rawtypes")
	private Profile getByEmail(String Email)
	{
		ObjectSet result = DB.queryByExample(new Profile(Email));
		if(result.hasNext())
		{
			return (Profile)result.next();
		}
		return null;
	}
	

	//********************************************Open and Close DB************************************//
	
	private void open()
	{
		config=Db4oEmbedded.newConfiguration();
		config.common().objectClass(Profile.class).cascadeOnUpdate(true);
		try
		{
			
			DB=Db4oEmbedded.openFile(config, PATH);
			System.out.println("[DB4O]Database was open");
			
		}
		catch(Exception e)
		{
			System.out.println("[DB4O]ERROR:Database could not be open");
			e.printStackTrace();
		}
	}
	private void close()
	{
		DB.close();
		System.out.println("[DB4O]Database was closed");
	}
	
	
}
