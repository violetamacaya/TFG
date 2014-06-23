package com.pfc.ballots.util;

public class PasswordGenerator {
	public static String NUMBERS = "0123456789";
	 
	public static String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
 
	public static String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
 
 
	public static String getPinNumber() {
		return getPassword(NUMBERS, 4);
	}
 
	public static String getPassword() {
		return getPassword(10);
	}
 
	public static String getPassword(int length) {
		return getPassword(NUMBERS + UPPERCASE + LOWERCASE, length);
	}
 
	public static String getPassword(String key, int length) {
		String pswd = "";
 
		for (int i = 0; i < length; i++) {
			pswd+=(key.charAt((int)(Math.random() * key.length())));
		}
 
		return pswd;
	}

}
