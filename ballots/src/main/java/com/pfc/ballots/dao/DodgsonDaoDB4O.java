package com.pfc.ballots.dao;


import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.DodgsonText;
import com.pfc.ballots.entities.ballotdata.Dodgson;
/**
 * 
 * Implementation of the interface DodgsonDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class DodgsonDaoDB4O implements DodgsonDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public DodgsonDaoDB4O(String DBName)
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
	 * Stores a dodgson entity
	 * @param dodgson entity to store
	 */
	public void store(Dodgson dodgson) {
		open();
		try
		{
			DB.store(dodgson);
			System.out.println("[DB4O]Dodgson stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Dodgson could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieves all Dodgson entities
	 * @return list<Dodgson> list of all Dodgson entities
	 */
	public List<Dodgson> retrieveAll() {
		List<Dodgson> dodgson=new LinkedList<Dodgson>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Dodgson.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					dodgson.add((Dodgson)result.next());
				}
				System.out.println("[DB4O]All Dodgson was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			dodgson.clear();
			return dodgson;
		}
		finally
		{
			close();
		}
		
		return dodgson;
	}

	/**
	 * Retrieves a Dodgson entity from its idBallot
	 * @param idBallot id of the ballot that use the Dodgson entity
	 * @retur Dodgson entity
	 */
	public Dodgson getByBallotId(String idBallot) {
		open();
		try
		{
			Dodgson dodgson=new Dodgson();
			dodgson.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(dodgson);
			if(result.hasNext())
			{
				return (Dodgson)result.next();
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
	 * Retrieves a Dodgson by its id
	 * @param id id of the Dodgson entity
	 * @return Dodgson
	 */
	public Dodgson getById(String id) {
		open();
		try
		{
			Dodgson dodgson=new Dodgson();
			dodgson.setId(id);
			ObjectSet result=DB.queryByExample(dodgson);
			if(result.hasNext())
			{
				return (Dodgson)result.next();
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
	 * Deletes all the Dodgson entities
	 */
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(Dodgson.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All Dodgson was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All Dodgson could not be deleted");
			
			}
			finally
			{
				close();
			}
		}


	/**
	 * Deteles a Dodgson from its ballotId
	 * @param ballotId id of the ballot that use the Dodgson entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Dodgson dodgson=new Dodgson();
			dodgson.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(dodgson);
			if(result.hasNext())
			{
				DB.delete((Dodgson)result.next());
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
	 * Deletes a Dodgson from its id
	 * @param id id of the Dodgson to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Dodgson dodgson=new Dodgson();
			dodgson.setId(id);
			ObjectSet result=DB.queryByExample(dodgson);
			if(result.hasNext())
			{
				DB.delete((Dodgson)result.next());
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
		 * Updates a Dodgson
		 * 
		 * @param updated Dodgson to update
		 */
		public void update(Dodgson updated)
		{
			open();
			try
			{
				Dodgson dodgson=new Dodgson();
				dodgson.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(dodgson);
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
		config.common().objectClass(Dodgson.class).cascadeOnUpdate(true);
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

	public DodgsonText getDodgsonText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(DodgsonText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (DodgsonText)result.next();
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

	public void deleteDodgsonText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(DodgsonText.class);
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
	public void updateDodgsonText(DodgsonText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(DodgsonText.class);
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
