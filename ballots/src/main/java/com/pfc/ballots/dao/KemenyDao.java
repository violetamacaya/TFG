package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.ballotdata.Kemeny;


public interface KemenyDao {
public void store(Kemeny kemeny);
	
	public Kemeny getByBallotId(String idBallot);
	public Kemeny getById(String id);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	public List<Kemeny> retrieveAll();
	public void update(Kemeny updated);
}
