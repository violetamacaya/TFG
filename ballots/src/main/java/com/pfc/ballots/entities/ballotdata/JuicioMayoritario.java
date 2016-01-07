
package com.pfc.ballots.entities.ballotdata;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Calculo.CalcJuicioMayoritario;

/**
 * JuicioMayoritario entity that contains the information of specific information
 * for a JuicioMayoritario ballot
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 OCT-2015
 *
 */

public class JuicioMayoritario {

	private String id;
	private String ballotId;
	private List<String> options;
	private List<List<String>> votes;
	private List<String> winners;
	private Map<String,Float> results;

	///////////////////////////////////////////// Constructors//////////////////////////////
	public JuicioMayoritario()
	{

	}
	public JuicioMayoritario(String nulltoinitialize)
	{
		setOptions(new LinkedList<String>());
		setVotes(new LinkedList<List<String>>());
		setWinners(new LinkedList<String>());


	}
	public JuicioMayoritario(List<String> options)
	{
		this.setOptions(options);
		setVotes(new LinkedList<List<String>>());
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
	public List<List<String>> getVotes() {
		return votes;
	}
	public void setVotes(List<List<String>> votes) {
		this.votes = votes;
	}
	public Map<String,Float> getResults() {
		return results;
	}
	public void setResults(Map<String,Float> results) {
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
	 * Adds a vote to the list
	 * @param List<String> vote
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
	public Float getResultOption(String option)
	{
		if(results!=null)
		{
			return results.get(option);
		}
		return (float) -1;
	}
	/**
	 * Calculate the result of the ballot(the calc method is in a external jar)
	 */
	public boolean calcularJuicioMayoritario()
	{
		if(votes==null ||options==null)
		{
			return false;
		}
		else
		{
			this.results=CalcJuicioMayoritario.CalculateJuicioMayoritario(options, votes);
			return true;
		}
	}

}

