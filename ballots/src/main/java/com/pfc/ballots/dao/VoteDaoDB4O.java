package com.pfc.ballots.dao;

import java.util.LinkedList;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.pfc.ballots.entities.Vote;

public class VoteDaoDB4O implements VoteDao {
	
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	
	public VoteDaoDB4O(String DBName)
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
	//********************************************* store *********************************************//
	public void store(Vote vote)
	{
		open();
		try
		{
			DB.store(vote);
			System.out.println("[DB4O]Vote stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Vote could not be stored");
		}
		finally
		{
			close();
		}
	}
	public void store(List<Vote> votes)
	{
		open();
		try
		{
			for(Vote temp:votes)
			{
				DB.store(temp);
			}
			System.out.println("[DB4O]Votes stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Vote could not be stored");
		}
		finally
		{
			close();
		}
	}
	//******************************************** getters*********************************************//
	
	public List<Vote> getBallotVotes(String idBallot)
	{
		open();
		List<Vote> list=new LinkedList<Vote>();
		try
		{
			Vote temp=new Vote();
			temp.setIdBallot(idBallot);
			ObjectSet result=DB.queryByExample(temp);
			while(result.hasNext())
			{
				list.add((Vote)result.next());
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
	public Vote getVoteByIds(String idBallot,String idUser)
	{
		open();
		try
		{
			ObjectSet result=DB.queryByExample(new Vote(idBallot,idUser));
			if(result.hasNext())
			{
				return (Vote)result.next();
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
	public List<Vote> getVotesUser(String idUser)
	{
		open();
		List<Vote> list=new LinkedList<Vote>();
		try
		{
			Vote temp=new Vote();
			temp.setIdUser(idUser);
			ObjectSet result=DB.queryByExample(temp);
			while(result.hasNext())
			{
				list.add((Vote)result.next());
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
	public List<String> getBallotsWithParticipation(String idUser)
	{
		open();
		List<String> list=new LinkedList<String>();
		try
		{
			Vote temp=new Vote();
			temp.setIdUser(idUser);
			ObjectSet result=DB.queryByExample(temp);
			while(result.hasNext())
			{
				list.add((String)result.next());
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
	public boolean isVoted(String idBallot,String idUser)
	{
		open();
		try
		{
			ObjectSet result=DB.queryByExample(new Vote(idBallot,idUser));
			if(result.hasNext())
			{
				return ((Vote)result.next()).isCounted();
			}
			return false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			close();
		}
	}
	//************************************************* Update ****************************************//
		public void updateVote(Vote vote)
		{
			open();
			try
			{
				ObjectSet result=DB.queryByExample(new Vote(vote.getIdBallot(),vote.getIdUser()));
				if(result.hasNext())
				{
					DB.delete(result.next());
					DB.store(vote);
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
	//************************************************* Delete ****************************************//
	public void deleteVoteOfBallot(String idBallot)
	{
		open();
		try
		{
			Vote temp=new Vote();
			temp.setIdBallot(idBallot);
			ObjectSet result=DB.queryByExample(temp);
			while(result.hasNext())
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
	//********************************************Open and Close DB************************************//
	
	private void open()
	{
		config=Db4oEmbedded.newConfiguration();
		config.common().objectClass(Vote.class).cascadeOnUpdate(true);
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
