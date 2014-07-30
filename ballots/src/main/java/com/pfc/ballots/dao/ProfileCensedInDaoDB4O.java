package com.pfc.ballots.dao;

import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.pfc.ballots.entities.Profile;
import com.pfc.ballots.entities.ProfileCensedIn;

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
	private void close()
	{
		DB.close();
		System.out.println("[DB4O]Database was closed");
	}
}
