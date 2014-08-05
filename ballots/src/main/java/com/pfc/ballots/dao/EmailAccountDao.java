package com.pfc.ballots.dao;

import com.pfc.ballots.entities.EmailAccount;
/**
 * Dao Interface to retrieves EmailAccount entity
 * 
 * @author Mario Temprano Martin
 * @version 1.0 JUN-2014
 *
 */
public interface EmailAccountDao {

	public EmailAccount getAccount();
	public void setEmailAccount(EmailAccount account);
	public void deleteEmailAccount();
	public void updateEmailAccount(EmailAccount newAccount);
}
