package com.pfc.ballots.util;
/**
 * Generates an UUID 
 *
 * @author Mario Temprano Martin
 * @version 1.0 FEB-2014
 *
 */
public class UUID {

	public static String generate()
	{
		return java.util.UUID.randomUUID().toString();
	}
}
