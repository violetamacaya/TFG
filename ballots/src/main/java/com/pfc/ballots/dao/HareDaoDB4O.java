package com.pfc.ballots.dao;


import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.HareText;
import com.pfc.ballots.entities.ballotdata.Hare;
/**
 * 
 * Implementation of the interface HareDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class HareDaoDB4O implements HareDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public HareDaoDB4O(String DBName)
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
	 * Stores a hare entity
	 * @param hare entity to store
	 */
	public void store(Hare hare) {
		open();
		try
		{
			DB.store(hare);
			System.out.println("[DB4O]Hare stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Hare could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieves all Hare entities
	 * @return list<Hare> list of all Hare entities
	 */
	public List<Hare> retrieveAll() {
		List<Hare> hare=new LinkedList<Hare>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Hare.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					hare.add((Hare)result.next());
				}
				System.out.println("[DB4O]All Hare was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			hare.clear();
			return hare;
		}
		finally
		{
			close();
		}
		
		return hare;
	}

	/**
	 * Retrieves a Hare entity from its idBallot
	 * @param idBallot id of the ballot that use the Hare entity
	 * @retur Hare entity
	 */
	public Hare getByBallotId(String idBallot) {
		open();
		try
		{
			Hare hare=new Hare();
			hare.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(hare);
			if(result.hasNext())
			{
				return (Hare)result.next();
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
	 * Retrieves a Hare by its id
	 * @param id id of the Hare entity
	 * @return Hare
	 */
	public Hare getById(String id) {
		open();
		try
		{
			Hare hare=new Hare();
			hare.setId(id);
			ObjectSet result=DB.queryByExample(hare);
			if(result.hasNext())
			{
				return (Hare)result.next();
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
	 * Deletes all the Hare entities
	 */
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(Hare.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All Hare was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All Hare could not be deleted");
			
			}
			finally
			{
				close();
			}
		}


	/**
	 * Deteles a Hare from its ballotId
	 * @param ballotId id of the ballot that use the Hare entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Hare hare=new Hare();
			hare.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(hare);
			if(result.hasNext())
			{
				DB.delete((Hare)result.next());
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
	 * Deletes a Hare from its id
	 * @param id id of the Hare to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Hare hare=new Hare();
			hare.setId(id);
			ObjectSet result=DB.queryByExample(hare);
			if(result.hasNext())
			{
				DB.delete((Hare)result.next());
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
		 * Updates a Hare
		 * 
		 * @param updated Hare to update
		 */
		public void update(Hare updated)
		{
			open();
			try
			{
				Hare hare=new Hare();
				hare.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(hare);
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
		config.common().objectClass(Hare.class).cascadeOnUpdate(true);
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

	public HareText getHareText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(HareText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (HareText)result.next();
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

	public void deleteHareText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(HareText.class);
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
	 * Updates the hare text
	 * @param about
	 */
	public void updateHareText(HareText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(HareText.class);
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
