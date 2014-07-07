package com.pfc.ballots.entities.ballotdata;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RelativeMajority {

	private String id;
	private String ballotId;
	private List<String> options;
	private List<String> votes;
	private Map<String,Integer> results;
	
	///////////////////////////////////////////// Constructors//////////////////////////////
	public RelativeMajority()
	{
		
	}
	public RelativeMajority(String nulltoinitialize)
	{
		setOptions(new LinkedList<String>());
		setVotes(new LinkedList<String>());
	
	}
	public RelativeMajority(List<String> options)
	{
		this.setOptions(options);
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
	public List<String> getVotes() {
		return votes;
	}
	public void setVotes(List<String> votes) {
		this.votes = votes;
	}
	public Map<String,Integer> getResults() {
		return results;
	}
	public void setResults(Map<String,Integer> results) {
		this.results = results;
	}

	///////////////////////////////////////////////////////// tools ///////////////////////////////////////
	
	public void addVote(String option)
	{
		if(votes==null)
		{
			setVotes(new LinkedList<String>());
		}
		votes.add(option);
	}
	public void addOption(String option)
	{
		if(options==null)
		{
			setOptions(new LinkedList<String>());
		}
		options.add(option);
	}
	public int getResultOption(String option)
	{
		if(results!=null)
		{
			return results.get(option);
		}
		return -1;
	}
	

}
