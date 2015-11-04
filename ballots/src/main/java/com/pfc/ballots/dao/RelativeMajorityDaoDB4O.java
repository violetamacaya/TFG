package com.pfc.ballots.dao;

import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.entities.ballotdata.RelativeMajority;

/**
 * 
 * Implementation of the interface RelativeMajorityDao for the DB4O database
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUL-2014
 *
 */

public class RelativeMajorityDaoDB4O implements RelativeMajorityDao{

	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	//SOLO PUEDE EXISITIR UNA ENTIDAD DE DATOS (RELATIVEMAJORITY) POR CADA VOTACION
	
	public RelativeMajorityDaoDB4O(String DBName)
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
	//********************************************* Store *********************************************//
	/**
	 * Stores a RelativeMajority
	 * @param relativeMajority to store
	 */
	public void store(RelativeMajority relativeMajority)
	{
		open();
		try
		{
			DB.store(relativeMajority);
			System.out.println("[DB4O]Vote stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Vote could not be stored");
		}
		finally
		{
			close();
		}
	}
	
	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieve all the RelativeMajority entities
	 * @return List<RelativeMajority>
	 */
	public List<RelativeMajority> retrieveAll()
	{
		List<RelativeMajority> relMays=new LinkedList<RelativeMajority>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(RelativeMajority.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					relMays.add((RelativeMajority)result.next());
				}
				System.out.println("[DB4O]All RelMay was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			relMays.clear();
			return relMays;
		}
		finally
		{
			close();
		}
		
		return relMays;
	}
	/**
	 * Retrieves a RelativeMajority from its idBallot
	 * @param idBallot id of the ballot that use the entity
	 * @return RelativeMajority
	 */
	public RelativeMajority getByBallotId(String idBallot)
	{
		open();
		try
		{
			RelativeMajority relativeMajority=new RelativeMajority();
			relativeMajority.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(relativeMajority);
			if(result.hasNext())
			{
				return (RelativeMajority)result.next();
			}
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			close();
		}
		
	}
	/**
	 * Retrieves a RelativaMajority from its id
	 * @param id id of the RelativeMajority
	 * @return RelativeMajority
	 */
	public RelativeMajority getById(String id)
	{
		open();
		try
		{
			RelativeMajority relativeMajority=new RelativeMajority();
			relativeMajority.setId(id);
			ObjectSet result=DB.queryByExample(relativeMajority);
			if(result.hasNext())
			{
				return (RelativeMajority)result.next();
			}
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			close();
		}
	}
	//********************************************** DELETE *******************************************//
	/**
	 * Deletes all the RelativeMajority entities
	 */
	public void deleteAll()
	{
		open();
		try
		{
				Query query=DB.query();
				query.constrain(RelativeMajority.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					DB.delete(result.next());
				}
				System.out.println("[DB4O]All RelMay was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All relMays could not be deleted");
		
		}
		finally
		{
			close();
		}
	}
	/**
	 * Deletes a RelativeMajority from its ballotId
	 * @param BallotId id of the ballot that use the entity
	 */
	public void deleteByBallotId(String ballotId)
	{
		open();
		try
		{
			RelativeMajority relativeMajority=new RelativeMajority();
			relativeMajority.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(relativeMajority);
			if(result.hasNext())
			{
				DB.delete((RelativeMajority)result.next());
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
	/**
	 * Deletes a RelativeMajortiy from its id
	 * @param id if of the RelativeMajority to delete
	 */
	public void deleteById(String id)
	{
		open();
		try
		{
			RelativeMajority relativeMajority=new RelativeMajority();
			relativeMajority.setId(id);
			ObjectSet result=DB.queryByExample(relativeMajority);
			if(result.hasNext())
			{
				DB.delete((RelativeMajority)result.next());
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
	//********************************************** UPDATE *******************************************//
	/**
	 * Updates a RelativeMajority
	 * @param updated RelativeMajority to update
	 */
	public void update(RelativeMajority updated)
	{
		open();
		try
		{
			RelativeMajority relativeMajority=new RelativeMajority();
			relativeMajority.setId(updated.getId());
			
			ObjectSet result=DB.queryByExample(relativeMajority);
			if(result.hasNext())
			{
				DB.delete(result.next());
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
	
	//********************************************Open and Close DB************************************//
	/**
	 * Opens database
	 */
	private void open()
	{
		config=Db4oEmbedded.newConfiguration();
		config.common().objectClass(RelativeMajority.class).cascadeOnUpdate(true);
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
