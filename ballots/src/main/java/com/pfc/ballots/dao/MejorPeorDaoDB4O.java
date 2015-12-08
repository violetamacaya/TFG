package com.pfc.ballots.dao;


import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.MejorPeorText;
import com.pfc.ballots.entities.ballotdata.MejorPeor;
/**
 * 
 * Implementation of the interface MejorPeorDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class MejorPeorDaoDB4O implements MejorPeorDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public MejorPeorDaoDB4O(String DBName)
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
	 * Stores a mejorPeor entity
	 * @param mejorPeor entity to store
	 */
	public void store(MejorPeor mejorPeor) {
		open();
		try
		{
			DB.store(mejorPeor);
			System.out.println("[DB4O]MejorPeor stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:MejorPeor could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieves all MejorPeor entities
	 * @return list<MejorPeor> list of all MejorPeor entities
	 */
	public List<MejorPeor> retrieveAll() {
		List<MejorPeor> mejorPeor=new LinkedList<MejorPeor>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(MejorPeor.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					mejorPeor.add((MejorPeor)result.next());
				}
				System.out.println("[DB4O]All MejorPeor was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			mejorPeor.clear();
			return mejorPeor;
		}
		finally
		{
			close();
		}
		
		return mejorPeor;
	}

	/**
	 * Retrieves a MejorPeor entity from its idBallot
	 * @param idBallot id of the ballot that use the MejorPeor entity
	 * @retur MejorPeor entity
	 */
	public MejorPeor getByBallotId(String idBallot) {
		open();
		try
		{
			MejorPeor mejorPeor=new MejorPeor();
			mejorPeor.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(mejorPeor);
			if(result.hasNext())
			{
				return (MejorPeor)result.next();
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
	 * Retrieves a MejorPeor by its id
	 * @param id id of the MejorPeor entity
	 * @return MejorPeor
	 */
	public MejorPeor getById(String id) {
		open();
		try
		{
			MejorPeor mejorPeor=new MejorPeor();
			mejorPeor.setId(id);
			ObjectSet result=DB.queryByExample(mejorPeor);
			if(result.hasNext())
			{
				return (MejorPeor)result.next();
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
	 * Deletes all the MejorPeor entities
	 */
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(MejorPeor.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All MejorPeor was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All MejorPeor could not be deleted");
			
			}
			finally
			{
				close();
			}
		}


	/**
	 * Deteles a MejorPeor from its ballotId
	 * @param ballotId id of the ballot that use the MejorPeor entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			MejorPeor mejorPeor=new MejorPeor();
			mejorPeor.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(mejorPeor);
			if(result.hasNext())
			{
				DB.delete((MejorPeor)result.next());
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
	 * Deletes a MejorPeor from its id
	 * @param id id of the MejorPeor to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			MejorPeor mejorPeor=new MejorPeor();
			mejorPeor.setId(id);
			ObjectSet result=DB.queryByExample(mejorPeor);
			if(result.hasNext())
			{
				DB.delete((MejorPeor)result.next());
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
		 * Updates a MejorPeor
		 * 
		 * @param updated MejorPeor to update
		 */
		public void update(MejorPeor updated)
		{
			open();
			try
			{
				MejorPeor mejorPeor=new MejorPeor();
				mejorPeor.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(mejorPeor);
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
		config.common().objectClass(MejorPeor.class).cascadeOnUpdate(true);
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

	public MejorPeorText getMejorPeorText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(MejorPeorText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (MejorPeorText)result.next();
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

	public void deleteMejorPeorText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(MejorPeorText.class);
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
	public void updateMejorPeorText(MejorPeorText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(MejorPeorText.class);
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
