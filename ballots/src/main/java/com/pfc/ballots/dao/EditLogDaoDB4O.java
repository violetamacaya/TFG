package com.pfc.ballots.dao;

import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.EditLog;

import java.lang.reflect.Field;


/**
 * 
 * Implementation of the interface EditLogDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2015
 *
 */
@SuppressWarnings("rawtypes")
public class EditLogDaoDB4O implements EditLogDao {

	String sep=System.getProperty("file.separator");
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	String PATH;
	
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	public EditLogDaoDB4O(String DBName)
	{
		if(DBName==null)
		{
			PATH=ruta+"DB4Obbdd.dat";
		}
		else
		{
			PATH=ruta+DBName;
		}
		System.out.println("Ruta que me ha llegado: "+PATH);

	}
	
	/************************************ Store  *********************************************************/
	
	/**
	 * Stores a editLog
	 * @param editLog editLog to store
	 */
	public void store(EditLog editLog){
		
		
		open();
		try
		{
			DB.store(editLog);
			System.out.println("[DB4O]EditLog stored");
			for (Field field : editLog.getClass().getDeclaredFields()) {
			    field.setAccessible(true);
			    String name = field.getName();
			    Object value = null;
				try {
					value = field.get(editLog);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			    System.out.printf("Field name: %s, Field value: %s%n", name, value);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]Error: EditLog could not be stored");
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
		config.common().objectClass(EditLog.class).cascadeOnUpdate(true);
		try
		{
			DB=Db4oEmbedded.openFile(config, PATH);
			System.out.println("[DB4O]Database on editLog was opened");
		}
		catch(Exception e)
		{
			System.out.println("[DB4O]ERROR:Database on editLog could not be open");
			e.printStackTrace();
		}
	}
	/**
	 * Closes database
	 */
	private void close()
	{
		DB.close();
		System.out.println("[DB4O]Database on editLog was closed");
	}

	/**
	 * Retrieves a ballot's information from its id
     * 
     * @param Id Id of the ballot that will be retrieved
     * @return EditLog Ballot that will be retrieved
	 */
	public List<EditLog> retrieve(String ballotId) {
		System.out.println("La id de retrieve es: "+ballotId);

//			lista=DB.query(new Predicate<EditLog>(){
//				public boolean match(EditLog candidate){
//					return candidate.getBallotId().equals(id);
//				}
//			});
//			EditLog temp=new EditLog();
//			temp.setBallotId(id);
//			@SuppressWarnings("rawtypes")
//			ObjectSet result=DB.queryByExample(temp);
//			EditLog x;
//			if(result.hasNext())
//			{
//				x=(EditLog)result.next();
//				System.out.println("La x vale:" +x);
//				lista.add(x);
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		finally
//		{
//			close();
//		}
//		return lista;
//	}
			List<EditLog> list=new LinkedList<EditLog>();
			open();
			try
			{
				Query query =DB.query();
				query.constrain(EditLog.class);
				if(ballotId!=null)
				{
					query.descend("ballotId").constrain(ballotId).endsWith(false);
					ObjectSet result=query.execute();
					while(result.hasNext())
					{
						EditLog x=(EditLog) result.next();
						list.add(x);
					}
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
			return list;
		}
}
