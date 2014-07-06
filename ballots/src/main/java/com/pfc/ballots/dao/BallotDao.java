package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.Ballot;

public interface BallotDao {

	public void store(Ballot ballot);
	
	public List<Ballot> retrieveAll();
	public Ballot getById(String id);
	
	public List<Ballot> getById(List<String> ids);
	public List<Ballot> getByOwnerId(String idOwner);
	
	public boolean isNameInUse(String name);
	
	public List<Ballot> getByIdCensus(String idCensus);
}
