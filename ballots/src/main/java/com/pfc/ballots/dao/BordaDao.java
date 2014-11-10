package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.AboutText;
import com.pfc.ballots.entities.ballotdata.Borda;

public interface BordaDao {
	public void store(Borda borda);
	
	public Borda getByBallotId(String idBallot);
	public Borda getById(String id);
	public Borda getBordaText();
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	public List<Borda> retrieveAll();
	public void update(Borda updated);
}
