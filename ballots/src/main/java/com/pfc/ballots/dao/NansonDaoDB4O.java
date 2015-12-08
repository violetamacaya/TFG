package com.pfc.ballots.dao;


import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.NansonText;
import com.pfc.ballots.entities.ballotdata.Nanson;
/**
 * 
 * Implementation of the interface NansonDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class NansonDaoDB4O implements NansonDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public NansonDaoDB4O(String DBName)
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
	 * Stores a nanson entity
	 * @param nanson entity to store
	 */
	public void store(Nanson nanson) {
		open();
		try
		{
			DB.store(nanson);
			System.out.println("[DB4O]Nanson stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Nanson could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieves all Nanson entities
	 * @return list<Nanson> list of all Nanson entities
	 */
	public List<Nanson> retrieveAll() {
		List<Nanson> nanson=new LinkedList<Nanson>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Nanson.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					nanson.add((Nanson)result.next());
				}
				System.out.println("[DB4O]All Nanson was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			nanson.clear();
			return nanson;
		}
		finally
		{
			close();
		}
		
		return nanson;
	}

	/**
	 * Retrieves a Nanson entity from its idBallot
	 * @param idBallot id of the ballot that use the Nanson entity
	 * @retur Nanson entity
	 */
	public Nanson getByBallotId(String idBallot) {
		open();
		try
		{
			Nanson nanson=new Nanson();
			nanson.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(nanson);
			if(result.hasNext())
			{
				return (Nanson)result.next();
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
	 * Retrieves a Nanson by its id
	 * @param id id of the Nanson entity
	 * @return Nanson
	 */
	public Nanson getById(String id) {
		open();
		try
		{
			Nanson nanson=new Nanson();
			nanson.setId(id);
			ObjectSet result=DB.queryByExample(nanson);
			if(result.hasNext())
			{
				return (Nanson)result.next();
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
	 * Deletes all the Nanson entities
	 */
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(Nanson.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All Nanson was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All Nanson could not be deleted");
			
			}
			finally
			{
				close();
			}
		}


	/**
	 * Deteles a Nanson from its ballotId
	 * @param ballotId id of the ballot that use the Nanson entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Nanson nanson=new Nanson();
			nanson.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(nanson);
			if(result.hasNext())
			{
				DB.delete((Nanson)result.next());
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
	 * Deletes a Nanson from its id
	 * @param id id of the Nanson to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Nanson nanson=new Nanson();
			nanson.setId(id);
			ObjectSet result=DB.queryByExample(nanson);
			if(result.hasNext())
			{
				DB.delete((Nanson)result.next());
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
		 * Updates a Nanson
		 * 
		 * @param updated Nanson to update
		 */
		public void update(Nanson updated)
		{
			open();
			try
			{
				Nanson nanson=new Nanson();
				nanson.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(nanson);
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
		config.common().objectClass(Nanson.class).cascadeOnUpdate(true);
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

	public NansonText getNansonText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(NansonText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (NansonText)result.next();
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

	public void deleteNansonText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(NansonText.class);
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
	public void updateNansonText(NansonText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(NansonText.class);
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
