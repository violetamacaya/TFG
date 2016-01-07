package com.pfc.ballots.dao;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.pfc.ballots.entities.VotoAcumulativoText;
import com.pfc.ballots.entities.ballotdata.VotoAcumulativo;
/**
 * 
 * Implementation of the interface VotoAcumulativoDao for the DB4O database
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2014
 *
 */
@SuppressWarnings("rawtypes")
public class VotoAcumulativoDaoDB4O implements VotoAcumulativoDao 
{
	String sep=System.getProperty("file.separator");
	String PATH;
	String ruta=System.getProperty("user.home")+sep+"BallotsFiles"+sep;
	EmbeddedConfiguration config = null;
	ObjectContainer DB=null;


	public VotoAcumulativoDaoDB4O(String DBName)
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
		config.common().objectClass(VotoAcumulativo.class).cascadeOnUpdate(true);
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
	/**
	 * Retrieves the VotoAcumulativo text
	 * @param about
	 */
	public VotoAcumulativoText getVotoAcumulativoText() {

		open();
		try
		{
			Query query=DB.query();
			query.constrain(VotoAcumulativoText.class);
			ObjectSet result=query.execute();
			if(result.hasNext())
			{
				return (VotoAcumulativoText)result.next();
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
	/**
	 * Deletes the VotoAcumulativo text
	 * @param about
	 */
	public void deleteVotoAcumulativoText()
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(VotoAcumulativoText.class);
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
	 * Updates the VotoAcumulativo text
	 * @param about
	 */
	public void updateVotoAcumulativoText(VotoAcumulativoText text)
	{
		open();
		try
		{
			Query query=DB.query();
			query.constrain(VotoAcumulativoText.class);
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
			VotoAcumulativo votoAcumulativo=new VotoAcumulativo();
			votoAcumulativo.setBallotId(ballotId);
			ObjectSet result=DB.queryByExample(votoAcumulativo);
			if(result.hasNext())
			{
				DB.delete((VotoAcumulativo)result.next());
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
			VotoAcumulativo votoAcumulativo=new VotoAcumulativo();
			votoAcumulativo.setId(id);
			ObjectSet result=DB.queryByExample(votoAcumulativo);
			if(result.hasNext())
			{
				DB.delete((VotoAcumulativo)result.next());
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
	 * Deletes all the VotoAcumulativo voting entities in the system
	 */
	public void deleteAll() {
		open();
		try
		{
			Query query=DB.query();
			query.constrain(VotoAcumulativo.class);
			ObjectSet result = query.execute();

			while(result.hasNext())
			{
				DB.delete(result.next());
			}
			System.out.println("[DB4O]All VotoAcumulativo was deleted");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("[DB4O]ERROR:All VotoAcumulativo could not be deleted");

		}
		finally
		{
			close();
		}

	}
	/**
	 * Stores a ballot entities
	 * @param about
	 */
	public void store(VotoAcumulativo votoAcumulativo) {
		open();
		try
		{
			DB.store(votoAcumulativo);
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
	
	/**
	 * Retrieves a ballot by Id
	 * @param about
	 */
	public VotoAcumulativo getByBallotId(String idBallot)
	{
		open();
		try
		{
			VotoAcumulativo votoAcumulativo=new VotoAcumulativo();
			votoAcumulativo.setBallotId(idBallot);
			ObjectSet result=DB.queryByExample(votoAcumulativo);
			if(result.hasNext())
			{
				return (VotoAcumulativo)result.next();
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
	/**
	 * Update a ballot by Id
	 * @param about
	 */
	public void update(VotoAcumulativo updated)
	{
		open();
		try
		{
			VotoAcumulativo votoAcumulativo=new VotoAcumulativo();
			votoAcumulativo.setId(updated.getId());

			ObjectSet result=DB.queryByExample(votoAcumulativo);
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
