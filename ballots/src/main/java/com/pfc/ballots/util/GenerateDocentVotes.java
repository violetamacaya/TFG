package com.pfc.ballots.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * Generates random votes for the ballots
 * 
 * @author Mario Temprano Martin
 * @version JUL-2014
 * 
 */
public class GenerateDocentVotes {

	/**
	 * Generates a list of random votes for a relative majority ballot
	 * 
	 * @param options options of the ballot
	 * @param numVotes number of votes to generate
	 * @return List<String> a list of random votes
	 */
	static public List<String> generateRelativeMajority(List<String> options,int numVotes)
	{
		List<String> votes=new LinkedList<String>();
		for(int i=0;i<numVotes;i++)
		{
			Random random=new Random();
			votes.add(options.get(random.nextInt(options.size())));
		}
		
		return votes;
	}
	/**
	 * Generates a list of random votes for a kemeny ballot
	 * @param kemenyOptions lists of options of the ballots
	 * @param numVotes number of votes to generate
	 * @return List<List<String>> list of random votes (a vote is a list)
	 */
	static public List<List<String>> generateKemeny(List<String> kemenyOptions,int numVotes)
	{
		Map<String,Integer> votesPerOption=new HashMap<String,Integer>();
		 List<List<String>>votes=new LinkedList<List<String>>();
		
		for(String option:kemenyOptions)
		{
			votesPerOption.put(option, 0);
		}
		
		for(int i=1; i<=numVotes;i++)
		{
			List<String> vote=new LinkedList<String>();
			for(int x=0;x<kemenyOptions.size();x++)
			{
				boolean notSelected=true;
				while(notSelected)
				{
					Random random=new Random();
					int numOption=random.nextInt(kemenyOptions.size());
					if(votesPerOption.get(kemenyOptions.get(numOption))<i)
					{
						notSelected=false;
						vote.add(kemenyOptions.get(numOption));
						votesPerOption.put(kemenyOptions.get(numOption), i);
					}
				}
			}
			votes.add(vote);
		}
		
		return votes;
		
	}
	/**
	 * Generates a list of random votes for a borda ballot
	 * 
	 * @param bordaOptions options of the ballot
	 * @param numVotes number of votes to generate
	 * @return List<String> a list of random votes
	 */
	
	static public List<List<String>> generateBorda(List<String> bordaOptions,int numVotes)
	{
		Map<String,Integer> votesPerOption=new HashMap<String,Integer>();
		 List<List<String>>votes=new LinkedList<List<String>>();
		
		for(String option:bordaOptions)
		{
			votesPerOption.put(option, 0);
		}
		
		for(int i=1; i<=numVotes;i++)
		{
			List<String> vote=new LinkedList<String>();
			for(int x=0;x<bordaOptions.size();x++)
			{
				boolean notSelected=true;
				while(notSelected)
				{
					Random random=new Random();
					int numOption=random.nextInt(bordaOptions.size());
					if(votesPerOption.get(bordaOptions.get(numOption))<i)
					{
						notSelected=false;
						vote.add(bordaOptions.get(numOption));
						votesPerOption.put(bordaOptions.get(numOption), i);
					}
				}
			}
			votes.add(vote);
		}
		
		return votes;
	}
	
	
	
	/**
	 * Generates a list of random votes for a RangeVoting ballot
	 * 
	 * @param options options of the ballot
	 * @param numVotes number of votes to generate
	 * @param min minimum value
	 * @param max maximum value
	 * @return List<String> a list of random votes
	 */
	static public List<List<String>> generateRangeVoting(List<String>options, int numVotes,int min,int max)
	{
	
		 List<List<String>>votes=new LinkedList<List<String>>();
		
		
		for(int x=0;x<numVotes;x++)
		{
			List<String>vote=new LinkedList<String>();
			for(String option:options)
			{
				Random random=new Random();
				int valor=random.nextInt(max-min+1)+min;
				vote.add(String.valueOf(valor));				
			}
			votes.add(vote);
		}
		return votes;
		
	}

	/**
	 * Generates a list of random votes for a Approval voting ballot
	 * 
	 * @param options options of the ballot
	 * @param numVotes number of votes to generate
	 * @return List<String> a list of random votes
	 */
	static public List<String> generateApprovalVoting(List<String> options,int numVotes)
	{
		List<String> votes=new LinkedList<String>();
		
		//Cada uno de los usuarios puede votar el numero de opciones que quiera, entre una y todas. 
		for(int i=0; i<numVotes; i++){
			System.out.println("burlando la ley");
			int[] votospersona = new int[options.size()];
			int numuservotes = 1 + (int)(Math.random() * ((options.size() - 1) + 1));
			for (int j = 0; j<numuservotes; j++){
				System.out.println("numvotos por persona " +j );

				int posvoto = 0 + (int)(Math.random() * ((votospersona.length - 1) + 1));
				if(votospersona[posvoto] == 0){
					votospersona[posvoto] = 1;
					votes.add(options.get(posvoto));
				}
				else {
					j--;
				}
			}	
		}
		
		return votes;
	}
}
