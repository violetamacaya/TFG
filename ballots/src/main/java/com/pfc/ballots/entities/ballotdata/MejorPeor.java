
package com.pfc.ballots.entities.ballotdata;

import java.util.HashMap;
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
	private List<String> votesPos;
	private List<String> votesNeg;
	private List<String> winners;
	private HashMap<String,Integer> results;

	///////////////////////////////////////////// Constructors//////////////////////////////
	public MejorPeor()
	{

	}
	public MejorPeor(String nulltoinitialize)
	{
		setOptions(new LinkedList<String>());
		setVotesPos(new LinkedList<String>());
		setVotesNeg(new LinkedList<String>());
		setWinners(new LinkedList<String>());


	}
	public MejorPeor(List<String> options)
	{
		this.setOptions(options);
		setVotesPos(new LinkedList<String>());
		setVotesNeg(new LinkedList<String>());
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
	public List<String> getVotesPos() {
		return votesPos;
	}
	public void setVotesPos(List<String> votesPos) {
		this.votesPos = votesPos;
	}
	public List<String> getVotesNeg() {
		return votesNeg;
	}
	public void setVotesNeg(List<String> votesNeg) {
		this.votesNeg = votesNeg;
	}
	
	public Map<String, Integer> getVotes(){
		return results;
	}
	public HashMap<String,Integer> getResults() {
		return results;
	}
	public void setResults(HashMap<String,Integer> results) {
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
	public void addVotePos(String option)
	{
		if(votesPos==null)
		{
			setVotesPos(new LinkedList<String>());
		}
		votesPos.add(option);
	}
	/**
	 * Add a vote to the list
	 * @param option vote to add
	 */
	public void addVoteNeg(String option)
	{
		if(votesNeg==null)
		{
			setVotesNeg(new LinkedList<String>());
		}
		votesNeg.add(option);
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
			return results.get(option);
		}
		return -1;
	}
	/**
	 * Calculate the result of the ballot(the calc method is in a external jar)
	 */
	public boolean calcularMejorPeor()
	{
		if(votesPos==null  || votesNeg==null ||options==null)
		{
			return false;
		}
		else
		{
			this.winners=new LinkedList<String>();
			this.results=(HashMap<String, Integer>) CalcMejorPeor.CalculateMejorPeor(options, votesPos, votesNeg);

			return true;
		}
	}

}

