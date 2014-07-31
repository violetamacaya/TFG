package com.pfc.ballots.dao;

import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.Census;

public class CensusDaoDB4O implements CensusDao{

	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	public CensusDaoDB4O(String DBName)
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
	//************************************** Store ****************************************************//
	
	public void store(Census census)
	{
		open();
		try
		{
			DB.store(census);
			System.out.println("[DB4O]Census was stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Census could not be stored");
		}
		finally
		{
			close();
		}
	}
	//***************************************** Retrievers **********************************************//
	
	@SuppressWarnings("rawtypes")
	public List<Census> retrieveAll()
	{
		
		open();
		List<Census> list=new LinkedList<Census>();
		try
		{
			Query query=DB.query();
			query.constrain(Census.class);
			
			ObjectSet result =query.execute();
			
			while(result.hasNext())
			{
				list.add((Census)result.next());
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
	
	
	@SuppressWarnings("rawtypes")
	public Census getById(String id) {
		
		open();
		try
		{
			Census census=new Census();
			census.setId(id);
			ObjectSet result=DB.queryByExample(census);
			
			if(result.hasNext())
			{
				System.out.println("[DB4O]");
				return (Census) result.next();
			}
			else
			{
				System.out.println("[DB4O]ERROR:Census does not exist");
				return null;
			}
		}
		catch(Exception e)
		{
			System.out.println("[DB4O]ERROR:Census could not be retrieved");
			e.printStackTrace();
		}
		finally
		{
			close();
		}
		return null;
		
		
		
	}
	
	@SuppressWarnings("rawtypes")
	public List<Census> getByOwnerId(String idOwner) {

		open();
		List<Census> list=new LinkedList<Census>();
		try
		{
			Census census=new Census();
			census.setIdOwner(idOwner);
			ObjectSet result=DB.queryByExample(census);
			
			while(result.hasNext())
			{
				list.add((Census)result.next());
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
	
	//*********************************************** DELETE ******************************************//
	
	@SuppressWarnings("rawtypes")
	public void deleteById(String id)
	{
		open();
		try
		{
			Census census= new Census();
			census.setId(id);
			ObjectSet result=DB.queryByExample(census);
			
			if(result.hasNext())
			{
				DB.delete((Census)result.next());
			}
			else
			{
				System.out.println("[DB4O]Census does not exist in database");
			}
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			close();
		}
		
	}
	@SuppressWarnings("rawtypes")
	public void deleteAllCensusOfOwner(String idOwner)
	{
		open();
		try
		{
			Census census= new Census();
			census.setIdOwner(idOwner);
			ObjectSet result=DB.queryByExample(census);
			
			while(result.hasNext())
			{
				DB.delete((Census)result.next());
			}
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			close();
		}
	}
	//************************************************ UPDATE *****************************************//
	
	@SuppressWarnings("rawtypes")
	public void update(Census census)
	{
		open();
		try
		{
			Census temp=new Census();
			temp.setId(census.getId());
			ObjectSet result=DB.queryByExample(temp);
			
			if(result.hasNext())
			{
				DB.delete((Census)result.next());
				DB.store(census);
				System.out.println("[DB4O] Census was updated");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4]ERROR:Census could not be updated");
		}
		finally
		{
			close();
		}
	}
	public void changeEmailOfCensus(List<Census> censuses,String email)
	{
		open();
		try
		{
			for(Census current:censuses)
			{
				Census temp=new Census();
				temp.setId(current.getId());
				ObjectSet result=DB.queryByExample(temp);
				
				if(result.hasNext())
				{
					Census old=(Census)result.next();
					Census updated=new Census(old);
					updated.setEmail(email);
					DB.delete(old);
					DB.store(updated);
					
				}
			}
			System.out.println("[DB4O] Census was updated");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4]ERROR:Census could not be updated");
		}
		finally
		{
			close();
		}
	}

	public void changeEmailOfCensus(String idCensus, String email) 
	{
		open();
		try
		{
			Census temp=new Census();
			temp.setId(idCensus);
			ObjectSet result=DB.queryByExample(temp);
			
			if(result.hasNext())
			{
				Census old=(Census)result.next();
				Census updated=new Census(old);
				updated.setEmail(email);
				DB.delete(old);
				DB.store(updated);
				System.out.println("[DB4O] Census was updated");
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4]ERROR:Census could not be updated");
		}
		finally
		{
			close();
		}
		
		
	}

	public void removeUserCountedOfCensus(List<String> idCensus,String idProfile)
	{
		open();
		try
		{
			for(String current:idCensus)
			{
				Census old=getCensusById(current);
				Census updated=new Census(old);
				updated.removeIdOfUsersCounted(idProfile);
				DB.delete(old);
				DB.store(updated);
			}
			System.out.println("[DB4O] Census was updated");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4]ERROR:Census could not be updated");
		}
		finally
		{
			close();
		}
		
		
	}
	
	//**************************************************UTIL*******************************************//
	
	@SuppressWarnings("rawtypes")
	public boolean isNameInUse(String name,String idOwner)
	{
		open();
		try
		{
			System.out.println(name);
			Query query=DB.query();
			query.constrain(Census.class);
			query.descend("idOwner").constrain(idOwner);
			query.descend("censusName").constrain(name).endsWith(false);
			
	
			
			ObjectSet result=query.execute();
			
			if(result.hasNext())
			{
				return true;
			}
			else
			{
				return false;
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
		
		return false;
		
	}
	////////////////////////////////////////////////// UTIL NO OPEN-CLOSE////////////////////////////////
	private Census getCensusById(String idCensus)
	{
		Census census= new Census();
		census.setId(idCensus);
		ObjectSet result=DB.queryByExample(census);
		
		if(result.hasNext())
		{
			return (Census)result.next();
		}
		return null;
	}
	
	
	//********************************************Open and Close DB************************************//
	
		private void open()
		{
			config=Db4oEmbedded.newConfiguration();
			config.common().objectClass(Census.class).cascadeOnUpdate(true);
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
		private void close()
		{
			DB.close();
			System.out.println("[DB4O]Database was closed");
		}

		
		

	
	
}
