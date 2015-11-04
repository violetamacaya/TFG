package com.pfc.ballots.entities.ballotdata;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Calculo.CalcKemeny;

/**
 * Kemeny entity that contains the information of specific information
 * for a kemeny ballot
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUL-2014
 *
 */

public class Kemeny {

	private String ballotId;
	private String id;
	private List<String> categories;
	private List<List<String>>options;
	private List<String> optionPairs;
	private List<String> permutations;
	private List<List<String>>votes;
	private List<String> winner;
	private Map<String,Integer>results;
	
	public Kemeny()
	{
	}
	public Kemeny(String nulltoinitalize)
	{
		setOptions(new LinkedList<List<String>>());
		setCategories(new LinkedList<String>());
		setOptionPairs(new LinkedList<String>());
		setVotes(new LinkedList<List<String>>());
		setWinner(new LinkedList<String>());
		setPermutations(new LinkedList<String>());
	}
	public Kemeny(List<List<String>>options)
	{
		setCategories(new LinkedList<String>());
		setOptions(options);
		setOptionPairs(CalcKemeny.CalcularOpcionesKemeny(1, options));
		setVotes(new LinkedList<List<String>>());
		setWinner(new LinkedList<String>());
		setPermutations(new LinkedList<String>());
	}
	public Kemeny(List<List<String>>options,List<String> categories)
	{
		setOptions(options);
		setOptionPairs(CalcKemeny.CalcularOpcionesKemeny(1, options));
		setVotes(new LinkedList<List<String>>());
		setWinner(new LinkedList<String>());
		setPermutations(new LinkedList<String>());
		setCategories(categories);
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
	
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public List<String> getOptionPairs() {
		return optionPairs;
	}
	public void setOptionPairs(List<String> optionPairs) {
		this.optionPairs = optionPairs;
	}
	public List<String> getPermutations() {
		return permutations;
	}
	public void setPermutations(List<String> permutations) {
		this.permutations = permutations;
	}
	public List<List<String>> getOptions() {
		return options;
	}
	public void setOptions(List<List<String>> options) {
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
	 * @param permutation
	 * @return
	 */
	public int getResult(String permutation)
	{
		if(permutation==null || results==null)
		{
			return -1;
		}
		else
		{
			return results.get(permutation);
		}
	}
	/**
	 * Calculate the result of the ballot(the calc method is in a external jar)
	 */
	public boolean calcularKemeny()
	{
		if(votes==null ||options==null || winner==null)
		{
			return false;
		}
		else
		{
			results=CalcKemeny.CalculateKemeny(options, votes, permutations, winner);
			return true;
		}
	}
}
