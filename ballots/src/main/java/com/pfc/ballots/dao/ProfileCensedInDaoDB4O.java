package com.pfc.ballots.dao;

import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.entities.ProfileCensedIn;


/**
 * 
 * Implementation of the interface ProfileCensedInDao for the DB4O database
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUN-2014
 *
 */
public class ProfileCensedInDaoDB4O implements ProfileCensedInDao{

	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public ProfileCensedInDaoDB4O(String DBName)
	{		
		if(DBName==null)
		{
			PATH=ruta+"DB4Obbdd.dat";
		}
		else
		{
			PATH=ruta+DBName;
		}
	}
	
	///////////////////////////////////////////////// STORE ////////////////////////////////////////////////////////////
	/**
	 * Stores a ProfileCensedIn
	 * @param censedIn entity to store
	 */
 	public void store(ProfileCensedIn censedIn)
 	{
 		open();
 		try
 		{
 			DB.store(censedIn);
 			System.out.println("[DB4O]CensedIn stored");
 		}
 		catch(Exception e)
 		{
 			e.printStackTrace();
 			System.out.println("[DB4O]ERROR: CensedIn could not be stored");
 		}
 		finally
 		{
 			close();
 		}
 	}
 	//////////////////////////////////////////////////// GETTER //////////////////////////////////////////////////////////////
 	/**
 	 * Retrieves a ProfileCensedIn from its idProfile
 	 * @param idProfile id of the profile that use the entity
 	 * @return ProfileCensedIn
 	 */
 	public ProfileCensedIn getProfileCensedIn(String idProfile)
 	{
 		open();
		try
		{
			ObjectSet result=DB.queryByExample(new ProfileCensedIn(idProfile));
			if(result.hasNext())
			{
				System.out.println("[DB4O]CensedIn retrieved");
				return (ProfileCensedIn)result.next();	
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]Error: id could not be added");
		}
		finally
		{
			close();
		}
		return null;
 	}
	//////////////////////////////////////////////////// UPDATE ////////////////////////////////////////////////////////////
 	/**
 	 * Updates a ProfileCensedIn
 	 * @param updated ProfileCensedIn to update
 	 */
 	public void update(ProfileCensedIn updated)
 	{
 		open();
		try
		{
			ObjectSet result=DB.queryByExample(new ProfileCensedIn(updated.getIdProfile()));
			if(result.hasNext())
			{
				DB.delete(result.next());
				DB.store(updated);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]Error: id could not be updateed");
		}
		finally
		{
			close();
		}
 	}
 	
 	/**
 	 * Adds an id to a ProfileCensusIn 
 	 * @param idProfile id of the profile that use the entity
 	 * @param idCensus id of the census to add
 	 */
	public void addIdCensus(String idProfile,String idCensus)
	{
		open();
		try
		{
			ObjectSet result=DB.queryByExample(new ProfileCensedIn(idProfile));
			if(result.hasNext())
			{
				ProfileCensedIn temp=(ProfileCensedIn)result.next();
				ProfileCensedIn updated=new ProfileCensedIn(temp);
				updated.addIdCensus(idCensus);
				DB.delete(temp);
				DB.store(updated);
			}
 			System.out.println("[DB4O]CensedIn updated");

		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]Error: id could not be added");
		}
		finally
		{
			close();
		}
	}
	/**
 	 * Adds a list of ids to a ProfileCensusIn 
 	 * @param idProfile list of ids of the profiles that use the entities
 	 * @param idCensus id of the census to add
 	 */
	public void addIdCensus(List<String> idProfile,String idCensus)
	{
		if(idProfile!=null)
		{
			open();
			try
			{
				for(String current:idProfile)
				{
					ProfileCensedIn temp=getById(current);
					ProfileCensedIn updated=new ProfileCensedIn(temp);
					updated.addIdCensus(idCensus);
					DB.delete(temp);
					DB.store(updated);
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
	}
	/**
	 * Removes the id of a census of a ProfileCensedIn
	 * @param idProfile id of the profile that use the entity
 	 * @param idCensus id of the census to remove
 	 */

	public void removeIdCensus(String idProfile,String idCensus)
	{
		open();
		try
		{
			ObjectSet result=DB.queryByExample(new ProfileCensedIn(idProfile));
			if(result.hasNext())
			{
				ProfileCensedIn temp=getById(idProfile);
				ProfileCensedIn updated=new ProfileCensedIn(temp);
				updated.removeIdCensus(idCensus);
				DB.delete(temp);
				DB.store(updated);
			}
 			System.out.println("[DB4O]CensedIn updated");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]Error: id could not be added");
		}
		finally
		{
			close();
		}
		
	}
	/**
 	 * Removes a list of ids to a ProfileCensusIn 
 	 * @param idProfile list of ids of the profiles that use the entities
 	 * @param idCensus id of the census to remove
 	 */
	public void removeIdCensus(List<String> idProfile,String idCensus)
	{
		if(idProfile!=null)
		{
			open();
			try
			{
				for(String current:idProfile)
				{
					ProfileCensedIn temp=getById(current);
					ProfileCensedIn updated=new ProfileCensedIn(temp);
					updated.removeIdCensus(idCensus);
					DB.delete(temp);
					DB.store(updated);
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
	}
	/**
	 * Adds and removes ids in a ProfileCensedIn
	 * @param idProfile id of the profile that use the entity
	 * @param added ids to add
	 * @param removed ids to remove
	 */
	public void addAndRemoveIds(String idProfile,List<String>added,List<String> removed)
	{
		open();
		try
		{
			ObjectSet result=DB.queryByExample(new ProfileCensedIn(idProfile));
			if(result.hasNext())
			{
				ProfileCensedIn temp=(ProfileCensedIn)result.next();
				ProfileCensedIn updated=new ProfileCensedIn(temp);
				updated.addIdCensus(added);
				updated.removeIdCensus(removed);
				DB.delete(temp);
				DB.store(updated);
			}
 			System.out.println("[DB4O]CensedIn updated");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]Error: id could not be added");
		}
		finally
		{
			close();
		}
	}
	/////////////////////////////////////////////////// DELETE /////////////////////////////////////////
	/**
	 * Deletes a ProfileCensedIn
	 * @param idProfile id of the profile that use the entity
	 */
	public void delete(String idProfile)
	{
		open();
		try
		{
			ObjectSet result=DB.queryByExample(new ProfileCensedIn(idProfile));
			if(result.hasNext())
			{
				DB.delete(result.next());	
			}
 			System.out.println("[DB4O]CensedIn updated");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]Error: id could not be added");
		}
		finally
		{
			close();
		}
	}
	
	//////////////////////////////////////////// UTIL NO OPEN-CLOSE ////////////////////////////////////
	/**
	 * Retrieves a ProfileCensed in from its idProfile
	 * @param idProfile id of the profile that use the entity
	 * @return ProfileCensedIn
	 */
	private ProfileCensedIn getById(String idProfile)
	{
		ObjectSet result=DB.queryByExample(new ProfileCensedIn(idProfile));
		if(result.hasNext())
		{
			System.out.println("[DB4O]CensedIn retrieved");
			return (ProfileCensedIn)result.next();	
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
		config.common().objectClass(ProfileCensedIn.class).cascadeOnUpdate(true);
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
