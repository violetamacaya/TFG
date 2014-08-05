package com.pfc.ballots.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * Census entity that contains the information of a census
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 *
 */
public class Census {

	private String id;
	private String idOwner;
	private String email;
	private String censusName;
	private List<String> usersCounted;

	public Census()
	{
		
	}
	
	public Census(Census old)
	{
		this.id=old.id;
		this.idOwner=old.getIdOwner();
		this.email=old.getEmail();
		this.censusName=old.getCensusName();
		usersCounted=new LinkedList<String>();
		for(String current:old.getUsersCounted())
		{
			usersCounted.add(current);
		}
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdOwner() {
		return idOwner;
	}
	public void setIdOwner(String idOwner) {
		this.idOwner = idOwner;
	}
	public String getCensusName() {
		return censusName;
	}
	public void setCensusName(String censusName) {
		this.censusName = censusName;
	}
	public List<String> getUsersCounted() {
		return usersCounted;
	}
	public void setUsersCounted(List<String> usersCounted) {
		this.usersCounted = usersCounted;
	}

	/**
	 * Adds an user to the census
	 * @param id of the user to add
	 */
	public void addIdToUsersCounted(String id)
	{
		if(usersCounted==null)
		{
			usersCounted=new LinkedList<String>();
		}
		usersCounted.add(id);
	}
	/**
	 * Deletes all users of the census
	 */
	public void emptyUsersCounted()
	{
		usersCounted.clear();
	}
	/**
	 * Removes the user from its id of the census
	 * @param id of the user to remove
	 */
	public void removeIdOfUsersCounted(String id)
	{
		if(usersCounted!=null)
		{
			for(String temp:usersCounted)
			{
				if(temp.equals(id))
				{
					usersCounted.remove(temp);
				}
			}
		}
	}
	
	/**
	 * Retrieves the number of user in the census
	 * @return int
	 */
	public int getNumberOfCensed()
	{
		return usersCounted.size();
	}
	/**
	 * Retrieves the email of the creator of the census
	 * @return String
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Set the email of the creator of the census
	 * @param email of the creator
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 *	Compare this census with the @Param census and
	 *	returns the users @Param added and @Param removed of the census
	 */
	public void calcDifference(Census updated,List<String>added,List<String>removed)
	{
		Map<String,String> map=new HashMap<String,String>();
		
		if(updated.getUsersCounted()!=null)
		{
			for(String current:this.getUsersCounted())
			{
				map.put(current, "Removed");
			}
			for(String current:updated.getUsersCounted())
			{
				if(map.get(current)!=null)
				{
					map.put(current,"Current");
				}
				else
				{
					added.add(current);//add the new user
				}
			}
			for(String current:this.getUsersCounted())
			{
				if(map.get(current).equals("Removed"))
				{
					removed.add(current);
				}
			}
			
		}
	}

	
	
}
