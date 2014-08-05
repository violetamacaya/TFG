package com.pfc.ballots.dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.Company;

/**
 * 
 * Implementation of the interface CompanyDao for the DB4O database
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 *
 */
public class CompanyDaoDB4O implements CompanyDao {
	
	String sep=System.getProperty("file.separator");
	String PATH=System.getProperty("user.home")+sep+"BallotsFiles"+sep+"DB4Obbdd.dat";
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	

	
	
	//************************************   Store    *****************************************//
	/**
	 * Stores a company
	 * 
	 * @param company company to store
	 */
	public void store(Company company) 
	{
		open();
		try
		{
			if(testCompany(company.getCompanyName()))
			{
				System.out.println("[DB4O]Warning:Company name was already in use");
			}
			else
			{
				DB.store(company);
				System.out.println("[DB4O]Company was stored");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR: Company could not be stored");
		}
		finally
		{
			close();
		}
	}
	
	//**************************************  Retrieve *******************************************//
	/**
	 * Retrieves a company from its name
	 * @param companyName name of the company to retrieve
	 * @return Company company whose name is companyName
	 */
	public Company getCompanyByName(String companyName)
	{
		Company temp=null;
		open();
		try
		{
			@SuppressWarnings("rawtypes")
			ObjectSet result=DB.queryByExample(new Company(companyName));
			if(result.hasNext())
			{
				temp=(Company)result.next();
				System.out.println("[DB4O]Company was retrieved");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]Company could not be retrieved");
		}
		finally
		{
			close();
		}
		return temp;
	}
	
	/**
	 * Retrieves all companies in database
	 * @return List<Company> list of retrieved companies
	 */
	@SuppressWarnings("rawtypes")
	public List<Company> RetrieveAllCompanies()
	{
		List<Company> list=new ArrayList<Company>();
		open();
		try
		{
			Query query=DB.query();
			query.constrain(Company.class);
			ObjectSet result=query.execute();
			while(result.hasNext())
			{
				list.add((Company) result.next());
			}
			System.out.println("[DB4O]All companies was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			list.clear();
			System.out.println("[DB4O]ERROR: All companies could not be retrieved");
			return list;
		}
		finally
		{
			close();
		}
		return list;
		
	}
	//*************************************** Update  ******************************************//
	/**
	 * Updates a company
	 * @param company company to update
	 */
	public void updateCompany(Company company)
	{
		
		open();
		try
		{
			Company temp=new Company();
			temp.setId(company.getId());
			@SuppressWarnings("rawtypes")
			ObjectSet result=DB.queryByExample(temp);
			if(result.hasNext())
			{
				DB.delete(result.next());
				DB.store(company);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR: Company could not be updated");
		}
		finally
		{
			close();
		}
		
	}
	

	
	
	//*************************************** Delete  ******************************************//
	/**
	 * Deletes a company by its name
	 * @param companyName name of the company to delete
	 */
	@SuppressWarnings("rawtypes")
	public void deleteCompanyByName(String companyName)
	{
		open();
		try
		{
			ObjectSet result=DB.queryByExample(new Company(companyName));
			Company comp=null;
			if(result.hasNext())
			{
				comp=(Company)result.next();
				DB.delete(comp);
				System.out.println("[DB4O]Company was erased from database");
				//Delete DataBase
				try
				{
					String temp=System.getProperty("user.home")+sep+"BallotsFiles"+sep+comp.getDBName();
					Path ruta=Paths.get(temp);
					Files.deleteIfExists(ruta);
				}catch(Exception e2)
				{
					e2.printStackTrace();
					System.out.println("DATABASE COULD NOT BE ERASED");
				}
				
			}
			else
			{
				System.out.println("[DB4O]ERROR:Profile doesn't exist in database");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Company could not be erased");
		}
		finally
		{
			close();
		}
		
	}
	
	//************************************ isRegistred  ****************************************//
	
	/**
	 * Checks if a company name is registred
	 * @Param companyName name to check
	 * @return boolean
	 */
	public boolean isCompanyRegistred(String companyName) {
		open();
		boolean temp=true;
		try
		{
			temp=testCompany(companyName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Could not look for Company");
		}
		finally
		{
			close();
		}
		return temp;
	}
	/**
	 * Checks if the name of a database is in use
	 * @param DBName name to check
	 * @return boolean
	 */
	public boolean isDBNameRegistred(String DBName)
	{
		open();
		boolean temp=true;
		try
		{
			temp=testDBName(DBName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Could not look for DBName");
		}
		finally
		{
			close();
		}
		return temp;
		
	}
	/**
	 * Checks if a database is active
	 * @param companyName name of the company to check
	 * @return boolean
	 */
	public boolean isActive(String companyName)
	{
		open();
		try
		{
			ObjectSet result=DB.queryByExample(new Company(companyName));
			if(result.hasNext())
			{
				if(((Company)result.next()).isActive())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close();
		}
		return false;
	}
	//*************************************Util (without open or close DB*************************//
	/**
	 * Checks if a companyName is in use
	 * @param companyName name to check
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	private boolean testCompany(String companyName)
	{
		
		ObjectSet result=DB.queryByExample(new Company(companyName));
		
		if(result.hasNext())
		{
			return true;
		}
		return false;
	}
	/**
	 * Checks if a DBname is in use
	 * @param DBName to check
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	private boolean testDBName(String DBName)
	{
		Company temp=new Company();
		temp.setDBName(DBName);
		ObjectSet result=DB.queryByExample(temp);
		
		if(result.hasNext())
		{
			return true;
		}
		return false;
	}
	
	
	
	//********************************************Open and Close DB************************************//
	/**
	 * Opens database
	 */
	private void open()
	{
		config=Db4oEmbedded.newConfiguration();
		config.common().objectClass(Company.class).cascadeOnUpdate(true);
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
	 * Closes databases
	 */
	private void close()
	{
		DB.close();
		System.out.println("[DB4O]Database was closed");
	}

	



	
}
