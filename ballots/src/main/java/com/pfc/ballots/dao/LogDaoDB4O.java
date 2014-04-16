package com.pfc.ballots.dao;

import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.DataLog;


public class LogDaoDB4O implements LogDao {

	String sep=System.getProperty("file.separator");
	String PATH=System.getProperty("user.home")+sep+"BallotsFiles"+sep+"LogBD.dat";;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	/************************************ Store  *********************************************************/
	
	
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

	public List<DataLog> retrieve(String company )
	{
		
		
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
	
	public void DeleteAll() {
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
	private void close()
	{
		DB.close();
		System.out.println("[DB4O]Database was closed");
	}







	
}
