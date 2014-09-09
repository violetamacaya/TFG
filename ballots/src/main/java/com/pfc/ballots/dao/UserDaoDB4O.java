package com.pfc.ballots.dao;

import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.Profile;

/**
 * 
 * Implementation of the interface UserDao for the DB4O database
 * 
 * @author Mario Temprano Martin
 * @version 2.0 FEB-2014
 *
 */

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
		//System.out.println(ruta);
	}
				   
	
	//*************************************************Store******************************************************
	/**
	 * Stores a profile
	 * @param profile profile to store
	 * 
	 */
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
	/**
	 * Stores a list of profiles
	 * @param profiles list of profiles to store
	 */
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
	/**
	 * Retrieves a list of profiles that match with the example
	 * @param example example for the search
	 * @return List<Profile>
	 */
	@SuppressWarnings("rawtypes")
	public List<Profile> getByExample(Profile example)
	{
		List<Profile> list=new LinkedList<Profile>();
		open();
		try
		{
			Query query =DB.query();
			query.constrain(Profile.class);
			if(example.getEmail()!=null)
				{query.descend("email").constrain(example.getEmail()).endsWith(false);}
			if(example.getId()!=null)
				{query.descend("id").constrain(example.getId()).endsWith(false);}
			if(example.isAdmin())
				{query.descend("admin").constrain(example.isAdmin());}
			if(example.isMaker())
				{query.descend("maker").constrain(example.isMaker());}
			if(example.getFirstName()!=null)
				{query.descend("firstName").constrain(example.getFirstName()).endsWith(false);}
			
			if(example.getLastName()!=null)
				{query.descend("lastName").constrain(example.getLastName()).endsWith(false);}

			if(example.getCity()!=null)
				{query.descend("city").constrain(example.getCity()).endsWith(false);}
			if(example.getCountry()!=null)
				{query.descend("country").constrain(example.getCountry()).endsWith(false);}
			
			
			ObjectSet result=query.execute();
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
	
	/**
	 * Retrieves a Profile from its email
	 * @param email of the profile
	 * @return Profile
	 */
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
	/**
	 * Retrieves a Profile from its id
	 * @param id of the Profile
	 * @return profile
	 */
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
	/**
	 * Retrieve a list of profiles from a list of its id
	 * @param id list of id of the profiles
	 * @return List<Profile>
	 */
	public List<Profile> getProfileById(List<String> id)
	{
		open();
		List<Profile> list=new LinkedList<Profile>();
		try
		{
			for(String current:id)
			{
				list.add(getById(current));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			return list;
		}
		finally
		{
			close();
		}
		return list;
	}
	
	/**
	 * Retrieves the email of a profile from its id
	 * @param id of the profile
	 * @return String 
	 */
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
	/**
	 * Retrieves an id of a profile from its email
	 * @param email of the profile
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public String getIdByEmail(String email)
	{
		
		open();
		try
		{
			
			ObjectSet result=DB.queryByExample(new Profile(email));
			
			if(result.hasNext())
			{
				System.out.println("[DB4O]Id Profile was retrieved");
				return ((Profile)result.next()).getId();
			}
			else
			{
				System.out.println("[DB4O]Id Profile does't exist in Database");
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
	/**
	 * Retrieves the profiles with "@nomail"
	 * @return List<Profile>
	 */
	public List<Profile> getNoMailProfiles()
	{
		List<Profile> all=RetrieveAllProfiles();
		List<Profile> nomail=new LinkedList<Profile>();
	
		for(Profile temp:all)
		{
			if(temp.getEmail().contains("@nomail"))
			{
				nomail.add(temp);
			}
		
		}
	
		
		return nomail;
	}
	
	//*******************************************Retrieves all****************************************//
	/**
	 * Retrieves all profiles in DB
	 * @return List<Profile>
	 */
	@SuppressWarnings("rawtypes")
	public List<Profile> RetrieveAllProfiles() {
		
		List<Profile> profiles=new LinkedList<Profile>();
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
	/**
	 * Retrieves all profiles sorted for last login
	 * @return List<Profile>
	 */
	@SuppressWarnings("rawtypes")
	public List<Profile> RetrieveAllProfilesSortLastLog() {
		List<Profile> profiles=new LinkedList<Profile>();
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
	
	//************************************************** get/set-Owner***************************************************//
	/**
	 * Retrieves the profile of the owner of the DB
	 * @return Profile
	 */
	public Profile getOwner()
	{

		open();
		try
		{
			Profile temp=new Profile();
			temp.setOwner(true);
			@SuppressWarnings("rawtypes")
			ObjectSet result=DB.queryByExample(temp);
			
			if(result.hasNext())
			{
				System.out.println("[DB4O]Id Profile was retrieved");
				return ((Profile)result.next());
			}
			else
			{
				System.out.println("[DB4O]There isn't owner Database");
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
	/**
	 * Set an user like owner of the database
	 * @param idNewOwner id of the new owner
	 */
	@SuppressWarnings("rawtypes")
	public void setOwner(String idNewOwner)
	{
		open();
		try
		{
			Profile temp=new Profile();
			temp.setOwner(true);
			
			ObjectSet result=DB.queryByExample(temp);
			if(result.hasNext())
			{
				Profile owner=(Profile)result.next();
				Profile newOwner=getById(idNewOwner);
				if(owner!=null && newOwner!=null)
				{
					DB.delete(owner);
					DB.delete(newOwner);
					owner.setOwner(false);
					newOwner.setOwner(true);
					newOwner.setAdmin(true);
					DB.store(owner);
					DB.store(newOwner);
				}
				
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
	}
	
	//***************************************************IsRegistred************************************************//
	/**
	 * Checks if an email is in use
	 * @param email to check
	 * @return boolean
	 */
	public boolean isProfileRegistred(String email)
	{
		boolean temp=true;
		open();
		try
		{
			temp=testEmail(email);
			
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
	/**
	 * Checks if a no mail is in use
	 * @param email to check
	 * @return boolean
	 */
	public boolean isNoMailRegistred(String email)
	{
		boolean isRegistred=true;
		if(email.contains("@nomail"))
		{
			open();
			try
			{
				isRegistred=testEmail(email);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				close();
			}
		}
		else
		{
			isRegistred=false;
		}
		return isRegistred;
	}
	
	//*********************************************** Updates ************************************************************//
	/**
	 * Updates a Profile from its email(BETER NOT USE)
	 * @param email of the profile to update
	 * @param updatedProfile profile to update
	 */
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
	
	/**
	 * Update a Profile from its email (BETTER NOT USE)
	 * @param updatedProfile profile to update
	 */
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
	/**
	 * Updates a profile
	 * @param updatedProfile profile to update
	 */
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
	/**
	 * Deletes a Profile from its email
	 * @param email of the profile to delete
	 */
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
	/**
	 * Deletes a Profile from its id
	 * @param id of the profile to delete
	 */
	public void deleteById(String id)
	{
		Profile temp=null;
		open();
		try
		{
			temp=getById(id);
			DB.delete(temp);	
					
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			close();
		}
		
	}
	
	
	//************************************************Util(without open or close)***********************************//
	/**
	 * Checks if an email is in use
	 * @param Email to check
	 * @return boolean
	 */
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
	/**
	 * Retrieves a Profile from its id
	 * @param id of the profile to retrieve
	 * @return
	 */
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
	/**
	 * Retrieves a Profile from its email
	 * @param Email of the profile to retrieve
	 * @return
	 */
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
	/**
	 * Opens database
	 */
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
	/**
	 * Closes database
	 */
	private void close()
	{
		DB.close();
		System.out.println("[DB4O]Database was closed");
	}
	
	
}
