package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.EditLog;

/**
 * Dao Interface to retrieves EditLog entity
 * 
 * @author Violeta Macaya Sánchez
 * @version 1.0 DIC-2015
 *
 */

public interface EditLogDao {

	public void store(EditLog editLog);
	public List<EditLog> retrieve(String id);
	
}
