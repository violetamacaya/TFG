package com.pfc.ballots.entities.ballotdata;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Calculo.CalcKemeny;
public class Kemeny {

	private String ballotId;
	private String id;
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
		setOptionPairs(new LinkedList<String>());
		setVotes(new LinkedList<List<String>>());
		setWinner(new LinkedList<String>());
		setPermutations(new LinkedList<String>());
	}
	public Kemeny(List<List<String>>options)
	{
		setOptions(options);
		setOptionPairs(CalcKemeny.CalcularOpcionesKemeny(1, options));
		setVotes(new LinkedList<List<String>>());
		setWinner(new LinkedList<String>());
		setPermutations(new LinkedList<String>());
		
		
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
	public void addVote(List<String> vote)
	{
		if(votes==null)
		{
			votes=new LinkedList<List<String>>();
		}
		if(vote!=null)
			{votes.add(vote);}
	}
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
}
