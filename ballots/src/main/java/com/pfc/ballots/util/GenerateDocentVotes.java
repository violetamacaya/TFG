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
}
