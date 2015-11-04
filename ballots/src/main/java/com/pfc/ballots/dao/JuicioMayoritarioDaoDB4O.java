package com.pfc.ballots.dao;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.RangeVotingText;
import com.pfc.ballots.entities.JuicioMayoritarioText;
import com.pfc.ballots.entities.ballotdata.JuicioMayoritario;
import com.pfc.ballots.entities.ballotdata.RangeVoting;
/**
 * 
 * Implementation of the interface JuicioMayoritarioDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
public class JuicioMayoritarioDaoDB4O implements JuicioMayoritarioDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public JuicioMayoritarioDaoDB4O(String DBName)
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
			config.common().objectClass(RangeVoting.class).cascadeOnUpdate(true);
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
	public JuicioMayoritarioText getJuicioMayoritarioText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(JuicioMayoritarioText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (JuicioMayoritarioText)result.next();
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

	public void deleteJuicioMayoritarioText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(JuicioMayoritarioText.class);
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
	 * Updates the JuicioMayoritario text
	 * @param about
	 */
	public void updateJuicioMayoritarioText(JuicioMayoritarioText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(JuicioMayoritarioText.class);
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
			JuicioMayoritario juicioMayoritario=new JuicioMayoritario();
			juicioMayoritario.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(juicioMayoritario);
			if(result.hasNext())
			{
				DB.delete((JuicioMayoritario)result.next());
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
			JuicioMayoritario juicioMayoritario=new JuicioMayoritario();
			juicioMayoritario.setId(id);
			ObjectSet result=DB.queryByExample(juicioMayoritario);
			if(result.hasNext())
			{
				DB.delete((JuicioMayoritario)result.next());
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
	 * Deletes all the JuicioMayoritario voting entities in the system
	 */
	public void deleteAll() {
		open();
		try
		{
				Query query=DB.query();
				query.constrain(JuicioMayoritario.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					DB.delete(result.next());
				}
				System.out.println("[DB4O]All JuicioMayoritario was deleted");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All JuicioMayoritario could not be deleted");
		
		}
		finally
		{
			close();
		}

	}
	public void store(JuicioMayoritario juicioMayoritario) {
		open();
		try
		{
			DB.store(juicioMayoritario);
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
	public JuicioMayoritario getByBallotId(String idBallot)
	{
		open();
		try
		{
			JuicioMayoritario juicioMayoritario=new JuicioMayoritario();
			juicioMayoritario.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(juicioMayoritario);
			if(result.hasNext())
			{
				return (JuicioMayoritario)result.next();
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
	
	public void update(JuicioMayoritario updated)
	{
		open();
		try
		{
			JuicioMayoritario juicioMayoritario=new JuicioMayoritario();
			juicioMayoritario.setId(updated.getId());
			
			ObjectSet result=DB.queryByExample(juicioMayoritario);
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
