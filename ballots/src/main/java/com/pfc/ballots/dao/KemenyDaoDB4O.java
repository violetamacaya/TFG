package com.pfc.ballots.dao;

import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.KemenyText;
import com.pfc.ballots.entities.ballotdata.Kemeny;
/**
 * 
 * Implementation of the interface KemenyDao for the DB4O database
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUL-2014
 *
 */
public class KemenyDaoDB4O implements KemenyDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public KemenyDaoDB4O(String DBName)
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
	 * Stores a kemeny entity
	 * @param kemeny entity to store
	 */
	public void store(Kemeny kemeny) {
		open();
		try
		{
			DB.store(kemeny);
			System.out.println("[DB4O]Kemeny stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Kemeny could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//
	/**
	 * Retrieves all Kemeny entities
	 * @return list<Kemeny> list of all Kemeny entities
	 */
	public List<Kemeny> retrieveAll() {
		List<Kemeny> kem=new LinkedList<Kemeny>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Kemeny.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					kem.add((Kemeny)result.next());
				}
				System.out.println("[DB4O]All Kemeny was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			kem.clear();
			return kem;
		}
		finally
		{
			close();
		}
		
		return kem;
	}

	/**
	 * Retrieves a Kemeny entity from its idBallot
	 * @param idBallot id of the ballot that use the Kemeny entity
	 * @retur Kemeny entity
	 */
	public Kemeny getByBallotId(String idBallot) {
		open();
		try
		{
			Kemeny kem=new Kemeny();
			kem.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(kem);
			if(result.hasNext())
			{
				return (Kemeny)result.next();
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
	 * Retrieves a Kemeny by its id
	 * @param id id of the Kemeny entity
	 * @return Kemeny
	 */
	public Kemeny getById(String id) {
		open();
		try
		{
			Kemeny kem=new Kemeny();
			kem.setId(id);
			ObjectSet result=DB.queryByExample(kem);
			if(result.hasNext())
			{
				return (Kemeny)result.next();
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
	 * Deletes all the Kemeny entities
	 */
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(Kemeny.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All Kemeny was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All Kemeny could not be deleted");
			
			}
			finally
			{
				close();
			}
		}


	/**
	 * Deteles a Kemeny from its ballotId
	 * @param ballotId id of the ballot that use the Kemeny entity
	 */
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Kemeny kem=new Kemeny();
			kem.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(kem);
			if(result.hasNext())
			{
				DB.delete((Kemeny)result.next());
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
	 * Deletes a Kemeny from its id
	 * @param id id of the Kemeny to delete
	 */
	public void deleteById(String id) {
		open();
		try
		{
			Kemeny kem=new Kemeny();
			kem.setId(id);
			ObjectSet result=DB.queryByExample(kem);
			if(result.hasNext())
			{
				DB.delete((Kemeny)result.next());
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
		 * Updates a Kemeny
		 * 
		 * @param updated Kemeny to update
		 */
		public void update(Kemeny updated)
		{
			open();
			try
			{
				Kemeny kem=new Kemeny();
				kem.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(kem);
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
		config.common().objectClass(Kemeny.class).cascadeOnUpdate(true);
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

	public KemenyText getKemenyText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(KemenyText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (KemenyText)result.next();
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

	public void deleteKemenyText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(KemenyText.class);
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
	public void updateKemenyText(KemenyText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(KemenyText.class);
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
