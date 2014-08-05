package com.pfc.ballots.encoder;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;

import com.pfc.ballots.entities.Census;
/**
 * Encoder to display and save the correct values for the 
 * census in the tapestr5 select component
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUL-2014
 * 
 *
 */
public class CensusEncoder implements ValueEncoder<Census>{

	private List<Census> censuses=null;
	
	public CensusEncoder(List<Census> censuses)
	{
		this.censuses=censuses;
	}
	
	public String toClient(Census value) 
	{
		return String.valueOf(value.getCensusName());
	}

	public Census toValue(String clientValue) 
	{
		
		for(Census temp:censuses)
		{
			if(temp.getCensusName().equals(clientValue))
			{
				return temp;
			}
		}
		return null;
	}

}
