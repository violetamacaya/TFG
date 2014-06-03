package com.pfc.ballots.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Census {

	private String id;
	private String idOwner;
	private String email;
	private String censusName;
	private List<String> usersCounted;

	
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

	public void addIdToUsersCounted(String id)
	{
		if(usersCounted==null)
		{
			usersCounted=new LinkedList<String>();
		}
		usersCounted.add(id);
	}
	public void emptyUsersCounted()
	{
		usersCounted.clear();
	}
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
	
	
	public int getNumberOfCensed()
	{
		return usersCounted.size();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
