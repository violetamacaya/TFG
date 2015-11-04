package com.pfc.ballots.dao;

import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.DataLog;

/**
 * 
 * Implementation of the interface LogDao for the DB4O database
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 *
 */
public class LogDaoDB4O implements LogDao {

	String sep=System.getProperty("file.separator");
	String PATH=System.getProperty("user.home")+sep+"BallotsFiles"+sep+"LogBD.dat";;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	/************************************ Store  *********************************************************/
	
	/**
	 * Stores a Datalog
	 * @param datalog DataLog to store
	 */
	public void store(DataLog datalog){
		
		
		open();
		try
		{
			DB.store(datalog);
			System.out.println("[DB4O]DataLog stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]Error: DataLog could not be stored");
		}
		finally
		{
			close();
		}
		
	}
	
	
	
	/***********************************   Retrieves    ***************************************************/
	
	/**
	 * Retrives all DataLog
	 * @return List<Datalog>
	 */
	@SuppressWarnings("rawtypes")
	public List<DataLog> retrieve() {
		open();
		List<DataLog> list=new LinkedList<DataLog>();
		try
		{
			 Query query = DB.query();
		     query.constrain(DataLog.class);
		     query.descend("date").orderDescending();
		     ObjectSet result=query.execute();
		     while(result.hasNext())
		     {
		    	list.add((DataLog) result.next());
		     }
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close();
		}
		return list;
	}

	/**
	 * Retrieves the DataLog of a company
	 * @param company name of the company to retrieve its dataLog
	 * @return List<DataLog> DataLog of the company
	 */
	@SuppressWarnings("rawtypes")
	public List<DataLog> retrieve(String companyName )
	{
		String company;
		if(companyName==null)
		{
			company="main";
		}
		else
		{
			company=companyName;
		}
		
		open();
		List<DataLog> list=new LinkedList<DataLog>();
		try
		{
			 Query query = DB.query();
		     query.constrain(DataLog.class);
		     query.descend("accessTo").constrain(company);
		     query.descend("date").orderDescending();
		     ObjectSet result=query.execute();
		     while(result.hasNext())
		     {
		    	list.add((DataLog) result.next());
		     }
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close();
		}
		return list;
	}


	/**
	 * Retrieve the recents logs
	 * @param last number of logs to retrieve
	 */
	@SuppressWarnings("rawtypes")
	public List<DataLog> retrieve(int last) {
		open();
		int retrieved=0;
		List<DataLog> list=new LinkedList<DataLog>();
		try
		{
			 Query query = DB.query();
		     query.constrain(DataLog.class);
		     query.descend("date").orderDescending();
		     ObjectSet result=query.execute();
		     while(result.hasNext())
		     {
		    	if(retrieved<last)
			    	{retrieved++;}
			    else
			    {break;}
		    	list.add((DataLog) result.next());
		    	
		     }
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close();
		}
		return list;
	}
	
	/***************************     Delete    **************************************************************/
	/**
	 * Delete all DataLogs
	 */
	@SuppressWarnings("rawtypes")
	public void DeleteAll() {
		open();
		try
		{
			 Query query = DB.query();
		     query.constrain(DataLog.class);
		     query.descend("date").orderDescending();
		     ObjectSet result=query.execute();
		     while(result.hasNext())
		     {
		    	DB.delete((DataLog) result.next());
		     }
			System.out.println("[DB4O]All DataLog was removed");
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All DataLog could not be removed");
		}
		finally
		{
			close();
		}
				
	}

	
	
	/***************************** Open and close Database ************************************************/
	
	/**
	 * Opens database
	 */
	private void open()
	{
		
		config=Db4oEmbedded.newConfiguration();
		config.common().objectClass(DataLog.class).cascadeOnUpdate(true);
		try
		{
			DB=Db4oEmbedded.openFile(config, PATH);
			System.out.println("[DB4O]Database was opened");
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







	
}
