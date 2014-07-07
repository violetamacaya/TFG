package com.pfc.ballots.util;

import java.util.LinkedList;
import java.util.List;

public class GenerateDocentVotes {

	static public List<String> generateRelativeMajority(List<String> options,int numVotes)
	{
		List<String> votes=new LinkedList<String>();
		int tam=options.size()-1;
		for(int i=0;i<numVotes;i++)
		{
			int random=(int) Math.floor(Math.random()*tam);
			votes.add(options.get(random));
		}
		
		return votes;
	}
}
