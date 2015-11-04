package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.DataLog;

/**
 * Dao Interface to retrieves DataLog entity
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 *
 */

public interface LogDao {

	public void store(DataLog datalog);
	public List<DataLog> retrieve();
	public List<DataLog> retrieve(int last);
	public List<DataLog> retrieve(String company);
	public void DeleteAll();
	
}
