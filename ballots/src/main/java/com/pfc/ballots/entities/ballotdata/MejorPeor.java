
package com.pfc.ballots.entities.ballotdata;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Calculo.CalcMejorPeor;

/**
 * MejorPeor entity that contains the information of specific information
 * for a MejorPeor ballot
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 OCT-2015
 *
 */

public class MejorPeor {

	private String id;
	private String ballotId;
	private List<String> options;
	private List<String> votes;
	private List<String> winners;
	private Map<String,Integer> results;

	///////////////////////////////////////////// Constructors//////////////////////////////
	public MejorPeor()
	{

	}
	public MejorPeor(String nulltoinitialize)
	{
		setOptions(new LinkedList<String>());
		setVotes(new LinkedList<String>());
		setWinners(new LinkedList<String>());


	}
	public MejorPeor(List<String> options)
	{
		this.setOptions(options);
		setVotes(new LinkedList<String>());
		setWinners(new LinkedList<String>());
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
	public void setWinners(LinkedList<String> winners) {
		this.winners=winners;		
	}
	public List<String> getWinners()
	{
		return this.winners;
	}

	///////////////////////////////////////////////////////// tools ///////////////////////////////////////
	/**
	 * Add a vote to the list
	 * @param option vote to add
	 */
	public void addVote(String option)
	{
		if(votes==null)
		{
			setVotes(new LinkedList<String>());
		}
		votes.add(option);
	}
	/**
	 * Adds an option to the list
	 * @param option to add
	 */
	public void addOption(String option)
	{
		if(options==null)
		{
			setOptions(new LinkedList<String>());
		}
		options.add(option);
	}
	/**
	 * Retrives the number of votes of a option
	 * @param option to retrieve its result
	 * @return
	 */
	public int getResultOption(String option)
	{
		if(results!=null)
		{
			return results.get(option.toLowerCase());
		}
		return -1;
	}
	/**
	 * Calculate the result of the ballot(the calc method is in a external jar)
	 */
	public boolean calcularMejorPeor()
	{
		if(votes==null ||options==null)
		{
			return false;
		}
		else
		{
			this.winners=new LinkedList<String>();
			this.results=CalcMejorPeor.CalculateMejorPeor(options, votes, winners);
			return true;
		}
	}

}

