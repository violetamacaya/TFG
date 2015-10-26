package com.pfc.ballots.dao;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.BramsText;
import com.pfc.ballots.entities.ballotdata.Brams;
/**
 * 
 * Implementation of the interface BramsDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
public class BramsDaoDB4O implements BramsDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public BramsDaoDB4O(String DBName)
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
	//********************************************Open and Close DB************************************//
		/**
		 * Opens database
		 */
		private void open()
		{
			config=Db4oEmbedded.newConfiguration();
			config.common().objectClass(Brams.class).cascadeOnUpdate(true);
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
	public BramsText getBramsText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(BramsText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (BramsText)result.next();
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

	public void deleteBramsText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(BramsText.class);
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
	 * Updates the Brams text
	 * @param about
	 */
	public void updateBramsText(BramsText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(BramsText.class);
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
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			Brams brams=new Brams();
			brams.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(brams);
			if(result.hasNext())
			{
				DB.delete((Brams)result.next());
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
	public void deleteById(String id) {
		open();
		try
		{
			Brams brams=new Brams();
			brams.setId(id);
			ObjectSet result=DB.queryByExample(brams);
			if(result.hasNext())
			{
				DB.delete((Brams)result.next());
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
	 * Deletes all the Approval voting entities in the system
	 */
	public void deleteAll() {
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Brams.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					DB.delete(result.next());
				}
				System.out.println("[DB4O]All Brams was deleted");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All Brams could not be deleted");
		
		}
		finally
		{
			close();
		}

	}
	public void store(Brams brams) {
		open();
		try
		{
			DB.store(brams);
			System.out.println("[DB4O]Ballot stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Ballot could not be stored");
		}
		finally
		{
			close();
		}
	}
	public Brams getByBallotId(String idBallot)
	{
		open();
		try
		{
			Brams brams=new Brams();
			brams.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(brams);
			if(result.hasNext())
			{
				return (Brams)result.next();
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
	
	public void update(Brams updated)
	{
		open();
		try
		{
			Brams brams=new Brams();
			brams.setId(updated.getId());
			
			ObjectSet result=DB.queryByExample(brams);
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

}
