package com.pfc.ballots.dao;


import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.BucklinText;
import com.pfc.ballots.entities.ballotdata.Bucklin;
/**
 * 
 * Implementation of the interface BucklinDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class BucklinDaoDB4O implements BucklinDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public BucklinDaoDB4O(String DBName)
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
	 * Stores a bucklin entity
	 * @param bucklin entity to store
	 */
	public void store(Bucklin bucklin) {
		open();
		try
		{
			DB.store(bucklin);
			System.out.println("[DB4O]Bucklin stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Bucklin could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieves all Bucklin entities
	 * @return list<Bucklin> list of all Bucklin entities
	 */
	public List<Bucklin> retrieveAll() {
		List<Bucklin> bucklin=new LinkedList<Bucklin>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Bucklin.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					bucklin.add((Bucklin)result.next());
				}
				System.out.println("[DB4O]All Bucklin was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			bucklin.clear();
			return bucklin;
		}
		finally
		{
			close();
		}
		
		return bucklin;
	}

	/**
	 * Retrieves a Bucklin entity from its idBallot
	 * @param idBallot id of the ballot that use the Bucklin entity
	 * @retur Bucklin entity
	 */
	public Bucklin getByBallotId(String idBallot) {
		open();
		try
		{
			Bucklin bucklin=new Bucklin();
			bucklin.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(bucklin);
			if(result.hasNext())
			{
				return (Bucklin)result.next();
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
	 * Retrieves a Bucklin by its id
	 * @param id id of the Bucklin entity
	 * @return Bucklin
	 */
	public Bucklin getById(String id) {
		open();
		try
		{
			Bucklin bucklin=new Bucklin();
			bucklin.setId(id);
			ObjectSet result=DB.queryByExample(bucklin);
			if(result.hasNext())
			{
				return (Bucklin)result.next();
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
	 * Deletes all the Bucklin entities
	 */
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(Bucklin.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All Bucklin was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All Bucklin could not be deleted");
			
			}
			finally
			{
				close();
			}
		}


	/**
	 * Deteles a Bucklin from its ballotId
	 * @param ballotId id of the ballot that use the Bucklin entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Bucklin bucklin=new Bucklin();
			bucklin.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(bucklin);
			if(result.hasNext())
			{
				DB.delete((Bucklin)result.next());
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
	 * Deletes a Bucklin from its id
	 * @param id id of the Bucklin to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Bucklin bucklin=new Bucklin();
			bucklin.setId(id);
			ObjectSet result=DB.queryByExample(bucklin);
			if(result.hasNext())
			{
				DB.delete((Bucklin)result.next());
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
		 * Updates a Bucklin
		 * 
		 * @param updated Bucklin to update
		 */
		public void update(Bucklin updated)
		{
			open();
			try
			{
				Bucklin bucklin=new Bucklin();
				bucklin.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(bucklin);
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
		config.common().objectClass(Bucklin.class).cascadeOnUpdate(true);
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

	public BucklinText getBucklinText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(BucklinText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (BucklinText)result.next();
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

	public void deleteBucklinText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(BucklinText.class);
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
	 * Updates the bucklin text
	 * @param about
	 */
	public void updateBucklinText(BucklinText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(BucklinText.class);
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
