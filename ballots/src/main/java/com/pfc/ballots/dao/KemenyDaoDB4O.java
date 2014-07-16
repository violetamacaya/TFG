package com.pfc.ballots.dao;

import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.ballotdata.Kemeny;

public class KemenyDaoDB4O implements KemenyDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public KemenyDaoDB4O(String DBName)
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
	
	
	//********************************************* Store *********************************************//
	public void store(Kemeny kemeny) {
		open();
		try
		{
			DB.store(kemeny);
			System.out.println("[DB4O]Kemeny stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Kemeny could not be stored");
		}
		finally
		{
			close();
		}		
	}

	//********************************************** GETTRS *******************************************//

	public List<Kemeny> retrieveAll() {
		List<Kemeny> kem=new LinkedList<Kemeny>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Kemeny.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					kem.add((Kemeny)result.next());
				}
				System.out.println("[DB4O]All RelMay was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			kem.clear();
			return kem;
		}
		finally
		{
			close();
		}
		
		return kem;
	}


	public Kemeny getByBallotId(String idBallot) {
		open();
		try
		{
			Kemeny kem=new Kemeny();
			kem.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(kem);
			if(result.hasNext())
			{
				return (Kemeny)result.next();
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






	public Kemeny getById(String id) {
		open();
		try
		{
			Kemeny kem=new Kemeny();
			kem.setId(id);
			ObjectSet result=DB.queryByExample(kem);
			if(result.hasNext())
			{
				return (Kemeny)result.next();
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


	//********************************************** DELETE *******************************************//
		public void deleteAll()
		{
			open();
			try
			{
					Query query=DB.query();
					query.constrain(Kemeny.class);
					ObjectSet result = query.execute();
				
					while(result.hasNext())
					{
						DB.delete(result.next());
					}
					System.out.println("[DB4O]All Kemeny was deleted");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("[DB4O]ERROR:All Kemeny could not be deleted");
			
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
			Kemeny kem=new Kemeny();
			kem.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(kem);
			if(result.hasNext())
			{
				DB.delete((Kemeny)result.next());
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
			Kemeny kem=new Kemeny();
			kem.setId(id);
			ObjectSet result=DB.queryByExample(kem);
			if(result.hasNext())
			{
				DB.delete((Kemeny)result.next());
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

	//********************************************** UPDATE *******************************************//
	
		public void update(Kemeny updated)
		{
			open();
			try
			{
				Kemeny kem=new Kemeny();
				kem.setId(updated.getId());
				
				ObjectSet result=DB.queryByExample(kem);
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
	
	
	
	//********************************************Open and Close DB************************************//
	private void open()
	{
		config=Db4oEmbedded.newConfiguration();
		config.common().objectClass(Kemeny.class).cascadeOnUpdate(true);
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
