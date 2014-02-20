package com.pfc.ballots.pages.admin;

import java.util.List;

import org.apache.tapestry5.annotations.Property;

import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.LogDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.entities.DataLog;
import com.pfc.ballots.entities.Profile;

public class LogList {

	FactoryDao DB4O =FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	LogDao dao =DB4O.getLogDao();
	
	
	@Property
	private DataLog log;
	
	public List<DataLog> getLogs()
	{
		return dao.retrieve();
	}
}
