package com.pfc.ballots.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.UserLoged;

/**
 * 
 * Implementation of the interface UserLogedDao for the DB4O database
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 *
 */

public class UserLogedDaoDB4O implements UserLogedDao{

	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	UserLogedDaoDB4O(String DBName)
	{
		if(DBName==null)
		{
			PATH=ruta+"DB4Obbdd.dat";
		}
		else
		{
			PATH=ruta+DBName;
		}
		System.out.println(ruta);
	}
	//***********************************************Store**********************************************//
	
	/**
	 * Stores an UserLoged
	 * @param userLoged to store
	 */
	public void store(UserLoged userLoged) {
		open();
		try
		{
			DB.store(userLoged);	
			System.out.println("[DB4O]UserLoged stored successfully");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:UserLoged could not be stored");
		}
		finally
		{
			close();
		}
		
	}
	//*********************************************** Getter ******************************************//
	/**
	 * Retrieves an UserLoged from its idSession
	 * @param idSession of the UserLoged to retrieve
	 * @return UserLoged
	 */
	public UserLoged getUserLoged(String idSession)
	{
		open();
		try
		{
			ObjectSet result = DB.queryByExample(new UserLoged(idSession));
			if(result.hasNext())
			{
				return (UserLoged)result.next();
			}
			return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR: Could not verify if user is loged in");
			return null;
		}
		finally
		{
			close();
		}
	}
	//**********************************************Retrieve all***************************************//
	/**
	 * Retrieves all the UserLoged entities in the database
	 * @return List<UserLoged>
	 */
	@SuppressWarnings("rawtypes")
	public List<UserLoged> retrieveAll()
	{
		open();
		List<UserLoged> users=new ArrayList<UserLoged>();
		try
		{
			Query query=DB.query();
			query.constrain(UserLoged.class);
			query.descend("date").orderDescending();
			
			ObjectSet resultado = query.execute();
			
			while (resultado.hasNext())
			{
				users.add((UserLoged)resultado.next());
			}
			System.out.println("[DB4O]All UserLoged retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR: UserLoged could not be retrieved");
			users.clear();
			return users;
		}
		finally
		{
			close();
		}
		return users;
	}
	
	//******************************************************Delete*************************************************************************//
	/**
	 * Delete all UserLoged in database
	 */
	public void deleteAll()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(UserLoged.class);
			ObjectSet resultado = query.execute();
			while (resultado.hasNext())
			{
				DB.delete((UserLoged)resultado.next());
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
	 * Delete an UserLoged from its idSession
	 * @param idSession of the UserLoged to delete
	 */
	@SuppressWarnings("rawtypes")
	public void delete(String idSession)
	{
		open();
		UserLoged temp=null;
		try
		{
			ObjectSet result = DB.queryByExample(new UserLoged(idSession));
			if(result.hasNext())
			{
				temp=(UserLoged)result.next();
				DB.delete(temp);
				System.out.println("[DB4O]UserLoged was erased");
			}
			else
			{
				System.out.println("[DB4O]UserLoged doesn't exist");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR: UserLoged could not be delete");
		}
		finally
		{
			close();
		}
	}
	/**
	 * Deletes an UserLoged from its email
	 * @param email of the UserLoged to delete
	 */
	@SuppressWarnings("rawtypes")
	public void deleteByEmail(String email)
	{
		open();
		UserLoged look=new UserLoged();
		look.setEmail(email);
		UserLoged temp=null;
		try
		{
			
			ObjectSet result = DB.queryByExample(look);
			while(result.hasNext())
			{
				temp=(UserLoged)result.next();
				DB.delete(temp);
				
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR: UserLoged could not be delete");
		}
		finally
		{
			close();
		}
	}
	//**********************************************isLogedIn******************************************//
	/**
	 * Checks if a UserLoged is in database from its idSession
	 * @param idSession to check
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public boolean isLogedIn(String idSession)
	{
		open();
		try
		{
			ObjectSet result = DB.queryByExample(new UserLoged(idSession));
			if(result.hasNext())
			{
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR: Could not verify if user is loged in");
			return false;
		}
		finally
		{
			close();
		}
		
	}
	/**
	 * 	Deletes all UserLoged that exceeds the time of session
	 * @param timeOfSession to check if the UserLoged exceeds
	 */
	@SuppressWarnings("rawtypes")
	public void clearSessions(long timeOfSession) {
	
		open();
		System.out.println("CLEAR SESSIONS");

		List<UserLoged> users=new ArrayList<UserLoged>();
		Date actualDate=new Date();
		//Date logDate=null;
		
		Calendar calActualDate=Calendar.getInstance();
		Calendar calLogDate=Calendar.getInstance();
		
		calActualDate.setTime(actualDate);
		long milis1=calActualDate.getTimeInMillis();
		long milis2;
		long diff;
		long diffMin;
		try
		{
			System.out.println("CLEAR SESSIONS");
			Query query=DB.query();
			query.constrain(UserLoged.class);
			query.descend("date").orderAscending();
			ObjectSet resultado = query.execute();
			
			while (resultado.hasNext())
			{
				users.add((UserLoged)resultado.next());
			}
			
		
			for(int i=0;i<users.size();i++)
			{
				calLogDate.setTime(users.get(i).getDate());
				milis2=calLogDate.getTimeInMillis();
				diff=milis1-milis2;
				diffMin=diff/(60*1000);
				
				if(diffMin>timeOfSession)
				{
					System.out.println("SI");
					DB.delete(users.get(i));
				}
				else
					System.out.println("NO");
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
	//*********************************************** Update ******************************************//
	/**
	 * Updates an UserLoged
	 * @param userLoged to update
	 */
	public void updateUserLoged(UserLoged userLoged)
	{
		open();
		UserLoged temp=null;
		try
		{
			ObjectSet result = DB.queryByExample(new UserLoged(userLoged.getIdSession()));
			if(result.hasNext())
			{
				temp=(UserLoged)result.next();
				DB.delete(temp);
				DB.store(userLoged);
				System.out.println("[DB4O]UserLoged Updated ");
			}
			else
			{
				System.out.println("[DB4O]UserLoged doesn't exist");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR: UserLoged could not be delete");
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
		config.common().objectClass(UserLoged.class).cascadeOnUpdate(true);
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
	



	
	
}
