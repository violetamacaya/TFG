package com.pfc.ballots.entities;
/**
 * 
 * Vote entity that contains if a user has voted in a ballot
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUL-2014
 *
 */
public class Vote {

	private String idBallot;
	private String idUser;
	private boolean counted;
	
	public Vote()
	{
		
	}
	public Vote(String idBallot,String idUser)
	{
		setIdBallot(idBallot);
		setIdUser(idUser);
		setCounted(false);
	}
	
	public Vote(String idBallot,String idUser,boolean counted)
	{
		setIdBallot(idBallot);
		setIdUser(idUser);
		setCounted(counted);
	}
	
	public String getIdBallot() {
		return idBallot;
	}
	public void setIdBallot(String idBallot) {
		this.idBallot = idBallot;
	}
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public boolean isCounted() {
		return counted;
	}
	public void setCounted(boolean counted) {
		this.counted = counted;
	}
	
}
