package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.Census;
/**
 * Dao Interface to retrieves Census entity
 * 
 * @author Mario Temprano Martin
 * @version 1.0 MAY-2014
 *
 */
public interface CensusDao {

	
	public void store(Census census);
	public List<Census> retrieveAll();
	public Census getById(String id);
	public List<Census> getByOwnerId(String idOwner);
	public void update(Census census);
	public void deleteById(String id);
	public void deleteAllCensusOfOwner(String idOwner);
	public void changeEmailOfCensus(List<Census> Censuses,String email);
	public boolean isNameInUse(String name,String idOwner);
	public void removeUserCountedOfCensus(List<String> idCensus,String idProfile);
}
