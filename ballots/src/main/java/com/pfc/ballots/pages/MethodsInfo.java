package com.pfc.ballots.pages;

import com.pfc.ballots.pages.Methods.ApprovalVotingMethod;
import com.pfc.ballots.pages.Methods.BlackMethod;
import com.pfc.ballots.pages.Methods.BordaMethod;
import com.pfc.ballots.pages.Methods.BramsMethod;
import com.pfc.ballots.pages.Methods.BucklinMethod;
import com.pfc.ballots.pages.Methods.CondorcetMethod;
import com.pfc.ballots.pages.Methods.CoombsMethod;
import com.pfc.ballots.pages.Methods.CopelandMethod;
import com.pfc.ballots.pages.Methods.DodgsonMethod;
import com.pfc.ballots.pages.Methods.HareMethod;
import com.pfc.ballots.pages.Methods.JuicioMayoritarioMethod;
import com.pfc.ballots.pages.Methods.KemenyMethod;
import com.pfc.ballots.pages.Methods.MejorPeorMethod;
import com.pfc.ballots.pages.Methods.NansonMethod;
import com.pfc.ballots.pages.Methods.RangeVotingMethod;
import com.pfc.ballots.pages.Methods.RelativeMajorityMethod;
import com.pfc.ballots.pages.Methods.SchulzeMethod;
import com.pfc.ballots.pages.Methods.SmallMethod;
import com.pfc.ballots.pages.Methods.VotoAcumulativoMethod;

public class MethodsInfo {

	Object onMenu(String section)
	{
		Object page=null;
		
		if(section.equals("majory"))
		{
			page=RelativeMajorityMethod.class;
		}
		else if(section.equals("kemeny"))
		{
			page=KemenyMethod.class;
		}
		else if(section.equals("borda"))
		{
			page=BordaMethod.class;
		}
		else if(section.equals("rangeVoting"))
		{
			page=RangeVotingMethod.class;
		}
		else if(section.equals("approvalVoting"))
		{
			page=ApprovalVotingMethod.class;
		}
		else if(section.equals("black"))
		{
			page=BlackMethod.class;
		}
		else if(section.equals("brams"))
		{
			page=BramsMethod.class;
		}
		else if(section.equals("bucklin"))
		{
			page=BucklinMethod.class;
		}
		else if(section.equals("condorcet"))
		{
			page=CondorcetMethod.class;
		}
		else if(section.equals("coombs"))
		{
			page=CoombsMethod.class;
		}
		else if(section.equals("copeland"))
		{
			page=CopelandMethod.class;
		}
		else if(section.equals("dodgson"))
		{
			page=DodgsonMethod.class;
		}
		else if(section.equals("hare"))
		{
			page=HareMethod.class;
		}
		else if(section.equals("juiciomayoritario"))
		{
			page=JuicioMayoritarioMethod.class;
		}
		else if(section.equals("mejorPeor"))
		{
			page=MejorPeorMethod.class;
		}
		else if(section.equals("nanson"))
		{
			page=NansonMethod.class;
		}
		else if(section.equals("schulze"))
		{
			page=SchulzeMethod.class;
		}
		else if(section.equals("small"))
		{
			page=SmallMethod.class;
		}
		else if(section.equals("votoAcumulativo"))
		{
			page=VotoAcumulativoMethod.class;
		}	
	return page;
	}
}
