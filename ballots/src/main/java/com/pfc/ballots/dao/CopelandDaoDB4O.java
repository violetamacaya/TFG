package com.pfc.ballots.dao;


import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.CopelandText;
import com.pfc.ballots.entities.ballotdata.Copeland;
/**
 * 
 * Implementation of the interface CopelandDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class CopelandDaoDB4O implements CopelandDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public CopelandDaoDB4O(String DBName)
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
	 * Stores a copeland entity
	 * @param copeland entity to store
	 */
	public void store(Copeland copeland) {
		open();
		try
		{
			DB.store(copeland);
			System.out.println("[DB4O]Copeland stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Copeland could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieves all Copeland entities
	 * @return list<Copeland> list of all Copeland entities
	 */
	public List<Copeland> retrieveAll() {
		List<Copeland> copeland=new LinkedList<Copeland>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Copeland.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					copeland.add((Copeland)result.next());
				}
				System.out.println("[DB4O]All Copeland was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			copeland.clear();
			return copeland;
		}
		finally
		{
			close();
		}
		
		return copeland;
	}

	/**
	 * Retrieves a Copeland entity from its idBallot
	 * @param idBallot id of the ballot that use the Copeland entity
	 * @retur Copeland entity
	 */
	public Copeland getByBallotId(String idBallot) {
		open();
		try
		{
			Copeland copeland=new Copeland();
			copeland.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(copeland);
			if(result.hasNext())
			{
				return (Copeland)result.next();
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
	 * Retrieves a Copeland by its id
	 * @param id id of the Copeland entity
	 * @return Copeland
	 */
	public Copeland getById(String id) {
		open();
		try
		{
			Copeland copeland=new Copeland();
			copeland.setId(id);
			ObjectSet result=DB.queryByExample(copeland);
			if(result.hasNext())
			{
				return (Copeland)result.next();
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
	 * Deletes all the Copeland entities
	 */
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(Copeland.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All Copeland was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All Copeland could not be deleted");
			
			}
			finally
			{
				close();
			}
		}


	/**
	 * Deteles a Copeland from its ballotId
	 * @param ballotId id of the ballot that use the Copeland entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Copeland copeland=new Copeland();
			copeland.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(copeland);
			if(result.hasNext())
			{
				DB.delete((Copeland)result.next());
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
	 * Deletes a Copeland from its id
	 * @param id id of the Copeland to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Copeland copeland=new Copeland();
			copeland.setId(id);
			ObjectSet result=DB.queryByExample(copeland);
			if(result.hasNext())
			{
				DB.delete((Copeland)result.next());
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
		 * Updates a Copeland
		 * 
		 * @param updated Copeland to update
		 */
		public void update(Copeland updated)
		{
			open();
			try
			{
				Copeland copeland=new Copeland();
				copeland.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(copeland);
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
		config.common().objectClass(Copeland.class).cascadeOnUpdate(true);
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

	public CopelandText getCopelandText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(CopelandText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (CopelandText)result.next();
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

	public void deleteCopelandText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(CopelandText.class);
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
	 * Updates the copeland text
	 * @param about
	 */
	public void updateCopelandText(CopelandText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(CopelandText.class);
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
