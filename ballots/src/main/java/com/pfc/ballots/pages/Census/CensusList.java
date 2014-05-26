package com.pfc.ballots.pages.Census;

import java.util.List;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import com.pfc.ballots.dao.CensusDao;
import com.pfc.ballots.dao.FactoryDao;
import com.pfc.ballots.dao.UserDao;
import com.pfc.ballots.data.DataSession;
import com.pfc.ballots.entities.Census;
import com.pfc.ballots.pages.Index;
import com.pfc.ballots.pages.SessionExpired;
import com.pfc.ballots.pages.UnauthorizedAttempt;

public class CensusList {

	
	FactoryDao DB4O=FactoryDao.getFactory(FactoryDao.DB4O_FACTORY);
	UserDao userDao=null;
	CensusDao censusDao=null;
	
	
	@InjectPage
	private UsersCounted usersCounted;
	
	@Property
	private Census census;
	

	@SessionState
	private DataSession datasession;
	public CensusList()
	{
		userDao=DB4O.getUsuarioDao(datasession.getDBName());
		censusDao= DB4O.getCensusDao(datasession.getDBName());
		
	}
	
	public List<Census> getCensuses()
	{
		//SUSTITUIR POR CENSUS  DE EL USUARIO
		return censusDao.retrieveAll();
	}
	public void onActionFromRemoveBut(String idCensus)
	{
		censusDao.deleteById(idCensus);
	}
	public String getEmail()
	{
		return userDao.getEmailById(census.getIdOwner());
	}
	public Object onActionFromDetailsBut(String idCensus)
	{
		usersCounted.setup(idCensus);
		return usersCounted;
	}

	  ////////////////////////////////////////////////////////////////////////////////////
	 /////////////////////////////////// ON ACTIVATE //////////////////////////////////// 
	////////////////////////////////////////////////////////////////////////////////////
	
	public Object onActivate()
	{
		switch(datasession.sessionState())
		{
			case 0:
				System.out.println("LOGEADO");
				if(datasession.isAdmin() || datasession.isMaker())
				{
					return null;
				}
				return UnauthorizedAttempt.class;
			case 1:
				System.out.println("NO LOGEADO");
				return Index.class;
			case 2:
				System.out.println("SESION EXPIRADA");
				return SessionExpired.class;
			default:
				return Index.class;
		}
		
	}

}

