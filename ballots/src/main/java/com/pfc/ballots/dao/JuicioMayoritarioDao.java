package com.pfc.ballots.dao;

import com.pfc.ballots.entities.JuicioMayoritarioText;
import com.pfc.ballots.entities.ballotdata.JuicioMayoritario;

/**
 * Dao Interface to retrieves JuicioMayoritario entity
 * @author Violeta Macaya Sánchez
 * @version 2.0 DIC-2014
 *
 */
public interface JuicioMayoritarioDao {
	
	public JuicioMayoritarioText getJuicioMayoritarioText();
	public void deleteJuicioMayoritarioText();
	public void updateJuicioMayoritarioText(JuicioMayoritarioText text);
	
	public void deleteByBallotId(String ballotId);
	public void deleteById(String id);
	public void deleteAll();
	
	public void store(JuicioMayoritario juicioMayoritario);
	public JuicioMayoritario getByBallotId(String idBallot);
	public void update(JuicioMayoritario updated);
	
}
