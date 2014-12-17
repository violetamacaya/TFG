package com.pfc.ballots.dao;


import com.pfc.ballots.dao.DB4OFactoryDao;
import com.pfc.ballots.dao.UserDao;

/**
 * FactoryDao Creates an abstract factory for databases
 * 
 * @version 2.0, FEB-2014
 * @author Mario Temprano Martin
 * @version 3.0 NOV-2014
 * @author Violeta Macaya Sánchez
 */
public abstract class FactoryDao {

	
	public static final int DB4O_FACTORY = 1;
		
	public abstract UserDao getUsuarioDao();
	public abstract UserDao getUsuarioDao(String DBName);
	
	public abstract UserLogedDao getUserLogedDao();
	public abstract UserLogedDao getUserLogedDao(String DBName);
	
	public abstract LogDao getLogDao();//in DB4O has its own DB
	
	public abstract ProfileCensedInDao getProfileCensedInDao();
	public abstract ProfileCensedInDao getProfileCensedInDao(String DBName);
	
	public abstract CensusDao getCensusDao();
	public abstract CensusDao getCensusDao(String DBName);
	
	public abstract CompanyDao getCompanyDao();//only can be in main DB in DB4O
	
	public abstract EmailAccountDao getEmailAccountDao();
	public abstract EmailAccountDao getEmailAccountDao(String DBName);
	
	public abstract BallotDao getBallotDao();
	public abstract BallotDao getBallotDao(String DBName);
	
	public abstract VoteDao getVoteDao();
	public abstract VoteDao getVoteDao(String DBName);
	
	////BALLOT DATA
	public abstract RelativeMajorityDao getRelativeMajorityDao();
	public abstract RelativeMajorityDao getRelativeMajorityDao(String DBName);
	
	public abstract KemenyDao getKemenyDao();
	public abstract KemenyDao getKemenyDao(String DBName);
	
	public abstract BordaDao getBordaDao();
	public abstract BordaDao getBordaDao(String DBName);
	
	public abstract RangeVotingDao getRangeVotingDao();
	public abstract RangeVotingDao getRangeVotingDao(String DBName);
	
	public abstract AboutTextDao getAboutTextDao();
	
	
	public static FactoryDao getFactory(int claveFactory) {
		
		switch (claveFactory) {
	
			case DB4O_FACTORY:
			
				return new DB4OFactoryDao();
				
			//any new kind of db would go here, for example
			//case SQL_FACTORY (would be number 2 up there)

			default:
			
				throw new IllegalArgumentException();
		}
	}
	public abstract BordaDao getBordaTextDao();
	public abstract KemenyDao getKemenyTextDao();
	public abstract MajoryDao getMajoryTextDao();
	public abstract ApprovalVotingDao getApprovalVotingTextDao();
	public abstract RangeVotingDao getRangeVotingTextDao();
	
}
