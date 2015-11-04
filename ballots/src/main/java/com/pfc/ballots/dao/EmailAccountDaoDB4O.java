package com.pfc.ballots.dao;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.EmailAccount;

public class EmailAccountDaoDB4O implements EmailAccountDao{

/**
 * 
 * Implementation of the interface EmailAccount for the DB4O database
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUN-2014
 *
 */
	
/*
 * THIS DAO IS IMPLEMENTED FOR ONLY ONE ITEM EmailAccount IN THE DATABASE
 * 
 */

	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	public EmailAccountDaoDB4O(String DBName)
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
	/**
	 * Retrieves the EmailAccout
	 * @return EmailAccount
	 */
	@SuppressWarnings("rawtypes")
	public EmailAccount getAccount() {
			
		open();
		EmailAccount account=null;
		try
		{
			Query query=DB.query();
			query.constrain(EmailAccount.class);
			
			
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				account=(EmailAccount) result.next();
				System.out.println("[DB4O]Account retrieved");
			}
				System.out.println("[DB4O]Account doesn't exists");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close();
		}
		return account;
	}

	/**
	 * Stores the emailAccount
	 * @param account account to store
	 */
	public void setEmailAccount(EmailAccount account) {
		open();
		try
		{
			DB.store(account);
			System.out.println("[DB4O]Account stored");
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
	 * Deletes the email account
	 */
	@SuppressWarnings("rawtypes")
	public void deleteEmailAccount() {
		open();
		
		try
		{
			Query query=DB.query();
			query.constrain(EmailAccount.class);
			
			
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				DB.delete((EmailAccount)result.next());
				System.out.println("[DB4O]EmailAccount deleted");
			}
			else
			{
				System.out.println("[DB4O]EmailAccount could not be deleted");
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
	 * Updates the account
	 * @param newAccount account to store
	 */
	@SuppressWarnings("rawtypes")
	public void updateEmailAccount(EmailAccount newAccount) {
		open();
		try
		{
			Query query=DB.query();
			query.constrain(EmailAccount.class);
			
			
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				DB.delete((EmailAccount)result.next());
				DB.store(newAccount);
				System.out.println("[DB4O]EmailAccount updated");
			}
			else
			{
				System.out.println("[DB4O]EmailAccount could not be updated");
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
				config.common().objectClass(EmailAccount.class).cascadeOnUpdate(true);
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
