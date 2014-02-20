package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.Profile;

public interface UserDao {

	public void store(Profile profile);
	public void store(List<Profile> profiles);
	public List<Profile> RetrieveAllProfiles();
	public List<Profile> RetrieveAllProfilesSortLastLog();
	public Profile getProfileByEmail(String Email);
	public void UpdateByEmail(String Email,Profile updatedProfile);
	public void UpdateByEmail(Profile updatedProfile);
	public boolean isProfileRegistred(String Email);
}
