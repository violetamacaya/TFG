package com.pfc.ballots.dao;

import java.util.List;

import com.pfc.ballots.entities.UserLoged;

/**
 * Dao Interface to retrieves UserLoged entity
 * 
 * @author Mario Temprano Martin
 * @version 2.0 MAY-2014
 *
 */

public interface UserLogedDao {
	
	public void store(UserLoged userLoged);
	public UserLoged getUserLoged(String idSession);
	public void updateUserLoged(UserLoged userLoged);
	public List<UserLoged> retrieveAll();
	public void clearSessions(long timeOfSession);
	
	public void delete(String idSession);
	public void deleteByEmail(String email);
	public void deleteAll();
	public boolean isLogedIn(String idSession);

	
}
