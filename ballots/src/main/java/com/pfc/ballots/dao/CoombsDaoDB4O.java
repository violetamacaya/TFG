package com.pfc.ballots.dao;


import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.CoombsText;
import com.pfc.ballots.entities.ballotdata.Coombs;
/**
 * 
 * Implementation of the interface CoombsDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class CoombsDaoDB4O implements CoombsDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public CoombsDaoDB4O(String DBName)
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
	 * Stores a coombs entity
	 * @param coombs entity to store
	 */
	public void store(Coombs coombs) {
		open();
		try
		{
			DB.store(coombs);
			System.out.println("[DB4O]Coombs stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Coombs could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieves all Coombs entities
	 * @return list<Coombs> list of all Coombs entities
	 */
	public List<Coombs> retrieveAll() {
		List<Coombs> coombs=new LinkedList<Coombs>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Coombs.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					coombs.add((Coombs)result.next());
				}
				System.out.println("[DB4O]All Coombs was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			coombs.clear();
			return coombs;
		}
		finally
		{
			close();
		}
		
		return coombs;
	}

	/**
	 * Retrieves a Coombs entity from its idBallot
	 * @param idBallot id of the ballot that use the Coombs entity
	 * @retur Coombs entity
	 */
	public Coombs getByBallotId(String idBallot) {
		open();
		try
		{
			Coombs coombs=new Coombs();
			coombs.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(coombs);
			if(result.hasNext())
			{
				return (Coombs)result.next();
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
	 * Retrieves a Coombs by its id
	 * @param id id of the Coombs entity
	 * @return Coombs
	 */
	public Coombs getById(String id) {
		open();
		try
		{
			Coombs coombs=new Coombs();
			coombs.setId(id);
			ObjectSet result=DB.queryByExample(coombs);
			if(result.hasNext())
			{
				return (Coombs)result.next();
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
	 * Deletes all the Coombs entities
	 */
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(Coombs.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All Coombs was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All Coombs could not be deleted");
			
			}
			finally
			{
				close();
			}
		}


	/**
	 * Deteles a Coombs from its ballotId
	 * @param ballotId id of the ballot that use the Coombs entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Coombs coombs=new Coombs();
			coombs.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(coombs);
			if(result.hasNext())
			{
				DB.delete((Coombs)result.next());
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
	 * Deletes a Coombs from its id
	 * @param id id of the Coombs to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Coombs coombs=new Coombs();
			coombs.setId(id);
			ObjectSet result=DB.queryByExample(coombs);
			if(result.hasNext())
			{
				DB.delete((Coombs)result.next());
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
		 * Updates a Coombs
		 * 
		 * @param updated Coombs to update
		 */
		public void update(Coombs updated)
		{
			open();
			try
			{
				Coombs coombs=new Coombs();
				coombs.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(coombs);
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
		config.common().objectClass(Coombs.class).cascadeOnUpdate(true);
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

	public CoombsText getCoombsText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(CoombsText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (CoombsText)result.next();
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

	public void deleteCoombsText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(CoombsText.class);
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
	 * Updates the coombs text
	 * @param about
	 */
	public void updateCoombsText(CoombsText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(CoombsText.class);
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
