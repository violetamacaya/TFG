package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.UserLoged;

public interface UserLogedDao {
	
	public void store(UserLoged userLoged);
	public List<UserLoged> retrieveAll();
	public boolean isLogedIn(String email);

}
