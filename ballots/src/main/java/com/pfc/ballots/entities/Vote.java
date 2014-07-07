package com.pfc.ballots.entities;

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
