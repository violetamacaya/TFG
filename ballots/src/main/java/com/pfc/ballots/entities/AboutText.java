package com.pfc.ballots.entities;
/**
 * 
 * AboutText entity that contains the information about
 * the page "About us"
 * 
 * @author Mario Temprano Martín
 * @version 1 JUN-2014
 *
 */
public class AboutText {

	
	
	private String head;
	private String text;
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}

}
