package com.pfc.ballots.dao;

import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.RangeVotingText;
import com.pfc.ballots.entities.ballotdata.Borda;
import com.pfc.ballots.entities.ballotdata.RangeVoting;

public class RangeVotingDaoDB4O implements RangeVotingDao{

	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public RangeVotingDaoDB4O(String DBName)
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
	public void store(RangeVoting rangeVoting) {
		open();
		try
		{
			DB.store(rangeVoting);
			System.out.println("[DB4O]RangeVoting stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:RangeVoting could not be stored");
		}
		finally
		{
			close();
		}		
		
	}




    /**
     * Gets a rangeVoting entity from its idBallot
     * @param idBallot , id of the ballot to get
     * @return RangeVoting entity
     */
	public RangeVoting getByBallotId(String idBallot) {
		open();
		try
		{
			RangeVoting rangeVoting=new RangeVoting();
			rangeVoting.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(rangeVoting);
			if(result.hasNext())
			{
				return (RangeVoting)result.next();
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
     * Gets a RangeVoting entity from its id
     * @param id, id of the RangeVoting to get
     * @return RangeVoting entity
     */

	public RangeVoting getById(String id) {
		open();
		try
		{
			RangeVoting rangeVoting = new RangeVoting();
			rangeVoting.setId(id);
			ObjectSet result=DB.queryByExample(rangeVoting);
			if(result.hasNext())
			{
				return (RangeVoting)result.next();
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
	 * Deletes a RangeVoting entity from its ballotId
	 * @param ballotId , id of the ballot to delete its RangeVoting entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			RangeVoting rangeVoting=new RangeVoting();
			rangeVoting.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(rangeVoting);
			if(result.hasNext())
			{
				DB.delete((RangeVoting)result.next());
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
	 * Deletes a RangeVoting entity from its id
	 * @param id , id of the RangeVoting entity to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			RangeVoting rangeVoting=new RangeVoting();
			rangeVoting.setId(id);
			ObjectSet result=DB.queryByExample(rangeVoting);
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
	 * Deletes all the RangeVoting entities in the system
	 */
	public void deleteAll() {
		open();
		try
		{
				Query query=DB.query();
				query.constrain(RangeVoting.class);
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
	public List<RangeVoting> retrieveAll() {
		List<RangeVoting> rangeVoting=new LinkedList<RangeVoting>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(RangeVoting.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					rangeVoting.add((RangeVoting)result.next());
				}
				System.out.println("[DB4O]All Borda was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			rangeVoting.clear();
			return rangeVoting;
		}
		finally
		{
			close();
		}
		
		return rangeVoting;
	}




	/**
	 * Updates a RangeVoting entity
	 * @param updated, entity to update
	 */
	public void update(RangeVoting updated) {
		open();
		try
		{
			RangeVoting rangeVoting=new RangeVoting();
			rangeVoting.setId(updated.getId());
			
			ObjectSet result=DB.queryByExample(rangeVoting);
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
		config.common().objectClass(RangeVoting.class).cascadeOnUpdate(true);
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

	

	public RangeVotingText getRangeVotingText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(RangeVotingText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (RangeVotingText)result.next();
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

	public void deleteRangeVotingText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(RangeVotingText.class);
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
	 * Updates the BORDA text
	 * @param about
	 */
	public void updateRangeVotingText(RangeVotingText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(RangeVotingText.class);
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




}
