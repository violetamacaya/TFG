package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.Ballot;

public interface BallotDao {

	public void store(Ballot ballot);
	
	public List<Ballot> retrieveAll();
	public Ballot getById(String id);
	

	
	public List<Ballot> getById(List<String> ids);
	public List<Ballot> getByOwnerId(String idOwner);
	
	//Cuando se pasen las votaciones que tiene un usuario devolvera las que not has sido contavilizadas
	public List<Ballot> getEndedNotCountedById(List<String> ids);
	public boolean isNameInUse(String name);
	public boolean isEnded(String idBallot);
	
	public void deleteBallotById(String id);
	public void updateBallot(Ballot UpdatedBallot);
	
	public List<Ballot> getByIdCensus(String idCensus);
}
