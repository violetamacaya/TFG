package com.pfc.ballots.util;

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
	String PATH=System.getProperty("user.home")+sep+"BallotsFiles"+sep+"DB4Obbdd.dat";;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	
	
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
	
	//********************************************Open and Close DB************************************//
	
	private void open()
	{
		
		config=Db4oEmbedded.newConfiguration();
		config.common().objectClass(Profile.class).cascadeOnUpdate(true);
		try
		{
			DB=Db4oEmbedded.openFile(config, PATH);
			System.out.println("[DB4O]Database was opened");
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
	

	
}
