package com.pfc.ballots.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.Ballot;
import com.pfc.ballots.entities.Profile;



public class BallotDaoDB4O implements BallotDao{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	Calendar calAct;
	Calendar calEnd;
	
	
	
	public BallotDaoDB4O(String DBName)
	{
		calAct=new GregorianCalendar();
		calEnd=new GregorianCalendar();
		
		if(DBName==null)
		{
			PATH=ruta+"DB4Obbdd.dat";
		}
		else
		{
			PATH=ruta+DBName;
		}
	}
	
	
	//*************************************************Store******************************************************
	public void store(Ballot ballot)
	{
		open();
		try
		{
			if(!testBallot(ballot.getName()))
			{
				DB.store(ballot);
				System.out.println("[DB4O]Ballot was Stored");
			}
			else
			{
				System.out.println("[DB4O]Warning:Email was already in use");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Profile could not be stored");
		}
		finally
		{
			close();
		}
		
	}
	//***************************************** retrievers ********************************************//

	public Ballot getById(String id) {
		
		open();
		try
		{
			Ballot temp=new Ballot();
			temp.setId(id);
			ObjectSet result=DB.queryByExample(temp);
			Ballot x;
			if(result.hasNext())
			{
				x=(Ballot)result.next();
				updateEndBallot(x);
				return x;
			}
			return null;
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
	public List<Ballot> retrieveAll() {
		List<Ballot> ballots=new LinkedList<Ballot>();
		open();
		try
		{
				Query query=DB.query();
				query.constrain(Ballot.class);
				ObjectSet result = query.execute();
				Ballot x;
				while(result.hasNext())
				{
					x=(Ballot)result.next();
					updateEndBallot(x);
					ballots.add(x);
				}
				System.out.println("[DB4O]All ballots was retrieved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ballots could not be retrieved");
			ballots.clear();
			return ballots;
		}
		finally
		{
			close();
		}
		
		return ballots;
	}

	public List<Ballot> getById(List<String> ids) {
		open();
		List<Ballot> list=new LinkedList<Ballot>();
		try
		{
			Ballot temp=new Ballot();
			Ballot x;
			for(int i=0;i<ids.size();i++)
			{
				temp.setId(ids.get(i));
				ObjectSet result=DB.queryByExample(temp);
				if(result.hasNext())
				{
					x=(Ballot)result.next();
					updateEndBallot(x);
					list.add(x);
				}
			}
			return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			list.clear();
			return list;
		}
		finally
		{
			close();
		}
	}
	public List<Ballot> getEndedNotCountedById(List<String> ids)
	{
		open();
		List<Ballot> list=new LinkedList<Ballot>();
		try
		{
			Ballot temp=new Ballot();
			temp.setEnded(true);
			Ballot x;
			for(int i=0;i<ids.size();i++)
			{
				temp.setId(ids.get(i));
				ObjectSet result=DB.queryByExample(temp);
				if(result.hasNext())
				{
					x=(Ballot)result.next();
					updateEndBallot(x);
					if(!x.isCounted())
						{list.add(x);}
				}
			}
			return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			list.clear();
			return list;
		}
		finally
		{
			close();
		}
	}
	
	public List<Ballot> getByOwnerId(String idOwner)
	{
		open();
		List<Ballot> list=new LinkedList<Ballot>();
		try
		{
			Ballot temp=new Ballot();
			temp.setIdOwner(idOwner);
			ObjectSet result=DB.queryByExample(temp);
			Ballot x;
			while(result.hasNext())
			{
				x=(Ballot)result.next();
				updateEndBallot(x);
				list.add(x);
			}
			return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			list.clear();
			return list;
		}
		finally
		{
			close();
		}
		
	}
	
	public List<Ballot> getByIdCensus(String idCensus) {
		open();
		List<Ballot> list=new LinkedList<Ballot>();
		try
		{
			Ballot temp=new Ballot();
			temp.setIdCensus(idCensus);
			ObjectSet result=DB.queryByExample(temp);
			Ballot x;
			while(result.hasNext())
			{
				x=(Ballot)result.next();
				updateEndBallot(x);
				list.add(x);
			}
			return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			list.clear();
			return list;
		}
		finally
		{
			close();
		}
		
	}
	//************************************************* Delete ****************************************************//
	public void deleteBallotById(String id) {
		open();
		try
		{
			Ballot temp=new Ballot();
			temp.setId(id);
			ObjectSet result=DB.queryByExample(temp);
			if(result.hasNext())
			{
				DB.delete((Ballot)result.next());
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

	public void updateBallot(Ballot updatedBallot)
	{
		open();
		try
		{
			Ballot temp=new Ballot();
			temp.setId(updatedBallot.getId());
			ObjectSet result=DB.queryByExample(temp);
			if(result.hasNext())
			{
				DB.delete((Ballot)result.next());
				DB.store(updatedBallot);
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
	
	
	
	
	
	
	//******************************************** tools **********************************************//
	
	//public
	public boolean isNameInUse(String name)
	{
		open();
		try
		{
			return testBallot(name);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			close();
		}
		return true;
	}
	
	private boolean updateEndBallot(Ballot ballot)//Marca como terminada la votacion y la actualiza
	{
		if(!ballot.isEnded())
		{
			calEnd.setTime(ballot.getEndDate());
			if(calEnd.before(calAct))
			{
				DB.delete(ballot);
				ballot.setEnded(true);
				DB.store(ballot);
				return true;
			}
		}
		return false;
	}
	//internal
	private boolean testBallot(String ballotName)
	{
		Ballot temp=new Ballot();
		temp.setName(ballotName);
		
		Query query =DB.query();
		query.descend("name").constrain(ballotName).endsWith(false);
		ObjectSet result=query.execute();
		if(result.hasNext())
		{
			return true;
		}
		return false;
	}
	
	//********************************************Open and Close DB************************************//
	
			private void open()
			{
				config=Db4oEmbedded.newConfiguration();
				config.common().objectClass(Ballot.class).cascadeOnUpdate(true);
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
