package com.pfc.ballots.entities;

import java.util.LinkedList;
import java.util.List;

public class ProfileCensedIn {

	
	private String idProfile;
	private List<String> inCensus;
	
	/**
	 * Usar este constructor para realizar busquedas en DAOs
	 */
	public ProfileCensedIn()
	{
		
	}
	/**
	 * Usar este constructor por defiecto, aun poniendo a null idProfile
	 * y dejar el constructor vacio principalmente para realizar busquedas en DAOs
	 * @param idProfle
	 */
	public ProfileCensedIn(String idProfle)
	{
		this.idProfile=idProfile;
		inCensus = new LinkedList<String>();
	}
	
	/**
	 * Usar este constructor para realizar una copia del censo(copiando la lista por elementos, no la referencia)
	 * @param old
	 */
	public ProfileCensedIn(ProfileCensedIn old)
	{
	
		this.idProfile=old.getIdProfile();
		inCensus=new LinkedList<String>();
		for(String current:old.getInCensus())
		{
			inCensus.add(current);
		}
	}
	

	
	public void setIdProfile(String idProfile)
	{
		this.idProfile=idProfile;
	}
	public String getIdProfile()
	{
		return this.idProfile;
	}
	public void setInCensus(List<String> inCensus)
	{
		this.inCensus=inCensus;
	}
	public List<String> getInCensus()
	{
		return inCensus;
	}
	public void addIdCensus(String idCensus)
	{
		if(inCensus==null)
		{
			inCensus= new LinkedList<String>();
		}
		inCensus.add(idCensus);
	}
	public void addIdCensus(List<String> idCensus)
	{
		if(inCensus==null)
		{
			inCensus= new LinkedList<String>();
		}
		
		if(idCensus!=null)
		{
			for(String current:idCensus)
			{
				inCensus.add(current);
			}
		}
	}
	public boolean removeIdCensus(String idCensus)
	{
		if(inCensus!=null)
		{
			for(String current:inCensus)
			{
				if(current.equals(idCensus))
				{
					inCensus.remove(current);
					return true;
				}
			}	
		}
		return false;
	}
	public void removeIdCensus(List<String>idCensus)
	{
		if(inCensus!=null && idCensus!=null)
		{
			for(String current:idCensus)
			{
				removeIdCensus(current);
			}
		}
	}
	public void clearInCensus()
	{
		if(inCensus!=null)
		{
			inCensus.clear();
		}
	}

}
