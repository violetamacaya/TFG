package com.pfc.ballots.entities.ballotdata;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Calculo.CalcBorda;


/**
 * Borda entity that contains the information of specific information
 * for a borda ballot
 * 
 * @author Mario Temprano Martin
 * @version 1.0 SEP-2014
 *
 */
public class Borda {

	private String id;
	private String ballotId;
	private List<String> categories;
	private List<List<String>> options;
	private List<String> bordaOptions;
	private List<List<String>> votes;
	private List<String> winner;
	private Map<String,Integer> results;
	
	public Borda()
	{
		
	}
	public Borda (String nulltoinitialize)
	{
		setCategories(new LinkedList<String>());
		setOptions(new LinkedList<List<String>>());
		setBordaOptions(new LinkedList<String>());
		setVotes(new LinkedList<List<String>>());
		setWinner(new LinkedList<String>());
	}
	public Borda(List<List<String>>options)
	{
		setCategories(new LinkedList<String>());
		setOptions(options);
		setBordaOptions(CalcBorda.CalcularOpcionesBorda(1, options));
		setVotes(new LinkedList<List<String>>());
		setWinner(new LinkedList<String>());
	}
	public Borda(List<List<String>>options,List<String> categories)
	{
		setCategories(categories);
		setOptions(new LinkedList<List<String>>());
		setBordaOptions(new LinkedList<String>());
		setVotes(new LinkedList<List<String>>());
		setWinner(new LinkedList<String>());
	}
	
	
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
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public List<List<String>> getOptions() {
		return options;
	}
	public void setOptions(List<List<String>> options) {
		this.options = options;
	}
	public List<String> getBordaOptions() {
		return bordaOptions;
	}
	public void setBordaOptions(List<String> bordaOptions) {
		this.bordaOptions = bordaOptions;
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
	public Map<String,Integer> getResults() {
		return results;
	}
	public void setResults(Map<String,Integer> results) {
		this.results = results;
	}


	/**
	 * Add a category to the list
	 * @param category
	 */
	public void addCategory(String category)
	{
		if(categories==null)
		{
			setCategories(new LinkedList<String>());
		}
		if(category!=null)
		{
			categories.add(category);
		}
	}
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
	 * if a ballot is counted, retrieve the result
	 * @param String bordaOption
	 * @return
	 */
	public int getResult(String bordaOption)
	{
		if(bordaOption==null || results==null)
		{
			return -1;
		}
		else
		{
			return results.get(bordaOption);
		}
	}
	/**
	 * Calculate the result of the ballot(the calc method is in a external jar)
	 */
	public boolean calcularBorda()
	{
		if(votes==null ||options==null || winner==null || bordaOptions==null)
		{
			return false;
		}
		else
		{
			results=CalcBorda.CalcularBorda(options, bordaOptions, votes, winner);
			return true;
		}
	
	}
	
}
