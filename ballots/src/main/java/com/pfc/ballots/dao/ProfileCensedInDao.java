package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.ProfileCensedIn;

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
