package com.pfc.ballots.util;

import java.util.List;

import com.pfc.ballots.entities.Profile;

public interface UserDao {

	public void store(Profile profile);
	public void store(List<Profile> profiles);
	public List<Profile> RetrieveAllProfiles();
	public Profile getProfileByEmail(String Email);
	public boolean isProfileRegistred(String Email);
}
