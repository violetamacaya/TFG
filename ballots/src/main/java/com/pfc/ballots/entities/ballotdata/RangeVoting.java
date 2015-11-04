package com.pfc.ballots.entities.ballotdata;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Calculo.CalcKemeny;
import com.Calculo.CalcRangeVoting;
/**
 * RangeVoting entity that contains the information of specific information
 * for a Range Voting ballot
 * 
 * @author Mario Temprano Martin
 * @version 1.0 SEP-2014
 *
 */
public class RangeVoting {

	
	
	private String ballotId;
	private String id;
	private Integer minValue;
	private Integer maxValue;
	private List<String>options;
	private List<List<String>>votes;
	private List<String> winner;
	private Map<String,Integer>results;
	
	
	
	
	public RangeVoting()
	{
		
	}
	
	public RangeVoting(String nulltoinitialize)
	{
		setOptions(new LinkedList<String>());
		setVotes(new LinkedList<List<String>>());
		setWinner(new LinkedList<String>());
		setMinValue(new Integer(0));
		setMaxValue(new Integer(0));
	}
	public RangeVoting(List<String> options)
	{
		setOptions(options);
		setVotes(new LinkedList<List<String>>());
		setWinner(new LinkedList<String>());
		setMinValue(new Integer(0));
		setMaxValue(new Integer(0));
	}
	
	
	public String getBallotId() {
		return ballotId;
	}
	
	
	public void setBallotId(String ballotId) {
		this.ballotId = ballotId;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Integer getMinValue() {
		return minValue;
	}


	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}


	public Integer getMaxValue() {
		return maxValue;
	}


	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}


	public List<String> getOptions() {
		return options;
	}


	public void setOptions(List<String> options) {
		this.options = options;
	}


	public List<List<String>> getVotes() {
		return votes;
	}


	public void setVotes(List<List<String>> votes) {
		this.votes = votes;
	}


	public List<String> getWinner() {
		return winner;
	}


	public void setWinner(List<String> winner) {
		this.winner = winner;
	}
	
	
	/**
	 * Adds a vote to the list
	 * @param vote
	 */
	public void addVote(List<String> vote)
	{
		if(votes==null)
		{
			votes=new LinkedList<List<String>>();
		}
		if(vote!=null)
			{votes.add(vote);}
	}
	/**
	 * if a ballot is counted, retrieve the result
	 * @param option
	 * @return
	 */
	public int getResult(String option)
	{
		if(option==null || results==null)
		{
			return -1;
		}
		else
		{
			return results.get(option);
		}
	}
	/**
	 * Calculate the result of the ballot(the calc method is in a external jar)
	 */
	public boolean calcularRangeVoting()
	{
		if(votes==null ||options==null || winner==null)
		{
			return false;
		}
		else
		{
			results=CalcRangeVoting.CalculateRangeVoting(options, minValue, maxValue, votes, winner);
			return true;
		}
	}
}
