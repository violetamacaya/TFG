package com.pfc.ballots.dao;


import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.SmallText;
import com.pfc.ballots.entities.ballotdata.Small;
/**
 * 
 * Implementation of the interface SmallDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class SmallDaoDB4O implements SmallDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public SmallDaoDB4O(String DBName)
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
	 * Stores a small entity
	 * @param small entity to store
	 */
	public void store(Small small) {
		open();
		try
		{
			DB.store(small);
			System.out.println("[DB4O]Small stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Small could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieves all Small entities
	 * @return list<Small> list of all Small entities
	 */
	public List<Small> retrieveAll() {
		List<Small> small=new LinkedList<Small>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Small.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					small.add((Small)result.next());
				}
				System.out.println("[DB4O]All Small was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			small.clear();
			return small;
		}
		finally
		{
			close();
		}
		
		return small;
	}

	/**
	 * Retrieves a Small entity from its idBallot
	 * @param idBallot id of the ballot that use the Small entity
	 * @retur Small entity
	 */
	public Small getByBallotId(String idBallot) {
		open();
		try
		{
			Small small=new Small();
			small.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(small);
			if(result.hasNext())
			{
				return (Small)result.next();
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
	 * Retrieves a Small by its id
	 * @param id id of the Small entity
	 * @return Small
	 */
	public Small getById(String id) {
		open();
		try
		{
			Small small=new Small();
			small.setId(id);
			ObjectSet result=DB.queryByExample(small);
			if(result.hasNext())
			{
				return (Small)result.next();
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
	 * Deletes all the Small entities
	 */
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(Small.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All Small was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All Small could not be deleted");
			
			}
			finally
			{
				close();
			}
		}


	/**
	 * Deteles a Small from its ballotId
	 * @param ballotId id of the ballot that use the Small entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Small small=new Small();
			small.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(small);
			if(result.hasNext())
			{
				DB.delete((Small)result.next());
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
	 * Deletes a Small from its id
	 * @param id id of the Small to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Small small=new Small();
			small.setId(id);
			ObjectSet result=DB.queryByExample(small);
			if(result.hasNext())
			{
				DB.delete((Small)result.next());
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
		 * Updates a Small
		 * 
		 * @param updated Small to update
		 */
		public void update(Small updated)
		{
			open();
			try
			{
				Small small=new Small();
				small.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(small);
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
		config.common().objectClass(Small.class).cascadeOnUpdate(true);
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

	public SmallText getSmallText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(SmallText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (SmallText)result.next();
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

	public void deleteSmallText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(SmallText.class);
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
	public void updateSmallText(SmallText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(SmallText.class);
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
