package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.DataLog;

public interface LogDao {

	public void store(DataLog datalog);
	public List<DataLog> retrieve();
	public List<DataLog> retrieve(int last);
	public List<DataLog> retrieve(String company);
	public void DeleteAll();
	
}
