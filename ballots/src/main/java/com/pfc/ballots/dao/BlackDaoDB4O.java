package com.pfc.ballots.dao;


import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.BlackText;
import com.pfc.ballots.entities.ballotdata.Black;
/**
 * 
 * Implementation of the interface BlackDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class BlackDaoDB4O implements BlackDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public BlackDaoDB4O(String DBName)
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
	 * Stores a black entity
	 * @param black entity to store
	 */
	public void store(Black black) {
		open();
		try
		{
			DB.store(black);
			System.out.println("[DB4O]Black stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Black could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieves all Black entities
	 * @return list<Black> list of all Black entities
	 */
	public List<Black> retrieveAll() {
		List<Black> black=new LinkedList<Black>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Black.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					black.add((Black)result.next());
				}
				System.out.println("[DB4O]All Black was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			black.clear();
			return black;
		}
		finally
		{
			close();
		}
		
		return black;
	}

	/**
	 * Retrieves a Black entity from its idBallot
	 * @param idBallot id of the ballot that use the Black entity
	 * @retur Black entity
	 */
	public Black getByBallotId(String idBallot) {
		open();
		try
		{
			Black black=new Black();
			black.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(black);
			if(result.hasNext())
			{
				return (Black)result.next();
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
	 * Retrieves a Black by its id
	 * @param id id of the Black entity
	 * @return Black
	 */
	public Black getById(String id) {
		open();
		try
		{
			Black black=new Black();
			black.setId(id);
			ObjectSet result=DB.queryByExample(black);
			if(result.hasNext())
			{
				return (Black)result.next();
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
	 * Deletes all the Black entities
	 */
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(Black.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All Black was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All Black could not be deleted");
			
			}
			finally
			{
				close();
			}
		}


	/**
	 * Deteles a Black from its ballotId
	 * @param ballotId id of the ballot that use the Black entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Black black=new Black();
			black.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(black);
			if(result.hasNext())
			{
				DB.delete((Black)result.next());
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
	 * Deletes a Black from its id
	 * @param id id of the Black to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Black black=new Black();
			black.setId(id);
			ObjectSet result=DB.queryByExample(black);
			if(result.hasNext())
			{
				DB.delete((Black)result.next());
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
		 * Updates a Black
		 * 
		 * @param updated Black to update
		 */
		public void update(Black updated)
		{
			open();
			try
			{
				Black black=new Black();
				black.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(black);
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
		config.common().objectClass(Black.class).cascadeOnUpdate(true);
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

	public BlackText getBlackText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(BlackText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (BlackText)result.next();
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

	public void deleteBlackText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(BlackText.class);
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
	 * Updates the black text
	 * @param about
	 */
	public void updateBlackText(BlackText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(BlackText.class);
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
