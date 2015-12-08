package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.SmallText;
import com.pfc.ballots.entities.ballotdata.Small;

/**
 * Dao Interface to retrieves Small entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface SmallDao {
	
	public SmallText getSmallText();
	public void deleteSmallText();
	public void updateSmallText(SmallText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public List<Small> retrieveAll();
	public void update(Small updated);
	public void store(Small small);
	
	public Small getByBallotId(String idBallot);
	public Small getById(String id);
	
	
}
