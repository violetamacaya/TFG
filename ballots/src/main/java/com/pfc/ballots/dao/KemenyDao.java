package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.KemenyText;
import com.pfc.ballots.entities.ballotdata.Kemeny;

/**
 * Dao Interface to retrieves kemeny entity
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUL-2014
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface KemenyDao {
	public void store(Kemeny kemeny);
	
	public Kemeny getByBallotId(String idBallot);
	public Kemeny getById(String id);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	public List<Kemeny> retrieveAll();
	public void update(Kemeny updated);
	
	public KemenyText getKemenyText();
	public void deleteKemenyText();
	public void updateKemenyText(KemenyText text);
	
}
