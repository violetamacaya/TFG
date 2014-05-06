package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.Profile;

public interface UserDao {

	public void store(Profile profile);
	public void store(List<Profile> profiles);
	public List<Profile> RetrieveAllProfiles();
	public List<Profile> RetrieveAllProfilesSortLastLog();
	public List<Profile> getByExample(Profile example);
	public Profile getProfileByEmail(String Email);
	public Profile getProfileById(String Id);
	public String  getEmailById(String Id);
	
	public void UpdateByEmail(String Email,Profile updatedProfile);
	public void UpdateByEmail(Profile updatedProfile);
	public void UpdateById(Profile updatedProfile);
	public void deleteByEmail(String Email);
	public boolean isProfileRegistred(String Email);
}
