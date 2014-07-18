package com.pfc.ballots.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GenerateDocentVotes {

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
