package com.pfc.ballots.util;

public class UUID {

	public static String generate()
	{
		return java.util.UUID.randomUUID().toString();
	}
}
