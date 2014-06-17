package com.pfc.ballots.dao;

import com.pfc.ballots.entities.EmailAccount;

public interface EmailAccountDao {

	public EmailAccount getAccount();
	public void setEmailAccount(EmailAccount account);
	public void deleteEmailAccount();
	public void updateEmailAccount(EmailAccount newAccount);
}
