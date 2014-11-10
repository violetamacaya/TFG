package com.pfc.ballots.dao;

import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.AboutText;
import com.pfc.ballots.entities.ballotdata.Borda;
import com.pfc.ballots.entities.ballotdata.Kemeny;
import com.pfc.ballots.entities.ballotdata.RangeVoting;


public class BordaDaoDB4O implements BordaDao {

	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public BordaDaoDB4O(String DBName)
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
	 * Stores a Borda entity
	 * @param borda, Borda entity to store
	 */
	public void store(Borda borda) {
		open();
		try
		{
			DB.store(borda);
			System.out.println("[DB4O]Borda stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Borda could not be stored");
		}
		finally
		{
			close();
		}		
		
	}




    /**
     * Gets a borda entity from its idBallot
     * @param idBallot , id of the ballot to get
     * @return Borda entity
     */
	public Borda getByBallotId(String idBallot) {
		open();
		try
		{
			Borda borda=new Borda();
			borda.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(borda);
			if(result.hasNext())
			{
				return (Borda)result.next();
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
     * Gets a borda entity from its id
     * @param id, id of the borda to get
     * @return Borda entity
     */

	public Borda getById(String id) {
		open();
		try
		{
			Borda borda = new Borda();
			borda.setId(id);
			ObjectSet result=DB.queryByExample(borda);
			if(result.hasNext())
			{
				return (Borda)result.next();
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
	 * Deletes a borda entity from its ballotId
	 * @param ballotId , id of the ballot to delete its borda entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Borda borda=new Borda();
			borda.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(borda);
			if(result.hasNext())
			{
				DB.delete((Borda)result.next());
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
	 * Deletes a borda entity from its id
	 * @param id , id of the borda entity to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Borda borda=new Borda();
			borda.setId(id);
			ObjectSet result=DB.queryByExample(borda);
			if(result.hasNext())
			{
				DB.delete((Borda)result.next());
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
	 * Deletes all the borda entities in the system
	 */
	public void deleteAll() {
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Borda.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					DB.delete(result.next());
				}
				System.out.println("[DB4O]All Borda was deleted");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All Borda could not be deleted");
		
		}
		finally
		{
			close();
		}
		
	}




	/**
	 * retrieves all Borda entities
	 */
	public List<Borda> retrieveAll() {
		List<Borda> borda=new LinkedList<Borda>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Borda.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					borda.add((Borda)result.next());
				}
				System.out.println("[DB4O]All Borda was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			borda.clear();
			return borda;
		}
		finally
		{
			close();
		}
		
		return borda;
	}




	/**
	 * Updates a borda entity
	 * @param updated, entity to update
	 */
	public void update(Borda updated) {
		open();
		try
		{
			Borda borda=new Borda();
			borda.setId(updated.getId());
			
			ObjectSet result=DB.queryByExample(borda);
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
		config.common().objectClass(Borda.class).cascadeOnUpdate(true);
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

	//*******************************************Get actual description of the method******************/
	public Borda getBordaText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(Borda.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (Borda)result.next();
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




}
