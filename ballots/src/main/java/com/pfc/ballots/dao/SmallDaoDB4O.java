package com.pfc.ballots.dao;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.RangeVotingText;
import com.pfc.ballots.entities.SmallText;
import com.pfc.ballots.entities.ballotdata.RangeVoting;
/**
 * 
 * Implementation of the interface SmallDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
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
	public RangeVotingText getRangeVotingText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(RangeVotingText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (RangeVotingText)result.next();
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

	public void deleteRangeVotingText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(RangeVotingText.class);
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
	public void updateRangeVotingText(RangeVotingText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(RangeVotingText.class);
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
