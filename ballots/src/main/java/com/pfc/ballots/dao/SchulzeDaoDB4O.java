package com.pfc.ballots.dao;


import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.SchulzeText;
import com.pfc.ballots.entities.ballotdata.Schulze;
/**
 * 
 * Implementation of the interface SchulzeDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class SchulzeDaoDB4O implements SchulzeDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public SchulzeDaoDB4O(String DBName)
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
	 * Stores a schulze entity
	 * @param schulze entity to store
	 */
	public void store(Schulze schulze) {
		open();
		try
		{
			DB.store(schulze);
			System.out.println("[DB4O]Schulze stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Schulze could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieves all Schulze entities
	 * @return list<Schulze> list of all Schulze entities
	 */
	public List<Schulze> retrieveAll() {
		List<Schulze> schulze=new LinkedList<Schulze>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Schulze.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					schulze.add((Schulze)result.next());
				}
				System.out.println("[DB4O]All Schulze was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			schulze.clear();
			return schulze;
		}
		finally
		{
			close();
		}
		
		return schulze;
	}

	/**
	 * Retrieves a Schulze entity from its idBallot
	 * @param idBallot id of the ballot that use the Schulze entity
	 * @retur Schulze entity
	 */
	public Schulze getByBallotId(String idBallot) {
		open();
		try
		{
			Schulze schulze=new Schulze();
			schulze.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(schulze);
			if(result.hasNext())
			{
				return (Schulze)result.next();
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
	 * Retrieves a Schulze by its id
	 * @param id id of the Schulze entity
	 * @return Schulze
	 */
	public Schulze getById(String id) {
		open();
		try
		{
			Schulze schulze=new Schulze();
			schulze.setId(id);
			ObjectSet result=DB.queryByExample(schulze);
			if(result.hasNext())
			{
				return (Schulze)result.next();
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
	 * Deletes all the Schulze entities
	 */
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(Schulze.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All Schulze was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All Schulze could not be deleted");
			
			}
			finally
			{
				close();
			}
		}


	/**
	 * Deteles a Schulze from its ballotId
	 * @param ballotId id of the ballot that use the Schulze entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Schulze schulze=new Schulze();
			schulze.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(schulze);
			if(result.hasNext())
			{
				DB.delete((Schulze)result.next());
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
	 * Deletes a Schulze from its id
	 * @param id id of the Schulze to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Schulze schulze=new Schulze();
			schulze.setId(id);
			ObjectSet result=DB.queryByExample(schulze);
			if(result.hasNext())
			{
				DB.delete((Schulze)result.next());
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
		 * Updates a Schulze
		 * 
		 * @param updated Schulze to update
		 */
		public void update(Schulze updated)
		{
			open();
			try
			{
				Schulze schulze=new Schulze();
				schulze.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(schulze);
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
		config.common().objectClass(Schulze.class).cascadeOnUpdate(true);
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

	public SchulzeText getSchulzeText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(SchulzeText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (SchulzeText)result.next();
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
	 * Deletes Schulze text
	 */
	public void deleteSchulzeText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(SchulzeText.class);
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
	 * Updates the Schulze text
	 * @param about
	 */
	public void updateSchulzeText(SchulzeText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(SchulzeText.class);
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
