package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.DodgsonText;
import com.pfc.ballots.entities.ballotdata.Dodgson;

/**
 * Dao Interface to retrieves Dodgson entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface DodgsonDao {
	
	public DodgsonText getDodgsonText();
	public void deleteDodgsonText();
	public void updateDodgsonText(DodgsonText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public List<Dodgson> retrieveAll();
	public void update(Dodgson updated);
	public void store(Dodgson dodgson);
	
	public Dodgson getByBallotId(String idBallot);
	public Dodgson getById(String id);
	
	
}
