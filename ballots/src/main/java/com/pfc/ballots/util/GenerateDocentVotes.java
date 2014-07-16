package com.pfc.ballots.util;

import java.util.LinkedList;
import java.util.List;
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
}
