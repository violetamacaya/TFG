package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.ProfileCensedIn;
/**
 * Dao Interface to retrieves ProfileCensedIn entity
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUN-2014
 *
 */

public interface ProfileCensedInDao {

	public void store(ProfileCensedIn censedIn);
	public ProfileCensedIn getProfileCensedIn(String idProfile);
	public void addIdCensus(String idProfile,String idCensus);
	public void addIdCensus(List<String> idProfile,String idCensus);
	public void removeIdCensus(String idProfile,String idCensus);
	public void removeIdCensus(List<String> idProfile,String idCensus);
	public void addAndRemoveIds(String idProfile,List<String>added,List<String> removed);
	public void update(ProfileCensedIn updated);
	public void delete(String idProfile);
}
