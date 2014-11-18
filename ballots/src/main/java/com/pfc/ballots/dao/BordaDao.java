package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.AboutText;
import com.pfc.ballots.entities.BordaText;
import com.pfc.ballots.entities.ballotdata.Borda;
import com.pfc.ballots.pages.Methods.BordaMethod;

public interface BordaDao {
	public void store(Borda borda);
	
	public Borda getByBallotId(String idBallot);
	public Borda getById(String id);

	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	public List<Borda> retrieveAll();
	public void update(Borda updated);
	
	
	public BordaText getBordaText();
	public void deleteBordaText();
	public void updateBordaText(BordaText text);
}
