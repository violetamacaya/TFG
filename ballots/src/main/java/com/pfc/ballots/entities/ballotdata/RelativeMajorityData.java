package com.pfc.ballots.entities.ballotdata;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RelativeMajorityData {

	private String id;
	private String ballotId;
	private List<String> options;
	private Map<String,Integer> results;
	
	///////////////////////////////////////////// Constructors//////////////////////////////
	public RelativeMajorityData()
	{
		setOptions(new LinkedList<String>());
		results=new HashMap<String,Integer>();
	}
	public RelativeMajorityData(List<String> options)
	{
		this.setOptions(options);
		results=new HashMap<String,Integer>();
		for(String temp:options)
		{
	    	results.put(temp, new Integer(0));
		}
	}
	
	///////////////////////////////////////////////// getter setter ////////////////////////////////////////////
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBallotId() {
		return ballotId;
	}
	public void setBallotId(String ballotId) {
		this.ballotId = ballotId;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public Map<String,Integer> getResults() {
		return results;
	}
	public void setResults(Map<String,Integer> results) {
		this.results = results;
	}
	
	///////////////////////////////////////////////////////// tools ///////////////////////////////////////
	
	public int addVote(String option)
	{
		Integer votes=results.get(option);
		results.put(option,votes+1);
		return votes+1;
	}
	public int addVote(String option,int numVotes)
	{
		Integer votes=results.get(option);
		results.put(option,votes+numVotes);
		return votes+numVotes;
	}
	public int getVotes(String option)
	{
		return results.get(option);
	}

}
