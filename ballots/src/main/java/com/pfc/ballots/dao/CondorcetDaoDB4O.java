package com.pfc.ballots.dao;


import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.CondorcetText;
import com.pfc.ballots.entities.ballotdata.Condorcet;
/**
 * 
 * Implementation of the interface CondorcetDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class CondorcetDaoDB4O implements CondorcetDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;


	public CondorcetDaoDB4O(String DBName)
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
	//********************************************Open and Close DB************************************//
	/**
	 * Opens database
	 */
	private void open()
	{
		config=Db4oEmbedded.newConfiguration();
		config.common().objectClass(Condorcet.class).cascadeOnUpdate(true);
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

	public CondorcetText getCondorcetText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(CondorcetText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (CondorcetText)result.next();
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

	public void deleteCondorcetText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(CondorcetText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				DB.delete(result.next());
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
	 * Updates the CONDORCET text
	 * @param about
	 */
	public void updateCondorcetText(CondorcetText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(CondorcetText.class);
			ObjectSet result=query.execute();
			while(result.hasNext())
			{
				DB.delete(result.next());
			}
			DB.store(text);
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
	 * Stores a Condorcet entity
	 * @param condorcet, Condorcet entity to store
	 */
	public void store(Condorcet condorcet) {
		open();
		try
		{
			DB.store(condorcet);
			System.out.println("[DB4O]Condorcet stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Condorcet could not be stored");
		}
		finally
		{
			close();
		}		
		
	}




    /**
     * Gets a condorcet entity from its idBallot
     * @param idBallot , id of the ballot to get
     * @return Condorcet entity
     */
	public Condorcet getByBallotId(String idBallot) {
		open();
		try
		{
			Condorcet condorcet=new Condorcet();
			condorcet.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(condorcet);
			if(result.hasNext())
			{
				return (Condorcet)result.next();
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
     * Gets a condorcet entity from its id
     * @param id, id of the condorcet to get
     * @return Condorcet entity
     */

	public Condorcet getById(String id) {
		open();
		try
		{
			Condorcet condorcet = new Condorcet();
			condorcet.setId(id);
			ObjectSet result=DB.queryByExample(condorcet);
			if(result.hasNext())
			{
				return (Condorcet)result.next();
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
	 * Deletes a condorcet entity from its ballotId
	 * @param ballotId , id of the ballot to delete its condorcet entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Condorcet condorcet=new Condorcet();
			condorcet.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(condorcet);
			if(result.hasNext())
			{
				DB.delete((Condorcet)result.next());
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
	 * Deletes a condorcet entity from its id
	 * @param id , id of the condorcet entity to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Condorcet condorcet=new Condorcet();
			condorcet.setId(id);
			ObjectSet result=DB.queryByExample(condorcet);
			if(result.hasNext())
			{
				DB.delete((Condorcet)result.next());
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
	 * Deletes all the condorcet entities in the system
	 */
	public void deleteAll() {
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Condorcet.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					DB.delete(result.next());
				}
				System.out.println("[DB4O]All Condorcet was deleted");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All Condorcet could not be deleted");
		
		}
		finally
		{
			close();
		}
		
	}




	/**
	 * retrieves all Condorcet entities
	 */
	public List<Condorcet> retrieveAll() {
		List<Condorcet> condorcet=new LinkedList<Condorcet>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Condorcet.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					condorcet.add((Condorcet)result.next());
				}
				System.out.println("[DB4O]All Condorcet was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			condorcet.clear();
			return condorcet;
		}
		finally
		{
			close();
		}
		
		return condorcet;
	}




	/**
	 * Updates a condorcet entity
	 * @param updated, entity to update
	 */
	
	public void update(Condorcet updated) {
		open();
		try
		{
			Condorcet condorcet=new Condorcet();
			condorcet.setId(updated.getId());
			
			ObjectSet result=DB.queryByExample(condorcet);
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

}
