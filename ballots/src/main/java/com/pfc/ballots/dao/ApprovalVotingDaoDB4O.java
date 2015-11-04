package com.pfc.ballots.dao;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.RangeVotingText;
import com.pfc.ballots.entities.ApprovalVotingText;
import com.pfc.ballots.entities.ballotdata.ApprovalVoting;
import com.pfc.ballots.entities.ballotdata.ApprovalVoting;
import com.pfc.ballots.entities.ballotdata.RangeVoting;
import com.pfc.ballots.entities.ballotdata.RelativeMajority;
/**
 * 
 * Implementation of the interface ApprovalVotingDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
public class ApprovalVotingDaoDB4O implements ApprovalVotingDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;
	
	
	public ApprovalVotingDaoDB4O(String DBName)
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
	//********************************************Open and Close DB************************************//
		/**
		 * Opens database
		 */
		private void open()
		{
			config=Db4oEmbedded.newConfiguration();
			config.common().objectClass(RangeVoting.class).cascadeOnUpdate(true);
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
	public ApprovalVotingText getApprovalVotingText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(ApprovalVotingText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (ApprovalVotingText)result.next();
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
		return null;
	}

	public void deleteApprovalVotingText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(ApprovalVotingText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
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
	
	/**
	 * Updates the approval voting text
	 * @param about
	 */
	public void updateApprovalVotingText(ApprovalVotingText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(ApprovalVotingText.class);
			ObjectSet result=query.execute();
			while(result.hasNext())
			{
				DB.delete(result.next());
			}
			DB.store(text);
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
	public void deleteByBallotId(String ballotId) {
		open();
		try
		{
			ApprovalVoting approvalVoting=new ApprovalVoting();
			approvalVoting.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(approvalVoting);
			if(result.hasNext())
			{
				DB.delete((ApprovalVoting)result.next());
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
			ApprovalVoting approvalVoting=new ApprovalVoting();
			approvalVoting.setId(id);
			ObjectSet result=DB.queryByExample(approvalVoting);
			if(result.hasNext())
			{
				DB.delete((ApprovalVoting)result.next());
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
	 * Deletes all the Approval voting entities in the system
	 */
	public void deleteAll() {
		open();
		try
		{
				Query query=DB.query();
				query.constrain(ApprovalVoting.class);
				ObjectSet result = query.execute();
			
				while(result.hasNext())
				{
					DB.delete(result.next());
				}
				System.out.println("[DB4O]All ApprovalVoting was deleted");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All ApprovalVoting could not be deleted");
		
		}
		finally
		{
			close();
		}

	}
	public void store(ApprovalVoting approvalVoting) {
		open();
		try
		{
			DB.store(approvalVoting);
			System.out.println("[DB4O]Ballot stored");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:Ballot could not be stored");
		}
		finally
		{
			close();
		}
	}
	public ApprovalVoting getByBallotId(String idBallot)
	{
		open();
		try
		{
			ApprovalVoting approvalVoting=new ApprovalVoting();
			approvalVoting.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(approvalVoting);
			if(result.hasNext())
			{
				return (ApprovalVoting)result.next();
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
	
	public void update(ApprovalVoting updated)
	{
		open();
		try
		{
			ApprovalVoting approvalVoting=new ApprovalVoting();
			approvalVoting.setId(updated.getId());
			
			ObjectSet result=DB.queryByExample(approvalVoting);
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
}
